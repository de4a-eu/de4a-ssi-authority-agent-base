package um.si.de4a.resources.vc;

import javax.ws.rs.*;

public class CheckOfferVCResponseResource {
    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    public int sendVC(@QueryParam("userId")String userID) {
        int vcStatusCode = 0;

        // call database getVCStatus(userID): status, piid

        // switch:
            // case "status == vc_accepted": return 3 (VC accepted)
            // case "status == vc_rejected": return -3 (VC rejected)
            // case "status == vc_sent":
                    // call Aries /issuecredential/actions: [action]
                    // switch:
                        // case PIID == null: return -1 (Offer has not been sent to the student)
                        // case PIID != null:
                            // if status == offer_rejected: return -2 (offer rejected)
                            // else if status == offer_sent: return 0 (offer sent, but not accepted)
                            // else if status == offer_accepted: return 1 (offer accepted, but VC not sent)
                    // return 2 (VC sent, but not accepted)

        return vcStatusCode;
    }
}
