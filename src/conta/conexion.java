/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package conta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.postgresql.util.Base64;
import java.util.GregorianCalendar;
import javax.swing.JTable;
import jxl.*;
import jxl.write.*;
import java.awt.Desktop;

/**
 *
 * @author ANGEL
 */
public class conexion {
    private static Connection conex=null;
    private static Connection conex_sql=null;
    private static ResultSet rsql=null;
    private static Statement stmt=null;
    private static String homeE = System.getProperty("user.home")+"/.temERP";
    private static Properties defaultProps = new Properties();
    private static FileOutputStream fin;
    private static FileInputStream ini;
    private static javax.crypto.Cipher cifrador;
    private static SecretKeySpec keySpec;
    private static byte[] llaveClave = "1324jsir836ctsd395udhn58".getBytes();
    private static SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    private static SimpleDateFormat fechamesano = new SimpleDateFormat("MMMMM '-' yyyy");

public static Connection abrirconexion()
{
    String driver="";
    String servidor="";
    String puerto="";
    String bd="";
    String usuario="";
    String clave="";
    try{
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
        driver=defaultProps.getProperty("Drivers");
        servidor=defaultProps.getProperty("Servidor");
        puerto=defaultProps.getProperty("Puerto");
        bd=defaultProps.getProperty("BD");
        usuario=defaultProps.getProperty("UsuarioBD");
        clave=decodificarCadena(defaultProps.getProperty("PassBD"));
    }
    catch(FileNotFoundException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(IOException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }


    conex=null;
    try{
        java.util.Properties prop = new java.util.Properties();
        prop.put("charSet", "utf8");
        prop.put("user", usuario);
        prop.put("password", clave);

        Class.forName(""+driver);
        conex=DriverManager.getConnection("jdbc:postgresql://"+servidor+":"+puerto+"/"+bd+"/",""+usuario+"",""+clave+"");
        //conex=DriverManager.getConnection("jdbc:postgresql://"+servidor+":"+puerto+"/"+bd+"/",prop);
        System.out.println("Conectado.................");
    }
    catch(java.lang.ClassNotFoundException e)     {  JOptionPane.showMessageDialog(null,"No se puede cargar el controlador:\n"+ e,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch (SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede realizar la conexion:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch (Exception ex)     {  JOptionPane.showMessageDialog(null,"No se puede realizar la conexion:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    return conex;
}

public static Connection abrirconexion_sqlserver()
{
    String driver="";
    String servidor="";
    String puerto="";
    String bd="";
    String usuario="";
    String clave="";
    String instancia="";
    try{
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
        driver=defaultProps.getProperty("sql_Drivers");
        servidor=defaultProps.getProperty("sql_Servidor");
        puerto=defaultProps.getProperty("sql_Puerto");
        bd=defaultProps.getProperty("sql_BD");
        usuario=defaultProps.getProperty("sql_UsuarioBD");
        clave=decodificarCadena(defaultProps.getProperty("sql_PassBD"));
        instancia=defaultProps.getProperty("sql_Instancia");
    }
    catch(FileNotFoundException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(IOException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }


    conex_sql=null;
    try{
        java.util.Properties prop = new java.util.Properties();
        prop.put("user", usuario);
        prop.put("password", clave);

        Class.forName(""+driver);
        String url = "jdbc:sqlserver://MYPC\\SQLEXPRESS;databaseName=MYDB";
        url = "jdbc:sqlserver://"+servidor+(puerto.equals("")? "": ":"+puerto)+"\\"+instancia+";databaseName="+bd+";";
        conex_sql=DriverManager.getConnection(url,""+usuario+"",""+clave+"");
        System.out.println("Conectado SQL Server .................");
    }
    catch(java.lang.ClassNotFoundException e)     {  JOptionPane.showMessageDialog(null,"No se puede cargar el controlador:\n"+ e,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch (SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede realizar la conexion:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch (Exception ex)     {  JOptionPane.showMessageDialog(null,"No se puede realizar la conexion:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    return conex_sql;
}

public static void modifica(String instruccion,Connection conec)
{
    String privi="";
    try{
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
    }
    catch(FileNotFoundException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(IOException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }


    if(privi.equals(""))//si no tiene datos busca en los datos en el XML
        privi=decodificarCadena(defaultProps.getProperty("Privilegios"));
    
    if(privi.equals("2")){
        stmt=null;
        try {
            stmt=conec.createStatement();
            stmt.executeUpdate(instruccion);
            stmt.close();
            JOptionPane.showMessageDialog(null, "DATOS GUARDADOS CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
        catch(Exception ex)     {   JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    }else{
        JOptionPane.showMessageDialog(null,"El USUARIO NO PUEDE LLEVAR ACABO ESTA ACCION","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
    }
 }
public static void modificamov(String instruccion,Connection conec)
{
    String privi="";
    try{
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
    }
    catch(FileNotFoundException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(IOException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

    if(privi.equals(""))//si no tiene datos busca en los datos en el XML
        privi=decodificarCadena(defaultProps.getProperty("Privilegios"));

    if(privi.equals("2")){
        stmt=null;
        try {
            stmt=conec.createStatement();
            stmt.executeUpdate(instruccion);
            stmt.close();
        }
        catch(SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
        catch(Exception ex)     {   JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    }
 }
public static void modificaupdate(String instruccion,Connection conec)
{
    String privi="";
    try{
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
    }
    catch(FileNotFoundException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(IOException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

    if(privi.equals(""))//si no tiene datos busca en los datos en el XML
        privi=decodificarCadena(defaultProps.getProperty("Privilegios"));

    if(privi.equals("2")){
        stmt=null;
        try {
            stmt=conec.createStatement();
            stmt.executeUpdate(instruccion);
            stmt.close();
            JOptionPane.showMessageDialog(null, "DATOS ACTUALIZADOS CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
        catch(Exception ex)     {   JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    }else{
        JOptionPane.showMessageDialog(null,"El USUARIO NO PUEDE LLEVAR ACABO ESTA ACCION","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
    }
 }

public static void modifica_p(String instruccion,Connection conec,String privi)
{
    try{
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
    }
    catch(FileNotFoundException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(IOException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }


    if(privi.equals(""))//si no tiene datos busca en los datos en el XML
        privi=decodificarCadena(defaultProps.getProperty("Privilegios"));

    if(privi.equals("2")){
        stmt=null;
        try {
            stmt=conec.createStatement();
            stmt.executeUpdate(instruccion);
            stmt.close();
            JOptionPane.showMessageDialog(null, "DATOS GUARDADOS CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
        catch(Exception ex)     {   JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    }else{
        JOptionPane.showMessageDialog(null,"El USUARIO NO PUEDE LLEVAR ACABO ESTA ACCION","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
    }
 }
public static void modificamov_p(String instruccion,Connection conec,String privi)
{
    try{
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
    }
    catch(FileNotFoundException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(IOException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

    if(privi.equals(""))//si no tiene datos busca en los datos en el XML
        privi=decodificarCadena(defaultProps.getProperty("Privilegios"));

    if(privi.equals("2")){
        stmt=null;
        try {
            stmt=conec.createStatement();
            stmt.executeUpdate(instruccion);
            stmt.close();
        }
        catch(SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
        catch(Exception ex)     {   JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    }
 }
public static void modificaupdate_p(String instruccion,Connection conec,String privi)
{
    try{
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
    }
    catch(FileNotFoundException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(IOException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

    if(privi.equals(""))//si no tiene datos busca en los datos en el XML
        privi=decodificarCadena(defaultProps.getProperty("Privilegios"));

    if(privi.equals("2")){
        stmt=null;
        try {
            stmt=conec.createStatement();
            stmt.executeUpdate(instruccion);
            stmt.close();
            JOptionPane.showMessageDialog(null, "DATOS ACTUALIZADOS CORRECTAMENTE", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
        catch(Exception ex)     {   JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    }else{
        JOptionPane.showMessageDialog(null,"El USUARIO NO PUEDE LLEVAR ACABO ESTA ACCION","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
    }
 }
public static void modificasin(String instruccion,Connection conec)
{
    try{
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
    }
    catch(FileNotFoundException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(IOException ex){JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

        stmt=null;
        try {
            stmt=conec.createStatement();
            stmt.executeUpdate(instruccion);
            stmt.close();
        }
        catch(SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
        catch(Exception ex)     {   JOptionPane.showMessageDialog(null,"No se puede llevar acabo la accion\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
 }
public static ResultSet consulta(String sentencia,Connection conec)
{
    rsql=null;
    stmt=null;
    try {
        stmt=conec.createStatement();
        rsql=stmt.executeQuery(sentencia);
    }
    catch (SQLException ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    //catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    return rsql;
}

public static ResultSet consultamov(String sentencia,Connection conec)
{
    rsql=null;
    stmt=null;
    try {
        stmt=conec.createStatement(rsql.TYPE_SCROLL_INSENSITIVE, rsql.CONCUR_READ_ONLY);
        rsql=stmt.executeQuery(sentencia);
    }
    catch (SQLException ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    //catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    return rsql;
}
public static String obtener_privilegios(Connection conn,String frame){
    Properties conf=archivoInicial();
    String valor="1";
    ResultSet rs0=null;
    try{
        String senSQL="SELECT * FROM privilegios2 WHERE (privilegio='"+conf.getProperty("UserID")+"' AND menu='"+frame+"');";
        rs0=consulta(senSQL,conn);
        if(rs0.next()){
            valor=rs0.getString("numero_sql");
        }
        if(rs0!=null) {  rs0.close();    }
    } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    return valor;
}
public static Properties archivoInicial(){
    /**generar archivo*/
    Directorio();
    Calendar calFin = Calendar.getInstance();
    calFin.set(calFin.get(Calendar.YEAR),calFin.get(Calendar.MONTH), 1);
    Date fechaIni=calFin.getTime();
    calFin.set(calFin.get(Calendar.YEAR),calFin.get(Calendar.MONTH), calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
    Date fechaFin = calFin.getTime();

    try {
        File archivo=new File(homeE + "/appE.xml");
        if (!archivo.exists()) {
            defaultProps.setProperty("Tema", "org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin");
            defaultProps.setProperty("Instancia", "0");
            defaultProps.setProperty("Servidor", "192.168.10.200");
            defaultProps.setProperty("Puerto", "5432");
            defaultProps.setProperty("BD", "Servikarton");
            defaultProps.setProperty("Privilegios", "");
            defaultProps.setProperty("UsuarioBD", "postgres");
            defaultProps.setProperty("PassBD", codificarCadena("sistemas2011"));
            defaultProps.setProperty("Drivers", "org.postgresql.Driver");
            defaultProps.setProperty("User", "");
            defaultProps.setProperty("UserID", "");
            defaultProps.setProperty("Periodo", fechamesano.format(fechaIni).toUpperCase());
            defaultProps.setProperty("FechaIni", fechainsertar.format(fechaIni));
            defaultProps.setProperty("FechaFin", fechainsertar.format(fechaFin));
            defaultProps.setProperty("Rutaplanos", "S:/BD_SK/PLANOS");
            defaultProps.setProperty("Rutaxml", System.getProperty("user.home"));
            fin = new FileOutputStream(homeE+"/appE.xml");
            defaultProps.storeToXML(fin, "Propiedades ENNOVI");
            fin.close();
            defaultProps.setProperty("sql_Servidor", "localhost");
            defaultProps.setProperty("sql_Puerto", "1433");
            defaultProps.setProperty("sql_BD", "aspel");
            defaultProps.setProperty("sql_UsuarioBD", "user");
            defaultProps.setProperty("sql_PassBD", codificarCadena("pass"));
            defaultProps.setProperty("sql_Drivers", "org.postgresql.Driver");
        }
        ini = new FileInputStream(homeE+"/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
    }
    catch(java.io.IOException ioex) { JOptionPane.showMessageDialog(null,"ERROR AL CREAR ARCHIVO PROPIEDADES:\n" +ioex.toString(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch(Exception ioex) { JOptionPane.showMessageDialog(null,"ERROR AL CREAR ARCHIVO PROPIEDADES:\n" +ioex.toString(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

    return defaultProps;
}

public static void escribir(String var, String val) {
    try {
        ini = new FileInputStream(homeE + "/appE.xml");
        defaultProps.loadFromXML(ini);
        ini.close();
        defaultProps.setProperty(var, var.equals("PassBD") || var.equals("sql_PassBD") || var.equals("Privilegios")?codificarCadena(val):val);
        fin = new FileOutputStream(homeE + "/appE.xml");
        defaultProps.storeToXML(fin, "Propiedades ENNOVI");
        fin.close();
    } catch (IOException ie) {ie.printStackTrace();}
}


private static String convertToHex(byte[] data) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
        int halfbyte = (data[i] >>> 4) & 0x0F;
        int two_halfs = 0;
        do {
            if ((0 <= halfbyte) && (halfbyte <= 9))
                buf.append((char) ('0' + halfbyte));
            else
                buf.append((char) ('a' + (halfbyte - 10)));
            halfbyte = data[i] & 0x0F;
        } while(two_halfs++ < 1);
    }
    return buf.toString();
}

public static String MD5(String text)
throws NoSuchAlgorithmException, UnsupportedEncodingException  {
    MessageDigest md;
    md = MessageDigest.getInstance("MD5");
    byte[] md5hash = new byte[32];
    md.update(text.getBytes("iso-8859-1"), 0, text.length());
    md5hash = md.digest();
    return convertToHex(md5hash);
}

public static String Directorio(){
    try {
        File dirPro = new File(homeE);
        if (!dirPro.exists()) {
            dirPro.mkdir();
        }
        if(System.getProperty("os.name").startsWith("Wind")){
                Runtime.getRuntime().exec("cmd /c attrib +h "+homeE.replace("\\", "/")+" /s /d");//agregar +s y será un archivo protegido por el sistema
        }

    }catch(IOException ioex) { JOptionPane.showMessageDialog(null,"ERROR AL CREAR DIRECTORIO:\n" +ioex.toString(),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }

    return homeE;
}

public static String codificarCadena(String contra){
    try{
        keySpec = new SecretKeySpec(llaveClave, "TripleDES");
        cifrador = Cipher.getInstance("TripleDES");
        cifrador.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] cipherbyte = cifrador.doFinal(contra.getBytes());
        String encodeTxt = Base64.encodeBytes(cipherbyte);
        return encodeTxt;
    }catch(Exception e){e.printStackTrace();return "nula";}
}

public static String decodificarCadena(String encrip){
    try {
        keySpec = new SecretKeySpec(llaveClave, "TripleDES");
        cifrador = Cipher.getInstance("TripleDES");
        cifrador.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encData = Base64.decode(encrip);
        byte[] decTxt = cifrador.doFinal(encData);
        return new String(decTxt);
    }catch(Exception e){e.printStackTrace();return "nula";}
}

////****eliminar para el control maestro*******////////
public static Connection abrirconexioncontrol() {
    Connection temcontrol=null;
    try{
        java.util.Properties prop = new java.util.Properties();
        prop.put("charSet", "ISO-8859-1");
        prop.put("user", "miguel");
        prop.put("password", "administrador2");

        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        temcontrol=DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=S:\\ControlM\\CONTROLM.mdb; SystemDB=system.mdw;",prop);
    }
    catch(java.lang.ClassNotFoundException e)     {  JOptionPane.showMessageDialog(null,"No se puede cargar el controlador:\n"+ e,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch (SQLException ex)     {  JOptionPane.showMessageDialog(null,"No se puede realizar la conexion:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    catch (Exception ex)     {  JOptionPane.showMessageDialog(null,"No se puede realizar la conexion:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    return temcontrol;
}
public static void actualizaSaldos(Connection conn,Date hoyini){
    Calendar calendarhoy = new GregorianCalendar();
    Calendar calendariniciosem = new GregorianCalendar();
    Calendar calendar1 = new GregorianCalendar();
    Calendar calendar2 = new GregorianCalendar();
    Calendar calendar3 = new GregorianCalendar();
    Calendar calendar4 = new GregorianCalendar();
    Calendar calendar5 = new GregorianCalendar();
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    //fecha de hoy para calculo de fechas de entrega
    calendarhoy.setTime(hoyini); //gregorian hoy
    calendariniciosem.setTime(hoyini); //gregorian hoy
    calendariniciosem.add(Calendar.DAY_OF_WEEK, (-calendariniciosem.get(Calendar.DAY_OF_WEEK)-1) );
    //fecha de fin de semana para fechas de entrega
    calendar1.setTime(calendariniciosem.getTime());
    calendar1.add(Calendar.DATE,7);
    calendar2.setTime(calendariniciosem.getTime());
    calendar2.add(Calendar.DATE,14);
    calendar3.setTime(calendariniciosem.getTime());
    calendar3.add(Calendar.DATE,21);
    calendar4.setTime(calendariniciosem.getTime());
    calendar4.add(Calendar.DATE,28);
    calendar5.setTime(calendariniciosem.getTime());
    calendar5.add(Calendar.DATE,35);

    /*System.err.println(""+fechainsertarhora.format(calendarhoy.getTime()));
    System.err.println(""+fechainsertarhora.format(calendariniciosem.getTime()));
    System.err.println(""+fechainsertarhora.format(calendar1.getTime()));
    System.err.println(""+fechainsertarhora.format(calendar2.getTime()));
    System.err.println(""+fechainsertarhora.format(calendar3.getTime()));
    System.err.println(""+fechainsertarhora.format(calendar4.getTime()));
    System.err.println(""+fechainsertarhora.format(calendar5.getTime()));*/

    //elimina los datos del sistema
    String senSQL="DELETE FROM saldos_pagos;";
    conexion.modificasin(senSQL, conn);

    ResultSet rs0=null;
    try{
        senSQL="SELECT docxcob.factura_serie,clientes.id_clientes,docxcob.fechaemision,docxcob.fechavencimiento,docxcob.importe,(docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0)) as saldo,COALESCE(notascreditorealizadas.importenotacredito,0) as importenotacredito,COALESCE(pagosrealizados.importepago,0) as importepago FROM ((docxcob LEFT JOIN clientes ON docxcob.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT notas_credito_detalle.factura_serie,sum(notas_credito_detalle.importe) as importenotacredito FROM notas_credito_detalle WHERE (notas_credito_detalle.estatus='1' AND notas_credito_detalle.fecha<='"+fechainsertar.format(hoyini)+" 23:59:59') GROUP BY notas_credito_detalle.factura_serie) as notascreditorealizadas ON docxcob.factura_serie=notascreditorealizadas.factura_serie ) LEFT JOIN (SELECT pagos_detalle.factura_serie,sum(pagos_detalle.importe+pagos_detalle.importe_factoraje) as importepago FROM pagos_detalle LEFT JOIN pagos ON pagos_detalle.id_pagos =pagos.id_pagos WHERE (pagos_detalle.estatus<>'Can' AND pagos_detalle.fecha<='"+fechainsertar.format(hoyini)+" 23:59:59') GROUP BY pagos_detalle.factura_serie) as pagosrealizados ON docxcob.factura_serie=pagosrealizados.factura_serie WHERE (docxcob.estatus<>'Can' AND docxcob.fechaemision<='"+fechainsertar.format(hoyini)+" 23:59:59' AND (docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0))>0.01)";
        rs0=conexion.consulta(senSQL,conn);
        while(rs0.next()){
            Date fechavencimiento = rs0.getTimestamp("fechavencimiento");
            Calendar calendarentrega = new GregorianCalendar();
            calendarentrega.setTime(fechavencimiento);

            Double saldo=rs0.getDouble("saldo");
            Double vencido=0.0;
            Double semana1=0.0;
            Double semana2=0.0;
            Double semana3=0.0;
            Double semana4=0.0;
            Double semana5=0.0;
            Double semana6=0.0;
            int dias=0;

            if (calendarentrega.before(calendarhoy)){ //vencido
                dias=diferenciasDeFechas(calendarentrega.getTime(), calendarhoy.getTime());
            }

            if (calendarentrega.before(calendarhoy) || calendarentrega.equals(calendarhoy)) //vencido
                vencido=saldo;
            //esta semana
            if ((calendarentrega.after(calendarhoy) && calendarentrega.before(calendar1)) || calendarentrega.equals(calendar1))
                semana1=saldo;
            //semana 2
            if ((calendarentrega.after(calendar1) && calendarentrega.before(calendar2)) || calendarentrega.equals(calendar2))
                semana2=saldo;
            //semana 3
            if ((calendarentrega.after(calendar2) && calendarentrega.before(calendar3)) || calendarentrega.equals(calendar3))
                semana3=saldo;
            //semana 4
            if ((calendarentrega.after(calendar3) && calendarentrega.before(calendar4)) || calendarentrega.equals(calendar4))
                semana4=saldo;
            //semana 5
            if ((calendarentrega.after(calendar4) && calendarentrega.before(calendar5)) || calendarentrega.equals(calendar5))
                semana5=saldo;
            //semana 6
            if (calendarentrega.after(calendar5))
                semana6=saldo;

            senSQL="INSERT INTO saldos_pagos(factura_serie, id_clientes, fechaemision, fechavencimiento, dias, importetotal, saldo, importenotacredito, importepago, vencido, semana1, semana2, semana3, semana4, semana5, adelante, fecha2, fecha3, fecha4, fecha5, fecha6) VALUES ('"+rs0.getString("factura_serie")+"', '"+rs0.getString("id_clientes")+"', '"+fechainsertarhora.format(rs0.getTimestamp("fechaemision"))+"', '"+fechainsertarhora.format(fechavencimiento)+"', '"+dias+"', '"+rs0.getDouble("importe")+"', '"+saldo+"', '"+rs0.getDouble("importenotacredito")+"', '"+rs0.getDouble("importepago")+"', '"+vencido+"', '"+semana1+"', '"+semana2+"', '"+semana3+"', '"+semana4+"', '"+semana5+"', '"+semana6+"', '"+fechainsertarhora.format(calendar1.getTime())+"', '"+fechainsertarhora.format(calendar2.getTime())+"', '"+fechainsertarhora.format(calendar3.getTime())+"', '"+fechainsertarhora.format(calendar4.getTime())+"', '"+fechainsertarhora.format(calendar5.getTime())+"');";
            conexion.modificasin(senSQL,conn);
        }
        if(rs0!=null) {   rs0.close();   }
    } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


}

public static void exportaTabla(JTable tabla_datos,String titulo,String archivo){

        File rutaarchivo=new File(System.getProperty("user.home")+"/"+archivo);
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

            WritableCellFormat arial10fdetalle = new WritableCellFormat (arial9);

            int filainicial=5;

            //titulo del documento
            sheet.addImage(new jxl.write.WritableImage(0, 0, 3, 4,new java.io.File(conexion.Directorio()+"/logoempresa.png")));
            sheet.addCell(new jxl.write.Label(0, filainicial, titulo,arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for(int j=0;j<(tabla_datos.getColumnCount());j=j+1){
                String titu=""+tabla_datos.getColumnName(j); //agrega el nombre del titulo
                int dotPos = titu.lastIndexOf(">")+1;//le quita el html de los titulos
                if(titu.contains("<html>")){
                    titu=titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu,arial10ftitulo));
                sheet.setColumnView( j, ((tabla_datos.getColumnModel().getColumn(j).getPreferredWidth())/6) );
            }

            filainicial++;//incrementa las filas
            for(int i=0;i<(tabla_datos.getRowCount());i=i+1){
                for(int j=0;j<(tabla_datos.getColumnCount());j=j+1){
                    if(tabla_datos.getValueAt(i, j) != null){
                        if(tabla_datos.getValueAt(i, j) instanceof String){
                            String dato=(String)tabla_datos.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">")+1;//le quita el html de los titulos
                            if(dato.contains("<html>")){
                                dato=dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i+filainicial, dato,arial10fdetalle));
                        }
                        else if(tabla_datos.getValueAt(i, j) instanceof java.lang.Number)
                            sheet.addCell(new jxl.write.Number(j, i+filainicial, Double.parseDouble(tabla_datos.getValueAt(i, j).toString()),arial10fdetalle));
                        else if(tabla_datos.getValueAt(i, j) instanceof java.util.Date)
                            sheet.addCell(new jxl.write.DateTime(j, i+filainicial, (java.util.Date)tabla_datos.getValueAt(i, j), jxl.write.DateTime.GMT));
                        else
                            sheet.addCell(new jxl.write.Boolean(j, i+filainicial,(java.lang.Boolean)tabla_datos.getValueAt(i, j),arial10fdetalle));
                    }
                }
            }/**fin de revisar los campos vacios*/


            //Escribimos los resultados al fichero Excel
            workbook.write();
            workbook.close();

            //abrir el documento creado
            Desktop.getDesktop().open(rutaarchivo);
        }
        catch (IOException ex) {
           JOptionPane.showMessageDialog(null,"EL ARCHIVO ESTA ABIERTO O NO SE PUEDE CREAR"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
        catch (WriteException exe) {
           JOptionPane.showMessageDialog(null,"ERROR AL EXPORTAR DATOS"+exe,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"NO SE PUEDE ABRIR EL ARCHIVO\n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }

}

public static synchronized int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String fechaInicioString = df.format(fechaInicial);
        try {
            fechaInicial = df.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = df.format(fechaFinal);
        try {
            fechaFinal = df.parse(fechaFinalString);
        } catch (ParseException ex) {
        }

        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }

    static ResultSet consulta(String senSQL) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
