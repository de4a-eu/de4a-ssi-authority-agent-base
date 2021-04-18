package um.si.de4a.resources.vp;


import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VPStatus;

import javax.ws.rs.*;
import java.net.MalformedURLException;

@Path("/validate-vp")
public class ValidateVPResource {

    @GET
    @Consumes({"text/plain","application/json"})
    @Produces("text/plain")
    @Path("{userId}")
    public int validateVP(@PathParam("userId") String userId, @QueryParam("eidasMDS")String eidasMds) throws MalformedURLException {
        int validationResult = 0;
        DBUtil dbUtil = new DBUtil();
        // DONE: call database getVPStatus(userId): VPStatus object
        VPStatus userVPStatus = dbUtil.getVPStatus(userId);

        // TODO: call Aries /verifiable/presentations: [presentation]

        // findVPId(name): VP_ID
        // dbUtil.findVPById(userVPStatus.getVp());
        // TODO: call Aries /verifiable/presentations/{VP_ID}: VP object

        // TODO: checkVPSubjectEidasMDS(VP): int
                // if (subject not the same): return -1 (VP subject does not match eIDAS login)
                // else:
                    // call EBSI checkISSUER(): boolean
                    // if (issuer not TIR): return -2 (issuer not valid TIR)
                    // else: return 1 (vp valid)

        return validationResult;
    }
}
