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

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
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
public class APruebasa_diografia_planeacion extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot_horas=null;
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
    DecimalFormat horasminutos=new DecimalFormat("##########00");

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
    public APruebasa_diografia_planeacion(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conn=connt;
        conf = conexion.archivoInicial();
        //modelos de las tablas
        modelot_horas=(DefaultTableModel) Tabla_horas.getModel();
        Tabla_horas.setModel(modelot_horas);
        ajusteTabladatos();
        horasminutos.setRoundingMode(RoundingMode.DOWN);
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena_participa = new TableRowSorter<TableModel>(modelot_horas);
        Tabla_horas.setRowSorter(elQueOrdena_participa);

        //trae los primeros datos
        todos_datos();

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
        Tabla_horas.getColumnModel().getColumn(0).setPreferredWidth(85);
        Tabla_horas.getColumnModel().getColumn(1).setPreferredWidth(260);
        Tabla_horas.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tabla_horas.getColumnModel().getColumn(3).setPreferredWidth(100);
        Tabla_horas.getColumnModel().getColumn(4).setPreferredWidth(100);
        Tabla_horas.getColumnModel().getColumn(5).setPreferredWidth(100);
        Tabla_horas.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabla_horas.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer());
        Tabla_horas.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer());
        Tabla_horas.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tabla_horas.getColumnModel().getColumn(7).setCellRenderer(new DoubleRenderer());

    }
    public void limpiatabla(){
        modelot_horas.setNumRows(0);

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
            String senSQL = "SELECT ops_pruebas.*,articulos.* FROM ops_pruebas LEFT JOIN articulos ON ops_pruebas.clavearticulo=articulos.clavearticulo WHERE ( (ops_pruebas.fechaentrega>='"+fi+" 00:00:00' AND ops_pruebas.fechaentrega<='"+ft+" 23:59:59')) ORDER BY ops_pruebas.fechaentrega;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad=rs0.getDouble("cantidad");
                Double kg=rs0.getDouble("kg")*cantidad;
                Double importe=(rs0.getDouble("preciomillar")/1000)*cantidad;
                String maq="NO";
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
        
        //grafica de participacion
        grafica_horas(fi,ft,sqlclavecli,"");

    }
    public void grafica_horas(String fi,String ft,String sqlclavecli,String save){
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //consulta
        String sqlcliacum="";
        if(!sqlclavecli.equals("")){
            sqlcliacum=" AND ops_pruebas."+sqlclavecli;
        }
        //resul en null
        modelot_horas.setNumRows(0);
        rs0=null;
        try{
            String senSQL="SELECT maquinas.clave,max(maquinas.descripcion) as desmaquina,sum(op_final.arreglo) as suma_arreglo,sum(op_final.tiempo_prod) as suma_produccion,sum(op_final.cantidad) as piezas,sum(op_final.cantidad_kg) as kg_piezas,count(op_final.op) as cuenta_cambios,avg(op_final.velocidad) as promvelocidad,avg(op_final.arreglo) as promarreglo FROM maquinas RIGHT JOIN (SELECT ops_pruebas.*,articulos_maquinas.*,((ops_pruebas.cantidad/articulos.piezas)/articulos_maquinas.velocidad) as tiempo_prod,(ops_pruebas.cantidad*articulos.kg) as cantidad_kg FROM ops_pruebas INNER JOIN (articulos_maquinas LEFT JOIN articulos ON articulos_maquinas.clavearticulo=articulos.clavearticulo) ON ops_pruebas.clavearticulo=articulos_maquinas.clavearticulo  WHERE ( (ops_pruebas.fechaentrega>='"+fi+" 00:00:00' AND ops_pruebas.fechaentrega<='"+ft+" 23:59:59') AND (articulos_maquinas.clave='TCY' OR articulos_maquinas.clave='MACARB' OR articulos_maquinas.clave='COM' OR articulos_maquinas.clave='SUAP' OR articulos_maquinas.clave='DIEC')"+sqlcliacum+")) as op_final ON maquinas.clave=op_final.clave GROUP BY maquinas.clave";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                String clavemaquina=rs0.getString("clave");
                Double arreglo=rs0.getDouble("suma_arreglo");
                Double prod=rs0.getDouble("suma_produccion");
                String cadena_arreglo=horasminutos.format(arreglo/60)+":"+horasminutos.format(arreglo%60);
                String cadena_prod=horasminutos.format(prod/60)+":"+horasminutos.format(prod%60);
                String cadena_total=horasminutos.format((arreglo+prod)/60)+":"+horasminutos.format((arreglo+prod)%60);
                //AGREGA EL TOTAL EN HORAS A LA GRAFICA
                dataset.addValue(Double.parseDouble(horasminutos.format((arreglo+prod)/60)), clavemaquina, "");
                if(!sqlclavecli.equals("")){
                    //this.setTitle("::: Planeacion ("+rs0.getString("nombrecli")+") :::");
                }
                Object datos[]={rs0.getString("clave"),rs0.getString("desmaquina"),rs0.getInt("piezas"),rs0.getInt("kg_piezas"),cadena_arreglo,cadena_prod,cadena_total,rs0.getInt("cuenta_cambios"),rs0.getInt("promarreglo"),rs0.getInt("promvelocidad")};
                modelot_horas.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }

        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

       rs0=null;
        try{
            String senSQL="SELECT maquinas.clave,max(maquinas.descripcion) as desmaquina,sum(op_final.arreglo) as suma_arreglo,sum(op_final.tiempo_prod) as suma_produccion,sum(op_final.cantidad) as piezas,sum(op_final.cantidad_kg) as kg_piezas,count(op_final.op) as cuenta_cambios,avg(op_final.velocidad) as promvelocidad,avg(op_final.arreglo) as promarreglo FROM maquinas RIGHT JOIN (SELECT ops_pruebas.*,articulos_maquinas.*,(ops_pruebas.cantidad/articulos_maquinas.velocidad) as tiempo_prod,(ops_pruebas.cantidad*articulos.kg) as cantidad_kg FROM ops_pruebas INNER JOIN (articulos_maquinas LEFT JOIN articulos ON articulos_maquinas.clavearticulo=articulos.clavearticulo) ON ops_pruebas.clavearticulo=articulos_maquinas.clavearticulo  WHERE ( (ops_pruebas.fechaentrega>='"+fi+" 00:00:00' AND ops_pruebas.fechaentrega<='"+ft+" 23:59:59') AND (articulos_maquinas.clave='PLTCY' OR articulos_maquinas.clave='PLMACARB' OR articulos_maquinas.clave='ENG' OR articulos_maquinas.clave='EMB' OR articulos_maquinas.clave='DES' OR articulos_maquinas.clave='MAN' OR articulos_maquinas.clave='FON')"+sqlcliacum+")) as op_final ON maquinas.clave=op_final.clave GROUP BY maquinas.clave";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                String clavemaquina=rs0.getString("clave");
                Double arreglo=rs0.getDouble("suma_arreglo");
                Double prod=rs0.getDouble("suma_produccion");
                String cadena_arreglo=horasminutos.format(arreglo/60)+":"+horasminutos.format(arreglo%60);
                String cadena_prod=horasminutos.format(prod/60)+":"+horasminutos.format(prod%60);
                String cadena_total=horasminutos.format((arreglo+prod)/60)+":"+horasminutos.format((arreglo+prod)%60);
                //AGREGA EL TOTAL EN HORAS A LA GRAFICA
                dataset.addValue(Double.parseDouble(horasminutos.format((arreglo+prod)/60)), clavemaquina, "");
                if(!sqlclavecli.equals("")){
                    //this.setTitle("::: Planeacion ("+rs0.getString("nombrecli")+") :::");
                }
                Object datos[]={rs0.getString("clave"),rs0.getString("desmaquina"),rs0.getInt("piezas"),rs0.getInt("kg_piezas"),cadena_arreglo,cadena_prod,cadena_total,rs0.getInt("cuenta_cambios"),rs0.getInt("promarreglo"),rs0.getInt("promvelocidad")};
                modelot_horas.addRow(datos);
            }
            if(rs0!=null) { rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }



        rs0=null;
        try{
            String senSQL="SELECT maquinas.clave,max(maquinas.descripcion) as desmaquina,sum(op_final.arreglo) as suma_arreglo,sum(op_final.tiempo_prod) as suma_produccion,sum(op_final.cantidad) as piezas,sum(op_final.cantidad_kg) as kg_piezas,count(op_final.op) as cuenta_cambios,avg(op_final.velocidad) as promvelocidad,avg(op_final.arreglo) as promarreglo FROM maquinas RIGHT JOIN (SELECT ops_pruebas.*,articulos_maquinas.*,((ops_pruebas.cantidad/10)/articulos_maquinas.velocidad) as tiempo_prod,(ops_pruebas.cantidad*articulos.kg) as cantidad_kg FROM ops_pruebas INNER JOIN (articulos_maquinas LEFT JOIN articulos ON articulos_maquinas.clavearticulo=articulos.clavearticulo) ON ops_pruebas.clavearticulo=articulos_maquinas.clavearticulo  WHERE ( (ops_pruebas.fechaentrega>='"+fi+" 00:00:00' AND ops_pruebas.fechaentrega<='"+ft+" 23:59:59') AND (articulos_maquinas.clave='PDO')"+sqlcliacum+")) as op_final ON maquinas.clave=op_final.clave GROUP BY maquinas.clave";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                String clavemaquina=rs0.getString("clave");
                Double arreglo=rs0.getDouble("suma_arreglo");
                Double prod=rs0.getDouble("suma_produccion");
                String cadena_arreglo=horasminutos.format(arreglo/60)+":"+horasminutos.format(arreglo%60);
                String cadena_prod=horasminutos.format(prod/60)+":"+horasminutos.format(prod%60);
                String cadena_total=horasminutos.format((arreglo+prod)/60)+":"+horasminutos.format((arreglo+prod)%60);
                //AGREGA EL TOTAL EN HORAS A LA GRAFICA
                dataset.addValue(Double.parseDouble(horasminutos.format((arreglo+prod)/60)), clavemaquina, "");
                if(!sqlclavecli.equals("")){
                    //this.setTitle("::: Planeacion ("+rs0.getString("nombrecli")+") :::");
                }
                Object datos[]={rs0.getString("clave"),rs0.getString("desmaquina"),rs0.getInt("piezas"),rs0.getInt("kg_piezas"),cadena_arreglo,cadena_prod,cadena_total,rs0.getInt("cuenta_cambios"),rs0.getInt("promarreglo"),rs0.getInt("promvelocidad")};
                modelot_horas.addRow(datos);
            }
            if(rs0!=null) {     rs0.close();    }

        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        final JFreeChart chart_horas = ChartFactory.createBarChart3D(
            "",         // chart title
            "Maquinas",               // domain axis label
            "Horas",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        CategoryPlot plot = chart_horas.getCategoryPlot();
        //plot.setLabelFont(font10plain);
        plot.setNoDataMessage("No hay datos");
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.12);
        CategoryAxis domainAxisrep = plot.getDomainAxis();
        domainAxisrep.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        LegendTitle leg=chart_horas.getLegend();
        leg.setPosition(RectangleEdge.RIGHT);
        leg.setItemFont(font10plain);
        chart_horas.getTitle().setFont(font12bold);
        //genera la grafica en el panel
        ChartPanel Panel_horas = new ChartPanel(chart_horas);
        frame_horas.setContentPane(Panel_horas);

        if(!save.equals("")){//funcion para guardar la imagen
            try
             {
                String rutafinal="";
                try{
                    File nuevo=new File(System.getProperty("user.home")+"/graf_horas.jpg");
                    guarda_file.setSelectedFile(nuevo);
                    if(guarda_file.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                        rutafinal=String.valueOf(guarda_file.getSelectedFile());
                    }
                }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR GRAFICA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                if(!rutafinal.equals("")){
                    ChartUtilities.saveChartAsJPEG(new File(rutafinal), chart_horas, 800,500);
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
        carga = new javax.swing.JPanel();
        frame_horas = new javax.swing.JInternalFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla_horas = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        buscar_horas = new javax.swing.JTextField();
        exportar_horas = new javax.swing.JButton();
        txt_res_participacion = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(APruebasa_diografia_planeacion.class);
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
                .addContainerGap(231, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel8.TabConstraints.tabTitle"), jPanel8); // NOI18N

        carga.setName("carga"); // NOI18N

        frame_horas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame_horas.setTitle(resourceMap.getString("frame_horas.title")); // NOI18N
        frame_horas.setFrameIcon(resourceMap.getIcon("frame_horas.frameIcon")); // NOI18N
        frame_horas.setName("frame_horas"); // NOI18N
        frame_horas.setVisible(true);

        javax.swing.GroupLayout frame_horasLayout = new javax.swing.GroupLayout(frame_horas.getContentPane());
        frame_horas.getContentPane().setLayout(frame_horasLayout);
        frame_horasLayout.setHorizontalGroup(
            frame_horasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 755, Short.MAX_VALUE)
        );
        frame_horasLayout.setVerticalGroup(
            frame_horasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 271, Short.MAX_VALUE)
        );

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        Tabla_horas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Clave Maquina", "Descripcion", "Cantidad", "KG", "Total Arreglo", "Total Produccion", "Total Tiempo", "OP's", "Avg_Arreglo", "Avg_Vel."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
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
        Tabla_horas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla_horas.setName("Tabla_horas"); // NOI18N
        Tabla_horas.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(Tabla_horas);

        jPanel5.setBackground(resourceMap.getColor("jPanel5.background")); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(resourceMap.getIcon("jLabel3.icon")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        buscar_horas.setName("buscar_horas"); // NOI18N
        buscar_horas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscar_horasFocusGained(evt);
            }
        });
        buscar_horas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_horasKeyReleased(evt);
            }
        });

        exportar_horas.setIcon(resourceMap.getIcon("exportar_horas.icon")); // NOI18N
        exportar_horas.setText(resourceMap.getString("exportar_horas.text")); // NOI18N
        exportar_horas.setMargin(new java.awt.Insets(0, 4, 0, 4));
        exportar_horas.setName("exportar_horas"); // NOI18N
        exportar_horas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar_horasActionPerformed(evt);
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
                        .addComponent(buscar_horas, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportar_horas, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(txt_res_participacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar_horas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportar_horas)
                        .addComponent(jButton2))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout cargaLayout = new javax.swing.GroupLayout(carga);
        carga.setLayout(cargaLayout);
        cargaLayout.setHorizontalGroup(
            cargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(frame_horas)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        cargaLayout.setVerticalGroup(
            cargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cargaLayout.createSequentialGroup()
                .addComponent(frame_horas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("carga.TabConstraints.tabTitle"), carga); // NOI18N

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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE))
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

    private void buscar_horasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscar_horasFocusGained
        // TODO add your handling code here:
        buscar_horas.selectAll();
    }//GEN-LAST:event_buscar_horasFocusGained

    private void buscar_horasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_horasKeyReleased
        // TODO add your handling code here:
        if(buscar_horas.getText().equals("")){
            Tabla_horas.setRowSorter(null);
            buscar_horas.setText("");
            Tabla_horas.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot_horas);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar_horas.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabla_horas.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscar_horasKeyReleased

    private void exportar_horasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportar_horasActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/pen_horas.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, "Pendientes por tiempo", arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabla_horas.getColumnCount()); j = j + 1) {
                String titu = "" + Tabla_horas.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabla_horas.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabla_horas.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabla_horas.getColumnCount()); j = j + 1) {
                    if (Tabla_horas.getValueAt(i, j) != null) {
                        if (Tabla_horas.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabla_horas.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabla_horas.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabla_horas.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabla_horas.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabla_horas.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabla_horas.getValueAt(i, j), arial10fdetalle));
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
    }//GEN-LAST:event_exportar_horasActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        grafica_horas(fechaini_guarda,fechafin_guarda,sqlcli_guarda,"grafica");
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla_horas;
    private javax.swing.JButton btnimprimir;
    private javax.swing.JTextField buscar_horas;
    private javax.swing.JTextField cantmf;
    private javax.swing.JTextField cantnf;
    private javax.swing.JPanel carga;
    private javax.swing.JButton exportar_horas;
    private javax.swing.JInternalFrame frame_horas;
    private javax.swing.JFileChooser guarda_file;
    private javax.swing.JTextField imptmf;
    private javax.swing.JTextField imptnf;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField kgtmf;
    private javax.swing.JTextField kgtnf;
    private javax.swing.JTextField pesotmf;
    private javax.swing.JTextField pesotnf;
    private javax.swing.JTextField pktmf;
    private javax.swing.JTextField pktnf;
    private javax.swing.JTextField txt_res_participacion;
    private javax.swing.JLabel txtfechas;
    // End of variables declaration//GEN-END:variables

}
