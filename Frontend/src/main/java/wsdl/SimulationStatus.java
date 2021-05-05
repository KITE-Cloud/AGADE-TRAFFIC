
package wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for simulationStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="simulationStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="currentTick" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="simulationIsDone" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "simulationStatus", propOrder = {
    "currentTick",
    "simulationIsDone"
})
public class SimulationStatus {

    protected int currentTick;
    protected boolean simulationIsDone;

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
     * Gets the value of the simulationIsDone property.
     * 
     */
    public boolean isSimulationIsDone() {
        return simulationIsDone;
    }

    /**
     * Sets the value of the simulationIsDone property.
     * 
     */
    public void setSimulationIsDone(boolean value) {
        this.simulationIsDone = value;
    }

}
