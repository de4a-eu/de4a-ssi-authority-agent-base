package um.si.de4a.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import um.si.de4a.model.json.*;
import um.si.de4a.model.xml.Credential;
import um.si.de4a.model.xml.LearningAchievementType;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLtoJSONConverter {

    public static String toJSONObject(Credential credential) throws JsonProcessingException {

        String outputJsonString = "";

        CredentialSubject credentialSubject = new CredentialSubject();
        ArrayList<Achievement> jsonAchievements = new ArrayList<>();
        List<LearningAchievementType> xmlAchievements = credential.getCredentialSubject().getAchievements().getAchievementTypes();
        for (LearningAchievementType type:
             xmlAchievements) {
            Achievement achievement = new Achievement(convertID(type.getId()),type.getTitle(),null, null,
                   null);
            jsonAchievements.add(achievement);
        }

        PersonTypeJSON jsonPerson = new PersonTypeJSON(convertID(credential.getCredentialSubject().getId()),credential.getCredentialSubject().getFullName(),
                jsonAchievements,null);
        credentialSubject.setPerson(jsonPerson);

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        outputJsonString = objectWriter.writeValueAsString(credentialSubject);

        return outputJsonString;
    }

    public static IDFieldType convertID(String id){
        return new IDFieldType(id);
    }
}

