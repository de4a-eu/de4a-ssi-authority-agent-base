package um.si.de4a.resources.offer;


import um.si.de4a.model.json.VerifiableCredential;

public class OfferRequest {
    private String created;
    private VerifiableCredential credential;
    private String did;
    private String signatureType;

    public OfferRequest(String created, VerifiableCredential credential, String did, String signatureType) {
        this.created = created;
        this.credential = credential;
        this.did = did;
        this.signatureType = signatureType;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public VerifiableCredential getCredential() {
        return credential;
    }

    public void setCredential(VerifiableCredential credential) {
        this.credential = credential;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }
}
