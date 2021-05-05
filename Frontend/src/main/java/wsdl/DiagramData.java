
package wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for diagramData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="diagramData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KPIName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="simulationId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xCoordinates" type="{http://www.w3.org/2001/XMLSchema}double" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="yCoordinates" type="{http://www.w3.org/2001/XMLSchema}double" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "diagramData", propOrder = {
    "kpiName",
    "simulationId",
    "xCoordinates",
    "yCoordinates"
})
public class DiagramData {

    @XmlElement(name = "KPIName")
    protected String kpiName;
    protected String simulationId;
    @XmlElement(nillable = true)
    protected List<Double> xCoordinates;
    @XmlElement(nillable = true)
    protected List<Double> yCoordinates;

    /**
     * Gets the value of the kpiName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKPIName() {
        return kpiName;
    }

    /**
     * Sets the value of the kpiName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKPIName(String value) {
        this.kpiName = value;
    }

    /**
     * Gets the value of the simulationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimulationId() {
        return simulationId;
    }

    /**
     * Sets the value of the simulationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimulationId(String value) {
        this.simulationId = value;
    }

    /**
     * Gets the value of the xCoordinates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xCoordinates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXCoordinates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getXCoordinates() {
        if (xCoordinates == null) {
            xCoordinates = new ArrayList<Double>();
        }
        return this.xCoordinates;
    }

    /**
     * Gets the value of the yCoordinates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the yCoordinates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getYCoordinates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getYCoordinates() {
        if (yCoordinates == null) {
            yCoordinates = new ArrayList<Double>();
        }
        return this.yCoordinates;
    }

}
