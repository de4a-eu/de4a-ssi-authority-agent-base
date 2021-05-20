package um.si.de4a.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.internal.parser.DateParser;
import org.json.simple.JSONObject;
import um.si.de4a.model.json.*;
import um.si.de4a.model.json.assessment.Assessment;
import um.si.de4a.model.json.assessment.AssessmentReferences;
import um.si.de4a.model.json.assessment.DateTime;
import um.si.de4a.model.json.assessment.IssuedDate;
import um.si.de4a.model.json.awardingprocess.AwardingBody;
import um.si.de4a.model.json.awardingprocess.AwardingLocation;
import um.si.de4a.model.json.awardingprocess.AwardingProcess;
import um.si.de4a.model.json.awardingprocess.AwardingProcessReferences;
import um.si.de4a.model.json.location.Location;
import um.si.de4a.model.json.location.LocationReferences;
import um.si.de4a.model.json.location.SpatialCode;
import um.si.de4a.model.json.opportunity.LearningOpportunity;
import um.si.de4a.model.json.opportunity.LearningOpportunityReferences;
import um.si.de4a.model.json.opportunity.LearningSchedule;
import um.si.de4a.model.json.organisation.AgentReferences;
import um.si.de4a.model.json.organisation.Organisation;
import um.si.de4a.model.json.qualification.*;
import um.si.de4a.model.xml.HigherEducationDiploma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class XMLtoJSONAdapter {

    public static HigherEducationDiploma convertXMLToPOJO(String xml){

        JAXBContext jaxbContext;
        HigherEducationDiploma diploma = null;
        try {
            jaxbContext = JAXBContext.newInstance(HigherEducationDiploma.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            diploma = (HigherEducationDiploma) jaxbUnmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return diploma;
    }

    public static VerifiableCredential convertPOJOtoJSON(HigherEducationDiploma diploma, String myDID) throws ParseException {
        String[] context = {"https://www.w3.org/2018/credentials/v1", "https://www.w3.org/2018/credentials/examples/v1"};
        String[] type = {"VerifiableCredential", "UniversityDegreeCredential"};
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


        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);

        String inputIssueDate = diploma.getDateOfIssue();
        String inputBirthDate = diploma.getHolderOfAchievement().getDateOfBirth();

        Date dateIssued = inputFormat.parse(inputIssueDate);
        String outputDateIssued = outputFormat.format(dateIssued);

        Date dateBirth = inputFormat.parse(inputIssueDate);
        String outputDateBirth = outputFormat.format(dateBirth);

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
                diploma.getHolderOfAchievement().getGivenNames().getText().getValue().toString(), outputDateBirth, diploma.getHolderOfAchievement().getNationalId(),learningAchievement, lsr, ar, awr, lr,
                agentReferences, lor);

      /*  CredentialSubject subject = new CredentialSubject(diploma.getHolderOfAchievement().getFamilyName().getText().getValue().toString(),
                diploma.getHolderOfAchievement().getGivenNames().getText().getValue().toString(), outputDateBirth,diploma.getHolderOfAchievement().getNationalId());
*/
        //CredentialStatus status = new CredentialStatus("https://essif.europa.eu/status/43", "CredentialsStatusList2020");
        //CredentialSchema schema = new CredentialSchema("https://essif.europa.eu/tsr-123/verifiableattestation.json", "JsonSchemaValidator2018");
       // Evidence evidence = new Evidence("https://essif.europa.eu/evidence/f2aeec97-fc0d-42bf-8ca7-0548192d4231", new String[]{"eIDAS"}, "https://essif.europa.eu/issuers/48", new String[]{"eIDAS identifier"});

        VerifiableCredential vc = new VerifiableCredential(context, "http://example.edu/credentials/" + UUID.randomUUID(), type, myDID, outputDateIssued,subject);

        return vc;
    }
}
