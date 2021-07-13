package um.si.de4a.resources.vp;

public class DataObj {
    /*private JsonObj json;

    public DataObj(JsonObj json) {
        this.json = json;
    }

    public JsonObj getJson() {
        return json;
    }

    public void setJson(JsonObj json) {
        this.json = json;
    }
    */
    private String base64;

    public DataObj(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
