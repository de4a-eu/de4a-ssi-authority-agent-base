package um.si.de4a.resources.vp;

public class DataObj {
    private JsonObj json;

    public DataObj(JsonObj json) {
        this.json = json;
    }

    public JsonObj getJson() {
        return json;
    }

    public void setJson(JsonObj json) {
        this.json = json;
    }
}
