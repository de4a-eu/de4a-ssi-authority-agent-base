package um.si.de4a.db;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

@JsonIgnoreProperties({"id", "revision"})
public class DIDConn extends CouchDbDocument {
    @JsonProperty("_id")
    @Ignore
    private String id;

    @JsonProperty("_rev")
    @Ignore
    private String revision;

    private String userId;

    private String myDID;

    private String theirDID;

    private String invitationId;

    private String invitationJSON;

    private String connectionId;

    private VCStatus VCStatus;

    @TypeDiscriminator
    private String type;

    public DIDConn() {
    }

    public DIDConn(String id, String revision, String userId, String myDID, String theirDID, String invitationId, String invitationJSON, String connectionId, VCStatus VCStatus, String type) {
        this.id = id;
        this.revision = revision;
        this.userId = userId;
        this.myDID = myDID;
        this.theirDID = theirDID;
        this.invitationId = invitationId;
        this.invitationJSON = invitationJSON;
        this.connectionId = connectionId;
        this.VCStatus = VCStatus;
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

    public VCStatus getStatus() {
        return VCStatus;
    }

    public void setStatus(VCStatus VCStatus) {
        this.VCStatus = VCStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}




