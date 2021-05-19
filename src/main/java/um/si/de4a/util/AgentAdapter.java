package um.si.de4a.util;

import um.si.de4a.model.xml.Agent;
import um.si.de4a.model.xml.Organization;
import um.si.de4a.model.xml.Person;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AgentAdapter extends XmlAdapter<AgentAdapter.AdaptedAgent, Agent> {


    @Override
    public Agent unmarshal(AdaptedAgent adaptedAgent) throws Exception {
        if (null == adaptedAgent) {
            return null;
        }
        if (null != adaptedAgent.prefLabel) {
            Organization resultOrganization = new Organization();
            resultOrganization.setType(adaptedAgent.type);
            resultOrganization.setIdentifier(adaptedAgent.identifier);
            resultOrganization.setPrefLabel(adaptedAgent.prefLabel);
            resultOrganization.setAltLabel(adaptedAgent.altLabel);
            resultOrganization.setHasLocation(adaptedAgent.hasLocation);
            resultOrganization.setAdditionalNote(adaptedAgent.additionalNote);

            return resultOrganization;
        } else {
            return null;
        }
    }

    @Override
    public AdaptedAgent marshal(Agent agent) throws Exception {
        if (agent == null)
            return null;
        AdaptedAgent adaptedAgent = new AdaptedAgent();
        if(agent instanceof Agent){
            Agent resultAgent = (Agent) agent;

            adaptedAgent.type = resultAgent.getType();
            adaptedAgent.identifier = resultAgent.getIdentifier();
            adaptedAgent.prefLabel = resultAgent.getPrefLabel();
            adaptedAgent.altLabel = resultAgent.getAltLabel();
            adaptedAgent.hasLocation = resultAgent.getHasLocation();
            adaptedAgent.additionalNote = resultAgent.getAdditionalNote();
        }
        else{

        }
        return adaptedAgent;
    }

    public static class AdaptedAgent {

        @XmlAttribute
        private String identifier;

        @XmlAttribute
        private String type;

        @XmlAttribute
        private String prefLabel;

        @XmlAttribute
        private String altLabel;

        @XmlAttribute
        private String additionalNote;

        @XmlAttribute
        private String hasLocation;

        @XmlAttribute
        private String contactPoint;
    }

}