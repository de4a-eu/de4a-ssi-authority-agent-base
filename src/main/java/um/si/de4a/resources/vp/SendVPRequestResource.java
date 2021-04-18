package um.si.de4a.resources.vp;

import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.VPStatusEnum;

import javax.ws.rs.*;
import java.net.MalformedURLException;

@Path("/send-vp-request")
public class SendVPRequestResource {
    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
     public boolean sendVPRequest(@PathParam("userId") String userID, @QueryParam("presentationFormat")String format) throws MalformedURLException {
        boolean vpRequestStatus = false;

        DBUtil dbUtil = new DBUtil();
        // DONE: call database getDIDConnStatus(userID): DIDConn object
        DIDConn userDIDConn = dbUtil.getDIDConnStatus(userID);

        // TODO: generate VPRequest (format, myDID, theirDID): VPRequest object

        // TODO: call Aries /presentproof/send-request-presentation(VPRequest):  PIID

        // DONE: call database saveVPStatus(userId, PIID, status: request_sent): boolean
        boolean response = dbUtil.saveVPStatus(userID,"piid1", "vp1", "name1", VPStatusEnum.REQUEST_SENT);
        if(response == true)
            vpRequestStatus = true;
        return vpRequestStatus;
    }
}
