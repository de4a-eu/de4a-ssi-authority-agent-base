package um.si.de4a.model.json.qualification;

import um.si.de4a.model.json.Qualification;

public class LearningSpecificationReferences {
    private Qualification qualification;

    public LearningSpecificationReferences(Qualification qualification) {
        this.qualification = qualification;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }
}
