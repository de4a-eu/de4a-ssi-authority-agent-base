package um.si.de4a;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import um.si.de4a.util.XMLConverter;

import javax.ws.rs.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringReader;

@Path("/generate-vc")
public class GenerateVCResource {
    @POST
    @Consumes({"text/plain","application/xml"})
    @Produces("text/plain")
    public String generateVC(@QueryParam("did")String did, @QueryParam("evidence") String evidence) {
        try {
            Document doc = XMLConverter.toXmlDocument(evidence);

            if(doc.getChildNodes().getLength() != 0 )
                return XMLConverter.toXmlString(doc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return "";
    }

}