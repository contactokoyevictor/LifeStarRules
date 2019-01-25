/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.novartis.messaging.preps;

import com.novartis.messaging.helpers.OtherResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Kwame
 */
public class MTPrep {
    private JAXBContext requestContext;
    private JAXBContext responseContext;
    private Unmarshaller unmarshaller;
    private Marshaller marshaller;
    private Schema schema;
    private com.novartis.xmlbinding.mt_response.Response responseObj;
    private StringWriter responseWriter;

    private InitialContext cxt = null;
    private DataSource datasource = null;
    private Connection conn = null;
    private String url;

    private com.novartis.xmlbinding.mt_response.Response getResponse(String xml) throws JAXBException, SAXException{
        /*
         * Returns MT response XML as an object
         */

        try{
        requestContext = JAXBContext.newInstance("com.novartis.xmlbinding.mt_response");

        schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                     .newSchema(MTPrep.class.getClassLoader().getResource("com/novartis/messaging/schemas/MTResponse.xsd"));

        unmarshaller = requestContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        }
        catch(JAXBException jex){
            throw new JAXBException(jex+"  in MTPrep.getResponse()");
        }
        catch(SAXException sex){
            throw new SAXException(sex+" in MTPrep.getResponse()");
        }

        //read xml into object
        com.novartis.xmlbinding.mt_response.Response res = (com.novartis.xmlbinding.mt_response.Response) unmarshaller.unmarshal(new StringReader(xml));

        return res;
    }

    private String createXml(String mobilenumber, String message, String shortcode, int carrierid, int campaignid, Date sendat, String msgfxn, int priority, String fteu, float tariff){
        String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        xml += "<request><mts><mt>";

        xml += "<mobilenumber>"+mobilenumber+"</mobilenumber>";
        xml += "<message>"+message+"</message>";
        xml += "<shortcode>"+shortcode+"</shortcode>";
        xml += "<carrierid>"+carrierid+"</carrierid>";
        xml += "<campaignid>"+campaignid+"</campaignid>";
        xml += "<sendat>"+sendat+"</sendat>";
        xml += "<msgfunction>"+msgfxn+"</msgfunction>";
        xml += "<priority>"+priority+"</priority>";
        xml += "<fteu>"+fteu+"</fteu>";
        xml += "<tariff>"+tariff+"</tariff>";
        xml += "</mt></mts></request>";

        return xml;
    }

    private boolean canSendMT(String mobilenumber) throws SQLException{
        try{
            String sql = "SELECT count(*) as num_rows FROM user WHERE user_mphone = ? AND sms_opt_out_dt IS NULL AND sms_opt_in_dt IS NOT NULL";
            conn = datasource.getConnection();
            PreparedStatement prep_statement = conn.prepareStatement(sql);

            prep_statement.setString(1, mobilenumber);
            ResultSet res = prep_statement.executeQuery();

            res.next();
            int rows = res.getInt("num_rows");
            if(rows == 0){
                return false;
            }
        }
        catch(SQLException se){
            throw se;
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException sex){

            }
        }

        //conn.close();


        return true;
    }

    private List<String> getStatusmessage(String xml) throws PrepException{
        /*
         * Handles <statusmessage>TEXT<exceptions><exception></exception>TEXT</exceptions></statusmessage>
         * and <statusmessage>OK</statusmessage>
         * Returns a list of the text values
         */

        boolean validate = false;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(validate);
        dbf.setNamespaceAware(true);
        dbf.setIgnoringElementContentWhitespace(true);

        Document doc = null;
        String statusmessage = "";

        List<String> exceptions = new ArrayList();



        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nodeMatches = doc.getElementsByTagName("statusmessage");
            Node statusmsgNode = nodeMatches.item(0); //should be only one <statusmessage> element
            NodeList immediateNodes = statusmsgNode.getChildNodes();

            Node textNode = immediateNodes.item(0); //text
            exceptions.add(textNode.getNodeValue());

            if(!textNode.getNodeValue().equalsIgnoreCase("ok")){
                statusmessage += textNode.getNodeValue();
                statusmessage += "<exceptions>";

                Node exceptionsNode = immediateNodes.item(1); //exceptions element node
                NodeList exceptionsList = exceptionsNode.getChildNodes();
                for(int i=0; i<exceptionsList.getLength(); i++){
                    Node exceptionNode = exceptionsList.item(i);
                    Node textContent = exceptionNode.getFirstChild();
                    statusmessage += "<exception>";
                    exceptions.add(textContent.getNodeValue());
                    statusmessage += textContent.getNodeValue();
                    statusmessage += "</exception>";
                }
                statusmessage += "</exceptions>";
            }

        } catch (SAXException e) {
            throw new PrepException("SAXException in MTPrep.getStatusmessage() :"+e);
        } catch (ParserConfigurationException e) {
            throw new PrepException("ParserConfigurationException in MTPrep.getStatusmessage() :"+e);
        } catch (IOException e) {
            throw new PrepException("IOException in MTPrep.getStatusmessage() :"+e);
        }
        return exceptions;
    }

    private String postMT(String xml) throws PrepException{
        /*
         * Takes an MT request xml and post to the MT connector
         * Returns a string representing the MT response
         */

        StringBuilder responseXml = new StringBuilder();
            try{
                URL mtUrl = new URL(this.url);
                HttpURLConnection httpconn = (HttpURLConnection)mtUrl.openConnection();
                httpconn.setDoInput(true);
                httpconn.setDoOutput(true);

                OutputStreamWriter out = new OutputStreamWriter(httpconn.getOutputStream());
                out.write(xml);
                out.close();

                int responseCode = httpconn.getResponseCode();
                if(responseCode != 200){
                    throw new PrepException("Error with HTTP connection in MTPrep.postMT(); HTTP response code:  "+responseCode);
                }
                //out.close();

                BufferedReader in = new BufferedReader( new InputStreamReader(httpconn.getInputStream()));

                String line;
                while (( line = in.readLine()) != null){
                    responseXml.append(line);
                }
                in.close();

            }
           catch(MalformedURLException malex ){
                throw new PrepException("URL Error in MTPrep.postMT(): "+malex.getMessage());
            }
            catch(IOException ioe){
                throw new PrepException("IO Error in MTPrep.postMT(): "+ioe.getMessage());
            }
        return responseXml.toString();
    }

    public OtherResponse sendMT(String mobilenumber, String message, String shortcode, int carrierid, int campaignid, Date sendat, String msgfxn, int priority, String fteu, float tariff) throws PrepException, SQLException, JAXBException, SAXException, MalformedURLException, IOException{
        //StringBuilder responseXml = new StringBuilder();
        String response;

        if(canSendMT(mobilenumber)){
            //System.out.print("User with mobile number:"+mobilenumber+" exist!");

            String mt_request_xml = createXml(mobilenumber, message, shortcode, carrierid, campaignid, sendat, msgfxn, priority, fteu, tariff);
            response = postMT(mt_request_xml);

            com.novartis.xmlbinding.mt_response.Response res = getResponse(response);



            List<String> statusmsg = getStatusmessage(response);

            return new OtherResponse(res.getStatus(), statusmsg);
       }
        else{//if user not registerd or opted-in send back message on how to register for GetOnTrack
            String registerMsg = "Novartis Pharmaceuticals Corp: You have to register for the GetOnTrack BP Tracker to use the service. Didn't mean to? Call 877-577-7726 for more information";
            String mt_request_xml = createXml(mobilenumber, registerMsg, shortcode, carrierid, campaignid, sendat, msgfxn, priority, fteu, tariff);
            response = postMT(mt_request_xml);
            com.novartis.xmlbinding.mt_response.Response res = getResponse(response);
            //com.novartis.xmlbinding.mt_response.Response res = getResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><response><mts><mt><mobilenumber>"+mobilenumber+"</mobilenumber></mt></mts><status>FAIL</status><statusmessage><exceptions><exception>User not opted in for SMS</exception></exceptions></statusmessage></response>");


            //System.out.print("User with mobile number:"+mobilenumber+" does not exist!");
            List<String> statusmsg = getStatusmessage(response);

            return new OtherResponse(res.getStatus(), statusmsg);
        }

    }

    public MTPrep(String url) throws NamingException, SQLException{

            this.url = url;
            cxt = new InitialContext();
            datasource = (DataSource)cxt.lookup("java:/jdbc/novartis");
            conn = datasource.getConnection();
    }

}
