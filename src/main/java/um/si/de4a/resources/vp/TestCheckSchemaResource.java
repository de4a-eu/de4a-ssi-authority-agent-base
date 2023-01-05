package um.si.de4a.resources.vp;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import id.walt.model.TrustedIssuer;
import id.walt.services.essif.TrustedIssuerClient;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VPStatus;
import um.si.de4a.db.VPStatusEnum;
import um.si.de4a.util.DE4ALogger;
import um.si.de4a.util.JsonSchemaValidator;

import javax.ws.rs.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/check-schema")
public class TestCheckSchemaResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String validateVP() throws IOException, ParseException {


        JSONObject jsonSchema = null;
        JSONParser jsonParser = new JSONParser();

        int schemaCheckResult = -1;

        //evidence = jsonOffer.get("evidence").toString();
        String evidence = "{\n" +
                "  \"credentialSubject\": {\n" +
                "    \"currentFamilyName\": \"Alves\",\n" +
                "    \"currentGivenName\": \"Alice\",\n" +
                "    \"dateOfBirth\": \"1997-01-01T00:00:00.000Z\",\n" +
                "    \"personIdentifier\": \"123456789\",\n" +
                "    \"learningAchievement\": {\n" +
                "      \"@id\": \"urn:epass:learningAchievement:1\",\n" +
                "      \"title\": {\n" +
                "        \"text\": {\n" +
                "          \"@content-type\": \"text/html\",\n" +
                "          \"@lang\": \"pt\",\n" +
                "          \"#text\": \"Mestrado em Engenharia Inform�tica e de Computadores\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"specifiedBy\": {\n" +
                "        \"@idref\": \"urn:epass:qualification:1\"\n" +
                "      },\n" +
                "      \"wasDerivedFrom\": {\n" +
                "        \"@idref\": \"urn:epass:assessment:1\"\n" +
                "      },\n" +
                "      \"wasAwardedBy\": {\n" +
                "        \"@idref\": \"urn:epass:awardingprocess:1\"\n" +
                "      },\n" +
                "      \"associatedLearningOpportunity\": {\n" +
                "        \"@idref\": \"urn:epass:learningopportunity:1\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"learningSpecificationReferences\": {\n" +
                "      \"qualification\": {\n" +
                "        \"@id\": \"urn:epass:qualification:1\",\n" +
                "        \"title\": {\n" +
                "          \"text\": {\n" +
                "            \"@content-type\": \"text/html\",\n" +
                "            \"@lang\": \"pt\",\n" +
                "            \"#text\": \"Mestrado em Engenharia Inform�tica e de Computadores\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"volumeOfLearning\": {\n" +
                "          \"duration\": {\n" +
                "            \"#duration\": \"P2Y\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"ISCEDFCode\": {\n" +
                "          \"code\": {\n" +
                "            \"#code\": \"123\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"ECTSCreditPoints\": {\n" +
                "          \"numericScore\": {\n" +
                "            \"#numericScore\": \"120\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"assessmentReferences\": {\n" +
                "      \"assessment\": {\n" +
                "        \"@id\": \"urn:epass:assessment:1\",\n" +
                "        \"title\": {\n" +
                "          \"text\": {\n" +
                "            \"@content-type\": \"text/html\",\n" +
                "            \"@lang\": \"pt\",\n" +
                "            \"#text\": \"Mestrado em Engenharia Inform�tica e de Computadores\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"specifiedBy\": {\n" +
                "          \"@idref\": \"urn:epass:assessmentspec:1\"\n" +
                "        },\n" +
                "        \"issuedDate\": {\n" +
                "          \"dateTime\": {\n" +
                "            \"#dateTime\": \"2021-01-31\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"awardingProcessReferences\": {\n" +
                "      \"awardingProcess\": {\n" +
                "        \"@id\": \"urn:epass:awardingprocess:1\",\n" +
                "        \"awardingDate\": {\n" +
                "          \"dateTime\": {\n" +
                "            \"#dateTime\": \"2021-01-31\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"awardingLocation\": {\n" +
                "          \"@idref\": \"urn:epass:location:1\"\n" +
                "        },\n" +
                "        \"awardingBody\": {\n" +
                "          \"@idref\": \"urn:epass:organisation:1\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"locationReferences\": {\n" +
                "      \"location\": {\n" +
                "        \"@id\": \"urn:epass:location:1\",\n" +
                "        \"geographicName\": {\n" +
                "          \"text\": {\n" +
                "            \"@content-type\": \"text/html\",\n" +
                "            \"@lang\": \"pt\",\n" +
                "            \"#text\": \"Lisboa - Portugal\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"spatialCode\": {\n" +
                "          \"@targetFrameworkUrl\": \"http://publications.europa.eu/resource/authority/country\",\n" +
                "          \"@targetNotation\": \"PT\",\n" +
                "          \"@uri\": \"http://publications.europa.eu/resource/authority/country/PT\",\n" +
                "          \"targetFrameworkName\": {\n" +
                "            \"text\": {\n" +
                "              \"@content-type\": \"text/plain\",\n" +
                "              \"@lang\": \"en\",\n" +
                "              \"#text\": \"Countries Named Authority List\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"targetName\": {\n" +
                "            \"text\": {\n" +
                "              \"@content-type\": \"text/plain\",\n" +
                "              \"@lang\": \"en\",\n" +
                "              \"#text\": \"Portugal\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"agentReferences\": {\n" +
                "      \"organisation\": {\n" +
                "        \"id\": \"urn:epass:organisation:1\",\n" +
                "        \"preferredName\": {\n" +
                "          \"text\": {\n" +
                "            \"@content-type\": \"text/html\",\n" +
                "            \"@lang\": \"pt\",\n" +
                "            \"#text\": \"Instituto Superior T�cnico\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"learningOpportunityReferences\": {\n" +
                "      \"learningOpportunity\": {\n" +
                "        \"id\": \"urn:epass:learningopportunity:1\",\n" +
                "        \"learningSchedule\": {\n" +
                "          \"@targetFrameworkUrl\": \"http://publications.europa.eu/resource/dataset/learning-schedule\",\n" +
                "          \"@targetNotation\": \"learning-schedule\",\n" +
                "          \"@uri\": \"http://publications.europa.eu/resource/dataset/learning-schedule/note_e69f39d050\",\n" +
                "          \"targetFrameworkName\": {\n" +
                "            \"text\": {\n" +
                "              \"@content-type\": \"text/plain\",\n" +
                "              \"@lang\": \"en\",\n" +
                "              \"#text\": \"Europass Standard List of Learning Schedule Types\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"targetName\": {\n" +
                "            \"text\": {\n" +
                "              \"@content-type\": \"text/plain\",\n" +
                "              \"@lang\": \"en\",\n" +
                "              \"#text\": \"Full time (more then 30 hours)\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"issuanceDate\": \"2021-01-31T00:00:00.000Z\",\n" +
                "  \"validFrom\": \"2022-02-16T07:32:08.686Z\",\n" +
                "  \"expirationDate\": \"2023-02-16T07:32:08.686Z\",\n" +
                "  \"id\": \"http://de4a.eu/credentials/d0b7145e-741a-4335-a602-2aee98f0ff7e\",\n" +
                "  \"proof\": {\n" +
                "    \"created\": \"2022-02-16T07:32:08.68748279Z\",\n" +
                "    \"jws\": \"eyJhbGciOiJFZERTQSIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..mXCipHdq4ephIbel-zpjYO_-4xSIfAc6pnRRi6fDWkwo8glxhcoIZgEIGJuDk9OWAKqQ8IkI9oOZM_VZO9o4AA\",\n" +
                "    \"proofPurpose\": \"assertionMethod\",\n" +
                "    \"type\": \"Ed25519Signature2018\",\n" +
                "    \"verificationMethod\": \"did:ebsi:zbsVd4GeCWaUpN4uZSQgeL6#b509306e45704e21a0e4662f556d487c\"\n" +
                "  },\n" +
                "  \"type\": [\n" +
                "    \"VerifiableCredential\",\n" +
                "    \"UniversityDegreeCredential\"\n" +
                "  ],\n" +
                "  \"@context\": [\n" +
                "    \"https://www.w3.org/2018/credentials/v1\",\n" +
                "    \"https://www.w3.org/2018/credentials/examples/v1\"\n" +
                "  ],\n" +
                "  \"issuer\": \"did:ebsi:zbsVd4GeCWaUpN4uZSQgeL6\"\n" +
                "}";

        /*try {
            jsonSchema = (JSONObject) jsonParser.parse(testSchema);


        } catch (ParseException e) {

            e.printStackTrace();
        }*/

        try {
            schemaCheckResult = checkSchema(evidence);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        ValidationObj validationObj = new ValidationObj(-1,schemaCheckResult,-1, -1, "");


        ObjectMapper mapper = new ObjectMapper();
        String jsonValidation = mapper.writeValueAsString(validationObj);

        return jsonValidation;
    }


    private int checkSchema(String vc) throws IOException, URISyntaxException {
        int result = 0;

        JsonSchemaValidator validator = new JsonSchemaValidator();
        //JsonSchema schema = validator.getJsonSchemaFromUrl("https://ec.europa.eu/digital-building-blocks/code/projects/EBSI/repos/json-schema/raw/schemas/ebsi-attestation/2022-02/schema.json?at=refs%2Fheads%2Fmain");
        String jsonObject = readJsonFromUrl("http://172.21.0.2:9099/de4a-diploma-schema.json");
        //String jsonObject = readJsonFromUrl("https://ec.europa.eu/digital-building-blocks/code/projects/EBSI/repos/json-schema/raw/schemas/ebsi-attestation/2022-02/schema.json?at=refs%2Fheads%2Fmain");

        String schemaJSON = StringEscapeUtils.escapeJava(jsonObject);

       // System.out.println("Schema JSON: " + jsonObject);
        JsonNode schemaNode = validator.getJsonNodeFromStringContent(jsonObject);
        System.out.println("Schema node initialized!");
        JsonSchema schema = validator.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);

        schema.initializeValidators();
        /*JsonNode schemaNode = validator.getJsonNodeFromStringContent(
                "{\n" +
                        "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                        "  \"title\": \"EBSI Verifiable Attestation\",\n" +
                        "  \"description\": \"Schema of an EBSI Verifiable Attestation\",\n" +
                        "  \"type\": \"object\",\n" +
                        "  \"properties\": {\n" +
                        "    \"@context\": {\n" +
                        "      \"description\": \"Defines semantic context of the Verifiable Attestation\",\n" +
                        "      \"type\": \"array\",\n" +
                        "      \"items\": {\n" +
                        "        \"type\": \"string\",\n" +
                        "        \"format\": \"uri\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"id\": {\n" +
                        "      \"description\": \"Defines unique identifier of the Verifiable Attestation\",\n" +
                        "      \"type\": \"string\",\n" +
                        "      \"format\": \"uri\"\n" +
                        "    },\n" +
                        "    \"type\": {\n" +
                        "      \"description\": \"Defines the Verifiable Credential type\",\n" +
                        "      \"type\": \"array\",\n" +
                        "      \"items\": {\n" +
                        "        \"type\": \"string\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"issuer\": {\n" +
                        "      \"description\": \"Defines the issuer of the Verifiable Attestation\",\n" +
                        "      \"type\": \"string\",\n" +
                        "      \"format\": \"uri\"\n" +
                        "    },\n" +
                        "    \"issuanceDate\": {\n" +
                        "      \"description\": \"Defines the date and time, when the Verifiable Attestation becomes valid\",\n" +
                        "      \"type\": \"string\",\n" +
                        "      \"format\": \"date-time\"\n" +
                        "    },\n" +
                        "    \"validFrom\": {\n" +
                        "      \"description\": \"Defines the date and time, when the Verifiable Attestation becomes valid\",\n" +
                        "      \"type\": \"string\",\n" +
                        "      \"format\": \"date-time\"\n" +
                        "    },\n" +
                        "    \"issued\": {\n" +
                        "      \"description\": \"Defines when the Verifiable Attestation was issued\",\n" +
                        "      \"type\": \"string\",\n" +
                        "      \"format\": \"date-time\"\n" +
                        "    },\n" +
                        "    \"expirationDate\": {\n" +
                        "      \"description\": \"Defines the date and time, when the Verifiable Attestation expires\",\n" +
                        "      \"type\": \"string\",\n" +
                        "      \"format\": \"date-time\"\n" +
                        "    },\n" +
                        "    \"credentialSubject\": {\n" +
                        "      \"description\": \"Defines information about the subject that is described by the Verifiable Attestation\",\n" +
                        "      \"type\": \"object\",\n" +
                        "      \"properties\": {\n" +
                        "        \"id\": {\n" +
                        "          \"description\": \"Defines the DID of the subject that is described by the Verifiable Attestation\",\n" +
                        "          \"type\": \"string\",\n" +
                        "          \"format\": \"uri\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"credentialStatus\": {\n" +
                        "      \"description\": \"Contains information about how to verify the status of the Verifiable Attestation (via the Revocation and Endorsement Registry, RER)\",\n" +
                        "      \"type\": \"object\",\n" +
                        "      \"properties\": {\n" +
                        "        \"id\": {\n" +
                        "          \"description\": \"References record in the Revocation and Endorsement Registry (RER) to enable verification of a Verifiable Attestation’s validity\",\n" +
                        "          \"type\": \"string\",\n" +
                        "          \"format\": \"uri\"\n" +
                        "        },\n" +
                        "        \"type\": {\n" +
                        "          \"description\": \"Defines the Verifiable Credential status type\",\n" +
                        "          \"type\": \"string\"\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"required\": [\"id\", \"type\"]\n" +
                        "    },\n" +
                        "    \"credentialSchema\": {\n" +
                        "      \"description\": \"Contains information about the credential schema (template) on which the Verifiable Authorisation is based\",\n" +
                        "      \"type\": \"object\",\n" +
                        "      \"properties\": {\n" +
                        "        \"id\": {\n" +
                        "          \"description\": \"References the credential schema (template) stored on the (relevant) Trusted Schemas Registry (TSR) on which the Verifiable Authorisation is based\",\n" +
                        "          \"type\": \"string\",\n" +
                        "          \"format\": \"uri\"\n" +
                        "        },\n" +
                        "        \"type\": {\n" +
                        "          \"description\": \"Defines credential schema type\",\n" +
                        "          \"type\": \"string\",\n" +
                        "          \"enum\": [\"FullJsonSchemaValidator2021\"]\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"required\": [\"id\", \"type\"]\n" +
                        "    },\n" +
                        "    \"evidence\": {\n" +
                        "      \"description\": \"Contains information about the process which resulted in the issuance of the Verifiable Attestation\",\n" +
                        "      \"type\": \"array\",\n" +
                        "      \"items\": {\n" +
                        "        \"type\": \"object\",\n" +
                        "        \"properties\": {\n" +
                        "          \"id\": {\n" +
                        "            \"description\": \"If present, it MUST contain a URL that points to where more information about this instance of evidence can be found.\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          },\n" +
                        "          \"type\": {\n" +
                        "            \"description\": \"Defines the evidence type\",\n" +
                        "            \"type\": \"array\",\n" +
                        "            \"items\": {\n" +
                        "              \"type\": \"string\"\n" +
                        "            }\n" +
                        "          },\n" +
                        "          \"verifier\": {\n" +
                        "            \"description\": \"Defines entity which has verified documents before Verifiable Attestation issuance\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          },\n" +
                        "          \"evidenceDocument\": {\n" +
                        "            \"description\": \"Defines document(s) which have been verified before Verifiable Attestation issuance\",\n" +
                        "            \"type\": \"array\",\n" +
                        "            \"items\": {\n" +
                        "              \"type\": \"string\"\n" +
                        "            }\n" +
                        "          },\n" +
                        "          \"subjectPresence\": {\n" +
                        "            \"description\": \"Defines if the Verifiable Attestation subject was physically present in the course of the verification\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          },\n" +
                        "          \"documentPresence\": {\n" +
                        "            \"description\": \"Defines how the document(s) which have been verified before Verifiable Attestation issuance have been provided (e.g. physically, digitally)\",\n" +
                        "            \"type\": \"array\",\n" +
                        "            \"items\": {\n" +
                        "              \"type\": \"string\"\n" +
                        "            }\n" +
                        "          }\n" +
                        "        },\n" +
                        "        \"required\": [\n" +
                        "          \"type\",\n" +
                        "          \"verifier\",\n" +
                        "          \"evidenceDocument\",\n" +
                        "          \"subjectPresence\",\n" +
                        "          \"documentPresence\"\n" +
                        "        ]\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"proof\": {\n" +
                        "      \"description\": \"Contains information about the proof\",\n" +
                        "      \"type\": \"object\",\n" +
                        "      \"properties\": {\n" +
                        "        \"type\": {\n" +
                        "          \"description\": \"Defines the proof type\",\n" +
                        "          \"type\": \"string\"\n" +
                        "        },\n" +
                        "        \"proofPurpose\": {\n" +
                        "          \"description\": \"Defines the purpose of the proof\",\n" +
                        "          \"type\": \"string\"\n" +
                        "        },\n" +
                        "        \"created\": {\n" +
                        "          \"description\": \"Defines the date and time, when the proof has been created\",\n" +
                        "          \"type\": \"string\",\n" +
                        "          \"format\": \"date-time\"\n" +
                        "        },\n" +
                        "        \"verificationMethod\": {\n" +
                        "          \"description\": \"Contains information about the verification method / proof mechanisms\",\n" +
                        "          \"type\": \"string\"\n" +
                        "        },\n" +
                        "        \"jws\": {\n" +
                        "          \"description\": \"Defines the proof value in JWS format\",\n" +
                        "          \"type\": \"string\"\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"required\": [\n" +
                        "        \"type\",\n" +
                        "        \"proofPurpose\",\n" +
                        "        \"created\",\n" +
                        "        \"verificationMethod\",\n" +
                        "        \"jws\"\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"required\": [\n" +
                        "    \"@context\",\n" +
                        "    \"id\",\n" +
                        "    \"type\",\n" +
                        "    \"issuer\",\n" +
                        "    \"issuanceDate\",\n" +
                        "    \"validFrom\",\n" +
                        "    \"issued\",\n" +
                        "    \"credentialSubject\",\n" +
                        "    \"credentialSchema\"\n" +
                        "  ]\n" +
                        "}");
        //JsonNode schemaNode = validator.getJsonNodeFromStringContent(
         //       "{\"$schema\": \"http://json-schema.org/draft-06/schema#\", \"properties\": { \"id\": {\"type\": \"number\"}}}");
        //JsonSchema schema = validator.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);

        //schema.initializeValidators();
*/
        JsonNode node = null;
        try {
            node = validator.getJsonNodeFromStringContent(vc);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Set<ValidationMessage> errors = null;
        try {
            errors = schema.validate(node);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("Schema validation result - no of errors: " + errors.size());

        Iterator<ValidationMessage> it = errors.iterator();
        while(it.hasNext())
            System.out.println("Schema error: " + it.next().getMessage());
        if(errors.size() == 0)
            result = 1;


        return result;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        JSONObject json = null;
        String jsonText = "";
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            jsonText = readAll(rd);

            JSONParser jsonParser = new JSONParser();
            json = (JSONObject) jsonParser.parse(jsonText);

        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        return jsonText;
    }
}

