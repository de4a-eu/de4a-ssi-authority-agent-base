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
