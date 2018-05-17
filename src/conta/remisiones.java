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

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import jxl.*;
import jxl.write.*;
import java.awt.Desktop;

/**
 *
 * @author IVONNE
 */
public class remisiones extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot1=null;
    ListSelectionModel modeloseleccion;
    ResultSet rs0=null,rs1=null,rs2=null;
    private Properties conf;
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Calendar calendar10diasantes = new GregorianCalendar();
    String usuariorem="";
    String valor_privilegio="1";

    /** Creates new form usuarios */
    public remisiones(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        usuariorem=conf.getProperty("UserID");
        conn=connt;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modeloseleccion = Tabladatos.getSelectionModel();
        ajusteTabladatos();
        datos_privilegios();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datos();
        //fecha de fin de semana
        calendar10diasantes.setTime(new Date());
        calendar10diasantes.add(Calendar.DATE, -15);

        modeloseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    double sumacantidad=0,sumakg=0.0;// cambio 2017 (sumakg=0.0) a numero Float para reconocer decimales
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabladatos.getValueAt(i,6)!=null && !Tabladatos.getValueAt(i,6).equals("")){ //suma cantidades
                                    sumacantidad+=Integer.parseInt(""+Tabladatos.getValueAt(i,6));
                            }
                            if(Tabladatos.getValueAt(i,7)!=null && !Tabladatos.getValueAt(i,7).equals("")){ //suma los kilogramos
                                    sumakg+=Double.parseDouble(""+Tabladatos.getValueAt(i,7)); // cambio 2017 a numero Float para reconocer decimales
                            }
                        }
                    }
                    noregistros.setText("Cantidad: "+estandarentero.format(sumacantidad)+"        KG: "+estandarentero.format(sumakg));
                }
            }
        });

    }

    public void salir(){
        if(conn!=null){
            conn=null;
        }
        dispose();
        this.setVisible(false);
    }
    public void datos_privilegios(){
        valor_privilegio=conexion.obtener_privilegios(conn,"Remisiones");
    }
    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(200);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(250);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(180);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(80);

    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos(){
        rs0=null;
        try{
            String senSQL="SELECT remisiones.remision, remisiones.fechahora, remisiones_detalle.op, remisiones_detalle.clavearticulo, remisiones_detalle.cantidadpzas, ( remisiones_detalle.cantidadpzas * articulos.kg ) AS cantidadkgpzas, ( remisiones_detalle.cantidadpzas * articulos.m2 ) AS cantidadm2pzas, articulos.articulo, articulos.kg, articulos.m2, clientes.nombre, transportistas.nombre AS tnombre, transportistas.tipounidad, transportistas.placas, tablaops.ordencompra AS OC FROM (( remisiones LEFT JOIN transportistas ON remisiones.clave_transportista = transportistas.clave_transportista ) INNER JOIN remisiones_detalle ON remisiones.remision = remisiones_detalle.remision ) LEFT JOIN ( articulos LEFT JOIN clientes ON articulos.id_clientes = clientes.id_clientes ) ON remisiones_detalle.clavearticulo = articulos.clavearticulo LEFT JOIN ( SELECT ops.id_op, ops.ordencompra FROM ops GROUP BY ops.id_op, ops.ordencompra ) AS tablaops ON remisiones.id_op = tablaops.id_op WHERE ( remisiones.estatus = 'Act' AND ( remisiones.fechahora >= '"+conf.getProperty("FechaIni")+" 00:00:00' AND remisiones.fechahora <= '"+conf.getProperty("FechaFin")+" 23:59:59' )) ORDER BY clientes.nombre, remisiones.fechahora;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Object datos[]={rs0.getString("remision"),rs0.getDate("fechahora"),rs0.getString("op"),rs0.getString("nombre"),rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getInt("cantidadpzas"),rs0.getDouble("cantidadkgpzas"),rs0.getString("tnombre"),rs0.getString("tipounidad"),rs0.getString("placas"), rs0.getString("OC")};
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

                int remision_kilos=0;
                //revisa si la remisiones por kilos o no
                rs1=null;
                try{
                    String senSQL="SELECT remisiones.* FROM remisiones WHERE (remisiones.estatus<>'Can' AND remisiones.remision='"+clavemodifica+"');";
                    rs1=conexion.consulta(senSQL,conn);
                    if(rs1.next()){
                        remision_kilos=rs1.getInt("kgrealbascula");
                    }
                    if(rs1!=null) {  rs1.close();      }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(remision_kilos>0){
                    datos_remisiones_kilogramos = new datos_remisiones_kilogramos(null,true,conn,clavemodifica,valor_privilegio);
                    datos_remisiones_kilogramos.setLocationRelativeTo(this);
                    datos_remisiones_kilogramos.setVisible(true);
                    limpiatabla();
                    datos_remisiones_kilogramos=null;
                    datos();

                }else{
                    datos_remisiones = new datos_remisiones(null,true,conn,clavemodifica,valor_privilegio);
                    datos_remisiones.setLocationRelativeTo(this);
                    datos_remisiones.setVisible(true);
                    limpiatabla();
                    datos_remisiones=null;
                    datos();
                }
            }
        }
    }
    public void actualizaOP(String opestatus){
        ////////////////actualiza la orden de produccion
        rs2=null;
        try{
            String senSQL="SELECT ops.cantidad,(ops.cantidad-COALESCE(remisionado.cantidadremision,0)) as cantidadpendiente,COALESCE(remisionado.cantidadremision,0) as cantidadremisionado,remisionado.fecharemisionado FROM ops LEFT JOIN (SELECT remisiones_detalle.op,sum(remisiones_detalle.cantidadpzas) as cantidadremision,max(remisiones.fechahora) as fecharemisionado FROM remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision WHERE (remisiones_detalle.estatus='Act') GROUP BY remisiones_detalle.op) as remisionado ON ops.op=remisionado.op WHERE (ops.estatus<>'Can' and ops.op='"+opestatus+"');";
            rs2=conexion.consulta(senSQL,conn);
            if(rs2.next()){
                int cantped=rs2.getInt("cantidad");
                int cantremisionado=rs2.getInt("cantidadremisionado");
                if(cantremisionado>=cantped){
                    String senSQLmov = "UPDATE ops SET estatus='Ter' WHERE op='" + opestatus + "';";
                    conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
                }else{
                    String senSQLmov = "UPDATE ops SET estatus='Act' WHERE op='" + opestatus + "';";
                    conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
                }
            }else{
                String senSQLmov = "UPDATE ops SET estatus='Act' WHERE op='" + opestatus + "';";
                conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
            }
            if(rs2!=null) {
                rs2.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menupop = new javax.swing.JPopupMenu();
        menueliminar = new javax.swing.JMenuItem();
        menureimp = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();

        menupop.setMinimumSize(new java.awt.Dimension(105, 28));
        menupop.setName("menupop"); // NOI18N
        menupop.setPreferredSize(new java.awt.Dimension(130, 64));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(remisiones.class);
        menueliminar.setIcon(resourceMap.getIcon("menueliminar.icon")); // NOI18N
        menueliminar.setText(resourceMap.getString("menueliminar.text")); // NOI18N
        menueliminar.setName("menueliminar"); // NOI18N
        menueliminar.setPreferredSize(new java.awt.Dimension(130, 28));
        menueliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menueliminarActionPerformed(evt);
            }
        });
        menupop.add(menueliminar);

        menureimp.setIcon(resourceMap.getIcon("menureimp.icon")); // NOI18N
        menureimp.setText(resourceMap.getString("menureimp.text")); // NOI18N
        menureimp.setName("menureimp"); // NOI18N
        menureimp.setPreferredSize(new java.awt.Dimension(130, 28));
        menureimp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menureimpActionPerformed(evt);
            }
        });
        menupop.add(menureimp);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setFrameIcon(resourceMap.getIcon("Form.frameIcon")); // NOI18N
        setName("Form"); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Remision", "Fecha", "OP", "Cliente", "Clave Articulo", "Articulo", "Cantidad", "KG", "Conductor", "Tipo Unidad", "Placas", "OC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.util.Date.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
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

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setToolTipText(resourceMap.getString("jButton1.toolTipText")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton1.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton1.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setToolTipText(resourceMap.getString("jButton4.toolTipText")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton4.setMaximumSize(new java.awt.Dimension(80, 48));
        jButton4.setMinimumSize(new java.awt.Dimension(80, 48));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(80, 48));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar1.add(jSeparator5);

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setToolTipText(resourceMap.getString("jButton2.toolTipText")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton2.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton2.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setToolTipText(resourceMap.getString("jButton5.toolTipText")); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton5.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton5.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton5.setName("jButton5"); // NOI18N
        jButton5.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jSeparator6.setName("jSeparator6"); // NOI18N
        jToolBar1.add(jSeparator6);

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setToolTipText(resourceMap.getString("jButton3.toolTipText")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton3.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton3.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(556, 48));

        buscar.setText(resourceMap.getString("buscar.text")); // NOI18N
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

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(167, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jToolBar1.add(jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N

        noregistros.setFont(resourceMap.getFont("noregistros.font")); // NOI18N
        noregistros.setForeground(resourceMap.getColor("noregistros.foreground")); // NOI18N
        noregistros.setText(resourceMap.getString("noregistros.text")); // NOI18N
        noregistros.setName("noregistros"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noregistros)
                .addContainerGap(822, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(noregistros)
                .addContainerGap(2, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formWindowClosing

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

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() ==2){
            modificar();
        }
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tabladatos.rowAtPoint(p);
            // variable for the beginning and end selects only that one row.
            Tabladatos.changeSelection(rowNumber, 0, false, false);
            menupop.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        datos_remisiones = new datos_remisiones(null,true,conn,"",valor_privilegio);
        datos_remisiones.setLocationRelativeTo(this);
        datos_remisiones.setVisible(true);
        limpiatabla();
        datos_remisiones=null;
        datos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        busca_fechas busca_fechas = new busca_fechas(null,true);
        busca_fechas.setLocationRelativeTo(this);
        busca_fechas.setVisible(true);
        String estado=busca_fechas.getEstado();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if(estado.equals("cancelar")){

        }else{

            JasperViewer visor=null;
            JasperPrint jasperPrint =null;
            try{
                String datos="REPORTE GENERADO DEL "+fechamediana.format(busca_fechas.getFechaini())+"  AL  "+fechamediana.format(busca_fechas.getFechafin());
                Map pars = new HashMap();
                File fichero = new File(conexion.Directorio()+"/logoempresa.png");
                pars.put("logoempresa",new FileInputStream(fichero));
                pars.put("subtitulo", datos);
                pars.put("fechaini", new Timestamp(busca_fechas.getFechaini().getTime()));
                pars.put("fechafin", new Timestamp(busca_fechas.getFechafin().getTime()));
                pars.put("senSQL", "");
                pars.put("version", resourceMap.getString("Application.title"));
                JasperReport masterReport = null;
                try{
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/remisiones_dias.jasper"));
                 }
                 catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

                jasperPrint = JasperFillManager.fillReport(masterReport,pars,conn);
                visor = new JasperViewer(jasperPrint,false);
                visor.setTitle("REPORTE");
                visor.setVisible(true);
            }
            catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }
        }

        busca_fechas=null;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
    }//GEN-LAST:event_buscarFocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        busca_fechas busca_fechas = new busca_fechas(null, true);
        busca_fechas.setLocationRelativeTo(this);
        busca_fechas.setVisible(true);
        String estado = busca_fechas.getEstado();
        /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if (estado.equals("cancelar")) {
        } else {
            limpiatabla();
            rs0=null;
            try{
                String senSQL="SELECT remisiones.remision, remisiones.fechahora, remisiones_detalle.op, remisiones_detalle.clavearticulo, remisiones_detalle.cantidadpzas, ( remisiones_detalle.cantidadpzas * articulos.kg ) AS cantidadkgpzas, ( remisiones_detalle.cantidadpzas * articulos.m2 ) AS cantidadm2pzas, articulos.articulo, articulos.kg, articulos.m2, clientes.nombre, transportistas.nombre AS tnombre, transportistas.tipounidad, transportistas.placas, tablaops.ordencompra AS OC FROM (( remisiones LEFT JOIN transportistas ON remisiones.clave_transportista = transportistas.clave_transportista ) INNER JOIN remisiones_detalle ON remisiones.remision = remisiones_detalle.remision ) LEFT JOIN ( articulos LEFT JOIN clientes ON articulos.id_clientes = clientes.id_clientes ) ON remisiones_detalle.clavearticulo = articulos.clavearticulo LEFT JOIN ( SELECT ops.id_op, ops.ordencompra FROM ops GROUP BY ops.id_op, ops.ordencompra ) AS tablaops ON remisiones.id_op = tablaops.id_op WHERE ( remisiones.estatus = 'Act' AND ( remisiones.fechahora >= '"+fechainsertar.format(busca_fechas.getFechaini())+" 00:00:00' AND remisiones.fechahora <= '"+fechainsertar.format(busca_fechas.getFechafin())+" 23:59:59' )) ORDER BY clientes.nombre, remisiones.fechahora;";
                rs0=conexion.consulta(senSQL,conn);
                while(rs0.next()){
                    Object datos[]={rs0.getString("remision"),rs0.getDate("fechahora"),rs0.getString("op"),rs0.getString("nombre"),rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getInt("cantidadpzas"),rs0.getDouble("cantidadkgpzas"),rs0.getString("tnombre"),rs0.getString("tipounidad"),rs0.getString("placas"), rs0.getString("OC")};
                    modelot1.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
        busca_fechas = null;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void menueliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menueliminarActionPerformed
        // TODO add your handling code here:
        int filano = Tabladatos.getSelectedRow();
        if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA CANCELAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int errorremision=3;
            rs1=null;
            try{
                String senSQL="SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='"+Tabladatos.getValueAt(filano, 0)+"');";
                rs1=conexion.consulta(senSQL,conn);
                if(rs1.next()){
                    errorremision=0;
                    Double cantidadfacturada=rs1.getDouble("facturacantidad");
                    if(cantidadfacturada>0.0)
                        errorremision=1;

                    Date fecharemi = rs1.getDate("fechahora");
                    Calendar calendarremi = new GregorianCalendar();
                    calendarremi.setTime(fecharemi);
                    if (calendarremi.before(calendar10diasantes)) {
                        errorremision=2;
                    }
                }
                if(rs1!=null) {
                    rs1.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            if(errorremision==1 ||errorremision==2){
                String err="ERROR GENERAL";
                if(errorremision==1)
                        err="LA REMISION YA FUE FACTURADA NO SE PUEDE CANCELAR";
                if(errorremision==2)
                    err="LA REMISION TIENE UNA ANTIGUEDAD DE MAS DE 15 DIAS NO SE PUEDE CANCELAR";

                JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                    String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE CANCELACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
                    if(!motivoelimina.equals("") && !motivoelimina.equals("null")){
                                    String senSQLmov = "UPDATE remisiones_detalle SET estatus='Can' WHERE remision='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
                                    conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
                                    rs1=null;
                                    try{
                                        String senSQL="SELECT remisiones_detalle.op,remisiones_detalle.clavearticulo,remisiones_detalle.cantidadpzas,remisiones_detalle.pzaspaquete,remisiones.fechareal,remisiones.facturado FROM remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision WHERE (remisiones.estatus='Act' AND remisiones_detalle.remision='"+Tabladatos.getValueAt(filano, 0)+"');";
                                        rs1=conexion.consulta(senSQL,conn);
                                        while(rs1.next()){
                                                    String opf=rs1.getString("op");
                                                    String clavearticulof=rs1.getString("clavearticulo");
                                                    int cantidadf=rs1.getInt("cantidadpzas");
                                                    int pzasp=rs1.getInt("pzaspaquete");
                                                    Date fechaf=rs1.getDate("fechareal");
                                                    //ingresa los datos al almacen de embarques
                                                    rs0=null;
                                                    try{
                                                        senSQL="SELECT * FROM almacen_pt WHERE (op='"+opf+"' AND clavearticulo='"+clavearticulof+"')";
                                                        rs0=conexion.consulta(senSQL,conn);
                                                        if(rs0.next()){
                                                            int nuevacantidad=rs0.getInt("cantidadpzas")+cantidadf;
                                                            senSQL = "UPDATE almacen_pt SET cantidadpzas='"+nuevacantidad+"' WHERE (op='"+opf+"' AND clavearticulo='"+clavearticulof+"');";
                                                            conexion.modificamov_p(senSQL, conn,valor_privilegio);
                                                        }else{
                                                            senSQL="INSERT INTO almacen_pt(fecha, op, clavearticulo, cantidadpzas, pzaspaquete, almacen)VALUES ('"+fechainsertar.format(fechaf)+"', '"+opf+"', '"+clavearticulof+"', '"+cantidadf+"', '"+pzasp+"', '1');";
                                                            conexion.modificamov_p(senSQL,conn,valor_privilegio);
                                                        }
                                                        if(rs0!=null) {             rs0.close();  }
                                                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                                                    //actualiza la op
                                                    actualizaOP(opf);
                                        }
                                        if(rs1!=null) {        rs1.close();          }
                                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                                    //cancela la remision y el detalle
                                    senSQLmov = "UPDATE remisiones SET estatus='Can' WHERE remision='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
                                    conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
                                    //cancela la salida
                                    senSQLmov = "UPDATE salidas_almacen_pt SET estatus='Can' WHERE remision='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
                                    conexion.modificamov_p(senSQLmov, conn,valor_privilegio);

                                    //registra la cancelacion
                                    senSQLmov = "INSERT INTO remisiones_cancela(remision, fecha_cancela, motivo, usuario) VALUES ('" + (String) Tabladatos.getValueAt(filano, 0) + "', '"+fechainsertarhora.format(new Date())+"', '"+motivoelimina+"', '"+usuariorem+"');";
                                    conexion.modificaupdate_p(senSQLmov, conn,valor_privilegio);
                    }
                }

                //elimina los ceros del inventario
                String senSQL="DELETE FROM almacen_pt WHERE (cantidadpzas<1) ";
                conexion.modificamov_p(senSQL, conn,valor_privilegio);

                limpiatabla();
                datos();
        }
    }//GEN-LAST:event_menueliminarActionPerformed

    private void menureimpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menureimpActionPerformed
        // TODO add your handling code here:
        int filano = Tabladatos.getSelectedRow();
        String rem=""+Tabladatos.getValueAt(filano, 0);
        int remision_kilos=0;
        //revisa si la remisiones por kilos o no
        rs1=null;
        try{
            String senSQL="SELECT remisiones.* FROM remisiones WHERE (remisiones.estatus<>'Can' AND remisiones.remision='"+rem+"');";
            rs1=conexion.consulta(senSQL,conn);
            if(rs1.next()){
                remision_kilos=rs1.getInt("kgrealbascula");
            }
            if(rs1!=null) {  rs1.close();      }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        JasperPrint jasperPrint = null;
        try {
            //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio() + "/logoempresa.png");
            pars.put("folio", rem);
            pars.put("logoempresa", new FileInputStream(fichero));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("senSQL", "");//SQL dinamico
            pars.put("version", resourceMap.getString("Application.title"));
            JasperReport masterReport = null;
            try {
                if(remision_kilos>0){
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_remision_kg.jasper"));
                }else{
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_remision.jasper"));
                }
            } catch (JRException e) {
                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
            JasperPrintManager.printReport( jasperPrint, true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
        }
    }//GEN-LAST:event_menureimpActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        datos_remisiones_kilogramos = new datos_remisiones_kilogramos(null,true,conn,"",valor_privilegio);
        datos_remisiones_kilogramos.setLocationRelativeTo(this);
        datos_remisiones_kilogramos.setVisible(true);
        limpiatabla();
        datos_remisiones_kilogramos=null;
        datos();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/remisiones.xls");
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

            WritableCellFormat arial10fdetalle = new WritableCellFormat(arial9);

            int filainicial = 5;

            //titulo del documento
            sheet.addImage(new jxl.write.WritableImage(0, 0, 3, 4, new java.io.File(conexion.Directorio() + "/logoempresa.png")));
            sheet.addCell(new jxl.write.Label(0, filainicial, "" + this.getTitle(), arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabladatos.getColumnCount()); j = j + 1) {
                String titu = "" + Tabladatos.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabladatos.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabladatos.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabladatos.getColumnCount()); j = j + 1) {
                    if (Tabladatos.getValueAt(i, j) != null) {
                        if (Tabladatos.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabladatos.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabladatos.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabladatos.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabladatos.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabladatos.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabladatos.getValueAt(i, j), arial10fdetalle));
                        }
                    }
                }
            }
            /**fin de revisar los campos vacios*/
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
}//GEN-LAST:event_jButton5ActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField buscar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem menueliminar;
    private javax.swing.JPopupMenu menupop;
    private javax.swing.JMenuItem menureimp;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_remisiones;
    private JDialog datos_remisiones_kilogramos;
}