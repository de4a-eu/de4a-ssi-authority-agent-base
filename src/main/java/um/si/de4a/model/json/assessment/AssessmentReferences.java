package json.assessment;

import json.assessment.Assessment;

public class AssessmentReferences {
    private Assessment assessment;

    public AssessmentReferences(Assessment assessment) {
        this.assessment = assessment;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }
}
