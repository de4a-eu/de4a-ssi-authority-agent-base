
package um.si.de4a.model.xml;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for nationalIdType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nationalIdType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="spatialID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nationalIdType", namespace = "http://data.europa.eu/europass/model/credentials#", propOrder = {
    "value"
})
public class NationalId {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "spatialID")
    protected String spatialID;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the spatialID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpatialID() {
        return spatialID;
    }

    /**
     * Sets the value of the spatialID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpatialID(String value) {
        this.spatialID = value;
    }

}
