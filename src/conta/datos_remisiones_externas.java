/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * datos_usuarios.java
 *
 * Created on 21/01/2010, 10:56:30 PM
 */

package conta;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public class datos_remisiones_externas extends javax.swing.JDialog {
    Connection connj;
    private Properties conf;
    ResultSet rs0=null,rs1=null,rs2=null;
    DefaultTableModel modelot1=null;
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat fijo1decimales=new DecimalFormat("######0.0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    SimpleDateFormat fechasin = new SimpleDateFormat("yyyyMMdd");
    String usuariorem="";
    String valor_privilegio="1";


    /** Creates new form datos_usuarios */
    public datos_remisiones_externas(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        conf=conexion.archivoInicial();
        fecha.setDate(new Date());
        hora.setText(horacorta.format(new Date()));
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        consultamodifica(clavemodifica);
        usuariorem=conf.getProperty("UserID");
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

         modelot1.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla

                    if (c == 0) {
                        if(model.getValueAt(f, c) != null){
                            Double cant=(Double) model.getValueAt(f, c);
                            if(cant>0){
                                model.setValueAt( cant * (Double) model.getValueAt(f, 3), f, 4);
                            }else{
                                model.setValueAt(0.0, f, 4);
                            }
                        }else{
                            model.setValueAt(0.0, f, 4);
                        }
                        sumatoria();
                    }
                    if (c == 3) {
                        if(model.getValueAt(f, c) != null){
                            Double preciounirem=(Double) model.getValueAt(f, c);
                            if(preciounirem>0.0){
                                model.setValueAt((Double) model.getValueAt(f, c) * (Double) model.getValueAt(f, 0), f, 4);

                            }else{
                                model.setValueAt(0.0, f, 4);
                            }
                        }else{
                            model.setValueAt(0.0, f, 4);
                        }
                        sumatoria();
                    }
                    
                    
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "NO SE PUEDE CALCULAR EL IMPORTE"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
          }
        });

    }
    public void salir(){
        if(connj!=null){
            connj=null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }
    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            btnaceptar.setEnabled(false);
            remision.setEditable(false);
            remision.setFocusable(false);
            rs0=null;
            try{
                String senSQL="SELECT remisiones_externas.*,remisiones_externas_detalle.* FROM remisiones_externas INNER JOIN remisiones_externas_detalle ON remisiones_externas.remision=remisiones_externas_detalle.remision WHERE (remisiones_externas.remision='"+clavemodifica+"' AND remisiones_externas.estatus<>'Can');";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    remision.setText(rs0.getString("remision"));
                    Date fechahora=rs0.getTimestamp("fechahora");
                    fecha.setDate(fechahora);
                    hora.setText(horacorta.format(fechahora));
                    clavecliente.setText(rs0.getString("id_clientes"));
                    nombrecliente.setText(rs0.getString("nombre_cliente"));
                    ciudad.setText(rs0.getString("municipio_estado"));
                    direccion.setText(rs0.getString("direccion"));
                    supedido.setText(rs0.getString("ordencompra"));
                    notas.setText(rs0.getString("observaciones"));

                    Object datos[]={rs0.getDouble("cantidad"),rs0.getString("um"),rs0.getString("descripcion"),rs0.getDouble("preciounitario"),rs0.getDouble("subtotal")};
                    modelot1.addRow(datos);

                }
                if(rs0!=null) {     rs0.close();   }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            sumatoria();
        }
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(200);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(100);

        Tabladatos.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer());
        
    }
    public void sumatoria(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{

            //vuelve a realizar las sumas
            Double sumacantidad=0.0;
            Double sumaimp=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                sumacantidad+=(Double) Tabladatos.getValueAt(i, 0);
                sumaimp+=(Double) Tabladatos.getValueAt(i, 4);
            }
            txttotalcant.setText(estandarentero.format(sumacantidad));
            txttotalimp.setText(moneda2decimales.format(sumaimp));
            
        }

    }

    public class DoubleRenderer extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos

            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                String textpunto = ""+value;

                if(column==3){
                    text = fijo5decimales.format( Double.parseDouble(""+value) );
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    int dotPos = textpunto.lastIndexOf(".")+1;
                    String partedecimal = textpunto.substring(dotPos);
                    if(partedecimal.length() > 5){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                }
                if(column==4){
                    text = fijo2decimales.format( Double.parseDouble(""+value) );
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    int dotPos2 = textpunto.lastIndexOf(".")+1;
                    String partedecimal2 = textpunto.substring(dotPos2);
                    if(partedecimal2.length() > 2){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                }
                if(column==0){
                    text = fijo1decimales.format( Double.parseDouble(""+value) );
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    int dotPos2 = textpunto.lastIndexOf(".")+1;
                    String partedecimal2 = textpunto.substring(dotPos2);
                    if(partedecimal2.length() > 1){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                }
                if(column==1){
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                }

                rend.setText( text );
                return rend;
            }

    }
    public void imprimirremision(){

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        JasperPrint jasperPrint = null;
        try {
            //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio() + "/logoempresa.png");
            pars.put("folio", remision.getText());
            pars.put("logoempresa", new FileInputStream(fichero));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("senSQL", "");//SQL dinamico
            pars.put("version", resourceMap.getString("Application.title"));
            JasperReport masterReport = null;
            try {

                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_remision_externas.jasper"));
            } catch (JRException e) {
                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            jasperPrint = JasperFillManager.fillReport(masterReport, pars, connj);
            JasperPrintManager.printReport( jasperPrint, true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        btnremove = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        lbiva = new javax.swing.JLabel();
        txttotalimp = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        supedido = new javax.swing.JTextField();
        hora = new javax.swing.JTextField();
        nombrecliente = new javax.swing.JTextField();
        clavecliente = new javax.swing.JTextField();
        remision = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnclientes = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        ciudad = new javax.swing.JTextField();
        direccion = new javax.swing.JTextField();
        txttotalcant = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        notas = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_remisiones_externas.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        btnaceptar.setIcon(resourceMap.getIcon("btnaceptar.icon")); // NOI18N
        btnaceptar.setText(resourceMap.getString("btnaceptar.text")); // NOI18N
        btnaceptar.setName("btnaceptar"); // NOI18N
        btnaceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaceptarActionPerformed(evt);
            }
        });

        btncancelar.setIcon(resourceMap.getIcon("btncancelar.icon")); // NOI18N
        btncancelar.setText(resourceMap.getString("btncancelar.text")); // NOI18N
        btncancelar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btncancelar.setName("btncancelar"); // NOI18N
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(182, Short.MAX_VALUE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(181, 181, 181))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel11.setFont(resourceMap.getFont("jLabel11.font")); // NOI18N
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cantidad", "UM", "Concepto", "Precio Unitario", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladatos.setToolTipText(resourceMap.getString("Tabladatos.toolTipText")); // NOI18N
        Tabladatos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos.setColumnSelectionAllowed(true);
        Tabladatos.setName("Tabladatos"); // NOI18N
        Tabladatos.setRowHeight(22);
        Tabladatos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Tabladatos.getTableHeader().setReorderingAllowed(false);
        Tabladatos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TabladatosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        btnremove.setIcon(resourceMap.getIcon("btnremove.icon")); // NOI18N
        btnremove.setText(resourceMap.getString("btnremove.text")); // NOI18N
        btnremove.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnremove.setName("btnremove"); // NOI18N
        btnremove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoveActionPerformed(evt);
            }
        });

        btnadd.setIcon(resourceMap.getIcon("btnadd.icon")); // NOI18N
        btnadd.setText(resourceMap.getString("btnadd.text")); // NOI18N
        btnadd.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnadd.setName("btnadd"); // NOI18N
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        lbiva.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbiva.setText(resourceMap.getString("lbiva.text")); // NOI18N
        lbiva.setName("lbiva"); // NOI18N

        txttotalimp.setBackground(resourceMap.getColor("txttotalimp.background")); // NOI18N
        txttotalimp.setEditable(false);
        txttotalimp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txttotalimp.setText(resourceMap.getString("txttotalimp.text")); // NOI18N
        txttotalimp.setFocusable(false);
        txttotalimp.setName("txttotalimp"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        supedido.setText(resourceMap.getString("supedido.text")); // NOI18N
        supedido.setName("supedido"); // NOI18N

        hora.setEditable(false);
        hora.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        hora.setText(resourceMap.getString("hora.text")); // NOI18N
        hora.setName("hora"); // NOI18N

        nombrecliente.setText(resourceMap.getString("nombrecliente.text")); // NOI18N
        nombrecliente.setName("nombrecliente"); // NOI18N

        clavecliente.setEditable(false);
        clavecliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavecliente.setText(resourceMap.getString("clavecliente.text")); // NOI18N
        clavecliente.setFocusable(false);
        clavecliente.setName("clavecliente"); // NOI18N

        remision.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        remision.setName("remision"); // NOI18N
        remision.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remisionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                remisionFocusLost(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        fecha.setName("fecha"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        btnclientes.setIcon(resourceMap.getIcon("btnclientes.icon")); // NOI18N
        btnclientes.setText(resourceMap.getString("btnclientes.text")); // NOI18N
        btnclientes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnclientes.setName("btnclientes"); // NOI18N
        btnclientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnclientesActionPerformed(evt);
            }
        });

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        ciudad.setText(resourceMap.getString("ciudad.text")); // NOI18N
        ciudad.setName("ciudad"); // NOI18N

        direccion.setText(resourceMap.getString("direccion.text")); // NOI18N
        direccion.setName("direccion"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remision, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(clavecliente, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(supedido, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hora, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(remision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(clavecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(supedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txttotalcant.setBackground(resourceMap.getColor("txttotalcant.background")); // NOI18N
        txttotalcant.setEditable(false);
        txttotalcant.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txttotalcant.setText(resourceMap.getString("txttotalcant.text")); // NOI18N
        txttotalcant.setName("txttotalcant"); // NOI18N

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        notas.setColumns(20);
        notas.setFont(resourceMap.getFont("notas.font")); // NOI18N
        notas.setLineWrap(true);
        notas.setRows(3);
        notas.setName("notas"); // NOI18N
        jScrollPane2.setViewportView(notas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnremove, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txttotalcant, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lbiva, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txttotalimp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnremove, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txttotalcant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbiva)
                            .addComponent(txttotalimp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
      int filas=Tabladatos.getRowCount();
      int columnas=Tabladatos.getColumnCount();
      if(remision.getText().equals("")||hora.getText().equals("")||fecha.getDate()==null || filas<=0){
            String err="VERIFICA HAY CAMPOS VACIOS";
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
      }else{
          if (Tabladatos.getCellEditor() != null) {//finaliza el editor
                Tabladatos.getCellEditor().stopCellEditing();
          }
          sumatoria();
          int camposvacios=0;

            for(int i=0;i<=(filas-1);i=i+1){
                for(int j=0;j<=(columnas-1);j=j+1){
                    if(modelot1.getValueAt(i, j) == null || modelot1.getValueAt(i, j).equals("")){
                        camposvacios=1;
                    }
                    if(j==0){
                        Double cantidad=(Double) modelot1.getValueAt(i, j);
                        if(cantidad<=0.0)
                            camposvacios=1;
                    }
                }
            }/**fin de revisar los campos vacios*/
          if(camposvacios==1){
                JOptionPane.showMessageDialog(this, "LA TABLA DE DETALLE TIENE CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
          }else{
              if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                  String senSQL="INSERT INTO remisiones_externas(remision, estatus, fechahora, fechareal, ordencompra, id_clientes, nombre_cliente, direccion, municipio_estado, usuario,observaciones) VALUES ('"+remision.getText()+"', 'Act', '"+fechainsertar.format(fecha.getDate())+" "+hora.getText()+"', '"+fechainsertarhora.format(new Date())+"', '"+supedido.getText()+"', '"+clavecliente.getText()+"', '"+nombrecliente.getText()+"', '"+direccion.getText()+"', '"+ciudad.getText()+"', '"+usuariorem+"', '"+notas.getText()+"');";
                  conexion.modifica_p(senSQL,connj,valor_privilegio);

                  for(int i=0;i<=(filas-1);i=i+1){
                      senSQL="INSERT INTO remisiones_externas_detalle(remision, estatus, cantidad, um, descripcion, preciounitario, subtotal) VALUES ('"+remision.getText()+"', 'Act', '"+modelot1.getValueAt(i, 0)+"', '"+modelot1.getValueAt(i, 1)+"', '"+modelot1.getValueAt(i, 2)+"', '"+modelot1.getValueAt(i, 3)+"', '"+modelot1.getValueAt(i, 4)+"');";
                      conexion.modificamov_p(senSQL,connj,valor_privilegio);
                  }
                  imprimirremision();
              }
          }
  
          salir();

        }
}//GEN-LAST:event_btnaceptarActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        salir();
}//GEN-LAST:event_btncancelarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void btnclientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnclientesActionPerformed
        // TODO add your handling code here:
        busca_clientes busca_clientes = new busca_clientes(null,true,connj,"");
        busca_clientes.setLocationRelativeTo(this);
        busca_clientes.setVisible(true);
        clavecliente.setText(busca_clientes.getText());//obtiene el valor seleccionado
        busca_clientes=null;
        if(clavecliente.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM clientes WHERE id_clientes='"+clavecliente.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    nombrecliente.setText(rs0.getString("nombre"));
                    direccion.setText(rs0.getString("calle")+" "+rs0.getString("numeroext")+" "+rs0.getString("referencia")+", COL. "+rs0.getString("colonia"));
                    ciudad.setText(rs0.getString("municipio")+", "+rs0.getString("estado"));
                }
                if(rs0!=null) { rs0.close();   }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
}//GEN-LAST:event_btnclientesActionPerformed

    private void btnremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoveActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO DE ELIMINAR LA FILA  "+(filano+1)+" ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                modelot1.removeRow(filano);
                sumatoria();
            }else{

            }
        }
}//GEN-LAST:event_btnremoveActionPerformed

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        if(filas<4){
            if(remision.getText().equals("")||fecha.getDate()==null||nombrecliente.getText().equals("")){
                JOptionPane.showMessageDialog(this, "HAY CAMPOS VACIOS LLENA LOS PRIMEROS CAMPOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                //deshabilita los botones para que no realizen modificaciones
                btnclientes.setEnabled(false);
                Object datos[]={0.0,"","",0.0,0.0};
                modelot1.addRow(datos);
            }
        }else{
            JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
}//GEN-LAST:event_btnaddActionPerformed

    private void TabladatosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }
    }//GEN-LAST:event_TabladatosKeyPressed

    private void remisionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remisionFocusGained
        // TODO add your handling code here:
        remision.selectAll();
    }//GEN-LAST:event_remisionFocusGained

    private void remisionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remisionFocusLost
        // TODO add your handling code here:
        if(remision.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM remisiones_externas WHERE remision='"+remision.getText()+"'";

                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    JOptionPane.showMessageDialog(this,"EL NUMERO DE REMISION YA EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    remision.setText("");
                    remision.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }//GEN-LAST:event_remisionFocusLost

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnclientes;
    private javax.swing.JButton btnremove;
    private javax.swing.JTextField ciudad;
    private javax.swing.JTextField clavecliente;
    private javax.swing.JTextField direccion;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField hora;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbiva;
    private javax.swing.JTextField nombrecliente;
    private javax.swing.JTextArea notas;
    private javax.swing.JTextField remision;
    private javax.swing.JTextField supedido;
    private javax.swing.JTextField txttotalcant;
    private javax.swing.JTextField txttotalimp;
    // End of variables declaration//GEN-END:variables

}
