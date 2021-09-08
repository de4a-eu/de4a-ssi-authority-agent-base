package um.si.de4a.resources.vp;

public class ValidateVCRequest {
    public String verifiableCredential;

    public ValidateVCRequest(String verifiableCredential) {
        this.verifiableCredential = verifiableCredential;
    }

    public String getVerifiableCredential() {
        return verifiableCredential;
    }

    public void setVerifiableCredential(String verifiableCredential) {
        this.verifiableCredential = verifiableCredential;
    }
}
