package um.si.de4a.model.json;

import com.google.gson.annotations.SerializedName;

public class WasDerivedFromUpdated {
    private String id;
    private String title;
    private String grade;
    private String issuedDate;

    public WasDerivedFromUpdated(String id, String title, String grade, String issuedDate) {
        this.id = id;
        this.title = title;
        this.grade = grade;
        this.issuedDate = issuedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }
}
