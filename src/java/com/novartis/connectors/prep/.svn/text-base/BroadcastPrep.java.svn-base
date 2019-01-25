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
import com.novartis.connectors.jaxb.BroadcastReq.Request;
import com.novartis.connectors.jaxb.BroadcastRes.Response;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXException;


public class BroadcastPrep {

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
                     .newSchema(BroadcastPrep.class.getClassLoader().getResource("com/novartis/connectors/schemas/BroadcastReq.xsd"));
            unmarshaller = requestContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            //read xml into object
            request = (Request) unmarshaller.unmarshal(new StringReader(xml));

            responseContext = JAXBContext.newInstance(Response.class);
            marshaller = responseContext.createMarshaller();

            //response object factory
            com.novartis.connectors.jaxb.BroadcastRes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.BroadcastRes.ObjectFactory();
            //response <broadcasts> object
            com.novartis.connectors.jaxb.BroadcastRes.Response.Broadcasts resBroadcasts
                                       = responseOF.createResponseBroadcasts();

            //request <broadcasts> object
            com.novartis.connectors.jaxb.BroadcastReq.Request.Broadcasts reqbroadcasts
                                        = request.getBroadcasts();

            //response Broadcast list
            List <Response.Broadcasts.Broadcast> resBroadcastList  = resBroadcasts.getBroadcast();

            //request Broadcast list
            List reqBroadcastList = reqbroadcasts.getBroadcast();

            //Process resquest (retrieve elements) and initialize response
            for(Iterator iter = reqBroadcastList.iterator();
                    iter.hasNext();) {
                Request.Broadcasts.Broadcast reqBroadcast = (Request.Broadcasts.Broadcast) iter.next();
                Response.Broadcasts.Broadcast broadcast = responseOF.createResponseBroadcastsBroadcast();
                broadcast.setBroadcastid(reqBroadcast.getBroadcastid());
                resBroadcastList.add(broadcast);
            }

            //create response object
            response = responseOF.createResponse();
            //initialize response object
            response.setBroadcasts(resBroadcasts);
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
            marshaller = responseContext.createMarshaller();
            //response object factory
            com.novartis.connectors.jaxb.BroadcastRes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.BroadcastRes.ObjectFactory();
            com.novartis.connectors.jaxb.BroadcastRes.Response.Broadcasts resBroadcasts
                                       = responseOF.createResponseBroadcasts();
            Response.Broadcasts.Broadcast broadcast = responseOF.createResponseBroadcastsBroadcast();
            resBroadcasts.getBroadcast().add(broadcast);
            Response errResponse =  responseOF.createResponse();
            response.setBroadcasts(resBroadcasts);
            response.setStatus(errtype);
            response.setStatusmessage(e.toString());

            responseContext = JAXBContext.newInstance(Response.class);
            responseWriter = new StringWriter();
            marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(errResponse, responseWriter);
            return responseWriter.toString();
    }

}

