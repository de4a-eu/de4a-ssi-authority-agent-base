package um.si.de4a.model.json;

import java.util.ArrayList;

public class GradeDetails {
    private String[] type = new String[]{"GradeDetails"};
    private NumericScore score; // change to factory based on score type
    private ArrayList<Note> additionalNote = new ArrayList<>();

    public GradeDetails(String[] type, NumericScore score, ArrayList<Note> additionalNote) {
        this.type = type;
        this.score = score;
        this.additionalNote = additionalNote;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public NumericScore getScore() {
        return score;
    }

    public void setScore(NumericScore score) {
        this.score = score;
    }

    public ArrayList<Note> getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(ArrayList<Note> additionalNote) {
        this.additionalNote = additionalNote;
    }
}

class NumericScore {
    private String[] type = new String[]{"NumericScore"};
    private Value value;
    private IDFieldType scoringScheme;
}

class TextScore {
    private String[] type = new String[]{"TextScore"};
    private Value value;
    private IDFieldType scoringScheme;
}




