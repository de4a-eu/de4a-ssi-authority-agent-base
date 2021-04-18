package um.si.de4a.db;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

@JsonIgnoreProperties({"id", "revision"})
public class VPStatus extends CouchDbDocument {
    @JsonProperty("_id")
    @Ignore
    private String id;

    @JsonProperty("_rev")
    @Ignore
    private String revision;

    private String userId;

    private String piid;

    private String vp;

    private DIDConn didConn;

    private VPStatusEnum VPStatusEnum;

    private String vpName;

    private long timeUpdated;

    @TypeDiscriminator
    private String type;

    public VPStatus() {
    }

    public VPStatus(String id, String revision, String userId, String piid, String vp, DIDConn didConn, um.si.de4a.db.VPStatusEnum VPStatusEnum, String vpName, long timeUpdated, String type) {
        this.id = id;
        this.revision = revision;
        this.userId = userId;
        this.piid = piid;
        this.vp = vp;
        this.didConn = didConn;
        this.VPStatusEnum = VPStatusEnum;
        this.vpName = vpName;
        this.timeUpdated = timeUpdated;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getRevision() {
        return revision;
    }

    @Override
    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPiid() {
        return piid;
    }

    public void setPiid(String piid) {
        this.piid = piid;
    }

    public DIDConn getDidConn() {
        return didConn;
    }

    public void setDidConn(DIDConn didConn) {
        this.didConn = didConn;
    }

    public long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVp() {
        return vp;
    }

    public void setVp(String vp) {
        this.vp = vp;
    }

    public um.si.de4a.db.VPStatusEnum getVPStatusEnum() {
        return VPStatusEnum;
    }

    public void setVPStatusEnum(um.si.de4a.db.VPStatusEnum VPStatusEnum) {
        this.VPStatusEnum = VPStatusEnum;
    }

    public String getVpName() {
        return vpName;
    }

    public void setVpName(String vpName) {
        this.vpName = vpName;
    }
}




