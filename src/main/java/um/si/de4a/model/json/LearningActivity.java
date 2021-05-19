package um.si.de4a.model.json;

import java.util.ArrayList;

public class LearningActivity {
    private IDFieldType id;
    private Enums.ACTIVITY_TYPE activityType;
    private String title;
    private PeriodOfTime period;
    private ArrayList<IDFieldType> influenced = new ArrayList<>(); // IDReference type in VC JSON schema
    private ArrayList<IDFieldType> directedBy = new ArrayList<>(); // IDReference type in VC JSON schema

    public LearningActivity(IDFieldType id, Enums.ACTIVITY_TYPE activityType, String title, PeriodOfTime period, ArrayList<IDFieldType> influenced, ArrayList<IDFieldType> directedBy) {
        this.id = id;
        this.activityType = activityType;
        this.title = title;
        this.period = period;
        this.influenced = influenced;
        this.directedBy = directedBy;
    }

    public IDFieldType getId() {
        return id;
    }

    public void setId(IDFieldType id) {
        this.id = id;
    }

    public Enums.ACTIVITY_TYPE getActivityType() {
        return activityType;
    }

    public void setActivityType(Enums.ACTIVITY_TYPE activityType) {
        this.activityType = activityType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PeriodOfTime getPeriod() {
        return period;
    }

    public void setPeriod(PeriodOfTime period) {
        this.period = period;
    }

    public ArrayList<IDFieldType> getInfluenced() {
        return influenced;
    }

    public void setInfluenced(ArrayList<IDFieldType> influenced) {
        this.influenced = influenced;
    }

    public ArrayList<IDFieldType> getDirectedBy() {
        return directedBy;
    }

    public void setDirectedBy(ArrayList<IDFieldType> directedBy) {
        this.directedBy = directedBy;
    }
}

