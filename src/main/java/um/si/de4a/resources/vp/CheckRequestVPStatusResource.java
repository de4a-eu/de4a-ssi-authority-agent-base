package um.si.de4a.resources.vp;


import org.jboss.resteasy.annotations.Query;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.db.VPStatus;
import um.si.de4a.db.VPStatusEnum;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;

@Path("/check-request-vp-response")
public class CheckRequestVPStatusResource {

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{userId}")
    public int getStatus(@PathParam("userId") String userId) throws IOException {
        int vpRequestStatus = 0;
        DBUtil dbUtil = new DBUtil();
        // DONE: call database getVPStatus(userID): VPStatus object
        VPStatus vpStatus = dbUtil.getVPStatus(userId);

        // if (VPStatus == null): return -1 (request was never sent)
        if(vpStatus == null)
            vpRequestStatus = -1; // return -1 (request was never sent)
        else{
            System.out.println("[CHECK-REQUEST-VP-RESPONSE] VP PIID: " + vpStatus.getPiid());

            if (vpStatus.getVPStatusEnum() == VPStatusEnum.VP_RECEIVED)
                vpRequestStatus = 1; // return 1 (VP received)
            else if (vpStatus.getVPStatusEnum() == VPStatusEnum.VP_REJECTED)
                vpRequestStatus = -2; // return -2 (request rejected)
            else if (vpStatus.getVPStatusEnum() == VPStatusEnum.REQUEST_SENT){
                // DONE: call Aries /presentproof/actions: [action]
                AriesUtil ariesUtil = new AriesUtil();
                try {
                    JSONObject action = ariesUtil.getActionVP(vpStatus.getPiid());
                    if (action != null){
                        System.out.println("[CHECK REQUEST VP STATUS] Action: " + action.toJSONString());
                        JSONObject msg = (JSONObject) action.get("Msg");


                        if(msg.get("@type").equals("https://didcomm.org/present-proof/2.0/presentation") && msg.get("description") == null){
                            boolean acceptResult = ariesUtil.acceptPresentation(vpStatus.getPiid(), new NamesObj(new String[]{"vp-" + vpStatus.getUserId() + "-" + vpStatus.getPiid()}));
                            if(acceptResult == true){
                                boolean updateStatus = dbUtil.updateVPStatus(userId, "vp-" + vpStatus.getUserId() + "-" + vpStatus.getPiid(),VPStatusEnum.VP_RECEIVED);
                                if(updateStatus == true)
                                    vpRequestStatus = 1; // (vp received)
                            }
                        }
                        else {
                            JSONObject description = (JSONObject) msg.get("description");
                            if (description.get("code").equals("rejected")) {
                                dbUtil.updateVPStatus(userId, VPStatusEnum.VP_REJECTED);
                                vpRequestStatus = -2; // return -2 (vp rejected)
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return vpRequestStatus;
    }
}
