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

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Point;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

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

import java.util.HashMap;
import java.util.Map;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import jxl.*;
import jxl.write.*;
import java.awt.Desktop;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author IVONNE
 */
public class facturas extends javax.swing.JInternalFrame {

    Connection conn = null;
    
    Connection connj;
    
    DefaultTableModel modelot1 = null;
    ListSelectionModel modeloseleccion;
    ResultSet rs0 = null, rs1 = null, rs2 = null;
    private Properties conf;
    DecimalFormat fijo2decimales = new DecimalFormat("######0.00");
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat fechahora_importar = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat fechamostrartodo = new SimpleDateFormat(" dd 'del mes de' MMMMM 'de' yyyy");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechames = new SimpleDateFormat("MM");
    SimpleDateFormat fechaano = new SimpleDateFormat("yyyy");
    SimpleDateFormat fechamesano = new SimpleDateFormat("MMyyyy");
    SimpleDateFormat fechasin = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat fechaparser = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar10diasantes = new GregorianCalendar();
    Calendar calendarvencimiento = new GregorianCalendar();
    String usuariorem = "";
    String valor_privilegio = "1";

    String homeE = System.getProperty("user.home")+"/.temERP";
    Properties defaultProps = new Properties();
    FileInputStream ini;

    Date fechavencimiento = new Date();
    String telefono = "";
    String estatusfacx = "";
    String variasfac = "";
    String varios = "0";
    String user = "";

    DecimalFormat num_qr_decimales = new DecimalFormat("0000000000.000000");

    /** Creates new form usuarios */
    public facturas(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        usuariorem = conf.getProperty("UserID");
        conn = connt;
        modelot1 = (DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modeloseleccion = Tabladatos.getSelectionModel();
        ajusteTabladatos();
        datos_privilegios();
        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        datos();
        //fecha de fin de semana
        calendar10diasantes.setTime(new Date());
        calendar10diasantes.add(Calendar.DATE, -10);

        user = conf.getProperty("UserID");

        modeloseleccion.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    Double sumaiva = 0.0, sumatotal = 0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if (Tabladatos.getValueAt(i, 4) != null && !Tabladatos.getValueAt(i, 4).equals("")) { //suma los metros lineales
                                sumaiva += Double.parseDouble("" + Tabladatos.getValueAt(i, 4));
                            }
                            if (Tabladatos.getValueAt(i, 5) != null && !Tabladatos.getValueAt(i, 5).equals("")) { //suma los metros lineales
                                sumatotal += Double.parseDouble("" + Tabladatos.getValueAt(i, 5));
                            }
                        }
                    }
                    noregistros.setText("IVA: " + moneda2decimales.format(sumaiva) + "        TOTAL: " + moneda2decimales.format(sumatotal));
                }
            }
        });

    }

    public void salir() {
        if (conn != null) {
            conn = null;
        }
        dispose();
        this.setVisible(false);
    }

    public void datos_privilegios() {
        valor_privilegio = conexion.obtener_privilegios(conn, "Facturas");
    }

    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(310);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(1);


        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(110);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(110);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(230);

    }

    public void limpiatabla() {
        modelot1.setNumRows(0);
    }

    public void datos() {
        rs0 = null;
        try {
            String senSQL = "SELECT MAX(facturas.estatus) as estatus,MAX(facturas.iva) as iva,MAX(facturas.total) as total, MAX(facturas.factura_serie) as factura_serie,MAX(facturas.fecha) as fecha,MAX(facturas.varios) as varios,MAX(clientes.nombre) as nombre,MAX(monedas.descripcion) as tipomoneda,MAX(xmlfinal.uuid) as uuid,array_to_string(array_agg(facturas_detalle.id_op), ',') AS ops,array_to_string(array_agg(facturas_detalle.remision), ',') AS remisiones, MAX (facturas.ordencompra) AS OC  FROM ((((( (facturas LEFT JOIN xmlfinal ON facturas.factura_serie=xmlfinal.factura_serie) LEFT JOIN folios ON facturas.id_folios=folios.id_folio) LEFT JOIN monedas ON facturas.id_moneda=monedas.id_moneda) LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes) LEFT JOIN lugaresemision ON facturas.id_lugaremision=lugaresemision.id_lugaremision) LEFT JOIN impuestos ON facturas.id_impuestos=impuestos.id_impuestos) INNER JOIN (facturas_detalle LEFT JOIN remisiones ON facturas_detalle.remision=remisiones.remision ) ON facturas.factura_serie=facturas_detalle.factura_serie WHERE ((facturas.fecha>='" + conf.getProperty("FechaIni") + " 00:00:00' AND facturas.fecha<='" + conf.getProperty("FechaFin") + " 23:59:59')) GROUP BY facturas.factura_serie ORDER BY MAX(facturas.fecha);";
            rs0 = conexion.consulta(senSQL, conn);
            while (rs0.next()) {
                String est = "" + rs0.getString("estatus");
                String con = "";
                Double ivaf = rs0.getDouble("iva");
                Double totalf = rs0.getDouble("total");
                if (est.equals("0")) {
                    con = " ( CANCELADA )";
                    ivaf = 0.0;
                    totalf = 0.0;
                }
                Object datos[] = {rs0.getString("factura_serie"), rs0.getDate("fecha"), rs0.getString("nombre") + con, rs0.getString("tipomoneda"), ivaf, totalf, rs0.getString("varios"),rs0.getString("ops"),rs0.getString("remisiones"),rs0.getString("uuid"),rs0.getString("OC")};
                modelot1.addRow(datos);

            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void modificar() {
        int filano = Tabladatos.getSelectedRow();
        if (filano < 0) {
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            String clavemodifica = (String) Tabladatos.getValueAt(filano, 0);
            String varios = (String) Tabladatos.getValueAt(filano, 6);
            if (clavemodifica.equals("")) {
            } else {
                if (varios.equals("0")) {
                    datos_facturas = new datos_facturas(null, true, conn, clavemodifica, valor_privilegio);
                    datos_facturas.setLocationRelativeTo(this);
                    datos_facturas.setVisible(true);
                    limpiatabla();
                    datos_facturas = null;
                    datos();
                } else {
                    datos_facturas_varios = new datos_facturas_varios(null, true, conn, clavemodifica, valor_privilegio);
                    datos_facturas_varios.setLocationRelativeTo(this);
                    datos_facturas_varios.setVisible(true);
                    limpiatabla();
                    datos_facturas_varios = null;
                    datos();
                }
            }
        }
    }

//    public void creaPDF32(String ruta, String rutaxml, String imprim,String uuid,String factura_serie) {
//        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(CONTAAboutBox.class);
//        JasperPrint jasperPrint = null;
//        try {
//            File nuevoxml = new File(rutaxml); //file xml
//            CFDv32 cfdi = new CFDv32(new FileInputStream(nuevoxml)); //ller xml para obetener xml
//
//            org.w3c.dom.Document dom = null;
//            javax.xml.parsers.DocumentBuilderFactory dbf;
//            javax.xml.parsers.DocumentBuilder db;
//            dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
//            try {
//                db = dbf.newDocumentBuilder();
//                dom = db.parse(nuevoxml.toURI().toString());
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error parse XML CFDI" + ex);
//            }
//            Map pars = new HashMap();
//            org.w3c.dom.Element rootElement = dom.getDocumentElement();
//            org.w3c.dom.NodeList Emisor = rootElement.getElementsByTagName("cfdi:Emisor");
//            org.w3c.dom.Element nodeEmisor = (org.w3c.dom.Element) Emisor.item(0);
//            org.w3c.dom.NodeList EmisorDom = nodeEmisor.getElementsByTagName("cfdi:DomicilioFiscal");
//            org.w3c.dom.Element nodeEmisorDom = (org.w3c.dom.Element) EmisorDom.item(0);
//            org.w3c.dom.NodeList EmisorExp = nodeEmisor.getElementsByTagName("cfdi:ExpedidoEn");
//            org.w3c.dom.Element nodeEmisorExp = (org.w3c.dom.Element) EmisorExp.item(0);
//            org.w3c.dom.NodeList EmisorReg = nodeEmisor.getElementsByTagName("cfdi:RegimenFiscal");
//            int ii = 0;
//            for (ii = 0; ii < EmisorReg.getLength(); ii++) {
//                org.w3c.dom.Element nodeEmisorReg = (org.w3c.dom.Element) EmisorReg.item(0);
//                if (nodeEmisorReg.hasAttribute("Regimen")) {
//                    pars.put("regimenfiscal", "REGIMEN FISCAL: " + nodeEmisorReg.getAttribute("Regimen"));
//                } else {
//                    pars.put("regimenfiscal", "");
//                }
//            }
//            org.w3c.dom.NodeList Receptor = rootElement.getElementsByTagName("cfdi:Receptor");
//            org.w3c.dom.Element nodeReceptor = (org.w3c.dom.Element) Receptor.item(0);
//            org.w3c.dom.NodeList ReceptorDom = nodeReceptor.getElementsByTagName("cfdi:Domicilio");
//            org.w3c.dom.Element nodeReceptorDom = (org.w3c.dom.Element) ReceptorDom.item(0);
//
//            nuevas lienas agregadas
//            org.w3c.dom.NodeList Complemento = rootElement.getElementsByTagName("cfdi:Complemento");
//            org.w3c.dom.Element nodeComplemento = (org.w3c.dom.Element) Complemento.item(0);
//            org.w3c.dom.NodeList Timbrefiscal = nodeComplemento.getElementsByTagName("tfd:TimbreFiscalDigital");
//            org.w3c.dom.Element nodeTimbrefiscal = (org.w3c.dom.Element) Timbrefiscal.item(0);
//
//            pars.put("uuid", nodeTimbrefiscal.getAttribute("UUID"));
//            pars.put("fechatimbrado", nodeTimbrefiscal.getAttribute("FechaTimbrado"));
//            pars.put("sellocfd", nodeTimbrefiscal.getAttribute("selloCFD"));
//            pars.put("nocersat", nodeTimbrefiscal.getAttribute("noCertificadoSAT"));
//            pars.put("sellosat", nodeTimbrefiscal.getAttribute("selloSAT"));
//            pars.put("versioncfdi", nodeTimbrefiscal.getAttribute("version"));
//
//
//            Double total_cfdi=Double.parseDouble(""+rootElement.getAttribute("total"));
//            String total_cfdi_c=num_qr_decimales.format(total_cfdi);
//
//            generamos el codigo de barras
//            String cadena_qr="?re="+nodeEmisor.getAttribute("rfc")+"&rr="+nodeReceptor.getAttribute("rfc")+"&tt="+total_cfdi_c+"&id="+nodeTimbrefiscal.getAttribute("UUID")+"";
//            System.err.println(cadena_qr);
//
//            File ruta_qr = new File(System.getProperty("user.home") + "/"+"QRC.PNG");
//            ByteArrayOutputStream out =QRCode.from(cadena_qr).to(ImageType.PNG).withSize(300, 300).stream();
//            try {
//                FileOutputStream fout = new FileOutputStream(ruta_qr);
//                fout.write(out.toByteArray());
//                fout.flush();
//                fout.close();
//            } catch (FileNotFoundException e) {
//                 Do Logging
//            } catch (IOException e) {
//                 Do Logging
//            }
//            le mando el archvio al pdf
//            pars.put("qruuid", new FileInputStream(ruta_qr));
//            luego lo borro
//            if (ruta_qr.exists()) {
//                if (ruta_qr.delete()) {
//                    System.err.println("archivo qr borrado");
//                }
//            }
//
//            System.err.println("Prueba de impresion despues de generar qqr");
//
//
//            lineas de codi
//            Map pars = new HashMap();
//            File fichero = new File(conexion.Directorio() + "/logoempresa.png");
//            File fichero2 = new File(conexion.Directorio() + "/rfcempresa.png");
//            pars.put("folio", null);
//            pars.put("logoempresa", new FileInputStream(fichero));
//            pars.put("rfcempresa", new FileInputStream(fichero2));
//            pars.put("subtitulo", null);//datos
//            pars.put("fechaini", null);//fechaini.getDate()
//            pars.put("fechafin", null);//fechafin.getDate()
//            pars.put("senSQL", "");//SQL dinamico
//            pars.put("version", resourceMap.getString("Application.title"));
//            pars.put("folio_serie", factura_serie);//SQL dinamico
//            pars.put("cadena", cfdi.getCadenaOriginal());//SQL dinamico
//
//            ingresa todos los datos XML
//            pars.put("folio", rootElement.getAttribute("folio"));
//            if (rootElement.hasAttribute("serie")) {
//                pars.put("serie", "" + rootElement.getAttribute("serie"));
//            } else {
//                pars.put("serie", "");
//            }
//
//            AGREGAMOS MOVIMIENTOS CFD 2012
//            if (rootElement.hasAttribute("metodoDePago")) {
//                pars.put("metodopago", "Método de Pago: " + rootElement.getAttribute("metodoDePago"));
//            } else {
//                pars.put("metodopago", "");
//            }
//            if (rootElement.hasAttribute("NumCtaPago")) {
//                pars.put("ctapago", "" + rootElement.getAttribute("NumCtaPago"));
//            } else {
//                pars.put("ctapago", "");
//            }
//            termina ajustes para CFD 2012
//
//            pars.put("certificado", rootElement.getAttribute("noCertificado"));
//            pars.put("noapro", rootElement.getAttribute("noAprobacion"));
//            pars.put("anoapro", rootElement.getAttribute("anoAprobacion"));
//            pars.put("sello", rootElement.getAttribute("sello"));
//            pars.put("formadepago", rootElement.getAttribute("formaDePago"));
//
//            String tipocfd = rootElement.getAttribute("tipoDeComprobante");
//            if (tipocfd.equals("ingreso")) {
//                pars.put("tipocfd", "Factura");
//            } else {
//                pars.put("tipocfd", "Nota de Crédito");
//            }
//
//            regenera fecha
//            String fecha1 = "" + rootElement.getAttribute("fecha");
//            String fechap1 = "";
//            String fechap2 = "";
//            String[] arrayfecha = fecha1.split("T");
//             En este momento tenemos un array en el que cada elemento del cc.
//            for (int i = 0; i < arrayfecha.length; i++) {
//                if (i == 0) {
//                    fechap1 = "" + arrayfecha[0];
//                }
//                if (i == 1) {
//                    fechap2 = "" + arrayfecha[1];
//                }
//            }
//            pars.put("fecha", fechamascorta.format(fechaparser.parse(fechap1)) + " " + fechap2);
//            pars.put("fechacreado", fechamostrartodo.format(fechaparser.parse(fechap1)));
//            pars.put("fechavencido", fechamostrartodo.format(fechavencimiento));
//            pars.put("totalfinal", "" + moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("total"))));
//
//            Datos emisor con domicilio fiscal
//            pars.put("nombre", nodeEmisor.getAttribute("nombre"));
//            pars.put("rfc", nodeEmisor.getAttribute("rfc"));
//            pars.put("calle", nodeEmisorDom.getAttribute("calle"));
//            if (nodeEmisorDom.hasAttribute("noExterior")) {
//                pars.put("numeroext", "" + nodeEmisorDom.getAttribute("noExterior"));
//            } else {
//                pars.put("numeroext", "");
//            }
//            if (nodeEmisorDom.hasAttribute("noInterior")) {
//                pars.put("numeroint", "" + nodeEmisorDom.getAttribute("noInterior"));
//            } else {
//                pars.put("numeroint", "");
//            }
//            if (nodeEmisorDom.hasAttribute("colonia")) {
//                pars.put("colonia", "" + nodeEmisorDom.getAttribute("colonia"));
//            } else {
//                pars.put("colonia", "");
//            }
//            if (nodeEmisorDom.hasAttribute("localidad")) {
//                pars.put("localidad", "" + nodeEmisorDom.getAttribute("localidad"));
//            } else {
//                pars.put("localidad", "");
//            }
//            pars.put("municipio", nodeEmisorDom.getAttribute("municipio"));
//            pars.put("estado", nodeEmisorDom.getAttribute("estado"));
//            pars.put("pais", nodeEmisorDom.getAttribute("pais"));
//            pars.put("cp", nodeEmisorDom.getAttribute("codigoPostal"));
//            pars.put("telefono", telefono);
//
//            if (nodeEmisorDom.hasAttribute("referencia")) {
//                pars.put("referencia", "" + nodeEmisorDom.getAttribute("referencia"));
//            } else {
//                pars.put("referencia", "");
//            }
//
//            Expedido En
//            if (nodeEmisorExp.hasAttribute("municipio")) {
//                pars.put("emunicipio", "" + nodeEmisorExp.getAttribute("municipio"));
//            } else {
//                pars.put("emunicipio", "");
//            }
//
//            if (nodeEmisorExp.hasAttribute("estado")) {
//                pars.put("eestado", "" + nodeEmisorExp.getAttribute("estado"));
//            } else {
//                pars.put("eestado", "");
//            }
//            if (nodeEmisorExp.hasAttribute("pais")) {
//                pars.put("epais", "" + nodeEmisorExp.getAttribute("pais"));
//            } else {
//                pars.put("epais", "");
//            }
//            Datos receptor
//            pars.put("rnombre", nodeReceptor.getAttribute("nombre"));
//            pars.put("rrfc", nodeReceptor.getAttribute("rfc"));
//            pars.put("rcalle", nodeReceptorDom.getAttribute("calle"));
//            if (nodeReceptorDom.hasAttribute("noExterior")) {
//                pars.put("rnumeroext", "" + nodeReceptorDom.getAttribute("noExterior"));
//            } else {
//                pars.put("rnumeroext", "");
//            }
//            if (nodeReceptorDom.hasAttribute("noInterior")) {
//                pars.put("rnumeroint", "" + nodeReceptorDom.getAttribute("noInterior"));
//            } else {
//                pars.put("rnumeroint", "");
//            }
//            if (nodeReceptorDom.hasAttribute("colonia")) {
//                pars.put("rcolonia", "" + nodeReceptorDom.getAttribute("colonia"));
//            } else {
//                pars.put("rcolonia", "");
//            }
//            if (nodeReceptorDom.hasAttribute("referencia")) {
//                pars.put("rreferencia", "" + nodeReceptorDom.getAttribute("referencia"));
//            } else {
//                pars.put("rreferencia", "");
//            }
//            if (nodeReceptorDom.hasAttribute("localidad")) {
//                pars.put("rlocalidad", "" + nodeReceptorDom.getAttribute("localidad"));
//            } else {
//                pars.put("rlocalidad", "");
//            }
//            pars.put("rmunicipio", nodeReceptorDom.getAttribute("municipio"));
//            pars.put("restado", nodeReceptorDom.getAttribute("estado"));
//            pars.put("rpais", nodeReceptorDom.getAttribute("pais"));
//            pars.put("rcp", nodeReceptorDom.getAttribute("codigoPostal"));
//
//
//            DATOS DINEROS
//            String tasas = "Sub-Total:\n";
//            String tasasimp = moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("subTotal"))) + "\n";
//            if (rootElement.hasAttribute("descuento")) {
//                Double des = Double.parseDouble("" + rootElement.getAttribute("descuento"));
//                if (des > 0.0) {
//                    tasas += "Descuento:\n";
//                    tasasimp += moneda2decimales.format(des) + "\n";
//                }
//            }
//
//            org.w3c.dom.NodeList Impuestos = rootElement.getElementsByTagName("cfdi:Impuestos");
//            org.w3c.dom.Element nodeImpuestos = (org.w3c.dom.Element) Impuestos.item(0);
//            int i = 0;
//            int j = 0;
//            for (j = (nodeImpuestos.getChildNodes().getLength() - 1); j >= 0; j--) { //lo recorremos alreves para mostrar datos correctamente
//                String nodo = "" + nodeImpuestos.getChildNodes().item(j).getNodeName();
//                if (nodo.equals("cfdi:Retenciones")) {
//                    org.w3c.dom.NodeList ImpuestosRetenciones = nodeImpuestos.getElementsByTagName("cfdi:Retenciones");
//                    org.w3c.dom.Element nodeImpuestosRetenciones = (org.w3c.dom.Element) ImpuestosRetenciones.item(0);
//                    if (nodeImpuestosRetenciones.hasChildNodes()) {
//                        org.w3c.dom.NodeList ImpuestosRetencion = nodeImpuestosRetenciones.getElementsByTagName("cfdi:Retencion");
//                        for (i = 0; i < ImpuestosRetencion.getLength(); i++) {
//                            org.w3c.dom.Element nodeImpuestosRetencion = (org.w3c.dom.Element) ImpuestosRetencion.item(i);
//                            tasas += "Ret. " + nodeImpuestosRetencion.getAttribute("impuesto") + ":\n";
//                            tasasimp += moneda2decimales.format(Double.parseDouble("" + nodeImpuestosRetencion.getAttribute("importe"))) + "\n";
//                        }
//                    }
//                }
//                if (nodo.equals("cfdi:Traslados")) {
//                    org.w3c.dom.NodeList ImpuestosTraslados = nodeImpuestos.getElementsByTagName("cfdi:Traslados");
//                    org.w3c.dom.Element nodeImpuestosTraslados = (org.w3c.dom.Element) ImpuestosTraslados.item(0);
//                    if (nodeImpuestosTraslados.hasChildNodes()) {
//                        org.w3c.dom.NodeList ImpuestosTraslado = nodeImpuestosTraslados.getElementsByTagName("cfdi:Traslado");
//                        for (i = 0; i < ImpuestosTraslado.getLength(); i++) {
//                            org.w3c.dom.Element nodeImpuestosTraslado = (org.w3c.dom.Element) ImpuestosTraslado.item(i);
//
//                            tasas += nodeImpuestosTraslado.getAttribute("impuesto") + " " + nodeImpuestosTraslado.getAttribute("tasa") + "%:\n";
//                            tasasimp += moneda2decimales.format(Double.parseDouble("" + nodeImpuestosTraslado.getAttribute("importe"))) + "\n";
//                        }
//                    }
//                }
//
//            }
//
//            tasas += "Total:";
//            tasasimp += moneda2decimales.format(Double.parseDouble("" + rootElement.getAttribute("total")));
//
//            agrega los dos datos
//            pars.put("tasas", tasas);
//            pars.put("tasasimp", tasasimp);
//
//            si la factura esta cancelada
//            pars.put("cancelafac", estatusfacx);
//
//            JasperReport masterReport = null;
//            try {
//                if (tipocfd.equals("ingreso")) {
//                    masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_factura32.jasper"));
//                } else {
//                    if (varios.equals("1")) {
//                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_nc32.jasper"));
//                    } else {
//                        masterReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/formato_nc_partidas32.jasper"));
//                    }
//                }
//            } catch (JRException e) {
//                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error cargando el reporte maestro:\n" + e.getMessage() + "\n\nNo se puede cargar el reporte...", "E R R O R !!!!!!!!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
//            }
//
//            jasperPrint = JasperFillManager.fillReport(masterReport, pars, conn);
//
//            if (imprim.equals("Si")) {
//                if (variasfac.equals("")) {
//                    JasperPrintManager.printReport(jasperPrint, true);
//                } else {
//                    JasperPrintManager.printReport(jasperPrint, false);
//                }
//            } else {
//                JasperExportManager.exportReportToPdfFile(jasperPrint, ruta);
//            }
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Error leyendo el reporte" + ex);
//        }
//    }
   
    
    public void creaPDF33(String ruta, String rutaxml, String imprim, String uuid, String factura_serie) {
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
            //pars.put("folio_serie", clavemodificaf);//SQL dinamico
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
                String senSQL = "SELECT factura_serie, clave_usocomprobante, usocomprobante, clave_formapago, formapago, clave_metodopago, metodopago, cuentapago FROM facturas WHERE factura_serie = '" + factura_serie + "';";
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
                String senSQL = "SELECT nota_credito_serie, clave_usocomprobante, usocomprobante, clave_formapago, formapago, clave_metodopago, metodopago, cuentapago FROM notas_credito WHERE nota_credito_serie = '" + factura_serie + "';";
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
                String senSQL = "SELECT facturas.factura_serie, lugaresemision.* FROM facturas LEFT JOIN lugaresemision ON facturas.id_lugaremision = lugaresemision.id_lugaremision WHERE factura_serie = '" + factura_serie + "';";
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
                String senSQL = "SELECT facturas.factura_serie,facturas.clave_usocomprobante,clientes.* FROM facturas LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes WHERE factura_serie='" + factura_serie + "';";
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
                String senSQL = "SELECT notas_credito.nota_credito_serie, notas_credito.clave_usocomprobante, clientes.* FROM notas_credito LEFT JOIN clientes ON notas_credito.id_clientes = clientes.id_clientes WHERE nota_credito_serie = '" + factura_serie + "';";
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
    
    

    private void importar_xml_cfdi(){
        if(file_load_xml.showDialog(this, null)==JFileChooser.APPROVE_OPTION){

            try {
                String xml_path = String.valueOf(file_load_xml.getSelectedFile());
                File xml_directory = new File(xml_path);
                if (xml_directory.exists()){ //Valida directorio
                    File[] files = xml_directory.listFiles(); //Lista de archivos en el directorio
                    for (int x=0;x<files.length;x++){
                        String file_path = String.valueOf(files[x].getAbsoluteFile()); //ruta absoluta del archivo
                        File xml=new File(file_path);
                        CFDv32 cfdi = new CFDv32(new FileInputStream(xml)); //ller xml para obetener xml

                        org.w3c.dom.Document dom = null; //Leemos XML
                        javax.xml.parsers.DocumentBuilderFactory dbf;
                        javax.xml.parsers.DocumentBuilder db;
                        dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                        db = dbf.newDocumentBuilder();
                        dom = db.parse(file_path);

                        org.w3c.dom.Element rootElement = dom.getDocumentElement();
                        org.w3c.dom.NodeList Complemento = rootElement.getElementsByTagName("cfdi:Complemento");
                        org.w3c.dom.Element nodeComplemento = (org.w3c.dom.Element) Complemento.item(0);
                        org.w3c.dom.NodeList Timbrefiscal = nodeComplemento.getElementsByTagName("tfd:TimbreFiscalDigital");
                        org.w3c.dom.Element nodeTimbrefiscal = (org.w3c.dom.Element) Timbrefiscal.item(0);
                        /*Variables generales*/
                        String uuid = nodeTimbrefiscal.getAttribute("UUID");
                        String folio = rootElement.getAttribute("folio");
                        String serie = "";
                        if (rootElement.hasAttribute("serie")) {
                            serie = rootElement.getAttribute("serie");
                        }
                        String factura_serie = folio+serie;
                        /*Comvertimos xml en cadena*/
                        byte[] buffer = new byte[(int) xml.length()];
                        FileInputStream f = new FileInputStream(file_path);
                        f.read(buffer);
                        String xml_final = new String(buffer);
                        if (f != null) {
                            f.close();
                        }

                        String senSQL = "SELECT facturas.* FROM facturas WHERE factura_serie='" + factura_serie + "' ;";
                        ResultSet factura_info = conexion.consulta(senSQL, conn);
                        if (factura_info.next()) {
                            String clave_cliente = factura_info.getString("id_clientes"); //Variable de encabezado
                            String fecha = fechainsertarhora.format(factura_info.getDate("fecha"));

                            senSQL = "SELECT xmlfinal.* FROM xmlfinal WHERE factura_serie='" + factura_serie + "' ;";
                            ResultSet xml_info = conexion.consulta(senSQL, conn);
                            if (xml_info.next()) {

                            }else{
                                senSQL = "INSERT INTO xmlfinal(factura_serie, fecha, estatus, xmlfinal, cadenaoriginal,id_clientes,uuid) VALUES ('" + factura_serie + "', '" + fecha + "', '1', '" + xml_final + "', '" + cfdi.getCadenaOriginal() + "','" + clave_cliente + "','"+uuid+ "');";
                                conexion.modificamov_p(senSQL, conn, valor_privilegio);
                            }
                            //xml.delete(); //Elimino el archivo
                            if (xml_info != null) {
                                xml_info.close();
                            }
                        }
                        if (factura_info != null) {
                            factura_info.close();
                        }

                    }

                    JOptionPane.showMessageDialog(this, "IMPORTACION DE XML CORRECTAMENTE", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);

                    //Cargar tabla nuevamente
                    limpiatabla();
                    datos();

                } else {
                    JOptionPane.showMessageDialog(this, "El directorio no existe\n", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void importar_facturas_csv(){
        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv");
        file_load_sae.setFileFilter(filter);
        if (file_load_sae.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {

            String csvFile = String.valueOf(file_load_sae.getSelectedFile());
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ";";
            try {

                /********************************************* Validacion de datos ************************************************/
                String error = "";
                br = new BufferedReader(new FileReader(csvFile)); //Leer archivo
                int i = 1;
                while ((line = br.readLine()) != null) {
                    String[] mov = line.split(cvsSplitBy); //Lectura de linea y guardar en arreglo
                    String p_serie = mov[0], p_folio = mov[1], p_factura_serie =  mov[1]+mov[0], p_fecha = mov[2], p_remision = mov[3], p_cantidad = mov[4], p_um = mov[5];
                    //Validamos campos vacios
                    if (p_remision.equals("") || p_factura_serie.equals("") || p_cantidad.equals("") || p_fecha.equals("") || p_um.equals("")) {
                        error += "Linea (" + i + ") Existen campos vacios\n";
                    } else {
                        //Valida que la remision exista
                        String senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + p_remision + "' AND facturado='false') ORDER BY remisiones.fechahora;";
                        ResultSet remision_info = conexion.consulta(senSQL, conn);
                        if (remision_info.next()) {
                            Double cantidad_pendiente = remision_info.getDouble("cantrempendiente");
                            Double cantidad_remision = Double.parseDouble(p_cantidad);
                            //Valida que la cantidad factura no supere la pendiente por facturar
                            if (cantidad_remision > cantidad_pendiente) {
                                error += "Linea (" + i + ") La cantidad (" + estandarentero.format(cantidad_remision) + ") de la remision " + p_remision + " es mayor a lo pendiente de remisionar (" + estandarentero.format(cantidad_pendiente) + ")\n";
                            }
                        } else {
                            error += "Linea (" + i + ") La remision " + p_remision + " no existe\n";
                        }
                    }
                    i++;
                }
                /********************************************* Fin validacion de datos ************************************************/
                if (!error.equals("")) { //Validamos si hay errores
                    JOptionPane.showMessageDialog(this, "Existen errores en el archivo\n" + error, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                } else {

                    br = new BufferedReader(new FileReader(csvFile)); //Lectura de archivo
                    String partidas[][] = new String[50][7]; //Variable temporal para guardar por factura
                    boolean nueva_factura = false; //Bandera para validar si cambia de factura
                    String factura_serie_ant = ""; //Validar cambio de factura
                    int fac_i = 0;

                    /******************************************** Recorrer archivo por lineas *******************************************/
                    while ((line = br.readLine()) != null) {

                        String[] mov = line.split(cvsSplitBy); //Lectura de linea y guardar en arreglo
                        String p_serie = mov[0], p_folio = mov[1], p_factura_serie = mov[1] + mov[0], p_fecha = mov[2], p_remision = mov[3], p_cantidad = mov[4], p_um = mov[5];


                        nueva_factura = false; //Cuando ya cambio de factura guardo el registro
                        if (factura_serie_ant.equals("")) { //Iniciamos variable para primera partida
                            factura_serie_ant = p_factura_serie;
                        }

                        //Leemos ordenado todas las remisiones hasta que cambia de folio
                        if (!factura_serie_ant.equals(p_factura_serie)) {
                            nueva_factura = true;
                        }

                        /****************************************** Nueva Factura *****************************************/
                        if (nueva_factura) {
                            int j = 0;
                            String senSQL = "";

                            //Variables cabecera de factura
                            String fecha = "";
                            String clave_cliente = "";
                            String serie = "";
                            String folio = "";
                            String factura_serie = "";
                            Double subtotal_general = 0.0;
                            Double fletes = 0.0;
                            Double descuento = 0.0;
                            Double iva = 0.0;
                            Double retiva = 0.0;
                            Double retisr = 0.0;

                            for (j = 0; j < partidas.length; j++) { //Recorer la variable temporal para detalles de factura

                                if (partidas[j][2] != null) { //Validamos que la fecha no este vacia

                                    //Variables de encabezado
                                    serie = partidas[j][0];
                                    folio = partidas[j][1];
                                    factura_serie = partidas[j][2];
                                    fecha = partidas[j][3];

                                    //Varibales por cada partida
                                    String remision = partidas[j][4];
                                    Double cantidad = Double.parseDouble(partidas[j][5]);
                                    String um = partidas[j][6]; //unidad de medida
                                    Double pu = 0.0; //precio unitario
                                    String id_op = ""; //op
                                    String clave_articulo = ""; //clave articulo
                                    String descripcion_articulo = ""; //descripcion articulo
                                    if (um.toUpperCase().equals("MILLAR")) { //para el caso de millar hace ajustes de cantidad
                                        //cantidad = cantidad*1000;
                                        //pu = pu/1000;
                                    }

                                    //Consultamos la remision para actualizar datos
                                    senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,remisiones.id_clientes,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + remision + "' AND remisiones.facturado='false') ORDER BY remisiones.fechahora;";
                                    ResultSet remision_info = conexion.consulta(senSQL, conn);
                                    if (remision_info.next()) {
                                        clave_cliente = remision_info.getString("id_clientes"); //Variable de encabezado
                                        pu = remision_info.getDouble("preciounitario");
                                        id_op = remision_info.getString("id_op");
                                        clave_articulo = remision_info.getString("clavearticulo");
                                        descripcion_articulo = remision_info.getString("articulo");
                                    }
                                    if (remision_info != null) {
                                        remision_info.close();
                                    }

                                    //CalculaSubtotales
                                    Double subtotal = cantidad * pu;
                                    subtotal_general += subtotal;

                                    //Guarda la partida
                                    senSQL = "INSERT INTO facturas_detalle(factura_serie, estatus, factura, serie, remision, id_op, clavearticulo, descripcion, cantidad, um, preciounitario, subtotal) VALUES ('" + factura_serie + "', '1', '" + folio + "', '" + serie + "', '" + remision + "', '" + id_op + "', '" + clave_articulo + "', '" + descripcion_articulo + "', '" + cantidad + "', '" + um + "', '" + pu + "', '" + subtotal + "');";
                                    conexion.modificamov_p(senSQL, conn, valor_privilegio);

                                    //Actualiza remision como facturada si esta completa
                                    senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + remision + "');";
                                    remision_info = conexion.consulta(senSQL, conn);
                                    if (remision_info.next()) {
                                        Double cantidadporrem = remision_info.getDouble("cantrempendiente");
                                        if (cantidadporrem <= 0.0) {
                                            senSQL = "UPDATE remisiones SET facturado='TRUE' WHERE remision='" + remision + "';";
                                            conexion.modificamov_p(senSQL, conn, valor_privilegio);
                                        }
                                    }
                                    if (remision_info != null) {
                                        remision_info.close();
                                    }
                                }
                            }//FIN Detalles de factura

                            //Partida detalle adicional por fletes
                            if (fletes > 0) {
                                senSQL = "INSERT INTO facturas_detalle(factura_serie, estatus, factura, serie, remision, id_op, clavearticulo, descripcion, cantidad, um, preciounitario, subtotal) VALUES ('" + factura_serie + "', '1', '" + folio + "', '" + serie + "', '0001', '0', 'FLT', 'FLETE FECHA " + fechainsertar.format(new Date()) + "', '1', 'ser','" + fletes + "','" + fletes + "');";
                                conexion.modificamov_p(senSQL, conn, valor_privilegio);
                            }

                            //Variables cabecera de factura
                            int diasc = 0;

                            //Datos del cliente
                            senSQL = "SELECT clientes.* FROM clientes WHERE id_clientes='" + clave_cliente + "' ";
                            ResultSet customer_info = conexion.consulta(senSQL, conn);
                            if (customer_info.next()) {
                                diasc = customer_info.getInt("dias");
                            }


                            //Variables cabecera de factura
                            Date fechacreado = fechahora_importar.parse(fecha); //Fecha factura
                            if (diasc > 0) {
                                calendarvencimiento.add(Calendar.DATE, diasc); //Fecha vencimiento
                            }
                            Date fechaven = calendarvencimiento.getTime(); //Fecha vencimiento
                            int id_folio = 6; //Creado para los folios 1A que seran del SAE y para el ENNOVI 1F
                            String oc = ""; //Este lo obtenemos de todas las op de las remisiones
                            int id_lugar = 1; //Lugar de emision
                            int id_moneda = 1; //Es para MXN pero hay que revisar si todo sera en pesos
                            Double valormoneda = 1.0;
                            String moneda = "PESOS";
                            String terminacion = "M.N.";
                            senSQL = "SELECT * FROM monedas WHERE id_moneda='" + id_moneda + "'";
                            ResultSet moneda_info = conexion.consulta(senSQL, conn);
                            if (moneda_info.next()) { //Actualiza informacion de moneda
                                valormoneda = moneda_info.getDouble("valor");
                                moneda = moneda_info.getString("descripcion");
                                terminacion = moneda_info.getString("terminacion");
                            }
                            int id_impuestos = 1; // Esta la tasa de impuestos normal de IVA
                            Double tasaiva = 0.0;
                            Double tasaretiva = 0.0;
                            Double tasaretisr = 0.0;
                            senSQL = "SELECT * FROM impuestos WHERE id_impuestos='" + id_impuestos + "'";
                            ResultSet impuesto_info = conexion.consulta(senSQL, conn);
                            if (impuesto_info.next()) { //Actualiza informacion de impuestos
                                tasaiva = impuesto_info.getDouble("iva");
                                tasaretiva = impuesto_info.getDouble("ivaretenido");
                                tasaretisr = impuesto_info.getDouble("isrretenido");
                            }
                            String notas = ""; //Notas de la factura
                            int id_formapago = 7; // Lo dejamos como no identificado
                            String ctapago = ""; // Cuenta de pago vacio


                            //Calculos finales
                            Double importe = subtotal_general + fletes - descuento;
                            iva = (tasaiva / 100) * importe;
                            retiva = (tasaretiva / 100) * importe;
                            retisr = (tasaretisr / 100) * importe;
                            Double total = importe + iva - retiva - retisr;
                            //Importe con letra
                            String total_str = fijo2decimales.format(total);
                            int dotPos = total_str.lastIndexOf(".") + 1;
                            String partedecimal = total_str.substring(dotPos);
                            String parteentero = total_str.substring(0, (dotPos - 1));
                            String numeroletra = "(" + new numerosletras().convertirLetras(Integer.parseInt(parteentero)) + " " + moneda + " " + partedecimal + "/100 " + terminacion + ")";
                            if (parteentero.equals("1")) {
                                numeroletra = "(UN PESO " + partedecimal + "/100 " + terminacion + ")";
                            }
                            numeroletra = numeroletra.toUpperCase();

                            //Guarda registros
                            senSQL = "INSERT INTO facturas(factura_serie, factura, serie, fecha, estatus, id_folios, id_moneda, id_clientes, id_lugaremision, id_impuestos, formadepago, ordencompra, valortipocambio, subtotal, descuento, fletes, iva, ieps, ivaretenido, isrretenido, total, importeletra, pagada, usuario, observaciones,id_formapago,cuentapago) VALUES ('"
                                    + factura_serie + "', '" + folio + "', '" + serie + "', '" + fechainsertarhora.format(fechacreado) + "', '1', '" + id_folio + "', '" + id_moneda + "', '" + clave_cliente + "', '" + id_lugar + "', '" + id_impuestos + "', 'Pago en una sola exhibición', '" + oc + "', '" + valormoneda + "', '" + fijo2decimales.format(importe) + "', '" + fijo2decimales.format(descuento) + "', '" + fijo2decimales.format(fletes) + "', '" + fijo2decimales.format(iva) + "', '0', '" + fijo2decimales.format(retiva) + "', '" + fijo2decimales.format(retisr) + "', '" + fijo2decimales.format(total) + "', '" + numeroletra + "', 'FALSE', '" + user + "', '" + notas + "', '" + id_formapago + "', '" + ctapago + "');";
                            conexion.modificamov_p(senSQL, conn, valor_privilegio);
                            senSQL = "INSERT INTO docxcob(factura_serie, fechaemision, fechavencimiento, importe, estatus, id_clientes) VALUES ('"
                                    + factura_serie + "', '" + fechainsertarhora.format(fechacreado) + "', '" + fechainsertarhora.format(fechaven) + "', '" + fijo2decimales.format(total) + "', 'Act', '" + clave_cliente + "');";
                            conexion.modificamov_p(senSQL, conn, valor_privilegio);

                            //Actualiza folios ultimo registro
                            senSQL = "UPDATE folios SET folioactual='" + (Integer.parseInt(folio) + 1) + "' WHERE id_folio='" + id_folio + "';";
                            conexion.modificamov_p(senSQL, conn, valor_privilegio);

                        }
                        /****************************************** FIN Nueva Factura *****************************************/

                        //Reinicio de variable temporal
                        if (!factura_serie_ant.equals(p_factura_serie)) {
                            fac_i = 0;
                            partidas = new String[50][7];
                            factura_serie_ant = p_factura_serie;
                        }
                        partidas[fac_i][0] = p_serie;
                        partidas[fac_i][1] = p_folio;
                        partidas[fac_i][2] = p_factura_serie;
                        partidas[fac_i][3] = p_fecha;
                        partidas[fac_i][4] = p_remision;
                        partidas[fac_i][5] = p_cantidad;
                        partidas[fac_i][6] = p_um;
                        fac_i++;

                    }
                    /******************************************** FIN Recorrer archivo por lineas *******************************************/

                    /************************************* Creamos ultima fila ******************************************/
                    int j = 0;
                    String senSQL = "";

                    //Variables cabecera de factura
                    String fecha = "";
                    String clave_cliente = "";
                    String serie = "";
                    String folio = "";
                    String factura_serie = "";
                    Double subtotal_general = 0.0;
                    Double fletes = 0.0;
                    Double descuento = 0.0;
                    Double iva = 0.0;
                    Double retiva = 0.0;
                    Double retisr = 0.0;

                    for (j = 0; j < partidas.length; j++) { //Recorer la variable temporal para detalles de factura

                        if (partidas[j][2] != null) { //Validamos que la fecha no este vacia

                            //Variables de encabezado
                            serie = partidas[j][0];
                            folio = partidas[j][1];
                            factura_serie = partidas[j][2];
                            fecha = partidas[j][3];

                            //Varibales por cada partida
                            String remision = partidas[j][4];
                            Double cantidad = Double.parseDouble(partidas[j][5]);
                            String um = partidas[j][6]; //unidad de medida
                            Double pu = 0.0; //precio unitario
                            String id_op = ""; //op
                            String clave_articulo = ""; //clave articulo
                            String descripcion_articulo = ""; //descripcion articulo
                            if (um.toUpperCase().equals("MILLAR")) { //para el caso de millar hace ajustes de cantidad
                                //cantidad = cantidad*1000;
                                //pu = pu/1000;
                            }

                            //Consultamos la remision para actualizar datos
                            senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,remisiones.id_clientes,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + remision + "' AND remisiones.facturado='false') ORDER BY remisiones.fechahora;";
                            ResultSet remision_info = conexion.consulta(senSQL, conn);
                            if (remision_info.next()) {
                                clave_cliente = remision_info.getString("id_clientes"); //Variable de encabezado
                                pu = remision_info.getDouble("preciounitario");
                                id_op = remision_info.getString("id_op");
                                clave_articulo = remision_info.getString("clavearticulo");
                                descripcion_articulo = remision_info.getString("articulo");
                            }
                            if (remision_info != null) {
                                remision_info.close();
                            }

                            //CalculaSubtotales
                            Double subtotal = cantidad * pu;
                            subtotal_general += subtotal;

                            //Guarda la partida
                            senSQL = "INSERT INTO facturas_detalle(factura_serie, estatus, factura, serie, remision, id_op, clavearticulo, descripcion, cantidad, um, preciounitario, subtotal) VALUES ('" + factura_serie + "', '1', '" + folio + "', '" + serie + "', '" + remision + "', '" + id_op + "', '" + clave_articulo + "', '" + descripcion_articulo + "', '" + cantidad + "', '" + um + "', '" + pu + "', '" + subtotal + "');";
                            conexion.modificamov_p(senSQL, conn, valor_privilegio);

                            //Actualiza remision como facturada si esta completa
                            senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + remision + "');";
                            remision_info = conexion.consulta(senSQL, conn);
                            if (remision_info.next()) {
                                Double cantidadporrem = remision_info.getDouble("cantrempendiente");
                                if (cantidadporrem <= 0.0) {
                                    senSQL = "UPDATE remisiones SET facturado='TRUE' WHERE remision='" + remision + "';";
                                    conexion.modificamov_p(senSQL, conn, valor_privilegio);
                                }
                            }
                            if (remision_info != null) {
                                remision_info.close();
                            }
                        }
                    }//FIN Detalles de factura

                    //Partida detalle adicional por fletes
                    if (fletes > 0.0) {
                        senSQL = "INSERT INTO facturas_detalle(factura_serie, estatus, factura, serie, remision, id_op, clavearticulo, descripcion, cantidad, um, preciounitario, subtotal) VALUES ('" + factura_serie + "', '1', '" + folio + "', '" + serie + "', '0001', '0', 'FLT', 'FLETE FECHA " + fechainsertar.format(new Date()) + "', '1', 'ser','" + fletes + "','" + fletes + "');";
                        conexion.modificamov_p(senSQL, conn, valor_privilegio);
                    }

                    //Variables cabecera de factura
                    int diasc = 0;

                    //Datos del cliente
                    senSQL = "SELECT clientes.* FROM clientes WHERE id_clientes='" + clave_cliente + "' ";
                    ResultSet customer_info = conexion.consulta(senSQL, conn);
                    if (customer_info.next()) {
                        diasc = customer_info.getInt("dias");
                    }


                    //Variables cabecera de factura
                    Date fechacreado = fechahora_importar.parse(fecha); //Fecha factura
                    if (diasc > 0) {
                        calendarvencimiento.add(Calendar.DATE, diasc); //Fecha vencimiento
                    }
                    Date fechaven = calendarvencimiento.getTime(); //Fecha vencimiento
                    int id_folio = 6; //Creado para los folios 1A que seran del SAE y para el ENNOVI 1F
                    String oc = ""; //Este lo obtenemos de todas las op de las remisiones
                    int id_lugar = 1; //Lugar de emision
                    int id_moneda = 1; //Es para MXN pero hay que revisar si todo sera en pesos
                    Double valormoneda = 1.0;
                    String moneda = "PESOS";
                    String terminacion = "M.N.";
                    senSQL = "SELECT * FROM monedas WHERE id_moneda='" + id_moneda + "'";
                    ResultSet moneda_info = conexion.consulta(senSQL, conn);
                    if (moneda_info.next()) { //Actualiza informacion de moneda
                        valormoneda = moneda_info.getDouble("valor");
                        moneda = moneda_info.getString("descripcion");
                        terminacion = moneda_info.getString("terminacion");
                    }
                    int id_impuestos = 1; // Esta la tasa de impuestos normal de IVA
                    Double tasaiva = 0.0;
                    Double tasaretiva = 0.0;
                    Double tasaretisr = 0.0;
                    senSQL = "SELECT * FROM impuestos WHERE id_impuestos='" + id_impuestos + "'";
                    ResultSet impuesto_info = conexion.consulta(senSQL, conn);
                    if (impuesto_info.next()) { //Actualiza informacion de impuestos
                        tasaiva = impuesto_info.getDouble("iva");
                        tasaretiva = impuesto_info.getDouble("ivaretenido");
                        tasaretisr = impuesto_info.getDouble("isrretenido");
                    }
                    String notas = ""; //Notas de la factura
                    int id_formapago = 7; // Lo dejamos como no identificado
                    String ctapago = ""; // Cuenta de pago vacio


                    //Calculos finales
                    Double importe = subtotal_general + fletes - descuento;
                    iva = (tasaiva / 100) * importe;
                    retiva = (tasaretiva / 100) * importe;
                    retisr = (tasaretisr / 100) * importe;
                    Double total = importe + iva - retiva - retisr;
                    //Importe con letra
                    String total_str = fijo2decimales.format(total);
                    int dotPos = total_str.lastIndexOf(".") + 1;
                    String partedecimal = total_str.substring(dotPos);
                    String parteentero = total_str.substring(0, (dotPos - 1));
                    String numeroletra = "(" + new numerosletras().convertirLetras(Integer.parseInt(parteentero)) + " " + moneda + " " + partedecimal + "/100 " + terminacion + ")";
                    if (parteentero.equals("1")) {
                        numeroletra = "(UN PESO " + partedecimal + "/100 " + terminacion + ")";
                    }
                    numeroletra = numeroletra.toUpperCase();

                    //Guarda registros
                    senSQL = "INSERT INTO facturas(factura_serie, factura, serie, fecha, estatus, id_folios, id_moneda, id_clientes, id_lugaremision, id_impuestos, formadepago, ordencompra, valortipocambio, subtotal, descuento, fletes, iva, ieps, ivaretenido, isrretenido, total, importeletra, pagada, usuario, observaciones,id_formapago,cuentapago) VALUES ('"
                            + factura_serie + "', '" + folio + "', '" + serie + "', '" + fechainsertarhora.format(fechacreado) + "', '1', '" + id_folio + "', '" + id_moneda + "', '" + clave_cliente + "', '" + id_lugar + "', '" + id_impuestos + "', 'Pago en una sola exhibición', '" + oc + "', '" + valormoneda + "', '" + fijo2decimales.format(importe) + "', '" + fijo2decimales.format(descuento) + "', '" + fijo2decimales.format(fletes) + "', '" + fijo2decimales.format(iva) + "', '0', '" + fijo2decimales.format(retiva) + "', '" + fijo2decimales.format(retisr) + "', '" + fijo2decimales.format(total) + "', '" + numeroletra + "', 'FALSE', '" + user + "', '" + notas + "', '" + id_formapago + "', '" + ctapago + "');";
                    conexion.modificamov_p(senSQL, conn, valor_privilegio);
                    senSQL = "INSERT INTO docxcob(factura_serie, fechaemision, fechavencimiento, importe, estatus, id_clientes) VALUES ('"
                            + factura_serie + "', '" + fechainsertarhora.format(fechacreado) + "', '" + fechainsertarhora.format(fechaven) + "', '" + fijo2decimales.format(total) + "', 'Act', '" + clave_cliente + "');";
                    conexion.modificamov_p(senSQL, conn, valor_privilegio);

                    //Actualiza folios ultimo registro
                    senSQL = "UPDATE folios SET folioactual='" + (Integer.parseInt(folio) + 1) + "' WHERE id_folio='" + id_folio + "';";
                    conexion.modificamov_p(senSQL, conn, valor_privilegio);

                    /************************************* FIN Creamos ultima fila **************************************/


                    JOptionPane.showMessageDialog(this, "IMPORTACION DE DATOS CORRECTAMENTE", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);

                    //Cargar tabla nuevamente
                    limpiatabla();
                    datos();

                } //Final de si valida

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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
        menueliminar = new javax.swing.JMenuItem();
        file_load_xml = new javax.swing.JFileChooser();
        file_load_sae = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnexportar = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btn_all_xml = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();

        menupop.setMinimumSize(new java.awt.Dimension(105, 28));
        menupop.setName("menupop"); // NOI18N
        menupop.setPreferredSize(new java.awt.Dimension(130, 34));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(facturas.class);
        menueliminar.setIcon(resourceMap.getIcon("menueliminar.icon")); // NOI18N
        menueliminar.setText(resourceMap.getString("menueliminar.text")); // NOI18N
        menueliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menueliminarActionPerformed(evt);
            }
        });
        menupop.add(menueliminar);

        file_load_xml.setDialogTitle(resourceMap.getString("file_load_xml.dialogTitle")); // NOI18N
        file_load_xml.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        file_load_xml.setName("file_load_xml"); // NOI18N

        file_load_sae.setName("file_load_sae"); // NOI18N

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
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Factura", "Fecha", "Cliente", "Moneda", "Iva", "Total", "Var", "OP","Remisión","Folio Fiscal","OC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.util.Date.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false,false,false,false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladatos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos.setName("Tabladatos"); // NOI18N
        Tabladatos.setRowHeight(22);
        Tabladatos.getTableHeader().setReorderingAllowed(false);
        Tabladatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabladatosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        jToolBar1.setBackground(resourceMap.getColor("jToolBar1.background")); // NOI18N
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setToolTipText(resourceMap.getString("jButton1.toolTipText")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton1.setMaximumSize(new java.awt.Dimension(80, 48));
        jButton1.setMinimumSize(new java.awt.Dimension(80, 48));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(80, 48));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton4.setMaximumSize(new java.awt.Dimension(80, 48));
        jButton4.setMinimumSize(new java.awt.Dimension(80, 48));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(80, 48));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar1.add(jSeparator5);

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setToolTipText(resourceMap.getString("jButton5.toolTipText")); // NOI18N
        jButton5.setEnabled(false);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton5.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton5.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton5.setName("jButton5"); // NOI18N
        jButton5.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jSeparator8.setName("jSeparator8"); // NOI18N
        jToolBar1.add(jSeparator8);

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setToolTipText(resourceMap.getString("jButton2.toolTipText")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton2.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton2.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        btnexportar.setIcon(resourceMap.getIcon("btnexportar.icon")); // NOI18N
        btnexportar.setText(resourceMap.getString("btnexportar.text")); // NOI18N
        btnexportar.setFocusable(false);
        btnexportar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnexportar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnexportar.setMaximumSize(new java.awt.Dimension(70, 48));
        btnexportar.setMinimumSize(new java.awt.Dimension(70, 48));
        btnexportar.setName("btnexportar"); // NOI18N
        btnexportar.setPreferredSize(new java.awt.Dimension(70, 48));
        btnexportar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnexportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnexportar);

        jSeparator7.setName("jSeparator7"); // NOI18N
        jToolBar1.add(jSeparator7);

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton3.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton3.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

        btn_all_xml.setIcon(resourceMap.getIcon("btn_all_xml.icon")); // NOI18N
        btn_all_xml.setText(resourceMap.getString("btn_all_xml.text")); // NOI18N
        btn_all_xml.setFocusable(false);
        btn_all_xml.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_all_xml.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btn_all_xml.setMaximumSize(new java.awt.Dimension(70, 48));
        btn_all_xml.setMinimumSize(new java.awt.Dimension(70, 48));
        btn_all_xml.setName("btn_all_xml"); // NOI18N
        btn_all_xml.setPreferredSize(new java.awt.Dimension(70, 48));
        btn_all_xml.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_all_xml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_all_xmlActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_all_xml);

        jSeparator6.setName("jSeparator6"); // NOI18N
        jToolBar1.add(jSeparator6);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(556, 48));

        buscar.setText(resourceMap.getString("buscar.text")); // NOI18N
        buscar.setToolTipText(resourceMap.getString("buscar.toolTipText")); // NOI18N
        buscar.setName("buscar"); // NOI18N
        buscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarFocusGained(evt);
            }
        });
        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jToolBar1.add(jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N

        noregistros.setFont(resourceMap.getFont("noregistros.font")); // NOI18N
        noregistros.setForeground(resourceMap.getColor("noregistros.foreground")); // NOI18N
        noregistros.setText(resourceMap.getString("noregistros.text")); // NOI18N
        noregistros.setName("noregistros"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noregistros)
                .addContainerGap(822, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(noregistros)
                .addContainerGap(2, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        // TODO add your handling code here:
        if (buscar.getText().equals("")) {
            Tabladatos.setRowSorter(null);
            buscar.setText("");
            Tabladatos.setAutoCreateRowSorter(true);
            limpiatabla();
            datos();
        } else {
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)" + buscar.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladatos.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscarKeyReleased

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            modificar();
        }
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            // get the coordinates of the mouse click
            Point p = evt.getPoint();
            // get the row index that contains that coordinate
            int rowNumber = Tabladatos.rowAtPoint(p);
            // variable for the beginning and end selects only that one row.
            Tabladatos.changeSelection(rowNumber, 0, false, false);
            menupop.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        datos_facturas = new datos_facturas(null, true, conn, "", valor_privilegio);
        datos_facturas.setLocationRelativeTo(this);
        datos_facturas.setVisible(true);
        limpiatabla();
        datos_facturas = null;
        datos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
    }//GEN-LAST:event_buscarFocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        busca_fechas busca_fechas = new busca_fechas(null, true);
        busca_fechas.setLocationRelativeTo(this);
        busca_fechas.setVisible(true);
        String estado = busca_fechas.getEstado();
        /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if (estado.equals("cancelar")) {
        } else {
            limpiatabla();
            rs0 = null;
            try {
               
                String senSQL = "SELECT MAX(facturas.estatus) as estatus,MAX(facturas.iva) as iva,MAX(facturas.total) as total, MAX(facturas.factura_serie) as factura_serie,MAX(facturas.fecha) as fecha,MAX(facturas.varios) as varios,MAX(clientes.nombre) as nombre,MAX(monedas.descripcion) as tipomoneda,MAX(xmlfinal.uuid) as uuid,array_to_string(array_agg(facturas_detalle.id_op), ',') AS ops,array_to_string(array_agg(facturas_detalle.remision), ',') AS remisiones  FROM ((((( (facturas LEFT JOIN xmlfinal ON facturas.factura_serie=xmlfinal.factura_serie) LEFT JOIN folios ON facturas.id_folios=folios.id_folio) LEFT JOIN monedas ON facturas.id_moneda=monedas.id_moneda) LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes) LEFT JOIN lugaresemision ON facturas.id_lugaremision=lugaresemision.id_lugaremision) LEFT JOIN impuestos ON facturas.id_impuestos=impuestos.id_impuestos) INNER JOIN (facturas_detalle LEFT JOIN remisiones ON facturas_detalle.remision=remisiones.remision ) ON facturas.factura_serie=facturas_detalle.factura_serie WHERE ((facturas.fecha>='" + fechainsertar.format(busca_fechas.getFechaini()) + " 00:00:00' AND facturas.fecha<='" + fechainsertar.format(busca_fechas.getFechafin()) + " 23:59:59')) GROUP BY facturas.factura_serie ORDER BY MAX(facturas.fecha);";
                rs0 = conexion.consulta(senSQL, conn);
                while (rs0.next()) {
                    String est = "" + rs0.getString("estatus");
                    String con = "";
                    Double ivaf = rs0.getDouble("iva");
                    Double totalf = rs0.getDouble("total");
                    if (est.equals("0")) {
                        con = " ( CANCELADA )";
                        ivaf = 0.0;
                        totalf = 0.0;
                    }
                    Object datos[] = {rs0.getString("factura_serie"), rs0.getDate("fecha"), rs0.getString("nombre") + con, rs0.getString("tipomoneda"), ivaf, totalf, rs0.getString("varios"),rs0.getString("ops"),rs0.getString("remisiones"),rs0.getString("uuid")};
                    modelot1.addRow(datos);
                }
                
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }


        }
        busca_fechas = null;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void menueliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menueliminarActionPerformed
        // TODO add your handling code here:
        boolean con_inter=CFDIconexion.valida_con_internet();
        if(!con_inter){
            JOptionPane.showMessageDialog(null, "NO SE PUEDE CONECTAR A INTERNET ", "E R R O R !!!!", JOptionPane.WARNING_MESSAGE);
        }

        int filano = Tabladatos.getSelectedRow();
        if (con_inter && JOptionPane.showConfirmDialog(this, "ESTA SEGURO QUE DESEA CANCELAR!!", " I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Timestamp fechaemi = null;
            int errorremision = 3;

            rs1 = null;
            try {
                String senSQL = "SELECT docxcob.factura_serie,docxcob.fechaemision,docxcob.importe,COALESCE(pagosreal.pagosimporte,0) as pagosimporte FROM docxcob LEFT JOIN (SELECT factura_serie,sum(importe) as pagosimporte FROM pagos_detalle WHERE estatus='Act' GROUP BY factura_serie) as pagosreal ON docxcob.factura_serie=pagosreal.factura_serie WHERE (estatus='Act' AND docxcob.factura_serie='" + Tabladatos.getValueAt(filano, 0) + "');";
                rs1 = conexion.consulta(senSQL, conn);
                if (rs1.next()) {
                    errorremision = 0;
                    Double pagosimporte = rs1.getDouble("pagosimporte");
                    if (pagosimporte > 0.0) {
                        errorremision = 1;
                    }
                    fechaemi = rs1.getTimestamp("fechaemision");

                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            rs1 = null;
            try {
                String senSQL = "SELECT notas_credito_detalle.* FROM notas_credito_detalle WHERE (notas_credito_detalle.estatus='1' AND notas_credito_detalle.factura_serie='" + Tabladatos.getValueAt(filano, 0) + "');";
                rs1 = conexion.consulta(senSQL, conn);
                if (rs1.next()) {
                    errorremision = 2;
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }


            if (errorremision == 1 || errorremision == 2) {
                String err = "ERROR GENERAL";
                if (errorremision == 1) {
                    err = "LA FACTURA YA TIENE PAGOS NO SE PUEDE CANCELAR";
                }
                if (errorremision == 2) {
                    err = "LA FACTURA YA TIENE NOTAS DE CREDITO";
                }
                JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            } else {

                //revisa que aun no se haya enviado el reporte mensual
                int mes = Integer.parseInt(fechames.format(fechaemi));
                int ano = Integer.parseInt(fechaano.format(fechaemi));
                String yaenviado = "";
                //consulata si no ha sido generado
                rs0 = null;
                try {
                    String senSQL = "SELECT * FROM reporte_sat WHERE (ano='" + ano + "' AND mes='" + mes + "')";
                    rs0 = conexion.consulta(senSQL, conn);
                    if (rs0.next()) {
                        yaenviado = "" + rs0.getBoolean("enviado");
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }

                if (yaenviado.toUpperCase().equals("TRUE")) {
                    JOptionPane.showMessageDialog(this, "EL REPORTE YA FUE ENVIADO AL SAT DE ESTA FACTURA\nNO SE PUEDE CANCELAR", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                } else {


                    String motivoelimina = "" + JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE CANCELACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
                    if (!motivoelimina.equals("") && !motivoelimina.equals("null")) {
                        //cancela los documentos
                        String senSQLmov = "UPDATE facturas SET estatus='0' WHERE factura_serie='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
                        conexion.modificamov_p(senSQLmov, conn, valor_privilegio);
                        senSQLmov = "UPDATE facturas_detalle SET estatus='0' WHERE factura_serie='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
                        conexion.modificamov_p(senSQLmov, conn, valor_privilegio);
                        senSQLmov = "UPDATE xmlfinal SET estatus='0' WHERE factura_serie='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
                        conexion.modificamov_p(senSQLmov, conn, valor_privilegio);
                        senSQLmov = "UPDATE docxcob SET estatus='Can' WHERE factura_serie='" + (String) Tabladatos.getValueAt(filano, 0) + "';";
                        conexion.modificamov_p(senSQLmov, conn, valor_privilegio);

                        //consultamos la nota cancelada para cancelarla si tienen tiembre
                        String sql_uuid="SELECT uuid,factura_serie FROM xmlfinal WHERE factura_serie='"+(String) Tabladatos.getValueAt(filano, 0)+"' ";
                        ResultSet rs_uuid=conexion.consulta(sql_uuid,conn);
                        String uuid_c="";
                        String folio_c="";
                        try{
                            if(rs_uuid.next()){
                                uuid_c=""+rs_uuid.getString("uuid");
                                folio_c=""+rs_uuid.getString("factura_serie");
                            }
                        } catch(Exception ex) {
                            JOptionPane.showMessageDialog(this,"ERROR AL CONSULTAR UUID \n"+ ex,"A D V E R T E N C I A !!!!",JOptionPane.ERROR_MESSAGE);
                        }
                        if(!uuid_c.equals("") && !uuid_c.equals("null") && !uuid_c.equals("0")){
                            CFDIconexion.cancela_cfdi_save(uuid_c,folio_c, conn);
                        }



                        //agrega registro a la bitacora
                        try {
                            senSQLmov = "INSERT INTO bitacora(usuario, fecha, descripcion,host) VALUES ('" + conf.getProperty("UserID") + "', '" + fechainsertarhora.format(new Date()) + "', 'Cancela factura: " + (String) Tabladatos.getValueAt(filano, 0) + "','" + java.net.InetAddress.getLocalHost().getCanonicalHostName() + "');";
                            conexion.modificamov_p(senSQLmov, conn, valor_privilegio);
                        } catch (Exception e) {
                        }


                        rs1 = null;
                        try {
                            String senSQL = "SELECT facturas_detalle.* FROM facturas_detalle WHERE factura_serie='" + Tabladatos.getValueAt(filano, 0) + "';";
                            rs1 = conexion.consulta(senSQL, conn);
                            while (rs1.next()) {
                                String rmf = rs1.getString("remision");
                                //actualiza las remsiones como facturada si ya factura todo
                                rs0 = null;
                                try {
                                    senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + rmf + "');";
                                    rs0 = conexion.consulta(senSQL, conn);
                                    if (rs0.next()) {
                                        Double cantidadporrem = rs0.getDouble("cantrempendiente");
                                        if (cantidadporrem > 0.0) {
                                            senSQLmov = "UPDATE remisiones SET facturado='FALSE' WHERE remision='" + rmf + "';";
                                            conexion.modificamov_p(senSQLmov, conn, valor_privilegio);
                                        }
                                    }
                                    if (rs0 != null) {
                                        rs0.close();
                                    }
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                                }

                            }
                            if (rs1 != null) {
                                rs1.close();
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                        }

                        //registra la cancelacion
                        senSQLmov = "INSERT INTO facturas_cancela(factura_serie, fecha_cancela, motivo, usuario) VALUES ('" + (String) Tabladatos.getValueAt(filano, 0) + "', '" + fechainsertarhora.format(new Date()) + "', '" + motivoelimina + "', '" + usuariorem + "');";
                        conexion.modificaupdate_p(senSQLmov, conn, valor_privilegio);
                    }

                }//final del else de reporte ya enviado al sat

            }

            limpiatabla();
            datos();
        }
    }//GEN-LAST:event_menueliminarActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int filano = Tabladatos.getSelectedRow();
        if (filano < 0) {
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            String fac_ser = "" + Tabladatos.getValueAt(filano, 0);
            busca_CFD busca_CFD = new busca_CFD(null, true, conn, fac_ser, "fac");
            busca_CFD.setLocationRelativeTo(this);
            busca_CFD.setVisible(true);
            String estado = busca_CFD.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {
            }
            busca_CFD = null;

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        datos_facturas_varios = new datos_facturas_varios(null, true, conn, "", valor_privilegio);
        datos_facturas_varios.setLocationRelativeTo(this);
        datos_facturas_varios.setVisible(true);
        limpiatabla();
        datos_facturas_varios = null;
        datos();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btn_all_xmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_all_xmlActionPerformed
        // TODO add your handling code here:
        String path_xml="";
        FileWriter fWriter;
        BufferedWriter bWriter;
        
        try{

            busca_fechas busca_fechas = new busca_fechas(null, true);
            busca_fechas.setLocationRelativeTo(this);
            busca_fechas.setVisible(true);
            String estado = busca_fechas.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {

                //leemos el path del XML de propiedades
                ini = new FileInputStream(homeE+"/appE.xml");
                defaultProps.loadFromXML(ini);
                ini.close();
                path_xml=defaultProps.getProperty("Rutaxml");

                //creamos la consulta
                rs0 = null;
                try {
                    String senSQL = "SELECT facturas.*,clientes.nombre,monedas.descripcion as tipomoneda,xmlfinal.xmlfinal,xmlfinal.uuid,docxcob.fechavencimiento FROM ( ( (facturas LEFT JOIN docxcob ON facturas.factura_serie=docxcob.factura_serie) LEFT JOIN xmlfinal ON facturas.factura_serie=xmlfinal.factura_serie) LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes) LEFT JOIN monedas ON facturas.id_moneda=monedas.id_moneda WHERE ((facturas.fecha>='" + fechainsertar.format(busca_fechas.getFechaini()) + " 00:00:00' AND facturas.fecha<='" + fechainsertar.format(busca_fechas.getFechafin()) + " 23:59:59')) AND facturas.estatus=1 ORDER BY facturas.id_clientes;";
                    rs0 = conexion.consulta(senSQL, conn);
                    while (rs0.next()) {

                        String cliente=rs0.getString("nombre");
                        cliente = cliente.replace(" ", "_");
                        cliente = cliente.replace(",", "");
                        cliente = cliente.replace(".", "");
                        Date fecha_factura=rs0.getDate("fecha");
                        String factura_serie=rs0.getString("factura_serie");
                        String foler_periodno=fechamesano.format(fecha_factura);
                        String path_new=path_xml+"/"+cliente+"/"+foler_periodno;
                        File folder_new = new File(path_new);
                        if(!folder_new.exists()){ //si no existe el folder creamos uno nuevo
                            folder_new.mkdirs();
                        }
                        //creamos el archivo
                        fWriter = new FileWriter(new File(path_new+"/"+factura_serie+"_" + fechasin.format(fecha_factura)+".xml"));
                        bWriter = new BufferedWriter(fWriter);
                        bWriter.write(new String(rs0.getBytes("xmlfinal")));
                        bWriter.close();
                        fWriter.close();

                        fechavencimiento = rs0.getTimestamp("fechavencimiento");
                        varios = rs0.getString("varios");
                        if (rs0.getString("estatus").equals("0")) {
                            estatusfacx = " ( CANCELADA )";
                        }

                        creaPDF33(path_new+"/"+factura_serie+"_" + fechasin.format(fecha_factura)+".pdf", path_new+"/"+factura_serie+"_" + fechasin.format(fecha_factura)+".xml", "",rs0.getString("uuid") ,factura_serie);
                        //creaPDF32(path_new+"/"+factura_serie+"_" + fechasin.format(fecha_factura)+".pdf", path_new+"/"+factura_serie+"_" + fechasin.format(fecha_factura)+".xml", "",rs0.getString("uuid") ,factura_serie);

                    }
                    if (rs0 != null) {
                        rs0.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                }
                
                JOptionPane.showMessageDialog(this, "GENERACIÓN DE XML CORRECTA", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);

            }

        }catch(FileNotFoundException ex){ JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
        catch(IOException ex){ JOptionPane.showMessageDialog(null,"No se encuentra archivo de propiedades:\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);  }
    }//GEN-LAST:event_btn_all_xmlActionPerformed

    private void btnexportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexportarActionPerformed
        // TODO add your handling code here:
        File rutaarchivo=new File(System.getProperty("user.home")+"/facturas.xls");
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
            sheet.addCell(new jxl.write.Label(0, filainicial, ""+this.getTitle(),arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for(int j=0;j<(Tabladatos.getColumnCount());j=j+1){
                String titu=""+Tabladatos.getColumnName(j);
                int dotPos = titu.lastIndexOf(">")+1;//le quita el html de los titulos
                if(titu.contains("<html>")){
                    titu=titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu,arial10ftitulo));
                sheet.setColumnView( j, ((Tabladatos.getColumnModel().getColumn(j).getPreferredWidth())/6) );
            }

            filainicial++;//incrementa las filas
            for(int i=0;i<(Tabladatos.getRowCount());i=i+1){
                for(int j=0;j<(Tabladatos.getColumnCount());j=j+1){
                    if(Tabladatos.getValueAt(i, j) != null){
                        if(Tabladatos.getValueAt(i, j) instanceof String){
                            String dato=(String)Tabladatos.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">")+1;//le quita el html de los titulos
                            if(dato.contains("<html>")){
                                dato=dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i+filainicial, dato,arial10fdetalle));
                        }
                        else if(Tabladatos.getValueAt(i, j) instanceof java.lang.Number)
                            sheet.addCell(new jxl.write.Number(j, i+filainicial, Double.parseDouble(Tabladatos.getValueAt(i, j).toString()),arial10fdetalle));
                        else if(Tabladatos.getValueAt(i, j) instanceof java.util.Date)
                            sheet.addCell(new jxl.write.DateTime(j, i+filainicial, (java.util.Date)Tabladatos.getValueAt(i, j), jxl.write.DateTime.GMT));
                        else
                            sheet.addCell(new jxl.write.Boolean(j, i+filainicial,(java.lang.Boolean)Tabladatos.getValueAt(i, j),arial10fdetalle));
                    }
                }
            }/**fin de revisar los campos vacios*/


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
    }//GEN-LAST:event_btnexportarActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        importar_facturas_csv();
        importar_xml_cfdi();

    }//GEN-LAST:event_jButton5ActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btn_all_xml;
    private javax.swing.JButton btnexportar;
    private javax.swing.JTextField buscar;
    private javax.swing.JFileChooser file_load_sae;
    private javax.swing.JFileChooser file_load_xml;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem menueliminar;
    private javax.swing.JPopupMenu menupop;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables
    private JDialog datos_facturas;
    private JDialog datos_facturas_varios;
}
