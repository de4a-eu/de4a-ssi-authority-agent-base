package json;

import com.google.gson.annotations.SerializedName;

public class Text {
    @SerializedName("@content-type")
    private String contentType;

    @SerializedName("@lang")
    private String lang;

    @SerializedName("#text")
    private String text;

    public Text(String contentType, String lang, String text) {
        this.contentType = contentType;
        this.lang = lang;
        this.text = text;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
