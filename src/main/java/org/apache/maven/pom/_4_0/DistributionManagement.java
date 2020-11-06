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
 * This elements describes all that pertains to distribution for a project. It is
 *         primarily used for deployment of artifacts and the site produced by the build.
 * 
 * <p>Java-Klasse für DistributionManagement complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DistributionManagement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="repository" type="{http://maven.apache.org/POM/4.0.0}DeploymentRepository" minOccurs="0"/>
 *         &lt;element name="snapshotRepository" type="{http://maven.apache.org/POM/4.0.0}DeploymentRepository" minOccurs="0"/>
 *         &lt;element name="site" type="{http://maven.apache.org/POM/4.0.0}Site" minOccurs="0"/>
 *         &lt;element name="downloadUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="relocation" type="{http://maven.apache.org/POM/4.0.0}Relocation" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DistributionManagement", propOrder = {

})
public class DistributionManagement {

    protected DeploymentRepository repository;
    protected DeploymentRepository snapshotRepository;
    protected Site site;
    protected String downloadUrl;
    protected Relocation relocation;
    protected String status;

    /**
     * Ruft den Wert der repository-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DeploymentRepository }
     *     
     */
    public DeploymentRepository getRepository() {
        return repository;
    }

    /**
     * Legt den Wert der repository-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DeploymentRepository }
     *     
     */
    public void setRepository(DeploymentRepository value) {
        this.repository = value;
    }

    /**
     * Ruft den Wert der snapshotRepository-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DeploymentRepository }
     *     
     */
    public DeploymentRepository getSnapshotRepository() {
        return snapshotRepository;
    }

    /**
     * Legt den Wert der snapshotRepository-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DeploymentRepository }
     *     
     */
    public void setSnapshotRepository(DeploymentRepository value) {
        this.snapshotRepository = value;
    }

    /**
     * Ruft den Wert der site-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Site }
     *     
     */
    public Site getSite() {
        return site;
    }

    /**
     * Legt den Wert der site-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Site }
     *     
     */
    public void setSite(Site value) {
        this.site = value;
    }

    /**
     * Ruft den Wert der downloadUrl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * Legt den Wert der downloadUrl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDownloadUrl(String value) {
        this.downloadUrl = value;
    }

    /**
     * Ruft den Wert der relocation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Relocation }
     *     
     */
    public Relocation getRelocation() {
        return relocation;
    }

    /**
     * Legt den Wert der relocation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Relocation }
     *     
     */
    public void setRelocation(Relocation value) {
        this.relocation = value;
    }

    /**
     * Ruft den Wert der status-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Legt den Wert der status-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

}
