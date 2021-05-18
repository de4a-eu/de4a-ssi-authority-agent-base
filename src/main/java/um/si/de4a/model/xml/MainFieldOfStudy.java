
package xml;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mainFieldOfStudyType", namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0", propOrder = {
    "value"
})
public class MainFieldOfStudy {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "uri")
    protected String uri;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
