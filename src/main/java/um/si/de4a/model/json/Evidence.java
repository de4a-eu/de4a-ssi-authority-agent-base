package um.si.de4a.model.json;

public class Evidence {
    private String id;
    private String[] type;
    private String verifier;
    private String[] evidenceDocument;

    public Evidence(String id, String[] type, String verifier, String[] evidenceDocument) {
        this.id = id;
        this.type = type;
        this.verifier = verifier;
        this.evidenceDocument = evidenceDocument;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public String[] getEvidenceDocument() {
        return evidenceDocument;
    }

    public void setEvidenceDocument(String[] evidenceDocument) {
        this.evidenceDocument = evidenceDocument;
    }
}
