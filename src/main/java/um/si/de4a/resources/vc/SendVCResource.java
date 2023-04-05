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
package um.si.de4a.resources.vc;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.model.json.SignedVerifiableCredential;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/send-vc")
public class SendVCResource {
    private AppConfig appConfig = null;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean sendVC(String user) throws IOException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        String alias = "";
        String doURL = "";
        appConfig = new AppConfig();
        try {
            alias = appConfig.getProperties().getProperty("alias");
            doURL = appConfig.getProperties().getProperty("do.url");
        }
        catch (Exception ex){
            logRecordSevere.setMessage( "Configuration error occurred on Authority Agent.");
            Object[] params = new Object[]{"AAE09", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        boolean vcStatusResult = false, vcAcceptStatus = false;
        JSONObject jsonRequest = null;

        DBUtil dbUtil = new DBUtil();
        VCStatus vcStatus = null;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonRequest = (JSONObject) jsonParser.parse(user);
            logRecordInfo.setMessage("SEND-VC: Received input eIDAS user data.");
            Object[] params = new Object[]{"AAI01", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        } catch (ParseException e) {
            logRecordSevere.setMessage("Object conversion error on Authority Agent: [SEND-VC] " + e.getMessage() + ".");
            Object[] params = new Object[]{"AAE03", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        if(jsonRequest != null) {
            String userID = "";
            try {
                userID = jsonRequest.get("userId").toString();
            }
            catch(Exception ex){
                logRecordSevere.setMessage("Missing or invalid arguments at Authority Agent /send-vc: userId");
                Object[] params = new Object[]{"AAE05", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            // DONE: call database getVCStatus(userID): status, piid
            try {
                vcStatus = dbUtil.getVCStatus(userID);
                logRecordInfo.setMessage("SEND-VC: Received user VC status data.");
                Object[] params = new Object[]{"AAI14", alias};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            } catch (Exception ex) {
                logRecordSevere.setMessage("Error accessing data on Authority Agent internal database: [SEND-VC] " + ex.getMessage() + ".");
                Object[] params = new Object[]{"AAE04", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            AriesUtil ariesUtil = new AriesUtil();
            Gson gson = new Gson();

            SignedVerifiableCredential credential = null;
            try {
                credential = gson.fromJson(vcStatus.getVc(), SignedVerifiableCredential.class);
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "Object conversion error in Authority Agent: [SEND-VC] " + ex.getMessage() + ".");
                Object[] params = new Object[]{"AAE03", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

            String outputLastModTime = "";
            try {
                Calendar cal = Calendar.getInstance();
                outputLastModTime = outputFormat.format(cal.getTime());
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "Object conversion error in Authority Agent: [SEND-VC] " + ex.getMessage() + ".");
                Object[] params = new Object[]{"AAE03", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }
            if (vcStatus != null) {
                try {
                    Data data = null;
                    try {
                        data = new Data(Base64.getEncoder().encodeToString(gson.toJson(credential).getBytes(StandardCharsets.UTF_8)));
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "Object conversion error in Authority Agent: [SEND-VC] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE03", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                    CredentialsAttach credentialsAttach = new CredentialsAttach(outputLastModTime, data);
                    IssueCredential issueCredential = new IssueCredential(new ArrayList<CredentialsAttach>(){{add(credentialsAttach);}});
                    SendVCRequest request = new SendVCRequest(issueCredential);

                    //System.out.println("[SEND VC] Signed VC ID: " + request.getCredential().getCredentialsAttach().get(0).getData().getJson().getId());
                    // DONE: call Aries /issuecredential/{PIID}/accept-request(VC):  null/empty
                    try {
                        vcAcceptStatus = ariesUtil.acceptRequest(vcStatus.getPiid(), request);

                        SignedVerifiableCredential vc = gson.fromJson(vcStatus.getVc(), SignedVerifiableCredential.class);
                        logRecordInfo.setMessage("SEND-VC: Sent Verifiable Credential " + vc.getId() + " to the edge agent under invitation " +
                                vcStatus.getDidConn().getInvitationId() + " from " + doURL + ".");
                        Object[] params = new Object[]{"AAI06", alias};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "SEND-VC: Error on response from Authority Agent: [SEND-VC] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE02", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                    // DONE: call database updateVCStatus(userId, status: vc_sent): boolean
                    if (vcAcceptStatus == true) {
                        try {
                            dbUtil.updateVCStatus(userID, VCStatusEnum.VC_SENT);
                            logRecordInfo.setMessage("SEND-VC: Stored current state in Authority Agent internal database.");
                            Object[] params = new Object[]{"AAI13", alias};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);
                        }
                        catch(Exception ex){
                            logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [SEND-VC] " + ex.getMessage() + ".");
                            Object[] params = new Object[]{"AAE04", alias};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                        vcStatusResult = true;
                    }

                } catch (Exception ex) {
                    logRecordSevere.setMessage("Error on response from Authority Agent: [SEND-VC] " + ex.getMessage() + ".");
                    Object[] params = new Object[]{"AAE02", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
            }
        }
        return vcStatusResult;
    }
}
