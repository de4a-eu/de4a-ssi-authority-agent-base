package um.si.de4a.resources.offer;


import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.SignedVerifiableCredential;
import um.si.de4a.model.json.VerifiableCredential;

public class OfferRequest {
    @SerializedName("my_did")
    private String myDID;
    @SerializedName("their_did")
    private String theirDID;
    @SerializedName("offer_credential")
    private SignedVerifiableCredential credential;

    public OfferRequest(String myDID, String theirDID, SignedVerifiableCredential credential) {
        this.myDID = myDID;
        this.theirDID = theirDID;
        this.credential = credential;
    }

    public String getMyDID() {
        return myDID;
    }

    public void setMyDID(String myDID) {
        this.myDID = myDID;
    }

    public String getTheirDID() {
        return theirDID;
    }

    public void setTheirDID(String theirDID) {
        this.theirDID = theirDID;
    }

    public SignedVerifiableCredential getCredential() {
        return credential;
    }

    public void setCredential(SignedVerifiableCredential credential) {
        this.credential = credential;
    }
}
