
package wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for simulation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="simulation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="simulationID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="agents" type="{http://service/}frontendAgent" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="currentTick" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="statisticalKPIs" type="{http://service/}statistics" minOccurs="0"/>
 *         &lt;element name="travelTimeDetails" type="{http://service/}travelTimeDetail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="locationDetails" type="{http://service/}locationDetail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="configurations" type="{http://service/}configurations" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "simulation", propOrder = {
    "simulationID",
    "agents",
    "currentTick",
    "statisticalKPIs",
    "travelTimeDetails",
    "locationDetails",
    "configurations"
})
public class Simulation {

    protected String simulationID;
    @XmlElement(nillable = true)
    protected List<FrontendAgent> agents;
    protected int currentTick;
    protected Statistics statisticalKPIs;
    @XmlElement(nillable = true)
    protected List<TravelTimeDetail> travelTimeDetails;
    @XmlElement(nillable = true)
    protected List<LocationDetail> locationDetails;
    protected Configurations configurations;

    /**
     * Gets the value of the simulationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimulationID() {
        return simulationID;
    }

    /**
     * Sets the value of the simulationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimulationID(String value) {
        this.simulationID = value;
    }

    /**
     * Gets the value of the agents property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agents property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FrontendAgent }
     * 
     * 
     */
    public List<FrontendAgent> getAgents() {
        if (agents == null) {
            agents = new ArrayList<FrontendAgent>();
        }
        return this.agents;
    }

    /**
     * Gets the value of the currentTick property.
     * 
     */
    public int getCurrentTick() {
        return currentTick;
    }

    /**
     * Sets the value of the currentTick property.
     * 
     */
    public void setCurrentTick(int value) {
        this.currentTick = value;
    }

    /**
     * Gets the value of the statisticalKPIs property.
     * 
     * @return
     *     possible object is
     *     {@link Statistics }
     *     
     */
    public Statistics getStatisticalKPIs() {
        return statisticalKPIs;
    }

    /**
     * Sets the value of the statisticalKPIs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Statistics }
     *     
     */
    public void setStatisticalKPIs(Statistics value) {
        this.statisticalKPIs = value;
    }

    /**
     * Gets the value of the travelTimeDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the travelTimeDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTravelTimeDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TravelTimeDetail }
     * 
     * 
     */
    public List<TravelTimeDetail> getTravelTimeDetails() {
        if (travelTimeDetails == null) {
            travelTimeDetails = new ArrayList<TravelTimeDetail>();
        }
        return this.travelTimeDetails;
    }

    /**
     * Gets the value of the locationDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locationDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocationDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocationDetail }
     * 
     * 
     */
    public List<LocationDetail> getLocationDetails() {
        if (locationDetails == null) {
            locationDetails = new ArrayList<LocationDetail>();
        }
        return this.locationDetails;
    }

    /**
     * Gets the value of the configurations property.
     * 
     * @return
     *     possible object is
     *     {@link Configurations }
     *     
     */
    public Configurations getConfigurations() {
        return configurations;
    }

    /**
     * Sets the value of the configurations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Configurations }
     *     
     */
    public void setConfigurations(Configurations value) {
        this.configurations = value;
    }

}
