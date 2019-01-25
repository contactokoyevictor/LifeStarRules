package com.novartis.connectors.prep;
/**
 *
 * @author Andrew Daawin
 */
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import com.novartis.connectors.jaxb.SetConsumerDetailsReq.Request;
import com.novartis.connectors.jaxb.SetConsumerDetailsRes.Response;
import org.xml.sax.SAXException;

public class SetConsumerDetailsPrep {
    private JAXBContext requestContext;
    private JAXBContext responseContext;
    private Unmarshaller unmarshaller;
    private Marshaller marshaller;
    private Schema schema;
    private Response response;
    private Request request;
    private StringWriter responseWriter;

     public String processXML(String xml) throws PropertyException, JAXBException {


        try{
            requestContext = JAXBContext.newInstance(Request.class);
            schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                     .newSchema(SetConsumerDetailsPrep.class.getClassLoader().getResource("com/novartis/connectors/schemas/SetConsumerDetailsReq.xsd"));
            unmarshaller = requestContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            //read xml into object
            request = (Request) unmarshaller.unmarshal(new StringReader(xml));

            responseContext = JAXBContext.newInstance(Response.class);
            marshaller = responseContext.createMarshaller();

            //request <> object
            com.novartis.connectors.jaxb.SetConsumerDetailsReq.Request.Consumer reqConsumer
                                        = request.getConsumer();

            //response object factory
            com.novartis.connectors.jaxb.SetConsumerDetailsRes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.SetConsumerDetailsRes.ObjectFactory();
            //response <> object
            com.novartis.connectors.jaxb.SetConsumerDetailsRes.Response.Consumer resConsumer
                                       = responseOF.createResponseConsumer();

            //create response object
            resConsumer.setConsumerid(reqConsumer.getConsumerid());
            response = responseOF.createResponse();
            response.setConsumer(resConsumer);
            response.setStatus("ok");
            response.setStatusmessage("OK");


            responseWriter = new StringWriter();
            marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(response, responseWriter);

        }catch(JAXBException e){
            return generateErrorResponse("FAIL: ", e);
        }
        catch(SAXException e){
            return generateErrorResponse("ERROR: ", e);
        }
        return  responseWriter.toString();
    }

    private String generateErrorResponse(String errtype, Exception e) throws PropertyException, JAXBException
    {

            responseContext = JAXBContext.newInstance(Response.class);
            marshaller = responseContext.createMarshaller();
            //response object factory
            com.novartis.connectors.jaxb.SetConsumerDetailsRes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.SetConsumerDetailsRes.ObjectFactory();

            com.novartis.connectors.jaxb.SetConsumerDetailsRes.Response.Consumer resConsumer
                                       = responseOF.createResponseConsumer();

            Response errResponse =  responseOF.createResponse();
            errResponse.setConsumer(resConsumer);
            errResponse.setStatus(errtype);
            errResponse.setStatusmessage(e.toString());
            responseWriter = new StringWriter();
            marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(errResponse, responseWriter);
            return responseWriter.toString();
    }


}
