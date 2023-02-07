package um.si.de4a.resources.vp;

public class    eIDASObject {
    private String personIdentifier;
    private String currentGivenName;
    private String currentFamilyName;
    private String dateOfBirth;

    public eIDASObject(String personIdentifier, String currentGivenName, String currentFamilyName, String dateOfBirth) {
        this.personIdentifier = personIdentifier;
        this.currentGivenName = currentGivenName;
        this.currentFamilyName = currentFamilyName;
        this.dateOfBirth = dateOfBirth;
    }

    public String getPersonIdentifier() {
        return personIdentifier;
    }

    public void setPersonIdentifier(String personIdentifier) {
        this.personIdentifier = personIdentifier;
    }

    public String getCurrentGivenName() {
        return currentGivenName;
    }

    public void setCurrentGivenName(String currentGivenName) {
        this.currentGivenName = currentGivenName;
    }

    public String getCurrentFamilyName() {
        return currentFamilyName;
    }

    public void setCurrentFamilyName(String currentFamilyName) {
        this.currentFamilyName = currentFamilyName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
