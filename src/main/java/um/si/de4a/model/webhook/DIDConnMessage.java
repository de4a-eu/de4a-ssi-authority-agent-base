package um.si.de4a.model.webhook;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.DIDConnStatusEnum;
import um.si.de4a.resources.websocket.EventBean;
import um.si.de4a.util.DE4ALogger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@SessionScoped
public class DIDConnMessage extends WebhookMessage  implements Serializable {

    @Inject
    EventBean eventBean;

    @Override
    public int updateStatus(String inputMessage) throws IOException {
        Gson gson = new Gson();

        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

       // EventNotifierResource eventNotifierResource = new EventNotifierResource();

        int connectionStatusCode = 0;
        DIDConn userDidConn = null;
        DBUtil dbUtil = new DBUtil();
        AriesUtil ariesUtil = new AriesUtil();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonMessage = null;
        try {
            jsonMessage = (JSONObject) jsonParser.parse(inputMessage);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject jsonProperties = (JSONObject) jsonMessage.get("Properties");
        String inputInvitationID = jsonProperties.get("invitationID").toString();


        try {
            userDidConn = dbUtil.getDIDConnbyInvitationID(inputInvitationID);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Error accessing data on Authority Agent DT.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //System.out.println("[DID-CONN-STATUS] Exception: " + ex.getMessage());
        }

        if(userDidConn != null){
            if(jsonMessage.get("Type").toString().equalsIgnoreCase("post_state") && jsonMessage.get("StateID").toString().equalsIgnoreCase("completed")){
                System.out.println("[WEBHOOK parser] Input type: " + jsonMessage.get("Type").toString().toLowerCase(Locale.ROOT));
                System.out.println("[WEBHOOK parser] Input stateID: " + jsonMessage.get("StateID").toString().toLowerCase(Locale.ROOT));

                System.out.println("[WEBHOOK PARSER] DID exchange completed!");

                System.out.println("[WEBHOOK PARSER] DIDConn for userID: " + userDidConn.getUserId());

                ArrayList<JSONObject> connections = null;
                try {
                    connections = ariesUtil.getConnections();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(connections.size() > 0){
                    for (JSONObject conn: connections){
                        //System.out.println("[DID-CONN-STATUS] Connection: " + conn.toString());
                        String connectionID = conn.get("ConnectionID").toString();

                        if (connectionID.equals("")) {
                            connectionStatusCode = 0; // return 0 (Invitation has been generated but not yet accepted)
                        } else {
                            String invitationID = conn.get("InvitationID").toString();
                            //System.out.println("[DID-CONN-STATUS] InvitationID: " + invitationID);

                            if(invitationID.equals(userDidConn.getInvitationId())) {
                                try {
                                    dbUtil.updateDIDConnectionStatus(userDidConn.getUserId(), conn.get("MyDID").toString(),
                                            conn.get("TheirDID").toString(), connectionID, DIDConnStatusEnum.CONNECTION_ESTABLISHED);
                                    logRecordInfo.setMessage("Stored current state in the Authority Agent DT database.");
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

                                connectionStatusCode = 1;// return 1 (Connection has been established)

                                logRecordInfo.setMessage("DID Connection has been established.");
                                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01009"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);

                                SocketEvent event = new SocketEvent("did-exchange",userDidConn.getUserId(), invitationID, connectionStatusCode);



                                try {
                                    System.out.println("Going to push notification......");
                                    System.out.println("Event: " + gson.toJson(eventBean.getEvent()));
                                    eventBean.getEvent().fire(event);

                                    logRecordInfo.setMessage("Event notification was successfully sent.");
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01009"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    System.out.println(ex.getMessage());

                                    logRecordSevere.setMessage("Event notification could not be sent.");
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }

                                /*Gson gson = new Gson();
                                boolean notificationSent = eventNotifierResource.sendEventNotification(gson.toJson(event));

                                if(notificationSent == false){
                                    logRecordSevere.setMessage("Event notification could not be sent.");
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                                else{
                                    logRecordInfo.setMessage("Event notification was successfully sent.");
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01009"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
*/
                            }
                        }
                    }
                }
            }
        }

        return connectionStatusCode;
    }
}
