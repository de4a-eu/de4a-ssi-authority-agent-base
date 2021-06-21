package um.si.de4a.resources.vp;

import com.google.gson.annotations.SerializedName;


public class RequestPresentationAttachObj {
    private DataObj data;

    public RequestPresentationAttachObj(DataObj data) {
        this.data = data;
    }

    public DataObj getData() {
        return data;
    }

    public void setData(DataObj data) {
        this.data = data;
    }
}
