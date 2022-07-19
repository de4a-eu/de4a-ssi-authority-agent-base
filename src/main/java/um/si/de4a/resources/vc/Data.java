package um.si.de4a.resources.vc;

import um.si.de4a.model.json.SignedVerifiableCredential;

public class Data {
    private String base64;

    public Data(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
