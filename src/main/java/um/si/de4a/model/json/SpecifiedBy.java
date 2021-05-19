package um.si.de4a.model.json;

import com.google.gson.annotations.SerializedName;

public class SpecifiedBy {
    @SerializedName("@idref")
    private String idRef;

    public SpecifiedBy(String idRef) {
        this.idRef = idRef;
    }

    public String getIdRef() {
        return idRef;
    }

    public void setIdRef(String idRef) {
        this.idRef = idRef;
    }
}
