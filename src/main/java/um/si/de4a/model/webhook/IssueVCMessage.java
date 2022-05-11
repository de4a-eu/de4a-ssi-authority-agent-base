package um.si.de4a.model.webhook;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.*;
import um.si.de4a.resources.vc.SendVCResource;
import um.si.de4a.resources.webhook.EventNotificationResource;
import um.si.de4a.util.DE4ALogger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class IssueVCMessage extends WebhookMessage {

    @Override
    public int updateStatus(String inputMessage) throws IOException {

        System.out.println("[WEBHOOK PARSER] Received input msg: " + inputMessage);
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        int vcStatusCode = 0;
        Gson gson = new Gson();
        VCStatus vcStatus = null;
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
            vcStatus = dbUtil.getVCStatusByPiid(inputPiid);
            //System.out.println("[WEBHOOK-PARSER] Found piid:" + inputPiid + ", userID: " + vcStatus.getUserId());
        } catch (Exception ex) {
            logRecordSevere.setMessage("Error accessing data on Authority Agent DT.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //System.out.println("[DID-CONN-STATUS] Exception: " + ex.getMessage());
        }

        if (vcStatus != null) {
            JSONObject jsonObject = (JSONObject) jsonMessage.get("Message");

            if (jsonObject.get("@type").equals("https://didcomm.org/issue-credential/2.0/request-credential")) {

                System.out.println("[WEBHOOK PARSER] Offer accepted!");

                System.out.println("[WEBHOOK PARSER] VCStatus for userID: " + vcStatus.getUserId());

                JSONObject action = null;
                try {
                    action = ariesUtil.getAction(vcStatus.getPiid());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (action != null) {
                    //System.out.println("[CHECK OFFER VC STATUS] Action: " + action.toJSONString());
                    JSONObject msg = (JSONObject) action.get("Msg");

                    if (msg.get("@type").equals("https://didcomm.org/issue-credential/2.0/request-credential") && msg.get("description") == null) {
                        /*try {
                            dbUtil.updateVCStatus(vcStatus.getUserId(), VCStatusEnum.OFFER_ACCEPTED);

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
                        vcStatusCode = 1; // (offer accepted)
                        */
                        if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT) {
                            SendVCResource sendVCResource = new SendVCResource();
                            JSONObject user = new JSONObject();
                            user.put("userId", vcStatus.getUserId());

                            boolean vcSent = sendVCResource.sendVC(user.toString());
                            if (vcSent == true)
                                vcStatusCode = 2;
                            else
                                vcStatusCode = -3;

                            SocketEvent event = new SocketEvent("vc-event", vcStatus.getUserId(), inputPiid, vcStatusCode);
                            eventNotifierResource.sendEventNotification(gson.toJson(event));
                        }
                    } else {
                        JSONObject description = (JSONObject) msg.get("description");
                        if (description.get("code").equals("rejected")) {
                            if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT) {
                                try {
                                    dbUtil.updateVCStatus(vcStatus.getUserId(), VCStatusEnum.OFFER_REJECTED);

                                    logRecordInfo.setMessage("Stored current state in the Authority Agent DT database.");
                                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01006"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                } catch (Exception ex) {
                                    logRecordSevere.setMessage("Error saving data on Authority Agent DT.");
                                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                                vcStatusCode = -2; // return -2 (offer rejected)
                                SocketEvent event = new SocketEvent("vc-event", vcStatus.getUserId(), inputPiid, vcStatusCode);

                                eventNotifierResource.sendEventNotification(gson.toJson(event));
                            }
                        }
                    }
                }
            } else if (jsonObject.get("@type").equals("https://didcomm.org/issue-credential/2.0/ack") && jsonMessage.get("Type").equals("post_state")) {
                System.out.println("[WEBHOOK PARSER] VC accepted!");

                if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT) {
                    try {
                        dbUtil.updateVCStatus(vcStatus.getUserId(), VCStatusEnum.VC_ACCEPTED);

                        logRecordInfo.setMessage("Stored current state in the Authority Agent DT database.");
                        Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01006"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    } catch (Exception ex) {
                        logRecordSevere.setMessage("Error saving data on Authority Agent DT.");
                        Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                    vcStatusCode = 5; // return 5 (VC accepted)
                    SocketEvent socketEvent = new SocketEvent("vc-event", vcStatus.getUserId(), inputPiid, vcStatusCode);

                    eventNotifierResource.sendEventNotification(gson.toJson(socketEvent));
                }
            }

        }

        return vcStatusCode;
    }
}
