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
import java.io.ByteArrayInputStream;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.commons.codec.binary.Base64;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import mx.bigdata.sat.cfdi.CFDv32;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.CertificateFactory;
import org.apache.commons.ssl.PKCS8Key;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import mx.bigdata.sat.cfdi.v32.schema.Comprobante;

/**
 *
 * @author IVONNE
 */
public class datos_facturas_pagadas extends javax.swing.JDialog {

    Connection connj;
    private Properties conf;
    ResultSet rs0 = null, rs1 = null;
    DefaultTableModel modelot1 = null;
    DecimalFormat fijo2decimales = new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales = new DecimalFormat("######0.000");
    DecimalFormat fijo5decimales = new DecimalFormat("######0.00000");
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat fijo0decimales = new DecimalFormat("######0");
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    SimpleDateFormat fechasin = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MM-yyyy");
    String terminacion = "PESOS";
    Double valormoneda = 1.0;
    Double tasaiva = 0.0, retiva = 0.0, retisr = 0.0;
    String user = "";
    String rfcempresa = "";
    int permitecambios = 1;
    Calendar calendarvencimiento = new GregorianCalendar();
    X509Certificate certificadoempresa = null;
    PrivateKey keyempresa = null;
    String passllave = "";
    String valor_privilegio = "1";

    /** Creates new form datos_usuarios */
    public datos_facturas_pagadas(java.awt.Frame parent, boolean modal, Connection conn, String clavemodifica, String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj = conn;
        valor_privilegio = valor_privilegio_t;
        conf = conexion.archivoInicial();
        fecha.setDate(new Date());
        hora.setText(horacorta.format(new Date()));
        modelot1 = (DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        

        //datosEmpresa_prueba();

        formaspagos();
        ajusteTabladatos();
        buscaMoneda("1");
        impuestosdatos("1");
        consultamodifica(clavemodifica);
        user = conf.getProperty("UserID");
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

        

    }

    public void salir() {
        if (connj != null) {
            connj = null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }

    public void formaspagos() {

        try {
            rs0 = null;
            String senSQL = "SELECT * FROM formaspagos ORDER BY id_formapago";
            rs0 = conexion.consulta(senSQL, connj);
            while (rs0.next()) {
                String uno=rs0.getString("clave_formapago");
                metodopagotxt.addItem(uno);
            }
            if (rs0 != null) {
                rs0.close();
            }

        }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }

    public void consultamodifica(String clavemodifica) {
        if (clavemodifica.equals("")) {
            /**si la clave esta vacia significa que es un registro nuevo*/
        } else {
            
            rs0 = null;
            try {
                String senSQL = "SELECT facturas.*,folios.aprobacion,folios.anoaprobacion,monedas.descripcion as desmoneda,clientes.rfc,clientes.nombre,clientes.dias,lugaresemision.municipio,lugaresemision.estado,impuestos.iva as ivaimpuestos,impuestos.ivaretenido,impuestos.isrretenido,facturas_detalle.remision,facturas_detalle.id_op,facturas_detalle.clavearticulo,facturas_detalle.descripcion,(CASE WHEN facturas_detalle.um='millar' OR facturas_detalle.um='MILLAR' THEN (facturas_detalle.cantidad/1000) ELSE facturas_detalle.cantidad END ) AS cantidad,facturas_detalle.um,(CASE WHEN facturas_detalle.um='millar' OR facturas_detalle.um='MILLAR' THEN (facturas_detalle.preciounitario*1000) ELSE facturas_detalle.preciounitario END ) AS preciounitario,facturas_detalle.subtotal as subtotalpartida FROM (((((facturas LEFT JOIN folios ON facturas.id_folios=folios.id_folio) LEFT JOIN monedas ON facturas.id_moneda=monedas.id_moneda) LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes) LEFT JOIN lugaresemision ON facturas.id_lugaremision=lugaresemision.id_lugaremision) LEFT JOIN impuestos ON facturas.id_impuestos=impuestos.id_impuestos) INNER JOIN facturas_detalle ON facturas.factura_serie=facturas_detalle.factura_serie WHERE facturas.factura_serie='" + clavemodifica + "'";
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
                    Double des = rs0.getDouble("descuento");
                    Double fle = rs0.getDouble("fletes");
                    subtotal.setText(moneda2decimales.format(sub - fle));
                    descuento.setText(moneda2decimales.format(des));
                    fletes.setText(moneda2decimales.format(fle));
                    importe.setText(moneda2decimales.format(sub));

                    iva.setText(moneda2decimales.format(rs0.getDouble("iva")));
                    ivaretenido.setText(moneda2decimales.format(rs0.getDouble("ivaretenido")));
                    isrretenido.setText(moneda2decimales.format(rs0.getDouble("isrretenido")));
                    total.setText(moneda2decimales.format(rs0.getDouble("total")));

                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            /*detalle de ingresos*/
            rs0=null;
            try{
                String senSQL="SELECT pagos.id_pagos,pagos.id_pagos_anticipos,pagos.documento,pagos.fechapago,pagos.id_clientes,pagos.tipo_abono,pagos.importe as importetotal,pagos.id_cuenta,clientes.nombre,cuentasbancarias.numerocuenta,pagos_detalle.factura_serie,pagos_detalle.importe,pagos_detalle.importe_factoraje,docxcob.fechavencimiento FROM ((pagos LEFT JOIN clientes ON pagos.id_clientes=clientes.id_clientes) LEFT JOIN cuentasbancarias ON pagos.id_cuenta=cuentasbancarias.id_cuenta) INNER JOIN (pagos_detalle LEFT JOIN docxcob ON pagos_detalle.factura_serie=docxcob.factura_serie) ON pagos.id_pagos=pagos_detalle.id_pagos WHERE (pagos.estatus='Act' AND pagos_detalle.factura_serie='"+clavemodifica+"') ORDER BY pagos.fechapago;";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={"Pago",rs0.getString("documento"),fechamediana.format(rs0.getDate("fechapago")),rs0.getString("numerocuenta"),rs0.getString("tipo_abono"),(rs0.getDouble("importe_factoraje")+rs0.getDouble("importe"))};
                    modelot1.addRow(datos);
                    
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            /*notas de credito*/
            rs0=null;
            try{
                String senSQL="SELECT notas_credito.*,folios.aprobacion,folios.anoaprobacion,monedas.descripcion as desmoneda,clientes.rfc,clientes.nombre,clientes.dias,lugaresemision.municipio,lugaresemision.estado,impuestos.iva as ivaimpuestos,impuestos.ivaretenido,impuestos.isrretenido,notas_credito_detalle.factura_serie,notas_credito_detalle.descripcion,notas_credito_detalle.cantidad,notas_credito_detalle.um,notas_credito_detalle.preciounitario,notas_credito_detalle.subtotal as subtotalpartida,notas_credito_detalle.importe as importepartida FROM (((((notas_credito LEFT JOIN folios ON notas_credito.id_folios=folios.id_folio) LEFT JOIN monedas ON notas_credito.id_moneda=monedas.id_moneda) LEFT JOIN clientes ON notas_credito.id_clientes=clientes.id_clientes) LEFT JOIN lugaresemision ON notas_credito.id_lugaremision=lugaresemision.id_lugaremision) LEFT JOIN impuestos ON notas_credito.id_impuestos=impuestos.id_impuestos) INNER JOIN notas_credito_detalle ON notas_credito.nota_credito_serie=notas_credito_detalle.nota_credito_serie WHERE notas_credito_detalle.factura_serie='"+clavemodifica+"' AND notas_credito.estatus=1";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={"Nota de crédito",rs0.getString("nota_credito"),fechamediana.format(rs0.getDate("fecha")),"","",(rs0.getDouble("importepartida"))};
                    modelot1.addRow(datos);

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }

    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(150);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(120);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(150);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(90);

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

    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        factura = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
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
        fletes = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        descuento = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
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
        metodopagotxt = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        ctapagotxt = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_facturas_pagadas.class);
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
                .addGap(310, 310, 310)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(309, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        btnfolios.setEnabled(false);
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
        btnmonedas.setEnabled(false);
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
        btnclientes.setEnabled(false);
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
        btnimpuestos.setEnabled(false);
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
                "Tipo", "Documento", "Fecha", "Cuenta", "Metodo Pago", "Importe"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

        iva.setBackground(resourceMap.getColor("iva.background")); // NOI18N
        iva.setEditable(false);
        iva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        iva.setText(resourceMap.getString("iva.text")); // NOI18N
        iva.setFocusable(false);
        iva.setName("iva"); // NOI18N

        lbiva.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbiva.setText(resourceMap.getString("lbiva.text")); // NOI18N
        lbiva.setName("lbiva"); // NOI18N

        ivaretenido.setBackground(resourceMap.getColor("ivaretenido.background")); // NOI18N
        ivaretenido.setEditable(false);
        ivaretenido.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ivaretenido.setText(resourceMap.getString("ivaretenido.text")); // NOI18N
        ivaretenido.setFocusable(false);
        ivaretenido.setName("ivaretenido"); // NOI18N

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        isrretenido.setBackground(resourceMap.getColor("isrretenido.background")); // NOI18N
        isrretenido.setEditable(false);
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

        total.setBackground(resourceMap.getColor("total.background")); // NOI18N
        total.setEditable(false);
        total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        total.setText(resourceMap.getString("total.text")); // NOI18N
        total.setFocusable(false);
        total.setName("total"); // NOI18N

        subtotal.setBackground(resourceMap.getColor("subtotal.background")); // NOI18N
        subtotal.setEditable(false);
        subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        subtotal.setText(resourceMap.getString("subtotal.text")); // NOI18N
        subtotal.setFocusable(false);
        subtotal.setName("subtotal"); // NOI18N

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        fletes.setEditable(false);
        fletes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        fletes.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fletes.setText(resourceMap.getString("fletes.text")); // NOI18N
        fletes.setFocusable(false);
        fletes.setName("fletes"); // NOI18N
        fletes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fletesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fletesFocusLost(evt);
            }
        });

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        descuento.setEditable(false);
        descuento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        descuento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        descuento.setText(resourceMap.getString("descuento.text")); // NOI18N
        descuento.setFocusable(false);
        descuento.setName("descuento"); // NOI18N
        descuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                descuentoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                descuentoFocusLost(evt);
            }
        });

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        notas.setColumns(20);
        notas.setEditable(false);
        notas.setFont(resourceMap.getFont("notas.font")); // NOI18N
        notas.setLineWrap(true);
        notas.setRows(4);
        notas.setName("notas"); // NOI18N
        jScrollPane2.setViewportView(notas);

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        importe.setBackground(resourceMap.getColor("importe.background")); // NOI18N
        importe.setEditable(false);
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
        btnlugaremision.setEnabled(false);
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

        oc.setEditable(false);
        oc.setText(resourceMap.getString("oc.text")); // NOI18N
        oc.setName("oc"); // NOI18N

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        metodopagotxt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione..." }));
        metodopagotxt.setToolTipText(resourceMap.getString("metodopagotxt.toolTipText")); // NOI18N
        metodopagotxt.setName("metodopagotxt"); // NOI18N
        metodopagotxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodopagotxtActionPerformed(evt);
            }
        });

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        ctapagotxt.setEditable(false);
        ctapagotxt.setText(resourceMap.getString("ctapagotxt.text")); // NOI18N
        ctapagotxt.setToolTipText(resourceMap.getString("ctapagotxt.toolTipText")); // NOI18N
        ctapagotxt.setName("ctapagotxt"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        jSeparator2.setName("jSeparator2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
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
                                .addComponent(idlugar, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnlugaremision, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(clavecliente, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(factura, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(idfolio, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
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
                                    .addComponent(rfc))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(oc, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
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
                                    .addComponent(moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(nombrecliente, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                            .addComponent(lugar, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(metodopagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ctapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fletes, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbiva, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(iva, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(ivaretenido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(isrretenido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(total, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)))
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE))
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
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(iva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbiva)
                        .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16)
                        .addComponent(ctapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(metodopagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ivaretenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(fletes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(isrretenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)))
                    .addComponent(jLabel22)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName(resourceMap.getString("Form.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        
}//GEN-LAST:event_btnfoliosActionPerformed

    private void btnmonedasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmonedasActionPerformed
        // TODO add your handling code here:
        
}//GEN-LAST:event_btnmonedasActionPerformed

    private void btnclientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnclientesActionPerformed
        // TODO add your handling code here:
       
}//GEN-LAST:event_btnclientesActionPerformed

    private void btnimpuestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimpuestosActionPerformed
        // TODO add your handling code here:
        
}//GEN-LAST:event_btnimpuestosActionPerformed

    private void TabladatosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyPressed
        
    }//GEN-LAST:event_TabladatosKeyPressed

    private void fletesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fletesFocusGained
        // TODO add your handling code here:
        fletes.selectAll();
    }//GEN-LAST:event_fletesFocusGained

    private void fletesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fletesFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_fletesFocusLost

    private void descuentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descuentoFocusGained
        // TODO add your handling code here:
        descuento.selectAll();
    }//GEN-LAST:event_descuentoFocusGained

    private void descuentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descuentoFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_descuentoFocusLost

    private void btnlugaremisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlugaremisionActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnlugaremisionActionPerformed

    private void metodopagotxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodopagotxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_metodopagotxtActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField aprobacion;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnclientes;
    private javax.swing.JButton btnfolios;
    private javax.swing.JButton btnimpuestos;
    private javax.swing.JButton btnlugaremision;
    private javax.swing.JButton btnmonedas;
    private javax.swing.JTextField clavecliente;
    private javax.swing.JTextField ctapagotxt;
    private javax.swing.JFormattedTextField descuento;
    private javax.swing.JTextField diascred;
    private javax.swing.JTextField factura;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JFormattedTextField fletes;
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
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbiva;
    private javax.swing.JTextField lugar;
    private javax.swing.JComboBox metodopagotxt;
    private javax.swing.JTextField moneda;
    private javax.swing.JTextField nombrecliente;
    private javax.swing.JTextArea notas;
    private javax.swing.JTextField oc;
    private javax.swing.JTextField rfc;
    private javax.swing.JTextField serie;
    private javax.swing.JTextField subtotal;
    private javax.swing.JTextField total;
    // End of variables declaration//GEN-END:variables
}
