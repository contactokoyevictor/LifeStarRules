package com.novartis.connectors.prep;
/**
 *
 * @author VICTOR_OKOYE
 */

import java.io.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import com.novartis.connectors.jaxb.email.*;

import java.util.logging.*;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class SendEmailRequest {

    public DocumentBuilderFactory dbf;
    public DocumentBuilder db;
    public Document doc1;
    public Document doc2 = null;


    public JAXBContext req_jaxbCtx;
    public Marshaller xml_Marshaller;
    public StringWriter writer;

    public String _errorOrWarningMessage = "";
    public Connection _dbConn;
    public PreparedStatement _sqlStatement;

    public URL url;
    public String urladdress, body;
    public DataOutputStream outputStream;
    public DataInputStream inputStream;
    public HttpURLConnection urlConnection;

    private String status;
    private String statusmessage;
    private String message_body;
    
    public String getStatusmessage() {
		return statusmessage;
	}
	public void setStatusmessage(String statusmessage) {
		this.statusmessage = statusmessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
        public String getMessage_body() {
		return message_body;
	}
	public void setMessage_body(String message_body) {
		this.message_body = message_body;
	}
    

    public void makeDBConnection() throws Exception {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/novartis");
            _dbConn = ds.getConnection();
        } catch (Exception e) {
            System.out.println("SQL Exception is caught during connection creation." + e);
            _errorOrWarningMessage = e.getMessage().toString();
            _dbConn.close();
            
        }
    }
    //Methods to create xml request(using jaxb marshaller).......................
    public void ForgotPasswordXmlRequest(int consumerid) {
        try {
            ObjectFactory reqXml_OF = new ObjectFactory();
            Request.Email reqEmail = reqXml_OF.createRequestEmail();
            Request req = reqXml_OF.createRequest();

            writer = new StringWriter();
            req_jaxbCtx = javax.xml.bind.JAXBContext.newInstance(com.novartis.connectors.jaxb.email.Request.class);
            xml_Marshaller = req_jaxbCtx.createMarshaller();

            xml_Marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            xml_Marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            try {
                   makeDBConnection();
                  _sqlStatement = _dbConn.prepareStatement("SELECT U.campaign_code_id as 'Campaignid', "
                    + "U.id as 'Consumerid', "
                    + "C.id as 'contacteventid',"
                    + "U.email as 'Emailaddr',"
                    + "U.novartis_campaign_indv_id as 'campaignindividualid', "
                    + "U.novartis_ib_contact_event_id as 'Inboundcontacteventid',"
                    + "S.nvs_kit_code as 'KitCode', "
                    + "U.user_fname as 'Consumerfname', "
                    + "U.password  as 'Temppassword',"
                    + "(SELECT value from msg_param_values, message_channel_params "
                    + " WHERE msg_param_values.msg_channel_param_id = message_channel_params.id AND message_id = 1 AND `message_channel_params`.`name`= 'flexid') as 'flexid' "
                    + "FROM user AS U "
                    + "LEFT JOIN scheduler S ON U.id = S.user_id "
                    + "LEFT JOIN contact_events C ON U.id = C.user_id "
                    + "LEFT JOIN message_type mt ON S.message_channel_id = mt.id "
                    + " WHERE U.email IS NOT NULL AND S.message_channel_id ='1' AND U.id= ? AND S.nvs_kit_code IS NOT NULL");
                   _sqlStatement.setInt(1, consumerid);
                   ResultSet rs = _sqlStatement.executeQuery();
                   if(!rs.next()){
                       setStatus("FAIL");
                       setStatusmessage("FAIL");
                   }
                   if(rs.next()){
                       System.out.println("Result set is Nexted..");
                       reqEmail.setCampaignid(rs.getInt(1));
                       reqEmail.setConsumerid(rs.getInt(2));
                       reqEmail.setTmacontacteventid(rs.getString(3));
                       reqEmail.setEmailaddr(rs.getString(4));
                       reqEmail.setIrmaindividualid(rs.getString(5));
                       //reqEmail.setIrmacampaignindividualid(rs.getString(6));
                       reqEmail.setInboundcontacteventid(rs.getString(6));
                       reqEmail.setKitcode(rs.getString(7));
                       reqEmail.setConsumerfname(rs.getString(8));
                       reqEmail.setTemppassword(rs.getString(9));
                       /*reqEmail.setFlexurl("http://getontrack.com/session/register/"
                        + "email?ci_id=[irmacampaignindividualid]&ind_id="
                        + "[irmaindividualid]&ibce_id=[inboundcontacteventid]&src_code=[flexid]");*/
                        reqEmail.setFlexid(rs.getString(10));

                        //Adding all elements to request(root element).............
                        req.setEmail(reqEmail);
                        
                        xml_Marshaller.marshal(req, writer);
                        String xmlcontent = "";
                        xmlcontent = writer.toString();
                        System.out.println("Query Result is ::" +xmlcontent);
                        //...................................
                        SendEmailRequest(xmlcontent);
                        //...............................
                        //verify response against the next step...
                        if(getStatus().equalsIgnoreCase("ok") && getStatusmessage().equalsIgnoreCase("ok")){
                           //move from message queue to message History
                            System.out.println("Not yet Moving message from the queue table to History table...");

                        }
                   }
                   rs.close();
                  _sqlStatement.close();
                  _dbConn.close();
            } catch (Exception ex) {
                Logger.getLogger(SendEmailRequest.class.getName()).log(Level.SEVERE, null, ex.getMessage());
                System.out.println(ex.getMessage());
            }
            
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
    }
  
     public void SendEmailRequest(String body) {
        try {
            //creating the http connection;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
            urladdress = "http://appdev.ipsh.net/connectoremail/sendemail";
            url = new URL(urladdress);
            urlConnection = (HttpURLConnection)(url.openConnection());
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "text/plain");
            urlConnection.setRequestProperty("Accept-Language", "en");
            urlConnection.setAllowUserInteraction(true);
            urlConnection.setRequestProperty("Content-Length", "" + String.valueOf(body.length()));
            System.out.println("Writing to email connector...");
            //Creating output channel to send xml request data;;;;;;;;;;;;;;;;;;;;;;;;;
            outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(body);//Data is sent;;;;;;;;;;;;;;;;;;;;;
            outputStream.flush();
            outputStream.close();
            //Creating input channel to read responds from http;;;;;;;;;;;;;;;;;;;;;;;;;
            BufferedReader datain = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine = "";
            String xmlString = "";
            
            while ((inputLine = datain.readLine()) != null)
            {
                xmlString += inputLine;
            }
            System.out.println("\n the response is ::\n"+xmlString+"\n");
            datain.close();//Close input channel;;;;;;;;;;;;;;;;;;;;;;;
            urlConnection.disconnect();
            ReadResults(xmlString.toString());
        } catch (Exception e) {
        }
    }
   

    public void ReadResults(String XML)
    {
     if(XML !=null){
     try{
           db = null;
           doc2 = null;
           dbf = DocumentBuilderFactory.newInstance();
	   db = dbf.newDocumentBuilder();
           InputSource is = new InputSource();
           is.setCharacterStream(new StringReader(XML));
	   doc2 = db.parse(is);
           doc2.normalizeDocument();
           //Extracting xml document content and reading its elements and Nodes values
            NodeList nodeList = doc2.getElementsByTagName("response");
            for(int t = 0; t < nodeList.getLength(); t++ ){
                Element element1 = (Element) nodeList.item(t);
                ///////////////////////////////////////////////
                NodeList elementEL1 = element1.getElementsByTagName("status");
                Element statusEL = (Element) elementEL1.item(0);
                setStatus(getCharacterDataFromElement(statusEL));
                ////////////////////////////////////////////////
                NodeList elementEL2 = element1.getElementsByTagName("statusmessage");
                Element statusmessageEL = (Element) elementEL2.item(0);
                setStatusmessage(getCharacterDataFromElement(statusmessageEL));
              }
	}catch (Exception e){
                System.out.println(e.getMessage().toString());
        }
       }
     }
     public String getCharacterDataFromElement(Element e) {
     Node child = e.getFirstChild();
     if(child instanceof CharacterData){
     CharacterData cd = (CharacterData) child;
     return cd.getData();
     }
     return "?";
    }
}

