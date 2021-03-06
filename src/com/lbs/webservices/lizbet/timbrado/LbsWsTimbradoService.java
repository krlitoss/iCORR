
package com.lbs.webservices.lizbet.timbrado;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "LbsWsTimbradoService", targetNamespace = "http://timbrado.webservices.server.lizbet.com/", wsdlLocation = "http://lizbetsoft.appspot.com/LbsWsTimbradoService.wsdl")
public class LbsWsTimbradoService
    extends Service
{

	
    private final static URL LBSWSTIMBRADOSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.lbs.webservices.lizbet.timbrado.LbsWsTimbradoService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.lbs.webservices.lizbet.timbrado.LbsWsTimbradoService.class.getResource(".");
            url = new URL(baseUrl, "http://lizbetsoft.appspot.com/LbsWsTimbradoService.wsdl");
//            url = new URL(baseUrl, "http://localhost:8888/LbsWsTimbradoService.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://lizbetsoft.appspot.com/LbsWsTimbradoService.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        LBSWSTIMBRADOSERVICE_WSDL_LOCATION = url;
    }

    public LbsWsTimbradoService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public LbsWsTimbradoService() {
        super(LBSWSTIMBRADOSERVICE_WSDL_LOCATION, new QName("http://timbrado.webservices.server.lizbet.com/", "LbsWsTimbradoService"));
    }
    

    /**
     * 
     * @return
     *     returns LbsWsTimbrado
     */
    @WebEndpoint(name = "LbsWsTimbradoPort")
    public LbsWsTimbrado getLbsWsTimbradoPort() {
        return super.getPort(new QName("http://timbrado.webservices.server.lizbet.com/", "LbsWsTimbradoPort"), LbsWsTimbrado.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns LbsWsTimbrado
     */
    @WebEndpoint(name = "LbsWsTimbradoPort")
    public LbsWsTimbrado getLbsWsTimbradoPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://timbrado.webservices.server.lizbet.com/", "LbsWsTimbradoPort"), LbsWsTimbrado.class, features);
    }

}
