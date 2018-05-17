/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * busca_agentes.java
 *
 * Created on 26/01/2010, 05:27:05 PM
 */

package conta;

import java.awt.Component;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author ANGEL
 */
public class busca_ops_pendientes extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    DefaultTableModel modelot1=null;
    String clavebusca="";
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    Calendar calendariniciosem = new GregorianCalendar();
    Calendar calendarfinsem = new GregorianCalendar();

    /** Creates new form busca_agentes */
    public busca_ops_pendientes(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica) {
        super(parent, modal);
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo.png")).getImage());
        this.setLocationRelativeTo(null);
        connj=conn;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        //fecha de hoy para calculo de fechas de entrega
        calendariniciosem.setTime(new Date()); //gregorian hoy
        calendariniciosem.add(Calendar.DAY_OF_WEEK, (-calendariniciosem.get(Calendar.DAY_OF_WEEK)+1) );
        //fecha de fin de semana para fechas de entrega
        calendarfinsem.setTime(calendariniciosem.getTime());
        calendarfinsem.add(Calendar.DATE,7);

        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datos(clavemodifica);
    }
    public void salir(){
        if(connj!=null){
            connj=null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }
    public void ajusteTabladatos() {
        //columnas corrugadora
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(75);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(240);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(12).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(13).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(14).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(15).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(16).setPreferredWidth(200);
        Tabladatos.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer_pla_corr());

    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public class DoubleRenderer_pla_corr extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                if(text.equals("null") || text.equals(""))
                    text="";

                if(!text.equals("")){
                    int cantidad_pedida=Integer.parseInt(text);
                    int cantidad_prog=Integer.parseInt(""+Tabladatos.getValueAt(row, 10));
                    int cantidad_prod=Integer.parseInt(""+Tabladatos.getValueAt(row, 14));
                    if(cantidad_prog>=cantidad_pedida)
                        rend.setBackground(new java.awt.Color(135,206,235));
                    if(cantidad_prod>=cantidad_pedida)
                        rend.setBackground(new java.awt.Color(124,205,124));
                }
                rend.setText( text );
                return rend;
            }
    }
    public void datos(String valorfiltro){
        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.ancho,articulos.kg,articulos.m2,clientes.nombre,prodcorr.fechaprod,COALESCE(prodcorr.prodml,0) as prodml,COALESCE(prodcorr.prodcantidad,0) as prodcantidad,COALESCE(prodcorr.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prodcorr.prodcantkgpiezas,0) as prodcantkgpiezas,programcorr.fechaprogram,COALESCE(programcorr.programcantidad,0) as programcantidad,COALESCE(programcorr.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(programcorr.programcantkgpiezas,0) as programcantkgpiezas,articulos_maquinas.clave FROM  ( (((ops LEFT JOIN (articulos LEFT JOIN resistencias ON articulos.claveresistencia=resistencias.clave) ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='CORR' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prodcorr ON ops.op=prodcorr.op ) LEFT JOIN (SELECT programa_corr_detalle.op,max(programa_corr.fechaproduccion) as fechaprogram,sum(programa_corr_detalle.laminas) as programcantidad,sum(articulos.piezas*programa_corr_detalle.laminas) as programcantidadpiezas,sum(articulos.piezas*programa_corr_detalle.laminas*articulos.kg) as programcantkgpiezas  FROM (programa_corr INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo  WHERE (programa_corr.estatus<>'Can' AND programa_corr.estatus<>'Pen') GROUP BY programa_corr_detalle.op) as programcorr ON ops.op=programcorr.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='CORR' "+valorfiltro+") ORDER BY ops.fechaentrega,ops.op;";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";

                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }
                int laminaspedidas=rs0.getInt("laminaspedidas");
                int laminasprogram=rs0.getInt("programcantidad");
                int laminasprod=rs0.getInt("prodcantidad");
                if(laminasprod<laminaspedidas){
                    Object datos[]={rs0.getString("op"),color+fechamascorta.format(fechaentrega),rs0.getString("maquila"),laminaspedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),rs0.getDouble("ancho"),rs0.getDate("fechaprogram"),null,laminasprogram,rs0.getInt("programcantkgpiezas"),rs0.getDate("fechaprod"),null,laminasprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre")};
                    modelot1.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public String getText() {
        return clavebusca;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        buscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(busca_ops_pendientes.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        buscar.setBackground(resourceMap.getColor("buscar.background")); // NOI18N
        buscar.setName("buscar"); // NOI18N
        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OP", "Fecha Entrega", "Maquila", "Lam. Pedida", "Clave Articulo", "Articulo", "Resis.", "Ancho", "<html><font color=#0066FF>Fecha PProd.", "<html><font color=#0066FF>ML", "<html><font color=#0066FF>Laminas", "<html><font color=#0066FF>KG", "<html><font color=green>Fecha Prod", "<html><font color=green>ML Prod", "<html><font color=green>Laminas Prod", "<html><font color=green>KG Prod", "Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false,false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladatos.setToolTipText(resourceMap.getString("Tabladatos.toolTipText")); // NOI18N
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(475, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            int filano=Tabladatos.getSelectedRow();
            clavebusca=(String)Tabladatos.getValueAt(filano, 0);
            salir();
}//GEN-LAST:event_TabladatosMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField buscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
