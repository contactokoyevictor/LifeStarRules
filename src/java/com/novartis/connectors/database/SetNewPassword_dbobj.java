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
public class SetNewPassword_dbobj {

	private String password = "";
	private String new_password ="";
	private String elementstatus="";
	private String elementstatusmessage="";
	private int consumer_id;
	private ResultSet keyResultSet;
	private int insert;
	private String Result = "";
	private int newKey;

	private Connection conn = null;
	private String ERRORorWARNING = "";

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNew_password() {
		return new_password;
	}
	public void setNew_password(String new_password) {
		this.new_password = new_password;
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

	public void setNewPassword(int consumer_id, String password, String new_password)throws Exception
        {
            setNewPassword(consumer_id, password, new_password, null, null);
        }

        public void setNewPassword(int consumer_id, String password, String new_password, String individualid, String campaignindividualid)throws Exception
        {
            PreparedStatement stat = null;
            try {
              makeDBConnection();
              if ((individualid != null) && (campaignindividualid != null))
              {
                  stat = conn.prepareStatement("SELECT * FROM `novartis`.`user` WHERE novartis_indv_id = ? AND novartis_campaign_indv_id = ?");
                  stat.setString(1, individualid);
                  stat.setString(2, campaignindividualid);
                  ResultSet rs = stat.executeQuery();
                  if (rs.next()) {
                    setConsumer_id(rs.getInt("id"));
                  } else {
                    //TODO: call IRMA and insert this user
                  }
                  if (password == null) {
                      stat = conn.prepareStatement("UPDATE `novartis`.`user` SET password = ? WHERE novartis_indv_id = ? AND novartis_campaign_indv_id = ? AND password is null");
                      stat.setString(1, new_password);
                      stat.setString(2, individualid);
                      stat.setString(3, campaignindividualid);
                  } else {
                      stat = conn.prepareStatement("UPDATE `novartis`.`user` SET password = ? WHERE novartis_indv_id = ? AND novartis_campaign_indv_id = ? AND password = ?");
                      stat.setString(1, new_password);
                      stat.setString(2, individualid);
                      stat.setString(3, campaignindividualid);
                      stat.setString(4, password);
                  }
              } else {
                  setConsumer_id(consumer_id);
                  if (password == null) {
                      stat = conn.prepareStatement("UPDATE `novartis`.`user` SET password = ? WHERE id = ? AND password is null");
                      stat.setString(1, new_password);
                      stat.setInt(2, consumer_id);
                  } else {
                      stat = conn.prepareStatement("UPDATE `novartis`.`user` SET password = ? WHERE id = ? AND password =?");
                      stat.setString(1, new_password);
                      stat.setInt(2, consumer_id);
                      stat.setString(3, password);
                  }
               }
              setInsert(stat.executeUpdate());
              if (getInsert() > 0) {
                  setElementstatus("OK");
                  setElementstatusmessage("Consumer Password Change was successful..");
                  stat.close();
              }  else if (getInsert() <= 0) {
                  setElementstatus("FAIL");
                  setElementstatusmessage("Consumer Password Change was unsuccessful..");
                  stat.close();
              } else if(getInsert() <= 0 && getMessages().length() > 1){
                  setElementstatus("ERROR");
                  setElementstatusmessage(getMessages());
                  stat.close();
              } else {
                  setElementstatus("ERROR");
                  setElementstatusmessage(getMessages());
                  stat.close();
              }
            conn.close();
            stat.close();
        } catch (Exception e) {
                ERRORorWARNING = e.getMessage();
                  setElementstatus("ERROR");
                  setElementstatusmessage(ERRORorWARNING);
                  conn.close();
            } finally {
                  try {
                       conn.close();
                  } catch (SQLException e) {
                      ERRORorWARNING = e.getMessage();
                      setElementstatus("ERROR");
                      setElementstatusmessage(ERRORorWARNING);
                      conn.close();
                  }
            }
	}
}
