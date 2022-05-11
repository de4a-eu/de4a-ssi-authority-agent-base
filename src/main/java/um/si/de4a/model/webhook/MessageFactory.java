package um.si.de4a.model.webhook;

public class MessageFactory {
    public WebhookMessage getMessageHandler(String messageType){
        if(messageType == null)
            return null;
        if(messageType.equalsIgnoreCase("didexchange_states")){
            return new DIDConnMessage();
        }
        else if(messageType.equalsIgnoreCase("issue-credential_actions") || messageType.equalsIgnoreCase("issue-credential_states")){
            return new IssueVCMessage();
        }
        else if(messageType.equalsIgnoreCase("present-proof_actions") || messageType.equalsIgnoreCase("present-proof_states")){
            return new SubmitVPMessage();
        }
        return null;
    }
}
