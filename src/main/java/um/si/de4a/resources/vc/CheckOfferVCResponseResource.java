package um.si.de4a.resources.vc;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;

@Path("/check-offer-vc-response")
public class CheckOfferVCResponseResource {
    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{userId}")
    public int checkResponse(@PathParam("userId") String userID) throws IOException, ParseException {
        int vcStatusCode = 0;
        DBUtil dbUtil = new DBUtil();

        // DONE: call database getVCStatus(userID): status, piid
        VCStatus vcStatus = dbUtil.getVCStatus(userID);
        if(vcStatus == null)
            vcStatusCode = -1; //  return -1 (offer was never sent)
        else {
            if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_ACCEPTED)
                vcStatusCode = 1; // return 1 (offer accepted)
            else  if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_ACCEPTED)
                vcStatusCode = 5; // return 5 (VC accepted)
            else if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_REJECTED)
                vcStatusCode = -2; // return -2 (offer rejected)
            else if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_REJECTED)
                vcStatusCode = -4; // return -4 (VC rejected)
            else if ((vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT) || (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT)){

                // DONE: call Aries /issuecredential/actions: [action]
                AriesUtil ariesUtil = new AriesUtil();
                JSONObject action = ariesUtil.getAction(vcStatus.getPiid());
                if(action != null) {
                    System.out.println("[CHECK OFFER VC STATUS] Action: " + action.toJSONString());
                    JSONObject msg = (JSONObject) action.get("Msg");

                    if(msg.get("@type").equals("https://didcomm.org/issue-credential/2.0/request-credential") && msg.get("description") == null){
                        dbUtil.updateVCStatus(userID, VCStatusEnum.OFFER_ACCEPTED);
                        vcStatusCode = 1; // (offer accepted)
                    }
                    else {
                        JSONObject description = (JSONObject) msg.get("description");
                        if(description.get("code").equals("rejected")){
                            if(action.get("MyDID") != null && action.get("TheirDID") != null) {
                                dbUtil.updateVCStatus(userID, VCStatusEnum.VC_REJECTED);
                                vcStatusCode = -4; // return -4 (VC rejected)
                            }
                            else{
                                dbUtil.updateVCStatus(userID, VCStatusEnum.OFFER_REJECTED);
                                vcStatusCode = -2; // return -1 (offer rejected)
                            }
                        }
                        else if(description.get("code").equals("internal") || description.get("code").equals("accepted")){
                            dbUtil.updateVCStatus(userID,VCStatusEnum.VC_ACCEPTED);
                            vcStatusCode = 5; // return 5 (VC accepted)
                        }
                    }
                }
                else{
                    if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT)
                        vcStatusCode = 0; // (offer sent and awaiting response)
                    else if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT)
                        vcStatusCode = 2; // (vc sent and awaiting response)
                }
            }
        }
        return vcStatusCode;
    }
}
