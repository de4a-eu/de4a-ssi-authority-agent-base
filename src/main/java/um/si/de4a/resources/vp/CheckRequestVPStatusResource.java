package um.si.de4a.resources.vp;


import org.jboss.resteasy.annotations.Query;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VPStatus;
import um.si.de4a.db.VPStatusEnum;

import javax.ws.rs.*;
import java.net.MalformedURLException;

@Path("/check-request-vp-response")
public class CheckRequestVPStatusResource {

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("{userId}")
    public int getStatus(@PathParam("userId") String userId) throws MalformedURLException {
        int vpRequestStatus = 0;
        DBUtil dbUtil = new DBUtil();
        // DONE: call database getVPStatus(userID): VPStatus object
        VPStatus vpStatus = dbUtil.getVPStatus(userId);

        // if (VPStatus == null): return -1 (request was never sent)
        if(vpStatus == null)
            vpRequestStatus = -1; // return -1 (request was never sent)
        else{
            if (vpStatus.getVPStatusEnum() == VPStatusEnum.VP_RECEIVED)
                vpRequestStatus = 1; // return 1 (VP received)
            else if (vpStatus.getVPStatusEnum() == VPStatusEnum.VP_REJECTED)
                vpRequestStatus = -2; // return -2 (request rejected)
            else if (vpStatus.getVPStatusEnum() == VPStatusEnum.REQUEST_SENT){
                // TODO: call Aries /presentproof/actions: [action]

                // for (action in actions):
                    // if (PIID == null): return 0 (request was sent but not yet accepted)
                    // else if (PIID != null && status == "vp_rejected"):
                        // DONE: call database updateVPStatus(userId, status:vp_rejected): boolean
                        //dbUtil.updateVPStatus(userId, VPStatusEnum.VP_REJECTED);
                        //vpRequestStatus = -2; // return -2 (request rejected)
                    // else if (PIID != null && status == "vp_accepted":
                        // generateName(userId, PIID): name
                        // call Aries /presentproof/{PIID}/accept-presentation(name): boolean
                        // call database updateVPStatus(userId, name, status: vp_received): boolean
                        // dbUtil.updateVPStatus(userId, VPStatusEnum.VP_RECEIVED);
                        // vpRequestStatus = 1; // return 1 (vp received)
            }
        }

        return vpRequestStatus;
    }
}
