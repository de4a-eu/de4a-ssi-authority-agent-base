package um.si.de4a.resources.vp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VPStatus;
import um.si.de4a.db.VPStatusEnum;
import um.si.de4a.model.json.SignedVerifiableCredential;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/validate-vp")
public class ValidateVPResource {

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

        String eidasMds = "";

        try {
            jsonEIDAS = (JSONObject) jsonParser.parse(eidasMdsInput);

            eidasMds = new String(Base64.getDecoder().decode(jsonEIDAS.get("eidas").toString()));
            logRecordInfo.setMessage("VALIDATE-VP: Received input eIDAS user data.");
            Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "0201"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);

        } catch (ParseException e) {
            logRecordSevere.setMessage("VALIDATE-VP: Object conversion error on Authority Agent DR.");
            Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "20005"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
           // e.printStackTrace();
        }

        try {
            inputDecodedEIDAS = (JSONObject) jsonParser.parse(eidasMds);
            logRecordInfo.setMessage("VALIDATE-VP: Decoded input eIDAS user data.");
            Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "0208"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("VALIDATE-VP: Object conversion error on Authority Agent DR.");
            Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "20005"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }


        // DONE: call database getVPStatus(userId): VPStatus object
        VPStatus userVPStatus = null;
        try{
            userVPStatus = dbUtil.getVPStatus(userId);

            logRecordInfo.setMessage("VALIDATE-VP: Received user VPStatus status data.");
            Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "0104"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage( "VALIDATE-VP: Error accessing data on Authority Agent DR.");
            Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "20006"};
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
                logRecordSevere.setMessage( "VALIDATE-VP: Error accessing data on Authority Agent DR.");
                Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "20006"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            if (jsonPresentation != null) {

                JSONObject vc, subject = null;
                // signature check
                if (jsonPresentation.containsKey("verifiableCredential")) {
                    JSONArray credentials = null;
                    try{
                        credentials = (JSONArray) jsonPresentation.get("verifiableCredential");

                        vc = (JSONObject) credentials.get(0);
                        subject = (JSONObject) vc.get("credentialSubject");

                        logRecordInfo.setMessage("VALIDATE-VP: Processing the JSON response received from /get-vp.");
                        Object[] params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0102"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "VALIDATE-VP: Error on response from the Aries Government Agent.");
                        Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "10704"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                    boolean vcCheck = ariesUtil.validateVCProof(new ValidateVCRequest(credentials.get(0).toString()));
                    if (vcCheck == true)
                        signatureCheck = 1;
                    else
                        signatureCheck = 0;

                    logRecordInfo.setMessage("VALIDATE-VP: Validated the digital signature of the submitted Verifiable Presentation.");
                    Object[] params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0209"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }


                // holder check
                if (inputDecodedEIDAS.get("personIdentifier") != null) {
                    try {
                        subjectCheckResult = checkSubject(new eIDASObject(inputDecodedEIDAS.get("personIdentifier").toString().trim(), inputDecodedEIDAS.get("currentGivenName").toString().trim(), inputDecodedEIDAS.get("currentFamilyName").toString().trim(), inputDecodedEIDAS.get("dateOfBirth").toString().trim()),
                                new eIDASObject(subject.get("personIdentifier").toString().trim(), subject.get("currentGivenName").toString().trim(), subject.get("currentFamilyName").toString().trim(), subject.get("dateOfBirth").toString().trim()));

                        logRecordInfo.setMessage("VALIDATE-VP: Validated the subject of the submitted Verifiable Presentation.");
                        Object[] params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0210"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "VALIDATE-VP: Error accessing data on Authority Agent DR.");
                        Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "20006"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                }

                validationObj = new ValidationObj(subjectCheckResult, 1, 1, signatureCheck, userVPStatus.getVpName());
                if (validationObj.getSubjectCheck() == 1 && validationObj.getSchemaCheck() == 1 && validationObj.getIssuerCheck() == 1 && validationObj.getSignatureCheck() == 1){ //TODO replace with checking TIR/TSR
                    //System.out.println("[VALIDATE VP] Updating DB status to VALID...");
                    try {
                        dbUtil.updateVPStatus(userId, VPStatusEnum.VP_VALID);

                        logRecordInfo.setMessage("VALIDATE-VP: Stored current state in the Authority Agent DT internal database.");
                        Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "0103"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "VALIDATE-VP: Error saving data on Authority Agent DR.");
                        Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "20006"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                }
                else {
                    // System.out.println("[VALIDATE VP] Updating DB status to INVALID...");
                    try {
                        dbUtil.updateVPStatus(userId, VPStatusEnum.VP_NOT_VALID);

                        logRecordInfo.setMessage("VALIDATE-VP: Stored current state in the Authority Agent DT internal database.");
                        Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "0103"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "VALIDATE-VP: Error saving data on Authority Agent DR.");
                        Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "20006"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonValidation = mapper.writeValueAsString(validationObj);

        return jsonValidation;
    }


    private int checkSubject(eIDASObject inputData, eIDASObject vcData){
        int result = 0;
        if(inputData.getPersonIdentifier().equals(vcData.getPersonIdentifier()) && inputData.getCurrentGivenName().equals(vcData.getCurrentGivenName()) && inputData.getCurrentFamilyName().equals(vcData.getCurrentFamilyName()) && inputData.getDateOfBirth().equals(vcData.getDateOfBirth()))
            result = 1;
        return result;
    }
}

