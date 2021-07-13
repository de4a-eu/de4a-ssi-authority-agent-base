package um.si.de4a.resources.offer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.model.json.SignedVerifiableCredential;
import um.si.de4a.model.json.VerifiableCredential;
import um.si.de4a.resources.vc.GenerateVCResource;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.*;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;

@Path("/send-vc-offer")
public class SendVCOfferResource {

    private AppConfig appConfig = null;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean sendVCOffer(String inputOffer) throws IOException, ParseException, java.text.ParseException {
        appConfig = new AppConfig();
        String didKey = appConfig.getProperties().getProperty("did.key");
        String signatureType = appConfig.getProperties().getProperty("signature.type");

        boolean resultStatus = false;
        JSONObject jsonOffer = null;

        DBUtil dbUtil = new DBUtil();
        AriesUtil ariesUtil = new AriesUtil();

        JSONParser jsonParser = new JSONParser();
        jsonOffer = (JSONObject) jsonParser.parse(inputOffer);
        if(jsonOffer != null) {
            System.out.println("user ID: " + jsonOffer.get("userId"));

            String userID = jsonOffer.get("userId").toString();
            // DONE: call database getDIDConnStatus(userID): DIDConn
            DIDConn userDIDConn = dbUtil.getDIDConnStatus(userID);
            if(userDIDConn != null){
                JSONObject jo = new JSONObject();
                jo.put("evidence", jsonOffer.get("evidence").toString());
                jo.put("publicDID", dbUtil.getDID());
                jo.put("myDID", userDIDConn.getMyDID());
                jo.put("theirDID", userDIDConn.getTheirDID());

                String jsonRequest = jo.toJSONString();
                // System.out.println("[SEND-VC-OFFER] JSON request: " + jsonRequest);

                // DONE: call generateVC(evidence, myDID, theirDID) method: VC
                GenerateVCResource generateVCResource = new GenerateVCResource();
                VerifiableCredential generatedVC = generateVCResource.generateVC(jsonRequest);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                //System.out.println("[GENERATE VC]: " + gson.toJson(generatedVC));
                if(generatedVC != null) {
                    // DONE: call Aries /verifiable/sign-credential(vc) : boolean
                    Clock clock = Clock.systemDefaultZone();

                    Instant now = clock.instant();

                    SignRequest jsonSignRequest = new SignRequest(now.toString(), generatedVC,
                            dbUtil.getDID(), signatureType);

                    // DONE: call Aries /issuecredential/send-offer(myDID, theirDID, VC) : PIID
                    SignedVerifiableCredential credential = ariesUtil.signCredential(jsonSignRequest);

                    if(credential != null){

                        String prettyJson = gson.toJson(credential);
                        System.out.println("[SEND-VC-OFFER] Signed credential: " + prettyJson);

                        OfferRequest offer = new OfferRequest(userDIDConn.getMyDID(),userDIDConn.getTheirDID(), credential);
                        String piid = ariesUtil.sendOffer(offer);

                        // DONE: call database saveVCStatus(userID, PIID, VC, status: offer_sent): boolean
                        if(piid != "") {
                            try {
                                dbUtil.saveVCStatus(userID, piid, gson.toJson(credential), VCStatusEnum.OFFER_SENT);

                            } catch (Exception ex) {
                                System.out.println("[SEND-VC-OFFER] Exception: " + ex.getMessage());
                            }
                            resultStatus = true;
                        }

                    }

                }
            }
        }
        return resultStatus;
    }
}
