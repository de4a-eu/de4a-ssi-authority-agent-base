package um.si.de4a.resources.offer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.resources.vc.GenerateVCResource;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;

@Path("/send-vc-offer")
public class SendVCOfferResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean sendVCOffer(String offer) throws IOException {
        boolean resultStatus = false;
        JSONObject jsonOffer = null;

        DBUtil dbUtil = new DBUtil();
        AriesUtil ariesUtil = new AriesUtil();

        JSONParser jsonParser = new JSONParser();
        try {
            jsonOffer = (JSONObject) jsonParser.parse(offer);
            System.out.println("JSON offer: " + jsonOffer.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(jsonOffer != null) {
            System.out.println("user ID: " + jsonOffer.get("userId"));

            String userID = jsonOffer.get("userId").toString();
            // DONE: call database getDIDConnStatus(userID): DIDConn
            DIDConn userDIDConn = dbUtil.getDIDConnStatus(userID);
            if(userDIDConn != null){
                System.out.println("[SEND-VC-OFFER] MyDID: " + userDIDConn.getMyDID());

                JSONObject jo = new JSONObject();
                jo.put("evidence", jsonOffer.get("evidence").toString());
                jo.put("publicDID", "did:ebsi:1234"); //TODO: replace later with EBSI DID
                jo.put("myDID", userDIDConn.getMyDID());
                jo.put("theirDID", userDIDConn.getTheirDID());

                String jsonRequest = jo.toJSONString();
                System.out.println("[SEND-VC-OFFER] JSON request: " + jsonRequest);
                // DONE: call generateVC(evidence, myDID, theirDID) method: VC
                GenerateVCResource generateVCResource = new GenerateVCResource();
                boolean vcGenerated = generateVCResource.generateVC(jsonRequest);

                if(vcGenerated == true) {
                    // TODO: call Aries /verifiable/sign-credential(vc) : boolean

                    // if (true)
                    // TODO: call Aries /issuecredential/send-offer(myDID, theirDID, VC) : PIID

                    // DONE: call database saveVCStatus(userID, PIID, VC, status: offer_sent): boolean
                    try {
                        dbUtil.saveVCStatus(userID, "piid1", "vc1", VCStatusEnum.OFFER_SENT);
                    } catch (Exception ex) {
                        System.out.println("[SEND-VC-OFFER] Exception: " + ex.getMessage());
                    }
                    resultStatus = true;
                }
            }
        }
        return resultStatus;
    }
}
