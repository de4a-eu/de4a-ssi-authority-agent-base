package um.si.de4a.model.webhook;

import java.io.Serializable;

public class SocketEvent implements Serializable {

    public String protocol;

    public String userID;

    public String uniqueIdentifier;

    public int statusCode;

    public SocketEvent(){}

    public SocketEvent(String protocol, String userID, String uniqueIdentifier, int statusCode) {
        this.protocol = protocol;
        this.userID = userID;
        this.uniqueIdentifier = uniqueIdentifier;
        this.statusCode = statusCode;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
