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
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
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
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public class requerimiento_resistencias extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot1=null;
    ListSelectionModel modeloseleccion;
    ResultSet rs0;
    DecimalFormat moneda4decimales=new DecimalFormat("#,###,##0.0000");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MMM-yy");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");

    String fechaini_guarda="";
    String fechafin_guarda="";
    private Properties conf;
    String valor_privilegio="1";

    /** Creates new form usuarios */
    public requerimiento_resistencias(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conn=connt;
        conf=conexion.archivoInicial();
        modelot1=(DefaultTableModel) Tabladatos_papel.getModel();
        Tabladatos_papel.setModel(modelot1);
        modeloseleccion = Tabladatos_papel.getSelectionModel();
        ajusteTabladatos();
        datos_privilegios();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos_papel.setRowSorter(elQueOrdena);
        datos();

        modeloseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    Double sumakg=0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabladatos_papel.getValueAt(i,2)!=null && !Tabladatos_papel.getValueAt(i,2).equals("")){ //suma los metros lineales
                                    sumakg+=Double.parseDouble(""+Tabladatos_papel.getValueAt(i,2));
                            }
                        }
                    }
                    noregistros.setText("KG Total: "+estandarentero.format(sumakg));
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
        valor_privilegio=conexion.obtener_privilegios(conn,"Requerimiento Papel");
    }
    public void ajusteTabladatos() {
        Tabladatos_papel.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos_papel.getColumnModel().getColumn(1).setPreferredWidth(200);
        Tabladatos_papel.getColumnModel().getColumn(2).setPreferredWidth(100);

        Tabladatos_papel.getColumnModel().getColumn(0).setCellRenderer(new Renderer());
        Tabladatos_papel.getColumnModel().getColumn(2).setCellRenderer(new Renderer());
        Tabladatos_papel.getColumnModel().getColumn(4).setCellRenderer(new Renderer());
        Tabladatos_papel.getColumnModel().getColumn(6).setCellRenderer(new Renderer());
        Tabladatos_papel.getColumnModel().getColumn(8).setCellRenderer(new Renderer());
        Tabladatos_papel.getColumnModel().getColumn(10).setCellRenderer(new Renderer());
        Tabladatos_papel.getColumnModel().getColumn(12).setCellRenderer(new Renderer());
     
    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos(){

        busca_fechas busca_fechas = new busca_fechas(null, true);
        busca_fechas.setLocationRelativeTo(this);
        busca_fechas.setVisible(true);
        String estado = busca_fechas.getEstado();
        /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if (estado.equals("cancelar")) {
        } else {

            limpiatabla();
            fechaini_guarda=fechainsertar.format(busca_fechas.getFechaini());
            fechafin_guarda=fechainsertar.format(busca_fechas.getFechafin());

            rs0 = null;
            try {
                String senSQL = "SELECT resistencias.clave,max(resistencias.descripcion) as descripcion,max(resistencias.l1r) as l1r,max(resistencias.r1r) as r1r,max(resistencias.l2r) as l2r,max(resistencias.r2r) as r2r,max(resistencias.l3r) as l3r,COALESCE(sum(pendiente_corr.kg_pendientes),0) as kg_pendientes FROM resistencias LEFT JOIN (SELECT ops.op,ops.fechaentrega,ops.maquila,((ops.cantidad-COALESCE(prodcorr.prodcantidadpiezas,0))*articulos.kg) as kg_pendientes,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,clientes.nombre,prodcorr.fechaprod,COALESCE(prodcorr.prodml,0) as prodml,COALESCE(prodcorr.prodcantidad,0) as prodcantidad,COALESCE(prodcorr.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prodcorr.prodcantkgpiezas,0) as prodcantkgpiezas,programcorr.fechaprogram,COALESCE(programcorr.programcantidad,0) as programcantidad,COALESCE(programcorr.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(programcorr.programcantkgpiezas,0) as programcantkgpiezas,articulos_maquinas.clave FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='CORR' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prodcorr ON ops.op=prodcorr.op ) LEFT JOIN (SELECT programa_corr_detalle.op,max(programa_corr.fechaproduccion) as fechaprogram,sum(programa_corr_detalle.laminas) as programcantidad,sum(articulos.piezas*programa_corr_detalle.laminas) as programcantidadpiezas,sum(articulos.piezas*programa_corr_detalle.laminas*articulos.kg) as programcantkgpiezas FROM (programa_corr INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo  WHERE (programa_corr.estatus<>'Can' AND programa_corr.estatus<>'Pen') GROUP BY programa_corr_detalle.op) as programcorr ON ops.op=programcorr.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND (ops.fechaentrega>='"+fechainsertar.format(busca_fechas.getFechaini())+" 00:00:00' AND ops.fechaentrega<='"+fechainsertar.format(busca_fechas.getFechafin())+" 23:59:59') AND articulos_maquinas.clave='CORR' AND ((ops.cantidad-COALESCE(prodcorr.prodcantidadpiezas,0))*articulos.kg)>0 ) ORDER BY ops.fechaentrega,ops.op) AS pendiente_corr ON resistencias.clave=pendiente_corr.claveresistencia WHERE COALESCE(pendiente_corr.kg_pendientes,0)>0 GROUP BY resistencias.clave ORDER BY resistencias.clave DESC;";
                rs0 = conexion.consulta(senSQL, conn);
                while (rs0.next()) {
                    String l1=""+rs0.getString("l1r");
                    String r1=""+rs0.getString("r1r");
                    String l2=""+rs0.getString("l2r");
                    String r2=""+rs0.getString("r2r");
                    String l3=""+rs0.getString("l3r");
                    Double gl1=0.0;
                    Double gr1=0.0;
                    Double gl2=0.0;
                    Double gr2=0.0;
                    Double gl3=0.0;

                    Double factor_arrollamiento_c=1.48;
                    Double factor_arrollamiento_b=1.35;

                    if(l1.length()>0 && !l1.equals("null"))
                        gl1=Double.parseDouble("0."+l1.charAt(1)+l1.charAt(2)+l1.charAt(3));
                    if(r1.length()>0 && !r1.equals("null"))
                        gr1=Double.parseDouble("0."+r1.charAt(1)+r1.charAt(2)+r1.charAt(3))*factor_arrollamiento_c;
                    if(l2.length()>0 && !l2.equals("null"))
                        gl2=Double.parseDouble("0."+l2.charAt(1)+l2.charAt(2)+l2.charAt(3));
                    if(r2.length()>0 && !r2.equals("null"))
                        gr2=Double.parseDouble("0."+r2.charAt(1)+r2.charAt(2)+r2.charAt(3))*factor_arrollamiento_b;
                    if(l3.length()>0 && !l3.equals("null"))
                        gl3=Double.parseDouble("0."+l3.charAt(1)+l3.charAt(2)+l3.charAt(3));

                    Double gramostotal=gl1+gr1+gl2+gr2+gl3;
                    Double kgtotal=rs0.getDouble("kg_pendientes");
                    Object datos[] = {rs0.getString("clave"), rs0.getString("descripcion"), kgtotal,l1,((kgtotal/gramostotal)*gl1),r1,((kgtotal/gramostotal)*gr1),l2,((kgtotal/gramostotal)*gl2),r2,((kgtotal/gramostotal)*gr2),l3,((kgtotal/gramostotal)*gl3)};
                    modelot1.addRow(datos);
                }
                if (rs0 != null) {  rs0.close();   }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

        }
        busca_fechas = null;

    }

    class Renderer extends SubstanceDefaultTableCellRenderer {
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {

                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                if(column==0){
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                }
                if(column!=0){
                    text = estandarentero.format( Double.parseDouble(""+value) );
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                }

                if(text.equals("null"))
                    text=null;

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

        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos_papel = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnexportar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(requerimiento_resistencias.class);
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

        Tabladatos_papel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Resistencia", "Descripcion", "KG Total", "L1", "L1_kg", "R2", "R2_kg", "L2", "L2_kg", "R2", "R2_kg", "L3", "L3_kg"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladatos_papel.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos_papel.setName("Tabladatos_papel"); // NOI18N
        Tabladatos_papel.setRowHeight(22);
        Tabladatos_papel.getTableHeader().setReorderingAllowed(false);
        Tabladatos_papel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabladatos_papelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos_papel);

        jPanel1.setName("jPanel1"); // NOI18N

        noregistros.setFont(resourceMap.getFont("noregistros.font")); // NOI18N
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
                .addContainerGap(735, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(noregistros)
                .addContainerGap(2, Short.MAX_VALUE))
        );

        jToolBar2.setBackground(resourceMap.getColor("jToolBar2.background")); // NOI18N
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setName("jToolBar2"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar2.add(jSeparator4);

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
        jToolBar2.add(jButton1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar2.add(jSeparator1);

        btnexportar.setIcon(resourceMap.getIcon("btnexportar.icon")); // NOI18N
        btnexportar.setText(resourceMap.getString("btnexportar.text")); // NOI18N
        btnexportar.setToolTipText(resourceMap.getString("btnexportar.toolTipText")); // NOI18N
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
        jToolBar2.add(btnexportar);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar2.add(jSeparator2);

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton2.setMaximumSize(new java.awt.Dimension(100, 48));
        jButton2.setMinimumSize(new java.awt.Dimension(100, 48));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(100, 48));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton2);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar2.add(jSeparator5);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(514, 48));

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

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(206, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jToolBar2.add(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        // TODO add your handling code here:
        if(buscar.getText().equals("")){
            Tabladatos_papel.setRowSorter(null);
            buscar.setText("");
            Tabladatos_papel.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladatos_papel.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscarKeyReleased

    private void Tabladatos_papelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabladatos_papelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Tabladatos_papelMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnexportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarActionPerformed
        // TODO add your handling code here:
        File rutaarchivo=new File(System.getProperty("user.home")+"/req_resis.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, ""+this.getTitle()+"( "+fechamediana.format(fechainsertar.parse(fechaini_guarda))+" al "+fechamediana.format(fechainsertar.parse(fechafin_guarda))+" )",arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for(int j=0;j<(Tabladatos_papel.getColumnCount());j=j+1){
                String titu=""+Tabladatos_papel.getColumnName(j);
                int dotPos = titu.lastIndexOf(">")+1;//le quita el html de los titulos
                if(titu.contains("<html>")){
                    titu=titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu,arial10ftitulo));
                sheet.setColumnView( j, ((Tabladatos_papel.getColumnModel().getColumn(j).getPreferredWidth())/6) );
            }

            filainicial++;//incrementa las filas
            for(int i=0;i<(Tabladatos_papel.getRowCount());i=i+1){
                for(int j=0;j<(Tabladatos_papel.getColumnCount());j=j+1){
                    if(Tabladatos_papel.getValueAt(i, j) != null){
                        if(Tabladatos_papel.getValueAt(i, j) instanceof String){
                            String dato=(String)Tabladatos_papel.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">")+1;//le quita el html de los titulos
                            if(dato.contains("<html>")){
                                dato=dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i+filainicial, dato,arial10fdetalle));
                        }
                        else if(Tabladatos_papel.getValueAt(i, j) instanceof java.lang.Number)
                            sheet.addCell(new jxl.write.Number(j, i+filainicial, Double.parseDouble(Tabladatos_papel.getValueAt(i, j).toString()),arial10fdetalle));
                        else if(Tabladatos_papel.getValueAt(i, j) instanceof java.util.Date)
                            sheet.addCell(new jxl.write.DateTime(j, i+filainicial, (java.util.Date)Tabladatos_papel.getValueAt(i, j), jxl.write.DateTime.GMT));
                        else
                            sheet.addCell(new jxl.write.Boolean(j, i+filainicial,(java.lang.Boolean)Tabladatos_papel.getValueAt(i, j),arial10fdetalle));
                    }
                }
            }/**fin de revisar los campos vacios*/


            //Escribimos los resultados al fichero Excel
            workbook.write();
            workbook.close();
        } catch (IOException ex) {
           JOptionPane.showMessageDialog(this,"EL ARCHIVO ESTA ABIERTO O NO SE PUEDE CREAR"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        } catch (WriteException exe) {
           JOptionPane.showMessageDialog(this,"ERROR AL EXPORTAR DATOS"+exe,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        } catch (Exception exi) {
           JOptionPane.showMessageDialog(this,"ERROR AL EXPORTAR DATOS"+exi,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }


        //abrir el documento creado
        try {
          Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"NO SE PUEDE ABRIR EL ARCHIVO\n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
}//GEN-LAST:event_btnexportarActionPerformed

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
    }//GEN-LAST:event_buscarFocusGained

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        datos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //actualiza los datos de los papeles en una tabla temporal
        String senSQLinv="DELETE FROM requerimiento_papel;";
        conexion.modificamov_p(senSQLinv, conn,valor_privilegio);
        for(int i=0;i<(Tabladatos_papel.getRowCount());i=i+1){
            for(int j=3;j<(12);j=j+2){
                String p1=""+Tabladatos_papel.getValueAt(i, j);
                if(!p1.equals("") && !p1.equals("null")){
                    String senSQL="INSERT INTO requerimiento_papel(clavepapel,kg) VALUES ('"+p1+"', '"+(Double)Tabladatos_papel.getValueAt(i, j+1)+"');";
                    conexion.modificamov_p(senSQL,conn,valor_privilegio);
                }
            }
        }
        //envia los datos a excel
        File rutaarchivo=new File(System.getProperty("user.home")+"/req_papel.xls");
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
            sheet.addImage(new jxl.write.WritableImage(0, 0, 2, 4,new java.io.File(conexion.Directorio()+"/logoempresa.png")));
            sheet.addCell(new jxl.write.Label(0, filainicial, ""+this.getTitle()+"( "+fechamediana.format(fechainsertar.parse(fechaini_guarda))+" al "+fechamediana.format(fechainsertar.parse(fechafin_guarda))+" )",arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            sheet.addCell(new jxl.write.Label(0, filainicial, "Clave Papel",arial10ftitulo));
            sheet.setColumnView( 0, 22 );
            sheet.addCell(new jxl.write.Label(1, filainicial, "KG",arial10ftitulo));
            sheet.setColumnView( 1, 15 );

            filainicial++;
            rs0=null;
            try{
                String senSQL="SELECT requerimiento_papel.clavepapel,sum(requerimiento_papel.kg) as kg FROM requerimiento_papel GROUP BY clavepapel ORDER BY clavepapel,sum(requerimiento_papel.kg);";
                rs0=conexion.consulta(senSQL,conn);
                while(rs0.next()){
                    sheet.addCell(new jxl.write.Label(0, filainicial, rs0.getString("clavepapel"),arial10fdetalle));
                    sheet.addCell(new jxl.write.Number(1, filainicial, rs0.getDouble("kg"),arial10fdetalle));
                    filainicial++;
                }
                if(rs0!=null) {  rs0.close(); }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //Escribimos los resultados al fichero Excel
            workbook.write();
            workbook.close();
        } catch (IOException ex) {
           JOptionPane.showMessageDialog(this,"EL ARCHIVO ESTA ABIERTO O NO SE PUEDE CREAR"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        } catch (WriteException exe) {
           JOptionPane.showMessageDialog(this,"ERROR AL EXPORTAR DATOS"+exe,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        } catch (Exception exi) {
           JOptionPane.showMessageDialog(this,"ERROR AL EXPORTAR DATOS"+exi,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }


        //abrir el documento creado
        try {
          Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"NO SE PUEDE ABRIR EL ARCHIVO\n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
        

    }//GEN-LAST:event_jButton2ActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos_papel;
    private javax.swing.JButton btnexportar;
    private javax.swing.JTextField buscar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables

}
