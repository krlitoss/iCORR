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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


/**
 *
 * @author IVONNE
 */
public class reporte_sat extends javax.swing.JInternalFrame {
    Connection conn=null;
    DefaultTableModel modelot1=null;
    ResultSet rs0;
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechamesnumero = new SimpleDateFormat("MM");
    SimpleDateFormat fechamescompleto = new SimpleDateFormat("MMMMM");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechasathora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo2enteros=new DecimalFormat("#####00");
    private Properties conf;
    String rfcempresa="";
    String nombrecliente="";
    String nombreempresacomercial="";
    String valor_privilegio="1";

    /** Creates new form usuarios */
    public reporte_sat(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conn=connt;
        conf=conexion.archivoInicial();
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        datos_privilegios();
        datosEmpresa();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datos();
        modelot1.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String numrep=""+model.getValueAt(f, 0);
                        if(!numrep.equals("null") && valimp.equals("true")){
                            String motivoelimina=""+JOptionPane.showInputDialog(null, "INTRODUCE REFERENCIA DE ENVIO:", "D A T O S !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
                            if(!motivoelimina.equals("") && !motivoelimina.equals("null")){
                                String senSQL="UPDATE reporte_sat SET enviado='"+valimp+"', observaciones='"+motivoelimina+"' WHERE (id_reporte='"+numrep+"');";
                                conexion.modifica_p(senSQL,conn,valor_privilegio);
                            }
                        }
                        limpiatabla();
                        datos();

                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
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
        valor_privilegio=conexion.obtener_privilegios(conn,"Reporte SAT");
    }

    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(250);

        Tabladatos.getTableHeader().setReorderingAllowed( false );
    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM reporte_sat ORDER BY id_reporte";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Object datos[]={rs0.getString("id_reporte"),fechamascorta.format(rs0.getDate("fecha")),fechamescompleto.format(fechamesnumero.parse(""+rs0.getInt("mes"))).toUpperCase(),rs0.getInt("ano"),rs0.getBoolean("enviado"),rs0.getString("observaciones")};
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
                datos_reporte_sat = new datos_reporte_sat(null,true,conn,clavemodifica,valor_privilegio);
                datos_reporte_sat.setLocationRelativeTo(this);
                datos_reporte_sat.setVisible(true);
                limpiatabla();
                datos_reporte_sat=null;
                datos();
            }
        }
    }
    public void datosEmpresa(){
        rs0=null;
        try{
            String senSQL="SELECT empresa.rfc,empresa.nombrecomercial FROM empresa WHERE id='1'";
            rs0=conexion.consulta(senSQL,conn);
            if(rs0.next()){
                rfcempresa=rs0.getString("rfc");
                nombreempresacomercial=rs0.getString("nombrecomercial");
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

        guardatxt = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(reporte_sat.class);
        guardatxt.setDialogTitle(resourceMap.getString("guardatxt.dialogTitle")); // NOI18N
        guardatxt.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        guardatxt.setName("guardatxt"); // NOI18N

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
                "ID", "Fecha", "Mes", "Año", "Enviado", "Observaciones"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE))
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
        if (evt.getClickCount() ==2){
            modificar();
        }
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        busca_periodo busca_periodo = new busca_periodo(null,true,"reporte");
        busca_periodo.setLocationRelativeTo(this);
        busca_periodo.setVisible(true);
        String estado=busca_periodo.getEstado();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if(estado.equals("cancelar")){

        }else{
            int ano=busca_periodo.getano();
            int mes=busca_periodo.getmes();
            System.err.println("Mes: "+mes+"   Año: "+ano);
            if(ano>=0 && mes>=0){
                Calendar calFin = Calendar.getInstance();
                calFin.set(ano, mes, 1);
                Date fechaIni=calFin.getTime();
                calFin.set(ano,mes, calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date fechaFin = calFin.getTime();

                //meses de 0 a 11
                mes=mes+1;

                int yagenerado=0;
                int regenerar=0;
                String yaenviado="";
                //consulata si no ha sido generado
                rs0=null;
                try{
                    String senSQL="SELECT * FROM reporte_sat WHERE (ano='"+ano+"' AND mes='"+mes+"')";
                    rs0=conexion.consulta(senSQL,conn);
                    if(rs0.next()){
                        yagenerado=1;
                        yaenviado=""+rs0.getBoolean("enviado");
                    }
                    if(rs0!=null) { rs0.close(); }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                //revisa si ya fue generado el reporte del mes
                if(yagenerado==1){
                    if(yaenviado.toUpperCase().equals("TRUE")){
                        JOptionPane.showMessageDialog(this,"EL REPORTE YA FUE ENVIADO AL SAT NO SE PUEDE REGENERAR","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    }else{
                        if (JOptionPane.showConfirmDialog(this,"EL REPORTE YA FUE GENERARO DESEA REGENERAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            yagenerado=0;
                            regenerar=1;
                        }
                    }
                }

                if(yagenerado==0){

                        //genera los datos del reporte y los guarda las facturas
                        String linea="";
                        rs0=null;
                        try{
                            String senSQL="SELECT clientes.rfc,facturas.factura,facturas.serie,folios.aprobacion,facturas.fecha,facturas.total,facturas.estatus,facturas.iva,facturas.factura_serie FROM (facturas LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes) LEFT JOIN folios ON facturas.id_folios=folios.id_folio WHERE (facturas.fecha>='"+fechainsertar.format(fechaIni)+" 00:00:00' AND facturas.fecha<='"+fechainsertar.format(fechaFin)+" 23:59:59') ORDER BY facturas.factura";
                            rs0=conexion.consulta(senSQL,conn);
                            while(rs0.next()){
                                //GENERAMOS CONSULTA INTERNA PARA QUITAR LOS QUE YA TIENEN UUID QUE NO SE ENVIAN
                                String sql_uuid="SELECT uuid FROM xmlfinal WHERE factura_serie='"+rs0.getString("factura_serie")+"' ";
                                ResultSet rs_uuid=conexion.consulta(sql_uuid,conn);
                                String uuid_c="";
                                if(rs_uuid.next()){
                                    uuid_c=""+rs_uuid.getString("uuid");
                                }
                                if(uuid_c.equals("") || uuid_c.equals("null") || uuid_c.equals("0")){

                                    linea+="|"+rs0.getString("rfc")+"|"+rs0.getString("serie")+"|"+rs0.getString("factura")+"|"+rs0.getString("aprobacion")+"|"+fechasathora.format(rs0.getTimestamp("fecha"))+"|"+fijo2decimales.format(rs0.getDouble("total"))+"|"+fijo2decimales.format(rs0.getDouble("iva"))+"|"+rs0.getString("estatus")+"|I||||\n";

                                }
                            }
                            if(rs0!=null) {    rs0.close();    }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                        //notas de credito
                        rs0=null;
                        try{
                            String senSQL="SELECT clientes.rfc,notas_credito.nota_credito,notas_credito.serie,folios.aprobacion,notas_credito.fecha,notas_credito.total,notas_credito.estatus,notas_credito.iva,notas_credito.nota_credito_serie FROM (notas_credito LEFT JOIN clientes ON notas_credito.id_clientes=clientes.id_clientes) LEFT JOIN folios ON notas_credito.id_folios=folios.id_folio WHERE (notas_credito.fecha>='"+fechainsertar.format(fechaIni)+" 00:00:00' AND notas_credito.fecha<='"+fechainsertar.format(fechaFin)+" 23:59:59') ORDER BY notas_credito.nota_credito";
                            rs0=conexion.consulta(senSQL,conn);
                            while(rs0.next()){
                                //GENERAMOS CONSULTA INTERNA PARA QUITAR LOS QUE YA TIENEN UUID QUE NO SE ENVIAN
                                String sql_uuid="SELECT uuid FROM xmlfinal WHERE factura_serie='"+rs0.getString("nota_credito_serie")+"' ";
                                ResultSet rs_uuid=conexion.consulta(sql_uuid,conn);
                                String uuid_c="";
                                if(rs_uuid.next()){
                                    uuid_c=""+rs_uuid.getString("uuid");
                                }
                                if(uuid_c.equals("") || uuid_c.equals("null") || uuid_c.equals("0")){
                                    linea+="|"+rs0.getString("rfc")+"|"+rs0.getString("serie")+"|"+rs0.getString("nota_credito")+"|"+rs0.getString("aprobacion")+"|"+fechasathora.format(rs0.getTimestamp("fecha"))+"|"+fijo2decimales.format(rs0.getDouble("total"))+"|"+fijo2decimales.format(rs0.getDouble("iva"))+"|"+rs0.getString("estatus")+"|E||||\n";
                                }
                            }
                            if(rs0!=null) {
                                rs0.close();
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                        if(!linea.equals("")){
                            //guarda los datos
                            if(regenerar==0){
                                String senSQL="INSERT INTO reporte_sat(fecha, ano, mes, texto, enviado, observaciones) VALUES ('"+fechainsertarhora.format(new Date())+"', '"+ano+"', '"+mes+"', '"+linea+"', 'false', '');";
                                conexion.modifica_p(senSQL,conn,valor_privilegio);
                            }else{
                                String senSQL="UPDATE reporte_sat SET fecha='"+fechainsertarhora.format(new Date())+"', texto='"+linea+"', enviado='FALSE', observaciones='regenerado' WHERE (ano='"+ano+"' AND  mes='"+mes+"');";
                                conexion.modifica_p(senSQL,conn,valor_privilegio);
                            }
                        }else{
                            JOptionPane.showMessageDialog(this,"NO SE ENCONTRARON PARTIDAS","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        }

                }

            }

        }
        busca_periodo=null;
        limpiatabla();
        datos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        modificar();
}//GEN-LAST:event_jButton2ActionPerformed

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
    }//GEN-LAST:event_buscarFocusGained

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            FileWriter fWriter;
            BufferedWriter bWriter;
            String clavemodifica=(String)Tabladatos.getValueAt(filano, 0);
            rs0=null;
            try{
                String senSQL="SELECT * FROM reporte_sat WHERE id_reporte='"+clavemodifica+"';";
                rs0=conexion.consulta(senSQL,conn);
                if(rs0.next()){
                    String rutafinal="";
                    try{
                        File nuevo=new File(System.getProperty("user.home")+"/1"+rfcempresa+fijo2enteros.format(rs0.getInt("mes"))+rs0.getInt("ano")+".txt");
                        guardatxt.setSelectedFile(nuevo);
                        if(guardatxt.showDialog(this, null)==JFileChooser.APPROVE_OPTION){//cuando regresa un uno no guarda nada
                            rutafinal=String.valueOf(guardatxt.getSelectedFile());
                        }
                    }catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL CREAR XML\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    if(!rutafinal.equals("")){
                        try{
                            fWriter= new FileWriter(new File(rutafinal));
                            bWriter = new BufferedWriter(fWriter);
                            String datos=rs0.getString("texto");
                            bWriter.write(datos.replace("\n","\r\n"));
                            System.err.print(datos);
                            bWriter.close();
                            fWriter.close();
                        }catch(IOException es){   es.printStackTrace(); }

                        JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField buscar;
    private javax.swing.JFileChooser guardatxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_reporte_sat;
}
