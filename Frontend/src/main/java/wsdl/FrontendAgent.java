
package wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for frontendAgent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="frontendAgent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="birthTick" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="destination" type="{http://service/}location" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="journey" type="{http://service/}journey" minOccurs="0"/>
 *         &lt;element name="origin" type="{http://service/}location" minOccurs="0"/>
 *         &lt;element name="personaProfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="routeCoordinates" type="{http://service/}locationFrontend" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="routeDetails" type="{http://service/}routeDetail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="routeHasChanged">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://service/}routeChangeBean" minOccurs="0"/>
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
 *         &lt;element name="totalTravelTime" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="vehicle" type="{http://service/}vehicle" minOccurs="0"/>
 *         &lt;element name="velocities" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "frontendAgent", propOrder = {
    "birthTick",
    "destination",
    "id",
    "journey",
    "origin",
    "personaProfile",
    "routeCoordinates",
    "routeDetails",
    "routeHasChanged",
    "totalTravelTime",
    "vehicle",
    "velocities"
})
public class FrontendAgent {

    protected Integer birthTick;
    protected Location destination;
    protected int id;
    protected Journey journey;
    protected Location origin;
    protected String personaProfile;
    @XmlElement(nillable = true)
    protected List<LocationFrontend> routeCoordinates;
    @XmlElement(nillable = true)
    protected List<RouteDetail> routeDetails;
    @XmlElement(required = true)
    protected FrontendAgent.RouteHasChanged routeHasChanged;
    protected int totalTravelTime;
    protected Vehicle vehicle;
    @XmlElement(nillable = true)
    protected List<Integer> velocities;

    /**
     * Gets the value of the birthTick property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBirthTick() {
        return birthTick;
    }

    /**
     * Sets the value of the birthTick property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBirthTick(Integer value) {
        this.birthTick = value;
    }

    /**
     * Gets the value of the destination property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getDestination() {
        return destination;
    }

    /**
     * Sets the value of the destination property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setDestination(Location value) {
        this.destination = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the journey property.
     * 
     * @return
     *     possible object is
     *     {@link Journey }
     *     
     */
    public Journey getJourney() {
        return journey;
    }

    /**
     * Sets the value of the journey property.
     * 
     * @param value
     *     allowed object is
     *     {@link Journey }
     *     
     */
    public void setJourney(Journey value) {
        this.journey = value;
    }

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setOrigin(Location value) {
        this.origin = value;
    }

    /**
     * Gets the value of the personaProfile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonaProfile() {
        return personaProfile;
    }

    /**
     * Sets the value of the personaProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonaProfile(String value) {
        this.personaProfile = value;
    }

    /**
     * Gets the value of the routeCoordinates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the routeCoordinates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRouteCoordinates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocationFrontend }
     * 
     * 
     */
    public List<LocationFrontend> getRouteCoordinates() {
        if (routeCoordinates == null) {
            routeCoordinates = new ArrayList<LocationFrontend>();
        }
        return this.routeCoordinates;
    }

    /**
     * Gets the value of the routeDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the routeDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRouteDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RouteDetail }
     * 
     * 
     */
    public List<RouteDetail> getRouteDetails() {
        if (routeDetails == null) {
            routeDetails = new ArrayList<RouteDetail>();
        }
        return this.routeDetails;
    }

    /**
     * Gets the value of the routeHasChanged property.
     * 
     * @return
     *     possible object is
     *     {@link FrontendAgent.RouteHasChanged }
     *     
     */
    public FrontendAgent.RouteHasChanged getRouteHasChanged() {
        return routeHasChanged;
    }

    /**
     * Sets the value of the routeHasChanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link FrontendAgent.RouteHasChanged }
     *     
     */
    public void setRouteHasChanged(FrontendAgent.RouteHasChanged value) {
        this.routeHasChanged = value;
    }

    /**
     * Gets the value of the totalTravelTime property.
     * 
     */
    public int getTotalTravelTime() {
        return totalTravelTime;
    }

    /**
     * Sets the value of the totalTravelTime property.
     * 
     */
    public void setTotalTravelTime(int value) {
        this.totalTravelTime = value;
    }

    /**
     * Gets the value of the vehicle property.
     * 
     * @return
     *     possible object is
     *     {@link Vehicle }
     *     
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Sets the value of the vehicle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vehicle }
     *     
     */
    public void setVehicle(Vehicle value) {
        this.vehicle = value;
    }

    /**
     * Gets the value of the velocities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the velocities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVelocities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getVelocities() {
        if (velocities == null) {
            velocities = new ArrayList<Integer>();
        }
        return this.velocities;
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
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://service/}routeChangeBean" minOccurs="0"/>
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
        "entry"
    })
    public static class RouteHasChanged {

        protected List<FrontendAgent.RouteHasChanged.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FrontendAgent.RouteHasChanged.Entry }
         * 
         * 
         */
        public List<FrontendAgent.RouteHasChanged.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<FrontendAgent.RouteHasChanged.Entry>();
            }
            return this.entry;
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
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
         *         &lt;element name="value" type="{http://service/}routeChangeBean" minOccurs="0"/>
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
            "key",
            "value"
        })
        public static class Entry {

            protected Integer key;
            protected RouteChangeBean value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setKey(Integer value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link RouteChangeBean }
             *     
             */
            public RouteChangeBean getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link RouteChangeBean }
             *     
             */
            public void setValue(RouteChangeBean value) {
                this.value = value;
            }

        }

    }

}
