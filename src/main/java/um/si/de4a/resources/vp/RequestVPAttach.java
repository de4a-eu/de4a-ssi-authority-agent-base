package um.si.de4a.resources.vp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RequestVPAttach {
    @SerializedName("@id")
    private String id;
    private Data data;
    @SerializedName("lastmod_time")
    private String lastModTime;
    @SerializedName("mime-type")
    private String mimeType;


    public RequestVPAttach(String id, Data data, String lastModTime, String mimeType) {
        this.id = id;
        this.data = data;
        this.lastModTime = lastModTime;
        this.mimeType = mimeType;
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

    public String getLastModTime() {
        return lastModTime;
    }

    public void setLastModTime(String lastModTime) {
        this.lastModTime = lastModTime;
    }
}
