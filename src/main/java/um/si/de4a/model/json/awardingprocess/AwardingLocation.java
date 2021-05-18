package json.awardingprocess;

import com.google.gson.annotations.SerializedName;

public class AwardingLocation {
    @SerializedName("@idref")
    private String idref;

    public AwardingLocation(String idref) {
        this.idref = idref;
    }

    public String getIdref() {
        return idref;
    }

    public void setIdref(String idref) {
        this.idref = idref;
    }
}
