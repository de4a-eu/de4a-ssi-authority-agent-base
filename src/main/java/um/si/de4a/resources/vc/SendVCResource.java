package um.si.de4a.resources.vc;

import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;

import javax.ws.rs.*;
import java.net.MalformedURLException;

@Path("/send-vc")
public class SendVCResource {
    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("{userId}")
    public boolean sendVC(@PathParam("userId") String userID) throws MalformedURLException {
        boolean vcStatusResult = false;
        DBUtil dbUtil = new DBUtil();
        VCStatus vcStatus = null;

        // DONE: call database getVCStatus(userID): status, piid
        try{
            vcStatus = dbUtil.getVCStatus(userID);
        }
        catch(Exception ex){
            System.out.println("[SEND-VC] Exception: " + ex.getMessage());
        }

        if(vcStatus != null) {
            try{
                // TODO: call Aries /issuecredential/{PIID}/accept-request(VC):  null/empty

                // DONE: call database updateVCStatus(userId, status: vc_sent): boolean
                dbUtil.updateVCStatus(userID, VCStatusEnum.VC_SENT);
                vcStatusResult = true;
            }
            catch(Exception ex){
                System.out.println("[SEND-VC] Exception: " + ex.getMessage());
            }
        }
        return vcStatusResult;
    }
}
