package um.si.de4a.model.json;

import um.si.de4a.model.xml.Credential;

public class CredentialSubject {
    private PersonTypeJSON person;

    public CredentialSubject(){};

    public CredentialSubject(PersonTypeJSON person) {
        this.person = person;
    }

    public PersonTypeJSON getPerson() {
        return person;
    }

    public void setPerson(PersonTypeJSON person) {
        this.person = person;
    }
}
