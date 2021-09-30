package um.si.de4a.resources.ebsi;


import org.json.simple.JSONObject;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;

import javax.ws.rs.*;
import java.io.IOException;

@Path("/get-did")
public class GetDIDEbsiResource {

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    public String getDID() throws IOException {
        DBUtil dbUtil = new DBUtil();
        String did = dbUtil.getDID();

        if(did != null)
            return did;
        return "";
    }
}
