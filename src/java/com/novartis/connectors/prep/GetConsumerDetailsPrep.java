package com.novartis.connectors.prep;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import com.novartis.connectors.jaxb.GetConsumerDetailsReq.Request;
import com.novartis.connectors.jaxb.GetConsumerDetailsRes.Response;
import java.math.BigInteger;
import org.xml.sax.SAXException;

/**
 *
 * @author Andrew Daawin
 */
public class GetConsumerDetailsPrep {

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
                     .newSchema(GetConsumerDetailsPrep.class.getClassLoader().getResource("com/novartis/connectors/schemas/GetConsumerDetailsReq.xsd"));
            unmarshaller = requestContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            //read xml into object
            request = (Request) unmarshaller.unmarshal(new StringReader(xml));

            responseContext = JAXBContext.newInstance(Response.class);
            marshaller = responseContext.createMarshaller();

            //request <> object
            com.novartis.connectors.jaxb.GetConsumerDetailsReq.Request.Consumer reqConsumer
                                        = request.getConsumer();

            //response object factory
            com.novartis.connectors.jaxb.GetConsumerDetailsRes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.GetConsumerDetailsRes.ObjectFactory();
            //response <> object
            com.novartis.connectors.jaxb.GetConsumerDetailsRes.Response.Consumer resConsumer
                                       = responseOF.createResponseConsumer();

            com.novartis.connectors.jaxb.GetConsumerDetailsRes.Response.Consumer.Geofences geofences
                    = responseOF.createResponseConsumerGeofences();

             com.novartis.connectors.jaxb.GetConsumerDetailsRes.Response.Consumer.Geofences.Geofence geofence
                     = responseOF.createResponseConsumerGeofencesGeofence();

             resConsumer.setConsumerid(reqConsumer.getConsumerid());
             resConsumer.setLat(123.44);
             resConsumer.setLon(99.223);
             geofence.setCampaignid(reqConsumer.getCampaignid());
             geofence.setCampaignid(reqConsumer.getCampaignid());
             geofence.setGeofenceid(BigInteger.ZERO);
             geofence.setLat(123.44);
             geofence.setLon(99.223);
             geofence.setPriority(BigInteger.ONE);
             geofence.setGeofencename("Accra");

             geofences.setGeofence(geofence);
             resConsumer.setGeofences(geofences);
            //create response object
            response = responseOF.createResponse();
            //initialize response object
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
            com.novartis.connectors.jaxb.GetConsumerDetailsRes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.GetConsumerDetailsRes.ObjectFactory();
            
            com.novartis.connectors.jaxb.GetConsumerDetailsRes.Response.Consumer resConsumer
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
