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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VPStatus;
import um.si.de4a.db.VPStatusEnum;
import um.si.de4a.resources.vp.NamesObj;
import um.si.de4a.util.DE4ALogger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SubmitVPMessage extends WebhookMessage {

    private AppConfig appConfig;
    private String clientURL = "", alias = "", deURL = "";
    private Logger logger = null;
    private LogRecord logRecordInfo = null;
    private LogRecord logRecordSevere = null;

    public SubmitVPMessage() throws IOException {
        appConfig = new AppConfig();
        logger = DE4ALogger.getLogger();
        logRecordInfo = new LogRecord(Level.INFO, "");
        logRecordSevere = new LogRecord(Level.SEVERE, "");
        try {
            clientURL = appConfig.getProperties().getProperty("client.url");
            alias = appConfig.getProperties().getProperty("alias");
            deURL = appConfig.getProperties().getProperty("de.url");
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

        int vpStatusCode = 0;
        Gson gson = new Gson();
        VPStatus vpStatus = null;
        DBUtil dbUtil = new DBUtil();
        AriesUtil ariesUtil = new AriesUtil();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonMessage = null;
        try {
            jsonMessage = (JSONObject) jsonParser.parse(inputMessage);
            System.out.println("WEBHOOK-PARSER-DEBUG: Received input webhook message: " + jsonMessage);

        } catch (ParseException e) {
            logRecordSevere.setMessage("Object conversion error in Authority Agent: [WEBHOOK-PARSER-VP] " + e.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        JSONObject jsonProperties = (JSONObject) jsonMessage.get("Properties");
        String inputPiid = jsonProperties.get("piid").toString();

        try {
            vpStatus = dbUtil.getVPStatusByPiid(inputPiid);
            logRecordInfo.setMessage("WEBHOOK-PARSER: Received user VP status data.");
            Object[] params = new Object[]{"AAI14", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        } catch (Exception ex) {
            logRecordSevere.setMessage("Error accessing data on Authority Agent internal database: [WEBHOOK-PARSER-VP] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        if (vpStatus != null) {

            JSONObject jsonObject = (JSONObject) jsonMessage.get("Message");

            if (jsonObject.get("@type").equals("https://didcomm.org/present-proof/2.0/presentation")) {

                try {
                    JSONObject action = ariesUtil.getActionVP(vpStatus.getPiid());
                    if (action != null){
                        JSONObject msg = (JSONObject) action.get("Msg");

                        if(msg.get("@type").equals("https://didcomm.org/present-proof/2.0/presentation") && msg.get("description") == null){
                            boolean acceptResult = ariesUtil.acceptPresentation(vpStatus.getPiid(), new NamesObj(new String[]{"vp-" + vpStatus.getUserId() + "-" + vpStatus.getPiid()}));
                            if(acceptResult == true){
                                boolean updateStatus = false;

                                JSONObject jsonPresentation = null;
                                try{
                                    jsonPresentation = ariesUtil.getPresentation("vp-" + vpStatus.getUserId() + "-" + vpStatus.getPiid());
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage( "Error on response from Authority Agent: [WEBHOOK-PARSER-VP] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE02", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                                if (jsonPresentation != null) {
                                    JSONObject vc = null;
                                    if (jsonPresentation.containsKey("verifiableCredential")) {
                                        JSONArray credentials = null;
                                        try {
                                            credentials = (JSONArray) jsonPresentation.get("verifiableCredential");

                                            vc = (JSONObject) credentials.get(0);

                                            logRecordInfo.setMessage("WEBHOOK-PARSER: Received Verifiable Credential " + vc.get("id") +
                                                    " at the verifier " + deURL + " under invitation " + vpStatus.getDidConn().getInvitationId() + ".");
                                            Object[] params = new Object[]{"AAI20", alias};
                                            logRecordInfo.setParameters(params);
                                            logger.log(logRecordInfo);
                                        } catch (Exception ex) {
                                            logRecordSevere.setMessage("Error on response from Authority Agent: [WEBHOOK-PARSER-VP] " + ex.getMessage() + ".");
                                            Object[] params = new Object[]{"AAE02", alias};
                                            logRecordSevere.setParameters(params);
                                            logger.log(logRecordSevere);
                                        }
                                    }
                                }
                                try {
                                    updateStatus = dbUtil.updateVPStatus(vpStatus.getUserId(), "vp-" + vpStatus.getUserId() + "-" + vpStatus.getPiid(), VPStatusEnum.VP_RECEIVED);
                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in Authority Agent internal database.");
                                    Object[] params = new Object[]{"AAI13", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                } catch (Exception ex) {
                                    logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [WEBHOOK-PARSER-VP] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE04", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }

                                if (updateStatus == true) {

                                    vpStatusCode = 1; // (vp received)
                                    SocketEvent event = new SocketEvent("vpstatus", vpStatus.getUserId(), inputPiid, vpStatusCode);

                                    CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
                                    try {
                                        HttpPost request = new HttpPost(clientURL);
                                        StringEntity input = new StringEntity(gson.toJson(event));

                                        input.setContentType("application/json;charset=UTF-8");
                                        input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                                        request.setEntity(input);

                                        HttpResponse response = httpClient.execute(request);
                                        logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification of type 'vpstatus' was successfully sent to " + clientURL +". Received HTTP response code: " + response.getStatusLine() + ".");
                                        Object[] params = new Object[]{"AAI32", alias};
                                        logRecordInfo.setParameters(params);
                                        logger.log(logRecordInfo);
                                    }
                                    catch(Exception ex){
                                        logRecordSevere.setMessage("Event notification of type 'vpstatus' could not be sent: [WEBHOOK-PARSER] " + ex.getMessage() + ".");
                                        Object[] params = new Object[]{"AAE10", alias};
                                        logRecordSevere.setParameters(params);
                                        logger.log(logRecordSevere);
                                    }
                                }

                            }
                        }
                        else {
                            JSONObject description = (JSONObject) msg.get("description");
                            if (description.get("code").equals("rejected")) {
                                try{
                                    dbUtil.updateVPStatus(vpStatus.getUserId(), VPStatusEnum.VP_REJECTED);
                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in Authority Agent internal database.");
                                    Object[] params = new Object[]{"AAI13", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [WEBHOOK-PARSER-VP] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE04", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                                vpStatusCode = -2; // return -2 (vp rejected)
                                SocketEvent event = new SocketEvent("vpstatus", vpStatus.getUserId(), inputPiid, vpStatusCode);

                                CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
                                try {

                                    HttpPost request = new HttpPost(clientURL);
                                    StringEntity input = new StringEntity(gson.toJson(event));

                                    input.setContentType("application/json;charset=UTF-8");
                                    input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                                    request.setEntity(input);

                                    HttpResponse response = httpClient.execute(request);

                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification of type 'vpstatus' was successfully sent to " + clientURL +". Received HTTP response code: " + response.getStatusLine() + ".");
                                    Object[] params = new Object[]{"AAI32", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("Event notification of type 'vpstatus' could not be sent: [WEBHOOK-PARSER] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE10", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }
                            }
                        }
                    }
                } catch (ParseException e) {
                    // e.printStackTrace();
                    logRecordSevere.setMessage( "Error on response from Authority Agent: [WEBHOOK-PARSER-VP] " + e.getMessage() + ".");
                    Object[] params = new Object[]{"AAE02", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
            }
            else if(jsonObject.get("@type").equals("https://didcomm.org/present-proof/2.0/problem-report")) {
                JSONObject description = (JSONObject) jsonObject.get("description");
                if (description.get("code").equals("rejected")) {
                    try{
                        dbUtil.updateVPStatus(vpStatus.getUserId(), VPStatusEnum.VP_REJECTED);
                        logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in Authority Agent internal database.");
                        Object[] params = new Object[]{"AAI13", alias};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [WEBHOOK-PARSER-VP] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE04", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                    vpStatusCode = -2; // return -2 (vp rejected)
                    SocketEvent event = new SocketEvent("vpstatus", vpStatus.getUserId(), inputPiid, vpStatusCode);

                    CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
                    try {

                        HttpPost request = new HttpPost(clientURL);
                        StringEntity input = new StringEntity(gson.toJson(event));

                        input.setContentType("application/json;charset=UTF-8");
                        input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                        request.setEntity(input);

                        HttpResponse response = httpClient.execute(request);

                        logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification of type 'vpstatus' was successfully sent to " + clientURL +". Received HTTP response code: " + response.getStatusLine() + ".");
                        Object[] params = new Object[]{"AAI32", alias};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage("Event notification of type 'vpstatus' could not be sent: [WEBHOOK-PARSER] " + ex.getMessage() + ".");
                        Object[] params = new Object[]{"AAE10", alias};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                }
            }
        }

        return vpStatusCode;
    }
}
