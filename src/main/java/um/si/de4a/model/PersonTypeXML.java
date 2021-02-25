package um.si.de4a.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "holderOfAchievement")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PersonTypeXML implements Serializable {
    private String firstName;
    private String lastName;

    public PersonTypeXML(){
        super();
    }

    public PersonTypeXML(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Holder [firstname=" + firstName + ", lastname=" + lastName + "]";
    }
}
