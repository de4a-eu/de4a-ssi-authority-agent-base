package um.si.de4a.resources.offer;


import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.SignedVerifiableCredential;
import um.si.de4a.model.json.VerifiableCredential;

public class OfferRequest {
    @SerializedName("my_did")
    private String myDID;
    @SerializedName("offer_credential")
    private OfferCredential credential;
    @SerializedName("their_did")
    private String theirDID;

    public OfferRequest(String myDID, OfferCredential credential, String theirDID) {
        this.myDID = myDID;
        this.credential = credential;
        this.theirDID = theirDID;
    }

    public String getMyDID() {
        return myDID;
    }

    public void setMyDID(String myDID) {
        this.myDID = myDID;
    }

    public OfferCredential getCredential() {
        return credential;
    }

    public void setCredential(OfferCredential credential) {
        this.credential = credential;
    }

    public String getTheirDID() {
        return theirDID;
    }

    public void setTheirDID(String theirDID) {
        this.theirDID = theirDID;
    }
}
