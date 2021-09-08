package um.si.de4a.model.json.location;

import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.Title;

public class Location {
    @SerializedName("@id")
    private String id;

    private Title geographicName;

    private SpatialCode spatialCode;

    public Location(String id, Title geographicName, SpatialCode spatialCode) {
        this.id = id;
        this.geographicName = geographicName;
        this.spatialCode = spatialCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Title getGeographicName() {
        return geographicName;
    }

    public void setGeographicName(Title geographicName) {
        this.geographicName = geographicName;
    }

    public SpatialCode getSpatialCode() {
        return spatialCode;
    }

    public void setSpatialCode(SpatialCode spatialCode) {
        this.spatialCode = spatialCode;
    }
}
