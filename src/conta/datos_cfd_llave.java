/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * empresa.java
 *
 * Created on 21/01/2010, 12:26:55 PM
 */

package conta;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import mx.bigdata.sat.security.KeyLoader;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author ANGEL
 */
public class datos_cfd_llave extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    String valor_privilegio="1";

    /** Creates new form empresa */
    public datos_cfd_llave(java.awt.Frame parent, boolean modal,Connection conn) {
        super(parent, modal);
        initComponents();
        connj=conn;
        configuracion();
        datos_privilegios();
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
    public void datos_privilegios(){
        valor_privilegio=conexion.obtener_privilegios(connj,"Llave");
    }
    public void configuracion(){

        rs0=null;
        try{
            String senSQL="SELECT id,nombre,nombrecomercial FROM empresa WHERE id='1'";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                id.setText(rs0.getString("id"));
                razon.setText(rs0.getString("nombre"));
                nombrec.setText(rs0.getString("nombrecomercial"));
            }
            if(rs0!=null) {
                rs0.close();
            }

        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        
    }
    private byte[] getBytes(InputStream is) {
        /*int totalBytes = 2048;
        byte[] buffer = null;
        try {
        buffer = new byte[totalBytes];
        is.read(buffer);
        is.close();
        } catch (IOException e) {
        e.printStackTrace();
        }
        return buffer;*/
        int len;
        int size = 1024;
        byte[] buf=null;

        try {
            if (is instanceof ByteArrayInputStream) {
                size = is.available();
                buf = new byte[size];
                len = is.read(buf, 0, size);
            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                buf = new byte[size];
                while ((len = is.read(buf, 0, size)) != -1) {
                    bos.write(buf, 0, len);
                }
                buf = bos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openllave = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        razon = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        nombrec = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rutallave = new javax.swing.JTextField();
        btnllave = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        password2 = new javax.swing.JPasswordField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_cfd_llave.class);
        openllave.setDialogTitle(resourceMap.getString("openllave.dialogTitle")); // NOI18N
        openllave.setName("openllave"); // NOI18N

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

        id.setEditable(false);
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setText(resourceMap.getString("id.text")); // NOI18N
        id.setFocusable(false);
        id.setName("id"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        razon.setColumns(2);
        razon.setEditable(false);
        razon.setText(resourceMap.getString("razon.text")); // NOI18N
        razon.setFocusable(false);
        razon.setName("razon"); // NOI18N
        razon.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                razonFocusGained(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

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
                .addContainerGap(137, Short.MAX_VALUE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        nombrec.setEditable(false);
        nombrec.setText(resourceMap.getString("nombrec.text")); // NOI18N
        nombrec.setFocusable(false);
        nombrec.setName("nombrec"); // NOI18N
        nombrec.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nombrecFocusGained(evt);
            }
        });

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        rutallave.setEditable(false);
        rutallave.setText(resourceMap.getString("rutallave.text")); // NOI18N
        rutallave.setFocusable(false);
        rutallave.setName("rutallave"); // NOI18N

        btnllave.setIcon(resourceMap.getIcon("btnllave.icon")); // NOI18N
        btnllave.setText(resourceMap.getString("btnllave.text")); // NOI18N
        btnllave.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnllave.setName("btnllave"); // NOI18N
        btnllave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnllaveActionPerformed(evt);
            }
        });

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        password.setText(resourceMap.getString("password.text")); // NOI18N
        password.setName("password"); // NOI18N
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        password2.setText(resourceMap.getString("password2.text")); // NOI18N
        password2.setName("password2"); // NOI18N
        password2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                password2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                password2FocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(rutallave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnllave, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(razon)
                            .addComponent(nombrec, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(password2)
                            .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(razon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nombrec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(password2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(rutallave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnllave, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        String pass=new String(password.getPassword());
        if(rutallave.getText().equals("") || pass.equals("") || !new String(password.getPassword()).equals(new String(password2.getPassword()))){
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            File f = new File(rutallave.getText());
            String fileExtension=f.getName();
            int dotPos = fileExtension.lastIndexOf(".")+1;
            String extension = fileExtension.substring(dotPos);
            if(extension.equals("key"))
            {
                try{

                    InputStream keyInputStream = new FileInputStream(rutallave.getText());
                    byte[] clavePrivada = getBytes(keyInputStream);
                    String codPK = new String(Base64.encodeBase64(clavePrivada));

                    String senSQL="UPDATE empresa set passwordllave='"+conexion.codificarCadena(new String(password.getPassword()))+"',llaveprivada='"+codPK+"' WHERE id='"+id.getText()+"'";
                    conexion.modifica_p(senSQL,connj,valor_privilegio);
                    salir();
                     
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE GUARDAR LA LLAVE"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

            }else{
                JOptionPane.showMessageDialog(this, "EL ARCHIVO NO TIENE LA EXTENSION CORRECTA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
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

    private void razonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_razonFocusGained
        // TODO add your handling code here:
        razon.selectAll();
}//GEN-LAST:event_razonFocusGained

    private void nombrecFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nombrecFocusGained
        // TODO add your handling code here:
        nombrec.selectAll();
}//GEN-LAST:event_nombrecFocusGained

    private void btnllaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnllaveActionPerformed
        // TODO add your handling code here:
        FileNameExtensionFilter filter = new FileNameExtensionFilter("key", "key");
        openllave.setFileFilter(filter);
        if(openllave.showDialog(this, null)==JFileChooser.APPROVE_OPTION){
            rutallave.setText(String.valueOf(openllave.getSelectedFile()));
        }else{
            rutallave.setText("");
        }
}//GEN-LAST:event_btnllaveActionPerformed

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        // TODO add your handling code here:
        password.selectAll();
    }//GEN-LAST:event_passwordFocusGained

    private void password2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_password2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_password2FocusLost

    private void password2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_password2FocusGained
        // TODO add your handling code here:
        password2.selectAll();
    }//GEN-LAST:event_password2FocusGained

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnllave;
    private javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nombrec;
    private javax.swing.JFileChooser openllave;
    private javax.swing.JPasswordField password;
    private javax.swing.JPasswordField password2;
    private javax.swing.JTextField razon;
    private javax.swing.JTextField rutallave;
    // End of variables declaration//GEN-END:variables

}