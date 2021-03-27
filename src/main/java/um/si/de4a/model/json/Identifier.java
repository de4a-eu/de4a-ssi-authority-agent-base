package um.si.de4a.model.json;

public class Identifier {
    private String[] items = new String[]{"Identifier"}; //check how to add unique values only
    private Notation notation; //NonEmptyString in VC JSON schema
    private String schemaAgency;
    private String issued;
    private String spatial;

    public Identifier(String[] items, Notation notation, String schemaAgency, String issued, String spatial) {
        this.items = items;
        this.notation = notation;
        this.schemaAgency = schemaAgency;
        this.issued = issued;
        this.spatial = spatial;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public Notation getNotation() {
        return notation;
    }

    public void setNotation(Notation notation) {
        this.notation = notation;
    }

    public String getSchemaAgency() {
        return schemaAgency;
    }

    public void setSchemaAgency(String schemaAgency) {
        this.schemaAgency = schemaAgency;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getSpatial() {
        return spatial;
    }

    public void setSpatial(String spatial) {
        this.spatial = spatial;
    }
}

class Notation {
    private String type;
    private String value;

    public Notation(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
