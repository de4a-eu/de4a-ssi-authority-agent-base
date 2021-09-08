package um.si.de4a.resources.vc;


import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.SignedVerifiableCredential;

import java.util.ArrayList;

public class SendVCRequest {
    @SerializedName("issue_credential")
    private IssueCredential credential;

    public SendVCRequest(IssueCredential credential) {
        this.credential = credential;
    }

    public IssueCredential getCredential() {
        return credential;
    }

    public void setCredential(IssueCredential credential) {
        this.credential = credential;
    }
}
