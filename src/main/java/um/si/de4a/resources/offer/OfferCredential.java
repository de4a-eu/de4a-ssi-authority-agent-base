package um.si.de4a.resources.offer;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfferCredential {
    private String comment;
    @SerializedName("credential_preview")
    private CredentialPreview credentialPreview;

    @SerializedName("offers~attach")
    private ArrayList<OffersAttach> offersAttach;

    public OfferCredential(String comment, CredentialPreview credentialPreview, ArrayList<OffersAttach> offersAttach) {
        this.comment = comment;
        this.credentialPreview = credentialPreview;
        this.offersAttach = offersAttach;
    }

    public CredentialPreview getCredentialPreview() {
        return credentialPreview;
    }

    public void setCredentialPreview(CredentialPreview credentialPreview) {
        this.credentialPreview = credentialPreview;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /*public OfferCredential(ArrayList<OffersAttach> offersAttach) {
        this.offersAttach = offersAttach;
    }*/

    public ArrayList<OffersAttach> getOffersAttach() {
        return offersAttach;
    }

    public void setOffersAttach(ArrayList<OffersAttach> offersAttach) {
        this.offersAttach = offersAttach;
    }
}
