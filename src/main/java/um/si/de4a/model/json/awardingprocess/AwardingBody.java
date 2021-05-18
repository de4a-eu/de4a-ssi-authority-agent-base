package json.awardingprocess;

import com.google.gson.annotations.SerializedName;

public class AwardingBody {
    @SerializedName("@idref")
    private String idref;

    public AwardingBody(String idref) {
        this.idref = idref;
    }

    public String getIdref() {
        return idref;
    }

    public void setIdref(String idref) {
        this.idref = idref;
    }
}
