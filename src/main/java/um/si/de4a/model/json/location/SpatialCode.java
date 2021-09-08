package um.si.de4a.model.json.location;

import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.Title;

public class SpatialCode {
    @SerializedName("@targetFrameworkUrl")
    private String targetFrameworkUrl;

    @SerializedName("@targetNotation")
    private String targetNotation;

    @SerializedName("@uri")
    private String uri;

    private Title targetFrameworkName;

    private Title targetName;

    public SpatialCode(String targetFrameworkUrl, String targetNotation, String uri, Title targetFrameworkName, Title targetName) {
        this.targetFrameworkUrl = targetFrameworkUrl;
        this.targetNotation = targetNotation;
        this.uri = uri;
        this.targetFrameworkName = targetFrameworkName;
        this.targetName = targetName;
    }

    public String getTargetFrameworkUrl() {
        return targetFrameworkUrl;
    }

    public void setTargetFrameworkUrl(String targetFrameworkUrl) {
        this.targetFrameworkUrl = targetFrameworkUrl;
    }

    public String getTargetNotation() {
        return targetNotation;
    }

    public void setTargetNotation(String targetNotation) {
        this.targetNotation = targetNotation;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Title getTargetFrameworkName() {
        return targetFrameworkName;
    }

    public void setTargetFrameworkName(Title targetFrameworkName) {
        this.targetFrameworkName = targetFrameworkName;
    }

    public Title getTargetName() {
        return targetName;
    }

    public void setTargetName(Title targetName) {
        this.targetName = targetName;
    }
}
