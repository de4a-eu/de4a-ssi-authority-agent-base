package json;

import com.google.gson.annotations.SerializedName;

public class AssociatedLearningOpportunity {

    @SerializedName("@idref")
    private String idRef;

    public AssociatedLearningOpportunity(String idRef) {
        this.idRef = idRef;
    }

    public String getIdRef() {
        return idRef;
    }

    public void setIdRef(String idRef) {
        this.idRef = idRef;
    }
}
