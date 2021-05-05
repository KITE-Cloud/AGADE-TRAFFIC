
package wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for configurations complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="configurations">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agentGeneratorType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationMatrix" type="{http://service/}startEnd" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dumpDestination" type="{http://service/}dumpDestination" minOccurs="0"/>
 *         &lt;element name="finalDestinations" type="{http://service/}finalDestination" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="fromPopulation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="lastTickInSimulation" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="personaProfiles" type="{http://service/}personaProfile" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="populationConfig" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="simulationTime" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="trafficActionObjects" type="{http://service/}trafficActionObject" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configurations", propOrder = {
    "agentGeneratorType",
    "destinationMatrix",
    "dumpDestination",
    "finalDestinations",
    "fromPopulation",
    "lastTickInSimulation",
    "personaProfiles",
    "populationConfig",
    "simulationTime",
    "trafficActionObjects"
})
public class Configurations {

    protected String agentGeneratorType;
    @XmlElement(nillable = true)
    protected List<StartEnd> destinationMatrix;
    protected DumpDestination dumpDestination;
    @XmlElement(nillable = true)
    protected List<FinalDestination> finalDestinations;
    protected boolean fromPopulation;
    protected int lastTickInSimulation;
    @XmlElement(nillable = true)
    protected List<PersonaProfile> personaProfiles;
    protected String populationConfig;
    protected int simulationTime;
    @XmlElement(nillable = true)
    protected List<TrafficActionObject> trafficActionObjects;

    /**
     * Gets the value of the agentGeneratorType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentGeneratorType() {
        return agentGeneratorType;
    }

    /**
     * Sets the value of the agentGeneratorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentGeneratorType(String value) {
        this.agentGeneratorType = value;
    }

    /**
     * Gets the value of the destinationMatrix property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the destinationMatrix property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDestinationMatrix().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StartEnd }
     * 
     * 
     */
    public List<StartEnd> getDestinationMatrix() {
        if (destinationMatrix == null) {
            destinationMatrix = new ArrayList<StartEnd>();
        }
        return this.destinationMatrix;
    }

    /**
     * Gets the value of the dumpDestination property.
     * 
     * @return
     *     possible object is
     *     {@link DumpDestination }
     *     
     */
    public DumpDestination getDumpDestination() {
        return dumpDestination;
    }

    /**
     * Sets the value of the dumpDestination property.
     * 
     * @param value
     *     allowed object is
     *     {@link DumpDestination }
     *     
     */
    public void setDumpDestination(DumpDestination value) {
        this.dumpDestination = value;
    }

    /**
     * Gets the value of the finalDestinations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the finalDestinations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFinalDestinations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FinalDestination }
     * 
     * 
     */
    public List<FinalDestination> getFinalDestinations() {
        if (finalDestinations == null) {
            finalDestinations = new ArrayList<FinalDestination>();
        }
        return this.finalDestinations;
    }

    /**
     * Gets the value of the fromPopulation property.
     * 
     */
    public boolean isFromPopulation() {
        return fromPopulation;
    }

    /**
     * Sets the value of the fromPopulation property.
     * 
     */
    public void setFromPopulation(boolean value) {
        this.fromPopulation = value;
    }

    /**
     * Gets the value of the lastTickInSimulation property.
     * 
     */
    public int getLastTickInSimulation() {
        return lastTickInSimulation;
    }

    /**
     * Sets the value of the lastTickInSimulation property.
     * 
     */
    public void setLastTickInSimulation(int value) {
        this.lastTickInSimulation = value;
    }

    /**
     * Gets the value of the personaProfiles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personaProfiles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonaProfiles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersonaProfile }
     * 
     * 
     */
    public List<PersonaProfile> getPersonaProfiles() {
        if (personaProfiles == null) {
            personaProfiles = new ArrayList<PersonaProfile>();
        }
        return this.personaProfiles;
    }

    /**
     * Gets the value of the populationConfig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPopulationConfig() {
        return populationConfig;
    }

    /**
     * Sets the value of the populationConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPopulationConfig(String value) {
        this.populationConfig = value;
    }

    /**
     * Gets the value of the simulationTime property.
     * 
     */
    public int getSimulationTime() {
        return simulationTime;
    }

    /**
     * Sets the value of the simulationTime property.
     * 
     */
    public void setSimulationTime(int value) {
        this.simulationTime = value;
    }

    /**
     * Gets the value of the trafficActionObjects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trafficActionObjects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrafficActionObjects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TrafficActionObject }
     * 
     * 
     */
    public List<TrafficActionObject> getTrafficActionObjects() {
        if (trafficActionObjects == null) {
            trafficActionObjects = new ArrayList<TrafficActionObject>();
        }
        return this.trafficActionObjects;
    }

}
