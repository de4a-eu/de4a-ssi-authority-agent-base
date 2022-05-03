package um.si.de4a.resources.webhook;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.model.webhook.MessageFactory;
import um.si.de4a.model.webhook.WebhookMessage;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/send-event")
public class EventNotifierResource {

    private AppConfig appConfig = null;

    @POST
    @Consumes({"application/json", "text/plain", "text/html", "application/x-www-form-urlencoded", "*/*"})
    @Produces({"application/json", "text/plain", "text/html", "application/x-www-form-urlencoded", "*/*"})
    public String sendEventNotification(String contents) throws IOException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        System.out.println("[EVENT-NOTIFIER] Sent output event notification: " + contents);
        return contents;

        //appConfig = new AppConfig();

    }
}

