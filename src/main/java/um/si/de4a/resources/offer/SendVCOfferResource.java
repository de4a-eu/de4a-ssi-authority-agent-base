package um.si.de4a.resources.offer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.SerializationUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.model.json.SignedVerifiableCredential;
import um.si.de4a.model.json.VerifiableCredential;
import um.si.de4a.resources.vc.Data;
import um.si.de4a.resources.vc.GenerateVCResource;
import org.json.simple.parser.JSONParser;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/send-vc-offer")
public class SendVCOfferResource {

    private AppConfig appConfig = null;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean sendVCOffer(String inputOffer) throws IOException, ParseException, java.text.ParseException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        String signatureType = "";
        appConfig = new AppConfig();
        try {
            signatureType = appConfig.getProperties().getProperty("signature.type");
        }
        catch (Exception ex){
            logRecordSevere.setMessage( "Configuration error occurred on Authority Agent DT.");
            Object[] params = new Object[]{"Authority Agent DT", "Authority Agent DT", "30017"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
        boolean resultStatus = false;
        JSONObject jsonOffer = null;

        DBUtil dbUtil = new DBUtil();
        AriesUtil ariesUtil = new AriesUtil();

        JSONParser jsonParser = new JSONParser();
        try {
            jsonOffer = (JSONObject) jsonParser.parse(inputOffer);
            logRecordInfo.setMessage("SEND-OFFER: Received input eIDAS user data.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0201"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("SEND-OFFER: Object conversion error on Authority Agent DT.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        if(jsonOffer != null) {

            String userID = "", evidence = "";
            try{
                userID = jsonOffer.get("userId").toString();
                System.out.println("SEND-OFFER userId: " + userID);
                evidence = jsonOffer.get("evidence").toString();
            }
            catch (Exception ex){
                logRecordSevere.setMessage("SEND-OFFER: Arguments missing or invalid at Authority Agent DT.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "10702"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            // DONE: call database getDIDConnStatus(userID): DIDConn
            DIDConn userDIDConn = dbUtil.getCurrentDIDConnStatus(userID);

            String myDID = "", publicDID = "", theirDID = "";
            if(userDIDConn != null){
                try{
                    System.out.println("SEND-OFFER Current Invitation ID: " + userDIDConn.getInvitationId());

                    myDID = userDIDConn.getMyDID();
                    logRecordInfo.setMessage("SEND-OFFER myDID: " + myDID);
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0201"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);

                    publicDID = dbUtil.getDID();
                    logRecordInfo.setMessage("SEND-OFFER publicDID: " + publicDID);
                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0201"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);

                    theirDID = userDIDConn.getTheirDID();
                    logRecordInfo.setMessage("SEND-OFFER theirDID: " + theirDID);
                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0201"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage( "SEND-OFFER: Error accessing data on Authority Agent DT.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal DO", "20006"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
                JSONObject jo = new JSONObject();
                jo.put("evidence", evidence);
                jo.put("publicDID", publicDID);
                jo.put("myDID", myDID);
                jo.put("theirDID", theirDID);

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


                    if(credential != null) {

                        //String prettyJson = gson.toJson(credential);
                        //System.out.println("[SEND-VC-OFFER] Signed credential: " + prettyJson);

                        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);

                        String outputLastModTime = "";
                        try {
                            Calendar cal = Calendar.getInstance();
                            outputLastModTime = outputFormat.format(cal.getTime());
                        } catch (Exception ex) {
                            logRecordSevere.setMessage("SEND-OFFER: Object conversion error on Authority Agent DT.");
                            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }

                        ArrayList<Attribute> attributes = new ArrayList<Attribute>() {
                            {
                                add(new Attribute("text/plain", "credentialSubject.currentFamilyName", "Current Family Name"));
                                add(new Attribute("text/plain", "credentialSubject.currentGivenName", "Current Given Name"));
                                add(new Attribute("text/plain", "credentialSubject.agentReferences.organisation.preferredName.text.#text", "Institution Name"));
                                add(new Attribute("text/plain", "credentialSubject.learningAchievement.title.text.#text", "Title"));
                                add(new Attribute("text/plain", "credentialSubject.learningSpecificationReferences.qualification.title.text.#text", "Degree"));
                                add(new Attribute("text/plain", "issuanceDate", "Date of Issuance"));

                            }
                        };
                        CredentialPreview credentialPreview = new CredentialPreview(attributes);

                        OffersAttach offersAttach = null;
                        ArrayList<OffersAttach> offersAttaches = new ArrayList<>();
                        try {
                            offersAttach = new OffersAttach(new Data(Base64.getEncoder().encodeToString(gson.toJson(credential).getBytes(StandardCharsets.UTF_8))), outputLastModTime);
                            offersAttaches.add(offersAttach);
                        }
                        catch(Exception ex){
                            logRecordSevere.setMessage("SEND-OFFER: Object conversion error on Authority Agent DT.");
                            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }

                        OfferCredential offerCredential = new OfferCredential("Please respond to the offered credential representing your diploma.",credentialPreview,offersAttaches);
                        OfferRequest offer = new OfferRequest(userDIDConn.getMyDID(),offerCredential,userDIDConn.getTheirDID());

                        String piid = "";
                        try {
                            piid = ariesUtil.sendOffer(offer);
                        }
                        catch(Exception ex){
                            logRecordSevere.setMessage( "SEND-OFFER: Error on response from the Aries Government Agent.");
                            Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10704"};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                        // DONE: call database saveVCStatus(userID, PIID, VC, status: offer_sent): boolean
                        if(piid != "") {
                            try {
                                dbUtil.saveVCStatus(userID, piid, gson.toJson(credential), VCStatusEnum.OFFER_SENT);

                                logRecordInfo.setMessage("SEND-OFFER: Stored current state in the Authority Agent DT internal database.");
                                Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal", "0103"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                            } catch (Exception ex) {
                                logRecordSevere.setMessage( "SEND-OFFER: Error saving data on Authority Agent DT.");
                                Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal DO", "20006"};
                                logRecordSevere.setParameters(params);
                                logger.log(logRecordSevere);
                                //System.out.println("[SEND-VC-OFFER] Exception: " + ex.getMessage());
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

