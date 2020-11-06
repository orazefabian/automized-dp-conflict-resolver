//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.04 um 10:04:23 PM CET 
//


package org.apache.maven.pom._4_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The conditions within the build runtime environment which will trigger the
 *         automatic inclusion of the build profile. Multiple conditions can be defined, which must
 *         be all satisfied to activate the profile.
 *       
 * 
 * <p>Java-Klasse für Activation complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Activation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="activeByDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="jdk" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="os" type="{http://maven.apache.org/POM/4.0.0}ActivationOS" minOccurs="0"/>
 *         &lt;element name="property" type="{http://maven.apache.org/POM/4.0.0}ActivationProperty" minOccurs="0"/>
 *         &lt;element name="file" type="{http://maven.apache.org/POM/4.0.0}ActivationFile" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Activation", propOrder = {

})
public class Activation {

    @XmlElement(defaultValue = "false")
    protected Boolean activeByDefault;
    protected String jdk;
    protected ActivationOS os;
    protected ActivationProperty property;
    protected ActivationFile file;

    /**
     * Ruft den Wert der activeByDefault-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isActiveByDefault() {
        return activeByDefault;
    }

    /**
     * Legt den Wert der activeByDefault-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setActiveByDefault(Boolean value) {
        this.activeByDefault = value;
    }

    /**
     * Ruft den Wert der jdk-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJdk() {
        return jdk;
    }

    /**
     * Legt den Wert der jdk-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJdk(String value) {
        this.jdk = value;
    }

    /**
     * Ruft den Wert der os-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ActivationOS }
     *     
     */
    public ActivationOS getOs() {
        return os;
    }

    /**
     * Legt den Wert der os-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivationOS }
     *     
     */
    public void setOs(ActivationOS value) {
        this.os = value;
    }

    /**
     * Ruft den Wert der property-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ActivationProperty }
     *     
     */
    public ActivationProperty getProperty() {
        return property;
    }

    /**
     * Legt den Wert der property-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivationProperty }
     *     
     */
    public void setProperty(ActivationProperty value) {
        this.property = value;
    }

    /**
     * Ruft den Wert der file-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ActivationFile }
     *     
     */
    public ActivationFile getFile() {
        return file;
    }

    /**
     * Legt den Wert der file-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivationFile }
     *     
     */
    public void setFile(ActivationFile value) {
        this.file = value;
    }

}
