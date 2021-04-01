package um.si.de4a.resources.vp;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public class SendVPRequestResource {
    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public boolean sendVPRequest(@QueryParam("userId")String userID, @QueryParam("presentationFormat")String format) {
        boolean vpRequestStatus = false;

        // call database getDIDConnStatus(userID): DIDConn object

        // generate VPRequest (format, myDID, theirDID): VPRequest object

        // call Aries /presentproof/send-request-presentation(VPRequest):  PIID

        // call database saveVPStatus(userId, PIID, status: request_sent): boolean

        return vpRequestStatus;
    }
}
