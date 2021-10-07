package um.si.de4a.resources.offer;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfferCredential {
    @SerializedName("credential_preview")
    private CredentialPreview credentialPreview;

    @SerializedName("offers~attach")
    private ArrayList<OffersAttach> offersAttach;
    public OfferCredential(CredentialPreview credentialPreview, ArrayList<OffersAttach> offersAttach) {
        this.credentialPreview = credentialPreview;
        this.offersAttach = offersAttach;
    }

    public CredentialPreview getCredentialPreview() {
        return credentialPreview;
    }

    public void setCredentialPreview(CredentialPreview credentialPreview) {
        this.credentialPreview = credentialPreview;
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
