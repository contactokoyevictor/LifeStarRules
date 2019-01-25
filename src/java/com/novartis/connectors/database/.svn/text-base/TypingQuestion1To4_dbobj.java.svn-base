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
public class TypingQuestion1To4_dbobj {

	private String question1 = "";
	private String question2 = "";
	private String question3 = "";
	private String question4 = "";
	private String elementstatus="";
	private String elementstatusmessage="";
	private int consumer_id;
	private ResultSet keyResultSet;
	private int insert;
	private String Result = "";
	private int newKey;


	private Connection conn = null;
	private String ERRORorWARNING = "";

	public String getQuestion1() {
		return question1;
	}
	public void setQuestion1(String question1) {
		this.question1 = question1;
	}
	public String getQuestion2() {
		return question2;
	}
	public void setQuestion2(String question2) {
		this.question2 = question2;
	}
	public String getQuestion3() {
		return question3;
	}
	public void setQuestion3(String question3) {
		this.question3 = question3;
	}
	public String getQuestion4() {
		return question4;
	}
	public void setQuestion4(String question4) {
		this.question4 = question4;
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
//////////////////////////////////////////////////////////////////////////////////////////
	public void typpinQ1to4(int consumer_id, int question_id1, int question_id2, int question_id3, int question_id4)throws Exception{
		PreparedStatement stat = null;
                setConsumer_id(consumer_id);
                try {
		      makeDBConnection();
                      conn.setAutoCommit(false);
                      stat = conn.prepareStatement("DELETE FROM `novartis`.`survey_answers` WHERE (user_id=?) AND (question_id IN (1,2,3,4))");
                      stat.setInt(1, consumer_id);
                      stat.execute();

                      stat = conn.prepareStatement("INSERT INTO `novartis`.`survey_answers`(user_id, question_id, answer) VALUES(?, ?, ?),(?, ?, ?),(?, ?, ?),(?, ?, ?)");
		      stat.setInt(1, consumer_id);
		      stat.setInt(2, 1); // the question id
		      stat.setInt(3, question_id1);

                      stat.setInt(4, consumer_id);
		      stat.setInt(5, 2); // the question id
		      stat.setInt(6, question_id2);
		      
		      stat.setInt(7, consumer_id);
		      stat.setInt(8, 3); // the question id
		      stat.setInt(9, question_id3);
		      
		      stat.setInt(10, consumer_id);
		      stat.setInt(11, 4); // the question id
		      stat.setInt(12, question_id4);
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
