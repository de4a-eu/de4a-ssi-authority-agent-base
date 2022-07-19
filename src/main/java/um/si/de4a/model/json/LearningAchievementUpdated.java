package um.si.de4a.model.json;

import java.util.ArrayList;

public class LearningAchievementUpdated {
    private String id;
    private String title;
    private WasAwardedByUpdated wasAwardedBy;
    private ArrayList<SpecifiedByUpdated> specifiedBy;
    private ArrayList<WasDerivedFromUpdated> wasDerivedFrom;
    private String associatedLearningOpportunity;

    public LearningAchievementUpdated(String id, String title, WasAwardedByUpdated wasAwardedBy, ArrayList<SpecifiedByUpdated> specifiedBy, ArrayList<WasDerivedFromUpdated> wasDerivedFrom, String associatedLearningOpportunity) {
        this.id = id;
        this.title = title;
        this.wasAwardedBy = wasAwardedBy;
        this.specifiedBy = specifiedBy;
        this.wasDerivedFrom = wasDerivedFrom;
        this.associatedLearningOpportunity = associatedLearningOpportunity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public WasAwardedByUpdated getWasAwardedBy() {
        return wasAwardedBy;
    }

    public void setWasAwardedBy(WasAwardedByUpdated wasAwardedBy) {
        this.wasAwardedBy = wasAwardedBy;
    }

    public ArrayList<SpecifiedByUpdated> getSpecifiedBy() {
        return specifiedBy;
    }

    public void setSpecifiedBy(ArrayList<SpecifiedByUpdated> specifiedBy) {
        this.specifiedBy = specifiedBy;
    }

    public ArrayList<WasDerivedFromUpdated> getWasDerivedFrom() {
        return wasDerivedFrom;
    }

    public void setWasDerivedFrom(ArrayList<WasDerivedFromUpdated> wasDerivedFrom) {
        this.wasDerivedFrom = wasDerivedFrom;
    }

    public String getAssociatedLearningOpportunity() {
        return associatedLearningOpportunity;
    }

    public void setAssociatedLearningOpportunity(String associatedLearningOpportunity) {
        this.associatedLearningOpportunity = associatedLearningOpportunity;
    }
}
