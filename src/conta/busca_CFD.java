/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * busca_fechasproveedor.java
 *
 * Created on 10/02/2010, 09:45:29 AM
 */
package conta;

import java.io.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JLabel;
import javax.swing.JTextField;

import mx.bigdata.sat.cfd.CFDv22;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import mx.bigdata.sat.cfdi.CFDv32;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

/**
 *
 * @author ANGEL
 */
public class busca_CFD extends javax.swing.JDialog {

    Connection connj;
    ResultSet rs0 = null;
    String estado = "cancelar";
    private Properties conf;
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechasin = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat fechaparser = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fechamostrartodo = new SimpleDateFormat(" dd 'del mes de' MMMMM 'de' yyyy");
    //SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    DecimalFormat num_qr_decimales = new DecimalFormat("0000000000.000000");
    String clavemodificaf = "";
    String rfcempresa = "";
    String nombrecliente = "";
    String nombreempresacomercial = "";
    Date fechacreado = new Date();
    Date fechavencimiento = new Date();
    String varios = "0";
    barraespera barraespera = new barraespera(null, true, "Enviando correo...");
    String errorenviando = "";
    Boolean cancelado = false;
    String telefono = "";
    String estatusfacx = "";
    String variasfac = "";

    /**
     * Creates new form busca_fechasproveedor
     */
    public busca_CFD(java.awt.Frame parent, boolean modal, Connection conn, String clavemodifica, String tipo) {
        super(parent, modal);
        initComponents();
        connj = conn;
        conf = conexion.archivoInicial();
        clavemodificaf = clavemodifica;
        asunto.setText("Comprobante Fiscal Digital No. " + clavemodifica);
        datosEmpresa();
        datosCorreo();
        datosCliente(tipo);
        panelcorreo.setVisible(false);
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);
    }

    public void salir() {
        if (connj != null) {
            connj = null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }

    public String getEstado() {
        return estado;
    }

    public void datosEmpresa() {
        rs0 = null;
        try {
            String senSQL = "SELECT empresa.rfc,empresa.nombrecomercial,empresa.telefono FROM empresa WHERE id='1'";
            rs0 = conexion.consulta(senSQL, connj);
            if (rs0.next()) {
                rfcempresa = rs0.getString("rfc");
                nombreempresacomercial = rs0.getString("nombrecomercial");
                telefono = rs0.getString("telefono");
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void datosCorreo() {
        rs0 = null;
        try {
            String senSQL = "SELECT usuario FROM correo WHERE uso='true'";
            rs0 = conexion.consulta(senSQL, connj);
            if (rs0.next()) {
                de.setText(rs0.getString("usuario"));
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void datosCliente(String tipof) {
        if (tipof.equals("fac")) {
            rs0 = null;
            try {
                String senSQL = "SELECT facturas.factura_serie,facturas.fecha,facturas.varios,clientes.*,docxcob.fechavencimiento,facturas.estatus FROM (facturas LEFT JOIN docxcob ON facturas.factura_serie=docxcob.factura_serie) LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes WHERE facturas.factura_serie='" + clavemodificaf + "';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    para.setText(rs0.getString("email_cfd"));
                    nombrecliente = rs0.getString("nombre");
                    fechacreado = rs0.getTimestamp("fecha");
                    fechavencimiento = rs0.getTimestamp("fechavencimiento");
                    varios = rs0.getString("varios");
                    if (rs0.getString("estatus").equals("0")) {
                        estatusfacx = " ( CANCELADA )";
                    }

                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
            //busca correo el lugares de entrega
            rs0 = null;
            try {
                String senSQL = "SELECT lugares_entregas.email_cfd FROM facturas_detalle LEFT JOIN (ops LEFT JOIN lugares_entregas ON ops.id_lugarentrega=lugares_entregas.id_lugarentrega) ON facturas_detalle.id_op=ops.id_op WHERE facturas_detalle.factura_serie='" + clavemodificaf + "';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    String pacorreo = para.getText();
                    para.setText(rs0.getString("email_cfd"));
                    if (para.getText().equals("") || para.getText().equals("null")) {
                        para.setText(pacorreo);
                    }
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            rs0 = null;
            try {
                String senSQL = "SELECT notas_credito.nota_credito_serie,notas_credito.fecha,notas_credito.varios,clientes.* FROM notas_credito LEFT JOIN clientes ON notas_credito.id_clientes=clientes.id_clientes WHERE nota_credito_serie='" + clavemodificaf + "';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    para.setText(rs0.getString("email_cfd"));
                    nombrecliente = rs0.getString("nombre");
                    fechacreado = rs0.getTimestamp("fecha");
                    varios = rs0.getString("varios");
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
            //busca correo el lugares de entrega
            rs0 = null;
            try {
                String senSQL = "SELECT lugares_entregas.email_cfd FROM notas_credito_detalle LEFT JOIN (facturas_detalle LEFT JOIN (ops LEFT JOIN lugares_entregas ON ops.id_lugarentrega=lugares_entregas.id_lugarentrega) ON facturas_detalle.id_op=ops.id_op) ON notas_credito_detalle.factura_serie=facturas_detalle.factura_serie WHERE notas_credito_detalle.factura_serie='" + clavemodificaf + "';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    String pacorreo = para.getText();
                    para.setText(rs0.getString("email_cfd"));
                    if (para.getText().equals("") || para.getText().equals("null")) {
                        para.setText(pacorreo);
                    }
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public String creaXML(String ruta) {
        String valor_uuid = "";
        FileWriter fWriter;
        BufferedWriter bWriter;
        rs0 = null;
        try {
            String senSQL = "SELECT xmlfinal.* FROM xmlfinal WHERE factura_serie='" + clavemodificaf + "';";
            rs0 = conexion.consulta(senSQL, connj);
            if (rs0.next()) {
                valor_uuid = "" + rs0.getString("uuid");
                valor_uuid = valor_uuid.trim();
                try {
                    fWriter = new FileWriter(new File(ruta));
                    bWriter = new BufferedWriter(fWriter);
                    bWriter.write(new String(rs0.getBytes("xmlfinal")));
                    bWriter.close();
                    fWriter.close();
                } catch (IOException es) {
                    es.printStackTrace();
                }
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
        return valor_uuid;
    }

    public String getVersion() {
        String version = "";
        rs0 = null;
        try {
            String senSQL = "SELECT xmlfinal.* FROM xmlfinal WHERE factura_serie='" + clavemodificaf + "';";
            rs0 = conexion.consulta(senSQL, connj);
            if (rs0.next()) {
                version = "" + rs0.getString("version");
                version = version.trim();
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
        return version;
    }

    public void creaPDF(String ruta, String rutaxml, String imprim) {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        JasperPrint jasperPrint = null;
        try {
            File nuevoxml = new File(rutaxml); //file xml
            //CfdiF33 cfdi =  new CfdiF33(new FileInputStream(nuevoxml));
            //CFDv22 cfd = new CFDv22(new FileInputStream(nuevoxml)); //ller xml para obetener xml
            CFDv32 cfd = new CFDv32(new FileInputStream(nuevoxml)); //ller xml para obetener xml

            org.w3c.dom.Document dom = null;
            javax.xml.parsers.DocumentBuilderFactory dbf;
            javax.xml.parsers.DocumentBuilder db;
            dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            try {
                db = dbf.newDocumentBuilder();
                dom = db.parse(nuevoxml.toURI().toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error parse XML" + ex);
            }
            Map pars = new HashMap();
            org.w3c.dom.Element rootElement = dom.getDocumentElement();
            org.w3c.dom.NodeList Emisor = rootElement.getElementsByTagName("Emisor");
            org.w3c.dom.Element nodeEmisor = (org.w3c.dom.Element) Emisor.item(0);
            org.w3c.dom.NodeList EmisorDom = nodeEmisor.getElementsByTagName("DomicilioFiscal");
            org.w3c.dom.Element nodeEmisorDom = (org.w3c.dom.Element) EmisorDom.item(0);
            org.w3c.dom.NodeList EmisorExp = nodeEmisor.getElementsByTagName("ExpedidoEn");
            org.w3c.dom.Element nodeEmisorExp = (org.w3c.dom.Element) EmisorExp.item(0);
            org.w3c.dom.NodeList EmisorReg = nodeEmisor.getElementsByTagName("RegimenFiscal");
            int ii = 0;
            for (ii = 0; ii < EmisorReg.getLength(); ii++) {
                org.w3c.dom.Element nodeEmisorReg = (org.w3c.dom.Element) EmisorReg.item(0);
                if (nodeEmisorReg.hasAttribute("Regimen")) {
                    pars.put("regimenfiscal", "REGIMEN FISCAL: " + nodeEmisorReg.getAttribute("Regimen"));
                } else {
                    pars.put("regimenfiscal", "");
                }
            }
            org.w3c.dom.NodeList Receptor = rootElement.getElementsByTagName("Receptor");
            org.w3c.dom.Element nodeReceptor = (org.w3c.dom.Element) Receptor.item(0);
            org.w3c.dom.NodeList ReceptorDom = nodeReceptor.getElementsByTagName("Domicilio");
            org.w3c.dom.Element nodeReceptorDom = (org.w3c.dom.Element) ReceptorDom.item(0);
//lineas de codi
            //Map pars = new HashMap();
            File fichero = new File(conexion.Directorio() + "/logoempresa.png");
            File fichero2 = new File(conexion.Directorio() + "/rfcempresa.png");
            pars.put("folio", null);
            pars.put("logoempresa", new FileInputStream(fichero));
            pars.put("rfcempresa", new FileInputStream(fichero2));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("senSQL", "");//SQL dinamico
            pars.put("version", resourceMap.getString("Application.title"));
            pars.put("folio_serie", clavemodificaf);//SQL dinamico
            pars.put("cadena", cfd.getCadenaOriginal());//SQL dinamico
            //pars.put("cadena", cfd.getCadenaOriginal());//SQL dinamico

            //ingresa todos los datos XML
            pars.put("folio", rootElement.getAttribute("folio"));
            if (rootElement.hasAttribute("serie")) {
                pars.put("serie", "" + rootElement.getAttribute("serie"));
            } else {
                pars.put("serie", "");
            }

            //AGREGAMOS MOVIMIENTOS CFD 2012
            if (rootElement.hasAttribute("metodoDePago")) {
                pars.put("metodopago", "Método de Pago: " + rootElement.getAttribute("metodoDePago"));
            } else {
                pars.put("metodopago", "");
            }
            if (rootElement.hasAttribute("NumCtaPago")) {
                pars.put("ctapago", "" + rootElement.getAttribute("NumCtaPago"));
            } else {
                pars.put("ctapago", "");
            }
            //termina ajustes para CFD 2012

            pars.put("certificado", rootElement.getAttribute("noCertificado"));
            pars.put("noapro", rootElement.getAttribute("noAprobacion"));
            pars.put("anoapro", rootElement.getAttribute("anoAprobacion"));
            pars.put("sello", rootElement.getAttribute("sello"));
            pars.put("formadepago", rootElement.getAttribute("formaDePago"));

            String tipocfd = rootElement.getAttribute("tipoDeComprobante");
            if (tipocfd.equals("ingreso")) {
                pars.put("tipocfd", "Factura");
            } else {
                pars.put("tipocfd", "Nota de Crédito");
            }

            //regenera fecha
            String fecha1 = "" + rootElement.getAttribute("fecha");
            String fechap1 = "";
            String fechap2 = "";
            String[] arrayfecha = fecha1.split("T");
            // En este momento tenemos un array en el que cada elemento del cc.
            for (int i = 0; i < arrayfecha.length; i++) {
                if (i == 0) {
                    fechap1 = "" + arrayfecha[0];
                }
                if (i == 1) {
                    fechap2 = "" + arrayfecha[1];
                }
            }
            pars.put("fecha", fechamascorta.format(fechaparser.parse(fechap1)) + " " + fechap2);
            pars.put("fechacreado", fechamostrartodo.format(fechaparser.parse(fechap1)));
            pars.put("fechavencido", fechamostrartodo.format(fechavencimiento));
            pars.put("totalfinal", "" + moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("total"))));

            //Datos emisor con domicilio fiscal
            pars.put("nombre", nodeEmisor.getAttribute("nombre"));
            pars.put("rfc", nodeEmisor.getAttribute("rfc"));
            pars.put("calle", nodeEmisorDom.getAttribute("calle"));
            if (nodeEmisorDom.hasAttribute("noExterior")) {
                pars.put("numeroext", "" + nodeEmisorDom.getAttribute("noExterior"));
            } else {
                pars.put("numeroext", "");
            }
            if (nodeEmisorDom.hasAttribute("noInterior")) {
                pars.put("numeroint", "" + nodeEmisorDom.getAttribute("noInterior"));
            } else {
                pars.put("numeroint", "");
            }
            if (nodeEmisorDom.hasAttribute("colonia")) {
                pars.put("colonia", "" + nodeEmisorDom.getAttribute("colonia"));
            } else {
                pars.put("colonia", "");
            }
            if (nodeEmisorDom.hasAttribute("localidad")) {
                pars.put("localidad", "" + nodeEmisorDom.getAttribute("localidad"));
            } else {
                pars.put("localidad", "");
            }
            pars.put("municipio", nodeEmisorDom.getAttribute("municipio"));
            pars.put("estado", nodeEmisorDom.getAttribute("estado"));
            pars.put("pais", nodeEmisorDom.getAttribute("pais"));
            pars.put("cp", nodeEmisorDom.getAttribute("codigoPostal"));
            pars.put("telefono", telefono);

            if (nodeEmisorDom.hasAttribute("referencia")) {
                pars.put("referencia", "" + nodeEmisorDom.getAttribute("referencia"));
            } else {
                pars.put("referencia", "");
            }

            //Expedido En
            if (nodeEmisorExp.hasAttribute("municipio")) {
                pars.put("emunicipio", "" + nodeEmisorExp.getAttribute("municipio"));
            } else {
                pars.put("emunicipio", "");
            }

            if (nodeEmisorExp.hasAttribute("estado")) {
                pars.put("eestado", "" + nodeEmisorExp.getAttribute("estado"));
            } else {
                pars.put("eestado", "");
            }
            if (nodeEmisorExp.hasAttribute("pais")) {
                pars.put("epais", "" + nodeEmisorExp.getAttribute("pais"));
            } else {
                pars.put("epais", "");
            }
            //Datos receptor
            pars.put("rnombre", nodeReceptor.getAttribute("nombre"));
            pars.put("rrfc", nodeReceptor.getAttribute("rfc"));
            pars.put("rcalle", nodeReceptorDom.getAttribute("calle"));
            if (nodeReceptorDom.hasAttribute("noExterior")) {
                pars.put("rnumeroext", "" + nodeReceptorDom.getAttribute("noExterior"));
            } else {
                pars.put("rnumeroext", "");
            }
            if (nodeReceptorDom.hasAttribute("noInterior")) {
                pars.put("rnumeroint", "" + nodeReceptorDom.getAttribute("noInterior"));
            } else {
                pars.put("rnumeroint", "");
            }
            if (nodeReceptorDom.hasAttribute("colonia")) {
                pars.put("rcolonia", "" + nodeReceptorDom.getAttribute("colonia"));
            } else {
                pars.put("rcolonia", "");
            }
            if (nodeReceptorDom.hasAttribute("referencia")) {
                pars.put("rreferencia", "" + nodeReceptorDom.getAttribute("referencia"));
            } else {
                pars.put("rreferencia", "");
            }
            if (nodeReceptorDom.hasAttribute("localidad")) {
                pars.put("rlocalidad", "" + nodeReceptorDom.getAttribute("localidad"));
            } else {
                pars.put("rlocalidad", "");
            }
            pars.put("rmunicipio", nodeReceptorDom.getAttribute("municipio"));
            pars.put("restado", nodeReceptorDom.getAttribute("estado"));
            pars.put("rpais", nodeReceptorDom.getAttribute("pais"));
            pars.put("rcp", nodeReceptorDom.getAttribute("codigoPostal"));

            //DATOS DINEROS
            String tasas = "Sub-Total:\n";
            String tasasimp = moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("subTotal"))) + "\n";
            if (rootElement.hasAttribute("descuento")) {
                Double des = Double.parseDouble("" + rootElement.getAttribute("descuento"));
                if (des > 0.0) {
                    tasas += "Descuento:\n";
                    tasasimp += moneda2decimales.format(des) + "\n";
                }
            }
            
            

            org.w3c.dom.NodeList Impuestos = rootElement.getElementsByTagName("Impuestos");
            org.w3c.dom.Element nodeImpuestos = (org.w3c.dom.Element) Impuestos.item(0);
            int i = 0;
            int j = 0;
            for (j = (nodeImpuestos.getChildNodes().getLength() - 1); j >= 0; j--) { //lo recorremos alreves para mostrar datos correctamente
                String nodo = "" + nodeImpuestos.getChildNodes().item(j).getNodeName();
                if (nodo.equals("Retenciones")) {
                    org.w3c.dom.NodeList ImpuestosRetenciones = nodeImpuestos.getElementsByTagName("Retenciones");
                    org.w3c.dom.Element nodeImpuestosRetenciones = (org.w3c.dom.Element) ImpuestosRetenciones.item(0);
                    if (nodeImpuestosRetenciones.hasChildNodes()) {
                        org.w3c.dom.NodeList ImpuestosRetencion = nodeImpuestosRetenciones.getElementsByTagName("Retencion");
                        for (i = 0; i < ImpuestosRetencion.getLength(); i++) {
                            org.w3c.dom.Element nodeImpuestosRetencion = (org.w3c.dom.Element) ImpuestosRetencion.item(i);
                            tasas += "Ret. " + nodeImpuestosRetencion.getAttribute("impuesto") + ":\n";
                            tasasimp += moneda2decimales.format(Double.parseDouble("" + nodeImpuestosRetencion.getAttribute("importe"))) + "\n";
                        }
                    }
                }
                if (nodo.equals("Traslados")) {
                    org.w3c.dom.NodeList ImpuestosTraslados = nodeImpuestos.getElementsByTagName("Traslados");
                    org.w3c.dom.Element nodeImpuestosTraslados = (org.w3c.dom.Element) ImpuestosTraslados.item(0);
                    if (nodeImpuestosTraslados.hasChildNodes()) {
                        org.w3c.dom.NodeList ImpuestosTraslado = nodeImpuestosTraslados.getElementsByTagName("Traslado");
                        for (i = 0; i < ImpuestosTraslado.getLength(); i++) {
                            org.w3c.dom.Element nodeImpuestosTraslado = (org.w3c.dom.Element) ImpuestosTraslado.item(i);

                            tasas += nodeImpuestosTraslado.getAttribute("impuesto") + " " + nodeImpuestosTraslado.getAttribute("tasa") + "%:\n";
                            tasasimp += moneda2decimales.format(Double.parseDouble("" + nodeImpuestosTraslado.getAttribute("importe"))) + "\n";
                        }
                    }
                }

            }

            tasas += "Total:";
            tasasimp += moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("total")));

            //agrega los dos datos
            pars.put("tasas", tasas);
            pars.put("tasasimp", tasasimp);

            //si la factura esta cancelada
            pars.put("cancelafac", estatusfacx);

            JasperReport masterReport = null;
            try {
                if (tipocfd.equals("ingreso")) {
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_factura.jasper"));
                } else {
                    if (varios.equals("1")) {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_nc.jasper"));
                    } else {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_nc_partidas.jasper"));
                    }
                }
            } catch (JRException e) {
                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            jasperPrint = JasperFillManager.fillReport(masterReport, pars, connj);

            if (imprim.equals("Si")) {
                if (variasfac.equals("")) {
                    JasperPrintManager.printReport(jasperPrint, true);
                } else {
                    JasperPrintManager.printReport(jasperPrint, false);
                }
            } else {
                JasperExportManager.exportReportToPdfFile(jasperPrint, ruta);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
        }
    }

    public void creaPDF32(String ruta, String rutaxml, String imprim, String uuid) {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        JasperPrint jasperPrint = null;
        try {
            File nuevoxml = new File(rutaxml); //file xml
            //CFDv22 cfd = new CFDv22(new FileInputStream(nuevoxml)); //ller xml para obetener xml
            CFDv32 cfd = new CFDv32(new FileInputStream(nuevoxml)); //ller xml para obetener xml

            org.w3c.dom.Document dom = null;
            javax.xml.parsers.DocumentBuilderFactory dbf;
            javax.xml.parsers.DocumentBuilder db;
            dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            try {
                db = dbf.newDocumentBuilder();
                dom = db.parse(nuevoxml.toURI().toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error parse XML CFDI" + ex);
            }
            Map pars = new HashMap();
            org.w3c.dom.Element rootElement = dom.getDocumentElement();
            org.w3c.dom.NodeList Emisor = rootElement.getElementsByTagName("cfdi:Emisor");
            org.w3c.dom.Element nodeEmisor = (org.w3c.dom.Element) Emisor.item(0);
            org.w3c.dom.NodeList EmisorDom = nodeEmisor.getElementsByTagName("cfdi:DomicilioFiscal");
            org.w3c.dom.Element nodeEmisorDom = (org.w3c.dom.Element) EmisorDom.item(0);
            org.w3c.dom.NodeList EmisorExp = nodeEmisor.getElementsByTagName("cfdi:ExpedidoEn");
            org.w3c.dom.Element nodeEmisorExp = (org.w3c.dom.Element) EmisorExp.item(0);
            org.w3c.dom.NodeList EmisorReg = nodeEmisor.getElementsByTagName("cfdi:RegimenFiscal");
            int ii = 0;
            for (ii = 0; ii < EmisorReg.getLength(); ii++) {
                org.w3c.dom.Element nodeEmisorReg = (org.w3c.dom.Element) EmisorReg.item(0);
                if (nodeEmisorReg.hasAttribute("Regimen")) {
                    pars.put("regimenfiscal", "REGIMEN FISCAL: " + nodeEmisorReg.getAttribute("Regimen"));
                } else {
                    pars.put("regimenfiscal", "");
                }
            }
            org.w3c.dom.NodeList Receptor = rootElement.getElementsByTagName("cfdi:Receptor");
            org.w3c.dom.Element nodeReceptor = (org.w3c.dom.Element) Receptor.item(0);
            org.w3c.dom.NodeList ReceptorDom = nodeReceptor.getElementsByTagName("cfdi:Domicilio");
            org.w3c.dom.Element nodeReceptorDom = (org.w3c.dom.Element) ReceptorDom.item(0);

            //nuevas lienas agregadas
            org.w3c.dom.NodeList Complemento = rootElement.getElementsByTagName("cfdi:Complemento");
            org.w3c.dom.Element nodeComplemento = (org.w3c.dom.Element) Complemento.item(0);
            org.w3c.dom.NodeList Timbrefiscal = nodeComplemento.getElementsByTagName("tfd:TimbreFiscalDigital");
            org.w3c.dom.Element nodeTimbrefiscal = (org.w3c.dom.Element) Timbrefiscal.item(0);

            pars.put("uuid", nodeTimbrefiscal.getAttribute("UUID"));
            pars.put("fechatimbrado", nodeTimbrefiscal.getAttribute("FechaTimbrado"));
            pars.put("sellocfd", nodeTimbrefiscal.getAttribute("selloCFD"));
            pars.put("nocersat", nodeTimbrefiscal.getAttribute("noCertificadoSAT"));
            pars.put("sellosat", nodeTimbrefiscal.getAttribute("selloSAT"));
            pars.put("versioncfdi", nodeTimbrefiscal.getAttribute("version"));

            Double total_cfdi = Double.parseDouble("" + rootElement.getAttribute("total"));
            String total_cfdi_c = num_qr_decimales.format(total_cfdi);

            //generamos el codigo de barras
            String cadena_qr = "?re=" + nodeEmisor.getAttribute("rfc") + "&rr=" + nodeReceptor.getAttribute("rfc") + "&tt=" + total_cfdi_c + "&id=" + nodeTimbrefiscal.getAttribute("UUID") + "";
            //System.err.println(cadena_qr);

            File ruta_qr = new File(System.getProperty("user.home") + "/" + "QRC.PNG");
            ByteArrayOutputStream out = QRCode.from(cadena_qr).to(ImageType.PNG).withSize(300, 300).stream();
            try {
                FileOutputStream fout = new FileOutputStream(ruta_qr);
                fout.write(out.toByteArray());
                fout.flush();
                fout.close();
            } catch (FileNotFoundException e) {
                // Do Logging
            } catch (IOException e) {
                // Do Logging
            }
            //le mando el archvio al pdf
            pars.put("qruuid", new FileInputStream(ruta_qr));
            //luego lo borro
            if (ruta_qr.exists()) {
                if (ruta_qr.delete()) {
                    System.err.println("archivo qr borrado");
                }
            }

            System.err.println("Prueba de impresion despues de generar qqr");

            //lineas de codi
            //Map pars = new HashMap();
            File fichero = new File(conexion.Directorio() + "/logoempresa.png");
            File fichero2 = new File(conexion.Directorio() + "/rfcempresa.png");
            pars.put("folio", null);
            pars.put("logoempresa", new FileInputStream(fichero));
            pars.put("rfcempresa", new FileInputStream(fichero2));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("senSQL", "");//SQL dinamico
            pars.put("version", resourceMap.getString("Application.title"));
            pars.put("folio_serie", clavemodificaf);//SQL dinamico
            pars.put("cadena", cfd.getCadenaOriginal());//SQL dinamico

            //ingresa todos los datos XML
            pars.put("folio", rootElement.getAttribute("folio"));
            if (rootElement.hasAttribute("serie")) {
                pars.put("serie", "" + rootElement.getAttribute("serie"));
            } else {
                pars.put("serie", "");
            }

            //AGREGAMOS MOVIMIENTOS CFD 2012
            if (rootElement.hasAttribute("metodoDePago")) {
                pars.put("metodopago", "Método de Pago: " + rootElement.getAttribute("metodoDePago"));
            } else {
                pars.put("metodopago", "");
            }
            if (rootElement.hasAttribute("NumCtaPago")) {
                pars.put("ctapago", "" + rootElement.getAttribute("NumCtaPago"));
            } else {
                pars.put("ctapago", "");
            }
            //termina ajustes para CFD 2012

            pars.put("certificado", rootElement.getAttribute("noCertificado"));
            //pars.put("noapro", rootElement.getAttribute("noAprobacion"));
            //pars.put("anoapro", rootElement.getAttribute("anoAprobacion"));
            pars.put("sello", rootElement.getAttribute("sello"));
            pars.put("formadepago", rootElement.getAttribute("formaDePago"));

            String tipocfd = rootElement.getAttribute("tipoDeComprobante");
            if (tipocfd.equals("ingreso")) {
                pars.put("tipocfd", "Factura");
            } else {
                pars.put("tipocfd", "Nota de Crédito");
            }

            //regenera fecha
            String fecha1 = "" + rootElement.getAttribute("fecha");
            String fechap1 = "";
            String fechap2 = "";
            String[] arrayfecha = fecha1.split("T");
            // En este momento tenemos un array en el que cada elemento del cc.
            for (int i = 0; i < arrayfecha.length; i++) {
                if (i == 0) {
                    fechap1 = "" + arrayfecha[0];
                }
                if (i == 1) {
                    fechap2 = "" + arrayfecha[1];
                }
            }
            pars.put("fecha", fechamascorta.format(fechaparser.parse(fechap1)) + " " + fechap2);
            pars.put("fechacreado", fechamostrartodo.format(fechaparser.parse(fechap1)));
            pars.put("fechavencido", fechamostrartodo.format(fechavencimiento));
            pars.put("totalfinal", "" + moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("total"))));

            //Datos emisor con domicilio fiscal
            pars.put("nombre", nodeEmisor.getAttribute("nombre"));
            pars.put("rfc", nodeEmisor.getAttribute("rfc"));
            pars.put("calle", nodeEmisorDom.getAttribute("calle"));
            if (nodeEmisorDom.hasAttribute("noExterior")) {
                pars.put("numeroext", "" + nodeEmisorDom.getAttribute("noExterior"));
            } else {
                pars.put("numeroext", "");
            }
            if (nodeEmisorDom.hasAttribute("noInterior")) {
                pars.put("numeroint", "" + nodeEmisorDom.getAttribute("noInterior"));
            } else {
                pars.put("numeroint", "");
            }
            if (nodeEmisorDom.hasAttribute("colonia")) {
                pars.put("colonia", "" + nodeEmisorDom.getAttribute("colonia"));
            } else {
                pars.put("colonia", "");
            }
            if (nodeEmisorDom.hasAttribute("localidad")) {
                pars.put("localidad", "" + nodeEmisorDom.getAttribute("localidad"));
            } else {
                pars.put("localidad", "");
            }
            pars.put("municipio", nodeEmisorDom.getAttribute("municipio"));
            pars.put("estado", nodeEmisorDom.getAttribute("estado"));
            pars.put("pais", nodeEmisorDom.getAttribute("pais"));
            pars.put("cp", nodeEmisorDom.getAttribute("codigoPostal"));
            pars.put("telefono", telefono);

            if (nodeEmisorDom.hasAttribute("referencia")) {
                pars.put("referencia", "" + nodeEmisorDom.getAttribute("referencia"));
            } else {
                pars.put("referencia", "");
            }

            //Expedido En
            if (nodeEmisorExp.hasAttribute("municipio")) {
                pars.put("emunicipio", "" + nodeEmisorExp.getAttribute("municipio"));
            } else {
                pars.put("emunicipio", "");
            }

            if (nodeEmisorExp.hasAttribute("estado")) {
                pars.put("eestado", "" + nodeEmisorExp.getAttribute("estado"));
            } else {
                pars.put("eestado", "");
            }
            if (nodeEmisorExp.hasAttribute("pais")) {
                pars.put("epais", "" + nodeEmisorExp.getAttribute("pais"));
            } else {
                pars.put("epais", "");
            }
            //Datos receptor
            pars.put("rnombre", nodeReceptor.getAttribute("nombre"));
            pars.put("rrfc", nodeReceptor.getAttribute("rfc"));
            pars.put("rcalle", nodeReceptorDom.getAttribute("calle"));
            if (nodeReceptorDom.hasAttribute("noExterior")) {
                pars.put("rnumeroext", "" + nodeReceptorDom.getAttribute("noExterior"));
            } else {
                pars.put("rnumeroext", "");
            }
            if (nodeReceptorDom.hasAttribute("noInterior")) {
                pars.put("rnumeroint", "" + nodeReceptorDom.getAttribute("noInterior"));
            } else {
                pars.put("rnumeroint", "");
            }
            if (nodeReceptorDom.hasAttribute("colonia")) {
                pars.put("rcolonia", "" + nodeReceptorDom.getAttribute("colonia"));
            } else {
                pars.put("rcolonia", "");
            }
            if (nodeReceptorDom.hasAttribute("referencia")) {
                pars.put("rreferencia", "" + nodeReceptorDom.getAttribute("referencia"));
            } else {
                pars.put("rreferencia", "");
            }
            if (nodeReceptorDom.hasAttribute("localidad")) {
                pars.put("rlocalidad", "" + nodeReceptorDom.getAttribute("localidad"));
            } else {
                pars.put("rlocalidad", "");
            }
            pars.put("rmunicipio", nodeReceptorDom.getAttribute("municipio"));
            pars.put("restado", nodeReceptorDom.getAttribute("estado"));
            pars.put("rpais", nodeReceptorDom.getAttribute("pais"));
            pars.put("rcp", nodeReceptorDom.getAttribute("codigoPostal"));

            //DATOS DINEROS
            String tasas = "Sub-Total:\n";
            String tasasimp = moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("subTotal"))) + "\n";
            if (rootElement.hasAttribute("descuento")) {
                Double des = Double.parseDouble("" + rootElement.getAttribute("descuento"));
                if (des > 0.0) {
                    tasas += "Descuento:\n";
                    tasasimp += moneda2decimales.format(des) + "\n";
                }
            }

            org.w3c.dom.NodeList Impuestos = rootElement.getElementsByTagName("cfdi:Impuestos");
            org.w3c.dom.Element nodeImpuestos = (org.w3c.dom.Element) Impuestos.item(0);
            int i = 0;
            int j = 0;
            for (j = (nodeImpuestos.getChildNodes().getLength() - 1); j >= 0; j--) { //lo recorremos alreves para mostrar datos correctamente
                String nodo = "" + nodeImpuestos.getChildNodes().item(j).getNodeName();
                if (nodo.equals("cfdi:Retenciones")) {
                    org.w3c.dom.NodeList ImpuestosRetenciones = nodeImpuestos.getElementsByTagName("cfdi:Retenciones");
                    org.w3c.dom.Element nodeImpuestosRetenciones = (org.w3c.dom.Element) ImpuestosRetenciones.item(0);
                    if (nodeImpuestosRetenciones.hasChildNodes()) {
                        org.w3c.dom.NodeList ImpuestosRetencion = nodeImpuestosRetenciones.getElementsByTagName("cfdi:Retencion");
                        for (i = 0; i < ImpuestosRetencion.getLength(); i++) {
                            org.w3c.dom.Element nodeImpuestosRetencion = (org.w3c.dom.Element) ImpuestosRetencion.item(i);
                            tasas += "Ret. " + nodeImpuestosRetencion.getAttribute("impuesto") + ":\n";
                            tasasimp += moneda2decimales.format(Double.parseDouble("" + nodeImpuestosRetencion.getAttribute("importe"))) + "\n";
                        }
                    }
                }
                if (nodo.equals("cfdi:Traslados")) {
                    org.w3c.dom.NodeList ImpuestosTraslados = nodeImpuestos.getElementsByTagName("cfdi:Traslados");
                    org.w3c.dom.Element nodeImpuestosTraslados = (org.w3c.dom.Element) ImpuestosTraslados.item(0);
                    if (nodeImpuestosTraslados.hasChildNodes()) {
                        org.w3c.dom.NodeList ImpuestosTraslado = nodeImpuestosTraslados.getElementsByTagName("cfdi:Traslado");
                        for (i = 0; i < ImpuestosTraslado.getLength(); i++) {
                            org.w3c.dom.Element nodeImpuestosTraslado = (org.w3c.dom.Element) ImpuestosTraslado.item(i);

                            tasas += nodeImpuestosTraslado.getAttribute("impuesto") + " " + nodeImpuestosTraslado.getAttribute("tasa") + "%:\n";
                            tasasimp += moneda2decimales.format(Double.parseDouble("" + nodeImpuestosTraslado.getAttribute("importe"))) + "\n";
                        }
                    }
                }

            }

            tasas += "Total:";
            tasasimp += moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("total")));

            //agrega los dos datos
            pars.put("tasas", tasas);
            pars.put("tasasimp", tasasimp);

            //si la factura esta cancelada
            pars.put("cancelafac", estatusfacx);

            JasperReport masterReport = null;
            try {
                if (tipocfd.equals("ingreso")) {
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_factura32.jasper"));
                } else {
                    if (varios.equals("1")) {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_nc32.jasper"));
                    } else {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_nc_partidas32.jasper"));
                    }
                }
            } catch (JRException e) {
                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            jasperPrint = JasperFillManager.fillReport(masterReport, pars, connj);

            if (imprim.equals("Si")) {
                if (variasfac.equals("")) {
                    JasperPrintManager.printReport(jasperPrint, true);
                } else {
                    JasperPrintManager.printReport(jasperPrint, false);
                }
            } else {
                JasperExportManager.exportReportToPdfFile(jasperPrint, ruta);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
        }
    }

    public void creaPDF33(String ruta, String rutaxml, String imprim, String uuid) {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
        JasperPrint jasperPrint = null;
        try {
            File nuevoxml = new File(rutaxml); //file xml
            CFDv32 cfd = new CFDv32(new FileInputStream(nuevoxml)); //ller xml para obetener xml

            org.w3c.dom.Document dom = null;
            javax.xml.parsers.DocumentBuilderFactory dbf;
            javax.xml.parsers.DocumentBuilder db;
            dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            try {
                db = dbf.newDocumentBuilder();
                dom = db.parse(nuevoxml.toURI().toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error parse XML CFDI" + ex);
            }
            Map pars = new HashMap();
            org.w3c.dom.Element rootElement = dom.getDocumentElement();
            org.w3c.dom.NodeList Emisor = rootElement.getElementsByTagName("cfdi:Emisor");
            org.w3c.dom.Element nodeEmisor = (org.w3c.dom.Element) Emisor.item(0);
            pars.put("regimenfiscal", "");
            org.w3c.dom.NodeList Receptor = rootElement.getElementsByTagName("cfdi:Receptor");
            org.w3c.dom.Element nodeReceptor = (org.w3c.dom.Element) Receptor.item(0);

            //nuevas lienas agregadas
            org.w3c.dom.NodeList Complemento = rootElement.getElementsByTagName("cfdi:Complemento");
            org.w3c.dom.Element nodeComplemento = (org.w3c.dom.Element) Complemento.item(0);
            
            org.w3c.dom.NodeList Timbrefiscal = nodeComplemento.getElementsByTagName("tfd:TimbreFiscalDigital");
            org.w3c.dom.Element nodeTimbrefiscal = (org.w3c.dom.Element) Timbrefiscal.item(0);
            
            
                        

            pars.put("uuid", nodeTimbrefiscal.getAttribute("UUID"));
            pars.put("fechatimbrado", nodeTimbrefiscal.getAttribute("FechaTimbrado"));
            pars.put("sellocfd", nodeTimbrefiscal.getAttribute("SelloCFD"));
            pars.put("nocersat", nodeTimbrefiscal.getAttribute("NoCertificadoSAT"));
            pars.put("sellosat", nodeTimbrefiscal.getAttribute("SelloSAT"));
            pars.put("versioncfdi", nodeTimbrefiscal.getAttribute("Version"));
            
            
            
            

            //Documento
            String documento = rootElement.getAttribute("Folio") + rootElement.getAttribute("Serie");

            Double total_cfdi = Double.parseDouble("" + rootElement.getAttribute("Total"));
            String total_cfdi_c = num_qr_decimales.format(total_cfdi);

            //generamos el codigo de barras
            String cadena_qr = "?re=" + nodeEmisor.getAttribute("Rfc") + "&rr=" + nodeReceptor.getAttribute("Rfc") + "&tt=" + total_cfdi_c + "&id=" + nodeTimbrefiscal.getAttribute("UUID") + "";
            //System.err.println(cadena_qr);

            File ruta_qr = new File(System.getProperty("user.home") + "/" + "QRC.PNG");
            ByteArrayOutputStream out = QRCode.from(cadena_qr).to(ImageType.PNG).withSize(300, 300).stream();
            try {
                FileOutputStream fout = new FileOutputStream(ruta_qr);
                fout.write(out.toByteArray());
                fout.flush();
                fout.close();
            } catch (FileNotFoundException e) {
                // Do Logging
            } catch (IOException e) {
                // Do Logging
            }
            //le mando el archvio al pdf
            pars.put("qruuid", new FileInputStream(ruta_qr));
            //luego lo borro
            if (ruta_qr.exists()) {
                if (ruta_qr.delete()) {
                    System.err.println("archivo qr borrado");
                }
            }

            System.err.println("Prueba de impresion despues de generar qqr");

            //lineas de codi
            //Map pars = new HashMap();
            File fichero = new File(conexion.Directorio() + "/logoempresa.png");
            File fichero2 = new File(conexion.Directorio() + "/rfcempresa.png");
            pars.put("folio", null);
            pars.put("logoempresa", new FileInputStream(fichero));
            pars.put("rfcempresa", new FileInputStream(fichero2));
            pars.put("subtitulo", null);//datos
            pars.put("fechaini", null);//fechaini.getDate()
            pars.put("fechafin", null);//fechafin.getDate()
            pars.put("senSQL", "");//SQL dinamico
            pars.put("version", resourceMap.getString("Application.title"));
            pars.put("folio_serie", clavemodificaf);//SQL dinamico
            pars.put("cadena", cfd.getCadenaOriginal());//SQL dinamico

            //ingresa todos los datos XML
            pars.put("folio", rootElement.getAttribute("Folio"));
            pars.put("moneda", rootElement.getAttribute("Moneda"));
            if (rootElement.hasAttribute("Serie")) {
                pars.put("serie", "" + rootElement.getAttribute("Serie"));
            } else {
                pars.put("serie", "");
            }

            //ATRIBUTOS, FORMAS, METODO Y USOS CFDI 3.3
            try {
                String senSQL = "SELECT factura_serie, clave_usocomprobante, usocomprobante, clave_formapago, formapago, clave_metodopago, metodopago, cuentapago FROM facturas WHERE factura_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    pars.put("f_Metodopago", rs0.getString("metodopago"));
                    pars.put("f_Clavemetodopago", rs0.getString("clave_metodopago"));
                    pars.put("f_Formapago", rs0.getString("formapago"));
                    pars.put("f_Claveformapago", rs0.getString("clave_formapago"));
                    pars.put("f_Usocomprobante", rs0.getString("usocomprobante"));
                    pars.put("f_Claveusocomprobante", rs0.getString("clave_usocomprobante"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR DATOS DE ATRIBUTOS CFDI 3.3" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
            //NOTA DE CREDITO
            try {
                String senSQL = "SELECT nota_credito_serie, clave_usocomprobante, usocomprobante, clave_formapago, formapago, clave_metodopago, metodopago, cuentapago FROM notas_credito WHERE nota_credito_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    pars.put("nc_Metodopago", rs0.getString("metodopago"));
                    pars.put("nc_Clavemetodopago", rs0.getString("clave_metodopago"));
                    pars.put("nc_Formapago", rs0.getString("formapago"));
                    pars.put("nc_Claveformapago", rs0.getString("clave_formapago"));
                    pars.put("nc_Usocomprobante", rs0.getString("usocomprobante"));
                    pars.put("nc_Claveusocomprobante", rs0.getString("clave_usocomprobante"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR DATOS DE ATRIBUTOS CFDI 3.3" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
            
            
            
            //termina ajustes para CFD 2012

            pars.put("certificado", rootElement.getAttribute("NoCertificado"));
            pars.put("sello", rootElement.getAttribute("Sello"));

            String tipocfd = rootElement.getAttribute("TipoDeComprobante");
            if (tipocfd.equals("I")) {
                pars.put("tipocfd", "I - Ingreso");
            } else {
                pars.put("tipocfd", "E - Egreso");
            }

            //regenera fecha
            String fecha1 = "" + rootElement.getAttribute("Fecha");
            String fechap1 = "";
            String fechap2 = "";
            String[] arrayfecha = fecha1.split("T");
            // En este momento tenemos un array en el que cada elemento del cc.
            for (int i = 0; i < arrayfecha.length; i++) {
                if (i == 0) {
                    fechap1 = "" + arrayfecha[0];
                }
                if (i == 1) {
                    fechap2 = "" + arrayfecha[1];
                }
            }
            pars.put("fecha", fechamascorta.format(fechaparser.parse(fechap1)) + " " + fechap2);
            pars.put("fechacreado", fechamostrartodo.format(fechaparser.parse(fechap1)));
            pars.put("fechavencido", fechamostrartodo.format(fechavencimiento));
            pars.put("totalfinal", "" + moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("Total"))));

            //DATOS EMISOR
            pars.put("nombre", nodeEmisor.getAttribute("Nombre"));
            pars.put("rfc", nodeEmisor.getAttribute("Rfc"));
            pars.put("calle", nodeEmisor.getAttribute("Calle"));
            pars.put("numeroext", "");
            pars.put("numeroint", "");
            pars.put("colonia", "");
            pars.put("localidad", "");
            pars.put("municipio", "");
            pars.put("estado", "");
            pars.put("pais", "");
            pars.put("cp", "");
            pars.put("telefono", "");
            pars.put("referencia", "");
            // SQL emisor
            try {
                String senSQL = "SELECT * FROM empresa WHERE id='1';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    pars.put("calle", rs0.getString("calle"));
                    pars.put("numeroext", rs0.getString("numeroext"));
                    pars.put("numeroint", rs0.getString("numeroint"));
                    pars.put("colonia", rs0.getString("colonia"));
                    pars.put("localidad", rs0.getString("localidad"));
                    pars.put("municipio", rs0.getString("municipio"));
                    pars.put("estado", rs0.getString("estado"));
                    pars.put("pais", rs0.getString("pais"));
                    pars.put("cp", rs0.getString("cod_postal"));

                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR DATOS DE EMISOR" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            //DATOS LUGAR DE EXPEDICION
            pars.put("emunicipio", "");
            pars.put("eestado", "");
            pars.put("epais", "");
            // SQL EXPEDICION
            try {
                String senSQL = "SELECT facturas.factura_serie, lugaresemision.* FROM facturas LEFT JOIN lugaresemision ON facturas.id_lugaremision = lugaresemision.id_lugaremision WHERE factura_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    pars.put("emunicipio", rs0.getString("municipio"));
                    pars.put("eestado", rs0.getString("estado"));
                    pars.put("epais", rs0.getString("pais"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR DATOS DE EXPEDICION" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            //DATOS RECEPTOR
            pars.put("f_rnombre", nodeReceptor.getAttribute("Nombre"));
            pars.put("f_rrfc", nodeReceptor.getAttribute("Rfc"));
            try {
                String senSQL = "SELECT facturas.factura_serie,facturas.clave_usocomprobante,clientes.* FROM facturas LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes WHERE factura_serie='" + documento + "';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    pars.put("f_rcalle", rs0.getString("calle"));
                    pars.put("f_rnumeroext", rs0.getString("numeroext"));
                    pars.put("f_rnumeroint", rs0.getString("numeroint"));
                    pars.put("f_rcolonia", rs0.getString("colonia"));
                    pars.put("f_rmunicipio", rs0.getString("municipio"));
                    pars.put("f_restado", rs0.getString("estado"));
                    pars.put("f_rpais", rs0.getString("pais"));
                    pars.put("f_rcp", rs0.getString("cod_postal"));
                    pars.put("f_rdias", rs0.getString("dias"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR DATOS DEL CLIENTE FACTURA" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            pars.put("nc_rnombre", nodeReceptor.getAttribute("Nombre"));
            pars.put("nc_rrfc", nodeReceptor.getAttribute("Rfc"));
            try {
                String senSQL = "SELECT notas_credito.nota_credito_serie, notas_credito.clave_usocomprobante, clientes.* FROM notas_credito LEFT JOIN clientes ON notas_credito.id_clientes = clientes.id_clientes WHERE nota_credito_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    pars.put("nc_rcalle", rs0.getString("calle"));
                    pars.put("nc_rnumeroext", rs0.getString("numeroext"));
                    pars.put("nc_rnumeroint", rs0.getString("numeroint"));
                    pars.put("nc_rcolonia", rs0.getString("colonia"));
                    pars.put("nc_rmunicipio", rs0.getString("municipio"));
                    pars.put("nc_restado", rs0.getString("estado"));
                    pars.put("nc_rpais", rs0.getString("pais"));
                    pars.put("nc_rcp", rs0.getString("cod_postal"));
                    pars.put("nc_rdias", rs0.getString("dias"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR DATOS DEL CLIENTE NOTA DE CREDITO" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            //DATOS DINEROS
            String tasas = "Sub-Total:\n";
            String tasasimp = moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("SubTotal"))) + "\n";
            if (rootElement.hasAttribute("Descuento")) {
                Double des = Double.parseDouble("" + rootElement.getAttribute("Descuento"));
                if (des > 0.0) {
                    tasas += "Descuento:\n";
                    tasasimp += moneda2decimales.format(des) + "\n";
                }
            }
            
          

            org.w3c.dom.NodeList Impuestos = rootElement.getElementsByTagName("cfdi:Impuestos");
            org.w3c.dom.Element nodeImpuestos = (org.w3c.dom.Element) Impuestos.item(Impuestos.getLength()-1);
            int i = 0;
            int j = 0;
            for (j = (nodeImpuestos.getChildNodes().getLength() - 1); j >= 0; j--) { //lo recorremos alreves para mostrar datos correctamente
                nodeImpuestos.getLastChild();
                String nodo = "" + nodeImpuestos.getLastChild().getNodeName();
                if (nodo.equals("cfdi:Retenciones")) {
                    org.w3c.dom.NodeList ImpuestosRetenciones = nodeImpuestos.getElementsByTagName("cfdi:Retenciones");
                    org.w3c.dom.Element nodeImpuestosRetenciones = (org.w3c.dom.Element) ImpuestosRetenciones.item(0);
                    if (nodeImpuestosRetenciones.hasChildNodes()) {
                        org.w3c.dom.NodeList ImpuestosRetencion = nodeImpuestosRetenciones.getElementsByTagName("cfdi:Retencion");
                        for (i = 0; i < ImpuestosRetencion.getLength(); i++) {
                            org.w3c.dom.Element nodeImpuestosRetencion = (org.w3c.dom.Element) ImpuestosRetencion.item(i);
                            tasas += "Ret. " + nodeImpuestosRetencion.getAttribute("Impuesto") + ":\n";
                            tasasimp += moneda2decimales.format(Double.parseDouble("" + nodeImpuestosRetencion.getAttribute("Importe"))) + "\n";
                        }
                    }
                }
                if (nodo.equals("cfdi:Traslados")) {
                    org.w3c.dom.NodeList ImpuestosTraslados = nodeImpuestos.getElementsByTagName("cfdi:Traslados");
                    org.w3c.dom.Element nodeImpuestosTraslados = (org.w3c.dom.Element) ImpuestosTraslados.item(0);
                    if (nodeImpuestosTraslados.hasChildNodes()) {
                        org.w3c.dom.NodeList ImpuestosTraslado = nodeImpuestosTraslados.getElementsByTagName("cfdi:Traslado");
                        for (i = 0; i < ImpuestosTraslado.getLength(); i++) {
                            org.w3c.dom.Element nodeImpuestosTraslado = (org.w3c.dom.Element) ImpuestosTraslado.item(i);

                            tasas += "Impuestos IVA" + " " + nodeImpuestosTraslado.getAttribute("TasaOCuota") + "%:\n";
                            //tasas += nodeImpuestosTraslado.getAttribute("Impuesto") + " " + nodeImpuestosTraslado.getAttribute("TasaOCuota") + "%:\n";
                            tasasimp += moneda2decimales.format(Double.parseDouble("" + nodeImpuestosTraslado.getAttribute("Importe"))) + "\n";
                        }
                    }
                }

            }
            
            
            
            tasas += "Total:";
            tasasimp += moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("Total")));

            //agrega los dos datos
            pars.put("tasas", tasas);
            pars.put("tasasimp", tasasimp);

            //si la factura esta cancelada
            pars.put("cancelafac", estatusfacx);

            JasperReport masterReport = null;
            try {
                if (tipocfd.equals("I")) {
                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_factura33.jasper"));
                } else {
                    //if (tipocfd.equals("E")) {
                    if (varios.equals("1")) {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_nc33.jasper"));
                    } else {
                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_nc_partidas33.jasper"));
                    }

                }
            } catch (JRException e) {
                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            jasperPrint = JasperFillManager.fillReport(masterReport, pars, connj);

            if (imprim.equals("Si")) {
                if (variasfac.equals("")) {
                    JasperPrintManager.printReport(jasperPrint, true);
                } else {
                    JasperPrintManager.printReport(jasperPrint, false);
                }
            } else {
                JasperExportManager.exportReportToPdfFile(jasperPrint, ruta);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
        }
    }

    class TareaPrueba extends javax.swing.SwingWorker<Void, Void> {

        public TareaPrueba() {
            setProgress(0);
        }

        @Override
        public Void doInBackground() {
            //obtiene los datos para los correos
            String h = "", pa = "", po = "";
            rs0 = null;
            try {
                String senSQL = "SELECT * FROM correo WHERE uso='true';";
                rs0 = conexion.consulta(senSQL, connj);
                if (rs0.next()) {
                    h = rs0.getString("host");
                    pa = conexion.decodificarCadena(rs0.getString("pass"));
                    po = rs0.getString("puerto");
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                errorenviando = "ERROR AL EJECUTAR LA CONSULTA\n" + ex;
                cancel(true);
            }

            //crea el xml para ser enviado
            String rutat = System.getProperty("user.home") + "/" + rfcempresa + "_" + clavemodificaf + "_" + fechasin.format(fechacreado) + ".xml";
            String valor_uuid = "" + creaXML(rutat);
            File nuevoxml = new File(rutat);
            //crea el pdf para ser enviado
            String rutapdf = System.getProperty("user.home") + "/" + rfcempresa + "_" + clavemodificaf + "_" + fechasin.format(fechacreado) + ".pdf";

            if (valor_uuid.isEmpty() || valor_uuid.equals("null") || valor_uuid.equals("")) {
                creaPDF(rutapdf, rutat, "No");
            } else {
                //creaPDF32(rutapdf, rutat, "No",valor_uuid);
                creaPDF33(rutapdf, rutat, "No", valor_uuid);
            }
            File nuevopdf = new File(rutapdf);
            //envia el correo
            try {
                String mensajefin = "<html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body><span style='font-family: Arial, Helvetica, sans-serif; font-size: 12px;'><span style='color: #006699; font-weight: bold; font-size: 14px;'>Estimado " + nombrecliente + "</span><br />" + mensaje.getText() + "<br /> <br /> <br /> <strong>Importante:</strong><br />De acuerdo a la reglamentaci&oacute;n del Servicio de Administraci&oacute;n Tributaria (SAT) publicada en el Diario Oficial de la Federaci&oacute;n (RMISC 2013) el 31 de mayo del 2013, la factura electr&oacute;nica es 100% valida y legal.<br /><br /><span style='font-size: 9px; font-weight: bold; text-decoration: underline'>Este correo ha sido generado autom&aacute;ticamente por iCorr v3.2<br /> No responda a este correo, si tiene alguna duda a cerca de su CFDI comun&iacute;quese a   <a href='mailto:cxc@skarton.com.mx'>cxc@skarton.com.mx</a></span><br /><br /><br /><center><span style='font-size: 13px; font-weight: bold; color:#009900;'>Atte.<br />  " + nombreempresacomercial + " </span></center></span></body></html>";
                Properties props = new Properties();
                props.put("mail.smtp.host", h);
                props.put("mail.smtp.socketFactory.port", po);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", po);

                final String paswo = pa;
                Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(de.getText(), paswo);
                    }
                });

                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(de.getText()));
                    //agrega las correos para
                    String parafinal = para.getText() + ";";
                    String[] arraypara = parafinal.split(";");
                    // En este momento tenemos un array en el que cada elemento del cc.
                    for (int i = 0; i < arraypara.length; i++) {
                        String parapartida = "" + arraypara[i];
                        System.err.println("" + parapartida);

                        if (!parapartida.equals("")) {
                            message.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(parapartida)});
                        }
                    }

                    //agrega las copias
                    String ccfinal = cc.getText() + ";";
                    String[] arraycc = ccfinal.split(";");
                    // En este momento tenemos un array en el que cada elemento del cc.
                    for (int i = 0; i < arraycc.length; i++) {
                        String ccpartida = "" + arraycc[i];
                        System.err.println("" + ccpartida);

                        if (!ccpartida.equals("")) {
                            message.addRecipients(Message.RecipientType.CC, new InternetAddress[]{new InternetAddress(ccpartida)});
                        }
                    }
                    message.addHeader("Disposition-Notification-To", de.getText());
                    message.setSubject(asunto.getText());
                    //message.setText(mensajefin);
                    //message.setContent(mensajefin,"text/html");

                    //crea todo el mensaje
                    MimeBodyPart mbp1 = new MimeBodyPart();
                    mbp1.setContent(mensajefin, "text/html");
                    //adjunta el archivo XML
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(rutat);
                    mbp2.setDataHandler(new DataHandler(fds));
                    mbp2.setFileName(fds.getName());
                    //adjunta el archivo PDF
                    MimeBodyPart mbppdf = new MimeBodyPart();
                    FileDataSource fdspdf = new FileDataSource(rutapdf);
                    mbppdf.setDataHandler(new DataHandler(fdspdf));
                    mbppdf.setFileName(fdspdf.getName());
                    //uno los mensajes creados
                    Multipart mp = new MimeMultipart();
                    mp.addBodyPart(mbp1);
                    mp.addBodyPart(mbp2);
                    mp.addBodyPart(mbppdf);

                    message.setContent(mp);
                    Transport.send(message);

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

                //elimina los archivos generados
                if (nuevoxml.exists()) {
                    nuevoxml.delete();
                }
                if (nuevopdf.exists()) {
                    nuevopdf.delete();
                }

            } catch (Exception ex) {
                errorenviando = "ERROR AL ENVIAR EL CORREO\n" + ex;
                cancel(true);
            }

            setProgress(100);
            return null;
        }

        @Override
        public void done() {
        }
    }

    public void imprimirlisto() {
        String rutaxml = System.getProperty("user.home") + "/" + rfcempresa + "_" + clavemodificaf + "_" + fechasin.format(fechacreado) + ".xml";
        String valor_uuid = "" + creaXML(rutaxml);
        File nuevoxml = new File(rutaxml);
        if (valor_uuid.isEmpty() || valor_uuid.equals("null") || valor_uuid.equals("")) {
            creaPDF("", rutaxml, "Si");
        } else {
            //creaPDF32("", rutaxml, "Si",valor_uuid);
            creaPDF33("", rutaxml, "Si", valor_uuid);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        guardaxml = new javax.swing.JFileChooser();
        guardapdf = new javax.swing.JFileChooser();
        fechas = new javax.swing.JPanel();
        btnxml = new javax.swing.JButton();
        btncorreo = new javax.swing.JButton();
        btnpdf = new javax.swing.JButton();
        btnimprimir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btncancelar = new javax.swing.JButton();
        panelcorreo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        de = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        para = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        asunto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mensaje = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(busca_CFD.class);
        guardaxml.setDialogTitle(resourceMap.getString("guardaxml.dialogTitle")); // NOI18N
        guardaxml.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        guardaxml.setName("guardaxml"); // NOI18N

        guardapdf.setDialogTitle(resourceMap.getString("guardapdf.dialogTitle")); // NOI18N
        guardapdf.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        guardapdf.setName("guardapdf"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        fechas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("fechas.border.titleFont"))); // NOI18N
        fechas.setName("fechas"); // NOI18N

        btnxml.setIcon(resourceMap.getIcon("btnxml.icon")); // NOI18N
        btnxml.setText(resourceMap.getString("btnxml.text")); // NOI18N
        btnxml.setToolTipText(resourceMap.getString("btnxml.toolTipText")); // NOI18N
        btnxml.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnxml.setMaximumSize(new java.awt.Dimension(75, 75));
        btnxml.setMinimumSize(new java.awt.Dimension(75, 75));
        btnxml.setName("btnxml"); // NOI18N
        btnxml.setPreferredSize(new java.awt.Dimension(75, 75));
        btnxml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxmlActionPerformed(evt);
            }
        });

        btncorreo.setIcon(resourceMap.getIcon("btncorreo.icon")); // NOI18N
        btncorreo.setText(resourceMap.getString("btncorreo.text")); // NOI18N
        btncorreo.setToolTipText(resourceMap.getString("btncorreo.toolTipText")); // NOI18N
        btncorreo.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btncorreo.setMaximumSize(new java.awt.Dimension(75, 75));
        btncorreo.setMinimumSize(new java.awt.Dimension(75, 75));
        btncorreo.setName("btncorreo"); // NOI18N
        btncorreo.setPreferredSize(new java.awt.Dimension(75, 75));
        btncorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncorreoActionPerformed(evt);
            }
        });

        btnpdf.setIcon(resourceMap.getIcon("btnpdf.icon")); // NOI18N
        btnpdf.setText(resourceMap.getString("btnpdf.text")); // NOI18N
        btnpdf.setToolTipText(resourceMap.getString("btnpdf.toolTipText")); // NOI18N
        btnpdf.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnpdf.setMaximumSize(new java.awt.Dimension(75, 75));
        btnpdf.setMinimumSize(new java.awt.Dimension(75, 75));
        btnpdf.setName("btnpdf"); // NOI18N
        btnpdf.setPreferredSize(new java.awt.Dimension(75, 75));
        btnpdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpdfActionPerformed(evt);
            }
        });

        btnimprimir.setIcon(resourceMap.getIcon("btnimprimir.icon")); // NOI18N
        btnimprimir.setText(resourceMap.getString("btnimprimir.text")); // NOI18N
        btnimprimir.setToolTipText(resourceMap.getString("btnimprimir.toolTipText")); // NOI18N
        btnimprimir.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnimprimir.setMaximumSize(new java.awt.Dimension(75, 75));
        btnimprimir.setMinimumSize(new java.awt.Dimension(75, 75));
        btnimprimir.setName("btnimprimir"); // NOI18N
        btnimprimir.setPreferredSize(new java.awt.Dimension(75, 75));
        btnimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fechasLayout = new javax.swing.GroupLayout(fechas);
        fechas.setLayout(fechasLayout);
        fechasLayout.setHorizontalGroup(
            fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fechasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btncorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(btnpdf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnxml, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        fechasLayout.setVerticalGroup(
            fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fechasLayout.createSequentialGroup()
                .addGroup(fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btncorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnxml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnpdf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setName("jPanel2"); // NOI18N

        btncancelar.setIcon(resourceMap.getIcon("btncancelar.icon")); // NOI18N
        btncancelar.setText(resourceMap.getString("btncancelar.text")); // NOI18N
        btncancelar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btncancelar.setName("btncancelar"); // NOI18N
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(161, 161, 161)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(164, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelcorreo.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelcorreo.border.title"))); // NOI18N
        panelcorreo.setName("panelcorreo"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        de.setEditable(false);
        de.setText(resourceMap.getString("de.text")); // NOI18N
        de.setFocusable(false);
        de.setName("de"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        para.setText(resourceMap.getString("para.text")); // NOI18N
        para.setName("para"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        cc.setText(resourceMap.getString("cc.text")); // NOI18N
        cc.setToolTipText(resourceMap.getString("cc.toolTipText")); // NOI18N
        cc.setName("cc"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        asunto.setEditable(false);
        asunto.setText(resourceMap.getString("asunto.text")); // NOI18N
        asunto.setFocusable(false);
        asunto.setName("asunto"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        mensaje.setColumns(20);
        mensaje.setFont(resourceMap.getFont("mensaje.font")); // NOI18N
        mensaje.setRows(5);
        mensaje.setName("mensaje"); // NOI18N
        jScrollPane1.setViewportView(mensaje);

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelcorreoLayout = new javax.swing.GroupLayout(panelcorreo);
        panelcorreo.setLayout(panelcorreoLayout);
        panelcorreoLayout.setHorizontalGroup(
            panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcorreoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelcorreoLayout.createSequentialGroup()
                        .addGroup(panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelcorreoLayout.createSequentialGroup()
                                .addGroup(panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(de, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                                    .addComponent(para, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                                    .addComponent(cc, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                                    .addComponent(asunto, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelcorreoLayout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155))))
        );
        panelcorreoLayout.setVerticalGroup(
            panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcorreoLayout.createSequentialGroup()
                .addGroup(panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(de, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(para, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelcorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(asunto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fechas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelcorreo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fechas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        estado = "cancelar";
        salir();
}//GEN-LAST:event_btncancelarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        estado = "cancelar";
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void btnxmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxmlActionPerformed
        // TODO add your handling code here:
        try {
            File nuevo = new File(System.getProperty("user.home") + "/" + rfcempresa + "_" + clavemodificaf + "_" + fechasin.format(fechacreado) + ".xml");
            guardaxml.setSelectedFile(nuevo);
            if (guardaxml.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {//cuando regresa un uno no guarda nada
                String rutafinal = String.valueOf(guardaxml.getSelectedFile());
                creaXML(rutafinal);
                JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL CREAR XML\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnxmlActionPerformed

    private void btncorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncorreoActionPerformed
        // TODO add your handling code here:
        panelcorreo.setVisible(true);
    }//GEN-LAST:event_btncorreoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (para.getText().equals("") || de.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            cancelado = false;
            final TareaPrueba TareaPrueba = new TareaPrueba();
            TareaPrueba.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        int completado = (Integer) evt.getNewValue();

                        if (TareaPrueba.isDone()) {
                            barraespera.setVisible(false);
                            if (cancelado.equals(false)) {
                                JOptionPane.showMessageDialog(null, "CORREO ENVIADO CORRECTAMENTE", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                    if (TareaPrueba.isCancelled()) {
                        barraespera.setVisible(false);
                        JOptionPane.showMessageDialog(null, errorenviando, " E R R O R !!!!!", JOptionPane.INFORMATION_MESSAGE);
                        cancelado = true;
                    }

                }
            });
            TareaPrueba.execute();
            barraespera.setLocationRelativeTo(this);
            barraespera.setVisible(true);

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnpdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpdfActionPerformed
        // TODO add your handling code here:
        try {
            File nuevo = new File(System.getProperty("user.home") + "/" + rfcempresa + "_" + clavemodificaf + "_" + fechasin.format(fechacreado) + ".pdf");
            guardapdf.setSelectedFile(nuevo);
            if (guardapdf.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {//cuando regresa un uno no guarda nada
                String rutaxml = System.getProperty("user.home") + "/" + rfcempresa + "_" + clavemodificaf + "_" + fechasin.format(fechacreado) + ".xml";
                String valor_uuid = "" + creaXML(rutaxml);
                String version = "" + getVersion();
                File nuevoxml = new File(rutaxml);

                String rutafinalpdf = String.valueOf(guardapdf.getSelectedFile());
                //creaPDF(rutafinalpdf, rutaxml, "No");

                if (valor_uuid.isEmpty() || valor_uuid.equals("null") || valor_uuid.equals("")) {
                    creaPDF(rutafinalpdf, rutaxml, "No");
                } else if (version.equals("cfdi33")) {
                    creaPDF33(rutafinalpdf, rutaxml, "No", valor_uuid);
                } else {
                    creaPDF32(rutafinalpdf, rutaxml, "No", valor_uuid);
                }

                //elimina los archivos generados
                if (nuevoxml.exists()) {
                    nuevoxml.delete();
                }

                JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL CREAR PDF\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnpdfActionPerformed

    private void btnimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimirActionPerformed
        // TODO add your handling code here:

        imprimirlisto();
        JOptionPane.showMessageDialog(this, "ARCHIVO ENVIADO A IMPRESORA", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnimprimirActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        String clavemodificafant = clavemodificaf;

        try {

            JLabel tituloc = new JLabel("NUMERO INICIAL:");
            JTextField noini = new JTextField();
            JLabel titulof = new JLabel("NUMERO FINAL:");
            JTextField nofin = new JTextField();
            JOptionPane.showConfirmDialog(this, new Object[]{tituloc, noini, titulof, nofin}, "CAPTURA LOS DATOS !!!", JOptionPane.PLAIN_MESSAGE);
            if (noini.getText().equals("") || noini.getText().equals("null") || nofin.getText().equals("") || nofin.getText().equals("null")) {
                JOptionPane.showMessageDialog(this, "CAMPOS VACIOS\n", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            } else {
                for (int i = Integer.parseInt(noini.getText()); i <= Integer.parseInt(nofin.getText()); i++) {
                    variasfac = "SI";
                    clavemodificaf = i + "F";
                    datosCliente("fac");
                    imprimirlisto();
                }
                JOptionPane.showMessageDialog(this, "ARCHIVOS ENVIADOS A IMPRESORA", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);
            }
            ///regresa variable
            variasfac = "";
            clavemodificaf = clavemodificafant;
            datosCliente("fac");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL IMPRIMIR\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jMenuItem1ActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField asunto;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btncorreo;
    private javax.swing.JButton btnimprimir;
    private javax.swing.JButton btnpdf;
    private javax.swing.JButton btnxml;
    private javax.swing.JTextField cc;
    private javax.swing.JTextField de;
    private javax.swing.JPanel fechas;
    private javax.swing.JFileChooser guardapdf;
    private javax.swing.JFileChooser guardaxml;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea mensaje;
    private javax.swing.JPanel panelcorreo;
    private javax.swing.JTextField para;
    // End of variables declaration//GEN-END:variables
}
