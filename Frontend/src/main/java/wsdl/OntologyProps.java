
package wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ontologyProps complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ontologyProps">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="censusProps" type="{http://service/}censusProps" minOccurs="0"/>
 *         &lt;element name="foodCategories" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="locationTypes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ontologyProps", propOrder = {
    "censusProps",
    "foodCategories",
    "locationTypes"
})
public class OntologyProps {

    protected CensusProps censusProps;
    @XmlElement(nillable = true)
    protected List<String> foodCategories;
    @XmlElement(nillable = true)
    protected List<String> locationTypes;

    /**
     * Gets the value of the censusProps property.
     * 
     * @return
     *     possible object is
     *     {@link CensusProps }
     *     
     */
    public CensusProps getCensusProps() {
        return censusProps;
    }

    /**
     * Sets the value of the censusProps property.
     * 
     * @param value
     *     allowed object is
     *     {@link CensusProps }
     *     
     */
    public void setCensusProps(CensusProps value) {
        this.censusProps = value;
    }

    /**
     * Gets the value of the foodCategories property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the foodCategories property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFoodCategories().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFoodCategories() {
        if (foodCategories == null) {
            foodCategories = new ArrayList<String>();
        }
        return this.foodCategories;
    }

    /**
     * Gets the value of the locationTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locationTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocationTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLocationTypes() {
        if (locationTypes == null) {
            locationTypes = new ArrayList<String>();
        }
        return this.locationTypes;
    }

}
