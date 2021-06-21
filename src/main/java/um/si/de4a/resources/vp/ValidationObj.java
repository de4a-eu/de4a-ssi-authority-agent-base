package um.si.de4a.resources.vp;

public class ValidationObj {
    private int subjectCheck;
    private int schemaCheck;
    private int issuerCheck;
    private String vpName;

    public ValidationObj(int subjectCheck, int schemaCheck, int issuerCheck, String vpName) {
        this.subjectCheck = subjectCheck;
        this.schemaCheck = schemaCheck;
        this.issuerCheck = issuerCheck;
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

    public String getVpName() {
        return vpName;
    }

    public void setVpName(String vpName) {
        this.vpName = vpName;
    }
}
