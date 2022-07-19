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
    private String clientURL = "";
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

        int vpStatusCode = 0;
        Gson gson = new Gson();
        VPStatus vpStatus = null;
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
            vpStatus = dbUtil.getVPStatusByPiid(inputPiid);
        } catch (Exception ex) {
            logRecordSevere.setMessage("WEBHOOK-PARSER: Error accessing data on Authority Agent DT.");
            params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
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
                                try {
                                    updateStatus = dbUtil.updateVPStatus(vpStatus.getUserId(), "vp-" + vpStatus.getUserId() + "-" + vpStatus.getPiid(), VPStatusEnum.VP_RECEIVED);
                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in the Authority Agent DR database.");
                                    params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "01006"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                } catch (Exception ex) {
                                    logRecordSevere.setMessage("WEBHOOK-PARSER: Error saving data on Authority Agent DR.");
                                    params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "1010"};
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
                        else {
                            JSONObject description = (JSONObject) msg.get("description");
                            if (description.get("code").equals("rejected")) {
                                try{
                                    dbUtil.updateVPStatus(vpStatus.getUserId(), VPStatusEnum.VP_REJECTED);
                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in the Authority Agent DR database.");
                                    params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "01006"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("WEBHOOK-PARSER: Error saving data on Authority Agent DR.");
                                    params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "1010"};
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
                } catch (ParseException e) {
                    // e.printStackTrace();
                    logRecordSevere.setMessage( "WEBHOOK-PARSER: Error on response from the Aries Government Agent.");
                    params = new Object[]{"Authority Agent DR", "Aries Government Agent", "1002"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
            }
        }

        return vpStatusCode;
    }
}
