package um.si.de4a.model.json;

import um.si.de4a.model.json.assessment.AssessmentReferences;
import um.si.de4a.model.json.awardingprocess.AwardingProcessReferences;
import um.si.de4a.model.json.location.LocationReferences;
import um.si.de4a.model.json.opportunity.LearningOpportunityReferences;
import um.si.de4a.model.json.organisation.AgentReferences;
import um.si.de4a.model.json.qualification.LearningSpecificationReferences;

public class CredentialSubject {
    private String currentFamilyName;
    private String currentGivenName;
    private String dateOfBirth;
    private String personIdentifier;
    private LearningAchievement learningAchievement;
    private LearningSpecificationReferences learningSpecificationReferences;
    private AssessmentReferences assessmentReferences;
    private AwardingProcessReferences awardingProcessReferences;
    private LocationReferences locationReferences;
    private AgentReferences agentReferences;
    private LearningOpportunityReferences learningOpportunityReferences;

    public CredentialSubject(String currentFamilyName, String currentGivenName, String dateOfBirth, String personIdentifier, LearningAchievement learningAchievement, LearningSpecificationReferences learningSpecificationReferences, AssessmentReferences assessmentReferences, AwardingProcessReferences awardingProcessReferences, LocationReferences locationReferences, AgentReferences agentReferences, LearningOpportunityReferences learningOpportunityReferences) {
        this.currentFamilyName = currentFamilyName;
        this.currentGivenName = currentGivenName;
        this.dateOfBirth = dateOfBirth;
        this.personIdentifier = personIdentifier;
        this.learningAchievement = learningAchievement;
        this.learningSpecificationReferences = learningSpecificationReferences;
        this.assessmentReferences = assessmentReferences;
        this.awardingProcessReferences = awardingProcessReferences;
        this.locationReferences = locationReferences;
        this.agentReferences = agentReferences;
        this.learningOpportunityReferences = learningOpportunityReferences;
    }

    public String getCurrentFamilyName() {
        return currentFamilyName;
    }

    public void setCurrentFamilyName(String currentFamilyName) {
        this.currentFamilyName = currentFamilyName;
    }

    public String getCurrentGivenName() {
        return currentGivenName;
    }

    public void setCurrentGivenName(String currentGivenName) {
        this.currentGivenName = currentGivenName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPersonIdentifier() {
        return personIdentifier;
    }

    public void setPersonIdentifier(String personIdentifier) {
        this.personIdentifier = personIdentifier;
    }

    public LearningAchievement getLearningAchievement() {
        return learningAchievement;
    }

    public void setLearningAchievement(LearningAchievement learningAchievement) {
        this.learningAchievement = learningAchievement;
    }

    public LearningSpecificationReferences getLearningSpecificationReferences() {
        return learningSpecificationReferences;
    }

    public void setLearningSpecificationReferences(LearningSpecificationReferences learningSpecificationReferences) {
        this.learningSpecificationReferences = learningSpecificationReferences;
    }

    public AssessmentReferences getAssessmentReferences() {
        return assessmentReferences;
    }

    public void setAssessmentReferences(AssessmentReferences assessmentReferences) {
        this.assessmentReferences = assessmentReferences;
    }

    public AwardingProcessReferences getAwardingProcessReferences() {
        return awardingProcessReferences;
    }

    public void setAwardingProcessReferences(AwardingProcessReferences awardingProcessReferences) {
        this.awardingProcessReferences = awardingProcessReferences;
    }

    public LocationReferences getLocationReferences() {
        return locationReferences;
    }

    public void setLocationReferences(LocationReferences locationReferences) {
        this.locationReferences = locationReferences;
    }

    public AgentReferences getAgentReferences() {
        return agentReferences;
    }

    public void setAgentReferences(AgentReferences agentReferences) {
        this.agentReferences = agentReferences;
    }

    public LearningOpportunityReferences getLearningOpportunityReferences() {
        return learningOpportunityReferences;
    }

    public void setLearningOpportunityReferences(LearningOpportunityReferences learningOpportunityReferences) {
        this.learningOpportunityReferences = learningOpportunityReferences;
    }
}
