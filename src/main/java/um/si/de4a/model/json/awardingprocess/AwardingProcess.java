package um.si.de4a.model.json.awardingprocess;

import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.assessment.IssuedDate;

public class AwardingProcess {
    @SerializedName("@id")
    private String id;

    private IssuedDate awardingDate;

    private AwardingLocation awardingLocation;

    private AwardingBody awardingBody;

    public AwardingProcess(String id, IssuedDate awardingDate, AwardingLocation awardingLocation, AwardingBody awardingBody) {
        this.id = id;
        this.awardingDate = awardingDate;
        this.awardingLocation = awardingLocation;
        this.awardingBody = awardingBody;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IssuedDate getAwardingDate() {
        return awardingDate;
    }

    public void setAwardingDate(IssuedDate awardingDate) {
        this.awardingDate = awardingDate;
    }

    public AwardingLocation getAwardingLocation() {
        return awardingLocation;
    }

    public void setAwardingLocation(AwardingLocation awardingLocation) {
        this.awardingLocation = awardingLocation;
    }

    public AwardingBody getAwardingBody() {
        return awardingBody;
    }

    public void setAwardingBody(AwardingBody awardingBody) {
        this.awardingBody = awardingBody;
    }
}
