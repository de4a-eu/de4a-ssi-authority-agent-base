package um.si.de4a.resources.vp;


import javax.ws.rs.*;

@Path("/validate-vp")
public class ValidateVPResource {

    @GET
    @Consumes({"text/plain","application/json"})
    @Produces("text/plain")
    public int validateVP(@QueryParam("userId")String userId, @QueryParam("eidasMDS")Object eidasMds) {
        int validationResult = 0;

        // call database getVPStatus(userId): VPStatus object

        // call Aries /verifiable/presentations: [presentation]

        // findVPId(name): VP_ID

        // call Aries /verifiable/presentations/{VP_ID}: VP object

        // checkVPSubjectEidasMDS(VP): int
                // if (subject not the same): return -1 (VP subject does not match eIDAS login)
                // else:
                    // call EBSI checkISSUER(): boolean
                    // if (issuer not TIR): return -2 (issuer not valid TIR)
                    // else: return 1 (vp valid)

        return validationResult;
    }
}
