package um.si.de4a.resources.vc;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.model.json.SignedVerifiableCredential;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;

@Path("/send-vc")
public class SendVCResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean sendVC(String user) throws IOException {
        boolean vcStatusResult = false, vcAcceptStatus = false;
        JSONObject jsonRequest = null;

        DBUtil dbUtil = new DBUtil();
        VCStatus vcStatus = null;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonRequest = (JSONObject) jsonParser.parse(user);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(jsonRequest != null) {
            System.out.println("[SEND-VC] User ID: " + jsonRequest.get("userId"));

            String userID = jsonRequest.get("userId").toString();

            // DONE: call database getVCStatus(userID): status, piid
            try {
                vcStatus = dbUtil.getVCStatus(userID);
            } catch (Exception ex) {
                System.out.println("[SEND-VC] Exception: " + ex.getMessage());
            }

            AriesUtil ariesUtil = new AriesUtil();
            Gson gson = new Gson();
            if (vcStatus != null) {
                try {
                    SendVCRequest request = new SendVCRequest(gson.fromJson(vcStatus.getVc(), SignedVerifiableCredential.class));
                    System.out.println("[SEND VC] Signed VC ID: " + request.getCredential().getId());
                    // DONE: call Aries /issuecredential/{PIID}/accept-request(VC):  null/empty
                    vcAcceptStatus = ariesUtil.acceptRequest(vcStatus.getPiid(),request);
                    // DONE: call database updateVCStatus(userId, status: vc_sent): boolean
                    if (vcAcceptStatus == true) {
                        dbUtil.updateVCStatus(userID, VCStatusEnum.VC_SENT);
                        vcStatusResult = true;
                    }

                } catch (Exception ex) {
                    System.out.println("[SEND-VC] Exception: " + ex.getMessage());
                }
            }
        }
        return vcStatusResult;
    }
}
