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
 * 
 *         
 *         The <code>&lt;plugin&gt;</code> element contains informations required for a report plugin.
 *         
 *       
 * 
 * <p>Java-Klasse für ReportPlugin complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ReportPlugin">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="groupId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="artifactId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reportSets" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="reportSet" type="{http://maven.apache.org/POM/4.0.0}ReportSet" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="inherited" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "ReportPlugin", propOrder = {

})
public class ReportPlugin {

    @XmlElement(defaultValue = "org.apache.maven.plugins")
    protected String groupId;
    protected String artifactId;
    protected String version;
    protected ReportSets reportSets;
    protected String inherited;
    protected Configuration configuration;

    /**
     * Ruft den Wert der groupId-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Legt den Wert der groupId-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setGroupId(String value) {
        this.groupId = value;
    }

    /**
     * Ruft den Wert der artifactId-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Legt den Wert der artifactId-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setArtifactId(String value) {
        this.artifactId = value;
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Ruft den Wert der reportSets-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link ReportSets }
     *
     */
    public ReportSets getReportSets() {
        return reportSets;
    }

    /**
     * Legt den Wert der reportSets-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link ReportSets }
     *
     */
    public void setReportSets(ReportSets value) {
        this.reportSets = value;
    }

    /**
     * Ruft den Wert der inherited-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInherited() {
        return inherited;
    }

    /**
     * Legt den Wert der inherited-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInherited(String value) {
        this.inherited = value;
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
     *         &lt;element name="reportSet" type="{http://maven.apache.org/POM/4.0.0}ReportSet" maxOccurs="unbounded" minOccurs="0"/>
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
        "reportSet"
    })
    public static class ReportSets {

        protected List<ReportSet> reportSet;

        /**
         * Gets the value of the reportSet property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the reportSet property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getReportSet().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ReportSet }
         * 
         * 
         */
        public List<ReportSet> getReportSet() {
            if (reportSet == null) {
                reportSet = new ArrayList<ReportSet>();
            }
            return this.reportSet;
        }

    }

}
