package json;

import com.google.gson.annotations.SerializedName;

public class WasDerivedFrom {
    @SerializedName("@idref")
    private String idRef;

    public WasDerivedFrom(String idRef) {
        this.idRef = idRef;
    }

    public String getIdRef() {
        return idRef;
    }

    public void setIdRef(String idRef) {
        this.idRef = idRef;
    }
}
