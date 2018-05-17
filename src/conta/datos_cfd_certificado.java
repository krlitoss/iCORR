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

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.security.cert.X509Certificate;
import mx.bigdata.sat.security.KeyLoader;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author ANGEL
 */
public class datos_cfd_certificado extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    String valor_privilegio="1";

    /** Creates new form empresa */
    public datos_cfd_certificado(java.awt.Frame parent, boolean modal,Connection conn) {
        super(parent, modal);
        initComponents();
        connj=conn;
        datos_privilegios();
        configuracion();
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
        valor_privilegio=conexion.obtener_privilegios(connj,"Certificado");
    }
    public void configuracion(){

        rs0=null;
        try{
            String senSQL="SELECT id,nombre,nombrecomercial,nocertificado FROM empresa WHERE id='1'";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                id.setText(rs0.getString("id"));
                razon.setText(rs0.getString("nombre"));
                nombrec.setText(rs0.getString("nombrecomercial"));
                nocer.setText(rs0.getString("nocertificado"));
            }
            if(rs0!=null) {
                rs0.close();
            }

        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        
    }
    public String quitarSaltos(String c){
        c = c.replaceAll("\r", "");
        c = c.replaceAll("\n", "");
        return c;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        opencer = new javax.swing.JFileChooser();
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
        rutacer = new javax.swing.JTextField();
        btncertificado = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        nocer = new javax.swing.JTextField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_cfd_certificado.class);
        opencer.setDialogTitle(resourceMap.getString("opencer.dialogTitle")); // NOI18N
        opencer.setName("opencer"); // NOI18N

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

        rutacer.setEditable(false);
        rutacer.setText(resourceMap.getString("rutacer.text")); // NOI18N
        rutacer.setFocusable(false);
        rutacer.setName("rutacer"); // NOI18N

        btncertificado.setIcon(resourceMap.getIcon("btncertificado.icon")); // NOI18N
        btncertificado.setText(resourceMap.getString("btncertificado.text")); // NOI18N
        btncertificado.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btncertificado.setName("btncertificado"); // NOI18N
        btncertificado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncertificadoActionPerformed(evt);
            }
        });

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        nocer.setEditable(false);
        nocer.setText(resourceMap.getString("nocer.text")); // NOI18N
        nocer.setFocusable(false);
        nocer.setName("nocer"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(10, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nocer, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(razon)
                    .addComponent(nombrec, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rutacer, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btncertificado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(nocer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(rutacer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncertificado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        if(rutacer.getText().equals("")){
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            File f = new File(rutacer.getText());
            String fileExtension=f.getName();
            int dotPos = fileExtension.lastIndexOf(".")+1;
            String extension = fileExtension.substring(dotPos);
            if(extension.equals("cer"))
            {
                try{
                    FileInputStream s = new FileInputStream(f);
                    //funcion para obtener el numero de certificado
                    X509Certificate cert = KeyLoader.loadX509Certificate(s);
                    codec.asn1.ASN1Integer intSerial = new codec.asn1.ASN1Integer(cert.getSerialNumber());
                    java.io.ByteArrayOutputStream asnSalida = new java.io.ByteArrayOutputStream();
                    codec.asn1.DEREncoder encoder = new codec.asn1.DEREncoder(asnSalida);
                    String asnSerial = null;
                    intSerial.encode(encoder);
                    asnSerial = asnSalida.toString().trim();
                    asnSalida.close();
                    nocer.setText(""+asnSerial);
                    
                    String certificado = "";
                    certificado = quitarSaltos(Base64.encodeBase64String(cert.getEncoded()));
                    String senSQL="UPDATE empresa set nocertificado='"+asnSerial+"',certificado='"+certificado+"' WHERE id='"+id.getText()+"'";
                    conexion.modifica_p(senSQL,connj,valor_privilegio);
                    salir();
                     
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE GUARDAR EL CERTIFICADO"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

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

    private void btncertificadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncertificadoActionPerformed
        // TODO add your handling code here:
        FileNameExtensionFilter filter = new FileNameExtensionFilter("cer", "cer");
        opencer.setFileFilter(filter);
        if(opencer.showDialog(this, null)==JFileChooser.APPROVE_OPTION){
            rutacer.setText(String.valueOf(opencer.getSelectedFile()));
        }else{
            rutacer.setText("");
        }
}//GEN-LAST:event_btncertificadoActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btncertificado;
    private javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nocer;
    private javax.swing.JTextField nombrec;
    private javax.swing.JFileChooser opencer;
    private javax.swing.JTextField razon;
    private javax.swing.JTextField rutacer;
    // End of variables declaration//GEN-END:variables

}
