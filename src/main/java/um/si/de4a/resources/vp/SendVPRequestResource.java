package um.si.de4a.resources.vp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.VPStatusEnum;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;

@Path("/send-vp-request")
public class SendVPRequestResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
     public boolean sendVPRequest(String vpRequest) throws IOException, ParseException {
        boolean vpRequestStatus = false;
        JSONObject jsonRequest = null;
        DBUtil dbUtil = new DBUtil();

        JSONParser jsonParser = new JSONParser();
        try {
            jsonRequest = (JSONObject) jsonParser.parse(vpRequest);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(jsonRequest != null) {
            System.out.println("[SEND VP REQUEST] User ID: " + jsonRequest.get("userId"));

            String userID = jsonRequest.get("userId").toString();

            // DONE: call database getDIDConnStatus(userID): DIDConn object
            DIDConn userDIDConn = dbUtil.getDIDConnStatus(userID);

            if(userDIDConn != null) { // if invitation is generated
                if (!userDIDConn.getConnectionId().equals("")) { // if DIDConn is established
                    // DONE: generate VPRequest (format, myDID, theirDID): VPRequest object
                    VPRequest vpRequestObj = new VPRequest(userDIDConn.getMyDID(), new RequestPresentationObj(), userDIDConn.getTheirDID());

                    // DONE: call Aries /presentproof/send-request-presentation(VPRequest):  PIID
                    AriesUtil ariesUtil = new AriesUtil();
                    String piid = ariesUtil.sendRequest(vpRequestObj);
                    if (piid != "") {
                        System.out.println("[SEND VP REQUEST] Received PIID: " + piid);

                        // DONE: call database saveVPStatus(userId, PIID, status: request_sent): boolean
                        boolean response = dbUtil.saveVPStatus(userID, piid, "vp-" + userID + "-" + piid, VPStatusEnum.REQUEST_SENT);
                        if (response == true)
                            vpRequestStatus = true;
                    }
                }
            }
        }
        return vpRequestStatus;
    }
}
