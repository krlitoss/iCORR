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
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jxl.*;
import jxl.write.*;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public class APruebas_combina_corr extends javax.swing.JInternalFrame {

    Connection conn = null,concontrol = null;
    DefaultTableModel modelot1 = null,modelot2 = null;
    ResultSet rs0;
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    DecimalFormat fijo0decimales = new DecimalFormat("######0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo1decimales=new DecimalFormat("######0.0");
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat estandar1decimal=new DecimalFormat("#,###,##0.0");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MMM-yy");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    DecimalFormat fijo2enteros=new DecimalFormat("#####00");
    DecimalFormat porcentaje2decimal=new DecimalFormat("##0.00 %");
    private Properties conf;
    Calendar calendariniciosem = new GregorianCalendar();
    Calendar calendarfinsem = new GregorianCalendar();
    int totalregistros=0,totalregistroscombinados=0;

    /** Creates new form usuarios */
    public APruebas_combina_corr(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        conn = connt;
        conexioncontrolmaestro();
        modelot1 = (DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modelot2 = (DefaultTableModel) Tabladatos2.getModel();
        Tabladatos2.setModel(modelot2);
        ajusteTabladatos();

        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        limpiatabla();
        datos();
        sumaregistros();

    }

    public void salir() {
        if (conn != null) {
            conn = null;
        }
        if(concontrol!=null){       ////****eliminar para el control maestro*******////////
            concontrol=null;
            System.out.println("cerrar la conexion:\n");
        }
        dispose();
        this.setVisible(false);
    }
    public void conexioncontrolmaestro(){       ////****eliminar para el control maestro*******////////
        if(concontrol!=null){

        }else{
            concontrol=conexion.abrirconexioncontrol();
        }
    }

    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(5);

        Tabladatos.getColumnModel().getColumn(0).setCellRenderer(new Renderer());
        Tabladatos.getColumnModel().getColumn(1).setCellRenderer(new Renderer());
        Tabladatos.getColumnModel().getColumn(2).setCellRenderer(new Renderer());
        Tabladatos.getColumnModel().getColumn(3).setCellRenderer(new Renderer());
        Tabladatos.getColumnModel().getColumn(4).setCellRenderer(new Renderer());
        Tabladatos.getColumnModel().getColumn(5).setCellRenderer(new Renderer());
        Tabladatos.getColumnModel().getColumn(6).setCellRenderer(new Renderer());
        Tabladatos.getColumnModel().getColumn(7).setCellRenderer(new Renderer());
        Tabladatos.getColumnModel().getColumn(8).setCellRenderer(new Renderer());
        Tabladatos.getColumnModel().getColumn(9).setCellRenderer(new Renderer());

        Tabladatos2.getColumnModel().getColumn(0).setPreferredWidth(55);
        Tabladatos2.getColumnModel().getColumn(4).setCellRenderer(new Renderer2());

    }

    public void limpiatabla() {
        modelot1.setNumRows(0);
        modelot2.setNumRows(0);
    }
    public void cargadatos(){
        //elimina datos de la tabla
        String senSQLinv="DELETE FROM pruebas_corr";
        conexion.modificamov(senSQLinv, conn);
        //carga los datos
        rs0=null;
        try{
            String senSQL="SELECT Corrugadora.Programa, Corrugadora.Fecha, [Programas Corrugadora].[Ancho Total], [Programas Corrugadora].[Ancho Util], [Programas Corrugadora].Resistencia, [Programas Corrugadora].[Rest 1° OP], [Programas Corrugadora].[Clave 1º Articulo], Articulos.Ancho AS A1, [Programas Corrugadora].[1Pliegos Ancho] AS 1Pliegos, IIf([Articulos_1].[Cliente]='0066' Or [Articulos_1].[Cliente]='0063' Or [Articulos_1].[Cliente]='0053','MUEBLERA',[Programas Corrugadora].[Rest 2° OP]) AS 2OP, [Programas Corrugadora].[Clave 2º Articulo], IIf([Articulos_1].[Cliente]='0066' Or [Articulos_1].[Cliente]='0063' Or [Articulos_1].[Cliente]='0053',0,[Articulos_1].[Ancho]) AS A2, IIf([Articulos_1].[Cliente]='0066' Or [Articulos_1].[Cliente]='0063' Or [Articulos_1].[Cliente]='0053',0,[Programas Corrugadora].[2Pliegos Ancho]) AS 2Pliegos, IIf([Articulos_2].[Cliente]='0066' Or [Articulos_2].[Cliente]='0063' Or [Articulos_2].[Cliente]='0053','MUEBLERA',[Programas Corrugadora].[Rest 3° OP]) AS 3OP, [Programas Corrugadora].[Clave 3º Articulo], IIf([Articulos_2].[Cliente]='0066' Or [Articulos_2].[Cliente]='0063' Or [Articulos_2].[Cliente]='0053',0,[Articulos_2].[Ancho]) AS A3, IIf([Articulos_2].[Cliente]='0066' Or [Articulos_2].[Cliente]='0063' Or [Articulos_2].[Cliente]='0053',0,[Programas Corrugadora].[3Pliegos Ancho]) AS 3Pliegos FROM (Articulos AS Articulos_2 RIGHT JOIN (Articulos AS Articulos_1 RIGHT JOIN (Articulos RIGHT JOIN [Programas Corrugadora] ON Articulos.[Clave Articulo] = [Programas Corrugadora].[Clave 1º Articulo]) ON Articulos_1.[Clave Articulo] = [Programas Corrugadora].[Clave 2º Articulo]) ON Articulos_2.[Clave Articulo] = [Programas Corrugadora].[Clave 3º Articulo]) RIGHT JOIN Corrugadora ON [Programas Corrugadora].Programa = Corrugadora.Programa WHERE (((Corrugadora.Fecha)>=#8/1/2010# And (Corrugadora.Fecha)<=#3/31/2011#) AND ((Corrugadora.Transf)=True)) ORDER BY [Programas Corrugadora].Resistencia, [Programas Corrugadora].[Ancho Util] DESC , [Programas Corrugadora].Fecha;";
            rs0=conexion.consulta(senSQL,concontrol);
            while(rs0.next()){
                String resis=""+rs0.getString("Resistencia");
                if(!(resis.equals("") || resis.equals("null"))){
                    Double a1=rs0.getDouble("A1");
                    Double a2=rs0.getDouble("A2");
                    Double a3=rs0.getDouble("A3");
                    int p1=rs0.getInt("1Pliegos");
                    int p2=rs0.getInt("2Pliegos");
                    int p3=rs0.getInt("3Pliegos");
                    Double anchounuevo=(a1*p1)+(a2*p2)+(a3*p3);

                    senSQL="INSERT INTO pruebas_corr(programa, fecha, ancho_total, ancho_util, clave_resistencia, op1, clave_art1, ancho1, pliegos1, op2, clave_art2, ancho2, pliegos2, op3, clave_art3, ancho3, pliegos3, ancho_total_nuevo, ancho_util_nuevo) VALUES ('"+rs0.getInt("Programa")+"', '"+fechainsertar.format(rs0.getDate("Fecha"))+"', '"+rs0.getDouble("Ancho Total")+"', '"+rs0.getDouble("Ancho Util")+"', '"+resis+"', '"+rs0.getString("Rest 1° OP")+"', '"+rs0.getString("Clave 1º Articulo")+"', '"+a1+"', '"+p1+"', '"+rs0.getString("2OP")+"', '"+rs0.getString("Clave 2º Articulo")+"', '"+a2+"', '"+p2+"', '"+rs0.getString("3OP")+"', '"+rs0.getString("Clave 3º Articulo")+"', '"+a3+"', '"+p3+"', '0', '"+anchounuevo+"');";
                    senSQL=senSQL.replace("null","");
                    conexion.modificamov(senSQL,conn);
                }
            }
            if(rs0!=null) { rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        datos();

    }
    public void datos(){
        totalregistros=0;
        totalregistroscombinados=0;
        Double totaltrimantes=0.0;
        Double totaltrimnuevo=0.0;
        rs0=null;
        try{
            String senSQL="SELECT pruebas_corr.* FROM pruebas_corr ORDER BY id_pruebas_corr;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double at=rs0.getDouble("ancho_total");
                Double au=rs0.getDouble("ancho_util");
                Double atn=rs0.getDouble("ancho_total_nuevo");
                Double aun=rs0.getDouble("ancho_util_nuevo");
                totaltrimantes+=(at-au);
                Object datos[]={rs0.getInt("programa"),fechacorta.format(rs0.getTimestamp("fecha")),at,au,(at-au),atn,aun,(atn-aun),rs0.getString("clave_resistencia"),rs0.getString("color")};
                modelot1.addRow(datos);
                totalregistros++;
                if(atn>0.0){
                    totalregistroscombinados++;
                    totaltrimnuevo+=(atn-aun);
                }
            }
            if(rs0!=null) { rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        txttotal.setText(estandarentero.format(totalregistros));
        txtcomb.setText(estandarentero.format(totalregistroscombinados));
        txtpor.setText(porcentaje2decimal.format(Double.parseDouble(""+totalregistroscombinados)/Double.parseDouble(""+totalregistros)));
        txtt.setText(estandar1decimal.format(totaltrimantes/Double.parseDouble(""+totalregistros)));
        txttn.setText(estandar1decimal.format(totaltrimnuevo/Double.parseDouble(""+totalregistroscombinados)));
        //anchos guardados
        rs0=null;
        try{
            String senSQL="SELECT pruebas_corr_ancho.* FROM pruebas_corr_ancho ORDER BY id_pruebas_corr_ancho;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double par=rs0.getDouble("partidas");
                Object datos[]={rs0.getString("id_pruebas_corr_ancho"),rs0.getDouble("ancho_total"),estandarentero.format(par),porcentaje2decimal.format(par/Double.parseDouble(""+totalregistros)),rs0.getString("color")};
                modelot2.addRow(datos);
            }
            if(rs0!=null) { rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    public void recombina(String ancho,String colorfin){

        rs0=null;
        try{
            //captura un nuevo color
            String cadenacolor=colorfin;
            
            //numero de partidas editadas
            int partidasfin=0;
            int index_corr_ancho=1;
            String senSQL="SELECT max(id_pruebas_corr_ancho) as max FROM pruebas_corr_ancho";
            rs0=conexion.consulta(senSQL,conn);
            if(rs0.next()){
                index_corr_ancho=rs0.getInt("max")+1;
            }
            if(rs0!=null) { rs0.close(); }
            
            senSQL="SELECT pruebas_corr.* FROM pruebas_corr ORDER BY id_pruebas_corr;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Double atn=rs0.getDouble("ancho_total_nuevo");
                if(atn<=0.0){
                    Double anchotnuevo=Double.parseDouble(ancho);
                    Double aun=rs0.getDouble("ancho_util_nuevo");
                    Double trimn=anchotnuevo-aun;
                    if(trimn>=Double.parseDouble(txttrim1.getText()) && trimn<=Double.parseDouble(txttrim2.getText())){
                        senSQL="UPDATE pruebas_corr SET ancho_total_nuevo='"+anchotnuevo+"',combinado='true',color='"+cadenacolor+"',id_pruebas_corr_ancho='"+index_corr_ancho+"' WHERE id_pruebas_corr='"+rs0.getString("id_pruebas_corr")+"';";
                        conexion.modificamov(senSQL,conn);
                        partidasfin++;
                    }
                }
            }
            if(rs0!=null) { rs0.close(); }
            //registra el movimiento
            senSQL="INSERT INTO pruebas_corr_ancho(id_pruebas_corr_ancho, ancho_total, color, partidas) VALUES ('"+index_corr_ancho+"', '"+Double.parseDouble(ancho)+"', '"+cadenacolor+"', '"+partidasfin+"');";
            conexion.modifica(senSQL,conn);
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }


    public void sumaregistros() {
        noregistros.setText("Registros: " + Tabladatos.getRowCount());
    }
    class Renderer extends SubstanceDefaultTableCellRenderer {
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {

                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                String textpunto = ""+value;
                
                String valf=""+modelot1.getValueAt(row, 9);
                if(!(valf.equals("null") || valf.equals(""))){
                    valf = valf.replace("]", "");
                    valf = valf.replace("[", "");
                    valf = valf+",";
                    String[] arraycolor = valf.split(",");
                    rend.setBackground(new Color(Integer.parseInt(arraycolor[0]),Integer.parseInt(arraycolor[1]),Integer.parseInt(arraycolor[2])));
                }
                if(column==2 || column==3 || column==4 || column==5 || column==6 || column==7){
                    text = fijo1decimales.format( Double.parseDouble(""+value) );
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    int dotPos2 = textpunto.lastIndexOf(".")+1;
                    String partedecimal2 = textpunto.substring(dotPos2);
                    if(partedecimal2.length() > 1){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                }
                if(text.equals("null")){
                    text=null;
                }
                if(column==0 || column==1 || column==8){
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                }

                rend.setText( text );
                return rend;
            }

    }
    class Renderer2 extends SubstanceDefaultTableCellRenderer {
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {

                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                String textpunto = ""+value;

                String valf=""+modelot2.getValueAt(row, 4);
                if(!(valf.equals("null") || valf.equals(""))){
                    valf = valf.replace("]", "");
                    valf = valf.replace("[", "");
                    valf = valf+",";
                    String[] arraycolor = valf.split(",");
                    rend.setBackground(new Color(Integer.parseInt(arraycolor[0]),Integer.parseInt(arraycolor[1]),Integer.parseInt(arraycolor[2])));
                }
                if(text.equals("null")){
                    text=null;
                }
                rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

                rend.setText( text );
                return rend;
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

        menupop = new javax.swing.JPopupMenu();
        menueliminar = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabladatos2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtcomb = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        txtpor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txttn = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txttrim1 = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        txttrim2 = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        txtt = new javax.swing.JTextField();

        menupop.setMinimumSize(new java.awt.Dimension(105, 28));
        menupop.setName("menupop"); // NOI18N
        menupop.setPreferredSize(new java.awt.Dimension(130, 34));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(APruebas_combina_corr.class);
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
                "Programa", "Fecha", "Ancho Total", "Ancho Util", "Trim", "Ancho T. N.", "Ancho U. N.", "Trim Nvo.", "Resis", "Comb"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
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
                .addContainerGap(778, Short.MAX_VALUE))
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

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
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

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

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
                .addContainerGap(297, Short.MAX_VALUE)
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

        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        Tabladatos2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Ancho", "Partidas", "%", "Color"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        Tabladatos2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos2.setName("Tabladatos2"); // NOI18N
        Tabladatos2.setRowHeight(20);
        Tabladatos2.getTableHeader().setReorderingAllowed(false);
        Tabladatos2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabladatos2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(Tabladatos2);

        jPanel4.setName("jPanel4"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        txtcomb.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtcomb.setText(resourceMap.getString("txtcomb.text")); // NOI18N
        txtcomb.setName("txtcomb"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        txttotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txttotal.setText(resourceMap.getString("txttotal.text")); // NOI18N
        txttotal.setName("txttotal"); // NOI18N

        txtpor.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtpor.setText(resourceMap.getString("txtpor.text")); // NOI18N
        txtpor.setName("txtpor"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        txttn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txttn.setText(resourceMap.getString("txttn.text")); // NOI18N
        txttn.setName("txttn"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtpor, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcomb, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txttn, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtcomb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtpor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txttn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        txttrim1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        txttrim1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txttrim1.setText(resourceMap.getString("txttrim1.text")); // NOI18N
        txttrim1.setName("txttrim1"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        txttrim2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        txttrim2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txttrim2.setText(resourceMap.getString("txttrim2.text")); // NOI18N
        txttrim2.setName("txttrim2"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        txtt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtt.setText(resourceMap.getString("txtt.text")); // NOI18N
        txtt.setName("txtt"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txttrim1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txttrim2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txttrim1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(txttrim2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
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
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void menueliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menueliminarActionPerformed
        // TODO add your handling code here:
        int filano = Tabladatos2.getSelectedRow();
        //elimina datos de la tabla
        String senSQLinv="DELETE FROM pruebas_corr_ancho WHERE id_pruebas_corr_ancho='"+(String) Tabladatos2.getValueAt(filano, 0)+"';";
        conexion.modificamov(senSQLinv, conn);
        String senSQLmov = "UPDATE pruebas_corr SET ancho_total_nuevo='0',combinado='false',color=null,id_pruebas_corr_ancho=null WHERE id_pruebas_corr_ancho='" + (String) Tabladatos2.getValueAt(filano, 0) + "';";
        conexion.modificaupdate(senSQLmov, conn);
        limpiatabla();
        datos();

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
            cargadatos();
        }
        busca_fechas = null;
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/corr.xls");
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        //recombina(JOptionPane.showInputDialog("Nuevo ancho"));
        Color newColor = JColorChooser.showDialog(this,"SELECCIONA UN COLOR !!!!",Color.blue);
        if(newColor!=null){
            String colorfin=newColor.getRed()+","+newColor.getGreen()+","+newColor.getBlue();
            recombina(JOptionPane.showInputDialog("Nuevo ancho"),colorfin);
        }
        limpiatabla();
        datos();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void Tabladatos2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabladatos2MouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tabladatos2.rowAtPoint(p);
            // variable for the beginning and end selects only that one row.
            Tabladatos2.changeSelection(rowNumber, 0, false, false);
            menupop.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_Tabladatos2MouseClicked
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTable Tabladatos2;
    private javax.swing.JTextField buscar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem menueliminar;
    private javax.swing.JPopupMenu menupop;
    private javax.swing.JLabel noregistros;
    private javax.swing.JTextField txtcomb;
    private javax.swing.JTextField txtpor;
    private javax.swing.JTextField txtt;
    private javax.swing.JTextField txttn;
    private javax.swing.JTextField txttotal;
    private javax.swing.JFormattedTextField txttrim1;
    private javax.swing.JFormattedTextField txttrim2;
    // End of variables declaration//GEN-END:variables
    
   
}

