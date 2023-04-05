/*
 * Copyright (C) 2023, Partners of the EU funded DE4A project consortium
 *   (https://www.de4a.eu/consortium), under Grant Agreement No.870635
 * Author: University of Maribor (UM)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package um.si.de4a.model.json;

import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.qualification.ECTSCreditPoints;
import um.si.de4a.model.json.qualification.ISCEDFCode;
import um.si.de4a.model.json.qualification.VolumeOfLearning;

public class Qualification {
    @SerializedName("@id")
    private String id;
    private Title title;
    private VolumeOfLearning volumeOfLearning;
    private um.si.de4a.model.json.qualification.ISCEDFCode ISCEDFCode;
    private um.si.de4a.model.json.qualification.ECTSCreditPoints ECTSCreditPoints;

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
