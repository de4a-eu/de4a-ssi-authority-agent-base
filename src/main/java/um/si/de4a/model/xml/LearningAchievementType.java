package um.si.de4a.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="learningAchievementType")
public class LearningAchievementType {
    private String id;
    private String identifier;
    private String title;
    private String description; // note type
    private String additionalNote; // note type
    private String wasInfluencedBy; // activity type
    private String wasAwardedBy; // ID reference
    private String specifiedBy; //ID reference
    private String associatedLearningOpportunity; //ID reference

    public LearningAchievementType(){};

    public LearningAchievementType(String id, String identifier, String title, String description, String additionalNote, String wasInfluencedBy, String wasAwardedBy, String specifiedBy, String associatedLearningOpportunity) {
        this.id = id;
        this.identifier = identifier;
        this.title = title;
        this.description = description;
        this.additionalNote = additionalNote;
        this.wasInfluencedBy = wasInfluencedBy;
        this.wasAwardedBy = wasAwardedBy;
        this.specifiedBy = specifiedBy;
        this.associatedLearningOpportunity = associatedLearningOpportunity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public String getWasInfluencedBy() {
        return wasInfluencedBy;
    }

    public void setWasInfluencedBy(String wasInfluencedBy) {
        this.wasInfluencedBy = wasInfluencedBy;
    }

    public String getWasAwardedBy() {
        return wasAwardedBy;
    }

    public void setWasAwardedBy(String wasAwardedBy) {
        this.wasAwardedBy = wasAwardedBy;
    }

    public String getSpecifiedBy() {
        return specifiedBy;
    }

    public void setSpecifiedBy(String specifiedBy) {
        this.specifiedBy = specifiedBy;
    }

    public String getAssociatedLearningOpportunity() {
        return associatedLearningOpportunity;
    }

    public void setAssociatedLearningOpportunity(String associatedLearningOpportunity) {
        this.associatedLearningOpportunity = associatedLearningOpportunity;
    }
}
