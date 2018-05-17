/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * privilegios.java
 *
 * Created on 1/06/2010, 03:15:37 PM
 */

package conta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Enumeration;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author ANGEL
 */
public class privilegios extends javax.swing.JInternalFrame {
    Connection conn=null;
    private DefaultTreeModel modeltrp=null;
    private DefaultMutableTreeNode ra=null;
    ResultSet rs0=null,rs3=null;
    int menuu=0;
    String node_eliminados[] = new String[300];
    String valor_privilegio="1";

    /** Creates new form privilegios */
    public privilegios(Connection connt,DefaultMutableTreeNode ro) {
        initComponents();
        conn=connt;
        ra=ro;
        modeltrp = new DefaultTreeModel(ra);
        arbolp1.setModel(modeltrp);
        datos_privilegios();
        llenargrupos();
        llenarusuarios();
    }
    public void salir(){
        if(conn!=null){
            conn=null;
        }
        ColorNode();
        dispose();
        this.setVisible(false);
    }
    public void datos_privilegios(){
        valor_privilegio=conexion.obtener_privilegios(conn,"Grupos y permisos");
    }
    public DefaultMutableTreeNode searchNode(String nodeStr)
    {
        DefaultMutableTreeNode node = null;
        Enumeration enume = ra.breadthFirstEnumeration();
        while(enume.hasMoreElements())
        {
            node = (DefaultMutableTreeNode)enume.nextElement();
            if(nodeStr.equals(node.getUserObject().toString()))
            {
                return node;
            }
        }
        return null;
    }
    public void llenargrupos(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM gpousuarios WHERE activo='Si';";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                combogrupo.addItem(rs0.getObject("id_gpousuario"));
            }
            if(rs0!=null) {  rs0.close();   }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }

    public void llenarusuarios(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM usuarios WHERE (activo='Si' AND usuario<>'sksistemas');";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                combouser.addItem(rs0.getObject("usuario"));
            }
            if(rs0!=null) { rs0.close();  }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    public void ColorNode()
    {
        DefaultMutableTreeNode node = null;
        Enumeration enume = ra.breadthFirstEnumeration();
        while(enume.hasMoreElements())
        {
            node = (DefaultMutableTreeNode)enume.nextElement();
            String A=node.getUserObject().toString();
            if(A.length()>25){
                if(A.substring(24, 25).equals(">")){
                    arbolp1.setSelectionPath(new TreePath(node.getPath()));
                    modeltrp.valueForPathChanged(arbolp1.getSelectionPath(),A.substring(25));
                }
            }
        }

    }

    public void ColorRojoNode(String nodestr)
    {
        DefaultMutableTreeNode node = null;
        Enumeration enume = ra.breadthFirstEnumeration();
        while(enume.hasMoreElements())
        {
            node = (DefaultMutableTreeNode)enume.nextElement();
            if(nodestr.equals(node.getUserObject().toString()))
            {
                arbolp1.setSelectionPath(new TreePath(node.getPath()));
                modeltrp.valueForPathChanged(arbolp1.getLeadSelectionPath(),"<html><font color=FF0000>"+nodestr);
            }
        }

    }
    public void SeleccionaNode(String nodestr)
    {
        DefaultMutableTreeNode node = null;
        Enumeration enume = ra.breadthFirstEnumeration();
        while(enume.hasMoreElements())
        {
            node = (DefaultMutableTreeNode)enume.nextElement();
            if(nodestr.equals(node.getUserObject().toString()))
            {
                arbolp1.expandPath(new TreePath(node.getPath()));
            }
        }
    }
    public void recorre_Arbol(String user_grupo)
    {
        int in=0;
        DefaultMutableTreeNode node = null;
        Enumeration enume = ra.breadthFirstEnumeration();
        while(enume.hasMoreElements())
        {
            node = (DefaultMutableTreeNode)enume.nextElement();
            String cadenanode=node.getUserObject().toString();

                if(!cadenanode.contains("<html>") && !cadenanode.equals("*")){
                    rs3=null;
                    try{
                        String senSQL="SELECT * FROM privilegios2 WHERE (privilegio='"+user_grupo+"'  AND menu='"+cadenanode+"');";
                        rs3=conexion.consulta(senSQL,conn);
                        if(rs3.next()){

                        }else{
                            node_eliminados[in]=cadenanode;
                            in++;
                        }
                        if(rs3!=null) {  rs3.close();   }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                 }
        }
    }
    public void limpia_arreglo(){
        for(int in=0;in<node_eliminados.length;in++){
            node_eliminados[in]=null;
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        combogrupo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        combouser = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        arbolp1 = new org.jdesktop.swingx.JXTree();
        jPanel3 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        historia = new javax.swing.JTextArea();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(privilegios.class);
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

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setName("jTabbedPane1"); // NOI18N
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        combogrupo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "n/a" }));
        combogrupo.setName("combogrupo"); // NOI18N
        combogrupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combogrupoActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setName("jLabel1"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setToolTipText(resourceMap.getString("jButton1.toolTipText")); // NOI18N
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton1.setMaximumSize(new java.awt.Dimension(75, 37));
        jButton1.setMinimumSize(new java.awt.Dimension(75, 37));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(75, 37));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(combogrupo, javax.swing.GroupLayout.Alignment.LEADING, 0, 191, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(combogrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        jLabel1.getAccessibleContext().setAccessibleName(resourceMap.getString("jLabel1.AccessibleContext.accessibleName")); // NOI18N

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), resourceMap.getIcon("jPanel1.TabConstraints.tabIcon"), jPanel1); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        combouser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "n/a" }));
        combouser.setName("combouser"); // NOI18N
        combouser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combouserActionPerformed(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setName("jLabel2"); // NOI18N

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jButton3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                            .addComponent(combouser, 0, 191, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(combouser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), resourceMap.getIcon("jPanel2.TabConstraints.tabIcon"), jPanel2); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        arbolp1.setModel(null);
        arbolp1.setClosedIcon(resourceMap.getIcon("arbolp1.closedIcon")); // NOI18N
        arbolp1.setCollapsedIcon(null);
        arbolp1.setExpandedIcon(null);
        arbolp1.setLeafIcon(resourceMap.getIcon("arbolp1.leafIcon")); // NOI18N
        arbolp1.setName("arbolp1"); // NOI18N
        arbolp1.setOpenIcon(resourceMap.getIcon("arbolp1.openIcon")); // NOI18N
        arbolp1.setToggleClickCount(1);
        arbolp1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                arbolp1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(arbolp1);

        jPanel3.setName("jPanel3"); // NOI18N

        btnaceptar.setIcon(resourceMap.getIcon("btnaceptar.icon")); // NOI18N
        btnaceptar.setText(resourceMap.getString("btnaceptar.text")); // NOI18N
        btnaceptar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnaceptar.setName("btnaceptar"); // NOI18N
        btnaceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(251, Short.MAX_VALUE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(224, 224, 224))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        historia.setColumns(20);
        historia.setFont(resourceMap.getFont("historia.font")); // NOI18N
        historia.setForeground(resourceMap.getColor("historia.foreground")); // NOI18N
        historia.setLineWrap(true);
        historia.setRows(5);
        historia.setName("historia"); // NOI18N
        jScrollPane2.setViewportView(historia);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        salir();
}//GEN-LAST:event_btnaceptarActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void arbolp1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arbolp1MouseClicked
        // TODO add your handling code here:
        String selmenu=""+arbolp1.getLastSelectedPathComponent();
        String grupo=""+combogrupo.getSelectedItem();
        if(menuu==1)
            grupo=""+combouser.getSelectedItem();

        if(selmenu.equals("null") || grupo.equals("n/a")){
            JOptionPane.showMessageDialog(this,"NO HAS SELECCIONDO UN GRUPO O EL MENU ES INVALIDO","E R R O R !!!!!!!!!!", JOptionPane.INFORMATION_MESSAGE);
        }else{
            DefaultMutableTreeNode node = searchNode(selmenu);
            String valclic=""+node.isLeaf();
            if(valclic.equals("true")){
                if(selmenu.contains("<html>")){
                    busca_usuario_sql busca_usuario_sql = new busca_usuario_sql(null,true);
                    busca_usuario_sql.setLocationRelativeTo(this);
                    busca_usuario_sql.setVisible(true);
                    modeltrp.valueForPathChanged(arbolp1.getLeadSelectionPath(),selmenu.substring(25));
                    historia.setText(historia.getText()+"\n("+grupo+") Agregado al menu:  "+selmenu.substring(25)+"  "+busca_usuario_sql.getValor());
                    //agrega a los privilegios
                    String senSQL="INSERT INTO privilegios2 (privilegio,menu,numero_sql) VALUES('"+grupo+"','"+selmenu.substring(25)+"','"+busca_usuario_sql.getValor()+"');";
                    conexion.modificamov_p(senSQL,conn,valor_privilegio);
                    busca_usuario_sql=null;
                    
                }else{
                    modeltrp.valueForPathChanged(arbolp1.getLeadSelectionPath(),"<html><font color=FF0000>"+selmenu);
                    historia.setText(historia.getText()+"\n("+grupo+") ELIMINADO del menu:  "+selmenu);
                    //quita de los privilegios
                    String senSQL="DELETE FROM privilegios2 WHERE (privilegio='"+grupo+"' AND menu='"+selmenu+"');";
                    conexion.modificamov_p(senSQL,conn,valor_privilegio);
                    
                }
            }
        }

    }//GEN-LAST:event_arbolp1MouseClicked

    private void combogrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combogrupoActionPerformed
        // TODO add your handling code here:
        String grupo=""+combogrupo.getSelectedItem();
        ColorNode();
        if(grupo.equals("n/a")){
            arbolp1.collapseAll();
            SeleccionaNode("*");
        }else{
            recorre_Arbol(grupo);
            for(int in=0;in<node_eliminados.length;in++){
                String val_node=""+node_eliminados[in];
                if(!val_node.equals("null") && !val_node.equals("")){
                    ColorRojoNode(val_node);
                }
            }
            limpia_arreglo();
        }

    }//GEN-LAST:event_combogrupoActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:
        combogrupo.setSelectedIndex(0);
        combouser.setSelectedIndex(0);
        menuu=jTabbedPane1.getSelectedIndex();
        arbolp1.collapseAll();
        SeleccionaNode("*");

    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void combouserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combouserActionPerformed
        // TODO add your handling code here:
        String user=""+combouser.getSelectedItem();
        ColorNode();
        if(user.equals("n/a")){
            arbolp1.collapseAll();
            SeleccionaNode("*");
        }else{
            recorre_Arbol(user);
            for(int in=0;in<node_eliminados.length;in++){
                String val_node=""+node_eliminados[in];
                if(!val_node.equals("null") && !val_node.equals("")){
                    ColorRojoNode(val_node);
                }
            }
            limpia_arreglo();
        }

    }//GEN-LAST:event_combouserActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
            datos_gpousuarios = new datos_gpousuarios(null,true,conn,"",valor_privilegio);
            datos_gpousuarios.setLocationRelativeTo(this);
            datos_gpousuarios.setVisible(true);
            datos_gpousuarios=null;
            combogrupo.removeAllItems();
            combogrupo.addItem("n/a");
            llenargrupos();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String grupo=""+combogrupo.getSelectedItem();
        if(grupo.equals("n/a")){
            JOptionPane.showMessageDialog(this,"DEBES SELECCIONAR UN GRUPO\n","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            datos_gpousuarios = new datos_gpousuarios(null,true,conn,grupo,valor_privilegio);
            datos_gpousuarios.setLocationRelativeTo(this);
            datos_gpousuarios.setVisible(true);
            datos_gpousuarios=null;
            combogrupo.removeAllItems();
            combogrupo.addItem("n/a");
            llenargrupos();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        String us=""+JOptionPane.showInputDialog(this,"USUARIO");
        String me=""+JOptionPane.showInputDialog(this,"MENU");
        if(!us.equals("") && !us.equals("null") && !me.equals("") && !me.equals("null")){
            String senSQL="INSERT INTO privilegios2 (privilegio,menu,numero_sql) VALUES('"+us+"','"+me+"','2');";
            conexion.modificamov_p(senSQL,conn,valor_privilegio);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
    * @param args the command line arguments
    */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXTree arbolp1;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JComboBox combogrupo;
    private javax.swing.JComboBox combouser;
    private javax.swing.JTextArea historia;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_gpousuarios;
}
