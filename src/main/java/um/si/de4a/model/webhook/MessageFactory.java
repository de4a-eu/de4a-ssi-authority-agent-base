package um.si.de4a.model.webhook;

public class MessageFactory {
    public WebhookMessage getMessageHandler(String messageType){
        if(messageType == null)
            return null;
        if(messageType.equalsIgnoreCase("didexchange_states")){
            return new DIDConnMessage();
        }
        return null;
    }
}
