package um.si.de4a.resources.offer;

import com.fasterxml.jackson.core.JsonProcessingException;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.model.xml.Credential;
import um.si.de4a.util.XMLtoJSONConverter;

import javax.ws.rs.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.net.MalformedURLException;

@Path("/send-vc-offer")
public class SendVCOfferResource {
    @POST
    @Consumes({"text/plain", "application/xml"})
    @Produces("text/plain")
    @Path("{userId}/{evidence}")
    public boolean sendVCOffer(@PathParam("userId") String userID, @PathParam("evidence") String evidence) throws MalformedURLException {
        boolean resultStatus = false;

        DBUtil dbUtil = new DBUtil();

        // DONE: call database getDIDConnStatus(userID): DIDConn
        DIDConn userDIDConn = dbUtil.getDIDConnStatus(userID);
        if(userDIDConn != null){
            System.out.println("[SEND-VC-OFFER] Status: " + userDIDConn.getMyDID());

            // TODO call generateVC(evidence, myDID, theirDID) method: VC

            // TODO: call Aries /verifiable/sign-credential(vc) : boolean

            // if (true)
            // TODO: call Aries /issuecredential/send-offer(myDID, theirDID, VC) : PIID

            // DONE: call database saveVCStatus(userID, PIID, VC, status: offer_sent): boolean
            try{
                dbUtil.saveVCStatus(userID, "piid1", "vc1", VCStatusEnum.OFFER_SENT);
            }
            catch(Exception ex){
                System.out.println("[SEND-VC-OFFER] Exception: " + ex.getMessage());
            }
            // return boolean
            resultStatus = true;
        }

        return resultStatus;
    }
}
