package um.si.de4a.model.webhook;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.resources.vc.SendVCResource;
import um.si.de4a.util.DE4ALogger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class IssueVCMessage extends WebhookMessage {

    private AppConfig appConfig;
    private String clientURL = "", alias = "";
    private Logger logger = null;
    private LogRecord logRecordInfo = null;
    private LogRecord logRecordSevere = null;

    public IssueVCMessage() throws IOException {
        appConfig = new AppConfig();
        logger = DE4ALogger.getLogger();
        logRecordInfo = new LogRecord(Level.INFO, "");
        logRecordSevere = new LogRecord(Level.SEVERE, "");
        try {
            clientURL = appConfig.getProperties().getProperty("client.url");
            alias = appConfig.getProperties().getProperty("alias");
        }
        catch(Exception ex){
            logRecordSevere.setMessage( "Configuration error occurred on Authority Agent.");
            Object[] params = new Object[]{"AAE09", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
    }

    @Override
    public int updateStatus(String inputMessage) throws IOException {

        int vcStatusCode = 0;
        Gson gson = new Gson();
        VCStatus vcStatus = null;
        DBUtil dbUtil = new DBUtil();
        AriesUtil ariesUtil = new AriesUtil();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonMessage = null;
        try {
            jsonMessage = (JSONObject) jsonParser.parse(inputMessage);
            System.out.println("WEBHOOK-PARSER-DEBUG: Received input webhook message: " + jsonMessage);
        } catch (ParseException e) {
            logRecordSevere.setMessage("Object conversion error in Authority Agent: [WEBHOOK-PARSER-VC] " + e.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        JSONObject jsonProperties = (JSONObject) jsonMessage.get("Properties");
        String inputPiid = jsonProperties.get("piid").toString();

        try {
            vcStatus = dbUtil.getVCStatusByPiid(inputPiid);
            logRecordInfo.setMessage("WEBHOOK-PARSER-DEBUG: Received user VC status data.");
            Object[] params = new Object[]{"AAI14", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        } catch (Exception ex) {
            logRecordSevere.setMessage("Error accessing data on Authority Agent internal database: [WEBHOOK-PARSER-VC] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //System.out.println("[DID-CONN-STATUS] Exception: " + ex.getMessage());
        }

        if (vcStatus != null) {
            System.out.println("WEBHOOK-PARSER-DEBUG: Found vcStatus for user.");
            JSONObject jsonObject = (JSONObject) jsonMessage.get("Message");

            if (jsonObject.get("@type").equals("https://didcomm.org/issue-credential/2.0/request-credential")) {

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

                        if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT) {

                            System.out.println("WEBHOOK-PARSER: Offer with piid: " + vcStatus.getPiid() + " has been accepted.");
                            /*params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01025"};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);*/

                            SendVCResource sendVCResource = new SendVCResource();
                            JSONObject user = new JSONObject();
                            user.put("userId", vcStatus.getUserId());

                            boolean vcSent = sendVCResource.sendVC(user.toString());
                            if (vcSent == true)
                                vcStatusCode = 2;
                            else
                                vcStatusCode = -3;

                            SocketEvent event = new SocketEvent("vcstatus", vcStatus.getUserId(), inputPiid, vcStatusCode);

                            CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
                            try {
                                HttpPost request = new HttpPost(clientURL);
                                StringEntity input = new StringEntity(gson.toJson(event));

                                input.setContentType("application/json;charset=UTF-8");
                                input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                                request.setEntity(input);

                                HttpResponse response = httpClient.execute(request);

                                logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification of type 'vcstatus' was successfully sent to " + clientURL +". Received HTTP response code: " + response.getStatusLine() + ".");
                                Object[] params = new Object[]{"AAI32", alias};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                            }
                            catch(Exception ex){
                                logRecordSevere.setMessage("Event notification of type 'vcstatus' could not be sent: [WEBHOOK-PARSER] " + ex.getMessage() + ".");
                                Object[] params = new Object[]{"AAE10", alias};
                                logRecordSevere.setParameters(params);
                                logger.log(logRecordSevere);
                            }
                        }
                    } else {
                        JSONObject description = (JSONObject) msg.get("description");
                        System.out.println("Description: " + description.toJSONString());
                        if (description.get("code").equals("rejected")) {
                            if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT) {
                                try {
                                    dbUtil.updateVCStatus(vcStatus.getUserId(), VCStatusEnum.OFFER_REJECTED);

                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in Authority Agent internal database.");
                                    Object[] params = new Object[]{"AAI13", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                } catch (Exception ex) {
                                    logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [WEBHOOK-PARSER-VC] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE04", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                                vcStatusCode = -2; // return -2 (offer rejected)
                                SocketEvent event = new SocketEvent("vcstatus", vcStatus.getUserId(), inputPiid, vcStatusCode);

                                CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
                                try {
                                    HttpPost request = new HttpPost(clientURL);
                                    StringEntity input = new StringEntity(gson.toJson(event));

                                    input.setContentType("application/json;charset=UTF-8");
                                    input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                                    request.setEntity(input);

                                    HttpResponse response = httpClient.execute(request);
                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification of type 'vcstatus' was successfully sent to " + clientURL +". Received HTTP response code: " + response.getStatusLine() + ".");
                                    Object[] params = new Object[]{"AAI32", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("Event notification of type 'vcstatus' could not be sent: [WEBHOOK-PARSER] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE10", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                            }
                        }
                    }
                }
            }
            else if (jsonObject.get("@type").equals("https://didcomm.org/issue-credential/2.0/problem-report") && jsonObject.get("description") != null) {

                JSONObject action = null;
                try {
                    action = ariesUtil.getAction(vcStatus.getPiid());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (action != null) {
                JSONObject description = (JSONObject) jsonObject.get("description");
                if (description.get("code").equals("rejected")) {
                    if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT) {
                        try {
                            dbUtil.updateVCStatus(vcStatus.getUserId(), VCStatusEnum.OFFER_REJECTED);

                            logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in Authority Agent internal database.");
                            Object[] params = new Object[]{"AAI13", alias};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);
                        } catch (Exception ex) {
                            logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [WEBHOOK-PARSER-VC] " + ex.getMessage() + ".");
                            Object[] params = new Object[]{"AAE04", alias};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                        vcStatusCode = -2; // return -2 (offer rejected)
                        SocketEvent event = new SocketEvent("vcstatus", vcStatus.getUserId(), inputPiid, vcStatusCode);

                        CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
                        try {
                            HttpPost request = new HttpPost(clientURL);
                            StringEntity input = new StringEntity(gson.toJson(event));

                            input.setContentType("application/json;charset=UTF-8");
                            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                            request.setEntity(input);

                            HttpResponse response = httpClient.execute(request);
                            logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification of type 'vcstatus' was successfully sent to " + clientURL + ". Received HTTP response code: " + response.getStatusLine() + ".");
                            Object[] params = new Object[]{"AAI32", alias};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);
                        } catch (Exception ex) {
                            logRecordSevere.setMessage("Event notification of type 'vcstatus' could not be sent: [WEBHOOK-PARSER] " + ex.getMessage() + ".");
                            Object[] params = new Object[]{"AAE10", alias};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                    } else {
                        try {
                            dbUtil.updateVCStatus(vcStatus.getUserId(), VCStatusEnum.VC_REJECTED);

                            logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in Authority Agent internal database.");
                            Object[] params = new Object[]{"AAI13", alias};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);
                        } catch (Exception ex) {
                            logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [WEBHOOK-PARSER-VC] " + ex.getMessage() + ".");
                            Object[] params = new Object[]{"AAE04", alias};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                        vcStatusCode = -4; // return -4 (vc rejected)
                        SocketEvent event = new SocketEvent("vcstatus", vcStatus.getUserId(), inputPiid, vcStatusCode);

                        CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
                        try {
                            HttpPost request = new HttpPost(clientURL);
                            StringEntity input = new StringEntity(gson.toJson(event));

                            input.setContentType("application/json;charset=UTF-8");
                            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                            request.setEntity(input);

                            HttpResponse response = httpClient.execute(request);
                            logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification of type 'vcstatus' was successfully sent to " + clientURL + ". Received HTTP response code: " + response.getStatusLine() + ".");
                            Object[] params = new Object[]{"AAI32", alias};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);
                        } catch (Exception ex) {
                            logRecordSevere.setMessage("Event notification of type 'vcstatus' could not be sent: [WEBHOOK-PARSER] " + ex.getMessage() + ".");
                            Object[] params = new Object[]{"AAE10", alias};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                    }
                }
                }
            }
            else if (jsonObject.get("@type").equals("https://didcomm.org/issue-credential/2.0/ack") && jsonMessage.get("Type").equals("post_state")) {
                System.out.println("WEBHOOK-PARSER: VC with piid: " + vcStatus.getPiid() + " has been accepted.");

                if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT) {
                    try {
                        dbUtil.updateVCStatus(vcStatus.getUserId(), VCStatusEnum.VC_ACCEPTED);

                        logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in Authority Agent internal database.");
                        Object[] params = new Object[]{"AAI13", alias};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    } catch (Exception ex) {
                        logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [WEBHOOK-PARSER-VC] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE04", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                    vcStatusCode = 5; // return 5 (VC accepted)
                    SocketEvent event = new SocketEvent("vcstatus", vcStatus.getUserId(), inputPiid, vcStatusCode);

                    CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
                    try {
                        HttpPost request = new HttpPost(clientURL);
                        StringEntity input = new StringEntity(gson.toJson(event));

                        input.setContentType("application/json;charset=UTF-8");
                        input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                        request.setEntity(input);

                        HttpResponse response = httpClient.execute(request);

                        logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification of type 'vcstatus' was successfully sent to " + clientURL +". Received HTTP response code: " + response.getStatusLine() + ".");
                        Object[] params = new Object[]{"AAI32", alias};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage("Event notification of type 'vcstatus' could not be sent: [WEBHOOK-PARSER] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE10", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                }
            }

        }

        return vcStatusCode;
    }
}
