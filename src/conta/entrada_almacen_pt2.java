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

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


/**
 *
 * @author IVONNE
 */
public class entrada_almacen_pt2 extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot1=null;
    ListSelectionModel modeloseleccion;
    ResultSet rs0;
    private Properties conf;
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String valor_privilegio="1";

    /** Creates new form usuarios */
    public entrada_almacen_pt2(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        conn=connt;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modeloseleccion = Tabladatos.getSelectionModel();
        ajusteTabladatos();
        datos_privilegios();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datos();

        modeloseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int sumacantidad=0,sumakg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tabladatos.getValueAt(i,7)!=null && !Tabladatos.getValueAt(i,7).equals("")){ //suma los metros lineales
                                    sumacantidad+=Integer.parseInt(""+Tabladatos.getValueAt(i,7));
                            }
                            if(Tabladatos.getValueAt(i,9)!=null && !Tabladatos.getValueAt(i,9).equals("")){ //suma los metros lineales
                                    sumakg+=Integer.parseInt(""+Tabladatos.getValueAt(i,9));
                            }
                        }
                    }
                    noregistros.setText("Cantidad: "+estandarentero.format(sumacantidad)+"        KG: "+estandarentero.format(sumakg));
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
        valor_privilegio=conexion.obtener_privilegios(conn,"Entrada almacen PT");
    }
    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(65);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(200);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(250);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(70);

    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos(){
        rs0=null;
        try{
            String senSQL="SELECT entradas_almacen_pt.*,articulos.articulo,articulos.kg,clientes.nombre FROM entradas_almacen_pt LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON entradas_almacen_pt.clavearticulo=articulos.clavearticulo WHERE (entradas_almacen_pt.estatus<>'Can' AND (entradas_almacen_pt.fecha>='"+conf.getProperty("FechaIni")+" 00:00:00' AND entradas_almacen_pt.fecha<='"+conf.getProperty("FechaFin")+" 23:59:59')) ORDER BY entradas_almacen_pt.fecha;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                int cantidad=rs0.getInt("cantidadpzas");
                int kg=(int) (cantidad * rs0.getDouble("kg"));
                Object datos[]={rs0.getString("id_entrada_almacen_pt"),rs0.getDate("fecha"),rs0.getString("turno"),rs0.getString("op"),rs0.getString("nombre"),rs0.getString("clavearticulo"),rs0.getString("articulo"),cantidad,rs0.getInt("pzaspaquete"),kg};
                modelot1.addRow(datos);
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    public void modificar(){
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String clavemodifica=(String)Tabladatos.getValueAt(filano, 0);
            if(clavemodifica.equals("")){

            }else{
                datos_entrada_almacen_pt2 = new datos_entrada_almacen_pt2(null,true,conn,clavemodifica,valor_privilegio);
                datos_entrada_almacen_pt2.setLocationRelativeTo(this);
                datos_entrada_almacen_pt2.setVisible(true);
                limpiatabla();
                datos_entrada_almacen_pt2=null;
                datos();
            }
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
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnnuevo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btncon = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnimpirmir = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();

        menupop.setMinimumSize(new java.awt.Dimension(105, 28));
        menupop.setName("menupop"); // NOI18N
        menupop.setPreferredSize(new java.awt.Dimension(130, 34));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(entrada_almacen_pt2.class);
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
                "ID", "Fecha", "Turno", "OP", "Cliente", "Clave Articulo", "Articulo", "Cantidad", "Pzas x Paq", "KG"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.util.Date.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
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
        });
        jScrollPane1.setViewportView(Tabladatos);

        jToolBar1.setBackground(resourceMap.getColor("jToolBar1.background")); // NOI18N
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        btnnuevo.setIcon(resourceMap.getIcon("btnnuevo.icon")); // NOI18N
        btnnuevo.setText(resourceMap.getString("btnnuevo.text")); // NOI18N
        btnnuevo.setFocusable(false);
        btnnuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnnuevo.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnnuevo.setMaximumSize(new java.awt.Dimension(70, 48));
        btnnuevo.setMinimumSize(new java.awt.Dimension(70, 48));
        btnnuevo.setName("btnnuevo"); // NOI18N
        btnnuevo.setPreferredSize(new java.awt.Dimension(70, 48));
        btnnuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnnuevo);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        btncon.setIcon(resourceMap.getIcon("btncon.icon")); // NOI18N
        btncon.setText(resourceMap.getString("btncon.text")); // NOI18N
        btncon.setFocusable(false);
        btncon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btncon.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btncon.setMaximumSize(new java.awt.Dimension(70, 48));
        btncon.setMinimumSize(new java.awt.Dimension(70, 48));
        btncon.setName("btncon"); // NOI18N
        btncon.setPreferredSize(new java.awt.Dimension(70, 48));
        btncon.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btncon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnconActionPerformed(evt);
            }
        });
        jToolBar1.add(btncon);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

        btnimpirmir.setIcon(resourceMap.getIcon("btnimpirmir.icon")); // NOI18N
        btnimpirmir.setText(resourceMap.getString("btnimpirmir.text")); // NOI18N
        btnimpirmir.setFocusable(false);
        btnimpirmir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnimpirmir.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnimpirmir.setMaximumSize(new java.awt.Dimension(70, 48));
        btnimpirmir.setMinimumSize(new java.awt.Dimension(70, 48));
        btnimpirmir.setName("btnimpirmir"); // NOI18N
        btnimpirmir.setPreferredSize(new java.awt.Dimension(70, 48));
        btnimpirmir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnimpirmir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimpirmirActionPerformed(evt);
            }
        });
        jToolBar1.add(btnimpirmir);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(556, 48));

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
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(322, Short.MAX_VALUE)
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
                .addContainerGap())
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-853)/2, (screenSize.height-553)/2, 853, 553);
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
            limpiatabla();
            datos();
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
        if (evt.getClickCount() ==2){
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

    private void btnnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoActionPerformed
        // TODO add your handling code here:
        datos_entrada_almacen_pt2 = new datos_entrada_almacen_pt2(null,true,conn,"",valor_privilegio);
        datos_entrada_almacen_pt2.setLocationRelativeTo(this);
        datos_entrada_almacen_pt2.setVisible(true);
        limpiatabla();
        datos_entrada_almacen_pt2=null;
        datos();
}//GEN-LAST:event_btnnuevoActionPerformed

    private void btnimpirmirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimpirmirActionPerformed
        // TODO add your handling code here:
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        busca_fechas busca_fechas = new busca_fechas(null,true);
        busca_fechas.setLocationRelativeTo(this);
        busca_fechas.setVisible(true);
        String estado=busca_fechas.getEstado();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if(estado.equals("cancelar")){

        }else{

            JasperViewer visor=null;
            JasperPrint jasperPrint =null;
            try{
                String datos="REPORTE GENERADO DEL "+fechamediana.format(busca_fechas.getFechaini())+"  AL  "+fechamediana.format(busca_fechas.getFechafin());
                Map pars = new HashMap();
                File fichero = new File(conexion.Directorio()+"/logoempresa.png");
                pars.put("logoempresa",new FileInputStream(fichero));
                pars.put("subtitulo", datos);
                pars.put("fechaini", new Timestamp(busca_fechas.getFechaini().getTime()));
                pars.put("fechafin", new Timestamp(busca_fechas.getFechafin().getTime()));
                pars.put("senSQL", "");
                pars.put("version", resourceMap.getString("Application.title"));
                JasperReport masterReport = null;
                try{
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/entrada_almacen_pt.jasper"));
                 }
                 catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

                jasperPrint = JasperFillManager.fillReport(masterReport,pars,conn);
                visor = new JasperViewer(jasperPrint,false);
                visor.setTitle("REPORTE");
                visor.setVisible(true);
            }
            catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }
        }

        busca_fechas=null;
}//GEN-LAST:event_btnimpirmirActionPerformed

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
    }//GEN-LAST:event_buscarFocusGained

    private void btnconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnconActionPerformed
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
                String senSQL="SELECT entradas_almacen_pt.*,articulos.articulo,articulos.kg,clientes.nombre FROM entradas_almacen_pt LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON entradas_almacen_pt.clavearticulo=articulos.clavearticulo WHERE (entradas_almacen_pt.estatus<>'Can' AND (entradas_almacen_pt.fecha>='"+fechainsertar.format(busca_fechas.getFechaini())+" 00:00:00' AND entradas_almacen_pt.fecha<='"+fechainsertar.format(busca_fechas.getFechafin())+" 23:59:59')) ORDER BY entradas_almacen_pt.fecha;";
                rs0=conexion.consulta(senSQL,conn);
                while(rs0.next()){
                    int cantidad=rs0.getInt("cantidadpzas");
                    int kg=(int) (cantidad * rs0.getDouble("kg"));
                    Object datos[]={rs0.getString("id_entrada_almacen_pt"),rs0.getDate("fecha"),rs0.getString("turno"),rs0.getString("op"),rs0.getString("nombre"),rs0.getString("clavearticulo"),rs0.getString("articulo"),cantidad,rs0.getInt("pzaspaquete"),kg};
                    modelot1.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
        busca_fechas = null;
}//GEN-LAST:event_btnconActionPerformed

    private void menueliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menueliminarActionPerformed
        // TODO add your handling code here:
        int filano = Tabladatos.getSelectedRow();
        String motivocancela = "" + JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE CANCELACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);

        if (!motivocancela.equals("") && !motivocancela.equals("null")) {

            //elimina los datos al almacen de embarques
            String op=""+Tabladatos.getValueAt(filano, 3);
            String claveart=""+Tabladatos.getValueAt(filano, 5);
            rs0=null;
            try{
                String senSQL="SELECT * FROM almacen_pt WHERE (op='"+op+"' AND clavearticulo='"+claveart+"')";
                rs0=conexion.consulta(senSQL,conn);
                if(rs0.next()){
                    int cantidadpzasalmacen=rs0.getInt("cantidadpzas");
                    if(Integer.parseInt(""+Tabladatos.getValueAt(filano, 7))>cantidadpzasalmacen){
                        JOptionPane.showMessageDialog(this,"NO PUEDES ELIMINAR LA ENTRADA \nNO HAY SUFICIENTE MATERIAL EN ALMACEN","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    }else{
                        //cancela la captura ELIMINA
                        String senSQLmov = "UPDATE entradas_almacen_pt SET estatus='Can',fechacancela='" + fechainsertarhora.format(new Date()) + "',motivocancela='" + motivocancela + "',usuario='" + conf.getProperty("UserID") + "' WHERE id_entrada_almacen_pt='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
                        conexion.modificaupdate_p(senSQLmov, conn,valor_privilegio);
                        //resta las cantidades del almacen
                        int nuevacantidad=cantidadpzasalmacen-Integer.parseInt(""+Tabladatos.getValueAt(filano, 7));
                        senSQL = "UPDATE almacen_pt SET cantidadpzas='"+nuevacantidad+"' WHERE (op='"+op+"' AND clavearticulo='"+claveart+"');";
                        conexion.modificamov_p(senSQL, conn,valor_privilegio);
                    }
                }else{
                    JOptionPane.showMessageDialog(this,"NO PUEDES ELIMINAR LA ENTRADA \nNO HAY MATERIAL EN ALMACEN","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
                if(rs0!=null) { rs0.close(); }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //elimina los ceros del inventario
            String senSQL="DELETE FROM almacen_pt WHERE (cantidadpzas<1) ";
            conexion.modificamov_p(senSQL, conn,valor_privilegio);

            limpiatabla();
            datos();
        }
    }//GEN-LAST:event_menueliminarActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btncon;
    private javax.swing.JButton btnimpirmir;
    private javax.swing.JButton btnnuevo;
    private javax.swing.JTextField buscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem menueliminar;
    private javax.swing.JPopupMenu menupop;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_entrada_almacen_pt2;
}
