/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.novartis.messaging.preps;

import com.novartis.xmlbinding.mo_req.Request;
import com.novartis.xmlbinding.mo_res.Response;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.naming.NamingException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Kwame
 */
public class MOPrep {
    private JAXBContext requestContext;
    private JAXBContext responseContext;
    private Unmarshaller unmarshaller;
    private Marshaller marshaller;
    private Schema schema;
    private Response responseObj;
    private Request requestObj;
    private StringWriter responseWriter;


    private List<Request.Mos.Mo> getMos(String xml) throws JAXBException, SAXException{
        /*
         * Returns a List of MOs from the MO request XML
         */

        requestContext = JAXBContext.newInstance("com.novartis.xmlbinding.mo_req");

        schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                     .newSchema(OptinPrep.class.getClassLoader().getResource("com/novartis/messaging/schemas/MORequest.xsd"));

        unmarshaller = requestContext.createUnmarshaller();
        unmarshaller.setSchema(schema);

        //read xml into object
        Request req = (Request) unmarshaller.unmarshal(new StringReader(xml));

        return req.getMos().getMo();
    }


    public String processMO(String xml){
        String response_mo ="";
        try{
            //Begin output xml generation
            responseContext =  JAXBContext.newInstance("com.novartis.xmlbinding.mo_res");
            marshaller = responseContext.createMarshaller();
            marshaller.setProperty("jaxb.encoding", "Unicode");

            com.novartis.xmlbinding.mo_res.ObjectFactory of = new com.novartis.xmlbinding.mo_res.ObjectFactory();
            responseObj = of.createResponse();

            Response.Mos res_mos = of.createResponseMos();


            GotSMSMessagingPrep msg = new GotSMSMessagingPrep();

            List<Request.Mos.Mo> mos = getMos(xml);
            Iterator<Request.Mos.Mo> i = mos.iterator();

            MTPrep mtprep = new MTPrep("http://appdev.ipsh.net/connectormessaging/mt");

            com.novartis.messaging.helpers.OtherResponse res = new com.novartis.messaging.helpers.OtherResponse();

            response_mo += "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
            response_mo += "<response>\n";

            while(i.hasNext()){
                Request.Mos.Mo mo = i.next();
                String res_msg = msg.processSMS(mo.getMessage(), mo.getMobilenumber(), mo.getShortcode(), mo.getCarrierid());

                response_mo += "\t<mobilenumber>"+mo.getMobilenumber()+"</mobilenumber>\n";

                int campaignid = 5; // Dev; Campaignid for staging is 6
                int priority = 2;
                String msgfxn = "MO";
                String fteu = "Y";
                float tariff = 0;
                Date sendat = null;

                res = mtprep.sendMT(mo.getMobilenumber(), res_msg, mo.getShortcode(),  mo.getCarrierid(), campaignid, sendat, msgfxn, priority, fteu, tariff);

                if(res.getStatus().matches("FAIL")){
                    String err_msg = "Could not send MT to "+mo.getMobilenumber()+" Reason:";
                    List<String> errorList = res.getStatusmessage();
                    Iterator el = errorList.iterator();

                    while(el.hasNext()){
                        err_msg += "<exception>"+el.next()+"</exception>";
                    }

                    throw new PrepException(err_msg);
                }
            }

            response_mo +="<status>"+res.getStatus()+"</status>\n";
            response_mo +="<statusmessage>";

            List<String> exList = res.getStatusmessage();
            Iterator e = exList.iterator();
            while(e.hasNext()){
                response_mo += e.next();
            }
            response_mo += "</statusmessage>\n";
            response_mo += "</response>\n";

        }catch(SQLException sex){
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\t<response>\n\t\t<mos>\n\t\t\t<mo></mo>\n\t\t</mos><status>FAIL</status><statusmessage><exceptions><exception>ERROR: "+sex+"</exception></exceptions></statusmessage>";
        }
        catch(SAXException ex){
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\t<response>\n\t\t<mos>\n\t\t\t<mo></mo>\n\t\t</mos><status>FAIL</status><statusmessage><exceptions><exception>ERROR: "+ex+"</exception></exceptions></statusmessage>";
        }
        catch(JAXBException jex){
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\t<response>\n\t\t<mos>\n\t\t\t<mo></mo>\n\t\t</mos><status>FAIL</status><statusmessage><exceptions><exception>ERROR: "+jex+"</exception></exceptions></statusmessage>";
        }
        catch(ClassNotFoundException cls){
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\t<response>\n\t\t<mos>\n\t\t\t<mo></mo>\n\t\t</mos><status>FAIL</status><statusmessage><exceptions><exception>ERROR: "+cls+"</exception></exceptions></statusmessage>";
        }
        catch(NamingException nex){
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\t<response>\n\t\t<mos>\n\t\t\t<mo></mo>\n\t\t</mos><status>FAIL</status><statusmessage><exceptions><exception>ERROR: "+nex+"</exception></exceptions></statusmessage>";
        }
        catch(MalformedURLException nex){
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\t<response>\n\t\t<mos>\n\t\t\t<mo></mo>\n\t\t</mos><status>FAIL</status><statusmessage><exceptions><exception>ERROR: "+nex+"</exception></exceptions></statusmessage>";
        }
        catch(IOException nex){
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\t<response>\n\t\t<mos>\n\t\t\t<mo></mo>\n\t\t</mos><status>FAIL</status><statusmessage><exceptions><exception>ERROR: "+nex+"</exception></exceptions></statusmessage>";
        }
        catch(PrepException pex){
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\t<response>\n\t\t<mos>\n\t\t\t<mo></mo>\n\t\t</mos><status>FAIL</status>\n<statusmessage><exceptions>"+pex+"</exceptions></statusmessage>";
        }

        return response_mo;
    }

}
