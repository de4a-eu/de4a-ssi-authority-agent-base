package um.si.de4a.resources;


import javax.ws.rs.*;

@Path("/request-vc")
public class RequestVCResource {

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    public int sendRequestPresentation(@QueryParam("did")String did, @QueryParam("format")String format) {

        return 1;
    }
}
