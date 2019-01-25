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
public class TypingQuestion5To7_dbobj {


	private String question5 = "";
	private String question6 = "";
	private String question7 = "";

	private String elementstatus="";
	private String elementstatusmessage="";
	private int consumer_id;
	private ResultSet keyResultSet;
	private int insert;
	private String Result = "";
	private int newKey;


	private Connection conn = null;
	private String ERRORorWARNING = "";


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
	public String getQuestion5() {
		return question5;
	}
	public void setQuestion5(String question5) {
		this.question5 = question5;
	}
	public String getQuestion6() {
		return question6;
	}
	public void setQuestion6(String question6) {
		this.question6 = question6;
	}
	public String getQuestion7() {
		return question7;
	}
	public void setQuestion7(String question7) {
		this.question7 = question7;
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
//////////////////////////////////////////////////////////////////////////////////////////
	public void typpinQ5to7(int consumer_id, int question_id5, int question_id6, int question_id7)throws Exception{
		PreparedStatement stat = null;
                setConsumer_id(consumer_id);
                try {
		      makeDBConnection();
                      conn.setAutoCommit(false);
                      stat = conn.prepareStatement("DELETE FROM `novartis`.`survey_answers` WHERE (user_id=?) AND (question_id IN (5,6,7,8))");
                      stat.setInt(1, consumer_id);
                      stat.execute();
                      
                      stat = conn.prepareStatement("INSERT INTO `novartis`.`survey_answers`(user_id, question_id, answer) VALUES(?, ?, ?),(?, ?, ?),(?, ?, ?)");
		      stat.setInt(1, consumer_id);
		      stat.setInt(2, 5); // the question id
		      stat.setInt(3, question_id5);

                      stat.setInt(4, consumer_id);
		      stat.setInt(5, 6); // the question id
		      stat.setInt(6, question_id6);

		      stat.setInt(7, consumer_id);
		      stat.setInt(8, 7); // the question id
		      stat.setInt(9, question_id7);

                      setInsert(stat.executeUpdate());
                      conn.commit();
                      if(insert > 0){
                            setElementstatus("OK");
                            setElementstatusmessage("OK");
                            stat.close();
                            conn.close();
                      }
                      if(insert <= 0){
                            setElementstatus("FAIL");
                            setElementstatusmessage(getMessages());
                            stat.close();
                            conn.close();
                      }

		    } catch (Exception e) {
		    	ERRORorWARNING = e.getMessage();
		    	conn.close();
                        setElementstatus("ERROR");
                        setElementstatusmessage(getMessages());
		    } finally {
		      try {
		    	   conn.close();
		      }    catch (SQLException e) {
		    	   ERRORorWARNING = e.getMessage();
		    	   conn.close();

		      }
		    }
	  }
}
