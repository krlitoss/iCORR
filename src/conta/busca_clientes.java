/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * busca_agentes.java
 *
 * Created on 26/01/2010, 05:27:05 PM
 */

package conta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author ANGEL
 */
public class busca_clientes extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    DefaultTableModel modelot1=null;
    String clavebusca="";

    /** Creates new form busca_agentes */
    public busca_clientes(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica) {
        super(parent, modal);
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo.png")).getImage());
        this.setLocationRelativeTo(null);
        connj=conn;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datos();
    }
    public void salir(){
        if(connj!=null){
            connj=null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(250);
    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM Clientes ORDER BY id_clientes";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                Object datos[]={rs0.getString("id_clientes"),rs0.getString("nombre")};
                modelot1.addRow(datos);
            }
            if(rs0!=null) { rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public String getText() {
        return clavebusca;
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
        buscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(busca_clientes.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        buscar.setBackground(resourceMap.getColor("buscar.background")); // NOI18N
        buscar.setName("buscar"); // NOI18N
        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "Nombre del Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
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
        Tabladatos.setName("Tabladatos"); // NOI18N
        Tabladatos.setRowHeight(20);
        Tabladatos.getTableHeader().setReorderingAllowed(false);
        Tabladatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabladatosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        // TODO add your handling code here:
        if(buscar.getText().equals("")){
            Tabladatos.setRowSorter(null);
            buscar.setText("");
            Tabladatos.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladatos.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarKeyReleased

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
            int filano=Tabladatos.getSelectedRow();
            clavebusca=(String)Tabladatos.getValueAt(filano, 0);
            salir();
}//GEN-LAST:event_TabladatosMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField buscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
