
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.*;
import json.assessment.Assessment;
import json.assessment.AssessmentReferences;
import json.assessment.DateTime;
import json.assessment.IssuedDate;
import json.awardingprocess.AwardingBody;
import json.awardingprocess.AwardingLocation;
import json.awardingprocess.AwardingProcess;
import json.awardingprocess.AwardingProcessReferences;
import json.location.Location;
import json.location.LocationReferences;
import json.location.SpatialCode;
import json.opportunity.LearningOpportunity;
import json.opportunity.LearningOpportunityReferences;
import json.opportunity.LearningSchedule;
import json.organisation.AgentReferences;
import json.organisation.Organisation;
import json.qualification.*;
import xml.HigherEducationDiploma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.UUID;

public class XMLtoJSONAdapter {

    public static HigherEducationDiploma convertXMLToPOJO(String xml){
        System.out.println(xml);
        JAXBContext jaxbContext;
        HigherEducationDiploma diploma = null;
        try {
            jaxbContext = JAXBContext.newInstance(HigherEducationDiploma.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            diploma = (HigherEducationDiploma) jaxbUnmarshaller.unmarshal(new StringReader(xml));
            System.out.println(diploma.getPlaceOfIssue().getName().getText().getValue());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return diploma;
    }

    public static String convertPOJOtoJSON(HigherEducationDiploma diploma, String myDID) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String[] context = {"https://www.w3.org/2018/credentials/v1", "https://essif.europa.eu/schemas/vc/2020/v1"};
        String[] type = {"VerifiableCredential", "VerifiableAttestation"};
        LearningAchievement learningAchievement = new LearningAchievement("urn:epass:learningAchievement:1",
                new Title(new Text(diploma.getTitle().getText().getContentType(), diploma.getTitle().getText().getLang(), diploma.getTitle().getText().getValue())), new SpecifiedBy("urn:epass:qualification:1"), new WasDerivedFrom("urn:epass:assessment:1"),
                new WasAwardedBy("urn:epass:awardingprocess:1"), new AssociatedLearningOpportunity("urn:epass:learningopportunity:1")
        );

        Qualification qualification = new Qualification("urn:epass:qualification:1",new Title(new Text(diploma.getTitle().getText().getContentType(), diploma.getTitle().getText().getLang(), diploma.getTitle().getText().getValue())),
                new VolumeOfLearning(new Duration(diploma.getDurationOfEducation())),new ISCEDFCode(new Code("123")),new ECTSCreditPoints(new NumericScore(diploma.getScope())));
        LearningSpecificationReferences lsr = new LearningSpecificationReferences(qualification);

        Assessment assessment = new Assessment("urn:epass:assessment:1", new Title(new Text(diploma.getStudyProgramme().getContentType(), diploma.getStudyProgramme().getLang(), diploma.getStudyProgramme().getValue())), new SpecifiedBy("urn:epass:assessmentspec:1"), new IssuedDate(new DateTime(diploma.getDateOfIssue())));
        AssessmentReferences ar = new AssessmentReferences(assessment);

        AwardingProcess awardingProcess = new AwardingProcess("urn:epass:awardingprocess:1", new IssuedDate(new DateTime(diploma.getDateOfIssue())), new AwardingLocation("urn:epass:location:1"), new AwardingBody("urn:epass:organisation:1"));
        AwardingProcessReferences awr = new AwardingProcessReferences(awardingProcess);

        String countryCode = "", countryName = "";
        if(diploma.getPlaceOfIssue().getName().getText().getValue().equals("Maribor") || diploma.getPlaceOfIssue().getName().getText().getValue().equals("Ljubljana")){
            countryCode = "SLO";
            countryName = "Slovenia";
        }
        else{
            countryCode = "PT";
            countryName = "Portugal";
        }

        String modeOfStudy = "";
        if(diploma.getModeOfStudy().equals("http://data.europa.eu/europass/learningScheduleType/fullTime"))
            modeOfStudy = "Full time (more then 30 hours)";
        else
            modeOfStudy = "Part time";

        Location location = new Location("urn:epass:location:1", new Title(new Text(diploma.getPlaceOfIssue().getName().getText().getContentType(), diploma.getPlaceOfIssue().getName().getText().getLang(), diploma.getPlaceOfIssue().getName().getText().getValue())),
                new SpatialCode("http://publications.europa.eu/resource/authority/country", countryCode, "http://publications.europa.eu/resource/authority/country/" + countryCode,
                        new Title(new Text("text/plain", "en", "Countries Named Authority List")), new Title(new Text("text/plain", "en", countryName))));
        LocationReferences lr = new LocationReferences(location);

        Organisation organisation = new Organisation("urn:epass:organisation:1", new Title(new Text(diploma.getInstitutionName().getContentType(), diploma.getInstitutionName().getLang(), diploma.getInstitutionName().getValue())));
        AgentReferences agentReferences = new AgentReferences(organisation);

        LearningOpportunity opportunity = new LearningOpportunity( "urn:epass:learningopportunity:1", new LearningSchedule("http://publications.europa.eu/resource/dataset/learning-schedule", "learning-schedule",
                "http://publications.europa.eu/resource/dataset/learning-schedule/note_e69f39d050", new Title(new Text("text/plain", "en", "Europass Standard List of Learning Schedule Types")),
                new Title(new Text("text/plain", "en", modeOfStudy))));
        LearningOpportunityReferences lor = new LearningOpportunityReferences(opportunity);

        CredentialSubject subject = new CredentialSubject(diploma.getHolderOfAchievement().getFamilyName().getText().getValue().toString(),
                diploma.getHolderOfAchievement().getGivenNames().getText().getValue().toString(), diploma.getHolderOfAchievement().getDateOfBirth(), diploma.getHolderOfAchievement().getNationalId(),learningAchievement, lsr, ar, awr, lr,
                agentReferences, lor);

        CredentialStatus status = new CredentialStatus("https://essif.europa.eu/status/43", "CredentialsStatusList2020");
        CredentialSchema schema = new CredentialSchema("https://essif.europa.eu/tsr-123/verifiableattestation.json", "JsonSchemaValidator2018");
        Evidence evidence = new Evidence("https://essif.europa.eu/evidence/f2aeec97-fc0d-42bf-8ca7-0548192d4231", new String[]{"eIDAS"}, "https:// essif.europa.eu /issuers/48", new String[]{"eIDAS identifier"});

        VerifiableCredential vc = new VerifiableCredential(context, UUID.randomUUID().toString(), type, myDID, diploma.getDateOfIssue(),subject, status, schema, evidence);

        return gson.toJson(vc);
    }
}
