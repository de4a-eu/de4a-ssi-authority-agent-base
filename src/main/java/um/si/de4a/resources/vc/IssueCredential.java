package um.si.de4a.resources.vc;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IssueCredential {
    @SerializedName("credentials~attach")
    private ArrayList<CredentialsAttach> credentialsAttach = null;

    public IssueCredential(ArrayList<CredentialsAttach> credentialsAttach) {
        this.credentialsAttach = credentialsAttach;
    }

    public ArrayList<CredentialsAttach> getCredentialsAttach() {
        return credentialsAttach;
    }

    public void setCredentialsAttach(ArrayList<CredentialsAttach> credentialsAttach) {
        this.credentialsAttach = credentialsAttach;
    }
}
