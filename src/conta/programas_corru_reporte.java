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

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Point;
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
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jxl.*;
import jxl.write.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author IVONNE
 */
public class programas_corru_reporte extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot1=null;
    ResultSet rs0;
    DecimalFormat moneda4decimales=new DecimalFormat("#,###,##0.0000");
    DecimalFormat moneda3decimales=new DecimalFormat("#,###,##0.000");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    
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
    public programas_corru_reporte(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf=conexion.archivoInicial();
        conn=connt;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modelot1seleccion = Tabladatos.getSelectionModel();
        ajusteTabladatos();
        datos_privilegios();
        //fecha de hoy
        calendariniciosem.setTime(new Date()); //gregorian hoy
        calendariniciosem.add(Calendar.DAY_OF_WEEK, (-calendariniciosem.get(Calendar.DAY_OF_WEEK)+1) );
        //fecha de fin de semana
        calendarfinsem.setTime(calendariniciosem.getTime());
        calendarfinsem.add(Calendar.DATE,7);
        
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        limpiatabla();
        datos("","");
        sumaregistros();
        ajusteTablaRender();
        
        modelot1seleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
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
        valor_privilegio=conexion.obtener_privilegios(conn,"R Combinaciones promedio de papel");
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(55);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(95);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(240);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(110);
        Tabladatos.getColumnModel().getColumn(12).setPreferredWidth(110);
        Tabladatos.getColumnModel().getColumn(28).setPreferredWidth(140);
        Tabladatos.getColumnModel().getColumn(29).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(30).setPreferredWidth(100);
        
    }

    public void ajusteTablaRender() {

        Tabladatos.getColumnModel().getColumn(30).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                javax.swing.JLabel label = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                label.setText(value.toString());
                if(Tabladatos.getValueAt(row, 1) != null){

                    if(row%2 == 0){
                        label.setBackground(new Color(240,245,250));
                    }else{
                        label.setBackground(new Color(230,235,239));
                    }
                }else{
                    label.setBackground(Color.LIGHT_GRAY);
                }
                return label;
            }
        });

        Tabladatos.getColumnModel().getColumn(31).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                javax.swing.JLabel label = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                label.setText(value.toString());
                if(Tabladatos.getValueAt(row, 1) != null){

                    if(row%2 == 0){
                        label.setBackground(new Color(240,245,250));
                    }else{
                        label.setBackground(new Color(230,235,239));
                    }
                }else{
                    label.setBackground(Color.LIGHT_GRAY);
                }
                return label;
            }
        });

        Tabladatos.getColumnModel().getColumn(32).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                javax.swing.JLabel label = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                label.setText(value.toString());
                if(Tabladatos.getValueAt(row, 1) != null){

                    if(row%2 == 0){
                        label.setBackground(new Color(240,245,250));
                    }else{
                        label.setBackground(new Color(230,235,239));
                    }
                }else{
                    label.setBackground(Color.LIGHT_GRAY);
                }
                return label;
            }
        });

    }

  


    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos(String fecha_ini, String fecha_fin) {
        rs0 = null;
        if (fecha_ini.equals("")) {
            fecha_ini = conf.getProperty("FechaIni");
        }
        if (fecha_fin.equals("")) {
            fecha_fin = conf.getProperty("FechaFin");
        }

        try {
            String senSQL = "SELECT programa_corr.*, "
                    + "programa_corr_detalle.*, "
                    + "articulos.claveresistencia as cr,articulos.articulo,articulos.largo as art_largo,articulos.ancho as art_ancho,articulos.m2 as art_m2,articulos.kg as art_kg,articulos.scores,articulos.piezas as art_piezas, "
                    + "ops.fechaentrega, "
                    + "clientes.nombre, "
                    + "sumaml.fechaprod,COALESCE(sumaml.totalml,0) as totalml,COALESCE(sumaml.totalkg,0) as totalkg , "
                    + "(programa_corr.l1r || ' X ' || round(programa_corr.al1r,0)) AS liner1, "
                    + "(pl1.peso*1000) as l1_peso, "
                    + "pl1.preciocompra as l1_costo, "
                    + "(programa_corr.r1r || ' X ' || round(programa_corr.ar1r,0)) AS medium1, "
                    + "(pr1.peso*1000) as r1_peso, "
                    + "pr1.preciocompra as r1_costo, "
                    + "(programa_corr.l2r || ' X ' || round(programa_corr.al2r,0)) AS liner2, "
                    + "(pl2.peso*1000) as l2_peso, "
                    + "pl2.preciocompra as l2_costo, "
                    + "(programa_corr.r2r || ' X ' || round(programa_corr.ar2r,0)) AS medium2, "
                    + "(pr2.peso*1000) as r2_peso, "
                    + "pr2.preciocompra as r2_costo, "
                    + "(programa_corr.l3r || ' X ' || round(programa_corr.al3r,0)) AS liner3, "
                    + "(pl3.peso*1000) as l3_peso, "
                    + "pl3.preciocompra as l3_costo, "
                    + "(((pl1.peso*1000)+(COALESCE(pl2.peso,0)*1000)+(COALESCE(pl3.peso,0)*1000)+(pr1.peso*1000*1.48)+(COALESCE(pr2.peso,0)*1000*1.36))/1000) AS consumo_papel_m2, "
                    + "(((pl1.peso*1000*pl1.preciocompra)+(COALESCE(pl2.peso,0)*1000*pl2.preciocompra)+(COALESCE(pl3.peso,0)*1000*COALESCE(pl3.preciocompra,0))+(pr1.peso*1000*1.48*pr1.preciocompra)+(COALESCE(pr2.peso,0)*1000*1.36*COALESCE(pr2.preciocompra,0)))/1000) AS costo_promedio, "
                    + "((((pl1.peso*1000*pl1.preciocompra)+(COALESCE(pl2.peso,0)*1000*pl2.preciocompra)+(COALESCE(pl3.peso,0)*1000*COALESCE(pl3.preciocompra,0))+(pr1.peso*1000*1.48*pr1.preciocompra)+(COALESCE(pr2.peso,0)*1000*1.36*COALESCE(pr2.preciocompra,0)))/1000) * articulos.m2) AS costo_promedio_pieza "
                    + "FROM ( ((programa_corr LEFT JOIN (SELECT programa_corr_captura.id_programa_corr,max(fechareal) as fechaprod,sum(prodml) as totalml,sum(prodkg) as totalkg FROM programa_corr_captura WHERE programa_corr_captura.estatus<>'Can' GROUP BY programa_corr_captura.id_programa_corr) as sumaml ON programa_corr.id_programa_corr=sumaml.id_programa_corr)  "
                    + "INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr)  "
                    + "LEFT JOIN ops ON programa_corr_detalle.op=ops.op)  "
                    + "LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_corr_detalle.clavearticulo=articulos.clavearticulo  "
                    + "LEFT JOIN productos AS  pl1 ON ( (programa_corr.l1r || ' X ' || round(programa_corr.al1r,0) )=pl1.clave OR (REPLACE(programa_corr.l1r,'BID-','BID-AD') || ' X ' || round(programa_corr.al1r,0) )=pl1.clave ) "
                    + "LEFT JOIN productos AS  pr1 ON ( (programa_corr.r1r || ' X ' || round(programa_corr.ar1r,0) )=pr1.clave OR (REPLACE(programa_corr.r1r,'BID-','BID-AD') || ' X ' || round(programa_corr.ar1r,0) )=pr1.clave ) "
                    + "LEFT JOIN productos AS  pl2 ON ( (programa_corr.l2r || ' X ' || round(programa_corr.al2r,0) )=pl2.clave OR (REPLACE(programa_corr.l2r,'BID-','BID-AD') || ' X ' || round(programa_corr.al2r,0) )=pl2.clave ) "
                    + "LEFT JOIN productos AS  pr2 ON ( (programa_corr.r2r || ' X ' || round(programa_corr.ar2r,0) )=pr2.clave OR (REPLACE(programa_corr.r2r,'BID-','BID-AD') || ' X ' || round(programa_corr.ar2r,0) )=pr2.clave ) "
                    + "LEFT JOIN productos AS  pl3 ON ( (programa_corr.l3r || ' X ' || round(programa_corr.al3r,0) )=pl3.clave OR (REPLACE(programa_corr.l3r,'BID-','BID-AD') || ' X ' || round(programa_corr.al3r,0) )=pl3.clave ) "
                    + "WHERE programa_corr.estatus<>'Can'  "
                    + "AND (programa_corr.fecha>='" + fecha_ini + " 00:00:00' AND programa_corr.fecha<='" + fecha_fin + " 23:59:59')  "
                    + "ORDER BY programa_corr_detalle.clavearticulo ";
            rs0 = conexion.consulta(senSQL, conn);
            String bandera = "";
            String articulo_ant = "";
            Double gran_count_art = 0.0;
            Double gran_total_consumo_papel = 0.0;
            Double gran_total_costo_promedio = 0.0;
            Double gran_total_costo_pieza = 0.0;
            Double count_art = 0.0;
            Double total_consumo_papel = 0.0;
            Double total_costo_promedio = 0.0;
            Double total_costo_pieza = 0.0;
            while (rs0.next()) {

                String noprograma = rs0.getString("id_programa_corr");
                String liner1 = rs0.getString("liner1");
                String medium1 = rs0.getString("medium1");
                String liner2 = rs0.getString("liner2");
                String medium2 = rs0.getString("medium2");
                String liner3 = rs0.getString("liner3");
                liner2 = (liner2.equals(" X 0")) ? "" : liner2;
                medium2 = (medium2.equals(" X 0")) ? "" : medium2;
                liner3 = (liner3.equals(" X 0")) ? "" : liner3;
                /*Sumatorias por articulo*/
                Double consumo_papel = rs0.getDouble("consumo_papel_m2");
                Double costo_promedio = rs0.getDouble("costo_promedio");
                Double costo_pieza = rs0.getDouble("costo_promedio_pieza");
                String clavearticulo = rs0.getString("clavearticulo");
                if (articulo_ant.equals("")) {
                    articulo_ant = clavearticulo;
                }
                if (!clavearticulo.equals(articulo_ant)) {
                    Double avg_consumo_papel = total_consumo_papel / count_art;
                    Double avg_costo_promedio = total_costo_promedio / count_art;
                    Double avg_costo_pieza = total_costo_pieza / count_art;
                    Object datos[] = {null, null, null, null, null, null, "<html><font color=green><b>PROMEDIO:", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "<html><font color=green><b>" + moneda3decimales.format(avg_consumo_papel), "<html><font color=green><b>" + moneda3decimales.format(avg_costo_promedio), "<html><font color=green><b>" + moneda3decimales.format(avg_costo_pieza)};
                    modelot1.addRow(datos);

                    count_art = 0.0;
                    total_consumo_papel = 0.0;
                    total_costo_promedio = 0.0;
                    total_costo_pieza = 0.0;
                    articulo_ant = clavearticulo;
                }
                count_art += 1.0;
                total_consumo_papel += consumo_papel;
                total_costo_promedio += costo_promedio;
                total_costo_pieza += costo_pieza;
                gran_count_art += 1.0;
                gran_total_consumo_papel += consumo_papel;
                gran_total_costo_promedio += costo_promedio;
                gran_total_costo_pieza += costo_pieza;

                Object datos[] = {noprograma, rs0.getString("estatus"), rs0.getString("op"), rs0.getString("nombre"), rs0.getString("claveresistencia"), clavearticulo, rs0.getString("articulo"), rs0.getInt("laminas"), rs0.getDouble("art_largo"), rs0.getDouble("art_ancho"), rs0.getDouble("art_m2"), rs0.getDate("fecha"), rs0.getDate("fechaprod"), rs0.getInt("art_piezas"), rs0.getDouble("anchototal"), liner1, rs0.getDouble("l1_peso"), rs0.getDouble("l1_costo"), medium1, rs0.getDouble("r1_peso"), rs0.getDouble("r1_costo"), liner2, rs0.getDouble("l2_peso"), rs0.getDouble("l2_costo"), medium2, rs0.getDouble("r2_peso"), rs0.getDouble("r2_costo"), liner3, rs0.getDouble("l3_peso"), rs0.getDouble("l3_costo"), moneda3decimales.format(consumo_papel), moneda3decimales.format(costo_promedio), moneda3decimales.format(costo_pieza)};
                modelot1.addRow(datos);
                bandera = noprograma;
            }
            /*Ultima fila*/
            Double avg_consumo_papel = total_consumo_papel / count_art;
            Double avg_costo_promedio = total_costo_promedio / count_art;
            Double avg_costo_pieza = total_costo_pieza / count_art;
            Object datos[] = {null, null, null, null, null, null, "<html><font color=green><b>PROMEDIO:", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "<html><font color=green><b>" + moneda3decimales.format(avg_consumo_papel), "<html><font color=green><b>" + moneda3decimales.format(avg_costo_promedio), "<html><font color=green><b>" + moneda3decimales.format(avg_costo_pieza)};
            modelot1.addRow(datos);
            /*Fila total*/
            avg_consumo_papel = gran_total_consumo_papel / gran_count_art;
            avg_costo_promedio = gran_total_costo_promedio / gran_count_art;
            avg_costo_pieza = gran_total_costo_pieza / gran_count_art;
            Object datos2[] = {null, null, null, null, null, null, "<html><font color=blue><b>PROMEDIO TOTAL:", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "<html><font color=blue><b>" + moneda3decimales.format(avg_consumo_papel), "<html><font color=blue><b>" + moneda3decimales.format(avg_costo_promedio), "<html><font color=blue><b>" + moneda3decimales.format(avg_costo_pieza)};
            modelot1.addRow(datos2);

            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTAx\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

    }
    public void modificar(){
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String clavemodifica=(String)Tabladatos.getValueAt(filano, 0);
            if(clavemodifica.equals("")){

            }else{
                datos_programas_corr = new datos_programas_corr(null,true,conn,clavemodifica,valor_privilegio);
                datos_programas_corr.setLocationRelativeTo(this);
                datos_programas_corr.setVisible(true);
                datos_programas_corr=null;
            }
        }
    }
    public void sumaregistros() {
        noregistros.setText("Registros: "+Tabladatos.getRowCount());
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
        detalle = new javax.swing.JMenuItem();
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

        menupop.setName("menupop"); // NOI18N
        menupop.setPreferredSize(new java.awt.Dimension(130, 120));

        detalle.setName("detalle"); // NOI18N
        detalle.setPreferredSize(new java.awt.Dimension(130, 30));
        detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleActionPerformed(evt);
            }
        });
        menupop.add(detalle);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(programas_corru_reporte.class);
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
                "Programa", "Status", "OP", "Cliente", "Resis.", "Clave Articulo", "Articulo", "Laminas", "Largo (cm)", "Ancho(cm)", "Area(m2)","Fecha Alta Programa","Fecha Produccion", "Piezas x Lamina", "Ancho Total(cm)", "Clave l1", "Gramos l1","Costo l1", "Clave m1", "Gramos m1","Costo m1", "Clave l2", "Gramos l2","Costo l2", "Clave m2", "Gramos m2","Costo m2", "Clave l3", "Gramos l3","Costo l3","Consum Papel(kg)","Costo Prom","Costo Pieza"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class,  java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.util.Date.class, java.util.Date.class, java.lang.Integer.class, java.lang.Double.class,java.lang.String.class,java.lang.Double.class,java.lang.Double.class,java.lang.String.class,java.lang.Double.class,java.lang.Double.class,java.lang.String.class,java.lang.Double.class,java.lang.Double.class,java.lang.String.class,java.lang.Double.class,java.lang.Double.class,java.lang.String.class,java.lang.Double.class,java.lang.Double.class,java.lang.String.class,java.lang.String.class,java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false,false, false, false,false, false, false,false, false, false,false, false, false,false, false, false,false, false, false, false, false, false, false, false
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
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
        if (evt.getClickCount() ==2){
            modificar();
            limpiatabla();
            datos("","");
        }
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3)
        {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tabladatos.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tabladatos.changeSelection(rowNumber, 0, false, false);
            menupop.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void TabladatosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_TabladatosMouseReleased

    private void detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleActionPerformed
        // TODO add your handling code here:
        modificar();
    }//GEN-LAST:event_detalleActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        busca_pendientes_cliente busca_pendientes_cliente = new busca_pendientes_cliente(null,true,conn,"");
        busca_pendientes_cliente.setLocationRelativeTo(this);
        busca_pendientes_cliente.setVisible(true);
        String estado=busca_pendientes_cliente.getEstado();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if(estado.equals("cancelar")){

        }else{

            limpiatabla();
            datos(fechainsertar.format(busca_pendientes_cliente.getFechaini()),fechainsertar.format(busca_pendientes_cliente.getFechafin()));

        }
        busca_pendientes_cliente=null;

}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        File rutaarchivo=new File(System.getProperty("user.home")+"/prog_corr.xls");
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
}//GEN-LAST:event_jButton2ActionPerformed

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
        }else{
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar.getText()));
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
    private javax.swing.JMenuItem detalle;
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
    private javax.swing.JPopupMenu menupop;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_programas_corr;
}
