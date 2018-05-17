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
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public final class datos_entradas_productos extends javax.swing.JDialog {
    DefaultTableModel modelot1=null;
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MM-yyyy");
    Connection connj;
    ResultSet rs0=null,rs1=null;
    Double valoriva=0.0,valorieps=0.0;
    int banderabuscaart=0;
    String terminacion="M.N.";
    String clavemodificaf="";
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_entradas_productos(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        fecha.setDate(new Date());
        consultamodifica(clavemodifica);
        clavemodifica=clavemodificaf;
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
                        if(model.getValueAt(f, c) != null && clavemodificaf.equals("")){
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
                if(column==3 || column==6 || column==7){
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
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(150);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(230);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(100);

        Tabladatos.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(7).setCellRenderer(new DoubleRenderer());
    }

    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            rs0=null;
            try{
                String senSQL="SELECT entradas_productos.*,proveedores.nombre,entradas_productos_detalle.*,productos.descripcion,productos.unidad FROM (entradas_productos LEFT JOIN proveedores ON entradas_productos.id_proveedor=proveedores.id_proveedor) INNER JOIN (entradas_productos_detalle LEFT JOIN productos ON entradas_productos_detalle.clave_producto=productos.clave) ON entradas_productos.id_entrada_producto=entradas_productos_detalle.id_entrada_producto WHERE (entradas_productos.id_entrada_producto='"+clavemodifica+"' AND entradas_productos_detalle.estatus<>'Can')";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    id.setText(rs0.getString("id_entrada_producto"));
                    proveedor.setText(rs0.getString("id_proveedor"));
                    desproveedor.setText(rs0.getString("nombre"));
                    fecha.setDate(rs0.getDate("fecha"));
                    turno.setSelectedItem(rs0.getString("turno"));
                    remision.setText(rs0.getString("remision"));
                    notas.setText(rs0.getString("observaciones"));
                    Double can=rs0.getDouble("cantidad");
                    Double pu=rs0.getDouble("preciounitario");
                       

                    Object datos[]={can,rs0.getString("clave_producto"),rs0.getString("descripcion"),rs0.getString("unidad"),pu,Double.parseDouble(fijo2decimales.format(can*pu)),rs0.getString("id_ordencompra"),rs0.getString("producto_proveedor"), rs0.getDouble ("ring_crush")};
                    modelot1.addRow(datos);

                    banderabuscaart=1;
                    btnbuscarprov.setEnabled(false);
                    btnbuscarprov.setFocusable(false);
                    btnadd.setEnabled(false);
                    btnadd.setFocusable(false);
                    btnremover.setEnabled(false);
                    btnremover.setFocusable(false);
                    sumatoria();

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
            Double sumakg=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                Double valimporte=(Double) Tabladatos.getValueAt(i, 5);///obtiene el importe
                Double valkg=(Double) Tabladatos.getValueAt(i, 0);///obtiene el importe
                sumaimportes=sumaimportes+valimporte;
                sumakg=sumakg+valkg;
            }
            importe.setText(moneda2decimales.format(sumaimportes));
            kg.setText(estandarentero.format(sumakg));
        }
    }
    public void imprimir(String entrada){
        rs0=null;
        try{
            String senSQL="SELECT entradas_productos_detalle.* FROM entradas_productos_detalle WHERE (entradas_productos_detalle.id_entrada_producto='"+entrada+"' AND entradas_productos_detalle.estatus<>'Can')";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                
                org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);

                JasperPrint jasperPrint =null;
                try{
                    //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
                    Map pars = new HashMap();
                    File fichero = new File(conexion.Directorio()+"/logoempresa.png");
                    pars.put("folio",rs0.getInt("id_entrada_producto_detalle"));
                    pars.put("logoempresa",new FileInputStream(fichero));
                    pars.put("subtitulo", null);//datos
                    pars.put("fechaini", null);//fechaini.getDate()
                    pars.put("fechafin", null);//fechafin.getDate()
                    pars.put("senSQL", "");//SQL dinamico
                    pars.put("version", resourceMap.getString("Application.title"));
                    JasperReport masterReport = null;
                    try{
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_rollo.jasper"));
                     }
                     catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

                    jasperPrint = JasperFillManager.fillReport(masterReport,pars,connj);
                    //manda la venta de la impresora con las copias solicitadas
                    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                    printRequestAttributeSet.add(new Copies(1));
                    printRequestAttributeSet.add(new JobName("Papeletas", null));
                    net.sf.jasperreports.engine.export.JRPrintServiceExporter exporter;
                    exporter = new net.sf.jasperreports.engine.export.JRPrintServiceExporter();
                    exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint);
                    //impresora predeterminada
                    PrintService impresoraPredeterminada =PrintServiceLookup.lookupDefaultPrintService();
                    exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE, impresoraPredeterminada);
                    exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, impresoraPredeterminada.getAttributes());
                    exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
                    exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,false);
                    exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);
                    exporter.exportReport();
                    }
                    catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }

            }
            if(rs0!=null) {rs0.close();
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
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        btnbuscarprov = new javax.swing.JButton();
        fecha = new com.toedter.calendar.JDateChooser();
        id = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        proveedor = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        turno = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        remision = new javax.swing.JTextField();
        desproveedor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        almacen = new javax.swing.JTextField();
        btnalmacen = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        btnremover = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        notas = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        kg = new javax.swing.JFormattedTextField();
        lbiva = new javax.swing.JLabel();
        importe = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_entradas_productos.class);
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

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        btnbuscarprov.setFont(resourceMap.getFont("btnbuscarprov.font")); // NOI18N
        btnbuscarprov.setIcon(resourceMap.getIcon("btnbuscarprov.icon")); // NOI18N
        btnbuscarprov.setText(resourceMap.getString("btnbuscarprov.text")); // NOI18N
        btnbuscarprov.setName("btnbuscarprov"); // NOI18N
        btnbuscarprov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarprovActionPerformed(evt);
            }
        });

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

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        proveedor.setEditable(false);
        proveedor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        proveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        proveedor.setText(resourceMap.getString("proveedor.text")); // NOI18N
        proveedor.setFocusable(false);
        proveedor.setName("proveedor"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        turno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3" }));
        turno.setName("turno"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        remision.setText(resourceMap.getString("remision.text")); // NOI18N
        remision.setName("remision"); // NOI18N

        desproveedor.setEditable(false);
        desproveedor.setText(resourceMap.getString("desproveedor.text")); // NOI18N
        desproveedor.setEnabled(false);
        desproveedor.setName("desproveedor"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        almacen.setEditable(false);
        almacen.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        almacen.setText(resourceMap.getString("almacen.text")); // NOI18N
        almacen.setFocusable(false);
        almacen.setName("almacen"); // NOI18N

        btnalmacen.setIcon(resourceMap.getIcon("btnalmacen.icon")); // NOI18N
        btnalmacen.setText(resourceMap.getString("btnalmacen.text")); // NOI18N
        btnalmacen.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnalmacen.setName("btnalmacen"); // NOI18N
        btnalmacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnalmacenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(proveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnbuscarprov, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(almacen, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnalmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(remision, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addComponent(desproveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(desproveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnbuscarprov, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(remision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnalmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)
                                .addComponent(almacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cantidad", "Codigo (F7 Buscar)", "Descripcion", "U.M.", "Importe Unitario", "Importe", "Orden Compra", "No. Rollo", "Ring Crush"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, true, true, true
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
        Tabladatos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        Tabladatos.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title0")); // NOI18N
        Tabladatos.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title1")); // NOI18N
        Tabladatos.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title2")); // NOI18N
        Tabladatos.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title3")); // NOI18N
        Tabladatos.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title4")); // NOI18N
        Tabladatos.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title5")); // NOI18N
        Tabladatos.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title6")); // NOI18N
        Tabladatos.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title7")); // NOI18N
        Tabladatos.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title8")); // NOI18N

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
        notas.setRows(3);
        notas.setName("notas"); // NOI18N
        notas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notasFocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(notas);
        notas.getAccessibleContext().setAccessibleParent(this);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        kg.setBackground(resourceMap.getColor("kg.background")); // NOI18N
        kg.setEditable(false);
        kg.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("######0.00"))));
        kg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        kg.setText(resourceMap.getString("kg.text")); // NOI18N
        kg.setFocusable(false);
        kg.setName("kg"); // NOI18N

        lbiva.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbiva.setText(resourceMap.getString("lbiva.text")); // NOI18N
        lbiva.setName("lbiva"); // NOI18N

        importe.setBackground(resourceMap.getColor("importe.background")); // NOI18N
        importe.setEditable(false);
        importe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####0.00"))));
        importe.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        importe.setText(resourceMap.getString("importe.text")); // NOI18N
        importe.setFocusable(false);
        importe.setName("importe"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kg, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbiva, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(kg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbiva))
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        int columnas=Tabladatos.getColumnCount();
        if(fecha.getDate()==null||proveedor.getText().equals("")||remision.getText().equals("")||filas<=0){
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
                if((Double)modelot1.getValueAt(i, 4) ==0.0){
                    camposvacios=1;
                }
                if((Double)modelot1.getValueAt(i, 5) ==0.0){
                    camposvacios=1;
                }
            }/**fin de revisar los campos vacios*/


            if(camposvacios==1){
                JOptionPane.showMessageDialog(this, "LA TABLA DE DETALLE TIENE CAMPOS VACIOS\nBORRE LAS FILAS VACIAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                sumatoria();
                
                if(id.getText().equals("")){
                    String senSQL="INSERT INTO entradas_productos(estatus, fecha, id_proveedor, remision, turno, observaciones) VALUES ('Act', '"+fechainsertar.format(fecha.getDate())+"', '"+proveedor.getText()+"', '"+remision.getText()+"', '"+turno.getSelectedItem()+"', '"+notas.getText()+"');";
                    conexion.modifica_p(senSQL,connj,valor_privilegio);
                    
                    /**funcion que regresa el numero con que se guardo la orden de compra*/
                    int claveidmax=0;
                    rs0=null;
                    try{
                        senSQL="SELECT max(id_entrada_producto) as pemax FROM entradas_productos";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            claveidmax=rs0.getInt("pemax");
                            id.setText(""+claveidmax);
                        }
                        if(rs0!=null) {
                            rs0.close();
                        }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    /**funcion que registra todos los movimientos de la orden de compra*/
                    for(int i=0;i<=(filas-1);i=i+1){
                        String senSQLmov="INSERT INTO entradas_productos_detalle(id_entrada_producto, estatus, id_ordencompra, producto_proveedor, clave_producto, preciounitario, cantidad,  almacen,fecha,almacen_existe, ring_crush) VALUES ('"+claveidmax+"', 'Act', '"+modelot1.getValueAt(i, 6)+"', '"+modelot1.getValueAt(i, 7)+"', '"+modelot1.getValueAt(i, 1)+"', '"+modelot1.getValueAt(i, 4)+"', '"+modelot1.getValueAt(i, 0)+"', '"+almacen.getText()+"','"+fechainsertar.format(fecha.getDate())+"', '"+almacen.getText()+"', '"+modelot1.getValueAt(i, 8)+"' );";
                        conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                    }

                    /**funcion para mandar a imprimir*/
                    int respuesta=JOptionPane.showConfirmDialog(this,"DESEA MANDAR A IMPRIMIR ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        imprimir(id.getText());
                    }
                }else{
                    String senSQL="UPDATE entradas_productos set fecha='"+fechainsertar.format(fecha.getDate())+"', remision='"+remision.getText()+"', turno='"+turno.getSelectedItem()+"',observaciones='"+notas.getText()+"' WHERE id_entrada_producto='"+id.getText()+"'";
                    conexion.modificaupdate_p(senSQL,connj,valor_privilegio);
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
                    desproveedor.setText(rs0.getString("nombre"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
}//GEN-LAST:event_btnbuscarprovActionPerformed

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        if(proveedor.getText().equals("")){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UN PROVEEDOR", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            if(filas<15){
                Object datos[]={0.0,"","","",0.0,0.0,"",""};
                modelot1.addRow(datos);
            }else{
                JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }
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
                        busca_productos busca_productos = new busca_productos(null,true,connj,valorfiltro,proveedor.getText(),"3");
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
        imprimir(id.getText());
}//GEN-LAST:event_btnimprimirActionPerformed

    private void btnalmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnalmacenActionPerformed
        // TODO add your handling code here:
        busca_no_almacen_rollos busca_no_almacen_rollos = new busca_no_almacen_rollos(null,true,connj,"");
        busca_no_almacen_rollos.setLocationRelativeTo(this);
        busca_no_almacen_rollos.setVisible(true);
        almacen.setText(busca_no_almacen_rollos.getText());//obtiene el valor seleccionado
        busca_no_almacen_rollos=null;

    }//GEN-LAST:event_btnalmacenActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        JasperPrint jasperPrint =null;
        try{
            String datos="";
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio()+"/logoempresa.png");
            pars.put("logoempresa",new FileInputStream(fichero));
            pars.put("subtitulo", datos);
            pars.put("fechaini", null);
            pars.put("fechafin", null);
            pars.put("folio", Integer.parseInt(id.getText()));
            pars.put("senSQL", "");
            pars.put("version", resourceMap.getString("Application.title"));
            JasperReport masterReport = null;
            try{
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/entradas_papel_remision.jasper"));
             }
             catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

            jasperPrint = JasperFillManager.fillReport(masterReport,pars,connj);
            JasperPrintManager.printReport( jasperPrint, true);
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField almacen;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btnalmacen;
    private javax.swing.JButton btnbuscarprov;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnimprimir;
    private javax.swing.JButton btnremover;
    private javax.swing.JTextField desproveedor;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField id;
    private javax.swing.JFormattedTextField importe;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JFormattedTextField kg;
    private javax.swing.JLabel lbiva;
    private javax.swing.JTextArea notas;
    private javax.swing.JFormattedTextField proveedor;
    private javax.swing.JTextField remision;
    private javax.swing.JComboBox turno;
    // End of variables declaration//GEN-END:variables

}
