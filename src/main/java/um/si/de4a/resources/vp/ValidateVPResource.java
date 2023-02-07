package um.si.de4a.resources.vp;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VPStatus;
import um.si.de4a.db.VPStatusEnum;
import um.si.de4a.model.AttributesDE4A;
import um.si.de4a.model.TrustedIssuerDE4A;
import um.si.de4a.model.json.SignedVerifiableCredential;
import um.si.de4a.util.DE4ALogger;
import um.si.de4a.util.JsonSchemaValidator;
import um.si.de4a.util.XMLtoJSONAdapter;

import javax.ws.rs.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/validate-vp")
public class ValidateVPResource {

    private AppConfig appConfig;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{userId}")
    public String validateVP(@PathParam("userId") String userId, String eidasMdsInput) throws IOException, ParseException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        int subjectCheckResult = -1, schemaCheckResult = -1, issuerCheckResult = -1, signatureCheck = -1; // not valid
        DBUtil dbUtil = new DBUtil();

        JSONObject jsonEIDAS = null, inputDecodedEIDAS = null;
        JSONParser jsonParser = new JSONParser();

        String alias = "";
        String deURL = "";
        appConfig = new AppConfig();
        try {
            alias = appConfig.getProperties().getProperty("alias");
            deURL = appConfig.getProperties().getProperty("de.url");
        }
        catch (Exception ex){
            logRecordSevere.setMessage( "Configuration error occurred on Authority Agent.");
            Object[] params = new Object[]{"AAE09", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        String eidasMds = "";

        try {
            jsonEIDAS = (JSONObject) jsonParser.parse(eidasMdsInput);

            eidasMds = new String(Base64.getDecoder().decode(jsonEIDAS.get("eidas").toString()));
            logRecordInfo.setMessage("VALIDATE-VP: Decoded input eIDAS user data.");
            Object[] params = new Object[]{"AAI08", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);

        } catch (ParseException e) {
            logRecordSevere.setMessage("Object conversion error on Authority Agent: [VALIDATE-VP] " + e.getMessage() + ".");
            Object[] params = new Object[]{"AAE03", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        try {
            inputDecodedEIDAS = (JSONObject) jsonParser.parse(eidasMds);
            logRecordInfo.setMessage("VALIDATE-VP: Received input eIDAS user data.");
            Object[] params = new Object[]{"AAI01", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Object conversion error on Authority Agent: [VALIDATE-VP] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE03", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }


        // DONE: call database getVPStatus(userId): VPStatus object
        VPStatus userVPStatus = null;
        try{
            userVPStatus = dbUtil.getVPStatus(userId);
            logRecordInfo.setMessage("VALIDATE-VP: Received user VP status data.");
            Object[] params = new Object[]{"AAI14", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Error accessing data on Authority Agent internal database: [VALIDATE-VP] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        ValidationObj validationObj = new ValidationObj(subjectCheckResult,schemaCheckResult,issuerCheckResult, signatureCheck, "");
        if(!(userVPStatus.getVPStatusEnum() == VPStatusEnum.REQUEST_SENT) && !(userVPStatus.getVPStatusEnum() == VPStatusEnum.VP_REJECTED) ){
            // DONE: call Aries /verifiable/presentations: [presentation]
            //System.out.println("[VALIDATE-VP] User VP name: " + userVPStatus.getVpName());
            AriesUtil ariesUtil = new AriesUtil();
            JSONObject jsonPresentation = null;
            try{
                jsonPresentation = ariesUtil.getPresentation(userVPStatus.getVpName());
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "Error on response from Authority Agent: [VALIDATE-VP] " + ex.getMessage() + ".");
                Object[] params = new Object[]{"AAE02", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            if (jsonPresentation != null) {

                JSONObject vc = null, subject = null;
                String issuer = "";
                // signature check
                if (jsonPresentation.containsKey("verifiableCredential")) {
                    JSONArray credentials = null;
                    try{
                        credentials = (JSONArray) jsonPresentation.get("verifiableCredential");

                        vc = (JSONObject) credentials.get(0);
                        subject = (JSONObject) vc.get("credentialSubject");
                        issuer = vc.get("issuer").toString();
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "Error on response from Authority Agent: [VALIDATE-VP] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE02", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                    boolean vcCheck = ariesUtil.validateVCProof(new ValidateVCRequest(credentials.get(0).toString()));
                    if (vcCheck == true)
                        signatureCheck = 1;
                    else
                        signatureCheck = 0;

                    logRecordInfo.setMessage("VALIDATE-VP: Checked the digital signature of the submitted VP.");
                    Object[] params = new Object[]{"AAI09", alias};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }


                // holder check
                if (inputDecodedEIDAS.get("currentGivenName") != null) {
                    try {
                        subjectCheckResult = checkSubject(new eIDASObject(inputDecodedEIDAS.get("personIdentifier").toString().trim(), inputDecodedEIDAS.get("currentGivenName").toString().trim(), inputDecodedEIDAS.get("currentFamilyName").toString().trim(), inputDecodedEIDAS.get("dateOfBirth").toString().trim()),
                                new eIDASObject(subject.get("personIdentifier").toString().trim(), subject.get("currentGivenName").toString().trim(), subject.get("currentFamilyName").toString().trim(), subject.get("dateOfBirth").toString().trim()));

                        logRecordInfo.setMessage("VALIDATE-VP: Validated the subject of the submitted VP.");
                        Object[] params = new Object[]{"AAI10", alias};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "Error on response from Authority Agent: [VALIDATE-VP] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE02", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                }

                try {
                    issuerCheckResult = checkIssuer(issuer);
                    logRecordInfo.setMessage("VALIDATE-VP: Validated the issuer the submitted VP.");
                    Object[] params = new Object[]{"AAI30", alias};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage( "Error on response from Authority Agent: [VALIDATE-VP] " + ex.getMessage() + ".");
                    Object[] params = new Object[]{"AAE02", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }

                try {
                    schemaCheckResult = checkSchema(vc);
                    logRecordInfo.setMessage("VALIDATE-VP: Validated the schema the submitted VP.");
                    Object[] params = new Object[]{"AAI30", alias};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage( "Error on response from Authority Agent: [VALIDATE-VP] " + ex.getMessage() + ".");
                    Object[] params = new Object[]{"AAE02", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }

                validationObj = new ValidationObj(subjectCheckResult, schemaCheckResult, issuerCheckResult, signatureCheck, userVPStatus.getVpName());
                if (validationObj.getSubjectCheck() == 1 && validationObj.getSchemaCheck() == 1 && validationObj.getIssuerCheck() == 1 && validationObj.getSignatureCheck() == 1 && validationObj.getSchemaCheck() == 1){ //TODO replace with checking TIR/TSR
                    //System.out.println("[VALIDATE VP] Updating DB status to VALID...");
                    try {
                        dbUtil.updateVPStatus(userId, VPStatusEnum.VP_VALID);

                        logRecordInfo.setMessage("VALIDATE-VP: Stored current state in Authority Agent internal database.");
                        Object[] params = new Object[]{"AAI13", alias};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [VALIDATE-VP] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE04", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                }
                else {
                    // System.out.println("[VALIDATE VP] Updating DB status to INVALID...");
                    try {
                        dbUtil.updateVPStatus(userId, VPStatusEnum.VP_NOT_VALID);

                        logRecordInfo.setMessage("VALIDATE-VP: Stored current state in Authority Agent internal database.");
                        Object[] params = new Object[]{"AAI13", alias};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [VALIDATE-VP] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE04", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                }
                logRecordInfo.setMessage("VALIDATE-VP: Validated Verifiable Credential " + vc.get("id") + " of type " + XMLtoJSONAdapter.namespace  +
                        " under invitation" + userVPStatus.getDidConn().getInvitationId() + " for " + deURL + ".");
                Object[] params = new Object[]{"AAI13", alias};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonValidation = mapper.writeValueAsString(validationObj);

        return jsonValidation;
    }


    private int checkSubject(eIDASObject inputData, eIDASObject vcData){
        int result = 0;
        if( inputData.getCurrentGivenName().equalsIgnoreCase(vcData.getCurrentGivenName()) &&
                inputData.getCurrentFamilyName().equalsIgnoreCase(vcData.getCurrentFamilyName())
                && inputData.getDateOfBirth().equals(vcData.getDateOfBirth()))
            result = 1;
        return result;
    }

   private int checkIssuer(String did) throws IOException {
        int result = 0;
        TrustedIssuerDE4A issuerRecord = null;
       Logger logger = DE4ALogger.getLogger();
       LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
       LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

       String alias = "";
       appConfig = new AppConfig();
       try {
           alias = appConfig.getProperties().getProperty("alias");
       }
       catch (Exception ex){
           logRecordSevere.setMessage( "Configuration error occurred on Authority Agent.");
           Object[] params = new Object[]{"AAE09", alias};
           logRecordSevere.setParameters(params);
           logger.log(logRecordSevere);
       }

        try{
            System.out.println("Checking the issuer....");

            HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://api-pilot.ebsi.eu/trusted-issuers-registry/v3/issuers/" + did).openConnection();
            urlConnection.setRequestMethod("GET");
            try {
                urlConnection.connect();
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "Connection error with EBSI TIR endpoint: " + ex.getMessage());
                Object[] params = new Object[]{"AAE01", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                InputStream stream = urlConnection.getInputStream();

                String resultEBSI = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

                try {
                    System.out.println("RESULT EBSI: " + resultEBSI);
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(resultEBSI);

                    System.out.println("EBSI object: " + jsonObject);
                    if (!jsonObject.isEmpty() && jsonObject.get("did") != null) {
                        issuerRecord = new TrustedIssuerDE4A(jsonObject.get("did").toString());
                    }
                }catch(Exception ex){
                    logRecordSevere.setMessage( "Object conversion error in Authority Agent: [VALIDATE-VP] " + ex.getMessage());
                    Object[] params = new Object[]{"AAE03", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                    //System.out.println("[ARIES get connections] Exception: " + ex.getMessage());
                }

            } else {
                logRecordSevere.setMessage("Error on response from EBSI TIR endpoint: " + urlConnection.getResponseMessage());
                Object[] params = new Object[]{"AAE02", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            urlConnection.disconnect();

        }
        catch(Exception ex){
            result = 0;
            logRecordSevere.setMessage( "Error on response from Authority Agent: [VALIDATE-VP] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE02", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
        if(issuerRecord != null)
            result = 1;
        return result;
    }

    private int checkSchema(JSONObject vc) throws IOException {
        int result = 0;

        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");
        appConfig = new AppConfig();

        JsonSchemaValidator validator = new JsonSchemaValidator();
        String jsonObject = readJsonFromUrl(appConfig.getProperties().getProperty("vc.schema.url"));

        JsonNode schemaNode = validator.getJsonNodeFromStringContent(jsonObject);

        JsonSchema schema = validator.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);

        JsonNode node = null;
        try {
            node = validator.getJsonNodeFromStringContent(vc.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<ValidationMessage> errors = schema.validate(node);
        /*logRecordInfo.setMessage("VALIDATE-VP: Validated submitted VC schema. Number of mismatches detected: " + errors.size());
        Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "01022"};
        logRecordInfo.setParameters(params);
        logger.log(logRecordInfo);*/

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

