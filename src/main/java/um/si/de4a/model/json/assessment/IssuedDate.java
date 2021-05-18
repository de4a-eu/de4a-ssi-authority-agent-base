package json.assessment;

public class IssuedDate {
    private DateTime dateTime;

    public IssuedDate(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
