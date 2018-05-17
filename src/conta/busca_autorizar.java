/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * busca_fechasproveedor.java
 *
 * Created on 10/02/2010, 09:45:29 AM
 */

package conta;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author ANGEL
 */
public class busca_autorizar extends javax.swing.JDialog {
    String estado="cancelar";
    private Properties conf;
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    Connection connj;
    ResultSet rs0=null;

    /** Creates new form busca_fechasproveedor */
    public busca_autorizar(java.awt.Frame parent, boolean modal,Connection conn) {
        super(parent, modal);
        initComponents();
        conf=conexion.archivoInicial();
        connj=conn;
    }
    public void salir(){
        dispose();
        this.setVisible(false);
    }
    public String getEstado() {
        return estado;
    }
    public String getMotivo() {
        return motivo.getText();
    }
    public String getUser() {
        return user.getText();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pass = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        motivo = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        user = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(busca_autorizar.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setName("jPanel2"); // NOI18N

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        pass.setText(resourceMap.getString("pass.text")); // NOI18N
        pass.setName("pass"); // NOI18N
        pass.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passFocusGained(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        motivo.setColumns(20);
        motivo.setFont(resourceMap.getFont("motivo.font")); // NOI18N
        motivo.setRows(5);
        motivo.setName("motivo"); // NOI18N
        jScrollPane1.setViewportView(motivo);

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        user.setText(resourceMap.getString("user.text")); // NOI18N
        user.setName("user"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(user, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                            .addComponent(pass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        String contra=new String(pass.getPassword());
        try{
            if(contra.equals("") || motivo.getText().equals("") || user.getText().equals("")){
                JOptionPane.showMessageDialog(this, "CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                /**convierte la cadena a md5*/
                try{
                    contra=conexion.MD5(contra);
                    }
                catch (NoSuchAlgorithmException e) {  JOptionPane.showMessageDialog(this,"ERROR EN ALGORITMO\n"+e,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);        }
                catch (UnsupportedEncodingException e) {  JOptionPane.showMessageDialog(this,"ERROR EN ALGORITMO\n"+e,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);        }

                rs0=null;
                try{
                        String senSQL="SELECT * FROM usuarios WHERE (usuario='"+user.getText()+"' and pass='"+contra+"' and activo<>'No' and autoriza='true')";
                        rs0=conexion.consulta(senSQL,connj);

                        if(rs0.next()){
                            /**privilegios del servidor*/
                            estado="aceptar";
                            salir();
                        }else{
                            JOptionPane.showMessageDialog(this,"USUARIO Y PASSWORD INCORRECTO O NO HABILITADO PARA ESTA ACCION","A D V E R T E N C I A  !!!!!!!!!!",JOptionPane.WARNING_MESSAGE);
                        }
                        if(rs0!=null) {  rs0.close();  }

                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "FECHAS INCORRECTAS  "+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
        
}//GEN-LAST:event_btnaceptarActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        estado="cancelar";
        salir();
}//GEN-LAST:event_btncancelarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        estado="cancelar";
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void passFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passFocusGained
        // TODO add your handling code here:
        pass.selectAll();
    }//GEN-LAST:event_passFocusGained

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea motivo;
    private javax.swing.JPasswordField pass;
    private javax.swing.JTextField user;
    // End of variables declaration//GEN-END:variables

}