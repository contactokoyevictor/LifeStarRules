package com.novartis.connectors.prep;

import com.novartis.connectors.database.*;
import com.novartis.connectors.jaxb.GetPageElementsRequest.Request.Page;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;

import java.math.BigInteger;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import com.novartis.connectors.jaxb.GetPageElementsResponse.Response;
import com.novartis.connectors.jaxb.GetPageElementsRequest.Request;
import com.novartis.connectors.jaxb.GetPageElementsRequest.Request.Page.Elements;
import java.util.Calendar;
import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import java.util.TimeZone;


import java.sql.*;
import javax.sql.DataSource;
import javax.naming.*;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;


public class GetPageElementsPrep {

    private JAXBContext requestContext;
    private JAXBContext responseContext;
    private Unmarshaller unmarshaller;
    private Marshaller marshaller;
    private Schema schema;
    private Response response;
    private Request request;
    private StringWriter responseWriter;

/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////
    private XMLGregorianCalendar tempDateTime() {
        return null;
    }
    private String _errorOrWarningMessage = "";
    private Connection _dbConn;

    public void makeDBConnection() throws Exception {
        if (_dbConn != null && _dbConn.isClosed()) _dbConn = null;
        if (_dbConn != null) {
            return;
        }
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/novartis");
            _dbConn = ds.getConnection();
        } catch (Exception e) {
            System.out.println("SQL Exception is caught during connection creation." + e);
            _errorOrWarningMessage = e.getMessage().toString();
            _dbConn.close();
            _dbConn = null;
        }
    }

/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////
    private String getElementStringValueFromList(String elementName, List elementList) {
        for (int i = 0; i < elementList.size(); i++) {
            JAXBElement e = (JAXBElement) elementList.get(i);
            if (e.getName().getLocalPart().equals(elementName)) {
                return e.getValue().toString();
            }
        }
        return null;
    }

    private XMLGregorianCalendar getElementDateValueFromList(String elementName, List elementList) {
        for (int i = 0; i < elementList.size(); i++) {
            JAXBElement e = (JAXBElement) elementList.get(i);
            if (e.getName().getLocalPart().equals(elementName)) {
                return (XMLGregorianCalendar)e.getValue();
            }
        }
        return null;
    }

    private XMLGregorianCalendar XMLGregorianCalendarFrom(Date dt)
            throws javax.xml.datatype.DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dt);
        if (!cal.isSet(cal.HOUR)) {
            cal.set(cal.HOUR, 0);
            cal.set(cal.MINUTE, 0);
            cal.set(cal.SECOND, 0);
        }
        cal.clear(cal.MILLISECOND);
        XMLGregorianCalendar gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        int i = Integer.parseInt(Long.toString(Math.round(Math.random() * 6) + 1));
        switch (i) {
            case 1:
                gc.setTimezone(-240); //EDT
                break;
            case 2:
                gc.setTimezone(-300); //CDT
                break;
            case 3:
                gc.setTimezone(-360); //MDT
                break;
            case 4:
                gc.setTimezone(-420); //PDT
                break;
            case 5:
                gc.setTimezone(-600); //HST
                break;
            case 6:
                gc.setTimezone(-540); //HDT
                break;
            case 7:
                gc.setTimezone(-480); //EDT
                break;
        }
        // TODO: should be based on consumerid
        return gc;
    }

    private String getStringValue(Object o)
    {
        try
        {
            if (o != null)
                return o.toString();
        } catch (Exception e) {
        }
        return "";
    }

    private int getIntValue(Object o)
    {
        try{
            return Integer.parseInt(getStringValue(o));
        } catch (Exception e) {
        }
        return Integer.MIN_VALUE;
    }

    public String processXML(String xml) throws JAXBException {
        int consumerID = 0; //consumer ID
        int campaignID = 0; //TODO
        PreparedStatement _sqlStatement;
        try {
System.out.println("******************** Request to MobileWeb Prep object :");
System.out.println(xml);
System.out.println("********************");
            requestContext = JAXBContext.newInstance(com.novartis.connectors.jaxb.GetPageElementsRequest.Request.class);
            schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(GetPageElementsPrep.class.getClassLoader().getResource("com/novartis/connectors/schemas/getPageElements.xsd"));
            unmarshaller = requestContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            //read xml request into request object
            request = (Request) unmarshaller.unmarshal(new StringReader(xml));
            //create response context object
            responseContext = JAXBContext.newInstance(com.novartis.connectors.jaxb.GetPageElementsResponse.Response.class);
            marshaller = responseContext.createMarshaller();
            //create response Object Factory
            com.novartis.connectors.jaxb.GetPageElementsResponse.ObjectFactory responseOF =
                    new com.novartis.connectors.jaxb.GetPageElementsResponse.ObjectFactory();
            //create response <Page> Object
            com.novartis.connectors.jaxb.GetPageElementsResponse.Response.Page responsePage = responseOF.createResponsePage();
            //create response <elements> object
            com.novartis.connectors.jaxb.GetPageElementsResponse.Response.Page.Elements responseElements = responseOF.createResponsePageElements();
            //initialize response object
            response = responseOF.createResponse();
            //create response Elements lsit
            List<Response.Page.Elements.Element> responseElementList = responseElements.getElement();
            //read request element <Page> into object
            Page requestPage = request.getPage();
            Elements myElements = (Elements) requestPage.getElements();
            List elementList = myElements.getElement();
            try {
                BigInteger consID = requestPage.getConsumerid();
                consumerID = consID.intValue(); //throw null pointer exception.
            } catch (Exception ex) {
                throw new Exception("ConsumerID is NULL!" + ex.getMessage());
            }

            for (Iterator iter = elementList.iterator(); iter.hasNext();) {
                Request.Page.Elements.Element requestElement = (Request.Page.Elements.Element) iter.next();
                List children = requestElement.getNameOrMessageOrWeekday();
                String s = requestElement.getId();
                Response.Page.Elements.Element resElement = responseOF.createResponsePageElementsElement();
                resElement.setId(s);
                resElement.setElementstatus("OK");
                resElement.setElementstatusmessage("");
                try {
                    if (s.equalsIgnoreCase("userdata")) {
                        /*
                         *       <element id="UserData">
                         *        <first_name></first_name>
                         *        <last_name></last_name>
                         *        <dob>1985-02-24</dob>
                         *        <gender></gender>
                         *        <address_line1></address_line1>
                         *        <address_line2></address_line2>
                         *        <city></city>
                         *        <state></state>
                         *        <zip></zip>
                         *        <email></email>
                         *        <password></password>
                         *        <phone></phone>
                         *      </element>
                         */
                        String fname = getElementStringValueFromList("first_name", children);
                        String lname = getElementStringValueFromList("last_name", children);
                        String dob = getElementStringValueFromList("dob", children);
                        long lDate = 0;
                        if (dob != null) lDate = DatatypeConverter.parseDateTime(dob).getTimeInMillis();
                        java.sql.Date dob_as_date = new java.sql.Date(lDate);
                        String gender = getElementStringValueFromList("gender", children);
                        if ((gender != null) && (gender.length() > 1)) gender = gender.substring(0, 1);
                        String address_line1 = getElementStringValueFromList("address_line1", children);
                        String address_line2 = getElementStringValueFromList("address_line2", children);
                        String city = getElementStringValueFromList("city", children);
                        String state = getElementStringValueFromList("state", children);
                        String zip = getElementStringValueFromList("zip", children);
                        String marketing = getElementStringValueFromList("marketing", children);
                        String email = getElementStringValueFromList("email", children);
                        String phone = getElementStringValueFromList("phone", children);
                        String sms_accept = getElementStringValueFromList("sms_accept", children);
                        UserData_dbobj user = new UserData_dbobj();
                        if (consumerID == 0) {
                            user.insertNewUserData(fname, lname, dob_as_date, gender, address_line1, address_line2, city, state, zip, email, phone, sms_accept);
                            resElement.setConsumerid(user.getConsumer_id());
                            resElement.setSkipTyping(user.getSkip_typing());
                            resElement.setElementstatus(user.getElementstatus());
                            resElement.setElementstatusmessage(user.getElementstatusmessage());
                        } else if (consumerID > 0) {
                            if (children.size() > 0) {
                                user.UpdateUserData(consumerID, fname, lname, dob_as_date, gender, address_line1, address_line2, city, state, zip, email, phone, sms_accept);
                                resElement.setConsumerid(user.getConsumer_id());
                                resElement.setElementstatus(user.getElementstatus());
                                resElement.setElementstatusmessage(user.getElementstatusmessage());
                            } else {
                                user.FetchUserData(consumerID);
                                resElement.setConsumerid(user.getConsumer_id());
                                resElement.setFirstName(user.getFirst_name());
                                resElement.setLastName(user.getLast_name());
                                dob_as_date = user.getDob();
                                if (dob_as_date != null) resElement.setDob(XMLGregorianCalendarFrom(dob_as_date));
                                resElement.setGender(user.getGender());
                                resElement.setAddressLine1(user.getAddress1());
                                resElement.setAddressLine2(user.getAddress2());
                                resElement.setEmail(user.getEmail());
                                resElement.setCity(user.getCity());
                                resElement.setState(user.getState());
                                resElement.setZip(user.getZip());
                                resElement.setPhone(user.getPhone());
                                resElement.setSmsAccept(user.getSmsAccept());
                                resElement.setElementstatus(user.getElementstatus());
                                resElement.setElementstatusmessage(user.getElementstatusmessage());
                            }
                        }
                    } else if (s.equalsIgnoreCase("forgotpassword")) {
                        /*
                         *       <element id="ForogotPassword">
                         *        <email_address></email_address>
                         *      </element>
                         *
                         * <?xml version='1.0' encoding='utf-8'?>
<request>
  <page>
    <campaignid>1</campaignid>
    <consumerid>1019</consumerid>
    <elements>
      <element id='ForgotPassword'>
        <email_address>frexjeff@yahoo.com.au</email_address>
      </element>
    </elements>
  </page>
</request>
                         * http://localhost:8080/LifeStarRules/prep/mobileweb/getpageelements
                         */
                        String email = getElementStringValueFromList("email_address", children);

                        ForgotPassword_dbobj fp = new ForgotPassword_dbobj();
                        fp.createXmlRequest(email);

                        resElement.setConsumerid(fp.getConsumer_id());
                        resElement.setElementstatus(fp.getElementstatus());
                        resElement.setElementstatusmessage(fp.getElementstatusmessage());

                    } else if (s.equalsIgnoreCase("enrollmentacceptdata")) {
                        /*
                         *      <element id="EnrollmentAcceptData">
                         *      <marketing></marketing>
                         *      <accept></accept>
                         *      </element>
                         *
                         */
                        String marketing = getElementStringValueFromList("marketing", children);
                        String accept = getElementStringValueFromList("accept", children);
                        int mkting = Integer.parseInt(marketing);
                        int accpt = Integer.parseInt(accept);

                        if ((mkting == 0 || mkting == 1) && (accpt == 0 || accpt == 1)) {
                            makeDBConnection();
                            _sqlStatement = _dbConn.prepareStatement("INSERT INTO `novartis`.`user`(accept_marketing, source_code, account_start_dt) VALUES (?, ?, (SELECT NOW()))", Statement.RETURN_GENERATED_KEYS);
                            _sqlStatement.setInt(1, mkting);
                            _sqlStatement.setString(2, getElementStringValueFromList("srccode", children));
                            if (_sqlStatement.executeUpdate() > 0) {
                                ResultSet insertedKeys = _sqlStatement.getGeneratedKeys();
                                if (insertedKeys.next())
                                {
                                    int k = insertedKeys.getInt(1);
                                    resElement.setConsumerid(k);
                                    if (accpt == 0) {
                                        _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`user` SET account_start_dt = null WHERE id=?");
                                        _sqlStatement.setInt(1, k);
                                        int i = _sqlStatement.executeUpdate();
                                        if (i != 1) {
                                            throw new PrepException("Unable to update enrollemnt status");
                                        }
                                    }
                                } else {
                                    throw new PrepException("Unable to verify newly created user record.");
                                }
                            } else {
                                throw new PrepException("Unable to create new user record.");
                            }
                        } else {
                            throw new Exception("Invalid values for <marketing> or <accept>!");
                        }
                    } else if (s.equalsIgnoreCase("EnrollmentDiagnosisData")) {
                        /*
                         *      <element id="EnrollmentDiagnosisData">
                         *          <high_bp>1</high_bp>
                         *          <medication>1</medication>
                         *      </element>
                         *
                         */
                        String sTemp;
                        sTemp = getElementStringValueFromList("high_bp", children);
                        int high_bp = Integer.parseInt(sTemp);
                        sTemp = getElementStringValueFromList("medication", children);
                        int medication = Integer.parseInt(sTemp);

                        if ((high_bp == 0 || high_bp == 1) && (medication == 0 || medication == 1)) {
                            makeDBConnection();
                            _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`user` SET high_bp = ?, medication = ? WHERE id = ?");
                            _sqlStatement.setInt(1, high_bp);
                            _sqlStatement.setInt(2, medication);
                            _sqlStatement.setInt(3, consumerID);
                            if (_sqlStatement.executeUpdate() < 1) {
                                throw new PrepException("Unable to create new user record.");
                            }
                        } else {
                            throw new Exception("Invalid values for <medication> or <hiph_bp>!");
                        }

                    } else if (s.equalsIgnoreCase("Login")) {
                        /*
                         *       <element id="Login">
                         *       <user_name></user_name>
                         *       <password></password>
                         *       </element>
                         *
                         */
                        String username = getElementStringValueFromList("user_name", children);
                        String password = getElementStringValueFromList("password", children);

                        Login_dbobj login = new Login_dbobj();
                        login.process(username, password);

                        int id = login.getConsumer_id();
                        String status = login.getElementstatus();
                        String msg = login.getElementstatusmessage();

                        resElement.setConsumerid(id);
                        resElement.setCampaignid(requestPage.getCampaignid());
                        resElement.setElementstatus(status);
                        resElement.setElementstatusmessage(msg);
                    } else if (s.equalsIgnoreCase("setnewpassword")) {
                        /*
                         *       <element id="SetNewPassword">
                         *       <password>originalpassword</password>
                         *       <new_password>newpassword</new_password>
                         *       </element>
                         *
                         */
                        String password = getElementStringValueFromList("password", children);
                        String new_password = getElementStringValueFromList("new_password", children);

                        SetNewPassword_dbobj passObj = new SetNewPassword_dbobj();

                        if (children.size() < 3) { // classic set password flow
                            passObj.setNewPassword(consumerID, password, new_password);
                        } else { // brand site enrollee
                            /*
                            <srccode>9999</srccode>
                            <ibcontacteventid>9600187860</ibcontacteventid>
                            <new_password>password10000</new_password>
                            <individualid>8085516</individualid>
                            <campaignindividualid>20134183</campaignindividualid>
                            */
                            String srccode = getElementStringValueFromList("srccode", children);
                            String ibcontacteventid = getElementStringValueFromList("ibcontacteventid", children);
                            String individualid = getElementStringValueFromList("individualid", children);
                            String campaignindividualid = getElementStringValueFromList("campaignindividualid", children);
                            passObj.setNewPassword(consumerID, password, new_password, individualid, campaignindividualid);
                        }
                        resElement.setConsumerid(passObj.getConsumer_id());
                        resElement.setElementstatus(passObj.getElementstatus());
                        resElement.setElementstatusmessage(passObj.getElementstatusmessage());
                    } else if (s.equalsIgnoreCase("DashBoardLastActivity")) {
                        /*
                         *     <element id="DashBoardLastActivity">
                         *     <last_time></last_time>
                         *     <today></today>
                         *     <week></week>
                         *      </element>
                         *
                         */

                        makeDBConnection();
                        java.sql.Date lastActivity = null;
                        int todayCount = 0;
                        int weekCount = 0;
                        ResultSet rs;

                        _sqlStatement = _dbConn.prepareStatement("SELECT activity_dt FROM `novartis`.`user_activities` WHERE user_id = ? " +
                                                                    "ORDER BY activity_dt DESC limit 1");
                        _sqlStatement.setInt(1, consumerID);
                        rs = _sqlStatement.executeQuery();
                        if (rs.next()) {
                            lastActivity = rs.getDate(1);
                            _sqlStatement = _dbConn.prepareStatement("SELECT count(1) FROM `novartis`.`user_activities` WHERE user_id = ? " +
                                                                        " AND (WEEK(activity_dt) = WEEK(now()))");
                            _sqlStatement.setInt(1, consumerID);
                            rs = _sqlStatement.executeQuery();
                            if (rs.next()) {
                                weekCount = rs.getInt(1);
                            }
                            _sqlStatement = _dbConn.prepareStatement("SELECT count(1) FROM `novartis`.`user_activities` WHERE user_id = ? " +
                                                                        " AND (DATE(activity_dt) = DATE(now()))");
                            _sqlStatement.setInt(1, consumerID);
                            rs = _sqlStatement.executeQuery();
                            if (rs.next()) {
                                todayCount = rs.getInt(1);
                            }
                            resElement.setLastTimeValue(XMLGregorianCalendarFrom(lastActivity));
                            resElement.setTodayValue(todayCount);
                            resElement.setWeekValue(weekCount);
                        }
                    } else if (s.equalsIgnoreCase("bpdata")) {
                        /*
                          <element id='BpData'>
                            <systolic>120</systolic>
                            <date>2011-04-01T14:21:02</date>
                            <diastolic>130</diastolic>
                          </element>
                         *
                         */
                        try
                        {
                            int systolic = Integer.parseInt(getElementStringValueFromList("systolic", children));
                            int diastolic = Integer.parseInt(getElementStringValueFromList("diastolic", children));
                            XMLGregorianCalendar reading = getElementDateValueFromList("date", children);

                            makeDBConnection();
                            java.sql.Date d = new java.sql.Date(reading.toGregorianCalendar().getTimeInMillis());
                            _sqlStatement = _dbConn.prepareStatement("SELECT * FROM `novartis`.`blood_pressures` WHERE user_id = ? AND reading_dt = ?");
                            _sqlStatement.setInt(1, consumerID);
                            _sqlStatement.setDate(2, d);
                            ResultSet rs = _sqlStatement.executeQuery();
                            if (rs.next()) {
                                _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`blood_pressures` SET systolic = ?, diastolic = ? WHERE user_id = ? AND reading_dt = ?");
                                _sqlStatement.setInt(1, systolic);
                                _sqlStatement.setInt(2, diastolic);
                                _sqlStatement.setInt(3, consumerID);
                                _sqlStatement.setDate(4, d);
                                _sqlStatement.execute();
                            } else {
                                _sqlStatement = _dbConn.prepareStatement("INSERT INTO `novartis`.`blood_pressures` (user_id, systolic, diastolic, reading_dt) VALUES (?, ?, ?, ?)");
                                _sqlStatement.setInt(1, consumerID);
                                _sqlStatement.setInt(2, systolic);
                                _sqlStatement.setInt(3, diastolic);
                                _sqlStatement.setDate(4, d);
                                _sqlStatement.execute();
                            }
                            resElement.setElementstatus("OK");
                            resElement.setElementstatusmessage("");
    if((systolic > 180) || (diastolic > 110) || (systolic < 90) || (diastolic < 60)) {
            HttpURLConnection conn = null;
            UserData_dbobj userdata = new UserData_dbobj();
            userdata.setConsumer_id(consumerID);
            userdata.FetchUserData(consumerID);


            String strResponseXml = "";
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            String strRequestXml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:pae=\"http://eps.novartis.com/wsdl/pae\" xmlns:xsd=\"http://to.service.pae.eps.novartis.com/xsd\"> " +
                                    "<soap:Header/><soap:Body><pae:sendReportedEvent><pae:param0>" +
            "<xsd:addressLine1>" + userdata.getAddress1() + "</xsd:addressLine1>" +
            "<xsd:addressLine2>" + ((userdata.getAddress2() == null)?"":userdata.getAddress2()) + "</xsd:addressLine2>" +
            "<xsd:adverseEventDescription>Systolic Readings: " + Integer.toString(systolic) + " Diastolic Readings: " + Integer.toString(diastolic) + "</xsd:adverseEventDescription>" +
            "<xsd:city>" + userdata.getCity() + "</xsd:city>" +
            "<xsd:emailAddress>" + userdata.getEmail() + "</xsd:emailAddress>" +
            "<xsd:firstName>" + userdata.getFirst_name() + "</xsd:firstName>" +
            "<xsd:lastName>" + userdata.getLast_name() + "</xsd:lastName>" +
//            "<xsd:messageSubject>" + userdata + "</xsd:messageSubject>" +
//            "<xsd:messageSubjectOther>" + userdata + "</xsd:messageSubjectOther>" +
            "<xsd:productName>GOT-" + userdata.getDrugName().toUpperCase() +  "</xsd:productName>" +

           // "<xsd:reportingDate>" + this.XMLGregorianCalendarFrom(new Date()) + "</xsd:reportingDate>" +

            "<xsd:reportingDate>" + DatatypeFactory.newInstance().newXMLGregorianCalendar(cal) + "</xsd:reportingDate>" +

            "<xsd:source>Get On Track Blood Pressure Reading</xsd:source>" +
            "<xsd:state>" + userdata.getState() + "</xsd:state>" +
            "<xsd:suppressionAutoAcknowledgement>Y</xsd:suppressionAutoAcknowledgement>" +
            "<xsd:telephoneNumber>" + ((userdata.getPhone() == null)?"":userdata.getPhone()) + "</xsd:telephoneNumber>" +
            "<xsd:transactionID>IPS" + userdata.getContactEvent() + "</xsd:transactionID>" +
            "<xsd:zip>" + userdata.getZip() + "</xsd:zip>" +
            "</pae:param0></pae:sendReportedEvent></soap:Body></soap:Envelope>";

            try {
                String hostName = "";
		try {
			java.net.InetAddress localMachine = java.net.InetAddress
					.getLocalHost();
			hostName = localMachine.getHostName();
		} catch (java.net.UnknownHostException uhe) {
			// handle exception
			//logger.error("Connector<init>: UnknownHostException : " + uhe, uhe);
			uhe.printStackTrace();
		}

                conn = (HttpURLConnection) new java.net.URL("http://appstaging.ipsh.net:18855/reportedEvents/services/ReportManager/").openConnection();

//                BASE64Encoder enc = new sun.misc.BASE64Encoder();
//                String userpassword = "pae-getontrack:pa3-g3t0ntrack";
//                String encodedAuthorization = enc.encode( userpassword.getBytes() );
                conn.setRequestProperty("Authorization", "Basic " + "cGFlLWdldG9udHJhY2s6cGEzLWczdDBudHJhY2s=");

                conn.setRequestMethod( "POST" );
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
//                    conn.setRequestProperty( "User-Agent", "" );
                conn.setRequestProperty( "Content-Type", "aplication/xml" );
//                   conn.setRequestProperty( "Content-Length", .//length() );
                try {
                    OutputStream os = conn.getOutputStream();
                    os.write( strRequestXml.getBytes() );
                } catch (IOException ioe) {
                //TODO
                }
                try {
                    int rc = conn.getResponseCode();
                    InputStream is = conn.getInputStream();
                    if (is != null) {
                        Writer writer = new StringWriter();
                        char[] buffer = new char[1024];
                        try {
                            Reader reader = new java.io.BufferedReader(
                            new InputStreamReader(is, "UTF-8"));
                            int n;
                            while ((n = reader.read(buffer)) != -1) {
                                writer.write(buffer, 0, n);
                            }
                             strResponseXml = writer.toString();
System.out.println("AE Reporting response:" + strResponseXml);
                        } finally {
                            is.close();
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            catch( IOException e ){
                e.printStackTrace();
            }

}
/***********************************************************************************/
                        } catch (Exception ex) {
                            resElement.setElementstatus("FAIL");
                            resElement.setElementstatusmessage(ex.getMessage());
                        }
                    } else if (s.equalsIgnoreCase("buddygreeting")) {
                        /*
                         *      <element id="BPBuddyGreeting">
                         *      <name></name>
                         *      <message></message>
                         *      </element>
                         */
                        resElement.setBuddyName("Buddy Holiday");
                        resElement.setBuddyGreeting("Welcome to the buddy program!");
                    } else if (s.equalsIgnoreCase("buddydata")) {
                        /*
                         *          <element id="BPBuddyData">
                         *          <first_name></first_name>
                         *          <last_name></last_name>
                         *          <email></email>
                         *          <option></option>
                         *          </element>
                         *
                         */
                        String sTemp = "";
                        String buddyId = getElementStringValueFromList("buddyid", children);
                        if ((children.isEmpty()) || (children.size() == 1)) { // retrieve buddy info or set buddy type
                            ResultSet rs = null;
                            String buddyOption = "";
                            makeDBConnection();
                            buddyOption = getElementStringValueFromList("option", children);
                            if (children.size() == 1 && buddyOption != null && !buddyOption.isEmpty()) { // set buddy type
                                if (buddyOption != null && !buddyOption.isEmpty()) {
                                    if (buddyOption.equalsIgnoreCase("none")) {
                                        sTemp = "N";
                                    } else if (buddyOption.equalsIgnoreCase("virtual")) {
                                        sTemp = "V";
                                    }
                                    _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`user` SET buddy_type = ? WHERE id = ?");
                                    _sqlStatement.setString(1, sTemp);
                                    _sqlStatement.setInt(2, consumerID);
                                    _sqlStatement.executeUpdate();
                                }
                            } else { // retrieve buddy info
                                if (children.size() == 1 && buddyId != null && !buddyId.isEmpty()) {
                                    _sqlStatement = _dbConn.prepareStatement("SELECT * FROM `novartis`.`user` WHERE id = ? AND buddy_id = ?");
                                    _sqlStatement.setInt(1, consumerID);
                                    _sqlStatement.setString(2, buddyId);
                                    rs = _sqlStatement.executeQuery();
                                } else if (children.size() == 0) {
                                    _sqlStatement = _dbConn.prepareStatement("SELECT * FROM `novartis`.`user` WHERE id = ?");
                                    _sqlStatement.setInt(1, consumerID);
                                    rs = _sqlStatement.executeQuery();
                                }
                                if ((rs == null)) {
                                    resElement.setElementstatus("ERROR");
                                    resElement.setElementstatusmessage("Invalid request for buddy data");
                                } else if (!rs.next()) {
                                    resElement.setElementstatus("ERROR");
                                    resElement.setElementstatusmessage("Error accessing user profile information.");
                                } else {
                                    buddyOption = rs.getString("buddy_type");
                                    if (buddyOption.equalsIgnoreCase("N")) {
                                        resElement.setOption("none");
                                    } else if(buddyOption.equalsIgnoreCase("V")){
                                        resElement.setOption("virtual");
                                    } else {
                                        int buddyID = rs.getInt("buddy_id");
                                        _sqlStatement = _dbConn.prepareStatement("SELECT * FROM `novartis`.`buddy` WHERE id = ?");
                                        _sqlStatement.setInt(1, buddyID);
                                        rs = _sqlStatement.executeQuery();
                                        if (!rs.next()) {
                                            resElement.setElementstatus("ERROR");
                                            resElement.setElementstatusmessage("Error accessing buddy profile information.");
                                        } else {
                                            resElement.setOption("live");
                                            resElement.setEmail(rs.getString("buddy_email"));
                                            resElement.setFirstName(rs.getString("buddy_fname"));
                                            resElement.setLastName(rs.getString("buddy_lname"));
                                            // "In", "Out", and "Pending" in the <opt_in_status>
                                            resElement.setOptInStatus(rs.getString("opt_in_out_flag"));
                                            resElement.setBuddyid(rs.getString("id"));
                                        }
                                    }
                                }
                            }
                        } else { // store buddy info
                            String fname = getElementStringValueFromList("first_name", children);
                            String lname = getElementStringValueFromList("last_name", children);
                            String email = getElementStringValueFromList("email", children);
                            String option = getElementStringValueFromList("option", children);
                            if (buddyId == null || buddyId.isEmpty()) { // insert new buddy record
                                BPBuddyData_dbobj buddy = new BPBuddyData_dbobj();
                                buddy.BuddyData(consumerID, fname, lname, email, option);
                                resElement.setConsumerid(buddy.getConsumer_id());
                                resElement.setBuddyid(buddy.getNewKey());
                                resElement.setElementstatus(buddy.getElementstatus());
                                resElement.setElementstatusmessage(buddy.getElementstatusmessage());
                            } else { // update exsiting buddy record
                                String optIn = getElementStringValueFromList("opt_in_status", children);
                                makeDBConnection();
                                _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`buddy` SET buddy_fname = ?, buddy_lname = ?, buddy_email = ?, opt_in_out_flag = ? WHERE id = ?");
                                _sqlStatement.setString(1, fname);
                                _sqlStatement.setString(2, lname);
                                _sqlStatement.setString(3, email);
                                _sqlStatement.setString(4, optIn);
                                _sqlStatement.setString(5, buddyId);
                                if (_sqlStatement.executeUpdate() != 1) {
                                    resElement.setElementstatus("ERROR");
                                    resElement.setElementstatusmessage("Unable to update buddy data.");
                                }
                                resElement.setBuddyid(buddyId);
                            }
                        }
                    } else if (s.equalsIgnoreCase("contactusdata")) {
                        /*
                         *       <element id="ContactUsData">
                         *       <email></email>
                         *       <message_type></message_type>
                         *       <other></other>
                         *       <title></title>
                         *       <first_name></first_name>
                         *       <last_name></last_name>
                         *       <address_line1></address_line1>
                         *       <address_line2></address_line2>
                         *       <city></city>
                         *       <state></state>
                         *       <zip></zip>
                         *       <phone></phone>
                         *       <comments></comments>
                         *
                         */
/***********************************************************************************/

            HttpURLConnection conn = null;
            UserData_dbobj userdata = new UserData_dbobj();
            userdata.setConsumer_id(consumerID);
            userdata.FetchUserData(consumerID);

            String strResponseXml = "";
            String strRequestXml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://eps.pharma.novartis.com/wsdl/contactus\"> " +
                                    "<soapenv:Header /><soapenv:Body><con:process soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                                    "<entries xsi:type=\"x-:Map\" xmlns:x-=\"http://xml.apache.org/xml-soap\">" +
                                    "<item xsi:type=\"x-:mapItem\">" +
                                    "<key>fromEmailAddress</key>" +
                                    "<value>" + getElementStringValueFromList("email", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\">" +
//	       <!-- 'My message is about' -->
                                    "<key>emailSubject</key><value>Get on track program</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\">" +
                                    "<key>Subject other</key>" +
                                    "<value>" + getElementStringValueFromList("other", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>Salutation</key>" +
                                    "<value>" + getElementStringValueFromList("title", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>First Name</key>" +
                                    "<value>" + getElementStringValueFromList("first_name", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>Last Name</key>" +
                                    "<value>" + getElementStringValueFromList("last_name", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>Address 1</key>" +
                                    "<value>" + getElementStringValueFromList("address_line1", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>Address 2</key>" +
                                    "<value>" + getElementStringValueFromList("address_line2", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>City</key>" +
                                    "<value>" + getElementStringValueFromList("city", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>State</key>" +
                                    "<value>" + getElementStringValueFromList("state", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>Zip</key>" +
                                    "<value>" + getElementStringValueFromList("zip", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>Phone</key>" +
                                    "<value>" + getElementStringValueFromList("phone", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>Comments</key>" +
                                    "<value>" + getElementStringValueFromList("comments", children) + "</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
                                    "<key>epsURLOrigin</key><value>GET ON TRACK</value>" +
                                    "</item><item xsi:type=\"x-:mapItem\"> " +
//               <!-- Your Hard coded Ip Address -->
//               <key>client.ip</key>
//               <value>74.55.219.42</value>
                                    "</item></entries></con:process></soapenv:Body></soapenv:Envelope>";
            try {
                String hostName = "";
		try {
			java.net.InetAddress localMachine = java.net.InetAddress
					.getLocalHost();
			hostName = localMachine.getHostName();
		} catch (java.net.UnknownHostException uhe) {
			// handle exception
			//logger.error("Connector<init>: UnknownHostException : " + uhe, uhe);
			uhe.printStackTrace();
		}

                conn = (HttpURLConnection) new java.net.URL("http://appstaging.ipsh.net:7979/contactus/services/ContactUs").openConnection();

//                BASE64Encoder enc = new sun.misc.BASE64Encoder();
//                String userpassword = "partner1:partner1";
//                String encodedAuthorization = enc.encode( userpassword.getBytes() );
                conn.setRequestProperty("Authorization", "Basic " + "cGFydG5lcjE6cGFydG5lcjE=");
                conn.setRequestProperty("SOAPAction", "");

                conn.setRequestMethod( "POST" );
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
//                    conn.setRequestProperty( "User-Agent", "" );
                conn.setRequestProperty( "Content-Type", "aplication/xml" );
//                   conn.setRequestProperty( "Content-Length", .//length() );
                try {
                    OutputStream os = conn.getOutputStream();
                    os.write( strRequestXml.getBytes() );
                } catch (IOException ioe) {
                //TODO
                }
                try {
                    int rc = conn.getResponseCode();
                    InputStream is = conn.getInputStream();
                    if (is != null) {
                        Writer writer = new StringWriter();
                        char[] buffer = new char[1024];
                        try {
                            Reader reader = new java.io.BufferedReader(
                            new InputStreamReader(is, "UTF-8"));
                            int n;
                            while ((n = reader.read(buffer)) != -1) {
                                writer.write(buffer, 0, n);
                            }
                             strResponseXml = writer.toString();
System.out.println("Contact US Reporting response:" + strResponseXml);
                        } finally {
                            is.close();
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            catch( IOException e ){
                e.printStackTrace();
            }
/***********************************************************************************/
                        resElement.setElementstatus("OK");
                        resElement.setElementstatusmessage("");

                    } else if (s.equalsIgnoreCase("enrollmentdrugchoicedata")) {
                        //<element id="EnrollmentDrugChoiceData">
                        //<option></option>
                        //</element>
                        if (children.isEmpty()) {
                            Response.Page.Elements.Element.Drug drugs;
                            List<Response.Page.Elements.Element.Drug> druglist = resElement.getDrug();
                            makeDBConnection();
                            _sqlStatement = _dbConn.prepareStatement("SELECT id, drug_name, nvs_answer_id, drug_order FROM `novartis`.`drugs`");
                            ResultSet rs = _sqlStatement.executeQuery();
                            while (rs.next()) {
                                drugs = responseOF.createResponsePageElementsElementDrug();
                                drugs.setDrugName(rs.getString("drug_name"));
                                drugs.setDrugUniqueId(rs.getString("id"));
                                drugs.setDrugNvsAnswer(rs.getString("nvs_answer_id"));
                                drugs.setOrder(BigInteger.valueOf(rs.getInt("drug_order")));
                                druglist.add(drugs);
                            }
                        } else {
                            String drugID = getElementStringValueFromList("drug_unique_id", children);
                            makeDBConnection();
                            // See if this drug is already set for this user. If so, don't try to re-insert it.
                            _sqlStatement = _dbConn.prepareStatement("SELECT * FROM `novartis`.`user_drugs` WHERE user_id = ? AND drug_id = ?");
                            _sqlStatement.setInt(1, consumerID);
                            _sqlStatement.setString(2, drugID);
                            ResultSet rs = _sqlStatement.executeQuery();
                            if (!rs.next())
                            { // Nope, need to insert it.
                                _sqlStatement = _dbConn.prepareStatement("INSERT INTO `novartis`.`user_drugs`(user_id, drug_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                                _sqlStatement.setInt(1, consumerID);
                                _sqlStatement.setString(2, drugID);
                                _sqlStatement.executeUpdate();
                            }
                        }
                    } else if (s.equalsIgnoreCase("typingquestions1to4")) {
                        //<element id="TypingQuestion1To4">
                        //<question1></question1>
                        //<question2></question2>
                        //<question3></question3>
                        //<question4></question4>
                        //</element>
                        int q1, q2, q3, q4;
                        try { //throws NumberFormatExcepion
                            q1 = Integer.parseInt(getElementStringValueFromList("question1", children));
                            q2 = Integer.parseInt(getElementStringValueFromList("question2", children));
                            q3 = Integer.parseInt(getElementStringValueFromList("question3", children));
                            q4 = Integer.parseInt(getElementStringValueFromList("question4", children));
                        } catch (Exception ex) {
                            throw new Exception("Questions 1 - 4 must be integers! : " + ex.getMessage());
                        }

                        TypingQuestion1To4_dbobj tq = new TypingQuestion1To4_dbobj();
                        tq.typpinQ1to4(consumerID, q1, q2, q3, q4);
                        resElement.setElementstatus(tq.getElementstatus());
                        resElement.setElementstatusmessage(tq.getElementstatusmessage());
                    } else if (s.equalsIgnoreCase("typingquestions5to7")) {
                        // <element id="TypingQuestion5To7">
                        //<question5></question5>
                        //<question6></question6>
                        //<question7></question7>
                        //</element>
                        int q5, q6, q7;

                        try { //throws NumberFormatExcepion
                            q5 = Integer.parseInt(getElementStringValueFromList("question5", children));
                            q6 = Integer.parseInt(getElementStringValueFromList("question6", children));
                            q7 = Integer.parseInt(getElementStringValueFromList("question7", children));
                        } catch (Exception ex) {
                            throw new Exception("Questions 5 - 7 must be integers! : " + ex.getMessage());
                        }

                        TypingQuestion5To7_dbobj tq = new TypingQuestion5To7_dbobj();
                        tq.typpinQ5to7(consumerID, q5, q6, q7);
                        resElement.setElementstatus(tq.getElementstatus());
                        resElement.setElementstatusmessage(tq.getElementstatusmessage());
                        UserData_dbobj userdata = new UserData_dbobj();
                        userdata.setConsumer_id(consumerID);
                        userdata.FetchUserData(consumerID);
                        userdata.AssignCodeSegment();

                        if (userdata.isEnrolledNovartis()) {
                            userdata.UpdateNovartisIRMA();
                        } else { // only do this on first registration?
                            if(!userdata.UpdateNovartisIRMA(true)){
                                resElement.setAlreadyEnrolled("1");
                            }
                        }
                    } else if (s.equalsIgnoreCase("trackerbp")) {
                        /*
                        <?xml version='1.0' encoding='utf-8'?>
                        <response>
                          <page>
                            <elements>
                              <element id='TrackerBP' >
                                <readings>
                                  <reading>
                                    <systolic>144</systolic>
                                    <date>2011-04-01T14:21:02Z</date>
                                    <diastolic>90</diastolic>
                                    <systolic_out_of_range>1</sysstolic_out_of_range>
                                    <diastolic_out_of_range>0</diastolic_out_of_range>
                                  </reading>
                                  <reading>
                                    <systolic>141</systolic>
                                    <date>2011-04-05T14:21:02Z</date>
                                    <diastolic>88</diastolic>
                                    <systolic_out_of_range>1</sysstolic_out_of_range>
                                    <diastolic_out_of_range>0</diastolic_out_of_range>
                                  </reading>
                                </readings>
                                <elementstatus>Ok</elementstatus>
                                <elementstatusmessage></elementstatusmessage>
                              </element>
                            </elements>
                                </page>
                                <status>OK</status>
                                <statusmessage>OK</statusmessage>
                        </response>
                         */
                        XMLGregorianCalendar startDateIn = getElementDateValueFromList("start_date", children);
                        XMLGregorianCalendar endDateIn = getElementDateValueFromList("end_date", children);
                        java.sql.Date startDate = new java.sql.Date(startDateIn.toGregorianCalendar().getTimeInMillis());
                        java.sql.Date endDate = new java.sql.Date(endDateIn.toGregorianCalendar().getTimeInMillis());
                        Response.Page.Elements.Element.Readings.Reading reading;
                        Response.Page.Elements.Element.Readings readings = new Response.Page.Elements.Element.Readings();
                        List<Response.Page.Elements.Element.Readings.Reading> readingList = readings.getReading();

                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("SELECT systolic, systolic_oor_flag, diastolic, diastolic_oor_flag, reading_dt FROM `novartis`.`blood_pressures` WHERE (user_id = ?) AND (reading_dt between ? and ?)");
                        _sqlStatement.setInt(1, consumerID);
                        _sqlStatement.setDate(2, startDate);
                        _sqlStatement.setDate(3, endDate);
                        ResultSet rs = _sqlStatement.executeQuery();
                        while (rs.next()) {
                            reading = responseOF.createResponsePageElementsElementReadingsReading();
                            reading.setDate(XMLGregorianCalendarFrom(rs.getDate("reading_dt")));
                            reading.setDiastolic(BigInteger.valueOf(rs.getInt("diastolic")));
                            reading.setDiastolicOutOfRange(BigInteger.valueOf(rs.getInt("diastolic_oor_flag")));
                            reading.setSystolic(BigInteger.valueOf(rs.getInt("systolic")));
                            reading.setSystolicOutOfRange(BigInteger.valueOf(rs.getInt("systolic_oor_flag")));
                            readingList.add(reading);
                        }
                        resElement.setReadings(readings);
                    } else if (s.equalsIgnoreCase("allremindersdelete")) {
                        //<element id="AllRemindersDelete">
                        //<selected></selected>
                        //</element>
                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`scheduler` SET schedule_active=false WHERE user_id = ? AND schedule_type='USER'");
                        _sqlStatement.setInt(1, consumerID);
                        if (_sqlStatement.executeUpdate() < 1) {
                            resElement.setElementstatus("ERROR");
                            resElement.setElementstatusmessage("Error deleting reminder records");
                        }
                    } else if (s.equalsIgnoreCase("tipforbp")) {
                        //<element id="TipForBp">
                        //<Systolic_value></Systolic_value>
                        //<Diastolic_value></Diastolic_value>
                        //</element>
                        resElement.setMessage("You can lower your BP by cutting sodium out of your diet");
                    } else if (s.equalsIgnoreCase("dashboardlastbpreading")) {
                        /*
                            <element id="DashboardLastBpReading">
                                <bp_message>Dash Board Last BPReading</bp_message>
                                <date>2011-04-01T00:00:00-08:00</date>
                                <systolic>120</systolic>
                                <diastolic>130</diastolic>
                                <elementstatus>OK</elementstatus>
                                <elementstatusmessage></elementstatusmessage>
                            </element>

                            [Conditional response  -- Systolic 140-179 / Diastolic 90-109]
                            Your blood pressure is too high, according to this reading. Only your doctor can tell you what numbers are healthy for you, so to talk to them about setting a target.

                            [Conditional response  -- Systolic 120-139 / Diastolic 80-89]
                            Your blood pressure is a bit above what is considered normal and healthy, according to this reading. Work with your doctor to set a healthy target number to work toward.

                            [Conditional response  -- Systolic 91-119 / Diastolic 61-79]
                            Your blood pressure is in a range that is considered normal and healthy, according to this reading. Check in with your doctor to make sure this is a healthy range to stay in.

                            [Conditional response  -- Systolic over 180 / Diastolic over 110]
                            Warning. Your blood pressure is at a severely high level, according to this reading. Please talk to your doctor right away.

                            [Conditional response  -- Systolic under 90 / Diastolic under 60]
                            Warning. Your blood pressure is at a severely low level, according to this reading. Please talk to your doctor right away
                         */
                        makeDBConnection();
                        ResultSet rs;
                        _sqlStatement = _dbConn.prepareStatement("SELECT systolic, diastolic, reading_dt FROM `novartis`.`blood_pressures` WHERE (user_id = ?) " +
                                                                    "ORDER BY reading_dt DESC limit 1");
                        _sqlStatement.setInt(1, consumerID);
                        rs = _sqlStatement.executeQuery();
                        if (rs.next()) {
                            resElement.setDate(XMLGregorianCalendarFrom(rs.getDate("reading_dt")));
                            int systolic = rs.getInt("systolic");
                            int diastolic = rs.getInt("diastolic");
                            resElement.setSystolic(systolic);
                            resElement.setDiastolic(diastolic);
                            if (((systolic >= 140) && (systolic <= 179)) || ((diastolic >= 90) && (diastolic <= 109)))
                                    resElement.setBpMessage("Your blood pressure is too high, according to this reading. Only your doctor can tell you what numbers are healthy for you, so to talk to them about setting a target.");
                            if (((systolic >= 120) && (systolic <= 139)) || ((diastolic >= 80) && (diastolic <= 89)))
                                    resElement.setBpMessage("Your blood pressure is a bit above what is considered normal and healthy, according to this reading. Work with your doctor to set a healthy target number to work toward.");
                            if (((systolic >= 91) && (systolic <= 119)) || ((diastolic >= 61) && (diastolic <= 79)))
                                    resElement.setBpMessage("Your blood pressure is in a range that is considered normal and healthy, according to this reading. Check in with your doctor to make sure this is a healthy range to stay in.");
                            if ((systolic > 180) || (diastolic > 110))
                                    resElement.setBpMessage("Warning. Your blood pressure is at a severely high level, according to this reading. Please talk to your doctor right away.");
                            if ((systolic < 90) || (diastolic < 60))
                                    resElement.setBpMessage("Warning. Your blood pressure is at a severely low level, according to this reading. Please talk to your doctor right away.");
                        }
                    } else if (s.equalsIgnoreCase("dashboardhealthreminder")) {
                        //<element id="DashBoardHealthReminder">
                        //<Next_Remind_Time></Next_Remind_Time>
                        //<Next_Remind_Message></Next_Remind_Message>
                        //</element>

                        /*
                        GregorianCalendar now = new GregorianCalendar();

                        DatatypeFactory df = DatatypeFactory.newInstance();
                        XMLGregorianCalendar gcTime =
                        df.newXMLGregorianCalendarTime(
                        now.get( Calendar.HOUR_OF_DAY ),
                        now.get( Calendar.MINUTE ),
                        now.get( Calendar.SECOND ),
                        null,
                        DatatypeConstants.FIELD_UNDEFINED );
                         */
                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("SELECT * FROM `novartis`.`scheduler` WHERE user_id = ? AND schedule_type='USER' AND schedule_active = true ORDER BY start_date LIMIT 1");
                        _sqlStatement.setInt(1, consumerID);
                        ResultSet rs = _sqlStatement.executeQuery();
                        if (rs.next()) {
                            String type = rs.getString("schedule_subtype");
                            if (type.equalsIgnoreCase("med")) {
                                resElement.setNextRemindMessage("Take your medication");
                            } else if (type.equalsIgnoreCase("rx")) {
                                resElement.setNextRemindMessage("Refill prescription");
                            } else if (type.equalsIgnoreCase("md")) {
                                resElement.setNextRemindMessage("Doctor's appointment");
                            }
                            resElement.setNextRemindTime(XMLGregorianCalendarFrom(rs.getDate("start_date")));
                        }
                        _dbConn.close();

                    } else if (s.equalsIgnoreCase("DashBoardBuddyMessage")) {
                        // <element id="DashBoardBuddyMessage">
                        // </element>
                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("SELECT `user`.buddy_type, buddy.* FROM `novartis`.`user` LEFT JOIN `novartis`.`buddy` ON buddy_id = `buddy`.id  WHERE `user`.id = ?");
                        _sqlStatement.setInt(1, consumerID);
                        ResultSet rs = _sqlStatement.executeQuery();
                        int i = Integer.parseInt(Long.toString(Math.round(Math.random() * 3) + 1));
                        if (rs.next()) {
                            String buddyOption = rs.getString("buddy_type");
                            if (buddyOption.equalsIgnoreCase("n")) {
                                resElement.setMessage("Taking on high BP can be a lot to handle. Doing it with a little extra support from a BP Buddy, along with your doctor, may make it a little easier.");
                            } else if (buddyOption.equalsIgnoreCase("l")) {
                                if (rs.getString("buddy.opt_in_out_flag").equalsIgnoreCase("pending")) {
                                    resElement.setMessage("Your BP Buddy request has been sent. As soon as " + rs.getString("buddy.buddy_fname") + " accepts your request, we'll let you know. ");
                                } else {
                                    switch (i) {
                                        case 1:
                                            resElement.setMessageTitle("How do you roll?");
                                            resElement.setMessage("Next time you want to lower your BP, call your buddy and tell them you want to go bowling. That's right. Whether you throw strikes or gutter balls, it's exercise, and it all can make a difference for your high BP.");
                                            break;
                                        case 2:
                                            resElement.setMessageTitle("Super-sized sodium");
                                            resElement.setMessage("America is all about super-sized portions, and they can come with super-sized amounts of sodium, which can overload your salt intake. Next time you and your buddy are out to eat, try sharing a salad and main course, and making the meal a little more BP-friendly.");
                                            break;
                                        case 3:
                                            resElement.setMessageTitle("Did you try videogames?");
                                            resElement.setMessage("Videogames have changed a lot in the last few years. The latest batch from the big game makers is all about one thing: moving your body around. If you're looking for something different to do with friends (and for your high BP), maybe this is it.");
                                            break;
                                        case 4:
                                            resElement.setMessageTitle("Meet you at the mall");
                                            resElement.setMessage("Malls are good for more than shopping, they can also help your high BP. Grab your buddy and go for a lap or three around it all, window-shopping along the way. One little warning: the smells that come out of the food court can be too much to resist. If you feel the need to snack at the end of it all, bring along something healthy like an apple or carrots.");
                                            break;
                                    }
                                }
                            } else if (buddyOption.equalsIgnoreCase("v")) {
                                switch (i) {
                                    case 1:
                                        resElement.setMessageTitle("How do you roll?");
                                        resElement.setMessage("Next time you want to lower your BP, call some friends and tell them you want to go bowling. That's right. Whether you throw strikes or gutter balls, it's exercise, and it all can make a difference for your high BP.");
                                        break;
                                    case 2:
                                        resElement.setMessageTitle("Super-sized sodium");
                                        resElement.setMessage("America is all about super-sized portions, and they can come with super-sized amounts of sodium, which can overload your salt intake. Next time you're out to eat, try sharing a salad and main course with a friend, and making the meal a little more BP-friendly.");
                                        break;
                                    case 3:
                                        resElement.setMessageTitle("Did you try videogames?");
                                        resElement.setMessage("Videogames have changed a lot in the last few years. The latest batch from big game makers is all about one thing: moving your body around. If you're looking for something different to do with friends (and for your high BP), maybe this is it.");
                                        break;
                                    case 4:
                                        resElement.setMessageTitle("Meet you at the mall");
                                        resElement.setMessage("Malls are good for more than shopping, they can also help your high BP. Grab a friend and go for a lap or three around it all, window-shopping along the way. One little warning: the smells that come out of the food court can be too much to resist. If you feel the need to snack at the end of it all, bring along something healthy like an apple or carrots.");
                                        break;
                                }
                            }
                        } else {
                            resElement.setElementstatus("ERROR");
                            resElement.setElementstatusmessage("Error accessing user profile information.");
                        }
                    } else if (s.equalsIgnoreCase("TipForActivity")) {
                        // no elements defined
                        resElement.setMessage("Try getting outside for a walk, it\'s great excersize.");
                    } else if (s.equalsIgnoreCase("ActivityGoal")) {
                        if (children.isEmpty()) {
                            makeDBConnection();
                            _sqlStatement = _dbConn.prepareStatement("SELECT activity_goal FROM `novartis`.`user` WHERE id = ?");
                            _sqlStatement.setInt(1, consumerID);
                            ResultSet rs = _sqlStatement.executeQuery();
                            if (rs.next()) {
                                resElement.setGoal(rs.getString("activity_goal"));
                            } else {
                                resElement.setElementstatus("FAIL");
                                resElement.setElementstatusmessage("Unable to retrieve activity goal.");
                            }
                        } else { // non-empty child, save the new activity goal
                            makeDBConnection();
                            _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`user` SET activity_goal = ? WHERE id = ?");
                            _sqlStatement.setInt(1, Integer.parseInt(getElementStringValueFromList("goal", children)));
                            _sqlStatement.setInt(2, consumerID);
                            if (_sqlStatement.executeUpdate() != 1) {
                                resElement.setElementstatus("FAIL");
                                resElement.setElementstatusmessage("Unable to update activity goal.");
                            }
                        }
                    } else if (s.equalsIgnoreCase("AgreeToTermsAndConditions")) {
                    } else if (s.equalsIgnoreCase("GetActivityHistory")) {
                        XMLGregorianCalendar startDateIn = getElementDateValueFromList("start_date", children);
                        XMLGregorianCalendar endDateIn = getElementDateValueFromList("end_date", children);
                        java.sql.Date startDate = new java.sql.Date(startDateIn.toGregorianCalendar().getTimeInMillis());
                        java.sql.Date endDate = new java.sql.Date(endDateIn.toGregorianCalendar().getTimeInMillis());
                        Response.Page.Elements.Element.Activity activity;
                        List<Response.Page.Elements.Element.Activity> activityList = resElement.getActivity();

                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("SELECT count(1) FROM `novartis`.`user_activities` WHERE user_id = ? ");
                        _sqlStatement.setInt(1, consumerID);
                        ResultSet rs = _sqlStatement.executeQuery();
                        if (rs.next()) {
                            resElement.setTotalActivities(BigInteger.valueOf(rs.getInt(1)));
                            _sqlStatement = _dbConn.prepareStatement("SELECT `novartis`.`user_activities`.id AS id, activity_id, activity_resource_id, activity_dt FROM `novartis`.`user_activities` LEFT JOIN `novartis`.`activities` ON (`novartis`.`user_activities`.activity_id = `novartis`.`activities`.id) WHERE (user_id = ?) AND (activity_dt between ? and ?)");
                            _sqlStatement.setInt(1, consumerID);
                            _sqlStatement.setDate(2, startDate);
                            _sqlStatement.setDate(3, endDate);
                            rs = _sqlStatement.executeQuery();
                            while (rs.next()) {
                                activity = responseOF.createResponsePageElementsElementActivity();
                                activity.setUserActivityId(rs.getInt("id"));
                                activity.setActivityid(rs.getInt("activity_id"));
                                activity.setDate(XMLGregorianCalendarFrom(rs.getDate("activity_dt")));
                                activity.setResourceid(rs.getString("activity_resource_id"));
                                activityList.add(activity);
                            }
                        } else {
                            resElement.setTotalActivities(BigInteger.ZERO);
                        }
                    } else if (s.equalsIgnoreCase("GetActivityList")) {
                        Response.Page.Elements.Element.Activity activity;
                        List<Response.Page.Elements.Element.Activity> activityList = resElement.getActivity();

                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("SELECT id, activity_name, activity_desc, activity_label, activity_resource_id, activity_group_id, activity_group_order FROM `novartis`.`activities`");
                        ResultSet rs = _sqlStatement.executeQuery();
                        while (rs.next()) {
                            activity = responseOF.createResponsePageElementsElementActivity();
                            activity.setActivityid(rs.getInt("id"));
                            activity.setDescription(rs.getString("activity_desc"));
                            activity.setLabel(rs.getString("activity_label"));
                            activity.setResourceid(rs.getString("activity_resource_id"));
                            activity.setActivitygroupid(rs.getString("activity_group_id"));
                            activity.setActivitygrouporder(rs.getString("activity_group_order"));
                            activityList.add(activity);
                        }
                    } else if (s.equalsIgnoreCase("UpdateActivityStatus")) {
                        /*
                        When  <user_activity_id > is empty, a new user activity record is created.

                        When a  <user_activity_id > supplied,  an existing user activity will be updated .

                        For each user activity, the value will be captured with it. For the GOT system, it is
                        assumed that any activity will have a value of 1, but in future programs, the activity
                        could have a scalar value and a unit (X minutes of exercise, N miles jogged).

                        To remove an activity, use DeleteActivity.

                        2.29.1	Request XML Node Descriptions
                        All Nodes are required except where otherwise noted. Only one of each element is allowed (unless noted below).
                        1)	element "id" attribute – Contains the id of the page element to retrieve.
                        2)	activity - Contains elements related to the activity. This request can contain multiple activity elements.
                        3)	user_activity_id - Integer.
                        4)	value - Integer. A value describing how much of the activity was accomplished. Must be 1 or greater.
                        5)	date - DateTime of the activity. Timezone must be omitted for GOT v1, the time will be assumed to be user's local time.

                        2.29.2	XML Example: Create Activities
                        Since the user_activity_id is empty, these are new activities.
                        Request:
                        <?xml version='1.0' encoding='utf-8'?>
                        <request>
                          <page>
                            <campaignid>2</campaignid>
                            <consumerid>1234</consumerid>
                            <elements>
                              <element id='UpdateActivityStatus'>
                                <activity>
                                                <activityid>101</activityid>
                                                <value>1</value>
                                                <date>2011-12-31T10:30:00</date>
                                </activity>
                                <activity>
                                                <activityid>102</activityid>
                                                <value>1</value>
                                                <date>2011-12-31T10:30:00</date>
                                </activity>
                              </element>
                            </elements>
                          </page>
                        </request>

                        Response:
                        <?xml version='1.0' encoding='utf-8'?>
                        <response>
                          <page>
                            <elements>
                              <element id='UpdateActivityStatus'>
                                <activity>
                                                <user_activity_id>2345678</user_activity_id>
                                </activity>
                                <activity>
                                                <user_activity_id>2345679</user_activity_id>
                                </activity>
                                        <elementstatus>Ok</elementstatus>
                                        <elementstatusmessage></elementstatusmessage>
                             </element>
                            </elements>
                          </page>
                          <status>OK</status>
                                 <statusmessage>OK</statusmessage>
                        </response>

                        2.29.3	XML Example: Update Activities
                        This example displays a request.

                        Request:
                        <?xml version='1.0' encoding='utf-8'?>
                        <request>
                          <page>
                            <campaignid>2</campaignid>
                            <consumerid>1234</consumerid>
                            <elements>
                              <element id='UpdateActivityStatus'>
                                <activity>
                                                <user_activity_id>2345678</user_activity_id>
                                                <value>1</value>
                                                <date>2011-12-31T10:30:00</date>
                                </activity>
                                <activity>
                                                <user_activity_id>2345679</user_activity_id>
                                                <value>1</value>
                                                <date>2011-12-31T10:30:00</date>
                                </activity>
                              </element>
                            </elements>
                          </page>
                        </request>

                        Response:
                        <?xml version='1.0' encoding='utf-8'?>
                        <response>
                          <page>
                            <elements>
                              <element id='UpdateActivityStatus'>
                                <activity>
                                                <user_activity_id>2345678</user_activity_id>
                                </activity>
                                <activity>
                                                <user_activity_id>2345999</user_activity_id>
                                </activity>
                                                <elementstatus>Ok</elementstatus>
                                                <elementstatusmessage></elementstatusmessage>

                             </element>
                            </elements>
                          </page>
                          <status>OK</status>
                                 <statusmessage>OK</statusmessage>
                        </response>
                        */

                        Response.Page.Elements.Element.Activity activity;
                        Request.Page.Elements.Element.Activity activityInput;
                        List<Response.Page.Elements.Element.Activity> activityList = resElement.getActivity();

                        makeDBConnection();
                        for (int i = 0; i < children.size(); i++) {
                            JAXBElement e = (JAXBElement) children.get(i);
                            if (e.getName().getLocalPart().equals("activity")) {
                                try {
                                    activityInput = (Request.Page.Elements.Element.Activity)e.getValue();
//TODO                                    int value = Integer.parseInt(getStringValue(activityInput.getValue()));
                                    int userActivityID = 0;
                                    java.sql.Date d = new java.sql.Date(activityInput.getDate().toGregorianCalendar().getTimeInMillis());
                                    int activityID = activityInput.getActivityid().intValue();

                                    if (activityInput.getUserActivityId() != null) {
                                        userActivityID = activityInput.getUserActivityId().intValue();
                                        _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`user_activities` SET activity_id = ?, activity_dt = ?, value = ? WHERE id = ? AND user_id = ?");
                                        _sqlStatement.setInt(1, activityID);
                                        _sqlStatement.setDate(2, d);
//TODO                                        _sqlStatement.setInt(3, value);
                                        _sqlStatement.setInt(3, 1);
                                        _sqlStatement.setInt(4, userActivityID);
                                        _sqlStatement.setInt(5, consumerID);
                                        _sqlStatement.executeUpdate();
                                    } else { // insert new activity record
                                        _sqlStatement = _dbConn.prepareStatement("INSERT INTO `novartis`.`user_activities`(activity_id, activity_dt, value, user_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                                        _sqlStatement.setInt(1, activityID);
                                        _sqlStatement.setDate(2, d);
//                                        _sqlStatement.setInt(3, value);
                                        _sqlStatement.setInt(3, 1);
                                        _sqlStatement.setInt(4, consumerID);
                                        if (_sqlStatement.executeUpdate() > 0) {
                                            ResultSet insertedKeys = _sqlStatement.getGeneratedKeys();
                                            if (insertedKeys.next())
                                            {
                                                userActivityID = insertedKeys.getInt(1);
                                            }
                                        }
                                    }
                                    activity = responseOF.createResponsePageElementsElementActivity();
                                    activity.setUserActivityId(userActivityID);
                                    activityList.add(activity);
                                } catch (Exception ex) {
                                    resElement.setElementstatus("ERROR");
                                    resElement.setElementstatusmessage(ex.getMessage());
                                }
                            }
                        }
                    } else if (s.equalsIgnoreCase("Logout")) {
                        UserData_dbobj userdata = new UserData_dbobj();
                        userdata.logout(consumerID);
                    } else if (s.equalsIgnoreCase("TipsForActivities")) {
                        Response.Page.Elements.Element.Tips tips = new Response.Page.Elements.Element.Tips();
                        List<Response.Page.Elements.Element.Tips.Tip> tipList = tips.getTip();
                        Response.Page.Elements.Element.Tips.Tip tip;
/*                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("SELECT id, activity_name, activity_desc, activity_label, activity_resource_id FROM `novartis`.`activities`");
                        ResultSet rs = _sqlStatement.executeQuery();
                        while (rs.next()) {
                            activity = responseOF.createResponsePageElementsElementActivity();
                            activity.setActivityid(rs.getInt("id"));
                            activity.setDescription(rs.getString("activity_desc"));
                            activity.setLabel(rs.getString("activity_label"));
                            activity.setResourceid(rs.getString("activity_resource_id"));
                            activityList.add(activity);
                        }

                        _dbConn.close();
*/
                        tip = new Response.Page.Elements.Element.Tips.Tip();
                        tip.setTipName("Healthy Eating");
                        tip.setTipContent("Here is copy for this tip & a smile.");
                        tip.setTipId(1234);
                        tipList.add(tip);

                        resElement.setTips(tips);

                    } else if (s.equalsIgnoreCase("TipsForBP")) {

                        Response.Page.Elements.Element.Tips tips = new Response.Page.Elements.Element.Tips();
                        List<Response.Page.Elements.Element.Tips.Tip> tipList = tips.getTip();
                        Response.Page.Elements.Element.Tips.Tip tip;

/*                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("SELECT id, activity_name, activity_desc, activity_label, activity_resource_id FROM `novartis`.`activities`");
                        ResultSet rs = _sqlStatement.executeQuery();
                        while (rs.next()) {
                            activity = responseOF.createResponsePageElementsElementActivity();
                            activity.setActivityid(rs.getInt("id"));
                            activity.setDescription(rs.getString("activity_desc"));
                            activity.setLabel(rs.getString("activity_label"));
                            activity.setResourceid(rs.getString("activity_resource_id"));
                            activityList.add(activity);
                        }

                        _dbConn.close();
*/
                        tip = new Response.Page.Elements.Element.Tips.Tip();
                        tip.setTipName("Healthy Eating");
                        tip.setTipContent("Here is copy for this tip & a smile.");
                        tip.setTipId(1234);
                        tipList.add(tip);

                        resElement.setTips(tips);
                    } else if (s.equalsIgnoreCase("UnsubscribeReminders")) { //TODO: Is this element currently used?
                    } else if (s.equalsIgnoreCase("DeleteActivity")) {
                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("DELETE FROM `novartis`.`user_activities` WHERE user_id = ? and id = ?");
                        _sqlStatement.setInt(1, consumerID);
                        _sqlStatement.setInt(2, Integer.parseInt(getElementStringValueFromList("user_activity_id", children)));
                        if (_sqlStatement.executeUpdate() != 1) {
                            resElement.setElementstatus("ERROR");
                            resElement.setElementstatusmessage("Error deleting activity record");
                        }
                    } else if (s.equalsIgnoreCase("DeleteReminders")) {
                        if (children.isEmpty()) {
                            resElement.setElementstatusmessage("Please specify reminders to delete");
                            resElement.setElementstatus("FAIL");
                        } else {
                            //TODO: mark the reminders as not active and remove messages from the message queue
                            makeDBConnection();
                            _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`scheduler` SET schedule_active=false WHERE user_id = ? AND schedule_type='USER' AND id = ?");
                            _sqlStatement.setInt(1, consumerID);
                            _sqlStatement.setInt(2, Integer.parseInt(getElementStringValueFromList("reminder_id", children)));
                            if (_sqlStatement.executeUpdate() != 1) {
                                resElement.setElementstatus("ERROR");
                                resElement.setElementstatusmessage("Error deleting reminder record");
                            }
                        }
                    } else if (s.equalsIgnoreCase("Reminders")) {
                        Response.Page.Elements.Element.Reminder reminder;
                        String sTemp;
                        String[] saTemp;
                        if (children.isEmpty()) {
                            // return a list of the reminders for this user
                            List<Response.Page.Elements.Element.Reminder> reminders = resElement.getReminder();
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                            Response.Page.Elements.Element.Reminder.Times times;
                            List<XMLGregorianCalendar> timesList;
                            Response.Page.Elements.Element.Reminder.Weekdays weekdays;
                            List<String> weekdayList;

                            makeDBConnection();
                            _sqlStatement = _dbConn.prepareStatement("SELECT * FROM `novartis`.`scheduler` WHERE user_id = ? AND schedule_type='USER' AND schedule_active = true");
                            _sqlStatement.setInt(1, consumerID);
                            ResultSet rs = _sqlStatement.executeQuery();
                            while (rs.next()) {
                                reminder = new Response.Page.Elements.Element.Reminder();
                                times = new Response.Page.Elements.Element.Reminder.Times();
                                timesList = times.getTime();
                                sTemp = rs.getString("schedule_times");
                                saTemp = sTemp.split("\\;");
                                for(int i =0; i < saTemp.length ; i++)
                                {
                                    try {
                                        Date d = sdf.parse(saTemp[i]);
                                        timesList.add(XMLGregorianCalendarFrom(d));
                                    } catch (Exception ex) { }
                                }
                                reminder.setTimes(times);
                                weekdays = new Response.Page.Elements.Element.Reminder.Weekdays();
                                weekdayList = weekdays.getWeekday();
                                sTemp = rs.getString("active_weekdays");
                                saTemp = sTemp.split(",");
                                for(int i =0; i < saTemp.length ; i++)
                                {
                                    weekdayList.add(saTemp[i]);
                                }
                                reminder.setWeekdays(weekdays);
                                Date d = rs.getDate("start_date");
                                reminder.setStartTime(XMLGregorianCalendarFrom(d)); // TODO: should be start date
                                reminder.setReminderId(BigInteger.valueOf(rs.getInt("id")));
                                reminder.setReminderType(rs.getString("schedule_subtype"));

                                sTemp = rs.getString("message_channels");
                                if (sTemp.contains("email")){
                                    reminder.setEmail(BigInteger.ONE);
                                } else {
                                    reminder.setEmail(BigInteger.ZERO);
                                }
                                if (sTemp.contains("sms")){
                                    reminder.setSms(BigInteger.ONE);
                                } else {
                                    reminder.setSms(BigInteger.ZERO);
                                }
                                reminder.setFrequency(rs.getString("frequency_type"));
                                reminders.add(reminder);
                            }
                            _dbConn.close();
                        } else { //save reminder updates
                            List<Response.Page.Elements.Element.Reminder> reminders = resElement.getReminder();
                            reminder = new Response.Page.Elements.Element.Reminder();
                            Request.Page.Elements.Element.Reminder reminderInput;
                            Request.Page.Elements.Element.Reminder.Times times;
                            List<XMLGregorianCalendar> timesList;
                            Request.Page.Elements.Element.Reminder.Weekdays weekdays;
                            List<String> weekdayList;

                            for (int i = 0; i < children.size(); i++) {
                                JAXBElement e = (JAXBElement) children.get(i);
                                reminderInput = (Request.Page.Elements.Element.Reminder)e.getValue();
                                weekdays = reminderInput.getWeekdays().get(0);
                                weekdayList = weekdays.getWeekday();
                                String weekdayValue = "";
                                for(int j = 0; j < weekdayList.size(); j++) {
                                    weekdayValue = weekdayValue + "," + weekdayList.get(j);
                                }
                                if (weekdayValue.length() > 0) weekdayValue = weekdayValue.substring(1, weekdayValue.length());
                                times = reminderInput.getTimes().get(0);
                                timesList = times.getTime();
                                String timeValue = "";
                                for(int j = 0; j < timesList.size(); j++) {
                                    timeValue = timeValue + ";" + timesList.get(j);
                                }
                                if (timeValue.length() > 0) timeValue = timeValue.substring(1, timeValue.length());
                                String reminderType = reminderInput.getReminderType();
                                String period = reminderInput.getFrequency();
                                java.sql.Date startTime;
                                if (reminderInput.getStartDate() != null) {
                                    startTime = new java.sql.Date(reminderInput.getStartDate().toGregorianCalendar().getTimeInMillis());
                                } else {
                                    startTime = new java.sql.Date(new Date().getTime());
                                }
                                String messageChannels = "";
                                if ((reminderInput.getEmail() != null) && (reminderInput.getEmail().equals(BigInteger.ONE)))
                                        messageChannels = messageChannels + ",email";
                                if ((reminderInput.getSms() != null) && (reminderInput.getSms().equals(BigInteger.ONE)))
                                        messageChannels = messageChannels + ",sms";
                                if (messageChannels.length() > 0) messageChannels = messageChannels.substring(1, messageChannels.length());

                                makeDBConnection();
                                if (reminderInput.getReminderId() == null || reminderInput.getReminderId().equals(BigInteger.ZERO))
                                { // insert new reminder
                                    _sqlStatement = _dbConn.prepareStatement("INSERT INTO `novartis`.`scheduler` (user_id, schedule_type, schedule_active, " +
                                                                            "`active_weekdays`, `frequency_type`, `start_date`, `schedule_times`, " +
                                                                            "`schedule_subtype`, `message_channels`) VALUES (?, 'USER', true, ?, ?, ?, ?, ?, ?)",
                                                                            Statement.RETURN_GENERATED_KEYS);
                                    _sqlStatement.setInt(1, consumerID);
                                    _sqlStatement.setString(2, weekdayValue);
                                    _sqlStatement.setString(3, period);
                                    _sqlStatement.setDate(4, startTime);
                                    _sqlStatement.setString(5, timeValue);
                                    _sqlStatement.setString(6, reminderType);
                                    _sqlStatement.setString(7, messageChannels);
                                    if (_sqlStatement.executeUpdate() > 0) {
                                        ResultSet insertedKeys = _sqlStatement.getGeneratedKeys();
                                        if (insertedKeys.next())
                                        {
                                            int k = insertedKeys.getInt(1);
                                            reminder.setReminderId(BigInteger.valueOf(k));
                                            reminders.add(reminder);
                                        }
                                    } else {
                                        resElement.setElementstatus("ERROR");
                                        resElement.setElementstatusmessage("Error creating new reminder record");
                                    }
                                } else { // update existing reminder
                                    _sqlStatement = _dbConn.prepareStatement("UPDATE `novartis`.`scheduler` SET `active_weekdays` = ?, `frequency_type` = ?, `start_date` = ?, `schedule_times` = ?, `schedule_subtype` = ?, `message_channels` = ? WHERE user_id = ? AND schedule_type='USER' AND id = ?");
                                    _sqlStatement.setInt(7, consumerID);
                                    _sqlStatement.setString(8, reminderInput.getReminderId());
                                    _sqlStatement.setString(1, weekdayValue);
                                    _sqlStatement.setString(2, period);
                                    _sqlStatement.setDate(3, startTime);
                                    _sqlStatement.setString(4, timeValue);
                                    _sqlStatement.setString(5, reminderType);
                                    _sqlStatement.setString(6, messageChannels);
                                    if (_sqlStatement.executeUpdate() != 1) {
                                        resElement.setElementstatus("ERROR");
                                        resElement.setElementstatusmessage("Error creating new reminder record");
                                    } else {
                                        reminder.setReminderId(BigInteger.valueOf(Long.parseLong(reminderInput.getReminderId())));
                                        reminders.add(reminder);
                                    }
                                }
                            }
                        }
                    } else if (s.equalsIgnoreCase("GetActivitySuggestion")) {
                        UserData_dbobj userdata = new UserData_dbobj();
                        userdata.FetchUserData(consumerID);
                        int lastSuggestion = userdata.getLastActivitySuggestion();

                        makeDBConnection();
                        _sqlStatement = _dbConn.prepareStatement("SELECT * FROM `novartis`.`activities` WHERE id NOT IN (SELECT activity_id FROM user_activities WHERE user_id = ?) ORDER BY activity_suggestion_order");
                        _sqlStatement.setInt(1, consumerID);
                        ResultSet rs = _sqlStatement.executeQuery();
                        int firstActivityId = 0;
                        int activityId = 0;
                        boolean pastLastSuggestion = false;
                        while (rs.next())
                        {
                            activityId = rs.getInt("id");
                            if (rs.isFirst()) {
                                firstActivityId = activityId;
                                if (lastSuggestion == 0) pastLastSuggestion = true; // default to first suggestion if none suggested before
                            }
                            if (pastLastSuggestion) {
                                break;
                            }
                            if (activityId == lastSuggestion) pastLastSuggestion = true;
                            if (rs.isLast()) {
                                activityId = firstActivityId;
                            }
                        }
                        if (activityId == 0 && pastLastSuggestion) activityId = firstActivityId;
                        if (activityId != 0) {
                            resElement.setActivityId(BigInteger.valueOf(activityId));
                            userdata.UpdateLastActivitySuggestion(activityId);
                        }
                    } else {
                        throw new Exception("Unknown element type in request.");
                    }
                } catch (PrepException pe) {
                    resElement.setElementstatus("FAIL");
                    resElement.setElementstatusmessage(pe.getMessage());
                } catch (Exception e) {
                    resElement.setElementstatus("ERROR");
                    String errorMessage = e.getMessage();
                    if (errorMessage == null) errorMessage = "Unknown exception thrown while processing element.";
                    resElement.setElementstatusmessage(errorMessage);
                    e.printStackTrace();
                } finally {
                    responseElementList.add(resElement);
                }
            } // for <element>

            responsePage.setElements(responseElements);
            response.setPage(responsePage);
            response.setStatus("ok");
            response.setStatusmessage("OK");


            responseWriter = new StringWriter();
            marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(response, responseWriter);



        } catch (JAXBException e) { // Why is this here? I think we should look at what a FAIL response indicates
            responseWriter = new StringWriter();
            responseWriter.write(generateErrorResponse("FAIL", e));
        } catch (Exception e) {
            e.printStackTrace();
            responseWriter = new StringWriter();
            responseWriter.write(generateErrorResponse("ERROR: ", e));
        } finally {
            try {
                if (_dbConn != null && !_dbConn.isClosed()) _dbConn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responseWriter.toString();
    }

    private String generateErrorResponse(String errtype, Exception e) throws PropertyException, JAXBException {

        com.novartis.connectors.jaxb.GetPageElementsResponse.ObjectFactory responseOF =
                new com.novartis.connectors.jaxb.GetPageElementsResponse.ObjectFactory();
        com.novartis.connectors.jaxb.GetPageElementsResponse.Response errResponse =
                new com.novartis.connectors.jaxb.GetPageElementsResponse.Response();

        com.novartis.connectors.jaxb.GetPageElementsResponse.Response.Page.Elements responseElements = responseOF.createResponsePageElements();

        Response.Page resPage = responseOF.createResponsePage();
        responseContext = JAXBContext.newInstance(
                com.novartis.connectors.jaxb.GetPageElementsResponse.Response.class);
        marshaller = responseContext.createMarshaller();

        resPage.setElements(responseElements);
        errResponse.setPage(resPage);
        errResponse.setStatus(errtype);
        errResponse.setStatusmessage(e.toString());
        responseWriter = new StringWriter();

        marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT,
                true);
        marshaller.marshal(errResponse, responseWriter);

        return responseWriter.toString();

    }
}
