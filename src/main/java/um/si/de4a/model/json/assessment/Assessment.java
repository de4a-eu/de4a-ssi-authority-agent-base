package json.assessment;

import com.google.gson.annotations.SerializedName;
import json.SpecifiedBy;
import json.Title;

public class Assessment {
    @SerializedName("@id")
    private String id;

    private Title title;

    private SpecifiedBy specifiedBy;

    private IssuedDate issuedDate;

    public Assessment(String id, Title title, SpecifiedBy specifiedBy, IssuedDate issuedDate) {
        this.id = id;
        this.title = title;
        this.specifiedBy = specifiedBy;
        this.issuedDate = issuedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public SpecifiedBy getSpecifiedBy() {
        return specifiedBy;
    }

    public void setSpecifiedBy(SpecifiedBy specifiedBy) {
        this.specifiedBy = specifiedBy;
    }

    public IssuedDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(IssuedDate issuedDate) {
        this.issuedDate = issuedDate;
    }
}
