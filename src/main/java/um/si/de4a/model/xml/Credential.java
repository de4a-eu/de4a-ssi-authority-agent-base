package um.si.de4a.model.xml;

import com.sun.xml.txw2.annotation.XmlNamespace;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@XmlRootElement(name="credential")
@XmlAccessorType(XmlAccessType.FIELD)
public class Credential {
    private String identifier;

    @XmlElement(name = "type")
    private String type;

    @XmlElement(name = "validFrom")
    private String validFrom;

    @XmlElement(name = "issued")
    private String issuedDate;

    @XmlElement(name = "validUntil")
    private String validUntil;

    @XmlElement(name = "issuer")
    private Organization issuer;

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "credentialSubject")
    private CredentialSubject credentialSubject;

    public Credential(){};

    public Credential(String identifier, String type, String validFrom, String issuedDate, String validUntil, Organization issuer, String title, String description, CredentialSubject credentialSubject) {
        this.identifier = identifier;
        this.type = type;
        this.validFrom = validFrom;
        this.issuedDate = issuedDate;
        this.validUntil = validUntil;
        this.issuer = issuer;
        this.title = title;
        this.description = description;
        this.credentialSubject = credentialSubject;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public Organization getIssuer() {
        return issuer;
    }

    public void setIssuer(Organization issuer) {
        this.issuer = issuer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CredentialSubject getCredentialSubject() {
        return credentialSubject;
    }

    public void setCredentialSubject(CredentialSubject credentialSubject) {
        this.credentialSubject = credentialSubject;
    }

    @Override
    public String toString() {
        Achievements achievementTypes = credentialSubject.getAchievements();
        for (LearningAchievementType type:
             achievementTypes.getAchievementTypes()) {
            System.out.println(type.getId());
        }
        return "Credential [id: " + identifier + ", issuer: " + issuer + "\n]" +
                "\t\t Subject [fullname: " + credentialSubject.getFullName() + ", achievements: \n" +
                "\t\t\t\t Achievement [id: " + credentialSubject.getAchievements().getAchievementTypes().get(0).getId() + "]";
    }
}
