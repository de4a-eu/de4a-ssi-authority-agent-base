package um.si.de4a.resources.vc;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public class SendVCResource {
    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public boolean sendVC(@QueryParam("userId")String userID) {
        boolean vcStatusResult = false;

        // call database getVCStatus(userID): status, piid

        // call Aries /issuecredential/{PIID}/accept-request(VC):  null/empty

        // call database updateVCStatus(userId, status: vc_sent): boolean

        return vcStatusResult;
    }
}
