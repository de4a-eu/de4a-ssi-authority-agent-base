package um.si.de4a.resources.vc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;

import javax.ws.rs.*;
import java.net.MalformedURLException;

@Path("/send-vc")
public class SendVCResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean sendVC(String user) throws MalformedURLException {
        boolean vcStatusResult = false;
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
            System.out.println("user ID: " + jsonRequest.get("userId"));

            String userID = jsonRequest.get("userId").toString();

            // DONE: call database getVCStatus(userID): status, piid
            try {
                vcStatus = dbUtil.getVCStatus(userID);
            } catch (Exception ex) {
                System.out.println("[SEND-VC] Exception: " + ex.getMessage());
            }

            if (vcStatus != null) {
                try {
                    // TODO: call Aries /issuecredential/{PIID}/accept-request(VC):  null/empty

                    // DONE: call database updateVCStatus(userId, status: vc_sent): boolean
                    dbUtil.updateVCStatus(userID, VCStatusEnum.VC_SENT);
                    vcStatusResult = true;
                } catch (Exception ex) {
                    System.out.println("[SEND-VC] Exception: " + ex.getMessage());
                }
            }
        }
        return vcStatusResult;
    }
}
