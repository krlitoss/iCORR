/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * datos_servidor.java
 *
 * Created on 17/01/2010, 08:56:48 AM
 */

package conta;

import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author IVONNE
 */
public class datos_servidor extends javax.swing.JDialog {
    private Properties conf;

    /** Creates new form datos_servidor */
    public datos_servidor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        configuracion();
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);
    }
    public void salir(){
        dispose();
        setVisible(false);
    }
    public void configuracion(){
        try{
            conf=conexion.archivoInicial();
            driver.setSelectedItem(conf.getProperty("Drivers"));
            servidor.setSelectedItem(conf.getProperty("Servidor"));
            puerto.setText(conf.getProperty("Puerto"));
            bd.setText(conf.getProperty("BD"));
            usuario.setText(conf.getProperty("UsuarioBD"));
            clave.setText(conexion.decodificarCadena(conf.getProperty("PassBD")));
        }
        catch(Exception ex){JOptionPane.showMessageDialog(this,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

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
        jLabel2 = new javax.swing.JLabel();
        driver = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        puerto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        usuario = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        clave = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        bd = new javax.swing.JTextField();
        servidor = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_servidor.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Form"); // NOI18N
        setResizable(false);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        driver.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "org.postgresql.Driver" }));
        driver.setName("driver"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        puerto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        puerto.setText(resourceMap.getString("puerto.text")); // NOI18N
        puerto.setName("puerto"); // NOI18N
        puerto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                puertoFocusGained(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        usuario.setText(resourceMap.getString("usuario.text")); // NOI18N
        usuario.setName("usuario"); // NOI18N
        usuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usuarioFocusGained(evt);
            }
        });

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        clave.setText(resourceMap.getString("clave.text")); // NOI18N
        clave.setName("clave"); // NOI18N
        clave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                claveFocusGained(evt);
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
                .addGap(41, 41, 41)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        bd.setText(resourceMap.getString("bd.text")); // NOI18N
        bd.setName("bd"); // NOI18N
        bd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bdFocusGained(evt);
            }
        });

        servidor.setEditable(true);
        servidor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "localhost", "192.168.42.15" }));
        servidor.setName("servidor"); // NOI18N
        servidor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                servidorFocusGained(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bd, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(driver, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(servidor, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(puerto)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(driver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(puerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(servidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(bd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
    String contra=new String(clave.getPassword());
    String serv=(String) servidor.getSelectedItem();
    if(serv.equals("") || puerto.getText().equals("") || bd.getText().equals("") || usuario.getText().equals("") || contra.equals(""))
    {
        JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
    }else{
        /**generar archivo*/
        conexion.escribir("Drivers", (String)driver.getSelectedItem());
        conexion.escribir("Servidor", (String)servidor.getSelectedItem());
        conexion.escribir("Puerto", puerto.getText());
        conexion.escribir("BD", bd.getText());
        conexion.escribir("UsuarioBD", usuario.getText());
        conexion.escribir("PassBD", contra);
        salir();
    }

}//GEN-LAST:event_btnaceptarActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        salir();
}//GEN-LAST:event_btncancelarActionPerformed

    private void bdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bdFocusGained
        // TODO add your handling code here:
        bd.selectAll();
    }//GEN-LAST:event_bdFocusGained

    private void usuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usuarioFocusGained
        // TODO add your handling code here:
        usuario.selectAll();
    }//GEN-LAST:event_usuarioFocusGained

    private void puertoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_puertoFocusGained
        // TODO add your handling code here:
        puerto.selectAll();
    }//GEN-LAST:event_puertoFocusGained

    private void claveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveFocusGained
        // TODO add your handling code here:
        clave.selectAll();
    }//GEN-LAST:event_claveFocusGained

    private void servidorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_servidorFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_servidorFocusGained

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bd;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JPasswordField clave;
    private javax.swing.JComboBox driver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField puerto;
    private javax.swing.JComboBox servidor;
    private javax.swing.JTextField usuario;
    // End of variables declaration//GEN-END:variables

}
