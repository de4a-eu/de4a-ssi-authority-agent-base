package um.si.de4a.model.webhook;

import org.json.simple.JSONObject;

import java.io.IOException;

public abstract class WebhookMessage {
    public abstract int updateStatus(String inputMessage) throws IOException;
}
