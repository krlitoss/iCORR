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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public class datos_ordenescompra extends javax.swing.JDialog {
    DefaultTableModel modelot1=null;
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MM-yyyy");
    Connection connj;
    ResultSet rs0=null,rs1=null;
    Double valoriva=0.0,valorieps=0.0;
    int banderabuscaart=0;
    String terminacion="M.N.";
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_ordenescompra(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        fecha.setDate(new Date());
        fechaent.setDate(new Date());
        configuracioninicial();
        consultamodifica(clavemodifica);
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
                            model.setValueAt((Double) model.getValueAt(f, c) * (Double) model.getValueAt(f, 4), f, 5);
                        }else{
                            model.setValueAt(0.0, f, 5);
                        }
                        sumatoria();
                    }
                    if (c == 4) {
                        if(model.getValueAt(f, c) != null){
                            model.setValueAt((Double) model.getValueAt(f, c) * (Double) model.getValueAt(f, 0), f, 5);
                            /**funcion que actualiza el precio del producto*/
                            String senSQLmov="UPDATE productos SET preciocompra='"+(Double) model.getValueAt(f, c)+"' WHERE clave='"+model.getValueAt(f, 1)+"';";
                            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                        }else{
                            model.setValueAt(0.0, f, 5);
                        }
                        sumatoria();
                    }

                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "NO SE PUEDE CALCULAR EL IMPORTE"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }

          }
        });

    }

    public class DoubleRenderer extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos

            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                String textpunto = ""+value;

                if((column==0 || column==5)){
                    text = fijo2decimales.format( Double.parseDouble(""+value) );
                    int dotPos = textpunto.lastIndexOf(".")+1;
                    String partedecimal = textpunto.substring(dotPos);
                    if(partedecimal.length() > 2){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                }
                if(column==4){
                    text = fijo5decimales.format( Double.parseDouble(""+value) );
                    int dotPos2 = textpunto.lastIndexOf(".")+1;
                    String partedecimal2 = textpunto.substring(dotPos2);
                    if(partedecimal2.length() > 5){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                }
                if(column==6){
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                }

                rend.setText( text );
                return rend;
            }

    }


    public void salir(){
        if(connj!=null){
            connj=null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }
    public void configuracioninicial(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM empresa WHERE id='1'";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                enviara.setText(rs0.getString("nombre")+"\n"+rs0.getString("calle")+" "+rs0.getString("numeroext")+" Col. "+rs0.getString("colonia")+"\n"+rs0.getString("municipio")+", "+rs0.getString("estado")+", "+rs0.getString("pais")+".    C.P.: "+rs0.getString("cod_postal"));
                facturara.setText(enviara.getText()+"\nR.F.C:   "+rs0.getString("rfc"));
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(150);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(230);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(100);

        Tabladatos.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
    }

    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            rs0=null;
            try{
                String senSQL="SELECT ordenescompra.id_ordencompra,ordenescompra.id_moneda, fecha, fechaentrega, salida,ordenescompra.id_proveedor,ordenescompra.id_impuestos, entregaren, facturara, comprador, salida, notas,metodoenvio, subtotal, descuento, iva, ieps, total, numeroletra,monedas.descripcion,proveedores.nombre, proveedores.rfc, proveedores.direccion,proveedores.cod_postal, proveedores.poblacion,proveedores.estado, proveedores.pais, proveedores.contacto, proveedores.telefono1,proveedores.dias,movordenescompra.partida,movordenescompra.clave,movordenescompra.cantidad,movordenescompra.preciocompra,movordenescompra.fechaitem,productos.descripcion as descripcionart,productos.unidad,empresa.nombrecomercial,empresa.direccion as dir,empresa.cod_postal as cp,empresa.poblacion as pob,empresa.estado as est, empresa.pais as pai,empresa.telefono FROM (productos RIGHT JOIN (movordenescompra RIGHT JOIN ((monedas RIGHT JOIN ordenescompra ON monedas.id_moneda=ordenescompra.id_moneda)LEFT JOIN proveedores ON ordenescompra.id_proveedor=proveedores.id_proveedor) ON movordenescompra.id_ordencompra=ordenescompra.id_ordencompra) ON productos.clave=movordenescompra.clave) LEFT JOIN empresa ON ordenescompra.id=empresa.id WHERE ordenescompra.id_ordencompra='"+clavemodifica+"' ORDER BY movordenescompra.partida";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    id.setText(rs0.getString("id_ordencompra"));
                    fecha.setDate(rs0.getDate("fecha"));
                    fechaent.setDate(rs0.getDate("fechaentrega"));
                    envio.setSelectedItem(rs0.getString("salida"));
                    proveedor.setText(rs0.getString("id_proveedor"));
                    impuestos.setText(rs0.getString("id_impuestos"));
                    impuestosdatos();
                    moneda.setText(rs0.getString("id_moneda"));
                    desmoneda.setText(rs0.getString("descripcion"));
                    enviara.setText(rs0.getString("entregaren"));
                    facturara.setText(rs0.getString("facturara"));
                    desproveedor.setText(rs0.getString("nombre")+"\n"+rs0.getString("direccion")+"\n"+rs0.getString("poblacion")+", "+rs0.getString("estado")+", "+rs0.getString("pais")+"\nC.P.: "+rs0.getString("cod_postal")+"\t\tR.F.C: "+rs0.getString("rfc"));
                    notas.setText(rs0.getString("notas"));
                    subtotal.setText(fijo2decimales.format(rs0.getDouble("subtotal")));
                    descuento.setText(fijo2decimales.format(rs0.getDouble("descuento")));
                    importe.setText(fijo2decimales.format(Double.parseDouble(subtotal.getText())-Double.parseDouble(descuento.getText())));
                    iva.setText(fijo2decimales.format(rs0.getDouble("iva")));
                    total.setText(fijo2decimales.format(rs0.getDouble("total")));
                    Double cantt=rs0.getDouble("cantidad");
                    Double pct=rs0.getDouble("preciocompra");
                    Object datos[]={cantt,rs0.getString("clave"),rs0.getString("descripcionart"),rs0.getString("unidad"),pct,(pct*cantt),fechacorta.format(rs0.getDate("fechaitem"))};
                    modelot1.addRow(datos);
                    /**deshabilita los controles no utilizados*/
                    banderabuscaart=1;
                    fecha.setEnabled(false);
                    fecha.setFocusable(false);
                    envio.setEnabled(false);
                    envio.setFocusable(false);
                    btnadd.setEnabled(false);
                    btnadd.setFocusable(false);
                    btnremover.setEnabled(false);
                    btnremover.setFocusable(false);

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void sumatoria(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{
            Double sumaimportes=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                Double valimporte=(Double) Tabladatos.getValueAt(i, 5);///obtiene el importe
                sumaimportes=sumaimportes+valimporte;
            }
            subtotal.setText(fijo2decimales.format(sumaimportes));
            importe.setText(fijo2decimales.format(sumaimportes-Double.parseDouble(descuento.getText())));
            iva.setText(fijo2decimales.format((valoriva/100)*Double.parseDouble(importe.getText())));
            total.setText(fijo2decimales.format(Double.parseDouble(importe.getText())+Double.parseDouble(iva.getText())));
        }
    }
    public void imprimir(){
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);

        JasperViewer visor=null;
        JasperPrint jasperPrint =null;
        try{
            //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio()+"/logoempresa.png");
            pars.put("folio",Integer.parseInt(id.getText()));
            pars.put("logoempresa",new FileInputStream(fichero));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("version", resourceMap.getString("Application.title"));
            JasperReport masterReport = null;
            try{
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/ordencompra.jasper"));
             }
             catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

            jasperPrint = JasperFillManager.fillReport(masterReport,pars,connj);
            visor = new JasperViewer(jasperPrint,false);
            visor.setTitle("REPORTE");
            visor.setVisible(true);
            salir();
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }

    }
    public void impuestosdatos(){
        rs1=null;
        try{
            String senSQL="SELECT * FROM impuestos WHERE id_impuestos='"+impuestos.getText()+"'";
            rs1=conexion.consulta(senSQL,connj);
            if(rs1.next()){
                valoriva=rs1.getDouble("iva");
                valorieps=rs1.getDouble("ieps");
                lbiva.setText("IVA "+fijo0decimales.format(valoriva)+"% :");
                lbieps.setText("IEPS "+fijo0decimales.format(valorieps)+"% :");
            }
            if(rs1!=null) {
                rs1.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
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
        btnimprimir = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnbuscarmon = new javax.swing.JButton();
        btnbuscarimp = new javax.swing.JButton();
        btnbuscarprov = new javax.swing.JButton();
        moneda = new javax.swing.JTextField();
        fecha = new com.toedter.calendar.JDateChooser();
        id = new javax.swing.JTextField();
        impuestos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        proveedor = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        fechaent = new com.toedter.calendar.JDateChooser();
        desmoneda = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        desproveedor = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        enviara = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        facturara = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        envio = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        btnremover = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        notas = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        subtotal = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        descuento = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        importe = new javax.swing.JFormattedTextField();
        lbiva = new javax.swing.JLabel();
        lbieps = new javax.swing.JLabel();
        total = new javax.swing.JFormattedTextField();
        iva = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_ordenescompra.class);
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
        btnaceptar.setMargin(new java.awt.Insets(2, 0, 2, 0));
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

        btnimprimir.setIcon(resourceMap.getIcon("btnimprimir.icon")); // NOI18N
        btnimprimir.setText(resourceMap.getString("btnimprimir.text")); // NOI18N
        btnimprimir.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnimprimir.setName("btnimprimir"); // NOI18N
        btnimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(196, 196, 196)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(188, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        btnbuscarmon.setFont(resourceMap.getFont("btnbuscarmon.font")); // NOI18N
        btnbuscarmon.setIcon(resourceMap.getIcon("btnbuscarmon.icon")); // NOI18N
        btnbuscarmon.setText(resourceMap.getString("btnbuscarmon.text")); // NOI18N
        btnbuscarmon.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnbuscarmon.setName("btnbuscarmon"); // NOI18N
        btnbuscarmon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarmonActionPerformed(evt);
            }
        });

        btnbuscarimp.setFont(resourceMap.getFont("btnbuscarimp.font")); // NOI18N
        btnbuscarimp.setIcon(resourceMap.getIcon("btnbuscarimp.icon")); // NOI18N
        btnbuscarimp.setText(resourceMap.getString("btnbuscarimp.text")); // NOI18N
        btnbuscarimp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnbuscarimp.setName("btnbuscarimp"); // NOI18N
        btnbuscarimp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarimpActionPerformed(evt);
            }
        });

        btnbuscarprov.setFont(resourceMap.getFont("btnbuscarprov.font")); // NOI18N
        btnbuscarprov.setIcon(resourceMap.getIcon("btnbuscarprov.icon")); // NOI18N
        btnbuscarprov.setText(resourceMap.getString("btnbuscarprov.text")); // NOI18N
        btnbuscarprov.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnbuscarprov.setName("btnbuscarprov"); // NOI18N
        btnbuscarprov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarprovActionPerformed(evt);
            }
        });

        moneda.setEditable(false);
        moneda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        moneda.setText(resourceMap.getString("moneda.text")); // NOI18N
        moneda.setFocusable(false);
        moneda.setName("moneda"); // NOI18N

        fecha.setDateFormatString(resourceMap.getString("fecha.dateFormatString")); // NOI18N
        fecha.setName("fecha"); // NOI18N
        fecha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fechaFocusGained(evt);
            }
        });

        id.setEditable(false);
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setText(resourceMap.getString("id.text")); // NOI18N
        id.setFocusable(false);
        id.setName("id"); // NOI18N

        impuestos.setEditable(false);
        impuestos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        impuestos.setText(resourceMap.getString("impuestos.text")); // NOI18N
        impuestos.setFocusable(false);
        impuestos.setName("impuestos"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        proveedor.setEditable(false);
        proveedor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        proveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        proveedor.setText(resourceMap.getString("proveedor.text")); // NOI18N
        proveedor.setFocusable(false);
        proveedor.setName("proveedor"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        fechaent.setDateFormatString(resourceMap.getString("fechaent.dateFormatString")); // NOI18N
        fechaent.setName("fechaent"); // NOI18N

        desmoneda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        desmoneda.setText(resourceMap.getString("desmoneda.text")); // NOI18N
        desmoneda.setName("desmoneda"); // NOI18N

        jTabbedPane1.setBackground(resourceMap.getColor("jTabbedPane1.background")); // NOI18N
        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        desproveedor.setColumns(20);
        desproveedor.setEditable(false);
        desproveedor.setFont(resourceMap.getFont("desproveedor.font")); // NOI18N
        desproveedor.setLineWrap(true);
        desproveedor.setRows(3);
        desproveedor.setTabSize(4);
        desproveedor.setFocusable(false);
        desproveedor.setName("desproveedor"); // NOI18N
        jScrollPane3.setViewportView(desproveedor);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jPanel3.setBackground(resourceMap.getColor("jPanel3.background")); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        enviara.setColumns(20);
        enviara.setFont(resourceMap.getFont("enviara.font")); // NOI18N
        enviara.setLineWrap(true);
        enviara.setRows(3);
        enviara.setTabSize(4);
        enviara.setFocusable(false);
        enviara.setName("enviara"); // NOI18N
        jScrollPane4.setViewportView(enviara);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jPanel4.setBackground(resourceMap.getColor("jPanel4.background")); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        facturara.setColumns(20);
        facturara.setFont(resourceMap.getFont("facturara.font")); // NOI18N
        facturara.setLineWrap(true);
        facturara.setRows(3);
        facturara.setTabSize(4);
        facturara.setFocusable(false);
        facturara.setName("facturara"); // NOI18N
        jScrollPane5.setViewportView(facturara);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        envio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Impresion" }));
        envio.setName("envio"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(envio, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fechaent, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnbuscarmon, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(impuestos, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnbuscarimp, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnbuscarprov, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(desmoneda, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnbuscarprov))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(impuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnbuscarimp))
                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fechaent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(envio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(desmoneda)))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)
                                .addComponent(btnbuscarmon)))))
                .addContainerGap())
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cantidad", "Codigo (F7 Buscar)", "Descripcion", "U.M.", "Importe Unitario", "Importe", "Fecha Entrega"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TabladatosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        btnremover.setIcon(resourceMap.getIcon("btnremover.icon")); // NOI18N
        btnremover.setText(resourceMap.getString("btnremover.text")); // NOI18N
        btnremover.setToolTipText(resourceMap.getString("btnremover.toolTipText")); // NOI18N
        btnremover.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnremover.setName("btnremover"); // NOI18N
        btnremover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoverActionPerformed(evt);
            }
        });

        btnadd.setIcon(resourceMap.getIcon("btnadd.icon")); // NOI18N
        btnadd.setText(resourceMap.getString("btnadd.text")); // NOI18N
        btnadd.setToolTipText(resourceMap.getString("btnadd.toolTipText")); // NOI18N
        btnadd.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnadd.setName("btnadd"); // NOI18N
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        notas.setColumns(20);
        notas.setFont(resourceMap.getFont("notas.font")); // NOI18N
        notas.setLineWrap(true);
        notas.setRows(4);
        notas.setName("notas"); // NOI18N
        notas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notasFocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(notas);
        notas.getAccessibleContext().setAccessibleParent(this);

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        subtotal.setBackground(resourceMap.getColor("subtotal.background")); // NOI18N
        subtotal.setEditable(false);
        subtotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("######0.00"))));
        subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        subtotal.setText(resourceMap.getString("subtotal.text")); // NOI18N
        subtotal.setFocusable(false);
        subtotal.setName("subtotal"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        descuento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####0.00"))));
        descuento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        descuento.setText(resourceMap.getString("descuento.text")); // NOI18N
        descuento.setName("descuento"); // NOI18N
        descuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                descuentoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                descuentoFocusLost(evt);
            }
        });

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        importe.setBackground(resourceMap.getColor("importe.background")); // NOI18N
        importe.setEditable(false);
        importe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####0.00"))));
        importe.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        importe.setText(resourceMap.getString("importe.text")); // NOI18N
        importe.setFocusable(false);
        importe.setName("importe"); // NOI18N

        lbiva.setText(resourceMap.getString("lbiva.text")); // NOI18N
        lbiva.setName("lbiva"); // NOI18N

        lbieps.setText(resourceMap.getString("lbieps.text")); // NOI18N
        lbieps.setName("lbieps"); // NOI18N

        total.setBackground(resourceMap.getColor("total.background")); // NOI18N
        total.setEditable(false);
        total.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####0.00"))));
        total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        total.setText(resourceMap.getString("total.text")); // NOI18N
        total.setFocusable(false);
        total.setName("total"); // NOI18N

        iva.setBackground(resourceMap.getColor("iva.background")); // NOI18N
        iva.setEditable(false);
        iva.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####0.00"))));
        iva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        iva.setText(resourceMap.getString("iva.text")); // NOI18N
        iva.setFocusable(false);
        iva.setName("iva"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbieps, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbiva, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(iva, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbiva)
                            .addComponent(iva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbieps)
                            .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        int columnas=Tabladatos.getColumnCount();
        if(fecha.getDate()==null||fechaent.getDate()==null||proveedor.getText().equals("")||impuestos.getText().equals("")||moneda.getText().equals("")||desproveedor.getText().equals("")||enviara.getText().equals("")||facturara.getText().equals("")||filas<=0){
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            if (Tabladatos.getCellEditor() != null) {//finaliza el editor
                Tabladatos.getCellEditor().stopCellEditing();
            }
            /**revisar que no existan campos vacios*/
            int camposvacios=0;
            for(int i=0;i<=(filas-1);i=i+1){
                for(int j=0;j<=(columnas-1);j=j+1){
                    if(modelot1.getValueAt(i, j) == null||modelot1.getValueAt(i, j).equals("")){
                        camposvacios=1;
                    }
                }
                /**funcion que verifica que no quede en ceros la cantidad*/
                if((Double)modelot1.getValueAt(i, 0) ==0.0){
                    camposvacios=1;
                }
                /**funcion que verifica que las fechas sean correctas*/
                String fechatem=(String)modelot1.getValueAt(i, 6);
                try{
                Date fechatem2=fechacorta.parse(fechatem);
                }catch(Exception ex){
                    camposvacios=1;
                    JOptionPane.showMessageDialog(this,"ERROR EN LAS FECHAS: "+fechatem+"  FILA: "+(i+1)+"\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }

            }/**fin de revisar los campos vacios*/

            if(camposvacios==1){
                JOptionPane.showMessageDialog(this, "LA TABLA DE DETALLE TIENE CAMPOS VACIOS\nBORRE LAS FILAS VACIAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                sumatoria();
                int dotPos = total.getText().lastIndexOf(".")+1;
                String partedecimal = total.getText().substring(dotPos);
                String parteentero=total.getText().substring(0, (dotPos-1));
                String numeroletra="("+new numerosletras().convertirLetras(Integer.parseInt(parteentero))+" "+desmoneda.getText()+" "+partedecimal+"/100 "+terminacion+")";
                numeroletra=numeroletra.toUpperCase();/**cambia la cantidad en letra a mayusculas*/
                String nombrecomprador=JOptionPane.showInputDialog(this,"ESCRIBE EL NOMBRE DEL COMPRADOR");
                String metodoenvio=JOptionPane.showInputDialog(this,"METODO DE ENVIO (TERRESTE,MARITIMO???)");
                String senSQL="";
                
                if(id.getText().equals("")){
                    senSQL="INSERT INTO ordenescompra(fecha, fechaentrega, status, id_proveedor, entregaren, facturara, comprador, id_impuestos, id_moneda, salida, notas, metodoenvio, subtotal, descuento, iva, ieps, total, numeroletra,id) VALUES('"+fechainsertar.format(fecha.getDate())+"','"+fechainsertar.format(fechaent.getDate())+"','Emitida','"+proveedor.getText()+"','"+enviara.getText()+"','"+facturara.getText()+"','"+nombrecomprador+"','"+impuestos.getText()+"','"+moneda.getText()+"','"+(String)envio.getSelectedItem()+"','"+notas.getText()+"','"+metodoenvio+"','"+importe.getText()+"','"+descuento.getText()+"','"+iva.getText()+"','0','"+total.getText()+"','"+numeroletra+"','1');";
                    conexion.modifica_p(senSQL,connj,valor_privilegio);
                    /**funcion que regresa el numero con que se guardo la orden de compra*/
                    int claveidmax=0;
                    rs0=null;
                    try{
                        senSQL="SELECT max(id_ordencompra) as ocmax FROM ordenescompra";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            claveidmax=rs0.getInt("ocmax");
                            id.setText(""+claveidmax);
                        }
                        if(rs0!=null) {
                            rs0.close();
                        }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    /**funcion que registra todos los movimientos de la orden de compra*/
                    for(int i=0;i<=(filas-1);i=i+1){
                        String senSQLmov="INSERT INTO movordenescompra(id_ordencompra, partida, clave, cantidad, preciocompra, fechaitem) VALUES('"+claveidmax+"','"+(i+1)+"','"+modelot1.getValueAt(i, 1)+"','"+fijo0decimales.format((Double)modelot1.getValueAt(i, 0))+"','"+modelot1.getValueAt(i, 4)+"','"+modelot1.getValueAt(i, 6)+"');";
                        conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                    }
                    
                }else{
                    senSQL="UPDATE ordenescompra set id_proveedor='"+proveedor.getText()+"',fechaentrega='"+fechainsertar.format(fechaent.getDate())+"',entregaren='"+enviara.getText()+"',facturara='"+facturara.getText()+"',id_impuestos='"+impuestos.getText()+"',comprador='"+nombrecomprador+"',id_moneda='"+moneda.getText()+"',notas='"+notas.getText()+"',subtotal='"+importe.getText()+"',descuento='"+descuento.getText()+"',iva='"+iva.getText()+"',total='"+total.getText()+"',numeroletra='"+numeroletra+"' WHERE id_ordencompra='"+id.getText()+"'";
                    conexion.modifica_p(senSQL,connj,valor_privilegio);
                    /**funcion que registra todos los movimientos de la orden de compra*/
                    for(int i=0;i<=(filas-1);i=i+1){
                        String senSQLmov="UPDATE movordenescompra set clave='"+modelot1.getValueAt(i, 1)+"', cantidad='"+fijo0decimales.format((Double)modelot1.getValueAt(i, 0))+"', preciocompra='"+modelot1.getValueAt(i, 4)+"', fechaitem='"+modelot1.getValueAt(i, 6)+"' WHERE (id_ordencompra='"+id.getText()+"' AND partida='"+(i+1)+"')";
                        conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                    }
                }
                /**funcion para mandar a imprimir*/
                int respuesta=JOptionPane.showConfirmDialog(this,"DESEA MANDAR A IMPRIMIR ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    imprimir();
                }else{
                    salir();
                }

                salir();

            }
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

    private void btnbuscarprovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarprovActionPerformed
        // TODO add your handling code here:
        busca_proveedores busca_proveedores = new busca_proveedores(null,true,connj,"");
        busca_proveedores.setLocationRelativeTo(this);
        busca_proveedores.setVisible(true);
        proveedor.setText(busca_proveedores.getText());//obtiene el valor seleccionado
        busca_proveedores=null;
        if(proveedor.getText().equals("0")){
            desproveedor.setText("");
        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM proveedores WHERE id_proveedor='"+proveedor.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    desproveedor.setText(rs0.getString("nombre")+"\n"+rs0.getString("calle")+" "+rs0.getString("numeroext")+" Col. "+rs0.getString("colonia")+"\n"+rs0.getString("municipio")+", "+rs0.getString("estado")+", "+rs0.getString("pais")+"\nC.P.: "+rs0.getString("cod_postal")+"\t\tR.F.C: "+rs0.getString("rfc"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
}//GEN-LAST:event_btnbuscarprovActionPerformed

    private void btnbuscarimpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarimpActionPerformed
        // TODO add your handling code here:
        busca_impuestos busca_impuestos = new busca_impuestos(null,true,connj,"");
        busca_impuestos.setLocationRelativeTo(this);
        busca_impuestos.setVisible(true);
        impuestos.setText(busca_impuestos.getText());//obtiene el valor seleccionado
        busca_impuestos=null;
        if(impuestos.getText().equals("")){
            lbiva.setText("IVA:");
            lbieps.setText("IEPS:");
        }else{
            impuestosdatos();
        }
}//GEN-LAST:event_btnbuscarimpActionPerformed

    private void btnbuscarmonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarmonActionPerformed
        // TODO add your handling code here:
        busca_monedas busca_monedas = new busca_monedas(null,true,connj,"");
        busca_monedas.setLocationRelativeTo(this);
        busca_monedas.setVisible(true);
        moneda.setText(busca_monedas.getText());//obtiene el valor seleccionado
        busca_monedas=null;
        if(moneda.getText().equals("0")){
            desmoneda.setText("-");
        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM monedas WHERE id_moneda='"+moneda.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    desmoneda.setText(rs0.getString("descripcion"));
                    terminacion=rs0.getString("terminacion");
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
}//GEN-LAST:event_btnbuscarmonActionPerformed

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        if(filas<15){
            Object datos[]={0.0,"","","",0.0,0.0,fechacorta.format(fechaent.getDate())};
            modelot1.addRow(datos);
        }else{
            JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }

}//GEN-LAST:event_btnaddActionPerformed

    private void btnremoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoverActionPerformed
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
    }//GEN-LAST:event_btnremoverActionPerformed

    private void descuentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descuentoFocusLost
        // TODO add your handling code here:
        sumatoria();
    }//GEN-LAST:event_descuentoFocusLost

    private void descuentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descuentoFocusGained
        // TODO add your handling code here:
        descuento.selectAll();
    }//GEN-LAST:event_descuentoFocusGained

    private void notasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_notasFocusGained
        // TODO add your handling code here:
        notas.selectAll();
    }//GEN-LAST:event_notasFocusGained

    private void fechaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fechaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaFocusGained

    private void TabladatosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }

        if(banderabuscaart==0){/**no consulta ni actualiza cuando es modificacion en 1*/
            if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
            {
                int filano=Tabladatos.getSelectedRow();
                if(proveedor.getText().equals("")){
                    JOptionPane.showMessageDialog(this,"TIENES QUE SELECCIONAR UN PROVEEDOR","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    if (Tabladatos.getCellEditor() != null) {
                        Tabladatos.getCellEditor().cancelCellEditing();
                    }
                    Tabladatos.changeSelection(filano, 0, false, false);
                }
                else{
                    int colno=Tabladatos.getSelectedColumn();
                    if(colno==1){
                        if (Tabladatos.getCellEditor() != null) {
                            Tabladatos.getCellEditor().stopCellEditing();
                        }
                        String valorfiltro=(String)Tabladatos.getValueAt(filano, colno);
                        busca_productos busca_productos = new busca_productos(null,true,connj,valorfiltro,proveedor.getText(),"");
                        busca_productos.setLocationRelativeTo(this);
                        busca_productos.setVisible(true);
                        Tabladatos.setValueAt(busca_productos.getText(), filano, colno);
                        valorfiltro=(String)Tabladatos.getValueAt(filano, colno);
                        busca_productos=null;

                        if(valorfiltro.equals("")){
                            Tabladatos.setValueAt("", filano, 2);
                        }else{

                            rs0=null;
                            try{
                                String senSQL="SELECT * FROM productos WHERE clave='"+valorfiltro+"'";
                                rs0=conexion.consulta(senSQL,connj);
                                if(rs0.next()){
                                    Tabladatos.setValueAt(rs0.getString("descripcion"), filano, 2);
                                    Tabladatos.setValueAt(rs0.getString("unidad"), filano, 3);
                                    Tabladatos.setValueAt(rs0.getDouble("preciocompra"), filano, 4);
                                }
                                if(rs0!=null) {
                                    rs0.close();
                                }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                        }

                        Tabladatos.changeSelection(filano, 4, false, false);
                    }
                }
            }
        }
    }//GEN-LAST:event_TabladatosKeyPressed

    private void TabladatosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TabladatosKeyReleased

    private void btnimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimirActionPerformed
        // TODO add your handling code here:
        imprimir();
}//GEN-LAST:event_btnimprimirActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btnbuscarimp;
    private javax.swing.JButton btnbuscarmon;
    private javax.swing.JButton btnbuscarprov;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnimprimir;
    private javax.swing.JButton btnremover;
    private javax.swing.JFormattedTextField descuento;
    private javax.swing.JLabel desmoneda;
    private javax.swing.JTextArea desproveedor;
    private javax.swing.JTextArea enviara;
    private javax.swing.JComboBox envio;
    private javax.swing.JTextArea facturara;
    private com.toedter.calendar.JDateChooser fecha;
    private com.toedter.calendar.JDateChooser fechaent;
    private javax.swing.JTextField id;
    private javax.swing.JFormattedTextField importe;
    private javax.swing.JTextField impuestos;
    private javax.swing.JFormattedTextField iva;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbieps;
    private javax.swing.JLabel lbiva;
    private javax.swing.JTextField moneda;
    private javax.swing.JTextArea notas;
    private javax.swing.JFormattedTextField proveedor;
    private javax.swing.JFormattedTextField subtotal;
    private javax.swing.JFormattedTextField total;
    // End of variables declaration//GEN-END:variables

}
