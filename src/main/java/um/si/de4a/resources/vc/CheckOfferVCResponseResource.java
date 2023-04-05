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

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/check-offer-vc-response")
public class CheckOfferVCResponseResource {
    private AppConfig appConfig = null;

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{userId}")
    public int checkResponse(@PathParam("userId") String userID) throws IOException, ParseException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        String alias = "";
        appConfig = new AppConfig();
        try {
            alias = appConfig.getProperties().getProperty("alias");
        }
        catch (Exception ex){
            logRecordSevere.setMessage( "Configuration error occurred on Authority Agent.");
            Object[] params = new Object[]{"AAE09", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        int vcStatusCode = 0;
        DBUtil dbUtil = new DBUtil();

        // DONE: call database getVCStatus(userID): status, piid
        VCStatus vcStatus = null;
        try {
            vcStatus = dbUtil.getVCStatus(userID);
            logRecordInfo.setMessage("CHECK-OFFER-VC-STATUS: Received user VC status data.");
            Object[] params = new Object[]{"AAI14", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Error accessing data on Authority Agent internal database: [CHECK-VC-OFFER] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
        if(vcStatus == null)
            vcStatusCode = -1; //  return -1 (offer was never sent)
        else {
            if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_ACCEPTED)
                vcStatusCode = 1; // return 1 (offer accepted)
            else  if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_ACCEPTED)
                vcStatusCode = 5; // return 5 (VC accepted)
            else if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_REJECTED)
                vcStatusCode = -2; // return -2 (offer rejected)
            else if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_REJECTED)
                vcStatusCode = -4; // return -4 (VC rejected)
            else if ((vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT) || (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT)){

                // DONE: call Aries /issuecredential/actions: [action]
                AriesUtil ariesUtil = new AriesUtil();
                JSONObject action = ariesUtil.getAction(vcStatus.getPiid());
                if(action != null) {
                    //System.out.println("[CHECK OFFER VC STATUS] Action: " + action.toJSONString());
                    JSONObject msg = (JSONObject) action.get("Msg");

                    if(msg.get("@type").equals("https://didcomm.org/issue-credential/2.0/request-credential") && msg.get("description") == null){
                        try {
                            dbUtil.updateVCStatus(userID, VCStatusEnum.OFFER_ACCEPTED);

                            logRecordInfo.setMessage("CHECK-VC-OFFER-STATUS: Stored current state in Authority Agent internal database.");
                            Object[] params = new Object[]{"AAI13", alias};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);
                        }
                        catch(Exception ex){
                            logRecordSevere.setMessage( "Error saving data on Authority Agent internal database: [CHECK-VC-OFFER-STATUS] " + ex.getMessage() + ".");
                            Object[] params = new Object[]{"AAE04", alias};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                        vcStatusCode = 1; // (offer accepted)
                    }
                    else {
                        JSONObject description = (JSONObject) msg.get("description");
                        if(description.get("code").equals("rejected")){
                            if(vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT) {
                                try {
                                    dbUtil.updateVCStatus(userID, VCStatusEnum.VC_REJECTED);

                                    logRecordInfo.setMessage("CHECK-VC-OFFER-STATUS: Stored current state in Authority Agent internal database.");
                                    Object[] params = new Object[]{"AAI13", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage( "Error saving data on Authority Agent internal database: [CHECK-VC-OFFER-STATUS] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE04", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                                vcStatusCode = -4; // return -4 (VC rejected)
                            }
                            else{
                                try {
                                    dbUtil.updateVCStatus(userID, VCStatusEnum.OFFER_REJECTED);

                                    logRecordInfo.setMessage("CHECK-VC-OFFER-STATUS: Stored current state in Authority Agent internal database.");
                                    Object[] params = new Object[]{"AAI13", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage( "Error saving data on Authority Agent internal database: [CHECK-VC-OFFER-STATUS] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE04", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                                vcStatusCode = -2; // return -1 (offer rejected)
                            }
                        }
                        /*else if(description.get("code").equals("internal") || description.get("code").equals("accepted")){
                            try {
                                dbUtil.updateVCStatus(userID, VCStatusEnum.VC_ACCEPTED);

                                logRecordInfo.setMessage("Stored current state in Authority Agent DT database.");
                                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01006"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                            }
                            catch(Exception ex){
                                logRecordSevere.setMessage("Error saving data on Authority Agent DT.");
                                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
                                logRecordSevere.setParameters(params);
                                logger.log(logRecordSevere);
                            }
                            vcStatusCode = 5; // return 5 (VC accepted)
                        }*/
                    }
                }
                else{
                    if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT)
                        vcStatusCode = 0; // (offer sent and awaiting response)
                    else if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT)
                        vcStatusCode = 2; // (vc sent and awaiting response)
                }
            }
        }
        return vcStatusCode;
    }
}
