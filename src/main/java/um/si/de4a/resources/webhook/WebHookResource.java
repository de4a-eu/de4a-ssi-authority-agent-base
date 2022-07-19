package um.si.de4a.resources.webhook;

import jnr.ffi.annotations.Encoding;
import org.jboss.resteasy.annotations.ContentEncoding;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.model.webhook.MessageFactory;
import um.si.de4a.model.webhook.WebhookMessage;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/webhook")
public class WebHookResource {

    private AppConfig appConfig = null;

    @POST
    @Consumes({"application/json", "text/plain", "text/html", "application/x-www-form-urlencoded", "*/*"})
    @Produces({"application/json", "text/plain", "text/html", "application/x-www-form-urlencoded", "*/*"})
    public void receiveWebhookInfo(String contents) throws IOException, ParseException {

       // System.out.println("Received webhook message: " + contents.toString());
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonContents = (JSONObject) jsonParser.parse(contents);
        String topicName = jsonContents.get("topic").toString();
        String msgContent = jsonContents.get("message").toString();

        MessageFactory msgFactory = new MessageFactory();
        WebhookMessage webhookMessage = msgFactory.getMessageHandler(topicName);

        webhookMessage.updateStatus(msgContent);

        //appConfig = new AppConfig();

    }
}

