
package wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for routeDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="routeDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="edgeEnd" type="{http://service/}location" minOccurs="0"/>
 *         &lt;element name="edgeId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="edgeStart" type="{http://service/}location" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "routeDetail", propOrder = {
    "edgeEnd",
    "edgeId",
    "edgeStart"
})
public class RouteDetail {

    protected Location edgeEnd;
    protected Long edgeId;
    protected Location edgeStart;

    /**
     * Gets the value of the edgeEnd property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getEdgeEnd() {
        return edgeEnd;
    }

    /**
     * Sets the value of the edgeEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setEdgeEnd(Location value) {
        this.edgeEnd = value;
    }

    /**
     * Gets the value of the edgeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEdgeId() {
        return edgeId;
    }

    /**
     * Sets the value of the edgeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEdgeId(Long value) {
        this.edgeId = value;
    }

    /**
     * Gets the value of the edgeStart property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getEdgeStart() {
        return edgeStart;
    }

    /**
     * Sets the value of the edgeStart property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setEdgeStart(Location value) {
        this.edgeStart = value;
    }

}
