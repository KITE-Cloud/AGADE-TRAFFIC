
package wsdl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vehicle.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="vehicle">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CAR"/>
 *     &lt;enumeration value="BIKE"/>
 *     &lt;enumeration value="FEET"/>
 *     &lt;enumeration value="PUBLICTRANSPORT"/>
 *     &lt;enumeration value="None"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "vehicle")
@XmlEnum
public enum Vehicle {

    CAR("CAR"),
    BIKE("BIKE"),
    FEET("FEET"),
    PUBLICTRANSPORT("PUBLICTRANSPORT"),
    @XmlEnumValue("None")
    NONE("None");
    private final String value;

    Vehicle(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Vehicle fromValue(String v) {
        for (Vehicle c: Vehicle.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
