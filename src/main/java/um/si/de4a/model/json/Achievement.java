package um.si.de4a.model.json;

import java.util.ArrayList;

public class Achievement {
    private IDFieldType id;
    private String title;
    private ArrayList<Identifier> identifier = new ArrayList<>();
    private LearningAward LearningAward;
    private ArrayList<IDFieldType> wasInfluencedBy = new ArrayList<>(); //IDReference in VC JSON schema

    public Achievement(IDFieldType id, String title, ArrayList<Identifier> identifier, um.si.de4a.model.json.LearningAward learningAward, ArrayList<IDFieldType> wasInfluencedBy) {
        this.id = id;
        this.title = title;
        this.identifier = identifier;
        LearningAward = learningAward;
        this.wasInfluencedBy = wasInfluencedBy;
    }

    public IDFieldType getId() {
        return id;
    }

    public void setId(IDFieldType id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(ArrayList<Identifier> identifier) {
        this.identifier = identifier;
    }

    public um.si.de4a.model.json.LearningAward getLearningAward() {
        return LearningAward;
    }

    public void setLearningAward(um.si.de4a.model.json.LearningAward learningAward) {
        LearningAward = learningAward;
    }

    public ArrayList<IDFieldType> getWasInfluencedBy() {
        return wasInfluencedBy;
    }

    public void setWasInfluencedBy(ArrayList<IDFieldType> wasInfluencedBy) {
        this.wasInfluencedBy = wasInfluencedBy;
    }
}
