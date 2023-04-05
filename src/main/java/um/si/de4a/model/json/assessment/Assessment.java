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
package um.si.de4a.model.json.assessment;

import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.SpecifiedBy;
import um.si.de4a.model.json.Title;

public class Assessment {
    @SerializedName("@id")
    private String id;

    private Title title;

    private SpecifiedBy specifiedBy;

    private IssuedDate issuedDate;

    public Assessment(String id, Title title, SpecifiedBy specifiedBy, IssuedDate issuedDate) {
        this.id = id;
        this.title = title;
        this.specifiedBy = specifiedBy;
        this.issuedDate = issuedDate;
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

    public SpecifiedBy getSpecifiedBy() {
        return specifiedBy;
    }

    public void setSpecifiedBy(SpecifiedBy specifiedBy) {
        this.specifiedBy = specifiedBy;
    }

    public IssuedDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(IssuedDate issuedDate) {
        this.issuedDate = issuedDate;
    }
}
