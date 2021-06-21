package um.si.de4a.resources.vp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VPStatus;
import um.si.de4a.db.VPStatusEnum;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Path("/validate-vp")
public class ValidateVPResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{userId}")
    public String validateVP(@PathParam("userId") String userId, String eidasMds) throws IOException {
        int subjectCheckResult = -1, schemaCheckResult = -1, issuerCheckResult = -1; // not relevant for validation
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

        ValidationObj validationObj = new ValidationObj(subjectCheckResult,schemaCheckResult,issuerCheckResult, "");
        if(!(userVPStatus.getVPStatusEnum() == VPStatusEnum.REQUEST_SENT) && !(userVPStatus.getVPStatusEnum() == VPStatusEnum.VP_REJECTED) ){
            // DONE: call Aries /verifiable/presentations: [presentation]
            System.out.println("user vp: " + userVPStatus.getVpName());
            AriesUtil ariesUtil = new AriesUtil();
            JSONObject jsonPresentation = ariesUtil.getPresentation(userVPStatus.getVpName());
            if (jsonPresentation != null){
                System.out.println("[VALIDATE VP] Presentation: " + jsonPresentation.toString());

                String vpID = jsonPresentation.get("id").toString();

                String base64vpID = Base64.getEncoder().encodeToString(vpID.getBytes(StandardCharsets.UTF_8));
                System.out.println("[VALIDATE VP] Encoded VP ID: " + base64vpID);

                JSONObject jsonVP = ariesUtil.findPresentationByID(base64vpID);

                if(jsonVP != null){
                    System.out.println("[VALIDATE VP] VP holder: " + jsonVP.get("holder"));
                    System.out.println("[VALIDATE VP] EIDAS user: " + jsonEIDAS.get("userId"));
                    if(jsonEIDAS.get("userId") != null) {
                        subjectCheckResult = checkSubject(jsonVP.get("holder").toString(), jsonEIDAS.get("userId").toString());
                        validationObj = new ValidationObj(subjectCheckResult, 1, 1, userVPStatus.getVpName()); // invalid
                        dbUtil.updateVPStatus(userId,VPStatusEnum.VP_VALID);
                    }
                    else {
                        validationObj = new ValidationObj(0, 0, 0, null); // invalid
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

