package um.si.de4a.model.json;

public class Proof {
    private String created;
    private String jws;
    private String proofPurpose;
    private String type;
    private String verificationMethod;

    public Proof(String created, String jws, String proofPurpose, String type, String verificationMethod) {
        this.created = created;
        this.jws = jws;
        this.proofPurpose = proofPurpose;
        this.type = type;
        this.verificationMethod = verificationMethod;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getJws() {
        return jws;
    }

    public void setJws(String jws) {
        this.jws = jws;
    }

    public String getProofPurpose() {
        return proofPurpose;
    }

    public void setProofPurpose(String proofPurpose) {
        this.proofPurpose = proofPurpose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerificationMethod() {
        return verificationMethod;
    }

    public void setVerificationMethod(String verificationMethod) {
        this.verificationMethod = verificationMethod;
    }
}
