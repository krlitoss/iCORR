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

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
public class datos_pedidos extends javax.swing.JDialog {
    Connection connj;
    private Properties conf;
    ResultSet rs0=null;
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat fechamostrarhora = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
    SimpleDateFormat fechamostrarhoracorta = new SimpleDateFormat("dd-MMM-yy HH:mm");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DefaultTableModel modelot1=null;

    JDateChooser fechaentrega = new JDateChooser();
    JComboBox cp = new JComboBox();
    Double sumasubtotal=0.0;
    String ocantigua="";
    String clavemodificau="";
    int permitecambios=1;
    String user="";

    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_pedidos(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        ajusteTabladatos();
        valor_privilegio=valor_privilegio_t;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        
        fecha.setText(fechamostrarhoracorta.format(new Date()));
        consultamodifica(clavemodifica);
        clavemodificau=clavemodifica;
        conf=conexion.archivoInicial();
        user=conf.getProperty("UserID");
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

        modelot1.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla

                    if (c == 2) {
                        String valj=""+model.getValueAt(f, c);
                        if(valj.equals("true") && clavemodificau.equals("")){
                            busca_articulos_juegos busca_articulos_juegos = new busca_articulos_juegos(null,true,connj,"",(""+model.getValueAt(f, 0)),valor_privilegio);
                            busca_articulos_juegos.setLocationRelativeTo(null);
                            busca_articulos_juegos.setVisible(true);
                            Double valorpu=busca_articulos_juegos.getText();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
                            busca_articulos_juegos=null;
                            if(valorpu>0.0){
                                permitecambios=0;
                                model.setValueAt(valorpu, f, 4);
                            }
                            Tabladatos.changeSelection(f, 3, false, false);
                        }
                        if(valj.equals("false") && clavemodificau.equals("")){
                            model.setValueAt(0.0, f, 4);
                        }
                    }

                    if (c == 3) {
                        if(model.getValueAt(f, c) != null){
                            model.setValueAt((Double) model.getValueAt(f, c) * (Double) model.getValueAt(f, 4), f, 5);
                        }else{
                            model.setValueAt(0.0, f, 5);
                        }
                        sumatoria();
                        
                        Double valorcantidad=(Double)model.getValueAt(f, c);
                        if(valorcantidad>0 && !clavemodificau.equals("")){
                            actualizaCantidad(""+model.getValueAt(f, 9),valorcantidad,""+model.getValueAt(f, 2),(""+model.getValueAt(f, 0)));
                        }
                    }
                    
                    if (c == 4 && permitecambios==1) {
                        if(model.getValueAt(f, c) != null){
                            Double preciomt=(Double) model.getValueAt(f, c);
                            if(preciomt>0.0){
                                model.setValueAt((Double) model.getValueAt(f, c) * (Double) model.getValueAt(f, 3), f, 5);
                                String valj=""+model.getValueAt(f, 2);
                                if(valj.equals("false") && clavemodificau.equals("")){ //**funcion que actualiza el precio del articulo
                                        actualizaPrecioArt(preciomt,(""+model.getValueAt(f, 0)) );
                                }
                                if(valj.equals("true") && clavemodificau.equals("")){
                                    /*busca_articulos_juegos busca_articulos_juegos = new busca_articulos_juegos(null,true,connj,"",(""+model.getValueAt(f, 0)) );
                                    busca_articulos_juegos.setLocationRelativeTo(null);
                                    busca_articulos_juegos.setVisible(true);
                                    Double valorpu=busca_articulos_juegos.getText();//obtiene si se pulso aceptar o calcelar en busca_fechas
                                    busca_articulos_juegos=null;
                                    if(valorpu>0.0){
                                        permitecambios=0;
                                        model.setValueAt(valorpu, f, 4 );
                                        model.setValueAt((Double) model.getValueAt(f, c) * (Double) model.getValueAt(f, 3), f, 5);
                                        permitecambios=0;
                                    }*/
                                }
                            }else{
                                model.setValueAt(0.0, f, 5);
                            }
                        }else{
                            model.setValueAt(0.0, f, 5);
                        }
                        sumatoria();

                        Double valorprecio=(Double)model.getValueAt(f, c);
                        if(valorprecio>0 && !clavemodificau.equals("")){
                            String valj=""+model.getValueAt(f, 2);
                            if(valj.equals("false")){
                                actualizaPrecio(""+model.getValueAt(f, 9),valorprecio,""+model.getValueAt(f, 2),(""+model.getValueAt(f, 0)));
                                System.err.println("-Actualizado-"+model.getValueAt(f, 0)+":"+valorprecio);////////////////////////
                            }else{
                                busca_ops_juegos busca_ops_juegos = new busca_ops_juegos(null,true,connj,"",(""+model.getValueAt(f, 9)),valor_privilegio);
                                busca_ops_juegos.setLocationRelativeTo(null);
                                busca_ops_juegos.setVisible(true);
                                Double valorpu=busca_ops_juegos.getText();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
                                if(valorpu>0.0){
                                    permitecambios=0;
                                    model.setValueAt(valorpu, f, 4);
                                    model.setValueAt((Double) model.getValueAt(f, c) * (Double) model.getValueAt(f, 3), f, 5);
                                    sumatoria();
                                    //modifica el precio unitario de la remision
                                    Double pjgo=Double.parseDouble(""+model.getValueAt( f, 4));
                                    String senSQLmov="UPDATE remisiones SET preciounitario='"+pjgo+"' WHERE (id_op='"+model.getValueAt(f, 9)+"' AND facturado<>'TRUE');";
                                    conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                                    senSQLmov="UPDATE pedidos_detalle SET preciounitario='"+pjgo+"' WHERE id_op='"+model.getValueAt(f, 9)+"';";
                                    conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                                    //actualiza la suma total
                                    senSQLmov="UPDATE pedidos SET subtotal='"+sumasubtotal+"' WHERE id_pedido='"+id.getText()+"';";
                                    conexion.modificaupdate_p(senSQLmov,connj,valor_privilegio);

                                    senSQLmov="INSERT INTO bitacora(usuario, fecha, descripcion,host) VALUES ('"+user+"', '"+fechainsertarhora.format(new Date())+"', 'Cambio de precio unitario a remision y OP: "+pjgo+"','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                                    conexion.modificamov_p(senSQLmov,connj,valor_privilegio);

                                    //System.err.println("-Actualizado-"+model.getValueAt(f, 0)+":"+valorpu);////////////////////////

                                }
                                busca_ops_juegos=null;
                            }
                        }
                    }else{
                        permitecambios=1;
                    }

                    if (c == 6) {
                        String valorlugarentrega=""+model.getValueAt(f, c);
                        if(!valorlugarentrega.equals("") && !clavemodificau.equals("")){
                            actualizaLugarEnt(""+model.getValueAt(f, 9),valorlugarentrega,""+model.getValueAt(f, 2),(""+model.getValueAt(f, 0)));
                        }
                    }
                    if (c == 7) {
                        String valorfechaentrega=""+model.getValueAt(f, c);
                        if(!valorfechaentrega.equals("") && !clavemodificau.equals("")){
                            actualizaFechaEnt(""+model.getValueAt(f, 9),valorfechaentrega,""+model.getValueAt(f, 2),(""+model.getValueAt(f, 0)));
                        }
                    }
                    if (c == 8) {
                        String valorobs=""+model.getValueAt(f, c);
                        if(!valorobs.equals("") && !clavemodificau.equals("")){
                            actualizaObser(""+model.getValueAt(f, 9),valorobs,""+model.getValueAt(f, 2),(""+model.getValueAt(f, 0)));
                        }
                    }

                    
                    
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "NO SE PUEDE CALCULAR EL IMPORTE"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }

          }
        });


    }
    public void salir(){
        if(connj!=null){
            connj=null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            //deshabilita los botones de agregar y eliminar filas
            btnadd.setEnabled(false);
            btnremove.setEnabled(false);
            btnclientes.setEnabled(false);
            rs0=null;
            try{
                //String senSQL="SELECT pedidos.*,clientes.nombre,pedidos_detalle.*,articulos.articulo FROM ((pedidos LEFT JOIN clientes ON pedidos.id_clientes=clientes.id_clientes) INNER JOIN pedidos_detalle ON pedidos.id_pedido=pedidos_detalle.id_pedido) LEFT JOIN articulos ON pedidos_detalle.clavearticulo=articulos.clavearticulo  WHERE pedidos.id_pedido='"+clavemodifica+"' ORDER BY pedidos_detalle.partida";
                String senSQL="SELECT pedidos.*,clientes.nombre,pedidos_detalle.*,articulos.articulo FROM ((pedidos LEFT JOIN clientes ON pedidos.id_clientes=clientes.id_clientes) INNER JOIN (pedidos_detalle LEFT JOIN  (SELECT ops.id_op,max(ops.estatus) as estatus_op FROM ops GROUP BY ops.id_op) as ops ON pedidos_detalle.id_op=ops.id_op) ON pedidos.id_pedido=pedidos_detalle.id_pedido) LEFT JOIN articulos ON pedidos_detalle.clavearticulo=articulos.clavearticulo WHERE (pedidos.id_pedido='"+clavemodifica+"' AND estatus_op<>'Can') ORDER BY pedidos_detalle.partida";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    id.setText(rs0.getString("id_pedido"));
                    nombrecliente.setText(rs0.getString("nombre"));
                    fecha.setText( fechamostrarhoracorta.format(rs0.getTimestamp("fecha")) );
                    ocantigua=rs0.getString("ordencompra");
                    oc.setText(ocantigua);
                    clavecliente.setText(rs0.getString("id_clientes"));
                    Double cant=rs0.getDouble("cantidad");
                    Double pu=rs0.getDouble("preciounitario");

                    Object datos[]={rs0.getString("clavearticulo"),rs0.getString("articulo"),new Boolean(rs0.getString("juego")),cant,pu,(cant*pu),rs0.getString("id_lugarentrega"),fechainsertar.format(rs0.getDate("fechaentrega")),rs0.getString("observaciones"),rs0.getString("id_op")};
                    modelot1.addRow(datos);

                    sumasubtotal=rs0.getDouble("subtotal");
                    subtotal.setText(moneda2decimales.format(sumasubtotal));
                    notas.setText(rs0.getString("notas"));
                    maquila.setSelectedItem(rs0.getString("maquila"));
                }
                if(rs0!=null) { rs0.close(); }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(95);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(160);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(50);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(110);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(95);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(150);

        Tabladatos.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(7).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tabladatos.getColumnModel().getColumn(9).setCellRenderer(new DoubleRenderer());

    }

    public class DoubleRenderer extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos

            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                String textpunto = ""+value;

                if(column==2){
                    JCheckBox j=new JCheckBox();
                    j.setSelected(new Boolean(text));
                    j.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    return j;
                }

                if(column==4){
                    text = fijo5decimales.format( Double.parseDouble(""+value) );
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    int dotPos = textpunto.lastIndexOf(".")+1;
                    String partedecimal = textpunto.substring(dotPos);
                    if(partedecimal.length() > 5){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                }
                if(column==5 || column==3){
                    text = fijo2decimales.format( Double.parseDouble(""+value) );
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    int dotPos2 = textpunto.lastIndexOf(".")+1;
                    String partedecimal2 = textpunto.substring(dotPos2);
                    if(partedecimal2.length() > 2){
                        modelot1.setValueAt(Double.parseDouble(text), row, column);
                    }
                }
                if(column==6 || column==7){
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                }

                rend.setText( text );
                return rend;
            }

    }
    public void sumatoria(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{
            Double sumaimportes=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                Double valimporte=(Double) Tabladatos.getValueAt(i, 5);///obtiene el importe
                sumaimportes=sumaimportes+valimporte;
            }
            sumasubtotal=Double.parseDouble(fijo2decimales.format(sumaimportes));
            subtotal.setText(moneda2decimales.format(sumaimportes));
        }
    }
    public void actualizaPrecioArt(Double pmt,String ca){
        rs0=null;
        try{
            String senSQL="SELECT articulos.*,monedas.valor FROM articulos LEFT JOIN monedas ON articulos.id_moneda=monedas.id_moneda WHERE articulos.clavearticulo='"+ca+"'";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                String senSQLmov="UPDATE articulos SET preciomillar='"+fijo2decimales.format( (pmt*1000)/rs0.getDouble("valor") )+"',fechaactualizado='"+fechainsertarhora.format(new Date())+"' WHERE clavearticulo='"+ca+"';";
                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                System.err.println("-Actualizado-"+ca+":"+fijo2decimales.format(pmt*1000));////////////////////////
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }

    public void actualizaLugarEnt(String op,String nuevovalor,String juego,String ca){
        rs0=null;
        try{
            String senSQLmov="UPDATE pedidos_detalle SET id_lugarentrega='"+nuevovalor+"' WHERE id_op='"+op+"';";
            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
            if(juego.equals("true")){
                    int opjuego=0;
                    rs0=null;
                    try{
                        String senSQL="SELECT articulos_juegos.*,articulos.articulo,articulos.preciomillar FROM articulos_juegos LEFT JOIN articulos ON articulos_juegos.clavearticulo1=articulos.clavearticulo WHERE articulos_juegos.clavearticulo='"+ca+"'";
                        rs0=conexion.consulta(senSQL,connj);
                        while(rs0.next()){
                            opjuego++;
                            String jop=op+"-"+opjuego;
                            senSQLmov="INSERT INTO bitacora_op(usuario, fecha, op, clavearticulo, descripcion,host) VALUES ('"+conf.getProperty("UserID")+"', '"+fechainsertarhora.format(new Date())+"', '"+jop+"', '"+rs0.getString("clavearticulo1")+"', 'Modificacion de Lugar de Entrega por: "+nuevovalor+"','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                        }
                        if(rs0!=null) {
                            rs0.close();
                        }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            }else{
                senSQLmov="INSERT INTO bitacora_op(usuario, fecha, op, clavearticulo, descripcion,host) VALUES ('"+conf.getProperty("UserID")+"', '"+fechainsertarhora.format(new Date())+"', '"+op+"', '"+ca+"', 'Modificacion de Lugar de Entrega por: "+nuevovalor+"','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
            }
            
            senSQLmov="UPDATE ops SET id_lugarentrega='"+nuevovalor+"' WHERE id_op='"+op+"';";
            conexion.modificaupdate_p(senSQLmov,connj,valor_privilegio);

        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL ACTUALIZAR LOS DATOS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public void actualizaFechaEnt(String op,String nuevovalor,String juego,String ca){
        rs0=null;
        try{
            String senSQLmov="UPDATE pedidos_detalle SET fechaentrega='"+nuevovalor+"' WHERE id_op='"+op+"';";
            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
            if(juego.equals("true")){
                    int opjuego=0;
                    rs0=null;
                    try{
                        String senSQL="SELECT articulos_juegos.*,articulos.articulo,articulos.preciomillar FROM articulos_juegos LEFT JOIN articulos ON articulos_juegos.clavearticulo1=articulos.clavearticulo WHERE articulos_juegos.clavearticulo='"+ca+"'";
                        rs0=conexion.consulta(senSQL,connj);
                        while(rs0.next()){
                            opjuego++;
                            String jop=op+"-"+opjuego;
                            senSQLmov="INSERT INTO bitacora_op(usuario, fecha, op, clavearticulo, descripcion,hots) VALUES ('"+conf.getProperty("UserID")+"', '"+fechainsertarhora.format(new Date())+"', '"+jop+"', '"+rs0.getString("clavearticulo1")+"', 'Modificacion de Fecha de Entrega por: "+nuevovalor+"','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                        }
                        if(rs0!=null) {
                            rs0.close();
                        }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            }else{
                senSQLmov="INSERT INTO bitacora_op(usuario, fecha, op, clavearticulo, descripcion,host) VALUES ('"+conf.getProperty("UserID")+"', '"+fechainsertarhora.format(new Date())+"', '"+op+"', '"+ca+"', 'Modificacion de Fecha de Entrega por: "+nuevovalor+"','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
            }
            senSQLmov="UPDATE ops SET fechaentrega='"+nuevovalor+"' WHERE id_op='"+op+"';";
            conexion.modificaupdate_p(senSQLmov,connj,valor_privilegio);
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }

    public void actualizaObser(String op,String nuevovalor,String juego,String ca){
        rs0=null;
        try{
            String senSQLmov="UPDATE pedidos_detalle SET observaciones='"+nuevovalor+"' WHERE id_op='"+op+"';";
            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
            senSQLmov="UPDATE ops SET observaciones='"+nuevovalor+"' WHERE id_op='"+op+"';";
            conexion.modificaupdate_p(senSQLmov,connj,valor_privilegio);
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }

    public void actualizaCantidad(String op,Double nuevovalor,String juego,String ca){
        rs0=null;
        try{
            String senSQLmov="UPDATE pedidos_detalle SET cantidad='"+nuevovalor+"' WHERE id_op='"+op+"';";
            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
            //actualiza la suma total
            senSQLmov="UPDATE pedidos SET subtotal='"+sumasubtotal+"' WHERE id_pedido='"+id.getText()+"';";
            conexion.modificaupdate_p(senSQLmov,connj,valor_privilegio);

            if(juego.equals("true")){
                    int opjuego=0;
                    rs0=null;
                    try{
                        String senSQL="SELECT articulos_juegos.*,articulos.articulo,articulos.preciomillar FROM articulos_juegos LEFT JOIN articulos ON articulos_juegos.clavearticulo1=articulos.clavearticulo WHERE articulos_juegos.clavearticulo='"+ca+"'";
                        rs0=conexion.consulta(senSQL,connj);
                        while(rs0.next()){
                            opjuego++;
                            String jop=op+"-"+opjuego;
                            Double juegoscantidad=rs0.getDouble("piezas")*nuevovalor;
                            senSQLmov="INSERT INTO bitacora_op(usuario, fecha, op, clavearticulo, descripcion,host) VALUES ('"+conf.getProperty("UserID")+"', '"+fechainsertarhora.format(new Date())+"', '"+jop+"', '"+rs0.getString("clavearticulo1")+"', 'Modificacion de Cantidad por: "+juegoscantidad+"','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                            //actualiza la cantidad de los articulos
                            senSQLmov="UPDATE ops SET cantidad='"+juegoscantidad+"' WHERE op='"+jop+"';";
                            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                        }
                        if(rs0!=null) {
                            rs0.close();
                        }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            }else{
                senSQLmov="INSERT INTO bitacora_op(usuario, fecha, op, clavearticulo, descripcion,host) VALUES ('"+conf.getProperty("UserID")+"', '"+fechainsertarhora.format(new Date())+"', '"+op+"', '"+ca+"', 'Modificacion de Cantidad por: "+nuevovalor+"','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                senSQLmov="UPDATE ops SET cantidad='"+nuevovalor+"' WHERE id_op='"+op+"';";
                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
            }

        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL ACTUALIZAR LOS DATOS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }

    public void actualizaPrecio(String op,Double nuevovalor,String juego,String ca){
        rs0=null;
        try{
            String senSQLmov="UPDATE pedidos_detalle SET preciounitario='"+nuevovalor+"' WHERE id_op='"+op+"';";
            conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
            //actualiza la suma total
            senSQLmov="UPDATE pedidos SET subtotal='"+sumasubtotal+"' WHERE id_pedido='"+id.getText()+"';";
            conexion.modificaupdate_p(senSQLmov,connj,valor_privilegio);

            if(juego.equals("true")){

                    Double preciojuego=0.0;
                    int opjuego=0;
                    


            }else{
                senSQLmov="INSERT INTO bitacora_op(usuario, fecha, op, clavearticulo, descripcion,host) VALUES ('"+conf.getProperty("UserID")+"', '"+fechainsertarhora.format(new Date())+"', '"+op+"', '"+ca+"', 'Modificacion de Precio por: "+nuevovalor+"','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                senSQLmov="UPDATE ops SET preciounitario='"+nuevovalor+"' WHERE id_op='"+op+"';";
                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                //cambia el precio unitario si aun no sido facturada
                senSQLmov="UPDATE remisiones SET preciounitario='"+nuevovalor+"' WHERE (id_op='"+op+"' AND facturado<>'TRUE');";
                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
            }

        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL ACTUALIZAR LOS DATOS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
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
        jLabel4 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        clavecliente = new javax.swing.JTextField();
        btnclientes = new javax.swing.JButton();
        nombrecliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        fecha = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        oc = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        btnremove = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        subtotal = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        notas = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        maquila = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_pedidos.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        id.setEditable(false);
        id.setFont(resourceMap.getFont("id.font")); // NOI18N
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setFocusable(false);
        id.setName("id"); // NOI18N

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(241, Short.MAX_VALUE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(255, 255, 255))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        clavecliente.setEditable(false);
        clavecliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavecliente.setText(resourceMap.getString("clavecliente.text")); // NOI18N
        clavecliente.setFocusable(false);
        clavecliente.setName("clavecliente"); // NOI18N

        btnclientes.setIcon(resourceMap.getIcon("btnclientes.icon")); // NOI18N
        btnclientes.setText(resourceMap.getString("btnclientes.text")); // NOI18N
        btnclientes.setToolTipText(resourceMap.getString("btnclientes.toolTipText")); // NOI18N
        btnclientes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnclientes.setName("btnclientes"); // NOI18N
        btnclientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnclientesActionPerformed(evt);
            }
        });

        nombrecliente.setEditable(false);
        nombrecliente.setText(resourceMap.getString("nombrecliente.text")); // NOI18N
        nombrecliente.setFocusable(false);
        nombrecliente.setName("nombrecliente"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        fecha.setEditable(false);
        fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fecha.setText(resourceMap.getString("fecha.text")); // NOI18N
        fecha.setFocusable(false);
        fecha.setName("fecha"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        oc.setText(resourceMap.getString("oc.text")); // NOI18N
        oc.setName("oc"); // NOI18N
        oc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ocFocusGained(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave Articulo", "Articulo", "Juego", "Cantidad", "Precio Unitario", "Importe", "Lugar de Entrega", "Fecha Entrega", "Observaciones", "OP"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, false, false, false, true, false
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
        Tabladatos.setColumnSelectionAllowed(true);
        Tabladatos.setName("Tabladatos"); // NOI18N
        Tabladatos.setRowHeight(22);
        Tabladatos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Tabladatos.getTableHeader().setReorderingAllowed(false);
        Tabladatos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TabladatosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        btnremove.setIcon(resourceMap.getIcon("btnremove.icon")); // NOI18N
        btnremove.setText(resourceMap.getString("btnremove.text")); // NOI18N
        btnremove.setMargin(new java.awt.Insets(2, 3, 2, 3));
        btnremove.setName("btnremove"); // NOI18N
        btnremove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoveActionPerformed(evt);
            }
        });

        btnadd.setIcon(resourceMap.getIcon("btnadd.icon")); // NOI18N
        btnadd.setText(resourceMap.getString("btnadd.text")); // NOI18N
        btnadd.setMargin(new java.awt.Insets(2, 3, 2, 3));
        btnadd.setName("btnadd"); // NOI18N
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        subtotal.setBackground(resourceMap.getColor("subtotal.background")); // NOI18N
        subtotal.setEditable(false);
        subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        subtotal.setText(resourceMap.getString("subtotal.text")); // NOI18N
        subtotal.setFocusable(false);
        subtotal.setName("subtotal"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        notas.setColumns(20);
        notas.setFont(resourceMap.getFont("notas.font")); // NOI18N
        notas.setLineWrap(true);
        notas.setRows(3);
        notas.setName("notas"); // NOI18N
        jScrollPane2.setViewportView(notas);

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        maquila.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Si", " " }));
        maquila.setSelectedIndex(1);
        maquila.setName("maquila"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(maquila, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(maquila, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(82, 82, 82)
                            .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnremove, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(clavecliente)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(id, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(oc))
                                .addComponent(nombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(oc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(clavecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(btnclientes)))
                    .addComponent(nombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnremove)
                    .addComponent(btnadd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        if (Tabladatos.getCellEditor() != null) {//finaliza el editor
            Tabladatos.getCellEditor().stopCellEditing();
        }
        int filas=Tabladatos.getRowCount();
        int columnas=Tabladatos.getColumnCount();
        int camposvacios=0;
        int nopartidas=0;
        for(int i=0;i<=(filas-1);i=i+1){
            for(int j=0;j<=(columnas-3);j=j+1){
                if(modelot1.getValueAt(i, j) == null||modelot1.getValueAt(i, j).equals("")){
                    camposvacios=1;
                }
                if(j==5){
                    Double importet=(Double) modelot1.getValueAt(i, j);
                    if(importet<=0.0)
                        camposvacios=1;
                }
            }
            nopartidas++;
        }/**fin de revisar los campos vacios*/

        if(fecha.getText().equals("") || clavecliente.getText().equals("") || sumasubtotal<=0.0 || camposvacios==1 || filas<=0){
            String err="VERIFICA HAY CAMPOS VACIOS";
            if(camposvacios==1)
                err="LA TABLA DE DETALLE TIENE CAMPOS VACIOS";
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{

            String senSQL="";
            if(id.getText().equals("")){                
                //guardamos la partida principal
                senSQL="INSERT INTO pedidos(fecha, estatus, id_clientes, ordencompra, notas, partidas, subtotal,maquila) VALUES ('"+fechainsertarhora.format(new Date())+"', 'Act', '"+clavecliente.getText()+"', '"+oc.getText()+"', '"+notas.getText()+"', '"+nopartidas+"', '"+sumasubtotal+"','"+(String)maquila.getSelectedItem()+"');";
                conexion.modifica_p(senSQL,connj,valor_privilegio);

                /**funcion que regresa el numero con que se guardo la orden de compra*/
                int claveidmax=1;
                rs0=null;
                try{
                    senSQL="SELECT max(id_pedido) as pedidomax FROM pedidos";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        claveidmax=rs0.getInt("pedidomax");
                        id.setText(""+claveidmax);
                    }
                    if(rs0!=null) {
                        rs0.close();
                    }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                /**funcion que regresa la ultima op*/
                int opmax=999; //numero inicial de las ordenes de produccion
                rs0=null;
                try{
                    senSQL="SELECT max(id_op) as opmax FROM ops";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        opmax=rs0.getInt("opmax");
                    }
                    if(rs0!=null) {
                        rs0.close();
                    }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                if(opmax==0)
                    opmax=999;

                /**funcion que registra todos los movimientos de la orden de compra*/
                for(int i=0;i<=(filas-1);i=i+1){
                    opmax=opmax+1;
                    String senSQLmov="INSERT INTO pedidos_detalle(id_pedido, partida, clavearticulo, juego, cantidad, preciounitario, id_lugarentrega, fechaentrega, observaciones, id_op) VALUES ('"+claveidmax+"', '"+(i+1)+"', '"+modelot1.getValueAt(i, 0)+"', '"+modelot1.getValueAt(i, 2)+"', '"+modelot1.getValueAt(i, 3)+"', '"+modelot1.getValueAt(i, 4)+"', '"+modelot1.getValueAt(i, 6)+"', '"+modelot1.getValueAt(i, 7)+"', '"+modelot1.getValueAt(i, 8)+"', '"+opmax+"');";
                    conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                    //**funcion que genera las ordenes de produccion//
                    String tipoop=""+modelot1.getValueAt(i, 2); //verifica la orden para los juegos
                    if(tipoop.equals("false")){
                        String senSQLop="INSERT INTO ops(id_op, op, estatus, fecha, id_clientes, ordencompra, id_pedido, clavearticulo, cantidad, preciounitario, id_lugarentrega, fechaentrega, observaciones,maquila) VALUES ('"+opmax+"', '"+opmax+"', 'Act', '"+fechainsertarhora.format(new Date())+"', '"+clavecliente.getText()+"', '"+oc.getText()+"', '"+claveidmax+"', '"+modelot1.getValueAt(i, 0)+"', '"+modelot1.getValueAt(i, 3)+"', '"+modelot1.getValueAt(i, 4)+"', '"+modelot1.getValueAt(i, 6)+"', '"+modelot1.getValueAt(i, 7)+"', '"+modelot1.getValueAt(i, 8)+"','"+(String)maquila.getSelectedItem()+"');";
                        conexion.modificamov_p(senSQLop,connj,valor_privilegio);
                    }else{
                        int opjuego=0;
                        rs0=null;
                        try{
                            senSQL="SELECT articulos_juegos.*,articulos.articulo,articulos.preciomillar,articulos.piezas FROM articulos_juegos LEFT JOIN articulos ON articulos_juegos.clavearticulo1=articulos.clavearticulo WHERE articulos_juegos.clavearticulo='"+modelot1.getValueAt(i, 0)+"'";
                            rs0=conexion.consulta(senSQL,connj);
                            while(rs0.next()){
                                opjuego++;
                                Double juegoscantidad=rs0.getDouble("piezas")*(Double)modelot1.getValueAt(i, 3);
                                String senSQLop="INSERT INTO ops(id_op, op, estatus, fecha, id_clientes, ordencompra, id_pedido, clavearticulo, cantidad, preciounitario, id_lugarentrega, fechaentrega, observaciones,maquila) VALUES ('"+opmax+"', '"+opmax+"-"+opjuego+"', 'Act', '"+fechainsertarhora.format(new Date())+"', '"+clavecliente.getText()+"', '"+oc.getText()+"', '"+claveidmax+"', '"+rs0.getString("clavearticulo1")+"', '"+juegoscantidad+"', '"+(rs0.getDouble("preciomillar")/1000)+"', '"+modelot1.getValueAt(i, 6)+"', '"+modelot1.getValueAt(i, 7)+"', '"+modelot1.getValueAt(i, 8)+"','"+(String)maquila.getSelectedItem()+"');";
                                conexion.modificamov_p(senSQLop,connj,valor_privilegio);
                            }
                            if(rs0!=null) {
                                rs0.close();
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
            }else{ //utilizado para las modificaciones
                int respuesta=JOptionPane.showConfirmDialog(this,"ACTUALIZARA ORDEN DE COMPRA(AFECTA OP's) Y NOTAS\nDESEA CONTINUAR ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    senSQL="UPDATE pedidos SET ordencompra='"+oc.getText()+"',  notas='"+notas.getText()+"',maquila='"+(String)maquila.getSelectedItem()+"' WHERE id_pedido='"+id.getText()+"';";
                    conexion.modifica_p(senSQL,connj,valor_privilegio);
                    senSQL="UPDATE ops SET ordencompra='"+oc.getText()+"',maquila='"+(String)maquila.getSelectedItem()+"' WHERE id_pedido='"+id.getText()+"';";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    for(int i=0;i<=(filas-1);i=i+1){
                        try{
                            senSQL="INSERT INTO bitacora_op(usuario, fecha, op, clavearticulo, descripcion,host) VALUES ('"+conf.getProperty("UserID")+"', '"+fechainsertarhora.format(new Date())+"', '"+modelot1.getValueAt(i, 9)+"', '"+modelot1.getValueAt(i, 0)+"', 'Modificacion de orden de Compra: "+ocantigua+" por: "+oc.getText()+"','"+java.net.InetAddress.getLocalHost().getCanonicalHostName()+"');";
                            conexion.modificamov_p(senSQL,connj,valor_privilegio);
                        }catch(Exception e){  }
                    }
                }
            }
            salir();
            
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

    private void btnclientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnclientesActionPerformed
        // TODO add your handling code here:
        limpiatabla();
        busca_clientes busca_clientes = new busca_clientes(null,true,connj,"");
        busca_clientes.setLocationRelativeTo(this);
        busca_clientes.setVisible(true);
        clavecliente.setText(busca_clientes.getText());//obtiene el valor seleccionado
        busca_clientes=null;
        if(clavecliente.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM clientes WHERE id_clientes='"+clavecliente.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    String bloqueado=""+rs0.getString("bloqueado");
                    if(bloqueado.equals("0")){
                        JOptionPane.showMessageDialog(this,"EL CLIENTE ESTA SUSPENDIDO NO PUEDES CAPTURAR PEDIDOS","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        clavecliente.setText("");
                    }else{
                        nombrecliente.setText(rs0.getString("nombre"));
                    }
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
}//GEN-LAST:event_btnclientesActionPerformed

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        if(clavecliente.getText().equals("")){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UN CLIENTE", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            if(filas<10){
                Object datos[]={"","",false,0.00,0.00000,0.00,"","","",""};
                modelot1.addRow(datos);
            }else{
                JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnaddActionPerformed

    private void btnremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoveActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO DE ELIMINAR LA FILA  "+(filano+1)+" ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                modelot1.removeRow(filano);
            }else{

            }
        }
    }//GEN-LAST:event_btnremoveActionPerformed

    private void TabladatosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }

        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
            int filano=Tabladatos.getSelectedRow();
            int colno=Tabladatos.getSelectedColumn();


            if(clavecliente.getText().equals("")){
                JOptionPane.showMessageDialog(this,"TIENES QUE SELECCIONAR UN CLIENTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                if (Tabladatos.getCellEditor() != null) {
                    Tabladatos.getCellEditor().cancelCellEditing();
                }
                Tabladatos.changeSelection(filano, 0, false, false);
            }else{

                if(colno==0 && clavemodificau.equals("")){ //columna de la clave del articulo

                        if (Tabladatos.getCellEditor() != null) {
                            Tabladatos.getCellEditor().stopCellEditing();
                        }
                        String valorfiltro=(String)Tabladatos.getValueAt(filano, colno);
                        busca_articulos busca_articulos = new busca_articulos(null,true,connj,"",clavecliente.getText());
                        busca_articulos.setLocationRelativeTo(this);
                        busca_articulos.setVisible(true);
                        Tabladatos.setValueAt(busca_articulos.getText(), filano, colno);
                        valorfiltro=(String)Tabladatos.getValueAt(filano, colno);
                        busca_articulos=null;

                        if(valorfiltro.equals("")){
                            Tabladatos.setValueAt("", filano, 1);
                            Tabladatos.setValueAt( 0.0, filano, 3);
                        }else{

                            rs0=null;
                            try{
                                String senSQL="SELECT articulos.*,monedas.valor FROM articulos LEFT JOIN monedas ON articulos.id_moneda=monedas.id_moneda WHERE articulos.clavearticulo='"+valorfiltro+"'";
                                rs0=conexion.consulta(senSQL,connj);
                                if(rs0.next()){
                                    Tabladatos.setValueAt(rs0.getString("articulo"), filano, 1);
                                    permitecambios=0;
                                    Tabladatos.setValueAt( ((rs0.getDouble("preciomillar")/1000)*rs0.getDouble("valor")) , filano, 4);
                                    permitecambios=1;
                                    Tabladatos.setValueAt( 0.0, filano, 3);
                                }
                                if(rs0!=null) {
                                    rs0.close();
                                }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                        }
                        Tabladatos.changeSelection(filano, 2, false, false);

                }//columna de la clave del articulo


                if(colno==6){ //columna del lugar de entrega

                        if (Tabladatos.getCellEditor() != null) {
                            Tabladatos.getCellEditor().stopCellEditing();
                        }
                        String valorfiltro=(String)Tabladatos.getValueAt(filano, colno);
                        busca_lugares_entrega busca_lugares_entrega = new busca_lugares_entrega(null,true,connj,"",clavecliente.getText());
                        busca_lugares_entrega.setLocationRelativeTo(this);
                        busca_lugares_entrega.setVisible(true);
                        Tabladatos.setValueAt(busca_lugares_entrega.getText(), filano, colno);
                        valorfiltro=(String)Tabladatos.getValueAt(filano, colno);
                        busca_lugares_entrega=null;

                        Tabladatos.changeSelection(filano, 7, false, false);
                }//columna de la clave del articulo

                if(colno==7){ //columna de fecha de entrega

                        if (Tabladatos.getCellEditor() != null) {
                            Tabladatos.getCellEditor().stopCellEditing();
                        }

                        busca_fechas_entrega busca_fechas_entrega = new busca_fechas_entrega(null,true,connj);
                        busca_fechas_entrega.setLocationRelativeTo(this);
                        busca_fechas_entrega.setVisible(true);
                        String estado=busca_fechas_entrega.getEstado();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
                        if(estado.equals("cancelar")){
                            Tabladatos.setValueAt("", filano, colno);
                        }else{
                            Tabladatos.setValueAt(fechainsertar.format( busca_fechas_entrega.getFechaentrega() ), filano, colno);
                        }
                        busca_fechas_entrega=null;

                        Tabladatos.changeSelection(filano, 8, false, false);


                }//columna de la clave del articulo

            }//fin de verificar el cliente
        }
    }//GEN-LAST:event_TabladatosKeyPressed

    private void ocFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ocFocusGained
        // TODO add your handling code here:
        oc.selectAll();
    }//GEN-LAST:event_ocFocusGained

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnclientes;
    private javax.swing.JButton btnremove;
    private javax.swing.JTextField clavecliente;
    private javax.swing.JTextField fecha;
    private javax.swing.JTextField id;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox maquila;
    private javax.swing.JTextField nombrecliente;
    private javax.swing.JTextArea notas;
    private javax.swing.JTextField oc;
    private javax.swing.JTextField subtotal;
    // End of variables declaration//GEN-END:variables

}
