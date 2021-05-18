package xml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="holderOfAchievement", namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
public class HolderOfAchievement {
    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#")
    private String nationalId;

    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#")
    private GivenNames givenNames;

    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#")
    private FamilyName familyName;

    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#")
    private String dateOfBirth;

    public HolderOfAchievement() {
    }

    public HolderOfAchievement(String nationalId, GivenNames givenNames, FamilyName familyName, String dateOfBirth) {
        this.nationalId = nationalId;
        this.givenNames = givenNames;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
    }


    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public GivenNames getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(GivenNames givenNames) {
        this.givenNames = givenNames;
    }

    public FamilyName getFamilyName() {
        return familyName;
    }

    public void setFamilyName(FamilyName familyName) {
        this.familyName = familyName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
