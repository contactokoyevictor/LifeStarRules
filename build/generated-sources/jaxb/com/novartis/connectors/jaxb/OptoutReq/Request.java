//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.04 at 09:33:13 AM GMT 
//


package com.novartis.connectors.jaxb.OptoutReq;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="optouts">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="consumer" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="mobilenumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="consumerid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                             &lt;element name="campaignid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                             &lt;element name="listid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "optouts"
})
@XmlRootElement(name = "request")
public class Request {

    @XmlElement(required = true)
    protected Request.Optouts optouts;

    /**
     * Gets the value of the optouts property.
     * 
     * @return
     *     possible object is
     *     {@link Request.Optouts }
     *     
     */
    public Request.Optouts getOptouts() {
        return optouts;
    }

    /**
     * Sets the value of the optouts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request.Optouts }
     *     
     */
    public void setOptouts(Request.Optouts value) {
        this.optouts = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="consumer" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="mobilenumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="consumerid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *                   &lt;element name="campaignid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *                   &lt;element name="listid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "consumer"
    })
    public static class Optouts {

        @XmlElement(required = true)
        protected List<Request.Optouts.Consumer> consumer;

        /**
         * Gets the value of the consumer property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the consumer property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getConsumer().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Request.Optouts.Consumer }
         * 
         * 
         */
        public List<Request.Optouts.Consumer> getConsumer() {
            if (consumer == null) {
                consumer = new ArrayList<Request.Optouts.Consumer>();
            }
            return this.consumer;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="mobilenumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="consumerid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
         *         &lt;element name="campaignid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
         *         &lt;element name="listid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
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
            "mobilenumber",
            "consumerid",
            "campaignid",
            "listid"
        })
        public static class Consumer {

            @XmlElement(required = true)
            protected String mobilenumber;
            @XmlElement(required = true)
            protected BigInteger consumerid;
            @XmlElement(required = true)
            protected BigInteger campaignid;
            @XmlElement(required = true)
            protected BigInteger listid;

            /**
             * Gets the value of the mobilenumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMobilenumber() {
                return mobilenumber;
            }

            /**
             * Sets the value of the mobilenumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMobilenumber(String value) {
                this.mobilenumber = value;
            }

            /**
             * Gets the value of the consumerid property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getConsumerid() {
                return consumerid;
            }

            /**
             * Sets the value of the consumerid property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setConsumerid(BigInteger value) {
                this.consumerid = value;
            }

            /**
             * Gets the value of the campaignid property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getCampaignid() {
                return campaignid;
            }

            /**
             * Sets the value of the campaignid property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setCampaignid(BigInteger value) {
                this.campaignid = value;
            }

            /**
             * Gets the value of the listid property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getListid() {
                return listid;
            }

            /**
             * Sets the value of the listid property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setListid(BigInteger value) {
                this.listid = value;
            }

        }

    }

}
