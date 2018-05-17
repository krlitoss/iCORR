
package com.lbs.webservices.lizbet.timbrado;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.lbs.webservices.lizbet.timbrado package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CambiarContrasenaResponse_QNAME = new QName("http://timbrado.webservices.server.lizbet.com/", "cambiarContrasenaResponse");
    private final static QName _CambiarContrasena_QNAME = new QName("http://timbrado.webservices.server.lizbet.com/", "cambiarContrasena");
    private final static QName _Cancelar_QNAME = new QName("http://timbrado.webservices.server.lizbet.com/", "cancelar");
    private final static QName _CancelarResponse_QNAME = new QName("http://timbrado.webservices.server.lizbet.com/", "cancelarResponse");
    private final static QName _TimbrarResponse_QNAME = new QName("http://timbrado.webservices.server.lizbet.com/", "timbrarResponse");
    private final static QName _Timbrar_QNAME = new QName("http://timbrado.webservices.server.lizbet.com/", "timbrar");
    private final static QName _CancelarArg3_QNAME = new QName("", "arg3");
    private final static QName _TimbrarArg0_QNAME = new QName("", "arg0");
    private final static QName _TimbrarResponseReturn_QNAME = new QName("", "return");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.lbs.webservices.lizbet.timbrado
     * 
     */
    public ObjectFactory() {
    }

    
    
    /**
     * Create an instance of {@link CancelarResponse }
     * 
     */
    public CancelarResponse createCancelarResponse() {
        return new CancelarResponse();
    }

    /**
     * Create an instance of {@link Cancelar }
     * 
     */
    public Cancelar createCancelar() {
        return new Cancelar();
    }

    /**
     * Create an instance of {@link CambiarContrasenaResponse }
     * 
     */
    public CambiarContrasenaResponse createCambiarContrasenaResponse() {
        return new CambiarContrasenaResponse();
    }

    /**
     * Create an instance of {@link Timbrar }
     * 
     */
    public Timbrar createTimbrar() {
        return new Timbrar();
    }

    /**
     * Create an instance of {@link TimbrarResponse }
     * 
     */
    public TimbrarResponse createTimbrarResponse() {
        return new TimbrarResponse();
    }

    /**
     * Create an instance of {@link CambiarContrasena }
     * 
     */
    public CambiarContrasena createCambiarContrasena() {
        return new CambiarContrasena();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CambiarContrasenaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://timbrado.webservices.server.lizbet.com/", name = "cambiarContrasenaResponse")
    public JAXBElement<CambiarContrasenaResponse> createCambiarContrasenaResponse(CambiarContrasenaResponse value) {
        return new JAXBElement<CambiarContrasenaResponse>(_CambiarContrasenaResponse_QNAME, CambiarContrasenaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CambiarContrasena }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://timbrado.webservices.server.lizbet.com/", name = "cambiarContrasena")
    public JAXBElement<CambiarContrasena> createCambiarContrasena(CambiarContrasena value) {
        return new JAXBElement<CambiarContrasena>(_CambiarContrasena_QNAME, CambiarContrasena.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Cancelar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://timbrado.webservices.server.lizbet.com/", name = "cancelar")
    public JAXBElement<Cancelar> createCancelar(Cancelar value) {
        return new JAXBElement<Cancelar>(_Cancelar_QNAME, Cancelar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://timbrado.webservices.server.lizbet.com/", name = "cancelarResponse")
    public JAXBElement<CancelarResponse> createCancelarResponse(CancelarResponse value) {
        return new JAXBElement<CancelarResponse>(_CancelarResponse_QNAME, CancelarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TimbrarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://timbrado.webservices.server.lizbet.com/", name = "timbrarResponse")
    public JAXBElement<TimbrarResponse> createTimbrarResponse(TimbrarResponse value) {
        return new JAXBElement<TimbrarResponse>(_TimbrarResponse_QNAME, TimbrarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Timbrar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://timbrado.webservices.server.lizbet.com/", name = "timbrar")
    public JAXBElement<Timbrar> createTimbrar(Timbrar value) {
        return new JAXBElement<Timbrar>(_Timbrar_QNAME, Timbrar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg3", scope = Cancelar.class)
    public JAXBElement<byte[]> createCancelarArg3(byte[] value) {
        return new JAXBElement<byte[]>(_CancelarArg3_QNAME, byte[].class, Cancelar.class, (value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg0", scope = Timbrar.class)
    public JAXBElement<byte[]> createTimbrarArg0(byte[] value) {
        return new JAXBElement<byte[]>(_TimbrarArg0_QNAME, byte[].class, Timbrar.class, (value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = TimbrarResponse.class)
    public JAXBElement<byte[]> createTimbrarResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_TimbrarResponseReturn_QNAME, byte[].class, TimbrarResponse.class, (value));
    }

}
