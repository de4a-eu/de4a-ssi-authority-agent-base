package um.si.de4a.resources.vc;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CredentialsAttach {
    @SerializedName("lastmod_time")
    private String lastmodTime;
    private Data data;

    public CredentialsAttach(String lastmodTime, Data data) {
        this.lastmodTime = lastmodTime;
        this.data = data;
    }

    public String getLastmodTime() {
        return lastmodTime;
    }

    public void setLastmodTime(String lastmodTime) {
        this.lastmodTime = lastmodTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
