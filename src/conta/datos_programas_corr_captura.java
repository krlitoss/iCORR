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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author IVONNE
 */
public class datos_programas_corr_captura extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    String clavemodificaf="";
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    int minarreglo=0,minproduccion=0,mltotales=0;
    int minclave1=0,minclave2=0,minclave3=0,minclave4=0;
    int pasa=0;
    Double anchototal=0.0,anchoutil=0.0,pesorealcombinacion=0.0;
    DefaultTableModel modelotcantidad=null;
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_programas_corr_captura(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        modelotcantidad=(DefaultTableModel) Tablacantidad.getModel();
        Tablacantidad.setModel(modelotcantidad);
        ajusteTabladatos();
        fecha.setDate(new Date());
        consultamodifica(clavemodifica);
        clavemodificaf=clavemodifica;
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

        modelotcantidad.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla


                    if (c == 1) {
                        if(model.getValueAt(f, c) != null){
                            int mlpartida=(Integer)model.getValueAt(f, c);
                            if(mlpartida>(mltotales*1.1)){
                                JOptionPane.showMessageDialog(null, "LOS METROS LINEALES EXCEDEN 10% DE LO PROGRAMADO ("+estandarentero.format(mltotales)+")", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                model.setValueAt(Integer.parseInt (fijo0decimales.format(mlpartida / ( (Double) model.getValueAt(f, 5)/100 ))), f, 2);
                                model.setValueAt(((Integer)model.getValueAt(f, 2) *  (Integer) model.getValueAt(f, 7))-(Integer)model.getValueAt(f, 4), f, 3);
                                Tablacantidad.changeSelection(f, 4, false, false);
                            }else{
                                model.setValueAt(Integer.parseInt (fijo0decimales.format(mlpartida / ( (Double) model.getValueAt(f, 5)/100 ))), f, 2);
                                model.setValueAt(((Integer)model.getValueAt(f, 2) *  (Integer) model.getValueAt(f, 7))-(Integer)model.getValueAt(f, 4), f, 3);
                                Tablacantidad.changeSelection(f, 4, false, false);
                            }
                        }else{
                            model.setValueAt(0, f, 2);
                            model.setValueAt(0, f, 3);
                            Tablacantidad.changeSelection(f, 4, false, false);
                        }
                    }
                    if (c == 4) {
                        int mlpartida=0;
                        if(model.getValueAt(f, 1) != null){
                            mlpartida=(Integer)model.getValueAt(f, 1);
                        }
                        if(model.getValueAt(f, c) != null){
                            model.setValueAt(Integer.parseInt (fijo0decimales.format(mlpartida / ( (Double) model.getValueAt(f, 5)/100 ))), f, 2);
                            model.setValueAt(((Integer)model.getValueAt(f, 2) *  (Integer) model.getValueAt(f, 7))-(Integer)model.getValueAt(f, 4), f, 3);
                        }else{
                            model.setValueAt(Integer.parseInt (fijo0decimales.format(mlpartida / ( (Double) model.getValueAt(f, 5)/100 ))), f, 2);
                            model.setValueAt(((Integer)model.getValueAt(f, 2) *  (Integer) model.getValueAt(f, 7))-(Integer)model.getValueAt(f, 4), f, 3);
                            model.setValueAt(0, f, 3);
                        }
                    }
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "NO SE PUEDE CALCULAR LA SUMAS"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
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
        public void ajusteTabladatos() {
        Tablacantidad.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablacantidad.getColumnModel().getColumn(1).setPreferredWidth(95);
        Tablacantidad.getColumnModel().getColumn(2).setPreferredWidth(70);
        Tablacantidad.getColumnModel().getColumn(3).setPreferredWidth(70);
        Tablacantidad.getColumnModel().getColumn(4).setPreferredWidth(95);
        Tablacantidad.getColumnModel().getColumn(5).setPreferredWidth(50);
        Tablacantidad.getColumnModel().getColumn(6).setPreferredWidth(50);
        Tablacantidad.getColumnModel().getColumn(7).setPreferredWidth(100);
        Tablacantidad.getColumnModel().getColumn(8).setPreferredWidth(100);

    }
    public void limpiatabla(){
        modelotcantidad.setNumRows(0);
    }
    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            rs0=null;
            btnaceptar.setEnabled(false);
            btnoperador.setEnabled(false);
            try{
                String senSQL="SELECT programa_corr_captura.*,conversion_captura.op,conversion_captura.prodml as prodmlop,conversion_captura.cantidad,conversion_captura.cantidadmalas,conversion_captura.clavearticulo,articulos.largo,articulos.ancho,operadores.nombre FROM (programa_corr_captura INNER JOIN (conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo) ON programa_corr_captura.id_programa_corr_captura=conversion_captura.id_programa_corr_captura) LEFT JOIN operadores ON programa_corr_captura.id_operador=operadores.id_operador  WHERE programa_corr_captura.id_programa_corr_captura='"+clavemodifica+"';";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    fecha.setDate(rs0.getDate("fechareal"));
                    turno.setSelectedItem(rs0.getString("turno"));
                    idoperador.setText(rs0.getString("id_operador"));
                    nombreoperador.setText(rs0.getString("nombre"));
                    programa.setText(rs0.getString("id_programa_corr"));
                    txtkgtotal.setText(estandarentero.format(rs0.getDouble("prodkg")));
                    arreinicio.setText(horacorta.format(rs0.getTime("inicioarreglo")));
                    arretermino.setText(horacorta.format(rs0.getTime("terminoarreglo")));
                    labelarreglo.setText("Arreglo ("+rs0.getInt("minutosarreglo")+" min)"); //muestra los datos obtenidos
                    prodinicio.setText(horacorta.format(rs0.getTime("inicioprod")));
                    prodtermino.setText(horacorta.format(rs0.getTime("terminoprod")));
                    labelprod.setText("Producción ("+rs0.getInt("minutosprod")+" min)"); //muestra los datos obtenidos
                    Double mlt=rs0.getDouble("prodmlop");
                    Double largo=rs0.getDouble("largo");
                    Double cortes= (mlt / (largo/100));
                    Double cant=rs0.getDouble("cantidad");
                    Double cantm=rs0.getDouble("cantidadmalas");
                    int cxp=(int) Double.parseDouble(fijo0decimales.format((cant+cantm)/cortes));
                    Object datos[]={rs0.getString("OP"),Integer.parseInt(fijo0decimales.format(mlt)),Integer.parseInt(fijo0decimales.format(cortes)),Integer.parseInt(fijo0decimales.format(cant)),Integer.parseInt(fijo0decimales.format(cantm)),largo,rs0.getDouble("ancho"),cxp,rs0.getString("clavearticulo")};
                    modelotcantidad.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //vacio de datos
            rs0=null;
            try{
                String senSQL="SELECT paros_captura.*,paros.descripcion FROM paros_captura LEFT JOIN paros ON paros_captura.id_paro=paros.id_paro WHERE (paros_captura.id_programa_corr_captura='"+clavemodifica+"' and paros_captura.estatus<>'Can')";
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
                    if(!turnot.equals("3") && (horaterprep.before(horainiprep) || horaterprep.equals(horainiprep))){
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
                    if(!turnot.equals("3") && (horaterprod.before(horainiprod) || horaterprod.equals(horainiprod))){
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
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablacantidad = new javax.swing.JTable();
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
        clave2 = new javax.swing.JTextField();
        clave3 = new javax.swing.JTextField();
        clave4 = new javax.swing.JTextField();
        lbmin1 = new javax.swing.JLabel();
        lbmin2 = new javax.swing.JLabel();
        lbmin3 = new javax.swing.JLabel();
        lbmin4 = new javax.swing.JLabel();
        programa = new javax.swing.JTextField();
        txtkgtotal = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_programas_corr_captura.class);
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
                .addContainerGap(272, Short.MAX_VALUE)
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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel3.border.titleFont"), resourceMap.getColor("jPanel3.border.titleColor"))); // NOI18N
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tablacantidad.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OP", "Metros Lineales", "Cortes", "Laminas", "Laminas Malas", "Largo", "Ancho", "Pliegos xAncho", "Clave Articulo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablacantidad.setToolTipText(resourceMap.getString("Tablacantidad.toolTipText")); // NOI18N
        Tablacantidad.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablacantidad.setColumnSelectionAllowed(true);
        Tablacantidad.setName("Tablacantidad"); // NOI18N
        Tablacantidad.setRowHeight(22);
        Tablacantidad.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Tablacantidad.getTableHeader().setReorderingAllowed(false);
        Tablacantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablacantidadKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Tablacantidad);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel4.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel4.border.titleFont"), resourceMap.getColor("jPanel4.border.titleColor"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
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

        lbdes1.setText(resourceMap.getString("lbdes1.text")); // NOI18N
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

        lbdes2.setText(resourceMap.getString("lbdes2.text")); // NOI18N
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

        lbdes3.setText(resourceMap.getString("lbdes3.text")); // NOI18N
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

        lbdes4.setText(resourceMap.getString("lbdes4.text")); // NOI18N
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
        lbmin1.setName("lbmin1"); // NOI18N

        lbmin2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin2.setText(resourceMap.getString("lbmin2.text")); // NOI18N
        lbmin2.setName("lbmin2"); // NOI18N

        lbmin3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin3.setText(resourceMap.getString("lbmin3.text")); // NOI18N
        lbmin3.setName("lbmin3"); // NOI18N

        lbmin4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmin4.setText(resourceMap.getString("lbmin4.text")); // NOI18N
        lbmin4.setName("lbmin4"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(clave2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(clave1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(clave3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(56, 56, 56)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(inicio1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(termino1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(inicio2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(termino2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(inicio3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(termino3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clave4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(inicio4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(termino4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbmin4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbmin3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbmin2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbmin1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbdes2, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbdes3, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbdes4, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbdes1, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(termino1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbdes1)
                                    .addComponent(lbmin1)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15)
                                .addComponent(clave2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbmin2)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inicio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inicio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(termino2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbdes2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(inicio3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(termino3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbdes3)
                    .addComponent(clave3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbmin3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(inicio4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(termino4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbdes4)
                    .addComponent(clave4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbmin4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        programa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        programa.setText(resourceMap.getString("programa.text")); // NOI18N
        programa.setName("programa"); // NOI18N
        programa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                programaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                programaFocusLost(evt);
            }
        });

        txtkgtotal.setEditable(false);
        txtkgtotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtkgtotal.setText(resourceMap.getString("txtkgtotal.text")); // NOI18N
        txtkgtotal.setFocusable(false);
        txtkgtotal.setName("txtkgtotal"); // NOI18N

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
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
                                    .addComponent(programa, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(idoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnoperador)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(nombreoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtkgtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nombreoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(programa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtkgtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        Double totalkgreal=0.0;
        Double totalkgmermalaminas=0.0;
        Double totalm2=0.0;

        if (Tablacantidad.getCellEditor() != null) {//finaliza el editor
            Tablacantidad.getCellEditor().stopCellEditing();
        }
        int filas=Tablacantidad.getRowCount();
        int columnas=Tablacantidad.getColumnCount();
        int camposvacios=0;
        int mlmax=0;

        for(int i=0;i<=(filas-1);i=i+1){
            for(int j=0;j<=(columnas-1);j=j+1){
                if(modelotcantidad.getValueAt(i, j) == null||modelotcantidad.getValueAt(i, j).equals("")){
                    camposvacios=1;
                }
                if(j==1){
                    int mlt=(Integer)modelotcantidad.getValueAt(i, j);
                    if(mlt<=0){ //revisa que hayas capturado los metros lineales
                        camposvacios=1;
                    }else{
                        if(mlt>mlmax)
                            mlmax=mlt; //guardamos los metros lineales mas altos
                    }
                }
                if(j==0){
                    if(camposvacios==0){ //si todos los datos estan correctos sacamos el peso de cada articulo
                        int totallaminas=(Integer) modelotcantidad.getValueAt(i, 3);
                        int totallaminasmalas=(Integer) modelotcantidad.getValueAt(i, 4);
                        Double kgreal=0.0;
                        Double kgmermalaminas=0.0;
                        Double m2real=0.0;
                            rs0=null;
                            try{
                                String senSQL="SELECT articulos.* FROM articulos WHERE (articulos.clavearticulo='"+(String) modelotcantidad.getValueAt(i, 8)+"')";
                                rs0=conexion.consulta(senSQL,connj);
                                if(rs0.next()){
                                    Double pzaslamina=rs0.getDouble("piezas");
                                    Double kglamina=rs0.getDouble("kg")*pzaslamina;
                                    Double m2lamina=rs0.getDouble("m2")*pzaslamina;
                                    kgreal=kglamina*totallaminas;
                                    kgmermalaminas=kglamina*totallaminasmalas;
                                    m2real=m2lamina*totallaminas;
                                }
                                if(rs0!=null) {
                                    rs0.close();
                                }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                        totalkgreal=totalkgreal+kgreal;
                        totalkgmermalaminas=totalkgmermalaminas+kgmermalaminas;
                        totalm2=totalm2+m2real;
                    }
                }

            }
        }/**fin de revisar los campos vacios*/

        //calcula de nuevo los minutos de arreglo y produccion
        minutosArreglo();
        minutosProduccion();
        minutosClave1();
        minutosClave2();
        minutosClave3();
        minutosClave4();


        if(fecha.getDate()==null || idoperador.getText().equals("")||programa.getText().equals("")||arreinicio.getText().equals("")||arretermino.getText().equals("")||prodtermino.getText().equals("")||camposvacios==1 || filas<=0){
            String err="VERIFICA HAY CAMPOS VACIOS";
            if(camposvacios==1)
                err="LA TABLA DE DETALLE TIENE CAMPOS VACIOS";
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
            txtkgtotal.setText(estandarentero.format(totalkgreal));
            if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR!!\n<html><font size=5 color=#DC143C><b>ML: </b>"+estandarentero.format(mlmax)+"<br><b>KG: </b>"+estandarentero.format(totalkgreal)+"<br><b>Min. Prod: </b>"+estandarentero.format(minproduccion)+"<br><b>Velocidad ML/min: </b>"+estandarentero.format(mlmax/minproduccion)+"<br></font></html>"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                String senSQL="INSERT INTO programa_corr_captura(estatus, id_programa_corr, fechareal, turno, id_operador, inicioarreglo, terminoarreglo, minutosarreglo, inicioprod, terminoprod, minutosprod, prodml, prodm2, prodkg, mermakgtrim, mermakglaminas, mermakgotros) VALUES ('Act', '"+programa.getText()+"', '"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', '"+arreinicio.getText()+"', '"+arretermino.getText()+"', '"+minarreglo+"', '"+prodinicio.getText()+"', '"+prodtermino.getText()+"', '"+minproduccion+"','"+mlmax+"', '"+totalm2+"', '"+totalkgreal+"', '"+((mlmax*((anchototal-anchoutil)/100))*pesorealcombinacion)+"', '"+totalkgmermalaminas+"', '0');";
                conexion.modifica_p(senSQL,connj,valor_privilegio);
                /**funcion que regresa el numero con que se guardo la orden de compra*/
                int claveidcapturamax=1;
                rs0=null;
                try{
                    senSQL="SELECT max(id_programa_corr_captura) as corr_capturamax FROM programa_corr_captura";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        claveidcapturamax=rs0.getInt("corr_capturamax");
                    }
                    if(rs0!=null) {
                        rs0.close();
                    }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                
                for(int i=0;i<=(filas-1);i=i+1){ //captura todas las partidas
                        //guarda los registros en todos los procesos en su unidad(laminas,golpes) y piezas (cantidad importante)
                        rs0=null;
                        try{
                            senSQL="SELECT articulos.* FROM articulos WHERE (articulos.clavearticulo='"+(String) modelotcantidad.getValueAt(i, 8)+"')";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                Double pzaslamina=rs0.getDouble("piezas");
                                int totallaminas=(Integer) modelotcantidad.getValueAt(i, 3);
                                int totallaminasmalas=(Integer) modelotcantidad.getValueAt(i, 4);
                                int pzas=(int) (totallaminas * pzaslamina);
                                int pzasmalas=(int) (totallaminasmalas * pzaslamina);
                                
                                String senSQLmov="INSERT INTO conversion_captura(estatus, id_programa_corr_captura, id_programa_corr, op, clavearticulo, fechareal, turno, id_operador, clave, inicioarreglo, terminoarreglo, minutosarreglo, inicioprod, terminoprod, minutosprod, prodml, cantidad, cantidadmalas,cantidadpiezas, cantidadmalaspiezas,sistema)VALUES ('Act', '"+claveidcapturamax+"', '"+programa.getText()+"', '"+modelotcantidad.getValueAt(i, 0)+"', '"+modelotcantidad.getValueAt(i, 8)+"', '"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', 'CORR', '"+arreinicio.getText()+"', '"+arretermino.getText()+"', '"+minarreglo+"', '"+prodinicio.getText()+"', '"+prodtermino.getText()+"', '"+minproduccion+"', '"+modelotcantidad.getValueAt(i, 1)+"', '"+modelotcantidad.getValueAt(i, 3)+"', '"+modelotcantidad.getValueAt(i, 4)+"', '"+pzas+"', '"+pzasmalas+"','true');";
                                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                                //inserta un registr en el inventario de corrugadora
                                senSQLmov="INSERT INTO entradas_inventario_corr(estatus, fecha, turno, id_programa_corr_captura, id_programa_corr, op, clavearticulo, cantidadlaminas, almacen, ubicacion, comentarios, sistema) VALUES ('Act', '"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+claveidcapturamax+"', '"+programa.getText()+"', '"+modelotcantidad.getValueAt(i, 0)+"', '"+modelotcantidad.getValueAt(i, 8)+"', '"+modelotcantidad.getValueAt(i, 3)+"', '2', '', '', true);";
                                conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                            }
                            if(rs0!=null) { rs0.close(); }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                }

                //captura los tiempos muertos
                if(!inicio1.getText().equals("") && !termino1.getText().equals("") && !clave1.getText().equals("")){
                    String senSQLparo="INSERT INTO paros_captura(estatus, id_programa_corr_captura, id_programa_corr, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+programa.getText()+"', '', '"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', 'CORR', '"+clave1.getText()+"', '"+inicio1.getText()+"', '"+termino1.getText()+"', '"+minclave1+"');";
                    conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                }
                if(!inicio2.getText().equals("") && !termino2.getText().equals("") && !clave2.getText().equals("")){
                    String senSQLparo="INSERT INTO paros_captura(estatus, id_programa_corr_captura, id_programa_corr, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+programa.getText()+"', '', '"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', 'CORR', '"+clave2.getText()+"', '"+inicio2.getText()+"', '"+termino2.getText()+"', '"+minclave2+"');";
                    conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                }
                if(!inicio3.getText().equals("") && !termino3.getText().equals("") && !clave3.getText().equals("")){
                    String senSQLparo="INSERT INTO paros_captura(estatus, id_programa_corr_captura, id_programa_corr, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+programa.getText()+"', '', '"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', 'CORR', '"+clave3.getText()+"', '"+inicio3.getText()+"', '"+termino3.getText()+"', '"+minclave3+"');";
                    conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                }
                if(!inicio4.getText().equals("") && !termino4.getText().equals("") && !clave4.getText().equals("")){
                    String senSQLparo="INSERT INTO paros_captura(estatus, id_programa_corr_captura, id_programa_corr, op, fechareal, turno, id_operador, clavemaquina, id_paro, inicioparo, terminoparo, minutos) VALUES ('Act', '"+claveidcapturamax+"','"+programa.getText()+"', '', '"+fechainsertar.format(fecha.getDate())+"', '"+(String)turno.getSelectedItem()+"', '"+idoperador.getText()+"', 'CORR', '"+clave4.getText()+"', '"+inicio4.getText()+"', '"+termino4.getText()+"', '"+minclave4+"');";
                    conexion.modificamov_p(senSQLparo,connj,valor_privilegio);
                }


                ///**funcion actualiza el status del programa*/
               rs0=null;
                try{
                    senSQL="SELECT programa_corr.id_programa_corr,programa_corr.ml,sumaml.totalml FROM programa_corr LEFT JOIN (SELECT programa_corr_captura.id_programa_corr,sum(prodml) as totalml FROM programa_corr_captura WHERE (programa_corr_captura.estatus<>'Can') GROUP BY programa_corr_captura.id_programa_corr) as sumaml ON programa_corr.id_programa_corr=sumaml.id_programa_corr WHERE (programa_corr.id_programa_corr='"+programa.getText()+"' and programa_corr.estatus<>'Can')";
                    rs0=conexion.consulta(senSQL,connj);
                    int vacio=0;
                    if(rs0.next()){
                        Double mlprogramado=rs0.getDouble("ml");
                        Double mlproducido=rs0.getDouble("totalml");
                        if(mlproducido>=mlprogramado){
                            String senSQLmov = "UPDATE programa_corr SET estatus='Ter',imprimir2='false' WHERE id_programa_corr='" + programa.getText() + "';";
                            conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                        }else{
                            String senSQLmov = "UPDATE programa_corr SET estatus='Act',imprimir2='false' WHERE id_programa_corr='" + programa.getText() + "';";
                            conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                        }
                        vacio=1;
                    }
                    if(vacio==0){
                        String senSQLmov = "UPDATE programa_corr SET estatus='Act',imprimir2='false' WHERE id_programa_corr='" + programa.getText() + "';";
                        conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                    }
                    if(rs0!=null) {
                        rs0.close();
                    }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

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

    private void TablacantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablacantidadKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }
    }//GEN-LAST:event_TablacantidadKeyPressed

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

    private void clave1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave1FocusGained
        // TODO add your handling code here:
        clave1.selectAll();
    }//GEN-LAST:event_clave1FocusGained

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

    private void programaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_programaFocusLost
        // TODO add your handling code here:
        limpiatabla();
        if(programa.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT programa_corr.ml,programa_corr.anchototal,programa_corr.anchoutil,programa_corr.pesorealcombinacion,programa_corr_detalle.*,articulos.largo,articulos.ancho FROM (programa_corr INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo  WHERE (programa_corr_detalle.id_programa_corr='"+programa.getText()+"' and programa_corr.estatus<>'Can') ORDER BY programa_corr_detalle.id_programa_corr_detalle";
                rs0=conexion.consulta(senSQL,connj);
                int vacio=0;
                while(rs0.next()){
                    mltotales=rs0.getInt("ml");
                    Object datos[]={rs0.getString("OP"),0,0,0,0,rs0.getDouble("largo"),rs0.getDouble("ancho"),rs0.getInt("pliegosancho"),rs0.getString("clavearticulo")};
                    modelotcantidad.addRow(datos);
                    anchototal=rs0.getDouble("anchototal");
                    anchoutil=rs0.getDouble("anchoutil");
                    pesorealcombinacion=rs0.getDouble("pesorealcombinacion");
                    vacio=1;
                }
                if(vacio==0){
                    JOptionPane.showMessageDialog(this,"EL PROGRAMA NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    programa.setText("");
                    programa.requestFocus();
                }

                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }//GEN-LAST:event_programaFocusLost

    private void programaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_programaFocusGained
        // TODO add your handling code here:
        programa.selectAll();
    }//GEN-LAST:event_programaFocusGained

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

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tablacantidad;
    private javax.swing.JFormattedTextField arreinicio;
    private javax.swing.JFormattedTextField arretermino;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnoperador;
    private javax.swing.JTextField clave1;
    private javax.swing.JTextField clave2;
    private javax.swing.JTextField clave3;
    private javax.swing.JTextField clave4;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField idoperador;
    private javax.swing.JFormattedTextField inicio1;
    private javax.swing.JFormattedTextField inicio2;
    private javax.swing.JFormattedTextField inicio3;
    private javax.swing.JFormattedTextField inicio4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelarreglo;
    private javax.swing.JLabel labelprod;
    private javax.swing.JLabel lbdes1;
    private javax.swing.JLabel lbdes2;
    private javax.swing.JLabel lbdes3;
    private javax.swing.JLabel lbdes4;
    private javax.swing.JLabel lbmin1;
    private javax.swing.JLabel lbmin2;
    private javax.swing.JLabel lbmin3;
    private javax.swing.JLabel lbmin4;
    private javax.swing.JTextField nombreoperador;
    private javax.swing.JFormattedTextField prodinicio;
    private javax.swing.JFormattedTextField prodtermino;
    private javax.swing.JTextField programa;
    private javax.swing.JFormattedTextField termino1;
    private javax.swing.JFormattedTextField termino2;
    private javax.swing.JFormattedTextField termino3;
    private javax.swing.JFormattedTextField termino4;
    private javax.swing.JComboBox turno;
    private javax.swing.JTextField txtkgtotal;
    // End of variables declaration//GEN-END:variables

}
