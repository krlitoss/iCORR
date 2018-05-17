/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package conta;

import java.io.*;
import java.util.Date;
import java.text.*;

import mx.bigdata.sat.cfdi.CFDv32;

import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.Connection;

import com.mx.sit.factura.soap.srv.CfdiServiceInterface;
import com.mx.sit.factura.soap.srv.FiscoClicException_Exception;
import com.mx.sit.factura.soap.srv.FiscoClicWS;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Complemento;
import mx.bigdata.sat.cfdi.v32.schema.TimbreFiscalDigital;
import mx.bigdata.sat.cfdi.v32.schema.ObjectFactory;
import org.xml.sax.InputSource;


/**
 *
 * @author USUARIO
 */
public class CFDIconexion {

    private static ResultSet rs0 = null;

    public static String timbra_cfdi(String xml,Connection conec) {
        
        String timbre_f="";
        xml=""+xml.replaceAll("\n|\r|\t", ""); //limpiamos retorno de carro
        xml = xml.trim(); //limpia espacios
        FiscoClicWS ws=new FiscoClicWS();
        final CfdiServiceInterface elservicio= ws.getFiscoClicSoapPort();
        try {
            String user="";//AAA111111ZZZ
            String pass="";//TeStInGfIsCoClIc2012Ws
            String rfcEmisor="";//AAA010101AAA
            String uuid="";//FD94ED4E-7E57-46FE-8FBD-D400A1FAF7FA
            rs0 = null; //realizamos consulta para datos de conexion
            String senSQL = "SELECT * FROM empresa WHERE id=1;";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                user=rs0.getString("user_fiscoclic");
                pass=rs0.getString("pass_fiscoclic");
                rfcEmisor=rs0.getString("rfc").toUpperCase().trim();
            }
            long startTime = System.currentTimeMillis();
            timbre_f=elservicio.timbraCFDIXML(xml, user, pass); //genera elk timbre para poder guardarlo
            System.out.println("timbraCFDIXMLTest " + (double)(System.currentTimeMillis()- startTime)/1000 + " segundos\n\n\n"); //tiempo en generar el timbre

        }catch(FiscoClicException_Exception  exx){ //manejo de errores del webservice
            String erro="Campo:" + exx.getFaultInfo().getCampo();
            erro+="Codigo:" + exx.getFaultInfo().getCodigo();
            erro+="Mensaje:" + exx.getFaultInfo().getMessage();
            erro="Servicio:" + exx.getFaultInfo().getServicio();
            JOptionPane.showMessageDialog(null, "NO SE PUDO TIMBRAR LA FACTURA LA DEBES CANCELAR \n" + erro+"\n ERROR:"+exx, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }catch(Exception  exx){
            JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR LOS DATOS DE CONEXION \n" + exx, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }
        return timbre_f;
        
    }
    public static String timbra_cfdi_pruebaxxx(String xml,Connection conec){
        //PRUEBAS DE DE FIJO
        //String xml_finalx="<?xml version=\"1.0\" encoding=\"UTF-8\"?><cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" LugarExpedicion=\"TOLCAYUCA, HIDALGO, MEXICO\" metodoDePago=\"No Identificado\" tipoDeComprobante=\"ingreso\" total=\"1160.00\" descuento=\"0.00\" subTotal=\"1000.00\" certificado=\"MIIEdDCCA1ygAwIBAgIUMjAwMDEwMDAwMDAxMDAwMDU4NjcwDQYJKoZIhvcNAQEFBQAwggFvMRgwFgYDVQQDDA9BLkMuIGRlIHBydWViYXMxLzAtBgNVBAoMJlNlcnZpY2lvIGRlIEFkbWluaXN0cmFjacOzbiBUcmlidXRhcmlhMTgwNgYDVQQLDC9BZG1pbmlzdHJhY2nDs24gZGUgU2VndXJpZGFkIGRlIGxhIEluZm9ybWFjacOzbjEpMCcGCSqGSIb3DQEJARYaYXNpc25ldEBwcnVlYmFzLnNhdC5nb2IubXgxJjAkBgNVBAkMHUF2LiBIaWRhbGdvIDc3LCBDb2wuIEd1ZXJyZXJvMQ4wDAYDVQQRDAUwNjMwMDELMAkGA1UEBhMCTVgxGTAXBgNVBAgMEERpc3RyaXRvIEZlZGVyYWwxEjAQBgNVBAcMCUNveW9hY8OhbjEVMBMGA1UELRMMU0FUOTcwNzAxTk4zMTIwMAYJKoZIhvcNAQkCDCNSZXNwb25zYWJsZTogSMOpY3RvciBPcm5lbGFzIEFyY2lnYTAeFw0xMjA3MjcxNzAyMDBaFw0xNjA3MjcxNzAyMDBaMIHbMSkwJwYDVQQDEyBBQ0NFTSBTRVJWSUNJT1MgRU1QUkVTQVJJQUxFUyBTQzEpMCcGA1UEKRMgQUNDRU0gU0VSVklDSU9TIEVNUFJFU0FSSUFMRVMgU0MxKTAnBgNVBAoTIEFDQ0VNIFNFUlZJQ0lPUyBFTVBSRVNBUklBTEVTIFNDMSUwIwYDVQQtExxBQUEwMTAxMDFBQUEgLyBIRUdUNzYxMDAzNFMyMR4wHAYDVQQFExUgLyBIRUdUNzYxMDAzTURGUk5OMDkxETAPBgNVBAsTCFVuaWRhZCAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2TTQSPONBOVxpXv9wLYo8jezBrb34i/tLx8jGdtyy27BcesOav2c1NS/Gdv10u9SkWtwdy34uRAVe7H0a3VMRLHAkvp2qMCHaZc4T8k47Jtb9wrOEh/XFS8LgT4y5OQYo6civfXXdlvxWU/gdM/e6I2lg6FGorP8H4GPAJ/qCNwIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQUFAAOCAQEATxMecTpMbdhSHo6KVUg4QVF4Op2IBhiMaOrtrXBdJgzGotUFcJgdBCMjtTZXSlq1S4DG1jr8p4NzQlzxsdTxaB8nSKJ4KEMgIT7E62xRUj15jI49qFz7f2uMttZLNThipunsN/NF1XtvESMTDwQFvas/Ugig6qwEfSZc0MDxMpKLEkEePmQwtZD+zXFSMVa6hmOu4M+FzGiRXbj4YJXn9Myjd8xbL/c+9UIcrYoZskxDvMxc6/6M3rNNDY3OFhBK+V/sPMzWWGt8S1yjmtPfXgFs1t65AZ2hcTwTAuHrKwDatJ1ZPfa482ZBROAAX1waz7WwXp0gso7sDCm2/yUVww==\" noCertificado=\"20001000000100005867\" formaDePago=\"PAGO EN UNA SOLA EXHIBICION\" sello=\"oeokhevcSW1oQsSsDEXDgTj6UY/J2+Y4JRTVdc4phNGMlbEoZtBhP6kyUZ9MG+XEwXS/1TArJ7wW7BWscYDlOewYM2ctivM3EuJvgQE9j4r/jP+CyxvyyFHEb7NJ6LpKUJHeJ87oHf84Qbeb3sbpFpr2BJBB4aBdLPy48uOnD0g=\" fecha=\"2013-11-01T11:35:25\" folio=\"407\" serie=\"I\" version=\"3.2\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3  http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd\">    <cfdi:Emisor nombre=\"Pruebas Fisco SA de CV\" rfc=\"AAA010101AAA\">        <cfdi:DomicilioFiscal codigoPostal=\"10000\" pais=\"Mexico\" estado=\"Distrito Federal\" municipio=\"Delegacion Prueba\" localidad=\"df\" colonia=\"Colonia Prueba\" noExterior=\"7\" calle=\"Calle Prueba\"/>        <cfdi:ExpedidoEn pais=\"MEXICO\" estado=\"Distrito Federal\" municipio=\"Alvaro Obregon\"/>        <cfdi:RegimenFiscal Regimen=\"Personas Morales del regimen simplificado\"/>    </cfdi:Emisor>    <cfdi:Receptor nombre=\"ARPELY, S.A. DE C.V.\" rfc=\"ARP960126UY6\">        <cfdi:Domicilio codigoPostal=\"42060\" pais=\"MEXICO\" estado=\"HIDALGO\" municipio=\"SAN AGUSTIN TLAXIACA\" colonia=\"SECTOR CASA GRANDE\" noExterior=\"60\" calle=\"HOMBRES ILUSTRES\"/>    </cfdi:Receptor>    <cfdi:Conceptos>        <cfdi:Concepto importe=\"1000.00\" valorUnitario=\"1000.00000\" descripcion=\"CONCEPTO PRUEBA\" noIdentificacion=\"n/a\" unidad=\"Pieza\" cantidad=\"1.00\"/>    </cfdi:Conceptos>    <cfdi:Impuestos totalImpuestosTrasladados=\"160.00\" totalImpuestosRetenidos=\"0.00\">        <cfdi:Traslados>            <cfdi:Traslado importe=\"160.00\" tasa=\"16.00\" impuesto=\"IVA\"/>        </cfdi:Traslados>    </cfdi:Impuestos></cfdi:Comprobante>";
        //xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" LugarExpedicion=\"TOLCAYUCA, HIDALGO, MEXICO\" metodoDePago=\"No Identificado\" tipoDeComprobante=\"ingreso\" total=\"15093.22\" descuento=\"0.00\" subTotal=\"13011.40\" certificado=\"MIIEaDCCA1CgAwIBAgIUMDAwMDEwMDAwMDAyMDI2OTczMzQwDQYJKoZIhvcNAQEFBQAwggGVMTgwNgYDVQQDDC9BLkMuIGRlbCBTZXJ2aWNpbyBkZSBBZG1pbmlzdHJhY2nDs24gVHJpYnV0YXJpYTEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSEwHwYJKoZIhvcNAQkBFhJhc2lzbmV0QHNhdC5nb2IubXgxJjAkBgNVBAkMHUF2LiBIaWRhbGdvIDc3LCBDb2wuIEd1ZXJyZXJvMQ4wDAYDVQQRDAUwNjMwMDELMAkGA1UEBhMCTVgxGTAXBgNVBAgMEERpc3RyaXRvIEZlZGVyYWwxFDASBgNVBAcMC0N1YXVodMOpbW9jMRUwEwYDVQQtEwxTQVQ5NzA3MDFOTjMxPjA8BgkqhkiG9w0BCQIML1Jlc3BvbnNhYmxlOiBDZWNpbGlhIEd1aWxsZXJtaW5hIEdhcmPDrWEgR3VlcnJhMB4XDTEyMTIyNzE2MzI0OFoXDTE2MTIyNzE2MzI0OFowgakxIDAeBgNVBAMTF1ZJQ1RPUiBKSU1FTkVaIE1BUlRJTkVaMSAwHgYDVQQpExdWSUNUT1IgSklNRU5FWiBNQVJUSU5FWjEgMB4GA1UEChMXVklDVE9SIEpJTUVORVogTUFSVElORVoxFjAUBgNVBC0TDUpJTVY3MjAxMzFOTTExGzAZBgNVBAUTEkpJTVY3MjAxMzFISEdNUkMwNzEMMAoGA1UECxMDRUVDMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVUvJdzyD3WuCCC30F2mKzmSfydIZGzOfxjDDEX/DMR7bvMJS6oBt7DXmNiZn1TF1xAFYOvxc61jCiHO9jwgeNLsud2NzzYWL0vi5nzaLzNaccwGiVfAODUOC6dj4ZoHftspB0tO/Ru1d2SDHvkEacGArQz3qiycZ9/jC4RFiB4QIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQUFAAOCAQEAi1f6A2cFvKMo1xV/rSVqv/S5RjXPqzwe2b81Fjo4EctZT0wUlPudc63FUcPICpn1wLYUvhzDbNssba5HEofuQdI6HKLSgby1h0WQg3dvfH3yCWhIEg3/TGdNhR6Pc9H3hMg2OZqF0AAJeN6UlSbqzt3GQjARiB9lBlV9Mc6+OjLoomMTBTcbwTkVUgoLEwm/8gDE/KQXZCjpYQksvWmDxP25nooZfSblAb7kRMwuAUnc64mS+2Cr1OiAtRTMCpe4mSY9uHjwkzsb9igZwxkFrf+Yq1xecVnnqO/XGrjAGYMrK2nzJIB99p8hHBouquDJxfnjNPo4pHr57g4ENh6lsA==\" noCertificado=\"00001000000202697334\" formaDePago=\"PAGO EN UNA SOLA EXHIBICION\" sello=\"gPJLqKibbodRUmQHQbUQbAB2jQ7n4hg/y3tQAHJkfBvGxxgOkSwiQy7Lma/gmkGzQ74helN+T2KUgzCIevS4++luTPcBsULCYTspen1ivb6sb+lubcDr9JTbkGleGf/Uc3inpDVpnaY3e2BTZ+wE6PopeoEqun0a2gofHGLjBrk=\" fecha=\"2013-11-14T15:09:38\" folio=\"460\" serie=\"I\" version=\"3.2\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3  http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd\">    <cfdi:Emisor nombre=\"Pruebas Fisco SA de CV\" rfc=\"AAA010101AAA\">        <cfdi:DomicilioFiscal codigoPostal=\"10000\" pais=\"Mexico\" estado=\"Distrito Federal\" municipio=\"Delegacion Prueba\" localidad=\"df\" colonia=\"Colonia Prueba\" noExterior=\"7\" calle=\"Calle Prueba\"/>        <cfdi:ExpedidoEn pais=\"MEXICO\" estado=\"Distrito Federal\" municipio=\"Alvaro Obregon\"/>        <cfdi:RegimenFiscal Regimen=\"Personas Morales del regimen simplificado\"/>    </cfdi:Emisor>    <cfdi:Receptor nombre=\"JOSE MIGUEL PEREZ TERAN\" rfc=\"PETM880309TD2\">        <cfdi:Domicilio codigoPostal=\"03100\" pais=\"MÃ©xico\" estado=\"D.F.\" municipio=\"n/a\" referencia=\"n/a\" localidad=\"MEXICO\" colonia=\"DEL VALLE\" noInterior=\"n/a\" noExterior=\"616-5\" calle=\"HERIBERTO FRIAS\"/>    </cfdi:Receptor>    <cfdi:Conceptos>        <cfdi:Concepto importe=\"13011.40\" valorUnitario=\"6.70000\" descripcion=\"CAJA 6 ROLLOS DE ALUMINIO (ALUFUERTE 400)\" noIdentificacion=\"1794\" unidad=\"Pieza\" cantidad=\"1942.00\"/>    </cfdi:Conceptos>    <cfdi:Impuestos totalImpuestosTrasladados=\"2081.82\" totalImpuestosRetenidos=\"0.00\">        <cfdi:Traslados>            <cfdi:Traslado importe=\"2081.82\" tasa=\"16.00\" impuesto=\"IVA\"/>        </cfdi:Traslados>    </cfdi:Impuestos></cfdi:Comprobante>";

        String timbre_f="";
        xml=""+xml.replaceAll("\n|\r|\t", "");
        xml = xml.trim();
        FiscoClicWS ws=new FiscoClicWS();
        final CfdiServiceInterface elservicio= ws.getFiscoClicSoapPort();
        try {
            String user="";//AAA111111ZZZ
            String pass="";//TeStInGfIsCoClIc2012Ws
            String rfcEmisor="";//AAA010101AAA
            String uuid="";//FD94ED4E-7E57-46FE-8FBD-D400A1FAF7FA
            rs0 = null;
            String senSQL = "SELECT * FROM empresa2 WHERE id=2;";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                user=rs0.getString("user_fiscoclic");
                pass=rs0.getString("pass_fiscoclic");
                rfcEmisor=rs0.getString("rfc").toUpperCase().trim();
            }
            long startTime = System.currentTimeMillis();
            System.err.println("XML:"+xml); //imprimirmos el timbre
            timbre_f=elservicio.timbraCFDIXMLTest(xml, user, pass);
            System.out.println("Resultado:"+timbre_f); //imprimimos el timbre
            System.out.println("timbraCFDIXMLTest " + (double)(System.currentTimeMillis()- startTime)/1000 + " segundos\n\n\n"); //tiempo en generar el timbre

        }catch(FiscoClicException_Exception  exx){
            String erro="Campo:" + exx.getFaultInfo().getCampo();
            erro+="Codigo:" + exx.getFaultInfo().getCodigo();
            erro+="Mensaje:" + exx.getFaultInfo().getMessage();
            erro="Servicio:" + exx.getFaultInfo().getServicio();
            JOptionPane.showMessageDialog(null, "NO SE PUDO TIMBRAR LA FACTURA LA DEBES CANCELAR \n" + erro+"\n ERROR:"+exx, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }catch(Exception  exx){
            JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR LOS DATOS DE XONEXION \n" + exx, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }
        return timbre_f;
        
    }
   
    public static void cancela_cfdi_save(String uuid_c,String folio,Connection conec){

        try {
            //cuando se guardan es en 1 y cuando se cancela se queda en 0 para despues cambiar estatus a 1 nuevamente
            SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String senSQLmov = "UPDATE xmlfinal SET estatus_cancela='1',fecha_cancela='"+fechainsertarhora.format(new Date())+"' WHERE factura_serie='" + folio + "' AND uuid='"+uuid_c+"';";
            conexion.modificamov(senSQLmov, conec);

        }catch(Exception  exx){
            JOptionPane.showMessageDialog(null, "NO SE PUDO CANCELAR EL DOCUMENTO:"+exx.getMessage(), "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }

    }


    public static boolean cancela_cfdi(String uuid_c,Connection conec){
        
//        //PRUEBAS DE DE FIJO
        boolean cancela_f=false;

        FiscoClicWS ws=new FiscoClicWS();
        final CfdiServiceInterface elservicio= ws.getFiscoClicSoapPort();

            try {
            String user="";//AAA111111ZZZ
            String pass="";//TeStInGfIsCoClIc2012Ws
            String rfcEmisor="";//AAA010101AAA
            String uuid=uuid_c;//FD94ED4E-7E57-46FE-8FBD-D400A1FAF7FA
            rs0 = null;
            String senSQL = "SELECT * FROM empresa WHERE id=1;";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                user=rs0.getString("user_fiscoclic");
                pass=rs0.getString("pass_fiscoclic");
                rfcEmisor=rs0.getString("rfc").toUpperCase().trim();
            }
            long startTime = System.currentTimeMillis();
            cancela_f=elservicio.cancelaCFDI(uuid, rfcEmisor, user, pass);
            System.out.println("cancelaCFDI " + (double)(System.currentTimeMillis()- startTime)/1000 + " segundos\n\n\n"); //tiempo en generar el timbre
            
            if(!cancela_f){
                JOptionPane.showMessageDialog(null, "NO SE PUDO CANCELAR EL DOCUMENTO: \n" + uuid, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
            }

        }catch(FiscoClicException_Exception  exx){
            String erro="Campo:" + exx.getFaultInfo().getCampo();
            erro+="Codigo:" + exx.getFaultInfo().getCodigo();
            erro+="Mensaje:" + exx.getFaultInfo().getMessage();
            erro="Servicio:" + exx.getFaultInfo().getServicio();
            JOptionPane.showMessageDialog(null, "NO SE PUDO CANCELAR EL DOCUMENTO \n" + erro+"\n ERROR:"+exx, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }catch(Exception  exx){
            JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR LOS DATOS DE XONEXION \n" + exx, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }

        try {
            String user="";//AAA111111ZZZ
            String pass="";//TeStInGfIsCoClIc2012Ws
            String rfcEmisor="";//AAA010101AAA
            String uuid=uuid_c;//FD94ED4E-7E57-46FE-8FBD-D400A1FAF7FA
            rs0 = null;
            String senSQL = "SELECT * FROM empresa WHERE id=1;";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                user=rs0.getString("user_fiscoclic");
                pass=rs0.getString("pass_fiscoclic");
                rfcEmisor=rs0.getString("rfc").toUpperCase().trim();
            }
            
            LbsCrypt crypt = new LbsCrypt(new FileInputStream("ACO560518KW7.key"), "12345678a", new FileInputStream("ACO560518KW7.cer"));
            CfdiF33.cancelar("Sistemas2011*", "ACO560518KW7", "6EA9FB58-37CB-40CD-B599-535F12EA4F55", crypt);
            cancela_f = true;

        }catch(Exception  exx){
            JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR LOS DATOS DE XONEXION \n" + exx, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }
        
        return cancela_f;

    }

    public static boolean cancela_cfdi_pruebaxxx(String uuid_c,Connection conec){
        //PRUEBAS DE DE FIJO
        boolean cancela_f=false;

        FiscoClicWS ws=new FiscoClicWS();
        final CfdiServiceInterface elservicio= ws.getFiscoClicSoapPort();

        try {
            String user="";//AAA111111ZZZ
            String pass="";//TeStInGfIsCoClIc2012Ws
            String rfcEmisor="";//AAA010101AAA
            String uuid="FD94ED4E-7E57-46FE-8FBD-D400A1FAF7FA";//FD94ED4E-7E57-46FE-8FBD-D400A1FAF7FA
            rs0 = null;
            String senSQL = "SELECT * FROM empresa2 WHERE id=2;";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                user=rs0.getString("user_fiscoclic");
                pass=rs0.getString("pass_fiscoclic");
                rfcEmisor=rs0.getString("rfc").toUpperCase().trim();
            }
            long startTime = System.currentTimeMillis();
            cancela_f=elservicio.cancelaCFDITest(uuid, rfcEmisor, user, pass);
            System.out.println("uuid:"+uuid_c);
            System.out.println("Resultado:"+cancela_f); //imprimimos el timbre
            System.out.println("timbraCFDIXMLTest " + (double)(System.currentTimeMillis()- startTime)/1000 + " segundos\n\n\n"); //tiempo en generar el timbre
            if(!cancela_f){
                JOptionPane.showMessageDialog(null, "NO SE PUDO CANCELAR EL DOCUMENTO: \n" + uuid, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
            }

        }catch(FiscoClicException_Exception  exx){
            String erro="Campo:" + exx.getFaultInfo().getCampo();
            erro+="Codigo:" + exx.getFaultInfo().getCodigo();
            erro+="Mensaje:" + exx.getFaultInfo().getMessage();
            erro="Servicio:" + exx.getFaultInfo().getServicio();
            JOptionPane.showMessageDialog(null, "NO SE PUDO CANCELAR EL DOCUMENTO \n" + erro+"\n ERROR:"+exx, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }catch(Exception  exx){
            JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR LOS DATOS DE XONEXION \n" + exx, "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }
        return cancela_f;

    }
    

    public static CFDv32 agregar_nodo_timbre(String timbre_cadena,Comprobante comp){

        CFDv32 cfdTimbrado=null;

        try{

            ObjectFactory of = new ObjectFactory();
            TimbreFiscalDigital timbre = of.createTimbreFiscalDigital();

            javax.xml.parsers.DocumentBuilder db = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(timbre_cadena));
            org.w3c.dom.Document dom = db.parse(is);

            org.w3c.dom.NodeList rootElement_node = dom.getElementsByTagName("tfd:TimbreFiscalDigital");
            org.w3c.dom.Element rootElement = (org.w3c.dom.Element) rootElement_node.item(0);
            //ajustamos fecha
            String fe=""+rootElement.getAttribute("FechaTimbrado");
            fe=fe.replace("T", " ");
            fe=fe.replace("-", "/");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            timbre.setFechaTimbrado(df.parse(fe));
            timbre.setNoCertificadoSAT(""+rootElement.getAttribute("noCertificadoSAT"));
            timbre.setVersion(""+rootElement.getAttribute("version"));
            timbre.setSelloCFD(""+rootElement.getAttribute("selloCFD"));
            timbre.setSelloSAT(""+rootElement.getAttribute("selloSAT"));
            timbre.setUUID(""+rootElement.getAttribute("UUID"));

            Complemento complemento = of.createComprobanteComplemento();
            complemento.getAny().add(timbre);
            comp.setComplemento(complemento);
            cfdTimbrado = new CFDv32(comp);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "ERROR EN FECHA DE TIMBRE O PARSE XML\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
        return cfdTimbrado;
    }

    public static String cadena_timbre(String timbre_cadena,String campo){

        String valor="";

        try{

            javax.xml.parsers.DocumentBuilder db = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(timbre_cadena));
            org.w3c.dom.Document dom = db.parse(is);

            org.w3c.dom.NodeList rootElement_node = dom.getElementsByTagName("tfd:TimbreFiscalDigital");
            org.w3c.dom.Element rootElement = (org.w3c.dom.Element) rootElement_node.item(0);

            //ajustamos fecha
            String fe=""+rootElement.getAttribute("FechaTimbrado");
            fe=fe.replace("T", " ");
            fe=fe.replace("-", "/");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date fechatim=df.parse(fe);

            valor=""+rootElement.getAttribute(campo);

        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "ERROR AL PARSE XML\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
        return valor;
    }

    public static boolean valida_con_internet(){
        /*boolean reachable = false;
        Socket socket = null;
        try {
            socket = new Socket("www.fiscoclic.mx", 80);
            reachable = true;
        } catch(Exception ex){

        } finally {
            if (socket != null) try { socket.close(); } catch(IOException e) {}
        }*/
        try {
            URL url = new URL("https://www.fiscoclic.mx");
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
            Object objData = urlConnect.getContent();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

        //return reachable;
    }

public static String remove1(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }//remove1

}
