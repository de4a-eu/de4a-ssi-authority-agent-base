package um.si.de4a.resources;


import javax.ws.rs.*;

@Path("/check-conn-status")
public class CheckConnStatusResource {

    @GET
    @Consumes("text/plain")
    @Produces("application/json")
    public String fetch(@QueryParam("did")String did) {

        return "";
    }
}
