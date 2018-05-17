/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * login.java
 *
 * Created on 30/12/2009, 10:10:31 AM
 */

package conta;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author ANGEL
 */
public class login extends javax.swing.JFrame {
    Connection conn=null;
    ResultSet rs0=null;
    private Properties conf;
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    
    /** Creates new form login */
    public login() {
        initComponents();
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo2.png")).getImage());
        //this.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
        //setBounds(0, 0, 581, 671);
        /**da formato al jframe cambiando el icono y centrandolo*/
        //this.setMaximumSize(new java.awt.Dimension(290,329));
        //this.setPreferredSize(new java.awt.Dimension(290,329));
        //this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo2.png")).getImage());
        //this.setLocationRelativeTo(null);

        /**verificar la existencia de otra ventana abierta*/
        conf=conexion.archivoInicial();
        if(conf.getProperty("Instancia").equals("1")){
            if(0 ==javax.swing.JOptionPane.showOptionDialog(rootPane, "La aplicacion ya esta abierta o NO se cerro correctamente", "V E R I F I C A N D O"
                    ,javax.swing.JOptionPane.DEFAULT_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Aceptar","Cerrar correctamente"}, "Aceptar")){
                System.exit(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                conexion.escribir("Instancia", "1");
            }else{
                conexion.escribir("Instancia", "0");
                System.exit(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            }
        }
        usuario.setText(conf.getProperty("UserID"));
        conexion.escribir("Instancia", "1");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        this.setTitle("::  "+resourceMap.getString("Application.title")+"  ::");
        
        configuraventana();
    }
    public void configuraventana(){
            int cord=(panel_principal.getWidth()/2)-(frame_login.getWidth()/2);
            int cord_h=(panel_principal.getHeight()/2)-(frame_login.getHeight()/2)-70;
            if(cord<0)
                cord=0;
            if(cord_h<0)
                cord_h=0;

            //System.err.println(""+cord+"-------**"+panel_principal.getWidth()+"-------**"+frame_login.getWidth());
            lbico.setLocation(panel_principal.getWidth()-lbico.getWidth(),panel_principal.getHeight()-lbico.getHeight());
            /*lb_autor.setLocation(3,panel_principal.getHeight()-lb_autor.getHeight()-10);*/
            frame_login.setLocation(cord,cord_h);
            frame_login.toFront();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        panel_principal = new javax.swing.JDesktopPane();
        frame_login = new javax.swing.JInternalFrame();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        clave = new javax.swing.JPasswordField();
        usuario = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        lbico = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        btn_sqlserver = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(login.class);
        menuBar.setFont(resourceMap.getFont("menuBar.font")); // NOI18N
        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N
        fileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuActionPerformed(evt);
            }
        });

        jMenuItem3.setIcon(resourceMap.getIcon("jMenuItem3.icon")); // NOI18N
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem3);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(resourceMap.getIcon("jMenuItem1.icon")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        jMenuItem2.setIcon(resourceMap.getIcon("jMenuItem2.icon")); // NOI18N
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem2);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getActionMap(login.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.setPreferredSize(new java.awt.Dimension(129, 28));
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setIcon(resourceMap.getIcon("aboutMenuItem.icon")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setIconImages(null);
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

        panel_principal.setBackground(resourceMap.getColor("panel_principal.background")); // NOI18N
        panel_principal.setName("panel_principal"); // NOI18N

        frame_login.setBackground(resourceMap.getColor("frame_login.background")); // NOI18N
        frame_login.setTitle(resourceMap.getString("frame_login.title")); // NOI18N
        frame_login.setFrameIcon(null);
        frame_login.setName("frame_login"); // NOI18N
        frame_login.setVisible(true);

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        clave.setForeground(resourceMap.getColor("clave.foreground")); // NOI18N
        clave.setToolTipText(resourceMap.getString("clave.toolTipText")); // NOI18N
        clave.setName("clave"); // NOI18N
        clave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claveActionPerformed(evt);
            }
        });
        clave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                claveFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                claveFocusLost(evt);
            }
        });

        usuario.setForeground(resourceMap.getColor("usuario.foreground")); // NOI18N
        usuario.setToolTipText(resourceMap.getString("usuario.toolTipText")); // NOI18N
        usuario.setName("usuario"); // NOI18N
        usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usuarioActionPerformed(evt);
            }
        });
        usuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usuarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usuarioFocusLost(evt);
            }
        });

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.setMaximumSize(new java.awt.Dimension(69, 25));
        jButton1.setMinimumSize(new java.awt.Dimension(69, 25));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(69, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(resourceMap.getIcon("jLabel5.icon")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        javax.swing.GroupLayout frame_loginLayout = new javax.swing.GroupLayout(frame_login.getContentPane());
        frame_login.getContentPane().setLayout(frame_loginLayout);
        frame_loginLayout.setHorizontalGroup(
            frame_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frame_loginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frame_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frame_loginLayout.createSequentialGroup()
                        .addGroup(frame_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(usuario, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(clave, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frame_loginLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70))))
        );
        frame_loginLayout.setVerticalGroup(
            frame_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frame_loginLayout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        frame_login.setBounds(10, 10, 240, 300);
        panel_principal.add(frame_login, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lbico.setIcon(resourceMap.getIcon("lbico.icon")); // NOI18N
        lbico.setText(resourceMap.getString("lbico.text")); // NOI18N
        lbico.setName("lbico"); // NOI18N
        lbico.setBounds(450, 320, 100, 50);
        panel_principal.add(lbico, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setToolTipText(resourceMap.getString("jButton2.toolTipText")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(40, 40));
        jButton2.setMinimumSize(new java.awt.Dimension(40, 40));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        btn_sqlserver.setIcon(resourceMap.getIcon("btn_sqlserver.icon")); // NOI18N
        btn_sqlserver.setText(resourceMap.getString("btn_sqlserver.text")); // NOI18N
        btn_sqlserver.setToolTipText(resourceMap.getString("btn_sqlserver.toolTipText")); // NOI18N
        btn_sqlserver.setFocusable(false);
        btn_sqlserver.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_sqlserver.setName("btn_sqlserver"); // NOI18N
        btn_sqlserver.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_sqlserver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sqlserverActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_sqlserver);

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setToolTipText(resourceMap.getString("jButton3.toolTipText")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(40, 40));
        jButton3.setMinimumSize(new java.awt.Dimension(40, 40));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setToolTipText(resourceMap.getString("jButton4.toolTipText")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMaximumSize(new java.awt.Dimension(40, 40));
        jButton4.setMinimumSize(new java.awt.Dimension(40, 40));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setToolTipText(resourceMap.getString("jButton5.toolTipText")); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton5.setMaximumSize(new java.awt.Dimension(40, 40));
        jButton5.setMinimumSize(new java.awt.Dimension(40, 40));
        jButton5.setName("jButton5"); // NOI18N
        jButton5.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_principal, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_principal, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-581)/2, (screenSize.height-471)/2, 581, 471);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        conexion.escribir("Instancia", "0");
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void usuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usuarioFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_usuarioFocusLost

    private void claveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_claveFocusLost

    private void claveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claveActionPerformed
        // TODO add your handling code here:
        verificarusuario();
    }//GEN-LAST:event_claveActionPerformed

    private void usuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usuarioFocusGained
        // TODO add your handling code here:
        usuario.selectAll();
    }//GEN-LAST:event_usuarioFocusGained

    private void claveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveFocusGained
        // TODO add your handling code here:
        clave.selectAll();
    }//GEN-LAST:event_claveFocusGained

    private void usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usuarioActionPerformed
        // TODO add your handling code here:
        verificarusuario();
    }//GEN-LAST:event_usuarioActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        verificarusuario();
}//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        // TODO add your handling code here:
        configuraventana();
    }//GEN-LAST:event_formWindowStateChanged

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        configuraventana();
    }//GEN-LAST:event_formComponentResized

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }//GEN-LAST:event_formWindowOpened

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try {
        File archivo=new File(System.getProperty("user.home")+"/.temERP" + "/appE.xml");
        if (archivo.exists()) {
            archivo.delete();
            conf=conexion.archivoInicial();
        }
        }catch(Exception e){ }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        servidor = new datos_servidor(this,true);
        servidor.setLocationRelativeTo(this);
        servidor.setVisible(true);
        servidor=null;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (aboutBox == null) {
            JFrame mainFrame = CONTAApp.getApplication().getMainFrame();
            aboutBox = new CONTAAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        CONTAApp.getApplication().show(aboutBox);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        conexion.escribir("Instancia", "0");
        salir();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        // TODO add your handling code here:
        if (aboutBox == null) {
            JFrame mainFrame = CONTAApp.getApplication().getMainFrame();
            aboutBox = new CONTAAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        CONTAApp.getApplication().show(aboutBox);
}//GEN-LAST:event_aboutMenuItemActionPerformed

    private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileMenuActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_exitMenuItemActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        try {
            File archivo=new File(System.getProperty("user.home")+"/.temERP" + "/appE.xml");
            if (archivo.exists()) {
                archivo.delete();
                conf=conexion.archivoInicial();
            }
        }catch(Exception e){ }
}//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        servidor = new datos_servidor(this,true);
        servidor.setLocationRelativeTo(this);
        servidor.setVisible(true);
        servidor=null;
}//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
}//GEN-LAST:event_jMenuItem3ActionPerformed

    private void btn_sqlserverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sqlserverActionPerformed
        // TODO add your handling code here:
        servidor_sqlserver = new datos_servidor_sqlserver(this,true);
        servidor_sqlserver.setLocationRelativeTo(this);
        servidor_sqlserver.setVisible(true);
        servidor_sqlserver=null;
    }//GEN-LAST:event_btn_sqlserverActionPerformed

    public void showAboutBox() {
        
    }

    public void verificarusuario(){
    String contra=new String(clave.getPassword());
    if(usuario.getText().equals("") || contra.equals(""))
    {
        JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
    }
    else
    {
        if(conn==null){
            conn=conexion.abrirconexion();
        }
        /**convierte la cadena a md5*/
        try{
            contra=conexion.MD5(contra);
            }
        catch (NoSuchAlgorithmException e) {  JOptionPane.showMessageDialog(this,"ERROR EN ALGORITMO\n"+e,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);        }
        catch (UnsupportedEncodingException e) {  JOptionPane.showMessageDialog(this,"ERROR EN ALGORITMO\n"+e,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);        }

        rs0=null;
        try{
                String senSQL="SELECT * FROM usuarios WHERE (usuario='"+usuario.getText()+"' and pass='"+contra+"' and activo<>'No')";
                rs0=conexion.consulta(senSQL,conn);

                if(rs0.next()){
                    /**privilegios del servidor*/
                    conexion.escribir("Privilegios", rs0.getString("sqle"));
                    conexion.escribir("User", rs0.getString("Nombre"));
                    conexion.escribir("UserID", usuario.getText());
                    try{
                        String senSQLmov="INSERT INTO bitacora_usuarios(usuario, fecha, descripcion,host) VALUES ('"+conf.getProperty("UserID")+"', '"+fechainsertarhora.format(new Date())+"', 'Ingreso al sistema','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                        conexion.modificasin(senSQLmov,conn);
                    }catch(Exception e){}

                    new menu_principal().setVisible(true);
                    salir();
                    
                }else{
                    JOptionPane.showMessageDialog(this,"EL USUARIO NO EXISTE O LA CONTRASEÑA ES INCORRECTA","A D V E R T E N C I A  !!!!!!!!!!",JOptionPane.WARNING_MESSAGE);
                }
                if(rs0!=null) {  rs0.close();          }

            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    }
    public void salir(){
        if(conn!=null){
            try{
                conn.close();
            }
            catch(SQLException sqle){ System.out.println("No se pudo cerrar la conexion:\n"+sqle); }
        }
        dispose();
        this.setVisible(false);
    }

    /**
    * @param args the command line arguments
    */
    /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_sqlserver;
    private javax.swing.JPasswordField clave;
    private javax.swing.JInternalFrame frame_login;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lbico;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JDesktopPane panel_principal;
    private javax.swing.JTextField usuario;
    // End of variables declaration//GEN-END:variables

    private JDialog aboutBox;
    private JDialog servidor;
    private JDialog servidor_sqlserver;
}