
package xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "placeOfIssue", namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0", propOrder = {
    "name"
})
public class PlaceOfIssue {

    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#", required = true)
    protected Name name;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }
}
