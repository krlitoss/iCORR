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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jxl.*;
import jxl.write.*;
//graficas
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public class radiografia_produccion extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot_corr=null,modelot_conver=null,modelot_paros=null,modelot_consumos=null,modelot_merma=null;
    ListSelectionModel modeloseleccion_corr=null,modeloseleccion_conver=null,modeloseleccion_paros=null,modeloseleccion_consumos=null,modeloseleccion_merma=null;
    ResultSet rs0;
    private Properties conf;
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MMM-yy");
    SimpleDateFormat fechamuycorta = new SimpleDateFormat("dd/MM/yy");
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales=new DecimalFormat("######0.000");
    DecimalFormat moneda0decimales=new DecimalFormat("$ #,###,##0");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat porcentaje2decimal=new DecimalFormat("##0.00 %");
    DecimalFormat horasminutos=new DecimalFormat("##########00");
    DecimalFormat estandar1decimal=new DecimalFormat("#,###,##0.0");

    String fechaini_guarda="";
    String fechafin_guarda="";
    String sqlcli_guarda="";
    java.awt.Font font8plain=new java.awt.Font("Arial", java.awt.Font.PLAIN, 8);
    java.awt.Font font9plain=new java.awt.Font("Arial", java.awt.Font.PLAIN, 9);
    java.awt.Font font10plain=new java.awt.Font("Arial", java.awt.Font.PLAIN, 10);
    java.awt.Font font11plain=new java.awt.Font("Arial", java.awt.Font.PLAIN, 11);
    java.awt.Font font12bold=new java.awt.Font("Arial", java.awt.Font.PLAIN, 12);

    /** Creates new form usuarios */
    public radiografia_produccion(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conn=connt;
        conf = conexion.archivoInicial();
        //modelos de las tablas
        modelot_corr=(DefaultTableModel) Tabla_corr.getModel();
        Tabla_corr.setModel(modelot_corr);
        modeloseleccion_corr = Tabla_corr.getSelectionModel();
        modelot_conver=(DefaultTableModel) Tabla_conver.getModel();
        Tabla_conver.setModel(modelot_conver);
        modeloseleccion_conver = Tabla_conver.getSelectionModel();
        modelot_paros=(DefaultTableModel) Tabla_paros.getModel();
        Tabla_paros.setModel(modelot_paros);
        modeloseleccion_paros = Tabla_paros.getSelectionModel();
        modelot_consumos=(DefaultTableModel) Tabla_consumos.getModel();
        Tabla_consumos.setModel(modelot_consumos);
        modeloseleccion_consumos = Tabla_consumos.getSelectionModel();
        modelot_merma=(DefaultTableModel) Tabla_merma.getModel();
        Tabla_merma.setModel(modelot_merma);
        modeloseleccion_merma = Tabla_merma.getSelectionModel();
        
        ajusteTabladatos();
        horasminutos.setRoundingMode(RoundingMode.DOWN);
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena_participa = new TableRowSorter<TableModel>(modelot_corr);
        Tabla_corr.setRowSorter(elQueOrdena_participa);
        //trae los primeros datos

        maquinas();
        todos_datos();

        modeloseleccion_corr.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumacambios=0;
                    Double sumaml=0.0,sumakg=0.0,sumavel=0.0,sumatrim=0.0,partidas=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            partidas++;
                            if(Tabla_corr.getValueAt(i,3)!=null && !Tabla_corr.getValueAt(i,3).equals("")){ //suma los metros lineales
                                    sumacambios+=Integer.parseInt(fijo0decimales.format((Double)Tabla_corr.getValueAt(i,3)));
                            }
                            if(Tabla_corr.getValueAt(i,1)!=null && !Tabla_corr.getValueAt(i,1).equals("")){ //suma los metros lineales
                                    sumaml+=Double.parseDouble(""+Tabla_corr.getValueAt(i,1));
                            }
                            if(Tabla_corr.getValueAt(i,2)!=null && !Tabla_corr.getValueAt(i,2).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabla_corr.getValueAt(i,2));
                            }
                            if(Tabla_corr.getValueAt(i,4)!=null && !Tabla_corr.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumavel+=Double.parseDouble(""+Tabla_corr.getValueAt(i,4));
                            }
                            if(Tabla_corr.getValueAt(i,8)!=null && !Tabla_corr.getValueAt(i,8).equals("")){ //suma los metros lineales
                                    sumatrim+=Double.parseDouble(""+Tabla_corr.getValueAt(i,8));
                            }

                        }
                    }
                    txt_res_corr.setText("Cambios: "+estandarentero.format(sumacambios)+"    ML: "+estandarentero.format(sumaml)+"    KG: "+estandarentero.format(sumakg)+"    Vel: "+estandarentero.format(sumavel/partidas)+"    Trim: "+estandar1decimal.format(sumatrim/partidas));
                }
            }
        });
        modeloseleccion_paros.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    Double sumatiempo=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabla_paros.getValueAt(i,0)!=null && !Tabla_paros.getValueAt(i,0).equals("")){ //suma los metros lineales
                                    sumatiempo+=Integer.parseInt(fijo0decimales.format((Double)Tabla_paros.getValueAt(i,0)));
                            }
                        }
                    }
                    txt_res_paros.setText("Horas: "+horasminutos.format(sumatiempo/60)+":"+horasminutos.format(sumatiempo%60));
                }
            }
        });
        modeloseleccion_conver.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumacambios=0;
                    Double sumacant=0.0,sumapzas=0.0,sumakg=0.0,sumavel=0.0,partidas=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            partidas++;
                            if(Tabla_conver.getValueAt(i,4)!=null && !Tabla_conver.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumacambios+=Integer.parseInt(fijo0decimales.format((Double)Tabla_conver.getValueAt(i,4)));
                            }
                            if(Tabla_conver.getValueAt(i,1)!=null && !Tabla_conver.getValueAt(i,1).equals("")){ //suma los metros lineales
                                    sumacant+=Double.parseDouble(""+Tabla_conver.getValueAt(i,1));
                            }
                            if(Tabla_conver.getValueAt(i,3)!=null && !Tabla_conver.getValueAt(i,3).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabla_conver.getValueAt(i,3));
                            }
                            if(Tabla_conver.getValueAt(i,6)!=null && !Tabla_conver.getValueAt(i,6).equals("")){ //suma los metros lineales
                                    sumavel+=Double.parseDouble(""+Tabla_conver.getValueAt(i,6));
                            }
                            if(Tabla_conver.getValueAt(i,2)!=null && !Tabla_conver.getValueAt(i,2).equals("")){ //suma los metros lineales
                                    sumapzas+=Double.parseDouble(""+Tabla_conver.getValueAt(i,2));
                            }
                        }
                    }
                    txt_res_conver.setText("Cambios: "+estandarentero.format(sumacambios)+"    Cantidad: "+estandarentero.format(sumacant)+"    Piezas: "+estandarentero.format(sumapzas)+"    KG: "+estandarentero.format(sumakg)+"    Vel: "+estandarentero.format(sumavel/partidas));
                }
            }
        });
        modeloseleccion_consumos.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumacambios=0;
                    Double sumakg=0.0,sumaimp=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabla_consumos.getValueAt(i,1)!=null && !Tabla_consumos.getValueAt(i,1).equals("")){ //suma los metros lineales
                                    sumacambios+=Integer.parseInt(fijo0decimales.format(Double.parseDouble(""+Tabla_consumos.getValueAt(i,1))));
                            }
                            if(Tabla_consumos.getValueAt(i,2)!=null && !Tabla_consumos.getValueAt(i,2).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabla_consumos.getValueAt(i,2));
                            }
                            if(Tabla_consumos.getValueAt(i,3)!=null && !Tabla_consumos.getValueAt(i,3).equals("")){ //suma los metros lineales
                                    sumaimp+=Double.parseDouble(""+Tabla_consumos.getValueAt(i,3));
                            }
                        }
                    }
                    txt_res_consumos.setText("Rollos: "+estandarentero.format(sumacambios)+"    KG: "+estandarentero.format(sumakg)+"    Importe: "+moneda2decimales.format(sumaimp)+"    P.K.: "+moneda2decimales.format(sumaimp/sumakg));
                }
            }
        });
        modeloseleccion_merma.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    Double sumakg=0.0,sumapzas=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabla_merma.getValueAt(i,1)!=null && !Tabla_merma.getValueAt(i,1).equals("")){ //suma los metros lineales
                                    sumakg+=(Double)Tabla_merma.getValueAt(i,1);
                            }
                            if(Tabla_merma.getValueAt(i,2)!=null && !Tabla_merma.getValueAt(i,2).equals("")){ //suma los metros lineales
                                    sumapzas+=(Double)Tabla_merma.getValueAt(i,2);
                            }
                        }
                    }
                    txt_res_merma.setText("KG: "+estandarentero.format(sumakg)+"    Piezas: "+estandarentero.format(sumapzas));
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
    public void ajusteTabladatos() {

        //participacion
        Tabla_corr.getColumnModel().getColumn(0).setPreferredWidth(85);
        Tabla_corr.getColumnModel().getColumn(1).setPreferredWidth(90);
        Tabla_corr.getColumnModel().getColumn(2).setPreferredWidth(90);
        Tabla_corr.getColumnModel().getColumn(3).setPreferredWidth(75);
        Tabla_corr.getColumnModel().getColumn(4).setPreferredWidth(75);
        Tabla_corr.getColumnModel().getColumn(5).setPreferredWidth(75);
        Tabla_corr.getColumnModel().getColumn(6).setPreferredWidth(75);
        Tabla_corr.getColumnModel().getColumn(7).setPreferredWidth(75);
        Tabla_corr.getColumnModel().getColumn(8).setPreferredWidth(75);
        Tabla_corr.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabla_corr.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer_num());
        Tabla_corr.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer_num());
        Tabla_corr.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer_num());
        Tabla_corr.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer_num());
        Tabla_corr.getColumnModel().getColumn(9).setCellRenderer(new DoubleRenderer_num_1());
        Tabla_corr.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer());
        Tabla_corr.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tabla_corr.getColumnModel().getColumn(7).setCellRenderer(new DoubleRenderer());
        Tabla_corr.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());

        Tabla_paros.getColumnModel().getColumn(0).setPreferredWidth(5);
        Tabla_paros.getColumnModel().getColumn(1).setPreferredWidth(80);
        Tabla_paros.getColumnModel().getColumn(2).setPreferredWidth(80);
        Tabla_paros.getColumnModel().getColumn(3).setPreferredWidth(250);
        Tabla_paros.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabla_paros.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());

        Tabla_conver.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabla_conver.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer_num());
        Tabla_conver.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer_num());
        Tabla_conver.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer_num());
        Tabla_conver.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer_num());
        Tabla_conver.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_num());
        Tabla_conver.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer_num());
        Tabla_conver.getColumnModel().getColumn(7).setCellRenderer(new DoubleRenderer());
        Tabla_conver.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tabla_conver.getColumnModel().getColumn(9).setCellRenderer(new DoubleRenderer());

        Tabla_consumos.getColumnModel().getColumn(0).setPreferredWidth(250);
        Tabla_consumos.getColumnModel().getColumn(3).setPreferredWidth(110);
        Tabla_consumos.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabla_consumos.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer_num_2_moneda());
        Tabla_consumos.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer_num_2_moneda());

        Tabla_merma.getColumnModel().getColumn(0).setPreferredWidth(150);
        Tabla_merma.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer_num());
        Tabla_merma.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer_num());

    }
    public void limpiatabla(){
        modelot_corr.setNumRows(0);
        modelot_conver.setNumRows(0);
        modelot_paros.setNumRows(0);
        modelot_consumos.setNumRows(0);
        modelot_merma.setNumRows(0);
    }
    public class DoubleRenderer extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                if(text.equals("null"))
                    text="";

                rend.setText( text );
                return rend;
            }
    }
    public class DoubleRenderer_num extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                if(text.equals("null"))
                    text="0";
                text=estandarentero.format(Double.parseDouble(text));
                rend.setText( text );
                return rend;
            }
    }
    public class DoubleRenderer_num_1 extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                if(text.equals("null"))
                    text="0";
                text=estandar1decimal.format(Double.parseDouble(text));
                rend.setText( text );
                return rend;
            }
    }
    public class DoubleRenderer_num_2_moneda extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                if(text.equals("null"))
                    text="0";
                text=moneda2decimales.format(Double.parseDouble(text));
                rend.setText( text );
                return rend;
            }
    }
    public void maquinas(){

        //carga los almacenes de papel
        rs0=null;
        try{
            String senSQL="SELECT * FROM maquinas ORDER BY id_maquina";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                String maq_t=""+rs0.getString("clave");
                combo_paros_maq.addItem(maq_t);
                if(!maq_t.toUpperCase().equals("CORR"))
                    combo_conver_maq.addItem(maq_t);
            }
            if(rs0!=null) { rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA CARGA DE ALMACENES\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    public void todos_datos(){
        this.setTitle("::: PLANEACION :::");
        conta.busca_fechascliente busca_fechascliente = new busca_fechascliente(null,true,conn,"");
        busca_fechascliente.setLocationRelativeTo(this);
        busca_fechascliente.setVisible(true);
        String estado=busca_fechascliente.getEstado();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if(estado.equals("cancelar")){

        }else{
            String dinamicoSQL="";
            if(busca_fechascliente.getProveedor().equals("Buscar")){ /**verifica si buscas un solo proveedor o todos*/
                dinamicoSQL="id_clientes='"+busca_fechascliente.getID()+"'";
            }
            //limpia la tabla
            limpiatabla();
            fechaini_guarda=fechainsertar.format(busca_fechascliente.getFechaini());
            fechafin_guarda=fechainsertar.format(busca_fechascliente.getFechafin());
            this.setTitle("PRODUCCION ( "+fechamediana.format(busca_fechascliente.getFechaini())+" al "+fechamediana.format(busca_fechascliente.getFechafin())+" )");
            sqlcli_guarda=dinamicoSQL;
            datos(fechainsertar.format(busca_fechascliente.getFechaini()),fechainsertar.format(busca_fechascliente.getFechafin()),dinamicoSQL);
        }
        busca_fechascliente=null;
    }
    public void datos(String fi,String ft,String sqlclavecli){        
        grafica_corr(fi,ft,sqlclavecli,"");
        combo_conver_maq.setSelectedIndex(1);
        grafica_paros(fi,ft,sqlclavecli,"","");
        grafica_consumos(fi,ft,sqlclavecli,"");
        grafica_merma(fi,ft,sqlclavecli,"");
    }
    public void grafica_corr(String fi,String ft,String sqlclavecli,String save){
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND xxxx."+sqlclavecli;
        }
        //resul en null
        modelot_corr.setNumRows(0);
        rs0=null;
        try{
            String senSQL="SELECT programa_corr_captura.fechareal, COUNT ( programa_corr_captura.fechareal ) AS cambiosfechas, SUM ( programa_corr_captura.prodml ) AS totalml, SUM ( programa_corr_captura.prodkg ) AS totalkg, SUM ( programa_corr_captura.minutosarreglo ) AS suma_arreglo, SUM ( programa_corr_captura.minutosprod ) AS suma_produccion, SUM (paros_suma.paroscap) AS suma_paros, SUM ( programa_corr.anchototal - programa_corr.anchoutil ) AS trimf FROM programa_corr_captura LEFT JOIN ( SELECT paros_captura.id_programa_corr_captura, SUM (paros_captura.minutos) AS paroscap FROM paros_captura WHERE paros_captura.estatus = 'Act' GROUP BY paros_captura.id_programa_corr_captura ) AS paros_suma ON programa_corr_captura.id_programa_corr_captura = paros_suma.id_programa_corr_captura LEFT JOIN programa_corr ON programa_corr_captura.id_programa_corr = programa_corr.id_programa_corr WHERE (( programa_corr_captura.fechareal >= '"+fi+" 00:00:00' AND programa_corr_captura.fechareal <= '"+ft+" 23:59:59' ) AND programa_corr_captura.estatus <> 'Can' ) GROUP BY programa_corr_captura.fechareal ORDER BY programa_corr_captura.fechareal;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double arreglo=rs0.getDouble("suma_arreglo");
                Double paros=rs0.getDouble("suma_paros");
                Double prod=rs0.getDouble("suma_produccion");
                String cadena_arreglo=horasminutos.format(arreglo/60)+":"+horasminutos.format(arreglo%60);
                String cadena_prod=horasminutos.format(prod/60)+":"+horasminutos.format(prod%60);
                String cadena_paros=horasminutos.format(paros/60)+":"+horasminutos.format(paros%60);
                String cadena_total=horasminutos.format((arreglo+prod+paros)/60)+":"+horasminutos.format((arreglo+prod+paros)%60);
                Double ml_f=rs0.getDouble("totalml");
                Date fecha_f=rs0.getDate("fechareal");
                //agrega el datset
                dataset.addValue(Double.parseDouble(fijo0decimales.format(ml_f)),"ml" , fechamuycorta.format(fecha_f));
                Double cambios_f=rs0.getDouble("cambiosfechas");
                Object datos[]={fechamediana.format(fecha_f),ml_f,rs0.getDouble("totalkg"),cambios_f,ml_f/prod,cadena_arreglo,cadena_prod,cadena_paros,cadena_total,rs0.getDouble("trimf")/cambios_f};
                modelot_corr.addRow(datos);

                /*if(!sqlclavecli.equals("")){
                    this.setTitle("::: Planeacion ("+rs0.getString("nombrecli")+") :::");
                }*/
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        final JFreeChart chart_corr = ChartFactory.createLineChart(
            "",         // chart title
            "Fecha",               // domain axis label
            "Metros Lineales",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );
        
        CategoryPlot plot = chart_corr.getCategoryPlot();
        plot.setNoDataMessage("No hay datos");
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        plot.setRangeGridlinePaint(java.awt.Color.LIGHT_GRAY);
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseItemLabelFont(font8plain);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.12);
        rangeAxis.setTickLabelFont(font9plain);
        CategoryAxis domainAxisrep = plot.getDomainAxis();
        domainAxisrep.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        domainAxisrep.setTickLabelFont(font9plain);

        LegendTitle leg=chart_corr.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setItemFont(font10plain);

        /*renderer.setSeriesStroke(
            0, new BasicStroke(2.0f,
                               BasicStroke.CAP_ROUND,
                               BasicStroke.JOIN_ROUND,
                               1.0f,
                               new float[] {10.0f, 6.0f},
                               0.0f)
        );*/
        
        //genera la grafica en el panel
        ChartPanel Panel_horas = new ChartPanel(chart_corr);
        frame_corr.setContentPane(Panel_horas);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_corr.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_corr, 950,500);
                    JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                }

             }catch (IOException e){ JOptionPane.showMessageDialog(this,"NO SE PUDO GENERAR LA IMAGEN"+e.getMessage(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);    }
        }

    }
    public void grafica_conver(String fi,String ft,String sqlclavecli,String save,String sqlmaq){

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND xxxx."+sqlclavecli;
        }
        //resul en null
        modelot_conver.setNumRows(0);
        String unicapacidad="";
        rs0=null;
        try{
            String senSQL="SELECT conversion_captura.fechareal, COUNT ( conversion_captura.clavearticulo ) AS cambios, SUM ( conversion_captura.minutosarreglo ) AS suma_arreglo, 	SUM ( conversion_captura.minutosprod ) AS suma_produccion, 	SUM (paros_suma.paroscap) AS suma_paros, SUM ( conversion_captura.cantidad ) AS cantidad, SUM ( conversion_captura.cantidadpiezas 	) AS cantidadpiezas, 	SUM ( articulos.kg * conversion_captura.cantidadpiezas ) AS kg, SUM ( articulos.m2 * conversion_captura.cantidadpiezas ) AS m2, MAX (maquinas.unidadcapacidad) AS unidadcapacidad FROM 	( conversion_captura LEFT JOIN maquinas ON conversion_captura.clave = maquinas.clave ) LEFT JOIN articulos ON conversion_captura.clavearticulo = articulos.clavearticulo LEFT JOIN (SELECT paros_captura.id_conversion_captura, SUM (paros_captura.minutos) AS paroscap FROM paros_captura GROUP BY paros_captura.id_conversion_captura) AS paros_suma ON conversion_captura.id_conversion_captura = paros_suma.id_conversion_captura WHERE ( conversion_captura.estatus <> 'Can' AND conversion_captura.clave = '"+sqlmaq+"' AND ( conversion_captura.fechareal >= '"+fi+" 00:00:00' AND conversion_captura.fechareal <= '"+ft+" 23:59:59' ) ) GROUP BY conversion_captura.fechareal ORDER BY 	conversion_captura.fechareal;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double arreglo=rs0.getDouble("suma_arreglo");
                Double paros=rs0.getDouble("suma_paros");
                Double prod=rs0.getDouble("suma_produccion");
                String cadena_arreglo=horasminutos.format(arreglo/60)+":"+horasminutos.format(arreglo%60);
                String cadena_prod=horasminutos.format((prod)/60)+":"+horasminutos.format((prod)%60);
                String cadena_paros=horasminutos.format(paros/60)+":"+horasminutos.format(paros%60);
                String cadena_total=horasminutos.format((arreglo+prod+paros)/60)+":"+horasminutos.format((arreglo+prod+paros)%60);
                Double cantidad_f=rs0.getDouble("cantidad");
                Date fecha_f=rs0.getDate("fechareal");
                unicapacidad=rs0.getString("unidadcapacidad");
                //agrega el datset
                dataset.addValue(Double.parseDouble(fijo0decimales.format(cantidad_f)),unicapacidad,fechamuycorta.format(fecha_f));
                Double cambios_f=rs0.getDouble("cambios");
                //Object datos[]={fechamediana.format(fecha_f),cantidad_f,rs0.getDouble("totalkg"),cambios_f,cantidad_f/prod,cadena_arreglo,cadena_prod,cadena_total,rs0.getDouble("trimf")/cambios_f};
                Object datos[]={fechamediana.format(fecha_f),cantidad_f,rs0.getDouble("cantidadpiezas"),rs0.getDouble("kg"),cambios_f,arreglo/cambios_f,cantidad_f/prod,cadena_arreglo,cadena_prod,cadena_paros, cadena_total};
                modelot_conver.addRow(datos);

                /*if(!sqlclavecli.equals("")){
                    this.setTitle("::: Planeacion ("+rs0.getString("nombrecli")+") :::");
                }*/
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        final JFreeChart chart_conver = ChartFactory.createLineChart(
            "",         // chart title
            "Fecha",               // domain axis label
            unicapacidad,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        CategoryPlot plot = chart_conver.getCategoryPlot();
        plot.setNoDataMessage("No hay datos");
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        plot.setRangeGridlinePaint(java.awt.Color.LIGHT_GRAY);
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseItemLabelFont(font8plain);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.12);
        rangeAxis.setTickLabelFont(font9plain);
        CategoryAxis domainAxisrep = plot.getDomainAxis();
        domainAxisrep.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        domainAxisrep.setTickLabelFont(font9plain);

        LegendTitle leg=chart_conver.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setItemFont(font10plain);

        //genera la grafica en el panel
        ChartPanel Panel_horas = new ChartPanel(chart_conver);
        frame_conver.setContentPane(Panel_horas);
        frame_conver.setTitle("PRODUCCIÓN POR DIAS "+sqlmaq);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_conv.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_conver, 950,500);
                    JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                }
             }catch (IOException e){ JOptionPane.showMessageDialog(this,"NO SE PUDO GENERAR LA IMAGEN"+e.getMessage(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);    }
        }

    }
    public void grafica_paros(String fi,String ft,String sqlclavecli,String save,String sqlmaqparos){

        DefaultPieDataset dataset = new DefaultPieDataset();//arreglo de datos
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND xxxx."+sqlclavecli;
        }
        String sqlmaq="";
        if(!sqlmaqparos.equals("")){
            sqlmaq=" AND paros_captura."+sqlmaqparos;
        }
        //resul en null
        modelot_paros.setNumRows(0);
        rs0=null;
        try{
            String senSQL="SELECT paros_captura.id_paro, count(paros_captura.id_paro) as cuenta_paros,sum(minutos) as suma_minutos,max(descripcion) as descripcion FROM (paros_captura LEFT JOIN paros ON paros_captura.id_paro=paros.id_paro) WHERE (estatus<>'Can' AND paros_captura.id_paro<>'88' AND (fechareal>='"+fi+" 00:00:00' AND fechareal<='"+ft+" 23:59:59')"+sqlmaq+") GROUP BY paros_captura.id_paro ORDER BY sum(minutos) DESC;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double minutos=rs0.getDouble("suma_minutos");
                String cadena_minutos=horasminutos.format(minutos/60)+":"+horasminutos.format(minutos%60);
                String cadena=rs0.getString("descripcion");
                dataset.setValue(cadena, minutos);
                Object datos[]={minutos,rs0.getInt("cuenta_paros"),cadena_minutos,cadena};
                modelot_paros.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        JFreeChart chart_paros = ChartFactory.createPieChart("", dataset,true,true,false);
        PiePlot plot = (PiePlot) chart_paros.getPlot();
        plot.setLabelFont(font11plain);
        plot.setNoDataMessage("No hay datos");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", estandarentero,porcentaje2decimal));
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        LegendTitle leg=chart_paros.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setBackgroundPaint(new java.awt.Color(233,233,233));
        leg.setItemFont(font11plain);
        chart_paros.getTitle().setFont(font12bold);
        chart_paros.setBackgroundPaint(java.awt.Color.WHITE);
        //genera la grafica en el panel
        ChartPanel Panel_participacion = new ChartPanel(chart_paros);
        frame_paros.setContentPane(Panel_participacion);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_paros.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_paros, 800,500);
                    JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                }

             }catch (IOException e){ JOptionPane.showMessageDialog(this,"NO SE PUDO GENERAR LA IMAGEN"+e.getMessage(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);    }
        }

    }
    public void grafica_consumos(String fi,String ft,String sqlclavecli,String save){

        DefaultPieDataset dataset = new DefaultPieDataset();//arreglo de datos
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND xxxx."+sqlclavecli;
        }
        //resul en null
        modelot_consumos.setNumRows(0);
        rs0=null;
        try{
            String senSQL="SELECT nombre,count(id_consumos_producto) as no_rollos,sum(cantidad) as cantidad_kg,sum(importe) as importe_kg FROM (SELECT entradas_productos.remision,proveedores.nombre,consumos_productos.*,productos.descripcion,productos.unidad,entradas_productos_detalle.preciounitario,(entradas_productos_detalle.preciounitario*consumos_productos.cantidad) as importe FROM (consumos_productos LEFT JOIN productos ON consumos_productos.clave_producto=productos.clave) LEFT JOIN (entradas_productos_detalle LEFT JOIN (entradas_productos LEFT JOIN proveedores ON entradas_productos.id_proveedor=proveedores.id_proveedor) ON entradas_productos_detalle.id_entrada_producto=entradas_productos.id_entrada_producto) ON consumos_productos.id_entrada_producto_detalle=entradas_productos_detalle.id_entrada_producto_detalle WHERE (consumos_productos.estatus<>'Can' AND (consumos_productos.fecha>='"+fi+" 00:00:00' AND consumos_productos.fecha<='"+ft+" 23:59:59')) ORDER BY consumos_productos.fecha) as consumos_detalle GROUP BY nombre";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad_kg=rs0.getDouble("cantidad_kg");
                Double importe_kg=rs0.getDouble("importe_kg");
                String proveedor_t=rs0.getString("nombre");
                dataset.setValue(proveedor_t, cantidad_kg);
                Object datos[]={proveedor_t,rs0.getInt("no_rollos"),cantidad_kg,importe_kg,importe_kg/cantidad_kg};
                modelot_consumos.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        JFreeChart chart_consumos = ChartFactory.createPieChart("", dataset,true,true,false);
        PiePlot plot = (PiePlot) chart_consumos.getPlot();
        plot.setLabelFont(font11plain);
        plot.setNoDataMessage("No hay datos");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", estandarentero,porcentaje2decimal));
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        LegendTitle leg=chart_consumos.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setBackgroundPaint(new java.awt.Color(233,233,233));
        leg.setItemFont(font11plain);
        chart_consumos.getTitle().setFont(font12bold);
        chart_consumos.setBackgroundPaint(java.awt.Color.WHITE);
        //genera la grafica en el panel
        ChartPanel Panel_participacion = new ChartPanel(chart_consumos);
        frame_consumos.setContentPane(Panel_participacion);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_consumos.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_consumos, 800,500);
                    JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                }

             }catch (IOException e){ JOptionPane.showMessageDialog(this,"NO SE PUDO GENERAR LA IMAGEN"+e.getMessage(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);    }
        }

    }
    public void grafica_merma(String fi,String ft,String sqlclavecli,String save){

        DefaultPieDataset dataset = new DefaultPieDataset();//arreglo de datos
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND xxxx."+sqlclavecli;
        }
        //resul en null
        modelot_merma.setNumRows(0);
        rs0=null;
        try{
            String senSQL="SELECT sum((((anchototal-anchoutil)/100)*programa_corr_captura.prodml)*resistencias.pesom2) as mermakgtrim,sum((((anchototal-anchoutil)/100)*programa_corr_captura.prodml)*resistencias.pesom2real) as mermakgtrim_real FROM (programa_corr_captura LEFT JOIN (programa_corr LEFT JOIN resistencias ON programa_corr.claveresistencia=resistencias.clave) ON programa_corr_captura.id_programa_corr=programa_corr.id_programa_corr) WHERE (programa_corr_captura.estatus<>'Can' AND (programa_corr_captura.fechareal>='"+fi+" 00:00:00' AND programa_corr_captura.fechareal<='"+ft+" 23:59:59'))";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                String titulo_p="Trim Corrugadora";
                Double merma_kg=rs0.getDouble("mermakgtrim");
                Double merma_pzas=0.0;
                dataset.setValue(titulo_p, merma_kg);
                Object datos[]={titulo_p,merma_kg,merma_pzas};
                modelot_merma.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        //merma procesos
        rs0=null;
        try{
            String senSQL="SELECT conversion_captura.clave,sum(conversion_captura.cantidadmalaspiezas) as mermapiezas,sum(articulos.kg * conversion_captura.cantidadmalaspiezas) as mermakg,sum(articulos.kgreal * conversion_captura.cantidadmalaspiezas) as mermakg_real,sum(articulos.m2 * conversion_captura.cantidadmalaspiezas) as mermam2 FROM (conversion_captura LEFT JOIN maquinas ON conversion_captura.clave=maquinas.clave) LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.estatus<>'Can' AND (conversion_captura.fechareal>='"+fi+" 00:00:00' AND conversion_captura.fechareal<='"+ft+" 23:59:59')) GROUP BY conversion_captura.clave ORDER BY max(maquinas.id_maquina);";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                String titulo_p=rs0.getString("clave");
                Double merma_kg=rs0.getDouble("mermakg");
                Double merma_pzas=rs0.getDouble("mermapiezas");
                if(merma_pzas>0.0){
                    dataset.setValue(titulo_p, merma_kg);
                    Object datos[]={titulo_p,merma_kg,merma_pzas};
                    modelot_merma.addRow(datos);
                }
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        JFreeChart chart_merma = ChartFactory.createPieChart("", dataset,true,true,false);
        PiePlot plot = (PiePlot) chart_merma.getPlot();
        plot.setLabelFont(font11plain);
        plot.setNoDataMessage("No hay datos");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", estandarentero,porcentaje2decimal));
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        LegendTitle leg=chart_merma.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setBackgroundPaint(new java.awt.Color(233,233,233));
        leg.setItemFont(font11plain);
        chart_merma.getTitle().setFont(font12bold);
        chart_merma.setBackgroundPaint(java.awt.Color.WHITE);
        //genera la grafica en el panel
        ChartPanel Panel_participacion = new ChartPanel(chart_merma);
        frame_merma.setContentPane(Panel_participacion);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_merma.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_merma, 800,500);
                    JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                }

             }catch (IOException e){ JOptionPane.showMessageDialog(this,"NO SE PUDO GENERAR LA IMAGEN"+e.getMessage(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);    }
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

        guarda_file = new javax.swing.JFileChooser();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnactualizar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panel_consumos = new javax.swing.JPanel();
        frame_consumos = new javax.swing.JInternalFrame();
        jScrollPane5 = new javax.swing.JScrollPane();
        Tabla_consumos = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        buscar_consumos = new javax.swing.JTextField();
        exportar_consumos = new javax.swing.JButton();
        txt_res_consumos = new javax.swing.JTextField();
        ima_consumos = new javax.swing.JButton();
        panel_corr = new javax.swing.JPanel();
        frame_corr = new javax.swing.JInternalFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla_corr = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        buscar_corr = new javax.swing.JTextField();
        exportar_corr = new javax.swing.JButton();
        txt_res_corr = new javax.swing.JTextField();
        ima_corr = new javax.swing.JButton();
        panel_conver = new javax.swing.JPanel();
        frame_conver = new javax.swing.JInternalFrame();
        jScrollPane4 = new javax.swing.JScrollPane();
        Tabla_conver = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        buscar_conver = new javax.swing.JTextField();
        exportar_conver = new javax.swing.JButton();
        txt_res_conver = new javax.swing.JTextField();
        ima_conver = new javax.swing.JButton();
        combo_conver_maq = new javax.swing.JComboBox();
        panel_merma = new javax.swing.JPanel();
        frame_merma = new javax.swing.JInternalFrame();
        jScrollPane6 = new javax.swing.JScrollPane();
        Tabla_merma = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        buscar_merma = new javax.swing.JTextField();
        exportar_merma = new javax.swing.JButton();
        txt_res_merma = new javax.swing.JTextField();
        ima_merma = new javax.swing.JButton();
        panel_paros = new javax.swing.JPanel();
        frame_paros = new javax.swing.JInternalFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tabla_paros = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        buscar_paros = new javax.swing.JTextField();
        exportar_paros = new javax.swing.JButton();
        txt_res_paros = new javax.swing.JTextField();
        ima_paros = new javax.swing.JButton();
        combo_paros_maq = new javax.swing.JComboBox();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(radiografia_produccion.class);
        guarda_file.setDialogTitle(resourceMap.getString("guarda_file.dialogTitle")); // NOI18N
        guarda_file.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        guarda_file.setName("guarda_file"); // NOI18N

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

        jToolBar1.setBackground(resourceMap.getColor("jToolBar1.background")); // NOI18N
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        btnactualizar.setIcon(resourceMap.getIcon("btnactualizar.icon")); // NOI18N
        btnactualizar.setText(resourceMap.getString("btnactualizar.text")); // NOI18N
        btnactualizar.setToolTipText(resourceMap.getString("btnactualizar.toolTipText")); // NOI18N
        btnactualizar.setFocusable(false);
        btnactualizar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnactualizar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnactualizar.setMaximumSize(new java.awt.Dimension(70, 48));
        btnactualizar.setMinimumSize(new java.awt.Dimension(70, 48));
        btnactualizar.setName("btnactualizar"); // NOI18N
        btnactualizar.setPreferredSize(new java.awt.Dimension(70, 48));
        btnactualizar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnactualizar);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(620, 48));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 678, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        jToolBar1.add(jPanel1);

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        panel_consumos.setName("panel_consumos"); // NOI18N

        frame_consumos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_consumos.setTitle(resourceMap.getString("frame_consumos.title")); // NOI18N
        frame_consumos.setFrameIcon(null);
        frame_consumos.setName("frame_consumos"); // NOI18N
        frame_consumos.setVisible(true);

        javax.swing.GroupLayout frame_consumosLayout = new javax.swing.GroupLayout(frame_consumos.getContentPane());
        frame_consumos.getContentPane().setLayout(frame_consumosLayout);
        frame_consumosLayout.setHorizontalGroup(
            frame_consumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_consumosLayout.setVerticalGroup(
            frame_consumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        Tabla_consumos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Proveedor", "No. Rollos", "KG", "Importe", "Avg P.K."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla_consumos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_consumos.setName("Tabla_consumos"); // NOI18N
        Tabla_consumos.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(Tabla_consumos);

        jPanel8.setBackground(resourceMap.getColor("jPanel8.background")); // NOI18N
        jPanel8.setName("jPanel8"); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setIcon(resourceMap.getIcon("jLabel6.icon")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        buscar_consumos.setName("buscar_consumos"); // NOI18N
        buscar_consumos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_consumosFocusGained(evt);
            }
        });
        buscar_consumos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_consumosKeyReleased(evt);
            }
        });

        exportar_consumos.setIcon(resourceMap.getIcon("exportar_consumos.icon")); // NOI18N
        exportar_consumos.setText(resourceMap.getString("exportar_consumos.text")); // NOI18N
        exportar_consumos.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_consumos.setName("exportar_consumos"); // NOI18N
        exportar_consumos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_consumosActionPerformed(evt);
            }
        });

        txt_res_consumos.setFont(resourceMap.getFont("txt_res_consumos.font")); // NOI18N
        txt_res_consumos.setForeground(resourceMap.getColor("txt_res_consumos.foreground")); // NOI18N
        txt_res_consumos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_consumos.setText(resourceMap.getString("txt_res_consumos.text")); // NOI18N
        txt_res_consumos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_consumos.setName("txt_res_consumos"); // NOI18N

        ima_consumos.setIcon(resourceMap.getIcon("ima_consumos.icon")); // NOI18N
        ima_consumos.setText(resourceMap.getString("ima_consumos.text")); // NOI18N
        ima_consumos.setMargin(new java.awt.Insets(0, 4, 0, 4));
        ima_consumos.setMaximumSize(new java.awt.Dimension(111, 27));
        ima_consumos.setMinimumSize(new java.awt.Dimension(111, 27));
        ima_consumos.setName("ima_consumos"); // NOI18N
        ima_consumos.setPreferredSize(new java.awt.Dimension(111, 27));
        ima_consumos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ima_consumosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_consumos, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_consumos, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(ima_consumos, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_consumos, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(txt_res_consumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_consumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_consumos)
                        .addComponent(ima_consumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_consumosLayout = new javax.swing.GroupLayout(panel_consumos);
        panel_consumos.setLayout(panel_consumosLayout);
        panel_consumosLayout.setHorizontalGroup(
            panel_consumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_consumos)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        panel_consumosLayout.setVerticalGroup(
            panel_consumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_consumosLayout.createSequentialGroup()
                .addComponent(frame_consumos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("panel_consumos.TabConstraints.tabTitle"), panel_consumos); // NOI18N

        panel_corr.setName("panel_corr"); // NOI18N

        frame_corr.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_corr.setTitle(resourceMap.getString("frame_corr.title")); // NOI18N
        frame_corr.setFrameIcon(resourceMap.getIcon("frame_corr.frameIcon")); // NOI18N
        frame_corr.setName("frame_corr"); // NOI18N
        frame_corr.setVisible(true);

        javax.swing.GroupLayout frame_corrLayout = new javax.swing.GroupLayout(frame_corr.getContentPane());
        frame_corr.getContentPane().setLayout(frame_corrLayout);
        frame_corrLayout.setHorizontalGroup(
            frame_corrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_corrLayout.setVerticalGroup(
            frame_corrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        Tabla_corr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "ML", "KG", "Cambios", "Avg_vel.", "Total Arreglo", "Total Produccion", "Total Paros", "Total Tiempo", "Trim (cm)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla_corr.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_corr.setName("Tabla_corr"); // NOI18N
        Tabla_corr.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(Tabla_corr);
        Tabla_corr.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title0")); // NOI18N
        Tabla_corr.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title1")); // NOI18N
        Tabla_corr.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title2")); // NOI18N
        Tabla_corr.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title3")); // NOI18N
        Tabla_corr.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title4")); // NOI18N
        Tabla_corr.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title5")); // NOI18N
        Tabla_corr.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title6")); // NOI18N
        Tabla_corr.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title9")); // NOI18N
        Tabla_corr.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title7")); // NOI18N
        Tabla_corr.getColumnModel().getColumn(9).setHeaderValue(resourceMap.getString("Tabla_corr.columnModel.title8")); // NOI18N

        jPanel5.setBackground(resourceMap.getColor("jPanel5.background")); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(resourceMap.getIcon("jLabel3.icon")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        buscar_corr.setName("buscar_corr"); // NOI18N
        buscar_corr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_corrFocusGained(evt);
            }
        });
        buscar_corr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_corrKeyReleased(evt);
            }
        });

        exportar_corr.setIcon(resourceMap.getIcon("exportar_corr.icon")); // NOI18N
        exportar_corr.setText(resourceMap.getString("exportar_corr.text")); // NOI18N
        exportar_corr.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_corr.setName("exportar_corr"); // NOI18N
        exportar_corr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_corrActionPerformed(evt);
            }
        });

        txt_res_corr.setFont(resourceMap.getFont("txt_res_corr.font")); // NOI18N
        txt_res_corr.setForeground(resourceMap.getColor("txt_res_corr.foreground")); // NOI18N
        txt_res_corr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_corr.setText(resourceMap.getString("txt_res_corr.text")); // NOI18N
        txt_res_corr.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_corr.setName("txt_res_corr"); // NOI18N

        ima_corr.setIcon(resourceMap.getIcon("ima_corr.icon")); // NOI18N
        ima_corr.setText(resourceMap.getString("ima_corr.text")); // NOI18N
        ima_corr.setMargin(new java.awt.Insets(0, 4, 0, 4));
        ima_corr.setMaximumSize(new java.awt.Dimension(111, 27));
        ima_corr.setMinimumSize(new java.awt.Dimension(111, 27));
        ima_corr.setName("ima_corr"); // NOI18N
        ima_corr.setPreferredSize(new java.awt.Dimension(111, 27));
        ima_corr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ima_corrActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_corr, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_corr, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(ima_corr, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_corr, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(txt_res_corr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_corr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_corr)
                        .addComponent(ima_corr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_corrLayout = new javax.swing.GroupLayout(panel_corr);
        panel_corr.setLayout(panel_corrLayout);
        panel_corrLayout.setHorizontalGroup(
            panel_corrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_corr)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        panel_corrLayout.setVerticalGroup(
            panel_corrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_corrLayout.createSequentialGroup()
                .addComponent(frame_corr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("panel_corr.TabConstraints.tabTitle"), panel_corr); // NOI18N

        panel_conver.setName("panel_conver"); // NOI18N

        frame_conver.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_conver.setTitle(resourceMap.getString("frame_conver.title")); // NOI18N
        frame_conver.setFrameIcon(null);
        frame_conver.setName("frame_conver"); // NOI18N
        frame_conver.setVisible(true);

        javax.swing.GroupLayout frame_converLayout = new javax.swing.GroupLayout(frame_conver.getContentPane());
        frame_conver.getContentPane().setLayout(frame_converLayout);
        frame_converLayout.setHorizontalGroup(
            frame_converLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_converLayout.setVerticalGroup(
            frame_converLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        Tabla_conver.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "Cantidad", "Piezas", "KG", "Cambios", "Avg_arreglo", "Avg_vel", "Total Arreglo", "Total Produccion", "Total Paros", "Total Tiempo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla_conver.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_conver.setName("Tabla_conver"); // NOI18N
        Tabla_conver.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(Tabla_conver);
        Tabla_conver.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title0")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title1")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title2")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title3")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title4")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title5")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title6")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title7")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title8")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(9).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title10")); // NOI18N
        Tabla_conver.getColumnModel().getColumn(10).setHeaderValue(resourceMap.getString("Tabla_conver.columnModel.title9")); // NOI18N

        jPanel7.setBackground(resourceMap.getColor("jPanel7.background")); // NOI18N
        jPanel7.setName("jPanel7"); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setIcon(resourceMap.getIcon("jLabel5.icon")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        buscar_conver.setName("buscar_conver"); // NOI18N
        buscar_conver.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_converFocusGained(evt);
            }
        });
        buscar_conver.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_converKeyReleased(evt);
            }
        });

        exportar_conver.setIcon(resourceMap.getIcon("exportar_conver.icon")); // NOI18N
        exportar_conver.setText(resourceMap.getString("exportar_conver.text")); // NOI18N
        exportar_conver.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_conver.setName("exportar_conver"); // NOI18N
        exportar_conver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_converActionPerformed(evt);
            }
        });

        txt_res_conver.setFont(resourceMap.getFont("txt_res_conver.font")); // NOI18N
        txt_res_conver.setForeground(resourceMap.getColor("txt_res_conver.foreground")); // NOI18N
        txt_res_conver.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_conver.setText(resourceMap.getString("txt_res_conver.text")); // NOI18N
        txt_res_conver.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_conver.setName("txt_res_conver"); // NOI18N

        ima_conver.setIcon(resourceMap.getIcon("ima_conver.icon")); // NOI18N
        ima_conver.setText(resourceMap.getString("ima_conver.text")); // NOI18N
        ima_conver.setMargin(new java.awt.Insets(0, 4, 0, 4));
        ima_conver.setMaximumSize(new java.awt.Dimension(111, 27));
        ima_conver.setMinimumSize(new java.awt.Dimension(111, 27));
        ima_conver.setName("ima_conver"); // NOI18N
        ima_conver.setPreferredSize(new java.awt.Dimension(111, 27));
        ima_conver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ima_converActionPerformed(evt);
            }
        });

        combo_conver_maq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos" }));
        combo_conver_maq.setName("combo_conver_maq"); // NOI18N
        combo_conver_maq.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_conver_maqItemStateChanged(evt);
            }
        });
        combo_conver_maq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_conver_maqActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_conver, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_conver, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                        .addComponent(combo_conver_maq, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ima_conver, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_conver, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(txt_res_conver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_conver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_conver)
                        .addComponent(ima_conver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(combo_conver_maq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_converLayout = new javax.swing.GroupLayout(panel_conver);
        panel_conver.setLayout(panel_converLayout);
        panel_converLayout.setHorizontalGroup(
            panel_converLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_conver)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        panel_converLayout.setVerticalGroup(
            panel_converLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_converLayout.createSequentialGroup()
                .addComponent(frame_conver)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("panel_conver.TabConstraints.tabTitle"), panel_conver); // NOI18N

        panel_merma.setName("panel_merma"); // NOI18N

        frame_merma.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_merma.setTitle(resourceMap.getString("frame_merma.title")); // NOI18N
        frame_merma.setFrameIcon(null);
        frame_merma.setName("frame_merma"); // NOI18N
        frame_merma.setVisible(true);

        javax.swing.GroupLayout frame_mermaLayout = new javax.swing.GroupLayout(frame_merma.getContentPane());
        frame_merma.getContentPane().setLayout(frame_mermaLayout);
        frame_mermaLayout.setHorizontalGroup(
            frame_mermaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_mermaLayout.setVerticalGroup(
            frame_mermaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        Tabla_merma.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Proceso", "KG", "Piezas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla_merma.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_merma.setName("Tabla_merma"); // NOI18N
        Tabla_merma.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(Tabla_merma);

        jPanel9.setBackground(resourceMap.getColor("jPanel9.background")); // NOI18N
        jPanel9.setName("jPanel9"); // NOI18N

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(resourceMap.getIcon("jLabel7.icon")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        buscar_merma.setName("buscar_merma"); // NOI18N
        buscar_merma.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_mermaFocusGained(evt);
            }
        });
        buscar_merma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_mermaKeyReleased(evt);
            }
        });

        exportar_merma.setIcon(resourceMap.getIcon("exportar_merma.icon")); // NOI18N
        exportar_merma.setText(resourceMap.getString("exportar_merma.text")); // NOI18N
        exportar_merma.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_merma.setName("exportar_merma"); // NOI18N
        exportar_merma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_mermaActionPerformed(evt);
            }
        });

        txt_res_merma.setFont(resourceMap.getFont("txt_res_merma.font")); // NOI18N
        txt_res_merma.setForeground(resourceMap.getColor("txt_res_merma.foreground")); // NOI18N
        txt_res_merma.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_merma.setText(resourceMap.getString("txt_res_merma.text")); // NOI18N
        txt_res_merma.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_merma.setName("txt_res_merma"); // NOI18N

        ima_merma.setIcon(resourceMap.getIcon("ima_merma.icon")); // NOI18N
        ima_merma.setText(resourceMap.getString("ima_merma.text")); // NOI18N
        ima_merma.setMargin(new java.awt.Insets(0, 4, 0, 4));
        ima_merma.setMaximumSize(new java.awt.Dimension(111, 27));
        ima_merma.setMinimumSize(new java.awt.Dimension(111, 27));
        ima_merma.setName("ima_merma"); // NOI18N
        ima_merma.setPreferredSize(new java.awt.Dimension(111, 27));
        ima_merma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ima_mermaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_merma, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_merma, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(ima_merma, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_merma, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(txt_res_merma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_merma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_merma)
                        .addComponent(ima_merma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_mermaLayout = new javax.swing.GroupLayout(panel_merma);
        panel_merma.setLayout(panel_mermaLayout);
        panel_mermaLayout.setHorizontalGroup(
            panel_mermaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_merma)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        panel_mermaLayout.setVerticalGroup(
            panel_mermaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_mermaLayout.createSequentialGroup()
                .addComponent(frame_merma)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("panel_merma.TabConstraints.tabTitle"), panel_merma); // NOI18N

        panel_paros.setName("panel_paros"); // NOI18N

        frame_paros.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_paros.setTitle(resourceMap.getString("frame_paros.title")); // NOI18N
        frame_paros.setFrameIcon(null);
        frame_paros.setName("frame_paros"); // NOI18N
        frame_paros.setVisible(true);

        javax.swing.GroupLayout frame_parosLayout = new javax.swing.GroupLayout(frame_paros.getContentPane());
        frame_paros.getContentPane().setLayout(frame_parosLayout);
        frame_parosLayout.setHorizontalGroup(
            frame_parosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_parosLayout.setVerticalGroup(
            frame_parosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        Tabla_paros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Minutos", "Cantidad", "Horas", "Descripción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla_paros.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_paros.setName("Tabla_paros"); // NOI18N
        Tabla_paros.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(Tabla_paros);

        jPanel6.setBackground(resourceMap.getColor("jPanel6.background")); // NOI18N
        jPanel6.setName("jPanel6"); // NOI18N

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(resourceMap.getIcon("jLabel4.icon")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        buscar_paros.setName("buscar_paros"); // NOI18N
        buscar_paros.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_parosFocusGained(evt);
            }
        });
        buscar_paros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_parosKeyReleased(evt);
            }
        });

        exportar_paros.setIcon(resourceMap.getIcon("exportar_paros.icon")); // NOI18N
        exportar_paros.setText(resourceMap.getString("exportar_paros.text")); // NOI18N
        exportar_paros.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_paros.setName("exportar_paros"); // NOI18N
        exportar_paros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_parosActionPerformed(evt);
            }
        });

        txt_res_paros.setFont(resourceMap.getFont("txt_res_paros.font")); // NOI18N
        txt_res_paros.setForeground(resourceMap.getColor("txt_res_paros.foreground")); // NOI18N
        txt_res_paros.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_paros.setText(resourceMap.getString("txt_res_paros.text")); // NOI18N
        txt_res_paros.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_paros.setName("txt_res_paros"); // NOI18N

        ima_paros.setIcon(resourceMap.getIcon("ima_paros.icon")); // NOI18N
        ima_paros.setText(resourceMap.getString("ima_paros.text")); // NOI18N
        ima_paros.setMargin(new java.awt.Insets(0, 4, 0, 4));
        ima_paros.setMaximumSize(new java.awt.Dimension(111, 27));
        ima_paros.setMinimumSize(new java.awt.Dimension(111, 27));
        ima_paros.setName("ima_paros"); // NOI18N
        ima_paros.setPreferredSize(new java.awt.Dimension(111, 27));
        ima_paros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ima_parosActionPerformed(evt);
            }
        });

        combo_paros_maq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos" }));
        combo_paros_maq.setName("combo_paros_maq"); // NOI18N
        combo_paros_maq.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_paros_maqItemStateChanged(evt);
            }
        });
        combo_paros_maq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_paros_maqActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_paros, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_paros, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                        .addComponent(combo_paros_maq, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ima_paros, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_paros, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(txt_res_paros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_paros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_paros)
                        .addComponent(ima_paros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(combo_paros_maq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_parosLayout = new javax.swing.GroupLayout(panel_paros);
        panel_paros.setLayout(panel_parosLayout);
        panel_parosLayout.setHorizontalGroup(
            panel_parosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_paros)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        panel_parosLayout.setVerticalGroup(
            panel_parosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_parosLayout.createSequentialGroup()
                .addComponent(frame_paros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("panel_paros.TabConstraints.tabTitle"), panel_paros); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        // TODO add your handling code here:
        todos_datos();
}//GEN-LAST:event_btnactualizarActionPerformed

    private void buscar_corrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_corrFocusGained
        // TODO add your handling code here:
        buscar_corr.selectAll();
    }//GEN-LAST:event_buscar_corrFocusGained

    private void buscar_corrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_corrKeyReleased
        // TODO add your handling code here:
        if(buscar_corr.getText().equals("")){
            Tabla_corr.setRowSorter(null);
            buscar_corr.setText("");
            Tabla_corr.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_corr);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_corr.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_corr.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_corrKeyReleased

    private void exportar_corrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_corrActionPerformed
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Producción corrugadora ( "+fechamediana.format(fechainsertar.parse(fechaini_guarda))+" al "+fechamediana.format(fechainsertar.parse(fechafin_guarda))+" )", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_corr.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_corr.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_corr.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_corr.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_corr.getColumnCount()); j = j + 1) {
                    if (Tabla_corr.getValueAt(i, j) != null) {
                        if (Tabla_corr.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_corr.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_corr.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_corr.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_corr.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_corr.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_corr.getValueAt(i, j), arial10fdetalle));
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
        } catch (Exception exi) {
            JOptionPane.showMessageDialog(this, "ERROR AL EXPORTAR DATOS" + exi, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        //abrir el documento creado
        try {
            Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "NO SE PUEDE ABRIR EL ARCHIVO\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exportar_corrActionPerformed

    private void ima_corrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ima_corrActionPerformed
        // TODO add your handling code here:
        grafica_corr(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica");
    }//GEN-LAST:event_ima_corrActionPerformed

    private void buscar_parosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_parosFocusGained
        // TODO add your handling code here:
        buscar_paros.selectAll();
    }//GEN-LAST:event_buscar_parosFocusGained

    private void buscar_parosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_parosKeyReleased
        // TODO add your handling code here:
        if(buscar_paros.getText().equals("")){
            Tabla_paros.setRowSorter(null);
            buscar_paros.setText("");
            Tabla_paros.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_paros);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_paros.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_paros.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_parosKeyReleased

    private void exportar_parosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_parosActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/prod_paros.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Paros técnicos ( "+fechamediana.format(fechainsertar.parse(fechaini_guarda))+" al "+fechamediana.format(fechainsertar.parse(fechafin_guarda))+" )", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_paros.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_paros.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_paros.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_paros.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_paros.getColumnCount()); j = j + 1) {
                    if (Tabla_paros.getValueAt(i, j) != null) {
                        if (Tabla_paros.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_paros.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_paros.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_paros.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_paros.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_paros.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_paros.getValueAt(i, j), arial10fdetalle));
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
        } catch (Exception exi) {
            JOptionPane.showMessageDialog(this, "ERROR AL EXPORTAR DATOS" + exi, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        //abrir el documento creado
        try {
            Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "NO SE PUEDE ABRIR EL ARCHIVO\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exportar_parosActionPerformed

    private void ima_parosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ima_parosActionPerformed
        // TODO add your handling code here:
        String d=""+combo_paros_maq.getSelectedItem();
        if(d.equals("Todos")){
            grafica_paros(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica","");
        }else{
            grafica_paros(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica","clavemaquina='"+combo_paros_maq.getSelectedItem()+"'");
        }
    }//GEN-LAST:event_ima_parosActionPerformed

    private void combo_paros_maqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_paros_maqActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_combo_paros_maqActionPerformed

    private void combo_paros_maqItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_paros_maqItemStateChanged
        // TODO add your handling code here:
        String d=""+combo_paros_maq.getSelectedItem();
        if(d.equals("Todos")){
            grafica_paros(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"","");
        }else{
            grafica_paros(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"","clavemaquina='"+combo_paros_maq.getSelectedItem()+"'");
        }
    }//GEN-LAST:event_combo_paros_maqItemStateChanged

    private void buscar_converFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_converFocusGained
        // TODO add your handling code here:
        buscar_conver.selectAll();
    }//GEN-LAST:event_buscar_converFocusGained

    private void buscar_converKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_converKeyReleased
        // TODO add your handling code here:
        if(buscar_conver.getText().equals("")){
            Tabla_conver.setRowSorter(null);
            buscar_conver.setText("");
            Tabla_conver.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_conver);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_conver.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_conver.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_converKeyReleased

    private void exportar_converActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_converActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/prod_conver.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, frame_conver.getTitle()+" ( "+fechamediana.format(fechainsertar.parse(fechaini_guarda))+" al "+fechamediana.format(fechainsertar.parse(fechafin_guarda))+" )", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_conver.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_conver.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_conver.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_conver.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_conver.getColumnCount()); j = j + 1) {
                    if (Tabla_conver.getValueAt(i, j) != null) {
                        if (Tabla_conver.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_conver.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_conver.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_conver.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_conver.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_conver.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_conver.getValueAt(i, j), arial10fdetalle));
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
        } catch (Exception exi) {
            JOptionPane.showMessageDialog(this, "ERROR AL EXPORTAR DATOS" + exi, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        //abrir el documento creado
        try {
            Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "NO SE PUEDE ABRIR EL ARCHIVO\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exportar_converActionPerformed

    private void ima_converActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ima_converActionPerformed
        // TODO add your handling code here:
        String d=""+combo_conver_maq.getSelectedItem();
        if(d.equals("Todos")){

        }else{
            grafica_conver(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica",""+combo_conver_maq.getSelectedItem());
        }
    }//GEN-LAST:event_ima_converActionPerformed

    private void combo_conver_maqItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_conver_maqItemStateChanged
        // TODO add your handling code here:
        String d=""+combo_conver_maq.getSelectedItem();
        if(d.equals("Todos")){

        }else{
            grafica_conver(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"",""+combo_conver_maq.getSelectedItem());
        }
    }//GEN-LAST:event_combo_conver_maqItemStateChanged

    private void combo_conver_maqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_conver_maqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_conver_maqActionPerformed

    private void buscar_consumosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_consumosFocusGained
        // TODO add your handling code here:
        buscar_consumos.selectAll();
    }//GEN-LAST:event_buscar_consumosFocusGained

    private void buscar_consumosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_consumosKeyReleased
        // TODO add your handling code here:
        if(buscar_consumos.getText().equals("")){
            Tabla_consumos.setRowSorter(null);
            buscar_consumos.setText("");
            Tabla_consumos.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_consumos);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_consumos.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_consumos.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_consumosKeyReleased

    private void exportar_consumosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_consumosActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/prod_consumos.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Consumos papel ( "+fechamediana.format(fechainsertar.parse(fechaini_guarda))+" al "+fechamediana.format(fechainsertar.parse(fechafin_guarda))+" )", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_consumos.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_consumos.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_consumos.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_consumos.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_consumos.getColumnCount()); j = j + 1) {
                    if (Tabla_consumos.getValueAt(i, j) != null) {
                        if (Tabla_consumos.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_consumos.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_consumos.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_consumos.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_consumos.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_consumos.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_consumos.getValueAt(i, j), arial10fdetalle));
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
        } catch (Exception exi) {
            JOptionPane.showMessageDialog(this, "ERROR AL EXPORTAR DATOS" + exi, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        //abrir el documento creado
        try {
            Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "NO SE PUEDE ABRIR EL ARCHIVO\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exportar_consumosActionPerformed

    private void ima_consumosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ima_consumosActionPerformed
        // TODO add your handling code here:
        grafica_consumos(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica");
    }//GEN-LAST:event_ima_consumosActionPerformed

    private void buscar_mermaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_mermaFocusGained
        // TODO add your handling code here:
        buscar_merma.selectAll();
    }//GEN-LAST:event_buscar_mermaFocusGained

    private void buscar_mermaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_mermaKeyReleased
        // TODO add your handling code here:
        if(buscar_merma.getText().equals("")){
            Tabla_merma.setRowSorter(null);
            buscar_merma.setText("");
            Tabla_merma.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_merma);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_merma.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_merma.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_mermaKeyReleased

    private void exportar_mermaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_mermaActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/prod_merma.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Merma por procesos ( "+fechamediana.format(fechainsertar.parse(fechaini_guarda))+" al "+fechamediana.format(fechainsertar.parse(fechafin_guarda))+" )", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_merma.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_merma.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_merma.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_merma.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_merma.getColumnCount()); j = j + 1) {
                    if (Tabla_merma.getValueAt(i, j) != null) {
                        if (Tabla_merma.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_merma.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_merma.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_merma.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_merma.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_merma.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_merma.getValueAt(i, j), arial10fdetalle));
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
        } catch (Exception exi) {
            JOptionPane.showMessageDialog(this, "ERROR AL EXPORTAR DATOS" + exi, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        //abrir el documento creado
        try {
            Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "NO SE PUEDE ABRIR EL ARCHIVO\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exportar_mermaActionPerformed

    private void ima_mermaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ima_mermaActionPerformed
        // TODO add your handling code here:
        grafica_merma(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica");
    }//GEN-LAST:event_ima_mermaActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla_consumos;
    private javax.swing.JTable Tabla_conver;
    private javax.swing.JTable Tabla_corr;
    private javax.swing.JTable Tabla_merma;
    private javax.swing.JTable Tabla_paros;
    private javax.swing.JButton btnactualizar;
    private javax.swing.JTextField buscar_consumos;
    private javax.swing.JTextField buscar_conver;
    private javax.swing.JTextField buscar_corr;
    private javax.swing.JTextField buscar_merma;
    private javax.swing.JTextField buscar_paros;
    private javax.swing.JComboBox combo_conver_maq;
    private javax.swing.JComboBox combo_paros_maq;
    private javax.swing.JButton exportar_consumos;
    private javax.swing.JButton exportar_conver;
    private javax.swing.JButton exportar_corr;
    private javax.swing.JButton exportar_merma;
    private javax.swing.JButton exportar_paros;
    private javax.swing.JInternalFrame frame_consumos;
    private javax.swing.JInternalFrame frame_conver;
    private javax.swing.JInternalFrame frame_corr;
    private javax.swing.JInternalFrame frame_merma;
    private javax.swing.JInternalFrame frame_paros;
    private javax.swing.JFileChooser guarda_file;
    private javax.swing.JButton ima_consumos;
    private javax.swing.JButton ima_conver;
    private javax.swing.JButton ima_corr;
    private javax.swing.JButton ima_merma;
    private javax.swing.JButton ima_paros;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel panel_consumos;
    private javax.swing.JPanel panel_conver;
    private javax.swing.JPanel panel_corr;
    private javax.swing.JPanel panel_merma;
    private javax.swing.JPanel panel_paros;
    private javax.swing.JTextField txt_res_consumos;
    private javax.swing.JTextField txt_res_conver;
    private javax.swing.JTextField txt_res_corr;
    private javax.swing.JTextField txt_res_merma;
    private javax.swing.JTextField txt_res_paros;
    // End of variables declaration//GEN-END:variables

}
