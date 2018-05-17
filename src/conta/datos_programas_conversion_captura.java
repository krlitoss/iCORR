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

import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IVONNE
 */
public class datos_programas_conversion_captura extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    String clavemodificaf="";
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    int minarreglo=0,minproduccion=0,mltotales=0;
    int minclave1=0,minclave2=0,minclave3=0,minclave4=0,minclave5=0,minclave6=0,minclave7=0,minclave8=0;
    int pasa=0;
    Double anchototal=0.0,anchoutil=0.0,pesorealcombinacion=0.0;
    DefaultTableModel modelotcantidad=null;
    String sonlaminas="";
    int piezasxlamina=1;
    Double kgart=0.0;
    String unidadvel="";
    String maq_inventario="";
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_programas_conversion_captura(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        modelotcantidad=(DefaultTableModel) Tablamerma.getModel();
        Tablamerma.setModel(modelotcantidad);
        ajusteTabladatos();
        fecha.setDate(new Date());
        consultamodifica(clavemodifica);
        clavemodificaf=clavemodifica;
        cantprogram.setVisible(false);
        programa.setVisible(false);
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
        Tablamerma.getColumnModel().getColumn(0).setPreferredWidth(60);
        Tablamerma.getColumnModel().getColumn(1).setPreferredWidth(65);
        Tablamerma.getColumnModel().getColumn(2).setPreferredWidth(65);
        Tablamerma.getColumnModel().getColumn(3).setPreferredWidth(250);

    }
    public void limpiatabla(){
        modelotcantidad.setNumRows(0);
    }
    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            limpiatabla();
            rs0=null;
            btnaceptar.setEnabled(false);
            btnoperador.setEnabled(false);
            btnmaquinas.setEnabled(false);
            rs0=null;
            try{
                String senSQL="SELECT conversion_captura.*,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,operadores.nombre,(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas,maquinas.unidadvelocidad,programa_conversion.cantidadpiezas as programcantidadpiezas FROM (((conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo) LEFT JOIN operadores ON conversion_captura.id_operador=operadores.id_operador) LEFT JOIN maquinas ON conversion_captura.clave=maquinas.clave) LEFT JOIN programa_conversion ON conversion_captura.id_programa_conversion=programa_conversion.id_programa_conversion  WHERE (conversion_captura.id_conversion_captura='"+clavemodifica+"' AND conversion_captura.estatus<>'Can')  ORDER BY conversion_captura.fechareal,conversion_captura.clave";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    clavemaquina.setText(rs0.getString("clave"));
                    fecha.setDate(rs0.getDate("fechareal"));
                    turno.setSelectedItem(rs0.getString("turno"));
                    idoperador.setText(rs0.getString("id_operador"));
                    nombreoperador.setText(rs0.getString("nombre"));
                    op.setText(rs0.getString("op"));
                    clavearticulo.setText(rs0.getString("clavearticulo"));
                    articulo.setText(rs0.getString("articulo"));
                    cantidad.setText(""+rs0.getInt("cantidad"));
                    txtkgtotal.setText(estandarentero.format(rs0.getDouble("prodcantkgpiezas")));
                    malas.setText(estandarentero.format(rs0.getDouble("cantidadmalaspiezas")));
                    arreinicio.setText(horacorta.format(rs0.getTime("inicioarreglo")));
                    arretermino.setText(horacorta.format(rs0.getTime("terminoarreglo")));
                    labelarreglo.setText("Arreglo ("+rs0.getInt("minutosarreglo")+" min)"); //muestra los datos obtenidos
                    prodinicio.setText(horacorta.format(rs0.getTime("inicioprod")));
                    prodtermino.setText(horacorta.format(rs0.getTime("terminoprod")));
                    labelprod.setText("Producción ("+rs0.getInt("minutosprod")+" min)"); //muestra los datos obtenidos
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //vacio de datos
            rs0=null;
            try{
                String senSQL="SELECT paros_captura.*,paros.descripcion FROM paros_captura LEFT JOIN paros ON paros_captura.id_paro=paros.id_paro WHERE (paros_captura.id_conversion_captura='"+clavemodifica+"' and paros_captura.estatus<>'Can')";
                rs0=conexion.consulta(senSQL,connj);
                int c=0;
                while(rs0.next()){
                    c++;

                    if(c==1){
                        clave1.setText(rs0.getString("id_paro"));
                        inicio1.setText(horacorta.format(rs0.getTime("inicioparo")));
                        termino1.setText(horacorta.format(rs0.getTime("terminoparo")));
                        lbmin1.setText("("+rs0.getInt("minutos")+" min)");
                        lbdes1.setText(rs0.getString("descripcion"));
                    }
                    if(c==2){
                        clave2.setText(rs0.getString("id_paro"));
                        inicio2.setText(horacorta.format(rs0.getTime("inicioparo")));
                        termino2.setText(horacorta.format(rs0.getTime("terminoparo")));
                        lbmin2.setText("("+rs0.getInt("minutos")+" min)");
                        lbdes2.setText(rs0.getString("descripcion"));
                    }
                    if(c==3){
                        clave3.setText(rs0.getString("id_paro"));
                        inicio3.setText(horacorta.format(rs0.getTime("inicioparo")));
                        termino3.setText(horacorta.format(rs0.getTime("terminoparo")));
                        lbmin3.setText("("+rs0.getInt("minutos")+" min)");
                        lbdes3.setText(rs0.getString("descripcion"));
                    }
                    if(c==4){
                        clave4.setText(rs0.getString("id_paro"));
                        inicio4.setText(horacorta.format(rs0.getTime("inicioparo")));
                        termino4.setText(horacorta.format(rs0.getTime("terminoparo")));
                        lbmin4.setText("("+rs0.getInt("minutos")+" min)");
                        lbdes4.setText(rs0.getString("descripcion"));
                    }
                    if(c==5){
                        clave5.setText(rs0.getString("id_paro"));
                        inicio5.setText(horacorta.format(rs0.getTime("inicioparo")));
                        termino5.setText(horacorta.format(rs0.getTime("terminoparo")));
                        lbmin5.setText("("+rs0.getInt("minutos")+" min)");
                        lbdes5.setText(rs0.getString("descripcion"));
                    }
                    if(c==6){
                        clave6.setText(rs0.getString("id_paro"));
                        inicio6.setText(horacorta.format(rs0.getTime("inicioparo")));
                        termino6.setText(horacorta.format(rs0.getTime("terminoparo")));
                        lbmin6.setText("("+rs0.getInt("minutos")+" min)");
                        lbdes6.setText(rs0.getString("descripcion"));
                    }
                    if(c==7){
                        clave7.setText(rs0.getString("id_paro"));
                        inicio7.setText(horacorta.format(rs0.getTime("inicioparo")));
                        termino7.setText(horacorta.format(rs0.getTime("terminoparo")));
                        lbmin7.setText("("+rs0.getInt("minutos")+" min)");
                        lbdes7.setText(rs0.getString("descripcion"));
                    }
                    if(c==8){
                        clave8.setText(rs0.getString("id_paro"));
                        inicio8.setText(horacorta.format(rs0.getTime("inicioparo")));
                        termino8.setText(horacorta.format(rs0.getTime("terminoparo")));
                        lbmin8.setText("("+rs0.getInt("minutos")+" min)");
                        lbdes8.setText(rs0.getString("descripcion"));
                    }
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //agrega los datos de la merma
            rs0=null;
            try{
                String senSQL="SELECT merma_captura.*,defectos_calidad.descripcion FROM merma_captura LEFT JOIN defectos_calidad ON merma_captura.id_defecto_calidad=defectos_calidad.id_defecto_calidad WHERE (merma_captura.id_conversion_captura='"+clavemodifica+"' and merma_captura.estatus<>'Can')";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    int cantidadf=rs0.getInt("cantidadpiezas");
                    Boolean eslamina=rs0.getBoolean("lamina");
                    if(eslamina)
                        cantidadf=rs0.getInt("cantidadlamina");

                    Object datos[]={cantidadf,eslamina,rs0.getString("id_defecto_calidad"),rs0.getString("descripcion")};
                    modelotcantidad.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void minutosArreglo(){
        String turnot=(String)turno.getSelectedItem();
        if(!arreinicio.getText().equals("") && !arretermino.getText().equals("")){
                Date horainiprep=null;
                Date horaterprep=null;
                try {
                    horainiprep = horacorta.parse(arreinicio.getText());
                    horaterprep = horacorta.parse(arretermino.getText());
                    if((horaterprep.before(horainiprep))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE PREPARACION ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           arretermino.setText("");
                           prodinicio.setText("");
                           arretermino.requestFocus();
                           minarreglo=0;
                           labelarreglo.setText("Arreglo");
                    }else{
                        prodinicio.setText(arretermino.getText()); //pasa la hora final arreglo como produccion inicio
                        minarreglo=(int) ( ((horaterprep.getTime()-horainiprep.getTime())/1000)/60 );
                        if (minarreglo<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minarreglo=minarreglo+1440;
                        }
                        labelarreglo.setText("Arreglo ("+minarreglo+" min)"); //muestra los datos obtenidos
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void minutosProduccion(){
        String turnot=(String)turno.getSelectedItem();
        if(!prodinicio.getText().equals("") && !prodtermino.getText().equals("")){
                Date horainiprod=null;
                Date horaterprod=null;
                try {
                    horainiprod = horacorta.parse(prodinicio.getText());
                    horaterprod = horacorta.parse(prodtermino.getText());
                    if((horaterprod.before(horainiprod))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE PRODUCCION ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           prodtermino.setText("");
                           prodtermino.requestFocus();
                           minproduccion=0;
                           labelprod.setText("Producción"); //muestra los datos obtenidos
                    }else{
                        minproduccion=(int) ( ((horaterprod.getTime()-horainiprod.getTime())/1000)/60 );
                        if (minproduccion<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minproduccion=minproduccion+1440;
                        }
                        labelprod.setText("Producción ("+minproduccion+" min)"); //muestra los datos obtenidos

                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void minutosClave1(){
        String turnot=(String)turno.getSelectedItem();
        if(!inicio1.getText().equals("") && !termino1.getText().equals("")){
                Date horaini1=null;
                Date horater1=null;
                try {
                    horaini1 = horacorta.parse(inicio1.getText());
                    horater1 = horacorta.parse(termino1.getText());
                    if(!turnot.equals("3") && (horater1.before(horaini1) || horater1.equals(horaini1))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE TIEMPO MUERTO ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           termino1.setText("");
                           termino1.requestFocus();
                           minclave1=0;
                           lbmin1.setText("-");
                    }else{
                        minclave1=(int) ( ((horater1.getTime()-horaini1.getTime())/1000)/60 );
                        if (minclave1<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minclave1=minclave1+1440;
                        }
                        lbmin1.setText("("+minclave1+" min)"); //muestra los datos obtenidos
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void minutosClave2(){
        String turnot=(String)turno.getSelectedItem();
        if(!inicio2.getText().equals("") && !termino2.getText().equals("")){
                Date horaini2=null;
                Date horater2=null;
                try {
                    horaini2 = horacorta.parse(inicio2.getText());
                    horater2 = horacorta.parse(termino2.getText());
                    if(!turnot.equals("3") && (horater2.before(horaini2) || horater2.equals(horaini2))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE TIEMPO MUERTO ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           termino2.setText("");
                           termino2.requestFocus();
                           minclave2=0;
                           lbmin2.setText("-");
                    }else{
                        minclave2=(int) ( ((horater2.getTime()-horaini2.getTime())/1000)/60 );
                        if (minclave2<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minclave2=minclave2+1440;
                        }
                        lbmin2.setText("("+minclave2+" min)"); //muestra los datos obtenidos
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void minutosClave3(){
        String turnot=(String)turno.getSelectedItem();
        if(!inicio3.getText().equals("") && !termino3.getText().equals("")){
                Date horaini3=null;
                Date horater3=null;
                try {
                    horaini3 = horacorta.parse(inicio3.getText());
                    horater3 = horacorta.parse(termino3.getText());
                    if(!turnot.equals("3") && (horater3.before(horaini3) || horater3.equals(horaini3))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE TIEMPO MUERTO ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           termino3.setText("");
                           termino3.requestFocus();
                           minclave3=0;
                           lbmin3.setText("-");
                    }else{
                        minclave3=(int) ( ((horater3.getTime()-horaini3.getTime())/1000)/60 );
                        if (minclave3<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minclave3=minclave3+1440;
                        }
                        lbmin3.setText("("+minclave3+" min)"); //muestra los datos obtenidos
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void minutosClave4(){
        String turnot=(String)turno.getSelectedItem();
        if(!inicio4.getText().equals("") && !termino4.getText().equals("")){
                Date horaini4=null;
                Date horater4=null;
                try {
                    horaini4 = horacorta.parse(inicio4.getText());
                    horater4 = horacorta.parse(termino4.getText());
                    if(!turnot.equals("3") && (horater4.before(horaini4) || horater4.equals(horaini4))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE TIEMPO MUERTO ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           termino4.setText("");
                           termino4.requestFocus();
                           minclave4=0;
                           lbmin4.setText("-");
                    }else{
                        minclave4=(int) ( ((horater4.getTime()-horaini4.getTime())/1000)/60 );
                        if (minclave4<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minclave4=minclave4+1440;
                        }
                        lbmin4.setText("("+minclave4+" min)"); //muestra los datos obtenidos
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void minutosClave5(){
        String turnot=(String)turno.getSelectedItem();
        if(!inicio5.getText().equals("") && !termino5.getText().equals("")){
                Date horaini5=null;
                Date horater5=null;
                try {
                    horaini5 = horacorta.parse(inicio5.getText());
                    horater5 = horacorta.parse(termino5.getText());
                    if(!turnot.equals("3") && (horater5.before(horaini5) || horater5.equals(horaini5))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE TIEMPO MUERTO ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           termino5.setText("");
                           termino5.requestFocus();
                           minclave5=0;
                           lbmin5.setText("-");
                    }else{
                        minclave5=(int) ( ((horater5.getTime()-horaini5.getTime())/1000)/60 );
                        if (minclave5<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minclave5=minclave5+1440;
                        }
                        lbmin5.setText("("+minclave5+" min)"); //muestra los datos obtenidos
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void minutosClave6(){
        String turnot=(String)turno.getSelectedItem();
        if(!inicio6.getText().equals("") && !termino6.getText().equals("")){
                Date horaini6=null;
                Date horater6=null;
                try {
                    horaini6 = horacorta.parse(inicio6.getText());
                    horater6 = horacorta.parse(termino6.getText());
                    if(!turnot.equals("3") && (horater6.before(horaini6) || horater6.equals(horaini6))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE TIEMPO MUERTO ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           termino6.setText("");
                           termino6.requestFocus();
                           minclave6=0;
                           lbmin6.setText("-");
                    }else{
                        minclave6=(int) ( ((horater6.getTime()-horaini6.getTime())/1000)/60 );
                        if (minclave6<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minclave6=minclave6+1440;
                        }
                        lbmin6.setText("("+minclave6+" min)"); //muestra los datos obtenidos
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void minutosClave7(){
        String turnot=(String)turno.getSelectedItem();
        if(!inicio7.getText().equals("") && !termino7.getText().equals("")){
                Date horaini7=null;
                Date horater7=null;
                try {
                    horaini7 = horacorta.parse(inicio7.getText());
                    horater7 = horacorta.parse(termino7.getText());
                    if(!turnot.equals("3") && (horater7.before(horaini7) || horater7.equals(horaini7))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE TIEMPO MUERTO ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           termino7.setText("");
                           termino7.requestFocus();
                           minclave7=0;
                           lbmin7.setText("-");
                    }else{
                        minclave7=(int) ( ((horater7.getTime()-horaini7.getTime())/1000)/60 );
                        if (minclave7<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minclave7=minclave7+1440;
                        }
                        lbmin7.setText("("+minclave7+" min)"); //muestra los datos obtenidos
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void minutosClave8(){
        String turnot=(String)turno.getSelectedItem();
        if(!inicio8.getText().equals("") && !termino8.getText().equals("")){
                Date horaini8=null;
                Date horater8=null;
                try {
                    horaini8 = horacorta.parse(inicio8.getText());
                    horater8 = horacorta.parse(termino8.getText());
                    if(!turnot.equals("3") && (horater8.before(horaini8) || horater8.equals(horaini8))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL DE TIEMPO MUERTO ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           termino8.setText("");
                           termino8.requestFocus();
                           minclave8=0;
                           lbmin8.setText("-");
                    }else{
                        minclave8=(int) ( ((horater8.getTime()-horaini8.getTime())/1000)/60 );
                        if (minclave8<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minclave8=minclave8+1440;
                        }
                        lbmin8.setText("("+minclave8+" min)"); //muestra los datos obtenidos
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void buscaParo1(){
            if(!clave1.getText().equals("")){
            rs0=null;
            try{
                String senSQL="SELECT * FROM paros WHERE id_paro='"+clave1.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    lbdes1.setText(rs0.getString("descripcion"));
                }else{
                    JOptionPane.showMessageDialog(this,"CLAVE DE PARO NO ENCONTRADA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    lbdes1.setText("");
                    clave1.setText("");
                    clave1.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void buscaParo2(){
            if(!clave2.getText().equals("")){
            rs0=null;
            try{
                String senSQL="SELECT * FROM paros WHERE id_paro='"+clave2.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    lbdes2.setText(rs0.getString("descripcion"));
                }else{
                    JOptionPane.showMessageDialog(this,"CLAVE DE PARO NO ENCONTRADA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    lbdes2.setText("");
                    clave2.setText("");
                    clave2.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void buscaParo3(){
            if(!clave3.getText().equals("")){
            rs0=null;
            try{
                String senSQL="SELECT * FROM paros WHERE id_paro='"+clave3.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    lbdes3.setText(rs0.getString("descripcion"));
                }else{
                    JOptionPane.showMessageDialog(this,"CLAVE DE PARO NO ENCONTRADA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    lbdes3.setText("");
                    clave3.setText("");
                    clave3.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void buscaParo4(){
            if(!clave4.getText().equals("")){
            rs0=null;
            try{
                String senSQL="SELECT * FROM paros WHERE id_paro='"+clave4.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    lbdes4.setText(rs0.getString("descripcion"));
                }else{
                    JOptionPane.showMessageDialog(this,"CLAVE DE PARO NO ENCONTRADA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    lbdes4.setText("");
                    clave4.setText("");
                    clave4.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void buscaParo5(){
            if(!clave5.getText().equals("")){
            rs0=null;
            try{
                String senSQL="SELECT * FROM paros WHERE id_paro='"+clave5.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    lbdes5.setText(rs0.getString("descripcion"));
                }else{
                    JOptionPane.showMessageDialog(this,"CLAVE DE PARO NO ENCONTRADA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    lbdes5.setText("");
                    clave5.setText("");
                    clave5.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void buscaParo6(){
            if(!clave6.getText().equals("")){
            rs0=null;
            try{
                String senSQL="SELECT * FROM paros WHERE id_paro='"+clave6.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    lbdes6.setText(rs0.getString("descripcion"));
                }else{
                    JOptionPane.showMessageDialog(this,"CLAVE DE PARO NO ENCONTRADA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    lbdes6.setText("");
                    clave6.setText("");
                    clave6.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void buscaParo7(){
            if(!clave7.getText().equals("")){
            rs0=null;
            try{
                String senSQL="SELECT * FROM paros WHERE id_paro='"+clave7.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    lbdes7.setText(rs0.getString("descripcion"));
                }else{
                    JOptionPane.showMessageDialog(this,"CLAVE DE PARO NO ENCONTRADA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    lbdes7.setText("");
                    clave7.setText("");
                    clave7.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void buscaParo8(){
            if(!clave8.getText().equals("")){
            rs0=null;
            try{
                String senSQL="SELECT * FROM paros WHERE id_paro='"+clave8.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    lbdes8.setText(rs0.getString("descripcion"));
                }else{
                    JOptionPane.showMessageDialog(this,"CLAVE DE PARO NO ENCONTRADA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    lbdes8.setText("");
                    clave8.setText("");
                    clave8.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void minutosResta(String minini,String minter,int minutos){
                Date horainiprep=null;
                Date horaterprep=null;
                Date horaterprod=null;
                Date horainiparo=null;
                Date horaterparo=null;
                try {
                    horainiprep = horacorta.parse(arreinicio.getText());
                    horaterprep = horacorta.parse(arretermino.getText());
                    horaterprod = horacorta.parse(prodtermino.getText());

                    horainiparo = horacorta.parse(minini);
                    horaterparo = horacorta.parse(minter);

                    if( (horainiparo.after(horainiprep) || horainiparo.equals(horainiprep)) && (horaterparo.before(horaterprep) || horaterparo.equals(horaterprep)) ){
                        minarreglo=minarreglo-minutos;//resta los minutos al arreglo si esta dentro del rango
                    }
                    if( (horainiparo.after(horaterprep) || horainiparo.equals(horaterprep)) && (horaterparo.before(horaterprod) || horaterparo.equals(horaterprod)) ){
                        minproduccion=minproduccion-minutos; //resta los minutos si esta en la pruduccion
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
    }
    public void minutosRango(String horap){
        if(!arreinicio.getText().equals("") && !arretermino.getText().equals("") && !prodinicio.getText().equals("") && !prodtermino.getText().equals(""))
        {
                Date horainiprep=null;
                Date horaterprod=null;
                Date horaparo=null;
                try {
                    horainiprep = horacorta.parse(arreinicio.getText());
                    horaterprod = horacorta.parse(prodtermino.getText());

                    horaparo = horacorta.parse(horap);

                    if( (horaparo.after(horainiprep) || horaparo.equals(horainiprep)) && (horaparo.before(horaterprod) || horaparo.equals(horaterprod)) ){
                        pasa=0;
                    }else{
                        JOptionPane.showMessageDialog(this,"LA HORA DEBE ESTAN DENTRO DEL RANGO DE PREPARACION O PRODUCCION","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        pasa=1;
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }
        }else{
                JOptionPane.showMessageDialog(this,"DEBES CAPTURAR PRIMERO LA HORA DE PREPARACION Y PRODUCCION","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                pasa=1;
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

        jLabel1 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        turno = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        idoperador = new javax.swing.JTextField();
        btnoperador = new javax.swing.JButton();
        nombreoperador = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        labelarreglo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        arreinicio = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        arretermino = new javax.swing.JFormattedTextField();
        labelprod = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        prodinicio = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        prodtermino = new javax.swing.JFormattedTextField();
        panelcantidad = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cantidad = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        txtkgtotal = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        malas = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablamerma = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        inicio1 = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        termino1 = new javax.swing.JFormattedTextField();
        lbdes1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        inicio2 = new javax.swing.JFormattedTextField();
        termino2 = new javax.swing.JFormattedTextField();
        lbdes2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        inicio3 = new javax.swing.JFormattedTextField();
        termino3 = new javax.swing.JFormattedTextField();
        lbdes3 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        inicio4 = new javax.swing.JFormattedTextField();
        termino4 = new javax.swing.JFormattedTextField();
        lbdes4 = new javax.swing.JLabel();
        clave1 = new javax.swing.JTextField();
        clave3 = new javax.swing.JTextField();
        clave2 = new javax.swing.JTextField();
        clave4 = new javax.swing.JTextField();
        lbmin1 = new javax.swing.JLabel();
        lbmin2 = new javax.swing.JLabel();
        lbmin3 = new javax.swing.JLabel();
        lbmin4 = new javax.swing.JLabel();
        clave5 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        clave6 = new javax.swing.JTextField();
        clave7 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        clave8 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        inicio5 = new javax.swing.JFormattedTextField();
        inicio6 = new javax.swing.JFormattedTextField();
        inicio7 = new javax.swing.JFormattedTextField();
        inicio8 = new javax.swing.JFormattedTextField();
        jLabel28 = new javax.swing.JLabel();
        termino5 = new javax.swing.JFormattedTextField();
        termino6 = new javax.swing.JFormattedTextField();
        termino7 = new javax.swing.JFormattedTextField();
        termino8 = new javax.swing.JFormattedTextField();
        lbmin5 = new javax.swing.JLabel();
        lbmin6 = new javax.swing.JLabel();
        lbmin7 = new javax.swing.JLabel();
        lbmin8 = new javax.swing.JLabel();
        lbdes5 = new javax.swing.JLabel();
        lbdes6 = new javax.swing.JLabel();
        lbdes7 = new javax.swing.JLabel();
        lbdes8 = new javax.swing.JLabel();
        op = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        clavemaquina = new javax.swing.JTextField();
        btnmaquinas = new javax.swing.JButton();
        clavearticulo = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        articulo = new javax.swing.JTextField();
        programa = new javax.swing.JTextField();
        cantprogram = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_programas_conversion_captura.class);
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

        fecha.setDateFormatString(resourceMap.getString("fecha.dateFormatString")); // NOI18N
        fecha.setName("fecha"); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        turno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3" }));
        turno.setName("turno"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        idoperador.setEditable(false);
        idoperador.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idoperador.setText(resourceMap.getString("idoperador.text")); // NOI18N
        idoperador.setFocusable(false);
        idoperador.setName("idoperador"); // NOI18N

        btnoperador.setIcon(resourceMap.getIcon("btnoperador.icon")); // NOI18N
        btnoperador.setText(resourceMap.getString("btnoperador.text")); // NOI18N
        btnoperador.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnoperador.setName("btnoperador"); // NOI18N
        btnoperador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnoperadorActionPerformed(evt);
            }
        });

        nombreoperador.setEditable(false);
        nombreoperador.setText(resourceMap.getString("nombreoperador.text")); // NOI18N
        nombreoperador.setFocusable(false);
        nombreoperador.setName("nombreoperador"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), 0, 0, resourceMap.getFont("jPanel3.border.titleFont"), resourceMap.getColor("jPanel3.border.titleColor"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        labelarreglo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelarreglo.setText(resourceMap.getString("labelarreglo.text")); // NOI18N
        labelarreglo.setName("labelarreglo"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        arreinicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        arreinicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        arreinicio.setToolTipText(resourceMap.getString("arreinicio.toolTipText")); // NOI18N
        arreinicio.setName("arreinicio"); // NOI18N
        arreinicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                arreinicioMouseExited(evt);
            }
        });
        arreinicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                arreinicioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                arreinicioFocusLost(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        arretermino.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        arretermino.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        arretermino.setToolTipText(resourceMap.getString("arretermino.toolTipText")); // NOI18N
        arretermino.setName("arretermino"); // NOI18N
        arretermino.setNextFocusableComponent(prodtermino);
        arretermino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                arreterminoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                arreterminoFocusLost(evt);
            }
        });

        labelprod.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelprod.setText(resourceMap.getString("labelprod.text")); // NOI18N
        labelprod.setName("labelprod"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        prodinicio.setEditable(false);
        prodinicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        prodinicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        prodinicio.setToolTipText(resourceMap.getString("prodinicio.toolTipText")); // NOI18N
        prodinicio.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        prodinicio.setFocusable(false);
        prodinicio.setName("prodinicio"); // NOI18N
        prodinicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                prodinicioFocusGained(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        prodtermino.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        prodtermino.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        prodtermino.setToolTipText(resourceMap.getString("prodtermino.toolTipText")); // NOI18N
        prodtermino.setName("prodtermino"); // NOI18N
        prodtermino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                prodterminoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                prodterminoFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelarreglo, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(arreinicio, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(arretermino, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(prodinicio, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(prodtermino, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelprod, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(labelarreglo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(arreinicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(arretermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(labelprod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(prodinicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(prodtermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        panelcantidad.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("panelcantidad.border.title"), 0, 0, resourceMap.getFont("panelcantidad.border.titleFont"), resourceMap.getColor("panelcantidad.border.titleColor"))); // NOI18N
        panelcantidad.setName("panelcantidad"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        cantidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        cantidad.setName("cantidad"); // NOI18N

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        txtkgtotal.setEditable(false);
        txtkgtotal.setText(resourceMap.getString("txtkgtotal.text")); // NOI18N
        txtkgtotal.setFocusable(false);
        txtkgtotal.setName("txtkgtotal"); // NOI18N

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        malas.setEditable(false);
        malas.setText(resourceMap.getString("malas.text")); // NOI18N
        malas.setFocusable(false);
        malas.setName("malas"); // NOI18N

        javax.swing.GroupLayout panelcantidadLayout = new javax.swing.GroupLayout(panelcantidad);
        panelcantidad.setLayout(panelcantidadLayout);
        panelcantidadLayout.setHorizontalGroup(
            panelcantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcantidadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelcantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelcantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(malas, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(txtkgtotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(cantidad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelcantidadLayout.setVerticalGroup(
            panelcantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcantidadLayout.createSequentialGroup()
                .addGroup(panelcantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelcantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtkgtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(panelcantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(malas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel6.border.title"), 0, 0, resourceMap.getFont("jPanel6.border.titleFont"), resourceMap.getColor("jPanel6.border.titleColor"))); // NOI18N
        jPanel6.setName("jPanel6"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tablamerma.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cantidad", "Lamina", "Clave", "Motivo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablamerma.setToolTipText(resourceMap.getString("Tablamerma.toolTipText")); // NOI18N
        Tablamerma.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablamerma.setColumnSelectionAllowed(true);
        Tablamerma.setName("Tablamerma"); // NOI18N
        Tablamerma.setRowHeight(21);
        Tablamerma.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Tablamerma.getTableHeader().setReorderingAllowed(false);
        Tablamerma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablamermaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Tablamerma);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelcantidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel4.border.title"), 0, 0, resourceMap.getFont("jPanel4.border.titleFont"), resourceMap.getColor("jPanel4.border.titleColor"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setIconTextGap(1);
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        inicio1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        inicio1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inicio1.setName("inicio1"); // NOI18N
        inicio1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inicio1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inicio1FocusLost(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        termino1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        termino1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        termino1.setName("termino1"); // NOI18N
        termino1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                termino1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                termino1FocusLost(evt);
            }
        });

        lbdes1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbdes1.setName("lbdes1"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        inicio2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        inicio2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inicio2.setName("inicio2"); // NOI18N
        inicio2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inicio2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inicio2FocusLost(evt);
            }
        });

        termino2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        termino2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        termino2.setName("termino2"); // NOI18N
        termino2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                termino2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                termino2FocusLost(evt);
            }
        });

        lbdes2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbdes2.setName("lbdes2"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        inicio3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        inicio3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inicio3.setName("inicio3"); // NOI18N
        inicio3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inicio3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inicio3FocusLost(evt);
            }
        });

        termino3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        termino3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        termino3.setName("termino3"); // NOI18N
        termino3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                termino3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                termino3FocusLost(evt);
            }
        });

        lbdes3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbdes3.setName("lbdes3"); // NOI18N

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        inicio4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        inicio4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inicio4.setName("inicio4"); // NOI18N
        inicio4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inicio4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inicio4FocusLost(evt);
            }
        });

        termino4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        termino4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        termino4.setName("termino4"); // NOI18N
        termino4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                termino4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                termino4FocusLost(evt);
            }
        });

        lbdes4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbdes4.setName("lbdes4"); // NOI18N

        clave1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave1.setText(resourceMap.getString("clave1.text")); // NOI18N
        clave1.setName("clave1"); // NOI18N
        clave1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clave1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clave1FocusLost(evt);
            }
        });
        clave1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clave1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                clave1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                clave1KeyTyped(evt);
            }
        });

        clave3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave3.setText(resourceMap.getString("clave3.text")); // NOI18N
        clave3.setName("clave3"); // NOI18N
        clave3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clave3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clave3FocusLost(evt);
            }
        });
        clave3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clave3KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                clave3KeyTyped(evt);
            }
        });

        clave2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave2.setText(resourceMap.getString("clave2.text")); // NOI18N
        clave2.setName("clave2"); // NOI18N
        clave2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clave2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clave2FocusLost(evt);
            }
        });
        clave2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clave2KeyPressed(evt);
            }
        });

        clave4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave4.setText(resourceMap.getString("clave4.text")); // NOI18N
        clave4.setName("clave4"); // NOI18N
        clave4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clave4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clave4FocusLost(evt);
            }
        });
        clave4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clave4KeyPressed(evt);
            }
        });

        lbmin1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin1.setText(resourceMap.getString("lbmin1.text")); // NOI18N
        lbmin1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbmin1.setName("lbmin1"); // NOI18N

        lbmin2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin2.setText(resourceMap.getString("lbmin2.text")); // NOI18N
        lbmin2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbmin2.setName("lbmin2"); // NOI18N

        lbmin3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin3.setText(resourceMap.getString("lbmin3.text")); // NOI18N
        lbmin3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbmin3.setName("lbmin3"); // NOI18N

        lbmin4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin4.setText(resourceMap.getString("lbmin4.text")); // NOI18N
        lbmin4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbmin4.setName("lbmin4"); // NOI18N

        clave5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave5.setName("clave5"); // NOI18N
        clave5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clave5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clave5FocusLost(evt);
            }
        });
        clave5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clave5KeyPressed(evt);
            }
        });

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setName("jLabel22"); // NOI18N

        jLabel23.setIconTextGap(1);
        jLabel23.setName("jLabel23"); // NOI18N

        jLabel24.setName("jLabel24"); // NOI18N

        clave6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave6.setName("clave6"); // NOI18N
        clave6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clave6FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clave6FocusLost(evt);
            }
        });
        clave6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clave6KeyPressed(evt);
            }
        });

        clave7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave7.setName("clave7"); // NOI18N
        clave7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clave7FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clave7FocusLost(evt);
            }
        });
        clave7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clave7KeyPressed(evt);
            }
        });

        jLabel25.setName("jLabel25"); // NOI18N

        jLabel26.setName("jLabel26"); // NOI18N

        clave8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave8.setName("clave8"); // NOI18N
        clave8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clave8FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clave8FocusLost(evt);
            }
        });
        clave8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clave8KeyPressed(evt);
            }
        });

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setName("jLabel27"); // NOI18N

        inicio5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        inicio5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inicio5.setName("inicio5"); // NOI18N
        inicio5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inicio5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inicio5FocusLost(evt);
            }
        });

        inicio6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        inicio6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inicio6.setName("inicio6"); // NOI18N
        inicio6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inicio6FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inicio6FocusLost(evt);
            }
        });

        inicio7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        inicio7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inicio7.setName("inicio7"); // NOI18N
        inicio7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inicio7FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inicio7FocusLost(evt);
            }
        });

        inicio8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        inicio8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inicio8.setName("inicio8"); // NOI18N
        inicio8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inicio8FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inicio8FocusLost(evt);
            }
        });

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setName("jLabel28"); // NOI18N

        termino5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        termino5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        termino5.setName("termino5"); // NOI18N
        termino5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                termino5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                termino5FocusLost(evt);
            }
        });

        termino6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        termino6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        termino6.setName("termino6"); // NOI18N
        termino6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                termino6FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                termino6FocusLost(evt);
            }
        });

        termino7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        termino7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        termino7.setName("termino7"); // NOI18N
        termino7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                termino7FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                termino7FocusLost(evt);
            }
        });

        termino8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        termino8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        termino8.setName("termino8"); // NOI18N
        termino8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                termino8FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                termino8FocusLost(evt);
            }
        });

        lbmin5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbmin5.setName("lbmin5"); // NOI18N

        lbmin6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbmin6.setName("lbmin6"); // NOI18N

        lbmin7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbmin7.setName("lbmin7"); // NOI18N

        lbmin8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbmin8.setName("lbmin8"); // NOI18N

        lbdes5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbdes5.setName("lbdes5"); // NOI18N

        lbdes6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbdes6.setName("lbdes6"); // NOI18N

        lbdes7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbdes7.setName("lbdes7"); // NOI18N

        lbdes8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbdes8.setName("lbdes8"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(clave4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clave3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(clave1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(clave2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(inicio1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(termino1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbmin1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inicio3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inicio2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(termino3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbmin3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(termino2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbmin2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbdes3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbdes2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbdes1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(inicio4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(termino4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbmin4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbdes4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(clave6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(clave7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(clave5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(clave8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inicio8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inicio7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inicio6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inicio5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(termino5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbmin5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbdes5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(termino7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbmin7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbdes7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(termino6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbmin6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbdes6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(termino8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbmin8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbdes8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel11))
                                .addGap(1, 1, 1)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(clave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(inicio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(termino1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4)
                                            .addComponent(lbmin1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(clave2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15)
                                            .addComponent(inicio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(clave3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel17)))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lbmin6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(lbdes1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(termino2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbmin2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(lbdes2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(clave6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(inicio6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(termino6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lbdes6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbdes3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(inicio3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(termino3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbmin3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lbmin7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(clave7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(inicio7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(termino7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addGap(13, 13, 13)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel23)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel24)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel25))
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(clave5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(inicio5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(termino5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lbmin5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(lbdes5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbdes7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(clave4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lbdes4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbmin4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inicio4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(termino4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lbmin8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(clave8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inicio8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(termino8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(lbdes8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        op.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        op.setText(resourceMap.getString("op.text")); // NOI18N
        op.setName("op"); // NOI18N
        op.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                opFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                opFocusLost(evt);
            }
        });
        op.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                opKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                opKeyReleased(evt);
            }
        });

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        clavemaquina.setEditable(false);
        clavemaquina.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavemaquina.setText(resourceMap.getString("clavemaquina.text")); // NOI18N
        clavemaquina.setFocusable(false);
        clavemaquina.setName("clavemaquina"); // NOI18N

        btnmaquinas.setIcon(resourceMap.getIcon("btnmaquinas.icon")); // NOI18N
        btnmaquinas.setText(resourceMap.getString("btnmaquinas.text")); // NOI18N
        btnmaquinas.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmaquinas.setName("btnmaquinas"); // NOI18N
        btnmaquinas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmaquinasActionPerformed(evt);
            }
        });

        clavearticulo.setEditable(false);
        clavearticulo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavearticulo.setText(resourceMap.getString("clavearticulo.text")); // NOI18N
        clavearticulo.setFocusable(false);
        clavearticulo.setName("clavearticulo"); // NOI18N
        clavearticulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                clavearticuloFocusLost(evt);
            }
        });

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        articulo.setEditable(false);
        articulo.setText(resourceMap.getString("articulo.text")); // NOI18N
        articulo.setFocusable(false);
        articulo.setName("articulo"); // NOI18N

        programa.setEditable(false);
        programa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        programa.setText(resourceMap.getString("programa.text")); // NOI18N
        programa.setFocusable(false);
        programa.setName("programa"); // NOI18N

        cantprogram.setEditable(false);
        cantprogram.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cantprogram.setText(resourceMap.getString("cantprogram.text")); // NOI18N
        cantprogram.setFocusable(false);
        cantprogram.setName("cantprogram"); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton1.setMaximumSize(new java.awt.Dimension(75, 21));
        jButton1.setMinimumSize(new java.awt.Dimension(75, 21));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(75, 21));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton2.setMaximumSize(new java.awt.Dimension(75, 21));
        jButton2.setMinimumSize(new java.awt.Dimension(75, 21));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(75, 21));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton3.setMaximumSize(new java.awt.Dimension(75, 21));
        jButton3.setMinimumSize(new java.awt.Dimension(75, 21));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(75, 21));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

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
                .addContainerGap(476, Short.MAX_VALUE)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(271, 271, 271))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(clavemaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnmaquinas, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(idoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(nombreoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(clavearticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cantprogram, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(programa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(clavemaquina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnmaquinas, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(programa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cantprogram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombreoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(clavearticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        Double totalkg=0.0;
        Double totalmerma=0.0;
        Double totalmermalaminas=0.0;

        if (Tablamerma.getCellEditor() != null) {//finaliza el editor
            Tablamerma.getCellEditor().stopCellEditing();
        }
        int filas=Tablamerma.getRowCount();
        int columnas=Tablamerma.getColumnCount();

        for(int i=0;i<=(filas-1);i=i+1){ //suma los datos de las mermas
            int cantm=0;
            if(modelotcantidad.getValueAt(i, 0)!= null)
                cantm=(Integer) modelotcantidad.getValueAt(i, 0);
            
            String silamina=""+modelotcantidad.getValueAt(i, 1);
            String clavemerma=""+modelotcantidad.getValueAt(i, 2);
            String desmerma=""+modelotcantidad.getValueAt(i, 3);
            if(cantm>0 && !clavemerma.equals("null") && !clavemerma.equals("")&& !desmerma.equals("null") && !desmerma.equals("")){
                if(silamina.toUpperCase().equals("TRUE")){
                    cantm=cantm*piezasxlamina;
                }
                totalmerma=totalmerma+cantm;
            }
        } /**fin de revisar los campos vacios*/

        //calcula de nuevo los minutos de arreglo y produccion
        minutosArreglo();
        minutosProduccion();
        minutosClave1();
        minutosClave2();
        minutosClave3();
        minutosClave4();
        minutosClave5();
        minutosClave6();
        minutosClave7();
        minutosClave8();


        if(clavemaquina.getText().equals("") ||fecha.getDate()==null || idoperador.getText().equals("")||op.getText().equals("")||programa.getText().equals("")||arreinicio.getText().equals("")||arretermino.getText().equals("")||prodtermino.getText().equals("")){
            String err="VERIFICA HAY CAMPOS VACIOS";
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            //resta los minutos de la produccion del los tiempos muertos
            if(!inicio1.getText().equals("") && !termino1.getText().equals("") && !clave1.getText().equals("")){
                minutosResta(inicio1.getText(),termino1.getText(),minclave1);
            }
            if(!inicio2.getText().equals("") && !termino2.getText().equals("") && !clave2.getText().equals("")){
                minutosResta(inicio2.getText(),termino2.getText(),minclave2);
            }
            if(!inicio3.getText().equals("") && !termino3.getText().equals("") && !clave3.getText().equals("")){
                minutosResta(inicio3.getText(),termino3.getText(),minclave3);
            }
            if(!inicio4.getText().equals("") && !termino4.getText().equals("") && !clave4.getText().equals("")){
                minutosResta(inicio4.getText(),termino4.getText(),minclave4);
            }
            if(!inicio5.getText().equals("") && !termino5.getText().equals("") && !clave5.getText().equals("")){
                minutosResta(inicio5.getText(),termino5.getText(),minclave5);
            }
            if(!inicio6.getText().equals("") && !termino6.getText().equals("") && !clave6.getText().equals("")){
                minutosResta(inicio6.getText(),termino6.getText(),minclave6);
            }
            if(!inicio7.getText().equals("") && !termino7.getText().equals("") && !clave7.getText().equals("")){
                minutosResta(inicio7.getText(),termino7.getText(),minclave7);
            }
            if(!inicio8.getText().equals("") && !termino8.getText().equals("") && !clave8.getText().equals("")){
                minutosResta(inicio8.getText(),termino8.getText(),minclave8);
            }
            //verifica la cantidad
            int cp=Integer.parseInt(cantidad.getText());
            int cpro=Integer.parseInt(cantprogram.getText());
            if(cp>(cpro*1.1)){
                JOptionPane.showMessageDialog(this,"EXCEDE MAS DEL 10% DE LA CANTIDAD PROGRAMADA\n<html><font size=5 color=blue><b>LA OP: "+op.getText()+"</b></font><br><font size=5 color=#DC143C><b>Cantidad Programada: </b>"+cpro+"</b></font><br><font size=5 color=#DC143C><b>Cantidad Producida: </b>"+cantidad.getText(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }
            
            int cantidadpzas=Integer.parseInt(cantidad.getText()); 
            totalmermalaminas=totalmerma;
            if(!sonlaminas.equals("PIEZAS")){  //verifica si sol golpes o piezas
                cantidadpzas=cantidadpzas*piezasxlamina; //convierte las laminasa piezas
                totalmermalaminas=totalmerma/piezasxlamina;
            }
            totalkg=kgart*cantidadpzas;
            txtkgtotal.setText(estandarentero.format(totalkg));
            malas.setText(estandarentero.format(totalmerma));

            int totalcantidadfinal=(int) (cantidadpzas + totalmerma);
            if(!sonlaminas.equals("PIEZAS")){  //verifica si sol golpes o piezas
                totalcantidadfinal=(int) (Integer.parseInt(cantidad.getText()) + totalmermalaminas);
            }

            int totallaminasinventario=0;
            String inventariovalidado="Si";
            
            //pregunta si se va a restar del inventario
            if (!(JOptionPane.showConfirmDialog(this,"DESEA RESTAR DEL INVENTARIO DE!!\n<html><font size=5 color=#DC143C><b>Maquina: </b>"+maq_inventario+"<br><b>Cantidad: </b>"+estandarentero.format(totalcantidadfinal)+"<br></font></html>"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
                    maq_inventario="";
            }else{
                //revisa que exista la cantidad en almacen alterior
                if(maq_inventario.toUpperCase().equals("CORR")){
                    //busca en el inventario
                    rs0=null;
                    try{
                        String senSQL="SELECT entradaslam.op,entradaslam.clavearticulo,(COALESCE(entradaslam.entradaslaminas,0)-COALESCE(salidaslam.salidaslaminas,0)) as inventario FROM (SELECT entradas_inventario_corr.clavearticulo,entradas_inventario_corr.op,sum(entradas_inventario_corr.cantidadlaminas) as entradaslaminas FROM entradas_inventario_corr WHERE (entradas_inventario_corr.estatus<>'Can' AND entradas_inventario_corr.fecha<='"+fechainsertar.format(new Date())+" 23:59:59') GROUP BY entradas_inventario_corr.clavearticulo,entradas_inventario_corr.op) as entradaslam LEFT JOIN(SELECT conversion_captura.clavearticulo,conversion_captura.op,sum(conversion_captura.cantidad+conversion_captura.cantidadmalas) as salidaslaminas FROM conversion_captura WHERE (conversion_captura.estatus<>'Can' AND conversion_captura.maquina_inventario='CORR' AND conversion_captura.fechareal<='"+fechainsertar.format(new Date())+" 23:59:59') GROUP BY conversion_captura.op,conversion_captura.clavearticulo) as salidaslam ON (entradaslam.op=salidaslam.op AND entradaslam.clavearticulo=salidaslam.clavearticulo) WHERE (entradaslam.op='"+op.getText()+"' AND entradaslam.clavearticulo='"+clavearticulo.getText()+"');";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            totallaminasinventario=rs0.getInt("inventario");
                        }
                        if(rs0!=null) { rs0.close(); }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                    if(totalcantidadfinal>totallaminasinventario)
                        inventariovalidado="No";
                    
                }else{
                    //busca inventario para las otras maquinas
                    rs0=null;
                    try{
                        String senSQL="SELECT entradas.op,entradas.clavearticulo,(COALESCE(entradas.entradaslaminas,0)-COALESCE(salidas.salidaslaminas,0)) as inventariolamina,(COALESCE(entradas.entradaspiezas,0)-COALESCE(salidas.salidaspiezas,0)) as inventariopiezas,articulos.piezas,articulos.kg,articulos.m2,articulos.preciomillar,articulos.largo,articulos.ancho,articulos.claveresistencia,articulos.articulo,resistencias.f1,resistencias.f2,resistencias.color,resistencias.referencia,clientes.nombre FROM ((SELECT conversion_captura.clavearticulo,conversion_captura.op,sum(conversion_captura.cantidad+conversion_captura.cantidadmalas) as entradaslaminas,sum(conversion_captura.cantidadpiezas+conversion_captura.cantidadmalaspiezas) as entradaspiezas FROM conversion_captura WHERE (conversion_captura.estatus<>'Can' AND conversion_captura.clave='"+maq_inventario.toUpperCase()+"' AND conversion_captura.fechareal<='"+fechainsertar.format(new Date())+" 23:59:59') GROUP BY conversion_captura.op,conversion_captura.clavearticulo) as entradas LEFT JOIN (SELECT conversion_captura.clavearticulo,conversion_captura.op,sum(conversion_captura.cantidad+conversion_captura.cantidadmalas) as salidaslaminas,sum(conversion_captura.cantidadpiezas+conversion_captura.cantidadmalaspiezas) as salidaspiezas FROM conversion_captura WHERE (conversion_captura.estatus<>'Can' AND conversion_captura.maquina_inventario='"+maq_inventario.toUpperCase()+"' AND conversion_captura.fechareal<='"+fechainsertar.format(new Date())+" 23:59:59') GROUP BY conversion_captura.op,conversion_captura.clavearticulo) as salidas ON (entradas.op=salidas.op AND entradas.clavearticulo=salidas.clavearticulo) ) LEFT JOIN ((articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) LEFT JOIN resistencias ON articulos.claveresistencia=resistencias.clave) ON entradas.clavearticulo=articulos.clavearticulo WHERE (entradas.op='"+op.getText()+"' AND entradas.clavearticulo='"+clavearticulo.getText()+"');";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            totallaminasinventario=rs0.getInt("inventariopiezas");
                            if(!sonlaminas.equals("PIEZAS")){
                                totallaminasinventario=rs0.getInt("inventariolamina");
                            }
                        }
                        if(rs0!=null) { rs0.close(); }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                    if(totalcantidadfinal>totallaminasinventario)
                        inventariovalidado="No";

                }
            }//final de maquina de inventario

            
            if(inventariovalidado.equals("No")){
                JOptionPane.showMessageDialog(this,"NO HAY MATERIAL SUFICIENTE EN:\n<html><font size=5 color=#DC143C><b>Maquina: </b>"+maq_inventario+"<br><b>Inventario: </b>"+estandarentero.format(totallaminasinventario)+"<br><b>Cantidad Producida: </b>"+estandarentero.format(totalcantidadfinal),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);

            }else{
                if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR!!\n<html><font size=5 color=#DC143C><b>Cantidad: </b>"+cantidad.getText()+"<br><b>KG: </b>"+txtkgtotal.getText()+"<br><b>Min. Prod: </b>"+estandarentero.format(minproduccion)+"<br><b>Velocidad "+unidadvel+": </b>"+estandarentero.format(Integer.parseInt(cantidad.getText())/minproduccion)+"<br></font></html>"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                        String senSQL="INSERT INTO conversion_captura(estatus, op, clavearticulo, fechareal, turno, id_operador, clave, inicioarreglo, terminoarreglo, minutosarreglo, inicioprod, terminoprod, minutosprod, cantidad, cantidadmalas, cantidadpiezas, cantidadmalaspiezas, id_programa_conversion,maquina_inventario,sistema) VALUES ('Act', '"+op.getText()+"', '"+clavearticulo.getText()+"', '"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+arreinicio.getText()+"', '"+arretermino.getText()+"', '"+minarreglo+"', '"+prodinicio.getText()+"', '"+prodtermino.getText()+"', '"+minproduccion+"', '"+cantidad.getText()+"', '"+fijo0decimales.format(totalmermalaminas)+"', '"+cantidadpzas+"', '"+totalmerma+"', '"+programa.getText()+"','"+maq_inventario+"','true');";
                        conexion.modifica_p(senSQL,connj,valor_privilegio);

                        /**funcion que regresa el numero con que se guardo de la captura*/
                        int claveidcapturamax=1;
                        rs0=null;
                        try{
                            senSQL="SELECT max(id_conversion_captura) as capturamax FROM conversion_captura";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                claveidcapturamax=rs0.getInt("capturamax");
                            }
                            if(rs0!=null) { rs0.close(); }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                        //captura los registros mermas
                        for(int i=0;i<=(filas-1);i=i+1){ //captura todas las partidas de merma
                            int cantm=0;
                            int cantmlaminas=0;
                            if(modelotcantidad.getValueAt(i, 0)!= null){
                                cantm=(Integer) modelotcantidad.getValueAt(i, 0);
                                cantmlaminas=0;
                            }
                            String silamina=""+modelotcantidad.getValueAt(i, 1);
                            if(silamina.equals("null"))
                                silamina="false";
                            String clavemerma=""+modelotcantidad.getValueAt(i, 2);
                            String desmerma=""+modelotcantidad.getValueAt(i, 3);
                            if(cantm>0 && !clavemerma.equals("null") && !clavemerma.equals("")&& !desmerma.equals("null") && !desmerma.equals("")){
                                if(silamina.toUpperCase().equals("TRUE")){
                                    cantmlaminas=cantm;
                                    cantm=cantm*piezasxlamina;
                                }
                                senSQL="INSERT INTO merma_captura(estatus, id_conversion_captura,fechareal, op, clavearticulo, id_operador, clavemaquina, cantidadpiezas, id_defecto_calidad,cantidadlamina,lamina) VALUES ('Act', '"+claveidcapturamax+"', '"+fechainsertar.format(fecha.getDate())+"', '"+op.getText()+"', '"+clavearticulo.getText()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+cantm+"', '"+clavemerma+"','"+cantmlaminas+"','"+silamina+"');";
                                conexion.modificamov_p(senSQL,connj,valor_privilegio);
                            }
                        }

                        //captura los tiempos muertos
                        if(!inicio1.getText().equals("") && !termino1.getText().equals("") && !clave1.getText().equals("")){
                            String senSQLparo="INSERT INTO paros_captura(estatus, id_conversion_captura, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+op.getText()+"','"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+clave1.getText()+"', '"+inicio1.getText()+"', '"+termino1.getText()+"', '"+minclave1+"');";
                            conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                        }
                        if(!inicio2.getText().equals("") && !termino2.getText().equals("") && !clave2.getText().equals("")){
                            String senSQLparo="INSERT INTO paros_captura(estatus, id_conversion_captura, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+op.getText()+"','"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+clave2.getText()+"', '"+inicio2.getText()+"', '"+termino2.getText()+"', '"+minclave2+"');";
                            conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                        }
                        if(!inicio3.getText().equals("") && !termino3.getText().equals("") && !clave3.getText().equals("")){
                            String senSQLparo="INSERT INTO paros_captura(estatus, id_conversion_captura, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+op.getText()+"','"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+clave3.getText()+"', '"+inicio3.getText()+"', '"+termino3.getText()+"', '"+minclave3+"');";
                            conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                        }
                        if(!inicio4.getText().equals("") && !termino4.getText().equals("") && !clave4.getText().equals("")){
                            String senSQLparo="INSERT INTO paros_captura(estatus, id_conversion_captura, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+op.getText()+"','"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+clave4.getText()+"', '"+inicio4.getText()+"', '"+termino4.getText()+"', '"+minclave4+"');";
                            conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                        }
                        if(!inicio5.getText().equals("") && !termino5.getText().equals("") && !clave5.getText().equals("")){
                            String senSQLparo="INSERT INTO paros_captura(estatus, id_conversion_captura, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+op.getText()+"','"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+clave5.getText()+"', '"+inicio5.getText()+"', '"+termino5.getText()+"', '"+minclave5+"');";
                            conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                        }
                        if(!inicio6.getText().equals("") && !termino6.getText().equals("") && !clave6.getText().equals("")){
                            String senSQLparo="INSERT INTO paros_captura(estatus, id_conversion_captura, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+op.getText()+"','"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+clave6.getText()+"', '"+inicio6.getText()+"', '"+termino6.getText()+"', '"+minclave6+"');";
                            conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                        }
                        if(!inicio7.getText().equals("") && !termino6.getText().equals("") && !clave7.getText().equals("")){
                            String senSQLparo="INSERT INTO paros_captura(estatus, id_conversion_captura, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+op.getText()+"','"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+clave7.getText()+"', '"+inicio7.getText()+"', '"+termino7.getText()+"', '"+minclave7+"');";
                            conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                        }
                        if(!inicio8.getText().equals("") && !termino7.getText().equals("") && !clave8.getText().equals("")){
                            String senSQLparo="INSERT INTO paros_captura(estatus, id_conversion_captura, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+op.getText()+"','"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+clavemaquina.getText()+"', '"+clave8.getText()+"', '"+inicio8.getText()+"', '"+termino8.getText()+"', '"+minclave8+"');";
                            conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                        }


                        rs0=null;
                        try{
                            senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,prod.prodcantidad,prod.prodcantidadpiezas,prod.prodcantkgpiezas,articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad FROM (((programa_conversion LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina.getText()+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.id_programa_conversion='"+programa.getText()+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.clavemaquina='"+clavemaquina.getText()+"') ORDER BY programa_conversion.fechaproduccion;";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                int cantprog=rs0.getInt("programcantidad");
                                int cantprod=rs0.getInt("prodcantidad");
                                if(cantprod>=cantprog){
                                    String senSQLmov = "UPDATE programa_conversion SET estatus='Ter' WHERE id_programa_conversion='" + programa.getText() + "';";
                                    conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                                }else{
                                    String senSQLmov = "UPDATE programa_conversion SET estatus='Act' WHERE id_programa_conversion='" + programa.getText() + "';";
                                    conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                                }
                            }else{
                                String senSQLmov = "UPDATE programa_conversion SET estatus='Act' WHERE id_programa_conversion='" + programa.getText() + "';";
                                conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                            }
                            if(rs0!=null) {
                                rs0.close();
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                        salir();

                }//final si desea guardar
            }//final de inventario
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

    private void btnoperadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnoperadorActionPerformed
        // TODO add your handling code here:
        busca_operador busca_operador = new busca_operador(null,true,connj,"");
        busca_operador.setLocationRelativeTo(this);
        busca_operador.setVisible(true);
        idoperador.setText(busca_operador.getText());//obtiene el valor seleccionado
        busca_operador=null;

        if(idoperador.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM operadores WHERE id_operador='"+idoperador.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    nombreoperador.setText(rs0.getString("nombre"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }//GEN-LAST:event_btnoperadorActionPerformed

    private void arreinicioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_arreinicioFocusGained
        // TODO add your handling code here:
        arreinicio.selectAll();
}//GEN-LAST:event_arreinicioFocusGained

    private void arreterminoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_arreterminoFocusGained
        // TODO add your handling code here:
        arretermino.selectAll();
}//GEN-LAST:event_arreterminoFocusGained

    private void arreinicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_arreinicioFocusLost
        // TODO add your handling code here:
        minutosArreglo();
}//GEN-LAST:event_arreinicioFocusLost

    private void prodinicioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prodinicioFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_prodinicioFocusGained

    private void prodterminoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prodterminoFocusGained
        // TODO add your handling code here:
        prodtermino.selectAll();
    }//GEN-LAST:event_prodterminoFocusGained

    private void arreterminoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_arreterminoFocusLost
        // TODO add your handling code here:
        minutosArreglo();
}//GEN-LAST:event_arreterminoFocusLost

    private void prodterminoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prodterminoFocusLost
        // TODO add your handling code here:
        minutosProduccion();
    }//GEN-LAST:event_prodterminoFocusLost

    private void TablamermaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablamermaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
            int filano=Tablamerma.getSelectedRow();
            int colno=Tablamerma.getSelectedColumn();
            if (Tablamerma.getCellEditor() != null) {//finaliza el editor
                Tablamerma.getCellEditor().stopCellEditing();
            }
            if(colno==2){
                busca_defectos_calidad busca_defectos_calidad = new busca_defectos_calidad(null,true,connj,"");
                busca_defectos_calidad.setLocationRelativeTo(this);
                busca_defectos_calidad.setVisible(true);
                Tablamerma.setValueAt(busca_defectos_calidad.getText(), filano, colno);
                String valorf=(String)Tablamerma.getValueAt(filano, colno);
                busca_defectos_calidad=null;
                if(valorf.equals("")){
                    Tablamerma.setValueAt("", filano, 3);
                }else{
                    rs0=null;
                    try{
                        String senSQL="SELECT * FROM defectos_calidad  WHERE id_defecto_calidad='"+valorf+"'";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            Tablamerma.setValueAt(rs0.getString("descripcion"), filano, 3);
                        }
                        if(rs0!=null) {
                            rs0.close();
                        }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    
                }
            }
        }
}//GEN-LAST:event_TablamermaKeyPressed

    private void inicio1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio1FocusGained
        // TODO add your handling code here:
        inicio1.selectAll();
    }//GEN-LAST:event_inicio1FocusGained

    private void termino1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino1FocusGained
        // TODO add your handling code here:
        termino1.selectAll();
    }//GEN-LAST:event_termino1FocusGained

    private void inicio2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio2FocusGained
        // TODO add your handling code here:
        inicio2.selectAll();
    }//GEN-LAST:event_inicio2FocusGained

    private void termino2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino2FocusGained
        // TODO add your handling code here:
        termino2.selectAll();
    }//GEN-LAST:event_termino2FocusGained

    private void inicio3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio3FocusGained
        // TODO add your handling code here:
        inicio3.selectAll();
    }//GEN-LAST:event_inicio3FocusGained

    private void termino3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino3FocusGained
        // TODO add your handling code here:
        termino3.selectAll();
    }//GEN-LAST:event_termino3FocusGained

    private void inicio4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio4FocusGained
        // TODO add your handling code here:
        inicio4.selectAll();
    }//GEN-LAST:event_inicio4FocusGained

    private void termino4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino4FocusGained
        // TODO add your handling code here:
        termino4.selectAll();
    }//GEN-LAST:event_termino4FocusGained

    private void clave2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave2FocusGained
        // TODO add your handling code here:
        clave2.selectAll();
    }//GEN-LAST:event_clave2FocusGained

    private void clave4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave4FocusGained
        // TODO add your handling code here:
        clave4.selectAll();
    }//GEN-LAST:event_clave4FocusGained

    private void clave3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave3FocusGained
        // TODO add your handling code here:
        clave3.selectAll();
    }//GEN-LAST:event_clave3FocusGained

    private void opFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusLost
        // TODO add your handling code here:
        op.setText(op.getText().toUpperCase());
        if(op.getText().equals("") || clavemaquina.getText().equals("")){
            if(clavemaquina.getText().equals("")){
                JOptionPane.showMessageDialog(this,"PRIMERO DEBES SELECCIONAR UNA MAQUINA O PROCESO","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                op.setText("");
            }

        }else{

            if(op.getText().equals("STOCK")){
                clavearticulo.setEditable(true);
                clavearticulo.setFocusable(true);
                clavearticulo.requestFocus();
            }else{
                busca_programas_conversion busca_programas_conversion = new busca_programas_conversion(null,true,connj,op.getText(),clavemaquina.getText(),clavearticulo.getText());
                busca_programas_conversion.setLocationRelativeTo(this);
                busca_programas_conversion.setVisible(true);
                op.setText(busca_programas_conversion.getText());
                programa.setText(busca_programas_conversion.getPrograma());
                busca_programas_conversion=null;
                if(op.getText().equals("")){
                    programa.setText("");
                    clavearticulo.setText("");
                    articulo.setText("");
                    cantprogram.setText("0");
                }else{
                    rs0=null;
                    try{
                        String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,prod.prodcantidad,prod.prodcantidadpiezas,prod.prodcantkgpiezas,articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad FROM (((programa_conversion LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina.getText()+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.id_programa_conversion='"+programa.getText()+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.clavemaquina='"+clavemaquina.getText()+"') ORDER BY programa_conversion.fechaproduccion;";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            String opprogram=rs0.getString("op");
                            int cantprog=rs0.getInt("programcantidad");
                            int cantprod=rs0.getInt("prodcantidad");
                            clavearticulo.setText(rs0.getString("clavearticulo"));
                            articulo.setText(rs0.getString("articulo"));
                            cantprogram.setText(""+cantprog);
                            piezasxlamina=rs0.getInt("piezas");
                            kgart=rs0.getDouble("kg");
                        }
                        if(rs0!=null) {rs0.close(); }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                    //busca el maquinas articulos la maquina de donde viene
                    rs0=null;
                    try{
                        String senSQL="SELECT * FROM articulos_maquinas WHERE clavearticulo='"+clavearticulo.getText()+"' ORDER BY id_articulo_maquina";
                        rs0=conexion.consulta(senSQL,connj);
                        while(rs0.next()){
                            String tmaq=rs0.getString("clave");
                            if(tmaq.toUpperCase().equals(clavemaquina.getText().toUpperCase()))
                                break;
                            maq_inventario=tmaq;
                        }
                        if(rs0!=null) {rs0.close(); }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                }
            }
        }
}//GEN-LAST:event_opFocusLost

    private void opFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusGained
        // TODO add your handling code here:
        op.selectAll();
}//GEN-LAST:event_opFocusGained

    private void clave2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave2FocusLost
        // TODO add your handling code here:
        buscaParo2();
    }//GEN-LAST:event_clave2FocusLost

    private void clave3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave3FocusLost
        // TODO add your handling code here:
        buscaParo3();
    }//GEN-LAST:event_clave3FocusLost

    private void clave4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave4FocusLost
        // TODO add your handling code here:
        buscaParo4();
    }//GEN-LAST:event_clave4FocusLost

    private void clave2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            busca_paros busca_paros = new busca_paros(null,true,connj,"");
            busca_paros.setLocationRelativeTo(this);
            busca_paros.setVisible(true);
            clave2.setText(busca_paros.getText());//obtiene el valor seleccionado
            busca_paros=null;
            buscaParo2();
        }
    }//GEN-LAST:event_clave2KeyPressed

    private void clave3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave3KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            busca_paros busca_paros = new busca_paros(null,true,connj,"");
            busca_paros.setLocationRelativeTo(this);
            busca_paros.setVisible(true);
            clave3.setText(busca_paros.getText());//obtiene el valor seleccionado
            busca_paros=null;
            buscaParo3();
        }
    }//GEN-LAST:event_clave3KeyPressed

    private void clave4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave4KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            busca_paros busca_paros = new busca_paros(null,true,connj,"");
            busca_paros.setLocationRelativeTo(this);
            busca_paros.setVisible(true);
            clave4.setText(busca_paros.getText());//obtiene el valor seleccionado
            busca_paros=null;
            buscaParo4();
        }
    }//GEN-LAST:event_clave4KeyPressed

    private void arreinicioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arreinicioMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_arreinicioMouseExited

    private void inicio1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio1FocusLost
        // TODO add your handling code here:
        if(!inicio1.getText().equals("")){
            pasa=0;
            minutosRango(inicio1.getText());
            if(pasa==0){
                minutosClave1();
            }else{
                inicio1.setText("");
            }
        }
    }//GEN-LAST:event_inicio1FocusLost

    private void termino1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino1FocusLost
        // TODO add your handling code here:
        if(!termino1.getText().equals("")){
            pasa=0;
            minutosRango(termino1.getText());
            if(pasa==0){
                minutosClave1();
            }else{
                termino1.setText("");
            }
        }
    }//GEN-LAST:event_termino1FocusLost

    private void inicio2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio2FocusLost
        // TODO add your handling code here:
        if(!inicio2.getText().equals("")){
            pasa=0;
            minutosRango(inicio2.getText());
            if(pasa==0){
                minutosClave2();
            }else{
                inicio2.setText("");
            }
        }
    }//GEN-LAST:event_inicio2FocusLost

    private void termino2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino2FocusLost
        // TODO add your handling code here:
        if(!termino2.getText().equals("")){
            pasa=0;
            minutosRango(termino2.getText());
            if(pasa==0){
                minutosClave2();
            }else{
                termino2.setText("");
            }
        }
    }//GEN-LAST:event_termino2FocusLost

    private void inicio3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio3FocusLost
        // TODO add your handling code here:
        if(!inicio3.getText().equals("")){
            pasa=0;
            minutosRango(inicio3.getText());
            if(pasa==0){
                minutosClave3();
            }else{
                inicio3.setText("");
            }
        }
    }//GEN-LAST:event_inicio3FocusLost

    private void termino3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino3FocusLost
        // TODO add your handling code here:
        if(!termino3.getText().equals("")){
            pasa=0;
            minutosRango(termino3.getText());
            if(pasa==0){
                minutosClave3();
            }else{
                termino3.setText("");
            }
        }
    }//GEN-LAST:event_termino3FocusLost

    private void inicio4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio4FocusLost
        // TODO add your handling code here:
        if(!inicio4.getText().equals("")){
            pasa=0;
            minutosRango(inicio4.getText());
            if(pasa==0){
                minutosClave4();
            }else{
                inicio4.setText("");
            }
        }
    }//GEN-LAST:event_inicio4FocusLost

    private void termino4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino4FocusLost
        // TODO add your handling code here:
        if(!termino4.getText().equals("")){
            pasa=0;
            minutosRango(termino4.getText());
            if(pasa==0){
                minutosClave4();
            }else{
                termino4.setText("");
            }
        }
    }//GEN-LAST:event_termino4FocusLost

    private void btnmaquinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmaquinasActionPerformed
        // TODO add your handling code here:
        busca_maquinas busca_maquinas = new busca_maquinas(null,true,connj,"");
        busca_maquinas.setLocationRelativeTo(this);
        busca_maquinas.setVisible(true);
        clavemaquina.setText(busca_maquinas.getText());
        busca_maquinas=null;
        if(clavemaquina.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM maquinas WHERE clave='"+clavemaquina.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    sonlaminas=rs0.getString("unidadcapacidad").toUpperCase();
                    TitledBorder bfin=BorderFactory.createTitledBorder("Cantidad "+sonlaminas);
                    bfin.setTitleColor(new java.awt.Color(51,102,0));
                    bfin.setTitleFont(new java.awt.Font("Tahoma",Font.BOLD,11));
                    panelcantidad.setBorder(bfin);
                    unidadvel=rs0.getString("unidadvelocidad");
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
}//GEN-LAST:event_btnmaquinasActionPerformed

    private void opKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_opKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_opKeyReleased

    private void opKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_opKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_opKeyPressed

    private void clavearticuloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clavearticuloFocusLost
        // TODO add your handling code here:
        op.setText(op.getText().toUpperCase());
        if(op.getText().equals("") || clavemaquina.getText().equals("")){
            if(clavemaquina.getText().equals("")){
                JOptionPane.showMessageDialog(this,"PRIMERO DEBES SELECCIONAR UNA MAQUINA O PROCESO","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                op.setText("");
            }

        }else{

                busca_programas_conversion busca_programas_conversion = new busca_programas_conversion(null,true,connj,op.getText(),clavemaquina.getText(),clavearticulo.getText());
                busca_programas_conversion.setLocationRelativeTo(this);
                busca_programas_conversion.setVisible(true);
                op.setText(busca_programas_conversion.getText());
                programa.setText(busca_programas_conversion.getPrograma());
                busca_programas_conversion=null;
                if(op.getText().equals("")){
                    programa.setText("");
                    clavearticulo.setText("");
                    articulo.setText("");
                    cantprogram.setText("0");
                }else{
                    rs0=null;
                    try{
                        String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,prod.prodcantidad,prod.prodcantidadpiezas,prod.prodcantkgpiezas,articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad FROM (((programa_conversion LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina.getText()+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.id_programa_conversion='"+programa.getText()+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.clavemaquina='"+clavemaquina.getText()+"') ORDER BY programa_conversion.fechaproduccion;";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            String opprogram=rs0.getString("op");
                            int cantprog=rs0.getInt("programcantidad");
                            int cantprod=rs0.getInt("prodcantidad");
                            clavearticulo.setText(rs0.getString("clavearticulo"));
                            articulo.setText(rs0.getString("articulo"));
                            cantprogram.setText(""+cantprog);
                            piezasxlamina=rs0.getInt("piezas");
                            kgart=rs0.getDouble("kg");
                        }
                        if(rs0!=null) {rs0.close(); }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                    //busca el maquinas articulos la maquina de donde viene
                    rs0=null;
                    try{
                        String senSQL="SELECT * FROM articulos_maquinas WHERE clavearticulo='"+clavearticulo.getText()+"' ORDER BY id_articulo_maquina";
                        rs0=conexion.consulta(senSQL,connj);
                        while(rs0.next()){
                            String tmaq=rs0.getString("clave");
                            if(tmaq.toUpperCase().equals(clavemaquina.getText().toUpperCase()))
                                break;
                            maq_inventario=tmaq;
                        }
                        if(rs0!=null) {rs0.close(); }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                }
        }
    }//GEN-LAST:event_clavearticuloFocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        datos_ops = new datos_ops(null,true,connj,"");
        datos_ops.setLocationRelativeTo(this);
        datos_ops.setVisible(true);
        datos_ops=null;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        datos_entrada_inventario_corr = new datos_entrada_inventario_corr(null,true,connj,"",valor_privilegio);
        datos_entrada_inventario_corr.setLocationRelativeTo(this);
        datos_entrada_inventario_corr.setVisible(true);
        datos_entrada_inventario_corr=null;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        String claveop=""+JOptionPane.showInputDialog(this, "OP:");
        String maqui=""+JOptionPane.showInputDialog(this, "MAQUINA:");
        if(!claveop.equals("") && !claveop.equals("null") && !maqui.equals("") && !maqui.equals("null")){ //programa si no esta programado
            datos_programas_conversion = new datos_programas_conversion(null,true,connj,claveop,maqui.toUpperCase(),"","",0,0,"Act",valor_privilegio);
            datos_programas_conversion.setLocationRelativeTo(this);
            datos_programas_conversion.setVisible(true);
            datos_programas_conversion=null;
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void clave1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            busca_paros busca_paros = new busca_paros(null,true,connj,"");
            busca_paros.setLocationRelativeTo(this);
            busca_paros.setVisible(true);
            clave1.setText(busca_paros.getText());//obtiene el valor seleccionado
            busca_paros=null;
            buscaParo1();
        }
}//GEN-LAST:event_clave1KeyPressed

    private void clave1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave1FocusLost
        // TODO add your handling code here:
        buscaParo1();
}//GEN-LAST:event_clave1FocusLost

    private void clave1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave1FocusGained
        // TODO add your handling code here:
        clave1.selectAll();
}//GEN-LAST:event_clave1FocusGained

    private void clave5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave5FocusGained
        // TODO add your handling code here:
         clave5.selectAll();
    }//GEN-LAST:event_clave5FocusGained

    private void clave5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave5FocusLost
        // TODO add your handling code here:
        buscaParo5();
    }//GEN-LAST:event_clave5FocusLost

    private void clave5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave5KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            busca_paros busca_paros = new busca_paros(null,true,connj,"");
            busca_paros.setLocationRelativeTo(this);
            busca_paros.setVisible(true);
            clave5.setText(busca_paros.getText());//obtiene el valor seleccionado
            busca_paros=null;
            buscaParo1();
        }
    }//GEN-LAST:event_clave5KeyPressed

    private void clave6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave6FocusGained
        // TODO add your handling code here:
         clave6.selectAll();
    }//GEN-LAST:event_clave6FocusGained

    private void clave6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave6FocusLost
        // TODO add your handling code here:
        buscaParo6();
    }//GEN-LAST:event_clave6FocusLost

    private void clave6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave6KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            busca_paros busca_paros = new busca_paros(null,true,connj,"");
            busca_paros.setLocationRelativeTo(this);
            busca_paros.setVisible(true);
            clave6.setText(busca_paros.getText());//obtiene el valor seleccionado
            busca_paros=null;
            buscaParo1();
        }
    }//GEN-LAST:event_clave6KeyPressed

    private void clave7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave7FocusGained
        // TODO add your handling code here:
         clave7.selectAll();
    }//GEN-LAST:event_clave7FocusGained

    private void clave7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave7FocusLost
        // TODO add your handling code here:
        buscaParo7();
    }//GEN-LAST:event_clave7FocusLost

    private void clave7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave7KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            busca_paros busca_paros = new busca_paros(null,true,connj,"");
            busca_paros.setLocationRelativeTo(this);
            busca_paros.setVisible(true);
            clave7.setText(busca_paros.getText());//obtiene el valor seleccionado
            busca_paros=null;
            buscaParo1();
        }
    }//GEN-LAST:event_clave7KeyPressed

    private void clave8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave8FocusGained
        // TODO add your handling code here:
         clave8.selectAll();
    }//GEN-LAST:event_clave8FocusGained

    private void clave8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave8FocusLost
        // TODO add your handling code here:
        buscaParo8();
    }//GEN-LAST:event_clave8FocusLost

    private void clave8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave8KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7) {
            busca_paros busca_paros = new busca_paros(null,true,connj,"");
            busca_paros.setLocationRelativeTo(this);
            busca_paros.setVisible(true);
            clave8.setText(busca_paros.getText());//obtiene el valor seleccionado
            busca_paros=null;
            buscaParo1();
        }
    }//GEN-LAST:event_clave8KeyPressed

    private void inicio5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio5FocusGained
        // TODO add your handling code here:
        inicio5.selectAll();
    }//GEN-LAST:event_inicio5FocusGained

    private void inicio5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio5FocusLost
        // TODO add your handling code here:
        if(!inicio5.getText().equals("")){
            pasa=0;
            minutosRango(inicio5.getText());
            if(pasa==0){
                minutosClave5();
            }else{
                inicio5.setText("");
            }
        }
    }//GEN-LAST:event_inicio5FocusLost

    private void inicio6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio6FocusGained
        // TODO add your handling code here:
        inicio6.selectAll();
    }//GEN-LAST:event_inicio6FocusGained

    private void inicio6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio6FocusLost
        // TODO add your handling code here:
        if(!inicio6.getText().equals("")){
            pasa=0;
            minutosRango(inicio6.getText());
            if(pasa==0){
                minutosClave6();
            }else{
                inicio6.setText("");
            }
        }

    }//GEN-LAST:event_inicio6FocusLost

    private void inicio7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio7FocusGained
        // TODO add your handling code here:
        inicio7.selectAll();
    }//GEN-LAST:event_inicio7FocusGained

    private void inicio7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio7FocusLost
        // TODO add your handling code here:
        if(!inicio7.getText().equals("")){
            pasa=0;
            minutosRango(inicio7.getText());
            if(pasa==0){
                minutosClave7();
            }else{
                inicio7.setText("");
            }
        }
    }//GEN-LAST:event_inicio7FocusLost

    private void inicio8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio8FocusGained
        // TODO add your handling code here:
        inicio8.selectAll();
    }//GEN-LAST:event_inicio8FocusGained

    private void inicio8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inicio8FocusLost
        // TODO add your handling code here:
        if(!inicio8.getText().equals("")){
            pasa=0;
            minutosRango(inicio8.getText());
            if(pasa==0){
                minutosClave8();
            }else{
                inicio8.setText("");
            }
        }
    }//GEN-LAST:event_inicio8FocusLost

    private void termino5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino5FocusGained
        // TODO add your handling code here:
        termino5.selectAll();
    }//GEN-LAST:event_termino5FocusGained

    private void termino5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino5FocusLost
        // TODO add your handling code here:
        if(!termino5.getText().equals("")){
            pasa=0;
            minutosRango(termino5.getText());
            if(pasa==0){
                minutosClave5();
            }else{
                termino5.setText("");
            }
        }
    }//GEN-LAST:event_termino5FocusLost

    private void termino6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino6FocusGained
        // TODO add your handling code here:
        termino6.selectAll();
    }//GEN-LAST:event_termino6FocusGained

    private void termino6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino6FocusLost
        // TODO add your handling code here:
        if(!termino6.getText().equals("")){
            pasa=0;
            minutosRango(termino6.getText());
            if(pasa==0){
                minutosClave6();
            }else{
                termino6.setText("");
            }
        }
    }//GEN-LAST:event_termino6FocusLost

    private void termino7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino7FocusGained
        // TODO add your handling code here:
        termino8.selectAll();
    }//GEN-LAST:event_termino7FocusGained

    private void termino7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino7FocusLost
        // TODO add your handling code here:
        if(!termino7.getText().equals("")){
            pasa=0;
            minutosRango(termino7.getText());
            if(pasa==0){
                minutosClave7();
            }else{
                termino7.setText("");
            }
        }
    }//GEN-LAST:event_termino7FocusLost

    private void termino8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino8FocusGained
        // TODO add your handling code here:
        termino8.selectAll();
    }//GEN-LAST:event_termino8FocusGained

    private void termino8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termino8FocusLost
        // TODO add your handling code here:
        if(!termino8.getText().equals("")){
            pasa=0;
            minutosRango(termino8.getText());
            if(pasa==0){
                minutosClave8();
            }else{
                termino8.setText("");
            }
        }
    }//GEN-LAST:event_termino8FocusLost

    private void clave1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_clave1KeyTyped

    private void clave1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_clave1KeyReleased

    private void clave3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clave3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_clave3KeyTyped

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tablamerma;
    private javax.swing.JFormattedTextField arreinicio;
    private javax.swing.JFormattedTextField arretermino;
    private javax.swing.JTextField articulo;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnmaquinas;
    private javax.swing.JButton btnoperador;
    private javax.swing.JFormattedTextField cantidad;
    private javax.swing.JTextField cantprogram;
    private javax.swing.JTextField clave1;
    private javax.swing.JTextField clave2;
    private javax.swing.JTextField clave3;
    private javax.swing.JTextField clave4;
    private javax.swing.JTextField clave5;
    private javax.swing.JTextField clave6;
    private javax.swing.JTextField clave7;
    private javax.swing.JTextField clave8;
    private javax.swing.JTextField clavearticulo;
    private javax.swing.JTextField clavemaquina;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField idoperador;
    private javax.swing.JFormattedTextField inicio1;
    private javax.swing.JFormattedTextField inicio2;
    private javax.swing.JFormattedTextField inicio3;
    private javax.swing.JFormattedTextField inicio4;
    private javax.swing.JFormattedTextField inicio5;
    private javax.swing.JFormattedTextField inicio6;
    private javax.swing.JFormattedTextField inicio7;
    private javax.swing.JFormattedTextField inicio8;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labelarreglo;
    private javax.swing.JLabel labelprod;
    private javax.swing.JLabel lbdes1;
    private javax.swing.JLabel lbdes2;
    private javax.swing.JLabel lbdes3;
    private javax.swing.JLabel lbdes4;
    private javax.swing.JLabel lbdes5;
    private javax.swing.JLabel lbdes6;
    private javax.swing.JLabel lbdes7;
    private javax.swing.JLabel lbdes8;
    private javax.swing.JLabel lbmin1;
    private javax.swing.JLabel lbmin2;
    private javax.swing.JLabel lbmin3;
    private javax.swing.JLabel lbmin4;
    private javax.swing.JLabel lbmin5;
    private javax.swing.JLabel lbmin6;
    private javax.swing.JLabel lbmin7;
    private javax.swing.JLabel lbmin8;
    private javax.swing.JTextField malas;
    private javax.swing.JTextField nombreoperador;
    private javax.swing.JTextField op;
    private javax.swing.JPanel panelcantidad;
    private javax.swing.JFormattedTextField prodinicio;
    private javax.swing.JFormattedTextField prodtermino;
    private javax.swing.JTextField programa;
    private javax.swing.JFormattedTextField termino1;
    private javax.swing.JFormattedTextField termino2;
    private javax.swing.JFormattedTextField termino3;
    private javax.swing.JFormattedTextField termino4;
    private javax.swing.JFormattedTextField termino5;
    private javax.swing.JFormattedTextField termino6;
    private javax.swing.JFormattedTextField termino7;
    private javax.swing.JFormattedTextField termino8;
    private javax.swing.JComboBox turno;
    private javax.swing.JTextField txtkgtotal;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_ops;
    private JDialog datos_entrada_inventario_corr;
    private JDialog datos_programas_conversion;
}
