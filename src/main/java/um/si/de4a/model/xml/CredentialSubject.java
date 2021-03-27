package um.si.de4a.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "credentialSubject")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CredentialSubject extends Person{
    private String id;

    private Achievements achievements;

    public CredentialSubject(){};

    public CredentialSubject(String id, Achievements achievements) {
        this.id = id;
        this.achievements = achievements;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Achievements getAchievements() {
        return achievements;
    }

    public void setAchievements(Achievements achievements) {
        this.achievements = achievements;
    }
}
