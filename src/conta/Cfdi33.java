package conta;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cfdi.exceptions.InconsistentDataException;
import com.cfdi.exceptions.LbsException;
import com.cfdi.exceptions.myXmlParsingException;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;

import com.lbs.webservices.lizbet.timbrado.LbsWsTimbrado;
import com.lbs.webservices.lizbet.timbrado.LbsWsTimbradoService;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

/**
 * Representa un documento tipo CFDI
 *
 *
 */
public class Cfdi33 {

    NumberFormat cfdiNumFormat = NumberFormat.getNumberInstance();
    NumberFormat cfdiNumFormat_cant = NumberFormat.getNumberInstance();
    
    private Integer decimales;
    protected SimpleDateFormat dformat;
    protected static String version = "3.3";
    protected Document xmlDoc = null;
    protected Element nodoComprobante;
    protected Element nodoRelacionados;
    protected Element nodoEmisor;
    protected Element nodoReceptor;
    protected Element nodoConceptos;
    protected String domainPrefix = "cfdi:";
    private String leyenda = "Este documento es una representacion impresa de un CFDI.";
    protected String mes, ano, dia;  //Se usan para el programa de descargas masivas
    LibPdfBuilder pdf;
    DecimalFormat numformatImpuestos = new DecimalFormat("###0.000000");

    private static DocumentBuilderFactory dbf;
    private static DocumentBuilder db;

    static {
        dbf = DocumentBuilderFactory.newInstance();
    }

    //Listas de datos
    protected List<PartidaCfdi> partidas = new ArrayList<PartidaCfdi>();
    protected List<ImpuestoCfdi> impuestos = new ArrayList<ImpuestoCfdi>();

    /**
     * Inicia el DOM con la estructura obligatoria de un CFDI 3.3 Tambien inicia
     * otras variables utilizadas
     */
    public Cfdi33(Integer decimales) {
        this.decimales = decimales;
        //Using factory get an instance of document builder
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            xmlDoc = db.newDocument();
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
        //Nodo Comprobante
        nodoComprobante = xmlDoc.createElement(domainPrefix + "Comprobante");
        xmlDoc.appendChild(nodoComprobante);
        nodoComprobante.setAttribute("xmlns:cfdi", "http://www.sat.gob.mx/cfd/3");
        nodoComprobante.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        nodoComprobante.setAttribute("xsi:schemaLocation", "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd");
        nodoComprobante.setAttribute("Version", version);

        //Creamos los nodos que son obligatorios
        nodoEmisor = xmlDoc.createElement(domainPrefix + "Emisor");
        nodoComprobante.appendChild(nodoEmisor);
        nodoReceptor = xmlDoc.createElement(domainPrefix + "Receptor");
        nodoComprobante.appendChild(nodoReceptor);
        nodoConceptos = xmlDoc.createElement(domainPrefix + "Conceptos");
        nodoComprobante.appendChild(nodoConceptos);

        //Formateador de fecha para estandard del SAT
        dformat = (SimpleDateFormat) DateFormat.getDateInstance();
        dformat.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
        dformat.applyPattern("yyyy-MM-dd'T'HH:mm:ss");

        //Formateador de decimales
        cfdiNumFormat.setMinimumFractionDigits(decimales);
        cfdiNumFormat.setMaximumFractionDigits(decimales);
        cfdiNumFormat.setGroupingUsed(false);
        
        cfdiNumFormat_cant.setMinimumFractionDigits(4);
        cfdiNumFormat_cant.setMaximumFractionDigits(4);
        cfdiNumFormat_cant.setGroupingUsed(false);
    }

    /**
     * Construimos un CFDI en base a una cadena de texto en formato arreglo de
     * bytes
     *
     */
    public Cfdi33(byte[] data) throws myXmlParsingException {
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream ins = new ByteArrayInputStream(data);
        try {
            xmlDoc = db.parse(ins);
            nodoComprobante = xmlDoc.getDocumentElement();
            domainPrefix = "cfdi:";
            parseFecha();
        } catch (SAXException e) {
            throw new myXmlParsingException(e);
        } catch (IOException e) {
            throw new myXmlParsingException(e);
        }
    }

    //Se reconstrulle en base a data (Para parsear la respuesta del PAC del CFDI timbrado)
    public void parse(byte[] data) throws myXmlParsingException {
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream ins = new ByteArrayInputStream(data);
        try {
            xmlDoc = db.parse(ins);
            nodoComprobante = xmlDoc.getDocumentElement();
            domainPrefix = "cfdi:";
            parseFecha();
        } catch (SAXException e) {
            throw new myXmlParsingException(e);
        } catch (IOException e) {
            throw new myXmlParsingException(e);
        }
    }

    private void parseFecha() {
        SimpleDateFormat dfReader = (SimpleDateFormat) DateFormat.getDateInstance();
        dfReader.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dfWriter = (SimpleDateFormat) DateFormat.getDateInstance();
        dfWriter.applyPattern("yyyy");

        try {
            dfWriter.applyPattern("yyyy");
            ano = dfWriter.format(dfReader.parse(getFecha()));
            dfWriter.applyPattern("MM");
            mes = dfWriter.format(dfReader.parse(getFecha()));
            dfWriter.applyPattern("dd");
            dia = dfWriter.format(dfReader.parse(getFecha()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion encargada de eliminar caracteres invalidos para el SAT
     *
     * @param s Cadena a limpiar
     * @return Cadena limpia
     */
    protected String limpiar(String s) {
        return s.replaceAll("\n", " ").replaceAll("\r", " ").replaceAll(" +", " ").replaceAll("\\|", "").trim();
    }

    public String getRfcEmisor() {
        return get("Emisor/Rfc");
    }

    /**
     * Establece el atributo FormaPago del CFDI
     *
     * @param s Clave del catalogo de formas de pago del SAT
     */
    public void setFormaPago(String s) {
        nodoComprobante.setAttribute("FormaPago", limpiar(s));
    }

    /**
     * Establece el atributo Serie del CFDI
     *
     * @param s Serie interna del contribuyente
     */
    public void setSerie(String s) {
        nodoComprobante.setAttribute("Serie", limpiar(s));
    }

    /**
     * Establece el atributo Folio del CFDI
     *
     * @param s Folio interno del contribuyente
     */
    public void setFolio(String s) {
        nodoComprobante.setAttribute("Folio", limpiar(s));
    }

    /**
     * Establece la fecha del CFDI
     *
     * @param s fecha
     */
    public void setFecha(Date s) {
        nodoComprobante.setAttribute("Fecha", dformat.format(s));
    }

    /**
     * Establece el atributo CondicionesDePago del CFDI
     *
     * @param s Clave del catalogo de CondicionesDePago del SAT
     */
    public void setCondicionesDePago(String s) {
        nodoComprobante.setAttribute("CondicionesDePago", limpiar(s));
    }

    /**
     * Establece el atributo Moneda del CFDI
     *
     * @param s Clave del catalogo de Moneda del SAT
     */
    public void setMoneda(String s) {
        nodoComprobante.setAttribute("Moneda", limpiar(s));
    }

    /**
     * Establece el atributo TipoCambio del CFDI
     *
     * @param s Clave del catalogo de Moneda del SAT
     */
    public void setTipoCambio(Double s) {
        //Si el tipo de cambio es uno (para modena MXN se pone sin decimales)
        if (s.equals(1D)) {
            nodoComprobante.setAttribute("TipoCambio", "1");
        } else {
            nodoComprobante.setAttribute("TipoCambio", cfdiNumFormat.format(s));
        }
    }

    /**
     * Establece el atributo MetodoPago del CFDI
     *
     * @param s Clave del catalogo de MetodoPago del SAT
     */
    public void setMetodoPago(String s) {
        nodoComprobante.setAttribute("MetodoPago", limpiar(s));
    }

    /**
     * Establece el atributo LugarExpedicion del CFDI
     *
     * @param s Codigo postal de la direccion de la empresa donde se emite el
     * CFDI
     */
    public void setLugarExpedicion(String s) {
        nodoComprobante.setAttribute("LugarExpedicion", limpiar(s));
    }

    /**
     * Establece el atributo TipoRelacion del CFDI
     *
     * @param s Clave del catalogo de TipoRelacion del SAT
     */
    public void setTipoRelacion(String s) {
        if (nodoRelacionados == null) {
            nodoRelacionados = xmlDoc.createElement(domainPrefix + "CfdiRelacionados");
            nodoComprobante.insertBefore(nodoRelacionados, nodoEmisor);
        }
        nodoRelacionados.setAttribute("TipoRelacion", limpiar(s));
    }

    public void addRelacionado(String s) {
        if (nodoRelacionados == null) {
            throw new RuntimeException("Debe agregar primero un TipoRelacion");
        }

        Element nodoRelacionado = xmlDoc.createElement(domainPrefix + "CfdiRelacionado");
        nodoRelacionados.appendChild(nodoRelacionado);
        nodoRelacionado.setAttribute("UUID", limpiar(s));
    }

    public String getFecha() {
        return get("Fecha");
    }

    /**
     * Establece los datos del emisor del CFDI
     *
     * @param nombre Nombre del emisor
     * @param rfc RFC del emisor
     * @param regimen Clave del catalogo de RegimenFiscal del SAT
     */
    public void setEmisor(String nombre, String rfc, String regimen) {
        nodoEmisor.setAttribute("Rfc", limpiar(rfc));
        nodoEmisor.setAttribute("Nombre", limpiar(nombre));
        nodoEmisor.setAttribute("RegimenFiscal", limpiar(regimen));
    }

    /**
     * Establece los datos del receptor (Extranjero) del CFDI
     *
     * @param nombre Nombre del receptor
     * @param rfc RFC del receptor
     * @param uso Clave del catalogo de UsoCFDI del SAT
     */
    public void setReceptor(String nombre, String rfc, String uso) {
        setReceptor(nombre, rfc, uso, null, null);
    }

    /**
     * Establece los datos del receptor (Extranjero) del CFDI
     *
     * @param nombre Nombre del receptor
     * @param rfc RFC del receptor
     * @param uso Clave del catalogo de UsoCFDI del SAT
     * @param residencia Clave del catalogo de ResidenciaFiscal del SAT
     * @param taxid Clave tributaria del cliente en su pais
     */
    public void setReceptor(String nombre, String rfc, String uso, String residencia, String taxid) {
        nodoReceptor.setAttribute("Rfc", limpiar(rfc));
        nodoReceptor.setAttribute("Nombre", limpiar(nombre));
        nodoReceptor.setAttribute("UsoCFDI", limpiar(uso));
        if (residencia != null) {
            nodoReceptor.setAttribute("ResidenciaFiscal", limpiar(residencia));
        }
        if (taxid != null) {
            nodoReceptor.setAttribute("NumRegIdTrib", limpiar(taxid));
        }
    }

    public void agregarPartida(PartidaCfdi p) {
        Element nodoConcepto = xmlDoc.createElement(domainPrefix + "Concepto");
        nodoConceptos.appendChild(nodoConcepto);
        nodoConcepto.setAttribute("Cantidad", cfdiNumFormat_cant.format(p.getCantidad()));
        nodoConcepto.setAttribute("ClaveProdServ", limpiar(p.getCodigoSat()));
        if (p.getCodigo() != null) {
            nodoConcepto.setAttribute("NoIdentificacion", limpiar(p.getCodigo()));
        }
        nodoConcepto.setAttribute("ClaveUnidad", limpiar(p.getMedidaSat()));
        if (p.getMedida() != null) {
            nodoConcepto.setAttribute("Unidad", limpiar(p.getMedida()));
        }
        nodoConcepto.setAttribute("Descripcion", limpiar(p.getDescripcion()));
        nodoConcepto.setAttribute("ValorUnitario", cfdiNumFormat.format(p.getImpUnit()));
        nodoConcepto.setAttribute("Importe", cfdiNumFormat.format(p.getImpTot()));
        if (p.getDescuento() != null && p.getDescuento() > 0) {
            nodoConcepto.setAttribute("Descuento", cfdiNumFormat.format(p.getDescuento()));
        }

        if (p.getImpuestos() != null && p.getImpuestos().size() > 0) {
            Element impuestos = xmlDoc.createElement(domainPrefix + "Impuestos");
            nodoConcepto.appendChild(impuestos);
            Element impRetElement = xmlDoc.createElement(domainPrefix + "Retenciones");
            Element impTrasElement = xmlDoc.createElement(domainPrefix + "Traslados");
            Integer numTrasladados = 0, numRetenidos = 0;

            for (ImpuestoCfdi imp : p.getImpuestos()) {
                if (imp.getTipo() == ImpuestoCfdi.TIPO_RETENCION) {
                    Element retencion = xmlDoc.createElement(domainPrefix + "Retencion");
                    retencion.setAttribute("Base", cfdiNumFormat.format(imp.getBase()));
                    retencion.setAttribute("Impuesto", imp.getImpuesto());
                    retencion.setAttribute("TipoFactor", imp.getTipoFactor());
                    retencion.setAttribute("TasaOCuota", numformatImpuestos.format(imp.getTasaCuota()));
                    retencion.setAttribute("Importe", cfdiNumFormat.format(imp.getImporte()));
                    impRetElement.appendChild(retencion);
                    numRetenidos++;
                } else {
                    Element traslado = this.xmlDoc.createElement(domainPrefix + "Traslado");
                    traslado.setAttribute("Base", cfdiNumFormat.format(imp.getBase()));
                    traslado.setAttribute("Impuesto", imp.getImpuesto());
                    traslado.setAttribute("TipoFactor", imp.getTipoFactor());
                    traslado.setAttribute("TasaOCuota", numformatImpuestos.format(imp.getTasaCuota()));
                    traslado.setAttribute("Importe", cfdiNumFormat.format(imp.getImporte()));
                    impTrasElement.appendChild(traslado);
                    numTrasladados++;
                }
            }
            if (numTrasladados > 0) {
                impuestos.appendChild(impTrasElement);
            }
            if (numRetenidos > 0) {
                impuestos.appendChild(impRetElement);
            }
        }

        partidas.add(p);
        for (ImpuestoCfdi i : p.getImpuestos()) {
            addImpuesto(i);
        }
    }

    private void addImpuesto(ImpuestoCfdi imp) {
        boolean finded = false;
        for (ImpuestoCfdi i : impuestos) {
            if (i.getImpuesto().equals(imp.getImpuesto()) && i.getTipo().equals(imp.getTipo())) {
                if (i.getTasaCuota().equals(imp.getTasaCuota())) {
                    i.setImporte(i.getImporte() + imp.getImporte());
                    finded = true;
                }
            }
        }

        if (!finded) {
            impuestos.add(imp);
        }

    }

    /**
     * Se encarga de sellar el documento, crear la cadena original y establecer
     * el No. de certificado
     *
     * @param crypt Objeto creado a partir del Certificado de Sello Digital, con
     * el cual Se sellaran las facturas
     * @throws Exception
     *
     */
    public void sellar(LbsCrypt crypt) throws Exception {
        calcTotales();
        nodoComprobante.setAttribute("NoCertificado", crypt.getCertNo());
        nodoComprobante.setAttribute("Certificado", crypt.getB64Certificado());

        //Para que no mande wuarnings al parsear los xslt
        final PrintStream err = new PrintStream(System.err);
        System.setErr(new NullOutputStream("/dev/null"));
        String sello;
        try {
            sello = crypt.sellarCfdi33(getCadenaOriginal().getBytes("UTF8"));
        } catch (Exception e) {
            System.setErr(err);
            throw e;
        }
        System.setErr(err);
        nodoComprobante.setAttribute("Sello", sello);
    }

    public String getCadenaOriginal() throws InconsistentDataException {
        String result = null;

        ByteArrayOutputStream cadena = new ByteArrayOutputStream();
        String file;
        file = nodoComprobante.getAttribute("Version") + ".xslt";

        TransformerFactory tFactory = TransformerFactory.newInstance();
        try {

            ByteArrayInputStream input = new ByteArrayInputStream(toString().getBytes("UTF8"));
            Transformer transformer
                    = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(file));
            transformer.transform(new javax.xml.transform.stream.StreamSource(input),
                    new javax.xml.transform.stream.StreamResult(cadena));
            result = cadena.toString("UTF8");
            result = cadena.toString("UTF8");
            //Quitamos la etiqueta <?xml....> en caso de existir
            if (result.startsWith("<?xml")) {
                result = result.substring(38);
            }

            //Existia un error al procesar <xsl:output method="text" version="1.0" encoding="UTF-8" indent="no"/>
            //En el appEngine
            result = result.replace("&amp;", "&");
            result = result.replace("&quot;", "\"");
            result = result.replace("&lt;", "<");
            result = result.replace("&gt;", ">");
            result = result.replace("&apos;", "'");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace(System.err);
            throw new InconsistentDataException(e.getMessage());
        } catch (TransformerException e) {
            e.printStackTrace(System.err);
            throw new InconsistentDataException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            throw new InconsistentDataException(e.getMessage());
        }
        return result;
    }

    /**
     * Timbra el documento.
     *
     * @param Contraseña
     * @throws LbsException
     * @throws IOException
     * @throws MalformedURLException
     * @throws BadElementException
     */
    public void timbrar(String contrasena) throws LbsException, MalformedURLException, IOException {
        LbsWsTimbrado wsTimbrado = null;
        try {
            wsTimbrado = (new LbsWsTimbradoService()).getLbsWsTimbradoPort();
        } catch (Exception e) {
            e.printStackTrace();
            throw new LbsException("Imposible conectar con el servidor. Verifique su conexion a internet.");
        }
        //TimeOut increment
        Map<String, Object> requestContext = ((BindingProvider) wsTimbrado).getRequestContext();
        requestContext.put("com.sun.xml.ws.request.timeout", 60000); // Timeout in millis
        requestContext.put("com.sun.xml.ws.connect.timeout", 60000);
        requestContext.put("com.sun.xml.internal.ws.request.timeout", 60000); // Timeout in millis
        requestContext.put("com.sun.xml.internal.ws.connect.timeout", 60000);

//				System.out.println(xml.getAsText());
        byte[] result = wsTimbrado.timbrar(toString().getBytes("UTF8"), contrasena);
//				((XmlFact)xml).setAdenda(addendaNode);
        if (new String(result).startsWith("Error")) {
            System.out.println(this);
            throw new LbsException(new String(result));
        }

        try {
            parse(result);
        } catch (myXmlParsingException e) {
            e.printStackTrace();
        }
    }

    public void timbrarFinkok(String user, String pass) throws InconsistentDataException {
        Long start = new Date().getTime();
        com.lbs.core.server.timbrado.finkok.stamp.Application application;
        //demostamp.Application application;   //este para hacer pruebas demo
        try {
            application = new com.lbs.core.server.timbrado.finkok.stamp.StampSOAP().getApplication();
            //application = new demostamp.StampSOAP().getApplication();   //este para hacer pruebas demo
        } catch (WebServiceException eb) {
            throw new InconsistentDataException("Error al conectar con el SAT.");
        }

        //TimeOut increment
        Map<String, Object> requestContext = ((BindingProvider) application).getRequestContext();
        requestContext.put("com.sun.xml.ws.request.timeout", 20000); // Timeout in millis
        requestContext.put("com.sun.xml.ws.connect.timeout", 20000);
        requestContext.put("com.sun.xml.internal.ws.request.timeout", 20000); // Timeout in millis
        requestContext.put("com.sun.xml.internal.ws.connect.timeout", 20000);

        com.lbs.core.server.timbrado.finkok.stamp.AcuseRecepcionCFDI acuse=null;
        //demostamp.AcuseRecepcionCFDI acuse = null;  //este para hacer pruebas demo
        try {
            acuse = application.stamp(toString().getBytes("UTF8"), user, pass);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (acuse.getUUID() == null) {
            //Hay que dar de alta RFC y se vuelve a llamar a timbrar
            if (acuse.getIncidencias().getValue().getIncidencia().get(0).getCodigoError().getValue().equals("702")) {
                //new com.lbs.core.server.timbrado.finkok.registration.RegistrationSOAP().getApplication().add(user,pass, getRfcEmisor(), null, null);
                timbrarFinkok(user, pass);
                //Ya esta timbrado el documento
            } else if (acuse.getIncidencias().getValue().getIncidencia().get(0).getCodigoError().getValue().equals("307")) {
                try {
                    acuse = application.stamped(toString().getBytes("UTF8"), user, pass);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (acuse.getUUID() == null) {
                    throw new InconsistentDataException(acuse.getIncidencias().getValue().getIncidencia().get(0).getCodigoError().getValue() + ": "
                            + acuse.getIncidencias().getValue().getIncidencia().get(0).getMensajeIncidencia().getValue());
                }
            } else {
                System.out.println(this);
                throw new InconsistentDataException(acuse.getIncidencias().getValue().getIncidencia().get(0).getCodigoError().getValue() + ": "
                        + acuse.getIncidencias().getValue().getIncidencia().get(0).getMensajeIncidencia().getValue());
            }
        }

        try {
            parse(acuse.getXml().getValue().getBytes("UTF8"));
        } catch (myXmlParsingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancela un uuid ante el SAT.
     *
     * @param pass Contraseña generada por el equipo de LizBetSoft
     * @param rfc	RFC del emisor de CFDI a cancelar
     * @param uuid UUID de CFDI a cancelar
     * @param crypt	Objeto LbsCrypt generado con el certificado de sello digital
     * del emisor
     *
     * @throws LbsException
     */
    public static void cancelar(String pass, String rfc, String uuid, LbsCrypt crypt) throws LbsException {
        LbsWsTimbrado wsTimbrado = null;
        try {
            wsTimbrado = (new LbsWsTimbradoService()).getLbsWsTimbradoPort();
        } catch (Exception e) {
            e.printStackTrace();
            throw new LbsException("Imposible conectar con el servidor. Verifique su conexion a internet.");
        }
        //TimeOut increment
        Map<String, Object> requestContext = ((BindingProvider) wsTimbrado).getRequestContext();
        requestContext.put("com.sun.xml.ws.request.timeout", 60000); // Timeout in millis
        requestContext.put("com.sun.xml.ws.connect.timeout", 60000);
        requestContext.put("com.sun.xml.internal.ws.request.timeout", 60000); // Timeout in millis
        requestContext.put("com.sun.xml.internal.ws.connect.timeout", 60000);

        try {
            String result = wsTimbrado.cancelar(LbsCrypt.PFX_PASS, rfc, Arrays.asList(uuid), crypt.getPfx(), LbsCrypt.PFX_PASS);
            if (!result.toUpperCase().equals("OK")) {
                throw new LbsException(result);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new LbsException(e.getMessage());
        } catch (CertificateException e) {
            throw new LbsException(e.getMessage());
        } catch (KeyStoreException e) {
            throw new LbsException(e.getMessage());
        } catch (NoSuchProviderException e) {
            throw new LbsException(e.getMessage());
        } catch (IOException e) {
            throw new LbsException(e.getMessage());
        }
    }

    public static void cancelarFinkok(String user, String pass, String rfc, String uuid, String key, String cer, String key_pass) throws GeneralSecurityException, IOException, CertificateException, InconsistentDataException, Exception {
        com.lbs.core.server.timbrado.finkok.cancel.Application application;
        try {
            application = new com.lbs.core.server.timbrado.finkok.cancel.CancelSOAP().getApplication();
        } catch (WebServiceException eb) {
            eb.printStackTrace();
            throw new InconsistentDataException("Error al conectar con el PAC.");
        }
        //Con estas lineas incrementamos el tiempo de espera de las llamadas al web service por si la respuesta del PAC es lenta
        Map<String, Object> requestContext = ((BindingProvider) application).getRequestContext();
        requestContext.put("com.sun.xml.ws.request.timeout", 60000); // Timeout in millis
        requestContext.put("com.sun.xml.ws.connect.timeout", 60000);
        requestContext.put("com.sun.xml.internal.ws.request.timeout", 60000); // Timeout in millis
        requestContext.put("com.sun.xml.internal.ws.connect.timeout", 60000);

        //Obtenemos streams de los archivos del CSD
        byte[] cer_file = null;
        byte[] key_file = null;
        try {
            cer_file = leeArchivo(cer).getBytes("UTF-8");
            key_file = leeArchivo(key).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Error al leer archivos pem: " + e.getMessage(), new Exception());
        }

        try {
            //Para las cancelaciones se necesita enviar encriptado el certificado en PFX
            //LbsCrypt crypt2 = new LbsCrypt(new ByteArrayInputStream(crypt.getPfx()),LbsCrypt.PFX_PASS);
            //cancelar(rfc, uuid, crypt2);
            //application.cancel(uuids, cer, version, cer, cer, key, Boolean.TRUE);

            com.lbs.core.server.timbrado.finkok.cancel.UUIDS uuids = new com.lbs.core.server.timbrado.finkok.cancel.ObjectFactory().createUUIDS();
            javax.xml.bind.JAXBElement<com.lbs.core.server.timbrado.finkok.cancel.StringArray> uuidArray = new com.lbs.core.server.timbrado.finkok.cancel.ObjectFactory().createUUIDSUuids(new com.lbs.core.server.timbrado.finkok.cancel.StringArray());
            uuids.setUuids(uuidArray);
            uuids.getUuids().getValue().getString().add(uuid);

            com.lbs.core.server.timbrado.finkok.cancel.CancelaCFDResult result = application.cancel(uuids, user, pass, rfc, cer_file, key_file, true);
            //com.lbs.core.server.timbrado.finkok.cancel.CancelaCFDResult result = application.cancel(uuids, user, pass, rfc, crypt2.getPemCert(), crypt2.getPemKey(key_pass), true);
            //Si no funciona con la linea anterior puede ser con esta
            //com.lbs.core.server.timbrado.finkok.cancel.CancelaCFDResult result = application.cancel(uuids, user, pass, rfc, crypt.getPemCert(), crypt.getPemKey(pass), true);

            //Errores posibles
            if (result.getFolios() == null && !result.getCodEstatus().getValue().toLowerCase().contains("previamente cancelado")) {
                if (result.getCodEstatus().getValue().toLowerCase().contains("no encontrado")) {
                    throw new Exception("No encontrado Error: " + result.getCodEstatus().getValue() , new Exception());
                } else {
                    throw new Exception("Finkok Error: " + result.getCodEstatus().getValue(), new Exception());
                }
            }
            if (result.getFolios() != null && !result.getFolios().getValue().getFolio().get(0).getEstatusUUID().getValue().equals("201")
                    && !result.getFolios().getValue().getFolio().get(0).getEstatusUUID().getValue().equals("202")) {
                throw new Exception("Finkok Error: " + result.getFolios().getValue().getFolio().get(0).getEstatusUUID().getValue(), new Exception());
            }
            /*if (result.getAcuse() != null) {
                System.out.println(result.getAcuse().getValue());
            }
            if (result.getCodEstatus() != null) {
                System.out.println("Codigo Estatus: " + result.getCodEstatus().getValue());
            }
            /*if (result.getFolios() != null) {
                com.lbs.core.server.timbrado.finkok.cancel.FolioArray folioArray = result.getFolios().getValue();
                for (com.lbs.core.server.timbrado.finkok.cancel.Folio f : folioArray.getFolio()) {
                    if (f.getUUID() != null) {
                        System.out.println("UUID: " + f.getUUID().getValue());
                    }
                    if (f.getEstatusUUID() != null) {
                        System.out.println("Estatus UUID: " + f.getEstatusUUID().getValue());
                    }
                }
            }*/

        } catch (Exception e) {
            throw new Exception("Finkok Error: " + e.getMessage(), new Exception());
        }
    }
    
    public static String leeArchivo(String ruta) {
        try {
            FileInputStream fstream = new FileInputStream(ruta);
            DataInputStream entrada = new DataInputStream(fstream);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;
            StringBuffer sB = new StringBuffer();
            while ((strLinea = buffer.readLine()) != null) {
                sB.append(strLinea).append("\n");
            }
            entrada.close();
            return sB.toString();
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Guarda el archivo XML.
     *
     * @param path Ruta para guardar.
     * @return
     * @throws IOException
     */
    public Boolean saveXML(String path) throws IOException {
        try {
            OutputStream fout = new FileOutputStream(path);
            OutputStream bout = new BufferedOutputStream(fout);
            OutputStreamWriter out = new OutputStreamWriter(bout, "UTF8");
            out.write(toString());
            out.flush();
            out.close();
        } catch (UnsupportedEncodingException e) {
            System.out.println("La Máquina Virtual no soporta la codificación UTF8.");
            return false;
        }
        return true;
    }

    public void savePdf(String ruta) throws IOException, myXmlParsingException, DocumentException {
        FileOutputStream fos = new FileOutputStream(ruta);
        LibPdfBuilder pdf = getPdf();
        fos.write(pdf.dataArray.toByteArray());
        fos.close();
    }

    private LibPdfBuilder getPdf() {
        if (pdf == null) {
            //Construccion del PDF
            try {
                String fondo = null;
                if (this instanceof CfdiF33 || this instanceof CfdiNC33) {
                    pdf = new PdfIE(this);
                } else if (this instanceof CfdiP33) {
                    pdf = new PdfP(this);
                }
            } catch (DocumentException | IOException | myXmlParsingException e) {
                e.printStackTrace();
            }
        }
        return pdf;
    }

    /**
     * Devuelve el documentos XML como una cadena de texto
     */
    @SuppressWarnings("deprecation")
    public String toString() {
        StringWriter strWriter = null;
        XMLSerializer xmlSerializer = null;
        OutputFormat outFormat = null;
        try {
            xmlSerializer = new XMLSerializer();
            strWriter = new StringWriter();
            outFormat = new OutputFormat();

            outFormat.setEncoding("UTF-8");
            //outFormat.setVersion();
            outFormat.setIndenting(true);
            outFormat.setIndent(2);
//		            outFormat.setPreserveEmptyAttributes(true);
            outFormat.setPreserveSpace(true);

            xmlSerializer.setOutputCharStream(strWriter);
            xmlSerializer.setOutputFormat(outFormat);
            xmlSerializer.serialize(xmlDoc);
            strWriter.close();
        } catch (IOException ioEx) {
            System.out.println("Error : " + ioEx);
        }
        return strWriter.toString();
    }

    /**
     * Calcula totales e impuestos de CFDI
     *
     */
    private void calcTotales() {
        Double subTotal = 0d;
        Double descuento = 0d;
        Double total = 0d;
        Double totalImpuestosTrasladados = 0d;
        Double totalImpuestosRetenidos = 0d;

        for (PartidaCfdi partida : partidas) {
            subTotal += partida.getImpTot();
            descuento += partida.getDescuento();
        }

        for (ImpuestoCfdi imp : impuestos) {
            if (imp.getTipo().equals(ImpuestoCfdi.TIPO_TRASLADO)) {
                totalImpuestosTrasladados += imp.getImporte();
            } else {
                totalImpuestosRetenidos += imp.getImporte();
            }
        }

        BigDecimal big;
        big = new BigDecimal(subTotal);
        big = big.setScale(decimales, RoundingMode.HALF_UP);
        subTotal = new Double(big.doubleValue());

        big = new BigDecimal(totalImpuestosTrasladados);
        big = big.setScale(decimales, RoundingMode.HALF_UP);
        totalImpuestosTrasladados = new Double(big.doubleValue());

        big = new BigDecimal(totalImpuestosRetenidos);
        big = big.setScale(decimales, RoundingMode.HALF_UP);
        totalImpuestosRetenidos = new Double(big.doubleValue());

        total = (subTotal + totalImpuestosTrasladados) - totalImpuestosRetenidos;

        big = new BigDecimal(total);
        big = big.setScale(decimales, RoundingMode.HALF_UP);
        total = new Double(big.doubleValue());

        nodoComprobante.setAttribute("SubTotal", cfdiNumFormat.format(subTotal));
        if (descuento > 0) {
            nodoComprobante.setAttribute("Descuento", cfdiNumFormat.format(descuento));
        }
        nodoComprobante.setAttribute("Total", cfdiNumFormat.format(total - descuento));

        //El nodo impuestos solo se requiere en comprobantes tipo Ingreso y Egreso
        if (this instanceof CfdiF33 || this instanceof CfdiNC33) {
            crearImpuestos();
        }
    }

    private void crearImpuestos() {
        Element elementImpuestos = xmlDoc.createElement(domainPrefix + "Impuestos");
        nodoComprobante.appendChild(elementImpuestos);

        Double totalTrasladado = 0d, totalRetenido = 0d;
        Integer numTrasladados = 0, numRetenidos = 0;
        Element impRetElement, impTrasElement;
        impRetElement = xmlDoc.createElement(domainPrefix + "Retenciones");
        impTrasElement = xmlDoc.createElement(domainPrefix + "Traslados");

        for (ImpuestoCfdi imp : impuestos) {
            if (imp.getTipo() == ImpuestoCfdi.TIPO_RETENCION) {
                Element retencion = xmlDoc.createElement(domainPrefix + "Retencion");
                retencion.setAttribute("Impuesto", imp.getImpuesto());
                retencion.setAttribute("Importe", cfdiNumFormat.format(imp.getImporte()));
                impRetElement.appendChild(retencion);
                totalRetenido += imp.getImporte();
                numRetenidos++;
            } else {
                Element traslado = this.xmlDoc.createElement(domainPrefix + "Traslado");
                traslado.setAttribute("Impuesto", imp.getImpuesto());
                traslado.setAttribute("TipoFactor", imp.getTipoFactor());
                traslado.setAttribute("TasaOCuota", numformatImpuestos.format(imp.getTasaCuota()));
                traslado.setAttribute("Importe", cfdiNumFormat.format(imp.getImporte()));
                impTrasElement.appendChild(traslado);
                totalTrasladado += imp.getImporte();
                numTrasladados++;
            }
        }
        if (numRetenidos > 0) {
            elementImpuestos.appendChild(impRetElement);
        }
        if (numTrasladados > 0) {
            elementImpuestos.appendChild(impTrasElement);
        }

        if (numRetenidos > 0) {
            elementImpuestos.setAttribute("TotalImpuestosRetenidos", cfdiNumFormat.format(totalRetenido));
            elementImpuestos.appendChild(impRetElement);
        }
        if (numTrasladados > 0) {
            elementImpuestos.setAttribute("TotalImpuestosTrasladados", cfdiNumFormat.format(totalTrasladado));
            elementImpuestos.appendChild(impTrasElement);
        }
    }

    /**
     * Devuelve todos los nodos del elemento indicado por path. Lista vacia si
     * no hay elementos y null si algun elemento del path no existe.
     *
     * @Param path: Path al elemento sin contar la raiz, para referirse a la
     * raiz utilizar null
     */
    public NodeList getNodes(String p) {
        Element element = xmlDoc.getDocumentElement();
        NodeList lst = null;
        NodeList tmpLst = null;
        String condition = null;
        String strElement;
        Boolean finded;
        if (p != null) {
            String[] path = p.split("/");
            for (int x = 0; x < path.length; x++) {
                if (x + 1 == path.length) //Llegamos al penultimo elemento
                {
                    if (element != null) {
                        if (path[x].contains("?")) {
                            strElement = path[x].split("\\?")[0];
                            condition = path[x].split("\\?")[1];
                        } else {
                            strElement = path[x];
                        }
                        tmpLst = element.getElementsByTagName(domainPrefix + strElement);
                        if (tmpLst.getLength() == 0) {
                            tmpLst = element.getElementsByTagName(strElement);
                        }

                        lst = tmpLst;
                    } else {
                        lst = new NodeList() {

                            public int getLength() {
                                return 0;
                            }

                            public Node item(int index) {
                                return null;
                            }
                        };
                    }
                } else if (element == null) {
                    return null;
                } else {
                    tmpLst = element.getElementsByTagName(domainPrefix + path[x]);
                    if (tmpLst == null || tmpLst.getLength() == 0 || tmpLst.item(0) == null) {
                        tmpLst = element.getElementsByTagName(path[x]);
                    }

                    if (tmpLst == null || tmpLst.getLength() == 0 || tmpLst.item(0) == null) {
                        element = null;
                    } else {
                        finded = false;
                        for (int z = 0; z < tmpLst.getLength(); z++) {
                            if (tmpLst.item(z).getParentNode().equals(element)) {
                                element = (Element) tmpLst.item(z);
                                finded = true;
                                break;
                            }
                        }
                        if (!finded) {
                            element = null;
                        }
                    }

                }
            }
        }
        return lst;
    }

    /**
     * Obtiene el valor de la ruta espesificada por s dentro del XML detecta si
     * es valor o atributo.
     */
    public String get(String s) {
        if (getValue(s).length() == 0) {
            String path = null, att = "";
            String[] fullPath = s.split("/");
            for (int x = 0; x < fullPath.length - 1; x++) {
                path = (path == null ? "" : path) + fullPath[x];
                if (x < fullPath.length - 2) {
                    path = path + "/";
                }
            }
            att = fullPath[fullPath.length - 1];
            return getAttrib(path, att);
        } else {
            return getValue(s);
        }
    }

    /**
     * Devuelve el atributo attrib del elemento indicado por path
     *
     * @Param path: Path al elemento sin contar la raiz, para referirse a la
     * raiz utilizar null
     * @Param attrib: Nombre del atributo
     */
    public String getAttrib(String path, String attrib) {
        Element element = xmlDoc.getDocumentElement();
        if (path != null) {
            String[] paths = path.split("/");
            for (int x = 0; x < paths.length; x++) {
                if (element == null) {
                    break;
                } else {
                    element = getNextElement(element, paths[x]);
                }
            }
        }
        return (element == null || element.getAttribute(attrib) == null || element.getAttribute(attrib).length() == 0) ? "" : element.getAttribute(attrib);
    }

    /**
     * Regresa de parent el elemento hijo de nombre strElement que puede ser
     * distinguido de una lista mediante la condicion element?att=val
     *
     * @param parent
     * @param strElement
     * @return
     */
    private Element getNextElement(Element parent, String strElement) {
        Element result = null;
        String elementName = null;
        String condition = null;
        String conditionParam = null;
        String conditionValue = null;

        //hay condicion en el elemento buscado?
        if (strElement.contains("?")) {
            elementName = strElement.split("\\?")[0];
            condition = strElement.split("\\?")[1];
            conditionParam = condition.split("=")[0];
            conditionValue = condition.split("=")[1];
        } else {
            elementName = strElement;
        }

        //Hay condicion
        if (condition != null) {
            //Buscamos el Elemento
            NodeList elementsList;
            if (!elementName.contains(":") && parent.getElementsByTagName(domainPrefix + elementName) != null && parent.getElementsByTagName(domainPrefix + elementName).getLength() > 0) {
                elementsList = parent.getElementsByTagName(domainPrefix + elementName);
            } else {
                elementsList = parent.getElementsByTagName(elementName);
            }
            for (int x = 0; x < elementsList.getLength(); x++) {
                if (((Element) elementsList.item(x)).getAttribute(conditionParam).equals(conditionValue)) {
                    result = (Element) elementsList.item(x);
                    break;
                }
            }
            //No hay condicion
        } else if (!elementName.contains(":") && parent.getElementsByTagName(domainPrefix + elementName).item(0) != null) {
            result = (Element) parent.getElementsByTagName(domainPrefix + elementName).item(0);
        } else {
            result = (Element) parent.getElementsByTagName(elementName).item(0);
        }

        return result;
    }

    /**
     * Devuelve el valor del elemento indicado por path
     *
     * @Param path: Path al elemento sin contar la raiz, para referirse a la
     * raiz utilizar null
     */
    public String getValue(String p) {
        Element element = xmlDoc.getDocumentElement();
        if (p != null) {
            String[] path = p.split("/");
            for (int x = 0; x < path.length; x++) {
                if (element == null) {
                    break;
                } else {
                    element = getNextElement(element, path[x]);

                }
            }
        }
        return (element == null || element.getTextContent() == null) ? "" : element.getTextContent();
    }

    public String getMes() {
        return this.mes;
    }

    public String getAno() {
        return this.ano;
    }

    public String getDia() {
        return this.dia;
    }

    public String getLeyenda() {
        return leyenda;
    }

    private class NullOutputStream extends PrintStream {

        public NullOutputStream(String fileName) throws FileNotFoundException {
            super(new ByteArrayOutputStream());
        }

        public void write(byte[] asd) {

        }

        public void write(int asd) {

        }

        public void write(byte[] a, int b, int c) {

        }
    }

    public String getCadenaTimbre() {
        return "||" + getAttrib(domainPrefix + "Complemento/tfd:TimbreFiscalDigital", "Version") + "|"
                + getAttrib(domainPrefix + "Complemento/tfd:TimbreFiscalDigital", "UUID") + "|"
                + getAttrib(domainPrefix + "Complemento/tfd:TimbreFiscalDigital", "FechaTimbrado") + "|"
                + getAttrib(domainPrefix + "Complemento/tfd:TimbreFiscalDigital", "RfcProvCertif") + "|"
                + getAttrib(domainPrefix + "Complemento/tfd:TimbreFiscalDigital", "SelloCFD") + "|"
                + getAttrib(domainPrefix + "Complemento/tfd:TimbreFiscalDigital", "NoCertificadoSAT") + "||";

    }
}
