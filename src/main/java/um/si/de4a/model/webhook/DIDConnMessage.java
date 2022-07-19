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
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.DIDConnStatusEnum;
import um.si.de4a.util.DE4ALogger;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class DIDConnMessage extends WebhookMessage  implements Serializable {

    private AppConfig appConfig;
    private String clientURL = "";
    private Logger logger = null;
    private LogRecord logRecordInfo = null;
    private LogRecord logRecordSevere = null;

    public DIDConnMessage() throws IOException {
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
        Gson gson = new Gson();

        logRecordInfo.setMessage("WEBHOOK-PARSER: Received input event: " + inputMessage);
        Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01024"};
        logRecordInfo.setParameters(params);
        logger.log(logRecordInfo);

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
            logRecordSevere.setMessage("WEBHOOK-PARSER: Error accessing data on Authority Agent DT.");
            params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //System.out.println("[DID-CONN-STATUS] Exception: " + ex.getMessage());
        }

        if(userDidConn != null){
            if(jsonMessage.get("Type").toString().equalsIgnoreCase("post_state") && jsonMessage.get("StateID").toString().equalsIgnoreCase("completed")){

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
                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in the Authority Agent DT database.");
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01006"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("WEBHOOK-PARSER: Error saving data on Authority Agent DT.");
                                    params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }

                                connectionStatusCode = 1;// return 1 (Connection has been established)

                                logRecordInfo.setMessage("WEBHOOK-PARSER: DID Connection has been established for user: " + userDidConn.getUserId() + ".");
                                params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01009"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);

                                SocketEvent event = new SocketEvent("did-exchange",userDidConn.getUserId(), invitationID, connectionStatusCode);


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
            }
        }

        return connectionStatusCode;
    }
}
