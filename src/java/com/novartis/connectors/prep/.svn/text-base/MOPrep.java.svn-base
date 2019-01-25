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
import com.novartis.connectors.jaxb.MOReq.Request;
import com.novartis.connectors.jaxb.MORes.Response;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXException;

public class MOPrep {

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
                     .newSchema(BroadcastPrep.class.getClassLoader().getResource("com/novartis/connectors/schemas/MOReq.xsd"));
            unmarshaller = requestContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            //read xml into object
            request = (Request) unmarshaller.unmarshal(new StringReader(xml));

            responseContext = JAXBContext.newInstance(Response.class);
            marshaller = responseContext.createMarshaller();

            //response object factory
            com.novartis.connectors.jaxb.MORes.ObjectFactory responseOF =
                                        new   com.novartis.connectors.jaxb.MORes.ObjectFactory();
            //response <mos> object
            com.novartis.connectors.jaxb.MORes.Response.Mos resMos
                                       = responseOF.createResponseMos();

            //request <mos> object
            com.novartis.connectors.jaxb.MOReq.Request.Mos reqMos
                                        = request.getMos();

            //response MO list
            List <Response.Mos.Mo> resMoList  = resMos.getMo();

            //request MO list
            List <Request.Mos.Mo> reqMosList = reqMos.getMo();

            //Process resquest (retrieve elements) and initialize response
            for(Iterator iter = reqMosList.iterator();
                    iter.hasNext();) {
                Request.Mos.Mo reqMo = (Request.Mos.Mo) iter.next();
                Response.Mos.Mo resMo = responseOF.createResponseMosMo();
                resMo.setMobilenumber(reqMo.getMobilenumber());
                resMoList.add(resMo);
            }

            //create response object
            response = responseOF.createResponse();
            //initialize response object
            response.setMos(resMos);
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
            com.novartis.connectors.jaxb.MORes.ObjectFactory responseOF =
                                        new com.novartis.connectors.jaxb.MORes.ObjectFactory();

            com.novartis.connectors.jaxb.MORes.Response.Mos resMos
                                       = responseOF.createResponseMos();
            Response.Mos.Mo resMo = responseOF.createResponseMosMo();
            resMos.getMo().add(resMo);
            Response errResponse =  responseOF.createResponse();
            errResponse.setMos(resMos);
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
