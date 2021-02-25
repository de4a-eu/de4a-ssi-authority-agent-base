package um.si.de4a.resources;


import javax.ws.rs.*;

@Path("/validate-vp")
public class ValidateVPResource {

    @GET
    @Consumes("application/json")
    @Produces("text/plain")
    public boolean validateSemantics(@QueryParam("vp")String verPresentation) {

        return true;
    }
}
