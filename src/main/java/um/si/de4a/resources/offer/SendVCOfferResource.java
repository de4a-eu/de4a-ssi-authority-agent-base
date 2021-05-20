package um.si.de4a.resources.offer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.model.json.VerifiableCredential;
import um.si.de4a.resources.vc.GenerateVCResource;

import javax.ws.rs.*;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;

@Path("/send-vc-offer")
public class SendVCOfferResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean sendVCOffer(String offer) throws IOException, ParseException, java.text.ParseException {
        boolean resultStatus = false;
        JSONObject jsonOffer = null;

        DBUtil dbUtil = new DBUtil();
        AriesUtil ariesUtil = new AriesUtil();

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
                System.out.println("[SEND-VC-OFFER] MyDID: " + userDIDConn.getMyDID());

                JSONObject jo = new JSONObject();
                jo.put("evidence", jsonOffer.get("evidence").toString());
                jo.put("publicDID", "did:ebsi:1234"); //TODO: replace later with EBSI DID
                jo.put("myDID", userDIDConn.getMyDID());
                jo.put("theirDID", userDIDConn.getTheirDID());

                String jsonRequest = jo.toJSONString();
                // System.out.println("[SEND-VC-OFFER] JSON request: " + jsonRequest);

                // DONE: call generateVC(evidence, myDID, theirDID) method: VC
                GenerateVCResource generateVCResource = new GenerateVCResource();
                VerifiableCredential generatedVC = generateVCResource.generateVC(jsonRequest);
                //System.out.println(gson.toJson(generatedVC));
                if(generatedVC != null) {
                    // TODO: call Aries /verifiable/sign-credential(vc) : boolean
                    Clock clock = Clock.systemDefaultZone();

                    Instant now = clock.instant();

                    OfferRequest jsonSignRequest = new OfferRequest(now.toString(), generatedVC,
                            "did:key:z6Mkf3mSeHmehoXVdXQt1uNimKxD9RqFXHS6EgbVaDx4B5Z5",
                            "Ed25519Signature2018");

                    // if (true)
                    // TODO: call Aries /issuecredential/send-offer(myDID, theirDID, VC) : PIID
                    try {
                        JSONObject credential = ariesUtil.signCredential(jsonSignRequest);

                        if(credential != null){
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String prettyJson = gson.toJson(credential);
                            System.out.println("[SEND-VC-OFFER] Result: " + prettyJson);

                            resultStatus = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // DONE: call database saveVCStatus(userID, PIID, VC, status: offer_sent): boolean
                    /*try {
                        dbUtil.saveVCStatus(userID, "piid1", "vc1", VCStatusEnum.OFFER_SENT);

                    } catch (Exception ex) {
                        System.out.println("[SEND-VC-OFFER] Exception: " + ex.getMessage());
                    }*/

                }
            }
        }
        return resultStatus;
    }
}
