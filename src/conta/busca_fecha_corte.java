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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author ANGEL
 */
public class busca_fecha_corte extends javax.swing.JDialog {
    String estado="cancelar";
    private Properties conf;
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /** Creates new form busca_fechasproveedor */
    public busca_fecha_corte(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        conf=conexion.archivoInicial();
        fechaini.setDate(new Date());
        configuracion();
    }
    private void configuracion() {
        Date FI=fechaini.getDate();
        String ffi=""+fechainsertar.format(FI)+" 23:59:59";
        try{
            if(ffi.equals("null") || ffi.equals("")){

            }else{
                FI=fechainsertarhora.parse(ffi);
            }

        }catch(Exception e){
           JOptionPane.showMessageDialog(this, "ERROR EN LAS FECHAS"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
        fechaini.setDate(FI);
    }
    public void salir(){
        dispose();
        this.setVisible(false);
    }
    public String getEstado() {
        return estado;
    }
    public Date getFechaini() {
        return fechaini.getDate();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fechas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fechaini = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(busca_fecha_corte.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        fechas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("fechas.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("fechas.border.titleFont"))); // NOI18N
        fechas.setName("fechas"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        fechaini.setToolTipText(resourceMap.getString("fechaini.toolTipText")); // NOI18N
        fechaini.setDateFormatString(resourceMap.getString("fechaini.dateFormatString")); // NOI18N
        fechaini.setName("fechaini"); // NOI18N

        javax.swing.GroupLayout fechasLayout = new javax.swing.GroupLayout(fechas);
        fechas.setLayout(fechasLayout);
        fechasLayout.setHorizontalGroup(
            fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fechasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fechaini, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        fechasLayout.setVerticalGroup(
            fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fechasLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fechaini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

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
                .addContainerGap()
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fechas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fechas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        try{
            if(fechaini.getDate()==null){
                JOptionPane.showMessageDialog(this, "FECHAS INCORRECTAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                configuracion(); //vuelve a ajustar la fecha
                estado="aceptar";
                salir();
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

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private com.toedter.calendar.JDateChooser fechaini;
    private javax.swing.JPanel fechas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

}