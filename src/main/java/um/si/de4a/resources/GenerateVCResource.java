package um.si.de4a.resources;

import um.si.de4a.model.HEEvidenceTypeXML;

import javax.ws.rs.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@Path("/generate-vc")
public class GenerateVCResource {
    @POST
    @Consumes({"text/plain","application/xml"})
    @Produces("application/xml")
    public String generateVC(@QueryParam("did")String did, @QueryParam("evidence") String evidence) {
        String result = "";

        JAXBContext context;

        try {
            context = JAXBContext.newInstance(HEEvidenceTypeXML.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();

            HEEvidenceTypeXML modelEvidence = (HEEvidenceTypeXML) jaxbUnmarshaller.unmarshal(new StringReader(evidence));
            result = modelEvidence.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        /*try {
            Document doc = XMLConverter.toXmlDocument(evidence);

            NodeList xmlNodes = doc.getElementsByTagName("xsd:element");

            HEEvidenceType evidenceType = new HEEvidenceType(
                    xmlNodes.item(1).getNodeValue(),
                    xmlNodes.item(2).getNodeValue(),
                    xmlNodes.item(3).getNodeValue(),
                    xmlNodes.item(4).getNodeValue(),
                    xmlNodes.item(5).getNodeValue(),
                    xmlNodes.item(6).getNodeValue(),
                    xmlNodes.item(7).getNodeValue(),
                    xmlNodes.item(8).getNodeValue(),
                    xmlNodes.item(9).getNodeValue(),
                    xmlNodes.item(10).getNodeValue(),
                    xmlNodes.item(11).getNodeValue());
            result = evidenceType.toString();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return result;
    }

}