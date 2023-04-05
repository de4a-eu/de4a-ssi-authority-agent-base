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

public class WasAwardedBy {

    private String id;
    private String[] awardingBody;
    private String awardingDate;
    private String[] awardingLocation;

    public WasAwardedBy(String id, String[] awardingBody, String awardingDate, String[] awardingLocation) {
        this.id = id;
        this.awardingBody = awardingBody;
        this.awardingDate = awardingDate;
        this.awardingLocation = awardingLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getAwardingBody() {
        return awardingBody;
    }

    public void setAwardingBody(String[] awardingBody) {
        this.awardingBody = awardingBody;
    }

    public String getAwardingDate() {
        return awardingDate;
    }

    public void setAwardingDate(String awardingDate) {
        this.awardingDate = awardingDate;
    }

    public String[] getAwardingLocation() {
        return awardingLocation;
    }

    public void setAwardingLocation(String[] awardingLocation) {
        this.awardingLocation = awardingLocation;
    }
}
