
package wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for statistics complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="statistics">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="createdAgentsPerTick" type="{http://service/}diagramData" minOccurs="0"/>
 *         &lt;element name="numberOfMovingAgentsPerTick" type="{http://service/}diagramData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "statistics", propOrder = {
    "createdAgentsPerTick",
    "numberOfMovingAgentsPerTick"
})
public class Statistics {

    protected DiagramData createdAgentsPerTick;
    protected DiagramData numberOfMovingAgentsPerTick;

    /**
     * Gets the value of the createdAgentsPerTick property.
     * 
     * @return
     *     possible object is
     *     {@link DiagramData }
     *     
     */
    public DiagramData getCreatedAgentsPerTick() {
        return createdAgentsPerTick;
    }

    /**
     * Sets the value of the createdAgentsPerTick property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiagramData }
     *     
     */
    public void setCreatedAgentsPerTick(DiagramData value) {
        this.createdAgentsPerTick = value;
    }

    /**
     * Gets the value of the numberOfMovingAgentsPerTick property.
     * 
     * @return
     *     possible object is
     *     {@link DiagramData }
     *     
     */
    public DiagramData getNumberOfMovingAgentsPerTick() {
        return numberOfMovingAgentsPerTick;
    }

    /**
     * Sets the value of the numberOfMovingAgentsPerTick property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiagramData }
     *     
     */
    public void setNumberOfMovingAgentsPerTick(DiagramData value) {
        this.numberOfMovingAgentsPerTick = value;
    }

}
