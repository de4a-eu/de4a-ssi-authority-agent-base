package um.si.de4a.model.json;

import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.assessment.AssessmentReferences;
import um.si.de4a.model.json.awardingprocess.AwardingProcessReferences;
import um.si.de4a.model.json.location.LocationReferences;
import um.si.de4a.model.json.opportunity.LearningOpportunityReferences;
import um.si.de4a.model.json.organisation.AgentReferences;
import um.si.de4a.model.json.qualification.LearningSpecificationReferences;

import java.util.ArrayList;

public class CredentialSubjectUpdated {
    @SerializedName("currentFamilyName")
    private String currentFamilyName;
    @SerializedName("currentGivenName")
    private String currentGivenName;
    @SerializedName("dateOfBirth")
    private String dateOfBirth;
    @SerializedName("personIdentifier")
    private String personIdentifier;
    @SerializedName("achieved")
    private ArrayList<LearningAchievementUpdated> achieved;

    public CredentialSubjectUpdated(String currentFamilyName, String currentGivenName, String dateOfBirth, String personIdentifier, ArrayList<LearningAchievementUpdated> achieved) {
        this.currentFamilyName = currentFamilyName;
        this.currentGivenName = currentGivenName;
        this.dateOfBirth = dateOfBirth;
        this.personIdentifier = personIdentifier;
        this.achieved = achieved;
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

    public ArrayList<LearningAchievementUpdated> getAchieved() {
        return achieved;
    }

    public void setAchieved(ArrayList<LearningAchievementUpdated> achieved) {
        this.achieved = achieved;
    }
}
