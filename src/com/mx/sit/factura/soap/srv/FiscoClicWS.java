package com.mx.sit.factura.soap.srv;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2012-10-03T18:35:47.844-05:00
 * Generated source version: 2.6.2
 * 
 */

//file:/W:/PROYECTOS SK/ENNOVI_CORR/EEC/ENNOVI/src/TimbraWS.wsdl
//"file:/C:/Users/Carlos/Desktop/FiscoClic Services/FiscoClicWS_ClienteJava/src/TimbraWS.wsdl"
//https://www.fiscoclic.mx/factura/WSEntityServices/timbre/TimbraWS?wsdl
//https://www.fiscoclic.mx/factura/WSEntityServices/timbre/TimbraWS.wsdl
@WebServiceClient(name = "FiscoClicWS", 
                  wsdlLocation = "https://www.fiscoclic.mx/factura/WSEntityServices/timbre/TimbraWS?wsdl",
                  targetNamespace = "http://srv.soap.factura.sit.mx.com") 
public class FiscoClicWS extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://srv.soap.factura.sit.mx.com", "FiscoClicWS");
    public final static QName FiscoClicSoapPort = new QName("http://srv.soap.factura.sit.mx.com", "FiscoClicSoapPort");
    static {
        URL url = null;
        try {
            url = new URL("https://www.fiscoclic.mx/factura/WSEntityServices/timbre/TimbraWS?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(FiscoClicWS.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "/TimbraWS.wsdl");
        }catch(Exception e){
            java.util.logging.Logger.getLogger(FiscoClicWS.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "/TimbraWS.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public FiscoClicWS(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public FiscoClicWS(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FiscoClicWS() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns CfdiServiceInterface
     */
    @WebEndpoint(name = "FiscoClicSoapPort")
    public CfdiServiceInterface getFiscoClicSoapPort() {
        return super.getPort(FiscoClicSoapPort, CfdiServiceInterface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CfdiServiceInterface
     */
    @WebEndpoint(name = "FiscoClicSoapPort")
    public CfdiServiceInterface getFiscoClicSoapPort(WebServiceFeature... features) {
        return super.getPort(FiscoClicSoapPort, CfdiServiceInterface.class, features);
    }

}
