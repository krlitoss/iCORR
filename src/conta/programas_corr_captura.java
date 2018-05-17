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
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
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
import jxl.*;
import jxl.write.*;

/**
 *
 * @author IVONNE
 */
public class programas_corr_captura extends javax.swing.JInternalFrame {

    Connection conn = null;
    DefaultTableModel modelot1 = null;
    ResultSet rs0;
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    DecimalFormat fijo0decimales = new DecimalFormat("######0");
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat estandar1decimal=new DecimalFormat("#,###,##0.0");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MMM-yy");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private Properties conf;
    Calendar calendariniciosem = new GregorianCalendar();
    Calendar calendarfinsem = new GregorianCalendar();
    ListSelectionModel modelot1seleccion;
    String valor_privilegio="1";

    /** Creates new form usuarios */
    public programas_corr_captura(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        conn = connt;
        modelot1 = (DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modelot1seleccion = Tabladatos.getSelectionModel();
        ajusteTabladatos();
        datos_privilegios();
        //fecha de hoy
        calendariniciosem.setTime(new Date()); //gregorian hoy
        calendariniciosem.add(Calendar.DAY_OF_WEEK, (-calendariniciosem.get(Calendar.DAY_OF_WEEK) + 1));
        //fecha de fin de semana
        calendarfinsem.setTime(calendariniciosem.getTime());
        calendarfinsem.add(Calendar.DATE, 7);

        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        limpiatabla();
        datos();
        sumaregistros();

        modelot1seleccion.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta = 0, sumaml = 0, sumakg = 0, sumam2 = 0, sumam2p = 0, sumavel = 0;
                    Double sumatrim = 0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;
                            if (Tabladatos.getValueAt(i, 8) != null && !Tabladatos.getValueAt(i, 8).equals("")) { //suma los metros lineales
                                sumaml += Integer.parseInt("" + Tabladatos.getValueAt(i, 8));
                            }
                            if (Tabladatos.getValueAt(i, 9) != null && !Tabladatos.getValueAt(i, 9).equals("")) {//suma los kilogramos
                                sumakg += Integer.parseInt("" + Tabladatos.getValueAt(i, 9));
                            }
                            if (Tabladatos.getValueAt(i, 10) != null && !Tabladatos.getValueAt(i, 10).equals("")) {//suma los metros2piezas
                                sumam2 += Integer.parseInt("" + Tabladatos.getValueAt(i, 10));
                            }
                            if (Tabladatos.getValueAt(i, 11) != null && !Tabladatos.getValueAt(i, 11).equals("")) {//suma los metros2producido
                                sumam2p += Integer.parseInt("" + Tabladatos.getValueAt(i, 11));
                            }
                            
                            if (Tabladatos.getValueAt(i, 12) != null && !Tabladatos.getValueAt(i, 12).equals("")) {//suma la velocidad
                                sumavel += Integer.parseInt("" + Tabladatos.getValueAt(i, 12));
                            }
                            if (Tabladatos.getValueAt(i, 6) != null && !Tabladatos.getValueAt(i, 6).equals("")) {//suma Trim
                                sumatrim += Double.parseDouble("" + Tabladatos.getValueAt(i, 6));
                            }
                        }
                    }
                    noregistros.setText("Cambios: " + estandarentero.format(cuenta) + "          ML: " + estandarentero.format(sumaml) + "          KG: " + estandarentero.format(sumakg)+ "          M2Piezas: " + estandarentero.format(sumam2)+ "          M2producidos: " + estandarentero.format(sumam2p)+ "          Vel: " + estandarentero.format(sumavel/cuenta)+ "          Trim: " + estandar1decimal.format(sumatrim/cuenta));
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
    public void datos_privilegios(){
        valor_privilegio=conexion.obtener_privilegios(conn,"Corrugadora");
    }

    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(1);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(50);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(12).setPreferredWidth(65);
        Tabladatos.getColumnModel().getColumn(13).setPreferredWidth(150);
        Tabladatos.getColumnModel().getColumn(14).setPreferredWidth(50);
    }

    public void limpiatabla() {
        modelot1.setNumRows(0);
    }

    public void datos(){
        rs0=null;
        try{
            String senSQL="SELECT programa_corr_captura.*,programa_corr.anchototal,programa_corr.anchoutil,programa_corr.ml,programa_corr.claveresistencia,resistencias.corrugado,operadores.nombre, anchototal*prodml/100 as ancho2 FROM (programa_corr_captura LEFT JOIN (programa_corr  LEFT JOIN resistencias ON programa_corr.claveresistencia=resistencias.clave) ON programa_corr_captura.id_programa_corr=programa_corr.id_programa_corr) LEFT JOIN operadores ON programa_corr_captura.id_operador=operadores.id_operador WHERE ((programa_corr_captura.fechareal>='"+conf.getProperty("FechaIni")+" 00:00:00' AND programa_corr_captura.fechareal<='"+conf.getProperty("FechaFin")+" 23:59:59') and programa_corr_captura.estatus<>'Can') ORDER BY programa_corr_captura.fechareal";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double anchototal=rs0.getDouble("anchototal");
                int mlt=rs0.getInt("prodml");
                Object datos[]={rs0.getString("id_programa_corr_captura"),rs0.getString("id_programa_corr"),rs0.getDate("fechareal"),rs0.getString("claveresistencia"),rs0.getString("corrugado"),anchototal,(anchototal-rs0.getDouble("anchoutil")),rs0.getInt("ml"),mlt,rs0.getInt("prodkg"),rs0.getInt("prodm2"),rs0.getInt("ancho2"),(mlt/rs0.getInt("minutosprod")),rs0.getString("nombre"), rs0.getString("turno")};
                modelot1.addRow(datos);
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }

    public void modificar() {
        int filano = Tabladatos.getSelectedRow();
        if (filano < 0) {
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            String clavemodifica = (String) Tabladatos.getValueAt(filano, 0);
            if (clavemodifica.equals("")) {
            } else {
                datos_programas_corr_captura = new datos_programas_corr_captura(null, true, conn, clavemodifica,valor_privilegio);
                datos_programas_corr_captura.setLocationRelativeTo(this);
                datos_programas_corr_captura.setVisible(true);
                datos_programas_corr_captura = null;
            }
        }
    }

    public void sumaregistros() {
        noregistros.setText("Registros: " + Tabladatos.getRowCount());
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
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        menupop.setMinimumSize(new java.awt.Dimension(105, 28));
        menupop.setName("menupop"); // NOI18N
        menupop.setPreferredSize(new java.awt.Dimension(130, 34));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(programas_corr_captura.class);
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

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
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
                "ID", "Programa", "Fecha", "Resis.", "Corrugado", "Ancho", "Trim", "<html><font color=#0066FF>ML Program.", "ML", "KG", "M2Piezas", "M2Producc", "Vel. ML/min", "Operador", "Turno"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                TabladatosMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        jPanel1.setName("jPanel1"); // NOI18N

        noregistros.setForeground(resourceMap.getColor("noregistros.foreground")); // NOI18N
        noregistros.setText(resourceMap.getString("noregistros.text")); // NOI18N
        noregistros.setName("noregistros"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noregistros)
                .addContainerGap(758, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(noregistros)
                .addContainerGap(2, Short.MAX_VALUE))
        );

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

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar1.add(jSeparator5);

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

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(556, 48));

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(134, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jToolBar1.add(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
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

    private void menueliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menueliminarActionPerformed
        // TODO add your handling code here:
        int filano = Tabladatos.getSelectedRow();
        if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA ELIMINAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String senSQLmov = "UPDATE programa_corr_captura SET estatus='Can' WHERE id_programa_corr_captura='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
            conexion.modificaupdate_p(senSQLmov, conn,valor_privilegio);
            senSQLmov = "UPDATE conversion_captura SET estatus='Can' WHERE id_programa_corr_captura='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
            conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
            //elimina del inventario de corrugadora
            senSQLmov = "UPDATE entradas_inventario_corr SET estatus='Can' WHERE id_programa_corr_captura='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
            conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
            senSQLmov = "UPDATE paros_captura SET estatus='Can' WHERE id_programa_corr_captura='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
            conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
           ///**funcion actualiza el status del programa*/
           rs0=null;
            try{
                String senSQL="SELECT programa_corr.id_programa_corr,programa_corr.ml,sumaml.totalml FROM programa_corr LEFT JOIN (SELECT programa_corr_captura.id_programa_corr,sum(prodml) as totalml FROM programa_corr_captura WHERE programa_corr_captura.estatus<>'Can' GROUP BY programa_corr_captura.id_programa_corr) as sumaml ON programa_corr.id_programa_corr=sumaml.id_programa_corr WHERE (programa_corr.id_programa_corr='"+(String) Tabladatos.getValueAt(filano, 1)+"')";
                rs0=conexion.consulta(senSQL,conn);
                int vacio=0;
                if(rs0.next()){
                    Double mlprogramado=rs0.getDouble("ml");
                    Double mlproducido=rs0.getDouble("totalml");
                    if(mlproducido>mlprogramado){
                        senSQLmov = "UPDATE programa_corr SET estatus='Ter' WHERE id_programa_corr='" + (String) Tabladatos.getValueAt(filano, 1) + "';";
                        conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
                    }else{
                        senSQLmov = "UPDATE programa_corr SET estatus='Act' WHERE id_programa_corr='" + (String) Tabladatos.getValueAt(filano, 1) + "';";
                        conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
                    }
                    vacio=1;
                }
                if(vacio==0){
                    senSQLmov = "UPDATE programa_corr SET estatus='Act' WHERE id_programa_corr='" + (String) Tabladatos.getValueAt(filano, 1) + "';";
                    conexion.modificamov_p(senSQLmov, conn,valor_privilegio);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            limpiatabla();
            datos();
        }

}//GEN-LAST:event_menueliminarActionPerformed

    private void TabladatosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TabladatosMouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
                String senSQL="SELECT programa_corr_captura.*, programa_corr.anchototal, programa_corr.anchoutil, programa_corr.ml, programa_corr.claveresistencia, resistencias.corrugado, operadores.nombre, anchototal * prodml / 100 AS ancho2 FROM ( programa_corr_captura LEFT JOIN ( programa_corr LEFT JOIN resistencias ON programa_corr.claveresistencia = resistencias.clave ) ON programa_corr_captura.id_programa_corr = programa_corr.id_programa_corr ) LEFT JOIN operadores ON programa_corr_captura.id_operador = operadores.id_operador WHERE (( programa_corr_captura.fechareal >= '"+fechainsertar.format(busca_fechas.getFechaini())+" 00:00:00' AND programa_corr_captura.fechareal <= '"+fechainsertar.format(busca_fechas.getFechafin())+" 23:59:59' ) AND programa_corr_captura.estatus <> 'Can' ) ORDER BY programa_corr_captura.fechareal";
                rs0=conexion.consulta(senSQL,conn);
                while(rs0.next()){
                    Double anchototal=rs0.getDouble("anchototal");
                    int mlt=rs0.getInt("prodml");
                    Object datos[]={rs0.getString("id_programa_corr_captura"),rs0.getString("id_programa_corr"),rs0.getDate("fechareal"),rs0.getString("claveresistencia"),rs0.getString("corrugado"),anchototal,(anchototal-rs0.getDouble("anchoutil")),rs0.getInt("ml"),mlt,rs0.getInt("prodkg"),rs0.getInt("prodm2"),rs0.getInt("ancho2"),(mlt/rs0.getInt("minutosprod")),rs0.getString("nombre"), rs0.getString("turno")};
                    modelot1.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
        busca_fechas = null;
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/prod_corr.xls");
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
}//GEN-LAST:event_jButton2ActionPerformed

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
}//GEN-LAST:event_buscarFocusGained

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
            //numero de registros en la consulta
            sumaregistros();//funcion que contabiliza los registros
        }
}//GEN-LAST:event_buscarKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        limpiatabla();
        datos();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        datos_programas_corr_captura = new datos_programas_corr_captura(null,true,conn,"",valor_privilegio);
        datos_programas_corr_captura.setLocationRelativeTo(this);
        datos_programas_corr_captura.setVisible(true);
        limpiatabla();
        datos_programas_corr_captura=null;
        datos();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        modificar();
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
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables
    private JDialog datos_programas_corr_captura;
}
