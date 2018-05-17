
package com.lbs.core.server.timbrado.finkok.stamp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for quick_stampResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="quick_stampResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="quick_stampResult" type="{apps.services.soap.core.views}AcuseRecepcionCFDI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "quick_stampResponse", propOrder = {
    "quickStampResult"
})
public class QuickStampResponse {

    @XmlElementRef(name = "quick_stampResult", namespace = "http://facturacion.finkok.com/stamp", type = JAXBElement.class, required = false)
    protected JAXBElement<AcuseRecepcionCFDI> quickStampResult;

    /**
     * Gets the value of the quickStampResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AcuseRecepcionCFDI }{@code >}
     *     
     */
    public JAXBElement<AcuseRecepcionCFDI> getQuickStampResult() {
        return quickStampResult;
    }

    /**
     * Sets the value of the quickStampResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AcuseRecepcionCFDI }{@code >}
     *     
     */
    public void setQuickStampResult(JAXBElement<AcuseRecepcionCFDI> value) {
        this.quickStampResult = value;
    }

}
