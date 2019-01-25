package com.novartis.connectors.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
/**
 * @author Okoye Victor Gift
 */
public class EnrollmentAcceptData_dbobj {

	private int marketing;
	private int accept;
	private int consumer_id;
	private String elementstatus ="";
	private String elementstatusmessage ="";
	private ResultSet keyResultSet;
	private int newKey;
	private int insert;
	private Connection conn = null;
	private String ERRORorWARNING = "";

	public int getMarketing() {
		return marketing;
	}
	public void setMarketing(int marketing) {
		this.marketing = marketing;
	}
	public int getAccept() {
		return accept;
	}
	public void setAccept(int accept) {
		this.accept = accept;
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
	public int getNewKey() {
		return newKey;
	}
	public void setNewKey(int newKey) {
		this.newKey = newKey;
	}
	public int getInsert() {
		return insert;
	}
	public void setInsert(int insert) {
		this.insert = insert;
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
		    DataSource ds = (DataSource)ctx.lookup("java:/jdbc/novartis");
		    conn = ds.getConnection();
		   }catch(Exception e) {
		        System.out.println("SQL Exception is caught during connection creation."+e);
		        ERRORorWARNING = e.getMessage().toString();
		        conn.close();
		     }
		}
	public void processUpdate(int Maketing, int accept)throws Exception{
		PreparedStatement stat = null;
		if(accept == 1)
		{
			try {
			      makeDBConnection();
			      stat = conn.prepareStatement("INSERT INTO `novartis`.`user`(accept_marketing, account_start_dt) VALUES (?, (SELECT NOW()))", Statement.RETURN_GENERATED_KEYS);
			      stat.setInt(1, Maketing);
			      insert = stat.executeUpdate();
			      setInsert(insert);
			      if (getInsert() > 0) {
                                  setKeyResultSet(stat.getGeneratedKeys());
			    	  setNewKey(keyResultSet.getInt(1));
                                  setConsumer_id(getNewKey());
			    	  setElementstatus("OK");
			    	  setElementstatusmessage("new profile has been set");
                                  System.out.println("stage 1 returns :"+newKey);
                                  stat.close();
			      }
			      if(insert <= 0 && newKey <= 0) {
			    	  setConsumer_id(getConsumer_id());
			    	  setElementstatus("FAIL");
			    	  setElementstatusmessage(getMessages());
			    	  System.out.println("stage 2 returns :"+consumer_id);
		    		  stat.close();
			      }
                              conn.close();
			      stat.close();
			    } catch (Exception e){
			    	ERRORorWARNING = e.getMessage();
                                setConsumer_id(consumer_id);
                                setElementstatus("ERROR");
                                setElementstatusmessage(getMessages());
			    	conn.close();
			      }finally {
			      try {conn.close();} catch (SQLException e) {
			    	   ERRORorWARNING = e.getMessage();
			    	   conn.close();
			      }
		}
	}
		if(accept == 0)
		{
			try {
			      makeDBConnection();
			      stat = conn.prepareStatement("INSERT INTO `novartis`.`user`(accept_marketing, account_start_dt) VALUES (?, NULL)", Statement.RETURN_GENERATED_KEYS);
			      stat.setInt(1, Maketing);
				  insert = stat.executeUpdate();
			      setInsert(insert);
                              if (getInsert() > 0) {
                                  setKeyResultSet(stat.getGeneratedKeys());
			    	  setNewKey(keyResultSet.getInt(1));
                                  setConsumer_id(getNewKey());
			    	  setElementstatus("OK");
			    	  setElementstatusmessage("new profile has been set");
                                  System.out.println("stage 1 returns :"+newKey);
                                  stat.close();
			      }
                              if(insert <= 0 && newKey <= 0) {
			    	  setConsumer_id(getConsumer_id());
			    	  setElementstatus("FAIL");
			    	  setElementstatusmessage(getMessages());
		    		  stat.close();
			      }
                              conn.close();
			        stat.close();
			    } catch (Exception e) {
			    	ERRORorWARNING = e.getMessage();
                                setConsumer_id(consumer_id);
                                setElementstatus("ERROR");
                                setElementstatusmessage(getMessages());
			    	conn.close();
			    } finally {
			      try {conn.close();} catch (SQLException e) {
			    	    ERRORorWARNING = e.getMessage();
			    	    conn.close();
			      }
			    }
		}

	}
}