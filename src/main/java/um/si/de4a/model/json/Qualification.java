package json;

import com.google.gson.annotations.SerializedName;
import json.qualification.ECTSCreditPoints;
import json.qualification.ISCEDFCode;
import json.qualification.VolumeOfLearning;

public class Qualification {
    @SerializedName("@id")
    private String id;
    private Title title;
    private VolumeOfLearning volumeOfLearning;
    private json.qualification.ISCEDFCode ISCEDFCode;
    private json.qualification.ECTSCreditPoints ECTSCreditPoints;

    public Qualification(String id, Title title, VolumeOfLearning volumeOfLearning, ISCEDFCode ISCEDFCode, ECTSCreditPoints ECTSCreditPoints) {
        this.id = id;
        this.title = title;
        this.volumeOfLearning = volumeOfLearning;
        this.ISCEDFCode = ISCEDFCode;
        this.ECTSCreditPoints = ECTSCreditPoints;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public VolumeOfLearning getVolumeOfLearning() {
        return volumeOfLearning;
    }

    public void setVolumeOfLearning(VolumeOfLearning volumeOfLearning) {
        this.volumeOfLearning = volumeOfLearning;
    }

    public ISCEDFCode getISCEDFCode() {
        return ISCEDFCode;
    }

    public void setISCEDFCode(ISCEDFCode ISCEDFCode) {
        this.ISCEDFCode = ISCEDFCode;
    }

    public ECTSCreditPoints getECTSCreditPoints() {
        return ECTSCreditPoints;
    }

    public void setECTSCreditPoints(ECTSCreditPoints ECTSCreditPoints) {
        this.ECTSCreditPoints = ECTSCreditPoints;
    }
}
