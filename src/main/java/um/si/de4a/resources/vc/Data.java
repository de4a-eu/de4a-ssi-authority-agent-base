package um.si.de4a.resources.vc;

import um.si.de4a.model.json.SignedVerifiableCredential;

public class Data {
    private SignedVerifiableCredential json;

    public Data(SignedVerifiableCredential json) {
        this.json = json;
    }

    public SignedVerifiableCredential getJson() {
        return json;
    }

    public void setJson(SignedVerifiableCredential json) {
        this.json = json;
    }
}
