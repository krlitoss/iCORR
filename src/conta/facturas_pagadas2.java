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

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Point;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import mx.bigdata.sat.cfd.CFDv22;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import mx.bigdata.sat.cfdi.CFDv32;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante;

import java.util.HashMap;
import java.util.Map;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import jxl.*;
import jxl.write.*;
import java.awt.Desktop;

/**
 *
 * @author IVONNE
 */
public class facturas_pagadas2 extends javax.swing.JInternalFrame {

    Connection conn = null;
    DefaultTableModel modelot1 = null;
    ListSelectionModel modeloseleccion;
    ResultSet rs0 = null, rs1 = null, rs2 = null;
    private Properties conf;
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat fechamostrartodo = new SimpleDateFormat(" dd 'del mes de' MMMMM 'de' yyyy");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechames = new SimpleDateFormat("MM");
    SimpleDateFormat fechaano = new SimpleDateFormat("yyyy");
    SimpleDateFormat fechamesano = new SimpleDateFormat("MMyyyy");
    SimpleDateFormat fechasin = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat fechaparser = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar10diasantes = new GregorianCalendar();
    String usuariorem = "";
    String valor_privilegio = "1";

    String homeE = System.getProperty("user.home")+"/.temERP";
    Properties defaultProps = new Properties();
    FileInputStream ini;

    Date fechavencimiento = new Date();
    String telefono = "";
    String estatusfacx = "";
    String variasfac = "";
    String varios = "0";

    DecimalFormat num_qr_decimales = new DecimalFormat("0000000000.000000");

    /** Creates new form usuarios */
    public facturas_pagadas2(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        usuariorem = conf.getProperty("UserID");
        conn = connt;
        modelot1 = (DefaultTableModel) Tabladatos.getModel();
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
        calendar10diasantes.add(Calendar.DATE, -10);

        modeloseleccion.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    Double sumaiva = 0.0, sumatotal = 0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if (Tabladatos.getValueAt(i, 4) != null && !Tabladatos.getValueAt(i, 4).equals("")) { //suma los metros lineales
                                sumaiva += Double.parseDouble("" + Tabladatos.getValueAt(i, 4));
                            }
                            if (Tabladatos.getValueAt(i, 5) != null && !Tabladatos.getValueAt(i, 5).equals("")) { //suma los metros lineales
                                sumatotal += Double.parseDouble("" + Tabladatos.getValueAt(i, 5));
                            }
                        }
                    }
                    noregistros.setText("IVA: " + moneda2decimales.format(sumaiva) + "        TOTAL: " + moneda2decimales.format(sumatotal));
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

    public void datos_privilegios() {
        valor_privilegio = conexion.obtener_privilegios(conn, "Facturas Pagadas");
    }

    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(210);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(230);

    }

    public void limpiatabla() {
        modelot1.setNumRows(0);
    }

    public void datos() {
        

        rs0 = null;
        try {

            String senSQL="SELECT docxcob.factura_serie,clientes.id_clientes,docxcob.fechaemision,docxcob.fechavencimiento,docxcob.importe,(docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0)) as saldo,COALESCE(notascreditorealizadas.importenotacredito,0) as importenotacredito,COALESCE(pagosrealizados.importepago,0) as importepago,clientes.nombre,xmlfinal.uuid,facturas.iva,facturas.total,monedas.descripcion as tipomoneda FROM (( ( (docxcob LEFT JOIN ( facturas LEFT JOIN monedas ON facturas.id_moneda=monedas.id_moneda ) ON docxcob.factura_serie=facturas.factura_serie) LEFT JOIN xmlfinal ON docxcob.factura_serie=xmlfinal.factura_serie) LEFT JOIN clientes ON docxcob.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT notas_credito_detalle.factura_serie,sum(notas_credito_detalle.importe) as importenotacredito FROM notas_credito_detalle WHERE (notas_credito_detalle.estatus='1' AND notas_credito_detalle.fecha<='"+conf.getProperty("FechaFin")+" 23:59:59') GROUP BY notas_credito_detalle.factura_serie) as notascreditorealizadas ON docxcob.factura_serie=notascreditorealizadas.factura_serie ) LEFT JOIN (SELECT pagos_detalle.factura_serie,sum(pagos_detalle.importe+pagos_detalle.importe_factoraje) as importepago FROM pagos_detalle LEFT JOIN pagos ON pagos_detalle.id_pagos =pagos.id_pagos WHERE (pagos_detalle.estatus<>'Can' AND pagos_detalle.fecha<='"+conf.getProperty("FechaFin")+" 23:59:59') GROUP BY pagos_detalle.factura_serie) as pagosrealizados ON docxcob.factura_serie=pagosrealizados.factura_serie WHERE (docxcob.estatus<>'Can' AND docxcob.fechaemision>='"+conf.getProperty("FechaIni")+" 23:59:59' AND docxcob.fechaemision<='"+conf.getProperty("FechaFin")+" 23:59:59' AND (docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0))<=0.01)";

            rs0 = conexion.consulta(senSQL, conn);
            while (rs0.next()) {
                Double ivaf = rs0.getDouble("iva");
                Double totalf = rs0.getDouble("total");
                Object datos[] = {rs0.getString("factura_serie"), rs0.getDate("fechaemision"), rs0.getString("nombre"), rs0.getString("tipomoneda"), ivaf, totalf,rs0.getString("uuid")};
                modelot1.addRow(datos);

            }
            if (rs0 != null) {
                rs0.close();
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void ver_factura() {
        int filano = Tabladatos.getSelectedRow();
        if (filano < 0) {
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            String clavemodifica = (String) Tabladatos.getValueAt(filano, 0);
            String varios = (String) Tabladatos.getValueAt(filano, 6);
            if (clavemodifica.equals("")) {
            } else {
                datos_facturas_pagadas = new datos_facturas_pagadas(null, true, conn, clavemodifica, valor_privilegio);
                datos_facturas_pagadas.setLocationRelativeTo(this);
                datos_facturas_pagadas.setVisible(true);
                limpiatabla();
                datos_facturas_pagadas = null;
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
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnconsultar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnexportar = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
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
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(facturas_pagadas2.class);
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
                "Factura", "Fecha", "Cliente", "Moneda", "Iva", "Total","Folio Fiscal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.util.Date.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        btnexportar.setIcon(resourceMap.getIcon("btnexportar.icon")); // NOI18N
        btnexportar.setText(resourceMap.getString("btnexportar.text")); // NOI18N
        btnexportar.setFocusable(false);
        btnexportar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnexportar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnexportar.setMaximumSize(new java.awt.Dimension(70, 48));
        btnexportar.setMinimumSize(new java.awt.Dimension(70, 48));
        btnexportar.setName("btnexportar"); // NOI18N
        btnexportar.setPreferredSize(new java.awt.Dimension(70, 48));
        btnexportar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnexportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnexportar);

        jSeparator7.setName("jSeparator7"); // NOI18N
        jToolBar1.add(jSeparator7);

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
                .addContainerGap(398, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
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
            limpiatabla();
            datos();
        } else {
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)" + buscar.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladatos.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscarKeyReleased

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        if (evt.getClickCount() == 2) {
            ver_factura();
        }
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
    }//GEN-LAST:event_buscarFocusGained

    private void btnconsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnconsultarActionPerformed
        // TODO add your handling code here:
        busca_fechas busca_fechas = new busca_fechas(null, true);
        busca_fechas.setLocationRelativeTo(this);
        busca_fechas.setVisible(true);
        String estado = busca_fechas.getEstado();
        /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if (estado.equals("cancelar")) {
        } else {
            limpiatabla();

            rs0 = null;
            try {

                String senSQL="SELECT docxcob.factura_serie,clientes.id_clientes,docxcob.fechaemision,docxcob.fechavencimiento,docxcob.importe,(docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0)) as saldo,COALESCE(notascreditorealizadas.importenotacredito,0) as importenotacredito,COALESCE(pagosrealizados.importepago,0) as importepago,clientes.nombre,xmlfinal.uuid,facturas.iva,facturas.total,monedas.descripcion as tipomoneda FROM (( ( (docxcob LEFT JOIN ( facturas LEFT JOIN monedas ON facturas.id_moneda=monedas.id_moneda ) ON docxcob.factura_serie=facturas.factura_serie) LEFT JOIN xmlfinal ON docxcob.factura_serie=xmlfinal.factura_serie) LEFT JOIN clientes ON docxcob.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT notas_credito_detalle.factura_serie,sum(notas_credito_detalle.importe) as importenotacredito FROM notas_credito_detalle WHERE (notas_credito_detalle.estatus='1' AND notas_credito_detalle.fecha<='"+fechainsertar.format(busca_fechas.getFechafin())+" 23:59:59') GROUP BY notas_credito_detalle.factura_serie) as notascreditorealizadas ON docxcob.factura_serie=notascreditorealizadas.factura_serie ) LEFT JOIN (SELECT pagos_detalle.factura_serie,sum(pagos_detalle.importe+pagos_detalle.importe_factoraje) as importepago FROM pagos_detalle LEFT JOIN pagos ON pagos_detalle.id_pagos =pagos.id_pagos WHERE (pagos_detalle.estatus<>'Can' AND pagos_detalle.fecha<='"+fechainsertar.format(busca_fechas.getFechafin())+" 23:59:59') GROUP BY pagos_detalle.factura_serie) as pagosrealizados ON docxcob.factura_serie=pagosrealizados.factura_serie WHERE (docxcob.estatus<>'Can' AND docxcob.fechaemision>='"+fechainsertar.format(busca_fechas.getFechaini())+" 23:59:59' AND docxcob.fechaemision<='"+fechainsertar.format(busca_fechas.getFechafin())+" 23:59:59' AND (docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0))<=0.01)";

                rs0 = conexion.consulta(senSQL, conn);
                while (rs0.next()) {
                    Double ivaf = rs0.getDouble("iva");
                    Double totalf = rs0.getDouble("total");
                    Object datos[] = {rs0.getString("factura_serie"), rs0.getDate("fechaemision"), rs0.getString("nombre"), rs0.getString("tipomoneda"), ivaf, totalf,rs0.getString("uuid")};
                    modelot1.addRow(datos);

                }
                if (rs0 != null) {
                    rs0.close();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

        }
        busca_fechas = null;
    }//GEN-LAST:event_btnconsultarActionPerformed

    private void btnexportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarActionPerformed

        // TODO add your handling code here:
        File rutaarchivo=new File(System.getProperty("user.home")+"/facturas_pagadas.xls");
        try
        {
            //Se crea el libro Excel
            WritableWorkbook workbook = Workbook.createWorkbook(rutaarchivo);
            //Se crea una nueva hoja dentro del libro
            WritableSheet sheet = workbook.createSheet("Datos", 0);
            //formatos de texto
            WritableFont arial12b = new WritableFont(WritableFont.createFont("Arial"),12,WritableFont.BOLD, false);
            WritableFont arial10b = new WritableFont(WritableFont.createFont("Arial"),10,WritableFont.BOLD, false);
            WritableFont arial9 = new WritableFont(WritableFont.createFont("Arial"),9,WritableFont.NO_BOLD, false);

            WritableCellFormat arial10fsupertitulo = new WritableCellFormat (arial12b);

            WritableCellFormat arial10ftitulo = new WritableCellFormat (arial10b);
            arial10ftitulo.setBackground(Colour.LIME);//.SKY_BLUE
            arial10ftitulo.setAlignment(Alignment.CENTRE);
            arial10ftitulo.setVerticalAlignment(VerticalAlignment.CENTRE);

            WritableCellFormat arial10fdetalle = new WritableCellFormat (arial9);

            int filainicial=5;

            //titulo del documento
            sheet.addImage(new jxl.write.WritableImage(0, 0, 3, 4,new java.io.File(conexion.Directorio()+"/logoempresa.png")));
            sheet.addCell(new jxl.write.Label(0, filainicial, ""+this.getTitle(),arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for(int j=0;j<(Tabladatos.getColumnCount());j=j+1){
                String titu=""+Tabladatos.getColumnName(j);
                int dotPos = titu.lastIndexOf(">")+1;//le quita el html de los titulos
                if(titu.contains("<html>")){
                    titu=titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu,arial10ftitulo));
                sheet.setColumnView( j, ((Tabladatos.getColumnModel().getColumn(j).getPreferredWidth())/6) );
            }

            filainicial++;//incrementa las filas
            for(int i=0;i<(Tabladatos.getRowCount());i=i+1){
                for(int j=0;j<(Tabladatos.getColumnCount());j=j+1){
                    if(Tabladatos.getValueAt(i, j) != null){
                        if(Tabladatos.getValueAt(i, j) instanceof String){
                            String dato=(String)Tabladatos.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">")+1;//le quita el html de los titulos
                            if(dato.contains("<html>")){
                                dato=dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i+filainicial, dato,arial10fdetalle));
                        }
                        else if(Tabladatos.getValueAt(i, j) instanceof java.lang.Number)
                            sheet.addCell(new jxl.write.Number(j, i+filainicial, Double.parseDouble(Tabladatos.getValueAt(i, j).toString()),arial10fdetalle));
                        else if(Tabladatos.getValueAt(i, j) instanceof java.util.Date)
                            sheet.addCell(new jxl.write.DateTime(j, i+filainicial, (java.util.Date)Tabladatos.getValueAt(i, j), jxl.write.DateTime.GMT));
                        else
                            sheet.addCell(new jxl.write.Boolean(j, i+filainicial,(java.lang.Boolean)Tabladatos.getValueAt(i, j),arial10fdetalle));
                    }
                }
            }/**fin de revisar los campos vacios*/


            //Escribimos los resultados al fichero Excel
            workbook.write();
            workbook.close();
        }
        catch (IOException ex) {
           JOptionPane.showMessageDialog(this,"EL ARCHIVO ESTA ABIERTO O NO SE PUEDE CREAR"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
        catch (WriteException exe) {
           JOptionPane.showMessageDialog(this,"ERROR AL EXPORTAR DATOS"+exe,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }


        //abrir el documento creado
        try {
          Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"NO SE PUEDE ABRIR EL ARCHIVO\n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnexportarActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btnconsultar;
    private javax.swing.JButton btnexportar;
    private javax.swing.JTextField buscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables
    private JDialog datos_facturas_pagadas;
}
