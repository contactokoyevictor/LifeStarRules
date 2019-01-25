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
import com.novartis.connectors.jaxb.MTReq.Request;
import com.novartis.connectors.jaxb.MTRes.Response;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXException;

public class MTPrep {
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
                     .newSchema(BroadcastPrep.class.getClassLoader().getResource("com/novartis/connectors/schemas/MTReq.xsd"));
            unmarshaller = requestContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            //read xml into object
            request = (Request) unmarshaller.unmarshal(new StringReader(xml));

            responseContext = JAXBContext.newInstance(Response.class);
            marshaller = responseContext.createMarshaller();

            //response object factory
            com.novartis.connectors.jaxb.MTRes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.MTRes.ObjectFactory();
            //response <mts> object
            com.novartis.connectors.jaxb.MTRes.Response.Mts resMts
                                       = responseOF.createResponseMts();

            //request <mts> object
            com.novartis.connectors.jaxb.MTReq.Request.Mts reqMts
                                        = request.getMts();

            //response MT list
            List <Response.Mts.Mt> resMtList  = resMts.getMt();

            //request MT list
            List <Request.Mts.Mt> reqMtsList = reqMts.getMt();

            //Process resquest (retrieve elements) and initialize response
            for(Iterator iter = reqMtsList.iterator();
                    iter.hasNext();) {
                Request.Mts.Mt reqMt = (Request.Mts.Mt) iter.next();
                Response.Mts.Mt resMt = responseOF.createResponseMtsMt();
                resMt.setMobilenumber(reqMt.getMobilenumber());
                resMtList.add(resMt);
            }

            //create response object
            response = responseOF.createResponse();
            //initialize response object
            response.setMts(resMts);
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
            com.novartis.connectors.jaxb.MTRes.ObjectFactory responseOF =
                                        new com.novartis.connectors.jaxb.MTRes.ObjectFactory();

            com.novartis.connectors.jaxb.MTRes.Response.Mts resMts
                                       = responseOF.createResponseMts();
            Response.Mts.Mt resMt = responseOF.createResponseMtsMt();
            resMts.getMt().add(resMt);
            Response errResponse =  responseOF.createResponse();
            errResponse.setMts(resMts);
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
