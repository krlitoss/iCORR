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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jxl.*;
import jxl.write.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JasperPrintManager;
/**
 *
 * @author IVONNE
 */
public class corrflex extends javax.swing.JInternalFrame {
    Connection conn=null;
    ResultSet rs0;
    DecimalFormat moneda4decimales=new DecimalFormat("#,###,##0.0000");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    DecimalFormat fijo1decimales=new DecimalFormat("######0.0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MMM-yy");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DecimalFormat horasminutos=new DecimalFormat("##########00");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");

    Double tinta_gramos_m2=0.0,tinta_modulo=0.0,tinta_porcentaje=0.0;

    private Properties conf;
    Calendar calendariniciosem = new GregorianCalendar();
    Calendar calendarfinsem = new GregorianCalendar();

    DefaultTableModel modelocorr=null,modelotcy=null,modelolinealtcy=null,modelosys=null,modelolinealsys=null,modelocom=null; //agrega modelos de tablas
    DefaultTableModel modeloeng=null,modelomarv=null,modelosuap=null,modelodiec=null,modeloray=null,modelopdo=null,modelodes=null,modeloman=null,modelofon=null,modelofle=null,modelotro=null;
    ListSelectionModel modelocorrseleccion,modelotcyseleccion,modelolinealtcyseleccion,modelosysseleccion,modelolinealsysseleccion,modelocomseleccion;
    ListSelectionModel modeloengseleccion,modelomarvseleccion,modelosuapseleccion,modelodiecseleccion,modelorayseleccion,modelopdoseleccion,modelodesseleccion,modelomanseleccion,modelofonseleccion,modelofleseleccion,modelotroseleccion;

    DefaultTableModel modelot1=null;//papel

    String nocolor="<html><font color=#778899>";
    String valor_privilegio="1";

    /** Creates new form usuarios */
    public corrflex(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf=conexion.archivoInicial();
        conn=connt;
        datos_parametros();
        datos_privilegios();
        ajusteTabladatos();
        horasminutos.setRoundingMode(RoundingMode.DOWN);
        //tabla de papel
        modelot1=(DefaultTableModel) Tabladatos_papel.getModel();
        Tabladatos_papel.setModel(modelot1);
        //asigna los modelos y suma de listas
        modelocorr=(DefaultTableModel) Tablacorr.getModel();//inicio de partida
        Tablacorr.setModel(modelocorr);
        modelocorrseleccion = Tablacorr.getSelectionModel();
        modelotcy=(DefaultTableModel) Tablatcy.getModel();//inicio de partida
        Tablatcy.setModel(modelotcy);
        modelotcyseleccion = Tablatcy.getSelectionModel();
        modelolinealtcy=(DefaultTableModel) Tablalinealtcy.getModel();//inicio de partida
        Tablalinealtcy.setModel(modelolinealtcy);
        modelolinealtcyseleccion = Tablalinealtcy.getSelectionModel();
        modelosys=(DefaultTableModel) Tablasys.getModel();//inicio de partida
        Tablasys.setModel(modelosys);
        modelosysseleccion = Tablasys.getSelectionModel();
        modelolinealsys=(DefaultTableModel) Tablalinealsys.getModel();//inicio de partida
        Tablalinealsys.setModel(modelolinealsys);
        modelolinealsysseleccion = Tablalinealsys.getSelectionModel();
        modelocom=(DefaultTableModel) Tablacom.getModel();//inicio de partida
        Tablacom.setModel(modelocom);
        modelocomseleccion = Tablacom.getSelectionModel();
        modeloeng=(DefaultTableModel) Tablaeng.getModel();//inicio de partida
        Tablaeng.setModel(modeloeng);
        modeloengseleccion = Tablaeng.getSelectionModel();
        modelomarv=(DefaultTableModel) Tablamarv.getModel();//inicio de partida
        Tablamarv.setModel(modelomarv);
        modelomarvseleccion = Tablamarv.getSelectionModel();
        modelosuap=(DefaultTableModel) Tablasuap.getModel();//inicio de partida
        Tablasuap.setModel(modelosuap);
        modelosuapseleccion = Tablasuap.getSelectionModel();
        modelodiec=(DefaultTableModel) Tabladiec.getModel();//inicio de partida
        Tabladiec.setModel(modelodiec);
        modelodiecseleccion = Tabladiec.getSelectionModel();
        modeloray=(DefaultTableModel) Tablaray.getModel();//inicio de partida
        Tablaray.setModel(modeloray);
        modelorayseleccion = Tablaray.getSelectionModel();
        modelopdo=(DefaultTableModel) Tablapdo.getModel();//inicio de partida
        Tablapdo.setModel(modelopdo);
        modelopdoseleccion = Tablapdo.getSelectionModel();
        modelodes=(DefaultTableModel) Tablades.getModel();//inicio de partida
        Tablades.setModel(modelodes);
        modelodesseleccion = Tablades.getSelectionModel();
        modeloman=(DefaultTableModel) Tablaman.getModel();//inicio de partida
        Tablaman.setModel(modeloman);
        modelomanseleccion = Tablaman.getSelectionModel();
        modelofon=(DefaultTableModel) Tablafon.getModel();//inicio de partida
        Tablafon.setModel(modelofon);
        modelofonseleccion = Tablafon.getSelectionModel();
        modelofle=(DefaultTableModel) Tablafle.getModel();//inicio de partida
        Tablafle.setModel(modelofle);
        modelofleseleccion = Tablafle.getSelectionModel();
        modelotro=(DefaultTableModel) Tablatro.getModel();//inicio de partida
        Tablatro.setModel(modelotro);
        modelotroseleccion = Tablatro.getSelectionModel();
        //fecha de hoy para calculo de fechas de entrega
        calendariniciosem.setTime(new Date()); //gregorian hoy
        calendariniciosem.add(Calendar.DAY_OF_WEEK, (-calendariniciosem.get(Calendar.DAY_OF_WEEK)+1) );
        //fecha de fin de semana para fechas de entrega
        calendarfinsem.setTime(calendariniciosem.getTime());
        calendarfinsem.add(Calendar.DATE,7);
        //carga los datos en la tabla correspondiente
        datoscorr();
        datostcy();
        datoslinealtcy();
        datossys();
        datoslinealsys();
        datoscom();
        datoseng();
        datosmarv();
        datossuap();
        datosdiec();
        datosray();
        datospdo();
        datosdes();
        datosman();
        datosfon();
        datosfle();
        datostro();

        modelocorrseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumalaminaspedidas=0,sumaprogramml=0,sumaprogramlaminas=0,sumaprogramkg=0,sumaprodml=0,sumaprodlaminas=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if(Tablacorr.getValueAt(i,0)!=null && !Tablacorr.getValueAt(i,0).equals("")){ //suma los metros lineales
                                    cuenta++;
                            }
                            if(Tablacorr.getValueAt(i,4)!=null && !Tablacorr.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumalaminaspedidas+=Integer.parseInt(""+Tablacorr.getValueAt(i,4));
                            }
                            if(Tablacorr.getValueAt(i,10)!=null && !Tablacorr.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramml+=Integer.parseInt(""+Tablacorr.getValueAt(i,10));
                            }
                            if(Tablacorr.getValueAt(i,11)!=null && !Tablacorr.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramlaminas+=Integer.parseInt(""+Tablacorr.getValueAt(i,11));
                            }
                            if(Tablacorr.getValueAt(i,12)!=null && !Tablacorr.getValueAt(i,12).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablacorr.getValueAt(i,12));
                            }
                            if(Tablacorr.getValueAt(i,17)!=null && !Tablacorr.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodml+=Integer.parseInt(""+Tablacorr.getValueAt(i,17));
                            }
                            if(Tablacorr.getValueAt(i,18)!=null && !Tablacorr.getValueAt(i,18).equals("")){ //suma los metros lineales
                                    sumaprodlaminas+=Integer.parseInt(""+Tablacorr.getValueAt(i,18));
                            }
                            if(Tablacorr.getValueAt(i,19)!=null && !Tablacorr.getValueAt(i,19).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablacorr.getValueAt(i,19));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Lam. Pedida: "+estandarentero.format(sumalaminaspedidas)+"        Lam. x Program: "+estandarentero.format(sumalaminaspedidas-sumaprogramlaminas)+"        Lam. x Prod: "+estandarentero.format(sumalaminaspedidas-sumaprodlaminas)+"        Program.( ML: "+estandarentero.format(sumaprogramml)+"    Lam.: "+estandarentero.format(sumaprogramlaminas)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( ML: "+estandarentero.format(sumaprodml)+"    Lam.: "+estandarentero.format(sumaprodlaminas)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"      Lam. Pedidas: "+estandarentero.format(sumalaminaspedidas)+"       Programado( ML: "+estandarentero.format(sumaprogramml)+"    Lam.: "+estandarentero.format(sumaprogramlaminas)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"       Fabricado:( ML: "+estandarentero.format(sumaprodml)+"    Lam.: "+estandarentero.format(sumaprodlaminas)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelotcyseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablatcy.getValueAt(i,5)!=null && !Tablatcy.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablatcy.getValueAt(i,5));
                            }

                            if(Tablatcy.getValueAt(i,10)!=null && !Tablatcy.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablatcy.getValueAt(i,10));
                            }
                            if(Tablatcy.getValueAt(i,11)!=null && !Tablatcy.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablatcy.getValueAt(i,11));
                            }

                            if(Tablatcy.getValueAt(i,16)!=null && !Tablatcy.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablatcy.getValueAt(i,16));
                            }
                            if(Tablatcy.getValueAt(i,17)!=null && !Tablatcy.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablatcy.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Golpes x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Golpes x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"       Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelolinealtcyseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablalinealtcy.getValueAt(i,5)!=null && !Tablalinealtcy.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablalinealtcy.getValueAt(i,5));
                            }

                            if(Tablalinealtcy.getValueAt(i,10)!=null && !Tablalinealtcy.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablalinealtcy.getValueAt(i,10));
                            }
                            if(Tablalinealtcy.getValueAt(i,11)!=null && !Tablalinealtcy.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablalinealtcy.getValueAt(i,11));
                            }

                            if(Tablalinealtcy.getValueAt(i,16)!=null && !Tablalinealtcy.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablalinealtcy.getValueAt(i,16));
                            }
                            if(Tablalinealtcy.getValueAt(i,17)!=null && !Tablalinealtcy.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablalinealtcy.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidas: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidas: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelosysseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablasys.getValueAt(i,5)!=null && !Tablasys.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablasys.getValueAt(i,5));
                            }

                            if(Tablasys.getValueAt(i,10)!=null && !Tablasys.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablasys.getValueAt(i,10));
                            }
                            if(Tablasys.getValueAt(i,11)!=null && !Tablasys.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablasys.getValueAt(i,11));
                            }

                            if(Tablasys.getValueAt(i,16)!=null && !Tablasys.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablasys.getValueAt(i,16));
                            }
                            if(Tablasys.getValueAt(i,17)!=null && !Tablasys.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablasys.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Golpes x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Golpes x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelolinealsysseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablalinealsys.getValueAt(i,5)!=null && !Tablalinealsys.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablalinealsys.getValueAt(i,5));
                            }

                            if(Tablalinealsys.getValueAt(i,10)!=null && !Tablalinealsys.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablalinealsys.getValueAt(i,10));
                            }
                            if(Tablalinealsys.getValueAt(i,11)!=null && !Tablalinealsys.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablalinealsys.getValueAt(i,11));
                            }

                            if(Tablalinealsys.getValueAt(i,16)!=null && !Tablalinealsys.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablalinealsys.getValueAt(i,16));
                            }
                            if(Tablalinealsys.getValueAt(i,17)!=null && !Tablalinealsys.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablalinealsys.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelocomseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablacom.getValueAt(i,5)!=null && !Tablacom.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablacom.getValueAt(i,5));
                            }

                            if(Tablacom.getValueAt(i,10)!=null && !Tablacom.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablacom.getValueAt(i,10));
                            }
                            if(Tablacom.getValueAt(i,11)!=null && !Tablacom.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablacom.getValueAt(i,11));
                            }

                            if(Tablacom.getValueAt(i,16)!=null && !Tablacom.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablacom.getValueAt(i,16));
                            }
                            if(Tablacom.getValueAt(i,17)!=null && !Tablacom.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablacom.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Golpes x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Golpes x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modeloengseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablaeng.getValueAt(i,5)!=null && !Tablaeng.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablaeng.getValueAt(i,5));
                            }

                            if(Tablaeng.getValueAt(i,10)!=null && !Tablaeng.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablaeng.getValueAt(i,10));
                            }
                            if(Tablaeng.getValueAt(i,11)!=null && !Tablaeng.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablaeng.getValueAt(i,11));
                            }

                            if(Tablaeng.getValueAt(i,16)!=null && !Tablaeng.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablaeng.getValueAt(i,16));
                            }
                            if(Tablaeng.getValueAt(i,17)!=null && !Tablaeng.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablaeng.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelomarvseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablamarv.getValueAt(i,5)!=null && !Tablamarv.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablamarv.getValueAt(i,5));
                            }

                            if(Tablamarv.getValueAt(i,10)!=null && !Tablamarv.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablamarv.getValueAt(i,10));
                            }
                            if(Tablamarv.getValueAt(i,11)!=null && !Tablamarv.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablamarv.getValueAt(i,11));
                            }

                            if(Tablamarv.getValueAt(i,16)!=null && !Tablamarv.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablamarv.getValueAt(i,16));
                            }
                            if(Tablamarv.getValueAt(i,17)!=null && !Tablamarv.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablamarv.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelosuapseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablasuap.getValueAt(i,5)!=null && !Tablasuap.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablasuap.getValueAt(i,5));
                            }

                            if(Tablasuap.getValueAt(i,10)!=null && !Tablasuap.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablasuap.getValueAt(i,10));
                            }
                            if(Tablasuap.getValueAt(i,11)!=null && !Tablasuap.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablasuap.getValueAt(i,11));
                            }

                            if(Tablasuap.getValueAt(i,16)!=null && !Tablasuap.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablasuap.getValueAt(i,16));
                            }
                            if(Tablasuap.getValueAt(i,17)!=null && !Tablasuap.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablasuap.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Golpes x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Golpes x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelodiecseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tabladiec.getValueAt(i,5)!=null && !Tabladiec.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tabladiec.getValueAt(i,5));
                            }

                            if(Tabladiec.getValueAt(i,10)!=null && !Tabladiec.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tabladiec.getValueAt(i,10));
                            }
                            if(Tabladiec.getValueAt(i,11)!=null && !Tabladiec.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tabladiec.getValueAt(i,11));
                            }

                            if(Tabladiec.getValueAt(i,16)!=null && !Tabladiec.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tabladiec.getValueAt(i,16));
                            }
                            if(Tabladiec.getValueAt(i,17)!=null && !Tabladiec.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tabladiec.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Golpes x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Golpes x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelorayseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablaray.getValueAt(i,5)!=null && !Tablaray.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablaray.getValueAt(i,5));
                            }

                            if(Tablaray.getValueAt(i,10)!=null && !Tablaray.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablaray.getValueAt(i,10));
                            }
                            if(Tablaray.getValueAt(i,11)!=null && !Tablaray.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablaray.getValueAt(i,11));
                            }

                            if(Tablaray.getValueAt(i,16)!=null && !Tablaray.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablaray.getValueAt(i,16));
                            }
                            if(Tablaray.getValueAt(i,17)!=null && !Tablaray.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablaray.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Golpes x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Golpes x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Golpes Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Golpes: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Golpes: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelopdoseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablapdo.getValueAt(i,5)!=null && !Tablapdo.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablapdo.getValueAt(i,5));
                            }

                            if(Tablapdo.getValueAt(i,10)!=null && !Tablapdo.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablapdo.getValueAt(i,10));
                            }
                            if(Tablapdo.getValueAt(i,11)!=null && !Tablapdo.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablapdo.getValueAt(i,11));
                            }

                            if(Tablapdo.getValueAt(i,16)!=null && !Tablapdo.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablapdo.getValueAt(i,16));
                            }
                            if(Tablapdo.getValueAt(i,17)!=null && !Tablapdo.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablapdo.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelodesseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablades.getValueAt(i,5)!=null && !Tablades.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablades.getValueAt(i,5));
                            }

                            if(Tablades.getValueAt(i,10)!=null && !Tablades.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablades.getValueAt(i,10));
                            }
                            if(Tablades.getValueAt(i,11)!=null && !Tablades.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablades.getValueAt(i,11));
                            }

                            if(Tablades.getValueAt(i,16)!=null && !Tablades.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablades.getValueAt(i,16));
                            }
                            if(Tablades.getValueAt(i,17)!=null && !Tablades.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablades.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelomanseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablaman.getValueAt(i,5)!=null && !Tablaman.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablaman.getValueAt(i,5));
                            }

                            if(Tablaman.getValueAt(i,10)!=null && !Tablaman.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablaman.getValueAt(i,10));
                            }
                            if(Tablaman.getValueAt(i,11)!=null && !Tablaman.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablaman.getValueAt(i,11));
                            }

                            if(Tablaman.getValueAt(i,16)!=null && !Tablaman.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablaman.getValueAt(i,16));
                            }
                            if(Tablaman.getValueAt(i,17)!=null && !Tablaman.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablaman.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelofonseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablafon.getValueAt(i,4)!=null && !Tablafon.getValueAt(i,4).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablafon.getValueAt(i,4));
                            }

                            if(Tablafon.getValueAt(i,9)!=null && !Tablafon.getValueAt(i,9).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablafon.getValueAt(i,9));
                            }
                            if(Tablafon.getValueAt(i,10)!=null && !Tablafon.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablafon.getValueAt(i,10));
                            }

                            if(Tablafon.getValueAt(i,15)!=null && !Tablafon.getValueAt(i,15).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablafon.getValueAt(i,15));
                            }
                            if(Tablafon.getValueAt(i,16)!=null && !Tablafon.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablafon.getValueAt(i,16));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelofleseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablafle.getValueAt(i,5)!=null && !Tablafle.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablafle.getValueAt(i,5));
                            }

                            if(Tablafle.getValueAt(i,10)!=null && !Tablafle.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablafle.getValueAt(i,10));
                            }
                            if(Tablafle.getValueAt(i,11)!=null && !Tablafle.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablafle.getValueAt(i,11));
                            }

                            if(Tablafle.getValueAt(i,16)!=null && !Tablafle.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablafle.getValueAt(i,16));
                            }
                            if(Tablafle.getValueAt(i,17)!=null && !Tablafle.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablafle.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelotroseleccion.addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e){
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()){

                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    int cuenta=0,sumacantidadpedidas=0,sumaprogramcantidad=0,sumaprogramkg=0,sumaprodcantidad=0,sumaprodkg=0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            cuenta++;

                            if(Tablatro.getValueAt(i,5)!=null && !Tablatro.getValueAt(i,5).equals("")){ //suma los metros lineales
                                    sumacantidadpedidas+=Integer.parseInt(""+Tablatro.getValueAt(i,5));
                            }

                            if(Tablatro.getValueAt(i,10)!=null && !Tablatro.getValueAt(i,10).equals("")){ //suma los metros lineales
                                    sumaprogramcantidad+=Integer.parseInt(""+Tablatro.getValueAt(i,10));
                            }
                            if(Tablatro.getValueAt(i,11)!=null && !Tablatro.getValueAt(i,11).equals("")){ //suma los metros lineales
                                    sumaprogramkg+=Integer.parseInt(""+Tablatro.getValueAt(i,11));
                            }

                            if(Tablatro.getValueAt(i,16)!=null && !Tablatro.getValueAt(i,16).equals("")){ //suma los metros lineales
                                    sumaprodcantidad+=Integer.parseInt(""+Tablatro.getValueAt(i,16));
                            }
                            if(Tablatro.getValueAt(i,17)!=null && !Tablatro.getValueAt(i,17).equals("")){ //suma los metros lineales
                                    sumaprodkg+=Integer.parseInt(""+Tablatro.getValueAt(i,17));
                            }
                        }
                    }
                    //noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Piezas x Program: "+estandarentero.format(sumacantidadpedidas-sumaprogramcantidad)+"        Piezas x Prod: "+estandarentero.format(sumacantidadpedidas-sumaprodcantidad)+"        Program.( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Prod.( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                    noregistros.setText("Cambios: "+estandarentero.format(cuenta)+"        Piezas Pedidos: "+estandarentero.format(sumacantidadpedidas)+"        Programado( Piezas: "+estandarentero.format(sumaprogramcantidad)+"    KG: "+estandarentero.format(sumaprogramkg)+" )"+"        Fabricado( Piezas: "+estandarentero.format(sumaprodcantidad)+"    KG: "+estandarentero.format(sumaprodkg)+" )");
                }
            }
        });
        modelocorr.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla

                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 0);
                        if(!pc.equals("null") && !pc.equals("")){
                            String senSQLmov="UPDATE programa_corr SET arreglo='"+valnum+"' WHERE id_programa_corr='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 14) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 0);
                        if(!pc.equals("null") && !pc.equals("")){
                            String senSQLmov="UPDATE programa_corr SET velocidad='"+valnum+"' WHERE id_programa_corr='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });

        modelotcy.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='TCY');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='TCY');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        
        modelolinealtcy.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='PLTCY');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='PLTCY');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelosys.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='MACARB');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='MACARB');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelolinealsys.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='PLMACARB');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='PLMACARB');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelocom.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='COM');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='COM');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modeloeng.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='ENG');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='ENG');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelomarv.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='MARVI');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='MARVI');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelosuap.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='SUAP');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='SUAP');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelodiec.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='DIEC');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='DIEC');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modeloray.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='RAY');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='RAY');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelopdo.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='PDO');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='PDO');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelodes.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='DES');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='DES');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modeloman.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='MAN');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='MAN');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelofon.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='FON');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='FON');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelofle.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='FLE');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='FLE');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        modelotro.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET id_numero='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 12) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET arreglo='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET arreglo='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='TRO');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 13) {
                        String valnum=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET velocidad='"+valnum+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            senSQLmov="UPDATE articulos_maquinas SET velocidad='"+valnum+"' WHERE (clavearticulo='"+model.getValueAt(f, 6)+"' AND clave='TRO');";
                            conexion.modificamov_p(senSQLmov,conn,valor_privilegio);
                            actualiza();
                        }
                    }
                    if (c == 4) {
                        String valimp=""+model.getValueAt(f, c);
                        String pc=""+model.getValueAt(f, 19);
                        if(!pc.equals("null")){
                            String senSQLmov="UPDATE programa_conversion SET imprimir='"+valimp+"' WHERE id_programa_conversion='"+pc+"';";
                            conexion.modificasin(senSQLmov,conn);
                        }
                    }
                }catch(Exception e){  JOptionPane.showMessageDialog(null, "NO SE PUEDE MODIFICAR PARA IMPRIMIR"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);                }
          }
        });
        
    }

    final public void salir(){
        if(conn!=null){
            conn=null;
        }
        dispose();
        this.setVisible(false);
    }
    final public void datos_privilegios(){
        valor_privilegio=conexion.obtener_privilegios(conn,"CORRFLEX");
    }
    final public void datos_parametros(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM parametros WHERE id_parametros='1'";
            rs0=conexion.consulta(senSQL,conn);
            if(rs0.next()){
                tinta_gramos_m2=rs0.getDouble("tinta_gramos_m2");
                tinta_modulo=rs0.getDouble("tinta_modulo");
                tinta_porcentaje=rs0.getDouble("tinta_porcentaje");
            }
            if(rs0!=null) {     rs0.close();   }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    final public void ajusteTabladatos() {
        //columnas corrugadora
        Tablacorr.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablacorr.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablacorr.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablacorr.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablacorr.getColumnModel().getColumn(4).setPreferredWidth(90);
        Tablacorr.getColumnModel().getColumn(5).setPreferredWidth(100);
        Tablacorr.getColumnModel().getColumn(6).setPreferredWidth(240);
        Tablacorr.getColumnModel().getColumn(7).setPreferredWidth(60);
        Tablacorr.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablacorr.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablacorr.getColumnModel().getColumn(10).setPreferredWidth(70);
        Tablacorr.getColumnModel().getColumn(11).setPreferredWidth(85);
        Tablacorr.getColumnModel().getColumn(12).setPreferredWidth(70);
        Tablacorr.getColumnModel().getColumn(13).setPreferredWidth(60);
        Tablacorr.getColumnModel().getColumn(14).setPreferredWidth(40);
        Tablacorr.getColumnModel().getColumn(15).setPreferredWidth(50);
        Tablacorr.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablacorr.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablacorr.getColumnModel().getColumn(18).setPreferredWidth(90);
        Tablacorr.getColumnModel().getColumn(19).setPreferredWidth(70);
        Tablacorr.getColumnModel().getColumn(20).setPreferredWidth(200);
        Tablacorr.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer_pla_corr());
        Tablacorr.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablacorr.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablacorr.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablacorr.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablacorr.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer());
        Tablacorr.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablacorr.getColumnModel().getColumn(15).setCellRenderer(new DoubleRenderer());

        //columnas tcy
        Tablatcy.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablatcy.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablatcy.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablatcy.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablatcy.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablatcy.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablatcy.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablatcy.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablatcy.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablatcy.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablatcy.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablatcy.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablatcy.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablatcy.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablatcy.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablatcy.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablatcy.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablatcy.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablatcy.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablatcy.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablatcy.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablatcy.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablatcy.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablatcy.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablatcy.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablatcy.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablatcy.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablatcy.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //columnas lineal tcy
        Tablalinealtcy.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablalinealtcy.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablalinealtcy.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablalinealtcy.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablalinealtcy.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablalinealtcy.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablalinealtcy.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablalinealtcy.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablalinealtcy.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablalinealtcy.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablalinealtcy.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablalinealtcy.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablalinealtcy.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablalinealtcy.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablalinealtcy.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablalinealtcy.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablalinealtcy.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablalinealtcy.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablalinealtcy.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablalinealtcy.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablalinealtcy.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablalinealtcy.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablalinealtcy.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablalinealtcy.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablalinealtcy.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablalinealtcy.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablalinealtcy.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablalinealtcy.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());

        //columnas MACARBOX
        Tablasys.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablasys.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablasys.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablasys.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablasys.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablasys.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablasys.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablasys.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablasys.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablasys.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablasys.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablasys.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablasys.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablasys.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablasys.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablasys.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablasys.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablasys.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablasys.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablasys.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablasys.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablasys.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablasys.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablasys.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablasys.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablasys.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablasys.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablasys.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //columnas LINEAL MACARBOX
        Tablalinealsys.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablalinealsys.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablalinealsys.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablalinealsys.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablalinealsys.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablalinealsys.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablalinealsys.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablalinealsys.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablalinealsys.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablalinealsys.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablalinealsys.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablalinealsys.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablalinealsys.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablalinealsys.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablalinealsys.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablalinealsys.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablalinealsys.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablalinealsys.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablalinealsys.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablalinealsys.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablalinealsys.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablalinealsys.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablalinealsys.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablalinealsys.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablalinealsys.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablalinealsys.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablalinealsys.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablalinealsys.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //columnas COMET
        Tablacom.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablacom.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablacom.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablacom.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablacom.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablacom.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablacom.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablacom.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablacom.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablacom.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablacom.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablacom.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablacom.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablacom.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablacom.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablacom.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablacom.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablacom.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablacom.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablacom.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablacom.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablacom.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablacom.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablacom.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablacom.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablacom.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablacom.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablacom.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //columnas ENGRAPADORA
        Tablaeng.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablaeng.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablaeng.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablaeng.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablaeng.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablaeng.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablaeng.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablaeng.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablaeng.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablaeng.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablaeng.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablaeng.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablaeng.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablaeng.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablaeng.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablaeng.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablaeng.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablaeng.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablaeng.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablaeng.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablaeng.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablaeng.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablaeng.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablaeng.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablaeng.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablaeng.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablaeng.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablaeng.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //COLUMNAS MARVI
        Tablamarv.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablamarv.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablamarv.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablamarv.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablamarv.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablamarv.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablamarv.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablamarv.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablamarv.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablamarv.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablamarv.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablamarv.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablamarv.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablamarv.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablamarv.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablamarv.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablamarv.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablamarv.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablamarv.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablamarv.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablamarv.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablamarv.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablamarv.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablamarv.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablamarv.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablamarv.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablamarv.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablamarv.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //COLUMNAS SUAJE PLANO
        Tablasuap.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablasuap.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablasuap.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablasuap.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablasuap.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablasuap.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablasuap.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablasuap.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablasuap.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablasuap.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablasuap.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablasuap.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablasuap.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablasuap.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablasuap.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablasuap.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablasuap.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablasuap.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablasuap.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablasuap.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablasuap.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablasuap.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablasuap.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablasuap.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablasuap.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablasuap.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablasuap.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablasuap.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //COLUMNAS DIE CUTTER
        Tabladiec.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladiec.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tabladiec.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tabladiec.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tabladiec.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tabladiec.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tabladiec.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tabladiec.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tabladiec.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tabladiec.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tabladiec.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tabladiec.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tabladiec.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tabladiec.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tabladiec.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tabladiec.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tabladiec.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tabladiec.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tabladiec.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tabladiec.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tabladiec.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tabladiec.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tabladiec.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tabladiec.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tabladiec.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tabladiec.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tabladiec.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tabladiec.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //COLUMNAS RAYADORA
        Tablaray.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablaray.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablaray.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablaray.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablaray.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablaray.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablaray.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablaray.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablaray.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablaray.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablaray.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablaray.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablaray.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablaray.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablaray.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablaray.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablaray.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablaray.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablaray.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablaray.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablaray.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablaray.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablaray.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablaray.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablaray.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablaray.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablaray.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablaray.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //COLUMNAS EMPLAYADORA
        Tablapdo.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablapdo.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablapdo.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablapdo.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablapdo.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablapdo.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablapdo.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablapdo.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablapdo.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablapdo.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablapdo.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablapdo.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablapdo.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablapdo.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablapdo.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablapdo.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablapdo.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablapdo.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablapdo.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablapdo.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablapdo.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablapdo.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablapdo.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablapdo.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablapdo.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablapdo.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablapdo.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablapdo.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //COLUMNAS EMPLAYADORA
        //COLUMNAS DESMERMADO
        Tablades.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablades.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablades.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablades.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablades.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablades.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablades.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablades.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablades.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablades.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablades.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablades.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablades.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablades.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablades.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablades.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablades.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablades.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablades.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablades.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablades.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablades.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablades.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablades.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablades.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablades.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablades.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablades.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //COLUMNAS MANUAL
        Tablaman.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablaman.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablaman.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablaman.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablaman.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablaman.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablaman.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablaman.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablaman.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablaman.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablaman.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablaman.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablaman.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablaman.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablaman.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablaman.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablaman.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablaman.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablaman.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablaman.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablaman.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablaman.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablaman.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablaman.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablaman.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablaman.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablaman.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablaman.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
         //COLUMNAS FONDEADO
        Tablafon.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablafon.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablafon.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablafon.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablafon.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablafon.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablafon.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablafon.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablafon.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablafon.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablafon.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablafon.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablafon.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablafon.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablafon.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablafon.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablafon.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablafon.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablafon.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablafon.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablafon.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablafon.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablafon.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablafon.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablafon.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablafon.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablafon.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablafon.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //COLUMNAS FLEJE
        Tablafle.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablafle.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablafle.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablafle.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablafle.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablafle.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablafle.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablafle.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablafle.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablafle.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablafle.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablafle.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablafle.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablafle.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablafle.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablafle.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablafle.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablafle.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablafle.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablafle.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablafle.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablafle.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablafle.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablafle.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablafle.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablafle.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablafle.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablafle.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());
        //COLUMNAS TROQUEL
        Tablatro.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablatro.getColumnModel().getColumn(1).setPreferredWidth(75);
        Tablatro.getColumnModel().getColumn(2).setPreferredWidth(100);
        Tablatro.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tablatro.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tablatro.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tablatro.getColumnModel().getColumn(6).setPreferredWidth(100);
        Tablatro.getColumnModel().getColumn(7).setPreferredWidth(240);
        Tablatro.getColumnModel().getColumn(8).setPreferredWidth(60);
        Tablatro.getColumnModel().getColumn(9).setPreferredWidth(85);
        Tablatro.getColumnModel().getColumn(10).setPreferredWidth(85);
        Tablatro.getColumnModel().getColumn(11).setPreferredWidth(70);
        Tablatro.getColumnModel().getColumn(12).setPreferredWidth(60);
        Tablatro.getColumnModel().getColumn(13).setPreferredWidth(40);
        Tablatro.getColumnModel().getColumn(14).setPreferredWidth(50);
        Tablatro.getColumnModel().getColumn(15).setPreferredWidth(85);
        Tablatro.getColumnModel().getColumn(16).setPreferredWidth(85);
        Tablatro.getColumnModel().getColumn(17).setPreferredWidth(70);
        Tablatro.getColumnModel().getColumn(18).setPreferredWidth(200);
        Tablatro.getColumnModel().getColumn(19).setPreferredWidth(10);
        Tablatro.getColumnModel().getColumn(5).setCellRenderer(new DoubleRenderer_pla_con());
        Tablatro.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderer());
        Tablatro.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderer());
        Tablatro.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderer());
        Tablatro.getColumnModel().getColumn(3).setCellRenderer(new DoubleRenderer());
        Tablatro.getColumnModel().getColumn(6).setCellRenderer(new DoubleRenderer());
        Tablatro.getColumnModel().getColumn(8).setCellRenderer(new DoubleRenderer());
        Tablatro.getColumnModel().getColumn(14).setCellRenderer(new DoubleRenderer());

    }
//ajustes para la tabla de datos e corrugadora
    final public void limpiatablacorr(){
        modelocorr.setNumRows(0);
    }
    final public class DoubleRenderer extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
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
    final public class DoubleRenderer_pla_corr extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                String prog_corr = ""+Tablacorr.getValueAt(row, 0);
                rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                if(text.equals("null") || text.equals(""))
                    text="";
                if(prog_corr.equals("null") || prog_corr.equals("")) //solo verifica los que no estan porgramador
                    prog_corr="";
                
                if(!text.equals("") && prog_corr.equals("")){
                    int cantidad_pedida=Integer.parseInt(text);
                    int cantidad_prog=Integer.parseInt(""+Tablacorr.getValueAt(row, 11));
                    int cantidad_prod=Integer.parseInt(""+Tablacorr.getValueAt(row, 18));
                    if(cantidad_prog>=cantidad_pedida)
                        rend.setBackground(new Color(135,206,235));
                    if(cantidad_prod>=cantidad_pedida)
                        rend.setBackground(new Color(124,205,124));
                }
                rend.setText( text );
                return rend;
            }
    }
    final public class DoubleRenderer_pla_con extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                String prog_con = ""+table.getValueAt(row, 0);
                rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                if(text.equals("null") || text.equals(""))
                    text="";
                if(prog_con.equals("null") || prog_con.equals("")) //solo verifica los que no estan porgramados
                    prog_con="";

                if(!text.equals("") && prog_con.equals("")){
                    int cantidad_pedida=Integer.parseInt(text);
                    int cantidad_prog=Integer.parseInt(""+table.getValueAt(row, 10));
                    int cantidad_prod=Integer.parseInt(""+table.getValueAt(row, 16));
                    if(cantidad_prog>=cantidad_pedida)
                        rend.setBackground(new Color(135,206,235));
                    if(cantidad_prod>=cantidad_pedida)
                        rend.setBackground(new Color(124,205,124));
                }
                rend.setText( text );
                return rend;
            }
    }
    final public void datoscorr(){
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        //obtiene hora inicial de la corrugadora
        String cadena_hora_inicial="07:00";
        rs0=null;
        try{
            String senSQL="SELECT * FROM maquinas WHERE clave='CORR'";
            rs0=conexion.consulta(senSQL,conn);
            if(rs0.next()){
                cadena_hora_inicial=rs0.getString("hora_inicial");
            }
            if(rs0!=null) {  rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agreagas los datos que ya se encuentran programados
        rs0=null;
        try{
            String senSQL="SELECT programa_corr.id_programa_corr,programa_corr.arreglo,programa_corr.anchoutil,programa_corr.velocidad,programa_corr.fechaproduccion as fechaprogram,programa_corr.ml as programml,programa_corr.kg as programkg,(programa_corr.claveresistencia) as resistenciaprograma,programa_corr_prod.op,programa_corr_prod.clavearticulo,programa_corr_prod.programcortes,programa_corr_prod.programlaminas,programa_corr_prod.prodcantidad,articulos.claveresistencia as cr,articulos.articulo,articulos.largo,articulos.ancho,articulos.scores,ops.maquila,ops.fechaentrega,(ops.cantidad/articulos.piezas) as laminaspedidas,clientes.nombre,prodcorr.fechaprod,COALESCE(prodcorr.prodml,0) as prodml,COALESCE(prodcorr.prodkg,0) as prodkg,COALESCE(prodcorr.prodm2,0) as prodm2 FROM ( ((programa_corr LEFT JOIN (SELECT programa_corr_captura.id_programa_corr,max(fechareal) as fechaprod,sum(prodml) as prodml,sum(prodkg) as prodkg,sum(prodm2) as prodm2 FROM programa_corr_captura WHERE programa_corr_captura.estatus<>'Can' GROUP BY programa_corr_captura.id_programa_corr) as prodcorr ON programa_corr.id_programa_corr=prodcorr.id_programa_corr) INNER JOIN (SELECT programa_corr_detalle.id_programa_corr_detalle,programa_corr_detalle.op,programa_corr_detalle.clavearticulo,programa_corr_detalle.cortes as programcortes,programa_corr_detalle.laminas as programlaminas,programa_corr_detalle.id_programa_corr,programcorr.fechaprod,programcorr.prodml,programcorr.prodcantidad,programcorr.prodcantidadpiezas,programcorr.prodcantkgpiezas FROM programa_corr_detalle LEFT JOIN (SELECT conversion_captura.id_programa_corr,conversion_captura.clavearticulo,max(conversion_captura.op)as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='CORR' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_corr,conversion_captura.clavearticulo) as programcorr ON (programa_corr_detalle.clavearticulo=programcorr.clavearticulo AND programa_corr_detalle.id_programa_corr=programcorr.id_programa_corr)) as programa_corr_prod ON programa_corr.id_programa_corr=programa_corr_prod.id_programa_corr) LEFT JOIN ops ON programa_corr_prod.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_corr_prod.clavearticulo=articulos.clavearticulo WHERE programa_corr.estatus='Act' ORDER BY programa_corr.fechaproduccion,programa_corr.imprimir,programa_corr_prod.id_programa_corr_detalle;";
            rs0=conexion.consulta(senSQL,conn);
            String bandera="";
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);

                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                String noprograma=rs0.getString("id_programa_corr");
                if(bandera.equals(noprograma)){
                    Object datos[]={"-",rs0.getString("op"),color+sfe,rs0.getString("maquila"),rs0.getInt("laminaspedidas"),rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("cr"),null,null,null,rs0.getInt("programlaminas"),null,null,null,"",null,null,rs0.getInt("prodcantidad"),null,rs0.getString("nombre")};
                    modelocorr.addRow(datos);
                }else{
                    int cantprogram=rs0.getInt("programml");
                    int cantprod=rs0.getInt("prodml");
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={noprograma,rs0.getString("op"),color+sfe,rs0.getString("maquila"),rs0.getInt("laminaspedidas"),rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("cr"),rs0.getDouble("anchoutil"),fecha_programado,cantprogram,rs0.getInt("programlaminas"),rs0.getInt("programkg"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantidad"),rs0.getInt("prodkg"),rs0.getString("nombre")};
                    modelocorr.addRow(datos);
                    bandera=noprograma;
                }
            }
            if(rs0!=null) {   rs0.close();  }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        
        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelocorr.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.ancho,articulos.kg,articulos.m2,clientes.nombre,prodcorr.fechaprod,COALESCE(prodcorr.prodml,0) as prodml,COALESCE(prodcorr.prodcantidad,0) as prodcantidad,COALESCE(prodcorr.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prodcorr.prodcantkgpiezas,0) as prodcantkgpiezas,programcorr.fechaprogram,COALESCE(programcorr.programcantidad,0) as programcantidad,COALESCE(programcorr.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(programcorr.programcantkgpiezas,0) as programcantkgpiezas,articulos_maquinas.clave FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='CORR' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prodcorr ON ops.op=prodcorr.op ) LEFT JOIN (SELECT programa_corr_detalle.op,max(programa_corr.fechaproduccion) as fechaprogram,sum(programa_corr_detalle.laminas) as programcantidad,sum(articulos.piezas*programa_corr_detalle.laminas) as programcantidadpiezas,sum(articulos.piezas*programa_corr_detalle.laminas*articulos.kg) as programcantkgpiezas  FROM (programa_corr INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo  WHERE (programa_corr.estatus<>'Can' AND programa_corr.estatus<>'Pen') GROUP BY programa_corr_detalle.op) as programcorr ON ops.op=programcorr.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='CORR' ) ORDER BY ops.fechaentrega,ops.op;";
            rs0=conexion.consulta(senSQL,conn);
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
                //if(laminasprod<laminaspedidas){
                    Object datos[]={"",nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),laminaspedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDouble("ancho"),rs0.getDate("fechaprogram"),null,laminasprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),null,laminasprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre")};
                    modelocorr.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablatcy(){
        modelotcy.setNumRows(0);
    }
    final public void datostcy(){
        String clavemaquina="TCY";
        String sonlaminas="Si";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));
                    
                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelotcy.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelotcy.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelotcy.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablalinealtcy(){
        modelolinealtcy.setNumRows(0);
    }
    final public void datoslinealtcy(){
        String clavemaquina="PLTCY";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelolinealtcy.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelolinealtcy.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelolinealtcy.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablasys(){
        modelosys.setNumRows(0);
    }
    final public void datossys(){
        String clavemaquina="MACARB";
        String sonlaminas="Si";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelosys.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelosys.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelosys.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablalinealsys(){
        modelolinealsys.setNumRows(0);
    }
    final public void datoslinealsys(){
        String clavemaquina="PLMACARB";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelolinealsys.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelolinealsys.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelolinealsys.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablacom(){
        modelocom.setNumRows(0);
    }
    final public void datoscom(){
        String clavemaquina="COM";
        String sonlaminas="Si";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelocom.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelocom.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelocom.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablaeng(){
        modeloeng.setNumRows(0);
    }
    final public void datoseng(){
        String clavemaquina="ENG";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modeloeng.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modeloeng.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modeloeng.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablamarv(){
        modelomarv.setNumRows(0);
    }
    final public void datosmarv(){
        String clavemaquina="MARVI";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelomarv.addRow(datos);
                }
            }
            if(rs0!=null) { rs0.close();           }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelomarv.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelomarv.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablasuap(){
        modelosuap.setNumRows(0);
    }
    final public void datossuap(){
        String clavemaquina="SUAP";
        String sonlaminas="Si";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelosuap.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelosuap.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelosuap.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatabladiec(){
        modelodiec.setNumRows(0);
    }
    final public void datosdiec(){
        String clavemaquina="DIEC";
        String sonlaminas="Si";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelodiec.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelodiec.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelodiec.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablaray(){
        modeloray.setNumRows(0);
    }
    final public void datosray(){
        String clavemaquina="RAY";
        String sonlaminas="Si";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modeloray.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modeloray.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modeloray.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablapdo(){
        modelopdo.setNumRows(0);
    }
    final public void datospdo(){
        String clavemaquina="PDO";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelopdo.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelopdo.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelopdo.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablades(){
        modelodes.setNumRows(0);
    }
    final public void datosdes(){
        String clavemaquina="DES";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelodes.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelodes.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelodes.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablaman(){
        modeloman.setNumRows(0);
    }
    final public void datosman(){
        String clavemaquina="MAN";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=""+rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modeloman.addRow(datos);
                }
            }
            if(rs0!=null) { rs0.close();   }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modeloman.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modeloman.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablafon(){
        modelofon.setNumRows(0);
    }
    final public void datosfon(){
        String clavemaquina="FON";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelofon.addRow(datos);
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelofon.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelofon.addRow(datos);
                //}
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablafle(){
        modelofle.setNumRows(0);
    }
    final public void datosfle(){
        String clavemaquina="FLE";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelofle.addRow(datos);
                }
            }
            if(rs0!=null) {  rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelofle.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelofle.addRow(datos);
                //}
            }
            if(rs0!=null) {     rs0.close();      }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void limpiatablatro(){
        modelotro.setNumRows(0);
    }
    final public void datostro(){
        String clavemaquina="TRO";
        String sonlaminas="No";
        Double totalminutos=0.0;//minutos aculuados por dia
        String fecha_cadena_igual="";

        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad,maquinas.hora_inicial FROM ((((programa_conversion LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave) LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                String sfe="";
                String color="<html><font color=green><b>";
                if(!(""+fechaentrega).equals("null")){
                    sfe=fechamascorta.format(fechaentrega);
                    Calendar calendarentrega = new GregorianCalendar();
                    calendarentrega.setTime(fechaentrega);
                    if(calendarentrega.before(calendariniciosem)){
                        color="<html><font color=#DC143C><b>"; }
                    if(calendarentrega.after(calendarfinsem)){
                        color="<html><font color=#0080FF><b>"; }
                }
                //verifica sin son golpes o piezas las que pide el proceso
                //verificar si es laminas o piezas
                String opprogram=rs0.getString("op");
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                if(cantprod<cantpedidas || opprogram.equals("STOCK")){
                    //calcula la hora aproximada de termino
                    int min_arreglo=rs0.getInt("arreglo");
                    int velocidad_partida=rs0.getInt("velocidad");
                    Double min_partida=Double.parseDouble(""+(cantprogram-cantprod))/velocidad_partida;
                    //verifica que sea el mismo dia en caso de que no pone totalminutos en 0
                    Date fecha_programado=rs0.getDate("fechaprogram");
                    if(!fecha_cadena_igual.equals(fechamascorta.format(fecha_programado))){
                        totalminutos=0.0;//si es otro dia lo vuelve a inicial con las 07:00 por default
                        fecha_cadena_igual=fechamascorta.format(fecha_programado);
                    }
                    totalminutos+=min_arreglo+min_partida;
                    String cadena_hora_inicial=rs0.getString("hora_inicial");
                    int horas_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(0)+""+cadena_hora_inicial.charAt(1));
                    int minutos_hora_inicial=Integer.parseInt(cadena_hora_inicial.charAt(3)+""+cadena_hora_inicial.charAt(4));
                    Calendar fecha_hora_hoy = Calendar.getInstance(); //calendar hoy
                    fecha_hora_hoy.set(fecha_hora_hoy.get(Calendar.YEAR),fecha_hora_hoy.get(Calendar.MONTH),fecha_hora_hoy.get(Calendar.DATE),horas_hora_inicial,minutos_hora_inicial,0);
                    fecha_hora_hoy.add(Calendar.MINUTE, (int)(totalminutos/1));
                    String cadena_nueva_hora=horasminutos.format(fecha_hora_hoy.get(Calendar.HOUR_OF_DAY))+":"+horasminutos.format(fecha_hora_hoy.get(Calendar.MINUTE));

                    Object datos[]={rs0.getInt("id_numero"),opprogram,color+sfe,rs0.getString("maquila"),rs0.getBoolean("imprimir"),cantpedidas,rs0.getString("clavearticulo"),rs0.getString("articulo"),rs0.getString("claveresistencia"),fecha_programado,cantprogram,rs0.getInt("programcantkgpiezas"),min_arreglo,velocidad_partida,cadena_nueva_hora,rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),rs0.getString("nombre"),rs0.getInt("id_programa_conversion")};
                    modelotro.addRow(datos);
                }
            }
            if(rs0!=null) {  rs0.close();    }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        //agrega un espacio vacio en tre lo programado y lo pendiente
        Object datosvacio[]={null};
        modelotro.addRow(datosvacio);

        //ingresas las ordenes de produccion faltantes por programar
        rs0=null;
        try{
            String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas,0) as programcantkgpiezas FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaquina+"' AND programa_conversion.estatus<>'Can' AND programa_conversion.estatus<>'Pen') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='"+clavemaquina+"') ORDER BY ops.fechaentrega,ops.op;";

            rs0=conexion.consulta(senSQL,conn);
            while(rs0.next()){
                Date fechaentrega=rs0.getDate("fechaentrega");
                Calendar calendarentrega = new GregorianCalendar();
                calendarentrega.setTime(fechaentrega);
                String color="<html><font color=green>";
                if(calendarentrega.before(calendariniciosem)){
                    color="<html><font color=#DC143C>"; }
                if(calendarentrega.after(calendarfinsem)){
                    color="<html><font color=#0080FF>"; }

                //verifica sin son golpes o piezas las que pide el proceso
                int cantpedidas=rs0.getInt("cantidad");
                if(sonlaminas.equals("Si")){
                    cantpedidas=rs0.getInt("laminaspedidas");
                }
                int cantprogram=rs0.getInt("programcantidad");
                int cantprod=rs0.getInt("prodcantidad");
                //if(cantprod<cantpedidas){
                    Object datos[]={null,nocolor+rs0.getString("op"),color+fechamascorta.format(fechaentrega),nocolor+rs0.getString("maquila"),false,cantpedidas,nocolor+rs0.getString("clavearticulo"),nocolor+rs0.getString("articulo"),nocolor+rs0.getString("claveresistencia"),rs0.getDate("fechaprogram"),cantprogram,rs0.getInt("programcantkgpiezas"),null,null,"",rs0.getDate("fechaprod"),cantprod,rs0.getInt("prodcantkgpiezas"),nocolor+rs0.getString("nombre"),null};
                    modelotro.addRow(datos);
                //}
            }
            if(rs0!=null) {     rs0.close();      }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }
    final public void verop(String clavemodifica){
        if(!clavemodifica.equals("")){
            datos_ops = new datos_ops(null,true,conn,clavemodifica);
            datos_ops.setLocationRelativeTo(this);
            datos_ops.setVisible(true);
            datos_ops=null;
        } 
    }
    final public void verarticulo(String clavemodifica){
        if(!clavemodifica.equals("")){
            datos_articulos = new datos_articulos(null,true,conn,clavemodifica,"ver",valor_privilegio);
            datos_articulos.setLocationRelativeTo(this);
            datos_articulos.setVisible(true);
            datos_articulos=null;
        }
    }
    final public void verarticulo_espec(String clavemodifica){
        if(!clavemodifica.equals("")){
            File rutaarchivo=new File(conf.getProperty("Rutaplanos")+"/"+clavemodifica+".jpg");
            if (rutaarchivo.exists()) { //verifica que la ruta exista
                try {
                  Desktop.getDesktop().open(rutaarchivo);
                } catch (Exception ex) { JOptionPane.showMessageDialog(null,"NO SE PUEDE ABRIR EL ARCHIVO\n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE); }
            }else{
                JOptionPane.showMessageDialog(this, "EL ARCHIVO NO EXISTE", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    final public void programarop(String clavemodifica,String cmaq,String programaconversion){
        if(!clavemodifica.equals("")){
            datos_programas_conversion = new datos_programas_conversion(null,true,conn,clavemodifica,cmaq,programaconversion,"",0,0,"Act",valor_privilegio);
            datos_programas_conversion.setLocationRelativeTo(this);
            datos_programas_conversion.setVisible(true);
            datos_programas_conversion=null;
        }
        actualiza();
    }
    final public void actualiza(){
        limpiatablacorr();
        datoscorr();
        limpiatablatcy();
        datostcy();
        limpiatablalinealtcy();
        datoslinealtcy();
        limpiatablasys();
        datossys();
        limpiatablalinealsys();
        datoslinealsys();
        limpiatablacom();
        datoscom();
        limpiatablaeng();
        datoseng();
        limpiatablamarv();
        datosmarv();
        limpiatablasuap();
        datossuap();
        limpiatabladiec();
        datosdiec();
        limpiatablaray();
        datosray();
        limpiatablapdo();
        datospdo();
        limpiatablades();
        datosdes();
        limpiatablaman();
        datosman();
        limpiatablafon();
        datosfon();
        limpiatablafle();
        datosfle();
        limpiatablatro();
        datostro();
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menupopcorr = new javax.swing.JPopupMenu();
        nuevo_corr = new javax.swing.JMenuItem();
        detalleopcorr = new javax.swing.JMenuItem();
        detalleartcorr = new javax.swing.JMenuItem();
        especartcorr = new javax.swing.JMenuItem();
        combinaopcorr = new javax.swing.JMenuItem();
        combinaartcorr = new javax.swing.JMenuItem();
        menupoptcy = new javax.swing.JPopupMenu();
        detalleoptcy = new javax.swing.JMenuItem();
        detallearttcy = new javax.swing.JMenuItem();
        especarttcy = new javax.swing.JMenuItem();
        programoptcy = new javax.swing.JMenuItem();
        cambiarprogtcy = new javax.swing.JMenuItem();
        terminaprogtcy = new javax.swing.JMenuItem();
        eliminarprogtcy = new javax.swing.JMenuItem();
        sumadiatcy = new javax.swing.JMenuItem();
        restadiatcy = new javax.swing.JMenuItem();
        menupoplinealtcy = new javax.swing.JPopupMenu();
        detalleoplinealtcy = new javax.swing.JMenuItem();
        detalleartlinealtcy = new javax.swing.JMenuItem();
        especartlinealtcy = new javax.swing.JMenuItem();
        programoplinealtcy = new javax.swing.JMenuItem();
        cambiarproglinealtcy = new javax.swing.JMenuItem();
        terminaproglinealtcy = new javax.swing.JMenuItem();
        eliminarproglinealtcy = new javax.swing.JMenuItem();
        sumadialinealtcy = new javax.swing.JMenuItem();
        restadialinealtcy = new javax.swing.JMenuItem();
        menupopsys = new javax.swing.JPopupMenu();
        detalleopsys = new javax.swing.JMenuItem();
        detalleartsys = new javax.swing.JMenuItem();
        especartsys = new javax.swing.JMenuItem();
        programopsys = new javax.swing.JMenuItem();
        cambiarprogsys = new javax.swing.JMenuItem();
        terminaprogsys = new javax.swing.JMenuItem();
        eliminarprogsys = new javax.swing.JMenuItem();
        sumadiasys = new javax.swing.JMenuItem();
        restadiasys = new javax.swing.JMenuItem();
        menupoplinealsys = new javax.swing.JPopupMenu();
        detalleoplinealsys = new javax.swing.JMenuItem();
        detalleartlinealsys = new javax.swing.JMenuItem();
        especartlinealsys = new javax.swing.JMenuItem();
        programoplinealsys = new javax.swing.JMenuItem();
        cambiarproglinealsys = new javax.swing.JMenuItem();
        terminaproglinealsys = new javax.swing.JMenuItem();
        eliminarproglinealsys = new javax.swing.JMenuItem();
        sumadialinealsys = new javax.swing.JMenuItem();
        restadialinealsys = new javax.swing.JMenuItem();
        menupopcom = new javax.swing.JPopupMenu();
        detalleopcom = new javax.swing.JMenuItem();
        detalleartcom = new javax.swing.JMenuItem();
        especartcom = new javax.swing.JMenuItem();
        programopcom = new javax.swing.JMenuItem();
        cambiarprogcom = new javax.swing.JMenuItem();
        terminaprogcom = new javax.swing.JMenuItem();
        eliminarprogcom = new javax.swing.JMenuItem();
        sumadiacom = new javax.swing.JMenuItem();
        restadiacom = new javax.swing.JMenuItem();
        menupopeng = new javax.swing.JPopupMenu();
        detalleopeng = new javax.swing.JMenuItem();
        detallearteng = new javax.swing.JMenuItem();
        especarteng = new javax.swing.JMenuItem();
        programopeng = new javax.swing.JMenuItem();
        cambiarprogeng = new javax.swing.JMenuItem();
        terminaprogeng = new javax.swing.JMenuItem();
        eliminarprogeng = new javax.swing.JMenuItem();
        sumadiaeng = new javax.swing.JMenuItem();
        restadiaeng = new javax.swing.JMenuItem();
        menupopmarv = new javax.swing.JPopupMenu();
        detalleopmarv = new javax.swing.JMenuItem();
        detalleartmarv = new javax.swing.JMenuItem();
        especartmarv = new javax.swing.JMenuItem();
        programopmarv = new javax.swing.JMenuItem();
        cambiarprogmarv = new javax.swing.JMenuItem();
        terminaprogmarv = new javax.swing.JMenuItem();
        eliminarprogmarv = new javax.swing.JMenuItem();
        sumadiamarv = new javax.swing.JMenuItem();
        restadiamarv = new javax.swing.JMenuItem();
        menupopsuap = new javax.swing.JPopupMenu();
        detalleopsuap = new javax.swing.JMenuItem();
        detalleartsuap = new javax.swing.JMenuItem();
        especartsuap = new javax.swing.JMenuItem();
        programopsuap = new javax.swing.JMenuItem();
        cambiarprogsuap = new javax.swing.JMenuItem();
        terminaprogsuap = new javax.swing.JMenuItem();
        eliminarprogsuap = new javax.swing.JMenuItem();
        sumadiasuap = new javax.swing.JMenuItem();
        restadiasuap = new javax.swing.JMenuItem();
        menupopdiec = new javax.swing.JPopupMenu();
        detalleopdiec = new javax.swing.JMenuItem();
        detalleartdiec = new javax.swing.JMenuItem();
        especartdiec = new javax.swing.JMenuItem();
        programopdiec = new javax.swing.JMenuItem();
        cambiarprogdiec = new javax.swing.JMenuItem();
        terminaprogdiec = new javax.swing.JMenuItem();
        eliminarprogdiec = new javax.swing.JMenuItem();
        sumadiadiec = new javax.swing.JMenuItem();
        restadiadiec = new javax.swing.JMenuItem();
        menupopray = new javax.swing.JPopupMenu();
        detalleopray = new javax.swing.JMenuItem();
        detalleartray = new javax.swing.JMenuItem();
        especartray = new javax.swing.JMenuItem();
        programopray = new javax.swing.JMenuItem();
        cambiarprogray = new javax.swing.JMenuItem();
        terminaprogray = new javax.swing.JMenuItem();
        eliminarprogray = new javax.swing.JMenuItem();
        sumadiaray = new javax.swing.JMenuItem();
        restadiaray = new javax.swing.JMenuItem();
        menupoppdo = new javax.swing.JPopupMenu();
        detalleoppdo = new javax.swing.JMenuItem();
        detalleartpdo = new javax.swing.JMenuItem();
        especartpdo = new javax.swing.JMenuItem();
        programoppdo = new javax.swing.JMenuItem();
        cambiarprogpdo = new javax.swing.JMenuItem();
        terminaprogpdo = new javax.swing.JMenuItem();
        eliminarprogpdo = new javax.swing.JMenuItem();
        sumadiapdo = new javax.swing.JMenuItem();
        restadiapdo = new javax.swing.JMenuItem();
        menupopdes = new javax.swing.JPopupMenu();
        detalleopdes = new javax.swing.JMenuItem();
        detalleartdes = new javax.swing.JMenuItem();
        especartdes = new javax.swing.JMenuItem();
        programoppdes = new javax.swing.JMenuItem();
        cambiarprogdes = new javax.swing.JMenuItem();
        terminaprogdes = new javax.swing.JMenuItem();
        eliminarprogdes = new javax.swing.JMenuItem();
        sumadiades = new javax.swing.JMenuItem();
        restadiades = new javax.swing.JMenuItem();
        menupopman = new javax.swing.JPopupMenu();
        detalleopman = new javax.swing.JMenuItem();
        detalleartman = new javax.swing.JMenuItem();
        especartman = new javax.swing.JMenuItem();
        programoppman = new javax.swing.JMenuItem();
        cambiarprogman = new javax.swing.JMenuItem();
        terminaprogman = new javax.swing.JMenuItem();
        eliminarprogman = new javax.swing.JMenuItem();
        sumadiaman = new javax.swing.JMenuItem();
        restadiaman = new javax.swing.JMenuItem();
        menupopfon = new javax.swing.JPopupMenu();
        detalleopfon = new javax.swing.JMenuItem();
        detalleartfon = new javax.swing.JMenuItem();
        especartfon = new javax.swing.JMenuItem();
        programoppfon = new javax.swing.JMenuItem();
        cambiarprogfon = new javax.swing.JMenuItem();
        terminaprogfon = new javax.swing.JMenuItem();
        eliminarprogfon = new javax.swing.JMenuItem();
        menupopfle = new javax.swing.JPopupMenu();
        detalleopfle = new javax.swing.JMenuItem();
        detalleartfle = new javax.swing.JMenuItem();
        especartfle = new javax.swing.JMenuItem();
        programoppfle = new javax.swing.JMenuItem();
        cambiarprogfle = new javax.swing.JMenuItem();
        terminaprogfle = new javax.swing.JMenuItem();
        eliminarprogfle = new javax.swing.JMenuItem();
        sumadiafle = new javax.swing.JMenuItem();
        restadiafle = new javax.swing.JMenuItem();
        menupoptro = new javax.swing.JPopupMenu();
        detalleoptro = new javax.swing.JMenuItem();
        detallearttro = new javax.swing.JMenuItem();
        especarttro = new javax.swing.JMenuItem();
        programopptro = new javax.swing.JMenuItem();
        cambiarprogtro = new javax.swing.JMenuItem();
        terminaprogtro = new javax.swing.JMenuItem();
        eliminarprogtro = new javax.swing.JMenuItem();
        sumadiatro = new javax.swing.JMenuItem();
        restadiatro = new javax.swing.JMenuItem();
        jScrollPane18 = new javax.swing.JScrollPane();
        Tabladatos_papel = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        corr = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablacorr = new javax.swing.JTable();
        subpaneltcy1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        buscarcorr = new javax.swing.JTextField();
        btnexportarcorr = new javax.swing.JButton();
        btn_insumos_corr = new javax.swing.JButton();
        tcy = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tablatcy = new javax.swing.JTable();
        subpaneltcy = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        buscartcy = new javax.swing.JTextField();
        btnexportartcy = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        linealtcy = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Tablalinealtcy = new javax.swing.JTable();
        subpanellinealtcy = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        buscarlinealtcy = new javax.swing.JTextField();
        btnexportarlinealtcy = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        sys = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tablasys = new javax.swing.JTable();
        subpanelsys = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        buscarsys = new javax.swing.JTextField();
        btnexportarsys = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        linealsys = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        Tablalinealsys = new javax.swing.JTable();
        subpanellinealsys = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        buscarlinealsys = new javax.swing.JTextField();
        btnexportarlinealsys = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        com = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        Tablacom = new javax.swing.JTable();
        subpanelcom = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        buscarcom = new javax.swing.JTextField();
        btnexportarcom = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        eng = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        Tablaeng = new javax.swing.JTable();
        subpaneleng = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        buscareng = new javax.swing.JTextField();
        btnexportareng = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        marv = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        Tablamarv = new javax.swing.JTable();
        subpanelmarv = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        buscarmarv = new javax.swing.JTextField();
        btnexportarmarv = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        suap = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        Tablasuap = new javax.swing.JTable();
        subpanelsuap = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        buscarsuap = new javax.swing.JTextField();
        btnexportarsuap = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        diec = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        Tabladiec = new javax.swing.JTable();
        subpaneldiec = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        buscardiec = new javax.swing.JTextField();
        btnexportardiec = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        ray = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        Tablaray = new javax.swing.JTable();
        subpanelray = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        buscarray = new javax.swing.JTextField();
        btnexportarray = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        pdo = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        Tablapdo = new javax.swing.JTable();
        subpanelpdo = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        buscarpdo = new javax.swing.JTextField();
        btnexportarpdo = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        des = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        Tablades = new javax.swing.JTable();
        subpaneldes = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        buscardes = new javax.swing.JTextField();
        btnexportardes = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        man = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        Tablaman = new javax.swing.JTable();
        subpanelman = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        buscarman = new javax.swing.JTextField();
        btnexportarman = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        fon = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        Tablafon = new javax.swing.JTable();
        subpanelfon = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        buscarfon = new javax.swing.JTextField();
        btnexportarfon = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        fle = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        Tablafle = new javax.swing.JTable();
        subpanelfon1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        buscarfle = new javax.swing.JTextField();
        btnexportarfle = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        tro = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        Tablatro = new javax.swing.JTable();
        subpaneltro = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        buscartro = new javax.swing.JTextField();
        btnexportartro = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel4 = new javax.swing.JPanel();
        btnactualizar = new javax.swing.JButton();
        btn_tintas = new javax.swing.JButton();
        btnimprimirmaq = new javax.swing.JButton();
        btnimprimir = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        noregistros = new javax.swing.JLabel();

        menupopcorr.setName("menupopcorr"); // NOI18N
        menupopcorr.setPreferredSize(new java.awt.Dimension(130, 183));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(corrflex.class);
        nuevo_corr.setIcon(resourceMap.getIcon("nuevo_corr.icon")); // NOI18N
        nuevo_corr.setText(resourceMap.getString("nuevo_corr.text")); // NOI18N
        nuevo_corr.setName("nuevo_corr"); // NOI18N
        nuevo_corr.setPreferredSize(new java.awt.Dimension(130, 30));
        nuevo_corr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevo_corrActionPerformed(evt);
            }
        });
        menupopcorr.add(nuevo_corr);

        detalleopcorr.setIcon(resourceMap.getIcon("detalleopcorr.icon")); // NOI18N
        detalleopcorr.setText(resourceMap.getString("detalleopcorr.text")); // NOI18N
        detalleopcorr.setName("detalleopcorr"); // NOI18N
        detalleopcorr.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopcorr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopcorrActionPerformed(evt);
            }
        });
        menupopcorr.add(detalleopcorr);

        detalleartcorr.setIcon(resourceMap.getIcon("detalleartcorr.icon")); // NOI18N
        detalleartcorr.setText(resourceMap.getString("detalleartcorr.text")); // NOI18N
        detalleartcorr.setName("detalleartcorr"); // NOI18N
        detalleartcorr.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartcorr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartcorrActionPerformed(evt);
            }
        });
        menupopcorr.add(detalleartcorr);

        especartcorr.setIcon(resourceMap.getIcon("especartcorr.icon")); // NOI18N
        especartcorr.setText(resourceMap.getString("especartcorr.text")); // NOI18N
        especartcorr.setName("especartcorr"); // NOI18N
        especartcorr.setPreferredSize(new java.awt.Dimension(130, 30));
        especartcorr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartcorrActionPerformed(evt);
            }
        });
        menupopcorr.add(especartcorr);

        combinaopcorr.setIcon(resourceMap.getIcon("combinaopcorr.icon")); // NOI18N
        combinaopcorr.setText(resourceMap.getString("combinaopcorr.text")); // NOI18N
        combinaopcorr.setName("combinaopcorr"); // NOI18N
        combinaopcorr.setPreferredSize(new java.awt.Dimension(130, 30));
        combinaopcorr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combinaopcorrActionPerformed(evt);
            }
        });
        menupopcorr.add(combinaopcorr);

        combinaartcorr.setIcon(resourceMap.getIcon("combinaartcorr.icon")); // NOI18N
        combinaartcorr.setText(resourceMap.getString("combinaartcorr.text")); // NOI18N
        combinaartcorr.setName("combinaartcorr"); // NOI18N
        combinaartcorr.setPreferredSize(new java.awt.Dimension(130, 30));
        combinaartcorr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combinaartcorrActionPerformed(evt);
            }
        });
        menupopcorr.add(combinaartcorr);

        menupoptcy.setName("menupoptcy"); // NOI18N
        menupoptcy.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleoptcy.setIcon(resourceMap.getIcon("detalleoptcy.icon")); // NOI18N
        detalleoptcy.setText(resourceMap.getString("detalleoptcy.text")); // NOI18N
        detalleoptcy.setName("detalleoptcy"); // NOI18N
        detalleoptcy.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleoptcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleoptcyActionPerformed(evt);
            }
        });
        menupoptcy.add(detalleoptcy);

        detallearttcy.setIcon(resourceMap.getIcon("detallearttcy.icon")); // NOI18N
        detallearttcy.setText(resourceMap.getString("detallearttcy.text")); // NOI18N
        detallearttcy.setName("detallearttcy"); // NOI18N
        detallearttcy.setPreferredSize(new java.awt.Dimension(130, 30));
        detallearttcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detallearttcyActionPerformed(evt);
            }
        });
        menupoptcy.add(detallearttcy);

        especarttcy.setIcon(resourceMap.getIcon("especarttcy.icon")); // NOI18N
        especarttcy.setText(resourceMap.getString("especarttcy.text")); // NOI18N
        especarttcy.setName("especarttcy"); // NOI18N
        especarttcy.setPreferredSize(new java.awt.Dimension(130, 30));
        especarttcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especarttcyActionPerformed(evt);
            }
        });
        menupoptcy.add(especarttcy);

        programoptcy.setIcon(resourceMap.getIcon("programoptcy.icon")); // NOI18N
        programoptcy.setText(resourceMap.getString("programoptcy.text")); // NOI18N
        programoptcy.setToolTipText(resourceMap.getString("programoptcy.toolTipText")); // NOI18N
        programoptcy.setName("programoptcy"); // NOI18N
        programoptcy.setPreferredSize(new java.awt.Dimension(130, 30));
        programoptcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programoptcyActionPerformed(evt);
            }
        });
        menupoptcy.add(programoptcy);

        cambiarprogtcy.setIcon(resourceMap.getIcon("cambiarprogtcy.icon")); // NOI18N
        cambiarprogtcy.setText(resourceMap.getString("cambiarprogtcy.text")); // NOI18N
        cambiarprogtcy.setToolTipText(resourceMap.getString("cambiarprogtcy.toolTipText")); // NOI18N
        cambiarprogtcy.setName("cambiarprogtcy"); // NOI18N
        cambiarprogtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogtcyActionPerformed(evt);
            }
        });
        menupoptcy.add(cambiarprogtcy);

        terminaprogtcy.setIcon(resourceMap.getIcon("terminaprogtcy.icon")); // NOI18N
        terminaprogtcy.setText(resourceMap.getString("terminaprogtcy.text")); // NOI18N
        terminaprogtcy.setToolTipText(resourceMap.getString("terminaprogtcy.toolTipText")); // NOI18N
        terminaprogtcy.setName("terminaprogtcy"); // NOI18N
        terminaprogtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogtcyActionPerformed(evt);
            }
        });
        menupoptcy.add(terminaprogtcy);

        eliminarprogtcy.setIcon(resourceMap.getIcon("eliminarprogtcy.icon")); // NOI18N
        eliminarprogtcy.setText(resourceMap.getString("eliminarprogtcy.text")); // NOI18N
        eliminarprogtcy.setToolTipText(resourceMap.getString("eliminarprogtcy.toolTipText")); // NOI18N
        eliminarprogtcy.setName("eliminarprogtcy"); // NOI18N
        eliminarprogtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogtcyActionPerformed(evt);
            }
        });
        menupoptcy.add(eliminarprogtcy);

        sumadiatcy.setIcon(resourceMap.getIcon("sumadiatcy.icon")); // NOI18N
        sumadiatcy.setText(resourceMap.getString("sumadiatcy.text")); // NOI18N
        sumadiatcy.setName("sumadiatcy"); // NOI18N
        sumadiatcy.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiatcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiatcyActionPerformed(evt);
            }
        });
        menupoptcy.add(sumadiatcy);

        restadiatcy.setIcon(resourceMap.getIcon("restadiatcy.icon")); // NOI18N
        restadiatcy.setText(resourceMap.getString("restadiatcy.text")); // NOI18N
        restadiatcy.setName("restadiatcy"); // NOI18N
        restadiatcy.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiatcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiatcyActionPerformed(evt);
            }
        });
        menupoptcy.add(restadiatcy);

        menupoplinealtcy.setName("menupoplinealtcy"); // NOI18N
        menupoplinealtcy.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleoplinealtcy.setIcon(resourceMap.getIcon("detalleoplinealtcy.icon")); // NOI18N
        detalleoplinealtcy.setText(resourceMap.getString("detalleoplinealtcy.text")); // NOI18N
        detalleoplinealtcy.setName("detalleoplinealtcy"); // NOI18N
        detalleoplinealtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleoplinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleoplinealtcyActionPerformed(evt);
            }
        });
        menupoplinealtcy.add(detalleoplinealtcy);

        detalleartlinealtcy.setIcon(resourceMap.getIcon("detalleartlinealtcy.icon")); // NOI18N
        detalleartlinealtcy.setText(resourceMap.getString("detalleartlinealtcy.text")); // NOI18N
        detalleartlinealtcy.setName("detalleartlinealtcy"); // NOI18N
        detalleartlinealtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartlinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartlinealtcyActionPerformed(evt);
            }
        });
        menupoplinealtcy.add(detalleartlinealtcy);

        especartlinealtcy.setIcon(resourceMap.getIcon("especartlinealtcy.icon")); // NOI18N
        especartlinealtcy.setText(resourceMap.getString("especartlinealtcy.text")); // NOI18N
        especartlinealtcy.setName("especartlinealtcy"); // NOI18N
        especartlinealtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        especartlinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartlinealtcyActionPerformed(evt);
            }
        });
        menupoplinealtcy.add(especartlinealtcy);

        programoplinealtcy.setIcon(resourceMap.getIcon("programoplinealtcy.icon")); // NOI18N
        programoplinealtcy.setText(resourceMap.getString("programoplinealtcy.text")); // NOI18N
        programoplinealtcy.setToolTipText(resourceMap.getString("programoplinealtcy.toolTipText")); // NOI18N
        programoplinealtcy.setName("programoplinealtcy"); // NOI18N
        programoplinealtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        programoplinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programoplinealtcyActionPerformed(evt);
            }
        });
        menupoplinealtcy.add(programoplinealtcy);

        cambiarproglinealtcy.setIcon(resourceMap.getIcon("cambiarproglinealtcy.icon")); // NOI18N
        cambiarproglinealtcy.setText(resourceMap.getString("cambiarproglinealtcy.text")); // NOI18N
        cambiarproglinealtcy.setToolTipText(resourceMap.getString("cambiarproglinealtcy.toolTipText")); // NOI18N
        cambiarproglinealtcy.setName("cambiarproglinealtcy"); // NOI18N
        cambiarproglinealtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarproglinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarproglinealtcyActionPerformed(evt);
            }
        });
        menupoplinealtcy.add(cambiarproglinealtcy);

        terminaproglinealtcy.setIcon(resourceMap.getIcon("terminaproglinealtcy.icon")); // NOI18N
        terminaproglinealtcy.setText(resourceMap.getString("terminaproglinealtcy.text")); // NOI18N
        terminaproglinealtcy.setToolTipText(resourceMap.getString("terminaproglinealtcy.toolTipText")); // NOI18N
        terminaproglinealtcy.setName("terminaproglinealtcy"); // NOI18N
        terminaproglinealtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaproglinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaproglinealtcyActionPerformed(evt);
            }
        });
        menupoplinealtcy.add(terminaproglinealtcy);

        eliminarproglinealtcy.setIcon(resourceMap.getIcon("eliminarproglinealtcy.icon")); // NOI18N
        eliminarproglinealtcy.setText(resourceMap.getString("eliminarproglinealtcy.text")); // NOI18N
        eliminarproglinealtcy.setToolTipText(resourceMap.getString("eliminarproglinealtcy.toolTipText")); // NOI18N
        eliminarproglinealtcy.setName("eliminarproglinealtcy"); // NOI18N
        eliminarproglinealtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarproglinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarproglinealtcyActionPerformed(evt);
            }
        });
        menupoplinealtcy.add(eliminarproglinealtcy);

        sumadialinealtcy.setIcon(resourceMap.getIcon("sumadialinealtcy.icon")); // NOI18N
        sumadialinealtcy.setText(resourceMap.getString("sumadialinealtcy.text")); // NOI18N
        sumadialinealtcy.setName("sumadialinealtcy"); // NOI18N
        sumadialinealtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadialinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadialinealtcyActionPerformed(evt);
            }
        });
        menupoplinealtcy.add(sumadialinealtcy);

        restadialinealtcy.setIcon(resourceMap.getIcon("restadialinealtcy.icon")); // NOI18N
        restadialinealtcy.setText(resourceMap.getString("restadialinealtcy.text")); // NOI18N
        restadialinealtcy.setName("restadialinealtcy"); // NOI18N
        restadialinealtcy.setPreferredSize(new java.awt.Dimension(130, 30));
        restadialinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadialinealtcyActionPerformed(evt);
            }
        });
        menupoplinealtcy.add(restadialinealtcy);

        menupopsys.setName("menupopsys"); // NOI18N
        menupopsys.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopsys.setIcon(resourceMap.getIcon("detalleopsys.icon")); // NOI18N
        detalleopsys.setText(resourceMap.getString("detalleopsys.text")); // NOI18N
        detalleopsys.setName("detalleopsys"); // NOI18N
        detalleopsys.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopsysActionPerformed(evt);
            }
        });
        menupopsys.add(detalleopsys);

        detalleartsys.setIcon(resourceMap.getIcon("detalleartsys.icon")); // NOI18N
        detalleartsys.setText(resourceMap.getString("detalleartsys.text")); // NOI18N
        detalleartsys.setName("detalleartsys"); // NOI18N
        detalleartsys.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartsysActionPerformed(evt);
            }
        });
        menupopsys.add(detalleartsys);

        especartsys.setIcon(resourceMap.getIcon("especartsys.icon")); // NOI18N
        especartsys.setText(resourceMap.getString("especartsys.text")); // NOI18N
        especartsys.setName("especartsys"); // NOI18N
        especartsys.setPreferredSize(new java.awt.Dimension(130, 30));
        especartsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartsysActionPerformed(evt);
            }
        });
        menupopsys.add(especartsys);

        programopsys.setIcon(resourceMap.getIcon("programopsys.icon")); // NOI18N
        programopsys.setText(resourceMap.getString("programopsys.text")); // NOI18N
        programopsys.setToolTipText(resourceMap.getString("programopsys.toolTipText")); // NOI18N
        programopsys.setName("programopsys"); // NOI18N
        programopsys.setPreferredSize(new java.awt.Dimension(130, 30));
        programopsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programopsysActionPerformed(evt);
            }
        });
        menupopsys.add(programopsys);

        cambiarprogsys.setIcon(resourceMap.getIcon("cambiarprogsys.icon")); // NOI18N
        cambiarprogsys.setText(resourceMap.getString("cambiarprogsys.text")); // NOI18N
        cambiarprogsys.setToolTipText(resourceMap.getString("cambiarprogsys.toolTipText")); // NOI18N
        cambiarprogsys.setName("cambiarprogsys"); // NOI18N
        cambiarprogsys.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogsysActionPerformed(evt);
            }
        });
        menupopsys.add(cambiarprogsys);

        terminaprogsys.setIcon(resourceMap.getIcon("terminaprogsys.icon")); // NOI18N
        terminaprogsys.setText(resourceMap.getString("terminaprogsys.text")); // NOI18N
        terminaprogsys.setToolTipText(resourceMap.getString("terminaprogsys.toolTipText")); // NOI18N
        terminaprogsys.setName("terminaprogsys"); // NOI18N
        terminaprogsys.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogsysActionPerformed(evt);
            }
        });
        menupopsys.add(terminaprogsys);

        eliminarprogsys.setIcon(resourceMap.getIcon("eliminarprogsys.icon")); // NOI18N
        eliminarprogsys.setText(resourceMap.getString("eliminarprogsys.text")); // NOI18N
        eliminarprogsys.setToolTipText(resourceMap.getString("eliminarprogsys.toolTipText")); // NOI18N
        eliminarprogsys.setName("eliminarprogsys"); // NOI18N
        eliminarprogsys.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogsysActionPerformed(evt);
            }
        });
        menupopsys.add(eliminarprogsys);

        sumadiasys.setIcon(resourceMap.getIcon("sumadiasys.icon")); // NOI18N
        sumadiasys.setText(resourceMap.getString("sumadiasys.text")); // NOI18N
        sumadiasys.setName("sumadiasys"); // NOI18N
        sumadiasys.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiasys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiasysActionPerformed(evt);
            }
        });
        menupopsys.add(sumadiasys);

        restadiasys.setIcon(resourceMap.getIcon("restadiasys.icon")); // NOI18N
        restadiasys.setText(resourceMap.getString("restadiasys.text")); // NOI18N
        restadiasys.setName("restadiasys"); // NOI18N
        restadiasys.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiasys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiasysActionPerformed(evt);
            }
        });
        menupopsys.add(restadiasys);

        menupoplinealsys.setName("menupoplinealsys"); // NOI18N
        menupoplinealsys.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleoplinealsys.setIcon(resourceMap.getIcon("detalleoplinealsys.icon")); // NOI18N
        detalleoplinealsys.setText(resourceMap.getString("detalleoplinealsys.text")); // NOI18N
        detalleoplinealsys.setName("detalleoplinealsys"); // NOI18N
        detalleoplinealsys.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleoplinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleoplinealsysActionPerformed(evt);
            }
        });
        menupoplinealsys.add(detalleoplinealsys);

        detalleartlinealsys.setIcon(resourceMap.getIcon("detalleartlinealsys.icon")); // NOI18N
        detalleartlinealsys.setText(resourceMap.getString("detalleartlinealsys.text")); // NOI18N
        detalleartlinealsys.setName("detalleartlinealsys"); // NOI18N
        detalleartlinealsys.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartlinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartlinealsysActionPerformed(evt);
            }
        });
        menupoplinealsys.add(detalleartlinealsys);

        especartlinealsys.setIcon(resourceMap.getIcon("especartlinealsys.icon")); // NOI18N
        especartlinealsys.setText(resourceMap.getString("especartlinealsys.text")); // NOI18N
        especartlinealsys.setName("especartlinealsys"); // NOI18N
        especartlinealsys.setPreferredSize(new java.awt.Dimension(130, 30));
        especartlinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartlinealsysActionPerformed(evt);
            }
        });
        menupoplinealsys.add(especartlinealsys);

        programoplinealsys.setIcon(resourceMap.getIcon("programoplinealsys.icon")); // NOI18N
        programoplinealsys.setText(resourceMap.getString("programoplinealsys.text")); // NOI18N
        programoplinealsys.setToolTipText(resourceMap.getString("programoplinealsys.toolTipText")); // NOI18N
        programoplinealsys.setName("programoplinealsys"); // NOI18N
        programoplinealsys.setPreferredSize(new java.awt.Dimension(130, 30));
        programoplinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programoplinealsysActionPerformed(evt);
            }
        });
        menupoplinealsys.add(programoplinealsys);

        cambiarproglinealsys.setIcon(resourceMap.getIcon("cambiarproglinealsys.icon")); // NOI18N
        cambiarproglinealsys.setText(resourceMap.getString("cambiarproglinealsys.text")); // NOI18N
        cambiarproglinealsys.setToolTipText(resourceMap.getString("cambiarproglinealsys.toolTipText")); // NOI18N
        cambiarproglinealsys.setName("cambiarproglinealsys"); // NOI18N
        cambiarproglinealsys.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarproglinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarproglinealsysActionPerformed(evt);
            }
        });
        menupoplinealsys.add(cambiarproglinealsys);

        terminaproglinealsys.setIcon(resourceMap.getIcon("terminaproglinealsys.icon")); // NOI18N
        terminaproglinealsys.setText(resourceMap.getString("terminaproglinealsys.text")); // NOI18N
        terminaproglinealsys.setToolTipText(resourceMap.getString("terminaproglinealsys.toolTipText")); // NOI18N
        terminaproglinealsys.setName("terminaproglinealsys"); // NOI18N
        terminaproglinealsys.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaproglinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaproglinealsysActionPerformed(evt);
            }
        });
        menupoplinealsys.add(terminaproglinealsys);

        eliminarproglinealsys.setIcon(resourceMap.getIcon("eliminarproglinealsys.icon")); // NOI18N
        eliminarproglinealsys.setText(resourceMap.getString("eliminarproglinealsys.text")); // NOI18N
        eliminarproglinealsys.setToolTipText(resourceMap.getString("eliminarproglinealsys.toolTipText")); // NOI18N
        eliminarproglinealsys.setName("eliminarproglinealsys"); // NOI18N
        eliminarproglinealsys.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarproglinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarproglinealsysActionPerformed(evt);
            }
        });
        menupoplinealsys.add(eliminarproglinealsys);

        sumadialinealsys.setIcon(resourceMap.getIcon("sumadialinealsys.icon")); // NOI18N
        sumadialinealsys.setText(resourceMap.getString("sumadialinealsys.text")); // NOI18N
        sumadialinealsys.setName("sumadialinealsys"); // NOI18N
        sumadialinealsys.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadialinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadialinealsysActionPerformed(evt);
            }
        });
        menupoplinealsys.add(sumadialinealsys);

        restadialinealsys.setIcon(resourceMap.getIcon("restadialinealsys.icon")); // NOI18N
        restadialinealsys.setText(resourceMap.getString("restadialinealsys.text")); // NOI18N
        restadialinealsys.setName("restadialinealsys"); // NOI18N
        restadialinealsys.setPreferredSize(new java.awt.Dimension(130, 30));
        restadialinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadialinealsysActionPerformed(evt);
            }
        });
        menupoplinealsys.add(restadialinealsys);

        menupopcom.setName("menupopcom"); // NOI18N
        menupopcom.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopcom.setIcon(resourceMap.getIcon("detalleopcom.icon")); // NOI18N
        detalleopcom.setText(resourceMap.getString("detalleopcom.text")); // NOI18N
        detalleopcom.setName("detalleopcom"); // NOI18N
        detalleopcom.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopcom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopcomActionPerformed(evt);
            }
        });
        menupopcom.add(detalleopcom);

        detalleartcom.setIcon(resourceMap.getIcon("detalleartcom.icon")); // NOI18N
        detalleartcom.setText(resourceMap.getString("detalleartcom.text")); // NOI18N
        detalleartcom.setName("detalleartcom"); // NOI18N
        detalleartcom.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartcom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartcomActionPerformed(evt);
            }
        });
        menupopcom.add(detalleartcom);

        especartcom.setIcon(resourceMap.getIcon("especartcom.icon")); // NOI18N
        especartcom.setText(resourceMap.getString("especartcom.text")); // NOI18N
        especartcom.setName("especartcom"); // NOI18N
        especartcom.setPreferredSize(new java.awt.Dimension(130, 30));
        especartcom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartcomActionPerformed(evt);
            }
        });
        menupopcom.add(especartcom);

        programopcom.setIcon(resourceMap.getIcon("programopcom.icon")); // NOI18N
        programopcom.setText(resourceMap.getString("programopcom.text")); // NOI18N
        programopcom.setToolTipText(resourceMap.getString("programopcom.toolTipText")); // NOI18N
        programopcom.setName("programopcom"); // NOI18N
        programopcom.setPreferredSize(new java.awt.Dimension(130, 30));
        programopcom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programopcomActionPerformed(evt);
            }
        });
        menupopcom.add(programopcom);

        cambiarprogcom.setIcon(resourceMap.getIcon("cambiarprogcom.icon")); // NOI18N
        cambiarprogcom.setText(resourceMap.getString("cambiarprogcom.text")); // NOI18N
        cambiarprogcom.setToolTipText(resourceMap.getString("cambiarprogcom.toolTipText")); // NOI18N
        cambiarprogcom.setName("cambiarprogcom"); // NOI18N
        cambiarprogcom.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogcom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogcomActionPerformed(evt);
            }
        });
        menupopcom.add(cambiarprogcom);

        terminaprogcom.setIcon(resourceMap.getIcon("terminaprogcom.icon")); // NOI18N
        terminaprogcom.setText(resourceMap.getString("terminaprogcom.text")); // NOI18N
        terminaprogcom.setToolTipText(resourceMap.getString("terminaprogcom.toolTipText")); // NOI18N
        terminaprogcom.setName("terminaprogcom"); // NOI18N
        terminaprogcom.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogcom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogcomActionPerformed(evt);
            }
        });
        menupopcom.add(terminaprogcom);

        eliminarprogcom.setIcon(resourceMap.getIcon("eliminarprogcom.icon")); // NOI18N
        eliminarprogcom.setText(resourceMap.getString("eliminarprogcom.text")); // NOI18N
        eliminarprogcom.setToolTipText(resourceMap.getString("eliminarprogcom.toolTipText")); // NOI18N
        eliminarprogcom.setName("eliminarprogcom"); // NOI18N
        eliminarprogcom.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogcom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogcomActionPerformed(evt);
            }
        });
        menupopcom.add(eliminarprogcom);

        sumadiacom.setIcon(resourceMap.getIcon("sumadiacom.icon")); // NOI18N
        sumadiacom.setText(resourceMap.getString("sumadiacom.text")); // NOI18N
        sumadiacom.setName("sumadiacom"); // NOI18N
        sumadiacom.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiacom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiacomActionPerformed(evt);
            }
        });
        menupopcom.add(sumadiacom);

        restadiacom.setIcon(resourceMap.getIcon("restadiacom.icon")); // NOI18N
        restadiacom.setText(resourceMap.getString("restadiacom.text")); // NOI18N
        restadiacom.setName("restadiacom"); // NOI18N
        restadiacom.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiacom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiacomActionPerformed(evt);
            }
        });
        menupopcom.add(restadiacom);

        menupopeng.setName("menupopeng"); // NOI18N
        menupopeng.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopeng.setIcon(resourceMap.getIcon("detalleopeng.icon")); // NOI18N
        detalleopeng.setText(resourceMap.getString("detalleopeng.text")); // NOI18N
        detalleopeng.setName("detalleopeng"); // NOI18N
        detalleopeng.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopeng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopengActionPerformed(evt);
            }
        });
        menupopeng.add(detalleopeng);

        detallearteng.setIcon(resourceMap.getIcon("detallearteng.icon")); // NOI18N
        detallearteng.setText(resourceMap.getString("detallearteng.text")); // NOI18N
        detallearteng.setName("detallearteng"); // NOI18N
        detallearteng.setPreferredSize(new java.awt.Dimension(130, 30));
        detallearteng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartengActionPerformed(evt);
            }
        });
        menupopeng.add(detallearteng);

        especarteng.setIcon(resourceMap.getIcon("especarteng.icon")); // NOI18N
        especarteng.setText(resourceMap.getString("especarteng.text")); // NOI18N
        especarteng.setName("especarteng"); // NOI18N
        especarteng.setPreferredSize(new java.awt.Dimension(130, 30));
        especarteng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartengActionPerformed(evt);
            }
        });
        menupopeng.add(especarteng);

        programopeng.setIcon(resourceMap.getIcon("programopeng.icon")); // NOI18N
        programopeng.setText(resourceMap.getString("programopeng.text")); // NOI18N
        programopeng.setToolTipText(resourceMap.getString("programopeng.toolTipText")); // NOI18N
        programopeng.setName("programopeng"); // NOI18N
        programopeng.setPreferredSize(new java.awt.Dimension(130, 30));
        programopeng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programopengActionPerformed(evt);
            }
        });
        menupopeng.add(programopeng);

        cambiarprogeng.setIcon(resourceMap.getIcon("cambiarprogeng.icon")); // NOI18N
        cambiarprogeng.setText(resourceMap.getString("cambiarprogeng.text")); // NOI18N
        cambiarprogeng.setToolTipText(resourceMap.getString("cambiarprogeng.toolTipText")); // NOI18N
        cambiarprogeng.setName("cambiarprogeng"); // NOI18N
        cambiarprogeng.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogeng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogengActionPerformed(evt);
            }
        });
        menupopeng.add(cambiarprogeng);

        terminaprogeng.setIcon(resourceMap.getIcon("terminaprogeng.icon")); // NOI18N
        terminaprogeng.setText(resourceMap.getString("terminaprogeng.text")); // NOI18N
        terminaprogeng.setToolTipText(resourceMap.getString("terminaprogeng.toolTipText")); // NOI18N
        terminaprogeng.setName("terminaprogeng"); // NOI18N
        terminaprogeng.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogeng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogengActionPerformed(evt);
            }
        });
        menupopeng.add(terminaprogeng);

        eliminarprogeng.setIcon(resourceMap.getIcon("eliminarprogeng.icon")); // NOI18N
        eliminarprogeng.setText(resourceMap.getString("eliminarprogeng.text")); // NOI18N
        eliminarprogeng.setToolTipText(resourceMap.getString("eliminarprogeng.toolTipText")); // NOI18N
        eliminarprogeng.setName("eliminarprogeng"); // NOI18N
        eliminarprogeng.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogeng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogengActionPerformed(evt);
            }
        });
        menupopeng.add(eliminarprogeng);

        sumadiaeng.setIcon(resourceMap.getIcon("sumadiaeng.icon")); // NOI18N
        sumadiaeng.setText(resourceMap.getString("sumadiaeng.text")); // NOI18N
        sumadiaeng.setName("sumadiaeng"); // NOI18N
        sumadiaeng.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiaeng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiaengActionPerformed(evt);
            }
        });
        menupopeng.add(sumadiaeng);

        restadiaeng.setIcon(resourceMap.getIcon("restadiaeng.icon")); // NOI18N
        restadiaeng.setText(resourceMap.getString("restadiaeng.text")); // NOI18N
        restadiaeng.setName("restadiaeng"); // NOI18N
        restadiaeng.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiaeng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiaengActionPerformed(evt);
            }
        });
        menupopeng.add(restadiaeng);

        menupopmarv.setName("menupopmarv"); // NOI18N
        menupopmarv.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopmarv.setIcon(resourceMap.getIcon("detalleopmarv.icon")); // NOI18N
        detalleopmarv.setText(resourceMap.getString("detalleopmarv.text")); // NOI18N
        detalleopmarv.setName("detalleopmarv"); // NOI18N
        detalleopmarv.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopmarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopmarvActionPerformed(evt);
            }
        });
        menupopmarv.add(detalleopmarv);

        detalleartmarv.setIcon(resourceMap.getIcon("detalleartmarv.icon")); // NOI18N
        detalleartmarv.setText(resourceMap.getString("detalleartmarv.text")); // NOI18N
        detalleartmarv.setName("detalleartmarv"); // NOI18N
        detalleartmarv.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartmarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartmarvActionPerformed(evt);
            }
        });
        menupopmarv.add(detalleartmarv);

        especartmarv.setIcon(resourceMap.getIcon("especartmarv.icon")); // NOI18N
        especartmarv.setText(resourceMap.getString("especartmarv.text")); // NOI18N
        especartmarv.setName("especartmarv"); // NOI18N
        especartmarv.setPreferredSize(new java.awt.Dimension(130, 30));
        especartmarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartmarvActionPerformed(evt);
            }
        });
        menupopmarv.add(especartmarv);

        programopmarv.setIcon(resourceMap.getIcon("programopmarv.icon")); // NOI18N
        programopmarv.setText(resourceMap.getString("programopmarv.text")); // NOI18N
        programopmarv.setToolTipText(resourceMap.getString("programopmarv.toolTipText")); // NOI18N
        programopmarv.setName("programopmarv"); // NOI18N
        programopmarv.setPreferredSize(new java.awt.Dimension(130, 30));
        programopmarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programopmarvActionPerformed(evt);
            }
        });
        menupopmarv.add(programopmarv);

        cambiarprogmarv.setIcon(resourceMap.getIcon("cambiarprogmarv.icon")); // NOI18N
        cambiarprogmarv.setText(resourceMap.getString("cambiarprogmarv.text")); // NOI18N
        cambiarprogmarv.setToolTipText(resourceMap.getString("cambiarprogmarv.toolTipText")); // NOI18N
        cambiarprogmarv.setName("cambiarprogmarv"); // NOI18N
        cambiarprogmarv.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogmarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogmarvActionPerformed(evt);
            }
        });
        menupopmarv.add(cambiarprogmarv);

        terminaprogmarv.setIcon(resourceMap.getIcon("terminaprogmarv.icon")); // NOI18N
        terminaprogmarv.setText(resourceMap.getString("terminaprogmarv.text")); // NOI18N
        terminaprogmarv.setToolTipText(resourceMap.getString("terminaprogmarv.toolTipText")); // NOI18N
        terminaprogmarv.setName("terminaprogmarv"); // NOI18N
        terminaprogmarv.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogmarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogmarvActionPerformed(evt);
            }
        });
        menupopmarv.add(terminaprogmarv);

        eliminarprogmarv.setIcon(resourceMap.getIcon("eliminarprogmarv.icon")); // NOI18N
        eliminarprogmarv.setText(resourceMap.getString("eliminarprogmarv.text")); // NOI18N
        eliminarprogmarv.setToolTipText(resourceMap.getString("eliminarprogmarv.toolTipText")); // NOI18N
        eliminarprogmarv.setName("eliminarprogmarv"); // NOI18N
        eliminarprogmarv.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogmarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogmarvActionPerformed(evt);
            }
        });
        menupopmarv.add(eliminarprogmarv);

        sumadiamarv.setIcon(resourceMap.getIcon("sumadiamarv.icon")); // NOI18N
        sumadiamarv.setText(resourceMap.getString("sumadiamarv.text")); // NOI18N
        sumadiamarv.setName("sumadiamarv"); // NOI18N
        sumadiamarv.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiamarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiamarvActionPerformed(evt);
            }
        });
        menupopmarv.add(sumadiamarv);

        restadiamarv.setIcon(resourceMap.getIcon("restadiamarv.icon")); // NOI18N
        restadiamarv.setText(resourceMap.getString("restadiamarv.text")); // NOI18N
        restadiamarv.setName("restadiamarv"); // NOI18N
        restadiamarv.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiamarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiamarvActionPerformed(evt);
            }
        });
        menupopmarv.add(restadiamarv);

        menupopsuap.setName("menupopsuap"); // NOI18N
        menupopsuap.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopsuap.setIcon(resourceMap.getIcon("detalleopsuap.icon")); // NOI18N
        detalleopsuap.setText(resourceMap.getString("detalleopsuap.text")); // NOI18N
        detalleopsuap.setName("detalleopsuap"); // NOI18N
        detalleopsuap.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopsuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopsuapActionPerformed(evt);
            }
        });
        menupopsuap.add(detalleopsuap);

        detalleartsuap.setIcon(resourceMap.getIcon("detalleartsuap.icon")); // NOI18N
        detalleartsuap.setText(resourceMap.getString("detalleartsuap.text")); // NOI18N
        detalleartsuap.setName("detalleartsuap"); // NOI18N
        detalleartsuap.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartsuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartsuapActionPerformed(evt);
            }
        });
        menupopsuap.add(detalleartsuap);

        especartsuap.setIcon(resourceMap.getIcon("especartsuap.icon")); // NOI18N
        especartsuap.setText(resourceMap.getString("especartsuap.text")); // NOI18N
        especartsuap.setName("especartsuap"); // NOI18N
        especartsuap.setPreferredSize(new java.awt.Dimension(130, 30));
        especartsuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartsuapActionPerformed(evt);
            }
        });
        menupopsuap.add(especartsuap);

        programopsuap.setIcon(resourceMap.getIcon("programopsuap.icon")); // NOI18N
        programopsuap.setText(resourceMap.getString("programopsuap.text")); // NOI18N
        programopsuap.setToolTipText(resourceMap.getString("programopsuap.toolTipText")); // NOI18N
        programopsuap.setName("programopsuap"); // NOI18N
        programopsuap.setPreferredSize(new java.awt.Dimension(130, 30));
        programopsuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programopsuapActionPerformed(evt);
            }
        });
        menupopsuap.add(programopsuap);

        cambiarprogsuap.setIcon(resourceMap.getIcon("cambiarprogsuap.icon")); // NOI18N
        cambiarprogsuap.setText(resourceMap.getString("cambiarprogsuap.text")); // NOI18N
        cambiarprogsuap.setToolTipText(resourceMap.getString("cambiarprogsuap.toolTipText")); // NOI18N
        cambiarprogsuap.setName("cambiarprogsuap"); // NOI18N
        cambiarprogsuap.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogsuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogsuapActionPerformed(evt);
            }
        });
        menupopsuap.add(cambiarprogsuap);

        terminaprogsuap.setIcon(resourceMap.getIcon("terminaprogsuap.icon")); // NOI18N
        terminaprogsuap.setText(resourceMap.getString("terminaprogsuap.text")); // NOI18N
        terminaprogsuap.setToolTipText(resourceMap.getString("terminaprogsuap.toolTipText")); // NOI18N
        terminaprogsuap.setName("terminaprogsuap"); // NOI18N
        terminaprogsuap.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogsuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogsuapActionPerformed(evt);
            }
        });
        menupopsuap.add(terminaprogsuap);

        eliminarprogsuap.setIcon(resourceMap.getIcon("eliminarprogsuap.icon")); // NOI18N
        eliminarprogsuap.setText(resourceMap.getString("eliminarprogsuap.text")); // NOI18N
        eliminarprogsuap.setToolTipText(resourceMap.getString("eliminarprogsuap.toolTipText")); // NOI18N
        eliminarprogsuap.setName("eliminarprogsuap"); // NOI18N
        eliminarprogsuap.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogsuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogsuapActionPerformed(evt);
            }
        });
        menupopsuap.add(eliminarprogsuap);

        sumadiasuap.setIcon(resourceMap.getIcon("sumadiasuap.icon")); // NOI18N
        sumadiasuap.setText(resourceMap.getString("sumadiasuap.text")); // NOI18N
        sumadiasuap.setName("sumadiasuap"); // NOI18N
        sumadiasuap.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiasuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiasuapActionPerformed(evt);
            }
        });
        menupopsuap.add(sumadiasuap);

        restadiasuap.setIcon(resourceMap.getIcon("restadiasuap.icon")); // NOI18N
        restadiasuap.setText(resourceMap.getString("restadiasuap.text")); // NOI18N
        restadiasuap.setName("restadiasuap"); // NOI18N
        restadiasuap.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiasuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiasuapActionPerformed(evt);
            }
        });
        menupopsuap.add(restadiasuap);

        menupopdiec.setName("menupopdiec"); // NOI18N
        menupopdiec.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopdiec.setIcon(resourceMap.getIcon("detalleopdiec.icon")); // NOI18N
        detalleopdiec.setText(resourceMap.getString("detalleopdiec.text")); // NOI18N
        detalleopdiec.setName("detalleopdiec"); // NOI18N
        detalleopdiec.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopdiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopdiecActionPerformed(evt);
            }
        });
        menupopdiec.add(detalleopdiec);

        detalleartdiec.setIcon(resourceMap.getIcon("detalleartdiec.icon")); // NOI18N
        detalleartdiec.setText(resourceMap.getString("detalleartdiec.text")); // NOI18N
        detalleartdiec.setName("detalleartdiec"); // NOI18N
        detalleartdiec.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartdiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartdiecActionPerformed(evt);
            }
        });
        menupopdiec.add(detalleartdiec);

        especartdiec.setIcon(resourceMap.getIcon("especartdiec.icon")); // NOI18N
        especartdiec.setText(resourceMap.getString("especartdiec.text")); // NOI18N
        especartdiec.setName("especartdiec"); // NOI18N
        especartdiec.setPreferredSize(new java.awt.Dimension(130, 30));
        especartdiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartdiecActionPerformed(evt);
            }
        });
        menupopdiec.add(especartdiec);

        programopdiec.setIcon(resourceMap.getIcon("programopdiec.icon")); // NOI18N
        programopdiec.setText(resourceMap.getString("programopdiec.text")); // NOI18N
        programopdiec.setToolTipText(resourceMap.getString("programopdiec.toolTipText")); // NOI18N
        programopdiec.setName("programopdiec"); // NOI18N
        programopdiec.setPreferredSize(new java.awt.Dimension(130, 30));
        programopdiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programopdiecActionPerformed(evt);
            }
        });
        menupopdiec.add(programopdiec);

        cambiarprogdiec.setIcon(resourceMap.getIcon("cambiarprogdiec.icon")); // NOI18N
        cambiarprogdiec.setText(resourceMap.getString("cambiarprogdiec.text")); // NOI18N
        cambiarprogdiec.setToolTipText(resourceMap.getString("cambiarprogdiec.toolTipText")); // NOI18N
        cambiarprogdiec.setName("cambiarprogdiec"); // NOI18N
        cambiarprogdiec.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogdiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogdiecActionPerformed(evt);
            }
        });
        menupopdiec.add(cambiarprogdiec);

        terminaprogdiec.setIcon(resourceMap.getIcon("terminaprogdiec.icon")); // NOI18N
        terminaprogdiec.setText(resourceMap.getString("terminaprogdiec.text")); // NOI18N
        terminaprogdiec.setToolTipText(resourceMap.getString("terminaprogdiec.toolTipText")); // NOI18N
        terminaprogdiec.setName("terminaprogdiec"); // NOI18N
        terminaprogdiec.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogdiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogdiecActionPerformed(evt);
            }
        });
        menupopdiec.add(terminaprogdiec);

        eliminarprogdiec.setIcon(resourceMap.getIcon("eliminarprogdiec.icon")); // NOI18N
        eliminarprogdiec.setText(resourceMap.getString("eliminarprogdiec.text")); // NOI18N
        eliminarprogdiec.setToolTipText(resourceMap.getString("eliminarprogdiec.toolTipText")); // NOI18N
        eliminarprogdiec.setName("eliminarprogdiec"); // NOI18N
        eliminarprogdiec.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogdiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogdiecActionPerformed(evt);
            }
        });
        menupopdiec.add(eliminarprogdiec);

        sumadiadiec.setIcon(resourceMap.getIcon("sumadiadiec.icon")); // NOI18N
        sumadiadiec.setText(resourceMap.getString("sumadiadiec.text")); // NOI18N
        sumadiadiec.setName("sumadiadiec"); // NOI18N
        sumadiadiec.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiadiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiadiecActionPerformed(evt);
            }
        });
        menupopdiec.add(sumadiadiec);

        restadiadiec.setIcon(resourceMap.getIcon("restadiadiec.icon")); // NOI18N
        restadiadiec.setText(resourceMap.getString("restadiadiec.text")); // NOI18N
        restadiadiec.setName("restadiadiec"); // NOI18N
        restadiadiec.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiadiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiadiecActionPerformed(evt);
            }
        });
        menupopdiec.add(restadiadiec);

        menupopray.setName("menupopray"); // NOI18N
        menupopray.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopray.setIcon(resourceMap.getIcon("detalleopray.icon")); // NOI18N
        detalleopray.setText(resourceMap.getString("detalleopray.text")); // NOI18N
        detalleopray.setName("detalleopray"); // NOI18N
        detalleopray.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleoprayActionPerformed(evt);
            }
        });
        menupopray.add(detalleopray);

        detalleartray.setIcon(resourceMap.getIcon("detalleartray.icon")); // NOI18N
        detalleartray.setText(resourceMap.getString("detalleartray.text")); // NOI18N
        detalleartray.setName("detalleartray"); // NOI18N
        detalleartray.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartrayActionPerformed(evt);
            }
        });
        menupopray.add(detalleartray);

        especartray.setIcon(resourceMap.getIcon("especartray.icon")); // NOI18N
        especartray.setText(resourceMap.getString("especartray.text")); // NOI18N
        especartray.setName("especartray"); // NOI18N
        especartray.setPreferredSize(new java.awt.Dimension(130, 30));
        especartray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartrayActionPerformed(evt);
            }
        });
        menupopray.add(especartray);

        programopray.setIcon(resourceMap.getIcon("programopray.icon")); // NOI18N
        programopray.setText(resourceMap.getString("programopray.text")); // NOI18N
        programopray.setToolTipText(resourceMap.getString("programopray.toolTipText")); // NOI18N
        programopray.setName("programopray"); // NOI18N
        programopray.setPreferredSize(new java.awt.Dimension(130, 30));
        programopray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programoprayActionPerformed(evt);
            }
        });
        menupopray.add(programopray);

        cambiarprogray.setIcon(resourceMap.getIcon("cambiarprogray.icon")); // NOI18N
        cambiarprogray.setText(resourceMap.getString("cambiarprogray.text")); // NOI18N
        cambiarprogray.setToolTipText(resourceMap.getString("cambiarprogray.toolTipText")); // NOI18N
        cambiarprogray.setName("cambiarprogray"); // NOI18N
        cambiarprogray.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprograyActionPerformed(evt);
            }
        });
        menupopray.add(cambiarprogray);

        terminaprogray.setIcon(resourceMap.getIcon("terminaprogray.icon")); // NOI18N
        terminaprogray.setText(resourceMap.getString("terminaprogray.text")); // NOI18N
        terminaprogray.setToolTipText(resourceMap.getString("terminaprogray.toolTipText")); // NOI18N
        terminaprogray.setName("terminaprogray"); // NOI18N
        terminaprogray.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprograyActionPerformed(evt);
            }
        });
        menupopray.add(terminaprogray);

        eliminarprogray.setIcon(resourceMap.getIcon("eliminarprogray.icon")); // NOI18N
        eliminarprogray.setText(resourceMap.getString("eliminarprogray.text")); // NOI18N
        eliminarprogray.setToolTipText(resourceMap.getString("eliminarprogray.toolTipText")); // NOI18N
        eliminarprogray.setName("eliminarprogray"); // NOI18N
        eliminarprogray.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprograyActionPerformed(evt);
            }
        });
        menupopray.add(eliminarprogray);

        sumadiaray.setIcon(resourceMap.getIcon("sumadiaray.icon")); // NOI18N
        sumadiaray.setText(resourceMap.getString("sumadiaray.text")); // NOI18N
        sumadiaray.setName("sumadiaray"); // NOI18N
        sumadiaray.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiaray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiarayActionPerformed(evt);
            }
        });
        menupopray.add(sumadiaray);

        restadiaray.setIcon(resourceMap.getIcon("restadiaray.icon")); // NOI18N
        restadiaray.setText(resourceMap.getString("restadiaray.text")); // NOI18N
        restadiaray.setName("restadiaray"); // NOI18N
        restadiaray.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiaray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiarayActionPerformed(evt);
            }
        });
        menupopray.add(restadiaray);

        menupoppdo.setName("menupoppdo"); // NOI18N
        menupoppdo.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleoppdo.setIcon(resourceMap.getIcon("detalleoppdo.icon")); // NOI18N
        detalleoppdo.setText(resourceMap.getString("detalleoppdo.text")); // NOI18N
        detalleoppdo.setName("detalleoppdo"); // NOI18N
        detalleoppdo.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleoppdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleoppdoActionPerformed(evt);
            }
        });
        menupoppdo.add(detalleoppdo);

        detalleartpdo.setIcon(resourceMap.getIcon("detalleartpdo.icon")); // NOI18N
        detalleartpdo.setText(resourceMap.getString("detalleartpdo.text")); // NOI18N
        detalleartpdo.setName("detalleartpdo"); // NOI18N
        detalleartpdo.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartpdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartpdoActionPerformed(evt);
            }
        });
        menupoppdo.add(detalleartpdo);

        especartpdo.setIcon(resourceMap.getIcon("especartpdo.icon")); // NOI18N
        especartpdo.setText(resourceMap.getString("especartpdo.text")); // NOI18N
        especartpdo.setName("especartpdo"); // NOI18N
        especartpdo.setPreferredSize(new java.awt.Dimension(130, 30));
        especartpdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartpdoActionPerformed(evt);
            }
        });
        menupoppdo.add(especartpdo);

        programoppdo.setIcon(resourceMap.getIcon("programoppdo.icon")); // NOI18N
        programoppdo.setText(resourceMap.getString("programoppdo.text")); // NOI18N
        programoppdo.setToolTipText(resourceMap.getString("programoppdo.toolTipText")); // NOI18N
        programoppdo.setName("programoppdo"); // NOI18N
        programoppdo.setPreferredSize(new java.awt.Dimension(130, 30));
        programoppdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programoppdoActionPerformed(evt);
            }
        });
        menupoppdo.add(programoppdo);

        cambiarprogpdo.setIcon(resourceMap.getIcon("cambiarprogpdo.icon")); // NOI18N
        cambiarprogpdo.setText(resourceMap.getString("cambiarprogpdo.text")); // NOI18N
        cambiarprogpdo.setToolTipText(resourceMap.getString("cambiarprogpdo.toolTipText")); // NOI18N
        cambiarprogpdo.setName("cambiarprogpdo"); // NOI18N
        cambiarprogpdo.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogpdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogpdoActionPerformed(evt);
            }
        });
        menupoppdo.add(cambiarprogpdo);

        terminaprogpdo.setIcon(resourceMap.getIcon("terminaprogpdo.icon")); // NOI18N
        terminaprogpdo.setText(resourceMap.getString("terminaprogpdo.text")); // NOI18N
        terminaprogpdo.setToolTipText(resourceMap.getString("terminaprogpdo.toolTipText")); // NOI18N
        terminaprogpdo.setName("terminaprogpdo"); // NOI18N
        terminaprogpdo.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogpdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogpdoActionPerformed(evt);
            }
        });
        menupoppdo.add(terminaprogpdo);

        eliminarprogpdo.setIcon(resourceMap.getIcon("eliminarprogpdo.icon")); // NOI18N
        eliminarprogpdo.setText(resourceMap.getString("eliminarprogpdo.text")); // NOI18N
        eliminarprogpdo.setToolTipText(resourceMap.getString("eliminarprogpdo.toolTipText")); // NOI18N
        eliminarprogpdo.setName("eliminarprogpdo"); // NOI18N
        eliminarprogpdo.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogpdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogpdoActionPerformed(evt);
            }
        });
        menupoppdo.add(eliminarprogpdo);

        sumadiapdo.setIcon(resourceMap.getIcon("sumadiapdo.icon")); // NOI18N
        sumadiapdo.setText(resourceMap.getString("sumadiapdo.text")); // NOI18N
        sumadiapdo.setName("sumadiapdo"); // NOI18N
        sumadiapdo.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiapdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiapdoActionPerformed(evt);
            }
        });
        menupoppdo.add(sumadiapdo);

        restadiapdo.setIcon(resourceMap.getIcon("restadiapdo.icon")); // NOI18N
        restadiapdo.setText(resourceMap.getString("restadiapdo.text")); // NOI18N
        restadiapdo.setName("restadiapdo"); // NOI18N
        restadiapdo.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiapdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiapdoActionPerformed(evt);
            }
        });
        menupoppdo.add(restadiapdo);

        menupopdes.setName("menupopdes"); // NOI18N
        menupopdes.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopdes.setIcon(resourceMap.getIcon("detalleopdes.icon")); // NOI18N
        detalleopdes.setText(resourceMap.getString("detalleopdes.text")); // NOI18N
        detalleopdes.setName("detalleopdes"); // NOI18N
        detalleopdes.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopdes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopdesActionPerformed(evt);
            }
        });
        menupopdes.add(detalleopdes);

        detalleartdes.setIcon(resourceMap.getIcon("detalleartdes.icon")); // NOI18N
        detalleartdes.setText(resourceMap.getString("detalleartdes.text")); // NOI18N
        detalleartdes.setName("detalleartdes"); // NOI18N
        detalleartdes.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartdes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartdesActionPerformed(evt);
            }
        });
        menupopdes.add(detalleartdes);

        especartdes.setIcon(resourceMap.getIcon("especartdes.icon")); // NOI18N
        especartdes.setText(resourceMap.getString("especartdes.text")); // NOI18N
        especartdes.setName("especartdes"); // NOI18N
        especartdes.setPreferredSize(new java.awt.Dimension(130, 30));
        especartdes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartdesActionPerformed(evt);
            }
        });
        menupopdes.add(especartdes);

        programoppdes.setIcon(resourceMap.getIcon("programoppdes.icon")); // NOI18N
        programoppdes.setText(resourceMap.getString("programoppdes.text")); // NOI18N
        programoppdes.setToolTipText(resourceMap.getString("programoppdes.toolTipText")); // NOI18N
        programoppdes.setName("programoppdes"); // NOI18N
        programoppdes.setPreferredSize(new java.awt.Dimension(130, 30));
        programoppdes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programoppdesActionPerformed(evt);
            }
        });
        menupopdes.add(programoppdes);

        cambiarprogdes.setIcon(resourceMap.getIcon("cambiarprogdes.icon")); // NOI18N
        cambiarprogdes.setText(resourceMap.getString("cambiarprogdes.text")); // NOI18N
        cambiarprogdes.setToolTipText(resourceMap.getString("cambiarprogdes.toolTipText")); // NOI18N
        cambiarprogdes.setName("cambiarprogdes"); // NOI18N
        cambiarprogdes.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogdes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogdesActionPerformed(evt);
            }
        });
        menupopdes.add(cambiarprogdes);

        terminaprogdes.setIcon(resourceMap.getIcon("terminaprogdes.icon")); // NOI18N
        terminaprogdes.setText(resourceMap.getString("terminaprogdes.text")); // NOI18N
        terminaprogdes.setToolTipText(resourceMap.getString("terminaprogdes.toolTipText")); // NOI18N
        terminaprogdes.setName("terminaprogdes"); // NOI18N
        terminaprogdes.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogdes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogdesActionPerformed(evt);
            }
        });
        menupopdes.add(terminaprogdes);

        eliminarprogdes.setIcon(resourceMap.getIcon("eliminarprogdes.icon")); // NOI18N
        eliminarprogdes.setText(resourceMap.getString("eliminarprogdes.text")); // NOI18N
        eliminarprogdes.setToolTipText(resourceMap.getString("eliminarprogdes.toolTipText")); // NOI18N
        eliminarprogdes.setName("eliminarprogdes"); // NOI18N
        eliminarprogdes.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogdes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogdesActionPerformed(evt);
            }
        });
        menupopdes.add(eliminarprogdes);

        sumadiades.setIcon(resourceMap.getIcon("sumadiades.icon")); // NOI18N
        sumadiades.setText(resourceMap.getString("sumadiades.text")); // NOI18N
        sumadiades.setName("sumadiades"); // NOI18N
        sumadiades.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiadesActionPerformed(evt);
            }
        });
        menupopdes.add(sumadiades);

        restadiades.setIcon(resourceMap.getIcon("restadiades.icon")); // NOI18N
        restadiades.setText(resourceMap.getString("restadiades.text")); // NOI18N
        restadiades.setName("restadiades"); // NOI18N
        restadiades.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiadesActionPerformed(evt);
            }
        });
        menupopdes.add(restadiades);

        menupopman.setName("menupopman"); // NOI18N
        menupopman.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopman.setIcon(resourceMap.getIcon("detalleopman.icon")); // NOI18N
        detalleopman.setText(resourceMap.getString("detalleopman.text")); // NOI18N
        detalleopman.setName("detalleopman"); // NOI18N
        detalleopman.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopmanActionPerformed(evt);
            }
        });
        menupopman.add(detalleopman);

        detalleartman.setIcon(resourceMap.getIcon("detalleartman.icon")); // NOI18N
        detalleartman.setText(resourceMap.getString("detalleartman.text")); // NOI18N
        detalleartman.setName("detalleartman"); // NOI18N
        detalleartman.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartmanActionPerformed(evt);
            }
        });
        menupopman.add(detalleartman);

        especartman.setIcon(resourceMap.getIcon("especartman.icon")); // NOI18N
        especartman.setText(resourceMap.getString("especartman.text")); // NOI18N
        especartman.setName("especartman"); // NOI18N
        especartman.setPreferredSize(new java.awt.Dimension(130, 30));
        especartman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartmanActionPerformed(evt);
            }
        });
        menupopman.add(especartman);

        programoppman.setIcon(resourceMap.getIcon("programoppman.icon")); // NOI18N
        programoppman.setText(resourceMap.getString("programoppman.text")); // NOI18N
        programoppman.setToolTipText(resourceMap.getString("programoppman.toolTipText")); // NOI18N
        programoppman.setName("programoppman"); // NOI18N
        programoppman.setPreferredSize(new java.awt.Dimension(130, 30));
        programoppman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programoppmanActionPerformed(evt);
            }
        });
        menupopman.add(programoppman);

        cambiarprogman.setIcon(resourceMap.getIcon("cambiarprogman.icon")); // NOI18N
        cambiarprogman.setText(resourceMap.getString("cambiarprogman.text")); // NOI18N
        cambiarprogman.setToolTipText(resourceMap.getString("cambiarprogman.toolTipText")); // NOI18N
        cambiarprogman.setName("cambiarprogman"); // NOI18N
        cambiarprogman.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogmanActionPerformed(evt);
            }
        });
        menupopman.add(cambiarprogman);

        terminaprogman.setIcon(resourceMap.getIcon("terminaprogman.icon")); // NOI18N
        terminaprogman.setText(resourceMap.getString("terminaprogman.text")); // NOI18N
        terminaprogman.setToolTipText(resourceMap.getString("terminaprogman.toolTipText")); // NOI18N
        terminaprogman.setName("terminaprogman"); // NOI18N
        terminaprogman.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogmanActionPerformed(evt);
            }
        });
        menupopman.add(terminaprogman);

        eliminarprogman.setIcon(resourceMap.getIcon("eliminarprogman.icon")); // NOI18N
        eliminarprogman.setText(resourceMap.getString("eliminarprogman.text")); // NOI18N
        eliminarprogman.setToolTipText(resourceMap.getString("eliminarprogman.toolTipText")); // NOI18N
        eliminarprogman.setName("eliminarprogman"); // NOI18N
        eliminarprogman.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogmanActionPerformed(evt);
            }
        });
        menupopman.add(eliminarprogman);

        sumadiaman.setIcon(resourceMap.getIcon("sumadiaman.icon")); // NOI18N
        sumadiaman.setText(resourceMap.getString("sumadiaman.text")); // NOI18N
        sumadiaman.setName("sumadiaman"); // NOI18N
        sumadiaman.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiamanActionPerformed(evt);
            }
        });
        menupopman.add(sumadiaman);

        restadiaman.setIcon(resourceMap.getIcon("restadiaman.icon")); // NOI18N
        restadiaman.setText(resourceMap.getString("restadiaman.text")); // NOI18N
        restadiaman.setName("restadiaman"); // NOI18N
        restadiaman.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiamanActionPerformed(evt);
            }
        });
        menupopman.add(restadiaman);

        menupopfon.setName("menupopfon"); // NOI18N
        menupopfon.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopfon.setIcon(resourceMap.getIcon("detalleopfon.icon")); // NOI18N
        detalleopfon.setText(resourceMap.getString("detalleopfon.text")); // NOI18N
        detalleopfon.setName("detalleopfon"); // NOI18N
        detalleopfon.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopfon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopfonActionPerformed(evt);
            }
        });
        menupopfon.add(detalleopfon);

        detalleartfon.setIcon(resourceMap.getIcon("detalleartfon.icon")); // NOI18N
        detalleartfon.setText(resourceMap.getString("detalleartfon.text")); // NOI18N
        detalleartfon.setName("detalleartfon"); // NOI18N
        detalleartfon.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartfon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartfonActionPerformed(evt);
            }
        });
        menupopfon.add(detalleartfon);

        especartfon.setIcon(resourceMap.getIcon("especartfon.icon")); // NOI18N
        especartfon.setText(resourceMap.getString("especartfon.text")); // NOI18N
        especartfon.setName("especartfon"); // NOI18N
        especartfon.setPreferredSize(new java.awt.Dimension(130, 30));
        especartfon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartfonActionPerformed(evt);
            }
        });
        menupopfon.add(especartfon);

        programoppfon.setIcon(resourceMap.getIcon("programoppfon.icon")); // NOI18N
        programoppfon.setText(resourceMap.getString("programoppfon.text")); // NOI18N
        programoppfon.setToolTipText(resourceMap.getString("programoppfon.toolTipText")); // NOI18N
        programoppfon.setName("programoppfon"); // NOI18N
        programoppfon.setPreferredSize(new java.awt.Dimension(130, 30));
        programoppfon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programoppfonActionPerformed(evt);
            }
        });
        menupopfon.add(programoppfon);

        cambiarprogfon.setIcon(resourceMap.getIcon("cambiarprogfon.icon")); // NOI18N
        cambiarprogfon.setText(resourceMap.getString("cambiarprogfon.text")); // NOI18N
        cambiarprogfon.setToolTipText(resourceMap.getString("cambiarprogfon.toolTipText")); // NOI18N
        cambiarprogfon.setName("cambiarprogfon"); // NOI18N
        cambiarprogfon.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogfon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogfonActionPerformed(evt);
            }
        });
        menupopfon.add(cambiarprogfon);

        terminaprogfon.setIcon(resourceMap.getIcon("terminaprogfon.icon")); // NOI18N
        terminaprogfon.setText(resourceMap.getString("terminaprogfon.text")); // NOI18N
        terminaprogfon.setToolTipText(resourceMap.getString("terminaprogfon.toolTipText")); // NOI18N
        terminaprogfon.setName("terminaprogfon"); // NOI18N
        terminaprogfon.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogfon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogfonActionPerformed(evt);
            }
        });
        menupopfon.add(terminaprogfon);

        eliminarprogfon.setIcon(resourceMap.getIcon("eliminarprogfon.icon")); // NOI18N
        eliminarprogfon.setText(resourceMap.getString("eliminarprogfon.text")); // NOI18N
        eliminarprogfon.setToolTipText(resourceMap.getString("eliminarprogfon.toolTipText")); // NOI18N
        eliminarprogfon.setName("eliminarprogfon"); // NOI18N
        eliminarprogfon.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogfon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogfonActionPerformed(evt);
            }
        });
        menupopfon.add(eliminarprogfon);

        menupopfle.setName("menupopfle"); // NOI18N
        menupopfle.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleopfle.setIcon(resourceMap.getIcon("detalleopfle.icon")); // NOI18N
        detalleopfle.setText(resourceMap.getString("detalleopfle.text")); // NOI18N
        detalleopfle.setName("detalleopfle"); // NOI18N
        detalleopfle.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleopfle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleopfleActionPerformed(evt);
            }
        });
        menupopfle.add(detalleopfle);

        detalleartfle.setIcon(resourceMap.getIcon("detalleartfle.icon")); // NOI18N
        detalleartfle.setText(resourceMap.getString("detalleartfle.text")); // NOI18N
        detalleartfle.setName("detalleartfle"); // NOI18N
        detalleartfle.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleartfle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleartfleActionPerformed(evt);
            }
        });
        menupopfle.add(detalleartfle);

        especartfle.setIcon(resourceMap.getIcon("especartfle.icon")); // NOI18N
        especartfle.setText(resourceMap.getString("especartfle.text")); // NOI18N
        especartfle.setName("especartfle"); // NOI18N
        especartfle.setPreferredSize(new java.awt.Dimension(130, 30));
        especartfle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especartfleActionPerformed(evt);
            }
        });
        menupopfle.add(especartfle);

        programoppfle.setIcon(resourceMap.getIcon("programoppfle.icon")); // NOI18N
        programoppfle.setText(resourceMap.getString("programoppfle.text")); // NOI18N
        programoppfle.setToolTipText(resourceMap.getString("programoppfle.toolTipText")); // NOI18N
        programoppfle.setName("programoppfle"); // NOI18N
        programoppfle.setPreferredSize(new java.awt.Dimension(130, 30));
        programoppfle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programoppfleActionPerformed(evt);
            }
        });
        menupopfle.add(programoppfle);

        cambiarprogfle.setIcon(resourceMap.getIcon("cambiarprogfle.icon")); // NOI18N
        cambiarprogfle.setText(resourceMap.getString("cambiarprogfle.text")); // NOI18N
        cambiarprogfle.setToolTipText(resourceMap.getString("cambiarprogfle.toolTipText")); // NOI18N
        cambiarprogfle.setName("cambiarprogfle"); // NOI18N
        cambiarprogfle.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogfle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogfleActionPerformed(evt);
            }
        });
        menupopfle.add(cambiarprogfle);

        terminaprogfle.setIcon(resourceMap.getIcon("terminaprogfle.icon")); // NOI18N
        terminaprogfle.setText(resourceMap.getString("terminaprogfle.text")); // NOI18N
        terminaprogfle.setToolTipText(resourceMap.getString("terminaprogfle.toolTipText")); // NOI18N
        terminaprogfle.setName("terminaprogfle"); // NOI18N
        terminaprogfle.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogfle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogfleActionPerformed(evt);
            }
        });
        menupopfle.add(terminaprogfle);

        eliminarprogfle.setIcon(resourceMap.getIcon("eliminarprogfle.icon")); // NOI18N
        eliminarprogfle.setText(resourceMap.getString("eliminarprogfle.text")); // NOI18N
        eliminarprogfle.setToolTipText(resourceMap.getString("eliminarprogfle.toolTipText")); // NOI18N
        eliminarprogfle.setName("eliminarprogfle"); // NOI18N
        eliminarprogfle.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogfle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogfleActionPerformed(evt);
            }
        });
        menupopfle.add(eliminarprogfle);

        sumadiafle.setIcon(resourceMap.getIcon("sumadiafle.icon")); // NOI18N
        sumadiafle.setText(resourceMap.getString("sumadiafle.text")); // NOI18N
        sumadiafle.setName("sumadiafle"); // NOI18N
        sumadiafle.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiafle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiafleActionPerformed(evt);
            }
        });
        menupopfle.add(sumadiafle);

        restadiafle.setIcon(resourceMap.getIcon("restadiafle.icon")); // NOI18N
        restadiafle.setText(resourceMap.getString("restadiafle.text")); // NOI18N
        restadiafle.setName("restadiafle"); // NOI18N
        restadiafle.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiafle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiafleActionPerformed(evt);
            }
        });
        menupopfle.add(restadiafle);

        menupoptro.setName("menupoptro"); // NOI18N
        menupoptro.setPreferredSize(new java.awt.Dimension(130, 204));

        detalleoptro.setIcon(resourceMap.getIcon("detalleoptro.icon")); // NOI18N
        detalleoptro.setText(resourceMap.getString("detalleoptro.text")); // NOI18N
        detalleoptro.setName("detalleoptro"); // NOI18N
        detalleoptro.setPreferredSize(new java.awt.Dimension(130, 30));
        detalleoptro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalleoptroActionPerformed(evt);
            }
        });
        menupoptro.add(detalleoptro);

        detallearttro.setIcon(resourceMap.getIcon("detallearttro.icon")); // NOI18N
        detallearttro.setText(resourceMap.getString("detallearttro.text")); // NOI18N
        detallearttro.setName("detallearttro"); // NOI18N
        detallearttro.setPreferredSize(new java.awt.Dimension(130, 30));
        detallearttro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detallearttroActionPerformed(evt);
            }
        });
        menupoptro.add(detallearttro);

        especarttro.setIcon(resourceMap.getIcon("especarttro.icon")); // NOI18N
        especarttro.setText(resourceMap.getString("especarttro.text")); // NOI18N
        especarttro.setName("especarttro"); // NOI18N
        especarttro.setPreferredSize(new java.awt.Dimension(130, 30));
        especarttro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                especarttroActionPerformed(evt);
            }
        });
        menupoptro.add(especarttro);

        programopptro.setIcon(resourceMap.getIcon("programopptro.icon")); // NOI18N
        programopptro.setText(resourceMap.getString("programopptro.text")); // NOI18N
        programopptro.setToolTipText(resourceMap.getString("programopptro.toolTipText")); // NOI18N
        programopptro.setName("programopptro"); // NOI18N
        programopptro.setPreferredSize(new java.awt.Dimension(130, 30));
        programopptro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programopptroActionPerformed(evt);
            }
        });
        menupoptro.add(programopptro);

        cambiarprogtro.setIcon(resourceMap.getIcon("cambiarprogtro.icon")); // NOI18N
        cambiarprogtro.setText(resourceMap.getString("cambiarprogtro.text")); // NOI18N
        cambiarprogtro.setToolTipText(resourceMap.getString("cambiarprogtro.toolTipText")); // NOI18N
        cambiarprogtro.setName("cambiarprogtro"); // NOI18N
        cambiarprogtro.setPreferredSize(new java.awt.Dimension(130, 30));
        cambiarprogtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarprogtroActionPerformed(evt);
            }
        });
        menupoptro.add(cambiarprogtro);

        terminaprogtro.setIcon(resourceMap.getIcon("terminaprogtro.icon")); // NOI18N
        terminaprogtro.setText(resourceMap.getString("terminaprogtro.text")); // NOI18N
        terminaprogtro.setToolTipText(resourceMap.getString("terminaprogtro.toolTipText")); // NOI18N
        terminaprogtro.setName("terminaprogtro"); // NOI18N
        terminaprogtro.setPreferredSize(new java.awt.Dimension(130, 30));
        terminaprogtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaprogtroActionPerformed(evt);
            }
        });
        menupoptro.add(terminaprogtro);

        eliminarprogtro.setIcon(resourceMap.getIcon("eliminarprogtro.icon")); // NOI18N
        eliminarprogtro.setText(resourceMap.getString("eliminarprogtro.text")); // NOI18N
        eliminarprogtro.setToolTipText(resourceMap.getString("eliminarprogtro.toolTipText")); // NOI18N
        eliminarprogtro.setName("eliminarprogtro"); // NOI18N
        eliminarprogtro.setPreferredSize(new java.awt.Dimension(130, 30));
        eliminarprogtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarprogtroActionPerformed(evt);
            }
        });
        menupoptro.add(eliminarprogtro);

        sumadiatro.setIcon(resourceMap.getIcon("sumadiatro.icon")); // NOI18N
        sumadiatro.setText(resourceMap.getString("sumadiatro.text")); // NOI18N
        sumadiatro.setName("sumadiatro"); // NOI18N
        sumadiatro.setPreferredSize(new java.awt.Dimension(130, 30));
        sumadiatro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumadiatroActionPerformed(evt);
            }
        });
        menupoptro.add(sumadiatro);

        restadiatro.setIcon(resourceMap.getIcon("restadiatro.icon")); // NOI18N
        restadiatro.setText(resourceMap.getString("restadiatro.text")); // NOI18N
        restadiatro.setName("restadiatro"); // NOI18N
        restadiatro.setPreferredSize(new java.awt.Dimension(130, 30));
        restadiatro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restadiatroActionPerformed(evt);
            }
        });
        menupoptro.add(restadiatro);

        jScrollPane18.setName("jScrollPane18"); // NOI18N

        Tabladatos_papel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Resistencia", "Descripcion", "KG Total", "L1", "L1_kg", "R2", "R2_kg", "L2", "L2_kg", "R2", "R2_kg", "L3", "L3_kg"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladatos_papel.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos_papel.setName("Tabladatos_papel"); // NOI18N
        Tabladatos_papel.setRowHeight(22);
        Tabladatos_papel.getTableHeader().setReorderingAllowed(false);
        Tabladatos_papel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabladatos_papelMouseClicked(evt);
            }
        });
        jScrollPane18.setViewportView(Tabladatos_papel);

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

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        corr.setToolTipText(resourceMap.getString("corr.toolTipText")); // NOI18N
        corr.setName("corr"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tablacorr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Programa", "OP", "Fecha Entrega", "Maquila", "Lam. Pedida", "Clave Articulo", "Articulo", "Resis.", "Ancho", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>ML", "<html><font color=#0066FF>Laminas", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>ML Prod", "<html><font color=green>Laminas Prod", "<html><font color=green>KG Prod", "Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false ,true ,true , false, false,false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablacorr.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablacorr.setName("Tablacorr"); // NOI18N
        Tablacorr.setRowHeight(22);
        Tablacorr.getTableHeader().setReorderingAllowed(false);
        Tablacorr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablacorrMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tablacorr);

        subpaneltcy1.setBackground(resourceMap.getColor("subpaneltcy1.background")); // NOI18N
        subpaneltcy1.setName("subpaneltcy1"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(resourceMap.getIcon("jLabel3.icon")); // NOI18N
        jLabel3.setToolTipText(resourceMap.getString("jLabel3.toolTipText")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        buscarcorr.setName("buscarcorr"); // NOI18N
        buscarcorr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarcorrFocusGained(evt);
            }
        });
        buscarcorr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarcorrKeyReleased(evt);
            }
        });

        btnexportarcorr.setIcon(resourceMap.getIcon("btnexportarcorr.icon")); // NOI18N
        btnexportarcorr.setText(resourceMap.getString("btnexportarcorr.text")); // NOI18N
        btnexportarcorr.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarcorr.setName("btnexportarcorr"); // NOI18N
        btnexportarcorr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarcorrActionPerformed(evt);
            }
        });

        btn_insumos_corr.setIcon(resourceMap.getIcon("btn_insumos_corr.icon")); // NOI18N
        btn_insumos_corr.setText(resourceMap.getString("btn_insumos_corr.text")); // NOI18N
        btn_insumos_corr.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btn_insumos_corr.setMaximumSize(new java.awt.Dimension(81, 27));
        btn_insumos_corr.setMinimumSize(new java.awt.Dimension(81, 27));
        btn_insumos_corr.setName("btn_insumos_corr"); // NOI18N
        btn_insumos_corr.setPreferredSize(new java.awt.Dimension(81, 27));
        btn_insumos_corr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_insumos_corrActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpaneltcy1Layout = new javax.swing.GroupLayout(subpaneltcy1);
        subpaneltcy1.setLayout(subpaneltcy1Layout);
        subpaneltcy1Layout.setHorizontalGroup(
            subpaneltcy1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpaneltcy1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarcorr, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 308, Short.MAX_VALUE)
                .addComponent(btn_insumos_corr, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarcorr, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpaneltcy1Layout.setVerticalGroup(
            subpaneltcy1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpaneltcy1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpaneltcy1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpaneltcy1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarcorr)
                        .addComponent(btn_insumos_corr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(subpaneltcy1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarcorr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout corrLayout = new javax.swing.GroupLayout(corr);
        corr.setLayout(corrLayout);
        corrLayout.setHorizontalGroup(
            corrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpaneltcy1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        corrLayout.setVerticalGroup(
            corrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(corrLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpaneltcy1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("corr.TabConstraints.tabTitle"), null, corr, resourceMap.getString("corr.TabConstraints.tabToolTip")); // NOI18N

        tcy.setToolTipText(resourceMap.getString("tcy.toolTipText")); // NOI18N
        tcy.setName("tcy"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        Tablatcy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablatcy.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablatcy.setName("Tablatcy"); // NOI18N
        Tablatcy.setRowHeight(22);
        Tablatcy.getTableHeader().setReorderingAllowed(false);
        Tablatcy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablatcyMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(Tablatcy);

        subpaneltcy.setBackground(resourceMap.getColor("subpaneltcy.background")); // NOI18N
        subpaneltcy.setName("subpaneltcy"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setToolTipText(resourceMap.getString("jLabel2.toolTipText")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        buscartcy.setText(resourceMap.getString("buscartcy.text")); // NOI18N
        buscartcy.setName("buscartcy"); // NOI18N
        buscartcy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscartcyFocusGained(evt);
            }
        });
        buscartcy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscartcyKeyReleased(evt);
            }
        });

        btnexportartcy.setIcon(resourceMap.getIcon("btnexportartcy.icon")); // NOI18N
        btnexportartcy.setText(resourceMap.getString("btnexportartcy.text")); // NOI18N
        btnexportartcy.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportartcy.setName("btnexportartcy"); // NOI18N
        btnexportartcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportartcyActionPerformed(evt);
            }
        });

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpaneltcyLayout = new javax.swing.GroupLayout(subpaneltcy);
        subpaneltcy.setLayout(subpaneltcyLayout);
        subpaneltcyLayout.setHorizontalGroup(
            subpaneltcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpaneltcyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscartcy, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportartcy, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpaneltcyLayout.setVerticalGroup(
            subpaneltcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpaneltcyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpaneltcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpaneltcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportartcy)
                        .addComponent(jButton1))
                    .addGroup(subpaneltcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscartcy, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout tcyLayout = new javax.swing.GroupLayout(tcy);
        tcy.setLayout(tcyLayout);
        tcyLayout.setHorizontalGroup(
            tcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpaneltcy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tcyLayout.setVerticalGroup(
            tcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tcyLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpaneltcy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("tcy.TabConstraints.tabTitle"), null, tcy, resourceMap.getString("tcy.TabConstraints.tabToolTip")); // NOI18N

        linealtcy.setToolTipText(resourceMap.getString("linealtcy.toolTipText")); // NOI18N
        linealtcy.setName("linealtcy"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        Tablalinealtcy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablalinealtcy.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablalinealtcy.setName("Tablalinealtcy"); // NOI18N
        Tablalinealtcy.setRowHeight(22);
        Tablalinealtcy.getTableHeader().setReorderingAllowed(false);
        Tablalinealtcy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablalinealtcyMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(Tablalinealtcy);

        subpanellinealtcy.setBackground(resourceMap.getColor("subpanellinealtcy.background")); // NOI18N
        subpanellinealtcy.setName("subpanellinealtcy"); // NOI18N

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setIcon(resourceMap.getIcon("jLabel5.icon")); // NOI18N
        jLabel5.setToolTipText(resourceMap.getString("jLabel5.toolTipText")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        buscarlinealtcy.setName("buscarlinealtcy"); // NOI18N
        buscarlinealtcy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarlinealtcyFocusGained(evt);
            }
        });
        buscarlinealtcy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarlinealtcyKeyReleased(evt);
            }
        });

        btnexportarlinealtcy.setIcon(resourceMap.getIcon("btnexportarlinealtcy.icon")); // NOI18N
        btnexportarlinealtcy.setText(resourceMap.getString("btnexportarlinealtcy.text")); // NOI18N
        btnexportarlinealtcy.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarlinealtcy.setName("btnexportarlinealtcy"); // NOI18N
        btnexportarlinealtcy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarlinealtcyActionPerformed(evt);
            }
        });

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanellinealtcyLayout = new javax.swing.GroupLayout(subpanellinealtcy);
        subpanellinealtcy.setLayout(subpanellinealtcyLayout);
        subpanellinealtcyLayout.setHorizontalGroup(
            subpanellinealtcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanellinealtcyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarlinealtcy, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarlinealtcy, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanellinealtcyLayout.setVerticalGroup(
            subpanellinealtcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanellinealtcyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanellinealtcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanellinealtcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarlinealtcy)
                        .addComponent(jButton2))
                    .addGroup(subpanellinealtcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarlinealtcy, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout linealtcyLayout = new javax.swing.GroupLayout(linealtcy);
        linealtcy.setLayout(linealtcyLayout);
        linealtcyLayout.setHorizontalGroup(
            linealtcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanellinealtcy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        linealtcyLayout.setVerticalGroup(
            linealtcyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, linealtcyLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanellinealtcy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("linealtcy.TabConstraints.tabTitle"), null, linealtcy, resourceMap.getString("linealtcy.TabConstraints.tabToolTip")); // NOI18N

        sys.setToolTipText(resourceMap.getString("sys.toolTipText")); // NOI18N
        sys.setName("sys"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        Tablasys.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablasys.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablasys.setName("Tablasys"); // NOI18N
        Tablasys.setRowHeight(22);
        Tablasys.getTableHeader().setReorderingAllowed(false);
        Tablasys.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablasysMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(Tablasys);

        subpanelsys.setBackground(resourceMap.getColor("subpanelsys.background")); // NOI18N
        subpanelsys.setName("subpanelsys"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(resourceMap.getIcon("jLabel4.icon")); // NOI18N
        jLabel4.setToolTipText(resourceMap.getString("jLabel4.toolTipText")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        buscarsys.setName("buscarsys"); // NOI18N
        buscarsys.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarsysFocusGained(evt);
            }
        });
        buscarsys.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarsysKeyReleased(evt);
            }
        });

        btnexportarsys.setIcon(resourceMap.getIcon("btnexportarsys.icon")); // NOI18N
        btnexportarsys.setText(resourceMap.getString("btnexportarsys.text")); // NOI18N
        btnexportarsys.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarsys.setName("btnexportarsys"); // NOI18N
        btnexportarsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarsysActionPerformed(evt);
            }
        });

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelsysLayout = new javax.swing.GroupLayout(subpanelsys);
        subpanelsys.setLayout(subpanelsysLayout);
        subpanelsysLayout.setHorizontalGroup(
            subpanelsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelsysLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarsys, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarsys, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelsysLayout.setVerticalGroup(
            subpanelsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelsysLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarsys)
                        .addComponent(jButton3))
                    .addGroup(subpanelsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarsys, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout sysLayout = new javax.swing.GroupLayout(sys);
        sys.setLayout(sysLayout);
        sysLayout.setHorizontalGroup(
            sysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanelsys, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sysLayout.setVerticalGroup(
            sysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sysLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelsys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("sys.TabConstraints.tabTitle"), null, sys, resourceMap.getString("sys.TabConstraints.tabToolTip")); // NOI18N

        linealsys.setToolTipText(resourceMap.getString("linealsys.toolTipText")); // NOI18N
        linealsys.setName("linealsys"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        Tablalinealsys.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablalinealsys.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablalinealsys.setName("Tablalinealsys"); // NOI18N
        Tablalinealsys.setRowHeight(22);
        Tablalinealsys.getTableHeader().setReorderingAllowed(false);
        Tablalinealsys.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablalinealsysMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(Tablalinealsys);

        subpanellinealsys.setBackground(resourceMap.getColor("subpanellinealsys.background")); // NOI18N
        subpanellinealsys.setName("subpanellinealsys"); // NOI18N

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setIcon(resourceMap.getIcon("jLabel6.icon")); // NOI18N
        jLabel6.setToolTipText(resourceMap.getString("jLabel6.toolTipText")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        buscarlinealsys.setName("buscarlinealsys"); // NOI18N
        buscarlinealsys.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarlinealsysFocusGained(evt);
            }
        });
        buscarlinealsys.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarlinealsysKeyReleased(evt);
            }
        });

        btnexportarlinealsys.setIcon(resourceMap.getIcon("btnexportarlinealsys.icon")); // NOI18N
        btnexportarlinealsys.setText(resourceMap.getString("btnexportarlinealsys.text")); // NOI18N
        btnexportarlinealsys.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarlinealsys.setName("btnexportarlinealsys"); // NOI18N
        btnexportarlinealsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarlinealsysActionPerformed(evt);
            }
        });

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanellinealsysLayout = new javax.swing.GroupLayout(subpanellinealsys);
        subpanellinealsys.setLayout(subpanellinealsysLayout);
        subpanellinealsysLayout.setHorizontalGroup(
            subpanellinealsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanellinealsysLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarlinealsys, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarlinealsys, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanellinealsysLayout.setVerticalGroup(
            subpanellinealsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanellinealsysLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanellinealsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanellinealsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarlinealsys)
                        .addComponent(jButton4))
                    .addGroup(subpanellinealsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarlinealsys, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout linealsysLayout = new javax.swing.GroupLayout(linealsys);
        linealsys.setLayout(linealsysLayout);
        linealsysLayout.setHorizontalGroup(
            linealsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanellinealsys, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        linealsysLayout.setVerticalGroup(
            linealsysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, linealsysLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanellinealsys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("linealsys.TabConstraints.tabTitle"), null, linealsys, resourceMap.getString("linealsys.TabConstraints.tabToolTip")); // NOI18N

        com.setToolTipText(resourceMap.getString("com.toolTipText")); // NOI18N
        com.setName("com"); // NOI18N

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        Tablacom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablacom.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablacom.setName("Tablacom"); // NOI18N
        Tablacom.setRowHeight(22);
        Tablacom.getTableHeader().setReorderingAllowed(false);
        Tablacom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablacomMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(Tablacom);

        subpanelcom.setBackground(resourceMap.getColor("subpanelcom.background")); // NOI18N
        subpanelcom.setName("subpanelcom"); // NOI18N

        jLabel7.setFont(resourceMap.getFont("jLabel7.font")); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(resourceMap.getIcon("jLabel7.icon")); // NOI18N
        jLabel7.setToolTipText(resourceMap.getString("jLabel7.toolTipText")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        buscarcom.setName("buscarcom"); // NOI18N
        buscarcom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarcomFocusGained(evt);
            }
        });
        buscarcom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarcomKeyReleased(evt);
            }
        });

        btnexportarcom.setIcon(resourceMap.getIcon("btnexportarcom.icon")); // NOI18N
        btnexportarcom.setText(resourceMap.getString("btnexportarcom.text")); // NOI18N
        btnexportarcom.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarcom.setName("btnexportarcom"); // NOI18N
        btnexportarcom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarcomActionPerformed(evt);
            }
        });

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelcomLayout = new javax.swing.GroupLayout(subpanelcom);
        subpanelcom.setLayout(subpanelcomLayout);
        subpanelcomLayout.setHorizontalGroup(
            subpanelcomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelcomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarcom, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarcom, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelcomLayout.setVerticalGroup(
            subpanelcomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelcomLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelcomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelcomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarcom)
                        .addComponent(jButton5))
                    .addGroup(subpanelcomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarcom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout comLayout = new javax.swing.GroupLayout(com);
        com.setLayout(comLayout);
        comLayout.setHorizontalGroup(
            comLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanelcom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        comLayout.setVerticalGroup(
            comLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, comLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelcom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("com.TabConstraints.tabTitle"), null, com, resourceMap.getString("com.TabConstraints.tabToolTip")); // NOI18N

        eng.setToolTipText(resourceMap.getString("eng.toolTipText")); // NOI18N
        eng.setName("eng"); // NOI18N

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        Tablaeng.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablaeng.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablaeng.setName("Tablaeng"); // NOI18N
        Tablaeng.setRowHeight(22);
        Tablaeng.getTableHeader().setReorderingAllowed(false);
        Tablaeng.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaengMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(Tablaeng);

        subpaneleng.setBackground(resourceMap.getColor("subpaneleng.background")); // NOI18N
        subpaneleng.setName("subpaneleng"); // NOI18N

        jLabel8.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setIcon(resourceMap.getIcon("jLabel8.icon")); // NOI18N
        jLabel8.setToolTipText(resourceMap.getString("jLabel8.toolTipText")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        buscareng.setName("buscareng"); // NOI18N
        buscareng.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarengFocusGained(evt);
            }
        });
        buscareng.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarengKeyReleased(evt);
            }
        });

        btnexportareng.setIcon(resourceMap.getIcon("btnexportareng.icon")); // NOI18N
        btnexportareng.setText(resourceMap.getString("btnexportareng.text")); // NOI18N
        btnexportareng.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportareng.setName("btnexportareng"); // NOI18N
        btnexportareng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarengActionPerformed(evt);
            }
        });

        jButton6.setIcon(resourceMap.getIcon("jButton6.icon")); // NOI18N
        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelengLayout = new javax.swing.GroupLayout(subpaneleng);
        subpaneleng.setLayout(subpanelengLayout);
        subpanelengLayout.setHorizontalGroup(
            subpanelengLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelengLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscareng, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportareng, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelengLayout.setVerticalGroup(
            subpanelengLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelengLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelengLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelengLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportareng)
                        .addComponent(jButton6))
                    .addGroup(subpanelengLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscareng, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout engLayout = new javax.swing.GroupLayout(eng);
        eng.setLayout(engLayout);
        engLayout.setHorizontalGroup(
            engLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpaneleng, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        engLayout.setVerticalGroup(
            engLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, engLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpaneleng, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("eng.TabConstraints.tabTitle"), null, eng, resourceMap.getString("eng.TabConstraints.tabToolTip")); // NOI18N

        marv.setToolTipText(resourceMap.getString("marv.toolTipText")); // NOI18N
        marv.setName("marv"); // NOI18N

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        Tablamarv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablamarv.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablamarv.setName("Tablamarv"); // NOI18N
        Tablamarv.setRowHeight(22);
        Tablamarv.getTableHeader().setReorderingAllowed(false);
        Tablamarv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablamarvMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(Tablamarv);

        subpanelmarv.setBackground(resourceMap.getColor("subpanelmarv.background")); // NOI18N
        subpanelmarv.setName("subpanelmarv"); // NOI18N

        jLabel9.setFont(resourceMap.getFont("jLabel9.font")); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setIcon(resourceMap.getIcon("jLabel9.icon")); // NOI18N
        jLabel9.setToolTipText(resourceMap.getString("jLabel9.toolTipText")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        buscarmarv.setName("buscarmarv"); // NOI18N
        buscarmarv.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarmarvFocusGained(evt);
            }
        });
        buscarmarv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarmarvKeyReleased(evt);
            }
        });

        btnexportarmarv.setIcon(resourceMap.getIcon("btnexportarmarv.icon")); // NOI18N
        btnexportarmarv.setText(resourceMap.getString("btnexportarmarv.text")); // NOI18N
        btnexportarmarv.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarmarv.setName("btnexportarmarv"); // NOI18N
        btnexportarmarv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarmarvActionPerformed(evt);
            }
        });

        jButton7.setIcon(resourceMap.getIcon("jButton7.icon")); // NOI18N
        jButton7.setText(resourceMap.getString("jButton7.text")); // NOI18N
        jButton7.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton7.setName("jButton7"); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelmarvLayout = new javax.swing.GroupLayout(subpanelmarv);
        subpanelmarv.setLayout(subpanelmarvLayout);
        subpanelmarvLayout.setHorizontalGroup(
            subpanelmarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelmarvLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarmarv, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarmarv, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelmarvLayout.setVerticalGroup(
            subpanelmarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelmarvLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelmarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelmarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarmarv)
                        .addComponent(jButton7))
                    .addGroup(subpanelmarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarmarv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout marvLayout = new javax.swing.GroupLayout(marv);
        marv.setLayout(marvLayout);
        marvLayout.setHorizontalGroup(
            marvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanelmarv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        marvLayout.setVerticalGroup(
            marvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, marvLayout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelmarv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("marv.TabConstraints.tabTitle"), null, marv, resourceMap.getString("marv.TabConstraints.tabToolTip")); // NOI18N
        marv.getAccessibleContext().setAccessibleDescription(resourceMap.getString("emb.AccessibleContext.accessibleDescription")); // NOI18N

        suap.setToolTipText(resourceMap.getString("suap.toolTipText")); // NOI18N
        suap.setName("suap"); // NOI18N

        jScrollPane9.setName("jScrollPane9"); // NOI18N

        Tablasuap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablasuap.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablasuap.setName("Tablasuap"); // NOI18N
        Tablasuap.setRowHeight(22);
        Tablasuap.getTableHeader().setReorderingAllowed(false);
        Tablasuap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablasuapMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(Tablasuap);

        subpanelsuap.setBackground(resourceMap.getColor("subpanelsuap.background")); // NOI18N
        subpanelsuap.setName("subpanelsuap"); // NOI18N

        jLabel10.setFont(resourceMap.getFont("jLabel10.font")); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setIcon(resourceMap.getIcon("jLabel10.icon")); // NOI18N
        jLabel10.setToolTipText(resourceMap.getString("jLabel10.toolTipText")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        buscarsuap.setName("buscarsuap"); // NOI18N
        buscarsuap.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarsuapFocusGained(evt);
            }
        });
        buscarsuap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarsuapKeyReleased(evt);
            }
        });

        btnexportarsuap.setIcon(resourceMap.getIcon("btnexportarsuap.icon")); // NOI18N
        btnexportarsuap.setText(resourceMap.getString("btnexportarsuap.text")); // NOI18N
        btnexportarsuap.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarsuap.setName("btnexportarsuap"); // NOI18N
        btnexportarsuap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarsuapActionPerformed(evt);
            }
        });

        jButton8.setIcon(resourceMap.getIcon("jButton8.icon")); // NOI18N
        jButton8.setText(resourceMap.getString("jButton8.text")); // NOI18N
        jButton8.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton8.setName("jButton8"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelsuapLayout = new javax.swing.GroupLayout(subpanelsuap);
        subpanelsuap.setLayout(subpanelsuapLayout);
        subpanelsuapLayout.setHorizontalGroup(
            subpanelsuapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelsuapLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarsuap, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarsuap, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelsuapLayout.setVerticalGroup(
            subpanelsuapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelsuapLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelsuapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelsuapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarsuap)
                        .addComponent(jButton8))
                    .addGroup(subpanelsuapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarsuap, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout suapLayout = new javax.swing.GroupLayout(suap);
        suap.setLayout(suapLayout);
        suapLayout.setHorizontalGroup(
            suapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanelsuap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        suapLayout.setVerticalGroup(
            suapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suapLayout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelsuap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("suap.TabConstraints.tabTitle"), null, suap, resourceMap.getString("suap.TabConstraints.tabToolTip")); // NOI18N

        diec.setToolTipText(resourceMap.getString("diec.toolTipText")); // NOI18N
        diec.setName("diec"); // NOI18N

        jScrollPane10.setName("jScrollPane10"); // NOI18N

        Tabladiec.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladiec.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladiec.setName("Tabladiec"); // NOI18N
        Tabladiec.setRowHeight(22);
        Tabladiec.getTableHeader().setReorderingAllowed(false);
        Tabladiec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabladiecMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(Tabladiec);

        subpaneldiec.setBackground(resourceMap.getColor("subpaneldiec.background")); // NOI18N
        subpaneldiec.setName("subpaneldiec"); // NOI18N

        jLabel11.setFont(resourceMap.getFont("jLabel11.font")); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setIcon(resourceMap.getIcon("jLabel11.icon")); // NOI18N
        jLabel11.setToolTipText(resourceMap.getString("jLabel11.toolTipText")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        buscardiec.setName("buscardiec"); // NOI18N
        buscardiec.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscardiecFocusGained(evt);
            }
        });
        buscardiec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscardiecKeyReleased(evt);
            }
        });

        btnexportardiec.setIcon(resourceMap.getIcon("btnexportardiec.icon")); // NOI18N
        btnexportardiec.setText(resourceMap.getString("btnexportardiec.text")); // NOI18N
        btnexportardiec.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportardiec.setName("btnexportardiec"); // NOI18N
        btnexportardiec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportardiecActionPerformed(evt);
            }
        });

        jButton9.setIcon(resourceMap.getIcon("jButton9.icon")); // NOI18N
        jButton9.setText(resourceMap.getString("jButton9.text")); // NOI18N
        jButton9.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton9.setName("jButton9"); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpaneldiecLayout = new javax.swing.GroupLayout(subpaneldiec);
        subpaneldiec.setLayout(subpaneldiecLayout);
        subpaneldiecLayout.setHorizontalGroup(
            subpaneldiecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpaneldiecLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscardiec, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportardiec, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpaneldiecLayout.setVerticalGroup(
            subpaneldiecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpaneldiecLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpaneldiecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpaneldiecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportardiec)
                        .addComponent(jButton9))
                    .addGroup(subpaneldiecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscardiec, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout diecLayout = new javax.swing.GroupLayout(diec);
        diec.setLayout(diecLayout);
        diecLayout.setHorizontalGroup(
            diecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpaneldiec, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        diecLayout.setVerticalGroup(
            diecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, diecLayout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpaneldiec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("diec.TabConstraints.tabTitle"), null, diec, resourceMap.getString("diec.TabConstraints.tabToolTip")); // NOI18N

        ray.setToolTipText(resourceMap.getString("ray.toolTipText")); // NOI18N
        ray.setName("ray"); // NOI18N

        jScrollPane11.setName("jScrollPane11"); // NOI18N

        Tablaray.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablaray.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablaray.setName("Tablaray"); // NOI18N
        Tablaray.setRowHeight(22);
        Tablaray.getTableHeader().setReorderingAllowed(false);
        Tablaray.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablarayMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(Tablaray);

        subpanelray.setBackground(resourceMap.getColor("subpanelray.background")); // NOI18N
        subpanelray.setName("subpanelray"); // NOI18N

        jLabel12.setFont(resourceMap.getFont("jLabel12.font")); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setIcon(resourceMap.getIcon("jLabel12.icon")); // NOI18N
        jLabel12.setToolTipText(resourceMap.getString("jLabel12.toolTipText")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        buscarray.setName("buscarray"); // NOI18N
        buscarray.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarrayFocusGained(evt);
            }
        });
        buscarray.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarrayKeyReleased(evt);
            }
        });

        btnexportarray.setIcon(resourceMap.getIcon("btnexportarray.icon")); // NOI18N
        btnexportarray.setText(resourceMap.getString("btnexportarray.text")); // NOI18N
        btnexportarray.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarray.setName("btnexportarray"); // NOI18N
        btnexportarray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarrayActionPerformed(evt);
            }
        });

        jButton10.setIcon(resourceMap.getIcon("jButton10.icon")); // NOI18N
        jButton10.setText(resourceMap.getString("jButton10.text")); // NOI18N
        jButton10.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelrayLayout = new javax.swing.GroupLayout(subpanelray);
        subpanelray.setLayout(subpanelrayLayout);
        subpanelrayLayout.setHorizontalGroup(
            subpanelrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelrayLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarray, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarray, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelrayLayout.setVerticalGroup(
            subpanelrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelrayLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarray)
                        .addComponent(jButton10))
                    .addGroup(subpanelrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarray, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout rayLayout = new javax.swing.GroupLayout(ray);
        ray.setLayout(rayLayout);
        rayLayout.setHorizontalGroup(
            rayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanelray, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        rayLayout.setVerticalGroup(
            rayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rayLayout.createSequentialGroup()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelray, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("ray.TabConstraints.tabTitle"), null, ray, resourceMap.getString("ray.TabConstraints.tabToolTip")); // NOI18N

        pdo.setToolTipText(resourceMap.getString("pdo.toolTipText")); // NOI18N
        pdo.setName("pdo"); // NOI18N

        jScrollPane12.setName("jScrollPane12"); // NOI18N

        Tablapdo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablapdo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablapdo.setName("Tablapdo"); // NOI18N
        Tablapdo.setRowHeight(22);
        Tablapdo.getTableHeader().setReorderingAllowed(false);
        Tablapdo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablapdoMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(Tablapdo);

        subpanelpdo.setBackground(resourceMap.getColor("subpanelpdo.background")); // NOI18N
        subpanelpdo.setName("subpanelpdo"); // NOI18N

        jLabel13.setFont(resourceMap.getFont("jLabel13.font")); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setIcon(resourceMap.getIcon("jLabel13.icon")); // NOI18N
        jLabel13.setToolTipText(resourceMap.getString("jLabel13.toolTipText")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        buscarpdo.setName("buscarpdo"); // NOI18N
        buscarpdo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarpdoFocusGained(evt);
            }
        });
        buscarpdo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarpdoKeyReleased(evt);
            }
        });

        btnexportarpdo.setIcon(resourceMap.getIcon("btnexportarpdo.icon")); // NOI18N
        btnexportarpdo.setText(resourceMap.getString("btnexportarpdo.text")); // NOI18N
        btnexportarpdo.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarpdo.setName("btnexportarpdo"); // NOI18N
        btnexportarpdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarpdoActionPerformed(evt);
            }
        });

        jButton11.setIcon(resourceMap.getIcon("jButton11.icon")); // NOI18N
        jButton11.setText(resourceMap.getString("jButton11.text")); // NOI18N
        jButton11.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton11.setName("jButton11"); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelpdoLayout = new javax.swing.GroupLayout(subpanelpdo);
        subpanelpdo.setLayout(subpanelpdoLayout);
        subpanelpdoLayout.setHorizontalGroup(
            subpanelpdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelpdoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarpdo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarpdo, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelpdoLayout.setVerticalGroup(
            subpanelpdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelpdoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelpdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelpdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarpdo)
                        .addComponent(jButton11))
                    .addGroup(subpanelpdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarpdo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout pdoLayout = new javax.swing.GroupLayout(pdo);
        pdo.setLayout(pdoLayout);
        pdoLayout.setHorizontalGroup(
            pdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanelpdo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pdoLayout.setVerticalGroup(
            pdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pdoLayout.createSequentialGroup()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelpdo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("pdo.TabConstraints.tabTitle"), null, pdo, resourceMap.getString("pdo.TabConstraints.tabToolTip")); // NOI18N

        des.setToolTipText(resourceMap.getString("des.toolTipText")); // NOI18N
        des.setName("des"); // NOI18N

        jScrollPane13.setName("jScrollPane13"); // NOI18N

        Tablades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablades.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablades.setName("Tablades"); // NOI18N
        Tablades.setRowHeight(22);
        Tablades.getTableHeader().setReorderingAllowed(false);
        Tablades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabladesMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(Tablades);

        subpaneldes.setBackground(resourceMap.getColor("subpaneldes.background")); // NOI18N
        subpaneldes.setName("subpaneldes"); // NOI18N

        jLabel14.setFont(resourceMap.getFont("jLabel14.font")); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setIcon(resourceMap.getIcon("jLabel14.icon")); // NOI18N
        jLabel14.setToolTipText(resourceMap.getString("jLabel14.toolTipText")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        buscardes.setName("buscardes"); // NOI18N
        buscardes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscardesFocusGained(evt);
            }
        });
        buscardes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscardesKeyReleased(evt);
            }
        });

        btnexportardes.setIcon(resourceMap.getIcon("btnexportardes.icon")); // NOI18N
        btnexportardes.setText(resourceMap.getString("btnexportardes.text")); // NOI18N
        btnexportardes.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportardes.setName("btnexportardes"); // NOI18N
        btnexportardes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportardesActionPerformed(evt);
            }
        });

        jButton12.setIcon(resourceMap.getIcon("jButton12.icon")); // NOI18N
        jButton12.setText(resourceMap.getString("jButton12.text")); // NOI18N
        jButton12.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton12.setName("jButton12"); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpaneldesLayout = new javax.swing.GroupLayout(subpaneldes);
        subpaneldes.setLayout(subpaneldesLayout);
        subpaneldesLayout.setHorizontalGroup(
            subpaneldesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpaneldesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscardes, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportardes, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpaneldesLayout.setVerticalGroup(
            subpaneldesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpaneldesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpaneldesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpaneldesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportardes)
                        .addComponent(jButton12))
                    .addGroup(subpaneldesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscardes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout desLayout = new javax.swing.GroupLayout(des);
        des.setLayout(desLayout);
        desLayout.setHorizontalGroup(
            desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpaneldes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        desLayout.setVerticalGroup(
            desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, desLayout.createSequentialGroup()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpaneldes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("des.TabConstraints.tabTitle"), null, des, resourceMap.getString("des.TabConstraints.tabToolTip")); // NOI18N

        man.setToolTipText(resourceMap.getString("man.toolTipText")); // NOI18N
        man.setName("man"); // NOI18N

        jScrollPane14.setName("jScrollPane14"); // NOI18N

        Tablaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablaman.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablaman.setName("Tablaman"); // NOI18N
        Tablaman.setRowHeight(22);
        Tablaman.getTableHeader().setReorderingAllowed(false);
        Tablaman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablamanMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(Tablaman);

        subpanelman.setBackground(resourceMap.getColor("subpanelman.background")); // NOI18N
        subpanelman.setName("subpanelman"); // NOI18N

        jLabel15.setFont(resourceMap.getFont("jLabel15.font")); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setIcon(resourceMap.getIcon("jLabel15.icon")); // NOI18N
        jLabel15.setToolTipText(resourceMap.getString("jLabel15.toolTipText")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        buscarman.setName("buscarman"); // NOI18N
        buscarman.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarmanFocusGained(evt);
            }
        });
        buscarman.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarmanKeyReleased(evt);
            }
        });

        btnexportarman.setIcon(resourceMap.getIcon("btnexportarman.icon")); // NOI18N
        btnexportarman.setText(resourceMap.getString("btnexportarman.text")); // NOI18N
        btnexportarman.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarman.setName("btnexportarman"); // NOI18N
        btnexportarman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarmanActionPerformed(evt);
            }
        });

        jButton13.setIcon(resourceMap.getIcon("jButton13.icon")); // NOI18N
        jButton13.setText(resourceMap.getString("jButton13.text")); // NOI18N
        jButton13.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton13.setName("jButton13"); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelmanLayout = new javax.swing.GroupLayout(subpanelman);
        subpanelman.setLayout(subpanelmanLayout);
        subpanelmanLayout.setHorizontalGroup(
            subpanelmanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelmanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarman, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarman, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelmanLayout.setVerticalGroup(
            subpanelmanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelmanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelmanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelmanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarman)
                        .addComponent(jButton13))
                    .addGroup(subpanelmanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarman, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout manLayout = new javax.swing.GroupLayout(man);
        man.setLayout(manLayout);
        manLayout.setHorizontalGroup(
            manLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanelman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        manLayout.setVerticalGroup(
            manLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manLayout.createSequentialGroup()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("man.TabConstraints.tabTitle"), null, man, resourceMap.getString("man.TabConstraints.tabToolTip")); // NOI18N

        fon.setToolTipText(resourceMap.getString("fon.toolTipText")); // NOI18N
        fon.setName("fon"); // NOI18N

        jScrollPane15.setName("jScrollPane15"); // NOI18N

        Tablafon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablafon.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablafon.setName("Tablafon"); // NOI18N
        Tablafon.setRowHeight(22);
        Tablafon.getTableHeader().setReorderingAllowed(false);
        Tablafon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablafonMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(Tablafon);

        subpanelfon.setBackground(resourceMap.getColor("subpanelfon.background")); // NOI18N
        subpanelfon.setName("subpanelfon"); // NOI18N

        jLabel16.setFont(resourceMap.getFont("jLabel16.font")); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setIcon(resourceMap.getIcon("jLabel16.icon")); // NOI18N
        jLabel16.setToolTipText(resourceMap.getString("jLabel16.toolTipText")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        buscarfon.setName("buscarfon"); // NOI18N
        buscarfon.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarfonFocusGained(evt);
            }
        });
        buscarfon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarfonKeyReleased(evt);
            }
        });

        btnexportarfon.setIcon(resourceMap.getIcon("btnexportarfon.icon")); // NOI18N
        btnexportarfon.setText(resourceMap.getString("btnexportarfon.text")); // NOI18N
        btnexportarfon.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarfon.setName("btnexportarfon"); // NOI18N
        btnexportarfon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarfonActionPerformed(evt);
            }
        });

        jButton14.setIcon(resourceMap.getIcon("jButton14.icon")); // NOI18N
        jButton14.setText(resourceMap.getString("jButton14.text")); // NOI18N
        jButton14.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton14.setName("jButton14"); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelfonLayout = new javax.swing.GroupLayout(subpanelfon);
        subpanelfon.setLayout(subpanelfonLayout);
        subpanelfonLayout.setHorizontalGroup(
            subpanelfonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelfonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarfon, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarfon, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelfonLayout.setVerticalGroup(
            subpanelfonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelfonLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelfonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelfonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarfon)
                        .addComponent(jButton14))
                    .addGroup(subpanelfonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarfon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout fonLayout = new javax.swing.GroupLayout(fon);
        fon.setLayout(fonLayout);
        fonLayout.setHorizontalGroup(
            fonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanelfon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        fonLayout.setVerticalGroup(
            fonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fonLayout.createSequentialGroup()
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelfon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("fon.TabConstraints.tabTitle"), null, fon, resourceMap.getString("fon.TabConstraints.tabToolTip")); // NOI18N

        fle.setToolTipText(resourceMap.getString("fle.toolTipText")); // NOI18N
        fle.setName("fle"); // NOI18N

        jScrollPane16.setName("jScrollPane16"); // NOI18N

        Tablafle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablafle.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablafle.setName("Tablafle"); // NOI18N
        Tablafle.setRowHeight(22);
        Tablafle.getTableHeader().setReorderingAllowed(false);
        Tablafle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablafleMouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(Tablafle);

        subpanelfon1.setBackground(resourceMap.getColor("subpanelfon1.background")); // NOI18N
        subpanelfon1.setName("subpanelfon1"); // NOI18N

        jLabel17.setFont(resourceMap.getFont("jLabel17.font")); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setIcon(resourceMap.getIcon("jLabel17.icon")); // NOI18N
        jLabel17.setToolTipText(resourceMap.getString("jLabel17.toolTipText")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        buscarfle.setName("buscarfle"); // NOI18N
        buscarfle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarfleFocusGained(evt);
            }
        });
        buscarfle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarfleKeyReleased(evt);
            }
        });

        btnexportarfle.setIcon(resourceMap.getIcon("btnexportarfle.icon")); // NOI18N
        btnexportarfle.setText(resourceMap.getString("btnexportarfle.text")); // NOI18N
        btnexportarfle.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportarfle.setName("btnexportarfle"); // NOI18N
        btnexportarfle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarfleActionPerformed(evt);
            }
        });

        jButton15.setIcon(resourceMap.getIcon("jButton15.icon")); // NOI18N
        jButton15.setText(resourceMap.getString("jButton15.text")); // NOI18N
        jButton15.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton15.setName("jButton15"); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpanelfon1Layout = new javax.swing.GroupLayout(subpanelfon1);
        subpanelfon1.setLayout(subpanelfon1Layout);
        subpanelfon1Layout.setHorizontalGroup(
            subpanelfon1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanelfon1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarfle, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportarfle, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpanelfon1Layout.setVerticalGroup(
            subpanelfon1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpanelfon1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpanelfon1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpanelfon1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportarfle)
                        .addComponent(jButton15))
                    .addGroup(subpanelfon1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscarfle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout fleLayout = new javax.swing.GroupLayout(fle);
        fle.setLayout(fleLayout);
        fleLayout.setHorizontalGroup(
            fleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpanelfon1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        fleLayout.setVerticalGroup(
            fleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fleLayout.createSequentialGroup()
                .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelfon1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("fle.TabConstraints.tabTitle"), null, fle, resourceMap.getString("fle.TabConstraints.tabToolTip")); // NOI18N

        tro.setToolTipText(resourceMap.getString("tro.toolTipText")); // NOI18N
        tro.setName("tro"); // NOI18N

        jScrollPane17.setName("jScrollPane17"); // NOI18N

        Tablatro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "OP", "Fecha Entrega", "Maquila","Imp.", "Cant. Pedida", "Clave Articulo", "Articulo", "Resis.", "<html><font color=#0066FF>Fecha Prod.", "<html><font color=#0066FF>Golpes", "<html><font color=#0066FF>KG", "<html><font color=#0066FF>Arreglo", "<html><font color=#0066FF>Vel.", "<html><font color=#0066FF>H. T.", "<html><font color=green>Fecha Prod", "<html><font color=green>Golpes Prod", "<html><font color=green>KG Prod", "Cliente", "Prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.util.Date.class, java.lang.Integer.class, java.lang.Integer.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablatro.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablatro.setName("Tablatro"); // NOI18N
        Tablatro.setRowHeight(22);
        Tablatro.getTableHeader().setReorderingAllowed(false);
        Tablatro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablatroMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(Tablatro);

        subpaneltro.setBackground(resourceMap.getColor("subpaneltro.background")); // NOI18N
        subpaneltro.setName("subpaneltro"); // NOI18N

        jLabel18.setFont(resourceMap.getFont("jLabel18.font")); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setIcon(resourceMap.getIcon("jLabel18.icon")); // NOI18N
        jLabel18.setToolTipText(resourceMap.getString("jLabel18.toolTipText")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        buscartro.setName("buscartro"); // NOI18N
        buscartro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscartroFocusGained(evt);
            }
        });
        buscartro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscartroKeyReleased(evt);
            }
        });

        btnexportartro.setIcon(resourceMap.getIcon("btnexportartro.icon")); // NOI18N
        btnexportartro.setText(resourceMap.getString("btnexportartro.text")); // NOI18N
        btnexportartro.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnexportartro.setName("btnexportartro"); // NOI18N
        btnexportartro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportartroActionPerformed(evt);
            }
        });

        jButton16.setIcon(resourceMap.getIcon("jButton16.icon")); // NOI18N
        jButton16.setText(resourceMap.getString("jButton16.text")); // NOI18N
        jButton16.setMargin(new java.awt.Insets(0, 4, 0, 4));
        jButton16.setName("jButton16"); // NOI18N
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subpaneltroLayout = new javax.swing.GroupLayout(subpaneltro);
        subpaneltro.setLayout(subpaneltroLayout);
        subpaneltroLayout.setHorizontalGroup(
            subpaneltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpaneltroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscartro, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(jButton16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnexportartro, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        subpaneltroLayout.setVerticalGroup(
            subpaneltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subpaneltroLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subpaneltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subpaneltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnexportartro)
                        .addComponent(jButton16))
                    .addGroup(subpaneltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buscartro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout troLayout = new javax.swing.GroupLayout(tro);
        tro.setLayout(troLayout);
        troLayout.setHorizontalGroup(
            troLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(subpaneltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        troLayout.setVerticalGroup(
            troLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, troLayout.createSequentialGroup()
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpaneltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("tro.TabConstraints.tabTitle"), null, tro, resourceMap.getString("tro.TabConstraints.tabToolTip")); // NOI18N
        tro.getAccessibleContext().setAccessibleDescription(resourceMap.getString("tro.AccessibleContext.accessibleDescription")); // NOI18N

        jToolBar1.setBackground(resourceMap.getColor("jToolBar1.background")); // NOI18N
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel4.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(556, 48));

        btnactualizar.setIcon(resourceMap.getIcon("btnactualizar.icon")); // NOI18N
        btnactualizar.setText(resourceMap.getString("btnactualizar.text")); // NOI18N
        btnactualizar.setToolTipText(resourceMap.getString("btnactualizar.toolTipText")); // NOI18N
        btnactualizar.setFocusable(false);
        btnactualizar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnactualizar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnactualizar.setMaximumSize(new java.awt.Dimension(70, 48));
        btnactualizar.setMinimumSize(new java.awt.Dimension(70, 48));
        btnactualizar.setName("btnactualizar"); // NOI18N
        btnactualizar.setPreferredSize(new java.awt.Dimension(70, 48));
        btnactualizar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });

        btn_tintas.setIcon(resourceMap.getIcon("btn_tintas.icon")); // NOI18N
        btn_tintas.setText(resourceMap.getString("btn_tintas.text")); // NOI18N
        btn_tintas.setFocusable(false);
        btn_tintas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_tintas.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btn_tintas.setMaximumSize(new java.awt.Dimension(80, 48));
        btn_tintas.setMinimumSize(new java.awt.Dimension(80, 48));
        btn_tintas.setName("btn_tintas"); // NOI18N
        btn_tintas.setPreferredSize(new java.awt.Dimension(80, 48));
        btn_tintas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_tintas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tintasActionPerformed(evt);
            }
        });

        btnimprimirmaq.setIcon(resourceMap.getIcon("btnimprimirmaq.icon")); // NOI18N
        btnimprimirmaq.setText(resourceMap.getString("btnimprimirmaq.text")); // NOI18N
        btnimprimirmaq.setToolTipText(resourceMap.getString("btnimprimirmaq.toolTipText")); // NOI18N
        btnimprimirmaq.setFocusable(false);
        btnimprimirmaq.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnimprimirmaq.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnimprimirmaq.setMaximumSize(new java.awt.Dimension(70, 48));
        btnimprimirmaq.setMinimumSize(new java.awt.Dimension(70, 48));
        btnimprimirmaq.setName("btnimprimirmaq"); // NOI18N
        btnimprimirmaq.setPreferredSize(new java.awt.Dimension(70, 48));
        btnimprimirmaq.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnimprimirmaq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirmaqActionPerformed(evt);
            }
        });

        btnimprimir.setIcon(resourceMap.getIcon("btnimprimir.icon")); // NOI18N
        btnimprimir.setText(resourceMap.getString("btnimprimir.text")); // NOI18N
        btnimprimir.setToolTipText(resourceMap.getString("btnimprimir.toolTipText")); // NOI18N
        btnimprimir.setFocusable(false);
        btnimprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnimprimir.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnimprimir.setMaximumSize(new java.awt.Dimension(70, 48));
        btnimprimir.setMinimumSize(new java.awt.Dimension(70, 48));
        btnimprimir.setName("btnimprimir"); // NOI18N
        btnimprimir.setPreferredSize(new java.awt.Dimension(70, 48));
        btnimprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirActionPerformed(evt);
            }
        });

        jSeparator1.setName("jSeparator1"); // NOI18N

        jSeparator2.setName("jSeparator2"); // NOI18N

        jSeparator3.setName("jSeparator3"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnimprimirmaq, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_tintas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnactualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(395, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnactualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tintas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnimprimirmaq, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnimprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jToolBar1.add(jPanel4);

        noregistros.setFont(resourceMap.getFont("noregistros.font")); // NOI18N
        noregistros.setForeground(resourceMap.getColor("noregistros.foreground")); // NOI18N
        noregistros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noregistros.setText(resourceMap.getString("noregistros.text")); // NOI18N
        noregistros.setName("noregistros"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(noregistros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noregistros)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    //TCY
    private void detalleoptcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleoptcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablatcy.getSelectedRow();
        String claveop=""+Tablatcy.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleoptcyActionPerformed

    private void TablatcyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablatcyMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablatcy.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablatcy.changeSelection(rowNumber, 0, false, false);
            menupoptcy.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablatcyMouseClicked

    private void buscartcyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscartcyKeyReleased
        // TODO add your handling code here:
        if(buscartcy.getText().equals("")){
            Tablatcy.setRowSorter(null);
            buscartcy.setText("");
            Tablatcy.setAutoCreateRowSorter(true);
            limpiatablatcy();
            datostcy();
        }else{
            TableRowSorter orden = new TableRowSorter(modelotcy);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscartcy.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablatcy.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscartcyKeyReleased

    private void btnexportartcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportartcyActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablatcy, "Planeación y programación de la producción (TCY)", "corrflex_tcy.xls");
}//GEN-LAST:event_btnexportartcyActionPerformed

    private void detallearttcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detallearttcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablatcy.getSelectedRow();
        String claveart=""+Tablatcy.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detallearttcyActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        // TODO add your handling code here:
        actualiza();
}//GEN-LAST:event_btnactualizarActionPerformed

    private void btnimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimirActionPerformed
        // TODO add your handling code here:
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);

        JasperViewer visor=null;
        JasperPrint jasperPrint =null;
        try{
            //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio()+"/logoempresa.png");
            pars.put("folio",null);
            pars.put("logoempresa",new FileInputStream(fichero));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("senSQL", "");//SQL dinamico
            pars.put("version", resourceMap.getString("Application.title"));
            JasperReport masterReport = null;
            try{
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/corrflex.jasper"));
             }
             catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

            jasperPrint = JasperFillManager.fillReport(masterReport,pars,conn);
            visor = new JasperViewer(jasperPrint,false);
            visor.setTitle("REPORTE");
            visor.setVisible(true);
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }
}//GEN-LAST:event_btnimprimirActionPerformed

    private void buscartcyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscartcyFocusGained
        // TODO add your handling code here:
        buscartcy.selectAll();
    }//GEN-LAST:event_buscartcyFocusGained

    private void buscarcorrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarcorrFocusGained
        // TODO add your handling code here:
        buscarcorr.selectAll();
}//GEN-LAST:event_buscarcorrFocusGained

    private void buscarcorrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarcorrKeyReleased
        // TODO add your handling code here:
        if(buscarcorr.getText().equals("")){
            Tablacorr.setRowSorter(null);
            buscarcorr.setText("");
            Tablacorr.setAutoCreateRowSorter(true);
            limpiatablacorr();
            datoscorr();
        }else{
            TableRowSorter orden = new TableRowSorter(modelocorr);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarcorr.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablacorr.setRowSorter(orden);
            //numero de registros en la consulta
        }
}//GEN-LAST:event_buscarcorrKeyReleased

    private void btnexportarcorrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarcorrActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablacorr, "Planeación y programación de la producción (Corrugadora)", "corrflex_corr.xls");
}//GEN-LAST:event_btnexportarcorrActionPerformed

    private void TablacorrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablacorrMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablacorr.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablacorr.changeSelection(rowNumber, 0, false, false);
            menupopcorr.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_TablacorrMouseClicked

    private void detalleopcorrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopcorrActionPerformed
        // TODO add your handling code here:
        int filano=Tablacorr.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String claveop=""+Tablacorr.getValueAt(filano, 1);
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            verop(claveop);
        }
}//GEN-LAST:event_detalleopcorrActionPerformed

    private void detalleartcorrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartcorrActionPerformed
        // TODO add your handling code here:
        int filano=Tablacorr.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String claveart=""+Tablacorr.getValueAt(filano, 5);
            int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
            if(claveart.contains("<html>")){
                claveart=claveart.substring(dotPos);
            }
            verarticulo(claveart);
        }
}//GEN-LAST:event_detalleartcorrActionPerformed

    private void programoptcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programoptcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablatcy.getSelectedRow();
        String claveop=""+Tablatcy.getValueAt(filano, 1);
        String programaconversion=""+Tablatcy.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"TCY","");
        }
}//GEN-LAST:event_programoptcyActionPerformed

    private void eliminarprogtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablatcy.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablatcy.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablatcy.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_eliminarprogtcyActionPerformed

    private void terminaprogtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablatcy.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablatcy.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablatcy.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_terminaprogtcyActionPerformed

    private void cambiarprogtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablatcy.getSelectedRow();
        String claveop=""+Tablatcy.getValueAt(filano, 1);
        String programaconversion=""+Tablatcy.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"TCY",programaconversion);
        }
    }//GEN-LAST:event_cambiarprogtcyActionPerformed

    private void TablasysMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablasysMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablasys.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablasys.changeSelection(rowNumber, 0, false, false);
            menupopsys.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablasysMouseClicked

    private void buscarsysFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarsysFocusGained
        // TODO add your handling code here:
        buscarsys.selectAll();
}//GEN-LAST:event_buscarsysFocusGained

    private void buscarsysKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarsysKeyReleased
        // TODO add your handling code here:
        if(buscarsys.getText().equals("")){
            Tablasys.setRowSorter(null);
            buscarsys.setText("");
            Tablasys.setAutoCreateRowSorter(true);
            limpiatablasys();
            datossys();
        }else{
            TableRowSorter orden = new TableRowSorter(modelosys);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarsys.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablasys.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarsysKeyReleased

    private void btnexportarsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarsysActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablasys, "Planeación y programación de la producción (MACARB)", "corrflex_macarb.xls");
}//GEN-LAST:event_btnexportarsysActionPerformed

    private void detalleopsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablasys.getSelectedRow();
        String claveop=""+Tablasys.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleopsysActionPerformed

    private void detalleartsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablasys.getSelectedRow();
        String claveart=""+Tablasys.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartsysActionPerformed

    private void programopsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programopsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablasys.getSelectedRow();
        String claveop=""+Tablasys.getValueAt(filano, 1);
        String programaconversion=""+Tablasys.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"MACARB","");
        }
}//GEN-LAST:event_programopsysActionPerformed

    private void cambiarprogsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablasys.getSelectedRow();
        String claveop=""+Tablasys.getValueAt(filano, 1);
        String programaconversion=""+Tablasys.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"MACARB",programaconversion);
        }
}//GEN-LAST:event_cambiarprogsysActionPerformed

    private void terminaprogsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablasys.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablasys.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablasys.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
        
        
}//GEN-LAST:event_terminaprogsysActionPerformed

    private void eliminarprogsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablasys.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablasys.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablasys.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogsysActionPerformed

    private void TablalinealtcyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablalinealtcyMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablalinealtcy.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablalinealtcy.changeSelection(rowNumber, 0, false, false);
            menupoplinealtcy.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablalinealtcyMouseClicked

    private void buscarlinealtcyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarlinealtcyFocusGained
        // TODO add your handling code here:
        buscarlinealtcy.selectAll();
}//GEN-LAST:event_buscarlinealtcyFocusGained

    private void buscarlinealtcyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarlinealtcyKeyReleased
        // TODO add your handling code here:
        if(buscarlinealtcy.getText().equals("")){
            Tablalinealtcy.setRowSorter(null);
            buscarlinealtcy.setText("");
            Tablalinealtcy.setAutoCreateRowSorter(true);
            limpiatablalinealtcy();
            datoslinealtcy();
        }else{
            TableRowSorter orden = new TableRowSorter(modelolinealtcy);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarlinealtcy.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablalinealtcy.setRowSorter(orden);
            //numero de registros en la consulta
        }
}//GEN-LAST:event_buscarlinealtcyKeyReleased

    private void btnexportarlinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarlinealtcyActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablalinealtcy, "Planeación y programación de la producción (PLTCY)", "corrflex_ltcy.xls");
}//GEN-LAST:event_btnexportarlinealtcyActionPerformed

    private void detalleoplinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleoplinealtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealtcy.getSelectedRow();
        String claveop=""+Tablalinealtcy.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleoplinealtcyActionPerformed

    private void detalleartlinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartlinealtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealtcy.getSelectedRow();
        String claveart=""+Tablalinealtcy.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartlinealtcyActionPerformed

    private void programoplinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programoplinealtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealtcy.getSelectedRow();
        String claveop=""+Tablalinealtcy.getValueAt(filano, 1);
        String programaconversion=""+Tablalinealtcy.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"PLTCY","");
        }
}//GEN-LAST:event_programoplinealtcyActionPerformed

    private void cambiarproglinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarproglinealtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealtcy.getSelectedRow();
        String claveop=""+Tablalinealtcy.getValueAt(filano, 1);
        String programaconversion=""+Tablalinealtcy.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"PLTCY",programaconversion);
        }
}//GEN-LAST:event_cambiarproglinealtcyActionPerformed

    private void terminaproglinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaproglinealtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealtcy.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablalinealtcy.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablalinealtcy.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaproglinealtcyActionPerformed

    private void eliminarproglinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarproglinealtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealtcy.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablalinealtcy.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablalinealtcy.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarproglinealtcyActionPerformed

    private void TablalinealsysMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablalinealsysMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablalinealsys.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablalinealsys.changeSelection(rowNumber, 0, false, false);
            menupoplinealsys.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablalinealsysMouseClicked

    private void buscarlinealsysFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarlinealsysFocusGained
        // TODO add your handling code here:
        buscarlinealsys.selectAll();
}//GEN-LAST:event_buscarlinealsysFocusGained

    private void buscarlinealsysKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarlinealsysKeyReleased
        // TODO add your handling code here:
        if(buscarlinealsys.getText().equals("")){
            Tablalinealsys.setRowSorter(null);
            buscarlinealsys.setText("");
            Tablalinealsys.setAutoCreateRowSorter(true);
            limpiatablalinealsys();
            datoslinealsys();
        }else{
            TableRowSorter orden = new TableRowSorter(modelolinealsys);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarlinealsys.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablalinealsys.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarlinealsysKeyReleased

    private void btnexportarlinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarlinealsysActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablalinealsys, "Planeación y programación de la producción (PLMACARB)", "corrflex_lsys.xls");
}//GEN-LAST:event_btnexportarlinealsysActionPerformed

    private void detalleoplinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleoplinealsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealsys.getSelectedRow();
        String claveop=""+Tablalinealsys.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleoplinealsysActionPerformed

    private void detalleartlinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartlinealsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealsys.getSelectedRow();
        String claveart=""+Tablalinealsys.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartlinealsysActionPerformed

    private void programoplinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programoplinealsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealsys.getSelectedRow();
        String claveop=""+Tablalinealsys.getValueAt(filano, 1);
        String programaconversion=""+Tablalinealsys.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"PLMACARB","");
        }
}//GEN-LAST:event_programoplinealsysActionPerformed

    private void cambiarproglinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarproglinealsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealsys.getSelectedRow();
        String claveop=""+Tablalinealsys.getValueAt(filano, 1);
        String programaconversion=""+Tablalinealsys.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"PLMACARB",programaconversion);
        }
}//GEN-LAST:event_cambiarproglinealsysActionPerformed

    private void terminaproglinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaproglinealsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealsys.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablalinealsys.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablalinealsys.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaproglinealsysActionPerformed

    private void eliminarproglinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarproglinealsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealsys.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablalinealsys.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablalinealsys.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarproglinealsysActionPerformed

    private void TablacomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablacomMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablacom.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablacom.changeSelection(rowNumber, 0, false, false);
            menupopcom.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablacomMouseClicked

    private void buscarcomFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarcomFocusGained
        // TODO add your handling code here:
        buscarcom.selectAll();
}//GEN-LAST:event_buscarcomFocusGained

    private void buscarcomKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarcomKeyReleased
        // TODO add your handling code here:
        if(buscarcom.getText().equals("")){
            Tablacom.setRowSorter(null);
            buscarcom.setText("");
            Tablacom.setAutoCreateRowSorter(true);
            limpiatablacom();
            datoscom();
        }else{
            TableRowSorter orden = new TableRowSorter(modelocom);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarcom.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablacom.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarcomKeyReleased

    private void btnexportarcomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarcomActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablacom, "Planeación y programación de la producción (COMET)", "corrflex_com.xls");
}//GEN-LAST:event_btnexportarcomActionPerformed

    private void detalleopcomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopcomActionPerformed
        // TODO add your handling code here:
        int filano=Tablacom.getSelectedRow();
        String claveop=""+Tablacom.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleopcomActionPerformed

    private void detalleartcomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartcomActionPerformed
        // TODO add your handling code here:
        int filano=Tablacom.getSelectedRow();
        String claveart=""+Tablacom.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartcomActionPerformed

    private void programopcomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programopcomActionPerformed
        // TODO add your handling code here:
        int filano=Tablacom.getSelectedRow();
        String claveop=""+Tablacom.getValueAt(filano, 1);
        String programaconversion=""+Tablacom.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"COM","");
        }
}//GEN-LAST:event_programopcomActionPerformed

    private void cambiarprogcomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogcomActionPerformed
        // TODO add your handling code here:
        int filano=Tablacom.getSelectedRow();
        String claveop=""+Tablacom.getValueAt(filano, 1);
        String programaconversion=""+Tablacom.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"COM",programaconversion);
        }
}//GEN-LAST:event_cambiarprogcomActionPerformed

    private void terminaprogcomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogcomActionPerformed
        // TODO add your handling code here:
        int filano=Tablacom.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablacom.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablacom.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprogcomActionPerformed

    private void eliminarprogcomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogcomActionPerformed
        // TODO add your handling code here:
        int filano=Tablacom.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablacom.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablacom.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogcomActionPerformed

    private void TablaengMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaengMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablaeng.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablaeng.changeSelection(rowNumber, 0, false, false);
            menupopeng.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablaengMouseClicked

    private void buscarengFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarengFocusGained
        // TODO add your handling code here:
        buscareng.selectAll();
}//GEN-LAST:event_buscarengFocusGained

    private void buscarengKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarengKeyReleased
        // TODO add your handling code here:
        if(buscareng.getText().equals("")){
            Tablaeng.setRowSorter(null);
            buscareng.setText("");
            Tablaeng.setAutoCreateRowSorter(true);
            limpiatablaeng();
            datoseng();
        }else{
            TableRowSorter orden = new TableRowSorter(modeloeng);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscareng.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablaeng.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarengKeyReleased

    private void btnexportarengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarengActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablaeng, "Planeación y programación de la producción (ENG)", "corrflex_eng.xls");
}//GEN-LAST:event_btnexportarengActionPerformed

    private void detalleopengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopengActionPerformed
        // TODO add your handling code here:
        int filano=Tablaeng.getSelectedRow();
        String claveop=""+Tablaeng.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleopengActionPerformed

    private void detalleartengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartengActionPerformed
        // TODO add your handling code here:
        int filano=Tablaeng.getSelectedRow();
        String claveart=""+Tablaeng.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartengActionPerformed

    private void programopengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programopengActionPerformed
        // TODO add your handling code here:
        int filano=Tablaeng.getSelectedRow();
        String claveop=""+Tablaeng.getValueAt(filano, 1);
        String programaconversion=""+Tablaeng.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"ENG","");
        }
}//GEN-LAST:event_programopengActionPerformed

    private void cambiarprogengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogengActionPerformed
        // TODO add your handling code here:
        int filano=Tablaeng.getSelectedRow();
        String claveop=""+Tablaeng.getValueAt(filano, 1);
        String programaconversion=""+Tablaeng.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"ENG",programaconversion);
        }
}//GEN-LAST:event_cambiarprogengActionPerformed

    private void terminaprogengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogengActionPerformed
        // TODO add your handling code here:
        int filano=Tablaeng.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablaeng.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablaeng.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprogengActionPerformed

    private void eliminarprogengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogengActionPerformed
        // TODO add your handling code here:
        int filano=Tablaeng.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablaeng.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablaeng.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogengActionPerformed

    private void TablamarvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablamarvMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablamarv.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablamarv.changeSelection(rowNumber, 0, false, false);
            menupopmarv.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablamarvMouseClicked

    private void buscarmarvFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarmarvFocusGained
        // TODO add your handling code here:
        buscarmarv.selectAll();
}//GEN-LAST:event_buscarmarvFocusGained

    private void buscarmarvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarmarvKeyReleased
        // TODO add your handling code here:
        if(buscarmarv.getText().equals("")){
            Tablamarv.setRowSorter(null);
            buscarmarv.setText("");
            Tablamarv.setAutoCreateRowSorter(true);
            limpiatablamarv();
            datosmarv();
        }else{
            TableRowSorter orden = new TableRowSorter(modelomarv);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarmarv.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablamarv.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarmarvKeyReleased

    private void btnexportarmarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarmarvActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablamarv, "Planeación y programación de la producción (MARVI)", "corrflex_marv.xls");
}//GEN-LAST:event_btnexportarmarvActionPerformed

    private void detalleopmarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopmarvActionPerformed
        // TODO add your handling code here:
        int filano=Tablamarv.getSelectedRow();
        String claveop=""+Tablamarv.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleopmarvActionPerformed

    private void detalleartmarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartmarvActionPerformed
        // TODO add your handling code here:
        int filano=Tablamarv.getSelectedRow();
        String claveart=""+Tablamarv.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartmarvActionPerformed

    private void programopmarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programopmarvActionPerformed
        // TODO add your handling code here:
        int filano=Tablamarv.getSelectedRow();
        String claveop=""+Tablamarv.getValueAt(filano, 1);
        String programaconversion=""+Tablamarv.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"MARVI","");
        }
}//GEN-LAST:event_programopmarvActionPerformed

    private void cambiarprogmarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogmarvActionPerformed
        // TODO add your handling code here:
        int filano=Tablamarv.getSelectedRow();
        String claveop=""+Tablamarv.getValueAt(filano, 1);
        String programaconversion=""+Tablamarv.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"MARVI",programaconversion);
        }
}//GEN-LAST:event_cambiarprogmarvActionPerformed

    private void terminaprogmarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogmarvActionPerformed
        // TODO add your handling code here:
        int filano=Tablamarv.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablamarv.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablamarv.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprogmarvActionPerformed

    private void eliminarprogmarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogmarvActionPerformed
        // TODO add your handling code here:
        int filano=Tablamarv.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablamarv.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablamarv.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogmarvActionPerformed

    private void TablasuapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablasuapMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablasuap.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablasuap.changeSelection(rowNumber, 0, false, false);
            menupopsuap.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablasuapMouseClicked

    private void buscarsuapFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarsuapFocusGained
        // TODO add your handling code here:
        buscarsuap.selectAll();
}//GEN-LAST:event_buscarsuapFocusGained

    private void buscarsuapKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarsuapKeyReleased
        // TODO add your handling code here:
        if(buscarsuap.getText().equals("")){
            Tablasuap.setRowSorter(null);
            buscarsuap.setText("");
            Tablasuap.setAutoCreateRowSorter(true);
            limpiatablasuap();
            datossuap();
        }else{
            TableRowSorter orden = new TableRowSorter(modelosuap);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarsuap.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablasuap.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarsuapKeyReleased

    private void btnexportarsuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarsuapActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablasuap, "Planeación y programación de la producción (SUAJE PLANO)", "corrflex_suap.xls");
}//GEN-LAST:event_btnexportarsuapActionPerformed

    private void detalleopsuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopsuapActionPerformed
        // TODO add your handling code here:
        int filano=Tablasuap.getSelectedRow();
        String claveop=""+Tablasuap.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleopsuapActionPerformed

    private void detalleartsuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartsuapActionPerformed
        // TODO add your handling code here:
        int filano=Tablasuap.getSelectedRow();
        String claveart=""+Tablasuap.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartsuapActionPerformed

    private void programopsuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programopsuapActionPerformed
        // TODO add your handling code here:
        int filano=Tablasuap.getSelectedRow();
        String claveop=""+Tablasuap.getValueAt(filano, 1);
        String programaconversion=""+Tablasuap.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"SUAP","");
        }
}//GEN-LAST:event_programopsuapActionPerformed

    private void cambiarprogsuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogsuapActionPerformed
        // TODO add your handling code here:
        int filano=Tablasuap.getSelectedRow();
        String claveop=""+Tablasuap.getValueAt(filano, 1);
        String programaconversion=""+Tablasuap.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"SUAP",programaconversion);
        }
}//GEN-LAST:event_cambiarprogsuapActionPerformed

    private void terminaprogsuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogsuapActionPerformed
        // TODO add your handling code here:
        int filano=Tablasuap.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablasuap.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablasuap.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprogsuapActionPerformed

    private void eliminarprogsuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogsuapActionPerformed
        // TODO add your handling code here:
        int filano=Tablasuap.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablasuap.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablasuap.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogsuapActionPerformed

    private void TabladiecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladiecMouseClicked
        // TODO add your handling code here:
         if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tabladiec.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tabladiec.changeSelection(rowNumber, 0, false, false);
            menupopdiec.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TabladiecMouseClicked

    private void buscardiecFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscardiecFocusGained
        // TODO add your handling code here:
        buscardiec.selectAll();
}//GEN-LAST:event_buscardiecFocusGained

    private void buscardiecKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscardiecKeyReleased
        // TODO add your handling code here:
        if(buscardiec.getText().equals("")){
            Tabladiec.setRowSorter(null);
            buscardiec.setText("");
            Tabladiec.setAutoCreateRowSorter(true);
            limpiatabladiec();
            datosdiec();
        }else{
            TableRowSorter orden = new TableRowSorter(modelodiec);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscardiec.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladiec.setRowSorter(orden);
        }
}//GEN-LAST:event_buscardiecKeyReleased

    private void btnexportardiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportardiecActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tabladiec, "Planeación y programación de la producción (DIE CUTTER)", "corrflex_diec.xls");
}//GEN-LAST:event_btnexportardiecActionPerformed

    private void detalleopdiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopdiecActionPerformed
        // TODO add your handling code here:
        int filano=Tabladiec.getSelectedRow();
        String claveop=""+Tabladiec.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleopdiecActionPerformed

    private void detalleartdiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartdiecActionPerformed
        // TODO add your handling code here:
        int filano=Tabladiec.getSelectedRow();
        String claveart=""+Tabladiec.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartdiecActionPerformed

    private void programopdiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programopdiecActionPerformed
        // TODO add your handling code here:
        int filano=Tabladiec.getSelectedRow();
        String claveop=""+Tabladiec.getValueAt(filano, 1);
        String programaconversion=""+Tabladiec.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"DIEC","");
        }
}//GEN-LAST:event_programopdiecActionPerformed

    private void cambiarprogdiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogdiecActionPerformed
        // TODO add your handling code here:
        int filano=Tabladiec.getSelectedRow();
        String claveop=""+Tabladiec.getValueAt(filano, 1);
        String programaconversion=""+Tabladiec.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"DIEC",programaconversion);
        }
}//GEN-LAST:event_cambiarprogdiecActionPerformed

    private void terminaprogdiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogdiecActionPerformed
        // TODO add your handling code here:
        int filano=Tabladiec.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tabladiec.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tabladiec.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprogdiecActionPerformed

    private void eliminarprogdiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogdiecActionPerformed
        // TODO add your handling code here:
        int filano=Tabladiec.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tabladiec.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tabladiec.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogdiecActionPerformed

    private void TablarayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablarayMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablaray.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablaray.changeSelection(rowNumber, 0, false, false);
            menupopray.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablarayMouseClicked

    private void buscarrayFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarrayFocusGained
        // TODO add your handling code here:
        buscarray.selectAll();
}//GEN-LAST:event_buscarrayFocusGained

    private void buscarrayKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarrayKeyReleased
        // TODO add your handling code here:
        if(buscarray.getText().equals("")){
            Tablaray.setRowSorter(null);
            buscarray.setText("");
            Tablaray.setAutoCreateRowSorter(true);
            limpiatablaray();
            datosray();
        }else{
            TableRowSorter orden = new TableRowSorter(modeloray);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarray.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablaray.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarrayKeyReleased

    private void btnexportarrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarrayActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablaray, "Planeación y programación de la producción (RAYADORA)", "corrflex_ray.xls");
}//GEN-LAST:event_btnexportarrayActionPerformed

    private void detalleoprayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleoprayActionPerformed
        // TODO add your handling code here:
        int filano=Tablaray.getSelectedRow();
        String claveop=""+Tablaray.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleoprayActionPerformed

    private void detalleartrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartrayActionPerformed
        // TODO add your handling code here:
        int filano=Tablaray.getSelectedRow();
        String claveart=""+Tablaray.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartrayActionPerformed

    private void programoprayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programoprayActionPerformed
        // TODO add your handling code here:
        int filano=Tablaray.getSelectedRow();
        String claveop=""+Tablaray.getValueAt(filano, 1);
        String programaconversion=""+Tablaray.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"RAY","");
        }
}//GEN-LAST:event_programoprayActionPerformed

    private void cambiarprograyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprograyActionPerformed
        // TODO add your handling code here:
        int filano=Tablaray.getSelectedRow();
        String claveop=""+Tablaray.getValueAt(filano, 1);
        String programaconversion=""+Tablaray.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"RAY",programaconversion);
        }
}//GEN-LAST:event_cambiarprograyActionPerformed

    private void terminaprograyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprograyActionPerformed
        // TODO add your handling code here:
        int filano=Tablaray.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablaray.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablaray.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprograyActionPerformed

    private void eliminarprograyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprograyActionPerformed
        // TODO add your handling code here:
        int filano=Tablaray.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablaray.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablaray.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprograyActionPerformed

    private void TablapdoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablapdoMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablapdo.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablapdo.changeSelection(rowNumber, 0, false, false);
            menupoppdo.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablapdoMouseClicked

    private void buscarpdoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarpdoFocusGained
        // TODO add your handling code here:
        buscarpdo.selectAll();
}//GEN-LAST:event_buscarpdoFocusGained

    private void buscarpdoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarpdoKeyReleased
        // TODO add your handling code here:
        if(buscarpdo.getText().equals("")){
            Tablapdo.setRowSorter(null);
            buscarpdo.setText("");
            Tablapdo.setAutoCreateRowSorter(true);
            limpiatablapdo();
            datospdo();
        }else{
            TableRowSorter orden = new TableRowSorter(modelopdo);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarpdo.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablapdo.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarpdoKeyReleased

    private void btnexportarpdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarpdoActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablapdo, "Planeación y programación de la producción (EMPLAYADORA)", "corrflex_pdo.xls");
}//GEN-LAST:event_btnexportarpdoActionPerformed

    private void detalleoppdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleoppdoActionPerformed
        // TODO add your handling code here:
        int filano=Tablapdo.getSelectedRow();
        String claveop=""+Tablapdo.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleoppdoActionPerformed

    private void detalleartpdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartpdoActionPerformed
        // TODO add your handling code here:
        int filano=Tablapdo.getSelectedRow();
        String claveart=""+Tablapdo.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartpdoActionPerformed

    private void programoppdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programoppdoActionPerformed
        // TODO add your handling code here:
        int filano=Tablapdo.getSelectedRow();
        String claveop=""+Tablapdo.getValueAt(filano, 1);
        String programaconversion=""+Tablapdo.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"PDO","");
        }
}//GEN-LAST:event_programoppdoActionPerformed

    private void cambiarprogpdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogpdoActionPerformed
        // TODO add your handling code here:
        int filano=Tablapdo.getSelectedRow();
        String claveop=""+Tablapdo.getValueAt(filano, 1);
        String programaconversion=""+Tablapdo.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"PDO",programaconversion);
        }
}//GEN-LAST:event_cambiarprogpdoActionPerformed

    private void terminaprogpdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogpdoActionPerformed
        // TODO add your handling code here:
        int filano=Tablapdo.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablapdo.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablapdo.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprogpdoActionPerformed

    private void eliminarprogpdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogpdoActionPerformed
        // TODO add your handling code here:
        int filano=Tablapdo.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablapdo.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablapdo.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogpdoActionPerformed

    private void TabladesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladesMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablades.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablades.changeSelection(rowNumber, 0, false, false);
            menupopdes.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TabladesMouseClicked

    private void buscardesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscardesFocusGained
        // TODO add your handling code here:
        buscardes.selectAll();
}//GEN-LAST:event_buscardesFocusGained

    private void buscardesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscardesKeyReleased
        // TODO add your handling code here:
        if(buscardes.getText().equals("")){
            Tablades.setRowSorter(null);
            buscardes.setText("");
            Tablades.setAutoCreateRowSorter(true);
            limpiatablades();
            datosdes();
        }else{
            TableRowSorter orden = new TableRowSorter(modelodes);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscardes.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablades.setRowSorter(orden);
        }
}//GEN-LAST:event_buscardesKeyReleased

    private void btnexportardesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportardesActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablades, "Planeación y programación de la producción (DESMERMADO)", "corrflex_des.xls");
}//GEN-LAST:event_btnexportardesActionPerformed

    private void detalleopdesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopdesActionPerformed
        // TODO add your handling code here:
        int filano=Tablades.getSelectedRow();
        String claveop=""+Tablades.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleopdesActionPerformed

    private void detalleartdesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartdesActionPerformed
        // TODO add your handling code here:
        int filano=Tablades.getSelectedRow();
        String claveart=""+Tablades.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartdesActionPerformed

    private void programoppdesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programoppdesActionPerformed
        // TODO add your handling code here:
        int filano=Tablades.getSelectedRow();
        String claveop=""+Tablades.getValueAt(filano, 1);
        String programaconversion=""+Tablades.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"DES","");
        }
}//GEN-LAST:event_programoppdesActionPerformed

    private void cambiarprogdesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogdesActionPerformed
        // TODO add your handling code here:
        int filano=Tablades.getSelectedRow();
        String claveop=""+Tablades.getValueAt(filano, 1);
        String programaconversion=""+Tablades.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"DES",programaconversion);
        }
}//GEN-LAST:event_cambiarprogdesActionPerformed

    private void terminaprogdesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogdesActionPerformed
        // TODO add your handling code here:
        int filano=Tablades.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablades.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablades.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprogdesActionPerformed

    private void eliminarprogdesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogdesActionPerformed
        // TODO add your handling code here:
        int filano=Tablades.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablades.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablades.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogdesActionPerformed

    private void TablamanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablamanMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablaman.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablaman.changeSelection(rowNumber, 0, false, false);
            menupopman.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablamanMouseClicked

    private void buscarmanFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarmanFocusGained
        // TODO add your handling code here:
        buscarman.selectAll();
}//GEN-LAST:event_buscarmanFocusGained

    private void buscarmanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarmanKeyReleased
        // TODO add your handling code here:
        if(buscarman.getText().equals("")){
            Tablaman.setRowSorter(null);
            buscarman.setText("");
            Tablaman.setAutoCreateRowSorter(true);
            limpiatablaman();
            datosman();
        }else{
            TableRowSorter orden = new TableRowSorter(modeloman);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarman.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablaman.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarmanKeyReleased

    private void btnexportarmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarmanActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablaman, "Planeación y programación de la producción (MANUAL)", "corrflex_man.xls");
}//GEN-LAST:event_btnexportarmanActionPerformed

    private void TablafonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablafonMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablafon.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablafon.changeSelection(rowNumber, 0, false, false);
            menupopfon.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_TablafonMouseClicked

    private void buscarfonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarfonFocusGained
        // TODO add your handling code here:
        buscarfon.selectAll();
}//GEN-LAST:event_buscarfonFocusGained

    private void buscarfonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarfonKeyReleased
        // TODO add your handling code here:
        if(buscarfon.getText().equals("")){
            Tablafon.setRowSorter(null);
            buscarfon.setText("");
            Tablafon.setAutoCreateRowSorter(true);
            limpiatablafon();
            datosfon();
        }else{
            TableRowSorter orden = new TableRowSorter(modelofon);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarfon.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablafon.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarfonKeyReleased

    private void btnexportarfonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarfonActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablafon, "Planeación y programación de la producción (FONDEDEADO)", "corrflex_fon.xls");
}//GEN-LAST:event_btnexportarfonActionPerformed

    private void detalleopmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopmanActionPerformed
        // TODO add your handling code here:
        int filano=Tablaman.getSelectedRow();
        String claveop=""+Tablaman.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleopmanActionPerformed

    private void detalleartmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartmanActionPerformed
        // TODO add your handling code here:
        int filano=Tablaman.getSelectedRow();
        String claveart=""+Tablaman.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartmanActionPerformed

    private void programoppmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programoppmanActionPerformed
        // TODO add your handling code here:
        int filano=Tablaman.getSelectedRow();
        String claveop=""+Tablaman.getValueAt(filano, 1);
        String programaconversion=""+Tablaman.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"MAN","");
        }
}//GEN-LAST:event_programoppmanActionPerformed

    private void cambiarprogmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogmanActionPerformed
        // TODO add your handling code here:
        int filano=Tablaman.getSelectedRow();
        String claveop=""+Tablaman.getValueAt(filano, 1);
        String programaconversion=""+Tablaman.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"MAN",programaconversion);
        }
}//GEN-LAST:event_cambiarprogmanActionPerformed

    private void terminaprogmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogmanActionPerformed
        // TODO add your handling code here:
        int filano=Tablaman.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablaman.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablaman.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprogmanActionPerformed

    private void eliminarprogmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogmanActionPerformed
        // TODO add your handling code here:
        int filano=Tablaman.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablaman.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablaman.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogmanActionPerformed

    private void detalleopfonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopfonActionPerformed
        // TODO add your handling code here:
        int filano=Tablafon.getSelectedRow();
        String claveop=""+Tablafon.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
}//GEN-LAST:event_detalleopfonActionPerformed

    private void detalleartfonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartfonActionPerformed
        // TODO add your handling code here:
        int filano=Tablafon.getSelectedRow();
        String claveart=""+Tablafon.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
}//GEN-LAST:event_detalleartfonActionPerformed

    private void programoppfonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programoppfonActionPerformed
        // TODO add your handling code here:
        int filano=Tablafon.getSelectedRow();
        String claveop=""+Tablafon.getValueAt(filano, 1);
        String programaconversion=""+Tablafon.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"FON","");
        }
}//GEN-LAST:event_programoppfonActionPerformed

    private void cambiarprogfonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogfonActionPerformed
        // TODO add your handling code here:
        int filano=Tablafon.getSelectedRow();
        String claveop=""+Tablafon.getValueAt(filano, 1);
        String programaconversion=""+Tablafon.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"FON",programaconversion);
        }
}//GEN-LAST:event_cambiarprogfonActionPerformed

    private void terminaprogfonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogfonActionPerformed
        // TODO add your handling code here:
        int filano=Tablafon.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablafon.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablafon.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_terminaprogfonActionPerformed

    private void eliminarprogfonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogfonActionPerformed
        // TODO add your handling code here:
        int filano=Tablafon.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablafon.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablafon.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
}//GEN-LAST:event_eliminarprogfonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","TCY","");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","PLTCY","");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","MACARB","");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","PLMACARB","");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","COM","");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","ENG","");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","MARVI","");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","SUAP","");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","DIEC","");
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","RAY","");
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","PDO","");
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","DES","");
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","MAN","");
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","FON","");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void btn_tintasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tintasActionPerformed
        // TODO add your handling code here:
        datos_parametros();
        //envia los datos a excel
        File rutaarchivo=new File(System.getProperty("user.home")+"/req_tintas.xls");
        try
        {
            //Se crea el libro Excel
            WritableWorkbook workbook = Workbook.createWorkbook(rutaarchivo);
            //Se crea una nueva hoja dentro del libro
            WritableSheet sheet = workbook.createSheet("Datos", 0);
            //formatos de texto
            WritableFont arial12b = new WritableFont(WritableFont.createFont("Arial"),12,WritableFont.BOLD, false);
            WritableFont arial10b = new WritableFont(WritableFont.createFont("Arial"),10,WritableFont.BOLD, false);
            WritableFont arial9 = new WritableFont(WritableFont.createFont("Arial"),9,WritableFont.NO_BOLD, false);

            WritableCellFormat arial10fsupertitulo = new WritableCellFormat (arial12b);

            WritableCellFormat arial10ftitulo = new WritableCellFormat (arial10b);
            arial10ftitulo.setBackground(Colour.LIME);//.SKY_BLUE
            arial10ftitulo.setAlignment(Alignment.CENTRE);
            arial10ftitulo.setVerticalAlignment(VerticalAlignment.CENTRE);

            WritableCellFormat subarial10ftitulo = new WritableCellFormat (arial10b);
            subarial10ftitulo.setBackground(Colour.SKY_BLUE);//.SKY_BLUE
            subarial10ftitulo.setAlignment(Alignment.CENTRE);
            subarial10ftitulo.setVerticalAlignment(VerticalAlignment.CENTRE);

            WritableCellFormat arial10fdetalle = new WritableCellFormat (arial9);
            WritableCellFormat arial10fdetallec = new WritableCellFormat (arial9);
            arial10fdetallec.setAlignment(Alignment.CENTRE);

            int filainicial=5;

            //titulo del documento
            sheet.addImage(new jxl.write.WritableImage(0, 0, 2, 4,new java.io.File(conexion.Directorio()+"/logoempresa.png")));
            sheet.addCell(new jxl.write.Label(0, filainicial, ""+this.getTitle(),arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            sheet.addCell(new jxl.write.Label(0, filainicial, "Fecha Prod.",arial10ftitulo));
            sheet.setColumnView( 0, 12 );
            sheet.addCell(new jxl.write.Label(1, filainicial, "Cliente",arial10ftitulo));
            sheet.setColumnView( 1, 15 );
            sheet.addCell(new jxl.write.Label(2, filainicial, "OP",arial10ftitulo));
            sheet.setColumnView( 2, 11 );
            sheet.addCell(new jxl.write.Label(3, filainicial, "Clave Art.",arial10ftitulo));
            sheet.setColumnView( 3, 14 );
            sheet.addCell(new jxl.write.Label(4, filainicial, "Articulo",arial10ftitulo));
            sheet.setColumnView( 4, 33 );
            sheet.addCell(new jxl.write.Label(5, filainicial, "Piezas",arial10ftitulo));
            sheet.setColumnView( 5, 10 );
            sheet.addCell(new jxl.write.Label(6, filainicial, "Tiempo",arial10ftitulo));
            sheet.setColumnView( 6, 11 );

            filainicial++;
            String flag_tinta="";
            String flag_maquina="";
            int columnas=7;
            int columnastotal=7;
            rs0=null;
            try{
                String senSQL="SELECT programa.*,articulos_tintas.claveproducto,articulos_tintas.porcentaje, articulos_tintas.gramos * programcantidadpiezas/1000 as kgtintas FROM (SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,programa_conversion.clavemaquina,articulos_maquinas.clave,programa_conversion.arreglo,(programa_conversion.cantidad/programa_conversion.velocidad) as tiempo,programa_conversion.velocidad,maquinas.unidadcapacidad,maquinas.descripcion,maquinas.unidadvelocidad FROM ( ((programa_conversion LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) ) LEFT JOIN maquinas ON programa_conversion.clavemaquina=maquinas.clave WHERE (programa_conversion.estatus='Act' AND programa_conversion.imprimir='true' AND (programa_conversion.clavemaquina='TCY' OR programa_conversion.clavemaquina='COM' OR programa_conversion.clavemaquina='MACARB')) ORDER BY maquinas.id_maquina,programa_conversion.clavemaquina,programa_conversion.fechaproduccion,programa_conversion.id_numero) as programa INNER JOIN articulos_tintas ON programa.clavearticulo=articulos_tintas.clavearticulo;";
                rs0=conexion.consulta(senSQL,conn);
                while(rs0.next()){
                    String clave_maqu=rs0.getString("clave");
                    if(!clave_maqu.equals(flag_maquina)){
                        sheet.addCell(new jxl.write.Label(0, filainicial, "--"+clave_maqu+" --",subarial10ftitulo));
                        flag_maquina=clave_maqu;
                        filainicial++;
                    }

                    String clave_art=rs0.getString("clavearticulo");
                    int cantidadp=rs0.getInt("programcantidadpiezas");
                    
                    
                    
                    Double m2p=cantidadp*rs0.getDouble("m2");
                    if(!clave_art.equals(flag_tinta)){ //ingresa los datos de la partida
                        columnas=9;
                        flag_tinta=clave_art;
                        sheet.addCell(new jxl.write.DateTime(0, filainicial, rs0.getDate("fechaprogram"),jxl.write.DateTime.GMT));
                        sheet.addCell(new jxl.write.Label(1, filainicial, rs0.getString("nombre"), arial10fdetalle));
                        sheet.addCell(new jxl.write.Label(2, filainicial, rs0.getString("op"),arial10fdetallec));
                        
                        sheet.addCell(new jxl.write.Label(3, filainicial, rs0.getString("clavearticulo"),arial10fdetalle));
                        
                        //sheet.addCell(new jxl.write.Label(3, filainicial, clave_art,arial10fdetallec));
                        sheet.addCell(new jxl.write.Label(4, filainicial, rs0.getString("articulo"),arial10fdetalle));
                        sheet.addCell(new jxl.write.Number(5, filainicial, cantidadp,arial10fdetalle));
                        Double tiempototal_partida=(rs0.getDouble("arreglo")+rs0.getDouble("tiempo"));
                        sheet.addCell(new jxl.write.Label(6, filainicial, horasminutos.format(tiempototal_partida/60)+":"+horasminutos.format(tiempototal_partida%60),arial10fdetallec));
                        
                        //agrega la primera tinta
                        sheet.addCell(new jxl.write.Label(7, filainicial, rs0.getString("claveproducto"),arial10fdetallec));
                        Double porcen=rs0.getDouble("porcentaje");
                        
                        
                        
                        
//                        if(porcen<=0.0) //cuando no tiene informacion toma el 60%
//                            porcen=tinta_porcentaje/100;
                        //26 gramso por metro cuadrado y 2 kilos por cada modulo
                        sheet.addCell(new jxl.write.Number(8, filainicial, rs0.getDouble("kgtintas"),arial10fdetalle));
                        //sheet.addCell(new jxl.write.Number(8, filainicial, Double.parseDouble(fijo1decimales.format(((porcen*m2p)*tinta_gramos_m2)+tinta_modulo)),arial10fdetalle));
                        if(columnas>columnastotal)//termina los titulos
                            columnastotal=columnas;

                        filainicial++;

                    }else{
                        sheet.addCell(new jxl.write.Label(columnas, filainicial-1, rs0.getString("claveproducto"),arial10fdetallec));
                        Double porcen=rs0.getDouble("porcentaje");
                        
//                        if(porcen<=0.0) //cuando no tiene informacion toma el 60%
//                            porcen=tinta_porcentaje/100;
                        //26 gramso por metro cuadrado y 2 kilos por cada modulo
                         sheet.addCell(new jxl.write.Number(columnas+1, filainicial-1, rs0.getDouble("kgtintas"),arial10fdetalle));
                        //sheet.addCell(new jxl.write.Number(columnas+1, filainicial-1, Double.parseDouble(fijo1decimales.format(((porcen*m2p)*tinta_gramos_m2)+tinta_modulo)),arial10fdetalle));
                        if(columnas>columnastotal)//termina los titulos
                            columnastotal=columnas;
                         
                       
                        
                        
                        columnas+=2;
                        
                    }

                }
                //titulos de las tintas
                int no_tinta=0;
                for(int i=7;i<=columnastotal;i=i+2){
                    no_tinta++;
                    sheet.addCell(new jxl.write.Label(i, 6, "Tinta_"+no_tinta,arial10ftitulo));
                    sheet.addCell(new jxl.write.Label(i+1,6, "kg",arial10ftitulo));
                }

                if(rs0!=null) {  rs0.close(); }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //Escribimos los resultados al fichero Excel
            workbook.write();
            workbook.close();
        }
        catch (IOException ex) {
           JOptionPane.showMessageDialog(this,"EL ARCHIVO ESTA ABIERTO O NO SE PUEDE CREAR"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
        catch (WriteException exe) {
           JOptionPane.showMessageDialog(this,"ERROR AL EXPORTAR DATOS"+exe,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }

        //abrir el documento creado
        try {
          Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"NO SE PUEDE ABRIR EL ARCHIVO\n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btn_tintasActionPerformed

    private void TablafleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablafleMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablafle.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablafle.changeSelection(rowNumber, 0, false, false);
            menupopfle.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_TablafleMouseClicked

    private void buscarfleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarfleFocusGained
        // TODO add your handling code here:
        buscarfle.selectAll();
    }//GEN-LAST:event_buscarfleFocusGained

    private void buscarfleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarfleKeyReleased
        // TODO add your handling code here:
        if(buscarfle.getText().equals("")){
            Tablafle.setRowSorter(null);
            buscarfle.setText("");
            Tablafle.setAutoCreateRowSorter(true);
            limpiatablafle();
            datosfle();
        }else{
            TableRowSorter orden = new TableRowSorter(modelofle);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscarfle.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablafle.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscarfleKeyReleased

    private void btnexportarfleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarfleActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablafle, "Planeación y programación de la producción (FLEJADO)", "corrflex_fle.xls");
    }//GEN-LAST:event_btnexportarfleActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","FLE","");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void detalleopfleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleopfleActionPerformed
        // TODO add your handling code here:
        int filano=Tablafle.getSelectedRow();
        String claveop=""+Tablafle.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
    }//GEN-LAST:event_detalleopfleActionPerformed

    private void detalleartfleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleartfleActionPerformed
        // TODO add your handling code here:
        int filano=Tablafle.getSelectedRow();
        String claveart=""+Tablafle.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
    }//GEN-LAST:event_detalleartfleActionPerformed

    private void programoppfleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programoppfleActionPerformed
        // TODO add your handling code here:
        int filano=Tablafle.getSelectedRow();
        String claveop=""+Tablafle.getValueAt(filano, 1);
        String programaconversion=""+Tablafle.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"FLE","");
        }
    }//GEN-LAST:event_programoppfleActionPerformed

    private void cambiarprogfleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogfleActionPerformed
        // TODO add your handling code here:
        int filano=Tablafle.getSelectedRow();
        String claveop=""+Tablafle.getValueAt(filano, 1);
        String programaconversion=""+Tablafle.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"FLE",programaconversion);
        }
    }//GEN-LAST:event_cambiarprogfleActionPerformed

    private void terminaprogfleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogfleActionPerformed
        // TODO add your handling code here:
        int filano=Tablafle.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablafle.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablafle.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_terminaprogfleActionPerformed

    private void eliminarprogfleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogfleActionPerformed
        // TODO add your handling code here:
        int filano=Tablafle.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablafle.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablafle.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_eliminarprogfleActionPerformed

    private void nuevo_corrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevo_corrActionPerformed
        // TODO add your handling code here:
        datos_programas_corr = new datos_programas_corr(null,true,conn,"",valor_privilegio);
        datos_programas_corr.setLocationRelativeTo(this);
        datos_programas_corr.setVisible(true);
        datos_programas_corr=null;
        actualiza();
    }//GEN-LAST:event_nuevo_corrActionPerformed

    private void combinaopcorrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combinaopcorrActionPerformed
        // TODO add your handling code here:

        int filano=Tablacorr.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String claveop=""+Tablacorr.getValueAt(filano, 1);
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            corrugadora_herramienta_combina = new corrugadora_herramienta_combina(null,true,conn,"",claveop);
            corrugadora_herramienta_combina.setLocationRelativeTo(this);
            corrugadora_herramienta_combina.setVisible(true);
            corrugadora_herramienta_combina=null;
        }
    }//GEN-LAST:event_combinaopcorrActionPerformed

    private void combinaartcorrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combinaartcorrActionPerformed
        // TODO add your handling code here:
        int filano=Tablacorr.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String claveart=""+Tablacorr.getValueAt(filano, 5);
            int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
            if(claveart.contains("<html>")){
                claveart=claveart.substring(dotPos);
            }
            corrugadora_herramienta_combina = new corrugadora_herramienta_combina(null,true,conn,claveart,"");
            corrugadora_herramienta_combina.setLocationRelativeTo(this);
            corrugadora_herramienta_combina.setVisible(true);
            corrugadora_herramienta_combina=null;
        }
    }//GEN-LAST:event_combinaartcorrActionPerformed

    private void especarttcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especarttcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablatcy.getSelectedRow();
        String claveart=""+Tablatcy.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especarttcyActionPerformed

    private void especartcorrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartcorrActionPerformed
        // TODO add your handling code here:
        int filano=Tablacorr.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String claveart=""+Tablacorr.getValueAt(filano, 5);
            int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
            if(claveart.contains("<html>")){
                claveart=claveart.substring(dotPos);
            }
            verarticulo_espec(claveart);
        }
    }//GEN-LAST:event_especartcorrActionPerformed

    private void especartlinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartlinealtcyActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealtcy.getSelectedRow();
        String claveart=""+Tablalinealtcy.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartlinealtcyActionPerformed

    private void especartsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablasys.getSelectedRow();
        String claveart=""+Tablasys.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartsysActionPerformed

    private void especartlinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartlinealsysActionPerformed
        // TODO add your handling code here:
        int filano=Tablalinealsys.getSelectedRow();
        String claveart=""+Tablalinealsys.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartlinealsysActionPerformed

    private void especartcomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartcomActionPerformed
        // TODO add your handling code here:
        int filano=Tablacom.getSelectedRow();
        String claveart=""+Tablacom.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartcomActionPerformed

    private void especartengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartengActionPerformed
        // TODO add your handling code here:
        int filano=Tablaeng.getSelectedRow();
        String claveart=""+Tablaeng.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartengActionPerformed

    private void especartmarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartmarvActionPerformed
        // TODO add your handling code here:
        int filano=Tablamarv.getSelectedRow();
        String claveart=""+Tablamarv.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartmarvActionPerformed

    private void especartsuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartsuapActionPerformed
        // TODO add your handling code here:
        int filano=Tablasuap.getSelectedRow();
        String claveart=""+Tablasuap.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartsuapActionPerformed

    private void especartdiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartdiecActionPerformed
        // TODO add your handling code here:
        int filano=Tabladiec.getSelectedRow();
        String claveart=""+Tabladiec.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartdiecActionPerformed

    private void especartrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartrayActionPerformed
        // TODO add your handling code here:
        int filano=Tablaray.getSelectedRow();
        String claveart=""+Tablaray.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartrayActionPerformed

    private void especartpdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartpdoActionPerformed
        // TODO add your handling code here:
        int filano=Tablapdo.getSelectedRow();
        String claveart=""+Tablapdo.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartpdoActionPerformed

    private void especartdesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartdesActionPerformed
        // TODO add your handling code here:
        int filano=Tablades.getSelectedRow();
        String claveart=""+Tablades.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartdesActionPerformed

    private void especartmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartmanActionPerformed
        // TODO add your handling code here:
        int filano=Tablaman.getSelectedRow();
        String claveart=""+Tablaman.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartmanActionPerformed

    private void especartfonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartfonActionPerformed
        // TODO add your handling code here:
        int filano=Tablafon.getSelectedRow();
        String claveart=""+Tablafon.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartfonActionPerformed

    private void especartfleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especartfleActionPerformed
        // TODO add your handling code here:
        int filano=Tablafle.getSelectedRow();
        String claveart=""+Tablafle.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especartfleActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        String claveop=""+JOptionPane.showInputDialog(this, "OP:");
        String maqui=""+JOptionPane.showInputDialog(this, "MAQUINA:");
        System.err.println(claveop+"--------"+maqui);
        if(!claveop.equals("") && !claveop.equals("null") && !maqui.equals("") && !maqui.equals("null")){ //programa si no esta programado
            
            programarop(claveop,maqui.toUpperCase(),"");
        }

    }//GEN-LAST:event_jLabel3MouseClicked

    private void TablatroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablatroMouseClicked
        // TODO add your handling code here:
        if ( evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tablatro.rowAtPoint( p );
            // variable for the beginning and end selects only that one row.
            Tablatro.changeSelection(rowNumber, 0, false, false);
            menupoptro.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_TablatroMouseClicked

    private void buscartroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscartroFocusGained
        // TODO add your handling code here:
        buscartro.selectAll();
    }//GEN-LAST:event_buscartroFocusGained

    private void buscartroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscartroKeyReleased
        // TODO add your handling code here:
        if(buscartro.getText().equals("")){
            Tablatro.setRowSorter(null);
            buscartro.setText("");
            Tablatro.setAutoCreateRowSorter(true);
            limpiatablatro();
            datostro();
        }else{
            TableRowSorter orden = new TableRowSorter(modelotro);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscartro.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tablatro.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscartroKeyReleased

    private void btnexportartroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportartroActionPerformed
        // TODO add your handling code here:
        conexion.exportaTabla(Tablatro, "Planeación y programación de la producción (TROQUEL)", "corrflex_tro.xls");
    }//GEN-LAST:event_btnexportartroActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        programarop("STOCK","TRO","");
    }//GEN-LAST:event_jButton16ActionPerformed

    private void detalleoptroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalleoptroActionPerformed
        // TODO add your handling code here:
        int filano=Tablatro.getSelectedRow();
        String claveop=""+Tablatro.getValueAt(filano, 1);
        int dotPos = claveop.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveop.contains("<html>")){
            claveop=claveop.substring(dotPos);
        }
        verop(claveop);
    }//GEN-LAST:event_detalleoptroActionPerformed

    private void detallearttroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detallearttroActionPerformed
        // TODO add your handling code here:
        int filano=Tablatro.getSelectedRow();
        String claveart=""+Tablatro.getValueAt(filano, 6);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo(claveart);
    }//GEN-LAST:event_detallearttroActionPerformed

    private void especarttroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_especarttroActionPerformed
        // TODO add your handling code here:
        int filano=Tablatro.getSelectedRow();
        String claveart=""+Tablatro.getValueAt(filano, 5);
        int dotPos = claveart.lastIndexOf(">")+1;//le quita el html de los titulos
        if(claveart.contains("<html>")){
            claveart=claveart.substring(dotPos);
        }
        verarticulo_espec(claveart);
    }//GEN-LAST:event_especarttroActionPerformed

    private void programopptroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programopptroActionPerformed
        // TODO add your handling code here:
        int filano=Tablatro.getSelectedRow();
        String claveop=""+Tablatro.getValueAt(filano, 1);
        String programaconversion=""+Tablatro.getValueAt(filano, 19);
        if(programaconversion.equals("null") && !claveop.equals("null")){ //programa si no esta programado
            int dotPos = claveop.lastIndexOf(">")+1;//le quita el html al texto
            if(claveop.contains("<html>")){
                claveop=claveop.substring(dotPos);
            }
            programarop(claveop,"TRO","");
        }
    }//GEN-LAST:event_programopptroActionPerformed

    private void cambiarprogtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarprogtroActionPerformed
        // TODO add your handling code here:
        int filano=Tablatro.getSelectedRow();
        String claveop=""+Tablatro.getValueAt(filano, 1);
        String programaconversion=""+Tablatro.getValueAt(filano, 19);
        if(!programaconversion.equals("null")){ //programa si no esta programado
            programarop(claveop,"TRO",programaconversion);
        }
    }//GEN-LAST:event_cambiarprogtroActionPerformed

    private void terminaprogtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaprogtroActionPerformed
        // TODO add your handling code here:
        int filano=Tablatro.getSelectedRow();
        String motivotermina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE TERMINO:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablatro.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablatro.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivotermina.equals("") && !motivotermina.equals("null") && cantidadprod>0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Ter',notas='Terminado: "+motivotermina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_terminaprogtroActionPerformed

    private void eliminarprogtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarprogtroActionPerformed
        // TODO add your handling code here:
        int filano=Tablatro.getSelectedRow();
        String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE ELIMINACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        String programaconversion=""+Tablatro.getValueAt(filano, 19);
        int cantidadprod=Integer.parseInt(""+Tablatro.getValueAt(filano, 16));

        if(!programaconversion.equals("null") && !motivoelimina.equals("") && !motivoelimina.equals("null") && cantidadprod<=0){
            String senSQLmov="UPDATE programa_conversion SET estatus='Can',notas='Cancelado: "+motivoelimina+"' WHERE id_programa_conversion='"+programaconversion+"';";
            conexion.modificaupdate_p(senSQLmov,conn,valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_eliminarprogtroActionPerformed

    private void btn_insumos_corrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_insumos_corrActionPerformed
        // TODO add your handling code here:
        conta.busca_fechascliente busca_fechascliente = new busca_fechascliente(null,true,conn,"");
        busca_fechascliente.setLocationRelativeTo(this);
        busca_fechascliente.setVisible(true);
        String estado=busca_fechascliente.getEstado();/**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if(estado.equals("cancelar")){

        }else{
            //datos para un solo cliente
            String dinamicoSQL="";
            if(busca_fechascliente.getProveedor().equals("Buscar")){ /**verifica si buscas un solo proveedor o todos*/
                dinamicoSQL="id_clientes='"+busca_fechascliente.getID()+"'";
            }
            //datos de fechas
            String fi=fechainsertar.format(busca_fechascliente.getFechaini());
            String ft=fechainsertar.format(busca_fechascliente.getFechafin());

            //datos de llenado de tabla de papel
            modelot1.setNumRows(0);
            rs0 = null;
            try {
                String senSQL = "SELECT resistencias.clave,max(resistencias.descripcion) as descripcion,max(resistencias.l1r) as l1r,max(resistencias.r1r) as r1r,max(resistencias.l2r) as l2r,max(resistencias.r2r) as r2r,max(resistencias.l3r) as l3r,COALESCE(sum(pendiente_corr.kg_pendientes),0) as kg_pendientes FROM resistencias LEFT JOIN (SELECT ops.op,ops.fechaentrega,ops.maquila,((ops.cantidad-COALESCE(prodcorr.prodcantidadpiezas,0))*articulos.kg) as kg_pendientes,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,clientes.nombre,prodcorr.fechaprod,COALESCE(prodcorr.prodml,0) as prodml,COALESCE(prodcorr.prodcantidad,0) as prodcantidad,COALESCE(prodcorr.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prodcorr.prodcantkgpiezas,0) as prodcantkgpiezas,programcorr.fechaprogram,COALESCE(programcorr.programcantidad,0) as programcantidad,COALESCE(programcorr.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(programcorr.programcantkgpiezas,0) as programcantkgpiezas,articulos_maquinas.clave FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='CORR' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prodcorr ON ops.op=prodcorr.op ) LEFT JOIN (SELECT programa_corr_detalle.op,max(programa_corr.fechaproduccion) as fechaprogram,sum(programa_corr_detalle.laminas) as programcantidad,sum(articulos.piezas*programa_corr_detalle.laminas) as programcantidadpiezas,sum(articulos.piezas*programa_corr_detalle.laminas*articulos.kg) as programcantkgpiezas FROM (programa_corr INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo  WHERE (programa_corr.estatus<>'Can' AND programa_corr.estatus<>'Pen') GROUP BY programa_corr_detalle.op) as programcorr ON ops.op=programcorr.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND (ops.fechaentrega>='"+fi+" 00:00:00' AND ops.fechaentrega<='"+ft+" 23:59:59') AND articulos_maquinas.clave='CORR' AND ((ops.cantidad-COALESCE(prodcorr.prodcantidadpiezas,0))*articulos.kg)>0 ) ORDER BY ops.fechaentrega,ops.op) AS pendiente_corr ON resistencias.clave=pendiente_corr.claveresistencia WHERE COALESCE(pendiente_corr.kg_pendientes,0)>0 GROUP BY resistencias.clave ORDER BY resistencias.clave DESC;";
                rs0 = conexion.consulta(senSQL, conn);
                while (rs0.next()) {
                    String l1=""+rs0.getString("l1r");
                    String r1=""+rs0.getString("r1r");
                    String l2=""+rs0.getString("l2r");
                    String r2=""+rs0.getString("r2r");
                    String l3=""+rs0.getString("l3r");
                    Double gl1=0.0;
                    Double gr1=0.0;
                    Double gl2=0.0;
                    Double gr2=0.0;
                    Double gl3=0.0;

                    Double factor_arrollamiento_c=1.48;
                    Double factor_arrollamiento_b=1.35;

                    if(l1.length()>0 && !l1.equals("null"))
                        gl1=Double.parseDouble("0."+l1.charAt(1)+l1.charAt(2)+l1.charAt(3));
                    if(r1.length()>0 && !r1.equals("null"))
                        gr1=Double.parseDouble("0."+r1.charAt(1)+r1.charAt(2)+r1.charAt(3))*factor_arrollamiento_c;
                    if(l2.length()>0 && !l2.equals("null"))
                        gl2=Double.parseDouble("0."+l2.charAt(1)+l2.charAt(2)+l2.charAt(3));
                    if(r2.length()>0 && !r2.equals("null"))
                        gr2=Double.parseDouble("0."+r2.charAt(1)+r2.charAt(2)+r2.charAt(3))*factor_arrollamiento_b;
                    if(l3.length()>0 && !l3.equals("null"))
                        gl3=Double.parseDouble("0."+l3.charAt(1)+l3.charAt(2)+l3.charAt(3));

                    Double gramostotal=gl1+gr1+gl2+gr2+gl3;
                    Double kgtotal=rs0.getDouble("kg_pendientes");
                    Object datos[] = {rs0.getString("clave"), rs0.getString("descripcion"), kgtotal,l1,((kgtotal/gramostotal)*gl1),r1,((kgtotal/gramostotal)*gr1),l2,((kgtotal/gramostotal)*gl2),r2,((kgtotal/gramostotal)*gr2),l3,((kgtotal/gramostotal)*gl3)};
                    modelot1.addRow(datos);
                }
                if (rs0 != null) {  rs0.close();   }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
            String senSQLinv="DELETE FROM requerimiento_papel;";
            conexion.modificamov_p(senSQLinv, conn,valor_privilegio);
            for(int i=0;i<(Tabladatos_papel.getRowCount());i=i+1){
                for(int j=3;j<(12);j=j+2){
                    String p1=""+Tabladatos_papel.getValueAt(i, j);
                    if(!p1.equals("") && !p1.equals("null")){
                        String senSQL="INSERT INTO requerimiento_papel(clavepapel,kg) VALUES ('"+p1+"', '"+(Double)Tabladatos_papel.getValueAt(i, j+1)+"');";
                        conexion.modificamov_p(senSQL,conn,valor_privilegio);
                    }
                }
            }
            //fin de llenado de tabla de papel

            File rutaarchivo=new File(System.getProperty("user.home")+"/insumos.xls");
            try
            {
                //Se crea el libro Excel
                WritableWorkbook workbook = Workbook.createWorkbook(rutaarchivo);
                //Se crea una nueva hoja dentro del libro
                WritableSheet sheet_0 = workbook.createSheet("0_Pen_Corr", 0);
                WritableSheet sheet_1 = workbook.createSheet("Papel_Corr", 0);
                WritableSheet sheet_2 = workbook.createSheet("Ins_Corr", 1);
                //formatos de texto
                WritableFont arial12b = new WritableFont(WritableFont.createFont("Arial"),12,WritableFont.BOLD, false);
                WritableFont arial10b = new WritableFont(WritableFont.createFont("Arial"),10,WritableFont.BOLD, false);
                WritableFont arial9 = new WritableFont(WritableFont.createFont("Arial"),9,WritableFont.NO_BOLD, false);

                WritableCellFormat arial10fsupertitulo = new WritableCellFormat (arial12b);

                WritableCellFormat arial10ftitulo = new WritableCellFormat (arial10b);
                arial10ftitulo.setBackground(Colour.LIME);//.SKY_BLUE
                arial10ftitulo.setAlignment(Alignment.CENTRE);
                arial10ftitulo.setVerticalAlignment(VerticalAlignment.CENTRE);

                WritableCellFormat arial10fdetalle = new WritableCellFormat (arial9);

                int filainicial_0=5;
                int filainicial_1=5;
                int filainicial_2=5;

                //titulo del documento
                sheet_0.addImage(new jxl.write.WritableImage(0, 0, 2, 4,new java.io.File(conexion.Directorio()+"/logoempresa.png")));
                sheet_0.addCell(new jxl.write.Label(0, filainicial_0, "Pendientes por  Entregar (CORR) "+fechamediana.format(busca_fechascliente.getFechaini())+" AL "+fechamediana.format(busca_fechascliente.getFechafin()),arial10fsupertitulo));
                sheet_1.addImage(new jxl.write.WritableImage(0, 0, 2, 4,new java.io.File(conexion.Directorio()+"/logoempresa.png")));
                sheet_1.addCell(new jxl.write.Label(0, filainicial_1, "INSUMOS P(APEL) "+fechamediana.format(busca_fechascliente.getFechaini())+" AL "+fechamediana.format(busca_fechascliente.getFechafin()),arial10fsupertitulo));
                sheet_2.addImage(new jxl.write.WritableImage(0, 0, 2, 4,new java.io.File(conexion.Directorio()+"/logoempresa.png")));
                sheet_2.addCell(new jxl.write.Label(0, filainicial_2, "INSUMOS DEL "+fechamediana.format(busca_fechascliente.getFechaini())+" AL "+fechamediana.format(busca_fechascliente.getFechafin()),arial10fsupertitulo));

                //CELDAS DE TITULO
                filainicial_0++;//incrementa las filas
                sheet_0.addCell(new jxl.write.Label(0, filainicial_0, "Piezas",arial10ftitulo));
                sheet_0.setColumnView( 0, 18 );
                sheet_0.addCell(new jxl.write.Label(1, filainicial_0, "M2",arial10ftitulo));
                sheet_0.setColumnView( 1, 18 );
                sheet_0.addCell(new jxl.write.Label(2, filainicial_0, "KG",arial10ftitulo));
                sheet_0.setColumnView( 2, 18 );
                sheet_0.addCell(new jxl.write.Label(3, filainicial_0, "OP's",arial10ftitulo));
                sheet_0.setColumnView( 3, 18 );
                //CELDAS DE TITULO
                filainicial_1++;//incrementa las filas
                sheet_1.addCell(new jxl.write.Label(0, filainicial_1, "Clave Papel",arial10ftitulo));
                sheet_1.setColumnView( 0, 22 );
                sheet_1.addCell(new jxl.write.Label(1, filainicial_1, "KG",arial10ftitulo));
                sheet_1.setColumnView( 1, 15 );
                //CELDAS DE TITULO
                filainicial_2++;//incrementa las filas
                sheet_2.addCell(new jxl.write.Label(0, filainicial_2, "Materia Prima",arial10ftitulo));
                sheet_2.setColumnView( 0, 23 );
                sheet_2.addCell(new jxl.write.Label(1, filainicial_2, "UM",arial10ftitulo));
                sheet_2.setColumnView( 1, 7 );
                sheet_2.addCell(new jxl.write.Label(2, filainicial_2, "Cantidad",arial10ftitulo));
                sheet_2.setColumnView( 2, 11 );

                
                //datos de corrugadora
                filainicial_0++;//incrementa las filas
                Double pen_pzas_corr=0.0,pen_kg_corr=0.0,pen_m2_corr=0.0;
                rs0=null;
                try{
                    String senSQL="SELECT sum(total.cantidad-total.prodcantidadpiezas) as cantidadpendientepzas ,sum((total.cantidad-total.prodcantidadpiezas)*total.kg) as kg,sum((total.cantidad-total.prodcantidadpiezas)*total.m2) as m2,count(total.op) as suma_cambios FROM (SELECT ops.op,ops.id_clientes,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,ops.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.ancho,articulos.kg,articulos.m2,clientes.nombre,prodcorr.fechaprod,COALESCE(prodcorr.prodml,0) as prodml,COALESCE(prodcorr.prodcantidad,0) as prodcantidad,COALESCE(prodcorr.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prodcorr.prodcantkgpiezas,0) as prodcantkgpiezas,programcorr.fechaprogram,COALESCE(programcorr.programcantidad,0) as programcantidad,COALESCE(programcorr.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(programcorr.programcantkgpiezas,0) as programcantkgpiezas,articulos_maquinas.clave FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='CORR' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prodcorr ON ops.op=prodcorr.op ) LEFT JOIN (SELECT programa_corr_detalle.op,max(programa_corr.fechaproduccion) as fechaprogram,sum(programa_corr_detalle.laminas) as programcantidad,sum(articulos.piezas*programa_corr_detalle.laminas) as programcantidadpiezas,sum(articulos.piezas*programa_corr_detalle.laminas*articulos.kg) as programcantkgpiezas FROM (programa_corr INNER JOIN programa_corr_detalle ON programa_corr.id_programa_corr=programa_corr_detalle.id_programa_corr) LEFT JOIN articulos ON programa_corr_detalle.clavearticulo=articulos.clavearticulo WHERE (programa_corr.estatus<>'Can' AND programa_corr.estatus<>'Pen') GROUP BY programa_corr_detalle.op) as programcorr ON ops.op=programcorr.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo WHERE ( ops.estatus<>'Can' AND ops.estatus<>'Ter' AND articulos_maquinas.clave='CORR') ORDER BY ops.fechaentrega,ops.op) as total WHERE ((total.fechaentrega>='"+fi+" 00:00:00' AND total.fechaentrega<='"+ft+" 23:59:59') AND ((total.cantidad-total.prodcantidadpiezas)>0));";
                    rs0=conexion.consulta(senSQL,conn);
                    while(rs0.next()){
                        pen_pzas_corr=rs0.getDouble("cantidadpendientepzas");
                        pen_kg_corr=rs0.getDouble("kg");
                        pen_m2_corr=rs0.getDouble("m2");
                        sheet_0.addCell(new jxl.write.Number(0, filainicial_0, pen_pzas_corr,arial10fdetalle));
                        sheet_0.addCell(new jxl.write.Number(1, filainicial_0, pen_m2_corr,arial10fdetalle));
                        sheet_0.addCell(new jxl.write.Number(2, filainicial_0, pen_kg_corr,arial10fdetalle));
                        sheet_0.addCell(new jxl.write.Number(3, filainicial_0, rs0.getInt("suma_cambios"),arial10fdetalle));
                    }
                    if(rs0!=null) {     rs0.close();    }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


                //datos de papel
                filainicial_1++;
                rs0=null;
                try{
                    String senSQL="SELECT requerimiento_papel.clavepapel,sum(requerimiento_papel.kg) as kg FROM requerimiento_papel GROUP BY clavepapel ORDER BY clavepapel,sum(requerimiento_papel.kg);";
                    rs0=conexion.consulta(senSQL,conn);
                    while(rs0.next()){
                        sheet_1.addCell(new jxl.write.Label(0, filainicial_1, rs0.getString("clavepapel"),arial10fdetalle));
                        sheet_1.addCell(new jxl.write.Number(1, filainicial_1, rs0.getDouble("kg"),arial10fdetalle));
                        filainicial_1++;
                    }
                    if(rs0!=null) {  rs0.close(); }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                //datos de pararmetros
                filainicial_2++;//incrementa las filas
                rs0=null;
                try{
                    String senSQL="SELECT * FROM parametros WHERE id_parametros='1'";
                    rs0=conexion.consulta(senSQL,conn);
                    if(rs0.next()){
                        //combustible
                        sheet_2.addCell(new jxl.write.Label(0, filainicial_2, "COMBUSTIBLE",arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Label(1, filainicial_2, rs0.getString("factor_um_combustible"),arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Number(2, filainicial_2, pen_kg_corr*rs0.getDouble("factor_combustible"),arial10fdetalle));
                        filainicial_2++;//incrementa las filas
                        //almidon
                        sheet_2.addCell(new jxl.write.Label(0, filainicial_2, "ALMIDON",arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Label(1, filainicial_2, rs0.getString("factor_um_almidon"),arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Number(2, filainicial_2, pen_kg_corr*rs0.getDouble("factor_almidon"),arial10fdetalle));
                        filainicial_2++;//incrementa las filas
                        //borax
                        sheet_2.addCell(new jxl.write.Label(0, filainicial_2, "BORAX",arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Label(1, filainicial_2, rs0.getString("factor_um_borax"),arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Number(2, filainicial_2, pen_kg_corr*rs0.getDouble("factor_borax"),arial10fdetalle));
                        filainicial_2++;//incrementa las filas
                        //sosa
                        sheet_2.addCell(new jxl.write.Label(0, filainicial_2, "SOSA",arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Label(1, filainicial_2, rs0.getString("factor_um_sosa"),arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Number(2, filainicial_2, pen_kg_corr*rs0.getDouble("factor_sosa"),arial10fdetalle));
                        filainicial_2++;//incrementa las filas
                        //salmuera
                        sheet_2.addCell(new jxl.write.Label(0, filainicial_2, "SALMUERA",arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Label(1, filainicial_2, rs0.getString("factor_um_salmuera"),arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Number(2, filainicial_2, pen_kg_corr*rs0.getDouble("factor_salmuera"),arial10fdetalle));
                        filainicial_2++;//incrementa las filas
                        //powerquim
                        sheet_2.addCell(new jxl.write.Label(0, filainicial_2, "POWER QUIM",arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Label(1, filainicial_2, rs0.getString("factor_um_powerquim"),arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Number(2, filainicial_2, pen_kg_corr*rs0.getDouble("factor_powerquim"),arial10fdetalle));
                        filainicial_2++;//incrementa las filas
                        //tarimas
                        sheet_2.addCell(new jxl.write.Label(0, filainicial_2, "TARIMAS",arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Label(1, filainicial_2, rs0.getString("factor_um_tarimas"),arial10fdetalle));
                        sheet_2.addCell(new jxl.write.Number(2, filainicial_2, pen_kg_corr*rs0.getDouble("factor_tarimas"),arial10fdetalle));
                        filainicial_2++;//incrementa las filas
                    }
                    if(rs0!=null) {     rs0.close();   }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                //Escribimos los resultados al fichero Excel
                workbook.write();
                workbook.close();
            }
            catch (IOException ex) {
               JOptionPane.showMessageDialog(this,"EL ARCHIVO ESTA ABIERTO O NO SE PUEDE CREAR"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }
            catch (WriteException exe) {
               JOptionPane.showMessageDialog(this,"ERROR AL EXPORTAR DATOS"+exe,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }

            //abrir el documento creado
            try {
              Desktop.getDesktop().open(rutaarchivo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,"NO SE PUEDE ABRIR EL ARCHIVO\n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }

        }

    }//GEN-LAST:event_btn_insumos_corrActionPerformed

    private void Tabladatos_papelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabladatos_papelMouseClicked
        // TODO add your handling code here:
}//GEN-LAST:event_Tabladatos_papelMouseClicked

    private void btnimprimirmaqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimirmaqActionPerformed
          org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        String clavemaq=JOptionPane.showInputDialog(this, "INTRODUCE CLAVE DE MAQUINA A IMPRIMIR", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
        
        

        JasperViewer visor=null;
        JasperPrint jasperPrint =null;
        try{
            //String datos="REPORTE GENERADO DEL "+formatofecha.format(tfecha.getDate())+"  AL  "+formatofecha.format(tfecha2.getDate());
            Map pars = new HashMap();
            File fichero = new File(conexion.Directorio()+"/logoempresa.png");
            pars.put("folio",null);
            pars.put("logoempresa",new FileInputStream(fichero));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("senSQL", "");//SQL dinamico
            pars.put("clavemaq", clavemaq );//SQL dinamico   
            pars.put("version", resourceMap.getString("Application.title"));
            JasperReport masterReport = null;
            try{
                masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/corrflex_maq.jasper"));
             }
             catch (JRException e) { javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Error cargando el reporte maestro:\n" + e.getMessage()+"\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);                 }

            jasperPrint = JasperFillManager.fillReport(masterReport,pars,conn);
            visor = new JasperViewer(jasperPrint,false);
            visor.setTitle("REPORTE");
            visor.setVisible(true);
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error leyendo el reporte"+ex);    }

        
    }//GEN-LAST:event_btnimprimirmaqActionPerformed
    //TCY botones sumar o restar dias
    private void sumadiatcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiatcyActionPerformed
        // TODO add your handling code here:
        //Incrementa un dia a la produccion actual TCY
        int filano = Tablatcy.getSelectedRow();
        String programaconversion = "" + Tablatcy.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadiatcyActionPerformed
    private void restadiatcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiatcyActionPerformed
        // TODO add your handling code here:
        //Resta un dia a la produccion actual TCY
        int filano = Tablatcy.getSelectedRow();
        String programaconversion = "" + Tablatcy.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiatcyActionPerformed
    //TCY LINEAL botones sumar o restar dias
    private void sumadialinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadialinealtcyActionPerformed
        // TODO add your handling code here:
        //Incrementa un dia a la produccion actual
        int filano = Tablalinealtcy.getSelectedRow();
        String programaconversion = "" + Tablalinealtcy.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadialinealtcyActionPerformed
    private void restadialinealtcyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadialinealtcyActionPerformed
        // TODO add your handling code here:
        //Resta un dia a la produccion actual TCY LINEAL
        int filano = Tablalinealtcy.getSelectedRow();
        String programaconversion = "" + Tablalinealtcy.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadialinealtcyActionPerformed
    // MACARBOX botones sumar o restar dias
    private void sumadiasysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiasysActionPerformed
        // TODO add your handling code here:
        //Incrementa un dia a la produccion actual MACARBOX
        int filano = Tablasys.getSelectedRow();
        String programaconversion = "" + Tablasys.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadiasysActionPerformed
    private void restadiasysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiasysActionPerformed
        // TODO add your handling code here:
        int filano = Tablasys.getSelectedRow();
        String programaconversion = "" + Tablasys.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiasysActionPerformed
    // MACARBOX LINEAL botones sumar o restar dias
    private void sumadialinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadialinealsysActionPerformed
        // TODO add your handling code here:
        //Incrementa un dia a la produccion actual MACARBOX
        int filano = Tablalinealsys.getSelectedRow();
        String programaconversion = "" + Tablalinealsys.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
        
    }//GEN-LAST:event_sumadialinealsysActionPerformed
    private void restadialinealsysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadialinealsysActionPerformed
        // TODO add your handling code here:
        int filano = Tablalinealsys.getSelectedRow();
        String programaconversion = "" + Tablalinealsys.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadialinealsysActionPerformed
    // COMET botones sumar o restar dias
    private void sumadiacomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiacomActionPerformed
        // TODO add your handling code here:
        int filano = Tablacom.getSelectedRow();
        String programaconversion = "" + Tablacom.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadiacomActionPerformed
    private void restadiacomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiacomActionPerformed
        // TODO add your handling code here:
        int filano = Tablacom.getSelectedRow();
        String programaconversion = "" + Tablacom.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiacomActionPerformed
    // ENGRAPADORA botones sumar o restar dias
    private void sumadiaengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiaengActionPerformed
        // TODO add your handling code here:
        int filano = Tablaeng.getSelectedRow();
        String programaconversion = "" + Tablaeng.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadiaengActionPerformed
    private void restadiaengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiaengActionPerformed
        // TODO add your handling code here:
        int filano = Tablaeng.getSelectedRow();
        String programaconversion = "" + Tablaeng.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiaengActionPerformed
    // MARVI botones sumar o restar dias
    private void sumadiamarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiamarvActionPerformed
        // TODO add your handling code here:
        int filano = Tablamarv.getSelectedRow();
        String programaconversion = "" + Tablamarv.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadiamarvActionPerformed
    private void restadiamarvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiamarvActionPerformed
        // TODO add your handling code here:
        int filano = Tablamarv.getSelectedRow();
        String programaconversion = "" + Tablamarv.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiamarvActionPerformed
    // SUAJE PLANO botones sumar o restar dias
    private void sumadiasuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiasuapActionPerformed
        // TODO add your handling code here:
        int filano = Tablasuap.getSelectedRow();
        String programaconversion = "" + Tablasuap.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadiasuapActionPerformed
    private void restadiasuapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiasuapActionPerformed
        // TODO add your handling code here:
        int filano = Tablasuap.getSelectedRow();
        String programaconversion = "" + Tablasuap.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiasuapActionPerformed
    // DIE CUTTER botones sumar o restar dias
    private void sumadiadiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiadiecActionPerformed
        // TODO add your handling code here:
        int filano = Tabladiec.getSelectedRow();
        String programaconversion = "" + Tabladiec.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
        
    }//GEN-LAST:event_sumadiadiecActionPerformed
    private void restadiadiecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiadiecActionPerformed
        // TODO add your handling code here:
        int filano = Tabladiec.getSelectedRow();
        String programaconversion = "" + Tabladiec.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiadiecActionPerformed
    // RAYADORA botones sumar o restar dias
    private void sumadiarayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiarayActionPerformed
        // TODO add your handling code here:
        int filano = Tablaray.getSelectedRow();
        String programaconversion = "" + Tablaray.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadiarayActionPerformed
    private void restadiarayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiarayActionPerformed
        // TODO add your handling code here:
        int filano = Tablaray.getSelectedRow();
        String programaconversion = "" + Tablaray.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiarayActionPerformed
    // PEGADO botones sumar o restar dias
    private void sumadiapdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiapdoActionPerformed
        // TODO add your handling code here:
        int filano = Tablapdo.getSelectedRow();
        String programaconversion = "" + Tablapdo.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadiapdoActionPerformed
    private void restadiapdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiapdoActionPerformed
        // TODO add your handling code here:
        int filano = Tablapdo.getSelectedRow();
        String programaconversion = "" + Tablapdo.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiapdoActionPerformed
    // DESMERMADO botones sumar o restar dias
    private void sumadiadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiadesActionPerformed
        // TODO add your handling code here:
        int filano = Tablades.getSelectedRow();
        String programaconversion = "" + Tablades.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_sumadiadesActionPerformed
    private void restadiadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiadesActionPerformed
        // TODO add your handling code here:
        int filano = Tablades.getSelectedRow();
        String programaconversion = "" + Tablades.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiadesActionPerformed
    // PEGADO MANUAL botones sumar o restar dias
    private void sumadiamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiamanActionPerformed
        // TODO add your handling code here:
        int filano = Tablaman.getSelectedRow();
        String programaconversion = "" + Tablaman.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
      
    }//GEN-LAST:event_sumadiamanActionPerformed
    private void restadiamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiamanActionPerformed
        // TODO add your handling code here:
        int filano = Tablaman.getSelectedRow();
        String programaconversion = "" + Tablaman.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiamanActionPerformed
    // TROQUEL botones sumar o restar dias
    private void sumadiatroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiatroActionPerformed
        // TODO add your handling code here:
        int filano = Tablatro.getSelectedRow();
        String programaconversion = "" + Tablatro.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
        
    }//GEN-LAST:event_sumadiatroActionPerformed
    private void restadiatroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiatroActionPerformed
        // TODO add your handling code here:
         int filano = Tablatro.getSelectedRow();
        String programaconversion = "" + Tablatro.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
    }//GEN-LAST:event_restadiatroActionPerformed
    // FLEJADO botones sumar o restar dias
    private void sumadiafleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumadiafleActionPerformed
        // TODO add your handling code here:
        int filano = Tablafle.getSelectedRow();
        String programaconversion = "" + Tablafle.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
        
    }//GEN-LAST:event_sumadiafleActionPerformed
    private void restadiafleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restadiafleActionPerformed
        // TODO add your handling code here:
        int filano = Tablafle.getSelectedRow();
        String programaconversion = "" + Tablafle.getValueAt(filano, 19);

        if (!programaconversion.equals("null")) {
            String senSQLmov = "UPDATE programa_conversion SET fechaproduccion = cast ( fechaproduccion AS date ) + cast ( '-1 days' AS INTERVAL )  WHERE id_programa_conversion='" + programaconversion + "';";
            conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
            actualiza();
        }
        
    }//GEN-LAST:event_restadiafleActionPerformed
   
     
    
    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tablacom;
    private javax.swing.JTable Tablacorr;
    private javax.swing.JTable Tabladatos_papel;
    private javax.swing.JTable Tablades;
    private javax.swing.JTable Tabladiec;
    private javax.swing.JTable Tablaeng;
    private javax.swing.JTable Tablafle;
    private javax.swing.JTable Tablafon;
    private javax.swing.JTable Tablalinealsys;
    private javax.swing.JTable Tablalinealtcy;
    private javax.swing.JTable Tablaman;
    private javax.swing.JTable Tablamarv;
    private javax.swing.JTable Tablapdo;
    private javax.swing.JTable Tablaray;
    private javax.swing.JTable Tablasuap;
    private javax.swing.JTable Tablasys;
    private javax.swing.JTable Tablatcy;
    private javax.swing.JTable Tablatro;
    private javax.swing.JButton btn_insumos_corr;
    private javax.swing.JButton btn_tintas;
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnexportarcom;
    private javax.swing.JButton btnexportarcorr;
    private javax.swing.JButton btnexportardes;
    private javax.swing.JButton btnexportardiec;
    private javax.swing.JButton btnexportareng;
    private javax.swing.JButton btnexportarfle;
    private javax.swing.JButton btnexportarfon;
    private javax.swing.JButton btnexportarlinealsys;
    private javax.swing.JButton btnexportarlinealtcy;
    private javax.swing.JButton btnexportarman;
    private javax.swing.JButton btnexportarmarv;
    private javax.swing.JButton btnexportarpdo;
    private javax.swing.JButton btnexportarray;
    private javax.swing.JButton btnexportarsuap;
    private javax.swing.JButton btnexportarsys;
    private javax.swing.JButton btnexportartcy;
    private javax.swing.JButton btnexportartro;
    private javax.swing.JButton btnimprimir;
    private javax.swing.JButton btnimprimirmaq;
    private javax.swing.JTextField buscarcom;
    private javax.swing.JTextField buscarcorr;
    private javax.swing.JTextField buscardes;
    private javax.swing.JTextField buscardiec;
    private javax.swing.JTextField buscareng;
    private javax.swing.JTextField buscarfle;
    private javax.swing.JTextField buscarfon;
    private javax.swing.JTextField buscarlinealsys;
    private javax.swing.JTextField buscarlinealtcy;
    private javax.swing.JTextField buscarman;
    private javax.swing.JTextField buscarmarv;
    private javax.swing.JTextField buscarpdo;
    private javax.swing.JTextField buscarray;
    private javax.swing.JTextField buscarsuap;
    private javax.swing.JTextField buscarsys;
    private javax.swing.JTextField buscartcy;
    private javax.swing.JTextField buscartro;
    private javax.swing.JMenuItem cambiarprogcom;
    private javax.swing.JMenuItem cambiarprogdes;
    private javax.swing.JMenuItem cambiarprogdiec;
    private javax.swing.JMenuItem cambiarprogeng;
    private javax.swing.JMenuItem cambiarprogfle;
    private javax.swing.JMenuItem cambiarprogfon;
    private javax.swing.JMenuItem cambiarproglinealsys;
    private javax.swing.JMenuItem cambiarproglinealtcy;
    private javax.swing.JMenuItem cambiarprogman;
    private javax.swing.JMenuItem cambiarprogmarv;
    private javax.swing.JMenuItem cambiarprogpdo;
    private javax.swing.JMenuItem cambiarprogray;
    private javax.swing.JMenuItem cambiarprogsuap;
    private javax.swing.JMenuItem cambiarprogsys;
    private javax.swing.JMenuItem cambiarprogtcy;
    private javax.swing.JMenuItem cambiarprogtro;
    private javax.swing.JPanel com;
    private javax.swing.JMenuItem combinaartcorr;
    private javax.swing.JMenuItem combinaopcorr;
    private javax.swing.JPanel corr;
    private javax.swing.JPanel des;
    private javax.swing.JMenuItem detalleartcom;
    private javax.swing.JMenuItem detalleartcorr;
    private javax.swing.JMenuItem detalleartdes;
    private javax.swing.JMenuItem detalleartdiec;
    private javax.swing.JMenuItem detallearteng;
    private javax.swing.JMenuItem detalleartfle;
    private javax.swing.JMenuItem detalleartfon;
    private javax.swing.JMenuItem detalleartlinealsys;
    private javax.swing.JMenuItem detalleartlinealtcy;
    private javax.swing.JMenuItem detalleartman;
    private javax.swing.JMenuItem detalleartmarv;
    private javax.swing.JMenuItem detalleartpdo;
    private javax.swing.JMenuItem detalleartray;
    private javax.swing.JMenuItem detalleartsuap;
    private javax.swing.JMenuItem detalleartsys;
    private javax.swing.JMenuItem detallearttcy;
    private javax.swing.JMenuItem detallearttro;
    private javax.swing.JMenuItem detalleopcom;
    private javax.swing.JMenuItem detalleopcorr;
    private javax.swing.JMenuItem detalleopdes;
    private javax.swing.JMenuItem detalleopdiec;
    private javax.swing.JMenuItem detalleopeng;
    private javax.swing.JMenuItem detalleopfle;
    private javax.swing.JMenuItem detalleopfon;
    private javax.swing.JMenuItem detalleoplinealsys;
    private javax.swing.JMenuItem detalleoplinealtcy;
    private javax.swing.JMenuItem detalleopman;
    private javax.swing.JMenuItem detalleopmarv;
    private javax.swing.JMenuItem detalleoppdo;
    private javax.swing.JMenuItem detalleopray;
    private javax.swing.JMenuItem detalleopsuap;
    private javax.swing.JMenuItem detalleopsys;
    private javax.swing.JMenuItem detalleoptcy;
    private javax.swing.JMenuItem detalleoptro;
    private javax.swing.JPanel diec;
    private javax.swing.JMenuItem eliminarprogcom;
    private javax.swing.JMenuItem eliminarprogdes;
    private javax.swing.JMenuItem eliminarprogdiec;
    private javax.swing.JMenuItem eliminarprogeng;
    private javax.swing.JMenuItem eliminarprogfle;
    private javax.swing.JMenuItem eliminarprogfon;
    private javax.swing.JMenuItem eliminarproglinealsys;
    private javax.swing.JMenuItem eliminarproglinealtcy;
    private javax.swing.JMenuItem eliminarprogman;
    private javax.swing.JMenuItem eliminarprogmarv;
    private javax.swing.JMenuItem eliminarprogpdo;
    private javax.swing.JMenuItem eliminarprogray;
    private javax.swing.JMenuItem eliminarprogsuap;
    private javax.swing.JMenuItem eliminarprogsys;
    private javax.swing.JMenuItem eliminarprogtcy;
    private javax.swing.JMenuItem eliminarprogtro;
    private javax.swing.JPanel eng;
    private javax.swing.JMenuItem especartcom;
    private javax.swing.JMenuItem especartcorr;
    private javax.swing.JMenuItem especartdes;
    private javax.swing.JMenuItem especartdiec;
    private javax.swing.JMenuItem especarteng;
    private javax.swing.JMenuItem especartfle;
    private javax.swing.JMenuItem especartfon;
    private javax.swing.JMenuItem especartlinealsys;
    private javax.swing.JMenuItem especartlinealtcy;
    private javax.swing.JMenuItem especartman;
    private javax.swing.JMenuItem especartmarv;
    private javax.swing.JMenuItem especartpdo;
    private javax.swing.JMenuItem especartray;
    private javax.swing.JMenuItem especartsuap;
    private javax.swing.JMenuItem especartsys;
    private javax.swing.JMenuItem especarttcy;
    private javax.swing.JMenuItem especarttro;
    private javax.swing.JPanel fle;
    private javax.swing.JPanel fon;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel linealsys;
    private javax.swing.JPanel linealtcy;
    private javax.swing.JPanel man;
    private javax.swing.JPanel marv;
    private javax.swing.JPopupMenu menupopcom;
    private javax.swing.JPopupMenu menupopcorr;
    private javax.swing.JPopupMenu menupopdes;
    private javax.swing.JPopupMenu menupopdiec;
    private javax.swing.JPopupMenu menupopeng;
    private javax.swing.JPopupMenu menupopfle;
    private javax.swing.JPopupMenu menupopfon;
    private javax.swing.JPopupMenu menupoplinealsys;
    private javax.swing.JPopupMenu menupoplinealtcy;
    private javax.swing.JPopupMenu menupopman;
    private javax.swing.JPopupMenu menupopmarv;
    private javax.swing.JPopupMenu menupoppdo;
    private javax.swing.JPopupMenu menupopray;
    private javax.swing.JPopupMenu menupopsuap;
    private javax.swing.JPopupMenu menupopsys;
    private javax.swing.JPopupMenu menupoptcy;
    private javax.swing.JPopupMenu menupoptro;
    private javax.swing.JLabel noregistros;
    private javax.swing.JMenuItem nuevo_corr;
    private javax.swing.JPanel pdo;
    private javax.swing.JMenuItem programopcom;
    private javax.swing.JMenuItem programopdiec;
    private javax.swing.JMenuItem programopeng;
    private javax.swing.JMenuItem programoplinealsys;
    private javax.swing.JMenuItem programoplinealtcy;
    private javax.swing.JMenuItem programopmarv;
    private javax.swing.JMenuItem programoppdes;
    private javax.swing.JMenuItem programoppdo;
    private javax.swing.JMenuItem programoppfle;
    private javax.swing.JMenuItem programoppfon;
    private javax.swing.JMenuItem programoppman;
    private javax.swing.JMenuItem programopptro;
    private javax.swing.JMenuItem programopray;
    private javax.swing.JMenuItem programopsuap;
    private javax.swing.JMenuItem programopsys;
    private javax.swing.JMenuItem programoptcy;
    private javax.swing.JPanel ray;
    private javax.swing.JMenuItem restadiacom;
    private javax.swing.JMenuItem restadiades;
    private javax.swing.JMenuItem restadiadiec;
    private javax.swing.JMenuItem restadiaeng;
    private javax.swing.JMenuItem restadiafle;
    private javax.swing.JMenuItem restadialinealsys;
    private javax.swing.JMenuItem restadialinealtcy;
    private javax.swing.JMenuItem restadiaman;
    private javax.swing.JMenuItem restadiamarv;
    private javax.swing.JMenuItem restadiapdo;
    private javax.swing.JMenuItem restadiaray;
    private javax.swing.JMenuItem restadiasuap;
    private javax.swing.JMenuItem restadiasys;
    private javax.swing.JMenuItem restadiatcy;
    private javax.swing.JMenuItem restadiatro;
    private javax.swing.JPanel suap;
    private javax.swing.JPanel subpanelcom;
    private javax.swing.JPanel subpaneldes;
    private javax.swing.JPanel subpaneldiec;
    private javax.swing.JPanel subpaneleng;
    private javax.swing.JPanel subpanelfon;
    private javax.swing.JPanel subpanelfon1;
    private javax.swing.JPanel subpanellinealsys;
    private javax.swing.JPanel subpanellinealtcy;
    private javax.swing.JPanel subpanelman;
    private javax.swing.JPanel subpanelmarv;
    private javax.swing.JPanel subpanelpdo;
    private javax.swing.JPanel subpanelray;
    private javax.swing.JPanel subpanelsuap;
    private javax.swing.JPanel subpanelsys;
    private javax.swing.JPanel subpaneltcy;
    private javax.swing.JPanel subpaneltcy1;
    private javax.swing.JPanel subpaneltro;
    private javax.swing.JMenuItem sumadiacom;
    private javax.swing.JMenuItem sumadiades;
    private javax.swing.JMenuItem sumadiadiec;
    private javax.swing.JMenuItem sumadiaeng;
    private javax.swing.JMenuItem sumadiafle;
    private javax.swing.JMenuItem sumadialinealsys;
    private javax.swing.JMenuItem sumadialinealtcy;
    private javax.swing.JMenuItem sumadiaman;
    private javax.swing.JMenuItem sumadiamarv;
    private javax.swing.JMenuItem sumadiapdo;
    private javax.swing.JMenuItem sumadiaray;
    private javax.swing.JMenuItem sumadiasuap;
    private javax.swing.JMenuItem sumadiasys;
    private javax.swing.JMenuItem sumadiatcy;
    private javax.swing.JMenuItem sumadiatro;
    private javax.swing.JPanel sys;
    private javax.swing.JPanel tcy;
    private javax.swing.JMenuItem terminaprogcom;
    private javax.swing.JMenuItem terminaprogdes;
    private javax.swing.JMenuItem terminaprogdiec;
    private javax.swing.JMenuItem terminaprogeng;
    private javax.swing.JMenuItem terminaprogfle;
    private javax.swing.JMenuItem terminaprogfon;
    private javax.swing.JMenuItem terminaproglinealsys;
    private javax.swing.JMenuItem terminaproglinealtcy;
    private javax.swing.JMenuItem terminaprogman;
    private javax.swing.JMenuItem terminaprogmarv;
    private javax.swing.JMenuItem terminaprogpdo;
    private javax.swing.JMenuItem terminaprogray;
    private javax.swing.JMenuItem terminaprogsuap;
    private javax.swing.JMenuItem terminaprogsys;
    private javax.swing.JMenuItem terminaprogtcy;
    private javax.swing.JMenuItem terminaprogtro;
    private javax.swing.JPanel tro;
    // End of variables declaration//GEN-END:variables

    private JDialog datos_ops;
    private JDialog datos_articulos;
    private JDialog datos_programas_conversion;
    private JDialog datos_programas_corr;
    private JDialog corrugadora_herramienta_combina;
}
