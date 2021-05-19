package um.si.de4a.model.json.qualification;

import com.google.gson.annotations.SerializedName;

public class Code {
    @SerializedName("#code")
    private String code;

    public Code(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
