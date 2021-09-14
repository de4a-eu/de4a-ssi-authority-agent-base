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
            logRecordSevere.setMessage( "Error reading configuration properties.");
            Object[] params = new Object[]{"Authority Agent DT", "Authority Agent DT", "3001"};
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
            logRecordInfo.setMessage("Received input evidence data.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01005"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Error parsing input parameters.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1005"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        if(jsonOffer != null) {

            String userID = "", evidence = "";
            try{
                userID = jsonOffer.get("userId").toString();
                evidence = jsonOffer.get("evidence").toString();
            }
            catch (Exception ex){
                logRecordSevere.setMessage("Error parsing input parameters.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1005"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            // DONE: call database getDIDConnStatus(userID): DIDConn
            DIDConn userDIDConn = dbUtil.getDIDConnStatus(userID);
            String myDID = "", publicDID = "", theirDID = "";
            if(userDIDConn != null){
                try{
                    myDID = userDIDConn.getMyDID();
                    publicDID = dbUtil.getDID();
                    theirDID = userDIDConn.getTheirDID();
                }
                catch(Exception ex){
                    logRecordSevere.setMessage("Error accessing data on Authority Agent DT.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
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
                            logRecordSevere.setMessage("Object conversion error on Authority Agent DT.");
                            Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "1008"};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }

                        ArrayList<Attribute> attributes = new ArrayList<Attribute>() {
                            {
                                add(new Attribute("text/plain", "credentialSubject.currentFamilyName", Base64.getEncoder().encodeToString(credential.getCredentialSubject().getCurrentFamilyName().getBytes(StandardCharsets.UTF_8))));
                                add(new Attribute("text/plain", "credentialSubject.currentGivenName", Base64.getEncoder().encodeToString(credential.getCredentialSubject().getCurrentGivenName().getBytes(StandardCharsets.UTF_8))));
                                add(new Attribute("text/plain", "credentialSubject.agentReferences.organisation.preferredName.text.#text", Base64.getEncoder().encodeToString(credential.getCredentialSubject().getAgentReferences().getOrganisation().getPreferredName().getText().getText().getBytes(StandardCharsets.UTF_8))));
                                add(new Attribute("text/plain", "credentialSubject.learningAchievement.title.text.#text", Base64.getEncoder().encodeToString(credential.getCredentialSubject().getLearningAchievement().getTitle().getText().getText().getBytes(StandardCharsets.UTF_8))));
                                add(new Attribute("text/plain", "credentialSubject.learningSpecificationReferences.qualification.title.text.#text", Base64.getEncoder().encodeToString(credential.getCredentialSubject().getLearningSpecificationReferences().getQualification().getTitle().getText().getText().getBytes(StandardCharsets.UTF_8))));
                                add(new Attribute("text/plain", "issuanceDate", Base64.getEncoder().encodeToString(credential.getIssuanceDate().getBytes(StandardCharsets.UTF_8))));

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
                            logRecordSevere.setMessage("Object conversion error on Authority Agent DT.");
                            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1008"};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }

                        OfferCredential offerCredential = new OfferCredential(credentialPreview,offersAttaches);
                        OfferRequest offer = new OfferRequest(userDIDConn.getMyDID(),offerCredential,userDIDConn.getTheirDID());

                        String piid = "";
                        try {
                            piid = ariesUtil.sendOffer(offer);
                        }
                        catch(Exception ex){
                            logRecordSevere.setMessage("Error on response from the Aries Government Agent");
                            Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "1002"};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                        // DONE: call database saveVCStatus(userID, PIID, VC, status: offer_sent): boolean
                        if(piid != "") {
                            try {
                                dbUtil.saveVCStatus(userID, piid, gson.toJson(credential), VCStatusEnum.OFFER_SENT);

                                logRecordInfo.setMessage("Stored current state in the Authority Agent DT database.");
                                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01006"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                            } catch (Exception ex) {
                                logRecordSevere.setMessage("Error saving data on Authority Agent DT.");
                                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1001"};
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

