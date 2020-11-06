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
 * Modifications to the build process which is activated based on environmental
 *         parameters or command line arguments.
 * 
 * <p>Java-Klasse für Profile complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Profile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="activation" type="{http://maven.apache.org/POM/4.0.0}Activation" minOccurs="0"/>
 *         &lt;element name="build" type="{http://maven.apache.org/POM/4.0.0}BuildBase" minOccurs="0"/>
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
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Profile", propOrder = {

})
public class Profile {

    @XmlElement(defaultValue = "default")
    protected String id;
    protected Activation activation;
    protected BuildBase build;
    protected Modules modules;
    protected DistributionManagement distributionManagement;
    protected Properties properties;
    protected DependencyManagement dependencyManagement;
    protected Dependencies dependencies;
    protected Repositories repositories;
    protected PluginRepositories pluginRepositories;
    protected Reports reports;
    protected Reporting reporting;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der activation-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Activation }
     *
     */
    public Activation getActivation() {
        return activation;
    }

    /**
     * Legt den Wert der activation-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Activation }
     *
     */
    public void setActivation(Activation value) {
        this.activation = value;
    }

    /**
     * Ruft den Wert der build-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link BuildBase }
     *
     */
    public BuildBase getBuild() {
        return build;
    }

    /**
     * Legt den Wert der build-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link BuildBase }
     *
     */
    public void setBuild(BuildBase value) {
        this.build = value;
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
