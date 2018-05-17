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
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
public class programas_corr extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot1=null;
    ResultSet rs0;
    DecimalFormat moneda4decimales=new DecimalFormat("#,###,##0.0000");
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
    public programas_corr(Connection connt) {
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
        datos();
        sumaregistros();
         modelot1.addTableModelListener(new TableModelListener() {
                  public void tableChanged(TableModelEvent te) {
                        try{
                            int f = te.getLastRow();//Fila modificada
                            int c = te.getColumn();//columna
                            TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                            if (c == 14) { //multiplica los anchos
                                if(model.getValueAt(f, c) != null && model.getValueAt(f, 0) != null){
                                    int secuencia=(Integer)model.getValueAt(f, c); //actualiza las secuencias
                                    if(secuencia>0){
                                        String senSQLmov="UPDATE programa_corr SET imprimir='"+model.getValueAt(f, 14)+"' WHERE id_programa_corr='"+model.getValueAt(f, 0)+"';";
                                        conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                                        limpiatabla();
                                        datos();
                                    }
                                }
                            }
                            if (c == 15) {
                                String valimp=""+model.getValueAt(f, c);
                                String pc=""+model.getValueAt(f, 0);
                                if(!pc.equals("null") && !pc.equals("")){
                                    String senSQLmov="UPDATE programa_corr SET imprimir2='"+valimp+"' WHERE id_programa_corr='"+model.getValueAt(f, 0)+"';";
                                    conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                                }
                            }

                        }catch(Exception e){
                            JOptionPane.showMessageDialog(null, "NO SE PUEDEN CALCULAR LOS DATOS"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        }
                  }
         });
        modelot1seleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumaml=0,sumakg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabladatos.getValueAt(i,2)!=null && !Tabladatos.getValueAt(i,2).equals("")){ //suma los metros lineales
                                sumaml+=Integer.parseInt(""+Tabladatos.getValueAt(i,2));
                                cuenta++;
                            }
                            if(Tabladatos.getValueAt(i,3)!=null && !Tabladatos.getValueAt(i,3).equals(""))//suma los kilogramos
                                sumakg+=Integer.parseInt(""+Tabladatos.getValueAt(i,3));
                        }
                    }
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"          ML: "+estandarentero.format(sumaml)+"          KG: "+estandarentero.format(sumakg));
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
        valor_privilegio=conexion.obtener_privilegios(conn,"Programar Corrugadora");
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(55);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(55);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(55);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(110);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(95);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(240);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(12).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(13).setPreferredWidth(70);
        
        Tabladatos.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tabladatos.getColumnModel().getColumn(15).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(16).setPreferredWidth(250);
    }


    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos(){
        rs0=null;
        try{
            String senSQL="SELECT programa_corr.*,programa_corr_detalle.*,articulos.claveresistencia as cr,articulos.articulo,articulos.largo,articulos.ancho,articulos.scores,ops.fechaentrega,clientes.nombre,sumaml.fechaprod,COALESCE(sumaml.totalml,0) as totalml,COALESCE(sumaml.totalkg,0) as totalkg FROM ( ((programa_corr LEFT JOIN (SELECT programa_corr_captura.id_programa_corr,max(fechareal) as fechaprod,sum(prodml) as totalml,sum(prodkg) as totalkg FROM programa_corr_captura WHERE programa_corr_captura.estatus<>'Can' GROUP BY programa_corr_captura.id_programa_corr) as sumaml ON programa_corr.id_programa_corr=sumaml.id_programa_corr) INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN ops ON programa_corr_detalle.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_corr_detalle.clavearticulo=articulos.clavearticulo WHERE programa_corr.estatus='Act' ORDER BY programa_corr.fechaproduccion,programa_corr.imprimir,programa_corr_detalle.id_programa_corr_detalle;";
            rs0=conexion.consulta(senSQL,conn);
            String bandera="";
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);

                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>";
                    }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>";
                    }
                }
                String noprograma=rs0.getString("id_programa_corr");
                if(bandera.equals(noprograma)){
                    Object datos[]={"","","","",null,rs0.getString("op"),color+sfe,rs0.getString("claveresistencia"),rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getInt("laminas"),null,"","","",false,rs0.getString("nombre")};
                    modelot1.addRow(datos);
                }else{
                    Object datos[]={noprograma,rs0.getString("estatus"),rs0.getInt("ml"),rs0.getInt("kg"),rs0.getDate("fechaproduccion"),rs0.getString("op"),color+sfe,rs0.getString("claveresistencia"),rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getInt("laminas"),rs0.getDate("fechaprod"),rs0.getInt("totalml"),rs0.getInt("totalkg"),rs0.getInt("imprimir"),rs0.getBoolean("imprimir2"),rs0.getString("nombre")};
                    modelot1.addRow(datos);
                    bandera=noprograma;
                }
            }
            if(rs0!=null) {   rs0.close();     }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTAx\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

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
        menuterminar = new javax.swing.JMenuItem();
        menuactivar = new javax.swing.JMenuItem();
        menueliminar = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButton6 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        menupop.setName("menupop"); // NOI18N
        menupop.setPreferredSize(new java.awt.Dimension(130, 120));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(programas_corr.class);
        detalle.setIcon(resourceMap.getIcon("detalle.icon")); // NOI18N
        detalle.setText(resourceMap.getString("detalle.text")); // NOI18N
        detalle.setName("detalle"); // NOI18N
        detalle.setPreferredSize(new java.awt.Dimension(130, 30));
        detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleActionPerformed(evt);
            }
        });
        menupop.add(detalle);

        menuterminar.setIcon(resourceMap.getIcon("menuterminar.icon")); // NOI18N
        menuterminar.setText(resourceMap.getString("menuterminar.text")); // NOI18N
        menuterminar.setName("menuterminar"); // NOI18N
        menuterminar.setPreferredSize(new java.awt.Dimension(130, 30));
        menuterminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuterminarActionPerformed(evt);
            }
        });
        menupop.add(menuterminar);

        menuactivar.setIcon(resourceMap.getIcon("menuactivar.icon")); // NOI18N
        menuactivar.setText(resourceMap.getString("menuactivar.text")); // NOI18N
        menuactivar.setName("menuactivar"); // NOI18N
        menuactivar.setPreferredSize(new java.awt.Dimension(130, 30));
        menuactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuactivarActionPerformed(evt);
            }
        });
        menupop.add(menuactivar);

        menueliminar.setIcon(resourceMap.getIcon("menueliminar.icon")); // NOI18N
        menueliminar.setText(resourceMap.getString("menueliminar.text")); // NOI18N
        menueliminar.setName("menueliminar"); // NOI18N
        menueliminar.setPreferredSize(new java.awt.Dimension(130, 30));
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
                "Programa", "Status", "ML", "KG", "Fecha Producción", "OP", "Fecha Entrega", "Resis.", "Clave Articulo", "Articulo", "Laminas", "<html><font color=green>Fecha Prod.", "<html><font color=green>ML Prod.", "<html><font color=green>KG Prod.","Orden","Imprimir", "Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.util.Date.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false,false, false,true,true, false
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

        jButton6.setIcon(resourceMap.getIcon("jButton6.icon")); // NOI18N
        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setToolTipText(resourceMap.getString("jButton6.toolTipText")); // NOI18N
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton6.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton6.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton6.setName("jButton6"); // NOI18N
        jButton6.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jSeparator7.setName("jSeparator7"); // NOI18N
        jToolBar1.add(jSeparator7);

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

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setToolTipText(resourceMap.getString("jButton3.toolTipText")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton3.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton3.setMinimumSize(new java.awt.Dimension(70, 48));
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
                .addContainerGap(65, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
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
        if (evt.getClickCount() ==2){
            modificar();
            limpiatabla();
            datos();
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

    private void menuterminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuterminarActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        String senSQLmov="UPDATE programa_corr SET estatus='Ter' WHERE id_programa_corr='"+(String)Tabladatos.getValueAt(filano, 0)+"';";
        conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
        limpiatabla();
        datos();
}//GEN-LAST:event_menuterminarActionPerformed

    private void TabladatosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_TabladatosMouseReleased

    private void detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleActionPerformed
        // TODO add your handling code here:
        modificar();
    }//GEN-LAST:event_detalleActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         String senSQLdinamico="";

        busca_pendientes_cliente busca_pendientes_cliente = new busca_pendientes_cliente(null,true,conn,"");
        busca_pendientes_cliente.setLocationRelativeTo(this);
        busca_pendientes_cliente.setVisible(true);
        String estado=busca_pendientes_cliente.getEstado();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if(estado.equals("cancelar")){

        }else{

            if(busca_pendientes_cliente.getProveedor().equals("Buscar")){ /**verifica si buscas un solo proveedor o todos*/
                senSQLdinamico="AND clientes.id_clientes='"+busca_pendientes_cliente.getID()+"' ";
            }
            if(busca_pendientes_cliente.getOPstatus().equals("Act")){ /**verifica si buscas un solo proveedor o todos*/
                senSQLdinamico=senSQLdinamico+"AND programa_corr.estatus='Act' ";
            }
            if(busca_pendientes_cliente.getfechastatus().equals("Rango")){ /**verifica si buscas un solo proveedor o todos*/
                senSQLdinamico=senSQLdinamico+"AND (programa_corr.fecha>='"+fechainsertar.format(busca_pendientes_cliente.getFechaini())+" 00:00:00' AND programa_corr.fecha<='"+fechainsertar.format(busca_pendientes_cliente.getFechafin())+" 23:59:59') ";
            }


            limpiatabla();

            rs0=null;
            try{
                String senSQL="SELECT programa_corr.*,programa_corr_detalle.*,articulos.claveresistencia as cr,articulos.articulo,articulos.largo,articulos.ancho,articulos.scores,ops.fechaentrega,clientes.nombre,sumaml.fechaprod,COALESCE(sumaml.totalml,0) as totalml,COALESCE(sumaml.totalkg,0) as totalkg FROM ( ((programa_corr LEFT JOIN (SELECT programa_corr_captura.id_programa_corr,max(fechareal) as fechaprod,sum(prodml) as totalml,sum(prodkg) as totalkg FROM programa_corr_captura WHERE programa_corr_captura.estatus<>'Can' GROUP BY programa_corr_captura.id_programa_corr) as sumaml ON programa_corr.id_programa_corr=sumaml.id_programa_corr) INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN ops ON programa_corr_detalle.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_corr_detalle.clavearticulo=articulos.clavearticulo WHERE (programa_corr.estatus<>'Can' AND programa_corr.estatus<>'Pen' "+senSQLdinamico+") ORDER BY programa_corr.fechaproduccion,programa_corr.imprimir,programa_corr_detalle.id_programa_corr_detalle";
                rs0=conexion.consulta(senSQL,conn);
                String bandera="";
                while(rs0.next()){
                    Date fechaentrega=rs0.getDate("fechaentrega");
                    String sfe="";
                    String color="<html><font color=green><b>";
                    if(!(""+fechaentrega).equals("null")){
                        sfe=fechamascorta.format(fechaentrega);
                        Calendar calendarentrega = new GregorianCalendar();
                        calendarentrega.setTime(fechaentrega);

                        if(calendarentrega.before(calendariniciosem)){
                            color="<html><font color=#DC143C><b>";
                        }
                        if(calendarentrega.after(calendarfinsem)){
                            color="<html><font color=#0080FF><b>";
                        }
                    }
                    String noprograma=rs0.getString("id_programa_corr");
                    if(bandera.equals(noprograma)){
                        Object datos[]={"","","","",null,rs0.getString("op"),color+sfe,rs0.getString("claveresistencia"),rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getInt("laminas"),null,"","","",false,rs0.getString("nombre")};
                        modelot1.addRow(datos);
                    }else{
                        Object datos[]={noprograma,rs0.getString("estatus"),rs0.getInt("ml"),rs0.getInt("kg"),rs0.getDate("fechaproduccion"),rs0.getString("op"),color+sfe,rs0.getString("claveresistencia"),rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getInt("laminas"),rs0.getDate("fechaprod"),rs0.getInt("totalml"),rs0.getInt("totalkg"),rs0.getInt("imprimir"),rs0.getBoolean("imprimir2"),rs0.getString("nombre")};
                        modelot1.addRow(datos);
                        bandera=noprograma;
                    }
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        String observaciones=JOptionPane.showInputDialog(this, "INTRODUCE OBSERVACIONES ADICIONALES", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        
        JasperPrint jasperPrint =null;
        try{
            //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio()+"/logoempresa.png");
            pars.put("logoempresa",new FileInputStream(fichero));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("version", resourceMap.getString("Application.title"));
            pars.put("observaciones", observaciones);
            JasperReport masterReport = null;
            try{
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/programa_corr.jasper"));
            } catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

            jasperPrint = JasperFillManager.fillReport(masterReport,pars,conn);
                //manda la venta de la impresora con las copias solicitadas
            JasperPrintManager.printReport( jasperPrint, true);
        } catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }

        JasperPrint jasperPrint2 =null;
        try{
            //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio()+"/logoempresa.png");
            pars.put("logoempresa",new FileInputStream(fichero));
            pars.put("logoempresa2",new FileInputStream(fichero));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("version", resourceMap.getString("Application.title"));
            pars.put("observaciones", observaciones);
            JasperReport masterReport = null;
            try{
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/programa_corr2.jasper"));
            } catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

            jasperPrint2= JasperFillManager.fillReport(masterReport,pars,conn);
            //manda la venta de la impresora con las copias solicitadas
            JasperPrintManager.printReport( jasperPrint2, true);
        } catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }

        //deja los valores de impresion en 0 para nueva secuencia
        //String senSQL="UPDATE programa_corr SET imprimir='0';";
        //conexion.modificamov(senSQL,conn);
        limpiatabla();
        datos();

       
}//GEN-LAST:event_jButton3ActionPerformed

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

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        limpiatabla();
        datos();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        datos_programas_corr = new datos_programas_corr(null,true,conn,"",valor_privilegio);
        datos_programas_corr.setLocationRelativeTo(this);
        datos_programas_corr.setVisible(true);
        limpiatabla();
        datos_programas_corr=null;
        datos();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        modificar();
        limpiatabla();
        datos();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void menuactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuactivarActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        String senSQLmov="UPDATE programa_corr SET estatus='Act' WHERE id_programa_corr='"+(String)Tabladatos.getValueAt(filano, 0)+"';";
        conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
        limpiatabla();
        datos();
    }//GEN-LAST:event_menuactivarActionPerformed

    private void menueliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menueliminarActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        int laminasprod=Integer.parseInt(""+Tabladatos.getValueAt(filano, 12));

        if(!motivoelimina.equals("") && !motivoelimina.equals("null") && laminasprod<=0){
            //guarda los datos en la bitacora para rastreo de datos
            String senSQLmov="UPDATE programa_corr SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_corr='"+(String)Tabladatos.getValueAt(filano, 0)+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_corr='"+(String)Tabladatos.getValueAt(filano, 0)+"';";
            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
            limpiatabla();
            datos();
        }
    }//GEN-LAST:event_menueliminarActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField buscar;
    private javax.swing.JMenuItem detalle;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
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
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem menuactivar;
    private javax.swing.JMenuItem menueliminar;
    private javax.swing.JPopupMenu menupop;
    private javax.swing.JMenuItem menuterminar;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_programas_corr;
}
