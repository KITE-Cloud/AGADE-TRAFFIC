
package wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for popAgentPojo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="popAgentPojo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="origin" type="{http://service/}location" minOccurs="0"/>
 *         &lt;element name="birthTick" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="persona" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="agentProperties" type="{http://service/}mapEntryAdapterObject" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="decisionFactors" type="{http://service/}mapEntryAdapter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="groceryList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "popAgentPojo", propOrder = {
    "id",
    "origin",
    "birthTick",
    "persona",
    "agentProperties",
    "decisionFactors",
    "groceryList"
})
public class PopAgentPojo {

    protected int id;
    protected Location origin;
    protected int birthTick;
    protected String persona;
    @XmlElement(nillable = true)
    protected List<MapEntryAdapterObject> agentProperties;
    @XmlElement(nillable = true)
    protected List<MapEntryAdapter> decisionFactors;
    @XmlElement(nillable = true)
    protected List<String> groceryList;

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
     * Gets the value of the birthTick property.
     * 
     */
    public int getBirthTick() {
        return birthTick;
    }

    /**
     * Sets the value of the birthTick property.
     * 
     */
    public void setBirthTick(int value) {
        this.birthTick = value;
    }

    /**
     * Gets the value of the persona property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersona() {
        return persona;
    }

    /**
     * Sets the value of the persona property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersona(String value) {
        this.persona = value;
    }

    /**
     * Gets the value of the agentProperties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agentProperties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgentProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MapEntryAdapterObject }
     * 
     * 
     */
    public List<MapEntryAdapterObject> getAgentProperties() {
        if (agentProperties == null) {
            agentProperties = new ArrayList<MapEntryAdapterObject>();
        }
        return this.agentProperties;
    }

    /**
     * Gets the value of the decisionFactors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the decisionFactors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDecisionFactors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MapEntryAdapter }
     * 
     * 
     */
    public List<MapEntryAdapter> getDecisionFactors() {
        if (decisionFactors == null) {
            decisionFactors = new ArrayList<MapEntryAdapter>();
        }
        return this.decisionFactors;
    }

    /**
     * Gets the value of the groceryList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groceryList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroceryList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getGroceryList() {
        if (groceryList == null) {
            groceryList = new ArrayList<String>();
        }
        return this.groceryList;
    }

}
