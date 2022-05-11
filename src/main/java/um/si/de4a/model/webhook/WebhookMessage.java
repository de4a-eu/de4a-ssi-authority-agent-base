package um.si.de4a.model.webhook;

import java.io.IOException;

public abstract class WebhookMessage {
    public abstract int updateStatus(String inputMessage) throws IOException;
}
