package um.si.de4a.resources.vc;

import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;

import javax.ws.rs.*;
import java.net.MalformedURLException;

@Path("/check-offer-vc-response")
public class CheckOfferVCResponseResource {
    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{userId}")
    public int checkResponse(@PathParam("userId") String userID) throws MalformedURLException {
        int vcStatusCode = 0;
        DBUtil dbUtil = new DBUtil();

        // DONE: call database getVCStatus(userID): status, piid
        VCStatus vcStatus = dbUtil.getVCStatus(userID);
        if(vcStatus == null)
            vcStatusCode = -1; //  return -1 (offer was never sent)
        else {
            if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_ACCEPTED)
                vcStatusCode = 1; // return 1 (offer accepted)
            else  if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_ACCEPTED)
                vcStatusCode = 5; // return 5 (VC accepted)
            else if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_REJECTED)
                vcStatusCode = -2; // return -2 (offer rejected)
            else if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_REJECTED)
                vcStatusCode = -4; // return -4 (VC rejected)
            else if ((vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT) || (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT)){
                // TODO: call Aries /issuecredential/actions: [action]

                if(vcStatus.getPiid() == null){
                    if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT)
                        vcStatusCode = 0; // return 0 (offer sent)
                    else if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT)
                        vcStatusCode = 2; // return 2 (VC sent)
                }
                else {
                    if ((vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_REJECTED) || (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_REJECTED))
                    {
                        try {
                            dbUtil.updateVCStatus(userID, vcStatus.getVCStatusEnum());
                        }
                        catch(Exception ex) {
                            System.out.println("[CHECK-OFFER-VC-RESPONSE] Exception: " + ex.getMessage());
                        }
                        if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_REJECTED)
                            vcStatusCode = -2; // return -1 (offer rejected)
                        else if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_REJECTED)
                            vcStatusCode = -4; // return -4 (VC rejected)
                    }
                    else if ((vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_ACCEPTED) || (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_ACCEPTED))
                    {
                        try {
                            dbUtil.updateVCStatus(userID, vcStatus.getVCStatusEnum()); // TODO: why update if it's already written in the DB?
                        }
                        catch(Exception ex) {
                            System.out.println("[CHECK-OFFER-VC-RESPONSE] Exception: " + ex.getMessage());
                        }
                        if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_ACCEPTED)
                            vcStatusCode = 1; // return 1 (offer accepted)
                        else if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_ACCEPTED)
                            vcStatusCode = 5; // return 5 (VC accepted)
                    }
                }
            }
        }
        return vcStatusCode;
    }
}
