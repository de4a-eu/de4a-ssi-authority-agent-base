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

public class CredentialSubject {
    private String currentFamilyName;
    private String currentGivenName;
    private String dateOfBirth;
    private String personIdentifier;
    private ArrayList<LearningAchievement> achieved;

    public CredentialSubject(String currentFamilyName, String currentGivenName, String dateOfBirth, String personIdentifier, ArrayList<LearningAchievement> achieved) {
        this.currentFamilyName = currentFamilyName;
        this.currentGivenName = currentGivenName;
        this.dateOfBirth = dateOfBirth;
        this.personIdentifier = personIdentifier;
        this.achieved = achieved;
    }

    public String getCurrentFamilyName() {
        return currentFamilyName;
    }

    public void setCurrentFamilyName(String currentFamilyName) {
        this.currentFamilyName = currentFamilyName;
    }

    public String getCurrentGivenName() {
        return currentGivenName;
    }

    public void setCurrentGivenName(String currentGivenName) {
        this.currentGivenName = currentGivenName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPersonIdentifier() {
        return personIdentifier;
    }

    public void setPersonIdentifier(String personIdentifier) {
        this.personIdentifier = personIdentifier;
    }

    public ArrayList<LearningAchievement> getAchieved() {
        return achieved;
    }

    public void setAchieved(ArrayList<LearningAchievement> achieved) {
        this.achieved = achieved;
    }
}
