package um.si.de4a.model.xml;

import um.si.de4a.util.AgentAdapter;
import um.si.de4a.util.PersonAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(AgentAdapter.class)
public class Agent {
    private String identifier;
    private String type;
    private String prefLabel;
    private String altLabel;
    private String additionalNote;
    private String hasLocation;
    private ContactPoint contactPoint;

    public Agent() {
    }

    public Agent(String identifier, String type, String prefLabel, String altLabel, String additionalNote, String hasLocation, ContactPoint contactPoint) {
        this.identifier = identifier;
        this.type = type;
        this.prefLabel = prefLabel;
        this.altLabel = altLabel;
        this.additionalNote = additionalNote;
        this.hasLocation = hasLocation;
        this.contactPoint = contactPoint;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrefLabel() {
        return prefLabel;
    }

    public void setPrefLabel(String prefLabel) {
        this.prefLabel = prefLabel;
    }

    public String getAltLabel() {
        return altLabel;
    }

    public void setAltLabel(String altLabel) {
        this.altLabel = altLabel;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public String getHasLocation() {
        return hasLocation;
    }

    public void setHasLocation(String hasLocation) {
        this.hasLocation = hasLocation;
    }

    public ContactPoint getContactPoint() {
        return contactPoint;
    }

    public void setContactPoint(ContactPoint contactPoint) {
        this.contactPoint = contactPoint;
    }
}
