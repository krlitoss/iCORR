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
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 *
 * @author IVONNE
 */
public class datos_remisiones extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null,rs1=null,rs2=null;
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    int cantidadpedida=0;
    String opprincipal="";
    String clienteprincipal="";
    Double preciounitarioop=0.0;
    String ordencompraop="";
    private Properties conf;
    String usuariorem="";
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_remisiones(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
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
                String senSQL="SELECT remisiones.*,articulos.articulo,transportistas.nombre as tnombre,transportistas.tipounidad,transportistas.placas FROM (remisiones LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN transportistas ON remisiones.clave_transportista=transportistas.clave_transportista WHERE remisiones.remision='"+clavemodifica+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    remision.setText(rs0.getString("remision"));
                    turno.setSelectedItem(rs0.getString("turno"));
                    Date fechahora=rs0.getTimestamp("fechahora");
                    fecha.setDate(fechahora);
                    hora.setText(horacorta.format(fechahora));
                    op.setText(rs0.getString("id_op"));
                    clavearticulo.setText(rs0.getString("clavearticulo"));
                    articulo.setText(rs0.getString("articulo"));
                    cantidad.setText(fijo0decimales.format(rs0.getInt("cantidad")));
                    idtransportista.setText(rs0.getString("clave_transportista"));
                    tipounidad.setText(rs0.getString("tipounidad"));
                    nombre.setText(rs0.getString("tnombre"));
                    remisioncomer.setText(rs0.getString("remision_comercializador"));
                    flete.setText(fijo2decimales.format(rs0.getDouble("importeflete")));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
        
    }
    public int revisainventario(String opf,String artf,int cantrem){
        int resultado=1;
        //busca con OP y articulo
        rs0=null;
        try{
            String senSQL="SELECT * FROM almacen_pt WHERE (op='"+opf+"' AND clavearticulo='"+artf+"')";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                int cantidadinventario=rs0.getInt("cantidadpzas");
                if(cantrem<=cantidadinventario){
                    resultado=0;
                }else{
                    resultado=2;
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        return resultado;
    }
    public int revisa10porciento(String opf,int cantrem){
        int resultado=1;
        //busca con OP y articulo
        rs0=null;
        try{
            String senSQL="SELECT ops.*,(ops.cantidad-COALESCE(remisionado.cantidadremision,0)) as cantidadpendiente,COALESCE(remisionado.cantidadremision,0) as cantidadremisionado,remisionado.fecharemisionado FROM ops LEFT JOIN (SELECT remisiones_detalle.op,sum(remisiones_detalle.cantidadpzas) as cantidadremision,max(remisiones.fechahora) as fecharemisionado FROM remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision WHERE (remisiones_detalle.estatus='Act') GROUP BY remisiones_detalle.op) as remisionado ON ops.op=remisionado.op WHERE ops.op='"+opf+"';";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                Double cantidadpedido=rs0.getDouble("cantidad");
                if((rs0.getDouble("cantidadremisionado")+cantrem)>(cantidadpedido*1.1)){
                    resultado=1;
                }else{
                    resultado=0;
                }
            }
            if(rs0!=null) { rs0.close();   }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        return resultado;
    }
    public void actualizaOP(String opestatus){
        ////////////////actualiza la orden de produccion
        rs2=null;
        try{
            String senSQL="SELECT ops.cantidad,(ops.cantidad-COALESCE(remisionado.cantidadremision,0)) as cantidadpendiente,COALESCE(remisionado.cantidadremision,0) as cantidadremisionado,remisionado.fecharemisionado FROM ops LEFT JOIN (SELECT remisiones_detalle.op,sum(remisiones_detalle.cantidadpzas) as cantidadremision,max(remisiones.fechahora) as fecharemisionado FROM remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision WHERE (remisiones_detalle.estatus='Act') GROUP BY remisiones_detalle.op) as remisionado ON ops.op=remisionado.op WHERE (ops.estatus<>'Can' and ops.op='"+opestatus+"');";
            rs2=conexion.consulta(senSQL,connj);
            if(rs2.next()){
                int cantped=rs2.getInt("cantidad");
                int cantremisionado=rs2.getInt("cantidadremisionado");
                if(cantremisionado>=cantped){
                    String senSQLmov = "UPDATE ops SET estatus='Ter' WHERE op='" + opestatus + "';";
                    conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                }else{
                    String senSQLmov = "UPDATE ops SET estatus='Act' WHERE op='" + opestatus + "';";
                    conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                }
            }else{
                String senSQLmov = "UPDATE ops SET estatus='Act' WHERE op='" + opestatus + "';";
                conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
            }
            if(rs2!=null) {  rs2.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public int insertarPartida(String opbusca,String opf,String artf,int cantrem,Double preciounitariopartida){
        int resultadoguarda=1;     //1 para decir que no se guardo nada
        //busca con OP y articulo
        rs0=null;
        try{
            String senSQL="SELECT almacen_pt.*,articulos.kg,articulos.m2 FROM almacen_pt LEFT JOIN articulos ON almacen_pt.clavearticulo=articulos.clavearticulo  WHERE (almacen_pt.op='"+opbusca+"' AND almacen_pt.clavearticulo='"+artf+"')";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                int cantidadinventario=rs0.getInt("cantidadpzas");
                if(cantrem<=cantidadinventario){
                    //funcion para guardar los datos de la partida
                    senSQL="INSERT INTO remisiones(remision, remision_comercializador, estatus, fechahora, fechareal, turno, id_clientes, ordencompra, clave_transportista,id_op,clavearticulo,cantidad,um,kgrealbascula, preciounitario, importeflete, observaciones, facturado,usuario) VALUES ('"+remision.getText()+"', '"+remisioncomer.getText()+"', 'Act', '"+fechainsertar.format(fecha.getDate())+" "+hora.getText()+"', '"+fechainsertarhora.format(new Date())+"', '"+(String)turno.getSelectedItem()+"', '"+clienteprincipal+"', '"+ordencompraop+"', '"+idtransportista.getText()+"', '"+op.getText()+"', '"+clavearticulo.getText()+"', '"+cantrem+"','piezas', '0', '"+preciounitariopartida+"', '"+flete.getText()+"', '', false,'"+usuariorem+"');";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    senSQL="INSERT INTO remisiones_detalle(remision, estatus, op, clavearticulo, cantidadpzas, pzaspaquete) VALUES ('"+remision.getText()+"', 'Act', '"+opf+"', '"+artf+"', '"+cantrem+"', '"+rs0.getInt("pzaspaquete")+"');";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    //iserta una partida en salida de almacen PT
                    senSQL="INSERT INTO salidas_almacen_pt( estatus, fecha, remision, op, clavearticulo, cantidadpzas, pzaspaquete, almacen) VALUES ('Act', '"+fechainsertarhora.format(new Date())+"', '"+remision.getText()+"', '"+opf+"', '"+artf+"', '"+cantrem+"', '"+rs0.getInt("pzaspaquete")+"', '"+rs0.getString("almacen")+"');";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    //actualiza el almacen de PT
                    senSQL = "UPDATE almacen_pt SET cantidadpzas='"+(cantidadinventario-cantrem)+"' WHERE (op='"+opbusca+"' AND clavearticulo='"+artf+"');";
                    conexion.modificamov_p(senSQL, connj,valor_privilegio);
                    //actualiza datos del articulo
                    senSQL="UPDATE articulos SET fechaultima='"+fechainsertar.format(new Date())+"', cantidadultima='"+cantrem+"' WHERE clavearticulo='"+artf+"';";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    ////////////////actualiza la orden de produccion
                    actualizaOP(opf);

                    resultadoguarda=0;
                }
            }
            if(rs0!=null) {    rs0.close();  }
            
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        return resultadoguarda;
    }
    public int insertarPartidaJgo(String opbusca,String opf,String artf,int cantrem,Double preciounitariopartida){
        int resultadoguarda=1; //1 para decir que no se guardo nada
        //busca con OP y articulo
        rs0=null;
        try{
            String senSQL="SELECT almacen_pt.*,articulos.kg,articulos.m2 FROM almacen_pt LEFT JOIN articulos ON almacen_pt.clavearticulo=articulos.clavearticulo  WHERE (almacen_pt.op='"+opbusca+"' AND almacen_pt.clavearticulo='"+artf+"')";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                int cantidadinventario=rs0.getInt("cantidadpzas");
                if(cantrem<=cantidadinventario){
                    senSQL="INSERT INTO remisiones_detalle(remision, estatus, op, clavearticulo, cantidadpzas, pzaspaquete) VALUES ('"+remision.getText()+"', 'Act', '"+opf+"', '"+artf+"', '"+cantrem+"', '"+rs0.getInt("pzaspaquete")+"');";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    senSQL = "UPDATE almacen_pt SET cantidadpzas='"+(cantidadinventario-cantrem)+"' WHERE (op='"+opbusca+"' AND clavearticulo='"+artf+"');";
                    conexion.modificamov_p(senSQL, connj,valor_privilegio);
                    //actualiza datos del articulo
                    senSQL="UPDATE articulos SET fechaultima='"+fechainsertar.format(new Date())+"', cantidadultima='"+cantrem+"' WHERE clavearticulo='"+artf+"';";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    ////////////////actualiza la orden de produccion
                    actualizaOP(opf);

                    resultadoguarda=0;
                }
            }
            if(rs0!=null) {  rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        return resultadoguarda;
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
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_remision.jasper"));
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
        turno = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        hora = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        idtransportista = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tipounidad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        flete = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        remisioncomer = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_remisiones.class);
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

        turno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3" }));
        turno.setName("turno"); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        hora.setEditable(false);
        hora.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        hora.setName("hora"); // NOI18N
        hora.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                horaFocusGained(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        idtransportista.setEditable(false);
        idtransportista.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idtransportista.setText(resourceMap.getString("idtransportista.text")); // NOI18N
        idtransportista.setFocusable(false);
        idtransportista.setName("idtransportista"); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        tipounidad.setEditable(false);
        tipounidad.setText(resourceMap.getString("tipounidad.text")); // NOI18N
        tipounidad.setFocusable(false);
        tipounidad.setName("tipounidad"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        nombre.setEditable(false);
        nombre.setText(resourceMap.getString("nombre.text")); // NOI18N
        nombre.setFocusable(false);
        nombre.setName("nombre"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        flete.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        flete.setText(resourceMap.getString("flete.text")); // NOI18N
        flete.setName("flete"); // NOI18N
        flete.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fleteFocusGained(evt);
            }
        });

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        remisioncomer.setText(resourceMap.getString("remisioncomer.text")); // NOI18N
        remisioncomer.setName("remisioncomer"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remision, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clavearticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(idtransportista)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tipounidad, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(articulo)))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remisioncomer, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(flete, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(remision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(jLabel7)
                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(idtransportista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(tipounidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(remisioncomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(flete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        if(remision.getText().equals("")||hora.getText().equals("")||fecha.getDate()==null||op.getText().equals("")||cantidad.getText().equals("")||cantidad.getText().equals("0")||flete.getText().equals("")||idtransportista.getText().equals("")){
            String err="VERIFICA HAY CAMPOS VACIOS";
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
        //revisar existencia en almacen
            int errorinventario=0;
            int errorinventariojuegos=0;
            int errorinventariojuegos_t=0;
            int error10porciento=0;
            int error10porcientojuegos=0;
            int error10porcientojuegos_t=0;
            int juegos=0;
            int cantidadremisionar=Integer.parseInt(cantidad.getText());
            String articuloprincipal=clavearticulo.getText();
            int dotPos = clavearticulo.getText().lastIndexOf("-");//obtiene el articulo principal
            articuloprincipal=articuloprincipal.substring(0,dotPos)+"-1"; //articulo principal para buscarlo en los juegos

            //verifica que sean juegos
            rs0=null;
            try{
                String senSQL="SELECT * FROM ops WHERE ops.id_op='"+opprincipal+"'";
                rs0=conexion.consulta(senSQL,connj);
                int i=0;
                while(rs0.next()){
                    i++;
                    if(i>1)
                        juegos=1;
                }
                if(rs0!=null) {  rs0.close();  }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            if(juegos==0){
                errorinventario=1;
                //busca inventario con la OP
                errorinventario=revisainventario(op.getText(),clavearticulo.getText(),cantidadremisionar);
                //busca el stock del mismo articulo
                if(errorinventario!=0) //silo encuentra en la primera ya no lo busca
                    errorinventario=revisainventario("STOCK",clavearticulo.getText(),cantidadremisionar);

                error10porciento=1;//revisa que la cantidad a remisionar no supere el 10 por cento
                error10porciento=revisa10porciento(op.getText(),cantidadremisionar);
            
            }else{
                //verifica que existan las cantidades de los juegos en al almacen_pt
                errorinventariojuegos=1;
                errorinventariojuegos_t=0;
                error10porcientojuegos=1;//revisa que la cantidad a remisionar no supere el 10 por cento
                error10porcientojuegos_t=0;
                int opjuego=0;
                rs1=null;
                try{
                    String senSQL="SELECT articulos_juegos.*,articulos.articulo,articulos.preciomillar FROM articulos_juegos LEFT JOIN articulos ON articulos_juegos.clavearticulo1=articulos.clavearticulo WHERE articulos_juegos.clavearticulo='"+articuloprincipal+"'";
                    rs1=conexion.consulta(senSQL,connj);
                    while(rs1.next()){
                        opjuego++;
                        int juegoscantidad=rs1.getInt("piezas")*cantidadremisionar;
                        String clavearticulo1=rs1.getString("clavearticulo1");
                        //busca inventari con la OP
                        errorinventariojuegos=revisainventario(opprincipal+"-"+opjuego,clavearticulo1,juegoscantidad);
                        if(errorinventariojuegos!=0) //silo encuentra en la primera ya no lo busca
                            errorinventariojuegos=revisainventario("STOCK",clavearticulo1,juegoscantidad);

                        if(errorinventariojuegos==1||errorinventariojuegos==2)
                           errorinventariojuegos_t=1 ;

                        error10porcientojuegos=revisa10porciento(opprincipal+"-"+opjuego,juegoscantidad);

                        if(error10porcientojuegos==1)
                           error10porcientojuegos_t=1 ;

                    }
                    if(rs1!=null) {      rs1.close();     }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }



            }


            if(errorinventario==1||errorinventario==2||errorinventariojuegos_t==1 || error10porciento==1 || error10porcientojuegos_t==1){
                String err="ERROR GENERAL";
                if(juegos==0){
                    if(errorinventario==1)
                        err="LA OP NO FUE ENCONTRADA EN ALMACEN PT";
                    if(errorinventario==2)
                        err="LA CANTIDAD ES MAYOR QUE LA EXISTENTE EN ALMACEN DE PT";
                    if(error10porciento==1)
                        err="LA OP SUPERA EL 10% ADICIONAL PERMITIDO PARA REMISIONAR ";
                }else{
                    if(errorinventariojuegos_t==1)
                        err="LA OP NO FUE ENCONTRADA EN ALMACEN PT PARA JUEGOS Ó ES MAYOR QUE LA EXISTENTE EN ALMACEN DE PT\nVERIFIQUE CANTIDADES Y PIEZAS POR JUEGO";
                    if(error10porcientojuegos_t==1)
                        err="UNA DE LAS OP SUPERA EL 10% ADICIONAL PERMITIDO PARA REMISIONAR ";
                }

                JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                if(juegos==0){ //guarda cuando es una sola partida sin juegos
                    if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        int res=insertarPartida(op.getText(),op.getText(),clavearticulo.getText(),cantidadremisionar,preciounitarioop);
                        //si no lo encuentra los busca en el stock del articulo
                        if(res!=0)
                            res=insertarPartida("STOCK",op.getText(),clavearticulo.getText(),cantidadremisionar,preciounitarioop);

                        if(res==0){
                            JOptionPane.showMessageDialog(this, "DATOS GUARDADOS CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                            imprimirremision();
                        }
                    }
                }else{
                    if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR JUEGO!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        Double m2jgo=0.0;
                        Double kgjgo=0.0;
                        Double preciounitariojgo=0.0;
                        int opjuego=0;
                        rs1=null;
                        try{
                            String senSQL="SELECT articulos_juegos.*,articulos.articulo,articulos.preciomillar FROM articulos_juegos LEFT JOIN articulos ON articulos_juegos.clavearticulo1=articulos.clavearticulo WHERE articulos_juegos.clavearticulo='"+articuloprincipal+"'";
                            rs1=conexion.consulta(senSQL,connj);
                            while(rs1.next()){
                                opjuego++;
                                int pzasj=rs1.getInt("piezas");
                                int juegoscantidad=pzasj*cantidadremisionar;
                                String clavearticulo1=rs1.getString("clavearticulo1");
                                String op1=opprincipal+"-"+opjuego;
                                Double preciounitariojgopartida=0.0;
                                
                                //revisa la op para obtener los kg y m2 y precio unitario de cada op
                                rs0=null;
                                try{
                                    senSQL="SELECT ops.*,articulos.articulo,articulos.kg,articulos.m2,agentes.id_clientes as clientecomercial,agentes.comercializador,clientes.id_clientes FROM (ops LEFT JOIN (clientes LEFT JOIN agentes ON clientes.id_agente=agentes.id_agente) ON ops.id_clientes=clientes.id_clientes) LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo WHERE (ops.estatus<>'Can' and ops.op='"+op1+"')";
                                    rs0=conexion.consulta(senSQL,connj);
                                    if(rs0.next()){
                                        m2jgo=m2jgo+(juegoscantidad*rs0.getDouble("m2"));
                                        kgjgo=kgjgo+(juegoscantidad*rs0.getDouble("kg"));
                                        //MUY IMPORTANTE PRECIO UNITARIO DE LA PARTIDA SUMA
                                        preciounitariojgopartida=pzasj*rs0.getDouble("preciounitario");
                                        preciounitariojgo=preciounitariojgo+preciounitariojgopartida;
                                    }
                                    if(rs0!=null) {   rs0.close(); }
                                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                                ///fin de obtener los datos de la op

                                int res=insertarPartidaJgo(op1,op1,clavearticulo1,juegoscantidad,preciounitariojgopartida);
                                //si no lo encuentra los busca en el stock del articulo
                                if(res!=0)
                                    res=insertarPartidaJgo("STOCK",op1,clavearticulo1,juegoscantidad,preciounitariojgopartida);
                                
                            }
                            if(rs1!=null) {
                                rs1.close();
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                        //despues de guardar el detalle se guarda la partida principal
                        String senSQL="INSERT INTO remisiones(remision, remision_comercializador, estatus, fechahora, fechareal, turno, id_clientes, ordencompra, clave_transportista,id_op,clavearticulo,cantidad,um,kgrealbascula, preciounitario, importeflete, observaciones, facturado,usuario) VALUES ('"+remision.getText()+"', '"+remisioncomer.getText()+"', 'Act', '"+fechainsertar.format(fecha.getDate())+" "+hora.getText()+"', '"+fechainsertarhora.format(new Date())+"', '"+(String)turno.getSelectedItem()+"', '"+clienteprincipal+"', '"+ordencompraop+"', '"+idtransportista.getText()+"', '"+opprincipal+"', '"+articuloprincipal+"', '"+cantidadremisionar+"','juegos', '0', '"+preciounitariojgo+"', '"+flete.getText()+"', '', false,'"+usuariorem+"');";
                        conexion.modificamov_p(senSQL,connj,valor_privilegio);
                        JOptionPane.showMessageDialog(this, "DATOS GUARDADOS CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                        imprimirremision();
                    }
                }
                //elimina los ceros del inventario
                String senSQLinv="DELETE FROM almacen_pt WHERE (cantidadpzas<1) ";
                conexion.modificamov_p(senSQLinv, connj,valor_privilegio);
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
        op.setText(op.getText().toUpperCase());
        if(op.getText().equals("")){

        }else{
                rs0=null;
                opprincipal="";
                clienteprincipal="";
                preciounitarioop=0.0;
                ordencompraop="";
                try{
                    String senSQL="SELECT ops.*,articulos.articulo,agentes.id_clientes as clientecomercial,agentes.comercializador,clientes.id_clientes FROM (ops LEFT JOIN (clientes LEFT JOIN agentes ON clientes.id_agente=agentes.id_agente) ON ops.id_clientes=clientes.id_clientes) LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo WHERE (ops.estatus<>'Can' and ops.op='"+op.getText()+"')";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        clavearticulo.setText(rs0.getString("clavearticulo"));
                        articulo.setText(rs0.getString("articulo"));
                        cantidadpedida=rs0.getInt("cantidad");
                        opprincipal=rs0.getString("id_op");
                        clienteprincipal=rs0.getString("id_clientes");
                        if(rs0.getString("comercializador").contains("Si"))
                            clienteprincipal=rs0.getString("clientecomercial");

                        
                        //MUY IMPORTANTE PRECIO UNITARIO DE LA PARTIDA DE LA OP
                        preciounitarioop=rs0.getDouble("preciounitario");
                        ordencompraop=rs0.getString("ordencompra");
                        
                    }else{
                        JOptionPane.showMessageDialog(this,"LA OP NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        op.setText("");
                        clavearticulo.setText("");
                        articulo.setText("");
                        op.requestFocus();
                    }
                    if(rs0!=null) {   rs0.close();    }
                    
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
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
                String senSQL="SELECT * FROM remisiones WHERE remision='"+remision.getText()+"'";

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        busca_transportistas busca_transportistas = new busca_transportistas(null,true,connj,"");
        busca_transportistas.setLocationRelativeTo(this);
        busca_transportistas.setVisible(true);
        idtransportista.setText(busca_transportistas.getText());
        busca_transportistas=null;
        if(idtransportista.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM transportistas WHERE clave_transportista='"+idtransportista.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    tipounidad.setText(rs0.getString("tipounidad"));
                    nombre.setText(rs0.getString("nombre"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField articulo;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JFormattedTextField cantidad;
    private javax.swing.JTextField clavearticulo;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JFormattedTextField flete;
    private javax.swing.JFormattedTextField hora;
    private javax.swing.JTextField idtransportista;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField op;
    private javax.swing.JTextField remision;
    private javax.swing.JTextField remisioncomer;
    private javax.swing.JTextField tipounidad;
    private javax.swing.JComboBox turno;
    // End of variables declaration//GEN-END:variables

}
