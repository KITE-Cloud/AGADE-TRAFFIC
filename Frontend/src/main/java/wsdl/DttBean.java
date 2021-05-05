
package wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dttBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dttBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="desiredArrivalTick" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="share" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dttBean", propOrder = {
    "desiredArrivalTick",
    "share"
})
public class DttBean {

    protected int desiredArrivalTick;
    protected double share;

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
     * Gets the value of the share property.
     * 
     */
    public double getShare() {
        return share;
    }

    /**
     * Sets the value of the share property.
     * 
     */
    public void setShare(double value) {
        this.share = value;
    }

}
