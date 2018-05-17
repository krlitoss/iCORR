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
import java.io.File;
import java.io.FileInputStream;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public class datos_facturas_varios extends javax.swing.JDialog {

    Connection connj;
    private Properties conf;
    ResultSet rs0 = null, rs1 = null;
    DefaultTableModel modelot1 = null;
    DecimalFormat fijo2decimales = new DecimalFormat("######0.00");
    DecimalFormat fijo5decimales = new DecimalFormat("######0.00000");
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat fijo0decimales = new DecimalFormat("######0");
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    SimpleDateFormat fechasin = new SimpleDateFormat("yyyyMMdd");
    String terminacion = "PESOS";
    Double valormoneda = 1.0;
    Double tasaiva = 0.0, retiva = 0.0, retisr = 0.0;
    String user = "";
    String rfcempresa = "";
    Calendar calendarvencimiento = new GregorianCalendar();
    X509Certificate certificadoempresa = null;
    PrivateKey keyempresa = null;
    String passllave = "";
    String modifica = "1";
    String valor_privilegio = "1";
    int permitecambios = 1;

    /**
     * Creates new form datos_usuarios
     */
    public datos_facturas_varios(java.awt.Frame parent, boolean modal, Connection conn, String clavemodifica, String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj = conn;
        valor_privilegio = valor_privilegio_t;
        conf = conexion.archivoInicial();
        fecha.setDate(new Date());
        hora.setText(horacorta.format(new Date()));
        modelot1 = (DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);

        //formaspagos();
        //metodospagos();
        //usocomprobantes();
        ajusteTabladatos();
        buscaMoneda("1");
        impuestosdatos("1");
        consultamodifica(clavemodifica);
        user = conf.getProperty("UserID");
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

        modelot1.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent te) {
                try {
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla

                    if (c == 0 && modifica.equals("1")) {
                        String clavefac = "" + model.getValueAt(f, c);
                        if (!clavefac.equals("") && !clavefac.equals("null")) {
                            /*                              rs0=null;
                                try{
                                    String senSQL="SELECT * FROM remisiones WHERE remision='"+clavefac+"'";
                                    rs0=conexion.consulta(senSQL,connj);
                                    if(rs0.next()){
                                        modifica="0";
                                        model.setValueAt("", f, 0);
                                        modifica="1";
                                        JOptionPane.showMessageDialog(null,"EL NUMERO DE REMISION YA EXISTE EN REMISIONES ARTICULOS","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                    }
                                    if(rs0!=null) {rs0.close();}
                                } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                             */
                        } else {
                            modifica = "0";
                            model.setValueAt("", f, 0);
                            modifica = "1";
                        }
                    }

                    if (c == 1) {
                        if (model.getValueAt(f, c) != null) {
                            Double cant = (Double) model.getValueAt(f, c);
                            if (cant > 0) {
                                model.setValueAt(cant * (Double) model.getValueAt(f, 7), f, 8);
                            } else {
                                model.setValueAt(0.0, f, 8);
                            }
                        } else {
                            model.setValueAt(0.0, f, 8);
                        }
                        sumatoria();
                    }
                    if (c == 7) {
                        if (model.getValueAt(f, c) != null) {
                            Double preciounirem = (Double) model.getValueAt(f, c);
                            if (preciounirem > 0.0) {
                                model.setValueAt((Double) model.getValueAt(f, c) * (Double) model.getValueAt(f, 1), f, 8);

                            } else {
                                model.setValueAt(0.0, f, 8);
                            }
                        } else {
                            model.setValueAt(0.0, f, 8);
                        }
                        sumatoria();
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "NO SE PUEDE CALCULAR EL IMPORTE" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

    }

    public void salir() {
        if (connj != null) {
            connj = null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }

    public void consultamodifica(String clavemodifica) {
        if (clavemodifica.equals("")) {
            /**
             * si la clave esta vacia significa que es un registro nuevo
             */
        } else {
            btnaceptar.setEnabled(false);
            btnadd.setEnabled(false);
            btnremove.setEnabled(false);
            rs0 = null;
            try {
                String senSQL = "SELECT facturas.*, folios.aprobacion, folios.anoaprobacion, monedas.descripcion AS desmoneda, clientes.rfc, clientes.nombre, clientes.dias, lugaresemision.municipio, lugaresemision.estado, impuestos.iva AS ivaimpuestos, impuestos.ivaretenido, impuestos.isrretenido, facturas_detalle.remision, facturas_detalle.id_op, facturas_detalle.clavearticulo, facturas_detalle.descripcion, facturas_detalle.clave_prodserv, ( CASE WHEN facturas_detalle.um = 'millar' OR facturas_detalle.um = 'MILLAR' THEN ( facturas_detalle.cantidad / 1000 ) ELSE facturas_detalle.cantidad END ) AS cantidad, facturas_detalle.um, facturas_detalle.clave_unidadmedida, ( CASE WHEN facturas_detalle.um = 'millar' OR facturas_detalle.um = 'MILLAR' THEN ( facturas_detalle.preciounitario * 1000 ) ELSE facturas_detalle.preciounitario END ) AS preciounitario, facturas_detalle.subtotal AS subtotalpartida FROM ((((( facturas LEFT JOIN folios ON facturas.id_folios = folios.id_folio ) LEFT JOIN monedas ON facturas.id_moneda = monedas.id_moneda ) LEFT JOIN clientes ON facturas.id_clientes = clientes.id_clientes ) LEFT JOIN lugaresemision ON facturas.id_lugaremision = lugaresemision.id_lugaremision ) LEFT JOIN impuestos ON facturas.id_impuestos = impuestos.id_impuestos ) INNER JOIN facturas_detalle ON facturas.factura_serie = facturas_detalle.factura_serie WHERE facturas.factura_serie = '" + clavemodifica + "'";
                rs0 = conexion.consulta(senSQL, connj);
                while (rs0.next()) {
                    factura.setText(rs0.getString("factura"));
                    serie.setText(rs0.getString("serie"));
                    Date fechahora = rs0.getTimestamp("fecha");
                    fecha.setDate(fechahora);
                    hora.setText(horacorta.format(fechahora));
                    idfolio.setText(rs0.getString("id_folios"));
                    aprobacion.setText(rs0.getString("aprobacion"));
                    idmoneda.setText(rs0.getString("id_moneda"));
                    moneda.setText(rs0.getString("desmoneda"));
                    clavecliente.setText(rs0.getString("id_clientes"));
                    rfc.setText(rs0.getString("rfc"));
                    nombrecliente.setText(rs0.getString("nombre"));
                    diascred.setText(rs0.getString("dias"));
                    idlugar.setText(rs0.getString("id_lugaremision"));
                    lugar.setText(rs0.getString("municipio") + ", " + rs0.getString("estado"));
                    impuestos.setText(rs0.getString("id_impuestos"));
                    tasaiva = rs0.getDouble("ivaimpuestos");
                    retiva = rs0.getDouble("ivaretenido");
                    retisr = rs0.getDouble("isrretenido");
                    lbiva.setText("IVA " + fijo0decimales.format(tasaiva) + "% :");
                    oc.setText(rs0.getString("ordencompra"));
                    notas.setText(rs0.getString("observaciones"));
                    Double sub = rs0.getDouble("subtotal");
                    subtotal.setText(moneda2decimales.format(sub));
                    importe.setText(moneda2decimales.format(sub));

                    //campos para CFDI 3.3
                    usocomprobantetxt.setText(rs0.getString("clave_usocomprobante"));
                    usocomprobantedes.setText(rs0.getString("usocomprobante"));
                    formapagotxt.setText(rs0.getString("clave_formapago"));
                    formapagodes.setText(rs0.getString("formapago"));
                    metodopagotxt.setText(rs0.getString("clave_metodopago"));
                    metodopagodes.setText(rs0.getString("metodopago"));

                    iva.setText(moneda2decimales.format(rs0.getDouble("iva")));
                    ivaretenido.setText(moneda2decimales.format(rs0.getDouble("ivaretenido")));
                    isrretenido.setText(moneda2decimales.format(rs0.getDouble("isrretenido")));
                    total.setText(moneda2decimales.format(rs0.getDouble("total")));
                    String claveart = rs0.getString("clavearticulo");
                    if (!claveart.contains("FLT")) {
                        Object datos[] = {rs0.getString("remision"), rs0.getDouble("cantidad"), rs0.getString("clavearticulo"), rs0.getString("descripcion"), rs0.getString("clave_prodserv"), rs0.getString("um"), rs0.getString("clave_unidadmedida"), rs0.getDouble("preciounitario"), rs0.getDouble("subtotalpartida")};
                        modelot1.addRow(datos);
                    }

                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(220);
//        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(60);
//        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(50);
//        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(50);
//        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(80);
//        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(80);

        Tabladatos.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
//        Tabladatos.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer());
//        Tabladatos.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer());
//        Tabladatos.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
//        Tabladatos.getColumnModel().getColumn(7).setCellRenderer(new DoubleRenderer());
//        Tabladatos.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
    }

    public void sumatoria() {
        int filas = Tabladatos.getRowCount();
        if (filas <= 0) {
        } else {
            Double sumaimportes = 0.0;
            for (int i = 0; i <= (filas - 1); i = i + 1) {
                Double valimporte = (Double) Tabladatos.getValueAt(i, 8);///obtiene el importe
                sumaimportes = sumaimportes + valimporte;
            }
            subtotal.setText(fijo2decimales.format(sumaimportes));
            importe.setText(fijo2decimales.format(Double.parseDouble(subtotal.getText())));
            iva.setText(fijo2decimales.format((tasaiva / 100) * Double.parseDouble(importe.getText())));
            if (retiva > 0) {
                ivaretenido.setText(fijo2decimales.format((retiva / 100) * Double.parseDouble(importe.getText())));
            }
            if (retisr > 0) {
                isrretenido.setText(fijo2decimales.format((retisr / 100) * Double.parseDouble(importe.getText())));
            }
            total.setText(fijo2decimales.format(Double.parseDouble(importe.getText()) + Double.parseDouble(iva.getText()) - Double.parseDouble(ivaretenido.getText()) - Double.parseDouble(isrretenido.getText())));
        }

    }

    public void buscaMoneda(String clave) {
        rs0 = null;
        try {
            String senSQL = "SELECT * FROM monedas WHERE id_moneda='" + clave + "'";
            rs0 = conexion.consulta(senSQL, connj);
            if (rs0.next()) {
                idmoneda.setText(clave);
                moneda.setText(rs0.getString("descripcion"));
                terminacion = rs0.getString("terminacion");
                valormoneda = rs0.getDouble("valor");
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void impuestosdatos(String claveimp) {
        impuestos.setText(claveimp);
        rs1 = null;
        try {
            String senSQL = "SELECT * FROM impuestos WHERE id_impuestos='" + claveimp + "'";
            rs1 = conexion.consulta(senSQL, connj);
            if (rs1.next()) {
                tasaiva = rs1.getDouble("iva");
                retiva = rs1.getDouble("ivaretenido");
                retisr = rs1.getDouble("isrretenido");
                lbiva.setText("IVA " + fijo0decimales.format(tasaiva) + "% :");
            }
            if (rs1 != null) {
                rs1.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public class DoubleRenderer extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel rend = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String text = "" + value;
            String textpunto = "" + value;

            if(column==7){
                    text = fijo5decimales.format( Double.parseDouble(""+value) );
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    int dotPos = textpunto.lastIndexOf(".")+1;
                    String partedecimal = textpunto.substring(dotPos);
                    if(partedecimal.length() > 5){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                }

            if(column==8 || column==1){
                    text = fijo2decimales.format( Double.parseDouble(""+value) );
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    int dotPos2 = textpunto.lastIndexOf(".")+1;
                    String partedecimal2 = textpunto.substring(dotPos2);
                    if(partedecimal2.length() > 2){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                }
            
            if (column == 0 || column == 2 || column == 3 || column == 4 || column == 5 || column == 6) {
                rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            }

            rend.setText(text);
            return rend;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        factura = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        serie = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        hora = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        idfolio = new javax.swing.JTextField();
        aprobacion = new javax.swing.JTextField();
        btnfolios = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        idmoneda = new javax.swing.JTextField();
        btnmonedas = new javax.swing.JButton();
        moneda = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        clavecliente = new javax.swing.JTextField();
        btnclientes = new javax.swing.JButton();
        rfc = new javax.swing.JTextField();
        nombrecliente = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        diascred = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        impuestos = new javax.swing.JTextField();
        btnimpuestos = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        btnremove = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        iva = new javax.swing.JTextField();
        lbiva = new javax.swing.JLabel();
        ivaretenido = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        isrretenido = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();
        subtotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        notas = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        importe = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        idlugar = new javax.swing.JTextField();
        btnlugaremision = new javax.swing.JButton();
        lugar = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        oc = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        ctapagotxt = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        usocomprobantedes = new javax.swing.JTextField();
        usocomprobantetxt = new javax.swing.JTextField();
        btnusocomprobante = new javax.swing.JButton();
        formapagotxt = new javax.swing.JTextField();
        metodopagotxt = new javax.swing.JTextField();
        formapagodes = new javax.swing.JTextField();
        metodopagodes = new javax.swing.JTextField();
        btnformapago = new javax.swing.JButton();
        btnmetodopago = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_facturas_varios.class);
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

        factura.setEditable(false);
        factura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        factura.setFocusable(false);
        factura.setName("factura"); // NOI18N

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
                .addGap(254, 254, 254)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        serie.setEditable(false);
        serie.setText(resourceMap.getString("serie.text")); // NOI18N
        serie.setFocusable(false);
        serie.setName("serie"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        fecha.setEnabled(false);
        fecha.setName("fecha"); // NOI18N

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        hora.setEditable(false);
        hora.setText(resourceMap.getString("hora.text")); // NOI18N
        hora.setFocusable(false);
        hora.setName("hora"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        idfolio.setEditable(false);
        idfolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idfolio.setText(resourceMap.getString("idfolio.text")); // NOI18N
        idfolio.setFocusable(false);
        idfolio.setName("idfolio"); // NOI18N

        aprobacion.setEditable(false);
        aprobacion.setText(resourceMap.getString("aprobacion.text")); // NOI18N
        aprobacion.setFocusable(false);
        aprobacion.setName("aprobacion"); // NOI18N

        btnfolios.setIcon(resourceMap.getIcon("btnfolios.icon")); // NOI18N
        btnfolios.setText(resourceMap.getString("btnfolios.text")); // NOI18N
        btnfolios.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnfolios.setName("btnfolios"); // NOI18N
        btnfolios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfoliosActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        idmoneda.setEditable(false);
        idmoneda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idmoneda.setText(resourceMap.getString("idmoneda.text")); // NOI18N
        idmoneda.setFocusable(false);
        idmoneda.setName("idmoneda"); // NOI18N

        btnmonedas.setIcon(resourceMap.getIcon("btnmonedas.icon")); // NOI18N
        btnmonedas.setText(resourceMap.getString("btnmonedas.text")); // NOI18N
        btnmonedas.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmonedas.setName("btnmonedas"); // NOI18N
        btnmonedas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmonedasActionPerformed(evt);
            }
        });

        moneda.setEditable(false);
        moneda.setText(resourceMap.getString("moneda.text")); // NOI18N
        moneda.setFocusable(false);
        moneda.setName("moneda"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        clavecliente.setEditable(false);
        clavecliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavecliente.setText(resourceMap.getString("clavecliente.text")); // NOI18N
        clavecliente.setFocusable(false);
        clavecliente.setName("clavecliente"); // NOI18N

        btnclientes.setIcon(resourceMap.getIcon("btnclientes.icon")); // NOI18N
        btnclientes.setText(resourceMap.getString("btnclientes.text")); // NOI18N
        btnclientes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnclientes.setName("btnclientes"); // NOI18N
        btnclientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnclientesActionPerformed(evt);
            }
        });

        rfc.setEditable(false);
        rfc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        rfc.setText(resourceMap.getString("rfc.text")); // NOI18N
        rfc.setFocusable(false);
        rfc.setName("rfc"); // NOI18N

        nombrecliente.setEditable(false);
        nombrecliente.setText(resourceMap.getString("nombrecliente.text")); // NOI18N
        nombrecliente.setFocusable(false);
        nombrecliente.setName("nombrecliente"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        diascred.setEditable(false);
        diascred.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        diascred.setText(resourceMap.getString("diascred.text")); // NOI18N
        diascred.setFocusable(false);
        diascred.setName("diascred"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        impuestos.setEditable(false);
        impuestos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        impuestos.setText(resourceMap.getString("impuestos.text")); // NOI18N
        impuestos.setFocusable(false);
        impuestos.setName("impuestos"); // NOI18N

        btnimpuestos.setIcon(resourceMap.getIcon("btnimpuestos.icon")); // NOI18N
        btnimpuestos.setText(resourceMap.getString("btnimpuestos.text")); // NOI18N
        btnimpuestos.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnimpuestos.setName("btnimpuestos"); // NOI18N
        btnimpuestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimpuestosActionPerformed(evt);
            }
        });

        jLabel11.setFont(resourceMap.getFont("jLabel11.font")); // NOI18N
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Remision", "Cantidad", "Clave", "Concepto", "Clave Producto", "UM", "Clave UM", "Precio Unitario", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, false, false, false, false, true, false
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
        Tabladatos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (Tabladatos.getColumnModel().getColumnCount() > 0) {
            Tabladatos.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title0")); // NOI18N
            Tabladatos.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title1")); // NOI18N
            Tabladatos.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title8")); // NOI18N
            Tabladatos.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title3")); // NOI18N
            Tabladatos.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title7")); // NOI18N
            Tabladatos.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title2")); // NOI18N
            Tabladatos.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title6")); // NOI18N
            Tabladatos.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title4")); // NOI18N
            Tabladatos.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title5")); // NOI18N
        }

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

        iva.setEditable(false);
        iva.setBackground(resourceMap.getColor("iva.background")); // NOI18N
        iva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        iva.setText(resourceMap.getString("iva.text")); // NOI18N
        iva.setFocusable(false);
        iva.setName("iva"); // NOI18N

        lbiva.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbiva.setText(resourceMap.getString("lbiva.text")); // NOI18N
        lbiva.setName("lbiva"); // NOI18N

        ivaretenido.setEditable(false);
        ivaretenido.setBackground(resourceMap.getColor("ivaretenido.background")); // NOI18N
        ivaretenido.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ivaretenido.setText(resourceMap.getString("ivaretenido.text")); // NOI18N
        ivaretenido.setFocusable(false);
        ivaretenido.setName("ivaretenido"); // NOI18N

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        isrretenido.setEditable(false);
        isrretenido.setBackground(resourceMap.getColor("isrretenido.background")); // NOI18N
        isrretenido.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        isrretenido.setText(resourceMap.getString("isrretenido.text")); // NOI18N
        isrretenido.setFocusable(false);
        isrretenido.setName("isrretenido"); // NOI18N

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        total.setEditable(false);
        total.setBackground(resourceMap.getColor("total.background")); // NOI18N
        total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        total.setText(resourceMap.getString("total.text")); // NOI18N
        total.setFocusable(false);
        total.setName("total"); // NOI18N

        subtotal.setEditable(false);
        subtotal.setBackground(resourceMap.getColor("subtotal.background")); // NOI18N
        subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        subtotal.setText(resourceMap.getString("subtotal.text")); // NOI18N
        subtotal.setFocusable(false);
        subtotal.setName("subtotal"); // NOI18N

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        notas.setColumns(20);
        notas.setFont(resourceMap.getFont("notas.font")); // NOI18N
        notas.setLineWrap(true);
        notas.setRows(4);
        notas.setName("notas"); // NOI18N
        jScrollPane2.setViewportView(notas);

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        importe.setEditable(false);
        importe.setBackground(resourceMap.getColor("importe.background")); // NOI18N
        importe.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        importe.setText(resourceMap.getString("importe.text")); // NOI18N
        importe.setFocusable(false);
        importe.setName("importe"); // NOI18N

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        idlugar.setEditable(false);
        idlugar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idlugar.setText(resourceMap.getString("idlugar.text")); // NOI18N
        idlugar.setFocusable(false);
        idlugar.setName("idlugar"); // NOI18N

        btnlugaremision.setIcon(resourceMap.getIcon("btnlugaremision.icon")); // NOI18N
        btnlugaremision.setText(resourceMap.getString("btnlugaremision.text")); // NOI18N
        btnlugaremision.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnlugaremision.setName("btnlugaremision"); // NOI18N
        btnlugaremision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlugaremisionActionPerformed(evt);
            }
        });

        lugar.setEditable(false);
        lugar.setText(resourceMap.getString("lugar.text")); // NOI18N
        lugar.setFocusable(false);
        lugar.setName("lugar"); // NOI18N

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        oc.setText(resourceMap.getString("oc.text")); // NOI18N
        oc.setName("oc"); // NOI18N

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        ctapagotxt.setText(resourceMap.getString("ctapagotxt.text")); // NOI18N
        ctapagotxt.setName("ctapagotxt"); // NOI18N

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        usocomprobantedes.setEditable(false);
        usocomprobantedes.setFont(resourceMap.getFont("usocomprobantedes.font")); // NOI18N
        usocomprobantedes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usocomprobantedes.setFocusable(false);
        usocomprobantedes.setName("usocomprobantedes"); // NOI18N

        usocomprobantetxt.setEditable(false);
        usocomprobantetxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usocomprobantetxt.setFocusable(false);
        usocomprobantetxt.setName("usocomprobantetxt"); // NOI18N

        btnusocomprobante.setIcon(resourceMap.getIcon("btnusocomprobante.icon")); // NOI18N
        btnusocomprobante.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnusocomprobante.setName("btnusocomprobante"); // NOI18N
        btnusocomprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnusocomprobanteActionPerformed(evt);
            }
        });

        formapagotxt.setEditable(false);
        formapagotxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        formapagotxt.setFocusable(false);
        formapagotxt.setName("formapagotxt"); // NOI18N

        metodopagotxt.setEditable(false);
        metodopagotxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        metodopagotxt.setFocusable(false);
        metodopagotxt.setName("metodopagotxt"); // NOI18N

        formapagodes.setEditable(false);
        formapagodes.setFont(resourceMap.getFont("formapagodes.font")); // NOI18N
        formapagodes.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        formapagodes.setFocusable(false);
        formapagodes.setName("formapagodes"); // NOI18N

        metodopagodes.setEditable(false);
        metodopagodes.setFont(resourceMap.getFont("metodopagodes.font")); // NOI18N
        metodopagodes.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        metodopagodes.setFocusable(false);
        metodopagodes.setName("metodopagodes"); // NOI18N

        btnformapago.setIcon(resourceMap.getIcon("btnformapago.icon")); // NOI18N
        btnformapago.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnformapago.setName("btnformapago"); // NOI18N
        btnformapago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnformapagoActionPerformed(evt);
            }
        });

        btnmetodopago.setIcon(resourceMap.getIcon("btnmetodopago.icon")); // NOI18N
        btnmetodopago.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmetodopago.setName("btnmetodopago"); // NOI18N
        btnmetodopago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmetodopagoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(metodopagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(formapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(metodopagodes, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                    .addComponent(formapagodes))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnformapago, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ctapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnmetodopago, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbiva, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(iva, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ivaretenido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(isrretenido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(total, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(impuestos)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnimpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(diascred, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idlugar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnlugaremision, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(clavecliente)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(factura, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(idfolio)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnfolios, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(aprobacion, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(serie, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(rfc)))
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(idmoneda)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnmonedas, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(hora, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(nombrecliente)
                            .addComponent(lugar)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(usocomprobantetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(usocomprobantedes, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(btnusocomprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnremove, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(oc))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(serie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(factura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(idfolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnfolios, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(aprobacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7)
                                .addComponent(idmoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnmonedas, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(clavecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(nombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(diascred, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(idlugar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(impuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnimpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnlugaremision, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lugar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(oc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnremove, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(usocomprobantedes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(usocomprobantetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnusocomprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(jLabel23)
                        .addComponent(formapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(formapagodes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(iva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbiva)
                        .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16)
                        .addComponent(ctapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnformapago, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ivaretenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(metodopagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(metodopagodes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnmetodopago, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(isrretenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        boolean con_inter = CFDIconexion.valida_con_internet();
        if (!con_inter) {
            JOptionPane.showMessageDialog(null, "NO SE PUEDE CONECTAR A INTERNET ", "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }

        if (!con_inter || formapagotxt.getText().toString().equals("")|| metodopagotxt.getText().toString().equals("")|| usocomprobantetxt.getText().toString().equals("")) {
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            if (Tabladatos.getCellEditor() != null) {//finaliza el editor
                Tabladatos.getCellEditor().stopCellEditing();
            }
            int filas = Tabladatos.getRowCount();
            int columnas = Tabladatos.getColumnCount();
            int camposvacios = 0;
            int nopartidas = 0;
            for (int i = 0; i <= (filas - 1); i = i + 1) {
                for (int j = 0; j <= (columnas - 1); j = j + 1) {
                    if (modelot1.getValueAt(i, j) == null || modelot1.getValueAt(i, j).equals("")) {
                        camposvacios = 1;
                    }
                    if (j == 1 || j == 7) {
                        Double cantidad = (Double) modelot1.getValueAt(i, j);
                        if (cantidad <= 0.0) {
                            camposvacios = 1;
                        }
                    }
                }
                nopartidas++;
            }
            /**
             * fin de revisar los campos vacios
             */
            if (camposvacios == 1 || filas <= 0) {
                JOptionPane.showMessageDialog(this, "LA TABLA DE DETALLE TIENE CAMPOS VACIOS", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            } else {
                sumatoria();
                if (JOptionPane.showConfirmDialog(this, "ESTA SEGURO QUE DESEA GUARDAR!!", " I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    //obtiene la fecha de vencimiento
                    try {
                        Date fechacreado = new Date();
                        calendarvencimiento.setTime(fechacreado); //gregorian hoy
                        int diasc = Integer.parseInt(diascred.getText());
                        if (diasc > 0) {
                            calendarvencimiento.add(Calendar.DATE, diasc);
                        }
                        Date fechaven = calendarvencimiento.getTime();
                        //obtiene el ultimo folio para la factura
                        int maxfolio = 1; //alfinal actualizaremos el folio de la factura
                        String seriefin = "";
                        rs1 = null;
                        String senSQL = "SELECT * FROM folios WHERE id_folio='" + idfolio.getText() + "'";
                        rs1 = conexion.consulta(senSQL, connj);
                        if (rs1.next()) {
                            maxfolio = rs1.getInt("folioactual");
                            seriefin = rs1.getString("serie");
                        }
                        if (rs1 != null) {
                            rs1.close();
                        }

                        //obtiene el importe con letra
                        sumatoria();
                        int dotPos = total.getText().lastIndexOf(".") + 1;
                        String partedecimal = total.getText().substring(dotPos);
                        String parteentero = total.getText().substring(0, (dotPos - 1));
                        String numeroletra = "(" + new numerosletras().convertirLetras(Integer.parseInt(parteentero)) + " " + moneda.getText() + " " + partedecimal + "/100 " + terminacion + ")";
                        if (parteentero.equals("1")) {
                            numeroletra = "(UN PESO " + partedecimal + "/100 " + terminacion + ")";
                        }
                        numeroletra = numeroletra.toUpperCase();
                        /**
                         * cambia la cantidad en letra a mayusculas
                         */

                        //guarda la factura
                        senSQL = "INSERT INTO facturas ( factura_serie, factura, serie, fecha, estatus, id_folios, id_moneda, id_clientes, id_lugaremision, id_impuestos, varios, ordencompra, valortipocambio, subtotal, iva, ieps, ivaretenido, isrretenido, total, importeletra, pagada, usuario, observaciones, cuentapago, clave_usocomprobante, clave_formapago, clave_metodopago, usocomprobante, formapago, metodopago ) VALUES ( '" + maxfolio + seriefin + "', '" + maxfolio + "', '" + seriefin + "', '" + fechainsertarhora.format(fechacreado) + "', '1', '" + idfolio.getText() + "', '" + idmoneda.getText() + "', '" + clavecliente.getText() + "', '" + idlugar.getText() + "', '" + impuestos.getText() + "', '1', '" + oc.getText() + "', '" + valormoneda + "', '" + importe.getText() + "', '" + iva.getText() + "', '0', '" + ivaretenido.getText() + "', '" + isrretenido.getText() + "', '" + total.getText() + "', '" + numeroletra + "', 'FALSE', '" + user + "', '" + notas.getText() + "', '" + ctapagotxt.getText() + "', '" + usocomprobantetxt.getText() + "', '" + formapagotxt.getText() + "', '" + metodopagotxt.getText() + "', '" + usocomprobantedes.getText() + "', '" + formapagodes.getText() + "',  '" + metodopagodes.getText() + "' );";
                        conexion.modificamov_p(senSQL, connj, valor_privilegio);

                        senSQL = "INSERT INTO docxcob(factura_serie, fechaemision, fechavencimiento, importe, estatus, id_clientes) VALUES ('" + maxfolio + seriefin + "', '" + fechainsertarhora.format(fechacreado) + "', '" + fechainsertarhora.format(fechaven) + "', '" + total.getText() + "', 'Act', '" + clavecliente.getText() + "');";
                        conexion.modificamov_p(senSQL, connj, valor_privilegio);
                        for (int i = 0; i <= (filas - 1); i = i + 1) {

                            //guarda las partidas
                            String rm = "" + modelot1.getValueAt(i, 0);
                            if (rm.equals("null")) {
                                rm = "";
                            }
                            senSQL = "INSERT INTO facturas_detalle ( factura_serie, estatus, factura, serie, remision, id_op, clavearticulo, descripcion, cantidad, um, preciounitario, subtotal, clave_unidadmedida, clave_prodserv ) VALUES ( '" + maxfolio + seriefin + "', '1', '" + maxfolio + "', '" + seriefin + "', '" + rm + "', '0', '" + modelot1.getValueAt(i, 2) + "', '" + modelot1.getValueAt(i, 3) + "', '" + modelot1.getValueAt(i, 1) + "', '" + modelot1.getValueAt(i, 5) + "', '" + modelot1.getValueAt(i, 7) + "', '" + modelot1.getValueAt(i, 8) + "', '" + modelot1.getValueAt(i, 6) + "', '" + modelot1.getValueAt(i, 4) + "' );";
                            conexion.modificamov_p(senSQL, connj, valor_privilegio);

                        }

                        //actualiza los folios
                        String senSQLmov = "UPDATE folios SET folioactual='" + (maxfolio + 1) + "' WHERE id_folio='" + idfolio.getText() + "';";
                        conexion.modificamov_p(senSQLmov, connj, valor_privilegio);

                        //nombra el archivo xml
                        String filePathuser = System.getProperty("user.home") + "/" + rfcempresa + "_" + (maxfolio + seriefin) + "_" + fechasin.format(fechacreado) + ".xml";
                        String filePath = conf.getProperty("Rutaxml") + "/" + rfcempresa + "_" + (maxfolio + seriefin) + "_" + fechasin.format(fechacreado) + ".xml";
                        File rutaxmlsave = new File(conf.getProperty("Rutaxml") + "dfd");
                        if (!rutaxmlsave.exists()) {
                            filePath = filePathuser;
                        }

                        // Nueva funcion para version 33
                        CfdiF33 cfdi = CFDFactory33.createComprobante(connj, (maxfolio + seriefin));

                        //temporal datos de certificado y llave
//                        LbsCrypt crypt = new LbsCrypt(new FileInputStream("ACO560518KW7.key"), "12345678a", new FileInputStream("ACO560518KW7.cer")); pruebas de factura
                        LbsCrypt crypt = new LbsCrypt(new FileInputStream("SKA100902AK8.key"), "SKARTON2016", new FileInputStream("SKA100902AK8.cer"));
                        cfdi.sellar(crypt);
                        cfdi.timbrarFinkok("sistemas@skarton.com.mx", "Sistemas2011*");
                        //Hay que tener cuidado a partir de esta linea ya que el documento se encuentra timbrado desde este momento cualquier problema al guardar no importa el CFDI ya estara registrado en el SAT
                        cfdi.saveXML(filePath);

                        //Obtiene uuid
                        String uuid = cfdi.get("tfd:TimbreFiscalDigital/UUID");

                        //inserta los datos en la base de datos
                        senSQLmov = "INSERT INTO xmlfinal(factura_serie, fecha, estatus, xmlfinal, cadenaoriginal,id_clientes,uuid,version) VALUES ('" + (maxfolio + seriefin) + "', '" + fechainsertarhora.format(fechacreado) + "', '1', '" + cfdi.toString() + "', '" + cfdi.getCadenaOriginal() + "','" + clavecliente.getText() + "','" + uuid + "','cfdi33');";
                        conexion.modifica_p(senSQLmov, connj, valor_privilegio);

                        //TE DA LAS OPCIONES
                        busca_CFD busca_CFD = new busca_CFD(null, true, connj, (maxfolio + seriefin), "fac");
                        busca_CFD.setLocationRelativeTo(this);
                        busca_CFD.setVisible(true);
                        busca_CFD = null;

                        salir();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR LA FACTURA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                    }
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

    private void btnfoliosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfoliosActionPerformed
        // TODO add your handling code here:
        busca_folios busca_folios = new busca_folios(null, true, connj, "", "F");
        busca_folios.setLocationRelativeTo(this);
        busca_folios.setVisible(true);
        String valor = busca_folios.getText();
        busca_folios = null;
        if (valor.equals("") || valor.equals("0")) {
            idfolio.setText("");
            aprobacion.setText("");
        } else {
            idfolio.setText(valor);//obtiene el valor seleccionado
            rs1 = null;
            try {
                String senSQL = "SELECT * FROM folios WHERE id_folio='" + idfolio.getText() + "'";
                rs1 = conexion.consulta(senSQL, connj);
                if (rs1.next()) {
                    aprobacion.setText(rs1.getString("aprobacion"));
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
}//GEN-LAST:event_btnfoliosActionPerformed

    private void btnmonedasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmonedasActionPerformed
        // TODO add your handling code here:
        busca_monedas busca_monedas = new busca_monedas(null, true, connj, "");
        busca_monedas.setLocationRelativeTo(this);
        busca_monedas.setVisible(true);
        idmoneda.setText(busca_monedas.getText());//obtiene el valor seleccionado
        busca_monedas = null;
        if (idmoneda.getText().equals("0")) {
            idmoneda.setText("");
            moneda.setText("");
        } else {
            buscaMoneda(idmoneda.getText());
        }
}//GEN-LAST:event_btnmonedasActionPerformed

    private void btnclientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnclientesActionPerformed
        // TODO add your handling code here:
        busca_clientes busca_clientes = new busca_clientes(null, true, connj, "");
        busca_clientes.setLocationRelativeTo(this);
        busca_clientes.setVisible(true);
        clavecliente.setText(busca_clientes.getText());//obtiene el valor seleccionado
        busca_clientes = null;
        if (clavecliente.getText().equals("")) {

        } else {
            rs0 = null;
            try {
                String senSQL = "SELECT * FROM clientes WHERE id_clientes='" + clavecliente.getText() + "'";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    rfc.setText(rs0.getString("rfc"));
                    nombrecliente.setText(rs0.getString("nombre"));
                    diascred.setText(rs0.getString("dias"));
                    ctapagotxt.setText(rs0.getString("ctapago"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

        }
}//GEN-LAST:event_btnclientesActionPerformed

    private void btnimpuestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimpuestosActionPerformed
        // TODO add your handling code here:
        busca_impuestos busca_impuestos = new busca_impuestos(null, true, connj, "");
        busca_impuestos.setLocationRelativeTo(this);
        busca_impuestos.setVisible(true);
        impuestos.setText(busca_impuestos.getText());//obtiene el valor seleccionado
        busca_impuestos = null;
        if (impuestos.getText().equals("")) {
            lbiva.setText("IVA:");
        } else {
            impuestosdatos(impuestos.getText());
        }
}//GEN-LAST:event_btnimpuestosActionPerformed
    //ESTABLECE CUANDO ELEIMINAMOS UNA FILA
    private void btnremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoveActionPerformed
        // TODO add your handling code here:
        int filano = Tabladatos.getSelectedRow();
        if (filano < 0) {
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            int respuesta = JOptionPane.showConfirmDialog(this, "ESTA SEGURO DE ELIMINAR LA FILA  " + (filano + 1) + " ?????", " L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                modelot1.removeRow(filano);
                sumatoria();
            } else {

            }
        }
}//GEN-LAST:event_btnremoveActionPerformed
    //ESTABLECE QUE CAMPOS SON EDITABLES ASI COMO SU CONFIGURACION
    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        // TODO add your handling code here:
        int filas = Tabladatos.getRowCount();
        if (filas < 20) {
            if (idfolio.getText().equals("") || idfolio.getText().equals("0") || idmoneda.getText().equals("") || idmoneda.getText().equals("0") || clavecliente.getText().equals("") || impuestos.getText().equals("") || impuestos.getText().equals("0") || idlugar.getText().equals("") || idlugar.getText().equals("0")|| usocomprobantetxt.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "HAY CAMPOS VACIOS LLENA LOS PRIMEROS CAMPOS", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            } else {
                //deshabilita los botones para que no realizen modificaciones
                btnfolios.setEnabled(false);
                btnmonedas.setEnabled(false);
                btnclientes.setEnabled(false);
                btnimpuestos.setEnabled(false);
                btnlugaremision.setEnabled(false);
                Object datos[] = {"", 0.0, "F7", "", "", "", "", 0.00000, 0.00};
                modelot1.addRow(datos);
            }
        } else {
            JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
}//GEN-LAST:event_btnaddActionPerformed
    //ACCEDEMOS A CONFIGURACION PARA INSERTAR DATOS DE ARTICULOS DE OTRA TABLA
    private void TabladatosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_F7) {
            int filano = Tabladatos.getSelectedRow();
            int colno = Tabladatos.getSelectedColumn();

            if (Tabladatos.getCellEditor() != null) {
                Tabladatos.getCellEditor().stopCellEditing();
            }

            if (colno == 2) { //columna de la clave del articulo
                permitecambios = 2;
                busca_articulos_varios busca_articulos_varios = new busca_articulos_varios(null, true, connj, "", clavecliente.getText());
                busca_articulos_varios.setLocationRelativeTo(this);
                busca_articulos_varios.setVisible(true);
                Tabladatos.setValueAt(busca_articulos_varios.getText(), filano, colno);
                String valorfiltro = (String) Tabladatos.getValueAt(filano, colno);
                busca_articulos_varios = null;

                if (valorfiltro.equals("")) {
                    Tabladatos.setValueAt(0.0, filano, 2);
                    Tabladatos.setValueAt(0.0, filano, 3);
                    Tabladatos.setValueAt(0.0, filano, 4);

                } else {

                    rs0 = null;
                    try {
                        String senSQL = "SELECT * FROM articulos_varios WHERE articulos_varios.obsoleto = 'No' AND articulos_varios.clavearticulo = '" + valorfiltro + "' ORDER BY articulos_varios.id_articulo ASC;";
                        rs0 = conexion.consulta(senSQL, connj);
                        if (rs0.next()) {

                            Tabladatos.setValueAt(rs0.getString("clavearticulo"), filano, 2);
                            Tabladatos.setValueAt(rs0.getString("articulo"), filano, 3);
                            Tabladatos.setValueAt(rs0.getString("clave_prodserv"), filano, 4);
                            Tabladatos.setValueAt(rs0.getString("um"), filano, 5);
                            Tabladatos.setValueAt(rs0.getString("clave_unidadmedida"), filano, 6);
                            

                        }
                        if (rs0 != null) {
                            rs0.close();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                    }
                    //calcula la suma de datos

                }
                Tabladatos.changeSelection(filano, 1, false, false);
                permitecambios = 2;
            }//columna de la clave del articulo

        }
    }//GEN-LAST:event_TabladatosKeyPressed

    private void btnlugaremisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlugaremisionActionPerformed
        // TODO add your handling code here:
        busca_lugaresemision busca_lugaresemision = new busca_lugaresemision(null, true, connj, "");
        busca_lugaresemision.setLocationRelativeTo(this);
        busca_lugaresemision.setVisible(true);
        String valor = busca_lugaresemision.getText();
        busca_lugaresemision = null;
        if (valor.equals("") || valor.equals("0")) {
            idlugar.setText("");
            lugar.setText("");
        } else {
            idlugar.setText(valor);//obtiene el valor seleccionado
            rs1 = null;
            try {
                String senSQL = "SELECT * FROM lugaresemision WHERE id_lugaremision='" + idlugar.getText() + "'";
                rs1 = conexion.consulta(senSQL, connj);
                if (rs1.next()) {
                    lugar.setText(rs1.getString("municipio") + ", " + rs1.getString("estado"));
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnlugaremisionActionPerformed

    private void btnusocomprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnusocomprobanteActionPerformed
        // TODO add your handling code here:
        busca_usocomprobante busca_usocomprobante = new busca_usocomprobante(null, true, connj, "");
        busca_usocomprobante.setLocationRelativeTo(this);
        busca_usocomprobante.setVisible(true);
        String valor = busca_usocomprobante.getText();
        busca_usocomprobante = null;
        if (valor.equals("") || valor.equals("0")) {
            usocomprobantetxt.setText("");
            usocomprobantedes.setText("");
        } else {
            usocomprobantetxt.setText(valor);//obtiene el valor seleccionado
            rs1 = null;
            try {
                String senSQL = "SELECT * FROM uso_comprobantes WHERE id_usocomprobantes='" + usocomprobantetxt.getText() + "'";
                rs1 = conexion.consulta(senSQL, connj);
                if (rs1.next()) {
                    usocomprobantetxt.setText(rs1.getString("clave_usocomprobante"));
                    usocomprobantedes.setText(rs1.getString("usocomprobante"));
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnusocomprobanteActionPerformed

    private void btnformapagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnformapagoActionPerformed
        // TODO add your handling code here:
        busca_formapago busca_formapago = new busca_formapago(null, true, connj, "");
        busca_formapago.setLocationRelativeTo(this);
        busca_formapago.setVisible(true);
        String valor = busca_formapago.getText();
        busca_formapago = null;
        if (valor.equals("") || valor.equals("0")) {
            formapagotxt.setText("");
            formapagodes.setText("");
        } else {
            formapagotxt.setText(valor);//obtiene el valor seleccionado
            rs1 = null;
            try {
                String senSQL = "SELECT * FROM formaspagos WHERE id_formapago='" + formapagotxt.getText() + "'";
                rs1 = conexion.consulta(senSQL, connj);
                if (rs1.next()) {
                    formapagotxt.setText(rs1.getString("clave_formapago"));
                    formapagodes.setText(rs1.getString("formapago"));
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnformapagoActionPerformed

    private void btnmetodopagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmetodopagoActionPerformed
        // TODO add your handling code here:
        busca_metodopago busca_metodopago = new busca_metodopago(null, true, connj, "");
        busca_metodopago.setLocationRelativeTo(this);
        busca_metodopago.setVisible(true);
        String valor = busca_metodopago.getText();
        busca_metodopago = null;
        if (valor.equals("") || valor.equals("0")) {
            metodopagotxt.setText("");
            metodopagodes.setText("");
        } else {
            metodopagotxt.setText(valor);//obtiene el valor seleccionado
            rs1 = null;
            try {
                String senSQL = "SELECT * FROM metodospagos WHERE id_metodopago='" + metodopagotxt.getText() + "'";
                rs1 = conexion.consulta(senSQL, connj);
                if (rs1.next()) {
                    metodopagotxt.setText(rs1.getString("clave_metodopago"));
                    metodopagodes.setText(rs1.getString("metodopago"));
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnmetodopagoActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField aprobacion;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnclientes;
    private javax.swing.JButton btnfolios;
    private javax.swing.JButton btnformapago;
    private javax.swing.JButton btnimpuestos;
    private javax.swing.JButton btnlugaremision;
    private javax.swing.JButton btnmetodopago;
    private javax.swing.JButton btnmonedas;
    private javax.swing.JButton btnremove;
    private javax.swing.JButton btnusocomprobante;
    private javax.swing.JTextField clavecliente;
    private javax.swing.JTextField ctapagotxt;
    private javax.swing.JTextField diascred;
    private javax.swing.JTextField factura;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField formapagodes;
    private javax.swing.JTextField formapagotxt;
    private javax.swing.JTextField hora;
    private javax.swing.JTextField idfolio;
    private javax.swing.JTextField idlugar;
    private javax.swing.JTextField idmoneda;
    private javax.swing.JTextField importe;
    private javax.swing.JTextField impuestos;
    private javax.swing.JTextField isrretenido;
    private javax.swing.JTextField iva;
    private javax.swing.JTextField ivaretenido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbiva;
    private javax.swing.JTextField lugar;
    private javax.swing.JTextField metodopagodes;
    private javax.swing.JTextField metodopagotxt;
    private javax.swing.JTextField moneda;
    private javax.swing.JTextField nombrecliente;
    private javax.swing.JTextArea notas;
    private javax.swing.JTextField oc;
    private javax.swing.JTextField rfc;
    private javax.swing.JTextField serie;
    private javax.swing.JTextField subtotal;
    private javax.swing.JTextField total;
    private javax.swing.JTextField usocomprobantedes;
    private javax.swing.JTextField usocomprobantetxt;
    // End of variables declaration//GEN-END:variables

}
