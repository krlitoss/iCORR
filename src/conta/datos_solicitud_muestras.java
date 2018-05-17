/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * muestras.java
 *
 * Created on 16/03/2009, 08:16:29 AM
 */

package conta;

import java.awt.Dimension;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 *
 * @author ANGEL
 */
public class datos_solicitud_muestras extends javax.swing.JDialog {
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MM-yyyy");
    Connection connj;
    ResultSet rs0;
    int partida=1;
    Date hoy=new Date();
    DefaultTableModel modelot1;
    String datosf[]={"","","n/a","","","","n/a","n/a","n/a","n/a","","","","n/a","n/a",""};
    String insertar="";

    Object[] objetos = new Object[]{"n/a","C","B","CB"};
    JComboBox comboflauta = new JComboBox(objetos);
    Object[] objetos2 = new Object[]{"n/a","1/2 Cuerpo","1/2 Regular","Armado Aut.","Banda Std","Caja Esp.","Caja Fnd/Crz","Caja Regular","Caja T/F Cru","Caja Tpa/Crz","Cja Traslape","Cojin","Charola STD","Charola Troq","Div. Corta", "Div. Larga", "División", "Entrepaño", "Esquinero", "Fondo", "Fondo Teles", "Hoja", "Lamina", "Maq. Caja", "Maq. Lamina", "Medio Fondo", "Rollo S.F.", "Sep rayado", "Separador", "Suaje Esp.", "Tapa", "Tapa Telescp", "Tira"};
    JComboBox combodiseño = new JComboBox(objetos2);
    Object[] objetos3 = new Object[]{"n/a","Sencillo","Doble"};
    JComboBox combocorr = new JComboBox(objetos3);
    Object[] objetos4 = new Object[]{"n/a","Cafe","Blanco"};
    JComboBox combocolor = new JComboBox(objetos4);
    Object[] objetos5 = new Object[]{"n/a", "SG", "7/9", "9/11", "11/12.5", "12.5/14", "14/16", "16/17.5", "16/18", "17.5/19", "19/21", "21/24", "21/25", "25/28", "25/30", "28/32", "32/35", "35/42", "ECT23", "ECT26", "ECT29", "ECT32", "ECT35"};
    JComboBox comboresis = new JComboBox(objetos5);
    Object[] objetos6 = new Object[]{"n/a", "Pegado", "Engrapado"};
    JComboBox combounion = new JComboBox(objetos6);
    Object[] objetos7 = new Object[]{"n/a", "Interna", "Externa"};
    JComboBox combounionceja = new JComboBox(objetos7);

    String valor_privilegio="1";


    /** Creates new form muestras */
    public datos_solicitud_muestras(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        txtfecha.setText(fechamediana.format(hoy));
        modelot1= new DefaultTableModel();
        Tabladatos.setModel(modelot1);
        llenaclientes();
        ajusteTabladatos();
        modelot1.addRow(datosf);
        org.jdesktop.swingx.autocomplete.AutoCompleteDecorator.decorate(this.cmbclientes);
        consultamodifica(clavemodifica);
        insertar=clavemodifica;
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

    }

    private void ajusteTabladatos() {

        modelot1.addColumn("<html><DIV ALIGN=center>Cant<br>&nbsp;&nbsp;</div></html>");//0
        modelot1.addColumn("<html><DIV ALIGN=center>Clave<br>&nbsp;&nbsp;</div></html>");//1
        modelot1.addColumn("<html><DIV ALIGN=center>Diseño<br>&nbsp;&nbsp;</div></html>");//2
        modelot1.addColumn("<html><DIV ALIGN=center>Largo<br>mm</div></html>");//3
        modelot1.addColumn("<html><DIV ALIGN=center>Ancho<br>mm</div></html>");//4
        modelot1.addColumn("<html><DIV ALIGN=center>Alto<br>mm</div></html>");//5
        modelot1.addColumn("<html><DIV ALIGN=center>Flauta<br>&nbsp;&nbsp;</div></html>");//6
        modelot1.addColumn("<html><DIV ALIGN=center>Tipo de<br>Corrugado</div></html>");//7
        modelot1.addColumn("<html><DIV ALIGN=center>Color<br>&nbsp;&nbsp;</div></html>");//8
        modelot1.addColumn("<html><DIV ALIGN=center>Resis.<br>&nbsp;&nbsp;</div></html>");//9
        modelot1.addColumn("<html><center>Pzas x<br>atado</center></html>");//10
        modelot1.addColumn("<html><center>Atados<br>x cama</center></html>");//11
        modelot1.addColumn("<html><DIV ALIGN=center>No.<br>Camas</div></html>");//12
        modelot1.addColumn("<html><DIV ALIGN=center>Tipo de<br>Union</div></html>");//13
        modelot1.addColumn("<html><center>Union<br>d ceja</center></html>");//14
        modelot1.addColumn("<html><center>Articulo<br>cancelado</center></html>");//15

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(33);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(157);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(73);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(37);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(39);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(32);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(40);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(63);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(45);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(55);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(45);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(43);
        Tabladatos.getColumnModel().getColumn(12).setPreferredWidth(42);
        Tabladatos.getColumnModel().getColumn(13).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(14).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(15).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(combodiseño));
        Tabladatos.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboflauta));
        Tabladatos.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(combocorr));
        Tabladatos.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(combocolor));
        Tabladatos.getColumnModel().getColumn(9).setCellEditor(new DefaultCellEditor(comboresis));
        Tabladatos.getColumnModel().getColumn(13).setCellEditor(new DefaultCellEditor(combounion));
        Tabladatos.getColumnModel().getColumn(14).setCellEditor(new DefaultCellEditor(combounionceja));

    }

    public void imprimir(){
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        JasperPrint jasperPrint =null;
        try{
            //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio()+"/logoempresa.png");
            pars.put("folio",Integer.parseInt(txtfolio.getText()));
            pars.put("logoempresa",new FileInputStream(fichero));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("version", resourceMap.getString("Application.title"));
            JasperReport masterReport = null;
            try{
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/solicitud_muestras.jasper"));
             }
             catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

            jasperPrint = JasperFillManager.fillReport(masterReport,pars,connj);
            PrinterJob job = PrinterJob.getPrinterJob();
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add(new Copies(2));
            printRequestAttributeSet.add(new JobName("Muestras", null));
            net.sf.jasperreports.engine.export.JRPrintServiceExporter exporter;
            exporter = new net.sf.jasperreports.engine.export.JRPrintServiceExporter();
            exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE, job.getPrintService());
            exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,false);
            exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, true);
            exporter.exportReport();
            salir();
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }

    }

    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM solmuestras WHERE (folio="+clavemodifica+" AND Cancelado='no') ORDER BY partida;";
                rs0=conexion.consulta(senSQL,connj);
                modelot1.setNumRows(0);
                while(rs0.next()){
                    txtfolio.setText(rs0.getString("folio"));
                    cmbclientes.setEditable(false);//error al llenar los datos
                    String val=rs0.getString("cliente");
                    cmbclientes.addItem(val);
                    cmbclientes.setSelectedItem(""+val);
                    cmbtipo.setSelectedItem(rs0.getObject("tipomuestra"));
                    txtfecha.setText(fechamediana.format(rs0.getDate("fecha")));

                    String datosas[]={rs0.getString("cantidad1"),rs0.getString("clave1"),rs0.getString("diseno1"),rs0.getString("largo1"),rs0.getString("ancho1"),rs0.getString("alto1"),rs0.getString("flauta1"),rs0.getString("corr1"),rs0.getString("color1"),rs0.getString("resis1"),rs0.getString("pzasxatado1"),rs0.getString("atadosxcama1"),rs0.getString("camas1"),rs0.getString("union1"),rs0.getString("tipounion1"),rs0.getString("artcancelado1")};
                    modelot1.addRow(datosas);

                    txtobservaciones.setText(rs0.getString("observaciones"));
                    cmbsolicitante.setSelectedItem(rs0.getObject("solicitante"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

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
        jLabel2 = new javax.swing.JLabel();
        txtfecha = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbtipo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtfolio = new javax.swing.JTextField();
        cmbclientes = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtobservaciones = new javax.swing.JTextArea();
        jLabel35 = new javax.swing.JLabel();
        cmbsolicitante = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        btnadd = new javax.swing.JButton();
        btnremover = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar1 = new javax.swing.JButton();
        btnimprimir1 = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_solicitud_muestras.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        txtfecha.setEditable(false);
        txtfecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtfecha.setText(resourceMap.getString("txtfecha.text")); // NOI18N
        txtfecha.setFocusable(false);
        txtfecha.setName("txtfecha"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        cmbtipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "GRAFICO", "ESTRUCTURAL", "GRAFICO Y ESTRUCTURAL" }));
        cmbtipo.setName("cmbtipo"); // NOI18N

        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        txtfolio.setEditable(false);
        txtfolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtfolio.setText(resourceMap.getString("txtfolio.text")); // NOI18N
        txtfolio.setFocusable(false);
        txtfolio.setName("txtfolio"); // NOI18N

        cmbclientes.setEditable(true);
        cmbclientes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        cmbclientes.setName("cmbclientes"); // NOI18N
        cmbclientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbclientesActionPerformed(evt);
            }
        });
        cmbclientes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbclientesFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtfolio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbtipo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(98, 98, 98)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(cmbclientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(248, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtfolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtfecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbclientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbtipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText(resourceMap.getString("jLabel26.text")); // NOI18N
        jLabel26.setName("jLabel26"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        txtobservaciones.setColumns(20);
        txtobservaciones.setFont(resourceMap.getFont("txtobservaciones.font")); // NOI18N
        txtobservaciones.setLineWrap(true);
        txtobservaciones.setRows(3);
        txtobservaciones.setText(resourceMap.getString("txtobservaciones.text")); // NOI18N
        txtobservaciones.setName("txtobservaciones"); // NOI18N
        txtobservaciones.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtobservacionesFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(txtobservaciones);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText(resourceMap.getString("jLabel35.text")); // NOI18N
        jLabel35.setName("jLabel35"); // NOI18N

        cmbsolicitante.setBackground(resourceMap.getColor("cmbsolicitante.background")); // NOI18N
        cmbsolicitante.setEditable(true);
        cmbsolicitante.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Concepcion Cruz Zamora", "Gilberto Coronado Orta" }));
        cmbsolicitante.setName("cmbsolicitante"); // NOI18N
        cmbsolicitante.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbsolicitanteFocusGained(evt);
            }
        });

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "<html>titulo<br>1</html>", "Title 2"
            }
        ));
        Tabladatos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos.setCellSelectionEnabled(true);
        Tabladatos.setDoubleBuffered(true);
        Tabladatos.setDragEnabled(true);
        Tabladatos.setGridColor(resourceMap.getColor("Tabladatos.gridColor")); // NOI18N
        Tabladatos.setName("Tabladatos"); // NOI18N
        Tabladatos.setRowHeight(21);
        Tabladatos.setSelectionBackground(resourceMap.getColor("Tabladatos.selectionBackground")); // NOI18N
        Tabladatos.setSelectionForeground(resourceMap.getColor("Tabladatos.selectionForeground")); // NOI18N
        Tabladatos.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(Tabladatos);

        btnadd.setIcon(resourceMap.getIcon("btnadd.icon")); // NOI18N
        btnadd.setToolTipText(resourceMap.getString("btnadd.toolTipText")); // NOI18N
        btnadd.setName("btnadd"); // NOI18N
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        btnremover.setIcon(resourceMap.getIcon("btnremover.icon")); // NOI18N
        btnremover.setToolTipText(resourceMap.getString("btnremover.toolTipText")); // NOI18N
        btnremover.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnremover.setName("btnremover"); // NOI18N
        btnremover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoverActionPerformed(evt);
            }
        });

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        btnaceptar.setIcon(resourceMap.getIcon("btnaceptar.icon")); // NOI18N
        btnaceptar.setText(resourceMap.getString("btnaceptar.text")); // NOI18N
        btnaceptar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnaceptar.setName("btnaceptar"); // NOI18N
        btnaceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaceptarActionPerformed(evt);
            }
        });

        btncancelar1.setIcon(resourceMap.getIcon("btncancelar1.icon")); // NOI18N
        btncancelar1.setText(resourceMap.getString("btncancelar1.text")); // NOI18N
        btncancelar1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btncancelar1.setName("btncancelar1"); // NOI18N
        btncancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelar1ActionPerformed(evt);
            }
        });

        btnimprimir1.setIcon(resourceMap.getIcon("btnimprimir1.icon")); // NOI18N
        btnimprimir1.setText(resourceMap.getString("btnimprimir1.text")); // NOI18N
        btnimprimir1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnimprimir1.setName("btnimprimir1"); // NOI18N
        btnimprimir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimir1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(204, Short.MAX_VALUE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnimprimir1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(197, 197, 197))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnimprimir1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbsolicitante, 0, 191, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel35)
                        .addComponent(cmbsolicitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel26)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void txtobservacionesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtobservacionesFocusGained
    // TODO add your handling code here:
}//GEN-LAST:event_txtobservacionesFocusGained

private void cmbsolicitanteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbsolicitanteFocusGained
    // TODO add your handling code here:

}//GEN-LAST:event_cmbsolicitanteFocusGained

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    // TODO add your handling code here:
    salir();
    
}//GEN-LAST:event_formWindowClosing

private void cmbclientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbclientesActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_cmbclientesActionPerformed

private void cmbclientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbclientesFocusLost
    // TODO add your handling code here:
}//GEN-LAST:event_cmbclientesFocusLost

private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
    // TODO add your handling code here:
    String datosa[]={"","","n/a","","","","n/a","n/a","n/a","n/a","","","","n/a","n/a",""};
    modelot1.addRow(datosa);
}//GEN-LAST:event_btnaddActionPerformed

private void btnremoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoverActionPerformed
    // TODO add your handling code here:
    int filano=Tabladatos.getSelectedRow();
    if(filano<0){
        JOptionPane.showMessageDialog(null, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
    }else{
        int respuesta=JOptionPane.showConfirmDialog(null,"ESTA SEGURO DE ELIMINAR LA FILA  "+(filano+1)+" ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            modelot1.removeRow(filano);
        }else{

        }
    }
}//GEN-LAST:event_btnremoverActionPerformed

private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
    // TODO add your handling code here:
    if(insertar.equals("")){
    /**si la clave esta vacia significa que es un registro nuevo*/
        rs0=null;
        int filas=Tabladatos.getRowCount();
        int columnas=Tabladatos.getColumnCount();
        int camposvacios=0;

        for(int i=0;i<=(filas-1);i=i+1){
            for(int j=0;j<=(columnas-2);j=j+1){
                if(modelot1.getValueAt(i, j) == null||modelot1.getValueAt(i, j).equals("")){
                    camposvacios=1;
                }
            }
        }/**fin de revisar los campos vacios*/

        if(cmbclientes.getSelectedItem().equals("") || cmbsolicitante.getSelectedItem().equals(" ")){
            JOptionPane.showMessageDialog(this,"HAY CAMPOS VACIOS\n SI NO NECESITAS UNA FILA BORRALA","V E R I F I C A !!!!",JOptionPane.WARNING_MESSAGE);
        }else{

            if(camposvacios==1){
                JOptionPane.showMessageDialog(this, "LA TABLA DE DETALLE TIENE CAMPOS VACIOS\nBORRE LAS FILAS VACIAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                try{
                    String instruccion="SELECT Max(Folio) as nclave FROM solmuestras";
                    rs0=conexion.consulta(instruccion,connj);
                    if(rs0.next()){
                        txtfolio.setText(Integer.toString(rs0.getInt("nclave")+1));
                    }else
                        JOptionPane.showMessageDialog(this,"NO SE PUEDE OBTENER NUEVO FOLIO","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);

                    if(rs0!=null) {
                        rs0.close();
                    }
                 }
                 catch(Exception ex){         JOptionPane.showMessageDialog(this,"NO SE PUEDE OBTENER NUEVO FOLIO \n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);             }

                //for para guardar todo con los numeros de partidas
                for(int i=0;i<=(filas-1);i=i+1)
                {
                    int partidaf=i+1;
                    String guarda="INSERT INTO solmuestras(folio, cliente, tipomuestra, fecha, partida, cantidad1, clave1, diseno1, largo1, ancho1, alto1, flauta1, corr1,color1, resis1, pzasxatado1, atadosxcama1, camas1, union1, tipounion1,artcancelado1, observaciones, solicitante, cancelado) VALUES('"+txtfolio.getText()+"','"+(String)cmbclientes.getSelectedItem()+"','"+(String)cmbtipo.getSelectedItem()+"','"+fechainsertar.format(hoy)+"','"+partidaf+"','"+(String)Tabladatos.getValueAt(i, 0)+"','"+(String)Tabladatos.getValueAt(i, 1)+"','"+(String)Tabladatos.getValueAt(i, 2)+"','"+(String)Tabladatos.getValueAt(i, 3)+"','"+(String)Tabladatos.getValueAt(i, 4)+"','"+(String)Tabladatos.getValueAt(i, 5)+"','"+(String)Tabladatos.getValueAt(i, 6)+"','"+(String)Tabladatos.getValueAt(i, 7)+"','"+(String)Tabladatos.getValueAt(i, 8)+"','"+(String)Tabladatos.getValueAt(i, 9)+"','"+(String)Tabladatos.getValueAt(i, 10)+"','"+(String)Tabladatos.getValueAt(i, 11)+"','"+(String)Tabladatos.getValueAt(i, 12)+"','"+(String)Tabladatos.getValueAt(i, 13)+"','"+(String)Tabladatos.getValueAt(i, 14)+"','"+(String)Tabladatos.getValueAt(i, 15)+"','"+txtobservaciones.getText()+"','"+(String)cmbsolicitante.getSelectedItem()+"','no');";
                    conexion.modificamov_p(guarda,connj,valor_privilegio);
                }
                JOptionPane.showMessageDialog(this, "DATOS CREADOS CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                imprimir();
            }
        }
    }else{
        JOptionPane.showMessageDialog(this,"ESTA DESHABILITADA LA OPCION DE MODIFICAR","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
    }
}//GEN-LAST:event_btnaceptarActionPerformed

private void btncancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelar1ActionPerformed
    // TODO add your handling code here:
    salir();
}//GEN-LAST:event_btncancelar1ActionPerformed

private void btnimprimir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimir1ActionPerformed
    // TODO add your handling code here:
    imprimir();
}//GEN-LAST:event_btnimprimir1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btncancelar1;
    private javax.swing.JButton btnimprimir1;
    private javax.swing.JButton btnremover;
    private javax.swing.JComboBox cmbclientes;
    private javax.swing.JComboBox cmbsolicitante;
    private javax.swing.JComboBox cmbtipo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtfecha;
    private javax.swing.JTextField txtfolio;
    private javax.swing.JTextArea txtobservaciones;
    // End of variables declaration//GEN-END:variables

    private void llenaclientes() {
        try{
            String instruccion="SELECT * FROM Clientes ORDER BY nombre";
            rs0=conexion.consulta(instruccion,connj);
            while(rs0.next()){
                cmbclientes.addItem(rs0.getString("nombre"));
            }

            if(rs0!=null) {
                rs0.close();
            }
        }
        catch(Exception ex){       JOptionPane.showMessageDialog(datos_solicitud_muestras.this,"NO SE PUEDE OBTENER LOS CLIENTES \n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE); }
    }

    private void salir() {
        if(connj!=null){
            connj=null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }
}
