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
    private String clientURL = "";
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
        }
        catch(Exception ex){
            logRecordSevere.setMessage( "Error reading configuration properties.");
            Object[] params = new Object[]{"Authority Agent DT", "Authority Agent DT", "3001"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
    }

    @Override
    public int updateStatus(String inputMessage) throws IOException {

        logRecordInfo.setMessage("WEBHOOK-PARSER: Received input event: " + inputMessage);
        Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01024"};
        logRecordInfo.setParameters(params);
        logger.log(logRecordInfo);

        int vcStatusCode = 0;
        Gson gson = new Gson();
        VCStatus vcStatus = null;
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
        String inputPiid = jsonProperties.get("piid").toString();

        try {
            vcStatus = dbUtil.getVCStatusByPiid(inputPiid);
        } catch (Exception ex) {
            logRecordSevere.setMessage("WEBHOOK-PARSER: Error accessing data on Authority Agent DT.");
            params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //System.out.println("[DID-CONN-STATUS] Exception: " + ex.getMessage());
        }

        if (vcStatus != null) {
            JSONObject jsonObject = (JSONObject) jsonMessage.get("Message");

            if (jsonObject.get("@type").equals("https://didcomm.org/issue-credential/2.0/request-credential")) {

                System.out.println("[WEBHOOK PARSER] Offer accepted!");

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

                            logRecordInfo.setMessage("WEBHOOK-PARSER: Offer with piid: " + vcStatus.getPiid() + " has been accepted.");
                            params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01025"};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);

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

                                logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification was successfully sent. Received HTTP response code: " + response.getStatusLine());
                                params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01023"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                            }
                            catch(Exception ex){
                                System.out.println(ex.getMessage());

                                logRecordSevere.setMessage("WEBHOOK-PARSER: Event notification could not be sent.");
                                params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1020"};
                                logRecordSevere.setParameters(params);
                                logger.log(logRecordSevere);
                            }
                        }
                    } else {
                        JSONObject description = (JSONObject) msg.get("description");
                        if (description.get("code").equals("rejected")) {
                            if (vcStatus.getVCStatusEnum() == VCStatusEnum.OFFER_SENT) {
                                try {
                                    dbUtil.updateVCStatus(vcStatus.getUserId(), VCStatusEnum.OFFER_REJECTED);

                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in the Authority Agent DT database.");
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01006"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                } catch (Exception ex) {
                                    logRecordSevere.setMessage("WEBHOOK-PARSER: Error saving data on Authority Agent DT.");
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
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
                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification was successfully sent. Received HTTP response code: " + response.getStatusLine());
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01023"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("WEBHOOK-PARSER: Event notification could not be sent.");
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1020"};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                            }
                        }
                    }
                }
            } else if (jsonObject.get("@type").equals("https://didcomm.org/issue-credential/2.0/ack") && jsonMessage.get("Type").equals("post_state")) {
                logRecordInfo.setMessage("WEBHOOK-PARSER: VC with piid: " + vcStatus.getPiid() + " has been accepted.");
                params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01025"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);

                if (vcStatus.getVCStatusEnum() == VCStatusEnum.VC_SENT) {
                    try {
                        dbUtil.updateVCStatus(vcStatus.getUserId(), VCStatusEnum.VC_ACCEPTED);

                        logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in the Authority Agent DT database.");
                        params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01006"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    } catch (Exception ex) {
                        logRecordSevere.setMessage("WEBHOOK-PARSER: Error saving data on Authority Agent DT.");
                        params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
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

                        logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification was successfully sent. Received HTTP response code: " + response.getStatusLine());
                        params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01023"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage("WEBHOOK-PARSER: Event notification could not be sent.");
                        params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1020"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                }
            }

        }

        return vcStatusCode;
    }
}
