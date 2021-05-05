
package wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for journey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="journey">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="totalTravelDistance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="totalUtilityScore" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="transportationMode" type="{http://service/}vehicle" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="trips" type="{http://service/}startEnd" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "journey", propOrder = {
    "totalTravelDistance",
    "totalUtilityScore",
    "transportationMode",
    "trips"
})
public class Journey {

    protected double totalTravelDistance;
    protected double totalUtilityScore;
    @XmlElement(nillable = true)
    protected List<Vehicle> transportationMode;
    @XmlElement(nillable = true)
    protected List<StartEnd> trips;

    /**
     * Gets the value of the totalTravelDistance property.
     * 
     */
    public double getTotalTravelDistance() {
        return totalTravelDistance;
    }

    /**
     * Sets the value of the totalTravelDistance property.
     * 
     */
    public void setTotalTravelDistance(double value) {
        this.totalTravelDistance = value;
    }

    /**
     * Gets the value of the totalUtilityScore property.
     * 
     */
    public double getTotalUtilityScore() {
        return totalUtilityScore;
    }

    /**
     * Sets the value of the totalUtilityScore property.
     * 
     */
    public void setTotalUtilityScore(double value) {
        this.totalUtilityScore = value;
    }

    /**
     * Gets the value of the transportationMode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transportationMode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransportationMode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Vehicle }
     * 
     * 
     */
    public List<Vehicle> getTransportationMode() {
        if (transportationMode == null) {
            transportationMode = new ArrayList<Vehicle>();
        }
        return this.transportationMode;
    }

    /**
     * Gets the value of the trips property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trips property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrips().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StartEnd }
     * 
     * 
     */
    public List<StartEnd> getTrips() {
        if (trips == null) {
            trips = new ArrayList<StartEnd>();
        }
        return this.trips;
    }

}
