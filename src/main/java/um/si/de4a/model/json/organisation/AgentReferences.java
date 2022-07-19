package um.si.de4a.model.json.organisation;

public class AgentReferences {
    private Organisation organisation;

    public AgentReferences(Organisation organisation) {
        this.organisation = organisation;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
}
