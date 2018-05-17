/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * usuarios.java
 *
 * Created on 17/01/2010, 09:38:56 PM
 */

package conta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author IVONNE
 */
public class solicitud_muestras extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot1=null;
    ResultSet rs0;
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    private Properties conf;
    String valor_privilegio="1";

    /** Creates new form usuarios */
    public solicitud_muestras(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf=conexion.archivoInicial();
        conn=connt;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        datos_privilegios();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datos();
    }

    public void salir(){
        if(conn!=null){
            conn=null;
        }
        dispose();
        this.setVisible(false);
    }
    public void datos_privilegios(){
        valor_privilegio=conexion.obtener_privilegios(conn,"Solicitud de muestras");
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(230);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(50);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(180);
        
    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos(){
        rs0=null;
        try{
            String senSQL="SELECT solmuestras.folio, MAX(solmuestras.cliente) as cliente, MAX(solmuestras.tipomuestra) as tipo, MAX(solmuestras.fecha) as fecha, MAX(solmuestras.partida) as partida, MAX(solmuestras.observaciones) as observaciones, MAX(solmuestras.solicitante) as solicitante, MAX(solmuestras.cancelado)as cancelado FROM solmuestras WHERE (solmuestras.cancelado='no' AND (solmuestras.fecha>='"+conf.getProperty("FechaIni")+"' AND solmuestras.fecha<='"+conf.getProperty("FechaFin")+"')) GROUP BY solmuestras.folio ORDER BY solmuestras.folio";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Object datos[]={rs0.getString("folio"),fechamediana.format(rs0.getDate("fecha")),rs0.getString("cliente"),rs0.getString("tipo"),rs0.getString("partida"),rs0.getString("solicitante")};
                modelot1.addRow(datos);
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    public void modificar(){
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String clavemodifica=(String)Tabladatos.getValueAt(filano, 0);
            if(clavemodifica.equals("")){

            }else{
                datos_solicitud_muestras = new datos_solicitud_muestras(null,true,conn,clavemodifica,valor_privilegio);
                datos_solicitud_muestras.setLocationRelativeTo(this);
                datos_solicitud_muestras.setVisible(true);
                limpiatabla();
                datos_solicitud_muestras=null;
                datos();
            }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnnuevo = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnmodificar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnconsultar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btneliminar = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(solicitud_muestras.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setFrameIcon(resourceMap.getIcon("Form.frameIcon")); // NOI18N
        setName("Form"); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha", "Nombre del Cliente", "Tipo", "Items", "Solicitante"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        Tabladatos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos.setName("Tabladatos"); // NOI18N
        Tabladatos.setRowHeight(22);
        Tabladatos.setShowHorizontalLines(false);
        Tabladatos.getTableHeader().setReorderingAllowed(false);
        Tabladatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabladatosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        jToolBar1.setBackground(resourceMap.getColor("jToolBar1.background")); // NOI18N
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        btnnuevo.setIcon(resourceMap.getIcon("btnnuevo.icon")); // NOI18N
        btnnuevo.setText(resourceMap.getString("btnnuevo.text")); // NOI18N
        btnnuevo.setToolTipText(resourceMap.getString("btnnuevo.toolTipText")); // NOI18N
        btnnuevo.setFocusable(false);
        btnnuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnnuevo.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnnuevo.setMaximumSize(new java.awt.Dimension(70, 48));
        btnnuevo.setMinimumSize(new java.awt.Dimension(70, 48));
        btnnuevo.setName("btnnuevo"); // NOI18N
        btnnuevo.setPreferredSize(new java.awt.Dimension(70, 48));
        btnnuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnnuevo);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

        btnmodificar.setIcon(resourceMap.getIcon("btnmodificar.icon")); // NOI18N
        btnmodificar.setText(resourceMap.getString("btnmodificar.text")); // NOI18N
        btnmodificar.setToolTipText(resourceMap.getString("btnmodificar.toolTipText")); // NOI18N
        btnmodificar.setFocusable(false);
        btnmodificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnmodificar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnmodificar.setMaximumSize(new java.awt.Dimension(70, 48));
        btnmodificar.setMinimumSize(new java.awt.Dimension(70, 48));
        btnmodificar.setName("btnmodificar"); // NOI18N
        btnmodificar.setPreferredSize(new java.awt.Dimension(70, 48));
        btnmodificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnmodificar);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        btnconsultar.setIcon(resourceMap.getIcon("btnconsultar.icon")); // NOI18N
        btnconsultar.setText(resourceMap.getString("btnconsultar.text")); // NOI18N
        btnconsultar.setToolTipText(resourceMap.getString("btnconsultar.toolTipText")); // NOI18N
        btnconsultar.setFocusable(false);
        btnconsultar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnconsultar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnconsultar.setMaximumSize(new java.awt.Dimension(70, 48));
        btnconsultar.setMinimumSize(new java.awt.Dimension(70, 48));
        btnconsultar.setName("btnconsultar"); // NOI18N
        btnconsultar.setPreferredSize(new java.awt.Dimension(70, 48));
        btnconsultar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnconsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnconsultarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnconsultar);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        btneliminar.setIcon(resourceMap.getIcon("btneliminar.icon")); // NOI18N
        btneliminar.setText(resourceMap.getString("btneliminar.text")); // NOI18N
        btneliminar.setToolTipText(resourceMap.getString("btneliminar.toolTipText")); // NOI18N
        btneliminar.setFocusable(false);
        btneliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btneliminar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btneliminar.setMaximumSize(new java.awt.Dimension(70, 48));
        btneliminar.setMinimumSize(new java.awt.Dimension(70, 48));
        btneliminar.setName("btneliminar"); // NOI18N
        btneliminar.setPreferredSize(new java.awt.Dimension(70, 48));
        btneliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });
        jToolBar1.add(btneliminar);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar1.add(jSeparator5);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(556, 48));

        buscar.setToolTipText(resourceMap.getString("buscar.toolTipText")); // NOI18N
        buscar.setName("buscar"); // NOI18N
        buscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarFocusGained(evt);
            }
        });
        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(201, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jToolBar1.add(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() ==2){
            modificar();
        }
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoActionPerformed
        // TODO add your handling code here:
        datos_solicitud_muestras = new datos_solicitud_muestras(null,true,conn,"",valor_privilegio);
        datos_solicitud_muestras.setLocationRelativeTo(this);
        datos_solicitud_muestras.setVisible(true);
        limpiatabla();
        datos_solicitud_muestras=null;
        datos();
        
}//GEN-LAST:event_btnnuevoActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        // TODO add your handling code here:
        modificar();
}//GEN-LAST:event_btnmodificarActionPerformed

    private void btnconsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnconsultarActionPerformed
        // TODO add your handling code here:
        busca_fechas busca_fechas = new busca_fechas(null,true);
        busca_fechas.setLocationRelativeTo(this);
        busca_fechas.setVisible(true);
        String estado=busca_fechas.getEstado();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if(estado.equals("cancelar")){

        }else{


            limpiatabla();
            rs0=null;
            try{
                String senSQL="SELECT solmuestras.folio, MAX(solmuestras.cliente) as cliente, MAX(solmuestras.tipomuestra) as tipo, MAX(solmuestras.fecha) as fecha, MAX(solmuestras.partida) as partida, MAX(solmuestras.observaciones) as observaciones, MAX(solmuestras.solicitante) as solicitante, MAX(solmuestras.cancelado)as cancelado FROM solmuestras WHERE (solmuestras.cancelado='no' AND (solmuestras.fecha>='"+fechainsertar.format(busca_fechas.getFechaini())+" 00:00:00' AND solmuestras.fecha<='"+fechainsertar.format(busca_fechas.getFechafin())+" 23:59:59')) GROUP BY solmuestras.folio ORDER BY solmuestras.folio";
                rs0=conexion.consulta(senSQL,conn);
                while(rs0.next()){
                    String datos[]={rs0.getString("folio"),fechamediana.format(rs0.getDate("fecha")),rs0.getString("cliente"),rs0.getString("tipo"),rs0.getString("partida"),rs0.getString("solicitante")};
                    modelot1.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }

        busca_fechas=null;
    }//GEN-LAST:event_btnconsultarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO DE ELIMINAR LA FILA  "+(filano+1)+" ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                /**funcion que actualiza el status de la orden de compra*/
                String senSQL="UPDATE solmuestras SET cancelado='Si' WHERE folio='"+(String)Tabladatos.getValueAt(filano, 0)+"';";
                conexion.modifica_p(senSQL,conn,valor_privilegio);
                limpiatabla();
                datos();
            }else{

            }
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
}//GEN-LAST:event_buscarFocusGained

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        // TODO add your handling code here:
        if(buscar.getText().equals("")){
            Tabladatos.setRowSorter(null);
            buscar.setText("");
            Tabladatos.setAutoCreateRowSorter(true);
            limpiatabla();
            datos();
        }else{
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladatos.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarKeyReleased

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btnconsultar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnnuevo;
    private javax.swing.JTextField buscar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_solicitud_muestras;
}