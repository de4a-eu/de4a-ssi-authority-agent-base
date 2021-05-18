package json.opportunity;

public class LearningOpportunity {
    private String id;
    private LearningSchedule learningSchedule;

    public LearningOpportunity(String id, LearningSchedule learningSchedule) {
        this.id = id;
        this.learningSchedule = learningSchedule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LearningSchedule getLearningSchedule() {
        return learningSchedule;
    }

    public void setLearningSchedule(LearningSchedule learningSchedule) {
        this.learningSchedule = learningSchedule;
    }
}
