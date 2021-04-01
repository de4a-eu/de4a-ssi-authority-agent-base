package um.si.de4a.resources.vp;


import org.jboss.resteasy.annotations.Query;

import javax.ws.rs.*;

@Path("/check-vp-status")
public class CheckRequestVPStatusResource {

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    public int getStatus(@QueryParam("userId")String userId) {
        int vpRequestStatus = 0;
        // call database getVPStatus(userID): VPStatus object

        // if (VPStatus == null): return -1 (VP was never sent)
        // else:
            // switch:
                // case "status == vp_received": return 1 (VP received)
                // case "status == vp_rejected": return -2 (request rejected)
                // case "status == request_sent":
                    // call Aries /presentproof/actions: [action]

                    // for (action in actions):
                        // if (PIID == null): return 0 (request was sent but not yet accepted)
                        // else if (PIID != null && status == "vp_rejected"):
                            // call database updateVPStatus(userId, status:vp_rejected): boolean
                            // return -2 (request rejected)
                        // else if (PIID != null && status == "vp_accepted":
                            // generateName(userId, PIID): name
                            // call Aries /presentproof/{PIID}/accept-presentation(name): boolean
                            // call database updateVPStatus(userId, name, status: vp_received): boolean
                            // return 1 (vp received)

        return vpRequestStatus;
    }
}
