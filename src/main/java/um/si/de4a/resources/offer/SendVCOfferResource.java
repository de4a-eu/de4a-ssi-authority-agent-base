/*
 * Copyright (C) 2023, Partners of the EU funded DE4A project consortium
 *   (https://www.de4a.eu/consortium), under Grant Agreement No.870635
 * Author: University of Maribor (UM)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import um.si.de4a.resources.vc.Data;
import um.si.de4a.resources.vc.GenerateVCResource;
import org.json.simple.parser.JSONParser;
import um.si.de4a.util.DE4ALogger;
import um.si.de4a.util.XMLtoJSONAdapter;

import javax.ws.rs.*;
import java.io.*;
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
        String alias = "";
        String doURL = "";
        appConfig = new AppConfig();
        try {
            signatureType = appConfig.getProperties().getProperty("signature.type");
            alias = appConfig.getProperties().getProperty("alias");
            doURL = appConfig.getProperties().getProperty("do.url");
        }
        catch (Exception ex){
            logRecordSevere.setMessage( "Configuration error occurred on Authority Agent.");
            Object[] params = new Object[]{"AAE09", alias};
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
            //jsonOffer = (JSONObject) jsonParser.parse(new InputStreamReader(new ByteArrayInputStream(inputOffer.getBytes(StandardCharsets.UTF_8)), "UTF-8"));
            logRecordInfo.setMessage("SEND-OFFER: Received input eIDAS user data.");
            Object[] params = new Object[]{"AAI01", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Object conversion error on Authority Agent: [SEND-OFFER] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE03", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        if(jsonOffer != null) {

            String userID = "", evidence = "";
            try{
                userID = jsonOffer.get("userId").toString();
            }
            catch (Exception ex){
                logRecordSevere.setMessage("Missing or invalid arguments at Authority Agent /send-offer: userId");
                Object[] params = new Object[]{"AAE05", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            try{

                byte[] evidenceBytes = Base64.getDecoder().decode(jsonOffer.get("evidence").toString());

                evidence = new String(evidenceBytes, "ISO-8859-1");
            }
            catch (Exception ex){
                logRecordSevere.setMessage("Missing or invalid arguments at Authority Agent /send-offer: evidence");
                Object[] params = new Object[]{"AAE05", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            // DONE: call database getDIDConnStatus(userID): DIDConn
            DIDConn userDIDConn = dbUtil.getCurrentDIDConnStatus(userID);

            String myDID = "", publicDID = "", theirDID = "";
            if(userDIDConn != null){
                try{
                    System.out.println("SEND-OFFER: Current Invitation ID: " + userDIDConn.getInvitationId());

                    myDID = userDIDConn.getMyDID();
                    System.out.println("SEND-OFFER: myDID: " + myDID);

                    publicDID = dbUtil.getDID();
                    System.out.println("SEND-OFFER: publicDID: " + publicDID);

                    theirDID = userDIDConn.getTheirDID();
                    System.out.println("SEND-OFFER: theirDID: " + theirDID);

                    logRecordInfo.setMessage("SEND-OFFER: Received user DIDConn status data.");
                    Object[] params = new Object[]{"AAI14", alias};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage( "Error accessing data on Authority Agent internal database: [SEND-OFFER] " + ex.getMessage() + ".");
                    Object[] params = new Object[]{"AAE04", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
                JSONObject jo = new JSONObject();
                jo.put("evidence", evidence);
                jo.put("publicDID", publicDID);
                jo.put("myDID", myDID);
                jo.put("theirDID", theirDID);

                String jsonRequest = jo.toJSONString();
                //System.out.println("[SEND-OFFER-DEBUG] JSON request: " + jsonRequest);

                // DONE: call generateVC(evidence, myDID, theirDID) method: VC
                GenerateVCResource generateVCResource = new GenerateVCResource();

                VerifiableCredential generatedVC = generateVCResource.generateVC(jsonRequest, alias);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

               // System.out.println("[GENERATE VC]: " + gson.toJson(generatedVC));
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

                        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

                        String outputLastModTime = "";
                        try {
                            Calendar cal = Calendar.getInstance();
                            outputLastModTime = outputFormat.format(cal.getTime());
                        } catch (Exception ex) {
                            logRecordSevere.setMessage("Object conversion error on Authority Agent: [SEND-OFFER] " + ex.getMessage() + ".");
                            Object[] params = new Object[]{"AAE03", alias};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }

                        ArrayList<Attribute> attributes = new ArrayList<Attribute>() {
                            {
                                add(new Attribute("text/plain", "credentialSubject.currentFamilyName", "Current Family Name"));
                                add(new Attribute("text/plain", "credentialSubject.currentGivenName", "Current Given Name"));
                                add(new Attribute("text/plain", "credentialSubject.achieved[0].wasAwardedBy.awardingBody[0]", "Institution Name"));
                                add(new Attribute("text/plain", "credentialSubject.achieved[0].title", "Title"));
                                add(new Attribute("text/plain", "credentialSubject.achieved[0].title", "Degree"));
                                add(new Attribute("text/plain", "credentialSubject.achieved[0].wasAwardedBy.awardingDate", "Date of Issuance"));

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
                            logRecordSevere.setMessage("Object conversion error on Authority Agent: [SEND-OFFER] " + ex.getMessage() + ".");
                            Object[] params = new Object[]{"AAE03", alias};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }

                        OfferCredential offerCredential = new OfferCredential("Please respond to the offered credential representing your diploma.",credentialPreview,offersAttaches);
                        OfferRequest offer = new OfferRequest(userDIDConn.getMyDID(),offerCredential,userDIDConn.getTheirDID());

                        String piid = "";
                        try {
                            piid = ariesUtil.sendOffer(offer);

                            logRecordInfo.setMessage("SEND-OFFER: Sent Offer for Verifiable Credential " + credential.getId() + " of type " + XMLtoJSONAdapter.namespace
                                            + " under invitation " + userDIDConn.getInvitationId() + " from " + doURL + ".");
                            Object[] params = new Object[]{"AAI22", alias};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);
                        }
                        catch(Exception ex){
                            logRecordSevere.setMessage( "Error on response from Aries Government Agent: [SEND-OFFER] " + ex.getMessage() + ".");
                            Object[] params = new Object[]{"AAE02", alias};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                        // DONE: call database saveVCStatus(userID, PIID, VC, status: offer_sent): boolean
                        if(piid != "") {
                            try {
                                dbUtil.saveVCStatus(userID, piid, gson.toJson(credential), VCStatusEnum.OFFER_SENT);

                                logRecordInfo.setMessage("SEND-OFFER: Stored current state in Authority Agent internal database.");
                                Object[] params = new Object[]{"AAI13", alias};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                            } catch (Exception ex) {
                                logRecordSevere.setMessage( "Error saving data on Authority Agent internal database: [SEND-OFFER] " + ex.getMessage() + ".");
                                Object[] params = new Object[]{"AAE04", alias};
                                logRecordSevere.setParameters(params);
                                logger.log(logRecordSevere);
                                //System.out.println("[SEND-VC-OFFER] Exception: " + ex.getMessage() + ".");
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

