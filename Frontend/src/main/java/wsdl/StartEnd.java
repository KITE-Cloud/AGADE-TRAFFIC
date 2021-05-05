
package wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for startEnd complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="startEnd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="desiredArrivalTick" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="desiredArrivalTickList" type="{http://service/}dttBean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="end" type="{http://service/}location" minOccurs="0"/>
 *         &lt;element name="numberOfCars" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numberOfCarsDTT" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numberOfCarsPoisson" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numberOfCarsPoissonAC" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="start" type="{http://service/}location" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "startEnd", propOrder = {
    "desiredArrivalTick",
    "desiredArrivalTickList",
    "end",
    "numberOfCars",
    "numberOfCarsDTT",
    "numberOfCarsPoisson",
    "numberOfCarsPoissonAC",
    "start"
})
public class StartEnd {

    protected int desiredArrivalTick;
    @XmlElement(nillable = true)
    protected List<DttBean> desiredArrivalTickList;
    protected Location end;
    protected int numberOfCars;
    protected int numberOfCarsDTT;
    protected int numberOfCarsPoisson;
    protected int numberOfCarsPoissonAC;
    protected Location start;

    /**
     * Gets the value of the desiredArrivalTick property.
     * 
     */
    public int getDesiredArrivalTick() {
        return desiredArrivalTick;
    }

    /**
     * Sets the value of the desiredArrivalTick property.
     * 
     */
    public void setDesiredArrivalTick(int value) {
        this.desiredArrivalTick = value;
    }

    /**
     * Gets the value of the desiredArrivalTickList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the desiredArrivalTickList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDesiredArrivalTickList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DttBean }
     * 
     * 
     */
    public List<DttBean> getDesiredArrivalTickList() {
        if (desiredArrivalTickList == null) {
            desiredArrivalTickList = new ArrayList<DttBean>();
        }
        return this.desiredArrivalTickList;
    }

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setEnd(Location value) {
        this.end = value;
    }

    /**
     * Gets the value of the numberOfCars property.
     * 
     */
    public int getNumberOfCars() {
        return numberOfCars;
    }

    /**
     * Sets the value of the numberOfCars property.
     * 
     */
    public void setNumberOfCars(int value) {
        this.numberOfCars = value;
    }

    /**
     * Gets the value of the numberOfCarsDTT property.
     * 
     */
    public int getNumberOfCarsDTT() {
        return numberOfCarsDTT;
    }

    /**
     * Sets the value of the numberOfCarsDTT property.
     * 
     */
    public void setNumberOfCarsDTT(int value) {
        this.numberOfCarsDTT = value;
    }

    /**
     * Gets the value of the numberOfCarsPoisson property.
     * 
     */
    public int getNumberOfCarsPoisson() {
        return numberOfCarsPoisson;
    }

    /**
     * Sets the value of the numberOfCarsPoisson property.
     * 
     */
    public void setNumberOfCarsPoisson(int value) {
        this.numberOfCarsPoisson = value;
    }

    /**
     * Gets the value of the numberOfCarsPoissonAC property.
     * 
     */
    public int getNumberOfCarsPoissonAC() {
        return numberOfCarsPoissonAC;
    }

    /**
     * Sets the value of the numberOfCarsPoissonAC property.
     * 
     */
    public void setNumberOfCarsPoissonAC(int value) {
        this.numberOfCarsPoissonAC = value;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setStart(Location value) {
        this.start = value;
    }

}
