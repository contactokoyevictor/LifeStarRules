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
import com.novartis.connectors.jaxb.OptinReq.Request;
import com.novartis.connectors.jaxb.OptinRes.Response;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXException;

public class OptinPrep {

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
                     .newSchema(BroadcastPrep.class.getClassLoader().getResource("com/novartis/connectors/schemas/OptinReq.xsd"));
            unmarshaller = requestContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            //read xml into object
            request = (Request) unmarshaller.unmarshal(new StringReader(xml));

            responseContext = JAXBContext.newInstance(Response.class);
            marshaller = responseContext.createMarshaller();

            //response object factory
            com.novartis.connectors.jaxb.OptinRes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.OptinRes.ObjectFactory();
            //response <optins> object
            com.novartis.connectors.jaxb.OptinRes.Response.Optins resOptins
                                       = responseOF.createResponseOptins();

            //request <optins> object
            com.novartis.connectors.jaxb.OptinReq.Request.Optins reqOptins
                                        = request.getOptins();

            //response consumer list
            List <Response.Optins.Consumer> resConsumerList  = resOptins.getConsumer();

            //request consumer list
            List <Request.Optins.Consumer> reqConsumerList = reqOptins.getConsumer();

            //Process resquest (retrieve elements) and initialize response
            for(Iterator iter = reqConsumerList.iterator();
                    iter.hasNext();) {
                Request.Optins.Consumer reqConsumer = (Request.Optins.Consumer) iter.next();
                Response.Optins.Consumer resConsumer = responseOF.createResponseOptinsConsumer();
                resConsumer.setConsumerid(reqConsumer.getConsumerid());
                resConsumerList.add(resConsumer);
            }

            //create response object
            response = responseOF.createResponse();
            //initialize response object
            response.setOptins(resOptins);
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
            com.novartis.connectors.jaxb.OptinRes.ObjectFactory responseOF =
                                        new com.novartis.connectors.jaxb.OptinRes.ObjectFactory();

            com.novartis.connectors.jaxb.OptinRes.Response.Optins  resOptins
                                       = responseOF.createResponseOptins();
            Response.Optins.Consumer resConsumer = responseOF.createResponseOptinsConsumer();

            resOptins.getConsumer().add(resConsumer);
            Response errResponse =  responseOF.createResponse();
            errResponse.setOptins(resOptins);
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
