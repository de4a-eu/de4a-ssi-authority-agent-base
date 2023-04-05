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
@XmlRootElement(name="HigherEducationDiploma", namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
public class HigherEducationDiploma {
    @XmlElement(namespace = "http://data.europa.eu/europass/model/credentials#", required = true)
    protected Title title;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private Degree degree;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private String country;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private InstitutionName institutionName;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private StudyProgramme studyProgramme;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private MainFieldOfStudy mainFieldOfStudy;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private String modeOfStudy;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private String durationOfEducation;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private String scope;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private String dateOfIssue;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private PlaceOfIssue placeOfIssue;

    @XmlElement(namespace = "urn:eu-de4a:xsd:CanonicalEvidenceType::HigherEducationEvidence:v1.0")
    private HolderOfAchievement holderOfAchievement;

    public HigherEducationDiploma() {
    }

    public HigherEducationDiploma(Title title, Degree degree, String country, InstitutionName institutionName, StudyProgramme studyProgramme, MainFieldOfStudy mainFieldOfStudy, String modeOfStudy, String durationOfEducation, String scope, String dateOfIssue, PlaceOfIssue placeOfIssue, HolderOfAchievement holderOfAchievement) {
        this.title = title;
        this.degree = degree;
        this.country = country;
        this.institutionName = institutionName;
        this.studyProgramme = studyProgramme;
        this.mainFieldOfStudy = mainFieldOfStudy;
        this.modeOfStudy = modeOfStudy;
        this.durationOfEducation = durationOfEducation;
        this.scope = scope;
        this.dateOfIssue = dateOfIssue;
        this.placeOfIssue = placeOfIssue;
        this.holderOfAchievement = holderOfAchievement;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public InstitutionName getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(InstitutionName institutionName) {
        this.institutionName = institutionName;
    }

    public StudyProgramme getStudyProgramme() {
        return studyProgramme;
    }

    public void setStudyProgramme(StudyProgramme studyProgramme) {
        this.studyProgramme = studyProgramme;
    }

    public MainFieldOfStudy getMainFieldOfStudy() {
        return mainFieldOfStudy;
    }

    public void setMainFieldOfStudy(MainFieldOfStudy mainFieldOfStudy) {
        this.mainFieldOfStudy = mainFieldOfStudy;
    }

    public String getModeOfStudy() {
        return modeOfStudy;
    }

    public void setModeOfStudy(String modeOfStudy) {
        this.modeOfStudy = modeOfStudy;
    }

    public String getDurationOfEducation() {
        return durationOfEducation;
    }

    public void setDurationOfEducation(String durationOfEducation) {
        this.durationOfEducation = durationOfEducation;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public PlaceOfIssue getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(PlaceOfIssue placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public HolderOfAchievement getHolderOfAchievement() {
        return holderOfAchievement;
    }

    public void setHolderOfAchievement(HolderOfAchievement holderOfAchievement) {
        this.holderOfAchievement = holderOfAchievement;
    }
}
