package um.si.de4a.model.json;

import java.util.ArrayList;

public class PersonTypeJSON {
    private IDFieldType id;
    private String fullName;
    private ArrayList<Achievement> achievements = new ArrayList<>();
    private ArrayList<LearningActivity> activities = new ArrayList<>();

    public PersonTypeJSON(IDFieldType id, String fullName, ArrayList<Achievement> achievements, ArrayList<LearningActivity> activities) {
        this.id = id;
        this.fullName = fullName;
        this.achievements = achievements;
        this.activities = activities;
    }

    public IDFieldType getId() {
        return id;
    }

    public void setId(IDFieldType id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList<Achievement> achievements) {
        this.achievements = achievements;
    }

    public ArrayList<LearningActivity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<LearningActivity> activities) {
        this.activities = activities;
    }
}
