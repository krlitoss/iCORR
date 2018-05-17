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
public class reporte_remisiones_clientes extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot1=null;
    ListSelectionModel modeloseleccion;
    ResultSet rs0=null,rs1=null,rs2=null;
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
    Calendar calendar5diasantes = new GregorianCalendar();
    String usuariorem="";

    /** Creates new form usuarios */
    public reporte_remisiones_clientes(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        usuariorem=conf.getProperty("UserID");
        conn=connt;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modeloseleccion = Tabladatos.getSelectionModel();
        ajusteTabladatos();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datosClientes(conf.getProperty("FechaIni"),conf.getProperty("FechaFin"));
        //fecha de fin de semana
        calendar5diasantes.setTime(new Date());
        calendar5diasantes.add(Calendar.DATE, -5);

        modeloseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumaop=0,sumam2=0;
                    Double sumacantidad=0.0,sumakg=0.0,sumaimporte=0.0,sumaporcentaje=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabladatos.getValueAt(i,3)!=null && !Tabladatos.getValueAt(i,3).equals("")){ //suma los metros lineales
                                    sumaop+=Integer.parseInt(""+Tabladatos.getValueAt(i,3));
                            }
                            if(Tabladatos.getValueAt(i,4)!=null && !Tabladatos.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumacantidad+=Double.parseDouble(""+Tabladatos.getValueAt(i,4));
                            }
                            if(Tabladatos.getValueAt(i,5)!=null && !Tabladatos.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabladatos.getValueAt(i,5));
                            }
                            if(Tabladatos.getValueAt(i,6)!=null && !Tabladatos.getValueAt(i,6).equals("")){ //suma los metros lineales
                                    sumam2+=Integer.parseInt(""+Tabladatos.getValueAt(i,6));
                            }
                            if(Tabladatos.getValueAt(i,7)!=null && !Tabladatos.getValueAt(i,7).equals("")){ //suma los metros lineales
                                    sumaimporte+=Double.parseDouble(""+Tabladatos.getValueAt(i,7));
                            }
                            if(Tabladatos.getValueAt(i,10)!=null && !Tabladatos.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaporcentaje+=Double.parseDouble(""+Tabladatos.getValueAt(i,10));
                            }
                        }
                    }
                    noregistros.setText("OP's: "+estandarentero.format(sumaop)+"    Cantidad: "+estandarentero.format(sumacantidad)+"    KG: "+estandarentero.format(sumakg)+"    M2: "+estandarentero.format(sumam2)+"    Importe: "+moneda0decimales.format(sumaimporte)+"    PK: "+moneda2decimales.format(sumaimporte/sumakg)+"    Peso Prom.: "+fijo3decimales.format(sumakg/sumacantidad)+"    "+fijo2decimales.format(sumaporcentaje)+" %");
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

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(250);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(50);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(110);

    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datosClientes(String fi,String ft){
        this.setTitle("::: REMISIONADO POR CLIENTES :::");
        Double kgperiodo=0.0;
        Double kgperiodomaquila=0.0;
        //obtienes los kilogramos totales
        rs0=null;
        try{
            String senSQL="SELECT count(remisiones_detalle.remision) as cuenta,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN ops ON remisiones_detalle.op=ops.op WHERE (remisiones_detalle.estatus='Act' AND ops.maquila<>'Si' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59'));";
            rs0=conexion.consulta(senSQL,conn);
            if(rs0.next()){
                kgperiodo=rs0.getDouble("totalkg");
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //obtienes los kilogramos totales de maquila
        rs0=null;
        try{
            String senSQL="SELECT count(remisiones_detalle.remision) as cuenta,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN ops ON remisiones_detalle.op=ops.op WHERE (remisiones_detalle.estatus='Act' AND ops.maquila='Si' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59'));";
            rs0=conexion.consulta(senSQL,conn);
            if(rs0.next()){
                kgperiodomaquila=rs0.getDouble("totalkg");
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        //consulta remisionado de todos los clientes
        rs0=null;
        try{
            String senSQL="SELECT clientes.nombre,ops.maquila,max(clientes.id_clientes) as clavecliente,count(remisiones_detalle.op) as cuentaop,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN (ops LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) ON remisiones_detalle.op=ops.op WHERE (remisiones_detalle.estatus='Act' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59')) GROUP BY clientes.nombre,ops.maquila ORDER BY ops.maquila,sum(remisiones_detalle.cantidadpzas*articulos.kg) DESC;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad=rs0.getDouble("totalcantidad");
                Double kg=rs0.getDouble("totalkg");
                Double importe=rs0.getDouble("importe");
                Double porcentaje=(kg/kgperiodo)*100;
                String maquila=rs0.getString("maquila");
                if(maquila.equals("Si"))
                    porcentaje=(kg/kgperiodomaquila)*100;

                Object datos[]={rs0.getString("maquila"),rs0.getString("clavecliente"),rs0.getString("nombre"),rs0.getInt("cuentaop"),Integer.parseInt(fijo0decimales.format(cantidad)),Integer.parseInt(fijo0decimales.format(kg)),rs0.getInt("totalm2"),importe,Double.parseDouble(fijo2decimales.format(importe/kg)),Double.parseDouble(fijo2decimales.format(kg/cantidad)),Double.parseDouble(fijo2decimales.format(porcentaje))};
                modelot1.addRow(datos);
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    public void datosComercializador(String fi,String ft){
        this.setTitle("::: REMISIONADO POR CLIENTES-COMERCIALIZADOR :::");
        Double kgperiodo=0.0;
        Double kgperiodomaquila=0.0;
        //obtienes los kilogramos totales
        rs0=null;
        try{
            String senSQL="SELECT count(remisiones_detalle.remision) as cuenta,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN ops ON remisiones_detalle.op=ops.op WHERE (remisiones_detalle.estatus='Act' AND ops.maquila<>'Si' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59'));";
            rs0=conexion.consulta(senSQL,conn);
            if(rs0.next()){
                kgperiodo=rs0.getDouble("totalkg");
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //obtienes los kilogramos totales de maquila
        rs0=null;
        try{
            String senSQL="SELECT count(remisiones_detalle.remision) as cuenta,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN ops ON remisiones_detalle.op=ops.op WHERE (remisiones_detalle.estatus='Act' AND ops.maquila='Si' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59'));";
            rs0=conexion.consulta(senSQL,conn);
            if(rs0.next()){
                kgperiodomaquila=rs0.getDouble("totalkg");
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


        //consulta remisionado de todos los clientes
        rs0=null;
        try{
            String senSQL="SELECT clientes.nombre,ops.maquila,max(clientes.id_clientes) as clavecliente,count(remisiones_detalle.op) as cuentaop,sum(remisiones_detalle.cantidadpzas) as totalcantidad,sum(remisiones_detalle.cantidadpzas*ops.preciounitario) as importe,sum(remisiones_detalle.cantidadpzas*articulos.kg) as totalkg,sum(remisiones_detalle.cantidadpzas*articulos.m2) as totalm2 FROM ((remisiones_detalle LEFT JOIN (remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes) ON remisiones_detalle.remision=remisiones.remision) LEFT JOIN articulos ON remisiones_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN ops ON remisiones_detalle.op=ops.op WHERE (remisiones_detalle.estatus='Act' AND (remisiones.fechahora>='"+fi+" 00:00:00' AND remisiones.fechahora<='"+ft+" 23:59:59')) GROUP BY clientes.nombre,ops.maquila ORDER BY ops.maquila,sum(remisiones_detalle.cantidadpzas*articulos.kg) DESC;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double cantidad=rs0.getDouble("totalcantidad");
                Double kg=rs0.getDouble("totalkg");
                Double importe=rs0.getDouble("importe");
                Double porcentaje=(kg/kgperiodo)*100;
                String maquila=rs0.getString("maquila");
                if(maquila.equals("Si"))
                    porcentaje=(kg/kgperiodomaquila)*100;

                Object datos[]={rs0.getString("maquila"),rs0.getString("clavecliente"),rs0.getString("nombre"),rs0.getInt("cuentaop"),Integer.parseInt(fijo0decimales.format(cantidad)),Integer.parseInt(fijo0decimales.format(kg)),rs0.getInt("totalm2"),importe,Double.parseDouble(fijo2decimales.format(importe/kg)),Double.parseDouble(fijo2decimales.format(kg/cantidad)),Double.parseDouble(fijo2decimales.format(porcentaje))};
                modelot1.addRow(datos);
            }
            if(rs0!=null) {
                rs0.close();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
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
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(reporte_remisiones_clientes.class);
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
                "Maquila", "Clave Cliente", "Cliente", "OP's", "Cantidad", "KG", "M2", "$ Importe", "$ P.K.", "Peso Prom.", "% Participación"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

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
                .addContainerGap(302, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
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
       
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

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
            datosClientes(fechainsertar.format(busca_fechas.getFechaini()),fechainsertar.format(busca_fechas.getFechafin()));
        }
        busca_fechas = null;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/rem_clientes.xls");
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
            datosComercializador(fechainsertar.format(busca_fechas.getFechaini()),fechainsertar.format(busca_fechas.getFechafin()));
        }
        busca_fechas = null;
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField buscar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables

}
