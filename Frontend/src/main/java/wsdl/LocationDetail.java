
package wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for locationDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="locationDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bikesParked" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="directParkedCars" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="directParkingSlots" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="location" type="{http://service/}location" minOccurs="0"/>
 *         &lt;element name="locationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parkingArea" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="simId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="surroundingParkedCars" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="surroundingParkingSlots" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "locationDetail", propOrder = {
    "bikesParked",
    "directParkedCars",
    "directParkingSlots",
    "location",
    "locationType",
    "parkingArea",
    "simId",
    "surroundingParkedCars",
    "surroundingParkingSlots"
})
public class LocationDetail {

    protected int bikesParked;
    protected int directParkedCars;
    protected int directParkingSlots;
    protected Location location;
    protected String locationType;
    protected double parkingArea;
    protected String simId;
    protected int surroundingParkedCars;
    protected int surroundingParkingSlots;

    /**
     * Gets the value of the bikesParked property.
     * 
     */
    public int getBikesParked() {
        return bikesParked;
    }

    /**
     * Sets the value of the bikesParked property.
     * 
     */
    public void setBikesParked(int value) {
        this.bikesParked = value;
    }

    /**
     * Gets the value of the directParkedCars property.
     * 
     */
    public int getDirectParkedCars() {
        return directParkedCars;
    }

    /**
     * Sets the value of the directParkedCars property.
     * 
     */
    public void setDirectParkedCars(int value) {
        this.directParkedCars = value;
    }

    /**
     * Gets the value of the directParkingSlots property.
     * 
     */
    public int getDirectParkingSlots() {
        return directParkingSlots;
    }

    /**
     * Sets the value of the directParkingSlots property.
     * 
     */
    public void setDirectParkingSlots(int value) {
        this.directParkingSlots = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the locationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * Sets the value of the locationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationType(String value) {
        this.locationType = value;
    }

    /**
     * Gets the value of the parkingArea property.
     * 
     */
    public double getParkingArea() {
        return parkingArea;
    }

    /**
     * Sets the value of the parkingArea property.
     * 
     */
    public void setParkingArea(double value) {
        this.parkingArea = value;
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

    /**
     * Gets the value of the surroundingParkedCars property.
     * 
     */
    public int getSurroundingParkedCars() {
        return surroundingParkedCars;
    }

    /**
     * Sets the value of the surroundingParkedCars property.
     * 
     */
    public void setSurroundingParkedCars(int value) {
        this.surroundingParkedCars = value;
    }

    /**
     * Gets the value of the surroundingParkingSlots property.
     * 
     */
    public int getSurroundingParkingSlots() {
        return surroundingParkingSlots;
    }

    /**
     * Sets the value of the surroundingParkingSlots property.
     * 
     */
    public void setSurroundingParkingSlots(int value) {
        this.surroundingParkingSlots = value;
    }

}
