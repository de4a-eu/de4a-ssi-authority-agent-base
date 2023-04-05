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
