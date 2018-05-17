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
public class datos_mullen_ect extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales=new DecimalFormat("######0.000");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");

    Double l1m=0.0,l2m=0.0,l3m=0.0;
    Double l1c=0.0,r1c=0.0,l2c=0.0,r2c=0.0,l3c=0.0;
    Double l1p=0.0,l2p=0.0,l3p=0.0,r1p=0.0,r2p=0.0;
    Double l1pu=0.0,l2pu=0.0,l3pu=0.0,r1pu=0.0,r2pu=0.0;
    

    /** Creates new form datos_usuarios */
    public datos_mullen_ect(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica) {
        super(parent, modal);
        initComponents();
        connj=conn;
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

        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        pesom2real = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        l1r = new javax.swing.JTextField();
        r1r = new javax.swing.JTextField();
        l2r = new javax.swing.JTextField();
        r2r = new javax.swing.JTextField();
        l3r = new javax.swing.JTextField();
        f2 = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        f1 = new javax.swing.JComboBox();
        corrugado = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        mullen = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        ect = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        preciom2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        preciokg = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_mullen_ect.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

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

        jLabel13.setFont(resourceMap.getFont("jLabel13.font")); // NOI18N
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        pesom2real.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.000"))));
        pesom2real.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pesom2real.setText(resourceMap.getString("pesom2real.text")); // NOI18N
        pesom2real.setFont(resourceMap.getFont("pesom2real.font")); // NOI18N
        pesom2real.setName("pesom2real"); // NOI18N
        pesom2real.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pesom2realFocusGained(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel2.border.titleFont"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

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

        f2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "B", " ", "C" }));
        f2.setSelectedIndex(1);
        f2.setName("f2"); // NOI18N

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        f1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "C", "B" }));
        f1.setName("f1"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(l1r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(r1r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l2r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(f1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(r2r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l3r, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l1r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l2r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l3r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(f1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(f2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15))))
        );

        corrugado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Single", "Sencillo", "Doble" }));
        corrugado.setSelectedIndex(1);
        corrugado.setName("corrugado"); // NOI18N
        corrugado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                corrugadoActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        mullen.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        mullen.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mullen.setFont(resourceMap.getFont("mullen.font")); // NOI18N
        mullen.setName("mullen"); // NOI18N
        mullen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mullenFocusGained(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        ect.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        ect.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ect.setFont(resourceMap.getFont("ect.font")); // NOI18N
        ect.setName("ect"); // NOI18N
        ect.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ectFocusGained(evt);
            }
        });

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        preciom2.setFont(resourceMap.getFont("preciom2.font")); // NOI18N
        preciom2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        preciom2.setText(resourceMap.getString("preciom2.text")); // NOI18N
        preciom2.setName("preciom2"); // NOI18N
        preciom2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                preciom2FocusGained(evt);
            }
        });

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        preciokg.setFont(resourceMap.getFont("preciokg.font")); // NOI18N
        preciokg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        preciokg.setText(resourceMap.getString("preciokg.text")); // NOI18N
        preciokg.setName("preciokg"); // NOI18N
        preciokg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                preciokgFocusGained(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(corrugado, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pesom2real, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mullen, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ect, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(preciom2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(preciokg, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(corrugado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(pesom2real, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(mullen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(preciom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(preciokg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:

        String tipocorr=(String)corrugado.getSelectedItem();

        //valida campos vacios para las diferentes combinaciones
        int camposvaciospapel=0;
        if(tipocorr.equals("Single") && (l1r.getText().equals("") || r1r.getText().equals("")) )
            camposvaciospapel=1;
        if(tipocorr.equals("Sencillo") && (l1r.getText().equals("") || r1r.getText().equals("") || l2r.getText().equals("")))
            camposvaciospapel=1;
        if(tipocorr.equals("Doble") && (l1r.getText().equals("") || r1r.getText().equals("") || l2r.getText().equals("") || r2r.getText().equals("") || l3r.getText().equals("")))
            camposvaciospapel=1;

        if(camposvaciospapel==1){
            String err="VERIFICA HAY CAMPOS VACIOS";
            if(camposvaciospapel==1)
                err="CAMPOS VACIOS PARA COMBINACION CORRUGADO "+tipocorr.toUpperCase();

            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{

            Double valfinalmullen=0.0;

            valfinalmullen=(l1m+l2m)*0.07;
            if(tipocorr.equals("Sencillo"))
                valfinalmullen=(l1m+l2m)*0.07;
            if(tipocorr.equals("Doble"))
                valfinalmullen=(l1m+l2m+l3m)*0.07;

            Double valfinalect=0.0;
            if(tipocorr.equals("Sencillo"))
                valfinalect=(((l1c+r1c+l2c)/6)*0.8)+12;
            if(tipocorr.equals("Doble"))
                valfinalect=(((l1c+r1c+l2c+r2c+l3c)/6)*0.8)+12;
            
            pesom2real.setText(fijo3decimales.format(l1p+r1p+l2p+r2p+l3p));
            mullen.setText(fijo2decimales.format(valfinalmullen));
            ect.setText(fijo2decimales.format(valfinalect));

            Double preciom2final=(l1p*l1pu)+(r1p*r1pu)+(l2p*l2pu)+(r2p*r2pu)+(l3p*l3pu);
            preciom2.setText( moneda2decimales.format(preciom2final) );
            preciokg.setText( moneda2decimales.format(preciom2final/(l1p+r1p+l2p+r2p+l3p)) );

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

    private void pesom2realFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesom2realFocusGained
        // TODO add your handling code here:
        pesom2real.selectAll();
}//GEN-LAST:event_pesom2realFocusGained

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

    private void mullenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mullenFocusGained
        // TODO add your handling code here:
        mullen.selectAll();
    }//GEN-LAST:event_mullenFocusGained

    private void ectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ectFocusGained
        // TODO add your handling code here:
        ect.selectAll();
    }//GEN-LAST:event_ectFocusGained

    private void preciom2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_preciom2FocusGained
        // TODO add your handling code here:
        preciom2.selectAll();
    }//GEN-LAST:event_preciom2FocusGained

    private void preciokgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_preciokgFocusGained
        // TODO add your handling code here:
        preciom2.selectAll();
    }//GEN-LAST:event_preciokgFocusGained

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

    private void corrugadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_corrugadoActionPerformed
        // TODO add your handling code here:
        String corr=(String)corrugado.getSelectedItem();
        if(corr.equals("Doble")){
            f1.setSelectedIndex(0);
            f2.setSelectedIndex(0);
        }else{
            f1.setSelectedIndex(0);
            f2.setSelectedIndex(1);
        }
    }//GEN-LAST:event_corrugadoActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JComboBox corrugado;
    private javax.swing.JFormattedTextField ect;
    private javax.swing.JComboBox f1;
    private javax.swing.JComboBox f2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JTextField l1r;
    private javax.swing.JTextField l2r;
    private javax.swing.JTextField l3r;
    private javax.swing.JFormattedTextField mullen;
    private javax.swing.JFormattedTextField pesom2real;
    private javax.swing.JTextField preciokg;
    private javax.swing.JTextField preciom2;
    private javax.swing.JTextField r1r;
    private javax.swing.JTextField r2r;
    // End of variables declaration//GEN-END:variables

}
