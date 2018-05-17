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
public class programas_corr_cap_reporte extends javax.swing.JInternalFrame {

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
    public programas_corr_cap_reporte(Connection connt) {
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
        datos("","");
        sumaregistros();

        modelot1seleccion.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta = 0, sumaml = 0, sumam2 = 0, sumaml_prod = 0, sumam2_prod = 0, sumam2_malas_prod = 0, sumam2_trim_prod = 0, sumavel = 0;
                    Double sumatrim = 0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;
                            if (Tabladatos.getValueAt(i, 7) != null && !Tabladatos.getValueAt(i, 7).equals("")) {
                                sumaml += Integer.parseInt("" + Tabladatos.getValueAt(i, 7));
                            }
                            if (Tabladatos.getValueAt(i, 8) != null && !Tabladatos.getValueAt(i, 8).equals("")) {
                                sumam2 += Integer.parseInt("" + Tabladatos.getValueAt(i, 8));
                            }
                            if (Tabladatos.getValueAt(i, 9) != null && !Tabladatos.getValueAt(i, 9).equals("")) {
                                sumaml_prod += Integer.parseInt("" + Tabladatos.getValueAt(i, 9));
                            }
                            if (Tabladatos.getValueAt(i, 10) != null && !Tabladatos.getValueAt(i, 10).equals("")) {
                                sumam2_prod += Integer.parseInt("" + Tabladatos.getValueAt(i, 10));
                            }
                            if (Tabladatos.getValueAt(i, 12) != null && !Tabladatos.getValueAt(i, 12).equals("")) {
                                sumam2_malas_prod += Double.parseDouble("" + Tabladatos.getValueAt(i, 12));
                            }
                            if (Tabladatos.getValueAt(i, 13) != null && !Tabladatos.getValueAt(i, 13).equals("")) {
                                sumam2_trim_prod += Double.parseDouble("" + Tabladatos.getValueAt(i, 13));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: " + estandarentero.format(cuenta) + "          ML(Program): " + estandarentero.format(sumaml) + "          M2(Program): " + estandarentero.format(sumam2)+ "          ML(Prod): " + estandarentero.format(sumaml_prod)+ "          M2(Prod): " + estandarentero.format(sumam2_prod)+ "          M2(Malas): " + estandarentero.format(sumam2_malas_prod)+ "          M2(Trim): " + estandarentero.format(sumam2_trim_prod));
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
        valor_privilegio=conexion.obtener_privilegios(conn,"R M2 Producidos");
    }

    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(1);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(50);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(12).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(13).setPreferredWidth(100);

    }

    public void limpiatabla() {
        modelot1.setNumRows(0);
    }

    public void datos(String fecha_ini,String fecha_fin){
        rs0=null;
        if(fecha_ini.equals("")){
            fecha_ini = conf.getProperty("FechaIni");
        }
        if(fecha_fin.equals("")){
            fecha_fin = conf.getProperty("FechaFin");
        }

        try{
            String senSQL = "SELECT "
            +"pcca.id_programa_corr_captura, "
            +"MAX(pcca.id_programa_corr) AS id_programa_corr, "
            +"MAX(pcca.fechareal) AS fechareal, "
            +"MAX(pcor.claveresistencia) AS claveresistencia, "
            +"MAX(resi.corrugado) AS corrugado, "
            +"MAX(pcor.anchototal) AS anchototal, "
            +"MAX(pcor.anchoutil) AS anchoutil, "
            +"MAX(pcor.anchototal-pcor.anchoutil) AS trim_program, "
            +"MAX(pcor.ml) AS ml_program, "
            +"MAX(pcor.m2) AS m2_program, "
            +"MAX(pcor.kg) AS kg_program, "
            +"MAX(pcca.prodml) AS ml_prod, "
            +"MAX(pcca.prodm2) AS m2_prod, "
            +"MAX(pcca.prodkg) AS kg_prod,"
            + "MAX(pcca.prodml*pcor.anchototal/100) AS m2_real, "
            +"SUM(pcde.cantidadpiezas * 1 *arti.m2) AS m2_prod2, "
            +"SUM(pcde.cantidadpiezas * 1 *arti.kg) AS kg_prod2, "
            +"SUM(pcde.cantidadmalaspiezas * 1 *arti.m2) AS m2_prod_malas, "
            +"SUM(pcde.cantidadmalaspiezas * 1 *arti.kg) AS kg_prod_malas, "
            +"MAX((pcor.anchototal-pcor.anchoutil) * pcca.prodml / 100) AS m2_prod_trim, "
            +"MAX((pcor.anchototal-pcor.anchoutil) * pcca.prodml / 100 * resi.pesom2) AS kg_prod_trim, "
            +"MAX(pcca.prodml/pcca.minutosprod ) AS velocidad_prod, "
            +"MAX(oper.nombre) AS nombre_operador "
            +"FROM programa_corr_captura AS pcca "
            +"INNER JOIN programa_corr AS pcor ON pcor.id_programa_corr = pcca.id_programa_corr "
            +"INNER JOIN resistencias AS resi ON resi.clave = pcor.claveresistencia "
            +"INNER JOIN conversion_captura AS pcde ON pcde.id_programa_corr_captura = pcca.id_programa_corr_captura "
            +"INNER JOIN articulos AS arti ON arti.clavearticulo = pcde.clavearticulo "
            +"INNER JOIN operadores AS oper ON oper.id_operador = pcde.id_operador "
            +"WHERE pcca.estatus <> 'Can' "
            +" AND (pcca.fechareal>='"+fecha_ini+" 00:00:00' AND pcca.fechareal<='"+fecha_fin+" 23:59:59') "
            +"GROUP BY pcca.id_programa_corr_captura "
            +"ORDER BY "
            +"MAX(pcca.fechareal) ASC";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){

                int m2_real = rs0.getInt("m2_real");
                int m2_prod2 = rs0.getInt("m2_prod2");
                int m2_prod_malas = rs0.getInt("m2_prod_malas");
                int m2_prod_trim = rs0.getInt("m2_prod_trim");
                int m2_no_reg = m2_real-m2_prod2-m2_prod_malas-m2_prod_trim;
                int m2_prod_malas_no_reg = m2_prod_malas+m2_no_reg;
                Object datos[]={rs0.getString("id_programa_corr_captura"),rs0.getString("id_programa_corr"),rs0.getDate("fechareal"),rs0.getString("claveresistencia"),rs0.getString("corrugado"),rs0.getDouble("anchototal"),rs0.getDouble("trim_program"),rs0.getInt("ml_program"),rs0.getInt("ml_prod"),rs0.getInt("kg_prod2"),rs0.getInt("m2_program"),m2_real,m2_prod2,m2_prod_malas,m2_prod_malas_no_reg,m2_prod_trim,m2_no_reg,rs0.getInt("velocidad_prod"),rs0.getString("nombre_operador")};
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

        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(programas_corr_cap_reporte.class);
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
                "ID", "Programa", "Fecha", "Resis.", "Corrugado", "Ancho", "Trim", "<html><font color=#0066FF>ML Program.", "ML Prod", "KG Prod","<html><font color=#0066FF>M2 Program.", "<html><font color=#006e2e>M2 Reales", "M2 Prod","<html><font color=#DF0101>M2 Malas Prod Reg","<html><font color=#DF0101>M2 Malas Prod","<html><font color=#DF0101>M2 TRIM Prod","<html><font color=#DF0101>M2 NO Reg.", "Vel. ML/min", "Operador"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class , java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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

        noregistros.setName("noregistros"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noregistros)
                .addContainerGap(762, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(noregistros)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
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

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(556, 48));

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
                .addContainerGap(353, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

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
            datos(fechainsertar.format(busca_fechas.getFechaini()),fechainsertar.format(busca_fechas.getFechafin()));

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
            datos("","");
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
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField buscar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables
    private JDialog datos_programas_corr_captura;
}
