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

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 *
 * @author IVONNE
 */
public class datos_devoluciones extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null,rs1=null,rs2=null;
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    int cantidadremisionada=0;
    String opprincipal="";
    String clienteprincipal="";
    Double preciounitarioop=0.0;
    String ordencompraop="";
    private Properties conf;
    String usuariorem="";
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_devoluciones(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        fecha.setDate(new Date());
        hora.setText(horacorta.format(new Date()));
        consultamodifica(clavemodifica);
        conf=conexion.archivoInicial();
        usuariorem=conf.getProperty("UserID");
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);
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
                String senSQL="SELECT devoluciones.*,articulos.articulo,clientes.nombre,remisiones.um FROM (devoluciones LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON devoluciones.clavearticulo=articulos.clavearticulo) LEFT JOIN remisiones ON devoluciones.remision=remisiones.remision  WHERE (devoluciones.estatus<>'Can' AND id_devolucion='"+clavemodifica+"') ORDER BY devoluciones.fecha;";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    remision.setText(rs0.getString("remision"));
                    Date fechahora=rs0.getTimestamp("fecha");
                    fecha.setDate(fechahora);
                    hora.setText(horacorta.format(fechahora));
                    op.setText(rs0.getString("id_op"));
                    clavearticulo.setText(rs0.getString("clavearticulo"));
                    articulo.setText(rs0.getString("articulo"));
                    cantidad.setText(fijo0decimales.format(rs0.getInt("cantidaddevuelta")));
                    flete.setText(fijo2decimales.format(rs0.getDouble("costoflete")));
                    cliente.setText(rs0.getString("nombre"));
                    motivo.setText(rs0.getString("motivo"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
        
    } 
   
    public void imprimir(int dev){
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);

        JasperPrint jasperPrint =null;
        try{
            //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio()+"/logoempresa.png");
            pars.put("folio",null);
            pars.put("logoempresa",new FileInputStream(fichero));
            pars.put("logoempresa2",new FileInputStream(fichero));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("senSQL", "");//SQL dinamico
            pars.put("version", resourceMap.getString("Application.title"));
            pars.put("folio", dev);//fechafin.getDate()
            JasperReport masterReport = null;
            try{
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_devolucion.jasper"));
             }
             catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

            jasperPrint = JasperFillManager.fillReport(masterReport,pars,connj);
            //manda la venta de la impresora con las copias solicitadas
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add(new Copies(1));
            printRequestAttributeSet.add(new JobName("Devolucion", null));
            net.sf.jasperreports.engine.export.JRPrintServiceExporter exporter;
            exporter = new net.sf.jasperreports.engine.export.JRPrintServiceExporter();
            exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,false);
            exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, true);
            exporter.exportReport();

        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        remision = new javax.swing.JTextField();
        op = new javax.swing.JTextField();
        articulo = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        cantidad = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        clavearticulo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        hora = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        flete = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        motivo = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        cliente = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_devoluciones.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        remision.setFont(resourceMap.getFont("remision.font")); // NOI18N
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

        op.setEditable(false);
        op.setFocusable(false);
        op.setName("op"); // NOI18N
        op.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                opFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                opFocusLost(evt);
            }
        });

        articulo.setEditable(false);
        articulo.setFocusable(false);
        articulo.setName("articulo"); // NOI18N
        articulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                articuloFocusGained(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cantidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        cantidad.setText(resourceMap.getString("cantidad.text")); // NOI18N
        cantidad.setName("cantidad"); // NOI18N
        cantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cantidadFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cantidadFocusLost(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        clavearticulo.setEditable(false);
        clavearticulo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavearticulo.setText(resourceMap.getString("clavearticulo.text")); // NOI18N
        clavearticulo.setFocusable(false);
        clavearticulo.setName("clavearticulo"); // NOI18N
        clavearticulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clavearticuloFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clavearticuloFocusLost(evt);
            }
        });

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        fecha.setEnabled(false);
        fecha.setName("fecha"); // NOI18N

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        hora.setEditable(false);
        hora.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        hora.setFocusable(false);
        hora.setName("hora"); // NOI18N
        hora.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                horaFocusGained(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        flete.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        flete.setText(resourceMap.getString("flete.text")); // NOI18N
        flete.setName("flete"); // NOI18N
        flete.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fleteFocusGained(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        motivo.setColumns(20);
        motivo.setFont(resourceMap.getFont("motivo.font")); // NOI18N
        motivo.setLineWrap(true);
        motivo.setRows(4);
        motivo.setName("motivo"); // NOI18N
        jScrollPane1.setViewportView(motivo);

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        cliente.setEditable(false);
        cliente.setText(resourceMap.getString("cliente.text")); // NOI18N
        cliente.setFocusable(false);
        cliente.setName("cliente"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remision, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hora, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cliente)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clavearticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(articulo)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(flete, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(remision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(clavearticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(flete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        if(remision.getText().equals("")||cantidad.getText().equals("")||cantidad.getText().equals("0")||flete.getText().equals("")||motivo.getText().equals("")){
            String err="VERIFICA HAY CAMPOS VACIOS";
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            //revisar la cantidad de la remision
            int cantidaddevolucion=Integer.parseInt(cantidad.getText());
            if(cantidaddevolucion>cantidadremisionada){
                JOptionPane.showMessageDialog(this, "LA CANTIDAD ES MAYOR QUE LA REMISIONADA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try{
                    Double m2jgo=0.0;
                    Double kgjgo=0.0;
                    Double impjgo=0.0;
                    //busca la remision si ya fue facturada
                    String factura="0";
                    rs0=null;
                    
                        String senSQL="SELECT * FROM facturas_detalle WHERE (estatus='1' and remision='"+remision.getText()+"')";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            factura=rs0.getString("factura_serie");
                        }
                        if(rs0!=null) {   rs0.close(); }

                    //guarda la devolucion
                    senSQL="INSERT INTO devoluciones(fecha, remision, estatus, id_op, clavearticulo, cantidaddevuelta, motivo, factura_serie, usuario, costoflete) VALUES ('"+fechainsertar.format(fecha.getDate())+" "+hora.getText()+"', '"+remision.getText()+"', 'Act', '"+op.getText()+"', '"+clavearticulo.getText()+"', '"+cantidad.getText()+"', '"+motivo.getText()+"', '"+factura+"', '"+usuariorem+"', '"+flete.getText()+"');";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    //obtiene la ultima devolucion guardada
                    int maxdevolucion=1;
                    rs0=null;

                        senSQL="SELECT max(id_devolucion) as devmax FROM devoluciones";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            maxdevolucion=rs0.getInt("devmax");
                        }
                        if(rs0!=null){  rs0.close();  }

                    //resta las cantidades y las guarda en el almacen de cuarentena
                    rs1=null;
                        senSQL="SELECT remisiones_detalle.op,remisiones_detalle.clavearticulo,remisiones_detalle.cantidadpzas,remisiones_detalle.pzaspaquete,remisiones.fechareal,remisiones.facturado,articulos.articulo,articulos.kg,articulos.m2,ops.preciounitario FROM ((remisiones_detalle LEFT JOIN ops ON remisiones_detalle.op=ops.op) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision WHERE (remisiones.estatus='Act' AND remisiones_detalle.remision='"+remision.getText()+"');";
                        rs1=conexion.consulta(senSQL,connj);
                        while(rs1.next()){
                                    String opf=rs1.getString("op");
                                    String clavearticulof=rs1.getString("clavearticulo");
                                    Double cantidadf=rs1.getDouble("cantidadpzas");
                                    int pzasp=rs1.getInt("pzaspaquete");
                                    Date fechaf=rs1.getDate("fechareal");
                                    Double pzasjgo=cantidadf/cantidadremisionada;
                                    Double nuevaspzas=cantidaddevolucion*pzasjgo;
                                    m2jgo=m2jgo+(nuevaspzas*rs1.getDouble("m2"));
                                    kgjgo=kgjgo+(nuevaspzas*rs1.getDouble("kg"));
                                    impjgo=impjgo+(nuevaspzas*rs1.getDouble("preciounitario"));
                                    //guardad la cantidad a cancelar en el almacen de cuarentena
                                    senSQL="INSERT INTO almacen_cuarentena(fecha, op, clavearticulo, cantidadpzas, pzaspaquete, almacen, motivo, id_devolucion, remision) VALUES ('"+fechainsertar.format(new Date())+"', '"+opf+"', '"+clavearticulof+"', '"+nuevaspzas+"','"+pzasp+"', '1', '"+motivo.getText()+"', '"+maxdevolucion+"', '"+remision.getText()+"');";
                                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                                    //actualiza la remision detalle
                                    String senSQLmov = "UPDATE remisiones_detalle SET cantidadpzas='"+(cantidadf-nuevaspzas)+"' WHERE (remision='" + remision.getText() + "' AND op='"+opf+"');";
                                    conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                        }
                        if(rs1!=null) {      rs1.close();   }
                    

                    //actualiza la cantidad de la remision
                    String senSQLmov = "UPDATE devoluciones SET kg='"+kgjgo+"',m2='"+m2jgo+"',importe='"+impjgo+"' WHERE id_devolucion='" + maxdevolucion + "';";
                    conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                    //actualiza la cantidad de la remision
                    senSQLmov = "UPDATE remisiones SET cantidad='"+(cantidadremisionada-cantidaddevolucion)+"' WHERE remision='" + remision.getText() + "';";
                    conexion.modifica_p(senSQLmov, connj,valor_privilegio);

                    imprimir(maxdevolucion);
                    //si las cantidades son iguales cancela las remisiones
                    if(cantidaddevolucion==cantidadremisionada){
                        senSQLmov = "UPDATE remisiones_detalle SET estatus='Can' WHERE remision='" + remision.getText() + "';";
                        conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                        //cancela la remision y el detalle
                        senSQLmov = "UPDATE remisiones SET estatus='Can',observaciones='cancelada por devolucion: "+maxdevolucion+"' WHERE remision='" + remision.getText() + "';";
                        conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                    }

                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL GUARDAR LA DEVOLUCION\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    salir();
                }
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

    private void opFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusGained
        // TODO add your handling code here:
        op.selectAll();
}//GEN-LAST:event_opFocusGained

    private void articuloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_articuloFocusGained
        // TODO add your handling code here:
        articulo.selectAll();
}//GEN-LAST:event_articuloFocusGained

    private void cantidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cantidadFocusGained
        // TODO add your handling code here:
        cantidad.selectAll();
}//GEN-LAST:event_cantidadFocusGained

    private void opFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_opFocusLost

    private void clavearticuloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clavearticuloFocusGained
        // TODO add your handling code here:
        clavearticulo.selectAll();
    }//GEN-LAST:event_clavearticuloFocusGained

    private void cantidadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cantidadFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cantidadFocusLost

    private void clavearticuloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clavearticuloFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_clavearticuloFocusLost

    private void remisionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remisionFocusLost
        // TODO add your handling code here:
        if(remision.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT remisiones.*,articulos.articulo,clientes.nombre FROM (remisiones LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes WHERE (remisiones.estatus<>'Can' AND remision='"+remision.getText()+"')";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    op.setText(rs0.getString("id_op"));
                    clavearticulo.setText(rs0.getString("clavearticulo"));
                    articulo.setText(rs0.getString("articulo"));
                    cliente.setText(rs0.getString("nombre"));
                    cantidadremisionada=rs0.getInt("cantidad");
                    
                }else{
                    JOptionPane.showMessageDialog(this,"EL NUMERO DE REMISION NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    remision.setText("");
                    remision.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
}//GEN-LAST:event_remisionFocusLost

    private void remisionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remisionFocusGained
        // TODO add your handling code here:
        remision.selectAll();
}//GEN-LAST:event_remisionFocusGained

    private void horaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horaFocusGained
        // TODO add your handling code here:
        hora.selectAll();
}//GEN-LAST:event_horaFocusGained

    private void fleteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fleteFocusGained
        // TODO add your handling code here:
        flete.selectAll();
    }//GEN-LAST:event_fleteFocusGained


    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField articulo;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JFormattedTextField cantidad;
    private javax.swing.JTextField clavearticulo;
    private javax.swing.JTextField cliente;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JFormattedTextField flete;
    private javax.swing.JFormattedTextField hora;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea motivo;
    private javax.swing.JTextField op;
    private javax.swing.JTextField remision;
    // End of variables declaration//GEN-END:variables

}
