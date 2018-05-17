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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;


/**
 *
 * @author IVONNE
 */
public class datos_clientes extends javax.swing.JDialog {

    Connection connj;
    ResultSet rs0 = null;
    String clavemodifica0 = "";
    String valor_privilegio = "1";

    /** Creates new form datos_usuarios */
    public datos_clientes(java.awt.Frame parent, boolean modal, Connection conn, String clavemodifica, String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj = conn;
        valor_privilegio = valor_privilegio_t;
       
        consultamodifica(clavemodifica);
        clavemodifica0 = clavemodifica;
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



    public void consultamodifica(String clavemodifica) {
        if (clavemodifica.equals("")) {
            /**si la clave esta vacia significa que es un registro nuevo*/
        } else {
            id.setEditable(false);
            id.setFocusable(false);
            nombre.requestFocus();
            rs0 = null;
            try {
                String senSQL = "SELECT * FROM clientes WHERE id_clientes='" + clavemodifica + "'";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    id.setText(rs0.getString("id_clientes"));
                    idsae.setText(rs0.getString("id_sae_clie"));
                    nombre.setText(rs0.getString("nombre"));
                    calle.setText(rs0.getString("calle"));
                    numeroext.setText(rs0.getString("numeroext"));
                    numeroint.setText(rs0.getString("numeroint"));
                    colonia.setText(rs0.getString("colonia"));
                    localidad.setText(rs0.getString("localidad"));
                    cp.setText(rs0.getString("cod_postal"));
                    municipio.setText(rs0.getString("municipio"));
                    estado.setText(rs0.getString("estado"));
                    pais.setText(rs0.getString("pais"));
                    dias.setText(rs0.getString("dias"));
                    rfc.setText(rs0.getString("rfc"));
                    revision.setText(rs0.getString("diasrevision"));
                    pago.setText(rs0.getString("diaspago"));
                    agente.setText(rs0.getString("id_agente"));
                    contacto.setText(rs0.getString("contacto"));
                    telefono1.setText(rs0.getString("telefono1"));
                    telefono2.setText(rs0.getString("telefono2"));
                    aceptacion.setText(rs0.getString("www"));
                    email.setText(rs0.getString("email"));
                    cuentacontable.setText(rs0.getString("cuenta_contable"));
                    formapagotxt.setText(rs0.getString("clave_formapago"));
                    formapagodes.setText(rs0.getString("formapago"));
                    metodopagotxt.setText(rs0.getString("clave_metodopago"));
                    metodopagodes.setText(rs0.getString("metodopago"));
                    usocomprobantetxt.setText(rs0.getString("clave_usocomprobantes"));
                    usocomprobantedes.setText(rs0.getString("usocomprobantes"));
                    emailcfd.setText(rs0.getString("email_cfd"));
                    referencia.setText(rs0.getString("referencia"));
                    observaciones.setText(rs0.getString("observaciones"));
                    cuentacontable_skarton.setText(rs0.getString("cuenta_contable_skarton"));
                    banco1.setText(rs0.getString("clave_banco"));
                    banco2.setText(rs0.getString("clave_banco2"));
                    ctapagotxt.setText(rs0.getString("ctapago"));
                    ctapagotxt2.setText(rs0.getString("ctapago2"));
                    
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    public boolean validarRfc(String rfc_t) {
        boolean resul = false;
        if (rfc_t.length() == 12) {
            rfc_t = rfc_t.toUpperCase().trim();
            resul = rfc_t.toUpperCase().matches("[A-Z]{3}[0-9]{6}[A-Z0-9]{3}");
        }
        if (rfc_t.length() == 13) {
            rfc_t = rfc_t.toUpperCase().trim();
            resul = rfc_t.toUpperCase().matches("[A-Z]{4}[0-9]{6}[A-Z0-9]{3}");
        }
        return resul;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel36 = new javax.swing.JLabel();
        formapago2 = new javax.swing.JFormattedTextField();
        btnbuscar5 = new javax.swing.JButton();
        formapago6 = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        calle = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btncancelar = new javax.swing.JButton();
        btnaceptar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        cp = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        municipio = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        estado = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        pais = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        rfc = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        revision = new javax.swing.JTextField();
        pago = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dias = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        contacto = new javax.swing.JTextField();
        agente = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        telefono1 = new javax.swing.JTextField();
        telefono2 = new javax.swing.JTextField();
        aceptacion = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        cuentacontable = new javax.swing.JTextField();
        id = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        emailcfd = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        numeroext = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        colonia = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        observaciones = new javax.swing.JTextArea();
        numeroint = new javax.swing.JTextField();
        localidad = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        referencia = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        cuentacontable_skarton = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        ctapagotxt = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        ctapagotxt2 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        btnformapago = new javax.swing.JButton();
        formapagotxt = new javax.swing.JFormattedTextField();
        banco1 = new javax.swing.JFormattedTextField();
        banco2 = new javax.swing.JFormattedTextField();
        btnbuscar2 = new javax.swing.JButton();
        btnbuscar3 = new javax.swing.JButton();
        idsae = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        metodopagotxt = new javax.swing.JFormattedTextField();
        btnmetodopago = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        usocomprobantetxt = new javax.swing.JFormattedTextField();
        btnusocomprobante = new javax.swing.JButton();
        formapagodes = new javax.swing.JFormattedTextField();
        metodopagodes = new javax.swing.JFormattedTextField();
        usocomprobantedes = new javax.swing.JFormattedTextField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_clientes.class);
        jLabel36.setText(resourceMap.getString("jLabel36.text")); // NOI18N
        jLabel36.setName("jLabel36"); // NOI18N

        formapago2.setEditable(false);
        formapago2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        formapago2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        formapago2.setFocusable(false);
        formapago2.setName("formapago2"); // NOI18N

        btnbuscar5.setBackground(resourceMap.getColor("btnbuscar5.background")); // NOI18N
        btnbuscar5.setFont(resourceMap.getFont("btnbuscar5.font")); // NOI18N
        btnbuscar5.setIcon(null);
        btnbuscar5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnbuscar5.setName("btnbuscar5"); // NOI18N
        btnbuscar5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscar5ActionPerformed(evt);
            }
        });

        formapago6.setEditable(false);
        formapago6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        formapago6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        formapago6.setFocusable(false);
        formapago6.setName("formapago6"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        nombre.setName("nombre"); // NOI18N
        nombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nombreFocusGained(evt);
            }
        });

        calle.setName("calle"); // NOI18N
        calle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                calleFocusGained(evt);
            }
        });

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

        btnaceptar.setIcon(resourceMap.getIcon("btnaceptar.icon")); // NOI18N
        btnaceptar.setText(resourceMap.getString("btnaceptar.text")); // NOI18N
        btnaceptar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnaceptar.setName("btnaceptar"); // NOI18N
        btnaceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(170, Short.MAX_VALUE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jLabel13.setFont(resourceMap.getFont("jLabel13.font")); // NOI18N
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        cp.setText(resourceMap.getString("cp.text")); // NOI18N
        cp.setName("cp"); // NOI18N
        cp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cpFocusGained(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        municipio.setName("municipio"); // NOI18N
        municipio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                municipioFocusGained(evt);
            }
        });

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setAlignmentY(0.0F);
        jLabel11.setName("jLabel11"); // NOI18N

        estado.setName("estado"); // NOI18N
        estado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                estadoFocusGained(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        pais.setText(resourceMap.getString("pais.text")); // NOI18N
        pais.setName("pais"); // NOI18N
        pais.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                paisFocusGained(evt);
            }
        });

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        rfc.setName("rfc"); // NOI18N
        rfc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rfcFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                rfcFocusLost(evt);
            }
        });

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        revision.setName("revision"); // NOI18N
        revision.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                revisionFocusGained(evt);
            }
        });

        pago.setName("pago"); // NOI18N
        pago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pagoFocusGained(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        dias.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        dias.setText(resourceMap.getString("dias.text")); // NOI18N
        dias.setName("dias"); // NOI18N
        dias.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                diasFocusGained(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        contacto.setText(resourceMap.getString("contacto.text")); // NOI18N
        contacto.setName("contacto"); // NOI18N
        contacto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contactoFocusGained(evt);
            }
        });

        agente.setEditable(false);
        agente.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        agente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        agente.setText(resourceMap.getString("agente.text")); // NOI18N
        agente.setFocusable(false);
        agente.setName("agente"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        telefono1.setText(resourceMap.getString("telefono1.text")); // NOI18N
        telefono1.setName("telefono1"); // NOI18N
        telefono1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                telefono1FocusGained(evt);
            }
        });

        telefono2.setText(resourceMap.getString("telefono2.text")); // NOI18N
        telefono2.setName("telefono2"); // NOI18N
        telefono2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                telefono2FocusGained(evt);
            }
        });

        aceptacion.setText(resourceMap.getString("aceptacion.text")); // NOI18N
        aceptacion.setName("aceptacion"); // NOI18N
        aceptacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                aceptacionFocusGained(evt);
            }
        });

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        email.setText(resourceMap.getString("email.text")); // NOI18N
        email.setName("email"); // NOI18N
        email.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emailFocusGained(evt);
            }
        });

        btnbuscar.setFont(resourceMap.getFont("btnbuscar.font")); // NOI18N
        btnbuscar.setIcon(resourceMap.getIcon("btnbuscar.icon")); // NOI18N
        btnbuscar.setText(resourceMap.getString("btnbuscar.text")); // NOI18N
        btnbuscar.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnbuscar.setName("btnbuscar"); // NOI18N
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        cuentacontable.setText(resourceMap.getString("cuentacontable.text")); // NOI18N
        cuentacontable.setName("cuentacontable"); // NOI18N
        cuentacontable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cuentacontableFocusGained(evt);
            }
        });

        id.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("0000"))));
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setFont(resourceMap.getFont("id.font")); // NOI18N
        id.setName("id"); // NOI18N
        id.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                idFocusLost(evt);
            }
        });

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        emailcfd.setText(resourceMap.getString("emailcfd.text")); // NOI18N
        emailcfd.setName("emailcfd"); // NOI18N
        emailcfd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emailcfdFocusGained(evt);
            }
        });

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        numeroext.setText(resourceMap.getString("numeroext.text")); // NOI18N
        numeroext.setName("numeroext"); // NOI18N
        numeroext.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                numeroextFocusGained(evt);
            }
        });

        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        colonia.setText(resourceMap.getString("colonia.text")); // NOI18N
        colonia.setName("colonia"); // NOI18N
        colonia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coloniaFocusGained(evt);
            }
        });

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        observaciones.setColumns(20);
        observaciones.setFont(resourceMap.getFont("observaciones.font")); // NOI18N
        observaciones.setLineWrap(true);
        observaciones.setRows(3);
        observaciones.setName("observaciones"); // NOI18N
        jScrollPane1.setViewportView(observaciones);

        numeroint.setText(resourceMap.getString("numeroint.text")); // NOI18N
        numeroint.setName("numeroint"); // NOI18N
        numeroint.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                numerointFocusGained(evt);
            }
        });

        localidad.setText(resourceMap.getString("localidad.text")); // NOI18N
        localidad.setName("localidad"); // NOI18N
        localidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                localidadFocusGained(evt);
            }
        });

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText(resourceMap.getString("jLabel26.text")); // NOI18N
        jLabel26.setName("jLabel26"); // NOI18N

        referencia.setText(resourceMap.getString("referencia.text")); // NOI18N
        referencia.setName("referencia"); // NOI18N
        referencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                referenciaFocusGained(evt);
            }
        });

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText(resourceMap.getString("jLabel27.text")); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N

        cuentacontable_skarton.setText(resourceMap.getString("cuentacontable_skarton.text")); // NOI18N
        cuentacontable_skarton.setName("cuentacontable_skarton"); // NOI18N
        cuentacontable_skarton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cuentacontable_skartonFocusGained(evt);
            }
        });

        jLabel28.setText(resourceMap.getString("jLabel28.text")); // NOI18N
        jLabel28.setName("jLabel28"); // NOI18N

        ctapagotxt.setText(resourceMap.getString("ctapagotxt.text")); // NOI18N
        ctapagotxt.setName("ctapagotxt"); // NOI18N

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText(resourceMap.getString("jLabel30.text")); // NOI18N
        jLabel30.setName("jLabel30"); // NOI18N

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText(resourceMap.getString("jLabel32.text")); // NOI18N
        jLabel32.setName("jLabel32"); // NOI18N

        jLabel33.setText(resourceMap.getString("jLabel33.text")); // NOI18N
        jLabel33.setName("jLabel33"); // NOI18N

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText(resourceMap.getString("jLabel34.text")); // NOI18N
        jLabel34.setName("jLabel34"); // NOI18N

        ctapagotxt2.setName("ctapagotxt2"); // NOI18N

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText(resourceMap.getString("jLabel35.text")); // NOI18N
        jLabel35.setName("jLabel35"); // NOI18N

        btnformapago.setBackground(resourceMap.getColor("btnformapago.background")); // NOI18N
        btnformapago.setFont(resourceMap.getFont("btnformapago.font")); // NOI18N
        btnformapago.setIcon(resourceMap.getIcon("btnformapago.icon")); // NOI18N
        btnformapago.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnformapago.setName("btnformapago"); // NOI18N
        btnformapago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnformapagoActionPerformed(evt);
            }
        });

        formapagotxt.setEditable(false);
        formapagotxt.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        formapagotxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        formapagotxt.setText(resourceMap.getString("formapagotxt.text")); // NOI18N
        formapagotxt.setFocusable(false);
        formapagotxt.setName("formapagotxt"); // NOI18N

        banco1.setEditable(false);
        banco1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        banco1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        banco1.setText(resourceMap.getString("banco1.text")); // NOI18N
        banco1.setFocusable(false);
        banco1.setName("banco1"); // NOI18N

        banco2.setEditable(false);
        banco2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        banco2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        banco2.setText(resourceMap.getString("banco2.text")); // NOI18N
        banco2.setFocusable(false);
        banco2.setName("banco2"); // NOI18N

        btnbuscar2.setBackground(resourceMap.getColor("btnbuscar2.background")); // NOI18N
        btnbuscar2.setFont(resourceMap.getFont("btnbuscar2.font")); // NOI18N
        btnbuscar2.setIcon(resourceMap.getIcon("btnbuscar2.icon")); // NOI18N
        btnbuscar2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnbuscar2.setName("btnbuscar2"); // NOI18N
        btnbuscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscar2ActionPerformed(evt);
            }
        });

        btnbuscar3.setBackground(resourceMap.getColor("btnbuscar3.background")); // NOI18N
        btnbuscar3.setFont(resourceMap.getFont("btnbuscar3.font")); // NOI18N
        btnbuscar3.setIcon(resourceMap.getIcon("btnbuscar3.icon")); // NOI18N
        btnbuscar3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnbuscar3.setName("btnbuscar3"); // NOI18N
        btnbuscar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscar3ActionPerformed(evt);
            }
        });

        idsae.setName("idsae"); // NOI18N
        idsae.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                idsaeFocusGained(evt);
            }
        });

        jLabel31.setText(resourceMap.getString("jLabel31.text")); // NOI18N
        jLabel31.setName("jLabel31"); // NOI18N

        metodopagotxt.setEditable(false);
        metodopagotxt.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        metodopagotxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        metodopagotxt.setFocusable(false);
        metodopagotxt.setName("metodopagotxt"); // NOI18N

        btnmetodopago.setBackground(resourceMap.getColor("btnmetodopago.background")); // NOI18N
        btnmetodopago.setFont(resourceMap.getFont("btnmetodopago.font")); // NOI18N
        btnmetodopago.setIcon(resourceMap.getIcon("btnmetodopago.icon")); // NOI18N
        btnmetodopago.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmetodopago.setName("btnmetodopago"); // NOI18N
        btnmetodopago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmetodopagoActionPerformed(evt);
            }
        });

        jLabel37.setText(resourceMap.getString("jLabel37.text")); // NOI18N
        jLabel37.setName("jLabel37"); // NOI18N

        usocomprobantetxt.setEditable(false);
        usocomprobantetxt.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        usocomprobantetxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usocomprobantetxt.setFocusable(false);
        usocomprobantetxt.setName("usocomprobantetxt"); // NOI18N

        btnusocomprobante.setBackground(resourceMap.getColor("btnusocomprobante.background")); // NOI18N
        btnusocomprobante.setFont(resourceMap.getFont("btnusocomprobante.font")); // NOI18N
        btnusocomprobante.setIcon(resourceMap.getIcon("btnusocomprobante.icon")); // NOI18N
        btnusocomprobante.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnusocomprobante.setName("btnusocomprobante"); // NOI18N
        btnusocomprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnusocomprobanteActionPerformed(evt);
            }
        });

        formapagodes.setEditable(false);
        formapagodes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        formapagodes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        formapagodes.setFocusable(false);
        formapagodes.setName("formapagodes"); // NOI18N

        metodopagodes.setEditable(false);
        metodopagodes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        metodopagodes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        metodopagodes.setFocusable(false);
        metodopagodes.setName("metodopagodes"); // NOI18N

        usocomprobantedes.setEditable(false);
        usocomprobantedes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        usocomprobantedes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usocomprobantedes.setFocusable(false);
        usocomprobantedes.setName("usocomprobantedes"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(16, 16, 16)
                            .addComponent(idsae, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(calle, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(numeroext, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(numeroint, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(referencia, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(colonia, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(localidad, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(municipio, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(pais, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(cp, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(dias, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(revision, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(pago, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(agente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(63, 63, 63)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(aceptacion, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel30))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(cuentacontable, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(cuentacontable_skarton, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ctapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6, 6, 6)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(banco1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(btnbuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(ctapagotxt2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, 8)
                            .addComponent(banco2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(btnbuscar3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(24, 24, 24)
                            .addComponent(contacto, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(emailcfd, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(99, 99, 99)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(4, 4, 4)
                                    .addComponent(formapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)
                                    .addComponent(btnformapago, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(formapagodes, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(4, 4, 4)
                                    .addComponent(telefono1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(4, 4, 4)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(4, 4, 4)
                                    .addComponent(telefono2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(usocomprobantetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnusocomprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(usocomprobantedes, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(metodopagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnmetodopago, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(metodopagodes, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel33))
                    .addComponent(idsae, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel4))
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(calle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numeroext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel26))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numeroint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(referencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(localidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(municipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel5)))
                    .addComponent(rfc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(revision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(agente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscar)
                    .addComponent(aceptacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel20)
                            .addComponent(jLabel30))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cuentacontable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cuentacontable_skarton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telefono1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(telefono2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel28))
                    .addComponent(formapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnformapago, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formapagodes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel31))
                    .addComponent(metodopagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnmetodopago, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(metodopagodes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel37))
                    .addComponent(usocomprobantetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnusocomprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usocomprobantedes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(banco1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ctapagotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ctapagotxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(banco2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscar3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(contacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel21))
                    .addComponent(emailcfd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        telefono1.getAccessibleContext().setAccessibleName(resourceMap.getString("telefono1.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        if (id.getText().equals("")|| idsae.getText().equals("") || nombre.getText().equals("") || calle.getText().equals("") || numeroext.getText().equals("") || colonia.getText().equals("") || municipio.getText().equals("") || estado.getText().equals("") || pais.getText().equals("") || cp.getText().equals("") || dias.getText().equals("") || rfc.getText().equals("") || agente.getText().equals("")|| aceptacion.getText().equals("")||  formapagotxt.getText().equals("") || metodopagotxt.getText().equals("") || usocomprobantetxt.getText().equals("")|| emailcfd.getText().equals("")  ) {
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            if (!validarRfc(rfc.getText()) && !rfc.getText().toUpperCase().equals("N/A")) {
                JOptionPane.showMessageDialog(this, "RFC INCORRECTO", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            } else {
                String senSQL = "UPDATE clientes SET id_sae_clie = '" + idsae.getText() + "', nombre = '" + nombre.getText() + "', rfc = '" + rfc.getText() + "', calle = '" + calle.getText() + "', cod_postal = '" + cp.getText() + "', municipio = '" + municipio.getText() + "', estado = '" + estado.getText() + "', pais = '" + pais.getText() + "', diasrevision = '" + revision.getText() + "', diaspago = '" + pago.getText() + "', contacto = '" + contacto.getText() + "', telefono1 = '" + telefono1.getText() + "', telefono2 = '" + telefono2.getText() + "', id_agente = '" + agente.getText() + "', dias = '" + dias.getText() + "', www = '" + aceptacion.getText() + "', email = '" + email.getText() + "', cuenta_contable = '" + cuentacontable.getText() + "', email_cfd = '" + emailcfd.getText() + "', colonia = '" + colonia.getText() + "', localidad = '" + localidad.getText() + "', numeroext = '" + numeroext.getText() + "', numeroint = '" + numeroint.getText() + "', referencia = '" + referencia.getText() + "', observaciones = '" + observaciones.getText() + "', cuenta_contable_skarton = '" + cuentacontable_skarton.getText() + "', clave_formapago = '" + formapagotxt.getText() +  "', formapago = '" + formapagodes.getText() +  "', clave_metodopago = '" + metodopagotxt.getText() +  "', metodopago = '" + metodopagodes.getText() +  "', clave_usocomprobantes = '" + usocomprobantetxt.getText() +  "', usocomprobantes = '" + usocomprobantedes.getText() + "', clave_banco = '" + banco1.getText() +  "', clave_banco2 = '" + banco2.getText() +  "', ctapago = '" + ctapagotxt.getText() + "', ctapago2 = '" + ctapagotxt2.getText() + "' WHERE id_clientes = '" + id.getText() + "' ";
                if (clavemodifica0.equals("")) {
                    senSQL = "INSERT INTO clientes ( id_clientes, nombre, rfc, calle, cod_postal, municipio, estado, pais, diasrevision, diaspago, contacto, telefono1, telefono2, id_agente, dias, www, email, cuenta_contable, email_cfd, colonia, localidad, numeroext, numeroint, referencia, observaciones, cuenta_contable_skarton, clave_formapago, formapago, clave_metodopago, metodopago, clave_usocomprobantes, usocomprobantes, id_sae_clie, obsoleto, ctapago, ctapago2, clave_banco, clave_banco2 ) VALUES ( '" + id.getText () + "', '" + nombre.getText () + "', '" + rfc.getText () + "', '" + calle.getText () + "', '" + cp.getText () + "', '" + municipio.getText () + "', '" + estado.getText () + "', '" + pais.getText () + "', '" + revision.getText () + "', '" + pago.getText () + "', '" + contacto.getText () + "', '" + telefono1.getText () + "', '" + telefono2.getText () + "', '" + agente.getText () + "', '" + dias.getText () + "', '" + aceptacion.getText () + "', '" + email.getText () + "', '" + cuentacontable.getText () + "', '" + emailcfd.getText () + "', '" + colonia.getText () + "', '" + localidad.getText () + "', '" + numeroext.getText () + "', '" + numeroint.getText () + "', '" + referencia.getText () + "', '" + observaciones.getText () + "', '" + cuentacontable_skarton.getText () + "', '" + formapagotxt.getText () + "', '" + formapagodes.getText() +  "', '" + metodopagotxt.getText() +  "', '" + metodopagodes.getText() +  "', '" + usocomprobantetxt.getText() +  "', '" + usocomprobantedes.getText() +  "', '" + idsae.getText () + "', 'No', '" + ctapagotxt.getText () + "', '" + ctapagotxt2.getText () + "', '" + banco1.getText () + "', '" + banco2.getText () + "' );";
                }
                conexion.modifica_p(senSQL, connj, valor_privilegio);
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

    private void nombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nombreFocusGained
        // TODO add your handling code here:
        nombre.selectAll();
    }//GEN-LAST:event_nombreFocusGained

    private void calleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_calleFocusGained
        // TODO add your handling code here:
        calle.selectAll();
}//GEN-LAST:event_calleFocusGained

    private void cpFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cpFocusGained
        // TODO add your handling code here:
        cp.selectAll();
}//GEN-LAST:event_cpFocusGained

    private void municipioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_municipioFocusGained
        // TODO add your handling code here:
        municipio.selectAll();
}//GEN-LAST:event_municipioFocusGained

    private void estadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_estadoFocusGained
        // TODO add your handling code here:
        estado.selectAll();
}//GEN-LAST:event_estadoFocusGained

    private void paisFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_paisFocusGained
        // TODO add your handling code here:
        pais.selectAll();
}//GEN-LAST:event_paisFocusGained

    private void rfcFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rfcFocusGained
        // TODO add your handling code here:
        rfc.selectAll();
}//GEN-LAST:event_rfcFocusGained

    private void revisionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_revisionFocusGained
        // TODO add your handling code here:
        revision.selectAll();
}//GEN-LAST:event_revisionFocusGained

    private void pagoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pagoFocusGained
        // TODO add your handling code here:
        pago.selectAll();
}//GEN-LAST:event_pagoFocusGained

    private void diasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_diasFocusGained
        // TODO add your handling code here:
        dias.selectAll();
    }//GEN-LAST:event_diasFocusGained

    private void contactoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactoFocusGained
        // TODO add your handling code here:
        contacto.selectAll();
    }//GEN-LAST:event_contactoFocusGained

    private void telefono1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_telefono1FocusGained
        // TODO add your handling code here:
        telefono1.selectAll();
    }//GEN-LAST:event_telefono1FocusGained

    private void telefono2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_telefono2FocusGained
        // TODO add your handling code here:
        telefono2.selectAll();
    }//GEN-LAST:event_telefono2FocusGained

    private void aceptacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_aceptacionFocusGained
        // TODO add your handling code here:
        aceptacion.selectAll();
    }//GEN-LAST:event_aceptacionFocusGained

    private void emailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailFocusGained
        // TODO add your handling code here:
        email.selectAll();
    }//GEN-LAST:event_emailFocusGained

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        busca_agentes busca_agentes = new busca_agentes(null, true, connj, "");
        busca_agentes.setLocationRelativeTo(this);
        busca_agentes.setVisible(true);
        agente.setText(busca_agentes.getText());//obtiene el valor seleccionado
        busca_agentes = null;

}//GEN-LAST:event_btnbuscarActionPerformed

    private void rfcFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rfcFocusLost
        // TODO add your handling code here:
        if (rfc.getText().length() > 13) {
            JOptionPane.showMessageDialog(this, "RFC DE CLIENTE MAYOR A 13 DIGITOS", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            rfc.setText("");
            rfc.requestFocus();
        }
    }//GEN-LAST:event_rfcFocusLost

    private void emailcfdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailcfdFocusGained
        // TODO add your handling code here:
        emailcfd.selectAll();
    }//GEN-LAST:event_emailcfdFocusGained

    private void numeroextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numeroextFocusGained
        // TODO add your handling code here:
        numeroext.selectAll();
    }//GEN-LAST:event_numeroextFocusGained

    private void numerointFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numerointFocusGained
        // TODO add your handling code here:
        numeroint.selectAll();
    }//GEN-LAST:event_numerointFocusGained

    private void coloniaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_coloniaFocusGained
        // TODO add your handling code here:
        colonia.selectAll();
    }//GEN-LAST:event_coloniaFocusGained

    private void localidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_localidadFocusGained
        // TODO add your handling code here:
        localidad.selectAll();
    }//GEN-LAST:event_localidadFocusGained

    private void referenciaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_referenciaFocusGained
        // TODO add your handling code here:
        referencia.selectAll();
    }//GEN-LAST:event_referenciaFocusGained

    private void cuentacontableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cuentacontableFocusGained
        // TODO add your handling code here:
        cuentacontable.selectAll();
    }//GEN-LAST:event_cuentacontableFocusGained

    private void cuentacontable_skartonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cuentacontable_skartonFocusGained
        // TODO add your handling code here:
        cuentacontable_skarton.selectAll();
    }//GEN-LAST:event_cuentacontable_skartonFocusGained

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
            rs0 = null;
            try {
                String senSQL = "SELECT * FROM formaspagos WHERE id_formapago='" + formapagotxt.getText() + "'";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    formapagotxt.setText(rs0.getString("clave_formapago"));
                    formapagodes.setText(rs0.getString("formapago"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnformapagoActionPerformed

    private void btnbuscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscar2ActionPerformed
        // TODO add your handling code here:
        busca_bancos busca_bancos = new busca_bancos(null, true, connj, "");
        busca_bancos.setLocationRelativeTo(this);
        busca_bancos.setVisible(true);
        banco1.setText(busca_bancos.getText());//obtiene el valor seleccionado
        busca_bancos = null;
    }//GEN-LAST:event_btnbuscar2ActionPerformed

    private void btnbuscar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscar3ActionPerformed
        // TODO add your handling code here:
        busca_bancos busca_bancos = new busca_bancos(null, true, connj, "");
        busca_bancos.setLocationRelativeTo(this);
        busca_bancos.setVisible(true);
        banco2.setText(busca_bancos.getText());//obtiene el valor seleccionado
        busca_bancos = null;
    }//GEN-LAST:event_btnbuscar3ActionPerformed

    private void idsaeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idsaeFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_idsaeFocusGained

    private void idFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idFocusLost
        // TODO add your handling code here:
        if (id.getText().equals("")) {
        } else {
            if (id.getText().length() > 4) {
                JOptionPane.showMessageDialog(this, "CLAVE DE CLIENTE MAYOR A 4 DIGITOS  EJEM. (1234)", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                id.setText("");
                id.requestFocus();
            } else {
                rs0 = null;
                try {
                    String senSQL = "SELECT * FROM clientes WHERE (id_clientes='" + id.getText() + "')";
                    rs0 = conexion.consulta(senSQL, connj);
                    if (rs0.next()) {
                        JOptionPane.showMessageDialog(this, "LA CLAVE DE CLIENTE YA EXISTE", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                        id.setText("");
                        id.requestFocus();
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_idFocusLost

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
            rs0 = null;
            try {
                String senSQL = "SELECT * FROM metodospagos WHERE id_metodopago='" + metodopagotxt.getText() + "'";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    metodopagotxt.setText(rs0.getString("clave_metodopago"));
                    metodopagodes.setText(rs0.getString("metodopago"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnmetodopagoActionPerformed

    private void btnbuscar5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscar5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnbuscar5ActionPerformed

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
            rs0 = null;
            try {
                String senSQL = "SELECT * FROM uso_comprobantes WHERE id_usocomprobantes='" + usocomprobantetxt.getText() + "'";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    usocomprobantetxt.setText(rs0.getString("clave_usocomprobante"));
                    usocomprobantedes.setText(rs0.getString("usocomprobante"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnusocomprobanteActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField aceptacion;
    private javax.swing.JFormattedTextField agente;
    private javax.swing.JFormattedTextField banco1;
    private javax.swing.JFormattedTextField banco2;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnbuscar2;
    private javax.swing.JButton btnbuscar3;
    private javax.swing.JButton btnbuscar5;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnformapago;
    private javax.swing.JButton btnmetodopago;
    private javax.swing.JButton btnusocomprobante;
    private javax.swing.JTextField calle;
    private javax.swing.JTextField colonia;
    private javax.swing.JTextField contacto;
    private javax.swing.JTextField cp;
    private javax.swing.JTextField ctapagotxt;
    private javax.swing.JTextField ctapagotxt2;
    private javax.swing.JTextField cuentacontable;
    private javax.swing.JTextField cuentacontable_skarton;
    private javax.swing.JFormattedTextField dias;
    private javax.swing.JTextField email;
    private javax.swing.JTextField emailcfd;
    private javax.swing.JTextField estado;
    private javax.swing.JFormattedTextField formapago2;
    private javax.swing.JFormattedTextField formapago6;
    private javax.swing.JFormattedTextField formapagodes;
    private javax.swing.JFormattedTextField formapagotxt;
    private javax.swing.JFormattedTextField id;
    private javax.swing.JTextField idsae;
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField localidad;
    private javax.swing.JFormattedTextField metodopagodes;
    private javax.swing.JFormattedTextField metodopagotxt;
    private javax.swing.JTextField municipio;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField numeroext;
    private javax.swing.JTextField numeroint;
    private javax.swing.JTextArea observaciones;
    private javax.swing.JTextField pago;
    private javax.swing.JTextField pais;
    private javax.swing.JTextField referencia;
    private javax.swing.JTextField revision;
    private javax.swing.JTextField rfc;
    private javax.swing.JTextField telefono1;
    private javax.swing.JTextField telefono2;
    private javax.swing.JFormattedTextField usocomprobantedes;
    private javax.swing.JFormattedTextField usocomprobantetxt;
    // End of variables declaration//GEN-END:variables
}
