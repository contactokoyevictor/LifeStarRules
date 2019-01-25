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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Kwame
 */
public class OptoutPrep {
    private InitialContext cxt = null;
    private DataSource datasource = null;
    private Connection conn = null;
    private String url;

    private String createXml(String mobilenumber, int consumerid, int campaignid, int listid){
        String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        xml += "<request><optouts>";
        xml += "<consumer>";
        xml += "<mobilenumber>"+mobilenumber+"</mobilenumber>";
        xml += "<consumerid>"+consumerid+"</consumerid>";
        xml += "<campaignid>"+campaignid+"</campaignid>";
        xml += "<listid>"+listid+"</listid>";
        xml += "</consumer></optouts></request>";

        return xml;
    }

    private List<String> getStatusmessage(String xml) throws PrepException{
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

    private boolean alreadyOptedOut(int consumerid) throws SQLException{
        /* returns false if the user doesn't exist or the user is already opted in*/

        String sql = "SELECT count(*) as num_rows FROM user WHERE id = ? AND sms_opt_out_dt IS NULL";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, consumerid);
        ResultSet res = statement.executeQuery();
        res.next();

        int rows = res.getInt("num_rows");

        if(rows == 0){
            return true;
        }

        return false;
    }

    private OtherResponse sendOptoutRequest(String xml) throws MalformedURLException, IOException, JAXBException, PrepException{
            StringBuilder responseXml = new StringBuilder();

            URL optoutUrl = new URL(url);
            HttpURLConnection httpconn = (HttpURLConnection)optoutUrl.openConnection();
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);


            OutputStreamWriter out = new OutputStreamWriter(httpconn.getOutputStream());
            out.write(xml);
            out.close();

            int responseCode = httpconn.getResponseCode();
            if(responseCode != 200){
                List<String> err = new ArrayList();
                err.add("Error with HTTP connection in OptoutPrep.sendOptoutRequest(); HTTP response code:  "+responseCode);
                return new OtherResponse("FAIL", err);
            }

            BufferedReader in = new BufferedReader( new InputStreamReader(httpconn.getInputStream()));

            String line;
            while (( line = in.readLine()) != null){
                responseXml.append(line);
            }
            in.close();

            JAXBContext responseContext = JAXBContext.newInstance("com.novartis.messaging.xmlbinding.optout_res");
            Unmarshaller unmarshaller = responseContext.createUnmarshaller();

            com.novartis.xmlbinding.optout_res.Response res = (com.novartis.xmlbinding.optout_res.Response)unmarshaller.unmarshal(new StringReader(responseXml.toString()));

            List<String> statmsg = getStatusmessage(responseXml.toString());


            return new OtherResponse(res.getStatus(),statmsg);

    }

    public OptoutPrep(String url, String datasource) throws PrepException{
        try{
            this.url = url;
            cxt = new InitialContext();

            this.datasource = (DataSource)cxt.lookup(datasource);
            this.conn = this.datasource.getConnection();
        }
        catch(NamingException ne){
            throw new PrepException("NamingException Error in OptoutPrep.OptoutPrep() :"+ne);
        }
        catch(SQLException ne){
            throw new PrepException("SQLException Error in OptoutPrep.OptoutPrep() :"+ne);
        }
    }

    public OtherResponse doOptout(String mobilenumber, int consumerid, int campaignid, int listid) throws PrepException{
        try{
            if(!alreadyOptedOut(consumerid)){
                OtherResponse res = sendOptoutRequest(createXml(mobilenumber, consumerid, campaignid, listid));

                String sql = "UPDATE user SET sms_opt_out_dt = curdate() WHERE id = ?"; //remove opt out
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, consumerid);
                statement.executeUpdate();

                return res;
            }
        }
        catch(SQLException se){
            throw new PrepException("SQL Error in OptoutPrep.doOptout() :"+se);
        }
        catch(MalformedURLException se){
            throw new PrepException("URL Error in OptoutPrep.doOptout() :"+se);
        }
        catch(IOException se){
            throw new PrepException("IO Error in OptoutPrep.doOptout() :"+se);
        }
        catch(JAXBException se){
            throw new PrepException("JAXB Error in OptoutPrep.doOptout() :"+se);
        }

        List<String> err = new ArrayList();
        err.add("User is already opted-in or user does not exist");
        return new OtherResponse("FAIL", err);
    }

}
