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
public class datos_remisiones_kilogramos extends javax.swing.JDialog {
    Connection connj;
    private Properties conf;
    ResultSet rs0=null,rs1=null,rs2=null;
    DefaultTableModel modelot1=null;
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    SimpleDateFormat fechasin = new SimpleDateFormat("yyyyMMdd");
    String terminacion="PESOS";
    Double valormoneda=1.0;
    Double tasaiva=0.0,retiva=0.0,retisr=0.0;
    String usuariorem="";
    String rfcempresa="";
    int permitecambios=1;
    Calendar calendarvencimiento = new GregorianCalendar();
    X509Certificate certificadoempresa=null;
    PrivateKey keyempresa=null;
    String passllave="";
    int cambia=1;
    Double importedxc=0.0,ivadxc=0.0;
    Double importenotadxc=0.0,ivanotadxc=0.0;
    Double saldodxc=0.0;
    String clienteprincipal="";
    String valor_privilegio="1";


    /** Creates new form datos_usuarios */
    public datos_remisiones_kilogramos(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
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

                    if (c == 0) { //obtiene los datos de la OP
                        String claveop=""+model.getValueAt(f, c);
                        if(!claveop.equals("") && !claveop.equals("null")){
                            rs0=null;
                            try{
                                String senSQL="SELECT ops.*,articulos.articulo,articulos.kg,articulos.claveresistencia,articulos.diseno,agentes.id_clientes as clientecomercial,agentes.comercializador,clientes.id_clientes FROM (ops LEFT JOIN (clientes LEFT JOIN agentes ON clientes.id_agente=agentes.id_agente) ON ops.id_clientes=clientes.id_clientes) LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo WHERE (ops.estatus<>'Can' and ops.op='"+claveop+"' and ops.id_clientes='"+clavecliente.getText()+"')";
                                rs0=conexion.consulta(senSQL,connj);
                                int error=0;
                                if(rs0.next()){
                                    model.setValueAt(rs0.getString("clavearticulo"), f, 1);
                                    model.setValueAt(rs0.getString("articulo"), f, 2);
                                    model.setValueAt(rs0.getString("claveresistencia"), f, 3);
                                    model.setValueAt(rs0.getDouble("kg"), f, 7);

                                    if(rs0.getString("comercializador").contains("Si")){
                                        clienteprincipal=rs0.getString("clientecomercial");
                                    }else{
                                        clienteprincipal=clavecliente.getText();
                                    }
                                    /*if(!rs0.getString("diseno").toUpperCase().equals("LAMINA")){
                                        JOptionPane.showMessageDialog(null,"EL DISEÑO DEL ARTICULO NO ES LAMINA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                        error=1;
                                    }*/
                                }
                                else{
                                    JOptionPane.showMessageDialog(null,"LA OP NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                    error=1;
                                }
                                if(error==1){
                                    model.setValueAt("", f, 0);
                                    model.setValueAt("", f, 1);
                                    model.setValueAt("", f, 2);
                                    model.setValueAt("", f, 3);
                                    model.setValueAt(0, f, 4);
                                    model.setValueAt(0, f, 5);
                                    model.setValueAt(0.0, f, 6);
                                    model.setValueAt(0.0, f, 7);
                                }
                                if(rs0!=null) {  rs0.close(); }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                        }
                        sumatoria();
                    }
                    if (c == 4) {
                        int cant=(Integer)model.getValueAt(f, c);
                        if(cant>0)
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
                String senSQL="SELECT remisiones.remision,remisiones.fechahora,remisiones.clave_transportista,remisiones.id_clientes,remisiones.kgrealbascula,transportistas.nombre as tnombre,clientes.nombre as cnombre,remisiones_detalle.op,remisiones_detalle.clavearticulo,remisiones_detalle.cantidadpzas,remisiones_detalle.kg_real_bascula,remisiones_detalle.importe_real_bascula,articulos.articulo,articulos.claveresistencia,articulos.kg FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes) LEFT JOIN transportistas ON remisiones.clave_transportista=transportistas.clave_transportista) INNER JOIN (remisiones_detalle LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) ON remisiones.remision=remisiones_detalle.remision WHERE remisiones.remision='"+clavemodifica+"'";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    remision.setText(rs0.getString("remision"));
                    Date fechahora=rs0.getTimestamp("fechahora");
                    fecha.setDate(fechahora);
                    hora.setText(horacorta.format(fechahora));
                    idtransportista.setText(rs0.getString("clave_transportista"));
                    nombre_trans.setText(rs0.getString("tnombre"));
                    nombre_trans.setText(rs0.getString("tnombre"));
                    clavecliente.setText(rs0.getString("id_clientes"));
                    nombrecliente.setText(rs0.getString("cnombre"));
                    totalkg_todo.setText(fijo0decimales.format(rs0.getDouble("kgrealbascula")));

                    Object datos[]={rs0.getString("op"),rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),rs0.getInt("cantidadpzas"),rs0.getInt("kg_real_bascula"),rs0.getDouble("importe_real_bascula"),rs0.getDouble("kg")};
                    modelot1.addRow(datos);

                }
                if(rs0!=null) {     rs0.close();   }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            sumatoria2();
        }
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(200);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(10);

        Tabladatos.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        
    }
    public void sumatoria2(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{
            //vuelve a realizar las sumas
            int sumacantidad=0;
            int sumakg=0;
            Double sumaimp=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                sumacantidad+=(Integer) Tabladatos.getValueAt(i, 4);
                sumakg+=(Integer) Tabladatos.getValueAt(i, 5);
                sumaimp+=(Double) Tabladatos.getValueAt(i, 6);
            }
            txttotalcant.setText(estandarentero.format(sumacantidad));
            txttotalkg.setText(estandarentero.format(sumakg));
            txttotalimp.setText(moneda2decimales.format(sumaimp));
        }
    }
    public void sumatoria(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{
            Double suma_kg_par=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                if((Integer)Tabladatos.getValueAt(i, 4)>0)
                    suma_kg_par+=Double.parseDouble(Integer.toString((Integer)Tabladatos.getValueAt(i, 4))) * (Double) Tabladatos.getValueAt(i, 7);
            }

            //ajusta los datos de los kilogramos por partida
            Double kg_total_bascula=Double.parseDouble(totalkg_todo.getText());
            for(int i=0;i<=(filas-1);i=i+1){
                if((Integer)Tabladatos.getValueAt(i, 4)>0){
                    Double suma_kg_sola=Double.parseDouble(Integer.toString((Integer)Tabladatos.getValueAt(i, 4))) * (Double) Tabladatos.getValueAt(i, 7);
                    Double porcen=suma_kg_sola/suma_kg_par;
                    int nuevos_kg_sola=Integer.parseInt(fijo0decimales.format(porcen*kg_total_bascula));
                    Tabladatos.setValueAt(nuevos_kg_sola,i, 5);
                    Tabladatos.setValueAt(nuevos_kg_sola*Double.parseDouble(preciokg.getText()),i, 6);
                }
            }

            //vuelve a realizar las sumas
            int sumacantidad=0;
            int sumakg=0;
            Double sumaimp=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                sumacantidad+=(Integer) Tabladatos.getValueAt(i, 4);
                sumakg+=(Integer) Tabladatos.getValueAt(i, 5);
                sumaimp+=(Double) Tabladatos.getValueAt(i, 6);
            }
            txttotalcant.setText(estandarentero.format(sumacantidad));
            txttotalkg.setText(estandarentero.format(sumakg));
            txttotalimp.setText(moneda2decimales.format(sumaimp));
            
        }

    }

    public class DoubleRenderer extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos

            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                String textpunto = ""+value;

                rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

                rend.setText( text );
                return rend;
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
            if(rs0!=null) {  rs0.close();   }
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
            if(rs2!=null) {  rs2.close();  }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public int insertarPartida(String opf,String artf,int cantrem,Double preciounitariopartida,Double kgpartida,Double kg_partida,Double imp_partida){
        int resultadoguarda=1; //1 para decir que no se guardo nada
        //busca con OP y articulo
        rs0=null;
        try{
            String senSQL="SELECT almacen_pt.*,articulos.kg,articulos.m2 FROM almacen_pt LEFT JOIN articulos ON almacen_pt.clavearticulo=articulos.clavearticulo  WHERE (almacen_pt.op='"+opf+"' AND almacen_pt.clavearticulo='"+artf+"')";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                int cantidadinventario=rs0.getInt("cantidadpzas");
                if(cantrem<=cantidadinventario){
                    senSQL="INSERT INTO remisiones_detalle(remision, estatus, op, clavearticulo, cantidadpzas, pzaspaquete,kg_real_bascula,importe_real_bascula) VALUES ('"+remision.getText()+"', 'Act', '"+opf+"', '"+artf+"', '"+cantrem+"', '"+rs0.getInt("pzaspaquete")+"', '"+kg_partida+"', '"+imp_partida+"');";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    //iserta una partida en salida de almacen PT
                    senSQL="INSERT INTO salidas_almacen_pt( estatus, fecha, remision, op, clavearticulo, cantidadpzas, pzaspaquete, almacen) VALUES ('Act', '"+fechainsertarhora.format(new Date())+"', '"+remision.getText()+"', '"+opf+"', '"+artf+"', '"+cantrem+"', '"+rs0.getInt("pzaspaquete")+"', '"+rs0.getString("almacen")+"');";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    //actualiza el almacen de PT
                    senSQL = "UPDATE almacen_pt SET cantidadpzas='"+(cantidadinventario-cantrem)+"' WHERE (op='"+opf+"' AND clavearticulo='"+artf+"');";
                    conexion.modificamov_p(senSQL, connj,valor_privilegio);
                    //actualiza datos del articulo
                    senSQL="UPDATE articulos SET fechaultima='"+fechainsertar.format(new Date())+"', cantidadultima='"+cantrem+"',kg='"+kgpartida+"' WHERE clavearticulo='"+artf+"';";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    senSQL="UPDATE ops SET preciounitario='"+preciounitariopartida+"' WHERE op='"+opf+"';";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    ////////////////actualiza la orden de produccion
                    actualizaOP(opf);

                    resultadoguarda=0;
                }
            }
            if(rs0!=null) {  rs0.close();      }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        return resultadoguarda;
    }
    public void imprimirremision(){
        int remision_kilos=0;
        //revisa si la remisiones por kilos o no
        rs1=null;
        try{
            String senSQL="SELECT remisiones.* FROM remisiones WHERE (remisiones.estatus<>'Can' AND remisiones.remision='"+remision.getText()+"');";
            rs1=conexion.consulta(senSQL,connj);
            if(rs1.next()){
                remision_kilos=rs1.getInt("kgrealbascula");
            }
            if(rs1!=null) {  rs1.close();      }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

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
                if(remision_kilos>0){
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_remision_kg.jasper"));
                }else{
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_remision.jasper"));
                }
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
        txttotalkg = new javax.swing.JTextField();
        lbiva = new javax.swing.JLabel();
        txttotalimp = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
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
        totalkg_todo = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        preciokg = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        idtransportista = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        nombre_trans = new javax.swing.JTextField();
        txttotalcant = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_remisiones_kilogramos.class);
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
                "OP", "Clave Art.", "Articulo", "Resis.", "Cantidad", "KG", "Importe", "kg_partida"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false
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

        txttotalkg.setBackground(resourceMap.getColor("txttotalkg.background")); // NOI18N
        txttotalkg.setEditable(false);
        txttotalkg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txttotalkg.setText(resourceMap.getString("txttotalkg.text")); // NOI18N
        txttotalkg.setFocusable(false);
        txttotalkg.setName("txttotalkg"); // NOI18N

        lbiva.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbiva.setText(resourceMap.getString("lbiva.text")); // NOI18N
        lbiva.setName("lbiva"); // NOI18N

        txttotalimp.setBackground(resourceMap.getColor("txttotalimp.background")); // NOI18N
        txttotalimp.setEditable(false);
        txttotalimp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txttotalimp.setText(resourceMap.getString("txttotalimp.text")); // NOI18N
        txttotalimp.setFocusable(false);
        txttotalimp.setName("txttotalimp"); // NOI18N

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

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

        nombrecliente.setEditable(false);
        nombrecliente.setText(resourceMap.getString("nombrecliente.text")); // NOI18N
        nombrecliente.setFocusable(false);
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

        fecha.setEnabled(false);
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

        totalkg_todo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.###"))));
        totalkg_todo.setName("totalkg_todo"); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        preciokg.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        preciokg.setName("preciokg"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        idtransportista.setEditable(false);
        idtransportista.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idtransportista.setText(resourceMap.getString("idtransportista.text")); // NOI18N
        idtransportista.setFocusable(false);
        idtransportista.setName("idtransportista"); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        nombre_trans.setEditable(false);
        nombre_trans.setText(resourceMap.getString("nombre_trans.text")); // NOI18N
        nombre_trans.setFocusable(false);
        nombre_trans.setName("nombre_trans"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idtransportista, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(totalkg_todo, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(preciokg, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nombre_trans, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(totalkg_todo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(preciokg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(idtransportista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(nombre_trans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnremove, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(410, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txttotalcant, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(410, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbiva, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txttotalkg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttotalimp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttotalcant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttotalkg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbiva))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttotalimp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
      int filas=Tabladatos.getRowCount();
      if(remision.getText().equals("")||hora.getText().equals("")||fecha.getDate()==null||clavecliente.getText().equals("")||idtransportista.getText().equals("")||totalkg_todo.getText().equals("")||preciokg.getText().equals("") || filas<=0){
            String err="VERIFICA HAY CAMPOS VACIOS";
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
      }else{
          if (Tabladatos.getCellEditor() != null) {//finaliza el editor
                Tabladatos.getCellEditor().stopCellEditing();
          }
          sumatoria();
        //revisar existencia en almacen
            int errorinventario=0;
            int errorcamposvacios=0;
            //ajusta los datos de los kilogramos por partida
            for(int i=0;i<=(filas-1);i=i+1){
                //busca inventario
                int canti=(Integer)Tabladatos.getValueAt(i, 4);
                if(canti<=0){
                    errorcamposvacios=1;
                }else{
                    errorinventario=revisainventario(""+Tabladatos.getValueAt(i, 0),""+Tabladatos.getValueAt(i, 1),canti);
                }

                if(errorinventario==1 || errorinventario==2){
                    String err="ERROR GENERAL";
                    if(errorinventario==1)
                        err="LA OP "+Tabladatos.getValueAt(i, 0)+" NO FUE ENCONTRADA EN ALMACEN PT";
                    if(errorinventario==2)
                        err="LA CANTIDAD DE LA OP "+Tabladatos.getValueAt(i, 0)+" ES MAYOR QUE LA EXISTENTE EN ALMACEN DE PT";
                    JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }

            if(errorcamposvacios==1){
                 JOptionPane.showMessageDialog(this, "CAMPOS VACIOS EN LA TABLA DE DETALLES", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{

                    if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        //guarda la partida principal
                        //funcion para guardar los datos de la partida
                        String senSQL="INSERT INTO remisiones(remision, remision_comercializador, estatus, fechahora, fechareal, turno, id_clientes, ordencompra, clave_transportista,id_op,clavearticulo,cantidad,um,kgrealbascula, preciounitario, importeflete, observaciones, facturado,usuario) VALUES ('"+remision.getText()+"', '', 'Act', '"+fechainsertar.format(fecha.getDate())+" "+hora.getText()+"', '"+fechainsertarhora.format(new Date())+"', '1', '"+clienteprincipal+"', '"+supedido.getText()+"', '"+idtransportista.getText()+"', '"+Tabladatos.getValueAt(0, 0)+"', '"+Tabladatos.getValueAt(0, 1)+"', '0','kg', '"+totalkg_todo.getText()+"', '0', '0', '', false,'"+usuariorem+"');";
                        conexion.modifica_p(senSQL,connj,valor_privilegio);

                        //guarda los detalles de la partida
                        for(int i=0;i<=(filas-1);i=i+1){
                            Double pu=(Double)Tabladatos.getValueAt(i, 6)/(Integer)Tabladatos.getValueAt(i, 4);
                            Double kgu=Double.parseDouble(""+Tabladatos.getValueAt(i, 5))/Double.parseDouble(""+Tabladatos.getValueAt(i, 4));
                            Double kg_partida=Double.parseDouble(""+Tabladatos.getValueAt(i, 5));
                            Double imp_partida=Double.parseDouble(""+Tabladatos.getValueAt(i, 6));
                            int res=insertarPartida(""+Tabladatos.getValueAt(i, 0),""+Tabladatos.getValueAt(i, 1),(Integer)Tabladatos.getValueAt(i, 4),pu,kgu,kg_partida,imp_partida);
                        }
                        imprimirremision();
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
            if(remision.getText().equals("")||clavecliente.getText().equals("")||totalkg_todo.getText().equals("")||Double.parseDouble(totalkg_todo.getText())==0.0||preciokg.getText().equals("")||Double.parseDouble(preciokg.getText())==0.0){
                JOptionPane.showMessageDialog(this, "HAY CAMPOS VACIOS LLENA LOS PRIMEROS CAMPOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                //deshabilita los botones para que no realizen modificaciones
                btnclientes.setEnabled(false);
                Object datos[]={"","","","",0,0,0.0,0.0};
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        busca_transportistas busca_transportistas = new busca_transportistas(null,true,connj,"");
        busca_transportistas.setLocationRelativeTo(this);
        busca_transportistas.setVisible(true);
        idtransportista.setText(busca_transportistas.getText());
        busca_transportistas=null;
        if(idtransportista.getText().equals("")){
            nombre_trans.setText("");
        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM transportistas WHERE clave_transportista='"+idtransportista.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    nombre_trans.setText(rs0.getString("nombre"));
                }
                if(rs0!=null) {    rs0.close();   }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JTextField clavecliente;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField hora;
    private javax.swing.JTextField idtransportista;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbiva;
    private javax.swing.JTextField nombre_trans;
    private javax.swing.JTextField nombrecliente;
    private javax.swing.JFormattedTextField preciokg;
    private javax.swing.JTextField remision;
    private javax.swing.JTextField supedido;
    private javax.swing.JFormattedTextField totalkg_todo;
    private javax.swing.JTextField txttotalcant;
    private javax.swing.JTextField txttotalimp;
    private javax.swing.JTextField txttotalkg;
    // End of variables declaration//GEN-END:variables

}
