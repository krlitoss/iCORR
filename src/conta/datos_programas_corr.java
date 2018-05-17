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

import java.awt.Color;
import java.awt.Point;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author IVONNE
 */
public class datos_programas_corr extends javax.swing.JDialog {
    Connection connj;
    Connection concontrol=null;
    private Properties conf;
    ResultSet rs0=null,rs1=null;
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat fijo1decimales=new DecimalFormat("######0.0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales=new DecimalFormat("######0.000");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat fechamostrarhora = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
    SimpleDateFormat fechamostrarhoracorta = new SimpleDateFormat("dd-MMM-yy HH:mm");
    SimpleDateFormat fechacm = new SimpleDateFormat("MM/dd/yyyy");

    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    DefaultTableModel modelot1=null;

    String clavemodificau="";

    Double l1m=0.0,l2m=0.0,l3m=0.0;
    Double l1c=0.0,r1c=0.0,l2c=0.0,r2c=0.0,l3c=0.0;
    Double l1p=0.0,l2p=0.0,l3p=0.0,r1p=0.0,r2p=0.0;
    Double l1pu=0.0,l2pu=0.0,l3pu=0.0,r1pu=0.0,r2pu=0.0;

    Double valorminimoresistencia=0.0;
    String tiporesistencia="";
    Double cortesopprincipal=0.0;
    Double valorrealresistencia=0.0;

    Double trim_std_min=2.0,trim_std_max=10.0;

    String noautorizado="Act";
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_programas_corr(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        if(!clavemodifica.equals(""))
            modelomodifica();
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        datos_parametros();
        ajusteTabladatos();
        conexioncontrolmaestro();
        
        fecha.setText(fechamostrarhoracorta.format(new Date()));
        fechaproducir.setDate(nuevaFechaProduccion());
        datosmaquinas();
        consultamodifica(clavemodifica);
        clavemodificau=clavemodifica;
        conf=conexion.archivoInicial();
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

        modelot1.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    int errorop=0;
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla

                    if (c == 0) { //obtiene los datos de la OP
                        String opnueva=(String)model.getValueAt(f, c);
                        if(!opnueva.equals("") && clavemodificau.equals("") && !opnueva.toUpperCase().equals("STOCK")){
                            rs0=null;
                            try{
                                //String senSQL2="SELECT ops.*,(ops.cantidad-COALESCE(remisionado.cantidadremision,0)) as cantidadpendiente,COALESCE(remisionado.cantidadremision,0) as cantidadremisionado,remisionado.fecharemisionado,articulos.articulo,articulos.diseno,articulos.claveresistencia,articulos.largo,articulos.ancho,articulos.scores,articulos.kg,articulos.m2,articulos.piezas,resistencias.corrugado FROM (ops LEFT JOIN (articulos LEFT JOIN resistencias ON articulos.claveresistencia=resistencias.clave) ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT remisiones_detalle.op,sum(remisiones_detalle.cantidadpzas) as cantidadremision,max(remisiones.fechahora) as fecharemisionado FROM remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision WHERE (remisiones_detalle.estatus='Act') GROUP BY remisiones_detalle.op) as remisionado ON ops.op=remisionado.op WHERE (ops.op='"+opnueva+"' and estatus<>'Can')";
                                String senSQL="SELECT ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.aplicaciones_especiales,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.flauta_art,articulos.largo,articulos.ancho,clientes.nombre,prodcorr.fechaprod,COALESCE(prodcorr.prodml,0) as prodml,COALESCE(prodcorr.prodcantidad,0) as prodcantidad,COALESCE(prodcorr.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prodcorr.prodcantkgpiezas,0) as prodcantkgpiezas,programcorr.fechaprogram,COALESCE(programcorr.programcantidad,0) as programcantidad,COALESCE(programcorr.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(programcorr.programcantkgpiezas,0) as programcantkgpiezas,articulos_maquinas.clave FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='CORR' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prodcorr ON ops.op=prodcorr.op ) LEFT JOIN (SELECT programa_corr_detalle.op,max(programa_corr.fechaproduccion) as fechaprogram,sum(programa_corr_detalle.laminas) as programcantidad,sum(articulos.piezas*programa_corr_detalle.laminas) as programcantidadpiezas,sum(articulos.piezas*programa_corr_detalle.laminas*articulos.kg) as programcantkgpiezas  FROM (programa_corr INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo  WHERE (programa_corr.estatus<>'Can' AND programa_corr.estatus<>'Pen') GROUP BY programa_corr_detalle.op) as programcorr ON ops.op=programcorr.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.op='"+opnueva+"' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='CORR' ) ORDER BY ops.fechaentrega,ops.op;";
                                rs0=conexion.consulta(senSQL,connj);
                                if(rs0.next()){
                                    model.setValueAt(rs0.getString("clavearticulo"), f, 1);
                                    String clave_res=rs0.getString("claveresistencia");
                                    model.setValueAt(clave_res, f, 2);
                                    Double largopartida=rs0.getDouble("largo");
                                    model.setValueAt(largopartida, f, 6);
                                    model.setValueAt(rs0.getDouble("ancho"), f, 7);
                                    int laminaspedidas=rs0.getInt("laminaspedidas");
                                    int laminasprogram=rs0.getInt("programcantidad");
                                    int laminasprod=rs0.getInt("prodcantidad");
                                    model.setValueAt(laminaspedidas-laminasprogram, f, 8);
                                    model.setValueAt(laminaspedidas-laminasprod, f, 9);
                                    model.setValueAt(rs0.getString("articulo"), f, 10);
                                    model.setValueAt(rs0.getString("aplicaciones_especiales"), f, 11);
                                    model.setValueAt(rs0.getString("flauta_art"), f, 12);
                                    //modifica los cortes
                                    if(f>0){
                                        int nuevoscortes=(int)(Integer.parseInt(metroslineales.getText())/(largopartida/100));
                                        model.setValueAt(Integer.parseInt(""+nuevoscortes),f, 4);
                                    }
                                    if(clave_res.length()>0){
                                        String r1=""+clave_res.charAt(0);
                                        String r2=""+corrugado.getText().charAt(0);
                                        if(!r1.equals(r2)){
                                            JOptionPane.showMessageDialog(null,"LA OP'S NO SON DEL MISMO TIPO DE CORRUGADO","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                            errorop=1; //verifica que tenga el mismo corrugado
                                        }
                                    }
                                }
                                else{
                                    JOptionPane.showMessageDialog(null,"LA ORDEN DE PRODUCCIÓN NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                    errorop=1;
                                }
                                if(rs0!=null) {
                                    rs0.close();
                                }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                            Tabladatos.changeSelection(f, 3, false, false);
                        }
                        sumatoria();
                    }
                    if (c == 1) { //obtiene los datos de la OP
                        String op_stock=(String)model.getValueAt(f, 0);
                        String articulo_stock=(String)model.getValueAt(f, c);
                        if(!articulo_stock.equals("") && clavemodificau.equals("") && op_stock.toUpperCase().equals("STOCK")){
                            rs0=null;
                            try{
                                String senSQL="SELECT * FROM articulos WHERE clavearticulo='"+articulo_stock+"';";
                                rs0=conexion.consulta(senSQL,connj);
                                if(rs0.next()){
                                    String clave_res=rs0.getString("claveresistencia");
                                    model.setValueAt(clave_res, f, 2);
                                    Double largopartida=rs0.getDouble("largo");
                                    model.setValueAt(largopartida, f, 6);
                                    model.setValueAt(rs0.getDouble("ancho"), f, 7);
                                    int laminaspedidas=0;
                                    int laminasprogram=0;
                                    int laminasprod=0;
                                    model.setValueAt(laminaspedidas-laminasprogram, f, 8);
                                    model.setValueAt(laminaspedidas-laminasprod, f, 9);
                                    model.setValueAt(rs0.getString("articulo"), f, 10);
                                    model.setValueAt(rs0.getString("flauta_art"), f, 12);

                                    //modifica los cortes
                                    if(f>0){
                                        int nuevoscortes=(int)(Integer.parseInt(metroslineales.getText())/(largopartida/100));
                                        model.setValueAt(Integer.parseInt(""+nuevoscortes),f, 4);
                                    }
                                    if(clave_res.length()>0){
                                        String r1=""+clave_res.charAt(0);
                                        String r2=""+corrugado.getText().charAt(0);
                                        if(!r1.equals(r2)){
                                            JOptionPane.showMessageDialog(null,"LA OP'S NO SON DEL MISMO TIPO DE CORRUGADO","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                            errorop=1; //verifica que tenga el mismo corrugado
                                        }
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null,"EL ARTICULO NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                    errorop=1;
                                }
                                if(rs0!=null) {     rs0.close(); }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                            Tabladatos.changeSelection(f, 3, false, false);
                        }
                        sumatoria();
                    }
                    if (c == 3) { //multiplica los anchos
                        int valorpliegos=(Integer)model.getValueAt(f, c);
                        if(valorpliegos>0){
                            model.setValueAt((Integer) model.getValueAt(f, c) * (Integer) model.getValueAt(f, 4), f, 5);
                        }else{
                            model.setValueAt(0, f, 5);
                        }
                        sumatoria();
                        compruebacantidades(f);
                    }
                    if(c==4){
                        int valorcortes=(Integer)model.getValueAt(f, c);
                        if(f==0 && valorcortes>0){
                            cortesopprincipal=Double.parseDouble(""+valorcortes);
                            model.setValueAt((Integer) model.getValueAt(f, c) * (Integer) model.getValueAt(f, 3), f, 5);
                            sumatoria();
                            cambiosotroscortes();
                        }
                        if(f>0){
                            Double largopartida=(Double)model.getValueAt(f, 6);
                            int cortes=(Integer)model.getValueAt(f, 4);
                            int nuevoscortes=(int)(Integer.parseInt(metroslineales.getText())/(largopartida/100));
                            if(cortes!=nuevoscortes && largopartida>0){
                                modelot1.setValueAt(nuevoscortes, f, 4);
                            }
                            model.setValueAt((Integer) model.getValueAt(f, c) * (Integer) model.getValueAt(f, 3), f, 5);

                        }
                        sumatoria();
                        compruebacantidades(f);
                    }

                    if(errorop==1){
                        model.setValueAt("", f, 0);
                        model.setValueAt("", f, 1);
                        model.setValueAt("", f, 2);
                        model.setValueAt(0.0, f, 6);
                        model.setValueAt(0.0, f, 7);
                        model.setValueAt(0, f, 8);
                        model.setValueAt(0, f, 9);
                        model.setValueAt("", f, 10);
                        model.setValueAt("", f, 12);
                    }

                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "NO SE PUEDEN CALCULAR LOS DATOS"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
          }
        });

    }
    //datos de control maestro
    public void actualiza_op_cm(String op,int can){
        if(!op.equals("") || op.equals("null")){
            int totalprogramado=0;
            int piezaslam=0;
            rs0=null;
            try{
                String senSQL="SELECT [Ordenes Produccion].*,Articulos.Piezas FROM [Ordenes Produccion] LEFT JOIN Articulos ON [Ordenes Produccion].[Clave Articulo]=Articulos.[Clave Articulo] WHERE [Orden Produccion]='"+op+"'";
                rs0=conexion.consulta(senSQL,concontrol);
                if(rs0.next()){
                    totalprogramado=rs0.getInt("Total Programado");
                    piezaslam=rs0.getInt("Piezas");
                    String sentenciac="UPDATE [Ordenes Produccion] set [Fecha Programado]=#"+fechacm.format(fechaproducir.getDate())+"#, [Total Programado]="+((can*piezaslam)+totalprogramado)+" WHERE [Orden Produccion]='"+op+"';";
                    conexion.modificamov_p(sentenciac,concontrol,valor_privilegio);
                }
                if(rs0!=null) {  rs0.close(); }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL ACTUALIZA CM\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }

    public void salir(){
        if(connj!=null){
            connj=null;
            System.out.println("Conexion en null...............");
        }
        if(concontrol!=null){       ////****eliminar para el control maestro*******////////
            concontrol=null;
        }
        dispose();
        this.setVisible(false);
    }
    public void conexioncontrolmaestro(){       ////****eliminar para el control maestro*******////////
        /*if(concontrol!=null){

        }else{
            concontrol=conexion.abrirconexioncontrol();
        }*/
    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public void datos_parametros(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM parametros WHERE id_parametros='1'";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                trim_std_min=rs0.getDouble("trim_std_min");
                trim_std_max=rs0.getDouble("trim_std_max");
            }
            if(rs0!=null) {     rs0.close();   }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public void datosmaquinas(){
        //obtiene datos de arreglo y velocidad de la maquina
        rs0=null;
        try{
            String senSQL="SELECT * FROM maquinas WHERE clave='CORR'";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                txtarreglo.setText(fijo0decimales.format(rs0.getInt("arreglo")));
                txtvelocidad.setText(fijo0decimales.format(rs0.getInt("velocidad")));
            }
            if(rs0!=null) {  rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            //deshabilita los botones de agregar y eliminar filas
            anchototal.setEnabled(false);
            anchototal.setFocusable(false);
            
            btnadd.setEnabled(false);
            btnremove.setEnabled(false);
            btnresistencia.setEnabled(false);
            f1.setEnabled(false);
            f2.setEnabled(false);
            Double valorresistencia=0.0;
            rs0=null;
            try{
                String senSQL="SELECT programa_corr.*,resistencias.corrugado,resistencias.color,resistencias.valorminimo,resistencias.tipo,programa_corr_detalle.*,articulos.claveresistencia as cr,articulos.articulo,articulos.flauta_art,articulos.largo,articulos.ancho,articulos.aplicaciones_especiales FROM ( (programa_corr LEFT JOIN resistencias ON programa_corr.claveresistencia=resistencias.clave) INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo   WHERE (programa_corr.id_programa_corr='"+clavemodifica+"') ORDER BY programa_corr_detalle.id_programa_corr_detalle";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    id.setText(rs0.getString("id_programa_corr"));
                    fecha.setText( fechamostrarhoracorta.format(rs0.getTimestamp("fecha")) );
                    fechaproducir.setDate(rs0.getDate("fechaproduccion"));
                    Double anchototalt=rs0.getDouble("anchototal");
                    anchototal.setText(fijo1decimales.format(anchototalt));
                    resistencia.setText(rs0.getString("claveresistencia"));
                    corrugado.setText(rs0.getString("corrugado"));
                    color.setText(rs0.getString("color"));
                    valorminimoresistencia=rs0.getDouble("valorminimo");
                    tiporesistencia=rs0.getString("tipo");
                    txtarreglo.setText(fijo0decimales.format(rs0.getInt("arreglo")));
                    txtvelocidad.setText(fijo0decimales.format(rs0.getInt("velocidad")));
                    
                    if(rs0.getString("estatus").equals("Ter"))
                        btnaceptar.setEnabled(false);

                    l1r.setText(rs0.getString("l1r"));
                    r1r.setText(rs0.getString("r1r"));
                    l2r.setText(rs0.getString("l2r"));
                    r2r.setText(rs0.getString("r2r"));
                    l3r.setText(rs0.getString("l3r"));
                    f1.setSelectedItem(rs0.getString("f1"));
                    f2.setSelectedItem(rs0.getString("f2"));
                    anchol1r.setText(rs0.getString("al1r"));
                    anchor1r.setText(rs0.getString("ar1r"));
                    anchol2r.setText(rs0.getString("al2r"));
                    anchor2r.setText(rs0.getString("ar2r"));
                    anchol3r.setText(rs0.getString("al3r"));
                    pesom2real.setText(fijo3decimales.format( rs0.getDouble("pesorealcombinacion")));

                    metroslineales.setText(fijo0decimales.format( rs0.getDouble("ml") ));
                    Double anchoutilt=rs0.getDouble("anchoutil");
                    anchoutil.setText(fijo1decimales.format(anchoutilt));
                    Double refilefinal=anchototalt-anchoutilt;
                    refile.setBackground(new Color(173,216,237));
                    if(refilefinal<2.0)
                        refile.setBackground(new Color(255,153,153));

                    refile.setText(fijo1decimales.format( refilefinal ));

                    m2.setText(estandarentero.format(rs0.getDouble("m2")));
                    kg.setText(estandarentero.format(rs0.getDouble("kg")));
                    notas.setText(rs0.getString("notas"));
                    valorresistencia=rs0.getDouble("valorresistencia");

                    Object datos[]={rs0.getString("op"),rs0.getString("clavearticulo"),rs0.getString("cr"),rs0.getInt("pliegosancho"),rs0.getInt("cortes"),rs0.getInt("laminas"),rs0.getDouble("largo"),rs0.getDouble("ancho"),0,0,rs0.getString("articulo"),rs0.getString("aplicaciones_especiales"), rs0.getString("flauta_art")};
                    modelot1.addRow(datos);

                }
                if(rs0!=null) {
                    rs0.close();
                }
                datospapel1("");
                datospapel2("");
                datospapel3("");
                datospapel4("");
                datospapel5("");
                resultadocombinacion();//calcula los primeros resultados
                
                if(tiporesistencia.equals("Mullen")){
                    valorrealresistencia=valorresistencia;//asigna el valor que dio la combinacion si es mullen
                    mullen.setBackground(new Color(240,240,240));
                    if(valorresistencia<valorminimoresistencia)
                        mullen.setBackground(new Color(255,153,153));
                }else{
                    valorrealresistencia=valorresistencia;//asigna el valor que dio la combinacion si es mullen
                    ect.setBackground(new Color(240,240,240));
                    if(valorresistencia<valorminimoresistencia)
                        ect.setBackground(new Color(255,153,153));
                }

            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(65);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(55);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(200);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(12).setPreferredWidth(30);
    }

    public void sumatoria(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{
            Double sumaanchos=0.0;
            Double mlfinal=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                if(i==0)
                    mlfinal=Double.parseDouble(""+Tabladatos.getValueAt(i, 4))*(Double) Tabladatos.getValueAt(i, 6);///obtiene los ML
                
                Double valancho=Double.parseDouble(""+Tabladatos.getValueAt(i, 3))*(Double) Tabladatos.getValueAt(i, 7);///obtiene el ancho final
                sumaanchos=sumaanchos+valancho;
            }
            metroslineales.setText(fijo0decimales.format( mlfinal/100 ));
            anchoutil.setText(fijo1decimales.format(sumaanchos));
            Double refilefinal=Double.parseDouble(anchototal.getText())-sumaanchos;
            refile.setBackground(new Color(173,216,237));
            if(refilefinal<trim_std_min)
                refile.setBackground(new Color(255,153,153));

            refile.setText(fijo1decimales.format( refilefinal ));
        }
    }
    public void cambiosotroscortes(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{
            for(int i=0;i<=(filas-1);i=i+1){
                if(i>0){
                    Double largopartida=(Double)modelot1.getValueAt(i, 6);
                    int cortes=(Integer)modelot1.getValueAt(i, 4);
                    int nuevoscortes=(int)(Integer.parseInt(metroslineales.getText())/(largopartida/100));
                    if(cortes!=nuevoscortes && largopartida>0){
                        System.err.println("Cambio cortes"+cortes+"-"+nuevoscortes);
                        modelot1.setValueAt(nuevoscortes, i, 4);
                    }
                }
            }
        }
    }
    public void compruebacantidades(int i){
        //verifica que la cantidad que estas programando no exceda el 10% de lo pendiente por entregar
        String op_partida=""+Tabladatos.getValueAt(i, 0);
        rs1=null;
        try{
            String senSQL="SELECT ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.largo,articulos.ancho,clientes.nombre,prodcorr.fechaprod,COALESCE(prodcorr.prodml,0) as prodml,COALESCE(prodcorr.prodcantidad,0) as prodcantidad,COALESCE(prodcorr.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prodcorr.prodcantkgpiezas,0) as prodcantkgpiezas,programcorr.fechaprogram,COALESCE(programcorr.programcantidad,0) as programcantidad,COALESCE(programcorr.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(programcorr.programcantkgpiezas,0) as programcantkgpiezas,articulos_maquinas.clave FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='CORR' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prodcorr ON ops.op=prodcorr.op ) LEFT JOIN (SELECT programa_corr_detalle.op,max(programa_corr.fechaproduccion) as fechaprogram,sum(programa_corr_detalle.laminas) as programcantidad,sum(articulos.piezas*programa_corr_detalle.laminas) as programcantidadpiezas,sum(articulos.piezas*programa_corr_detalle.laminas*articulos.kg) as programcantkgpiezas  FROM (programa_corr INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo  WHERE (programa_corr.estatus<>'Can' AND programa_corr.estatus<>'Pen') GROUP BY programa_corr_detalle.op) as programcorr ON ops.op=programcorr.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.op='"+op_partida+"' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='CORR' ) ORDER BY ops.fechaentrega,ops.op;";
            rs1=conexion.consulta(senSQL,connj);
            if(rs1.next()){
                int laminaspedidas=rs1.getInt("laminaspedidas");
                int laminasprogram=rs1.getInt("programcantidad");
                int laminasprod=rs1.getInt("prodcantidad");
                int cantidadpendientelaminas=laminaspedidas-laminasprogram;
                int cantidadlaminasprograma=(Integer) Tabladatos.getValueAt(i, 5);
                if((cantidadlaminasprograma>(cantidadpendientelaminas*1.1)) && !op_partida.toUpperCase().equals("STOCK")){
                    JOptionPane.showMessageDialog(null,"EXCEDE MAS DEL 10% DE LAMINAS A PROGRAMAR\n<html><font size=5 color=blue><b>LA OP: "+Tabladatos.getValueAt(i, 0)+"</b></font><br><font size=5 color=#DC143C><b>Laminas Programadas: </b>"+cantidadlaminasprograma+"<br><b>Laminas Pendientes Programar: </b>"+cantidadpendientelaminas,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    Tabladatos.changeSelection(0, 5, false, false);
                }
            }
            if(rs1!=null) { rs1.close();  }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTAxx\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public Date nuevaFechaProduccion(){
        Date diae=new Date();
        diae=fechaMas(diae,0); //se modifico los dias que se adelanta la fecha de produccion corrugado
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(diae);

        if(calendar.get(Calendar.DAY_OF_WEEK)==1){ //valida que la fecha de entrega no sea domingo
            diae=fechaMas(diae,1);
        }
        return diae;
    }
    public java.util.Date fechaMas(java.util.Date fch, int dias){
         Calendar cal = new GregorianCalendar();
         cal.setTimeInMillis(fch.getTime());
         cal.add(Calendar.DATE, dias);
         return new Date(cal.getTimeInMillis());
   }
    public java.util.Date fechaMenos(java.util.Date fch, int dias){
         Calendar cal = new GregorianCalendar();
         cal.setTimeInMillis(fch.getTime());
         cal.add(Calendar.DATE, - dias);
         return new Date(cal.getTimeInMillis());
   }
    public void programarop(String clavemodifica,String cmaq,String programaconversion,String claveart_stock,int cantidad_prog_pzas,int program_corr_f){
        if(!clavemodifica.equals("")){
            datos_programas_conversion = new datos_programas_conversion(null,true,connj,clavemodifica,cmaq,programaconversion,claveart_stock,cantidad_prog_pzas,program_corr_f,noautorizado,valor_privilegio);
            datos_programas_conversion.setLocationRelativeTo(this);
            datos_programas_conversion.setVisible(true);
            datos_programas_conversion=null;
        }
    }
    public void programa_partida_conversion(String op_f,String claveart_f,int cantidad_prog,int program_corr_f){
        //selecciona las maquinas
        if(!op_f.equals("") || !op_f.equals("null")){
            int pregunta=0;
            rs0=null;
            try{
                String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,articulos.piezas,maquinas.unidadcapacidad FROM (articulos_maquinas LEFT JOIN maquinas ON articulos_maquinas.clave=maquinas.clave) LEFT JOIN articulos ON articulos_maquinas.clavearticulo=articulos.clavearticulo WHERE (articulos_maquinas.clavearticulo='"+claveart_f+"' AND articulos_maquinas.clave<>'CORR') ORDER BY articulos_maquinas.id_articulo_maquina;";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    if(pregunta==0){
                        if (JOptionPane.showConfirmDialog(this,"OP: "+op_f+"\nCLAVE ARTICULO: "+claveart_f+"\nDESEA PROGRAMAR LOS PROCESOS DE CONVERSION?????"," DESEAN CONTINUAR !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            pregunta=1;
                        }else{
                            pregunta=2;
                        }
                    }
                    if(pregunta==1){
                        String sonlaminas=rs0.getString("unidadcapacidad").toUpperCase();
                        int cant_prog_maquina=cantidad_prog;
                        if(sonlaminas.equals("PIEZAS")){ //verifica si son piezas las multiplica las laminas por piezas por lamina
                            //de lo contrario las envia como laminas para usarlas como golpes
                            cant_prog_maquina=(cantidad_prog*rs0.getInt("piezas"));
                        }
                        programarop(op_f,rs0.getString("clave"),"",claveart_f,cant_prog_maquina,program_corr_f);
                    }
                }
                if(rs0!=null) {    rs0.close();  }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }





    public void datospapel1(String ban){
        l1r.setText(l1r.getText().toUpperCase());
        String papel=l1r.getText();
        l1m=0.0;
        l1c=0.0;
        l1p=0.0;
        l1pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    l1r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    l1m=rs0.getDouble("mullen");
                    l1c=rs0.getDouble("rc");
                    l1p=rs0.getDouble("peso");
                    l1pu=rs0.getDouble("preciocompra");
                    resultadocombinacion();//calcula los primeros resultados
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    l1r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!l1r.getText().equals("")){
                        l1r.setText( l1r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+l1r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                l1m=rs0.getDouble("mullen");
                                l1c=rs0.getDouble("rc");
                                l1p=rs0.getDouble("peso");
                                l1pu=rs0.getDouble("preciocompra");
                                resultadocombinacion();//calcula los primeros resultados
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void datospapel2(String ban){
        r1r.setText(r1r.getText().toUpperCase());
        String papel=r1r.getText();
        r1c=0.0;
        r1p=0.0;
        r1pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){
            //VALIDA EL FACTOR DE ARROLLAMIENTO DEL PAPEL POR LA FLAUTA
            String flau1=(String) f1.getSelectedItem();
            Double factora1=1.48;
            if(flau1.equals("B"))
                factora1=1.36;

            Double factora2=1.44;
            if(flau1.equals("B"))
                factora2=1.33;

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    r1r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    r1c=rs0.getDouble("rc")*factora2;
                    r1p=rs0.getDouble("peso")*factora1;
                    r1pu=rs0.getDouble("preciocompra");
                    resultadocombinacion();//calcula los primeros resultados
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    r1r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!r1r.getText().equals("")){
                        r1r.setText( r1r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+r1r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                r1c=rs0.getDouble("rc")*factora2;
                                r1p=rs0.getDouble("peso")*factora1;
                                r1pu=rs0.getDouble("preciocompra");
                                resultadocombinacion();//calcula los primeros resultados
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void datospapel3(String ban){
        l2r.setText(l2r.getText().toUpperCase());
        String papel=l2r.getText();
        l2m=0.0;
        l2c=0.0;
        l2p=0.0;
        l2pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    l2r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    l2m=rs0.getDouble("mullen");
                    l2c=rs0.getDouble("rc");
                    l2p=rs0.getDouble("peso");
                    l2pu=rs0.getDouble("preciocompra");
                    resultadocombinacion();//calcula los primeros resultados
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    l2r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!l2r.getText().equals("")){
                        l2r.setText( l2r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+l2r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                l2m=rs0.getDouble("mullen");
                                l2c=rs0.getDouble("rc");
                                l2p=rs0.getDouble("peso");
                                l2pu=rs0.getDouble("preciocompra");
                                resultadocombinacion();//calcula los primeros resultados
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void datospapel4(String ban){
        r2r.setText(r2r.getText().toUpperCase());
        String papel=r2r.getText();
        r2c=0.0;
        r2p=0.0;
        r2pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){
            //VALIDA EL FACTOR DE ARROLLAMIENTO DEL PAPEL POR LA FLAUTA
            String flau1=(String) f2.getSelectedItem();
            Double factora1=1.48;
            if(flau1.equals("B"))
                factora1=1.36;

            Double factora2=1.44;
            if(flau1.equals("B"))
                factora2=1.33;

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    r2r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    r2c=rs0.getDouble("rc")*factora2;
                    r2p=rs0.getDouble("peso")*factora1;
                    r2pu=rs0.getDouble("preciocompra");
                    resultadocombinacion();//calcula los primeros resultados
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    r2r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!r2r.getText().equals("")){
                        r2r.setText( r2r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+r2r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                r2c=rs0.getDouble("rc")*factora2;
                                r2p=rs0.getDouble("peso")*factora1;
                                r2pu=rs0.getDouble("preciocompra");
                                resultadocombinacion();//calcula los primeros resultados
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void datospapel5(String ban){
        l3r.setText(l3r.getText().toUpperCase());
        String papel=l3r.getText();
        l3m=0.0;
        l3c=0.0;
        l3p=0.0;
        l3pu=0.0;

        if(!papel.equals("") || ban.equals("F7")){

            rs0=null;
            try{
                String senSQL="SELECT * FROM productos WHERE clave LIKE '"+papel+"%'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next() && !papel.equals("")){
                    l3r.setText( rs0.getString("clave").substring(0, 8).trim() );
                    l3m=rs0.getDouble("mullen");
                    l3c=rs0.getDouble("rc");
                    l3p=rs0.getDouble("peso");
                    l3pu=rs0.getDouble("preciocompra");
                    resultadocombinacion();//calcula los primeros resultados
                }else{

                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","3");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    l3r.setText(busca_productos.getText());
                    busca_productos=null;
                    if(!l3r.getText().equals("")){
                        l3r.setText( l3r.getText().substring(0, 8).trim() );

                        rs0=null;
                        try{
                            senSQL="SELECT * FROM productos WHERE clave LIKE '"+l3r.getText()+"%'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                l3m=rs0.getDouble("mullen");
                                l3c=rs0.getDouble("rc");
                                l3p=rs0.getDouble("peso");
                                l3pu=rs0.getDouble("preciocompra");
                                resultadocombinacion();//calcula los primeros resultados
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void resultadocombinacion(){
            Double valfinalmullen=0.0;

            valfinalmullen=(l1m+l2m)*0.07;
            if(corrugado.getText().equals("Sencillo"))
                valfinalmullen=(l1m+l2m)*0.07;
            if(corrugado.getText().equals("Doble"))
                valfinalmullen=(l1m+l2m+l3m)*0.07;

            Double valfinalect=0.0;
            if(corrugado.getText().equals("Sencillo"))
                valfinalect=(((l1c+r1c+l2c)/6)*0.8)+12;
            if(corrugado.getText().equals("Doble"))
                valfinalect=(((l1c+r1c+l2c+r2c+l3c)/6)*0.8)+12;

            pesom2real.setText(fijo3decimales.format(l1p+r1p+l2p+r2p+l3p));
            mullen.setText(fijo2decimales.format(valfinalmullen));
            ect.setText(fijo2decimales.format(valfinalect));

            if(tiporesistencia.equals("Mullen")){
                valorrealresistencia=valfinalmullen;//asigna el valor que dio la combinacion si es mullen
                mullen.setBackground(new Color(240,240,240));
                if(valfinalmullen<valorminimoresistencia)
                    mullen.setBackground(new Color(255,153,153));
            }else{
                valorrealresistencia=valfinalect;//asigna el valor que dio la combinacion si es mullen
                ect.setBackground(new Color(240,240,240));
                if(valfinalect<valorminimoresistencia)
                    ect.setBackground(new Color(255,153,153));
            }
    }

    public void verop(){
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String clavemodifica=(String)Tabladatos.getValueAt(filano, 0);
            if(clavemodifica.equals("")){

            }else{
                datos_ops = new datos_ops(null,true,connj,clavemodifica);
                datos_ops.setLocationRelativeTo(this);
                datos_ops.setVisible(true);
                datos_ops=null;
            }
        }
    }
    public void verarticulo(){
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String clavemodifica=(String)Tabladatos.getValueAt(filano, 1);
            if(clavemodifica.equals("")){

            }else{
                datos_articulos = new datos_articulos(null,true,connj,clavemodifica,"ver",valor_privilegio);
                datos_articulos.setLocationRelativeTo(this);
                datos_articulos.setVisible(true);
                datos_articulos=null;
            }
        }
    }
    public void modelomodifica(){
        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OP", "Clave Articulo", "Resis.", "Pliegos x Ancho", "Cortes", "Laminas", "Largo", "Ancho","<html><font color=#0066FF>LP x Program","<html><font color=green>LP x Producir", "Articulo","Aplicaciones Especiales", "Flauta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
    }
    public void busca_resistencia(String class_resis){
        if(resistencia.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM resistencias WHERE clave='"+class_resis+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    String corr=rs0.getString("corrugado");
                    corrugado.setText(corr);
                    color.setText(rs0.getString("color"));
                    if(corr.equals("Doble")){
                        f1.setSelectedIndex(0);
                        f2.setSelectedIndex(0);
                        anchol1r.setText(anchototal.getText());
                        anchor1r.setText(anchototal.getText());
                        anchol2r.setText(anchototal.getText());
                        anchor2r.setText(anchototal.getText());
                        anchol3r.setText(anchototal.getText());
                    }else{
                        f1.setSelectedIndex(0);
                        f2.setSelectedIndex(1);
                        anchol1r.setText(anchototal.getText());
                        anchor1r.setText(anchototal.getText());
                        anchol2r.setText(anchototal.getText());
                        anchor2r.setText("0.0");
                        anchol3r.setText("0.0");
                    }
                    l1r.setText(rs0.getString("l1r"));
                    r1r.setText(rs0.getString("r1r"));
                    l2r.setText(rs0.getString("l2r"));
                    r2r.setText(rs0.getString("r2r"));
                    l3r.setText(rs0.getString("l3r"));
                    valorminimoresistencia=rs0.getDouble("valorminimo");
                    tiporesistencia=rs0.getString("tipo");

                    datospapel1("");
                    datospapel2("");
                    datospapel3("");
                    datospapel4("");
                    datospapel5("");

                    resultadocombinacion();//calcula los primeros resultados

                }
                if(rs0!=null) {   rs0.close(); }
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

        menupop = new javax.swing.JPopupMenu();
        detalleop = new javax.swing.JMenuItem();
        detalleart = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        fecha = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        btnremove = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        refile = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        notas = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        anchoutil = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        metroslineales = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        m2 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        kg = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        l1r = new javax.swing.JTextField();
        r1r = new javax.swing.JTextField();
        l2r = new javax.swing.JTextField();
        r2r = new javax.swing.JTextField();
        l3r = new javax.swing.JTextField();
        f2 = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        f1 = new javax.swing.JComboBox();
        anchol1r = new javax.swing.JFormattedTextField();
        anchor1r = new javax.swing.JFormattedTextField();
        anchol2r = new javax.swing.JFormattedTextField();
        anchor2r = new javax.swing.JFormattedTextField();
        anchol3r = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pesom2real = new javax.swing.JFormattedTextField();
        mullen = new javax.swing.JFormattedTextField();
        ect = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        btnresistencia = new javax.swing.JButton();
        anchototal = new javax.swing.JFormattedTextField();
        color = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        corrugado = new javax.swing.JTextField();
        resistencia = new javax.swing.JTextField();
        fechaproducir = new com.toedter.calendar.JDateChooser();
        jLabel24 = new javax.swing.JLabel();
        txtvelocidad = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        txtarreglo = new javax.swing.JFormattedTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        op_cm1 = new javax.swing.JTextField();
        op_cm2 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        op_cm3 = new javax.swing.JTextField();

        menupop.setName("menupop"); // NOI18N
        menupop.setPreferredSize(new java.awt.Dimension(130, 58));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_programas_corr.class);
        detalleop.setIcon(resourceMap.getIcon("detalleop.icon")); // NOI18N
        detalleop.setText(resourceMap.getString("detalleop.text")); // NOI18N
        detalleop.setName("detalleop"); // NOI18N
        detalleop.setPreferredSize(new java.awt.Dimension(129, 30));
        detalleop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopActionPerformed(evt);
            }
        });
        menupop.add(detalleop);

        detalleart.setIcon(resourceMap.getIcon("detalleart.icon")); // NOI18N
        detalleart.setText(resourceMap.getString("detalleart.text")); // NOI18N
        detalleart.setName("detalleart"); // NOI18N
        detalleart.setPreferredSize(new java.awt.Dimension(129, 30));
        detalleart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartActionPerformed(evt);
            }
        });
        menupop.add(detalleart);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(366, 366, 366)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(358, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        fecha.setEditable(false);
        fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fecha.setText(resourceMap.getString("fecha.text")); // NOI18N
        fecha.setFocusable(false);
        fecha.setName("fecha"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OP", "Clave Articulo", "Resis.", "Pliegos x Ancho", "Cortes", "Laminas", "Largo", "Ancho", "<html><font color=#0066FF>LP x Program", "<html><font color=green>LP x Producir", "Articulo", "Aplicaciones Especiales", "Flauta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, true, true, false, false, false, false, false, false, false, true
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
        Tabladatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabladatosMouseClicked(evt);
            }
        });
        Tabladatos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TabladatosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);
        Tabladatos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (Tabladatos.getColumnModel().getColumnCount() > 0) {
            Tabladatos.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title0")); // NOI18N
            Tabladatos.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title1")); // NOI18N
            Tabladatos.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title2")); // NOI18N
            Tabladatos.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title3")); // NOI18N
            Tabladatos.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title4")); // NOI18N
            Tabladatos.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title5")); // NOI18N
            Tabladatos.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title6")); // NOI18N
            Tabladatos.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title7")); // NOI18N
            Tabladatos.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title8")); // NOI18N
            Tabladatos.getColumnModel().getColumn(9).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title9")); // NOI18N
            Tabladatos.getColumnModel().getColumn(10).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title10")); // NOI18N
            Tabladatos.getColumnModel().getColumn(11).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title11")); // NOI18N
            Tabladatos.getColumnModel().getColumn(12).setHeaderValue(resourceMap.getString("Tabladatos.columnModel.title12")); // NOI18N
        }

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

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        refile.setBackground(resourceMap.getColor("refile.background")); // NOI18N
        refile.setEditable(false);
        refile.setFont(resourceMap.getFont("refile.font")); // NOI18N
        refile.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        refile.setText(resourceMap.getString("refile.text")); // NOI18N
        refile.setFocusable(false);
        refile.setName("refile"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        notas.setColumns(20);
        notas.setFont(resourceMap.getFont("notas.font")); // NOI18N
        notas.setLineWrap(true);
        notas.setRows(3);
        notas.setName("notas"); // NOI18N
        jScrollPane2.setViewportView(notas);

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        anchoutil.setBackground(resourceMap.getColor("anchoutil.background")); // NOI18N
        anchoutil.setEditable(false);
        anchoutil.setFont(resourceMap.getFont("anchoutil.font")); // NOI18N
        anchoutil.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        anchoutil.setText(resourceMap.getString("anchoutil.text")); // NOI18N
        anchoutil.setFocusable(false);
        anchoutil.setName("anchoutil"); // NOI18N

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        metroslineales.setBackground(resourceMap.getColor("metroslineales.background")); // NOI18N
        metroslineales.setEditable(false);
        metroslineales.setFont(resourceMap.getFont("metroslineales.font")); // NOI18N
        metroslineales.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        metroslineales.setText(resourceMap.getString("metroslineales.text")); // NOI18N
        metroslineales.setFocusable(false);
        metroslineales.setName("metroslineales"); // NOI18N

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        m2.setBackground(resourceMap.getColor("m2.background")); // NOI18N
        m2.setEditable(false);
        m2.setFont(resourceMap.getFont("m2.font")); // NOI18N
        m2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m2.setText(resourceMap.getString("m2.text")); // NOI18N
        m2.setFocusable(false);
        m2.setName("m2"); // NOI18N

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        kg.setBackground(resourceMap.getColor("kg.background")); // NOI18N
        kg.setEditable(false);
        kg.setFont(resourceMap.getFont("kg.font")); // NOI18N
        kg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        kg.setText(resourceMap.getString("kg.text")); // NOI18N
        kg.setFocusable(false);
        kg.setName("kg"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(metroslineales, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(anchoutil, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refile, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kg, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(anchoutil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(metroslineales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(m2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(kg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jScrollPane2)
        );

        jPanel3.setName("jPanel3"); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel4.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel4.border.titleFont"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        l1r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        l1r.setName("l1r"); // NOI18N
        l1r.setNextFocusableComponent(anchol1r);
        l1r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                l1rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                l1rFocusLost(evt);
            }
        });
        l1r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l1rKeyPressed(evt);
            }
        });

        r1r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r1r.setName("r1r"); // NOI18N
        r1r.setNextFocusableComponent(anchor1r);
        r1r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                r1rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                r1rFocusLost(evt);
            }
        });
        r1r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                r1rKeyPressed(evt);
            }
        });

        l2r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        l2r.setName("l2r"); // NOI18N
        l2r.setNextFocusableComponent(anchol2r);
        l2r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                l2rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                l2rFocusLost(evt);
            }
        });
        l2r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l2rKeyPressed(evt);
            }
        });

        r2r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        r2r.setName("r2r"); // NOI18N
        r2r.setNextFocusableComponent(anchor2r);
        r2r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                r2rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                r2rFocusLost(evt);
            }
        });
        r2r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                r2rKeyPressed(evt);
            }
        });

        l3r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        l3r.setName("l3r"); // NOI18N
        l3r.setNextFocusableComponent(anchol3r);
        l3r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                l3rFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                l3rFocusLost(evt);
            }
        });
        l3r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l3rKeyPressed(evt);
            }
        });

        f2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "B", " ", "C" }));
        f2.setSelectedIndex(1);
        f2.setName("f2"); // NOI18N
        f2.setNextFocusableComponent(l3r);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        f1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "C", "B" }));
        f1.setName("f1"); // NOI18N
        f1.setNextFocusableComponent(l2r);

        anchol1r.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        anchol1r.setText(resourceMap.getString("anchol1r.text")); // NOI18N
        anchol1r.setToolTipText(resourceMap.getString("anchol1r.toolTipText")); // NOI18N
        anchol1r.setName("anchol1r"); // NOI18N
        anchol1r.setNextFocusableComponent(r1r);
        anchol1r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                anchol1rFocusGained(evt);
            }
        });

        anchor1r.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        anchor1r.setText(resourceMap.getString("anchor1r.text")); // NOI18N
        anchor1r.setToolTipText(resourceMap.getString("anchor1r.toolTipText")); // NOI18N
        anchor1r.setName("anchor1r"); // NOI18N
        anchor1r.setNextFocusableComponent(f1);
        anchor1r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                anchor1rFocusGained(evt);
            }
        });

        anchol2r.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        anchol2r.setText(resourceMap.getString("anchol2r.text")); // NOI18N
        anchol2r.setToolTipText(resourceMap.getString("anchol2r.toolTipText")); // NOI18N
        anchol2r.setName("anchol2r"); // NOI18N
        anchol2r.setNextFocusableComponent(r2r);
        anchol2r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                anchol2rFocusGained(evt);
            }
        });

        anchor2r.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        anchor2r.setText(resourceMap.getString("anchor2r.text")); // NOI18N
        anchor2r.setToolTipText(resourceMap.getString("anchor2r.toolTipText")); // NOI18N
        anchor2r.setName("anchor2r"); // NOI18N
        anchor2r.setNextFocusableComponent(f2);
        anchor2r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                anchor2rFocusGained(evt);
            }
        });

        anchol3r.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        anchol3r.setText(resourceMap.getString("anchol3r.text")); // NOI18N
        anchol3r.setToolTipText(resourceMap.getString("anchol3r.toolTipText")); // NOI18N
        anchol3r.setName("anchol3r"); // NOI18N
        anchol3r.setNextFocusableComponent(btnadd);
        anchol3r.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                anchol3rFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l1r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anchol1r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(f1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(anchor1r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(anchol2r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l2r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(f2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(anchor2r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l3r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anchol3r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l1r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l2r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l3r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(anchol1r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anchor1r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anchol2r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anchor2r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anchol3r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(f1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18)
                    .addComponent(f2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel5.border.title"), javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel5.border.titleFont"), resourceMap.getColor("jPanel5.border.titleColor"))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        pesom2real.setEditable(false);
        pesom2real.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pesom2real.setFocusable(false);
        pesom2real.setFont(resourceMap.getFont("pesom2real.font")); // NOI18N
        pesom2real.setName("pesom2real"); // NOI18N
        pesom2real.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pesom2realFocusGained(evt);
            }
        });

        mullen.setEditable(false);
        mullen.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mullen.setFocusable(false);
        mullen.setFont(resourceMap.getFont("mullen.font")); // NOI18N
        mullen.setName("mullen"); // NOI18N
        mullen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mullenFocusGained(evt);
            }
        });

        ect.setEditable(false);
        ect.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ect.setFocusable(false);
        ect.setFont(resourceMap.getFont("ect.font")); // NOI18N
        ect.setName("ect"); // NOI18N
        ect.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ectFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pesom2real, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ect, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mullen, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(pesom2real, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(mullen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel6.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel6.border.titleFont"))); // NOI18N
        jPanel6.setName("jPanel6"); // NOI18N

        btnresistencia.setIcon(resourceMap.getIcon("btnresistencia.icon")); // NOI18N
        btnresistencia.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnresistencia.setName("btnresistencia"); // NOI18N
        btnresistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresistenciaActionPerformed(evt);
            }
        });

        anchototal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        anchototal.setText(resourceMap.getString("anchototal.text")); // NOI18N
        anchototal.setName("anchototal"); // NOI18N
        anchototal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                anchototalFocusGained(evt);
            }
        });

        color.setEditable(false);
        color.setFocusable(false);
        color.setName("color"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        corrugado.setEditable(false);
        corrugado.setFocusable(false);
        corrugado.setName("corrugado"); // NOI18N

        resistencia.setEditable(false);
        resistencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        resistencia.setFocusable(false);
        resistencia.setName("resistencia"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(corrugado, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(resistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnresistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(anchototal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(anchototal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(resistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnresistencia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(corrugado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fechaproducir.setFocusable(false);
        fechaproducir.setName("fechaproducir"); // NOI18N

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        txtvelocidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        txtvelocidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtvelocidad.setText(resourceMap.getString("txtvelocidad.text")); // NOI18N
        txtvelocidad.setName("txtvelocidad"); // NOI18N
        txtvelocidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtvelocidadFocusGained(evt);
            }
        });

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        txtarreglo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        txtarreglo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtarreglo.setText(resourceMap.getString("txtarreglo.text")); // NOI18N
        txtarreglo.setName("txtarreglo"); // NOI18N
        txtarreglo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtarregloFocusGained(evt);
            }
        });

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText(resourceMap.getString("jLabel26.text")); // NOI18N
        jLabel26.setName("jLabel26"); // NOI18N

        jLabel27.setText(resourceMap.getString("jLabel27.text")); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N

        op_cm1.setText(resourceMap.getString("op_cm1.text")); // NOI18N
        op_cm1.setName("op_cm1"); // NOI18N

        op_cm2.setName("op_cm2"); // NOI18N

        jLabel30.setText(resourceMap.getString("jLabel30.text")); // NOI18N
        jLabel30.setName("jLabel30"); // NOI18N

        jLabel31.setText(resourceMap.getString("jLabel31.text")); // NOI18N
        jLabel31.setName("jLabel31"); // NOI18N

        op_cm3.setName("op_cm3"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnremove, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 942, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fechaproducir, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtarreglo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtvelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(op_cm1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(op_cm2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(op_cm3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 652, Short.MAX_VALUE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtvelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25)
                        .addComponent(txtarreglo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26))
                    .addComponent(fechaproducir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnremove)
                    .addComponent(btnadd)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(op_cm1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(op_cm2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(op_cm3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        if (Tabladatos.getCellEditor() != null) {//finaliza el editor
            Tabladatos.getCellEditor().stopCellEditing();
        }
        sumatoria();//actualiza las sumas
        int filas=Tabladatos.getRowCount();
        int columnas=Tabladatos.getColumnCount();
        int camposvacios=0;
        int resistenciasdirefentes=0;
        int flautasdiferentes=0;
        int anchosdirefentes=0;
        Double totalkgcombinacion=0.0;
        int erroanchoutil=0;

        for(int i=0;i<=(filas-1);i=i+1){
            for(int j=0;j<=(columnas-1);j=j+1){
                if(modelot1.getValueAt(i, j) == null||modelot1.getValueAt(i, j).equals("")){
                    camposvacios=1;
                }
                if(j==2){
                    String partidaresistencia=(String) modelot1.getValueAt(i, j);
                    if(!partidaresistencia.equals(resistencia.getText()))
                        resistenciasdirefentes=1;
                }
                if(j==3){
                    int pliegos=(Integer) modelot1.getValueAt(i, j);
                    if(pliegos<=0.0)
                        camposvacios=1;
                }
                if(j==4){
                    int cortes=(Integer) modelot1.getValueAt(i, j);
                    if(cortes<=0.0)
                        camposvacios=1;
                }
                if(j==5){
                    int laminas=(Integer) modelot1.getValueAt(i, j);
                    if(laminas<=0.0)
                        camposvacios=1;
                }
                if(j==12){
                    String partidaflautas=(String) modelot1.getValueAt(i, j);
                    String flauta_combinacion=(""+f1.getSelectedItem()+""+f2.getSelectedItem()).trim();
                    if(!partidaflautas.equals(flauta_combinacion))
                        flautasdiferentes=1;
                }
                if(j==0){
                    if(camposvacios==0){ //si todos los datos estan correctos sacamos el peso de cada articulo
                        String claveart=(String) modelot1.getValueAt(i, 1);
                        int totallaminas=(Integer) modelot1.getValueAt(i, 5);
                        Double kgcombinacion=0.0;
                            rs0=null;
                            try{
                                String senSQL="SELECT * FROM articulos WHERE (clavearticulo='"+claveart+"')";
                                rs0=conexion.consulta(senSQL,connj);
                                if(rs0.next()){
                                    kgcombinacion=rs0.getDouble("kg")*rs0.getDouble("piezas")*totallaminas;
                                }
                                if(rs0!=null) {  rs0.close();        }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                        totalkgcombinacion=totalkgcombinacion+kgcombinacion;
                    }
                }
            }
        }/**fin de revisar los campos vacios*/

        //valida campos vacios para las diferentes combinaciones
        int camposvaciospapel=0;
        if(corrugado.getText().equals("Single") && (l1r.getText().equals("") || r1r.getText().equals("")) )
            camposvaciospapel=1;
        if(corrugado.getText().equals("Sencillo") && (l1r.getText().equals("") || r1r.getText().equals("") || l2r.getText().equals("")))
            camposvaciospapel=1;
        if(corrugado.getText().equals("Doble") && (l1r.getText().equals("") || r1r.getText().equals("") || l2r.getText().equals("") || r2r.getText().equals("") || l3r.getText().equals("")))
            camposvaciospapel=1;

        //valida que los anchos de papel sean iguales
        if(corrugado.getText().equals("Single")){
            if(!anchototal.getText().equals(anchol1r.getText()))
                anchosdirefentes=1;
            if(!anchototal.getText().equals(anchor1r.getText()))
                anchosdirefentes=1;
        }
        if(corrugado.getText().equals("Sencillo")){
            if(!anchototal.getText().equals(anchol1r.getText()))
                anchosdirefentes=1;
            if(!anchototal.getText().equals(anchor1r.getText()))
                anchosdirefentes=1;
            if(!anchototal.getText().equals(anchol2r.getText()))
                anchosdirefentes=1;
        }
        if(corrugado.getText().equals("Doble")){
            if(!anchototal.getText().equals(anchol1r.getText()))
                anchosdirefentes=1;
            if(!anchototal.getText().equals(anchor1r.getText()))
                anchosdirefentes=1;
            if(!anchototal.getText().equals(anchol2r.getText()))
                anchosdirefentes=1;
            if(!anchototal.getText().equals(anchor2r.getText()))
                anchosdirefentes=1;
            if(!anchototal.getText().equals(anchol3r.getText()))
                anchosdirefentes=1;
        }

        if(Double.parseDouble(refile.getText())<=1) //verifica que el trim sea correcto
            erroanchoutil=1;



        if(fechaproducir.getDate()==null || resistencia.getText().equals("") || anchototal.getText().equals("") || anchototal.getText().equals("0.0") || camposvaciospapel==1 || camposvacios==1 || filas<=0||erroanchoutil==1||flautasdiferentes==1){
            String err="VERIFICA HAY CAMPOS VACIOS";
            if(camposvacios==1)
                err="LA TABLA DE DETALLE TIENE CAMPOS VACIOS";
            if(camposvaciospapel==1)
                err="CAMPOS VACIOS PARA COMBINACION CORRUGADO "+corrugado.getText().toUpperCase();
            if(erroanchoutil==1)
                err="EEROR EN EL REFILE: "+refile.getText();
            if(flautasdiferentes==1)
                err="LAS FLAUTAS DEL ARTICULO SON DIFERENTES A LA COMBINACION";
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            if(resistenciasdirefentes==1){
                int respuesta=JOptionPane.showConfirmDialog(this,"LA RESISTENCIAS SON DIFERENTES\nESTA SEGURO QUE DESEA GUARDAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    resistenciasdirefentes=0;
                }
            }
            if(anchosdirefentes==1){
                int respuesta=JOptionPane.showConfirmDialog(this,"LOS ANCHOS DE LOS PAPELES SON DIFERENTES\nESTA SEGURO QUE DESEA GUARDAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    anchosdirefentes=0;
                }
            }

            if(resistenciasdirefentes==0 && anchosdirefentes==0){
                Double m2finales=Double.parseDouble(metroslineales.getText())*(Double.parseDouble(anchototal.getText())/100);
                m2.setText(estandarentero.format(m2finales));
                kg.setText(estandarentero.format(totalkgcombinacion));
                String senSQL="";
                if(id.getText().equals("")){

                    if(Double.parseDouble(refile.getText())>=trim_std_min && Double.parseDouble(refile.getText())<=trim_std_max) //verifica que el trim sea correcto
                    {
                        senSQL="INSERT INTO programa_corr(estatus, fecha, fechaproduccion, anchototal, anchoutil, claveresistencia, l1r, r1r, l2r, r2r, l3r, f1, f2, al1r, ar1r, al2r, ar2r, al3r, ml, m2, kg, valorresistencia,pesorealcombinacion,notas,arreglo,velocidad) VALUES ('Act', '"+fechainsertarhora.format(new Date())+"', '"+fechainsertarhora.format(fechaproducir.getDate())+"', '"+anchototal.getText()+"', '"+anchoutil.getText()+"', '"+resistencia.getText()+"', '"+l1r.getText()+"', '"+r1r.getText()+"', '"+l2r.getText()+"', '"+r2r.getText()+"', '"+l3r.getText()+"', '"+(String)f1.getSelectedItem()+"', '"+(String)f2.getSelectedItem()+"', '"+anchol1r.getText()+"', '"+anchor1r.getText()+"', '"+anchol2r.getText()+"', '"+anchor2r.getText()+"', '"+anchol3r.getText()+"', '"+metroslineales.getText()+"', '"+m2finales+"', '"+totalkgcombinacion+"', '"+valorrealresistencia+"', '"+pesom2real.getText()+"', '"+notas.getText()+"', '"+txtarreglo.getText()+"', '"+txtvelocidad.getText()+"');";
                        conexion.modificamov_p(senSQL,connj,valor_privilegio);
                        //guarda el detalle del programa
                        /**funcion que regresa el numero con que se guardo la orden de compra*/
                        int claveid_programmax=0;
                        rs0=null;
                        try{
                            senSQL="SELECT max(id_programa_corr) as programa_corrmax FROM programa_corr";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                claveid_programmax=rs0.getInt("programa_corrmax");
                                id.setText(""+claveid_programmax);
                            }
                            if(rs0!=null) {   rs0.close();    }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                        for(int i=0;i<=(filas-1);i=i+1){
                            //programar cada partida en los siguientes procesos
                            senSQL="INSERT INTO programa_corr_detalle(id_programa_corr, op, clavearticulo, pliegosancho, cortes, laminas) VALUES ('"+id.getText()+"', '"+modelot1.getValueAt(i, 0).toString().toUpperCase()+"', '"+modelot1.getValueAt(i, 1)+"', '"+modelot1.getValueAt(i, 3)+"', '"+modelot1.getValueAt(i, 4)+"', '"+modelot1.getValueAt(i, 5)+"');";
                            conexion.modificamov_p(senSQL,connj,valor_privilegio);
                            programa_partida_conversion((""+modelot1.getValueAt(i, 0).toString().toUpperCase()),(""+modelot1.getValueAt(i, 1)),Integer.parseInt(""+modelot1.getValueAt(i, 5)),Integer.parseInt(id.getText()));
                        }
                        if(valor_privilegio.equals("2")){
                            JOptionPane.showMessageDialog(null, "DATOS GUARDADOS CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null,"El USUARIO NO PUEDE LLEVAR ACABO ESTA ACCION","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        }
                        

                    }else{ //guarda la partida en pendiente por autorizar por que esta fuera del rango
                        if (JOptionPane.showConfirmDialog(this,"EL TRIM ESTA FUERA DE RANGO (>"+fijo1decimales.format(trim_std_min)+" y <"+fijo1decimales.format(trim_std_max)+" cm)!!\n DESEAS GUARDARLO COMO PENDIENTE DE AUTORIZAR?????"," DESEAN CONTINUAR !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            noautorizado="Pen";
                            senSQL="INSERT INTO programa_corr(estatus, fecha, fechaproduccion, anchototal, anchoutil, claveresistencia, l1r, r1r, l2r, r2r, l3r, f1, f2, al1r, ar1r, al2r, ar2r, al3r, ml, m2, kg, valorresistencia,pesorealcombinacion,notas,arreglo,velocidad) VALUES ('Pen', '"+fechainsertarhora.format(new Date())+"', '"+fechainsertarhora.format(fechaproducir.getDate())+"', '"+anchototal.getText()+"', '"+anchoutil.getText()+"', '"+resistencia.getText()+"', '"+l1r.getText()+"', '"+r1r.getText()+"', '"+l2r.getText()+"', '"+r2r.getText()+"', '"+l3r.getText()+"', '"+(String)f1.getSelectedItem()+"', '"+(String)f2.getSelectedItem()+"', '"+anchol1r.getText()+"', '"+anchor1r.getText()+"', '"+anchol2r.getText()+"', '"+anchor2r.getText()+"', '"+anchol3r.getText()+"', '"+metroslineales.getText()+"', '"+m2finales+"', '"+totalkgcombinacion+"', '"+valorrealresistencia+"', '"+pesom2real.getText()+"', '"+notas.getText()+"', '"+txtarreglo.getText()+"', '"+txtvelocidad.getText()+"');";
                            conexion.modificamov_p(senSQL,connj,valor_privilegio);
                            //guarda el detalle del programa
                            /**funcion que regresa el numero con que se guardo la orden de compra*/
                            int claveid_programmax=0;
                            rs0=null;
                            try{
                                senSQL="SELECT max(id_programa_corr) as programa_corrmax FROM programa_corr";
                                rs0=conexion.consulta(senSQL,connj);
                                if(rs0.next()){
                                    claveid_programmax=rs0.getInt("programa_corrmax");
                                    id.setText(""+claveid_programmax);
                                }
                                if(rs0!=null) {        rs0.close();    }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                            for(int i=0;i<=(filas-1);i=i+1){
                                senSQL="INSERT INTO programa_corr_detalle(id_programa_corr, op, clavearticulo, pliegosancho, cortes, laminas) VALUES ('"+id.getText()+"', '"+modelot1.getValueAt(i, 0).toString().toUpperCase()+"', '"+modelot1.getValueAt(i, 1)+"', '"+modelot1.getValueAt(i, 3)+"', '"+modelot1.getValueAt(i, 4)+"', '"+modelot1.getValueAt(i, 5)+"');";
                                conexion.modificamov_p(senSQL,connj,valor_privilegio);
                                programa_partida_conversion((""+modelot1.getValueAt(i, 0).toString().toUpperCase()),(""+modelot1.getValueAt(i, 1)),Integer.parseInt(""+modelot1.getValueAt(i, 5)),Integer.parseInt(id.getText()));
                                
                                //actualiza datos de control maestro de cada op
                                if(i==0)
                                    actualiza_op_cm(op_cm1.getText(),Integer.parseInt(""+modelot1.getValueAt(i, 5)));
                                if(i==2)
                                    actualiza_op_cm(op_cm2.getText(),Integer.parseInt(""+modelot1.getValueAt(i, 5)));
                                if(i==3)
                                    actualiza_op_cm(op_cm3.getText(),Integer.parseInt(""+modelot1.getValueAt(i, 5)));
                            }
                            if(valor_privilegio.equals("2")){
                                JOptionPane.showMessageDialog(null, "DATOS GUARDADOS CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                            }else{
                                JOptionPane.showMessageDialog(null,"El USUARIO NO PUEDE LLEVAR ACABO ESTA ACCION","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                            }

                        }

                    }


                }else{
                    if (JOptionPane.showConfirmDialog(this,"SE VAN A ACTUALIZAR LOS DATOS!!"," DESEAN CONTINUAR !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        senSQL="UPDATE programa_corr SET fechaproduccion='"+fechainsertarhora.format(fechaproducir.getDate())+"',anchoutil='"+anchoutil.getText()+"',l1r='"+l1r.getText()+"',r1r='"+r1r.getText()+"',l2r='"+l2r.getText()+"',r2r='"+r2r.getText()+"',l3r='"+l3r.getText()+"',al1r='"+anchol1r.getText()+"',ar1r='"+anchor1r.getText()+"',al2r='"+anchol2r.getText()+"',ar2r='"+anchor2r.getText()+"', al3r='"+anchol3r.getText()+"',valorresistencia='"+valorrealresistencia+"',pesorealcombinacion='"+pesom2real.getText()+"',notas='"+notas.getText()+"',ml='"+metroslineales.getText()+"', m2='"+m2finales+"', kg='"+totalkgcombinacion+"',arreglo='"+txtarreglo.getText()+"',velocidad='"+txtvelocidad.getText()+"' WHERE id_programa_corr='"+id.getText()+"';";
                        conexion.modificaupdate_p(senSQL,connj,valor_privilegio);
                        
                        for(int i=0;i<=(filas-1);i=i+1){
                            senSQL="UPDATE  programa_corr_detalle SET pliegosancho='"+modelot1.getValueAt(i, 3)+"', cortes='"+modelot1.getValueAt(i, 4)+"',laminas='"+modelot1.getValueAt(i, 5)+"' WHERE (id_programa_corr='"+id.getText()+"' AND clavearticulo='"+modelot1.getValueAt(i, 1)+"');";
                            conexion.modificamov_p(senSQL,connj,valor_privilegio);

                            //actualiza tambien las partidas programadas
                            String claveart_f=""+modelot1.getValueAt(i, 1);
                            int cantidad_prog=Integer.parseInt(""+modelot1.getValueAt(i, 5));
                            rs0=null;
                            try{
                                senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,articulos.piezas,maquinas.unidadcapacidad FROM (articulos_maquinas LEFT JOIN maquinas ON articulos_maquinas.clave=maquinas.clave) LEFT JOIN articulos ON articulos_maquinas.clavearticulo=articulos.clavearticulo WHERE (articulos_maquinas.clavearticulo='"+claveart_f+"' AND articulos_maquinas.clave<>'CORR') ORDER BY articulos_maquinas.id_articulo_maquina;";
                                rs0=conexion.consulta(senSQL,connj);
                                while(rs0.next()){
                                        String sonlaminas=rs0.getString("unidadcapacidad").toUpperCase();
                                        int cant_prog_maquina=cantidad_prog;
                                        int piezas_x_art=rs0.getInt("piezas");
                                        if(sonlaminas.equals("PIEZAS")){ //verifica si son piezas las multiplica las laminas por piezas por lamina
                                            //de lo contrario las envia como laminas para usarlas como golpes
                                            cant_prog_maquina=(cantidad_prog*piezas_x_art);
                                        }
                                        senSQL="UPDATE  programa_conversion SET cantidad='"+cant_prog_maquina+"', cantidadpiezas='"+(cantidad_prog*piezas_x_art)+"' WHERE (id_programa_corr='"+id.getText()+"' AND clavemaquina='"+rs0.getString("clave")+"');";
                                        conexion.modificamov_p(senSQL,connj,valor_privilegio);

                                }
                                if(rs0!=null) {    rs0.close();  }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                            //fin de actualizar los articulos

                        }
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
        if(filas<3){
            Object datos[]={"","","",0,0,0,0.0,0.0,""};
            modelot1.addRow(datos);
        }else{
            JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnaddActionPerformed

    private void btnremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoveActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO DE ELIMINAR LA OP  "+(filano+1)+" ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                modelot1.removeRow(filano);
                sumatoria();
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
        if ((evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) && clavemodificau.equals(""))                                                                                    
        {
            int filano=Tabladatos.getSelectedRow();
            int colno=Tabladatos.getSelectedColumn();
            if (Tabladatos.getCellEditor() != null) {
                Tabladatos.getCellEditor().stopCellEditing();
            }
            if(resistencia.getText().equals("") || anchototal.getText().equals("") || anchototal.getText().equals("0.0")){
                JOptionPane.showMessageDialog(this,"TIENES QUE CAPTURAR EL ANCHO A COMBINAR Y SELECCIONAR UNA RESISTENCIA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                if(colno==0 && filano==0){
                    busca_ops_pendientes busca_ops_pendientes = new busca_ops_pendientes(null,true,connj,"AND resistencias.corrugado='"+corrugado.getText()+"' AND resistencias.color='"+color.getText()+"'");
                    busca_ops_pendientes.setLocationRelativeTo(this);
                    busca_ops_pendientes.setVisible(true);
                    /***cambio para mostrar cuadro de dialogo*/
                    if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA COMBINAR ESTA OP!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
                    Tabladatos.setValueAt(busca_ops_pendientes.getText(), filano, 0);
                    busca_ops_pendientes=null;
                    
                }
                
                if(colno==0 && filano==1){
                    busca_ops_combinar busca_ops_combinar = new busca_ops_combinar(null,true,connj,"AND resistencias.corrugado='"+corrugado.getText()+"' AND resistencias.color='"+color.getText()+"'",Double.parseDouble(anchototal.getText()),Double.parseDouble(""+Tabladatos.getValueAt(0, 7)));
                    busca_ops_combinar.setLocationRelativeTo(this);
                    busca_ops_combinar.setVisible(true);
                    
                    Tabladatos.setValueAt(busca_ops_combinar.getText(), filano, 0);
                    /***cambio para mostrar cuadro de dialogo*/
                    if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEAS COMBINAR ESTA OP!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
                    Tabladatos.setValueAt(busca_ops_combinar.getPliegosxancho(), filano, 3);
                    busca_ops_combinar=null;
                    
                }
            }
        }
    }//GEN-LAST:event_TabladatosKeyPressed

    private void btnresistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresistenciaActionPerformed
        // TODO add your handling code here:
        busca_resistencias busca_resistencias = new busca_resistencias(null,true,connj,"");
        busca_resistencias.setLocationRelativeTo(this);
        busca_resistencias.setVisible(true);
        resistencia.setText(busca_resistencias.getText());//obtiene el valor seleccionado
        busca_resistencias=null;
        
        busca_resistencia(resistencia.getText());

}//GEN-LAST:event_btnresistenciaActionPerformed

    private void l1rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l1rFocusGained
        // TODO add your handling code here:
        l1r.selectAll();
}//GEN-LAST:event_l1rFocusGained

    private void l1rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l1rFocusLost
        // TODO add your handling code here:
        datospapel1("");
}//GEN-LAST:event_l1rFocusLost

    private void l1rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l1rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            datospapel1("F7");
        }
}//GEN-LAST:event_l1rKeyPressed

    private void r1rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r1rFocusGained
        // TODO add your handling code here:
        r1r.selectAll();
}//GEN-LAST:event_r1rFocusGained

    private void r1rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r1rFocusLost
        // TODO add your handling code here:
        datospapel2("");
}//GEN-LAST:event_r1rFocusLost

    private void r1rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_r1rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            datospapel2("F7");
        }
}//GEN-LAST:event_r1rKeyPressed

    private void l2rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l2rFocusGained
        // TODO add your handling code here:
        l2r.selectAll();
}//GEN-LAST:event_l2rFocusGained

    private void l2rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l2rFocusLost
        // TODO add your handling code here:
        datospapel3("");
}//GEN-LAST:event_l2rFocusLost

    private void l2rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l2rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            datospapel3("F7");
        }
}//GEN-LAST:event_l2rKeyPressed

    private void r2rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r2rFocusGained
        // TODO add your handling code here:
        r2r.selectAll();
}//GEN-LAST:event_r2rFocusGained

    private void r2rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_r2rFocusLost
        // TODO add your handling code here:
        datospapel4("");
}//GEN-LAST:event_r2rFocusLost

    private void r2rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_r2rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            datospapel4("F7");
        }
}//GEN-LAST:event_r2rKeyPressed

    private void l3rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l3rFocusGained
        // TODO add your handling code here:
        l3r.selectAll();
}//GEN-LAST:event_l3rFocusGained

    private void l3rFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l3rFocusLost
        // TODO add your handling code here:
        datospapel5("");
}//GEN-LAST:event_l3rFocusLost

    private void l3rKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l3rKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            datospapel5("F7");
        }
}//GEN-LAST:event_l3rKeyPressed

    private void anchototalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchototalFocusGained
        // TODO add your handling code here:
        anchototal.selectAll();
    }//GEN-LAST:event_anchototalFocusGained

    private void anchol1rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchol1rFocusGained
        // TODO add your handling code here:
        anchol1r.selectAll();
    }//GEN-LAST:event_anchol1rFocusGained

    private void anchor1rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchor1rFocusGained
        // TODO add your handling code here:
        anchor1r.selectAll();
    }//GEN-LAST:event_anchor1rFocusGained

    private void anchol2rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchol2rFocusGained
        // TODO add your handling code here:
        anchol2r.selectAll();
    }//GEN-LAST:event_anchol2rFocusGained

    private void anchor2rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchor2rFocusGained
        // TODO add your handling code here:
        anchor2r.selectAll();
    }//GEN-LAST:event_anchor2rFocusGained

    private void anchol3rFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchol3rFocusGained
        // TODO add your handling code here:
        anchol3r.selectAll();
    }//GEN-LAST:event_anchol3rFocusGained

    private void pesom2realFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesom2realFocusGained
        // TODO add your handling code here:
        pesom2real.selectAll();
}//GEN-LAST:event_pesom2realFocusGained

    private void ectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ectFocusGained
        // TODO add your handling code here:
        ect.selectAll();
}//GEN-LAST:event_ectFocusGained

    private void mullenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mullenFocusGained
        // TODO add your handling code here:
        mullen.selectAll();
}//GEN-LAST:event_mullenFocusGained

    private void detalleopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopActionPerformed
        // TODO add your handling code here:
        verop();
}//GEN-LAST:event_detalleopActionPerformed

    private void detalleartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartActionPerformed
        // TODO add your handling code here:
        verarticulo();
}//GEN-LAST:event_detalleartActionPerformed

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
        if ( (evt.getButton() == java.awt.event.MouseEvent.BUTTON3)) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tabladatos.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tabladatos.changeSelection(rowNumber, 0, false, false);
            menupop.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_TabladatosMouseClicked

    private void txtarregloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtarregloFocusGained
        // TODO add your handling code here:
        txtarreglo.selectAll();
    }//GEN-LAST:event_txtarregloFocusGained

    private void txtvelocidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtvelocidadFocusGained
        // TODO add your handling code here:
        txtvelocidad.selectAll();
    }//GEN-LAST:event_txtvelocidadFocusGained

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JFormattedTextField anchol1r;
    private javax.swing.JFormattedTextField anchol2r;
    private javax.swing.JFormattedTextField anchol3r;
    private javax.swing.JFormattedTextField anchor1r;
    private javax.swing.JFormattedTextField anchor2r;
    private javax.swing.JFormattedTextField anchototal;
    private javax.swing.JTextField anchoutil;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnremove;
    private javax.swing.JButton btnresistencia;
    private javax.swing.JTextField color;
    private javax.swing.JTextField corrugado;
    private javax.swing.JMenuItem detalleart;
    private javax.swing.JMenuItem detalleop;
    private javax.swing.JFormattedTextField ect;
    private javax.swing.JComboBox f1;
    private javax.swing.JComboBox f2;
    private javax.swing.JTextField fecha;
    private com.toedter.calendar.JDateChooser fechaproducir;
    private javax.swing.JTextField id;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField kg;
    private javax.swing.JTextField l1r;
    private javax.swing.JTextField l2r;
    private javax.swing.JTextField l3r;
    private javax.swing.JTextField m2;
    private javax.swing.JPopupMenu menupop;
    private javax.swing.JTextField metroslineales;
    private javax.swing.JFormattedTextField mullen;
    private javax.swing.JTextArea notas;
    private javax.swing.JTextField op_cm1;
    private javax.swing.JTextField op_cm2;
    private javax.swing.JTextField op_cm3;
    private javax.swing.JFormattedTextField pesom2real;
    private javax.swing.JTextField r1r;
    private javax.swing.JTextField r2r;
    private javax.swing.JTextField refile;
    private javax.swing.JTextField resistencia;
    private javax.swing.JFormattedTextField txtarreglo;
    private javax.swing.JFormattedTextField txtvelocidad;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_ops;
    private JDialog datos_articulos;
    private JDialog datos_programas_conversion;
}
