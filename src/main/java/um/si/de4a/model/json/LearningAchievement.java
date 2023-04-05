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

import java.util.ArrayList;

public class LearningAchievement {
    private String id;
    private String title;
    private WasAwardedBy wasAwardedBy;
    private ArrayList<SpecifiedByUpdated> specifiedBy;
    private ArrayList<WasDerivedFrom> wasDerivedFrom;
    private String associatedLearningOpportunity;

    public LearningAchievement(String id, String title, WasAwardedBy wasAwardedBy, ArrayList<SpecifiedByUpdated> specifiedBy, ArrayList<WasDerivedFrom> wasDerivedFrom, String associatedLearningOpportunity) {
        this.id = id;
        this.title = title;
        this.wasAwardedBy = wasAwardedBy;
        this.specifiedBy = specifiedBy;
        this.wasDerivedFrom = wasDerivedFrom;
        this.associatedLearningOpportunity = associatedLearningOpportunity;
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

    public WasAwardedBy getWasAwardedBy() {
        return wasAwardedBy;
    }

    public void setWasAwardedBy(WasAwardedBy wasAwardedBy) {
        this.wasAwardedBy = wasAwardedBy;
    }

    public ArrayList<SpecifiedByUpdated> getSpecifiedBy() {
        return specifiedBy;
    }

    public void setSpecifiedBy(ArrayList<SpecifiedByUpdated> specifiedBy) {
        this.specifiedBy = specifiedBy;
    }

    public ArrayList<WasDerivedFrom> getWasDerivedFrom() {
        return wasDerivedFrom;
    }

    public void setWasDerivedFrom(ArrayList<WasDerivedFrom> wasDerivedFrom) {
        this.wasDerivedFrom = wasDerivedFrom;
    }

    public String getAssociatedLearningOpportunity() {
        return associatedLearningOpportunity;
    }

    public void setAssociatedLearningOpportunity(String associatedLearningOpportunity) {
        this.associatedLearningOpportunity = associatedLearningOpportunity;
    }
}
