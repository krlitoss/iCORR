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

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import jxl.*;
import jxl.write.*;

/**
 *
 * @author IVONNE
 */
public final class saldos_pagos extends javax.swing.JInternalFrame {

    Connection conn = null;
    DefaultTableModel modelot1 = null;
    ListSelectionModel modeloseleccion;
    ResultSet rs0;
    private Properties conf;
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat estandar4decimales = new DecimalFormat("#,###,##0.0000");
    DecimalFormat fijo0decimales = new DecimalFormat("######0");
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    DecimalFormat moneda0decimales = new DecimalFormat("$ #,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Calendar calendarhoy = new GregorianCalendar();
    Calendar calendariniciosem = new GregorianCalendar();
    Calendar calendar1 = new GregorianCalendar();
    Calendar calendar2 = new GregorianCalendar();
    Calendar calendar3 = new GregorianCalendar();
    Calendar calendar4 = new GregorianCalendar();
    Calendar calendar5 = new GregorianCalendar();
    String mifechacorte ="";

    /** Creates new form usuarios */
    public saldos_pagos(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        conn = connt;
        modelot1 = (DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modeloseleccion = Tabladatos.getSelectionModel();
        ajusteTabladatos();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datos();

        modeloseleccion.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumavencido = 0, suma1 = 0, suma2 = 0, suma3 = 0, suma4 = 0, suma5 = 0, sumadespues = 0;
                    Double sumacosto = 0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if (Tabladatos.getValueAt(i, 5) != null && !Tabladatos.getValueAt(i, 5).equals("")) { //suma los metros lineales
                                sumavencido += Double.parseDouble("" + Tabladatos.getValueAt(i,5));
                            }
                            if (Tabladatos.getValueAt(i, 6) != null && !Tabladatos.getValueAt(i, 6).equals("")) { //suma los metros lineales
                                suma1 += Double.parseDouble("" + Tabladatos.getValueAt(i, 6));
                            }
                            if (Tabladatos.getValueAt(i, 7) != null && !Tabladatos.getValueAt(i, 7).equals("")) { //suma los metros lineales
                                suma2 += Double.parseDouble("" + Tabladatos.getValueAt(i, 7));
                            }
                            if (Tabladatos.getValueAt(i, 8) != null && !Tabladatos.getValueAt(i, 8).equals("")) { //suma los metros lineales
                                suma3 += Double.parseDouble("" + Tabladatos.getValueAt(i, 8));
                            }
                            if (Tabladatos.getValueAt(i, 9) != null && !Tabladatos.getValueAt(i, 9).equals("")) { //suma los metros lineales
                                suma4 += Double.parseDouble("" + Tabladatos.getValueAt(i, 9));
                            }
                            if (Tabladatos.getValueAt(i, 10) != null && !Tabladatos.getValueAt(i, 10).equals("")) { //suma los metros lineales
                                suma5 += Double.parseDouble("" + Tabladatos.getValueAt(i, 10));
                            }
                            if (Tabladatos.getValueAt(i, 11) != null && !Tabladatos.getValueAt(i, 11).equals("")) { //suma los metros lineales
                                sumadespues += Double.parseDouble("" + Tabladatos.getValueAt(i, 11));
                            }
                        }
                    }
                    noregistros.setText("Vencido: " + moneda0decimales.format(sumavencido) + "     Esta Semana: " + moneda0decimales.format(suma1) + "     Prox. Semana: " + moneda0decimales.format(suma2) + "     3ra Semana: " + moneda0decimales.format(suma3) + "     4ta Semana: " + moneda0decimales.format(suma4) + "     5ta Semana: " + moneda0decimales.format(suma5) + "     En adelante: " + moneda0decimales.format(sumadespues));
                }
            }
        });

    }

    public void salir() {
        if (conn != null) {
            conn = null;
        }
        dispose();
        this.setVisible(false);
    }

    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(110);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(210);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(90);

    }

    public void limpiatabla() {
        modelot1.setNumRows(0);
    }

    public void datos() {
        busca_fecha_corte busca_fecha_corte = new busca_fecha_corte(null, true);

        busca_fecha_corte.setLocationRelativeTo(this);
        busca_fecha_corte.setVisible(true);
        String estado = busca_fecha_corte.getEstado();
        /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if (estado.equals("cancelar")) {
            mifechacorte="";

        } else {
            mifechacorte="Fecha Corte al: "+fechamediana.format(busca_fecha_corte.getFechaini());
            this.setTitle("::: "+"SALDOS Y PROGRAMA DE COBROS AL "+fechamediana.format(busca_fecha_corte.getFechaini())+" :::");
            //actualiza los datos de los saldos
            conexion.actualizaSaldos(conn,busca_fecha_corte.getFechaini());
            rs0 = null;
            try {
                //String senSQL = "SELECT saldos_pagos.*,clientes.nombre,contrarecibos_g.contrarecibo FROM (saldos_pagos left join (select contrarecibos_detalle.factura_serie,max (contrarecibos.contrarecibo) as contrarecibo from contrarecibos_detalle left join contrarecibos on contrarecibos_detalle.id_contrarecibo = contrarecibos.id_contrarecibo where contrarecibos.estatus = 'Act' group by contrarecibos_detalle.factura_serie) as contrarecibos_g on saldos_pagos.factura_serie = contrarecibos_g.factura_serie) LEFT JOIN clientes ON saldos_pagos.id_clientes=clientes.id_clientes ORDER BY fechaemision;";
                String senSQL = "SELECT saldos_pagos.*,clientes.nombre,contrarecibos_g.contrarecibo,xmlfinal.uuid, facturas.ordencompra FROM ( (saldos_pagos LEFT JOIN xmlfinal ON saldos_pagos.factura_serie=xmlfinal.factura_serie ) left join (select contrarecibos_detalle.factura_serie,max (contrarecibos.contrarecibo) as contrarecibo from contrarecibos_detalle left join contrarecibos on contrarecibos_detalle.id_contrarecibo = contrarecibos.id_contrarecibo where contrarecibos.estatus = 'Act' group by contrarecibos_detalle.factura_serie) as contrarecibos_g on saldos_pagos.factura_serie = contrarecibos_g.factura_serie) LEFT JOIN clientes ON saldos_pagos.id_clientes=clientes.id_clientes LEFT JOIN facturas ON saldos_pagos.factura_serie = facturas.factura_serie ORDER BY fechaemision;";
                System.err.println(senSQL);
                rs0 = conexion.consulta(senSQL, conn);
                while (rs0.next()) {
                    Object datos[] = {rs0.getString("factura_serie"), rs0.getString("contrarecibo"), rs0.getDate("fechaemision"), rs0.getDate("fechavencimiento"), rs0.getString("nombre"), rs0.getDouble("vencido"), rs0.getDouble("semana1"), rs0.getDouble("semana2"), rs0.getDouble("semana3"), rs0.getDouble("semana4"), rs0.getDouble("semana5"), rs0.getDouble("adelante"), rs0.getString("uuid"), rs0.getString("ordencompra")};
                    modelot1.addRow(datos);
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
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
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(saldos_pagos.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setFrameIcon(resourceMap.getIcon("Form.frameIcon")); // NOI18N
        setName("Form"); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Factura", "Contrarecibo", "Fecha Emision", "Fecha Vencim.", "Cliente", "Vencido", "Esta Semana", "Prox. Semana", "3ra Semana", "4ta Semana", "5ta Semana", "En adelante", "UUID", "OC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
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

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setToolTipText(resourceMap.getString("jButton3.toolTipText")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton3.setMaximumSize(new java.awt.Dimension(95, 48));
        jButton3.setMinimumSize(new java.awt.Dimension(95, 48));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(95, 48));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton5.setMaximumSize(new java.awt.Dimension(95, 48));
        jButton5.setMinimumSize(new java.awt.Dimension(95, 48));
        jButton5.setName("jButton5"); // NOI18N
        jButton5.setPreferredSize(new java.awt.Dimension(95, 48));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jSeparator6.setName("jSeparator6"); // NOI18N
        jToolBar1.add(jSeparator6);

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setToolTipText(resourceMap.getString("jButton2.toolTipText")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton2.setMaximumSize(new java.awt.Dimension(90, 48));
        jButton2.setMinimumSize(new java.awt.Dimension(90, 48));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(90, 48));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar1.add(jSeparator5);

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

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setToolTipText(resourceMap.getString("jButton4.toolTipText")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton4.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton4.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

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
                .addContainerGap(110, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
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
        if (buscar.getText().equals("")) {
            Tabladatos.setRowSorter(null);
            buscar.setText("");
            Tabladatos.setAutoCreateRowSorter(true);
        } else {
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)" + buscar.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladatos.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscarKeyReleased

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        JasperViewer visor = null;
        JasperPrint jasperPrint = null;
        try {
            //String datos="REPORTE GENERADO DEL "+fechamediana.format(fechainsertarhora.parse(fi+" 00:00:00"))+"  AL  "+fechamediana.format(fechainsertarhora.parse(ft+" 23:59:59"));
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio() + "/logoempresa.png");
            pars.put("logoempresa", new FileInputStream(fichero));
            pars.put("subtitulo",mifechacorte);
            pars.put("fechaini", null);
            pars.put("fechafin", null);
            pars.put("senSQL", "");
            pars.put("version", resourceMap.getString("Application.title"));
            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/saldos_pagos.jasper"));
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

    }//GEN-LAST:event_jButton3ActionPerformed

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
    }//GEN-LAST:event_buscarFocusGained

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/saldos_pagos.xls");
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        limpiatabla();
        datos();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        busca_clientes busca_clientes = new busca_clientes(null, true, conn, "");
        busca_clientes.setLocationRelativeTo(this);
        busca_clientes.setVisible(true);
        String clavecliente = busca_clientes.getText();//obtiene el valor seleccionado
        busca_clientes = null;
        if (!clavecliente.equals("")) {
            //genera saldo total
            Double saldo_total_importe = 0.0;
            rs0 = null;
            try {
                String senSQL = "SELECT saldos_pagos.*,clientes.nombre FROM saldos_pagos LEFT JOIN clientes ON saldos_pagos.id_clientes=clientes.id_clientes WHERE saldos_pagos.id_clientes='" + clavecliente + "' ORDER BY fechaemision;";
                rs0 = conexion.consulta(senSQL, conn);
                while (rs0.next()) {
                    saldo_total_importe += rs0.getDouble("saldo");
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }


            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
            JasperViewer visor = null;
            JasperPrint jasperPrint = null;
            try {
                Map pars = new HashMap();
                File fichero = new File(conexion.Directorio() + "/logoempresa.png");
                pars.put("logoempresa", new FileInputStream(fichero));
                pars.put("subtitulo", "");
                pars.put("fechaini", null);
                pars.put("fechafin", null);
                pars.put("fechahoy", new java.sql.Timestamp(new Date().getTime()));
                pars.put("total_saldo", saldo_total_importe);
                pars.put("id_cliente_fin", clavecliente);
                pars.put("senSQL", "");
                pars.put("version", resourceMap.getString("Application.title"));
                JasperReport masterReport = null;
                try {
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/estado_cuenta_cliente.jasper"));
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
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        JasperViewer visor = null;
        JasperPrint jasperPrint = null;
        try {
            //String datos="REPORTE GENERADO DEL "+fechamediana.format(fechainsertarhora.parse(fi+" 00:00:00"))+"  AL  "+fechamediana.format(fechainsertarhora.parse(ft+" 23:59:59"));
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio() + "/logoempresa.png");
            pars.put("logoempresa", new FileInputStream(fichero));
            pars.put("subtitulo",mifechacorte);
            pars.put("fechaini", null);
            pars.put("fechafin", null);
            pars.put("senSQL", "");
            pars.put("version", resourceMap.getString("Application.title"));
            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/saldos_pagos_detalle.jasper"));
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
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables
}
