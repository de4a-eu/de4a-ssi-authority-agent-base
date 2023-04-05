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
package um.si.de4a.resources.vp;

public class ValidationObj {
    private int subjectCheck;
    private int schemaCheck;
    private int issuerCheck;
    private int signatureCheck;
    private String vpName;

    public ValidationObj(int subjectCheck, int schemaCheck, int issuerCheck, int signatureCheck, String vpName) {
        this.subjectCheck = subjectCheck;
        this.schemaCheck = schemaCheck;
        this.issuerCheck = issuerCheck;
        this.signatureCheck = signatureCheck;
        this.vpName = vpName;
    }

    public int getSubjectCheck() {
        return subjectCheck;
    }

    public void setSubjectCheck(int subjectCheck) {
        this.subjectCheck = subjectCheck;
    }

    public int getSchemaCheck() {
        return schemaCheck;
    }

    public void setSchemaCheck(int schemaCheck) {
        this.schemaCheck = schemaCheck;
    }

    public int getIssuerCheck() {
        return issuerCheck;
    }

    public void setIssuerCheck(int issuerCheck) {
        this.issuerCheck = issuerCheck;
    }

    public int getSignatureCheck() {
        return signatureCheck;
    }

    public void setSignatureCheck(int signatureCheck) {
        this.signatureCheck = signatureCheck;
    }

    public String getVpName() {
        return vpName;
    }

    public void setVpName(String vpName) {
        this.vpName = vpName;
    }
}
