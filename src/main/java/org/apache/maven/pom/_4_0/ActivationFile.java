//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.04 um 10:04:23 PM CET 
//


package org.apache.maven.pom._4_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * This is the file specification used to activate the profile. The <code>missing</code> value
 *         is the location of a file that needs to exist, and if it doesn't, the profile will be
 *         activated. On the other hand, <code>exists</code> will test for the existence of the file and if it is
 *         there, the profile will be activated.<br/>
 *         Variable interpolation for these file specifications is limited to <code>${basedir}</code>,
 *         System properties and request properties.
 * 
 * <p>Java-Klasse für ActivationFile complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ActivationFile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="missing" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exists" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActivationFile", propOrder = {

})
public class ActivationFile {

    protected String missing;
    protected String exists;

    /**
     * Ruft den Wert der missing-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMissing() {
        return missing;
    }

    /**
     * Legt den Wert der missing-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMissing(String value) {
        this.missing = value;
    }

    /**
     * Ruft den Wert der exists-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExists() {
        return exists;
    }

    /**
     * Legt den Wert der exists-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExists(String value) {
        this.exists = value;
    }

}
