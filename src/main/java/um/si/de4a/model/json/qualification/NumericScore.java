package json.qualification;

import com.google.gson.annotations.SerializedName;

public class NumericScore {
    @SerializedName("#numericScore")
    private String numericScore;

    public NumericScore(String numericScore) {
        this.numericScore = numericScore;
    }

    public String getNumericScore() {
        return numericScore;
    }

    public void setNumericScore(String numericScore) {
        this.numericScore = numericScore;
    }
}
