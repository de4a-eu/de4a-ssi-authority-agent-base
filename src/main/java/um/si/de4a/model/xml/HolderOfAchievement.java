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
package um.si.de4a.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="holderOfAchievement", namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
public class HolderOfAchievement {
    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#")
    private String nationalId;

    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#")
    private GivenNames givenNames;

    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#")
    private FamilyName familyName;

    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#")
    private String dateOfBirth;

    public HolderOfAchievement() {
    }

    public HolderOfAchievement(String nationalId, GivenNames givenNames, FamilyName familyName, String dateOfBirth) {
        this.nationalId = nationalId;
        this.givenNames = givenNames;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
    }


    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public GivenNames getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(GivenNames givenNames) {
        this.givenNames = givenNames;
    }

    public FamilyName getFamilyName() {
        return familyName;
    }

    public void setFamilyName(FamilyName familyName) {
        this.familyName = familyName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
