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

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

@Path("/validate-vp")
public class ValidateVPResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{userId}")
    public String validateVP(@PathParam("userId") String userId, String eidasMds) throws IOException, ParseException {
        int subjectCheckResult = 0, schemaCheckResult = 0, issuerCheckResult = 0, signatureCheck = 0; // not valid
        DBUtil dbUtil = new DBUtil();

        JSONObject jsonEIDAS = null;
        JSONParser jsonParser = new JSONParser();
        try {
            jsonEIDAS = (JSONObject) jsonParser.parse(eidasMds);
            System.out.println("Received: " + jsonEIDAS.toJSONString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // DONE: call database getVPStatus(userId): VPStatus object
        VPStatus userVPStatus = dbUtil.getVPStatus(userId);

        ValidationObj validationObj = new ValidationObj(subjectCheckResult,schemaCheckResult,issuerCheckResult, signatureCheck, "");
        if(!(userVPStatus.getVPStatusEnum() == VPStatusEnum.REQUEST_SENT) && !(userVPStatus.getVPStatusEnum() == VPStatusEnum.VP_REJECTED) ){
            // DONE: call Aries /verifiable/presentations: [presentation]
            System.out.println("[VALIDATE-VP] User VP name: " + userVPStatus.getVpName());
            AriesUtil ariesUtil = new AriesUtil();
            JSONObject jsonPresentation = ariesUtil.getPresentation(userVPStatus.getVpName());
            if (jsonPresentation != null) {
                System.out.println("[VALIDATE VP] Presentation: " + jsonPresentation.toString());

                String vpID = jsonPresentation.get("id").toString();

                String base64vpID = Base64.getEncoder().encodeToString(vpID.getBytes(StandardCharsets.UTF_8));
                System.out.println("[VALIDATE VP] Encoded VP ID: " + base64vpID);

                JSONObject jsonVP = ariesUtil.findPresentationByID(base64vpID);

                if (jsonVP != null) {
                    System.out.println("[VALIDATE VP] VP holder: " + jsonVP.get("holder"));
                    System.out.println("[VALIDATE VP] EIDAS user: " + jsonEIDAS.get("userId"));

                    // signature check
                    if (jsonVP.containsKey("verifiableCredential")) {
                        JSONArray credentials = (JSONArray) jsonVP.get("verifiableCredential");
                        System.out.println("[VALIDATE VP] VC: " + credentials.get(0));
                        boolean vcCheck = ariesUtil.validateVCProof(new ValidateVCRequest(credentials.get(0).toString()));
                        System.out.println("Signature check: " + vcCheck);
                        if (vcCheck == true)
                            signatureCheck = 1;
                        else
                            signatureCheck = 0;
                    }

                    // holder check
                    if (jsonEIDAS.get("userId") != null) {
                        subjectCheckResult = checkSubject(jsonVP.get("holder").toString().trim(), jsonEIDAS.get("userId").toString().trim());
                        System.out.println(subjectCheckResult);
                    }

                    validationObj = new ValidationObj(subjectCheckResult, 1, 1, signatureCheck, userVPStatus.getVpName());
                    if (validationObj.getSubjectCheck() == 1 && validationObj.getSchemaCheck() == 1 && validationObj.getIssuerCheck() == 1 && validationObj.getSignatureCheck() == 1){ //TODO replace with checking TIR/TSR
                        System.out.println("[VALIDATE VP] Updating DB status to VALID...");
                        dbUtil.updateVPStatus(userId, VPStatusEnum.VP_VALID);
                    }
                    else {
                        System.out.println("[VALIDATE VP] Updating DB status to INVALID...");
                        dbUtil.updateVPStatus(userId,VPStatusEnum.VP_NOT_VALID);
                    }
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonValidation = mapper.writeValueAsString(validationObj);

        return jsonValidation;
    }


    private int checkSubject(String vpSubject, String eidasSubject){
        int result = 0;
        if(vpSubject.equals(eidasSubject))
            result = 1;
        return result;
    }
}

