package um.si.de4a.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "agentReferences")
@XmlAccessorType(XmlAccessType.FIELD)
public class AgentReferences {

    @XmlElement(name = "organization", type = Organization.class)
    private List<Organization> organizations = new ArrayList<Organization>();

    public AgentReferences() {
    }

    public AgentReferences(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }
}
