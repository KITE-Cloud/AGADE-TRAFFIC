
package wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for travelTimeDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="travelTimeDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="avgTravelTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="driverType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locationFrom" type="{http://service/}location" minOccurs="0"/>
 *         &lt;element name="locationTo" type="{http://service/}location" minOccurs="0"/>
 *         &lt;element name="numberAgents" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="phase" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="simId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "travelTimeDetail", propOrder = {
    "avgTravelTime",
    "driverType",
    "locationFrom",
    "locationTo",
    "numberAgents",
    "phase",
    "simId"
})
public class TravelTimeDetail {

    protected double avgTravelTime;
    protected String driverType;
    protected Location locationFrom;
    protected Location locationTo;
    protected int numberAgents;
    protected int phase;
    protected String simId;

    /**
     * Gets the value of the avgTravelTime property.
     * 
     */
    public double getAvgTravelTime() {
        return avgTravelTime;
    }

    /**
     * Sets the value of the avgTravelTime property.
     * 
     */
    public void setAvgTravelTime(double value) {
        this.avgTravelTime = value;
    }

    /**
     * Gets the value of the driverType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDriverType() {
        return driverType;
    }

    /**
     * Sets the value of the driverType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDriverType(String value) {
        this.driverType = value;
    }

    /**
     * Gets the value of the locationFrom property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocationFrom() {
        return locationFrom;
    }

    /**
     * Sets the value of the locationFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocationFrom(Location value) {
        this.locationFrom = value;
    }

    /**
     * Gets the value of the locationTo property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocationTo() {
        return locationTo;
    }

    /**
     * Sets the value of the locationTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocationTo(Location value) {
        this.locationTo = value;
    }

    /**
     * Gets the value of the numberAgents property.
     * 
     */
    public int getNumberAgents() {
        return numberAgents;
    }

    /**
     * Sets the value of the numberAgents property.
     * 
     */
    public void setNumberAgents(int value) {
        this.numberAgents = value;
    }

    /**
     * Gets the value of the phase property.
     * 
     */
    public int getPhase() {
        return phase;
    }

    /**
     * Sets the value of the phase property.
     * 
     */
    public void setPhase(int value) {
        this.phase = value;
    }

    /**
     * Gets the value of the simId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimId() {
        return simId;
    }

    /**
     * Sets the value of the simId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimId(String value) {
        this.simId = value;
    }

}
