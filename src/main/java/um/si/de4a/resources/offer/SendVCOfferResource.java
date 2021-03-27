package um.si.de4a.resources.offer;

import com.fasterxml.jackson.core.JsonProcessingException;
import um.si.de4a.model.xml.Credential;
import um.si.de4a.util.XMLtoJSONConverter;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class SendVCOfferResource {
    @POST
    @Consumes({"text/plain", "application/xml"})
    @Produces("text/plain")
    public boolean sendVCOffer(@QueryParam("userId")String userID, @QueryParam("evidence") String evidence) {
        boolean result = false;

        // call database getDIDConnStatus(userID): row

        // call generateVC(evidence, myDID, theirDID) method: VC

        // call Aries /verifiable/sign-credential(vc) : boolean

        // if (true)
            // call Aries /issuecredential/send-offer(myDID, theirDID, VC) : PIID
            // call database saveVCStatus(userID, PIID, VC, status: offer_sent): boolean
            // return boolean

        return result;
    }
}
