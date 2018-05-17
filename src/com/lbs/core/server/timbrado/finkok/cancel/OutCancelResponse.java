
package com.lbs.core.server.timbrado.finkok.cancel;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for out_cancelResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="out_cancelResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out_cancelResult" type="{apps.services.soap.core.views}CancelaCFDResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "out_cancelResponse", propOrder = {
    "outCancelResult"
})
public class OutCancelResponse {

    @XmlElementRef(name = "out_cancelResult", namespace = "http://facturacion.finkok.com/cancel", type = JAXBElement.class, required = false)
    protected JAXBElement<CancelaCFDResult> outCancelResult;

    /**
     * Gets the value of the outCancelResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CancelaCFDResult }{@code >}
     *     
     */
    public JAXBElement<CancelaCFDResult> getOutCancelResult() {
        return outCancelResult;
    }

    /**
     * Sets the value of the outCancelResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CancelaCFDResult }{@code >}
     *     
     */
    public void setOutCancelResult(JAXBElement<CancelaCFDResult> value) {
        this.outCancelResult = value;
    }

}
