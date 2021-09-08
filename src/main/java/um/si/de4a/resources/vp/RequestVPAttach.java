package um.si.de4a.resources.vp;

import com.google.gson.annotations.SerializedName;

public class RequestVPAttach {
    @SerializedName("@id")
    private String id;
    @SerializedName("mime-type")
    private String mimeType;
    private Data data;

    public RequestVPAttach(String id, String mimeType, Data data) {
        this.id = id;
        this.mimeType = mimeType;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
