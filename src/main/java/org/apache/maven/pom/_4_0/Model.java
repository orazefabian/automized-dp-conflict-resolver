//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.04 um 10:04:23 PM CET 
//


package org.apache.maven.pom._4_0;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 *         
 *         The <code>&lt;project&gt;</code> element is the root of the descriptor.
 *         The following table lists all of the possible child elements.
 *         
 *       
 * 
 * <p>Java-Klasse für Model complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Model">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="modelVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parent" type="{http://maven.apache.org/POM/4.0.0}Parent" minOccurs="0"/>
 *         &lt;element name="groupId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="artifactId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="packaging" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inceptionYear" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organization" type="{http://maven.apache.org/POM/4.0.0}Organization" minOccurs="0"/>
 *         &lt;element name="licenses" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="license" type="{http://maven.apache.org/POM/4.0.0}License" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="developers" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="developer" type="{http://maven.apache.org/POM/4.0.0}Developer" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="contributors" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="contributor" type="{http://maven.apache.org/POM/4.0.0}Contributor" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="mailingLists" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="mailingList" type="{http://maven.apache.org/POM/4.0.0}MailingList" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="prerequisites" type="{http://maven.apache.org/POM/4.0.0}Prerequisites" minOccurs="0"/>
 *         &lt;element name="modules" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="module" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="scm" type="{http://maven.apache.org/POM/4.0.0}Scm" minOccurs="0"/>
 *         &lt;element name="issueManagement" type="{http://maven.apache.org/POM/4.0.0}IssueManagement" minOccurs="0"/>
 *         &lt;element name="ciManagement" type="{http://maven.apache.org/POM/4.0.0}CiManagement" minOccurs="0"/>
 *         &lt;element name="distributionManagement" type="{http://maven.apache.org/POM/4.0.0}DistributionManagement" minOccurs="0"/>
 *         &lt;element name="properties" minOccurs="0">
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
 *         &lt;element name="dependencyManagement" type="{http://maven.apache.org/POM/4.0.0}DependencyManagement" minOccurs="0"/>
 *         &lt;element name="dependencies" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="dependency" type="{http://maven.apache.org/POM/4.0.0}Dependency" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="repositories" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="repository" type="{http://maven.apache.org/POM/4.0.0}Repository" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="pluginRepositories" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="pluginRepository" type="{http://maven.apache.org/POM/4.0.0}Repository" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="build" type="{http://maven.apache.org/POM/4.0.0}Build" minOccurs="0"/>
 *         &lt;element name="reports" minOccurs="0">
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
 *         &lt;element name="reporting" type="{http://maven.apache.org/POM/4.0.0}Reporting" minOccurs="0"/>
 *         &lt;element name="profiles" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="profile" type="{http://maven.apache.org/POM/4.0.0}Profile" maxOccurs="unbounded" minOccurs="0"/>
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
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Model", propOrder = {
})
public class Model {

    protected String modelVersion;
    protected Parent parent;
    protected String groupId;
    protected String artifactId;
    protected String version;
    @XmlElement(defaultValue = "jar")
    protected String packaging;
    protected String name;
    protected String description;
    protected String url;
    protected String inceptionYear;
    protected Organization organization;
    protected Licenses licenses;
    protected Developers developers;
    protected Contributors contributors;
    protected MailingLists mailingLists;
    protected Prerequisites prerequisites;
    protected Modules modules;
    protected Scm scm;
    protected IssueManagement issueManagement;
    protected CiManagement ciManagement;
    protected DistributionManagement distributionManagement;
    @JsonIgnore
    protected Properties properties;
    protected DependencyManagement dependencyManagement;
    protected Dependencies dependencies;
    protected Repositories repositories;
    protected PluginRepositories pluginRepositories;
    protected Build build;
    protected Reports reports;
    protected Reporting reporting;
    protected Profiles profiles;

    /**
     * Ruft den Wert der modelVersion-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getModelVersion() {
        return modelVersion;
    }

    /**
     * Legt den Wert der modelVersion-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setModelVersion(String value) {
        this.modelVersion = value;
    }

    /**
     * Ruft den Wert der parent-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Parent }
     *
     */
    public Parent getParent() {
        return parent;
    }

    /**
     * Legt den Wert der parent-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Parent }
     *
     */
    public void setParent(Parent value) {
        this.parent = value;
    }

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
     * Ruft den Wert der packaging-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPackaging() {
        return packaging;
    }

    /**
     * Legt den Wert der packaging-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPackaging(String value) {
        this.packaging = value;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der description-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Legt den Wert der description-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Ruft den Wert der url-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUrl() {
        return url;
    }

    /**
     * Legt den Wert der url-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Ruft den Wert der inceptionYear-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInceptionYear() {
        return inceptionYear;
    }

    /**
     * Legt den Wert der inceptionYear-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInceptionYear(String value) {
        this.inceptionYear = value;
    }

    /**
     * Ruft den Wert der organization-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Organization }
     *
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * Legt den Wert der organization-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Organization }
     *
     */
    public void setOrganization(Organization value) {
        this.organization = value;
    }

    /**
     * Ruft den Wert der licenses-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Licenses }
     *
     */
    public Licenses getLicenses() {
        return licenses;
    }

    /**
     * Legt den Wert der licenses-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Licenses }
     *
     */
    public void setLicenses(Licenses value) {
        this.licenses = value;
    }

    /**
     * Ruft den Wert der developers-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Developers }
     *
     */
    public Developers getDevelopers() {
        return developers;
    }

    /**
     * Legt den Wert der developers-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Developers }
     *
     */
    public void setDevelopers(Developers value) {
        this.developers = value;
    }

    /**
     * Ruft den Wert der contributors-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Contributors }
     *
     */
    public Contributors getContributors() {
        return contributors;
    }

    /**
     * Legt den Wert der contributors-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Contributors }
     *
     */
    public void setContributors(Contributors value) {
        this.contributors = value;
    }

    /**
     * Ruft den Wert der mailingLists-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link MailingLists }
     *
     */
    public MailingLists getMailingLists() {
        return mailingLists;
    }

    /**
     * Legt den Wert der mailingLists-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link MailingLists }
     *
     */
    public void setMailingLists(MailingLists value) {
        this.mailingLists = value;
    }

    /**
     * Ruft den Wert der prerequisites-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Prerequisites }
     *
     */
    public Prerequisites getPrerequisites() {
        return prerequisites;
    }

    /**
     * Legt den Wert der prerequisites-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Prerequisites }
     *
     */
    public void setPrerequisites(Prerequisites value) {
        this.prerequisites = value;
    }

    /**
     * Ruft den Wert der modules-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Modules }
     *
     */
    public Modules getModules() {
        return modules;
    }

    /**
     * Legt den Wert der modules-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Modules }
     *
     */
    public void setModules(Modules value) {
        this.modules = value;
    }

    /**
     * Ruft den Wert der scm-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Scm }
     *
     */
    public Scm getScm() {
        return scm;
    }

    /**
     * Legt den Wert der scm-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Scm }
     *
     */
    public void setScm(Scm value) {
        this.scm = value;
    }

    /**
     * Ruft den Wert der issueManagement-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link IssueManagement }
     *
     */
    public IssueManagement getIssueManagement() {
        return issueManagement;
    }

    /**
     * Legt den Wert der issueManagement-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link IssueManagement }
     *
     */
    public void setIssueManagement(IssueManagement value) {
        this.issueManagement = value;
    }

    /**
     * Ruft den Wert der ciManagement-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link CiManagement }
     *
     */
    public CiManagement getCiManagement() {
        return ciManagement;
    }

    /**
     * Legt den Wert der ciManagement-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link CiManagement }
     *
     */
    public void setCiManagement(CiManagement value) {
        this.ciManagement = value;
    }

    /**
     * Ruft den Wert der distributionManagement-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link DistributionManagement }
     *
     */
    public DistributionManagement getDistributionManagement() {
        return distributionManagement;
    }

    /**
     * Legt den Wert der distributionManagement-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link DistributionManagement }
     *
     */
    public void setDistributionManagement(DistributionManagement value) {
        this.distributionManagement = value;
    }

    /**
     * Ruft den Wert der properties-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Properties }
     *
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Legt den Wert der properties-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Properties }
     *
     */
    public void setProperties(Properties value) {
        this.properties = value;
    }

    /**
     * Ruft den Wert der dependencyManagement-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link DependencyManagement }
     *
     */
    public DependencyManagement getDependencyManagement() {
        return dependencyManagement;
    }

    /**
     * Legt den Wert der dependencyManagement-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link DependencyManagement }
     *
     */
    public void setDependencyManagement(DependencyManagement value) {
        this.dependencyManagement = value;
    }

    /**
     * Ruft den Wert der dependencies-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Dependencies }
     *
     */
    public Dependencies getDependencies() {
        return dependencies;
    }

    /**
     * Legt den Wert der dependencies-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Dependencies }
     *
     */
    public void setDependencies(Dependencies value) {
        this.dependencies = value;
    }

    /**
     * Ruft den Wert der repositories-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Repositories }
     *
     */
    public Repositories getRepositories() {
        return repositories;
    }

    /**
     * Legt den Wert der repositories-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Repositories }
     *
     */
    public void setRepositories(Repositories value) {
        this.repositories = value;
    }

    /**
     * Ruft den Wert der pluginRepositories-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link PluginRepositories }
     *
     */
    public PluginRepositories getPluginRepositories() {
        return pluginRepositories;
    }

    /**
     * Legt den Wert der pluginRepositories-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link PluginRepositories }
     *
     */
    public void setPluginRepositories(PluginRepositories value) {
        this.pluginRepositories = value;
    }

    /**
     * Ruft den Wert der build-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Build }
     *
     */
    public Build getBuild() {
        return build;
    }

    /**
     * Legt den Wert der build-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Build }
     *
     */
    public void setBuild(Build value) {
        this.build = value;
    }

    /**
     * Ruft den Wert der reports-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Reports }
     *
     */
    public Reports getReports() {
        return reports;
    }

    /**
     * Legt den Wert der reports-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Reports }
     *
     */
    public void setReports(Reports value) {
        this.reports = value;
    }

    /**
     * Ruft den Wert der reporting-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Reporting }
     *
     */
    public Reporting getReporting() {
        return reporting;
    }

    /**
     * Legt den Wert der reporting-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Reporting }
     *
     */
    public void setReporting(Reporting value) {
        this.reporting = value;
    }

    /**
     * Ruft den Wert der profiles-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Profiles }
     *
     */
    public Profiles getProfiles() {
        return profiles;
    }

    /**
     * Legt den Wert der profiles-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Profiles }
     *
     */
    public void setProfiles(Profiles value) {
        this.profiles = value;
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
     *         &lt;element name="contributor" type="{http://maven.apache.org/POM/4.0.0}Contributor" maxOccurs="unbounded" minOccurs="0"/>
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
        "contributor"
    })
    public static class Contributors {

        protected List<Contributor> contributor;

        /**
         * Gets the value of the contributor property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the contributor property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContributor().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Contributor }
         * 
         * 
         */
        public List<Contributor> getContributor() {
            if (contributor == null) {
                contributor = new ArrayList<Contributor>();
            }
            return this.contributor;
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
     *         &lt;element name="dependency" type="{http://maven.apache.org/POM/4.0.0}Dependency" maxOccurs="unbounded" minOccurs="0"/>
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
        "dependency"
    })
    public static class Dependencies {

        protected List<Dependency> dependency;

        /**
         * Gets the value of the dependency property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dependency property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDependency().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Dependency }
         * 
         * 
         */
        public List<Dependency> getDependency() {
            if (dependency == null) {
                dependency = new ArrayList<Dependency>();
            }
            return this.dependency;
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
     *         &lt;element name="developer" type="{http://maven.apache.org/POM/4.0.0}Developer" maxOccurs="unbounded" minOccurs="0"/>
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
        "developer"
    })
    public static class Developers {

        protected List<Developer> developer;

        /**
         * Gets the value of the developer property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the developer property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDeveloper().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Developer }
         * 
         * 
         */
        public List<Developer> getDeveloper() {
            if (developer == null) {
                developer = new ArrayList<Developer>();
            }
            return this.developer;
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
     *         &lt;element name="license" type="{http://maven.apache.org/POM/4.0.0}License" maxOccurs="unbounded" minOccurs="0"/>
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
        "license"
    })
    public static class Licenses {

        protected List<License> license;

        /**
         * Gets the value of the license property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the license property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLicense().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link License }
         * 
         * 
         */
        public List<License> getLicense() {
            if (license == null) {
                license = new ArrayList<License>();
            }
            return this.license;
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
     *         &lt;element name="mailingList" type="{http://maven.apache.org/POM/4.0.0}MailingList" maxOccurs="unbounded" minOccurs="0"/>
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
        "mailingList"
    })
    public static class MailingLists {

        protected List<MailingList> mailingList;

        /**
         * Gets the value of the mailingList property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the mailingList property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMailingList().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MailingList }
         * 
         * 
         */
        public List<MailingList> getMailingList() {
            if (mailingList == null) {
                mailingList = new ArrayList<MailingList>();
            }
            return this.mailingList;
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
     *         &lt;element name="module" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
        "module"
    })
    public static class Modules {

        protected List<String> module;

        /**
         * Gets the value of the module property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the module property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getModule().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getModule() {
            if (module == null) {
                module = new ArrayList<String>();
            }
            return this.module;
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
     *         &lt;element name="pluginRepository" type="{http://maven.apache.org/POM/4.0.0}Repository" maxOccurs="unbounded" minOccurs="0"/>
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
        "pluginRepository"
    })
    public static class PluginRepositories {

        protected List<Repository> pluginRepository;

        /**
         * Gets the value of the pluginRepository property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the pluginRepository property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPluginRepository().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Repository }
         * 
         * 
         */
        public List<Repository> getPluginRepository() {
            if (pluginRepository == null) {
                pluginRepository = new ArrayList<Repository>();
            }
            return this.pluginRepository;
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
     *         &lt;element name="profile" type="{http://maven.apache.org/POM/4.0.0}Profile" maxOccurs="unbounded" minOccurs="0"/>
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
        "profile"
    })
    public static class Profiles {

        protected List<Profile> profile;

        /**
         * Gets the value of the profile property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the profile property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProfile().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Profile }
         * 
         * 
         */
        public List<Profile> getProfile() {
            if (profile == null) {
                profile = new ArrayList<Profile>();
            }
            return this.profile;
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
    public static class Properties {

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
    public static class Reports {

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
     *         &lt;element name="repository" type="{http://maven.apache.org/POM/4.0.0}Repository" maxOccurs="unbounded" minOccurs="0"/>
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
        "repository"
    })
    public static class Repositories {

        protected List<Repository> repository;

        /**
         * Gets the value of the repository property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the repository property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRepository().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Repository }
         * 
         * 
         */
        public List<Repository> getRepository() {
            if (repository == null) {
                repository = new ArrayList<Repository>();
            }
            return this.repository;
        }

    }

}
