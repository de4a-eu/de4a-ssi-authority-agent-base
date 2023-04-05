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
package um.si.de4a.model.json.location;

import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.Title;

public class Location {
    @SerializedName("@id")
    private String id;

    private Title geographicName;

    private SpatialCode spatialCode;

    public Location(String id, Title geographicName, SpatialCode spatialCode) {
        this.id = id;
        this.geographicName = geographicName;
        this.spatialCode = spatialCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Title getGeographicName() {
        return geographicName;
    }

    public void setGeographicName(Title geographicName) {
        this.geographicName = geographicName;
    }

    public SpatialCode getSpatialCode() {
        return spatialCode;
    }

    public void setSpatialCode(SpatialCode spatialCode) {
        this.spatialCode = spatialCode;
    }
}
