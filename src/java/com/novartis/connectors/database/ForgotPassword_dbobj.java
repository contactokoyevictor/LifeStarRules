package com.novartis.connectors.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.novartis.connectors.prep.*;
/**
 * @author Okoye Victor Gift
 */
public class ForgotPassword_dbobj {
        private SendEmailRequest esender;
	private int consumer_id;

	private ResultSet keyResultSet;
	private String Result ="";

	private String elementstatus="";
	private String elementstatusmessage="";
	private String email_address="";

	private Connection conn = null;
	private String ERRORorWARNING = "";

	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public int getConsumer_id() {
		return consumer_id;
	}
	public void setConsumer_id(int consumer_id) {
		this.consumer_id = consumer_id;
	}
	public ResultSet getKeyResultSet() {
		return keyResultSet;
	}
	public void setKeyResultSet(ResultSet keyResultSet) {
		this.keyResultSet = keyResultSet;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}

	public String getElementstatus() {
		return elementstatus;
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

	public String getERRORorWARNING() {
		return ERRORorWARNING;
	}
	public void setERRORorWARNING(String eRRORorWARNING) {
		ERRORorWARNING = eRRORorWARNING;
	}
	public String getMessages(){
		   return ERRORorWARNING;
	}

	public void makeDBConnection() throws Exception{
		try{
		    Context ctx = new InitialContext();
		    DataSource ds = (DataSource)ctx.lookup("java:/novartis");
		    conn = ds.getConnection();
		   }catch(Exception e) {
		        System.out.println("SQL Exception is caught during connection creation."+e);
		        ERRORorWARNING = e.getMessage().toString();
		        conn.close();
		     }
		}
        
	public void createXmlRequest(String email_address)throws Exception
	{
		PreparedStatement stat = null;
		try {
		      makeDBConnection();
		      stat = conn.prepareStatement("SELECT id FROM `novartis`.`user` where email = ?");
		      stat.setString(1, email_address);
                      ResultSet rs = stat.executeQuery();
		      //setKeyResultSet(stat.executeQuery());
                      esender = new SendEmailRequest();
		      if(rs.next()){
                          setConsumer_id(rs.getInt(1));
		    	  //TODO: Sent forgot password email
                          esender.ForgotPasswordXmlRequest(getConsumer_id());
                          setElementstatus(esender.getStatus());
		    	  setElementstatusmessage(esender.getStatusmessage());

		      } else {
                          this.setElementstatus("Fail");
                          this.setElementstatusmessage("Email does not exist in our records. Please try another email address.");
		    	  setEmail_address(email_address);
		      }
		        stat.close();
		        conn.close();

		    } catch (Exception e) {
		    	ERRORorWARNING = e.getMessage();
		    } finally {
		      try {
		    	   stat.close();
		    	   conn.close();
		      } catch (SQLException e) {
		    	  ERRORorWARNING = e.getMessage();
		      }
		    }
	}
}
