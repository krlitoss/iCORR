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

import java.awt.Component;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public class datos_ops extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    DecimalFormat fijo1decimales=new DecimalFormat("######0.0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales=new DecimalFormat("######0.000");
    DecimalFormat fijo4decimales=new DecimalFormat("######0.0000");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");

    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat fechamostrarhora = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
    SimpleDateFormat fechamostrarhoracorta = new SimpleDateFormat("dd-MMM-yy HH:mm");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    DefaultTableModel modelot1=null;
    DefaultTableModel modelot2=null;
    DefaultTableModel modelot3=null;

    /** Creates new form datos_usuarios */
    public datos_ops(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica) {
        super(parent, modal);
        initComponents();
        connj=conn;
        modelot1=(DefaultTableModel) Tablamaquinas.getModel();
        Tablamaquinas.setModel(modelot1);
        modelot2=(DefaultTableModel) Tablabitacora.getModel();
        Tablabitacora.setModel(modelot2);
        modelot3=(DefaultTableModel) Tablafacturas.getModel();
        Tablafacturas.setModel(modelot3);
        ajusteTabladatos();
        consultamodifica(clavemodifica);
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);
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
        Tablamaquinas.getColumnModel().getColumn(0).setPreferredWidth(80);
        Tablamaquinas.getColumnModel().getColumn(1).setPreferredWidth(80);
        Tablamaquinas.getColumnModel().getColumn(2).setPreferredWidth(80);
        Tablamaquinas.getColumnModel().getColumn(3).setPreferredWidth(70);
        Tablamaquinas.getColumnModel().getColumn(4).setPreferredWidth(70);
        Tablamaquinas.getColumnModel().getColumn(5).setPreferredWidth(75);
        Tablamaquinas.getColumnModel().getColumn(6).setPreferredWidth(70);
        Tablamaquinas.getColumnModel().getColumn(7).setPreferredWidth(90);
        Tablamaquinas.getColumnModel().getColumn(8).setPreferredWidth(80);
        Tablamaquinas.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablamaquinas.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablamaquinas.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablamaquinas.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer());
        Tablamaquinas.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer());
        Tablamaquinas.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());

        Tablabitacora.getColumnModel().getColumn(0).setPreferredWidth(80);
        Tablabitacora.getColumnModel().getColumn(1).setPreferredWidth(100);
        Tablabitacora.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablabitacora.getColumnModel().getColumn(3).setPreferredWidth(400);

        Tablafacturas.getColumnModel().getColumn(0).setPreferredWidth(100);
        Tablafacturas.getColumnModel().getColumn(1).setPreferredWidth(120);
        Tablafacturas.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablafacturas.getColumnModel().getColumn(3).setPreferredWidth(100);
        Tablafacturas.getColumnModel().getColumn(4).setPreferredWidth(120);

    }
    public class DoubleRenderer extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;

                rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                if(text.equals("null"))
                    text="";

                rend.setText( text );
                return rend;
            }

    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
        modelot2.setNumRows(0);
        modelot3.setNumRows(0);
    }

    public void consultamodifica(String clavemodifica){
        limpiatabla();
        if(clavemodifica.equals("")){
            
        }else{

            rs0=null;
            try{
                String senSQL="SELECT ops.*,(ops.cantidad-COALESCE(remisionado.cantidadremision,0)) as cantidadpendiente,COALESCE(remisionado.cantidadremision,0) as cantidadremisionado,remisionado.fecharemisionado,articulos.articulo,articulos.diseno,articulos.claveresistencia,articulos.largo,articulos.ancho,articulos.scores,articulos.kg,articulos.m2,articulos.piezas,resistencias.color,resistencias.corrugado,clientes.nombre,lugares_entregas.id_lugarentrega,lugares_entregas.calle,lugares_entregas.numeroext,lugares_entregas.colonia,lugares_entregas.municipio,lugares_entregas.estado,lugares_entregas.contacto FROM (((ops LEFT JOIN (articulos LEFT JOIN resistencias ON articulos.claveresistencia=resistencias.clave) ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN lugares_entregas ON ops.id_lugarentrega=lugares_entregas.id_lugarentrega) LEFT JOIN (SELECT remisiones_detalle.op,sum(remisiones_detalle.cantidadpzas) as cantidadremision,max(remisiones.fechahora) as fecharemisionado FROM remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision WHERE (remisiones_detalle.estatus='Act') GROUP BY remisiones_detalle.op) as remisionado ON ops.op=remisionado.op WHERE ops.op='"+clavemodifica+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    op.setText(rs0.getString("op"));
                    clavearticulo.setText(rs0.getString("clavearticulo"));
                    articulo.setText(rs0.getString("articulo"));
                    Double cant=rs0.getDouble("cantidad");
                    cantidad.setText(estandarentero.format(cant));
                    oc.setText(rs0.getString("ordencompra"));
                    nombre.setText(rs0.getString("nombre"));
                    diseno.setSelectedItem(rs0.getString("diseno"));
                    resistencia.setText(rs0.getString("claveresistencia"));
                    corrugado.setText(rs0.getString("corrugado"));
                    color.setText(rs0.getString("color"));
                    largo.setText(fijo1decimales.format(rs0.getDouble("largo")));
                    ancho.setText(fijo1decimales.format(rs0.getDouble("ancho")));
                    piezas.setText(fijo1decimales.format(rs0.getDouble("piezas")));
                    scores.setText(rs0.getString("scores"));
                    fechaalta.setText(fechamascorta.format(rs0.getDate("fecha")));
                    fechaentrega.setText(fechamascorta.format(rs0.getDate("fechaentrega")));
                    estatus.setText(rs0.getString("estatus"));
                    Double pu=rs0.getDouble("preciounitario");
                    precio.setText(moneda2decimales.format(cant*pu));
                    Double kg=rs0.getDouble("kg");
                    pk.setText(moneda2decimales.format(pu/kg));
                    aream2.setText(estandarentero.format(rs0.getDouble("m2")*cant));
                    pesokg.setText(estandarentero.format(kg*cant));
                    lugarentrega.setText(rs0.getString("id_lugarentrega")+"\n"+rs0.getString("calle")+" "+rs0.getString("numeroext")+" Col. "+rs0.getString("colonia")+"\n"+rs0.getString("municipio")+", "+rs0.getString("estado")+"\nCONTACTO: "+rs0.getString("contacto"));
                    cantrem.setText(estandarentero.format(rs0.getDouble("cantidadremisionado")));
                    cantpen.setText(estandarentero.format(rs0.getDouble("cantidadpendiente")));
                }
                else{
                    JOptionPane.showMessageDialog(this,"LA ORDEN DE PRODUCCIÓN NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
                if(rs0!=null) {    rs0.close();     }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


            //PARA CONSULTAR BITACORA DE LAS ORDENES DE PRODUCCION
            rs0=null;
            try{
                String senSQL="SELECT * FROM bitacora_op WHERE bitacora_op.op='"+op.getText()+"' ORDER BY fecha";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={rs0.getString("usuario"),fechamostrarhoracorta.format( rs0.getTimestamp("fecha") ),rs0.getString("clavearticulo"),rs0.getString("descripcion")};
                    modelot2.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //PARA CONSULTA DE LOS PROCESOS DE LA OP
            //programado en corrugadora
            rs0=null;
            try{
                String senSQL="SELECT programa_corr_detalle.*,articulos.piezas,programa_corr.fechaproduccion FROM (programa_corr_detalle LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo) LEFT JOIN programa_corr ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr WHERE (programa_corr_detalle.op='"+op.getText()+"' AND programa_corr.estatus<>'Can') ORDER BY programa_corr.fechaproduccion";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={"Programado","CORR",fechamascorta.format( rs0.getTimestamp("fechaproduccion") ),rs0.getInt("id_programa_corr"),null,null,null,(rs0.getInt("laminas")*rs0.getInt("piezas")),null};
                    modelot1.addRow(datos);
                }
                if(rs0!=null) {  rs0.close();   }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            //producido en corrugadora
            rs0=null;
            try{
                String senSQL="SELECT conversion_captura.*,maquinas.unidadvelocidad FROM conversion_captura LEFT JOIN maquinas ON conversion_captura.clave=maquinas.clave WHERE (conversion_captura.op='"+op.getText()+"' AND conversion_captura.clave='CORR' AND conversion_captura.estatus<>'Can') ORDER BY conversion_captura.fechareal";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={"Producido",rs0.getString("clave"),fechamascorta.format( rs0.getTimestamp("fechareal") ),rs0.getInt("id_programa_corr"),rs0.getInt("minutosarreglo"),(rs0.getInt("prodml")/rs0.getInt("minutosprod")),rs0.getString("unidadvelocidad"),rs0.getInt("cantidadpiezas"),rs0.getInt("cantidadmalaspiezas")};
                    modelot1.addRow(datos);
                }
                if(rs0!=null) {  rs0.close();   }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            //programado en conversion
            rs0=null;
            try{
                String senSQL="SELECT programa_conversion.*,maquinas.unidadvelocidad FROM programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave WHERE (programa_conversion.op='"+op.getText()+"' AND programa_conversion.estatus<>'Can') ORDER BY programa_conversion.fechaproduccion;";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={"Programado",rs0.getString("clavemaquina"),fechamascorta.format( rs0.getTimestamp("fechaproduccion") ),rs0.getInt("id_programa_conversion"),rs0.getInt("arreglo"),rs0.getInt("velocidad"),rs0.getString("unidadvelocidad"),rs0.getInt("cantidadpiezas"),null};
                    modelot1.addRow(datos);
                }
                if(rs0!=null) {  rs0.close();   }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            //producido en conversion
            rs0=null;
            try{
                String senSQL="SELECT conversion_captura.*,maquinas.unidadvelocidad FROM conversion_captura LEFT JOIN maquinas ON conversion_captura.clave=maquinas.clave WHERE (conversion_captura.op='"+op.getText()+"' AND conversion_captura.clave<>'CORR' AND conversion_captura.estatus<>'Can') ORDER BY conversion_captura.fechareal";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={"Producido",rs0.getString("clave"),fechamascorta.format( rs0.getTimestamp("fechareal") ),rs0.getInt("id_programa_conversion"),rs0.getInt("minutosarreglo"),(rs0.getInt("cantidad")/rs0.getInt("minutosprod")),rs0.getString("unidadvelocidad"),rs0.getInt("cantidadpiezas"),rs0.getInt("cantidadmalaspiezas")};
                    modelot1.addRow(datos);
                }
                if(rs0!=null) {  rs0.close();   }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


            //PARA CONSULTAR LAS FACTURAS REALIZADAS DE LA OP
            rs0=null;
            try{
                String senSQL = "SELECT facturas.*,folios.aprobacion,folios.anoaprobacion,monedas.descripcion as desmoneda,clientes.rfc,clientes.nombre,clientes.dias,lugaresemision.municipio,lugaresemision.estado,impuestos.iva as ivaimpuestos,impuestos.ivaretenido,impuestos.isrretenido,facturas_detalle.remision,facturas_detalle.id_op,facturas_detalle.clavearticulo,facturas_detalle.descripcion,(CASE WHEN facturas_detalle.um='millar' OR facturas_detalle.um='MILLAR' THEN (facturas_detalle.cantidad/1000) ELSE facturas_detalle.cantidad END ) AS cantidad,facturas_detalle.um,(CASE WHEN facturas_detalle.um='millar' OR facturas_detalle.um='MILLAR' THEN (facturas_detalle.preciounitario*1000) ELSE facturas_detalle.preciounitario END ) AS preciounitario,facturas_detalle.subtotal as subtotalpartida,remisiones.fechahora as fecha_remision FROM (((((facturas LEFT JOIN folios ON facturas.id_folios=folios.id_folio) LEFT JOIN monedas ON facturas.id_moneda=monedas.id_moneda) LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes) LEFT JOIN lugaresemision ON facturas.id_lugaremision=lugaresemision.id_lugaremision) LEFT JOIN impuestos ON facturas.id_impuestos=impuestos.id_impuestos) INNER JOIN (facturas_detalle LEFT JOIN remisiones ON facturas_detalle.remision=remisiones.remision ) ON facturas.factura_serie=facturas_detalle.factura_serie WHERE facturas_detalle.id_op='" + op.getText() + "' AND facturas.estatus=1 ";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={rs0.getString("remision"),fechamostrarhoracorta.format( rs0.getTimestamp("fecha_remision") ), rs0.getDouble("cantidad"),rs0.getString("factura_serie"),fechamostrarhoracorta.format( rs0.getTimestamp("fecha") )};
                    modelot3.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        op = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        articulo = new javax.swing.JTextField();
        clavearticulo = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        cantidad = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        oc = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        diseno = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        resistencia = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        corrugado = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        color = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        precio = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        aream2 = new javax.swing.JTextField();
        pesokg = new javax.swing.JTextField();
        pk = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        fechaalta = new javax.swing.JTextField();
        fechaentrega = new javax.swing.JTextField();
        estatus = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        largo = new javax.swing.JFormattedTextField();
        ancho = new javax.swing.JFormattedTextField();
        scores = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        piezas = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablamaquinas = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tablabitacora = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Tablafacturas = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lugarentrega = new javax.swing.JTextArea();
        jLabel21 = new javax.swing.JLabel();
        cantrem = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        cantpen = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_ops.class);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(344, 344, 344)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(351, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        op.setFont(resourceMap.getFont("op.font")); // NOI18N
        op.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        op.setText(resourceMap.getString("op.text")); // NOI18N
        op.setName("op"); // NOI18N
        op.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                opFocusGained(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        articulo.setName("articulo"); // NOI18N
        articulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                articuloFocusGained(evt);
            }
        });

        clavearticulo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavearticulo.setText(resourceMap.getString("clavearticulo.text")); // NOI18N
        clavearticulo.setName("clavearticulo"); // NOI18N
        clavearticulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clavearticuloFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clavearticuloFocusLost(evt);
            }
        });

        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N

        cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cantidad.setText(resourceMap.getString("cantidad.text")); // NOI18N
        cantidad.setName("cantidad"); // NOI18N
        cantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cantidadFocusGained(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        oc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        oc.setText(resourceMap.getString("oc.text")); // NOI18N
        oc.setName("oc"); // NOI18N
        oc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ocFocusGained(evt);
            }
        });

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText(resourceMap.getString("jLabel30.text")); // NOI18N
        jLabel30.setName("jLabel30"); // NOI18N

        nombre.setText(resourceMap.getString("nombre.text")); // NOI18N
        nombre.setName("nombre"); // NOI18N
        nombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nombreFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clavearticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(oc, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(clavearticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(oc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setName("jPanel8"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel3.border.titleColor"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        diseno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Caja Regular", "1/2 Regular", "Armado Aut.", "Tapa", "Fondo", "Charola Troq", "Caja Fnd/Crz", "Caja Tpa/Crz", "Charola STD", "Lamina", "Div. Corta", "Div. Larga", "Separador", "Banda Std", "Suaje Esp.", "Esquinero", "Entrepaño", "Tapa Telescp", "Fondo Teles", "1/2 Cuerpo", "Rollo S.F.", "Cja Traslape", "División", "Medio Fondo", "Hoja", "Caja Esp.", "Caja T/F Cru", "Cojin", "Tira", "Sep rayado", "Maq. Lamina", "Maq. Caja" }));
        diseno.setName("diseno"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        resistencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        resistencia.setText(resourceMap.getString("resistencia.text")); // NOI18N
        resistencia.setName("resistencia"); // NOI18N
        resistencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                resistenciaFocusGained(evt);
            }
        });

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        corrugado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        corrugado.setText(resourceMap.getString("corrugado.text")); // NOI18N
        corrugado.setName("corrugado"); // NOI18N
        corrugado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                corrugadoFocusGained(evt);
            }
        });

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        color.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        color.setText(resourceMap.getString("color.text")); // NOI18N
        color.setName("color"); // NOI18N
        color.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diseno, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(corrugado, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(diseno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(resistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(corrugado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel6.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel6.border.titleColor"))); // NOI18N
        jPanel6.setName("jPanel6"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        precio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        precio.setName("precio"); // NOI18N
        precio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                precioMouseClicked(evt);
            }
        });
        precio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                precioFocusGained(evt);
            }
        });

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        aream2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        aream2.setText(resourceMap.getString("aream2.text")); // NOI18N
        aream2.setName("aream2"); // NOI18N
        aream2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                aream2FocusGained(evt);
            }
        });

        pesokg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pesokg.setText(resourceMap.getString("pesokg.text")); // NOI18N
        pesokg.setName("pesokg"); // NOI18N
        pesokg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pesokgFocusGained(evt);
            }
        });

        pk.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pk.setText(resourceMap.getString("pk.text")); // NOI18N
        pk.setName("pk"); // NOI18N
        pk.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pkFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pesokg, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aream2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pk, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(pk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(aream2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(pesokg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel5.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel5.border.titleColor"))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        fechaalta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechaalta.setText(resourceMap.getString("fechaalta.text")); // NOI18N
        fechaalta.setName("fechaalta"); // NOI18N
        fechaalta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fechaaltaFocusGained(evt);
            }
        });

        fechaentrega.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechaentrega.setText(resourceMap.getString("fechaentrega.text")); // NOI18N
        fechaentrega.setName("fechaentrega"); // NOI18N
        fechaentrega.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fechaentregaFocusGained(evt);
            }
        });

        estatus.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        estatus.setText(resourceMap.getString("estatus.text")); // NOI18N
        estatus.setName("estatus"); // NOI18N
        estatus.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                estatusFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fechaalta, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fechaentrega, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(estatus, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(fechaalta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(fechaentrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(estatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel4.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel4.border.titleColor"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        largo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        largo.setText(resourceMap.getString("largo.text")); // NOI18N
        largo.setName("largo"); // NOI18N
        largo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                largoFocusGained(evt);
            }
        });

        ancho.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        ancho.setText(resourceMap.getString("ancho.text")); // NOI18N
        ancho.setName("ancho"); // NOI18N
        ancho.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                anchoFocusGained(evt);
            }
        });

        scores.setText(resourceMap.getString("scores.text")); // NOI18N
        scores.setName("scores"); // NOI18N
        scores.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                scoresFocusGained(evt);
            }
        });

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        piezas.setText(resourceMap.getString("piezas.text")); // NOI18N
        piezas.setName("piezas"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(ancho, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(largo, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(piezas, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scores, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(largo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(ancho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(piezas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(scores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tablamaquinas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Accion", "Proceso", "Fecha", "Programa", "Arreglo", "Velocidad", "U.M.", "Cantidad Pzas", "Merma Pzas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablamaquinas.setToolTipText(resourceMap.getString("Tablamaquinas.toolTipText")); // NOI18N
        Tablamaquinas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablamaquinas.setName("Tablamaquinas"); // NOI18N
        Tablamaquinas.setRowHeight(22);
        Tablamaquinas.getTableHeader().setReorderingAllowed(false);
        Tablamaquinas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablamaquinasKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Tablamaquinas);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel7.TabConstraints.tabTitle"), resourceMap.getIcon("jPanel7.TabConstraints.tabIcon"), jPanel7); // NOI18N

        jPanel10.setName("jPanel10"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        Tablabitacora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Usuario", "Fecha", "Clave Articulo", "Descripción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablabitacora.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablabitacora.setName("Tablabitacora"); // NOI18N
        Tablabitacora.setRowHeight(20);
        Tablabitacora.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(Tablabitacora);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel10.TabConstraints.tabTitle"), resourceMap.getIcon("jPanel10.TabConstraints.tabIcon"), jPanel10); // NOI18N

        jPanel11.setName("jPanel11"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        Tablafacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Remisión", "Fecha Remisión", "Cantidad", "Fecha", "Fecha Factura"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
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
        Tablafacturas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablafacturas.setName("Tablafacturas"); // NOI18N
        Tablafacturas.setRowHeight(20);
        Tablafacturas.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(Tablafacturas);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 790, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 117, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel11.TabConstraints.tabTitle"), resourceMap.getIcon("jPanel11.TabConstraints.tabIcon"), jPanel11); // NOI18N

        jPanel9.setName("jPanel9"); // NOI18N

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        lugarentrega.setColumns(20);
        lugarentrega.setFont(resourceMap.getFont("lugarentrega.font")); // NOI18N
        lugarentrega.setLineWrap(true);
        lugarentrega.setRows(4);
        lugarentrega.setName("lugarentrega"); // NOI18N
        jScrollPane2.setViewportView(lugarentrega);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        cantrem.setBackground(resourceMap.getColor("cantrem.background")); // NOI18N
        cantrem.setEditable(false);
        cantrem.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cantrem.setText(resourceMap.getString("cantrem.text")); // NOI18N
        cantrem.setFocusable(false);
        cantrem.setName("cantrem"); // NOI18N

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        cantpen.setBackground(resourceMap.getColor("cantpen.background")); // NOI18N
        cantpen.setEditable(false);
        cantpen.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cantpen.setText(resourceMap.getString("cantpen.text")); // NOI18N
        cantpen.setFocusable(false);
        cantpen.setName("cantpen"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cantpen, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cantrem, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cantrem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(cantpen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName(resourceMap.getString("jTabbedPane1.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        String claveop=JOptionPane.showInputDialog(this, "INTRODUCE EL NUMERO DE ORDEN DE PRODUCCIÓN:", "B U S C A R !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        consultamodifica(claveop);
}//GEN-LAST:event_btnaceptarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void clavearticuloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clavearticuloFocusGained
        // TODO add your handling code here:
        clavearticulo.selectAll();
}//GEN-LAST:event_clavearticuloFocusGained

    private void articuloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_articuloFocusGained
        // TODO add your handling code here:
        articulo.selectAll();
}//GEN-LAST:event_articuloFocusGained

    private void clavearticuloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clavearticuloFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_clavearticuloFocusLost

    private void largoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_largoFocusGained
        // TODO add your handling code here:
        largo.selectAll();
    }//GEN-LAST:event_largoFocusGained

    private void anchoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchoFocusGained
        // TODO add your handling code here:
        ancho.selectAll();
    }//GEN-LAST:event_anchoFocusGained

    private void scoresFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_scoresFocusGained
        // TODO add your handling code here:
        scores.selectAll();
    }//GEN-LAST:event_scoresFocusGained

    private void precioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_precioFocusGained
        // TODO add your handling code here:
        precio.selectAll();
}//GEN-LAST:event_precioFocusGained

    private void TablamaquinasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablamaquinasKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_TablamaquinasKeyPressed

    private void precioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_precioMouseClicked
        // TODO add your handling code here:
}//GEN-LAST:event_precioMouseClicked

    private void opFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusGained
        // TODO add your handling code here:
        op.selectAll();
    }//GEN-LAST:event_opFocusGained

    private void cantidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cantidadFocusGained
        // TODO add your handling code here:
        cantidad.selectAll();
}//GEN-LAST:event_cantidadFocusGained

    private void ocFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ocFocusGained
        // TODO add your handling code here:
        oc.selectAll();
    }//GEN-LAST:event_ocFocusGained

    private void nombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nombreFocusGained
        // TODO add your handling code here:
        nombre.selectAll();
    }//GEN-LAST:event_nombreFocusGained

    private void resistenciaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resistenciaFocusGained
        // TODO add your handling code here:
        resistencia.selectAll();
    }//GEN-LAST:event_resistenciaFocusGained

    private void corrugadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_corrugadoFocusGained
        // TODO add your handling code here:
        corrugado.selectAll();
    }//GEN-LAST:event_corrugadoFocusGained

    private void colorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_colorFocusGained
        // TODO add your handling code here:
        color.selectAll();
    }//GEN-LAST:event_colorFocusGained

    private void fechaaltaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fechaaltaFocusGained
        // TODO add your handling code here:
        fechaalta.selectAll();
    }//GEN-LAST:event_fechaaltaFocusGained

    private void fechaentregaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fechaentregaFocusGained
        // TODO add your handling code here:
        fechaentrega.selectAll();
    }//GEN-LAST:event_fechaentregaFocusGained

    private void estatusFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_estatusFocusGained
        // TODO add your handling code here:
        estatus.selectAll();
    }//GEN-LAST:event_estatusFocusGained

    private void pkFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pkFocusGained
        // TODO add your handling code here:
        pk.selectAll();
    }//GEN-LAST:event_pkFocusGained

    private void aream2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_aream2FocusGained
        // TODO add your handling code here:
        aream2.selectAll();
    }//GEN-LAST:event_aream2FocusGained

    private void pesokgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesokgFocusGained
        // TODO add your handling code here:
        pesokg.selectAll();
    }//GEN-LAST:event_pesokgFocusGained

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tablabitacora;
    private javax.swing.JTable Tablafacturas;
    private javax.swing.JTable Tablamaquinas;
    private javax.swing.JFormattedTextField ancho;
    private javax.swing.JTextField aream2;
    private javax.swing.JTextField articulo;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JTextField cantidad;
    private javax.swing.JTextField cantpen;
    private javax.swing.JTextField cantrem;
    private javax.swing.JTextField clavearticulo;
    private javax.swing.JTextField color;
    private javax.swing.JTextField corrugado;
    private javax.swing.JComboBox diseno;
    private javax.swing.JTextField estatus;
    private javax.swing.JTextField fechaalta;
    private javax.swing.JTextField fechaentrega;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JFormattedTextField largo;
    private javax.swing.JTextArea lugarentrega;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField oc;
    private javax.swing.JTextField op;
    private javax.swing.JTextField pesokg;
    private javax.swing.JTextField piezas;
    private javax.swing.JTextField pk;
    private javax.swing.JFormattedTextField precio;
    private javax.swing.JTextField resistencia;
    private javax.swing.JTextField scores;
    // End of variables declaration//GEN-END:variables

}
