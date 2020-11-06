//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.04 um 10:04:23 PM CET 
//


package org.apache.maven.pom._4_0;

import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Configures one method for notifying users/developers when a build breaks.
 * 
 * <p>Java-Klasse für Notifier complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Notifier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sendOnError" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="sendOnFailure" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="sendOnSuccess" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="sendOnWarning" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="configuration" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;any processContents='skip' maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Notifier", propOrder = {

})
public class Notifier {

    @XmlElement(defaultValue = "mail")
    protected String type;
    @XmlElement(defaultValue = "true")
    protected Boolean sendOnError;
    @XmlElement(defaultValue = "true")
    protected Boolean sendOnFailure;
    @XmlElement(defaultValue = "true")
    protected Boolean sendOnSuccess;
    @XmlElement(defaultValue = "true")
    protected Boolean sendOnWarning;
    protected String address;
    protected Configuration configuration;

    /**
     * Ruft den Wert der type-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getType() {
        return type;
    }

    /**
     * Legt den Wert der type-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Ruft den Wert der sendOnError-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSendOnError() {
        return sendOnError;
    }

    /**
     * Legt den Wert der sendOnError-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSendOnError(Boolean value) {
        this.sendOnError = value;
    }

    /**
     * Ruft den Wert der sendOnFailure-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSendOnFailure() {
        return sendOnFailure;
    }

    /**
     * Legt den Wert der sendOnFailure-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSendOnFailure(Boolean value) {
        this.sendOnFailure = value;
    }

    /**
     * Ruft den Wert der sendOnSuccess-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSendOnSuccess() {
        return sendOnSuccess;
    }

    /**
     * Legt den Wert der sendOnSuccess-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSendOnSuccess(Boolean value) {
        this.sendOnSuccess = value;
    }

    /**
     * Ruft den Wert der sendOnWarning-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSendOnWarning() {
        return sendOnWarning;
    }

    /**
     * Legt den Wert der sendOnWarning-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSendOnWarning(Boolean value) {
        this.sendOnWarning = value;
    }

    /**
     * Ruft den Wert der address-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAddress() {
        return address;
    }

    /**
     * Legt den Wert der address-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Ruft den Wert der configuration-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Configuration }
     *
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Legt den Wert der configuration-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Configuration }
     *
     */
    public void setConfiguration(Configuration value) {
        this.configuration = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;any processContents='skip' maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "any"
    })
    public static class Configuration {

        @XmlAnyElement
        protected List<Element> any;

        /**
         * Gets the value of the any property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the any property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAny().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Element }
         * 
         * 
         */
        public List<Element> getAny() {
            if (any == null) {
                any = new ArrayList<Element>();
            }
            return this.any;
        }

    }

}
