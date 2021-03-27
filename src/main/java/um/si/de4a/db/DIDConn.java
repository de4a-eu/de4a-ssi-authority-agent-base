package um.si.de4a.db;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

public class DIDConn extends CouchDbDocument {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("_rev")
    private String revision;

    private String userId;

    private String myDID;

    private String theirDID;

    private String invitationId;

    private String invitationJSON;

    private String connectionId;

    private Status status;

    @TypeDiscriminator
    private String type;

    public DIDConn(String id, String revision, String userId, String myDID, String theirDID, String invitationId, String invitationJSON, String connectionId, Status status, String type) {
        this.id = id;
        this.revision = revision;
        this.userId = userId;
        this.myDID = myDID;
        this.theirDID = theirDID;
        this.invitationId = invitationId;
        this.invitationJSON = invitationJSON;
        this.connectionId = connectionId;
        this.status = status;
        this.type = type;
    }

    public DIDConn(String userId, String invitationId, String invitationJSON, Status status, String type) {
        this.userId = userId;
        this.invitationId = invitationId;
        this.invitationJSON = invitationJSON;
        this.status = status;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMyDID() {
        return myDID;
    }

    public void setMyDID(String myDID) {
        this.myDID = myDID;
    }

    public String getTheirDID() {
        return theirDID;
    }

    public void setTheirDID(String theirDID) {
        this.theirDID = theirDID;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getInvitationJSON() {
        return invitationJSON;
    }

    public void setInvitationJSON(String invitationJSON) {
        this.invitationJSON = invitationJSON;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}



enum Status {
    CONNECTION_ESTABLISHED("ConnectionEstablished"),
    INVITATION_GENERATED("InvitationGenerated"),
    INVITATION_ACCEPTED("InvitationAccepted"),
    INVITATION_REJECTED("InvitationRejected"),
    VC_ACCEPTED("VCAccepted"),
    VC_SENT("VCSent"),
    OFFER_ACCEPTED("OfferAccepted"),
    OFFER_NOT_SENT("OfferNotSent"),
    OFFER_REJECTED("OfferRejected"),
    VC_REJECTED("VCRejected");

    private final String text;

    /**
     * @param text
     */
    Status(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}



