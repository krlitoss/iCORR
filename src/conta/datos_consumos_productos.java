/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * datos_usuarios.java
 *
 * Created on 21/01/2010, 10:56:30 PM
 */

package conta;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public final class datos_consumos_productos extends javax.swing.JDialog {
    DefaultTableModel modelot1=null;
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MM-yyyy");
    Connection connj;
    ResultSet rs0=null,rs1=null;
    Double valoriva=0.0,valorieps=0.0;
    int banderabuscaart=0;
    String terminacion="M.N.";
    int cambia=1;
    Double limitealmacen=0.20;
    String nuevoalmacen="A2";
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_consumos_productos(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        fecha.setDate(new Date());
        //cambia la tecla enter por un tab
        HashSet conj = new HashSet(this.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

        modelot1.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) { //obtiene los datos de la OP
                        String norollo=""+model.getValueAt(f, c);
                        if(!norollo.equals("") && !norollo.equals("null") && !norollo.equals("0")){
                            rs0=null;
                            try{
                                String senSQL="SELECT entradas_productos.fecha,entradas_productos.id_entrada_producto,entradas_productos.remision,proveedores.nombre,entradas_productos_detalle.*,productos.descripcion,productos.unidad,productos.peso,productos.ancho,COALESCE(consumos.cantidadconsumo,0) as cantidadconsumo,consumos.fechaconsumo FROM (( (entradas_productos_detalle LEFT JOIN (entradas_productos LEFT JOIN proveedores ON entradas_productos.id_proveedor=proveedores.id_proveedor) ON entradas_productos_detalle.id_entrada_producto=entradas_productos.id_entrada_producto) LEFT JOIN (SELECT consumos_productos.id_entrada_producto_detalle,sum(consumos_productos.cantidad) as cantidadconsumo,max(consumos_productos.fecha) as fechaconsumo FROM consumos_productos WHERE consumos_productos.estatus<>'Can' GROUP BY consumos_productos.id_entrada_producto_detalle) as consumos ON entradas_productos_detalle.id_entrada_producto_detalle=consumos.id_entrada_producto_detalle) LEFT JOIN productos ON entradas_productos_detalle.clave_producto=productos.clave)  WHERE (entradas_productos_detalle.estatus<>'Can' AND entradas_productos_detalle.id_entrada_producto_detalle='"+norollo+"') ;";
                                rs0=conexion.consulta(senSQL,connj);
                                if(rs0.next()){
                                    Double cantidadfaltante=(rs0.getDouble("cantidad")-rs0.getDouble("cantidadconsumo"));
                                    if(cantidadfaltante>0.0){
                                        cambia=0;
                                        model.setValueAt(cantidadfaltante, f, 1);
                                        cambia=1;
                                        model.setValueAt(rs0.getString("clave_producto"), f, 2);
                                        model.setValueAt(rs0.getString("descripcion"), f, 3);
                                        model.setValueAt(rs0.getString("unidad"), f, 4);
                                        model.setValueAt(rs0.getDouble("preciounitario"), f, 5);
                                        model.setValueAt((Double) model.getValueAt(f, 1) * (Double) model.getValueAt(f, 5), f, 6);
                                        model.setValueAt(rs0.getString("almacen_existe"), f, 8);
                                    }else{
                                        JOptionPane.showMessageDialog(null,"EL ROLLO YA NO TIENE EXISTENCIAS","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                        model.setValueAt(0.0, f, 1);
                                        model.setValueAt("", f, 2);
                                        model.setValueAt("", f, 3);
                                        model.setValueAt(0.0, f, 4);
                                        model.setValueAt(0.0, f, 5);
                                        model.setValueAt(0.0, f, 6);
                                        model.setValueAt("", f, 7);
                                        model.setValueAt("", f, 8);
                                        model.setValueAt(0, f, 0);
                                    }
                                }
                                else{
                                    JOptionPane.showMessageDialog(null,"LA NUMERO DE ROLLO NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                    model.setValueAt(0.0, f, 1);
                                    model.setValueAt("", f, 2);
                                    model.setValueAt("", f, 3);
                                    model.setValueAt(0.0, f, 4);
                                    model.setValueAt(0.0, f, 5);
                                    model.setValueAt(0.0, f, 6);
                                    model.setValueAt("", f, 7);
                                    model.setValueAt("", f, 8);
                                    model.setValueAt(0, f, 0);
                                }
                                if(rs0!=null) {  rs0.close(); }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                        }
                        sumatoria();
                    }
                    if (c == 1 && cambia==1) {
                        String kgpapel=""+model.getValueAt(f, c);
                        String norollo=""+model.getValueAt(f, 0);
                        if(!kgpapel.equals("") && !kgpapel.equals("null") && !norollo.equals("") && !norollo.equals("null") && !norollo.equals("0")){
                            
                            Double kgp=(Double)model.getValueAt(f, c);
                            if(kgp>0.0){
                                    rs1=null;
                                    try{
                                        String senSQL="SELECT entradas_productos.fecha,entradas_productos.id_entrada_producto,entradas_productos.remision,proveedores.nombre,entradas_productos_detalle.*,productos.descripcion,productos.unidad,productos.peso,productos.ancho,COALESCE(consumos.cantidadconsumo,0) as cantidadconsumo,consumos.fechaconsumo FROM (( (entradas_productos_detalle LEFT JOIN (entradas_productos LEFT JOIN proveedores ON entradas_productos.id_proveedor=proveedores.id_proveedor) ON entradas_productos_detalle.id_entrada_producto=entradas_productos.id_entrada_producto) LEFT JOIN (SELECT consumos_productos.id_entrada_producto_detalle,sum(consumos_productos.cantidad) as cantidadconsumo,max(consumos_productos.fecha) as fechaconsumo FROM consumos_productos WHERE consumos_productos.estatus<>'Can' GROUP BY consumos_productos.id_entrada_producto_detalle) as consumos ON entradas_productos_detalle.id_entrada_producto_detalle=consumos.id_entrada_producto_detalle) LEFT JOIN productos ON entradas_productos_detalle.clave_producto=productos.clave)  WHERE (entradas_productos_detalle.estatus<>'Can' AND entradas_productos_detalle.id_entrada_producto_detalle='"+norollo+"') ;";
                                        rs1=conexion.consulta(senSQL,connj);
                                        if(rs1.next()){
                                            Double cantidadfaltante=(rs1.getDouble("cantidad")-rs1.getDouble("cantidadconsumo"));
                                            if(kgp>cantidadfaltante){
                                                    JOptionPane.showMessageDialog(null,"LA CANTIDAD ES MAYOR A LA EXISTENTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                                    model.setValueAt(0.0, f, 1);
                                                    model.setValueAt(0.0, f, 6);
                                            }else{
                                                model.setValueAt((Double) model.getValueAt(f, 1) * (Double) model.getValueAt(f, 5), f, 6);
                                            }

                                        }else{
                                            model.setValueAt(0.0, f, 1);
                                            model.setValueAt(0.0, f, 6);
                                        }
                                        if(rs1!=null) {  rs1.close(); }
                                    } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                                    
                            }
                        }
                        sumatoria();
                    }
                    if (c == 7) {
                        String programa=""+model.getValueAt(f, c);
                        if(!programa.equals("") && !programa.equals("null") && !programa.equals("0")){
/*   se elima esta opcion hasta que el sistema este totalmente con skarton
                                    rs1=null;
                                    try{
                                        String senSQL="SELECT * FROM programa_corr WHERE (programa_corr.estatus<>'Can' AND programa_corr.id_programa_corr='"+programa+"') ;";
                                        rs1=conexion.consulta(senSQL,connj);
                                        if(rs1.next()){

                                        }else{
                                            JOptionPane.showMessageDialog(null,"EL PROGRAMA NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                            model.setValueAt("", f, 7);
                                        }
                                        if(rs1!=null) {  rs1.close(); }
                                    } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
*/

                        }
                    }

                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "NO SE PUEDE CALCULAR EL IMPORTE"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }

          }
        });

    }

    public class DoubleRenderer extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                String textpunto = ""+value;

                if((column==1 || column==6)){
                    text = fijo2decimales.format( Double.parseDouble(""+value) );
                    int dotPos = textpunto.lastIndexOf(".")+1;
                    String partedecimal = textpunto.substring(dotPos);
                    if(partedecimal.length() > 2){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                }
                if(column==5){
                    text = fijo5decimales.format( Double.parseDouble(""+value) );
                    int dotPos2 = textpunto.lastIndexOf(".")+1;
                    String partedecimal2 = textpunto.substring(dotPos2);
                    if(partedecimal2.length() > 5){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                }
                if(column==4 || column==8){
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                }

                rend.setText( text );
                return rend;
            }

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
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(150);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(230);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(90);

        Tabladatos.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(7).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
    }

    public void sumatoria(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{
            Double sumaimportes=0.0;
            Double sumakg=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                Double valimporte=(Double) Tabladatos.getValueAt(i, 6);///obtiene el importe
                Double valkg=(Double) Tabladatos.getValueAt(i, 1);///obtiene el importe
                sumaimportes=sumaimportes+valimporte;
                sumakg=sumakg+valkg;
            }
            importe.setText(moneda2decimales.format(sumaimportes));
            kg.setText(estandarentero.format(sumakg));
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

        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        id = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        turno = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        btnremover = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        kg = new javax.swing.JFormattedTextField();
        lbiva = new javax.swing.JLabel();
        importe = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_consumos_productos.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        btnaceptar.setIcon(resourceMap.getIcon("btnaceptar.icon")); // NOI18N
        btnaceptar.setText(resourceMap.getString("btnaceptar.text")); // NOI18N
        btnaceptar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnaceptar.setName("btnaceptar"); // NOI18N
        btnaceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaceptarActionPerformed(evt);
            }
        });

        btncancelar.setIcon(resourceMap.getIcon("btncancelar.icon")); // NOI18N
        btncancelar.setText(resourceMap.getString("btncancelar.text")); // NOI18N
        btncancelar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btncancelar.setName("btncancelar"); // NOI18N
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(244, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        fecha.setDateFormatString(resourceMap.getString("fecha.dateFormatString")); // NOI18N
        fecha.setName("fecha"); // NOI18N
        fecha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fechaFocusGained(evt);
            }
        });

        id.setEditable(false);
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setText(resourceMap.getString("id.text")); // NOI18N
        id.setFocusable(false);
        id.setName("id"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        turno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3" }));
        turno.setName("turno"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(164, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rollo", "Cantidad", "Clave", "Descripcion", "U.M.", "Importe Unitario", "Importe", "Programa Corr", "Almacen"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladatos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos.setColumnSelectionAllowed(true);
        Tabladatos.setName("Tabladatos"); // NOI18N
        Tabladatos.setRowHeight(22);
        Tabladatos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Tabladatos.getTableHeader().setReorderingAllowed(false);
        Tabladatos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TabladatosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TabladatosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        btnremover.setIcon(resourceMap.getIcon("btnremover.icon")); // NOI18N
        btnremover.setText(resourceMap.getString("btnremover.text")); // NOI18N
        btnremover.setToolTipText(resourceMap.getString("btnremover.toolTipText")); // NOI18N
        btnremover.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnremover.setName("btnremover"); // NOI18N
        btnremover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoverActionPerformed(evt);
            }
        });

        btnadd.setIcon(resourceMap.getIcon("btnadd.icon")); // NOI18N
        btnadd.setText(resourceMap.getString("btnadd.text")); // NOI18N
        btnadd.setToolTipText(resourceMap.getString("btnadd.toolTipText")); // NOI18N
        btnadd.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnadd.setName("btnadd"); // NOI18N
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        kg.setBackground(resourceMap.getColor("kg.background")); // NOI18N
        kg.setEditable(false);
        kg.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("######0.00"))));
        kg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        kg.setText(resourceMap.getString("kg.text")); // NOI18N
        kg.setFocusable(false);
        kg.setName("kg"); // NOI18N

        lbiva.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbiva.setText(resourceMap.getString("lbiva.text")); // NOI18N
        lbiva.setName("lbiva"); // NOI18N

        importe.setBackground(resourceMap.getColor("importe.background")); // NOI18N
        importe.setEditable(false);
        importe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####0.00"))));
        importe.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        importe.setText(resourceMap.getString("importe.text")); // NOI18N
        importe.setFocusable(false);
        importe.setName("importe"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(369, 369, 369)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kg, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbiva, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(kg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbiva))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        int columnas=Tabladatos.getColumnCount();
        if(fecha.getDate()==null||filas<=0){
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            if (Tabladatos.getCellEditor() != null) {//finaliza el editor
                Tabladatos.getCellEditor().stopCellEditing();
            }
            /**revisar que no existan campos vacios*/
            int camposvacios=0;
            for(int i=0;i<=(filas-1);i=i+1){
                for(int j=0;j<=(columnas-1);j=j+1){
                    if(modelot1.getValueAt(i, j) == null||modelot1.getValueAt(i, j).equals("")){
                        camposvacios=1;
                    }

                }
                /**funcion que verifica que no quede en ceros la cantidad*/
                if((Double)modelot1.getValueAt(i, 1) ==0.0){
                    camposvacios=1;
                }
                if((Integer)modelot1.getValueAt(i, 0) ==0){
                    camposvacios=1;
                }
            }/**fin de revisar los campos vacios*/

            if(camposvacios==1){
                JOptionPane.showMessageDialog(this, "LA TABLA DE DETALLE TIENE CAMPOS VACIOS\nBORRE LAS FILAS VACIAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                sumatoria();
                if(id.getText().equals("")){
                    /**funcion que registra todos los movimientos de la orden de compra*/
                    for(int i=0;i<=(filas-1);i=i+1){
                        String senSQLmov="INSERT INTO consumos_productos(id_entrada_producto_detalle, estatus, fecha, id_programa_corr, clave_producto, cantidad, turno,almacen) VALUES ('"+modelot1.getValueAt(i, 0)+"', 'Act', '"+fechainsertar.format(fecha.getDate())+"', '"+modelot1.getValueAt(i, 7)+"', '"+modelot1.getValueAt(i, 2)+"', '"+modelot1.getValueAt(i, 1)+"', '"+turno.getSelectedItem()+"', '"+modelot1.getValueAt(i, 8)+"');";
                        conexion.modificamov_p(senSQLmov,connj,valor_privilegio);

                        //Verifica si existe menos del 20 % del peso del rollo lo asigna al almacen de colas
                        try{
                            String norollo=""+modelot1.getValueAt(i, 0);
                            senSQLmov="SELECT entradas_productos.fecha,entradas_productos.id_entrada_producto,entradas_productos.remision,proveedores.nombre,entradas_productos_detalle.*,productos.descripcion,productos.unidad,COALESCE(consumos.cantidadconsumo,0) as cantidadconsumo,consumos.fechaconsumo FROM (entradas_productos LEFT JOIN proveedores ON entradas_productos.id_proveedor=proveedores.id_proveedor) INNER JOIN  ((entradas_productos_detalle LEFT JOIN (SELECT consumos_productos.id_entrada_producto_detalle,sum(consumos_productos.cantidad) as cantidadconsumo,max(consumos_productos.fecha) as fechaconsumo FROM consumos_productos WHERE consumos_productos.estatus<>'Can' GROUP BY consumos_productos.id_entrada_producto_detalle) as consumos ON entradas_productos_detalle.id_entrada_producto_detalle=consumos.id_entrada_producto_detalle) LEFT JOIN productos ON entradas_productos_detalle.clave_producto=productos.clave) ON entradas_productos.id_entrada_producto=entradas_productos_detalle.id_entrada_producto WHERE (entradas_productos_detalle.estatus<>'Can' AND entradas_productos_detalle.id_entrada_producto_detalle='"+norollo+"');";
                            rs0=conexion.consultamov(senSQLmov,connj);
                            if(rs0.next()){
                                Double cantidad=rs0.getDouble("cantidad");
                                Double cantidadconsumo=rs0.getDouble("cantidadconsumo");
                                if((cantidad-cantidadconsumo)<=(cantidad*limitealmacen)){
                                    senSQLmov="UPDATE entradas_productos_detalle SET almacen_existe='"+nuevoalmacen+"' WHERE id_entrada_producto_detalle='"+norollo+"';";
                                    conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                                }

                            }
                            if(rs0!=null) { rs0.close();  }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                        //final de verificar el peso del rollo
                        
                    }
                }
                salir();

            }
        }
}//GEN-LAST:event_btnaceptarActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        salir();
}//GEN-LAST:event_btncancelarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        if(filas<15){
            Object datos[]={0,0.0,"","","",0.0,0.0,""};
            modelot1.addRow(datos);
        }else{
            JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }

}//GEN-LAST:event_btnaddActionPerformed

    private void btnremoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoverActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO DE ELIMINAR LA FILA  "+(filano+1)+" ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                modelot1.removeRow(filano);
                sumatoria();
            }else{

            }
        }
    }//GEN-LAST:event_btnremoverActionPerformed

    private void fechaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fechaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaFocusGained

    private void TabladatosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }
    }//GEN-LAST:event_TabladatosKeyPressed

    private void TabladatosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TabladatosKeyReleased

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnremover;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField id;
    private javax.swing.JFormattedTextField importe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFormattedTextField kg;
    private javax.swing.JLabel lbiva;
    private javax.swing.JComboBox turno;
    // End of variables declaration//GEN-END:variables

}
