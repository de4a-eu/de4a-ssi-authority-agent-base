package um.si.de4a.model.json;

import java.util.ArrayList;

public class PeriodOfTime {
    private String[] type = new String[]{"PeriodOfTime"};
    private String startedAtTime;
    private String endedAtTime;
    private ArrayList<Note> description = new ArrayList<>();

    public PeriodOfTime(String[] type, String startedAtTime, String endedAtTime, ArrayList<Note> description) {
        this.type = type;
        this.startedAtTime = startedAtTime;
        this.endedAtTime = endedAtTime;
        this.description = description;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String getStartedAtTime() {
        return startedAtTime;
    }

    public void setStartedAtTime(String startedAtTime) {
        this.startedAtTime = startedAtTime;
    }

    public String getEndedAtTime() {
        return endedAtTime;
    }

    public void setEndedAtTime(String endedAtTime) {
        this.endedAtTime = endedAtTime;
    }

    public ArrayList<Note> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<Note> description) {
        this.description = description;
    }
}
