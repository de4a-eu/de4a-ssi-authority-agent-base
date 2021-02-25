package um.si.de4a.resources;


import javax.ws.rs.*;

@Path("/present-vp")
public class PresentVPDataResource {

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    public String presentHumanReadable(@QueryParam("vpid")String verPresID) {

        return "";
    }
}
