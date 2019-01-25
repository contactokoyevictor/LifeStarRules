package com.novartis.connectors.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
/**
 * @author Okoye Victor Gift
 */
public class Login_dbobj {
	private String user_name = "";
	private String password = "";
	private String elementstatus="";
	private String elementstatusmessage="";
	private int consumer_id;
	private ResultSet keyResultSet;
	private int insert;
	private String Result = "";
	private int newKey;
	private Connection conn = null;
	private String ERRORorWARNING = "";

	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
		    DataSource ds = (DataSource)ctx.lookup("java:/jdbc/novartis");
		    conn = ds.getConnection();
		   }catch(Exception e) {
		        System.out.println("SQL Exception is caught during connection creation."+e);
		        ERRORorWARNING = e.getMessage().toString();
		        conn.close();
		     }
		}
	public void process(String user_name, String password)throws Exception{

		PreparedStatement stat = null;
		try {
		      makeDBConnection();
		      stat = conn.prepareStatement("SELECT id FROM `novartis`.`user` WHERE email = ? AND password = ?");
		      stat.setString(1, user_name);
		      stat.setString(2, password);
		      setKeyResultSet(stat.executeQuery());
		      if (keyResultSet.next()) {
		    	    setConsumer_id(keyResultSet.getInt(1));
		    	    setElementstatus("OK");
		    	    setElementstatusmessage("Success");
		    	  	stat.close();
		      }
		      else if(!keyResultSet.next() && getMessages().isEmpty()){
		    	  	setConsumer_id(0);
		    	    setElementstatusmessage("Login Failure");
		    	    setElementstatus("FAIL");
		    	    setElementstatusmessage("That email does not exist in our records. Please try another email address.");
		    	  	stat.close();
		      }
		      else{
		    	  	setConsumer_id(0);
		    	    setElementstatusmessage("Login Failure");
		    	    setElementstatus("ERROR");
		    	    setElementstatusmessage(getMessages());
		    	  	stat.close();

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
	}
}
