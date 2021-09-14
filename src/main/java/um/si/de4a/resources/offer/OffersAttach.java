package um.si.de4a.resources.offer;

import com.google.gson.annotations.SerializedName;
import um.si.de4a.resources.vc.Data;

public class OffersAttach {

    private Data data;
    @SerializedName("lastmod_time")
    private String lastmodTime;

    public OffersAttach(Data data, String lastmodTime) {
        this.data = data;
        this.lastmodTime = lastmodTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getLastmodTime() {
        return lastmodTime;
    }

    public void setLastmodTime(String lastmodTime) {
        this.lastmodTime = lastmodTime;
    }
}
