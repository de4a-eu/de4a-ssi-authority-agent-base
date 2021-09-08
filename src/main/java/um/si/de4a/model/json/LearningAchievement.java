package um.si.de4a.model.json;

import com.google.gson.annotations.SerializedName;

public class LearningAchievement {
    @SerializedName("@id")
    private String id;
    private Title title;
    private SpecifiedBy specifiedBy;
    private WasDerivedFrom wasDerivedFrom;
    private WasAwardedBy wasAwardedBy;
    private AssociatedLearningOpportunity associatedLearningOpportunity;

    public LearningAchievement(String id, Title title, SpecifiedBy specifiedBy, WasDerivedFrom wasDerivedFrom, WasAwardedBy wasAwardedBy, AssociatedLearningOpportunity associatedLearningOpportunity) {
        this.id = id;
        this.title = title;
        this.specifiedBy = specifiedBy;
        this.wasDerivedFrom = wasDerivedFrom;
        this.wasAwardedBy = wasAwardedBy;
        this.associatedLearningOpportunity = associatedLearningOpportunity;
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

    public WasDerivedFrom getWasDerivedFrom() {
        return wasDerivedFrom;
    }

    public void setWasDerivedFrom(WasDerivedFrom wasDerivedFrom) {
        this.wasDerivedFrom = wasDerivedFrom;
    }

    public WasAwardedBy getWasAwardedBy() {
        return wasAwardedBy;
    }

    public void setWasAwardedBy(WasAwardedBy wasAwardedBy) {
        this.wasAwardedBy = wasAwardedBy;
    }

    public AssociatedLearningOpportunity getAssociatedLearningOpportunity() {
        return associatedLearningOpportunity;
    }

    public void setAssociatedLearningOpportunity(AssociatedLearningOpportunity associatedLearningOpportunity) {
        this.associatedLearningOpportunity = associatedLearningOpportunity;
    }


}
