package um.si.de4a.resources;


import org.jboss.resteasy.annotations.Query;

import javax.ws.rs.*;

@Path("/check-vp-status")
public class CheckVPStatusResource {

    @GET
    @Consumes("text/plain")
    @Produces("application/json")
    public String fetch(@QueryParam("vpid")String verPresID, @QueryParam("")String text) {

        return "";
    }
}
