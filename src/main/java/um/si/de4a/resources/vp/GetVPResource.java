package um.si.de4a.resources.vp;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VPStatus;
import um.si.de4a.db.VPStatusEnum;

import javax.ws.rs.*;
import java.io.IOException;

@Path("/get-vp")
public class GetVPResource {

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{vpName}")
    public String getVP(@PathParam("vpName") String vpName) throws IOException {
        JSONObject vp = new JSONObject();
        AriesUtil ariesUtil = new AriesUtil();
        System.out.println("[GEt-VP] VPName: " + vpName);
        vp = ariesUtil.getPresentation(vpName);

        if(vp != null)
            return vp.toJSONString();
        return "";
    }
}
