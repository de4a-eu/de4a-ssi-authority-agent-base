package um.si.de4a.model.json;

public class Invitation {
    private String id;
    private String endpoint;
    private String[] recipientKeys;
    private String label;
    private String type;

    public Invitation() {
    }

    public Invitation(String id, String endpoint, String[] recipientKeys, String label, String type) {
        this.id = id;
        this.endpoint = endpoint;
        this.recipientKeys = recipientKeys;
        this.label = label;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String[] getRecipientKeys() {
        return recipientKeys;
    }

    public void setRecipientKeys(String[] recipientKeys) {
        this.recipientKeys = recipientKeys;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
