package um.si.de4a.util;

import um.si.de4a.model.xml.CredentialSubject;
import um.si.de4a.model.xml.Person;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PersonAdapter extends XmlAdapter<PersonAdapter.AdaptedPerson, Person> {


    @Override
    public Person unmarshal(AdaptedPerson adaptedPerson) throws Exception {
        if (null == adaptedPerson) {
            return null;
        }
        if (null != adaptedPerson.nationalId) {
            Person resultPerson = new Person(){};
            resultPerson.setFullName(adaptedPerson.fullName);
            resultPerson.setNationalId(adaptedPerson.nationalId);
            resultPerson.setFamilyName(adaptedPerson.familyName);
            resultPerson.setGivenNames(adaptedPerson.givenNames);
            resultPerson.setGender(adaptedPerson.gender);
            resultPerson.setDateOfBirth(adaptedPerson.dateOfBirth);
            resultPerson.setPlaceOfBirth(adaptedPerson.placeOfBirth);
            resultPerson.setCitizenshipCountry(adaptedPerson.citizenshipCountry);
            return resultPerson;
        } else {
           return null;
        }
    }

    @Override
    public AdaptedPerson marshal(Person person) throws Exception {
        if (person == null)
            return null;
        AdaptedPerson adaptedPerson = new AdaptedPerson();
        if(person instanceof Person){
            Person resultPerson = (Person) person;
            adaptedPerson.nationalId = resultPerson.getNationalId();
            adaptedPerson.citizenshipCountry = resultPerson.getCitizenshipCountry();
            adaptedPerson.dateOfBirth = resultPerson.getDateOfBirth();
            adaptedPerson.familyName = resultPerson.getFamilyName();
            adaptedPerson.fullName = resultPerson.getFullName();
            adaptedPerson.givenNames = resultPerson.getGivenNames();
            adaptedPerson.gender = resultPerson.getGender();
            adaptedPerson.placeOfBirth = resultPerson.getPlaceOfBirth();
        }
        else{

        }
        return adaptedPerson;
    }

    public static class AdaptedPerson {
        @XmlAttribute
        private String nationalId;

        @XmlAttribute
        private String fullName;

        @XmlAttribute
        private String givenNames;

        @XmlAttribute
        private String familyName;

        @XmlAttribute
        private String dateOfBirth;

        @XmlAttribute
        private String placeOfBirth;

        @XmlAttribute
        private String gender;

        @XmlAttribute
        private String citizenshipCountry;

    }
}

