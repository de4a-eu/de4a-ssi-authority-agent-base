package um.si.de4a.resources.offer;


import um.si.de4a.model.json.VerifiableCredential;
import um.si.de4a.model.json.VerifiableCredentialUpdated;

public class SignRequest {
    private String created;
    private VerifiableCredentialUpdated credential;
    private String did;
    private String signatureType;

    public SignRequest(String created, VerifiableCredentialUpdated credential, String did, String signatureType) {
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

    public VerifiableCredentialUpdated getCredential() {
        return credential;
    }

    public void setCredential(VerifiableCredentialUpdated credential) {
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
