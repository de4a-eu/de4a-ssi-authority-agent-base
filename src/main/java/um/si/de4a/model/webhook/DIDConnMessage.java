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
    private String clientURL = "", alias = "";
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
        Gson gson = new Gson();

        int connectionStatusCode = 0;
        DIDConn userDidConn = null;
        DBUtil dbUtil = new DBUtil();
        AriesUtil ariesUtil = new AriesUtil();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonMessage = null;
        try {
            jsonMessage = (JSONObject) jsonParser.parse(inputMessage);
            System.out.println("WEBHOOK-PARSER: Received input webhook message: " + jsonMessage);

        } catch (ParseException e) {
            logRecordSevere.setMessage("Object conversion error in Authority Agent: [WEBHOOK-PARSER-DIDConn] " + e.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        JSONObject jsonProperties = (JSONObject) jsonMessage.get("Properties");
        String inputInvitationID = jsonProperties.get("invitationID").toString();


        try {
            userDidConn = dbUtil.getDIDConnbyInvitationID(inputInvitationID);
            logRecordInfo.setMessage("WEBHOOK-PARSER: Received user DIDConn status data.");
            Object[] params = new Object[]{"AAI14", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Error accessing data on Authority Agent internal database: [WEBHOOK-PARSER-DIDConn] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        if(userDidConn != null){
            if(jsonMessage.get("Type").toString().equalsIgnoreCase("post_state") && jsonMessage.get("StateID").toString().equalsIgnoreCase("completed")){

                ArrayList<JSONObject> connections = null;
                try {
                    connections = ariesUtil.getConnectionsForWebhooks();
                } catch (ParseException e) {
                    logRecordSevere.setMessage( "Error on response from Authority Agent: [WEBHOOK-PARSER-DIDConn] " + e.getMessage() + ".");
                    Object[] params = new Object[]{"AAE02", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
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
                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Stored current state in Authority Agent internal database.");
                                    Object[] params = new Object[]{"AAI13", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("Error saving data on Authority Agent internal database: [WEBHOOK-PARSER-DIDConn] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE04", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }

                                connectionStatusCode = 1;// return 1 (Connection has been established)

                                logRecordInfo.setMessage("WEBHOOK-PARSER: DID Connection has been established for invitation: " + userDidConn.getInvitationId() + ".");
                                Object[] params = new Object[]{"AAI29", alias};
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
                                    logRecordInfo.setMessage("WEBHOOK-PARSER: Event notification of type 'did-exchange' was successfully sent to " + clientURL +". Received HTTP response code: " + response.getStatusLine() + ".");
                                    params = new Object[]{"AAI32", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("Event notification of type 'did-exchange' could not be sent: [WEBHOOK-PARSER] " + ex.getMessage() + ".");
                                    params = new Object[]{"AAE10", alias};
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
