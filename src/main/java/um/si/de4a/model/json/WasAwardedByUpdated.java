package um.si.de4a.model.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WasAwardedByUpdated {

    private String id;
    private String[] awardingBody;
    private String awardingDate;
    private String[] awardingLocation;

    public WasAwardedByUpdated(String id, String[] awardingBody, String awardingDate, String[] awardingLocation) {
        this.id = id;
        this.awardingBody = awardingBody;
        this.awardingDate = awardingDate;
        this.awardingLocation = awardingLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getAwardingBody() {
        return awardingBody;
    }

    public void setAwardingBody(String[] awardingBody) {
        this.awardingBody = awardingBody;
    }

    public String getAwardingDate() {
        return awardingDate;
    }

    public void setAwardingDate(String awardingDate) {
        this.awardingDate = awardingDate;
    }

    public String[] getAwardingLocation() {
        return awardingLocation;
    }

    public void setAwardingLocation(String[] awardingLocation) {
        this.awardingLocation = awardingLocation;
    }
}
