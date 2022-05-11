package um.si.de4a.model.webhook;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.*;
import um.si.de4a.resources.vp.NamesObj;
import um.si.de4a.resources.webhook.EventNotificationResource;
import um.si.de4a.util.DE4ALogger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SubmitVPMessage extends WebhookMessage {

    @Override
    public int updateStatus(String inputMessage) throws IOException {

        System.out.println("[WEBHOOK PARSER] Received input msg: " + inputMessage);
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        int vpStatusCode = 0;
        Gson gson = new Gson();
        VPStatus vpStatus = null;
        DBUtil dbUtil = new DBUtil();
        AriesUtil ariesUtil = new AriesUtil();
        EventNotificationResource eventNotifierResource = new EventNotificationResource();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonMessage = null;
        try {
            jsonMessage = (JSONObject) jsonParser.parse(inputMessage);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject jsonProperties = (JSONObject) jsonMessage.get("Properties");
        String inputPiid = jsonProperties.get("piid").toString();


        try {
            vpStatus = dbUtil.getVPStatusByPiid(inputPiid);
            //System.out.println("[WEBHOOK-PARSER] Found piid:" + inputPiid + ", userID: " + vcStatus.getUserId());
        } catch (Exception ex) {
            logRecordSevere.setMessage("Error accessing data on Authority Agent DT.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //System.out.println("[DID-CONN-STATUS] Exception: " + ex.getMessage());
        }

        if (vpStatus != null) {

            JSONObject jsonObject = (JSONObject) jsonMessage.get("Message");

            if (jsonObject.get("@type").equals("https://didcomm.org/present-proof/2.0/presentation")) {

                System.out.println("[WEBHOOK PARSER] VP submitted!");

                System.out.println("[WEBHOOK PARSER] VPStatus for userID: " + vpStatus.getUserId());

                try {
                    JSONObject action = ariesUtil.getActionVP(vpStatus.getPiid());
                    if (action != null){
                        JSONObject msg = (JSONObject) action.get("Msg");

                        if(msg.get("@type").equals("https://didcomm.org/present-proof/2.0/presentation") && msg.get("description") == null){
                            boolean acceptResult = ariesUtil.acceptPresentation(vpStatus.getPiid(), new NamesObj(new String[]{"vp-" + vpStatus.getUserId() + "-" + vpStatus.getPiid()}));
                            if(acceptResult == true){
                                boolean updateStatus = false;
                                try {
                                    updateStatus = dbUtil.updateVPStatus(vpStatus.getUserId(), "vp-" + vpStatus.getUserId() + "-" + vpStatus.getPiid(), VPStatusEnum.VP_RECEIVED);
                                    logRecordInfo.setMessage("Stored current state in the Authority Agent DR database.");
                                    Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "01006"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                } catch (Exception ex) {
                                    logRecordSevere.setMessage("Error saving data on Authority Agent DR.");
                                    Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "1010"};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }

                                if (updateStatus == true) {
                                    vpStatusCode = 1; // (vp received)
                                    SocketEvent event = new SocketEvent("vp-event", vpStatus.getUserId(), inputPiid, vpStatusCode);

                                    eventNotifierResource.sendEventNotification(gson.toJson(event));
                                }

                            }
                        }
                        else {
                            JSONObject description = (JSONObject) msg.get("description");
                            if (description.get("code").equals("rejected")) {
                                try{
                                    dbUtil.updateVPStatus(vpStatus.getUserId(), VPStatusEnum.VP_REJECTED);
                                    logRecordInfo.setMessage("Stored current state in the Authority Agent DR database.");
                                    Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "01006"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("Error saving data on Authority Agent DR.");
                                    Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "1010"};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                                vpStatusCode = -2; // return -2 (vp rejected)
                                SocketEvent event = new SocketEvent("vp-event", vpStatus.getUserId(), inputPiid, vpStatusCode);

                                eventNotifierResource.sendEventNotification(gson.toJson(event));
                            }
                        }
                    }
                } catch (ParseException e) {
                    // e.printStackTrace();
                    logRecordSevere.setMessage( "Error on response from the Aries Government Agent.");
                    Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "1002"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
            }
        }

        return vpStatusCode;
    }
}
