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
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author IVONNE
 */
public class datos_resistencias extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales=new DecimalFormat("######0.000");
    Double l1m=0.0,l2m=0.0,l3m=0.0;
    Double l1c=0.0,r1c=0.0,l2c=0.0,r2c=0.0,l3c=0.0;
    Double l1p=0.0,l2p=0.0,l3p=0.0,r1p=0.0,r2p=0.0;
    Double l1pu=0.0,l2pu=0.0,l3pu=0.0,r1pu=0.0,r2pu=0.0;
    String valor_privilegio="1";
    

    /** Creates new form datos_usuarios */
    public datos_resistencias(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        consultamodifica(clavemodifica);
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
            descripcion.requestFocus();
            clave.setEditable(false);
            pesom2.setEditable(false);
            l1.setEditable(false);
            r1.setEditable(false);
            l2.setEditable(false);
            r2.setEditable(false);
            l3.setEditable(false);
            f1.setEnabled(false);
            f2.setEnabled(false);
            rs0=null;
            try{
                String senSQL="SELECT * FROM resistencias WHERE id_resistencia='"+clavemodifica+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    id.setText(rs0.getString("id_resistencia"));
                    clave.setText(rs0.getString("clave"));
                    descripcion.setText(rs0.getString("descripcion"));
                    pesom2.setText(fijo3decimales.format(rs0.getDouble("pesom2")));
                    pesom2real.setText(fijo3decimales.format(rs0.getDouble("pesom2real")));
                    tipo.setSelectedItem(rs0.getObject("tipo"));
                    valor.setText(fijo2decimales.format(rs0.getDouble("valorminimo")));
                    l1.setText(rs0.getString("l1"));
                    r1.setText(rs0.getString("r1"));
                    l2.setText(rs0.getString("l2"));
                    r2.setText(rs0.getString("r2"));
                    l3.setText(rs0.getString("l3"));
                    f1.setSelectedItem(rs0.getObject("f1"));
                    f2.setSelectedItem(rs0.getObject("f2"));
                    color.setSelectedItem(rs0.getObject("color"));
                    corrugado.setSelectedItem(rs0.getObject("corrugado"));
                    l1r.setText(rs0.getString("l1r"));
                    r1r.setText(rs0.getString("r1r"));
                    l2r.setText(rs0.getString("l2r"));
                    r2r.setText(rs0.getString("r2r"));
                    l3r.setText(rs0.getString("l3r"));
                    valorreal.setText(fijo2decimales.format(rs0.getDouble("valorreal")));
                    preciokg.setText(fijo2decimales.format(rs0.getDouble("precio_kg")));
                    preciom2.setText(fijo2decimales.format(rs0.getDouble("precio_m2")));
                    
                    datospapel1("");
                    datospapel2("");
                    datospapel3("");
                    datospapel4("");
                    datospapel5("");

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void datospapel1(String ban){
        l1r.setText(l1r.getText().toUpperCase());
        String papel=l1r.getText();
        l1m=0.0;
        l1c=0.0;
        l1p=0.0;
        l1pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    l1r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    l1m=rs0.getDouble("mullen");
                    l1c=rs0.getDouble("rc");
                    l1p=rs0.getDouble("peso");
                    l1pu=rs0.getDouble("preciocompra");
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    l1r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!l1r.getText().equals("")){
                        l1r.setText( l1r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+l1r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                l1m=rs0.getDouble("mullen");
                                l1c=rs0.getDouble("rc");
                                l1p=rs0.getDouble("peso");
                                l1pu=rs0.getDouble("preciocompra");
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void datospapel2(String ban){
        r1r.setText(r1r.getText().toUpperCase());
        String papel=r1r.getText();
        r1c=0.0;
        r1p=0.0;
        r1pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){
            //VALIDA EL FACTOR DE ARROLLAMIENTO DEL PAPEL POR LA FLAUTA
            String flau1=(String) f1.getSelectedItem();
            Double factora1=1.48;
            if(flau1.equals("B"))
                factora1=1.36;

            Double factora2=1.44;
            if(flau1.equals("B"))
                factora2=1.33;

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    r1r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    r1c=rs0.getDouble("rc")*factora2;
                    r1p=rs0.getDouble("peso")*factora1;
                    r1pu=rs0.getDouble("preciocompra");
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    r1r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!r1r.getText().equals("")){
                        r1r.setText( r1r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+r1r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                r1c=rs0.getDouble("rc")*factora2;
                                r1p=rs0.getDouble("peso")*factora1;
                                r1pu=rs0.getDouble("preciocompra");
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void datospapel3(String ban){
        l2r.setText(l2r.getText().toUpperCase());
        String papel=l2r.getText();
        l2m=0.0;
        l2c=0.0;
        l2p=0.0;
        l2pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    l2r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    l2m=rs0.getDouble("mullen");
                    l2c=rs0.getDouble("rc");
                    l2p=rs0.getDouble("peso");
                    l2pu=rs0.getDouble("preciocompra");
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    l2r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!l2r.getText().equals("")){
                        l2r.setText( l2r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+l2r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                l2m=rs0.getDouble("mullen");
                                l2c=rs0.getDouble("rc");
                                l2p=rs0.getDouble("peso");
                                l2pu=rs0.getDouble("preciocompra");
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void datospapel4(String ban){
        r2r.setText(r2r.getText().toUpperCase());
        String papel=r2r.getText();
        r2c=0.0;
        r2p=0.0;
        r2pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){
            //VALIDA EL FACTOR DE ARROLLAMIENTO DEL PAPEL POR LA FLAUTA
            String flau1=(String) f2.getSelectedItem();
            Double factora1=1.48;
            if(flau1.equals("B"))
                factora1=1.36;

            Double factora2=1.44;
            if(flau1.equals("B"))
                factora2=1.33;

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    r2r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    r2c=rs0.getDouble("rc")*factora2;
                    r2p=rs0.getDouble("peso")*factora1;
                    r2pu=rs0.getDouble("preciocompra");
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    r2r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!r2r.getText().equals("")){
                        r2r.setText( r2r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+r2r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                r2c=rs0.getDouble("rc")*factora2;
                                r2p=rs0.getDouble("peso")*factora1;
                                r2pu=rs0.getDouble("preciocompra");
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void datospapel5(String ban){
        l3r.setText(l3r.getText().toUpperCase());
        String papel=l3r.getText();
        l3m=0.0;
        l3c=0.0;
        l3p=0.0;
        l3pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    l3r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    l3m=rs0.getDouble("mullen");
                    l3c=rs0.getDouble("rc");
                    l3p=rs0.getDouble("peso");
                    l3pu=rs0.getDouble("preciocompra");
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    l3r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!l3r.getText().equals("")){
                        l3r.setText( l3r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+l3r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                l3m=rs0.getDouble("mullen");
                                l3c=rs0.getDouble("rc");
                                l3p=rs0.getDouble("peso");
                                l3pu=rs0.getDouble("preciocompra");
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

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

        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        clave = new javax.swing.JTextField();
        descripcion = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        medidareal = new javax.swing.JLabel();
        pesom2 = new javax.swing.JFormattedTextField();
        valor = new javax.swing.JFormattedTextField();
        pesom2real = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        tipo = new javax.swing.JComboBox();
        medidares = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        l1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        r1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        l2 = new javax.swing.JTextField();
        r2 = new javax.swing.JTextField();
        l3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        f1 = new javax.swing.JComboBox();
        f2 = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        color = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        corrugado = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        l1r = new javax.swing.JTextField();
        r1r = new javax.swing.JTextField();
        l2r = new javax.swing.JTextField();
        r2r = new javax.swing.JTextField();
        l3r = new javax.swing.JTextField();
        valorreal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        preciokg = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        preciom2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_resistencias.class);
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

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        id.setEditable(false);
        id.setFont(resourceMap.getFont("id.font")); // NOI18N
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setFocusable(false);
        id.setName("id"); // NOI18N

        clave.setName("clave"); // NOI18N
        clave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                claveFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                claveFocusLost(evt);
            }
        });

        descripcion.setName("descripcion"); // NOI18N
        descripcion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                descripcionFocusGained(evt);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        medidareal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        medidareal.setText(resourceMap.getString("medidareal.text")); // NOI18N
        medidareal.setName("medidareal"); // NOI18N

        pesom2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.000"))));
        pesom2.setText(resourceMap.getString("pesom2.text")); // NOI18N
        pesom2.setName("pesom2"); // NOI18N
        pesom2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pesom2FocusGained(evt);
            }
        });

        valor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        valor.setText(resourceMap.getString("valor.text")); // NOI18N
        valor.setName("valor"); // NOI18N
        valor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                valorFocusGained(evt);
            }
        });

        pesom2real.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.000"))));
        pesom2real.setText(resourceMap.getString("pesom2real.text")); // NOI18N
        pesom2real.setName("pesom2real"); // NOI18N
        pesom2real.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pesom2realFocusGained(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mullen", "ECT" }));
        tipo.setName("tipo"); // NOI18N
        tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoActionPerformed(evt);
            }
        });

        medidares.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        medidares.setText(resourceMap.getString("medidares.text")); // NOI18N
        medidares.setName("medidares"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel2.border.titleFont"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        l1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        l1.setText(resourceMap.getString("l1.text")); // NOI18N
        l1.setName("l1"); // NOI18N
        l1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                l1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                l1FocusLost(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        r1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r1.setText(resourceMap.getString("r1.text")); // NOI18N
        r1.setName("r1"); // NOI18N
        r1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                r1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                r1FocusLost(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        l2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        l2.setText(resourceMap.getString("l2.text")); // NOI18N
        l2.setName("l2"); // NOI18N
        l2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                l2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                l2FocusLost(evt);
            }
        });

        r2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r2.setText(resourceMap.getString("r2.text")); // NOI18N
        r2.setName("r2"); // NOI18N
        r2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                r2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                r2FocusLost(evt);
            }
        });

        l3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        l3.setText(resourceMap.getString("l3.text")); // NOI18N
        l3.setName("l3"); // NOI18N
        l3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                l3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                l3FocusLost(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        f1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "C", "B" }));
        f1.setName("f1"); // NOI18N

        f2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "B", " ", "C" }));
        f2.setSelectedIndex(1);
        f2.setName("f2"); // NOI18N

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(r1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(l2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(f1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(r2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(l3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(f1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)))
        );

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        color.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kraft", "Blanco" }));
        color.setName("color"); // NOI18N

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        corrugado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Single", "Sencillo", "Doble" }));
        corrugado.setSelectedIndex(1);
        corrugado.setName("corrugado"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BOTTOM, resourceMap.getFont("jPanel3.border.titleFont"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        l1r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        l1r.setName("l1r"); // NOI18N
        l1r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                l1rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                l1rFocusLost(evt);
            }
        });
        l1r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l1rKeyPressed(evt);
            }
        });

        r1r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r1r.setName("r1r"); // NOI18N
        r1r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                r1rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                r1rFocusLost(evt);
            }
        });
        r1r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                r1rKeyPressed(evt);
            }
        });

        l2r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        l2r.setName("l2r"); // NOI18N
        l2r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                l2rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                l2rFocusLost(evt);
            }
        });
        l2r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l2rKeyPressed(evt);
            }
        });

        r2r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r2r.setName("r2r"); // NOI18N
        r2r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                r2rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                r2rFocusLost(evt);
            }
        });
        r2r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                r2rKeyPressed(evt);
            }
        });

        l3r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        l3r.setName("l3r"); // NOI18N
        l3r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                l3rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                l3rFocusLost(evt);
            }
        });
        l3r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l3rKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(l1r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(r1r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l2r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(r2r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l3r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(l1r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(r1r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(l2r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(r2r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(l3r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        valorreal.setEditable(false);
        valorreal.setFont(resourceMap.getFont("valorreal.font")); // NOI18N
        valorreal.setText(resourceMap.getString("valorreal.text")); // NOI18N
        valorreal.setFocusable(false);
        valorreal.setName("valorreal"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        preciokg.setEditable(false);
        preciokg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        preciokg.setText(resourceMap.getString("preciokg.text")); // NOI18N
        preciokg.setToolTipText(resourceMap.getString("preciokg.toolTipText")); // NOI18N
        preciokg.setFocusable(false);
        preciokg.setName("preciokg"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        preciom2.setEditable(false);
        preciom2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        preciom2.setText(resourceMap.getString("preciom2.text")); // NOI18N
        preciom2.setFocusable(false);
        preciom2.setName("preciom2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(corrugado, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(medidares, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(pesom2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(valor, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pesom2real, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(descripcion)
                            .addComponent(clave)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(preciokg, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(preciom2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(medidareal, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valorreal, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10)
                            .addComponent(pesom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(medidares)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pesom2real, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(corrugado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(medidareal)
                    .addComponent(valorreal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(preciokg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(preciom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        clave.setText(clave.getText().toUpperCase());
        String tipocorr=(String)corrugado.getSelectedItem();

        //valida campos vacios para las diferentes combinaciones
        int camposvaciospapel=0;
        if(tipocorr.equals("Single") && (l1r.getText().equals("") || r1r.getText().equals("")) )
            camposvaciospapel=1;
        if(tipocorr.equals("Sencillo") && (l1r.getText().equals("") || r1r.getText().equals("") || l2r.getText().equals("")))
            camposvaciospapel=1;
        if(tipocorr.equals("Doble") && (l1r.getText().equals("") || r1r.getText().equals("") || l2r.getText().equals("") || r2r.getText().equals("") || l3r.getText().equals("")))
            camposvaciospapel=1;

        if(clave.getText().equals("")||descripcion.getText().equals("")||pesom2real.getText().equals("")||pesom2.getText().equals("")||valor.getText().equals("")||l1.getText().equals("")||r1.getText().equals("")||camposvaciospapel==1){
            String err="VERIFICA HAY CAMPOS VACIOS";
            if(camposvaciospapel==1)
                err="CAMPOS VACIOS PARA COMBINACION CORRUGADO "+tipocorr.toUpperCase();

            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String tipores=(String)tipo.getSelectedItem();
            Double valfinal=0.0;
            if(tipores.equals("Mullen")){//tipo de resistencia calculo para el mullen///////////////////
                valfinal=(l1m+l2m)*0.07;
                if(tipocorr.equals("Sencillo"))
                    valfinal=(l1m+l2m)*0.07;
                if(tipocorr.equals("Doble"))
                    valfinal=(l1m+l2m+l3m)*0.07;
            }else{//tipo de resistencia calculo para el ECT////////////////
                if(tipocorr.equals("Sencillo"))
                    valfinal=(((l1c+r1c+l2c)/6)*0.8)+12;
                if(tipocorr.equals("Doble"))
                    valfinal=(((l1c+r1c+l2c+r2c+l3c)/6)*0.8)+12;
            }
            pesom2real.setText(fijo3decimales.format(l1p+r1p+l2p+r2p+l3p));
            valorreal.setText(fijo2decimales.format(valfinal));

            Double preciom2final=(l1p*l1pu)+(r1p*r1pu)+(l2p*l2pu)+(r2p*r2pu)+(l3p*l3pu);
            preciom2.setText( fijo2decimales.format(preciom2final) );
            preciokg.setText( fijo2decimales.format(preciom2final/(l1p+r1p+l2p+r2p+l3p)) );

            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR!!\n<html><font size=5 color=#DC143C><b>Peso real M2: </b>"+pesom2real.getText()+"<br><b>"+tipores+": </b>"+fijo2decimales.format(valfinal)+"<br></font></html>"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION);


            if (respuesta == JOptionPane.YES_OPTION) {
                String senSQL="UPDATE resistencias SET clave='"+clave.getText()+"', descripcion='"+descripcion.getText()+"', pesom2='"+pesom2.getText()+"', pesom2real='"+pesom2real.getText()+"', tipo='"+(String)tipo.getSelectedItem()+"', valorminimo='"+valor.getText()+"', l1='"+l1.getText()+"', r1='"+r1.getText()+"', l2='"+l2.getText()+"', r2='"+r2.getText()+"', l3='"+l3.getText()+"', f1='"+(String)f1.getSelectedItem()+"', f2='"+(String)f2.getSelectedItem()+"', color='"+(String)color.getSelectedItem()+"', corrugado='"+(String)corrugado.getSelectedItem()+"', l1r='"+l1r.getText()+"', r1r='"+r1r.getText()+"', l2r='"+l2r.getText()+"', r2r='"+r2r.getText()+"', l3r='"+l3r.getText()+"', valorreal='"+fijo2decimales.format(valfinal)+"', precio_kg='"+preciokg.getText()+"', precio_m2='"+preciom2.getText()+"' WHERE id_resistencia='"+id.getText()+"';";
                if(id.getText().equals("")){
                    senSQL="INSERT INTO resistencias(clave, descripcion, pesom2, pesom2real, tipo, valorminimo, l1, r1, l2, r2, l3, f1, f2,color,corrugado, l1r, r1r, l2r, r2r, l3r,valorreal,precio_kg,precio_m2) VALUES('"+clave.getText()+"','"+descripcion.getText()+"','"+pesom2.getText()+"','"+pesom2real.getText()+"','"+(String)tipo.getSelectedItem()+"','"+valor.getText()+"','"+l1.getText()+"','"+r1.getText()+"','"+l2.getText()+"','"+r2.getText()+"','"+l3.getText()+"','"+(String)f1.getSelectedItem()+"','"+(String)f2.getSelectedItem()+"','"+(String)color.getSelectedItem()+"','"+(String)corrugado.getSelectedItem()+"','"+l1r.getText()+"','"+r1r.getText()+"','"+l2r.getText()+"','"+r2r.getText()+"','"+l3r.getText()+"','"+fijo2decimales.format(valfinal)+"','"+preciokg.getText()+"','"+preciom2.getText()+"');";
                }
                conexion.modifica_p(senSQL,connj,valor_privilegio);
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

    private void claveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveFocusGained
        // TODO add your handling code here:
        clave.selectAll();
}//GEN-LAST:event_claveFocusGained

    private void descripcionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descripcionFocusGained
        // TODO add your handling code here:
        descripcion.selectAll();
}//GEN-LAST:event_descripcionFocusGained

    private void pesom2realFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesom2realFocusGained
        // TODO add your handling code here:
        pesom2real.selectAll();
}//GEN-LAST:event_pesom2realFocusGained

    private void pesom2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesom2FocusGained
        // TODO add your handling code here:
        pesom2.selectAll();
}//GEN-LAST:event_pesom2FocusGained

    private void valorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valorFocusGained
        // TODO add your handling code here:
        valor.selectAll();
}//GEN-LAST:event_valorFocusGained

    private void claveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveFocusLost
        // TODO add your handling code here:
        clave.setText(clave.getText().toUpperCase());
        if(clave.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM resistencias WHERE (id_resistencia<>'"+id.getText()+"' and clave='"+clave.getText()+"')";
                if(id.getText().equals("")){
                    senSQL="SELECT * FROM resistencias WHERE clave='"+clave.getText()+"'";
                }

                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    JOptionPane.showMessageDialog(this,"LA CLAVE DE LA RESISTENCIA YA EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    clave.setText("");
                    clave.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }//GEN-LAST:event_claveFocusLost

    private void l1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l1FocusLost
        // TODO add your handling code here:
        l1.setText(l1.getText().toUpperCase());
    }//GEN-LAST:event_l1FocusLost

    private void l1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l1FocusGained
        // TODO add your handling code here:
        l1.selectAll();
    }//GEN-LAST:event_l1FocusGained

    private void r1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r1FocusGained
        // TODO add your handling code here:
        r1.selectAll();
    }//GEN-LAST:event_r1FocusGained

    private void l2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l2FocusGained
        // TODO add your handling code here:
        l2.selectAll();
    }//GEN-LAST:event_l2FocusGained

    private void r2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r2FocusGained
        // TODO add your handling code here:
        r2.selectAll();
    }//GEN-LAST:event_r2FocusGained

    private void l3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l3FocusGained
        // TODO add your handling code here:
        l3.selectAll();
    }//GEN-LAST:event_l3FocusGained

    private void r1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r1FocusLost
        // TODO add your handling code here:
        r1.setText(r1.getText().toUpperCase());
    }//GEN-LAST:event_r1FocusLost

    private void l2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l2FocusLost
        // TODO add your handling code here:
        l2.setText(l2.getText().toUpperCase());
    }//GEN-LAST:event_l2FocusLost

    private void r2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r2FocusLost
        // TODO add your handling code here:
        r2.setText(r2.getText().toUpperCase());
    }//GEN-LAST:event_r2FocusLost

    private void l3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l3FocusLost
        // TODO add your handling code here:
        l3.setText(l3.getText().toUpperCase());
    }//GEN-LAST:event_l3FocusLost

    private void l1rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l1rFocusGained
        // TODO add your handling code here:
        l1r.selectAll();
}//GEN-LAST:event_l1rFocusGained

    private void l1rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l1rFocusLost
        // TODO add your handling code here:
        datospapel1("");
}//GEN-LAST:event_l1rFocusLost

    private void r1rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r1rFocusGained
        // TODO add your handling code here:
        r1r.selectAll();
}//GEN-LAST:event_r1rFocusGained

    private void r1rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r1rFocusLost
        // TODO add your handling code here:
        datospapel2("");
}//GEN-LAST:event_r1rFocusLost

    private void l2rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l2rFocusGained
        // TODO add your handling code here:
        l2r.selectAll();
}//GEN-LAST:event_l2rFocusGained

    private void l2rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l2rFocusLost
        // TODO add your handling code here:
        datospapel3("");
}//GEN-LAST:event_l2rFocusLost

    private void r2rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r2rFocusGained
        // TODO add your handling code here:
        r2r.selectAll();
}//GEN-LAST:event_r2rFocusGained

    private void r2rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r2rFocusLost
        // TODO add your handling code here:
        datospapel4("");
}//GEN-LAST:event_r2rFocusLost

    private void l3rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l3rFocusGained
        // TODO add your handling code here:
        l3r.selectAll();
}//GEN-LAST:event_l3rFocusGained

    private void l3rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l3rFocusLost
        // TODO add your handling code here:
        datospapel5("");
}//GEN-LAST:event_l3rFocusLost

    private void tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoActionPerformed
        // TODO add your handling code here:
        String tipor=(String)tipo.getSelectedItem();
        if(tipor.equals("Mullen")){
            medidares.setText("Valor Min. (Kg/cm2):");
            medidareal.setText("Valor Real (Kg/cm2):");
        }else{
            medidares.setText("Valor Min. (lib/in):");
            medidareal.setText("Valor Real (lib/in):");
        }
    }//GEN-LAST:event_tipoActionPerformed

    private void l1rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l1rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
          datospapel1("F7");
        }
    }//GEN-LAST:event_l1rKeyPressed

    private void r1rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_r1rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
          datospapel2("F7");
        }
    }//GEN-LAST:event_r1rKeyPressed

    private void l2rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l2rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
          datospapel3("F7");
        }
    }//GEN-LAST:event_l2rKeyPressed

    private void r2rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_r2rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
          datospapel4("F7");
        }
    }//GEN-LAST:event_r2rKeyPressed

    private void l3rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l3rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
          datospapel5("F7");
        }
    }//GEN-LAST:event_l3rKeyPressed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JTextField clave;
    private javax.swing.JComboBox color;
    private javax.swing.JComboBox corrugado;
    private javax.swing.JTextField descripcion;
    private javax.swing.JComboBox f1;
    private javax.swing.JComboBox f2;
    private javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JTextField l1;
    private javax.swing.JTextField l1r;
    private javax.swing.JTextField l2;
    private javax.swing.JTextField l2r;
    private javax.swing.JTextField l3;
    private javax.swing.JTextField l3r;
    private javax.swing.JLabel medidareal;
    private javax.swing.JLabel medidares;
    private javax.swing.JFormattedTextField pesom2;
    private javax.swing.JFormattedTextField pesom2real;
    private javax.swing.JTextField preciokg;
    private javax.swing.JTextField preciom2;
    private javax.swing.JTextField r1;
    private javax.swing.JTextField r1r;
    private javax.swing.JTextField r2;
    private javax.swing.JTextField r2r;
    private javax.swing.JComboBox tipo;
    private javax.swing.JFormattedTextField valor;
    private javax.swing.JTextField valorreal;
    // End of variables declaration//GEN-END:variables

}
