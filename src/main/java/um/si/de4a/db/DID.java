package um.si.de4a.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

public class DID extends CouchDbDocument {
    @JsonProperty("_id")
    @JsonIgnore
    private String id;

    @JsonProperty("_rev")
    @JsonIgnore
    private String revision;

    private String value;

    private long timeCreated;
    private long timeUntil;


    @TypeDiscriminator
    private String type;

    public DID(){}

    public DID(String id, String revision, String value, String type, long timeCreated, long timeUpdated){
        this.id = id;
        this.revision = revision;
        this.value = value;
        this.type = type;
        this.timeCreated = timeCreated;
        this.timeUntil = timeUpdated;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getTimeUntil() {
        return timeUntil;
    }

    public void setTimeUntil(long timeUntil) {
        this.timeUntil = timeUntil;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
