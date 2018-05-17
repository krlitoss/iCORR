
package com.lbs.core.server.timbrado.finkok.cancel;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cancelResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cancelResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cancelResult" type="{apps.services.soap.core.views}CancelaCFDResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelResponse", propOrder = {
    "cancelResult"
})
public class CancelResponse {

    @XmlElementRef(name = "cancelResult", namespace = "http://facturacion.finkok.com/cancel", type = JAXBElement.class, required = false)
    protected JAXBElement<CancelaCFDResult> cancelResult;

    /**
     * Gets the value of the cancelResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CancelaCFDResult }{@code >}
     *     
     */
    public JAXBElement<CancelaCFDResult> getCancelResult() {
        return cancelResult;
    }

    /**
     * Sets the value of the cancelResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CancelaCFDResult }{@code >}
     *     
     */
    public void setCancelResult(JAXBElement<CancelaCFDResult> value) {
        this.cancelResult = value;
    }

}
