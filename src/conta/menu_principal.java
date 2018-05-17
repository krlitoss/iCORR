/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * menu_principal.java
 *
 * Created on 30/12/2009, 12:48:17 PM
 */
package conta;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import jxl.*;
import jxl.write.*;

/**
 *
 * @author ANGEL
 */
public class menu_principal extends javax.swing.JFrame {

    Connection conn = conexion.abrirconexion();
    private Properties conf;
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MMM-yy");
    SimpleDateFormat fechamuycorta = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechalarga = new SimpleDateFormat("EEEEEEEEE',' dd-MMM-yyyy");
    SimpleDateFormat fechamesano = new SimpleDateFormat("MMMMM '-' yyyy");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechaparserhora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fechaparser = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fechamostrar = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat fechamostrarhora = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
    SimpleDateFormat fechasathora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat fechamostrarhoracorta = new SimpleDateFormat("dd-MMM-yy HH:mm");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    SimpleDateFormat fechasin = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat fechamesnumero = new SimpleDateFormat("MM");
    SimpleDateFormat fechamescompleto = new SimpleDateFormat("MMMMM");
    SimpleDateFormat fechadianumero = new SimpleDateFormat("dd");
    DecimalFormat horasminutos = new DecimalFormat("##########00");
    DecimalFormat fijo2enteros = new DecimalFormat("#####00");
    DecimalFormat fijo0decimales = new DecimalFormat("######0");
    DecimalFormat fijo1decimales = new DecimalFormat("######0.0");
    DecimalFormat fijo2decimales = new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales = new DecimalFormat("######0.000");
    DecimalFormat fijo4decimales = new DecimalFormat("######0.0000");
    DecimalFormat fijo5decimales = new DecimalFormat("######0.00000");
    DecimalFormat moneda0decimales = new DecimalFormat("$ #,###,##0");
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    DecimalFormat moneda5decimales = new DecimalFormat("$ #,###,##0.00000");
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat estandar1decimal = new DecimalFormat("#,###,##0.0");
    DecimalFormat porcentaje2decimal = new DecimalFormat("##0.00 %");
    ResultSet rs0 = null;
    int ver = 1, buscanodoint = 0;
    private DefaultTreeModel modeltr = null;
    private DefaultMutableTreeNode r = null;
    ////****eliminar para el control maestro*******////////
    Connection concontrol = null;
    String node_eliminados[] = new String[300];
    String valor_privilegio = "1";

    /** Creates new form menu_principal */
    public menu_principal() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        /**da formato al jframe cambiando el icono y centrandolo*/
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo2.png")).getImage());
        this.setLocationRelativeTo(null);

        /**funcion para el cursor
        java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
        micursor = tk.createCustomCursor(imagen, new java.awt.Point(0, 2), null);
        setCursor(micursor);*/
        conf = conexion.archivoInicial();

        r = (DefaultMutableTreeNode) arbol1.getModel().getRoot();
        modeltr = new DefaultTreeModel(r);
        arbol1.setModel(modeltr);

        configuracioninicial();
        conectado();
        leerprivilegios();
        leerarreglo();
        arbol1.collapseAll();
        SeleccionaNode("*");
        verifica_periodo();

    }

    public void conexioncontrolmaestro() {       ////****eliminar para el control maestro*******////////
        if (concontrol != null) {
        } else {
            concontrol = conexion.abrirconexioncontrol();
        }

    }

    public void conectado() {
        if (conn == null) {
            lbbd.setForeground(new java.awt.Color(204, 0, 51));
            JOptionPane.showMessageDialog(this, "SE HA PERDIDO LA CONEXION A LA BASE DE DATOS", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            lbbd.setForeground(new java.awt.Color(102, 153, 0));
        }
    }

    public void salir() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqle) {
                System.out.println("No se pudo cerrar la conexion:\n" + sqle);
            }
        }

        if (concontrol != null) {       ////****eliminar para el control maestro*******////////
            try {
                concontrol.close();
            } catch (SQLException sqle) {
                System.out.println("No se pudo cerrar la conexion:\n" + sqle);
            }
        }
        dispose();
        this.setVisible(false);
    }

    public void configuracioninicial() {

        try {
            ((JPanelFondo) panelprincipal).setImagen("/imagenes/bg.jpg");
        } catch (Exception e) {
        }

        rs0 = null;
        try {
            String senSQL = "SELECT nombrecomercial,logo,logo_rfc FROM empresa WHERE id='1'";
            rs0 = conexion.consulta(senSQL, conn);
            if (rs0.next()) {
                this.setTitle("::: " + rs0.getString("nombrecomercial") + " :::");

                byte bytes2[] = rs0.getBytes("logo");
                if (bytes2 != null) {
                    File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                    try {
                        InputStream in = new ByteArrayInputStream(bytes2);
                        BufferedImage imagen = javax.imageio.ImageIO.read(in);
                        ImageIO.write(imagen, "png", fichero);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, "NO SE PUEDE CREAR LA IMAGEN" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                    }
                }

                byte bytes3[] = rs0.getBytes("logo_rfc");
                if (bytes3 != null) {
                    File fichero3 = new File(conexion.Directorio() + "/rfcempresa.png");
                    try {
                        InputStream in = new ByteArrayInputStream(bytes3);
                        BufferedImage imagen = javax.imageio.ImageIO.read(in);
                        ImageIO.write(imagen, "png", fichero3);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, "NO SE PUEDE CREAR LA IMAGEN" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        Date hoy = new Date();
        fecha.setText(fechalarga.format(hoy) + " ");

        /**lectura de archivo de privilegios*/
        usuario.setText(conf.getProperty("User"));
        perio.setText(conf.getProperty("Periodo"));
        lbbd.setText("Conectado (" + conf.getProperty("BD") + ")");

    }

    public void verifica_periodo() {
        try {
            Date fi = fechainsertar.parse(conf.getProperty("FechaIni"));
            Date ff = fechainsertar.parse(conf.getProperty("FechaFin"));
            Date hoy_t = new Date();
            if (hoy_t.before(fi) || hoy_t.after(ff)) {
                if (JOptionPane.showConfirmDialog(this, "EL PERIODO DEL SISTEMA ES DIFERENTE AL ACTUAL\nDESEA CAMBIARLO!!", " I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    busca_periodo = new busca_periodo(this, true, "");
                    busca_periodo.setLocationRelativeTo(this);
                    busca_periodo.setVisible(true);
                    busca_periodo = null;
                    perio.setText(conf.getProperty("Periodo"));
                }
            } else {
            }
        } catch (Exception e) {
        }
    }

    public void buscaNode() {
        DefaultMutableTreeNode node = searchNode(txtbuscar.getText());
        if (node != null) {
            TreeNode[] nodes = modeltr.getPathToRoot(node);
            TreePath path = new TreePath(nodes);
            arbol1.scrollPathToVisible(path);
            arbol1.setSelectionPath(path);
        } else {
            JOptionPane.showMessageDialog(this, "NO SE ENCONTRO:   " + txtbuscar.getText(), "E R R O R !!!!!!!!!!", JOptionPane.INFORMATION_MESSAGE);
            buscanodoint = 0;
        }
    }

    public DefaultMutableTreeNode searchNode(String nodeStr) {
        int bni = 0;
        DefaultMutableTreeNode node = null;
        Enumeration enume = r.breadthFirstEnumeration();
        while (enume.hasMoreElements()) {
            bni++;
            node = (DefaultMutableTreeNode) enume.nextElement();
            String A = node.getUserObject().toString().toUpperCase();
            //if(nodeStr.equals(node.getUserObject().toString()))
            if (A.indexOf(nodeStr.toUpperCase()) != -1 && bni > buscanodoint) {
                buscanodoint = bni;
                return node;
            }
        }
        return null;
    }

    public void leerprivilegios() {
        int in = 0;
        DefaultMutableTreeNode node = null;
        Enumeration enume = r.breadthFirstEnumeration();
        while (enume.hasMoreElements()) {
            node = (DefaultMutableTreeNode) enume.nextElement();
            String cadenanode = node.getUserObject().toString();

            if (!cadenanode.contains("<html>") && !cadenanode.equals("*")) {
                rs0 = null;
                try {
                    String senSQL = "SELECT * FROM privilegios2 WHERE (privilegio='" + conf.getProperty("UserID") + "' AND menu='" + cadenanode + "');";
                    rs0 = conexion.consulta(senSQL, conn);
                    if (rs0.next()) {
                    } else {
                        node_eliminados[in] = cadenanode;
                        in++;
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void leerarreglo() {
        for (int in = 0; in < node_eliminados.length; in++) {
            String val_node = "" + node_eliminados[in];
            if (!val_node.equals("null") && !val_node.equals("")) {
                RemoveNodo("" + val_node);
            }
        }
    }

    public void RemoveNodo(String nodestr) {

        DefaultMutableTreeNode node = null;
        Enumeration enume = r.breadthFirstEnumeration();
        while (enume.hasMoreElements()) {
            node = (DefaultMutableTreeNode) enume.nextElement();
            if (nodestr.equals(node.getUserObject().toString())) {
                modeltr.removeNodeFromParent(node);
            }
        }
    }

    public void SeleccionaNode(String nodestr) {
        DefaultMutableTreeNode node = null;
        Enumeration enume = r.breadthFirstEnumeration();
        while (enume.hasMoreElements()) {
            node = (DefaultMutableTreeNode) enume.nextElement();
            if (nodestr.equals(node.getUserObject().toString())) {
                arbol1.expandPath(new TreePath(node.getPath()));
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu20 = new javax.swing.JMenu();
        jMenu21 = new javax.swing.JMenu();
        panelprincipal = new conta.JPanelFondo();
        jLabel1 = new javax.swing.JLabel();
        jXCollapsiblePane1 = new org.jdesktop.swingx.JXCollapsiblePane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtbuscar = new javax.swing.JTextField();
        btnbuscararbol = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        arbol1 = new org.jdesktop.swingx.JXTree();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        BarraUsuario = new javax.swing.JToolBar();
        usuario = new javax.swing.JLabel();
        BarraFecha = new javax.swing.JToolBar();
        fecha = new javax.swing.JLabel();
        BarraPeriodo = new javax.swing.JToolBar();
        perio = new javax.swing.JLabel();
        BarraBD = new javax.swing.JToolBar();
        lbbd = new javax.swing.JLabel();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        jPopupMenu2.setName("jPopupMenu2"); // NOI18N

        jMenuBar2.setName("jMenuBar2"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(menu_principal.class);
        jMenu20.setText(resourceMap.getString("jMenu20.text")); // NOI18N
        jMenu20.setName("jMenu20"); // NOI18N
        jMenuBar2.add(jMenu20);

        jMenu21.setText(resourceMap.getString("jMenu21.text")); // NOI18N
        jMenu21.setName("jMenu21"); // NOI18N
        jMenuBar2.add(jMenu21);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panelprincipal.setBackground(resourceMap.getColor("panelprincipal.background")); // NOI18N
        panelprincipal.setForeground(resourceMap.getColor("panelprincipal.foreground")); // NOI18N
        panelprincipal.setAutoscrolls(true);
        panelprincipal.setName("panelprincipal"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jXCollapsiblePane1.setDirection(org.jdesktop.swingx.JXCollapsiblePane.Direction.LEFT);
        jXCollapsiblePane1.setName("jXCollapsiblePane1"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(resourceMap.getColor("jPanel2.border.border.lineColor"), 0, true), resourceMap.getString("jPanel2.border.title"), 1, 0, resourceMap.getFont("jPanel2.border.titleFont"), resourceMap.getColor("jPanel2.border.titleColor"))); // NOI18N
        jPanel2.setToolTipText(resourceMap.getString("jPanel2.toolTipText")); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        txtbuscar.setBackground(resourceMap.getColor("txtbuscar.background")); // NOI18N
        txtbuscar.setText(resourceMap.getString("txtbuscar.text")); // NOI18N
        txtbuscar.setName("txtbuscar"); // NOI18N
        txtbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbuscarActionPerformed(evt);
            }
        });
        txtbuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtbuscarKeyReleased(evt);
            }
        });

        btnbuscararbol.setFont(resourceMap.getFont("btnbuscararbol.font")); // NOI18N
        btnbuscararbol.setIcon(resourceMap.getIcon("btnbuscararbol.icon")); // NOI18N
        btnbuscararbol.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnbuscararbol.setName("btnbuscararbol"); // NOI18N
        btnbuscararbol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscararbolActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(txtbuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnbuscararbol))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnbuscararbol)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jScrollPane3.border.title"), 0, 0, resourceMap.getFont("jScrollPane3.border.titleFont"), resourceMap.getColor("jScrollPane3.border.titleColor"))); // NOI18N
        jScrollPane3.setName("jScrollPane3"); // NOI18N

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("*");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Configuración General");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Aplicación");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Skin");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Administración de TI");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Grupos y permisos");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Usuarios");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Empresa");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Logotipo");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("CIF (rfc)");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>CFD");
        javax.swing.tree.DefaultMutableTreeNode treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("Certificado");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("Llave");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("Folios");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("Cancela CFDI");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("SMTP Mail");
        treeNode4.add(treeNode5);
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("URL Files");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("BackupBD");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("RestaurarBD");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Pruebas");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Datos Maestros");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Agenda de contactos");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Areas de la Empresa");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Gestión de MRP");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Requerimiento Papel");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Requerimiento Tintas");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Compras");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Proveedores");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Productos");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Grupo de Productos");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Ordenes de Compra");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Almacen Papel");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Claves Almacen");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Entradas Papel");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Consumos Papel");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Inventario Papel");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Inventario por Almacen");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Inventario Papel Detalle");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Reportes");
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("Entradas Proveedor");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("Entradas Tipo de Papel");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("Consumos Proveedor");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("Consumos Tipo de Papel");
        treeNode4.add(treeNode5);
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Gestión de Producción");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Parametros");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Máquinas");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Resistencias");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Operadores");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Claves de Paro");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Suajes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Grabados");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Consulta OP");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Planeacion");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Detalle Pendientes x entregar");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Herr. Programacion Corrugadora");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("CM_Herr. Programacion Corrugadora");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Programar Corrugadora");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Corrugadora Autorizar");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Papeleta");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Papeleta Producto Terminado");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("CORRFLEX");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Registrar producción");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Corrugadora");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Conversión");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Inventarios");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Entradas Inventario Corrugadora");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Inventario Corrugadora");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Entradas Inventario Conversion");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Salidas Inventario Conversion");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Inventario Conversion");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Calidad");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Mullen,ECT,BCT");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Defectos de Calidad");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Almacen Cuarentena");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Certificados");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Reportes");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Gráfica Corrugadora");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Graficas Producción");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Mantenimiento");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Mtto. Diario");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Ventas");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Vendedores");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Cotizaciones");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Solicitud de muestras");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Articulos");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Clientes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Lugares de Entrega");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Bitacora Clientes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Pedidos");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Consulta OP");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("OPs Pendientes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Estatus OPs");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Embarques");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Transportistas");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Entrada almacen PT");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Almacen PT");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Codigo de Barras");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Certificados");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Remisiones");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Remisiones por Facturar");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Devoluciones");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Reportes");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Detalles Remisionado-Clientes");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Remisionado Acumulado Articulos");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Remisionado Clientes");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Remisionado Grafica Clientes");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Remisionado Grafica Mes");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Gestión Financiera");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Monedas");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Lugares Emisión");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Esquema de Impuestos");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Cuentas Bancarias");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Conceptos Movimientos bancarios");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Remisiones por Facturar");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Facturas");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Cuentas x Cobrar");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Contrarecibos");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Saldos y Programa de cobros");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Ingresos Anticipados");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Ingresos");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Auxiliar Cliente");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Facturas Pagadas");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Notas de Credito");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Reporte SAT");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Polizas Modelo");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("PM Ventas");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("PM Ingresos");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Costos");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Costos Detalle-Procesos");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Caja Chica");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Remisiones Externas");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Movimientos Caja");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Varios");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Calculadora");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Articulos Corrugadora");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Pruebas Corr");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Planeacion CM");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Reportes");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("R M2 Producidos");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("R Combinaciones promedio de papel");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("R Laminas Fabricadas Corrugadora");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("<html><b>Interfaz SAE");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Importar Materia Prima");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Importar Remisiones Facturadas");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        arbol1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        arbol1.setClosedIcon(resourceMap.getIcon("arbol1.closedIcon")); // NOI18N
        arbol1.setCollapsedIcon(resourceMap.getIcon("arbol1.collapsedIcon")); // NOI18N
        arbol1.setExpandedIcon(resourceMap.getIcon("arbol1.expandedIcon")); // NOI18N
        arbol1.setLeafIcon(resourceMap.getIcon("arbol1.leafIcon")); // NOI18N
        arbol1.setOpenIcon(resourceMap.getIcon("arbol1.openIcon")); // NOI18N
        arbol1.setRowHeight(17);
        arbol1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                arbol1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                arbol1MousePressed(evt);
            }
        });
        arbol1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                arbol1ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(arbol1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jXCollapsiblePane1.getContentPane().add(jPanel3);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setAlignmentY(0.478264F);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setToolTipText(resourceMap.getString("jButton1.toolTipText")); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        jButton1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton1.setMaximumSize(new java.awt.Dimension(70, 19));
        jButton1.setMinimumSize(new java.awt.Dimension(70, 19));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(70, 19));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        BarraUsuario.setRollover(true);
        BarraUsuario.setName("BarraUsuario"); // NOI18N

        usuario.setForeground(resourceMap.getColor("usuario.foreground")); // NOI18N
        usuario.setIcon(resourceMap.getIcon("usuario.icon")); // NOI18N
        usuario.setText(resourceMap.getString("usuario.text")); // NOI18N
        usuario.setToolTipText(resourceMap.getString("usuario.toolTipText")); // NOI18N
        usuario.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        usuario.setName("usuario"); // NOI18N
        BarraUsuario.add(usuario);

        jToolBar1.add(BarraUsuario);

        BarraFecha.setRollover(true);
        BarraFecha.setName("BarraFecha"); // NOI18N

        fecha.setForeground(resourceMap.getColor("fecha.foreground")); // NOI18N
        fecha.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        fecha.setIcon(resourceMap.getIcon("fecha.icon")); // NOI18N
        fecha.setText(resourceMap.getString("fecha.text")); // NOI18N
        fecha.setToolTipText(resourceMap.getString("fecha.toolTipText")); // NOI18N
        fecha.setName("fecha"); // NOI18N
        BarraFecha.add(fecha);

        jToolBar1.add(BarraFecha);

        BarraPeriodo.setRollover(true);
        BarraPeriodo.setName("BarraPeriodo"); // NOI18N

        perio.setForeground(resourceMap.getColor("perio.foreground")); // NOI18N
        perio.setIcon(resourceMap.getIcon("perio.icon")); // NOI18N
        perio.setText(resourceMap.getString("perio.text")); // NOI18N
        perio.setToolTipText(resourceMap.getString("perio.toolTipText")); // NOI18N
        perio.setName("perio"); // NOI18N
        perio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                perioMouseClicked(evt);
            }
        });
        BarraPeriodo.add(perio);

        jToolBar1.add(BarraPeriodo);

        BarraBD.setRollover(true);
        BarraBD.setName("BarraBD"); // NOI18N

        lbbd.setIcon(resourceMap.getIcon("lbbd.icon")); // NOI18N
        lbbd.setText(resourceMap.getString("lbbd.text")); // NOI18N
        lbbd.setToolTipText(resourceMap.getString("lbbd.toolTipText")); // NOI18N
        lbbd.setName("lbbd"); // NOI18N
        BarraBD.add(lbbd);

        jToolBar1.add(BarraBD);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jXCollapsiblePane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelprincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelprincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                    .addComponent(jXCollapsiblePane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        try {
            String senSQLmov = "INSERT INTO bitacora_usuarios(usuario, fecha, descripcion,host) VALUES ('" + conf.getProperty("UserID") + "', '" + fechainsertarhora.format(new Date()) + "', 'Salida del sistema','" + java.net.InetAddress.getLocalHost().getCanonicalHostName() + "');";
            conexion.modificasin(senSQLmov, conn);
        } catch (Exception e) {
        }
        conexion.escribir("Instancia", "0");
        salir();
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void arbol1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arbol1MouseClicked
        // TODO add your handling code here:
        String selmenu = "" + arbol1.getLastSelectedPathComponent();

        if (selmenu.equals("Usuarios")) {////***************************opcion de arbol1***********************************
            usuarios usuarios = new usuarios(conn);

            panelprincipal.add(usuarios, javax.swing.JLayeredPane.DEFAULT_LAYER);
            usuarios.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (usuarios.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                usuarios.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            usuarios.setLocation(cord, 0);
            usuarios.toFront();
        }
        if (selmenu.equals("Empresa")) {////***************************opcion de arbol1***********************************
            empresa = new empresa(this, true, conn);
            empresa.setLocationRelativeTo(this);
            empresa.setVisible(true);
            empresa = null;
        }
        if (selmenu.equals("Logotipo")) {////***************************opcion de arbol1***********************************
            logoempresa = new logoempresa(this, true, conn);
            logoempresa.setLocationRelativeTo(this);
            logoempresa.setVisible(true);
            logoempresa = null;
        }
        if (selmenu.equals("CIF (rfc)")) {////***************************opcion de arbol1***********************************
            rfcempresa = new rfcempresa(this, true, conn);
            rfcempresa.setLocationRelativeTo(this);
            rfcempresa.setVisible(true);
            rfcempresa = null;
        }

        if (selmenu.equals("BackupBD")) {////***************************opcion de arbol1***********************************
            backup = new backup(this, true);
            backup.setLocationRelativeTo(this);
            backup.setVisible(true);
            backup = null;
        }
        if (selmenu.equals("RestaurarBD")) {////***************************opcion de arbol1***********************************
            restaurar = new restaurar(this, true);
            restaurar.setLocationRelativeTo(this);
            restaurar.setVisible(true);
            restaurar = null;
        }

        if (selmenu.equals("Skin")) {////***************************opcion de arbol1***********************************
            temas = new temas(this, true);
            temas.setLocationRelativeTo(this);
            temas.setVisible(true);
            temas = null;
        }
        if (selmenu.equals("Agenda de contactos")) {////***************************opcion de arbol1***********************************
            contactos contactos = new contactos(conn);

            panelprincipal.add(contactos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            contactos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (contactos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                contactos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            contactos.setLocation(cord, 0);
            contactos.toFront();
        }
        if (selmenu.equals("Grupos y permisos")) {////***************************opcion de arbol1***********************************
            privilegios privilegios = new privilegios(conn, r);

            panelprincipal.add(privilegios, javax.swing.JLayeredPane.DEFAULT_LAYER);
            privilegios.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (privilegios.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                privilegios.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            privilegios.setLocation(cord, 0);
            privilegios.toFront();
        }
        if (selmenu.equals("Solicitud de muestras")) {////***************************opcion de arbol1***********************************
            solicitud_muestras solicitud_muestras = new solicitud_muestras(conn);

            panelprincipal.add(solicitud_muestras, javax.swing.JLayeredPane.DEFAULT_LAYER);
            solicitud_muestras.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (solicitud_muestras.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                solicitud_muestras.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            solicitud_muestras.setLocation(cord, 0);
            solicitud_muestras.toFront();
        }
        if (selmenu.equals("Clientes")) {////***************************opcion de arbol1***********************************
            clientes clientes = new clientes(conn);

            panelprincipal.add(clientes, javax.swing.JLayeredPane.DEFAULT_LAYER);
            clientes.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (clientes.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                clientes.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            clientes.setLocation(cord, 0);
            clientes.toFront();
        }
        if (selmenu.equals("Proveedores")) {////***************************opcion de arbol1***********************************
            proveedores proveedores = new proveedores(conn);

            panelprincipal.add(proveedores, javax.swing.JLayeredPane.DEFAULT_LAYER);
            proveedores.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (proveedores.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                proveedores.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            proveedores.setLocation(cord, 0);
            proveedores.toFront();
        }
        if (selmenu.equals("Vendedores")) {////***************************opcion de arbol1***********************************
            agentes agentes = new agentes(conn);

            panelprincipal.add(agentes, javax.swing.JLayeredPane.DEFAULT_LAYER);
            agentes.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (agentes.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                agentes.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            agentes.setLocation(cord, 0);
            agentes.toFront();
        }
        if (selmenu.equals("Productos")) {////***************************opcion de arbol1***********************************
            productos productos = new productos(conn);

            panelprincipal.add(productos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            productos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (productos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                productos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            productos.setLocation(cord, 0);
            productos.toFront();
        }
        if (selmenu.equals("Grupo de Productos")) {////***************************opcion de arbol1***********************************
            gpoproductos gpoproductos = new gpoproductos(conn);

            panelprincipal.add(gpoproductos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            gpoproductos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (gpoproductos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                gpoproductos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            gpoproductos.setLocation(cord, 0);
            gpoproductos.toFront();
        }

        if (selmenu.equals("Monedas")) {////***************************opcion de arbol1***********************************
            monedas monedas = new monedas(conn);

            panelprincipal.add(monedas, javax.swing.JLayeredPane.DEFAULT_LAYER);
            monedas.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (monedas.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                monedas.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            monedas.setLocation(cord, 0);
            monedas.toFront();
        }
        if (selmenu.equals("Esquema de Impuestos")) {////***************************opcion de arbol1***********************************
            impuestos impuestos = new impuestos(conn);

            panelprincipal.add(impuestos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            impuestos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (impuestos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                impuestos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            impuestos.setLocation(cord, 0);
            impuestos.toFront();
        }
        if (selmenu.equals("Cuentas Bancarias")) {////***************************opcion de arbol1***********************************
            cuentas cuentas = new cuentas(conn);

            panelprincipal.add(cuentas, javax.swing.JLayeredPane.DEFAULT_LAYER);
            cuentas.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (cuentas.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                cuentas.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            cuentas.setLocation(cord, 0);
            cuentas.toFront();
        }
        if (selmenu.equals("Conceptos Movimientos bancarios")) {////***************************opcion de arbol1***********************************
            conceptos conceptos = new conceptos(conn);

            panelprincipal.add(conceptos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            conceptos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (conceptos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                conceptos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            conceptos.setLocation(cord, 0);
            conceptos.toFront();
        }
        if (selmenu.equals("Ordenes de Compra")) {////***************************opcion de arbol1***********************************
            ordenescompra ordenescompra = new ordenescompra(conn);

            panelprincipal.add(ordenescompra, javax.swing.JLayeredPane.DEFAULT_LAYER);
            ordenescompra.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (ordenescompra.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                ordenescompra.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            ordenescompra.setLocation(cord, 0);
            ordenescompra.toFront();
        }
        if (selmenu.equals("Lugares de Entrega")) {////***************************opcion de arbol1***********************************
            lugares_entrega lugares_entrega = new lugares_entrega(conn);

            panelprincipal.add(lugares_entrega, javax.swing.JLayeredPane.DEFAULT_LAYER);
            lugares_entrega.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (lugares_entrega.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                lugares_entrega.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            lugares_entrega.setLocation(cord, 0);
            lugares_entrega.toFront();
        }
        if (selmenu.equals("Máquinas")) {////***************************opcion de arbol1***********************************
            maquinas maquinas = new maquinas(conn);

            panelprincipal.add(maquinas, javax.swing.JLayeredPane.DEFAULT_LAYER);
            maquinas.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (maquinas.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                maquinas.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            maquinas.setLocation(cord, 0);
            maquinas.toFront();
        }

        if (selmenu.equals("Resistencias")) {////***************************opcion de arbol1***********************************
            resistencias resistencias = new resistencias(conn);

            panelprincipal.add(resistencias, javax.swing.JLayeredPane.DEFAULT_LAYER);
            resistencias.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (resistencias.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                resistencias.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            resistencias.setLocation(cord, 0);
            resistencias.toFront();
        }
        if (selmenu.equals("Articulos")) {////***************************opcion de arbol1***********************************
            articulos articulos = new articulos(conn);

            panelprincipal.add(articulos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            articulos.setVisible(true);
            //ubica la ventana en el centro del panel
            articulos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            articulos.setLocation(0, 0);
            articulos.toFront();
        }
        if (selmenu.equals("Pedidos")) {////***************************opcion de arbol1***********************************
            pedidos pedidos = new pedidos(conn);

            panelprincipal.add(pedidos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            pedidos.setVisible(true);
            //ubica la ventana en el centro del panel
            pedidos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            pedidos.setLocation(0, 0);
            pedidos.toFront();
        }
        if (selmenu.equals("Consulta OP")) {////***************************opcion de arbol1***********************************
            datos_ops = new datos_ops(this, true, conn, "");
            datos_ops.setLocationRelativeTo(this);
            datos_ops.setVisible(true);
            datos_ops = null;
        }
        if (selmenu.equals("OPs Pendientes")) {////***************************opcion de arbol1***********************************
            ops ops = new ops(conn);

            panelprincipal.add(ops, javax.swing.JLayeredPane.DEFAULT_LAYER);
            ops.setVisible(true);
            //ubica la ventana en el centro del panel
            ops.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            ops.setLocation(0, 0);
            ops.toFront();
        }

        if (selmenu.equals("Estatus OPs")) {////***************************opcion de arbol1***********************************
            estatus_ops estatus_ops = new estatus_ops(conn);

            panelprincipal.add(estatus_ops, javax.swing.JLayeredPane.DEFAULT_LAYER);
            estatus_ops.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (estatus_ops.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                estatus_ops.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            estatus_ops.setLocation(cord, 0);
            estatus_ops.toFront();
        }

        if (selmenu.equals("CORRFLEX")) {////***************************opcion de arbol1***********************************
            corrflex corrflex = new corrflex(conn);

            panelprincipal.add(corrflex, javax.swing.JLayeredPane.DEFAULT_LAYER);
            corrflex.setVisible(true);
            //ubica la ventana en el centro del panel
            corrflex.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            corrflex.setLocation(0, 0);
            corrflex.toFront();
        }
        if (selmenu.equals("Operadores")) {////***************************opcion de arbol1***********************************
            operadores operadores = new operadores(conn);

            panelprincipal.add(operadores, javax.swing.JLayeredPane.DEFAULT_LAYER);
            operadores.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (operadores.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                operadores.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            operadores.setLocation(cord, 0);
            operadores.toFront();
        }

        if (selmenu.equals("Claves de Paro")) {////***************************opcion de arbol1***********************************
            paros paros = new paros(conn);

            panelprincipal.add(paros, javax.swing.JLayeredPane.DEFAULT_LAYER);
            paros.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (paros.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                paros.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            paros.setLocation(cord, 0);
            paros.toFront();
        }

        if (selmenu.equals("Defectos de Calidad")) {////***************************opcion de arbol1***********************************
            defectos_calidad defectos_calidad = new defectos_calidad(conn);

            panelprincipal.add(defectos_calidad, javax.swing.JLayeredPane.DEFAULT_LAYER);
            defectos_calidad.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (defectos_calidad.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                defectos_calidad.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            defectos_calidad.setLocation(cord, 0);
            defectos_calidad.toFront();
        }
        if (selmenu.equals("Mullen,ECT,BCT")) {////***************************opcion de arbol1***********************************
            datos_mullen_ect = new datos_mullen_ect(this, true, conn, "");
            datos_mullen_ect.setLocationRelativeTo(this);
            datos_mullen_ect.setVisible(true);
            datos_mullen_ect = null;
        }
        if (selmenu.equals("Programar Corrugadora")) {////***************************opcion de arbol1***********************************
            programas_corr programas_corr = new programas_corr(conn);

            panelprincipal.add(programas_corr, javax.swing.JLayeredPane.DEFAULT_LAYER);
            programas_corr.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (programas_corr.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                programas_corr.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            programas_corr.setLocation(cord, 0);
            programas_corr.toFront();
        }
        



        if (selmenu.equals("Papeleta")) {////***************************opcion de arbol1***********************************
            datos_papeleta = new datos_papeleta(this, true, conn, "");
            datos_papeleta.setLocationRelativeTo(this);
            datos_papeleta.setVisible(true);
            datos_papeleta = null;
        }

        if (selmenu.equals("Papeleta Producto Terminado")) {////***************************opcion de arbol1***********************************
            datos_papeleta_pt = new datos_papeleta_pt(this, true, conn, "");
            datos_papeleta_pt.setLocationRelativeTo(this);
            datos_papeleta_pt.setVisible(true);
            datos_papeleta_pt = null;
        }

        if (selmenu.equals("Corrugadora")) {////***************************opcion de arbol1***********************************
            programas_corr_captura programas_corr_captura = new programas_corr_captura(conn);

            panelprincipal.add(programas_corr_captura, javax.swing.JLayeredPane.DEFAULT_LAYER);
            programas_corr_captura.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (programas_corr_captura.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                programas_corr_captura.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            programas_corr_captura.setLocation(cord, 0);
            programas_corr_captura.toFront();
        }

        if (selmenu.equals("Gráfica Corrugadora")) {////***************************opcion de arbol1***********************************
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
            busca_fechas busca_fechas = new busca_fechas(null, true);
            busca_fechas.setLocationRelativeTo(this);
            busca_fechas.setVisible(true);
            String estado = busca_fechas.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {

                JasperViewer visor = null;
                JasperPrint jasperPrint = null;
                try {
                    String datos = "REPORTE GENERADO DEL " + fechamediana.format(busca_fechas.getFechaini()) + "  AL  " + fechamediana.format(busca_fechas.getFechafin());
                    Map pars = new HashMap();
                    File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                    pars.put("logoempresa", new FileInputStream(fichero));
                    pars.put("subtitulo", datos);
                    pars.put("fechaini", new java.sql.Timestamp(busca_fechas.getFechaini().getTime()));
                    pars.put("fechafin", new java.sql.Timestamp(busca_fechas.getFechafin().getTime()));
                    pars.put("senSQL", "");
                    pars.put("version", resourceMap.getString("Application.title"));
                    JasperReport masterReport = null;
                    try {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/corr_graf_dias.jasper"));
                    } catch (JRException e) {
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
                    visor = new JasperViewer(jasperPrint, false);
                    visor.setTitle("REPORTE");
                    visor.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
                }
            }

            busca_fechas = null;
        }

        if (selmenu.equals("Conversión")) {////***************************opcion de arbol1***********************************
            programas_conversion_captura programas_conversion_captura = new programas_conversion_captura(conn);

            panelprincipal.add(programas_conversion_captura, javax.swing.JLayeredPane.DEFAULT_LAYER);
            programas_conversion_captura.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (programas_conversion_captura.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                programas_conversion_captura.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            programas_conversion_captura.setLocation(cord, 0);
            programas_conversion_captura.toFront();
        }
        if (selmenu.equals("Entrada almacen PT")) {////***************************opcion de arbol1***********************************
            entrada_almacen_pt2 entrada_almacen_pt2 = new entrada_almacen_pt2(conn);

            panelprincipal.add(entrada_almacen_pt2, javax.swing.JLayeredPane.DEFAULT_LAYER);
            entrada_almacen_pt2.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (entrada_almacen_pt2.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                entrada_almacen_pt2.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            entrada_almacen_pt2.setLocation(cord, 0);
            entrada_almacen_pt2.toFront();
        }
        if (selmenu.equals("Almacen PT")) {////***************************opcion de arbol1***********************************
            almacen_pt almacen_pt = new almacen_pt(conn);

            panelprincipal.add(almacen_pt, javax.swing.JLayeredPane.DEFAULT_LAYER);
            almacen_pt.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (almacen_pt.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                almacen_pt.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            almacen_pt.setLocation(cord, 0);
            almacen_pt.toFront();
        }
        if (selmenu.equals("Transportistas")) {////***************************opcion de arbol1***********************************
            transportistas transportistas = new transportistas(conn);

            panelprincipal.add(transportistas, javax.swing.JLayeredPane.DEFAULT_LAYER);
            transportistas.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (transportistas.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                transportistas.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            transportistas.setLocation(cord, 0);
            transportistas.toFront();
        }
        if (selmenu.equals("Codigo de Barras")) {////***************************opcion de arbol1***********************************
            datos_codigo_barras = new datos_codigo_barras(this, true, conn, "");
            datos_codigo_barras.setLocationRelativeTo(this);
            datos_codigo_barras.setVisible(true);
            datos_codigo_barras = null;
        }
        if (selmenu.equals("Remisiones")) {////***************************opcion de arbol1***********************************
            remisiones remisiones = new remisiones(conn);

            panelprincipal.add(remisiones, javax.swing.JLayeredPane.DEFAULT_LAYER);
            remisiones.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (remisiones.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                remisiones.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            remisiones.setLocation(cord, 0);
            remisiones.toFront();
        }
        if (selmenu.equals("Remisionado Clientes")) {////***************************opcion de arbol1***********************************
            reporte_remisiones_clientes reporte_remisiones_clientes = new reporte_remisiones_clientes(conn);

            panelprincipal.add(reporte_remisiones_clientes, javax.swing.JLayeredPane.DEFAULT_LAYER);
            reporte_remisiones_clientes.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (reporte_remisiones_clientes.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                reporte_remisiones_clientes.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            reporte_remisiones_clientes.setLocation(cord, 0);
            reporte_remisiones_clientes.toFront();
        }
        if (selmenu.equals("Remisionado Grafica Clientes")) {////***************************opcion de arbol1***********************************

            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
            busca_fechas busca_fechas = new busca_fechas(null, true);
            busca_fechas.setLocationRelativeTo(this);
            busca_fechas.setVisible(true);
            String estado = busca_fechas.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {
                String fi = fechainsertar.format(busca_fechas.getFechaini());
                String ft = fechainsertar.format(busca_fechas.getFechafin());
                Double kgperiodo = 0.0;
                Double kgperiodomaquila = 0.0;
                //obtienes los kilogramos totales
                rs0 = null;
                try {
                    String senSQL = "SELECT count(remisiones_detalle.remision) as cuenta,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN ops ON remisiones_detalle.op=ops.op WHERE (remisiones_detalle.estatus='Act' AND ops.maquila<>'Si' AND (remisiones.fechahora>='" + fi + " 00:00:00' AND remisiones.fechahora<='" + ft + " 23:59:59'));";
                    rs0 = conexion.consulta(senSQL, conn);
                    if (rs0.next()) {
                        kgperiodo = rs0.getDouble("totalkg");
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }

                //obtienes los kilogramos totales de maquila
                rs0 = null;
                try {
                    String senSQL = "SELECT count(remisiones_detalle.remision) as cuenta,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN ops ON remisiones_detalle.op=ops.op WHERE (remisiones_detalle.estatus='Act' AND ops.maquila='Si' AND (remisiones.fechahora>='" + fi + " 00:00:00' AND remisiones.fechahora<='" + ft + " 23:59:59'));";
                    rs0 = conexion.consulta(senSQL, conn);
                    if (rs0.next()) {
                        kgperiodomaquila = rs0.getDouble("totalkg");
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }


                JasperViewer visor = null;
                JasperPrint jasperPrint = null;
                try {
                    String datos = "REPORTE GENERADO DEL " + fechamediana.format(busca_fechas.getFechaini()) + "  AL  " + fechamediana.format(busca_fechas.getFechafin());
                    Map pars = new HashMap();
                    File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                    pars.put("logoempresa", new FileInputStream(fichero));
                    pars.put("subtitulo", datos);
                    pars.put("fechaini", new java.sql.Timestamp(busca_fechas.getFechaini().getTime()));
                    pars.put("fechafin", new java.sql.Timestamp(busca_fechas.getFechafin().getTime()));
                    pars.put("senSQL", "");
                    pars.put("version", resourceMap.getString("Application.title"));
                    pars.put("kgperiodo", kgperiodo);
                    pars.put("kgperiodomaquila", kgperiodomaquila);
                    JasperReport masterReport = null;
                    try {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/remisiones_clientes.jasper"));
                    } catch (JRException e) {
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
                    visor = new JasperViewer(jasperPrint, false);
                    visor.setTitle("REPORTE");
                    visor.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
                }
            }

            busca_fechas = null;
        }
        if (selmenu.equals("Remisionado Acumulado Articulos")) {////***************************opcion de arbol1***********************************
            reporte_remisiones_articulos reporte_remisiones_articulos = new reporte_remisiones_articulos(conn);

            panelprincipal.add(reporte_remisiones_articulos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            reporte_remisiones_articulos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (reporte_remisiones_articulos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                reporte_remisiones_articulos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            reporte_remisiones_articulos.setLocation(cord, 0);
            reporte_remisiones_articulos.toFront();
        }
        if (selmenu.equals("Almacen Cuarentena")) {////***************************opcion de arbol1***********************************
            almacen_cuarentena almacen_cuarentena = new almacen_cuarentena(conn);

            panelprincipal.add(almacen_cuarentena, javax.swing.JLayeredPane.DEFAULT_LAYER);
            almacen_cuarentena.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (almacen_cuarentena.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                almacen_cuarentena.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            almacen_cuarentena.setLocation(cord, 0);
            almacen_cuarentena.toFront();
        }
        if (selmenu.equals("Devoluciones")) {////***************************opcion de arbol1***********************************
            devoluciones devoluciones = new devoluciones(conn);

            panelprincipal.add(devoluciones, javax.swing.JLayeredPane.DEFAULT_LAYER);
            devoluciones.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (devoluciones.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                devoluciones.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            devoluciones.setLocation(cord, 0);
            devoluciones.toFront();
        }
        if (selmenu.equals("Folios")) {////***************************opcion de arbol1***********************************
            folios folios = new folios(conn);

            panelprincipal.add(folios, javax.swing.JLayeredPane.DEFAULT_LAYER);
            folios.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (folios.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                folios.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            folios.setLocation(cord, 0);
            folios.toFront();
        }
        if (selmenu.equals("Lugares Emisión")) {////***************************opcion de arbol1***********************************
            lugaresemision lugaresemision = new lugaresemision(conn);

            panelprincipal.add(lugaresemision, javax.swing.JLayeredPane.DEFAULT_LAYER);
            lugaresemision.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (lugaresemision.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                lugaresemision.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            lugaresemision.setLocation(cord, 0);
            lugaresemision.toFront();
        }
        if (selmenu.equals("Facturas")) {////***************************opcion de arbol1***********************************
            facturas facturas = new facturas(conn);

            panelprincipal.add(facturas, javax.swing.JLayeredPane.DEFAULT_LAYER);
            facturas.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (facturas.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                facturas.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            facturas.setLocation(cord, 0);
            facturas.toFront();
        }
        if (selmenu.equals("Entradas Papel")) {////***************************opcion de arbol1***********************************
            entradas_productos entradas_productos = new entradas_productos(conn);

            panelprincipal.add(entradas_productos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            entradas_productos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (entradas_productos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                entradas_productos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            entradas_productos.setLocation(cord, 0);
            entradas_productos.toFront();
        }
        if (selmenu.equals("Consumos Papel")) {////***************************opcion de arbol1***********************************
            consumos_productos consumos_productos = new consumos_productos(conn);

            panelprincipal.add(consumos_productos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            consumos_productos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (consumos_productos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                consumos_productos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            consumos_productos.setLocation(cord, 0);
            consumos_productos.toFront();
        }
        if (selmenu.equals("Inventario Papel")) {////***************************opcion de arbol1***********************************
            almacen_rollos almacen_rollos = new almacen_rollos(conn);
            panelprincipal.add(almacen_rollos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            almacen_rollos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (almacen_rollos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                almacen_rollos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            almacen_rollos.setLocation(cord, 0);
            almacen_rollos.toFront();
        }
        if (selmenu.equals("Saldos y Programa de cobros")) {////***************************opcion de arbol1***********************************
            saldos_pagos saldos_pagos = new saldos_pagos(conn);
            panelprincipal.add(saldos_pagos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            saldos_pagos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (saldos_pagos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                saldos_pagos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            saldos_pagos.setLocation(cord, 0);
            saldos_pagos.toFront();
        }
        if (selmenu.equals("Facturas Pagadas")) {////***************************opcion de arbol1***********************************
            facturas_pagadas2 facturas_pagadas2 = new facturas_pagadas2(conn);
            panelprincipal.add(facturas_pagadas2, javax.swing.JLayeredPane.DEFAULT_LAYER);
            facturas_pagadas2.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (facturas_pagadas2.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                facturas_pagadas2.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            facturas_pagadas2.setLocation(cord, 0);
            facturas_pagadas2.toFront();
        }
        if (selmenu.equals("Ingresos")) {////***************************opcion de arbol1***********************************
            pagos pagos = new pagos(conn);
            panelprincipal.add(pagos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            pagos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (pagos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                pagos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            pagos.setLocation(cord, 0);
            pagos.toFront();
        }
        if (selmenu.equals("Corrugadora Autorizar")) {////***************************opcion de arbol1***********************************
            programas_corr_autorizar programas_corr_autorizar = new programas_corr_autorizar(conn);
            panelprincipal.add(programas_corr_autorizar, javax.swing.JLayeredPane.DEFAULT_LAYER);
            programas_corr_autorizar.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (programas_corr_autorizar.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                programas_corr_autorizar.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            programas_corr_autorizar.setLocation(cord, 0);
            programas_corr_autorizar.toFront();
        }
        if (selmenu.equals("Cancela CFDI")) {////***************************opcion de arbol1***********************************
            CFDIcancelasat32 CFDIcancelasat32 = new CFDIcancelasat32(conn);
            panelprincipal.add(CFDIcancelasat32, javax.swing.JLayeredPane.DEFAULT_LAYER);
            CFDIcancelasat32.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (CFDIcancelasat32.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                CFDIcancelasat32.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            CFDIcancelasat32.setLocation(cord, 0);
            CFDIcancelasat32.toFront();
        }
        if (selmenu.equals("Certificados")) {////***************************opcion de arbol1***********************************
            certificados certificados = new certificados(conn);
            panelprincipal.add(certificados, javax.swing.JLayeredPane.DEFAULT_LAYER);
            certificados.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (certificados.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                certificados.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            certificados.setLocation(cord, 0);
            certificados.toFront();
        }
        if (selmenu.equals("SMTP Mail")) {////***************************opcion de arbol1***********************************
            mail mail = new mail(conn);
            panelprincipal.add(mail, javax.swing.JLayeredPane.DEFAULT_LAYER);
            mail.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (mail.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                mail.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            mail.setLocation(cord, 0);
            mail.toFront();
        }
        if (selmenu.equals("Certificado")) {////***************************opcion de arbol1***********************************
            datos_cfd_certificado = new datos_cfd_certificado(this, true, conn);
            datos_cfd_certificado.setLocationRelativeTo(this);
            datos_cfd_certificado.setVisible(true);
            datos_cfd_certificado = null;
        }
        if (selmenu.equals("Llave")) {////***************************opcion de arbol1***********************************
            datos_cfd_llave = new datos_cfd_llave(this, true, conn);
            datos_cfd_llave.setLocationRelativeTo(this);
            datos_cfd_llave.setVisible(true);
            datos_cfd_llave = null;
        }
        if (selmenu.equals("URL Files")) {////***************************opcion de arbol1***********************************
            datos_rutas = new datos_rutas(this, true);
            datos_rutas.setLocationRelativeTo(this);
            datos_rutas.setVisible(true);
            datos_rutas = null;
        }
        if (selmenu.equals("Notas de Credito")) {////***************************opcion de arbol1***********************************
            notas_credito notas_credito = new notas_credito(conn);
            panelprincipal.add(notas_credito, javax.swing.JLayeredPane.DEFAULT_LAYER);
            notas_credito.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (notas_credito.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                notas_credito.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            notas_credito.setLocation(cord, 0);
            notas_credito.toFront();
        }
        if (selmenu.equals("Reporte SAT")) {////***************************opcion de arbol1***********************************
            reporte_sat reporte_sat = new reporte_sat(conn);
            panelprincipal.add(reporte_sat, javax.swing.JLayeredPane.DEFAULT_LAYER);
            reporte_sat.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (reporte_sat.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                reporte_sat.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            reporte_sat.setLocation(cord, 0);
            reporte_sat.toFront();
        }
        if (selmenu.equals("Mtto. Diario")) {////***************************opcion de arbol1***********************************
            mantenimiento_diario mantenimiento_diario = new mantenimiento_diario(conn);
            panelprincipal.add(mantenimiento_diario, javax.swing.JLayeredPane.DEFAULT_LAYER);
            mantenimiento_diario.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (mantenimiento_diario.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                mantenimiento_diario.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            mantenimiento_diario.setLocation(cord, 0);
            mantenimiento_diario.toFront();
        }
        if (selmenu.equals("Areas de la Empresa")) {////***************************opcion de arbol1***********************************
            areas_empresa areas_empresa = new areas_empresa(conn);
            panelprincipal.add(areas_empresa, javax.swing.JLayeredPane.DEFAULT_LAYER);
            areas_empresa.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (areas_empresa.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                areas_empresa.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            areas_empresa.setLocation(cord, 0);
            areas_empresa.toFront();
        }
        if (selmenu.equals("Suajes")) {////***************************opcion de arbol1***********************************
            suajes suajes = new suajes(conn);
            panelprincipal.add(suajes, javax.swing.JLayeredPane.DEFAULT_LAYER);
            suajes.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (suajes.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                suajes.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            suajes.setLocation(cord, 0);
            suajes.toFront();
        }
        if (selmenu.equals("Grabados")) {////***************************opcion de arbol1***********************************
            grabados grabados = new grabados(conn);
            panelprincipal.add(grabados, javax.swing.JLayeredPane.DEFAULT_LAYER);
            grabados.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (grabados.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                grabados.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            grabados.setLocation(cord, 0);
            grabados.toFront();
        }
        if (selmenu.equals("Claves Almacen")) {////***************************opcion de arbol1***********************************
            no_almacen_rollos no_almacen_rollos = new no_almacen_rollos(conn);
            panelprincipal.add(no_almacen_rollos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            no_almacen_rollos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (no_almacen_rollos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                no_almacen_rollos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            no_almacen_rollos.setLocation(cord, 0);
            no_almacen_rollos.toFront();
        }
        if (selmenu.equals("Inventario por Almacen")) {////***************************opcion de arbol1***********************************
            almacen_rollos_almacen almacen_rollos_almacen = new almacen_rollos_almacen(conn);
            panelprincipal.add(almacen_rollos_almacen, javax.swing.JLayeredPane.DEFAULT_LAYER);
            almacen_rollos_almacen.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (almacen_rollos_almacen.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                almacen_rollos_almacen.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            almacen_rollos_almacen.setLocation(cord, 0);
            almacen_rollos_almacen.toFront();
        }
        if (selmenu.equals("Entradas Proveedor")) {////***************************opcion de arbol1***********************************

            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
            busca_fechas busca_fechas = new busca_fechas(null, true);
            busca_fechas.setLocationRelativeTo(this);
            busca_fechas.setVisible(true);
            String estado = busca_fechas.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {
                String fi = fechainsertar.format(busca_fechas.getFechaini());
                String ft = fechainsertar.format(busca_fechas.getFechafin());
                Double kgperiodo = 0.0;
                //obtienes los kilogramos totales
                rs0 = null;
                try {
                    String senSQL = "SELECT count(detalle.id_entrada_producto_detalle) as norollos,sum(detalle.preciounitario*cantidad) as importe,sum(detalle.cantidad) as kg FROM (SELECT entradas_productos.fecha,entradas_productos.id_entrada_producto,entradas_productos.remision,proveedores.nombre,entradas_productos_detalle.*,productos.descripcion,productos.unidad,COALESCE(consumos.cantidadconsumo,0) as cantidadconsumo,consumos.fechaconsumo FROM (entradas_productos LEFT JOIN proveedores ON entradas_productos.id_proveedor=proveedores.id_proveedor) INNER JOIN  ((entradas_productos_detalle LEFT JOIN (SELECT consumos_productos.id_entrada_producto_detalle,sum(consumos_productos.cantidad) as cantidadconsumo,max(consumos_productos.fecha) as fechaconsumo FROM consumos_productos WHERE consumos_productos.estatus<>'Can' GROUP BY consumos_productos.id_entrada_producto_detalle) as consumos ON entradas_productos_detalle.id_entrada_producto_detalle=consumos.id_entrada_producto_detalle) LEFT JOIN productos ON entradas_productos_detalle.clave_producto=productos.clave) ON entradas_productos.id_entrada_producto=entradas_productos_detalle.id_entrada_producto WHERE (entradas_productos_detalle.estatus<>'Can' AND (entradas_productos.fecha>='" + fi + " 00:00:00' AND entradas_productos.fecha<='" + ft + " 23:59:59')) ORDER BY entradas_productos.fecha) as detalle;";
                    rs0 = conexion.consulta(senSQL, conn);
                    if (rs0.next()) {
                        kgperiodo = rs0.getDouble("kg");
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }

                JasperViewer visor = null;
                JasperPrint jasperPrint = null;
                try {
                    String datos = "REPORTE GENERADO DEL " + fechamediana.format(busca_fechas.getFechaini()) + "  AL  " + fechamediana.format(busca_fechas.getFechafin());
                    Map pars = new HashMap();
                    File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                    pars.put("logoempresa", new FileInputStream(fichero));
                    pars.put("subtitulo", datos);
                    pars.put("fechaini", new java.sql.Timestamp(busca_fechas.getFechaini().getTime()));
                    pars.put("fechafin", new java.sql.Timestamp(busca_fechas.getFechafin().getTime()));
                    pars.put("senSQL", "");
                    pars.put("version", resourceMap.getString("Application.title"));
                    pars.put("kgperiodo", kgperiodo);
                    JasperReport masterReport = null;
                    try {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/entradas_proveedor_grafica.jasper"));
                    } catch (JRException e) {
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
                    visor = new JasperViewer(jasperPrint, false);
                    visor.setTitle("REPORTE");
                    visor.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
                }
            }

            busca_fechas = null;
        }
        if (selmenu.equals("Entradas Tipo de Papel")) {////***************************opcion de arbol1***********************************
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
            busca_fechas busca_fechas = new busca_fechas(null, true);
            busca_fechas.setLocationRelativeTo(this);
            busca_fechas.setVisible(true);
            String estado = busca_fechas.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {
                String fi = fechainsertar.format(busca_fechas.getFechaini());
                String ft = fechainsertar.format(busca_fechas.getFechafin());
                Double kgperiodo = 0.0;
                //obtienes los kilogramos totales
                rs0 = null;
                try {
                    String senSQL = "SELECT count(detalle.id_entrada_producto_detalle) as norollos,sum(detalle.preciounitario*cantidad) as importe,sum(detalle.cantidad) as kg FROM (SELECT entradas_productos.fecha,entradas_productos.id_entrada_producto,entradas_productos.remision,proveedores.nombre,entradas_productos_detalle.*,productos.descripcion,productos.unidad,COALESCE(consumos.cantidadconsumo,0) as cantidadconsumo,consumos.fechaconsumo FROM (entradas_productos LEFT JOIN proveedores ON entradas_productos.id_proveedor=proveedores.id_proveedor) INNER JOIN  ((entradas_productos_detalle LEFT JOIN (SELECT consumos_productos.id_entrada_producto_detalle,sum(consumos_productos.cantidad) as cantidadconsumo,max(consumos_productos.fecha) as fechaconsumo FROM consumos_productos WHERE consumos_productos.estatus<>'Can' GROUP BY consumos_productos.id_entrada_producto_detalle) as consumos ON entradas_productos_detalle.id_entrada_producto_detalle=consumos.id_entrada_producto_detalle) LEFT JOIN productos ON entradas_productos_detalle.clave_producto=productos.clave) ON entradas_productos.id_entrada_producto=entradas_productos_detalle.id_entrada_producto WHERE (entradas_productos_detalle.estatus<>'Can' AND (entradas_productos.fecha>='" + fi + " 00:00:00' AND entradas_productos.fecha<='" + ft + " 23:59:59')) ORDER BY entradas_productos.fecha) as detalle;";
                    rs0 = conexion.consulta(senSQL, conn);
                    if (rs0.next()) {
                        kgperiodo = rs0.getDouble("kg");
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }

                JasperViewer visor = null;
                JasperPrint jasperPrint = null;
                try {
                    String datos = "REPORTE GENERADO DEL " + fechamediana.format(busca_fechas.getFechaini()) + "  AL  " + fechamediana.format(busca_fechas.getFechafin());
                    Map pars = new HashMap();
                    File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                    pars.put("logoempresa", new FileInputStream(fichero));
                    pars.put("subtitulo", datos);
                    pars.put("fechaini", new java.sql.Timestamp(busca_fechas.getFechaini().getTime()));
                    pars.put("fechafin", new java.sql.Timestamp(busca_fechas.getFechafin().getTime()));
                    pars.put("senSQL", "");
                    pars.put("version", resourceMap.getString("Application.title"));
                    pars.put("kgperiodo", kgperiodo);
                    JasperReport masterReport = null;
                    try {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/entradas_tipopapel_grafica.jasper"));
                    } catch (JRException e) {
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
                    visor = new JasperViewer(jasperPrint, false);
                    visor.setTitle("REPORTE");
                    visor.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
                }
            }
            busca_fechas = null;
        }
        if (selmenu.equals("Consumos Proveedor")) {////***************************opcion de arbol1***********************************

            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
            busca_fechas busca_fechas = new busca_fechas(null, true);
            busca_fechas.setLocationRelativeTo(this);
            busca_fechas.setVisible(true);
            String estado = busca_fechas.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {
                String fi = fechainsertar.format(busca_fechas.getFechaini());
                String ft = fechainsertar.format(busca_fechas.getFechafin());
                Double kgperiodo = 0.0;
                //obtienes los kilogramos totales
                rs0 = null;
                try {
                    String senSQL = "SELECT detalle.clave_producto,count(detalle.id_entrada_producto_detalle) as norollos,sum(detalle.preciounitario*cantidad) as importe,sum(detalle.cantidad) as kg FROM (SELECT entradas_productos.remision,proveedores.nombre,consumos_productos.*,productos.descripcion,productos.unidad,entradas_productos_detalle.preciounitario FROM (consumos_productos LEFT JOIN productos ON consumos_productos.clave_producto=productos.clave) LEFT JOIN (entradas_productos_detalle LEFT JOIN (entradas_productos LEFT JOIN proveedores ON entradas_productos.id_proveedor=proveedores.id_proveedor) ON entradas_productos_detalle.id_entrada_producto=entradas_productos.id_entrada_producto) ON consumos_productos.id_entrada_producto_detalle=entradas_productos_detalle.id_entrada_producto_detalle WHERE (consumos_productos.estatus<>'Can' AND (consumos_productos.fecha>='" + fi + " 00:00:00' AND consumos_productos.fecha<='" + ft + " 23:59:59')) ORDER BY consumos_productos.fecha) as detalle GROUP BY detalle.clave_producto ORDER BY sum(detalle.cantidad) DESC;";
                    rs0 = conexion.consulta(senSQL, conn);
                    while (rs0.next()) {
                        kgperiodo += rs0.getDouble("kg");
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }

                JasperViewer visor = null;
                JasperPrint jasperPrint = null;
                try {
                    String datos = "REPORTE GENERADO DEL " + fechamediana.format(busca_fechas.getFechaini()) + "  AL  " + fechamediana.format(busca_fechas.getFechafin());
                    Map pars = new HashMap();
                    File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                    pars.put("logoempresa", new FileInputStream(fichero));
                    pars.put("subtitulo", datos);
                    pars.put("fechaini", new java.sql.Timestamp(busca_fechas.getFechaini().getTime()));
                    pars.put("fechafin", new java.sql.Timestamp(busca_fechas.getFechafin().getTime()));
                    pars.put("senSQL", "");
                    pars.put("version", resourceMap.getString("Application.title"));
                    pars.put("kgperiodo", kgperiodo);
                    JasperReport masterReport = null;
                    try {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/consumos_proveedor_grafica.jasper"));
                    } catch (JRException e) {
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
                    visor = new JasperViewer(jasperPrint, false);
                    visor.setTitle("REPORTE");
                    visor.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
                }
            }

            busca_fechas = null;
        }
        if (selmenu.equals("Consumos Tipo de Papel")) {////***************************opcion de arbol1***********************************

            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
            busca_fechas busca_fechas = new busca_fechas(null, true);
            busca_fechas.setLocationRelativeTo(this);
            busca_fechas.setVisible(true);
            String estado = busca_fechas.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {
                String fi = fechainsertar.format(busca_fechas.getFechaini());
                String ft = fechainsertar.format(busca_fechas.getFechafin());
                Double kgperiodo = 0.0;
                //obtienes los kilogramos totales
                rs0 = null;
                try {
                    String senSQL = "SELECT detalle.clave_producto,count(detalle.id_entrada_producto_detalle) as norollos,sum(detalle.preciounitario*cantidad) as importe,sum(detalle.cantidad) as kg FROM (SELECT entradas_productos.remision,proveedores.nombre,consumos_productos.*,productos.descripcion,productos.unidad,entradas_productos_detalle.preciounitario FROM (consumos_productos LEFT JOIN productos ON consumos_productos.clave_producto=productos.clave) LEFT JOIN (entradas_productos_detalle LEFT JOIN (entradas_productos LEFT JOIN proveedores ON entradas_productos.id_proveedor=proveedores.id_proveedor) ON entradas_productos_detalle.id_entrada_producto=entradas_productos.id_entrada_producto) ON consumos_productos.id_entrada_producto_detalle=entradas_productos_detalle.id_entrada_producto_detalle WHERE (consumos_productos.estatus<>'Can' AND (consumos_productos.fecha>='" + fi + " 00:00:00' AND consumos_productos.fecha<='" + ft + " 23:59:59')) ORDER BY consumos_productos.fecha) as detalle GROUP BY detalle.clave_producto ORDER BY sum(detalle.cantidad) DESC;";
                    rs0 = conexion.consulta(senSQL, conn);
                    while (rs0.next()) {
                        kgperiodo += rs0.getDouble("kg");
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }

                JasperViewer visor = null;
                JasperPrint jasperPrint = null;
                try {
                    String datos = "REPORTE GENERADO DEL " + fechamediana.format(busca_fechas.getFechaini()) + "  AL  " + fechamediana.format(busca_fechas.getFechafin());
                    Map pars = new HashMap();
                    File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                    pars.put("logoempresa", new FileInputStream(fichero));
                    pars.put("subtitulo", datos);
                    pars.put("fechaini", new java.sql.Timestamp(busca_fechas.getFechaini().getTime()));
                    pars.put("fechafin", new java.sql.Timestamp(busca_fechas.getFechafin().getTime()));
                    pars.put("senSQL", "");
                    pars.put("version", resourceMap.getString("Application.title"));
                    pars.put("kgperiodo", kgperiodo);
                    JasperReport masterReport = null;
                    try {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/consumos_tipopapel_grafica.jasper"));
                    } catch (JRException e) {
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
                    visor = new JasperViewer(jasperPrint, false);
                    visor.setTitle("REPORTE");
                    visor.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
                }
            }
            busca_fechas = null;
        }
        if (selmenu.equals("Inventario Papel Detalle")) {////***************************opcion de arbol1***********************************
            almacen_rollos_detalle almacen_rollos_detalle = new almacen_rollos_detalle(conn);
            panelprincipal.add(almacen_rollos_detalle, javax.swing.JLayeredPane.DEFAULT_LAYER);
            almacen_rollos_detalle.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (almacen_rollos_detalle.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                almacen_rollos_detalle.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            almacen_rollos_detalle.setLocation(cord, 0);
            almacen_rollos_detalle.toFront();
        }
        if (selmenu.equals("Entradas Inventario Corrugadora")) {////***************************opcion de arbol1***********************************
            entrada_inventario_corr entrada_inventario_corr = new entrada_inventario_corr(conn);
            panelprincipal.add(entrada_inventario_corr, javax.swing.JLayeredPane.DEFAULT_LAYER);
            entrada_inventario_corr.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (entrada_inventario_corr.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                entrada_inventario_corr.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            entrada_inventario_corr.setLocation(cord, 0);
            entrada_inventario_corr.toFront();
        }
        if (selmenu.equals("Inventario Corrugadora")) {////***************************opcion de arbol1***********************************
            inventario_corrugadora inventario_corrugadora = new inventario_corrugadora(conn);
            panelprincipal.add(inventario_corrugadora, javax.swing.JLayeredPane.DEFAULT_LAYER);
            inventario_corrugadora.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (inventario_corrugadora.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                inventario_corrugadora.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            inventario_corrugadora.setLocation(cord, 0);
            inventario_corrugadora.toFront();
        }
        if (selmenu.equals("Inventario Conversion")) {////***************************opcion de arbol1***********************************
            inventario_conversion inventario_conversion = new inventario_conversion(conn);
            panelprincipal.add(inventario_conversion, javax.swing.JLayeredPane.DEFAULT_LAYER);
            inventario_conversion.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (inventario_conversion.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                inventario_conversion.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            inventario_conversion.setLocation(cord, 0);
            inventario_conversion.toFront();
        }
        if (selmenu.equals("Entradas Inventario Conversion")) {////***************************opcion de arbol1***********************************
            entrada_inventario_conversion entrada_inventario_conversion = new entrada_inventario_conversion(conn);
            panelprincipal.add(entrada_inventario_conversion, javax.swing.JLayeredPane.DEFAULT_LAYER);
            entrada_inventario_conversion.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (entrada_inventario_conversion.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                entrada_inventario_conversion.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            entrada_inventario_conversion.setLocation(cord, 0);
            entrada_inventario_conversion.toFront();
        }
        if (selmenu.equals("Salidas Inventario Conversion")) {////***************************opcion de arbol1***********************************
            salidas_inventario_conversion salidas_inventario_conversion = new salidas_inventario_conversion(conn);
            panelprincipal.add(salidas_inventario_conversion, javax.swing.JLayeredPane.DEFAULT_LAYER);
            salidas_inventario_conversion.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (salidas_inventario_conversion.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                salidas_inventario_conversion.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            salidas_inventario_conversion.setLocation(cord, 0);
            salidas_inventario_conversion.toFront();
        }
        if (selmenu.equals("Bitacora Clientes")) {////***************************opcion de arbol1***********************************
            bitacora_clientes bitacora_clientes = new bitacora_clientes(conn);
            panelprincipal.add(bitacora_clientes, javax.swing.JLayeredPane.DEFAULT_LAYER);
            bitacora_clientes.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (bitacora_clientes.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                bitacora_clientes.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            bitacora_clientes.setLocation(cord, 0);
            bitacora_clientes.toFront();
        }
        if (selmenu.equals("Contrarecibos")) {////***************************opcion de arbol1***********************************
            contrarecibos contrarecibos = new contrarecibos(conn);
            panelprincipal.add(contrarecibos, javax.swing.JLayeredPane.DEFAULT_LAYER);
            contrarecibos.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (contrarecibos.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                contrarecibos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            contrarecibos.setLocation(cord, 0);
            contrarecibos.toFront();
        }
        if (selmenu.equals("Cotizaciones")) {////***************************opcion de arbol1***********************************
            cotizaciones cotizaciones = new cotizaciones(conn);
            panelprincipal.add(cotizaciones, javax.swing.JLayeredPane.DEFAULT_LAYER);
            cotizaciones.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (cotizaciones.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                cotizaciones.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            cotizaciones.setLocation(cord, 0);
            cotizaciones.toFront();
        }
        if (selmenu.equals("Detalles Remisionado-Clientes")) {////***************************opcion de arbol1***********************************
            try {
                clientes_radiografia clientes_radiografia = new clientes_radiografia(conn);
                panelprincipal.add(clientes_radiografia, javax.swing.JLayeredPane.DEFAULT_LAYER);
                clientes_radiografia.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (clientes_radiografia.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    clientes_radiografia.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                clientes_radiografia.setLocation(cord, 0);
                //clientes_radiografia.setMaximum(true);
                clientes_radiografia.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Detalle Pendientes x entregar")) {////***************************opcion de arbol1***********************************
            try {
                radiografia_planeacion radiografia_planeacion = new radiografia_planeacion(conn);
                panelprincipal.add(radiografia_planeacion, javax.swing.JLayeredPane.DEFAULT_LAYER);
                radiografia_planeacion.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (radiografia_planeacion.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    radiografia_planeacion.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                radiografia_planeacion.setLocation(cord, 0);
                //clientes_radiografia.setMaximum(true);
                radiografia_planeacion.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Requerimiento Papel")) {////***************************opcion de arbol1***********************************
            try {
                requerimiento_resistencias requerimiento_resistencias = new requerimiento_resistencias(conn);
                panelprincipal.add(requerimiento_resistencias, javax.swing.JLayeredPane.DEFAULT_LAYER);
                requerimiento_resistencias.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (requerimiento_resistencias.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    requerimiento_resistencias.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                requerimiento_resistencias.setLocation(cord, 0);
                //clientes_radiografia.setMaximum(true);
                requerimiento_resistencias.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Requerimiento Tintas")) {////***************************opcion de arbol1***********************************
            try {
                requerimiento_tintas requerimiento_tintas = new requerimiento_tintas(conn);
                panelprincipal.add(requerimiento_tintas, javax.swing.JLayeredPane.DEFAULT_LAYER);
                requerimiento_tintas.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (requerimiento_tintas.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    requerimiento_tintas.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                requerimiento_tintas.setLocation(cord, 0);
                //clientes_radiografia.setMaximum(true);
                requerimiento_tintas.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Parametros")) {////***************************opcion de arbol1***********************************
            datos_parametros = new datos_parametros(this, true, conn);
            datos_parametros.setLocationRelativeTo(this);
            datos_parametros.setVisible(true);
            datos_parametros = null;
        }
        if (selmenu.equals("Herr. Programacion Corrugadora")) {////***************************opcion de arbol1***********************************
            corrugadora_herramienta_combina = new corrugadora_herramienta_combina(this, true, conn, "", "");
            corrugadora_herramienta_combina.setLocationRelativeTo(this);
            corrugadora_herramienta_combina.setVisible(true);
            corrugadora_herramienta_combina = null;
        }
        if (selmenu.equals("Costos Detalle-Procesos")) {////***************************opcion de arbol1***********************************
            File rutaarchivo = new File(System.getProperty("user.home") + "/costos.xls");
            try {
                //Se crea el libro Excel
                WritableWorkbook workbook = Workbook.createWorkbook(rutaarchivo);
                //Se crea una nueva hoja dentro del libro
                WritableSheet sheet = workbook.createSheet("Datos", 0);
                //formatos de texto
                WritableFont arial12b = new WritableFont(WritableFont.createFont("Arial"), 12, WritableFont.BOLD, false);
                WritableFont arial10b = new WritableFont(WritableFont.createFont("Arial"), 10, WritableFont.BOLD, false);
                WritableFont arial9 = new WritableFont(WritableFont.createFont("Arial"), 9, WritableFont.NO_BOLD, false);

                WritableCellFormat arial10fsupertitulo = new WritableCellFormat(arial12b);

                WritableCellFormat arial10ftitulo = new WritableCellFormat(arial10b);
                arial10ftitulo.setBackground(Colour.LIME);//.SKY_BLUE
                arial10ftitulo.setAlignment(Alignment.CENTRE);
                arial10ftitulo.setVerticalAlignment(VerticalAlignment.CENTRE);

                WritableCellFormat subarial10ftitulo = new WritableCellFormat(arial10b);
                subarial10ftitulo.setBackground(Colour.SKY_BLUE);//.SKY_BLUE
                subarial10ftitulo.setAlignment(Alignment.CENTRE);
                subarial10ftitulo.setVerticalAlignment(VerticalAlignment.CENTRE);

                WritableCellFormat arial10fdetalle = new WritableCellFormat(arial9);
                WritableCellFormat arial10fdetallec = new WritableCellFormat(arial9);
                arial10fdetallec.setAlignment(Alignment.CENTRE);

                int filainicial = 5;

                //titulo del documento
                sheet.addImage(new jxl.write.WritableImage(0, 0, 2, 4, new java.io.File(conexion.Directorio() + "/logoempresa.png")));
                sheet.addCell(new jxl.write.Label(0, filainicial, "" + this.getTitle(), arial10fsupertitulo));

                //escribimos los datos en las celdas (columnas,filas)
                filainicial++;//incrementa las filas
                sheet.addCell(new jxl.write.Label(0, filainicial, "Cliente", arial10ftitulo));
                sheet.setColumnView(0, 22);
                sheet.addCell(new jxl.write.Label(1, filainicial, "Clave Art.", arial10ftitulo));
                sheet.setColumnView(1, 11);
                sheet.addCell(new jxl.write.Label(2, filainicial, "Articulo", arial10ftitulo));
                sheet.setColumnView(2, 33);
                sheet.addCell(new jxl.write.Label(3, filainicial, "Cantidad", arial10ftitulo));
                sheet.setColumnView(3, 11);
                sheet.addCell(new jxl.write.Label(4, filainicial, "$ Millar", arial10ftitulo));
                sheet.setColumnView(4, 11);
                sheet.addCell(new jxl.write.Label(5, filainicial, "kg_Teorico", arial10ftitulo));
                sheet.setColumnView(5, 12);
                sheet.addCell(new jxl.write.Label(6, filainicial, "$PK_Teorico", arial10ftitulo));
                sheet.setColumnView(6, 12);
                sheet.addCell(new jxl.write.Label(7, filainicial, "kg_Real", arial10ftitulo));
                sheet.setColumnView(7, 12);
                sheet.addCell(new jxl.write.Label(8, filainicial, "$PK_Real", arial10ftitulo));
                sheet.setColumnView(8, 12);
                sheet.addCell(new jxl.write.Label(9, filainicial, "$PK_Costo", arial10ftitulo));
                sheet.setColumnView(9, 12);
                sheet.addCell(new jxl.write.Label(10, filainicial, "", arial10ftitulo));
                sheet.setColumnView(10, 3);
                sheet.addCell(new jxl.write.Label(11, filainicial, "PAPEL", arial10ftitulo));
                sheet.setColumnView(11, 9);
                //coloca las columnas de las maquinas
                String arreglo_maq[] = new String[20];
                int columnas = 11;
                int columnastodos = columnas;
                rs0 = null;
                try {
                    String senSQL = "SELECT * FROM maquinas ORDER BY id_maquina";
                    rs0 = conexion.consulta(senSQL, conn);
                    int vector = 0;
                    while (rs0.next()) {
                        columnastodos++;
                        String m = rs0.getString("clave");
                        sheet.addCell(new jxl.write.Label(columnas + vector + 1, filainicial, m, arial10ftitulo));
                        sheet.setColumnView(columnas + vector + 1, 9);
                        arreglo_maq[vector] = m;
                        vector++;
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }
                columnastodos++;
                sheet.addCell(new jxl.write.Label(columnastodos, filainicial, "FLETES", arial10ftitulo));
                sheet.setColumnView(columnastodos, 9);
                columnastodos++;
                sheet.addCell(new jxl.write.Label(columnastodos, filainicial, "COMISIÓN", arial10ftitulo));
                sheet.setColumnView(columnastodos, 9);
                columnastodos++;
                sheet.addCell(new jxl.write.Label(columnastodos, filainicial, "OTROS", arial10ftitulo));
                sheet.setColumnView(columnastodos, 9);

                filainicial++;
                String flag_art = "";
                rs0 = null;
                try {
                    String senSQL = "SELECT art_acum.*,articulos_maquinas.clave,maquinas.costokg FROM (SELECT clientes.nombre,articulos_pruebas.clavearticulo,articulos.articulo,articulos_pruebas.cantidad,articulos.preciomillar,articulos.m2,articulos.claveresistencia,resistencias.pesom2,resistencias.pesom2real,articulos.kg as kg_teorico,((articulos.preciomillar/1000)/articulos.kg) as pk_teorico,(articulos.m2*resistencias.pesom2real) as kg_real,((articulos.preciomillar/1000)/(articulos.m2*resistencias.pesom2real)) as pk_real,resistencias.precio_kg as papel_kg FROM articulos_pruebas LEFT JOIN ((articulos LEFT JOIN resistencias ON articulos.claveresistencia=resistencias.clave) LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON articulos_pruebas.clavearticulo=articulos.clavearticulo) as art_acum INNER JOIN (articulos_maquinas LEFT JOIN maquinas ON articulos_maquinas.clave=maquinas.clave) ON art_acum.clavearticulo=articulos_maquinas.clavearticulo ORDER BY art_acum.cantidad DESC,art_acum.clavearticulo,articulos_maquinas.id_articulo_maquina";
                    rs0 = conexion.consulta(senSQL, conn);
                    while (rs0.next()) {
                        String clave_art = rs0.getString("clavearticulo");
                        if (!clave_art.equals(flag_art)) { //ingresa los datos de la partida
                            filainicial++;
                            flag_art = clave_art;
                            sheet.addCell(new jxl.write.Label(0, filainicial, rs0.getString("nombre"), arial10fdetalle));
                            sheet.addCell(new jxl.write.Label(1, filainicial, clave_art, arial10fdetallec));
                            sheet.addCell(new jxl.write.Label(2, filainicial, rs0.getString("articulo"), arial10fdetalle));
                            sheet.addCell(new jxl.write.Number(3, filainicial, rs0.getInt("cantidad"), arial10fdetalle));
                            sheet.addCell(new jxl.write.Number(4, filainicial, rs0.getDouble("preciomillar"), arial10fdetalle));
                            sheet.addCell(new jxl.write.Number(5, filainicial, rs0.getDouble("kg_teorico"), arial10fdetalle));
                            sheet.addCell(new jxl.write.Number(6, filainicial, rs0.getDouble("pk_teorico"), arial10fdetalle));
                            sheet.addCell(new jxl.write.Number(7, filainicial, rs0.getDouble("kg_real"), arial10fdetalle));
                            sheet.addCell(new jxl.write.Number(8, filainicial, rs0.getDouble("pk_real"), arial10fdetalle));
                            sheet.addCell(new jxl.write.Number(9, filainicial, 0, arial10fdetalle));
                            sheet.addCell(new jxl.write.Label(10, filainicial, "", arial10fdetalle));
                            sheet.addCell(new jxl.write.Number(11, filainicial, rs0.getDouble("papel_kg"), arial10fdetalle));
                        }
                        String cm = rs0.getString("clave");
                        for (int v = 0; v < arreglo_maq.length; v++) {
                            String cm_t = "" + arreglo_maq[v];
                            if (cm.equals(cm_t)) {
                                sheet.addCell(new jxl.write.Number(columnas + v + 1, filainicial, rs0.getDouble("costokg"), arial10fdetalle));
                            }
                        }
                    }

                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }

                //Escribimos los resultados al fichero Excel
                workbook.write();
                workbook.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "EL ARCHIVO ESTA ABIERTO O NO SE PUEDE CREAR" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            } catch (WriteException exe) {
                JOptionPane.showMessageDialog(this, "ERROR AL EXPORTAR DATOS" + exe, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            //abrir el documento creado
            try {
                Desktop.getDesktop().open(rutaarchivo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "NO SE PUEDE ABRIR EL ARCHIVO\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Remisiones Externas")) {////***************************opcion de arbol1***********************************
            try {
                remisiones_externas remisiones_externas = new remisiones_externas(conn);
                panelprincipal.add(remisiones_externas, javax.swing.JLayeredPane.DEFAULT_LAYER);
                remisiones_externas.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (remisiones_externas.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    remisiones_externas.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                remisiones_externas.setLocation(cord, 0);
                //clientes_radiografia.setMaximum(true);
                remisiones_externas.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Ingresos Anticipados")) {////***************************opcion de arbol1***********************************
            try {
                pagos_anticipos pagos_anticipos = new pagos_anticipos(conn);
                panelprincipal.add(pagos_anticipos, javax.swing.JLayeredPane.DEFAULT_LAYER);
                pagos_anticipos.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (pagos_anticipos.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    pagos_anticipos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                pagos_anticipos.setLocation(cord, 0);
                pagos_anticipos.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Remisiones por Facturar")) {////***************************opcion de arbol1***********************************
            busca_remisiones busca_remisiones = new busca_remisiones(null, true, conn, "", "");
            busca_remisiones.setLocationRelativeTo(this);
            busca_remisiones.setVisible(true);
            busca_remisiones = null;
        }
        if (selmenu.equals("PM Ventas")) {////***************************opcion de arbol1***********************************
            try {
                valor_privilegio = conexion.obtener_privilegios(conn, selmenu);//obtiene valor de privilegio
                pm_ventas pm_ventas = new pm_ventas(conn, valor_privilegio);
                panelprincipal.add(pm_ventas, javax.swing.JLayeredPane.DEFAULT_LAYER);
                pm_ventas.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (pm_ventas.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    pm_ventas.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                pm_ventas.setLocation(cord, 0);
                pm_ventas.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("PM Ingresos")) {////***************************opcion de arbol1***********************************
            try {
                valor_privilegio = conexion.obtener_privilegios(conn, selmenu);//obtiene valor de privilegio
                pm_pagos pm_pagos = new pm_pagos(conn, valor_privilegio);
                panelprincipal.add(pm_pagos, javax.swing.JLayeredPane.DEFAULT_LAYER);
                pm_pagos.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (pm_pagos.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    pm_pagos.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                pm_pagos.setLocation(cord, 0);
                pm_pagos.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Graficas Producción")) {////***************************opcion de arbol1***********************************
            try {
                radiografia_produccion radiografia_produccion = new radiografia_produccion(conn);
                panelprincipal.add(radiografia_produccion, javax.swing.JLayeredPane.DEFAULT_LAYER);
                radiografia_produccion.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (radiografia_produccion.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    radiografia_produccion.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                radiografia_produccion.setLocation(cord, 0);
                //clientes_radiografia.setMaximum(true);
                radiografia_produccion.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Remisionado Grafica Mes")) {////***************************opcion de arbol1***********************************

            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
            busca_fechas busca_fechas = new busca_fechas(null, true);
            busca_fechas.setLocationRelativeTo(this);
            busca_fechas.setVisible(true);
            String estado = busca_fechas.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {
                String fi = fechainsertar.format(busca_fechas.getFechaini());
                String ft = fechainsertar.format(busca_fechas.getFechafin());
                Double kgperiodo = 0.0;
                Double kgperiodomaquila = 0.0;

                JasperViewer visor = null;
                JasperPrint jasperPrint = null;
                try {
                    String datos = "REPORTE GENERADO DEL " + fechamediana.format(busca_fechas.getFechaini()) + "  AL  " + fechamediana.format(busca_fechas.getFechafin());
                    Map pars = new HashMap();
                    File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                    pars.put("logoempresa", new FileInputStream(fichero));
                    pars.put("subtitulo", datos);
                    pars.put("fechaini", new java.sql.Timestamp(busca_fechas.getFechaini().getTime()));
                    pars.put("fechafin", new java.sql.Timestamp(busca_fechas.getFechafin().getTime()));
                    pars.put("senSQL", "");
                    pars.put("version", resourceMap.getString("Application.title"));
                    pars.put("kgperiodo", kgperiodo);
                    pars.put("kgperiodomaquila", kgperiodomaquila);
                    JasperReport masterReport = null;
                    try {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/remisiones_mes.jasper"));
                    } catch (JRException e) {
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
                    visor = new JasperViewer(jasperPrint, false);
                    visor.setTitle("REPORTE");
                    visor.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
                }
            }

            busca_fechas = null;
        }
        if (selmenu.equals("Movimientos Caja")) {////***************************opcion de arbol1***********************************
            try {
                caja_chica caja_chica = new caja_chica(conn);
                panelprincipal.add(caja_chica, javax.swing.JLayeredPane.DEFAULT_LAYER);
                caja_chica.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (caja_chica.getWidth() / 2);
                if (cord < 0) {
                    cord = 0;
                    caja_chica.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                caja_chica.setLocation(cord, 0);
                //clientes_radiografia.setMaximum(true);
                caja_chica.toFront();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR CARGANDO EL MARCO !!!!!" + e.getMessage(), "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        if (selmenu.equals("Auxiliar Cliente")) {////***************************opcion de arbol1***********************************
            busca_clientes busca_clientes = new busca_clientes(null, true, conn, "");
            busca_clientes.setLocationRelativeTo(this);
            busca_clientes.setVisible(true);
            String clavecliente = busca_clientes.getText();//obtiene el valor seleccionado
            busca_clientes = null;
            if (!clavecliente.equals("")) {
                busca_fechas busca_fechas = new busca_fechas(null, true);
                busca_fechas.setLocationRelativeTo(this);
                busca_fechas.setVisible(true);
                String estado = busca_fechas.getEstado();
                /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
                if (estado.equals("cancelar")) {
                } else {

                    //obtenemos el saldo antes de la fecha inicial seleccionada
                    Double saldo_inicial = 0.0;
                    rs0 = null;
                    try {
                        String senSQL = "SELECT docxcob.id_clientes,SUM(docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0)) as saldo FROM ((docxcob LEFT JOIN clientes ON docxcob.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT notas_credito_detalle.factura_serie,sum(notas_credito_detalle.importe) as importenotacredito FROM notas_credito_detalle WHERE (notas_credito_detalle.estatus='1' AND notas_credito_detalle.fecha<'" + fechainsertar.format(busca_fechas.getFechaini()) + " 00:00:00') GROUP BY notas_credito_detalle.factura_serie) as notascreditorealizadas ON docxcob.factura_serie=notascreditorealizadas.factura_serie ) LEFT JOIN (SELECT pagos_detalle.factura_serie,sum(pagos_detalle.importe+pagos_detalle.importe_factoraje) as importepago FROM pagos_detalle LEFT JOIN pagos ON pagos_detalle.id_pagos =pagos.id_pagos WHERE (pagos_detalle.estatus<>'Can' AND pagos_detalle.fecha<'" + fechainsertar.format(busca_fechas.getFechaini()) + " 00:00:00') GROUP BY pagos_detalle.factura_serie) as pagosrealizados ON docxcob.factura_serie=pagosrealizados.factura_serie WHERE (docxcob.estatus<>'Can' AND docxcob.id_clientes='" + clavecliente + "' AND docxcob.fechaemision<'" + fechainsertar.format(busca_fechas.getFechaini()) + " 00:00:00' AND (docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0))>0.01) GROUP BY docxcob.id_clientes";
                        rs0 = conexion.consulta(senSQL, conn);
                        while (rs0.next()) {
                            saldo_inicial = rs0.getDouble("saldo");
                        }
                        if (rs0 != null) {
                            rs0.close();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                    }


                    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
                    JasperViewer visor = null;
                    JasperPrint jasperPrint = null;
                    try {
                        String datos = "REPORTE GENERADO DEL " + fechamediana.format(busca_fechas.getFechaini()) + "  AL  " + fechamediana.format(busca_fechas.getFechafin());
                        Map pars = new HashMap();
                        File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                        pars.put("logoempresa", new FileInputStream(fichero));
                        pars.put("subtitulo", datos);
                        pars.put("fechaini", new java.sql.Timestamp(busca_fechas.getFechaini().getTime()));
                        pars.put("fechafin", new java.sql.Timestamp(busca_fechas.getFechafin().getTime()));
                        pars.put("fechahoy", new java.sql.Timestamp(new Date().getTime()));
                        pars.put("total_saldo", 0.0);
                        pars.put("saldo_inicial", saldo_inicial);
                        pars.put("id_cliente_fin", clavecliente);
                        pars.put("senSQL", "");
                        pars.put("version", resourceMap.getString("Application.title"));
                        JasperReport masterReport = null;
                        try {
                            masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/estado_cuenta_cliente_aux.jasper"));
                        } catch (JRException e) {
                            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }

                        jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
                        visor = new JasperViewer(jasperPrint, false);
                        visor.setTitle("REPORTE");
                        visor.setVisible(true);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
                    }
                }
                busca_fechas = null;
            }
        }

        if (selmenu.equals("R M2 Producidos")) {////***************************opcion de arbol1***********************************
            programas_corr_cap_reporte programas_corr_cap_reporte = new programas_corr_cap_reporte(conn);

            panelprincipal.add(programas_corr_cap_reporte, javax.swing.JLayeredPane.DEFAULT_LAYER);
            programas_corr_cap_reporte.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (programas_corr_cap_reporte.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                programas_corr_cap_reporte.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            programas_corr_cap_reporte.setLocation(cord, 0);
            programas_corr_cap_reporte.toFront();
        }
        if (selmenu.equals("R Combinaciones promedio de papel")) {////***************************opcion de arbol1***********************************
            programas_corru_reporte programas_corru_reporte = new programas_corru_reporte(conn);

            panelprincipal.add(programas_corru_reporte, javax.swing.JLayeredPane.DEFAULT_LAYER);
            programas_corru_reporte.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (programas_corru_reporte.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                programas_corru_reporte.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            programas_corru_reporte.setLocation(cord, 0);
            programas_corru_reporte.toFront();
        }

        if (selmenu.equals("R Laminas Fabricadas Corrugadora")) {////***************************opcion de arbol1***********************************
            programas_corru_detalle_reporte programas_corru_detalle_reporte = new programas_corru_detalle_reporte(conn);

            panelprincipal.add(programas_corru_detalle_reporte, javax.swing.JLayeredPane.DEFAULT_LAYER);
            programas_corru_detalle_reporte.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (programas_corru_detalle_reporte.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                programas_corru_detalle_reporte.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            programas_corru_detalle_reporte.setLocation(cord, 0);
            programas_corru_detalle_reporte.toFront();
        }

        /*Modulos importacion de SAE*/
        if (selmenu.equals("Importar Remisiones Facturadas")) {////***************************opcion de arbol1***********************************
                sae_imp_remisiones_facturadas sae_imp_remisiones_facturadas = new sae_imp_remisiones_facturadas(conn);

                panelprincipal.add(sae_imp_remisiones_facturadas, javax.swing.JLayeredPane.DEFAULT_LAYER);
                sae_imp_remisiones_facturadas.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (sae_imp_remisiones_facturadas.getWidth() / 2);
                if (cord < 0) {
                        cord = 0;
                        sae_imp_remisiones_facturadas.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                sae_imp_remisiones_facturadas.setLocation(cord, 0);
                sae_imp_remisiones_facturadas.toFront();
        }

        if (selmenu.equals("Importar Materia Prima")) {////***************************opcion de arbol1***********************************
                sae_imp_materia_prima sae_imp_materia_prima = new sae_imp_materia_prima(conn);

                panelprincipal.add(sae_imp_materia_prima, javax.swing.JLayeredPane.DEFAULT_LAYER);
                sae_imp_materia_prima.setVisible(true);
                //ubica la ventana en el centro del panel
                int cord = (panelprincipal.getWidth() / 2) - (sae_imp_materia_prima.getWidth() / 2);
                if (cord < 0) {
                        cord = 0;
                        sae_imp_materia_prima.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
                }
                sae_imp_materia_prima.setLocation(cord, 0);
                sae_imp_materia_prima.toFront();
        }




























        if (selmenu.equals("Pruebas")) {////***************************opcion de arbol1***********************************
            APruebas = new APruebas(this, true, conn);
            APruebas.setLocationRelativeTo(this);
            APruebas.setVisible(true);
            APruebas = null;
        }
        if (selmenu.equals("Pruebas Corr")) {////***************************opcion de arbol1***********************************
            APruebas_combina_corr APruebas_combina_corr = new APruebas_combina_corr(conn);
            panelprincipal.add(APruebas_combina_corr, javax.swing.JLayeredPane.DEFAULT_LAYER);
            APruebas_combina_corr.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (APruebas_combina_corr.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                APruebas_combina_corr.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            APruebas_combina_corr.setLocation(cord, 0);
            APruebas_combina_corr.toFront();
        }
        if (selmenu.equals("Planeacion CM")) {////***************************opcion de arbol1***********************************
            APruebasa_diografia_planeacion APruebasa_diografia_planeacion = new APruebasa_diografia_planeacion(conn);
            panelprincipal.add(APruebasa_diografia_planeacion, javax.swing.JLayeredPane.DEFAULT_LAYER);
            APruebasa_diografia_planeacion.setVisible(true);
            //ubica la ventana en el centro del panel
            int cord = (panelprincipal.getWidth() / 2) - (APruebasa_diografia_planeacion.getWidth() / 2);
            if (cord < 0) {
                cord = 0;
                APruebasa_diografia_planeacion.setSize(panelprincipal.getWidth(), panelprincipal.getHeight());
            }
            APruebasa_diografia_planeacion.setLocation(cord, 0);
            APruebasa_diografia_planeacion.toFront();
        }
        if (selmenu.equals("CM_Herr. Programacion Corrugadora")) {////***************************opcion de arbol1***********************************
            ACorrugadora_herramienta_combina = new ACorrugadora_herramienta_combina(this, true, conn, "", "");
            ACorrugadora_herramienta_combina.setLocationRelativeTo(this);
            ACorrugadora_herramienta_combina.setVisible(true);
            ACorrugadora_herramienta_combina = null;
        }/*
        if(selmenu.equals("Descuentos Factoraje")){////***************************opcion de arbol1***********************************
        descuento_factoraje descuento_factoraje=new descuento_factoraje(conn);
        panelprincipal.add(descuento_factoraje, javax.swing.JLayeredPane.DEFAULT_LAYER);
        descuento_factoraje.setVisible(true);
        //ubica la ventana en el centro del panel
        int cord=(panelprincipal.getWidth()/2)-(descuento_factoraje.getWidth()/2);
        if(cord<0){
        cord=0;
        descuento_factoraje.setSize(panelprincipal.getWidth(),panelprincipal.getHeight());
        }
        descuento_factoraje.setLocation(cord,0);
        descuento_factoraje.toFront();
        }*/

    }//GEN-LAST:event_arbol1MouseClicked

    private void btnbuscararbolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscararbolActionPerformed
        // TODO add your handling code here:
        buscaNode();
        //revisar datos
        /*String us=JOptionPane.showInputDialog(this,"USUARIO");
        DefaultMutableTreeNode node = null;
        Enumeration enume = r.breadthFirstEnumeration();
        while(enume.hasMoreElements())
        {
        node = (DefaultMutableTreeNode)enume.nextElement();
        String cadenanode=node.getUserObject().toString();
        //guarda los nodos no
        if(!cadenanode.contains("<html>") && !cadenanode.equals("*")){
        System.err.println(""+cadenanode);
        rs0=null;
        try{
        String senSQL="SELECT * FROM privilegios WHERE (privilegio='"+us+"' AND menu='"+cadenanode+"');";
        rs0=conexion.consulta(senSQL,conn);
        if(rs0.next()){

        }else{
        senSQL="INSERT INTO privilegios2 (privilegio,menu,numero_sql) VALUES('"+us+"','"+cadenanode+"','2');";
        conexion.modificamov(senSQL,conn);
        }
        if(rs0!=null) {  rs0.close();   }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
        }*/


}//GEN-LAST:event_btnbuscararbolActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (ver == 1) {
            jXCollapsiblePane1.setCollapsed(true);
            ver = 0;
            jButton1.setText("Menu >>");
        } else {
            jXCollapsiblePane1.setCollapsed(false);
            ver = 1;
            jButton1.setText("<< Menu");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void arbol1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_arbol1ValueChanged
        // TODO add your handling code here:
/*        String selmenu=""+arbol1.getLastSelectedPathComponent();

        //if(selmenu.equals("[*, APLICACION, Usuarios]")){////***************************opcion de arbol1***********************************
        if(selmenu.equals("Usuarios")){////***************************opcion de arbol1***********************************
        usuarios usuarios=new usuarios(conn);

        panelprincipal.add(usuarios, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usuarios.setVisible(true);
        //ubica la ventana en el centro del panel
        int cord=(panelprincipal.getWidth()/2)-(usuarios.getWidth()/2);
        if(cord<0){
        cord=0;
        usuarios.setSize(panelprincipal.getWidth(),panelprincipal.getHeight());
        }
        usuarios.setLocation(cord,0);
        usuarios.toFront();
        }

         */
    }//GEN-LAST:event_arbol1ValueChanged

    private void perioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_perioMouseClicked
        // TODO add your handling code here:
        busca_periodo = new busca_periodo(this, true, "");
        busca_periodo.setLocationRelativeTo(this);
        busca_periodo.setVisible(true);
        busca_periodo = null;
        perio.setText(conf.getProperty("Periodo"));

}//GEN-LAST:event_perioMouseClicked

    private void txtbuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarKeyReleased
        // TODO add your handling code here:
        if (txtbuscar.getText().equals("")) {
            buscanodoint = 0;
        }
    }//GEN-LAST:event_txtbuscarKeyReleased

    private void arbol1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arbol1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_arbol1MousePressed

    private void txtbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbuscarActionPerformed
        // TODO add your handling code here:
        buscaNode();
    }//GEN-LAST:event_txtbuscarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new menu_principal().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar BarraBD;
    private javax.swing.JToolBar BarraFecha;
    private javax.swing.JToolBar BarraPeriodo;
    private javax.swing.JToolBar BarraUsuario;
    private org.jdesktop.swingx.JXTree arbol1;
    private javax.swing.JButton btnbuscararbol;
    private javax.swing.JLabel fecha;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu20;
    private javax.swing.JMenu jMenu21;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private org.jdesktop.swingx.JXCollapsiblePane jXCollapsiblePane1;
    private javax.swing.JLabel lbbd;
    private javax.swing.JDesktopPane panelprincipal;
    private javax.swing.JLabel perio;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JLabel usuario;
    // End of variables declaration//GEN-END:variables
    private JDialog APruebas;
    private JDialog backup;
    private JDialog restaurar;
    private JDialog temas;
    private JDialog empresa;
    private JDialog logoempresa;
    private JDialog rfcempresa;
    private JDialog busca_periodo;
    private JDialog datos_ops;
    private JDialog datos_mullen_ect;
    private JDialog datos_papeleta;
    private JDialog datos_papeleta_pt;
    private JDialog datos_codigo_barras;
    private JDialog datos_cfd_certificado;
    private JDialog datos_cfd_llave;
    private JDialog datos_rutas;
    private JDialog datos_parametros;
    private JDialog corrugadora_herramienta_combina;
    private JDialog ACorrugadora_herramienta_combina;
}
