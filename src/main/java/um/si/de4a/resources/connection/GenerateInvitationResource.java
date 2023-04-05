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
package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.util.CustomDE4ALogFormatter;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.logging.*;

@Path("/generate-invitation")
public class GenerateInvitationResource {

    private AppConfig appConfig = null;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String createInvitation(String user) throws IOException, ParseException {
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

        boolean dbStored= false;
        String invitationJson = "";
        JSONObject jsonUserID = null;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonUserID = (JSONObject) jsonParser.parse(user);
            logRecordInfo.setMessage("GENERATE-INVITATION: Received input eIDAS user data.");
            Object[] params = new Object[]{"AAI01", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);

        } catch (ParseException e) {
            logRecordSevere.setMessage("Object conversion error on Authority Agent: [GENERATE-INVITATION] " + e.getMessage() + ".");
            Object[] params = new Object[]{"AAE03", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);

            e.printStackTrace();
        }

        JSONObject invitation = null;
        if(jsonUserID != null) {
            String userID = "";
            try {
                userID = jsonUserID.get("userId").toString();
                logRecordInfo.setMessage("GENERATE-INVITATION: Received input userId data.");
                Object[] params = new Object[]{"AAI02", alias};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            }
            catch(Exception ex){
                logRecordSevere.setMessage("Object conversion error on Authority Agent: [GENERATE-INVITATION] " + ex.getMessage() + ".");
                Object[] params = new Object[]{"AAE03", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            // DONE: call Aries /connections/create-invitation : JSON
            AriesUtil aries = new AriesUtil();
            invitation = aries.generateInvitation();

            if (invitation != null) {
                try {
                    invitationJson = invitation.get("invitation").toString();
                }
                catch(Exception ex){
                    logRecordSevere.setMessage("Object conversion error in Authority Agent: [GENERATE-INVITATION] " + ex.getMessage() + ".");
                    Object[] params = new Object[]{"AAE03", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
                JSONObject jsonObjectInv = (JSONObject) jsonParser.parse(invitationJson);

                long currentTime = System.currentTimeMillis();

                // DONE: saveDIDConn(userID, invitationID, invitationJSON, status: invitation_generated): boolean
                DBUtil dbUtil = new DBUtil();
                try {
                    //System.out.println("GENERATE-INVITATION: Current DIDConn: " + dbUtil.getCurrentDIDConnStatus(userID));

                    if(dbUtil.getCurrentDIDConnStatus(userID) != null){ // there is an existing DIDConn
                        logRecordInfo.setMessage("GENERATE-INVITATION: Overwriting DID connection with ID: " + dbUtil.getCurrentDIDConnStatus(userID).getInvitationId() + " to ID " +
                                jsonObjectInv.get("@id") + ".");
                        Object[] params = new Object[]{"AAI28", alias};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);

                        List<DIDConn> didConnList = dbUtil.getDIDConnStatuses(userID);
                        for (DIDConn didConn: didConnList){
                            if(didConn.getTimeDeleted() == -1){
                                dbUtil.updateOldDIDConnections(userID, didConn.getMyDID(), didConn.getTheirDID(), didConn.getConnectionId(), didConn.getStatus());
                            }
                        }
                        //dbStored = dbUtil.saveDIDConn(userID, jsonObjectInv.get("@id").toString(), invitation.toString(), currentTime);
                    }

                    dbStored = dbUtil.saveDIDConn(userID, jsonObjectInv.get("@id").toString(), invitation.toString(), currentTime);
                    logRecordInfo.setMessage("GENERATE-INVITATION: Stored current state in Authority Agent internal database.");
                    Object[] params = new Object[]{"AAI13", alias};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                } catch (Exception ex) {
                    logRecordSevere.setMessage( "Error saving data on Authority Agent internal database: [GENERATE-INVITATION] " + ex.getMessage() + ".");
                    Object[] params = new Object[]{"AAE04", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                    //System.out.println("[GENERATE-INVITATION] Exception: " + ex.getMessage() + ".");
                }
            }
        }
        return invitation.toString();
    }
}
