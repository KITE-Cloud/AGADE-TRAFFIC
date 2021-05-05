
package wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for parkingDestination complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="parkingDestination">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service/}destination">
 *       &lt;sequence>
 *         &lt;element name="finalDestination" type="{http://service/}finalDestination" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parkingDestination", propOrder = {
    "finalDestination"
})
public class ParkingDestination
    extends Destination
{

    protected FinalDestination finalDestination;

    /**
     * Gets the value of the finalDestination property.
     * 
     * @return
     *     possible object is
     *     {@link FinalDestination }
     *     
     */
    public FinalDestination getFinalDestination() {
        return finalDestination;
    }

    /**
     * Sets the value of the finalDestination property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinalDestination }
     *     
     */
    public void setFinalDestination(FinalDestination value) {
        this.finalDestination = value;
    }

}
