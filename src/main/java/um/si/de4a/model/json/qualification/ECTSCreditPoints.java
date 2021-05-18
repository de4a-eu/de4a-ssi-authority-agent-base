package json.qualification;

public class ECTSCreditPoints {
    private NumericScore numericScore;

    public ECTSCreditPoints(NumericScore numericScore) {
        this.numericScore = numericScore;
    }

    public NumericScore getNumericScore() {
        return numericScore;
    }

    public void setNumericScore(NumericScore numericScore) {
        this.numericScore = numericScore;
    }
}
