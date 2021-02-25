package um.si.de4a.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "HigherEducationEvidenceType")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class HEEvidenceTypeXML implements Serializable {
    private String degree;
    private String country;
    private String institutionName;
    private String studyProgramme;
    private String mainFieldOfStudy;
    private String modeOfStudy;
    private Integer durationOfEducation;
    private Integer scope;
    private String dateOfIssue;
    private String placeOfIssue;
    private PersonTypeXML holderOfAchievement;

    public HEEvidenceTypeXML(){
        super();
    }

    public HEEvidenceTypeXML(String degree, String country, String institutionName, String studyProgramme, String mainFieldOfStudy, String modeOfStudy, Integer durationOfEducation, Integer scope, String dateOfIssue, String placeOfIssue, PersonTypeXML holderOfAchievement) {
        this.degree = degree;
        this.country = country;
        this.institutionName = institutionName;
        this.studyProgramme = studyProgramme;
        this.mainFieldOfStudy = mainFieldOfStudy;
        this.modeOfStudy = modeOfStudy;
        this.durationOfEducation = durationOfEducation;
        this.scope = scope;
        this.dateOfIssue = dateOfIssue;
        this.placeOfIssue = placeOfIssue;
        this.holderOfAchievement = holderOfAchievement;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getStudyProgramme() {
        return studyProgramme;
    }

    public void setStudyProgramme(String studyProgramme) {
        this.studyProgramme = studyProgramme;
    }

    public String getMainFieldOfStudy() {
        return mainFieldOfStudy;
    }

    public void setMainFieldOfStudy(String mainFieldOfStudy) {
        this.mainFieldOfStudy = mainFieldOfStudy;
    }

    public String getModeOfStudy() {
        return modeOfStudy;
    }

    public void setModeOfStudy(String modeOfStudy) {
        this.modeOfStudy = modeOfStudy;
    }

    public Integer getDurationOfEducation() {
        return durationOfEducation;
    }

    public void setDurationOfEducation(Integer durationOfEducation) {
        this.durationOfEducation = durationOfEducation;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public PersonTypeXML getHolderOfAchievement() {
        return holderOfAchievement;
    }

    public void setHolderOfAchievement(PersonTypeXML holderOfAchievement) {
        this.holderOfAchievement = holderOfAchievement;
    }

    @Override
    public String toString() {
        return "Evidence [degree=" + degree + ", country=" + country + ", institution=" + institutionName +
                ", studyProgramme=" + studyProgramme + ", mainFieldOfStudy=" + mainFieldOfStudy + ", modeOfStudy=" + modeOfStudy
        + ", duration=" + durationOfEducation + ", scope=" + scope + ", dateOfIssue=" + dateOfIssue + ", placeOfIssue=" + placeOfIssue +
                ", holder=" + holderOfAchievement + "]";
    }
}
