package com.novartis.connectors.database;


import java.math.BigInteger;


/**
 * @author Okoye Victor Gift
 */
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.*;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.StringReader;
import java.io.StringWriter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;


/**
 * @author Okoye Victor Gift
 */
public class UserData_dbobj{




	///////////////////////////////////
	//Start UserData variables.......
	///////////////////////////////////
	private String first_name = "";
	private String last_name = "";
	private Date dob;
	private String gender ="";
	private String address1 ="";
	private String address2 ="";
	private String city ="";
	private String state ="";
	private String zip ="";
	private String email = "";
	private String phone="";
	private String elementstatus="OK";
	private String elementstatusmessage="";
        private String drugName = "";
	private int consumer_id;
	private ResultSet keyResultSet;
	private int insert;
	private int skip_typing;
	private String Result = "";
	private int newKey;
	private Connection conn = null;
	private String ERRORorWARNING = "";
        private int acceptMarketing = 0;
        private int acceptTerms = 0;
        private int highBP = 0;
        private int medications = 0;
        private int smsAccept = 0;
        private String drugAnswerID = "";
        private String nvsIndividualID = "";
        private String nvsCampaignIndivId;
        private int lastActivitySuggestion = 0;
        private String segmentCode = "";


	/////////////////////////////////////////////////////////////////////////
	//Getters and Setters Zone
	/////////////////////////////////////////////////////////////////////////
		public String getERRORorWARNING() {
			return ERRORorWARNING;
		}

		public void setERRORorWARNING(String eRRORorWARNING) {
			ERRORorWARNING = eRRORorWARNING;
		}
		public String getMessages(){
			   return ERRORorWARNING;
		}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getElementstatus() {
		return elementstatus;
	}

        public int getSmsAccept() {
            return this.smsAccept;
        }

        public void setSmsAccept(int smsAccept) {
            this.smsAccept = smsAccept;
        }

	public void setElementstatus(String elementstatus) {
		this.elementstatus = elementstatus;
	}

	public String getElementstatusmessage() {
		return elementstatusmessage;
	}
	public void setElementstatusmessage(String elementstatusmessage) {
		this.elementstatusmessage = elementstatusmessage;
	}
	public ResultSet getKeyResultSet() {
		return keyResultSet;
	}
	public void setKeyResultSet(ResultSet keyResultSet) {
		this.keyResultSet = keyResultSet;
	}
	public int getInsert() {
		return insert;
	}
	public void setInsert(int insert) {
		this.insert = insert;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public int getNewKey() {
		return newKey;
	}
	public void setNewKey(int newKey) {
		this.newKey = newKey;
	}
	public int getConsumer_id() {
		return consumer_id;
	}
	public void setConsumer_id(int consumer_id) {
		this.consumer_id = consumer_id;
	}
	public int getSkip_typing() {
		return skip_typing;
	}
	public void setSkip_typing(int skip_typing) {
		this.skip_typing = skip_typing;
	}
        public int getLastActivitySuggestion(){
            return lastActivitySuggestion;
        }
        public void setLastActivitySuggestion(int lastActivitySuggestion){
            this.lastActivitySuggestion = lastActivitySuggestion;
        }
        public String getDrugName(){
            return drugName;
        }
        public String getSegmentCode(){
            return segmentCode;
        }

	public void makeDBConnection()throws Exception{
	try{
	    Context ctx = new InitialContext();
	    DataSource ds = (DataSource)ctx.lookup("java:/jdbc/novartis");
	    conn = ds.getConnection();
	   }catch(Exception e) {
	        System.out.println("SQL Exception is caught during connection creation."+e);
	        ERRORorWARNING = e.getMessage().toString();
	        conn.close();
	     }
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	public void insertNewUserData(String first_name, String last_name, Date dob, String gender, String address1, String address2, String city, String state, String zip, String email, String phone, String smsAccept)
                throws Exception {
		PreparedStatement stat = null;
		int myCheck = CheckEmail(email);
		if(myCheck == 0){
		try {
		     java.sql.Date sd = new java.sql.Date(dob.getTime());
		      makeDBConnection();
		      stat = conn.prepareStatement("INSERT INTO `novartis`.`user`(user_fname, user_lname, dob, gender, address_1, address_2, city, state, post_code, email, user_mphone, sms_accept) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		      stat.setString(1, first_name);
		      stat.setString(2, last_name);
		      stat.setDate(3, sd);
		      stat.setString(4, gender);
		      stat.setString(5, address1);
		      stat.setString(6, address2);
		      stat.setString(7, city);
		      stat.setString(8, state);
		      stat.setString(9, zip);
		      stat.setString(10, email);
		      stat.setString(11, phone);
                      stat.setString(12, smsAccept);
		      setInsert(stat.executeUpdate());
		      setKeyResultSet(stat.getGeneratedKeys());

		      if (keyResultSet.next()) {
		       setNewKey(keyResultSet.getInt(1));
		      }
		      if(insert > 0 && getNewKey() > 0) {
		    	  setConsumer_id(getNewKey());
		    	  setElementstatus("OK");
		    	  setSkip_typing(0);
		    	  setElementstatusmessage("new profile has been set");
	    		  stat.close();
		      }
		      if(insert <= 0 && newKey <= 0) {
		    	  setConsumer_id(getConsumer_id());
		    	  setElementstatus("FAIL");
		    	  setSkip_typing(1);
		    	  setElementstatusmessage("Email already exists.");
	    		  stat.close();
		      }
		      else{
		    	    setConsumer_id(consumer_id);
		    	    setFirst_name(first_name);
		    	    setLast_name(last_name);
		    	    setDob(dob);
		    	    setGender(gender);
		    	    setAddress1(address1);
		    	    setAddress2(address2);
		    	    setCity(city);
		    	    setState(state);
		    	    setZip(zip);
		    	    setEmail(email);
		    	    setPhone(phone);
		    	    setElementstatus("FAIL");
		    	    setSkip_typing(1);
		    	    setElementstatusmessage(getMessages());
		    	    stat.close();
		      }
		        conn.close();
		        stat.close();
		    } catch (Exception e) {
		    	 ERRORorWARNING = e.getMessage();
                         setConsumer_id(getConsumer_id());
		    	 setElementstatus("ERROR");
		    	 setElementstatusmessage(getMessages());
                         conn.close();
		    } finally {
		      try {
		    	   conn.close();
		      } catch (SQLException e) {
		    	  ERRORorWARNING = e.getMessage();
                          setConsumer_id(getConsumer_id());
                          setElementstatus("ERROR");
		    	  setElementstatusmessage(getMessages());
		    	  conn.close();
		      }
		    }
		}
	  }

        public int CheckEmail(String email)throws Exception{
		PreparedStatement stat = null;
		int v = 0;
		try {
		      makeDBConnection();
		      stat = conn.prepareStatement("SELECT * FROM `novartis`.`user` WHERE email = ?");
		      stat.setString(1, email);
		      ResultSet rs = stat.executeQuery();
		      if(rs.next()) {
		    	  v = 1;
                          int consumerID = Integer.parseInt(rs.getString(1));
		    	  setConsumer_id(consumerID);
		    	  setElementstatus("OK");
		    	  setSkip_typing(1);
		    	  setElementstatusmessage("Email Address already exist..");
	    		  stat.close();
		      }
		      else {
		    	  v = 0;
		      }
		        conn.close();
		        stat.close();
		    } catch (Exception e) {
		    	ERRORorWARNING = e.getMessage();
		    	conn.close();
		    } finally {
		      try {
		    	   conn.close();
		      } catch (SQLException e) {
		    	  ERRORorWARNING = e.getMessage();
		    	   conn.close();
		      }
		    }
		    return v;
	  }
//////////////////////////////////////////////////////////////////////////////////////////
        public void UpdateLastActivitySuggestion(int lastActivitySuggestion){
            PreparedStatement stat = null;
            try {
                makeDBConnection();
                stat = conn.prepareStatement("UPDATE `novartis`.`user` SET last_activity_suggestion = ? WHERE id = '" + consumer_id + "'");
                stat.setInt(1, lastActivitySuggestion);
                int i = stat.executeUpdate();
            } catch (Exception e) {
                ERRORorWARNING = e.getMessage();
                setElementstatus("ERROR");
                setElementstatusmessage(getMessages());
            } finally {
              try {
                   stat.close();
                   conn.close();
              } catch (SQLException e) {
                  ERRORorWARNING = e.getMessage();
              }
            }

        }
	public void UpdateUserData(int consumerid, String first_name, String last_name, Date dob, String gender, String address1, String address2, String city, String state, String zip, String email, String phone, String smsAccept)throws Exception{
		PreparedStatement stat = null;
                setConsumer_id(consumerid);
		try {
                      java.sql.Date sd = null;
                      if (dob != null) {
                          sd = new java.sql.Date(dob.getTime());
                      }
		      makeDBConnection();

                      stat = conn.prepareStatement("SELECT id FROM `novartis`.`user` WHERE user_mphone = ? AND user_fname IS NULL AND email IS NULL AND id <> ? ");
                      stat.setString(1, phone);
                      stat.setInt(2, consumerid);
                      ResultSet rs = stat.executeQuery();
                      if (rs.next()) { // switch to a new consumer ID
                          setConsumer_id(rs.getInt("id"));
                      }

		      stat = conn.prepareStatement("UPDATE `novartis`.`user` SET user_fname = ?, user_lname = ?, dob = ?, gender = ?, address_1 = ?, address_2 = ?, city = ?, state = ?, post_code = ?, email = ?, user_mphone = ?, sms_accept= ? WHERE id = '" + consumer_id + "'");
		      stat.setString(1, first_name);
		      stat.setString(2, last_name);
		      stat.setDate(3, sd);
		      stat.setString(4, gender);
		      stat.setString(5, address1);
		      stat.setString(6, address2);
		      stat.setString(7, city);
		      stat.setString(8, state);
		      stat.setString(9, zip);
		      stat.setString(10, email);
		      stat.setString(11, phone);
                      stat.setString(12, smsAccept);
		      int i = stat.executeUpdate();
		      setConsumer_id(consumerid);
		      setElementstatus("OK");
		      setElementstatusmessage("Consumer information has been updated");
	              stat.close();
                      conn.close();

                      this.FetchUserData(consumerid);
                      if (nvsIndividualID != null && !nvsIndividualID.isEmpty()) {
                          this.UpdateNovartisIRMA(false); // update IRMA with the profile changes
                      }
                    } catch (Exception e) {
		    	ERRORorWARNING = e.getMessage();
                        setElementstatus("ERROR");
                        setElementstatusmessage(getMessages());
		    	conn.close();
		    } finally {
		      try {
		    	   conn.close();
		      } catch (SQLException e) {
		    	  ERRORorWARNING = e.getMessage();
		    	   conn.close();
		      }
		    }
	  }

        public boolean UpdateNovartisIRMA()
                throws javax.xml.bind.JAXBException, Exception
        {
            return UpdateNovartisIRMA(false);
        }

        public boolean UpdateNovartisIRMA(boolean onlyIfNotRegistered)
                throws javax.xml.bind.JAXBException, Exception
        {
            PreparedStatement statement;            

            JAXBContext requestContext = JAXBContext.newInstance(com.novartis.connectors.SetEnrollmentDetails.Request.class);
            Marshaller marshaller = requestContext.createMarshaller();
            JAXBContext responseContext = JAXBContext.newInstance(com.novartis.connectors.SetEnrollmentDetails.Response.class);
            Unmarshaller unmarshaller = responseContext.createUnmarshaller();

            com.novartis.connectors.SetEnrollmentDetails.Request request =
                new com.novartis.connectors.SetEnrollmentDetails.Request();
            com.novartis.connectors.SetEnrollmentDetails.Request.Consumer consumer =
                new com.novartis.connectors.SetEnrollmentDetails.Request.Consumer();

            FetchUserData(getConsumer_id());
            
            if (nvsIndividualID != null && !nvsIndividualID.isEmpty()) {
                if (onlyIfNotRegistered)
                    return false; // don't register if the record has already been sent to Novartis.
                consumer.setDispositionCode("88"); //Update profile
                consumer.setNvsindivid(nvsIndividualID);
                consumer.setCampaignIndividualId(Integer.parseInt(nvsCampaignIndivId));
            } else {
                consumer.setDispositionCode("4"); //New enrollment
            }
            consumer.setAddressLine1(address1);
            consumer.setAddressLine2(address2);
            consumer.setCity(city);
            try {
                if (dob != null) {
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(dob);
                    XMLGregorianCalendar gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
                consumer.setDob(gc);
                }
            } catch (DatatypeConfigurationException dce) {}
            consumer.setEmailAddress(email);
            consumer.setFirstName(first_name);
            consumer.setLastName(last_name);
            consumer.setGender(gender);
            consumer.setSegmentCode(segmentCode);
            consumer.setSmsAccept(BigInteger.valueOf(smsAccept));
            consumer.setSmsNumber(phone);
            consumer.setState(state);
            consumer.setZip(zip);
            consumer.setAcceptMarketing(BigInteger.valueOf(acceptMarketing));
            consumer.setAcceptTerms(BigInteger.valueOf(acceptTerms));
            consumer.setHighBp(BigInteger.valueOf(highBP));
            consumer.setMedication(BigInteger.valueOf(medications));
            consumer.setDrugAnswerID(drugAnswerID);
            consumer.setConsumerid(BigInteger.valueOf(consumer_id));
            consumer.setSrcSysKey(getContactEvent());
//            consumer.setTitle(title);
            if (onlyIfNotRegistered) { // only send the survey data in a registration enrollment

System.out.println("*** Sending survey data to IRMA");

                makeDBConnection();
                statement = conn.prepareStatement("SELECT question_id, answer FROM `novartis`.`survey_answers` WHERE user_id = ?");
                statement.setInt(1, consumer_id);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    switch (rs.getInt("question_id")) {
                        case 1:
                            consumer.setTypingQuestion1(BigInteger.valueOf(rs.getInt("answer")));
                            break;
                        case 2:
                            consumer.setTypingQuestion2(BigInteger.valueOf(rs.getInt("answer")));
                            break;
                        case 3:
                            consumer.setTypingQuestion3(BigInteger.valueOf(rs.getInt("answer")));
                            break;
                        case 4:
                            consumer.setTypingQuestion4(BigInteger.valueOf(rs.getInt("answer")));
                            break;
                        case 5:
                            consumer.setTypingQuestion5(BigInteger.valueOf(rs.getInt("answer")));
                            break;
                        case 6:
                            consumer.setTypingQuestion6(BigInteger.valueOf(rs.getInt("answer")));
                            break;
                        case 7:
                            consumer.setTypingQuestion7(BigInteger.valueOf(rs.getInt("answer")));
                            break;
                    }
                }
            }

            request.setConsumer(consumer);

            StringWriter requestWriter = new StringWriter();
            marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(request, requestWriter);
            String strRequestXml = requestWriter.toString();

            
            HttpURLConnection conn = null;
            String strResponseXml = "";

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

                conn = (HttpURLConnection) new java.net.URL("http://" + hostName + "/connector/novartisIRMA/SetEnrollmentDetails").openConnection();
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
                        } finally {
                            is.close();
                        }
                    }
                } catch (IOException ioe) {
                }
            }
            catch( IOException e ){
            }
            try {
                com.novartis.connectors.SetEnrollmentDetails.Response responseObject =
                    (com.novartis.connectors.SetEnrollmentDetails.Response)
                        unmarshaller.unmarshal(new StringReader(strResponseXml));
                com.novartis.connectors.SetEnrollmentDetails.Response.Consumer responseConsumer =
                    responseObject.getConsumer();
                if ((responseObject.getAlreadyJoined()) != null && (!responseObject.getAlreadyJoined().isEmpty())) {
                    return false;
                }
                makeDBConnection();
                statement = this.conn.prepareStatement("UPDATE `novartis`.`user` SET novartis_campaign_indv_id = ?, novartis_indv_id = ?, novartis_ib_contact_event_id = ? WHERE id = ?");
                statement.setString(1, responseConsumer.getCampaignIndividualIds());
                statement.setString(2, responseConsumer.getNvsIndividualId());
                statement.setString(3, responseConsumer.getContactEventId());
                statement.setInt(4, this.consumer_id);
                statement.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

//////////////////////////////////////////////////////////////////////////////////////////
	public void FetchUserData(int consumerid)throws Exception{
		PreparedStatement stat = null;
		try {
                      //java.util.Date ud = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dob);
		      //java.sql.Date sd = new java.sql.Date(ud.getTime());
		      makeDBConnection();
		      stat = conn.prepareStatement("SELECT user_fname, user_lname, dob, gender, address_1, address_2, city, state, post_code, " +
                                                    "email, user_mphone, accept_marketing, sms_accept, high_bp, medication, " +
                                                    "case when account_start_dt is null then 0 else 1 end  AS accept_terms, novartis_indv_id, " +
                                                    "novartis_campaign_indv_id, nvs_answer_id, last_activity_suggestion, drugs.drug_name, segment_code " +
                                                    "FROM `novartis`.`user` LEFT JOIN `novartis`.`user_drugs` ON (`user`.id = `user_id`) " +
                                                    "LEFT JOIN `novartis`.`drugs` ON (`user_drugs`.drug_id = `drugs`.id) " +
                                                    " WHERE `user`.id = ?");
		      stat.setInt(1, consumerid);
		      setKeyResultSet(stat.executeQuery());
		      if (keyResultSet.next()) {
                           setConsumer_id(consumerid);
		    	    setFirst_name(keyResultSet.getString(1));
		    	    setLast_name(keyResultSet.getString(2));
		    	    setDob(keyResultSet.getDate(3));
		    	    setGender(keyResultSet.getString(4));
		    	    setAddress1(keyResultSet.getString(5));
		    	    setAddress2(keyResultSet.getString(6));
		    	    setCity(keyResultSet.getString(7));
		    	    setState(keyResultSet.getString(8));
		    	    setZip(keyResultSet.getString(9));
		    	    setEmail(keyResultSet.getString(10));
		    	    setPhone(keyResultSet.getString(11));
                            acceptMarketing = keyResultSet.getInt(12);
                            smsAccept = keyResultSet.getInt(13);
                            highBP = keyResultSet.getInt(14);
                            medications = keyResultSet.getInt(15);
                            acceptTerms = keyResultSet.getInt(16);
                            nvsIndividualID = keyResultSet.getString(17);
                            nvsCampaignIndivId = keyResultSet.getString(18);
                            drugAnswerID = keyResultSet.getString(19);
                            lastActivitySuggestion = keyResultSet.getInt(20);
                            drugName = keyResultSet.getString(21);
                            segmentCode = keyResultSet.getString(22);

                            setElementstatus("OK");
		    	    setElementstatusmessage("Consumer Information...");
		    	  	stat.close();
		      } /*** Why is this in here?
		      else if(!keyResultSet.next()){
		    	    setFirst_name(first_name);
		    	    setLast_name(last_name);
		    	    setDob(dob);
		    	    setGender(gender);
		    	    setAddress1(address1);
		    	    setAddress2(address2);
		    	    setCity(city);
		    	    setState(state);
		    	    setZip(zip);
		    	    setEmail(email);
		    	    setPassword(password);
		    	    setPhone(phone);
		    	    setElementstatus("FAIL");
		    	    setElementstatusmessage(getMessages());
		    	  	stat.close();
		      }
		      else{
		    	    setFirst_name(first_name);
		    	    setLast_name(last_name);
		    	    setDob(dob);
		    	    setGender(gender);
		    	    setAddress1(address1);
		    	    setAddress2(address2);
		    	    setCity(city);
		    	    setState(state);
		    	    setZip(zip);
		    	    setEmail(email);
		    	    setPassword(password);
		    	    setPhone(phone);
		    	    setElementstatus("FAIL");
		    	    setElementstatusmessage(getMessages());
		    	  	stat.close();

		      } */
		        conn.close();
		        stat.close();
		    } catch (Exception e) {
		    	ERRORorWARNING = e.getMessage();
		    	conn.close();
		    } finally {
		      try {
		    	   conn.close();
		      } catch (SQLException e) {
		    	  ERRORorWARNING = e.getMessage();
		    	   conn.close();
		      }
		    }
	  }
//=======================================================================================================================================================================
        public boolean isRegistered(int consumerID) throws Exception
        {
            PreparedStatement stat = null;
	    ResultSet result = null;
            makeDBConnection();
            stat = conn.prepareStatement("select * from `novartis`.`user` where id = ?");
            stat.setInt(1,consumerID);
            result = stat.executeQuery();
            if(result.next() == false)
                return false;
            else
                return true;
        }

        public String getContactEvent()
        {
            String contactEventID = "00000";
            try {
                PreparedStatement stat = null;
                ResultSet result = null;
                makeDBConnection();
                stat = conn.prepareStatement("UPDATE `novartis`.`contact_events` set end_dt = now() WHERE user_id = ? AND end_dt is null");
                stat.setInt(1,consumer_id);
                stat.executeUpdate();
                // create a new contact event
                stat = conn.prepareStatement("INSERT INTO `novartis`.`contact_events` (user_id, start_dt) VALUES (?, (SELECT NOW()))", Statement.RETURN_GENERATED_KEYS);
                stat.setInt(1,consumer_id);
                if (stat.executeUpdate() > 0) {
                    result = stat.getGeneratedKeys();
                    if (result.next()) contactEventID = result.getString(1);
                } else {
                    throw new Exception("Unable to create new contact event record");
                }
            } catch (Exception e) {
                System.out.println("ERROR Creating contact event:" + e.getMessage());
                e.printStackTrace();
            }            
            return contactEventID;
        }
                

        	public void AssignCodeSegment() throws SQLException, ClassNotFoundException, Exception
	{
		  int rowCount;
		  int currentRow;

		TypingToolProcessing Tp = new TypingToolProcessing();

                    makeDBConnection();


        PreparedStatement statement = conn.prepareStatement("SELECT 	user_id,question_id,answer FROM survey_answers WHERE user_id = ? AND answer IS NOT NULL");
        statement.setInt(1, consumer_id);
		ResultSet result = statement.executeQuery();

		   currentRow = result.getRow();
		   rowCount = result.last() ? result.getRow() : 0;
		   if (currentRow == 0)
		      result.beforeFirst();
		   else
	       result.absolute(currentRow);
		   System.out.println(rowCount);
		   if(rowCount < 7){
		   //return "Error With Number Of Questions Answered";
		   }else{

		while(result.next())
		{
		//Check if Question/Answer is a Number
			try{
				 if(result.getString(3) == null){
		//return "Error With Number Of Questions Answered";
				}
                        Tp.generateSegmentArray(Tp.getCoefficientValues(Tp.getQuestionName(result.getInt("question_id"))), Tp.getQuestionCategory(result.getInt("question_id"), result.getInt("answer")));

			}catch(NumberFormatException nFE){
                            System.out.println();
			}
			//System.out.println(result.getString(2)+ " "+ result.getString(3));

		}

		//Update Consumer with Segment Code
                statement = conn.prepareStatement("UPDATE user SET  segment_code = ? WHERE id = ? ");
		statement.setString(1, Tp.getMax());
		statement.setInt(2, consumer_id);
		statement.executeUpdate();

            }
    }


        public void logout(int consumer_id){
            this.setConsumer_id(consumer_id);
            logout();
        }

        public void logout(){
            try {
                PreparedStatement stat = null;
                ResultSet result = null;
                makeDBConnection();
                stat = conn.prepareStatement("UPDATE `novartis`.`contact_events` SET end_dt = now() WHERE user_id = ? AND end_dt is null");
                stat.setInt(1,consumer_id);
                stat.executeUpdate();
            } catch (Exception e) {
                System.out.println("ERROR updating contact event for logout:" + e.getMessage());
                e.printStackTrace();
            }
        }

        public boolean isEnrolledNovartis(){
            return (this.nvsCampaignIndivId != null) && (this.nvsIndividualID != null) && (!this.nvsCampaignIndivId.isEmpty()) && (!this.nvsIndividualID.isEmpty());
        }
}
