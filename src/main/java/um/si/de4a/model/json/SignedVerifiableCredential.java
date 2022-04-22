package um.si.de4a.model.json;

import com.google.gson.annotations.SerializedName;

public class SignedVerifiableCredential {
    @SerializedName("credentialSubject")
    private CredentialSubject credentialSubject;
    private String issuanceDate;
    private String validFrom;
    private String expirationDate;
    private String id;
    private Proof proof;
    private String[] type;
    @SerializedName("@context")
     private String[] context;
     private String issuer;

    public SignedVerifiableCredential(CredentialSubject credentialSubject, String issuanceDate, String validFrom, String expirationDate, String id, Proof proof, String[] type, String[] context, String issuer) {
        this.credentialSubject = credentialSubject;
        this.issuanceDate = issuanceDate;
        this.validFrom = validFrom;
        this.expirationDate = expirationDate;
        this.id = id;
        this.proof = proof;
        this.type = type;
        this.context = context;
        this.issuer = issuer;
    }

    public String[] getContext() {
        return context;
    }

    public void setContext(String[] context) {
        this.context = context;
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

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public CredentialSubject getCredentialSubject() {
        return credentialSubject;
    }

    public void setCredentialSubject(CredentialSubject credentialSubject) {
        this.credentialSubject = credentialSubject;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
