package um.si.de4a.resources.offer;

import com.google.gson.annotations.SerializedName;

public class Attribute {
    @SerializedName("mime-type")
    private String mimeType;

    private String name;

    private String value;

    public Attribute(String mimeType, String name, String value) {
        this.mimeType = mimeType;
        this.name = name;
        this.value = value;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
