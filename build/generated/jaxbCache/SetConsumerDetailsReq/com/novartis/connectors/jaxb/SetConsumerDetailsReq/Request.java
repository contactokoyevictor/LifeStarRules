//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.04 at 09:33:04 AM GMT 
//


package com.novartis.connectors.jaxb.SetConsumerDetailsReq;

import java.math.BigInteger;
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
 *         &lt;element name="consumer">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="campaignid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="consumerid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="lat" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                   &lt;element name="lon" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                   &lt;element name="geofences">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="geofence">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="geofenceid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
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
    "consumer"
})
@XmlRootElement(name = "request")
public class Request {

    @XmlElement(required = true)
    protected Request.Consumer consumer;

    /**
     * Gets the value of the consumer property.
     * 
     * @return
     *     possible object is
     *     {@link Request.Consumer }
     *     
     */
    public Request.Consumer getConsumer() {
        return consumer;
    }

    /**
     * Sets the value of the consumer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request.Consumer }
     *     
     */
    public void setConsumer(Request.Consumer value) {
        this.consumer = value;
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
     *         &lt;element name="campaignid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="consumerid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="lat" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *         &lt;element name="lon" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *         &lt;element name="geofences">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="geofence">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="geofenceid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
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
        "campaignid",
        "consumerid",
        "lat",
        "lon",
        "geofences"
    })
    public static class Consumer {

        @XmlElement(required = true)
        protected BigInteger campaignid;
        @XmlElement(required = true)
        protected BigInteger consumerid;
        @XmlElement(required = true)
        protected Object lat;
        @XmlElement(required = true)
        protected Object lon;
        @XmlElement(required = true)
        protected Request.Consumer.Geofences geofences;

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
         * Gets the value of the lat property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getLat() {
            return lat;
        }

        /**
         * Sets the value of the lat property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setLat(Object value) {
            this.lat = value;
        }

        /**
         * Gets the value of the lon property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getLon() {
            return lon;
        }

        /**
         * Sets the value of the lon property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setLon(Object value) {
            this.lon = value;
        }

        /**
         * Gets the value of the geofences property.
         * 
         * @return
         *     possible object is
         *     {@link Request.Consumer.Geofences }
         *     
         */
        public Request.Consumer.Geofences getGeofences() {
            return geofences;
        }

        /**
         * Sets the value of the geofences property.
         * 
         * @param value
         *     allowed object is
         *     {@link Request.Consumer.Geofences }
         *     
         */
        public void setGeofences(Request.Consumer.Geofences value) {
            this.geofences = value;
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
         *         &lt;element name="geofence">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="geofenceid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
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
            "geofence"
        })
        public static class Geofences {

            @XmlElement(required = true)
            protected Request.Consumer.Geofences.Geofence geofence;

            /**
             * Gets the value of the geofence property.
             * 
             * @return
             *     possible object is
             *     {@link Request.Consumer.Geofences.Geofence }
             *     
             */
            public Request.Consumer.Geofences.Geofence getGeofence() {
                return geofence;
            }

            /**
             * Sets the value of the geofence property.
             * 
             * @param value
             *     allowed object is
             *     {@link Request.Consumer.Geofences.Geofence }
             *     
             */
            public void setGeofence(Request.Consumer.Geofences.Geofence value) {
                this.geofence = value;
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
             *         &lt;element name="geofenceid" type="{http://www.w3.org/2001/XMLSchema}integer"/>
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
                "geofenceid"
            })
            public static class Geofence {

                @XmlElement(required = true)
                protected BigInteger geofenceid;

                /**
                 * Gets the value of the geofenceid property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getGeofenceid() {
                    return geofenceid;
                }

                /**
                 * Sets the value of the geofenceid property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setGeofenceid(BigInteger value) {
                    this.geofenceid = value;
                }

            }

        }

    }

}
