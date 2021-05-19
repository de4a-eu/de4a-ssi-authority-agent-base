package um.si.de4a.resources.offer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    @Consumes("application/json")
    @Produces("application/json")
    public boolean sendVCOffer(String offer) throws MalformedURLException {
        boolean resultStatus = false;
        JSONObject jsonOffer = null;

        DBUtil dbUtil = new DBUtil();

        JSONParser jsonParser = new JSONParser();
        try {
            jsonOffer = (JSONObject) jsonParser.parse(offer);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(jsonOffer != null) {
            System.out.println("user ID: " + jsonOffer.get("userId"));

            String userID = jsonOffer.get("userId").toString();
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
        }
        return resultStatus;
    }
}
