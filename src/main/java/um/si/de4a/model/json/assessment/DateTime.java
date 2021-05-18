package json.assessment;

import com.google.gson.annotations.SerializedName;

public class DateTime {
     @SerializedName("#dateTime")
    private String dateTime;

    public DateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
