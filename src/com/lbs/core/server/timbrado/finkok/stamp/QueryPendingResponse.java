
package com.lbs.core.server.timbrado.finkok.stamp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for query_pendingResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="query_pendingResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="query_pendingResult" type="{apps.services.soap.core.views}QueryPendingResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "query_pendingResponse", propOrder = {
    "queryPendingResult"
})
public class QueryPendingResponse {

    @XmlElementRef(name = "query_pendingResult", namespace = "http://facturacion.finkok.com/stamp", type = JAXBElement.class, required = false)
    protected JAXBElement<QueryPendingResult> queryPendingResult;

    /**
     * Gets the value of the queryPendingResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QueryPendingResult }{@code >}
     *     
     */
    public JAXBElement<QueryPendingResult> getQueryPendingResult() {
        return queryPendingResult;
    }

    /**
     * Sets the value of the queryPendingResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QueryPendingResult }{@code >}
     *     
     */
    public void setQueryPendingResult(JAXBElement<QueryPendingResult> value) {
        this.queryPendingResult = value;
    }

}
