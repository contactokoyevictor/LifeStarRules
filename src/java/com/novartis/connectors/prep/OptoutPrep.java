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
import com.novartis.connectors.jaxb.OptoutReq.Request;
import com.novartis.connectors.jaxb.OptoutRes.Response;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXException;

public class OptoutPrep {

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
                     .newSchema(BroadcastPrep.class.getClassLoader().getResource("com/novartis/connectors/schemas/OptoutReq.xsd"));
            unmarshaller = requestContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            //read xml into object
            request = (Request) unmarshaller.unmarshal(new StringReader(xml));

            responseContext = JAXBContext.newInstance(Response.class);
            marshaller = responseContext.createMarshaller();

            //response object factory
            com.novartis.connectors.jaxb.OptoutRes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.OptoutRes.ObjectFactory();
            //response <optouts> object
            com.novartis.connectors.jaxb.OptoutRes.Response.Optouts resOptouts
                                       = responseOF.createResponseOptouts();

            //request <optouts> object
            com.novartis.connectors.jaxb.OptoutReq.Request.Optouts reqOptouts
                                        = request.getOptouts();

            //response consumer list
            List <Response.Optouts.Consumer> resConsumerList  = resOptouts.getConsumer();

            //request consumer list
            List <Request.Optouts.Consumer> reqConsumerList = reqOptouts.getConsumer();

            //Process resquest (retrieve elements) and initialize response
            for(Iterator iter = reqConsumerList.iterator();
                    iter.hasNext();) {
                Request.Optouts.Consumer reqConsumer = (Request.Optouts.Consumer) iter.next();
                Response.Optouts.Consumer resConsumer = responseOF.createResponseOptoutsConsumer();
                resConsumer.setConsumerid(reqConsumer.getConsumerid());
                resConsumerList.add(resConsumer);
            }

            //create response object
            response = responseOF.createResponse();
            //initialize response object
            response.setOptouts(resOptouts);
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

            //response object factory
            com.novartis.connectors.jaxb.OptoutRes.ObjectFactory responseOF =
                                        new com.novartis.connectors.jaxb.OptoutRes.ObjectFactory();

            com.novartis.connectors.jaxb.OptoutRes.Response.Optouts  resOptouts
                                       = responseOF.createResponseOptouts();
            Response.Optouts.Consumer resConsumer = responseOF.createResponseOptoutsConsumer();

            resOptouts.getConsumer().add(resConsumer);
            Response errResponse =  responseOF.createResponse();
            errResponse.setOptouts(resOptouts);
            errResponse.setStatus(errtype);
            errResponse.setStatusmessage(e.toString());
            responseContext = JAXBContext.newInstance(Response.class);
            marshaller = responseContext.createMarshaller();
            responseWriter = new StringWriter();
            marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(errResponse, responseWriter);
            return responseWriter.toString();
    }


}
