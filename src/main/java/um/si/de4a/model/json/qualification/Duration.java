package json.qualification;

import com.google.gson.annotations.SerializedName;

public class Duration {
    @SerializedName("#duration")
    private String duration;

    public Duration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
