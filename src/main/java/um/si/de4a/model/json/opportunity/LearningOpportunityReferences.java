package json.opportunity;

import json.opportunity.LearningOpportunity;

public class LearningOpportunityReferences {
    private LearningOpportunity learningOpportunity;

    public LearningOpportunityReferences(LearningOpportunity learningOpportunity) {
        this.learningOpportunity = learningOpportunity;
    }

    public LearningOpportunity getLearningOpportunity() {
        return learningOpportunity;
    }

    public void setLearningOpportunity(LearningOpportunity learningOpportunity) {
        this.learningOpportunity = learningOpportunity;
    }
}
