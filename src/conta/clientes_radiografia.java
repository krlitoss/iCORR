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
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.swing.JFileChooser;
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
//graficas
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author IVONNE
 */
public class clientes_radiografia extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot_acum_art=null,modelot_participacion=null,modelot_resis=null,modelot_disenos=null,modelot_tintas=null;
    ListSelectionModel modeloseleccion_acum_art=null,modeloseleccion_participacion=null,modeloseleccion_resis=null,modeloseleccion_disenos=null,modeloseleccion_tintas=null;
    ResultSet rs0;
    private Properties conf;
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales=new DecimalFormat("######0.000");
    DecimalFormat moneda0decimales=new DecimalFormat("$ #,###,##0");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat porcentaje2decimal=new DecimalFormat("##0.00 %");
    Double kgtotalfinal=0.0;
    Double kgtotalfinalmaquila=0.0;
    Double cantotalfinal=0.0;
    Double cantotalfinalmaquila=0.0;
    Double imptotalfinal=0.0;
    Double imptotalfinalmaquila=0.0;
    String fechaini_guarda="";
    String fechafin_guarda="";
    String sqlcli_guarda="";
    java.awt.Font font10plain=new java.awt.Font("Arial", java.awt.Font.PLAIN, 10);
    java.awt.Font font11plain=new java.awt.Font("Arial", java.awt.Font.PLAIN, 11);
    java.awt.Font font12bold=new java.awt.Font("Arial", java.awt.Font.PLAIN, 12);

    /** Creates new form usuarios */
    public clientes_radiografia(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conn=connt;
        conf = conexion.archivoInicial();
        //modelos de las tablas
        modelot_acum_art=(DefaultTableModel) Tabla_acum_art.getModel();
        Tabla_acum_art.setModel(modelot_acum_art);
        modeloseleccion_acum_art = Tabla_acum_art.getSelectionModel();
        modelot_participacion=(DefaultTableModel) Tabla_participacion.getModel();
        Tabla_participacion.setModel(modelot_participacion);
        modeloseleccion_participacion = Tabla_participacion.getSelectionModel();
        modelot_resis=(DefaultTableModel) Tabla_resis.getModel();
        Tabla_resis.setModel(modelot_resis);
        modeloseleccion_resis = Tabla_resis.getSelectionModel();
        modelot_disenos=(DefaultTableModel) Tabla_disenos.getModel();
        Tabla_disenos.setModel(modelot_disenos);
        modeloseleccion_disenos = Tabla_disenos.getSelectionModel();
        modelot_tintas=(DefaultTableModel) Tabla_tintas.getModel();
        Tabla_tintas.setModel(modelot_tintas);
        modeloseleccion_tintas = Tabla_tintas.getSelectionModel();
        ajusteTabladatos();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena_acum_art = new TableRowSorter<TableModel>(modelot_acum_art);
        Tabla_acum_art.setRowSorter(elQueOrdena_acum_art);
        TableRowSorter<TableModel> elQueOrdena_participa = new TableRowSorter<TableModel>(modelot_participacion);
        Tabla_participacion.setRowSorter(elQueOrdena_participa);
        TableRowSorter<TableModel> elQueOrdena_resis = new TableRowSorter<TableModel>(modelot_resis);
        Tabla_resis.setRowSorter(elQueOrdena_resis);
        TableRowSorter<TableModel> elQueOrdena_disenos = new TableRowSorter<TableModel>(modelot_disenos);
        Tabla_disenos.setRowSorter(elQueOrdena_disenos);
        TableRowSorter<TableModel> elQueOrdena_tintas = new TableRowSorter<TableModel>(modelot_tintas);
        Tabla_tintas.setRowSorter(elQueOrdena_tintas);

        //trae los primeros datos
        todos_datos();

        modeloseleccion_acum_art.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumaop=0,sumam2=0;
                    Double sumacantidad=0.0,sumakg=0.0,sumaimporte=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabla_acum_art.getValueAt(i,4)!=null && !Tabla_acum_art.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumaop+=Integer.parseInt(""+Tabla_acum_art.getValueAt(i,4));
                            }
                            if(Tabla_acum_art.getValueAt(i,5)!=null && !Tabla_acum_art.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidad+=Double.parseDouble(""+Tabla_acum_art.getValueAt(i,5));
                            }
                            if(Tabla_acum_art.getValueAt(i,6)!=null && !Tabla_acum_art.getValueAt(i,6).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabla_acum_art.getValueAt(i,6));
                            }
                            if(Tabla_acum_art.getValueAt(i,7)!=null && !Tabla_acum_art.getValueAt(i,7).equals("")){ //suma los metros lineales
                                    sumam2+=Integer.parseInt(""+Tabla_acum_art.getValueAt(i,7));
                            }
                            if(Tabla_acum_art.getValueAt(i,8)!=null && !Tabla_acum_art.getValueAt(i,8).equals("")){ //suma los metros lineales
                                    sumaimporte+=Double.parseDouble(""+Tabla_acum_art.getValueAt(i,8));
                            }
                        }
                    }
                    txt_res_acumulado.setText("OP's: "+estandarentero.format(sumaop)+"    Cantidad: "+estandarentero.format(sumacantidad)+"    KG: "+estandarentero.format(sumakg)+"    M2: "+estandarentero.format(sumam2)+"    Importe: "+moneda0decimales.format(sumaimporte)+"    PK: "+moneda2decimales.format(sumaimporte/sumakg));
                }
            }
        });
        modeloseleccion_participacion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumaop=0,sumam2=0;
                    Double sumacantidad=0.0,sumakg=0.0,sumaimporte=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabla_participacion.getValueAt(i,3)!=null && !Tabla_participacion.getValueAt(i,3).equals("")){ //suma los metros lineales
                                    sumaop+=Integer.parseInt(""+Tabla_participacion.getValueAt(i,3));
                            }
                            if(Tabla_participacion.getValueAt(i,4)!=null && !Tabla_participacion.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumacantidad+=Double.parseDouble(""+Tabla_participacion.getValueAt(i,4));
                            }
                            if(Tabla_participacion.getValueAt(i,5)!=null && !Tabla_participacion.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabla_participacion.getValueAt(i,5));
                            }
                            if(Tabla_participacion.getValueAt(i,6)!=null && !Tabla_participacion.getValueAt(i,6).equals("")){ //suma los metros lineales
                                    sumam2+=Integer.parseInt(""+Tabla_participacion.getValueAt(i,6));
                            }
                            if(Tabla_participacion.getValueAt(i,7)!=null && !Tabla_participacion.getValueAt(i,7).equals("")){ //suma los metros lineales
                                    sumaimporte+=Double.parseDouble(""+Tabla_participacion.getValueAt(i,7));
                            }
                        }
                    }
                    txt_res_participacion.setText("OP's: "+estandarentero.format(sumaop)+"    Cantidad: "+estandarentero.format(sumacantidad)+"    KG: "+estandarentero.format(sumakg)+"    M2: "+estandarentero.format(sumam2)+"    Importe: "+moneda0decimales.format(sumaimporte)+"    PK: "+moneda2decimales.format(sumaimporte/sumakg)+"    Peso Prom.: "+fijo3decimales.format(sumakg/sumacantidad));
                }
            }
        });
        modeloseleccion_resis.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumaop=0,sumam2=0;
                    Double sumacantidad=0.0,sumakg=0.0,sumaimporte=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabla_resis.getValueAt(i,3)!=null && !Tabla_resis.getValueAt(i,3).equals("")){ //suma los metros lineales
                                    sumaop+=Integer.parseInt(""+Tabla_resis.getValueAt(i,3));
                            }
                            if(Tabla_resis.getValueAt(i,4)!=null && !Tabla_resis.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumacantidad+=Double.parseDouble(""+Tabla_resis.getValueAt(i,4));
                            }
                            if(Tabla_resis.getValueAt(i,5)!=null && !Tabla_resis.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabla_resis.getValueAt(i,5));
                            }
                            if(Tabla_resis.getValueAt(i,6)!=null && !Tabla_resis.getValueAt(i,6).equals("")){ //suma los metros lineales
                                    sumam2+=Integer.parseInt(""+Tabla_resis.getValueAt(i,6));
                            }
                            if(Tabla_resis.getValueAt(i,7)!=null && !Tabla_resis.getValueAt(i,7).equals("")){ //suma los metros lineales
                                    sumaimporte+=Double.parseDouble(""+Tabla_resis.getValueAt(i,7));
                            }
                        }
                    }
                    txt_res_resis.setText("OP's: "+estandarentero.format(sumaop)+"    Cantidad: "+estandarentero.format(sumacantidad)+"    KG: "+estandarentero.format(sumakg)+"    M2: "+estandarentero.format(sumam2)+"    Importe: "+moneda0decimales.format(sumaimporte)+"    PK: "+moneda2decimales.format(sumaimporte/sumakg)+"    Peso Prom.: "+fijo3decimales.format(sumakg/sumacantidad));
                }
            }
        });
        modeloseleccion_disenos.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumaop=0,sumam2=0;
                    Double sumacantidad=0.0,sumakg=0.0,sumaimporte=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabla_disenos.getValueAt(i,3)!=null && !Tabla_disenos.getValueAt(i,3).equals("")){ //suma los metros lineales
                                    sumaop+=Integer.parseInt(""+Tabla_disenos.getValueAt(i,3));
                            }
                            if(Tabla_disenos.getValueAt(i,4)!=null && !Tabla_disenos.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumacantidad+=Double.parseDouble(""+Tabla_disenos.getValueAt(i,4));
                            }
                            if(Tabla_disenos.getValueAt(i,5)!=null && !Tabla_disenos.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabla_disenos.getValueAt(i,5));
                            }
                            if(Tabla_disenos.getValueAt(i,6)!=null && !Tabla_disenos.getValueAt(i,6).equals("")){ //suma los metros lineales
                                    sumam2+=Integer.parseInt(""+Tabla_disenos.getValueAt(i,6));
                            }
                            if(Tabla_disenos.getValueAt(i,7)!=null && !Tabla_disenos.getValueAt(i,7).equals("")){ //suma los metros lineales
                                    sumaimporte+=Double.parseDouble(""+Tabla_disenos.getValueAt(i,7));
                            }
                        }
                    }
                    txt_res_disenos.setText("OP's: "+estandarentero.format(sumaop)+"    Cantidad: "+estandarentero.format(sumacantidad)+"    KG: "+estandarentero.format(sumakg)+"    M2: "+estandarentero.format(sumam2)+"    Importe: "+moneda0decimales.format(sumaimporte)+"    PK: "+moneda2decimales.format(sumaimporte/sumakg)+"    Peso Prom.: "+fijo3decimales.format(sumakg/sumacantidad));
                }
            }
        });
        modeloseleccion_tintas.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumaop=0,sumam2=0;
                    Double sumacantidad=0.0,sumakg=0.0,sumaimporte=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabla_tintas.getValueAt(i,3)!=null && !Tabla_tintas.getValueAt(i,3).equals("")){ //suma los metros lineales
                                    sumaop+=Integer.parseInt(""+Tabla_tintas.getValueAt(i,3));
                            }
                            if(Tabla_tintas.getValueAt(i,4)!=null && !Tabla_tintas.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumacantidad+=Double.parseDouble(""+Tabla_tintas.getValueAt(i,4));
                            }
                            if(Tabla_tintas.getValueAt(i,5)!=null && !Tabla_tintas.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabla_tintas.getValueAt(i,5));
                            }
                            if(Tabla_tintas.getValueAt(i,6)!=null && !Tabla_tintas.getValueAt(i,6).equals("")){ //suma los metros lineales
                                    sumam2+=Integer.parseInt(""+Tabla_tintas.getValueAt(i,6));
                            }
                            if(Tabla_tintas.getValueAt(i,7)!=null && !Tabla_tintas.getValueAt(i,7).equals("")){ //suma los metros lineales
                                    sumaimporte+=Double.parseDouble(""+Tabla_tintas.getValueAt(i,7));
                            }
                        }
                    }
                    txt_res_tintas.setText("OP's: "+estandarentero.format(sumaop)+"    Cantidad: "+estandarentero.format(sumacantidad)+"    KG: "+estandarentero.format(sumakg)+"    M2: "+estandarentero.format(sumam2)+"    Importe: "+moneda0decimales.format(sumaimporte)+"    PK: "+moneda2decimales.format(sumaimporte/sumakg)+"    Peso Prom.: "+fijo3decimales.format(sumakg/sumacantidad));
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
        //orden acumulado de articulos
        Tabla_acum_art.getColumnModel().getColumn(0).setPreferredWidth(60);
        Tabla_acum_art.getColumnModel().getColumn(1).setPreferredWidth(190);
        Tabla_acum_art.getColumnModel().getColumn(2).setPreferredWidth(90);
        Tabla_acum_art.getColumnModel().getColumn(3).setPreferredWidth(250);
        Tabla_acum_art.getColumnModel().getColumn(4).setPreferredWidth(50);
        Tabla_acum_art.getColumnModel().getColumn(5).setPreferredWidth(80);
        Tabla_acum_art.getColumnModel().getColumn(6).setPreferredWidth(80);
        Tabla_acum_art.getColumnModel().getColumn(7).setPreferredWidth(80);
        Tabla_acum_art.getColumnModel().getColumn(8).setPreferredWidth(90);
        Tabla_acum_art.getColumnModel().getColumn(9).setPreferredWidth(70);
        Tabla_acum_art.getColumnModel().getColumn(10).setPreferredWidth(80);
        Tabla_acum_art.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tabla_acum_art.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tabla_acum_art.getColumnModel().getColumn(13).setPreferredWidth(60);
        Tabla_acum_art.getColumnModel().getColumn(14).setPreferredWidth(60);
        Tabla_acum_art.getColumnModel().getColumn(15).setPreferredWidth(60);
        
        //participacion
        Tabla_participacion.getColumnModel().getColumn(1).setPreferredWidth(85);
        Tabla_participacion.getColumnModel().getColumn(2).setPreferredWidth(260);
        Tabla_resis.getColumnModel().getColumn(1).setPreferredWidth(85);
        Tabla_resis.getColumnModel().getColumn(2).setPreferredWidth(260);
        Tabla_disenos.getColumnModel().getColumn(1).setPreferredWidth(85);
        Tabla_disenos.getColumnModel().getColumn(2).setPreferredWidth(260);
        Tabla_tintas.getColumnModel().getColumn(1).setPreferredWidth(85);
        Tabla_tintas.getColumnModel().getColumn(2).setPreferredWidth(260);

    }
    public void limpiatabla(){
        modelot_acum_art.setNumRows(0);
        modelot_participacion.setNumRows(0);
        modelot_resis.setNumRows(0);
        modelot_disenos.setNumRows(0);
        modelot_tintas.setNumRows(0);

    }
    public void todos_datos(){
        this.setTitle("::: REMISIONADO DETALLADO :::");
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
            //genera la informacion
            kgtotalfinal=0.0;
            kgtotalfinalmaquila=0.0;
            cantotalfinal=0.0;
            cantotalfinalmaquila=0.0;
            imptotalfinal=0.0;
            imptotalfinalmaquila=0.0;
            fechaini_guarda=fechainsertar.format(busca_fechascliente.getFechaini());
            fechafin_guarda=fechainsertar.format(busca_fechascliente.getFechafin());
            txtfechas.setText("Reporte Generado del "+fechamediana.format(busca_fechascliente.getFechaini())+" al "+fechamediana.format(busca_fechascliente.getFechafin()));
            sqlcli_guarda=dinamicoSQL;
            datos(fechainsertar.format(busca_fechascliente.getFechaini()),fechainsertar.format(busca_fechascliente.getFechafin()),dinamicoSQL);
        }
        busca_fechascliente=null;
    }
    public void total_fecha(String fi,String ft){
        rs0=null;
        try{
            String senSQL="SELECT clientes.nombre,ops.maquila,max(clientes.id_clientes) as clavecliente,count(remisiones_detalle.op) as cuentaop,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN (ops LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) ON remisiones_detalle.op=ops.op WHERE (remisiones.estatus='Act' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59')) GROUP BY clientes.nombre,ops.maquila ORDER BY ops.maquila,sum(remisiones_detalle.cantidadpzas*articulos.kg) DESC;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad=rs0.getDouble("totalcantidad");
                Double kg=rs0.getDouble("totalkg");
                Double importe=rs0.getDouble("importe");
                String maq=rs0.getString("maquila");
                if(maq.toUpperCase().equals("SI")){
                    kgtotalfinalmaquila+=kg;
                    cantotalfinalmaquila+=cantidad;
                    imptotalfinalmaquila+=importe;
                }else{
                    kgtotalfinal+=kg;
                    cantotalfinal+=cantidad;
                    imptotalfinal+=importe;
                }
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega los datos
        kgtnf.setText(estandarentero.format(kgtotalfinal));
        cantnf.setText(estandarentero.format(cantotalfinal));
        imptnf.setText(moneda2decimales.format(imptotalfinal));
        pktnf.setText(moneda2decimales.format(imptotalfinal/kgtotalfinal));
        pesotnf.setText(fijo3decimales.format(kgtotalfinal/cantotalfinal));
        //maquila
        kgtmf.setText(estandarentero.format(kgtotalfinalmaquila));
        cantmf.setText(estandarentero.format(cantotalfinalmaquila));
        imptmf.setText(moneda2decimales.format(imptotalfinalmaquila));
        pktmf.setText(moneda2decimales.format(imptotalfinalmaquila/kgtotalfinalmaquila));
        pesotmf.setText(fijo3decimales.format(kgtotalfinalmaquila/cantotalfinalmaquila));

    }
    public void datos(String fi,String ft,String sqlclavecli){
        //datos totales
        total_fecha(fi,ft);
        
        //consulta acumulado de articulos
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND remisiones."+sqlclavecli;
        }
        //resul en null
        rs0=null;
        try{
            String senSQL="SELECT articulos.clavearticulo,ops.maquila,max(clientes.id_clientes) as clavecliente,max(clientes.nombre) as nombre,count(remisiones_detalle.op) as cuentaop,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe, articulos.preciomillar AS precio,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2, max(articulos.articulo) as articulo ,max(articulos.diseno) as diseno,max(articulos.claveresistencia) as claveresistencia,max(articulos.largo) as largo,max(articulos.ancho) as ancho,max(articulos.inlargo) as inlargo,max(articulos.inancho) as inancho,max(articulos.inalto) as inalto FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN (ops LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) ON remisiones_detalle.op=ops.op WHERE (remisiones.estatus='Act' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59')"+sqlcliacum+") GROUP BY articulos.clavearticulo,ops.maquila,articulos.preciomillar ORDER BY ops.maquila,max(clientes.nombre),sum(remisiones_detalle.cantidadpzas*articulos.kg) DESC;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad=rs0.getDouble("totalcantidad");
                Double kg=rs0.getDouble("totalkg");
                Double importe=rs0.getDouble("importe");
                String maq=rs0.getString("maquila");
                String cadena=rs0.getString("nombre");
                if(!sqlclavecli.equals("")){
                    this.setTitle("::: REMISIONADO DETALLADO ("+cadena+") :::");
                }
                Object datos[]={maq,cadena,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getInt("cuentaop"),Integer.parseInt(fijo0decimales.format(cantidad)),Integer.parseInt(fijo0decimales.format(kg)),rs0.getInt("totalm2"),importe,rs0.getDouble ("precio"),Double.parseDouble(fijo2decimales.format(importe/kg)),rs0.getString("diseno"),rs0.getString("claveresistencia"),rs0.getDouble("largo"),rs0.getDouble("ancho"),rs0.getDouble("inlargo"),rs0.getDouble("inancho"),rs0.getDouble("inalto")};
                modelot_acum_art.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //grafica de participacion
        grafica_paticipacion(fi,ft,sqlclavecli,"");
        grafica_resis(fi,ft,sqlclavecli,"");
        grafica_disenos(fi,ft,sqlclavecli,"");
        grafica_tintas(fi,ft,sqlclavecli,"");

    }
    public void grafica_paticipacion(String fi,String ft,String sqlclavecli,String save){
        
        DefaultPieDataset dataset = new DefaultPieDataset();//arreglo de datos
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND remisiones."+sqlclavecli;
        }
        //resul en null
        modelot_participacion.setNumRows(0);
        rs0=null;
        try{
            String senSQL="SELECT clientes.nombre,ops.maquila,max(clientes.id_clientes) as clavecliente,count(remisiones_detalle.op) as cuentaop,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN (ops LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) ON remisiones_detalle.op=ops.op WHERE (remisiones.estatus='Act' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59')"+sqlcliacum+") GROUP BY clientes.nombre,ops.maquila ORDER BY ops.maquila,sum(remisiones_detalle.cantidadpzas*articulos.kg) DESC;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad=rs0.getDouble("totalcantidad");
                Double kg=rs0.getDouble("totalkg");
                Double importe=rs0.getDouble("importe");
                String maq=rs0.getString("maquila");
                String porce="";
                String cadena=rs0.getString("nombre");
                if(maq.toUpperCase().equals("SI")){
                    porce=porcentaje2decimal.format(kg/kgtotalfinalmaquila);
                }else{
                    porce=porcentaje2decimal.format(kg/kgtotalfinal);
                    String nuevacadena=cadena;
                    if(nuevacadena.length()>32)
                        nuevacadena=cadena.substring(0, 31)+"...";
                    dataset.setValue(nuevacadena, kg);
                    //si es de un solo cliente
                    if(!sqlclavecli.equals("")){
                        dataset.setValue("Otros", kgtotalfinal-kg);
                    }
                }
                Object datos[]={maq,rs0.getString("clavecliente"),cadena,rs0.getInt("cuentaop"),Integer.parseInt(fijo0decimales.format(cantidad)),Integer.parseInt(fijo0decimales.format(kg)),rs0.getInt("totalm2"),importe,Double.parseDouble(fijo2decimales.format(importe/kg)),Double.parseDouble(fijo3decimales.format(kg/cantidad)),porce};
                modelot_participacion.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        JFreeChart chart_participacion = ChartFactory.createPieChart("", dataset,true,true,false);
        PiePlot plot = (PiePlot) chart_participacion.getPlot();
        plot.setLabelFont(font10plain);
        plot.setNoDataMessage("No hay datos");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", estandarentero,porcentaje2decimal));
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        LegendTitle leg=chart_participacion.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setBackgroundPaint(new java.awt.Color(233,233,233));
        leg.setItemFont(font10plain);
        chart_participacion.getTitle().setFont(font12bold);
        chart_participacion.setBackgroundPaint(java.awt.Color.WHITE);
        //genera la grafica en el panel
        ChartPanel Panel_participacion = new ChartPanel(chart_participacion);
        frame_participacion.setContentPane(Panel_participacion);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_cli.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_participacion, 800,500);
                    JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                }

             }catch (IOException e){ JOptionPane.showMessageDialog(this,"NO SE PUDO GENERAR LA IMAGEN"+e.getMessage(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);    }
        }

    }
    public void grafica_resis(String fi,String ft,String sqlclavecli,String save){

        DefaultPieDataset dataset = new DefaultPieDataset();//arreglo de datos
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND remisiones."+sqlclavecli;
        }
        //resul en null
        modelot_resis.setNumRows(0);
        rs0=null;
        try{
            String senSQL="SELECT articulos.claveresistencia,max(resistencias.descripcion) as descripcion,max(resistencias.color) as color,max(resistencias.corrugado) as corrugado,max(resistencias.referencia) as referencia,ops.maquila,max(clientes.id_clientes) as clavecliente,count(remisiones_detalle.op) as cuentaop,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN (articulos LEFT JOIN resistencias ON articulos.claveresistencia=resistencias.clave) ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN (ops LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) ON remisiones_detalle.op=ops.op WHERE (remisiones.estatus='Act' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59')"+sqlcliacum+") GROUP BY articulos.claveresistencia,ops.maquila ORDER BY ops.maquila,sum(remisiones_detalle.cantidadpzas*articulos.kg) DESC;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad=rs0.getDouble("totalcantidad");
                Double kg=rs0.getDouble("totalkg");
                Double importe=rs0.getDouble("importe");
                String maq=rs0.getString("maquila");
                String porce="";
                String cadena=rs0.getString("descripcion");
                if(maq.toUpperCase().equals("SI")){
                    porce=porcentaje2decimal.format(kg/kgtotalfinalmaquila);
                }else{
                    porce=porcentaje2decimal.format(kg/kgtotalfinal);
                    dataset.setValue(rs0.getString("referencia")+" "+rs0.getString("corrugado")+"-"+rs0.getString("color"), kg);
                }
                Object datos[]={maq,rs0.getString("claveresistencia"),cadena,rs0.getInt("cuentaop"),Integer.parseInt(fijo0decimales.format(cantidad)),Integer.parseInt(fijo0decimales.format(kg)),rs0.getInt("totalm2"),importe,Double.parseDouble(fijo2decimales.format(importe/kg)),Double.parseDouble(fijo3decimales.format(kg/cantidad)),porce};
                modelot_resis.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        JFreeChart chart_participacion = ChartFactory.createPieChart("", dataset,true,true,false);
        PiePlot plot = (PiePlot) chart_participacion.getPlot();
        plot.setLabelFont(font11plain);
        plot.setNoDataMessage("No hay datos");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", estandarentero,porcentaje2decimal));
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        LegendTitle leg=chart_participacion.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setBackgroundPaint(new java.awt.Color(233,233,233));
        leg.setItemFont(font11plain);
        chart_participacion.getTitle().setFont(font12bold);
        chart_participacion.setBackgroundPaint(java.awt.Color.WHITE);
        //genera la grafica en el panel
        ChartPanel Panel_participacion = new ChartPanel(chart_participacion);
        frame_resis.setContentPane(Panel_participacion);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_resis.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_participacion, 800,500);
                    JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                }

             }catch (IOException e){ JOptionPane.showMessageDialog(this,"NO SE PUDO GENERAR LA IMAGEN"+e.getMessage(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);    }
        }

    }
    public void grafica_disenos(String fi,String ft,String sqlclavecli,String save){

        DefaultPieDataset dataset = new DefaultPieDataset();//arreglo de datos
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND remisiones."+sqlclavecli;
        }
        //resul en null
        modelot_disenos.setNumRows(0);
        rs0=null;
        try{
            String senSQL="SELECT articulos.diseno,ops.maquila,max(clientes.id_clientes) as clavecliente,count(remisiones_detalle.op) as cuentaop,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN (ops LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) ON remisiones_detalle.op=ops.op WHERE (remisiones.estatus='Act' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59')"+sqlcliacum+") GROUP BY articulos.diseno,ops.maquila ORDER BY ops.maquila,sum(remisiones_detalle.cantidadpzas*articulos.kg) DESC;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad=rs0.getDouble("totalcantidad");
                Double kg=rs0.getDouble("totalkg");
                Double importe=rs0.getDouble("importe");
                String maq=rs0.getString("maquila");
                String porce="";
                String cadena=rs0.getString("diseno");
                if(maq.toUpperCase().equals("SI")){
                    porce=porcentaje2decimal.format(kg/kgtotalfinalmaquila);
                }else{
                    porce=porcentaje2decimal.format(kg/kgtotalfinal);
                    dataset.setValue(cadena, kg);
                }
                Object datos[]={maq,"",cadena,rs0.getInt("cuentaop"),Integer.parseInt(fijo0decimales.format(cantidad)),Integer.parseInt(fijo0decimales.format(kg)),rs0.getInt("totalm2"),importe,Double.parseDouble(fijo2decimales.format(importe/kg)),Double.parseDouble(fijo3decimales.format(kg/cantidad)),porce};
                modelot_disenos.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        JFreeChart chart_participacion = ChartFactory.createPieChart("", dataset,true,true,false);
        PiePlot plot = (PiePlot) chart_participacion.getPlot();
        plot.setLabelFont(font11plain);
        plot.setNoDataMessage("No hay datos");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", estandarentero,porcentaje2decimal));
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        LegendTitle leg=chart_participacion.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setBackgroundPaint(new java.awt.Color(233,233,233));
        leg.setItemFont(font11plain);
        chart_participacion.getTitle().setFont(font12bold);
        chart_participacion.setBackgroundPaint(java.awt.Color.WHITE);
        //genera la grafica en el panel
        ChartPanel Panel_participacion = new ChartPanel(chart_participacion);
        frame_disenos.setContentPane(Panel_participacion);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_dis.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_participacion, 800,500);
                    JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                }

             }catch (IOException e){ JOptionPane.showMessageDialog(this,"NO SE PUDO GENERAR LA IMAGEN"+e.getMessage(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);    }
        }

    }
    public void grafica_tintas(String fi,String ft,String sqlclavecli,String save){

        DefaultPieDataset dataset = new DefaultPieDataset();//arreglo de datos
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND remisiones."+sqlclavecli;
        }
        //resul en null
        modelot_tintas.setNumRows(0);
        rs0=null;
        try{
            String senSQL="SELECT articulos.tintas,ops.maquila,max(clientes.id_clientes) as clavecliente,count(remisiones_detalle.op) as cuentaop,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN (ops LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) ON remisiones_detalle.op=ops.op WHERE (remisiones.estatus='Act' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59')"+sqlcliacum+") GROUP BY articulos.tintas,ops.maquila ORDER BY ops.maquila,sum(remisiones_detalle.cantidadpzas*articulos.kg) DESC;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad=rs0.getDouble("totalcantidad");
                Double kg=rs0.getDouble("totalkg");
                Double importe=rs0.getDouble("importe");
                String maq=rs0.getString("maquila");
                String porce="";
                String cadena=rs0.getString("tintas");
                String cadenat=cadena;
                if(!cadena.equals("0") && !cadena.equals("1"))
                        cadena+=" Tintas";
                if(cadena.equals("0"))
                        cadena="Sin Impresion";
                if(cadena.equals("1"))
                        cadena+=" Tinta";

                if(maq.toUpperCase().equals("SI")){
                    porce=porcentaje2decimal.format(kg/kgtotalfinalmaquila);
                }else{
                    porce=porcentaje2decimal.format(kg/kgtotalfinal);
                    dataset.setValue(cadena, kg);
                }
                Object datos[]={maq,cadenat,cadena,rs0.getInt("cuentaop"),Integer.parseInt(fijo0decimales.format(cantidad)),Integer.parseInt(fijo0decimales.format(kg)),rs0.getInt("totalm2"),importe,Double.parseDouble(fijo2decimales.format(importe/kg)),Double.parseDouble(fijo3decimales.format(kg/cantidad)),porce};
                modelot_tintas.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        JFreeChart chart_participacion = ChartFactory.createPieChart("", dataset,true,true,false);
        PiePlot plot = (PiePlot) chart_participacion.getPlot();
        plot.setLabelFont(font11plain);
        plot.setNoDataMessage("No hay datos");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", estandarentero,porcentaje2decimal));
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        LegendTitle leg=chart_participacion.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setBackgroundPaint(new java.awt.Color(233,233,233));
        leg.setItemFont(font11plain);
        chart_participacion.getTitle().setFont(font12bold);
        chart_participacion.setBackgroundPaint(java.awt.Color.WHITE);
        //genera la grafica en el panel
        ChartPanel Panel_participacion = new ChartPanel(chart_participacion);
        frame_tintas.setContentPane(Panel_participacion);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_tin.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_participacion, 800,500);
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
        btnimprimir = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        kgtnf = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cantnf = new javax.swing.JTextField();
        imptnf = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        pktnf = new javax.swing.JTextField();
        pesotnf = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        kgtmf = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cantmf = new javax.swing.JTextField();
        imptmf = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        pktmf = new javax.swing.JTextField();
        pesotmf = new javax.swing.JTextField();
        txtfechas = new javax.swing.JLabel();
        parti = new javax.swing.JPanel();
        frame_participacion = new javax.swing.JInternalFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla_participacion = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        buscar_participacion = new javax.swing.JTextField();
        exportar_partici = new javax.swing.JButton();
        txt_res_participacion = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        resis = new javax.swing.JPanel();
        frame_resis = new javax.swing.JInternalFrame();
        jScrollPane5 = new javax.swing.JScrollPane();
        Tabla_resis = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        buscar_resis = new javax.swing.JTextField();
        exportar_resis = new javax.swing.JButton();
        txt_res_resis = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        disenos = new javax.swing.JPanel();
        frame_disenos = new javax.swing.JInternalFrame();
        jScrollPane4 = new javax.swing.JScrollPane();
        Tabla_disenos = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        buscar_disenos = new javax.swing.JTextField();
        exportar_disenos = new javax.swing.JButton();
        txt_res_disenos = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        tintas = new javax.swing.JPanel();
        frame_tintas = new javax.swing.JInternalFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tabla_tintas = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        buscar_tintas = new javax.swing.JTextField();
        exportar_tintas = new javax.swing.JButton();
        txt_res_tintas = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        acum = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        buscar_acumulado = new javax.swing.JTextField();
        exportar_acum = new javax.swing.JButton();
        txt_res_acumulado = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla_acum_art = new javax.swing.JTable();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(clientes_radiografia.class);
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

        jToolBar1.setBackground(resourceMap.getColor("jToolBar1.background")); // NOI18N
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        btnimprimir.setIcon(resourceMap.getIcon("btnimprimir.icon")); // NOI18N
        btnimprimir.setText(resourceMap.getString("btnimprimir.text")); // NOI18N
        btnimprimir.setToolTipText(resourceMap.getString("btnimprimir.toolTipText")); // NOI18N
        btnimprimir.setFocusable(false);
        btnimprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnimprimir.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnimprimir.setMaximumSize(new java.awt.Dimension(70, 48));
        btnimprimir.setMinimumSize(new java.awt.Dimension(70, 48));
        btnimprimir.setName("btnimprimir"); // NOI18N
        btnimprimir.setPreferredSize(new java.awt.Dimension(70, 48));
        btnimprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(btnimprimir);

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

        jPanel8.setName("jPanel8"); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel9.border.title"))); // NOI18N
        jPanel9.setName("jPanel9"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        kgtnf.setFont(resourceMap.getFont("kgtnf.font")); // NOI18N
        kgtnf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        kgtnf.setText(resourceMap.getString("kgtnf.text")); // NOI18N
        kgtnf.setName("kgtnf"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        cantnf.setFont(resourceMap.getFont("cantnf.font")); // NOI18N
        cantnf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cantnf.setText(resourceMap.getString("cantnf.text")); // NOI18N
        cantnf.setName("cantnf"); // NOI18N

        imptnf.setFont(resourceMap.getFont("imptnf.font")); // NOI18N
        imptnf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        imptnf.setText(resourceMap.getString("imptnf.text")); // NOI18N
        imptnf.setName("imptnf"); // NOI18N

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setFont(resourceMap.getFont("jLabel7.font")); // NOI18N
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        pktnf.setFont(resourceMap.getFont("pktnf.font")); // NOI18N
        pktnf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pktnf.setText(resourceMap.getString("pktnf.text")); // NOI18N
        pktnf.setName("pktnf"); // NOI18N

        pesotnf.setFont(resourceMap.getFont("pesotnf.font")); // NOI18N
        pesotnf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pesotnf.setText(resourceMap.getString("pesotnf.text")); // NOI18N
        pesotnf.setName("pesotnf"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cantnf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(imptnf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(pktnf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(pesotnf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(kgtnf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(kgtnf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cantnf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(imptnf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(pktnf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(pesotnf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel10.border.title"))); // NOI18N
        jPanel10.setName("jPanel10"); // NOI18N

        jLabel8.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        kgtmf.setFont(resourceMap.getFont("kgtmf.font")); // NOI18N
        kgtmf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        kgtmf.setName("kgtmf"); // NOI18N

        jLabel9.setFont(resourceMap.getFont("jLabel9.font")); // NOI18N
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setFont(resourceMap.getFont("jLabel10.font")); // NOI18N
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        cantmf.setFont(resourceMap.getFont("cantmf.font")); // NOI18N
        cantmf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cantmf.setName("cantmf"); // NOI18N

        imptmf.setFont(resourceMap.getFont("imptmf.font")); // NOI18N
        imptmf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        imptmf.setName("imptmf"); // NOI18N

        jLabel11.setFont(resourceMap.getFont("jLabel11.font")); // NOI18N
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setFont(resourceMap.getFont("jLabel12.font")); // NOI18N
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        pktmf.setFont(resourceMap.getFont("pktmf.font")); // NOI18N
        pktmf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pktmf.setName("pktmf"); // NOI18N

        pesotmf.setFont(resourceMap.getFont("pesotmf.font")); // NOI18N
        pesotmf.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pesotmf.setName("pesotmf"); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cantmf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(imptmf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(pktmf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(pesotmf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(kgtmf, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(kgtmf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cantmf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(imptmf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(pktmf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(pesotmf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtfechas.setFont(resourceMap.getFont("txtfechas.font")); // NOI18N
        txtfechas.setForeground(resourceMap.getColor("txtfechas.foreground")); // NOI18N
        txtfechas.setText(resourceMap.getString("txtfechas.text")); // NOI18N
        txtfechas.setName("txtfechas"); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtfechas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(161, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtfechas, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(243, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel8.TabConstraints.tabTitle"), jPanel8); // NOI18N

        parti.setName("parti"); // NOI18N

        frame_participacion.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_participacion.setTitle(resourceMap.getString("frame_participacion.title")); // NOI18N
        frame_participacion.setFrameIcon(resourceMap.getIcon("frame_participacion.frameIcon")); // NOI18N
        frame_participacion.setName("frame_participacion"); // NOI18N
        frame_participacion.setVisible(true);

        javax.swing.GroupLayout frame_participacionLayout = new javax.swing.GroupLayout(frame_participacion.getContentPane());
        frame_participacion.getContentPane().setLayout(frame_participacionLayout);
        frame_participacionLayout.setHorizontalGroup(
            frame_participacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_participacionLayout.setVerticalGroup(
            frame_participacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        Tabla_participacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Maquila", "Clave Cliente", "Cliente", "OP's", "Cantidad", "KG", "M2", "$ Importe", "$ P.K.", "Peso Prom.", "%"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
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
        Tabla_participacion.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_participacion.setName("Tabla_participacion"); // NOI18N
        Tabla_participacion.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(Tabla_participacion);

        jPanel5.setBackground(resourceMap.getColor("jPanel5.background")); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(resourceMap.getIcon("jLabel3.icon")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        buscar_participacion.setName("buscar_participacion"); // NOI18N
        buscar_participacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_participacionFocusGained(evt);
            }
        });
        buscar_participacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_participacionKeyReleased(evt);
            }
        });

        exportar_partici.setIcon(resourceMap.getIcon("exportar_partici.icon")); // NOI18N
        exportar_partici.setText(resourceMap.getString("exportar_partici.text")); // NOI18N
        exportar_partici.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_partici.setName("exportar_partici"); // NOI18N
        exportar_partici.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_particiActionPerformed(evt);
            }
        });

        txt_res_participacion.setFont(resourceMap.getFont("txt_res_participacion.font")); // NOI18N
        txt_res_participacion.setForeground(resourceMap.getColor("txt_res_participacion.foreground")); // NOI18N
        txt_res_participacion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_participacion.setText(resourceMap.getString("txt_res_participacion.text")); // NOI18N
        txt_res_participacion.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_participacion.setName("txt_res_participacion"); // NOI18N

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_participacion, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_participacion, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_partici, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(txt_res_participacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_participacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_partici)
                        .addComponent(jButton2))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout partiLayout = new javax.swing.GroupLayout(parti);
        parti.setLayout(partiLayout);
        partiLayout.setHorizontalGroup(
            partiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_participacion)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        partiLayout.setVerticalGroup(
            partiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, partiLayout.createSequentialGroup()
                .addComponent(frame_participacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("parti.TabConstraints.tabTitle"), parti); // NOI18N

        resis.setName("resis"); // NOI18N

        frame_resis.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_resis.setTitle(resourceMap.getString("frame_resis.title")); // NOI18N
        frame_resis.setFrameIcon(null);
        frame_resis.setName("frame_resis"); // NOI18N
        frame_resis.setVisible(true);

        javax.swing.GroupLayout frame_resisLayout = new javax.swing.GroupLayout(frame_resis.getContentPane());
        frame_resis.getContentPane().setLayout(frame_resisLayout);
        frame_resisLayout.setHorizontalGroup(
            frame_resisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_resisLayout.setVerticalGroup(
            frame_resisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        Tabla_resis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Maquila", "Clave Resis.", "Descripcion", "OP's", "Cantidad", "KG", "M2", "$ Importe", "$ P.K.", "Peso Prom.", "%"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
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
        Tabla_resis.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_resis.setName("Tabla_resis"); // NOI18N
        Tabla_resis.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(Tabla_resis);

        jPanel16.setBackground(resourceMap.getColor("jPanel16.background")); // NOI18N
        jPanel16.setName("jPanel16"); // NOI18N

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setIcon(resourceMap.getIcon("jLabel15.icon")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        buscar_resis.setName("buscar_resis"); // NOI18N
        buscar_resis.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_resisFocusGained(evt);
            }
        });
        buscar_resis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_resisKeyReleased(evt);
            }
        });

        exportar_resis.setIcon(resourceMap.getIcon("exportar_resis.icon")); // NOI18N
        exportar_resis.setText(resourceMap.getString("exportar_resis.text")); // NOI18N
        exportar_resis.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_resis.setName("exportar_resis"); // NOI18N
        exportar_resis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_resisActionPerformed(evt);
            }
        });

        txt_res_resis.setFont(resourceMap.getFont("txt_res_resis.font")); // NOI18N
        txt_res_resis.setForeground(resourceMap.getColor("txt_res_resis.foreground")); // NOI18N
        txt_res_resis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_resis.setText(resourceMap.getString("txt_res_resis.text")); // NOI18N
        txt_res_resis.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_resis.setName("txt_res_resis"); // NOI18N

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_resis, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_resis, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_resis, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addComponent(txt_res_resis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_resis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_resis)
                        .addComponent(jButton5))
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout resisLayout = new javax.swing.GroupLayout(resis);
        resis.setLayout(resisLayout);
        resisLayout.setHorizontalGroup(
            resisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_resis)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        resisLayout.setVerticalGroup(
            resisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, resisLayout.createSequentialGroup()
                .addComponent(frame_resis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("resis.TabConstraints.tabTitle"), resis); // NOI18N

        disenos.setName("disenos"); // NOI18N

        frame_disenos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_disenos.setTitle(resourceMap.getString("frame_disenos.title")); // NOI18N
        frame_disenos.setFrameIcon(null);
        frame_disenos.setName("frame_disenos"); // NOI18N
        frame_disenos.setVisible(true);

        javax.swing.GroupLayout frame_disenosLayout = new javax.swing.GroupLayout(frame_disenos.getContentPane());
        frame_disenos.getContentPane().setLayout(frame_disenosLayout);
        frame_disenosLayout.setHorizontalGroup(
            frame_disenosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_disenosLayout.setVerticalGroup(
            frame_disenosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        Tabla_disenos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Maquila", "-", "Diseño", "OP's", "Cantidad", "KG", "M2", "$ Importe", "$ P.K.", "Peso Prom.", "%"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
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
        Tabla_disenos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_disenos.setName("Tabla_disenos"); // NOI18N
        Tabla_disenos.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(Tabla_disenos);

        jPanel14.setBackground(resourceMap.getColor("jPanel14.background")); // NOI18N
        jPanel14.setName("jPanel14"); // NOI18N

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setIcon(resourceMap.getIcon("jLabel14.icon")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        buscar_disenos.setName("buscar_disenos"); // NOI18N
        buscar_disenos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_disenosFocusGained(evt);
            }
        });
        buscar_disenos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_disenosKeyReleased(evt);
            }
        });

        exportar_disenos.setIcon(resourceMap.getIcon("exportar_disenos.icon")); // NOI18N
        exportar_disenos.setText(resourceMap.getString("exportar_disenos.text")); // NOI18N
        exportar_disenos.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_disenos.setName("exportar_disenos"); // NOI18N
        exportar_disenos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_disenosActionPerformed(evt);
            }
        });

        txt_res_disenos.setFont(resourceMap.getFont("txt_res_disenos.font")); // NOI18N
        txt_res_disenos.setForeground(resourceMap.getColor("txt_res_disenos.foreground")); // NOI18N
        txt_res_disenos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_disenos.setText(resourceMap.getString("txt_res_disenos.text")); // NOI18N
        txt_res_disenos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_disenos.setName("txt_res_disenos"); // NOI18N

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_disenos, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_disenos, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_disenos, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addComponent(txt_res_disenos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_disenos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_disenos)
                        .addComponent(jButton4))
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout disenosLayout = new javax.swing.GroupLayout(disenos);
        disenos.setLayout(disenosLayout);
        disenosLayout.setHorizontalGroup(
            disenosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_disenos)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        disenosLayout.setVerticalGroup(
            disenosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, disenosLayout.createSequentialGroup()
                .addComponent(frame_disenos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("disenos.TabConstraints.tabTitle"), disenos); // NOI18N

        tintas.setName("tintas"); // NOI18N

        frame_tintas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_tintas.setTitle(resourceMap.getString("frame_tintas.title")); // NOI18N
        frame_tintas.setFrameIcon(null);
        frame_tintas.setName("frame_tintas"); // NOI18N
        frame_tintas.setVisible(true);

        javax.swing.GroupLayout frame_tintasLayout = new javax.swing.GroupLayout(frame_tintas.getContentPane());
        frame_tintas.getContentPane().setLayout(frame_tintasLayout);
        frame_tintasLayout.setHorizontalGroup(
            frame_tintasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_tintasLayout.setVerticalGroup(
            frame_tintasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        Tabla_tintas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Maquila", "Numero", "Descripcion", "OP's", "Cantidad", "KG", "M2", "$ Importe", "$ P.K.", "Peso Prom.", "%"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
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
        Tabla_tintas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_tintas.setName("Tabla_tintas"); // NOI18N
        Tabla_tintas.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(Tabla_tintas);

        jPanel12.setBackground(resourceMap.getColor("jPanel12.background")); // NOI18N
        jPanel12.setName("jPanel12"); // NOI18N

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setIcon(resourceMap.getIcon("jLabel13.icon")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        buscar_tintas.setName("buscar_tintas"); // NOI18N
        buscar_tintas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_tintasFocusGained(evt);
            }
        });
        buscar_tintas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_tintasKeyReleased(evt);
            }
        });

        exportar_tintas.setIcon(resourceMap.getIcon("exportar_tintas.icon")); // NOI18N
        exportar_tintas.setText(resourceMap.getString("exportar_tintas.text")); // NOI18N
        exportar_tintas.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_tintas.setName("exportar_tintas"); // NOI18N
        exportar_tintas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_tintasActionPerformed(evt);
            }
        });

        txt_res_tintas.setFont(resourceMap.getFont("txt_res_tintas.font")); // NOI18N
        txt_res_tintas.setForeground(resourceMap.getColor("txt_res_tintas.foreground")); // NOI18N
        txt_res_tintas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_tintas.setText(resourceMap.getString("txt_res_tintas.text")); // NOI18N
        txt_res_tintas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_tintas.setName("txt_res_tintas"); // NOI18N

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_tintas, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_tintas, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_tintas, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addComponent(txt_res_tintas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_tintas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_tintas)
                        .addComponent(jButton3))
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout tintasLayout = new javax.swing.GroupLayout(tintas);
        tintas.setLayout(tintasLayout);
        tintasLayout.setHorizontalGroup(
            tintasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_tintas)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        tintasLayout.setVerticalGroup(
            tintasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tintasLayout.createSequentialGroup()
                .addComponent(frame_tintas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("tintas.TabConstraints.tabTitle"), tintas); // NOI18N

        acum.setName("acum"); // NOI18N

        jPanel4.setBackground(resourceMap.getColor("jPanel4.background")); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        buscar_acumulado.setText(resourceMap.getString("buscar_acumulado.text")); // NOI18N
        buscar_acumulado.setName("buscar_acumulado"); // NOI18N
        buscar_acumulado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_acumuladoFocusGained(evt);
            }
        });
        buscar_acumulado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_acumuladoKeyReleased(evt);
            }
        });

        exportar_acum.setIcon(resourceMap.getIcon("exportar_acum.icon")); // NOI18N
        exportar_acum.setText(resourceMap.getString("exportar_acum.text")); // NOI18N
        exportar_acum.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_acum.setName("exportar_acum"); // NOI18N
        exportar_acum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_acumActionPerformed(evt);
            }
        });

        txt_res_acumulado.setFont(resourceMap.getFont("txt_res_acumulado.font")); // NOI18N
        txt_res_acumulado.setForeground(resourceMap.getColor("txt_res_acumulado.foreground")); // NOI18N
        txt_res_acumulado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_res_acumulado.setText(resourceMap.getString("txt_res_acumulado.text")); // NOI18N
        txt_res_acumulado.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txt_res_acumulado.setName("txt_res_acumulado"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_res_acumulado, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar_acumulado, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 412, Short.MAX_VALUE)
                        .addComponent(exportar_acum, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(txt_res_acumulado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_acumulado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportar_acum)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabla_acum_art.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Maquila", "Cliente", "Clave Art.", "Articulo", "OP's", "Cantidad", "KG", "M2", "$ Importe", "$ precio", "$ P.K.", "Diseño", "Resis.", "Largo", "Ancho", "In Ancho", "In Ancho", "In Alto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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
        Tabla_acum_art.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_acum_art.setName("Tabla_acum_art"); // NOI18N
        Tabla_acum_art.setRowHeight(22);
        Tabla_acum_art.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(Tabla_acum_art);
        Tabla_acum_art.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title0")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title1")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title2")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title3")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title4")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title5")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title6")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title7")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title8")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(9).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title18")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(10).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title9")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(11).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title10")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(12).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title11")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(13).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title12")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(14).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title13")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(15).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title14")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(16).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title15")); // NOI18N
        Tabla_acum_art.getColumnModel().getColumn(17).setHeaderValue(resourceMap.getString("Tabla_acum_art.columnModel.title16")); // NOI18N

        javax.swing.GroupLayout acumLayout = new javax.swing.GroupLayout(acum);
        acum.setLayout(acumLayout);
        acumLayout.setHorizontalGroup(
            acumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        acumLayout.setVerticalGroup(
            acumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, acumLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("acum.TabConstraints.tabTitle"), acum); // NOI18N

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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))
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

    private void btnimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimirActionPerformed
        // TODO add your handling code here:
        todos_datos();
}//GEN-LAST:event_btnimprimirActionPerformed

    private void buscar_acumuladoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_acumuladoFocusGained
        // TODO add your handling code here:
        buscar_acumulado.selectAll();
    }//GEN-LAST:event_buscar_acumuladoFocusGained

    private void buscar_acumuladoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_acumuladoKeyReleased
        // TODO add your handling code here:
        if(buscar_acumulado.getText().equals("")){
            Tabla_acum_art.setRowSorter(null);
            buscar_acumulado.setText("");
            Tabla_acum_art.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_acum_art);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_acumulado.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_acum_art.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_acumuladoKeyReleased

    private void exportar_acumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_acumActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/rem_acum.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Remisionado Acumulado Articulos", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_acum_art.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_acum_art.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_acum_art.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_acum_art.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_acum_art.getColumnCount()); j = j + 1) {
                    if (Tabla_acum_art.getValueAt(i, j) != null) {
                        if (Tabla_acum_art.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_acum_art.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_acum_art.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_acum_art.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_acum_art.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_acum_art.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_acum_art.getValueAt(i, j), arial10fdetalle));
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
    }//GEN-LAST:event_exportar_acumActionPerformed

    private void buscar_participacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_participacionFocusGained
        // TODO add your handling code here:
        buscar_participacion.selectAll();
    }//GEN-LAST:event_buscar_participacionFocusGained

    private void buscar_participacionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_participacionKeyReleased
        // TODO add your handling code here:
        if(buscar_participacion.getText().equals("")){
            Tabla_participacion.setRowSorter(null);
            buscar_participacion.setText("");
            Tabla_participacion.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_participacion);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_participacion.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_participacion.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_participacionKeyReleased

    private void exportar_particiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_particiActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/rem_clie.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Remisionado por cliente", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_participacion.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_participacion.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_participacion.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_participacion.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_participacion.getColumnCount()); j = j + 1) {
                    if (Tabla_participacion.getValueAt(i, j) != null) {
                        if (Tabla_participacion.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_participacion.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_participacion.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_participacion.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_participacion.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_participacion.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_participacion.getValueAt(i, j), arial10fdetalle));
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
    }//GEN-LAST:event_exportar_particiActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        grafica_paticipacion(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void buscar_tintasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_tintasFocusGained
        // TODO add your handling code here:
        buscar_tintas.selectAll();
    }//GEN-LAST:event_buscar_tintasFocusGained

    private void buscar_tintasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_tintasKeyReleased
        // TODO add your handling code here:
        if(buscar_tintas.getText().equals("")){
            Tabla_tintas.setRowSorter(null);
            buscar_tintas.setText("");
            Tabla_tintas.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_tintas);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_tintas.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_tintas.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_tintasKeyReleased

    private void exportar_tintasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_tintasActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/rem_tintas.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Remisionado por Tintas", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_tintas.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_tintas.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_tintas.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_tintas.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_tintas.getColumnCount()); j = j + 1) {
                    if (Tabla_tintas.getValueAt(i, j) != null) {
                        if (Tabla_tintas.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_tintas.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_tintas.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_tintas.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_tintas.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_tintas.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_tintas.getValueAt(i, j), arial10fdetalle));
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
    }//GEN-LAST:event_exportar_tintasActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        grafica_tintas(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void buscar_disenosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_disenosFocusGained
        // TODO add your handling code here:
        buscar_disenos.selectAll();
    }//GEN-LAST:event_buscar_disenosFocusGained

    private void buscar_disenosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_disenosKeyReleased
        // TODO add your handling code here:
        if(buscar_disenos.getText().equals("")){
            Tabla_disenos.setRowSorter(null);
            buscar_disenos.setText("");
            Tabla_disenos.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_disenos);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_disenos.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_disenos.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_disenosKeyReleased

    private void exportar_disenosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_disenosActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/rem_disen.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Remisionado por diseños", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_disenos.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_disenos.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_disenos.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_disenos.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_disenos.getColumnCount()); j = j + 1) {
                    if (Tabla_disenos.getValueAt(i, j) != null) {
                        if (Tabla_disenos.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_disenos.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_disenos.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_disenos.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_disenos.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_disenos.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_disenos.getValueAt(i, j), arial10fdetalle));
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
    }//GEN-LAST:event_exportar_disenosActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        grafica_disenos(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void buscar_resisFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_resisFocusGained
        // TODO add your handling code here:
        buscar_resis.selectAll();
    }//GEN-LAST:event_buscar_resisFocusGained

    private void buscar_resisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_resisKeyReleased
        // TODO add your handling code here:
        if(buscar_resis.getText().equals("")){
            Tabla_resis.setRowSorter(null);
            buscar_resis.setText("");
            Tabla_resis.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_resis);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_resis.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_resis.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_resisKeyReleased

    private void exportar_resisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_resisActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/rem_resis.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Remisionado por resistencias", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_resis.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_resis.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_resis.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_resis.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_resis.getColumnCount()); j = j + 1) {
                    if (Tabla_resis.getValueAt(i, j) != null) {
                        if (Tabla_resis.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_resis.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_resis.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_resis.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_resis.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_resis.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_resis.getValueAt(i, j), arial10fdetalle));
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
    }//GEN-LAST:event_exportar_resisActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        grafica_resis(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica");
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla_acum_art;
    private javax.swing.JTable Tabla_disenos;
    private javax.swing.JTable Tabla_participacion;
    private javax.swing.JTable Tabla_resis;
    private javax.swing.JTable Tabla_tintas;
    private javax.swing.JPanel acum;
    private javax.swing.JButton btnimprimir;
    private javax.swing.JTextField buscar_acumulado;
    private javax.swing.JTextField buscar_disenos;
    private javax.swing.JTextField buscar_participacion;
    private javax.swing.JTextField buscar_resis;
    private javax.swing.JTextField buscar_tintas;
    private javax.swing.JTextField cantmf;
    private javax.swing.JTextField cantnf;
    private javax.swing.JPanel disenos;
    private javax.swing.JButton exportar_acum;
    private javax.swing.JButton exportar_disenos;
    private javax.swing.JButton exportar_partici;
    private javax.swing.JButton exportar_resis;
    private javax.swing.JButton exportar_tintas;
    private javax.swing.JInternalFrame frame_disenos;
    private javax.swing.JInternalFrame frame_participacion;
    private javax.swing.JInternalFrame frame_resis;
    private javax.swing.JInternalFrame frame_tintas;
    private javax.swing.JFileChooser guarda_file;
    private javax.swing.JTextField imptmf;
    private javax.swing.JTextField imptnf;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField kgtmf;
    private javax.swing.JTextField kgtnf;
    private javax.swing.JPanel parti;
    private javax.swing.JTextField pesotmf;
    private javax.swing.JTextField pesotnf;
    private javax.swing.JTextField pktmf;
    private javax.swing.JTextField pktnf;
    private javax.swing.JPanel resis;
    private javax.swing.JPanel tintas;
    private javax.swing.JTextField txt_res_acumulado;
    private javax.swing.JTextField txt_res_disenos;
    private javax.swing.JTextField txt_res_participacion;
    private javax.swing.JTextField txt_res_resis;
    private javax.swing.JTextField txt_res_tintas;
    private javax.swing.JLabel txtfechas;
    // End of variables declaration//GEN-END:variables

}
