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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author ANGEL
 */
public class busca_programas_conversion extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    DefaultTableModel modelot1=null;
    String clavebusca="",claveprograma="";
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");

    /** Creates new form busca_agentes */
    public busca_programas_conversion(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String cmaquina,String claveart) {
        super(parent, modal);
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo.png")).getImage());
        this.setLocationRelativeTo(null);
        connj=conn;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datos(clavemodifica,cmaquina,claveart);
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
        //columnas tcy
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(75);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(240);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(85);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(200);

    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos(String valorfiltro,String clavemaquina,String claveart){
        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad FROM (((programa_conversion LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.op='"+valorfiltro+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion;";
            if(!claveart.equals(""))
                senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad FROM (((programa_conversion LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.op='"+valorfiltro+"' AND programa_conversion.clavearticulo='"+claveart+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion;";

            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");

                Object datos[]={rs0.getInt("id_programa_conversion"),opprogram,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre")};
                modelot1.addRow(datos);
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    public String getText() {
        return clavebusca;
    }
    public String getPrograma() {
        return claveprograma;
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
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(busca_programas_conversion.class);
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
                "Programa", "OP", "Clave Articulo", "Articulo", "<html><font color=#0066FF>Fecha PProd.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
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
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            claveprograma=""+Tabladatos.getValueAt(filano, 0);
            clavebusca=""+Tabladatos.getValueAt(filano, 1);
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
