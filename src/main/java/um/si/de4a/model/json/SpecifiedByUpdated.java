package um.si.de4a.model.json;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SpecifiedByUpdated {
    private String id;
    private String title;
    private String volumeOfLearning;
    private String[] iSCEDFCode;
    private float eCTSCreditPoints;

    public SpecifiedByUpdated(String id, String title, String volumeOfLearning, String[] iSCEDFCode, float eCTSCreditPoints) {
        this.id = id;
        this.title = title;
        this.volumeOfLearning = volumeOfLearning;
        this.iSCEDFCode = iSCEDFCode;
        this.eCTSCreditPoints = eCTSCreditPoints;
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

    public String getVolumeOfLearning() {
        return volumeOfLearning;
    }

    public void setVolumeOfLearning(String volumeOfLearning) {
        this.volumeOfLearning = volumeOfLearning;
    }

    public String[] getiSCEDFCode() {
        return iSCEDFCode;
    }

    public void setiSCEDFCode(String[] iSCEDFCode) {
        this.iSCEDFCode = iSCEDFCode;
    }

    public float geteCTSCreditPoints() {
        return eCTSCreditPoints;
    }

    public void seteCTSCreditPoints(float eCTSCreditPoints) {
        this.eCTSCreditPoints = eCTSCreditPoints;
    }
}
