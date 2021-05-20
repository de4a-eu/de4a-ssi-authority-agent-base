package um.si.de4a.resources.vc;


import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.SignedVerifiableCredential;

public class SendVCRequest {
    @SerializedName("issue_credential")
    private SignedVerifiableCredential credential;

    public SendVCRequest(SignedVerifiableCredential credential) {
        this.credential = credential;
    }

    public SignedVerifiableCredential getCredential() {
        return credential;
    }

    public void setCredential(SignedVerifiableCredential credential) {
        this.credential = credential;
    }
}
