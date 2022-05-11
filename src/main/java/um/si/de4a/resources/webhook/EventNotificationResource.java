package um.si.de4a.resources.webhook;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import um.si.de4a.AppConfig;
import um.si.de4a.model.json.SignedVerifiableCredentialUpdated;
import um.si.de4a.util.DE4ALogger;

import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class EventNotificationResource {

    private AppConfig appConfig = null;
    private String clientURL = "";
    private Logger logger = null;
    private LogRecord logRecordInfo = null;
    private LogRecord logRecordSevere = null;

    public EventNotificationResource() throws IOException {
        logger = DE4ALogger.getLogger();
        logRecordInfo = new LogRecord(Level.INFO, "");
        logRecordSevere = new LogRecord(Level.SEVERE, "");

        appConfig = new AppConfig();

        try {
            clientURL = appConfig.getProperties().getProperty("events.url");
        }
        catch(Exception ex){
            logRecordSevere.setMessage( "Error reading configuration properties.");
            Object[] params = new Object[]{"Authority Agent DT", "Authority Agent DT", "3001"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
    }

    public boolean sendEventNotification(String contents) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;

        boolean isSent = false;
        Gson gson = new Gson();
        try {
            HttpPost request = new HttpPost(clientURL);
            StringEntity input = new StringEntity(contents);

            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);

            try {
                response = httpClient.execute(request);
                logRecordInfo.setMessage("EVENT-NOTIFIER: Received HTTP response code " + response.getStatusLine().getStatusCode() + " from endpoint: " + clientURL);
                Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "0101"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "EVENT-NOTIFIER: Connection error with the client.");
                Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            if (response.getStatusLine().getStatusCode() == 200) {
                isSent = true;
                System.out.println("[EVENT-NOTIFIER] Sent output event notification: " + contents);
            }
        }
        catch(Exception ex) {
            //System.out.println("[ARIES sign-credential] Exception: " + ex.getMessage());
            logRecordSevere.setMessage( "EVENT-NOTIFIER: Connection error with the client.");
            Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        } finally {
            httpClient.close();
        }

        return isSent;
    }
}

