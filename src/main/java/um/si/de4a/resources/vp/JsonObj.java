package um.si.de4a.resources.vp;

import com.google.gson.annotations.SerializedName;

public class JsonObj {
    @SerializedName("@context")
    private String[] context;
    private String[] type;

    public JsonObj(String[] context, String[] type) {
        this.context = context;
        this.type = type;
    }

    public String[] getContext() {
        return context;
    }

    public void setContext(String[] context) {
        this.context = context;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }
}
