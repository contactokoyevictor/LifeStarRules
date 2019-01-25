package com.novartis.connectors.database;

import java.sql.*;

import javax.sql.DataSource;
import javax.naming.*;
/**
 * @author Okoye Victor Gift
 */
public class BPBuddyData_dbobj {


	private String first_name = "";
    private String last_name = "";
    private String email = "";
    private String option ="";


    private String elementstatus="";
	private String elementstatusmessage="";
	private int consumer_id;
	private ResultSet keyResultSet;
	private int insert;
	private String Result = "";
	private int newKey;

	private Connection conn = null;
	private String ERRORorWARNING = "";


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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getOption() {
		return option;
	}


	public void setOption(String option) {
		this.option = option;
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
            if ( (conn != null) && (!conn.isClosed())) return; // nothing to be done
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
	public void BuddyData(int consumer_id, String buddy_fname, String buddy_lname, String buddy_email, String buddy_option)throws Exception{
		PreparedStatement stat = null;
		try {
			 makeDBConnection();
			 int myCheck = CheckID(consumer_id);
			 if(myCheck == 1){
                             stat = conn.prepareStatement("SELECT email FROM `novartis`.`user` WHERE id = ?");
                             stat.setInt(1, consumer_id);
                             ResultSet rs = stat.executeQuery();
                             if (rs.next()) {
                                 String sTemp = rs.getString("email");
                                 if (sTemp.equalsIgnoreCase(buddy_email)) {
                                     setElementstatus("FAIL");
                                     setElementstatusmessage("You can't register as your own buddy.");
                                     return;
                                 }
                             } else {
                                 setElementstatus("ERROR");
        			 setElementstatusmessage("Invalid consumer record.");
                             }
			     stat = conn.prepareStatement("INSERT INTO `novartis`.`buddy`(buddy_fname, buddy_lname, buddy_email) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			     stat.setString(1, buddy_fname);
			     stat.setString(2, buddy_lname);
			     stat.setString(3, buddy_email);
			     insert = stat.executeUpdate();
			     setInsert(insert);
			     setKeyResultSet(stat.getGeneratedKeys());
			     if (keyResultSet.next()) {
			       setNewKey(keyResultSet.getInt(1));
			      }
			     if(insert > 0 && newKey > 0){
			    	 keyResultSet.close();
			    	 stat.close();
			    	 stat = conn.prepareStatement("UPDATE `novartis`.`user` SET buddy_id = ?, buddy_type = ? WHERE id = '" + consumer_id + "'");
                                 stat.setInt(1, newKey);
                                 if (buddy_option.equalsIgnoreCase("live")) {
                                     stat.setString(2, "L");
                                 } else if (buddy_option.equalsIgnoreCase("virtual")) {
                                     stat.setString(2, "V");
                                 } else {
                                     stat.setString(2, "N");
                                 }
                                 stat.executeUpdate();
                                 setConsumer_id(consumer_id);
                                 setElementstatus("OK");
                                 setElementstatusmessage("");
                                 stat.close();
                              }
                              if(insert <= 0){
                              setConsumer_id(consumer_id);
                              setElementstatus("FAIL");
                              setElementstatusmessage(getMessages());
                              stat.close();
                              }
			 }
		        if (conn != null) conn.close();
		        if (stat != null) stat.close();
		    } catch (Exception e) {
		    	ERRORorWARNING = e.getMessage();
                        setConsumer_id(consumer_id);
                        setElementstatus("ERROR");
			setElementstatusmessage(getMessages());
		    	conn.close();
		    } finally {
		      try {
		    	   conn.close();
		      }    catch (SQLException e) {
		    	   ERRORorWARNING = e.getMessage();
                           setConsumer_id(consumer_id);
                           setElementstatus("ERROR");
                           setElementstatusmessage(getMessages());
		    	   conn.close();
		      }
		    }
	  }
	public int CheckID(int consumer_id)throws Exception{
		PreparedStatement stat = null;
		int v = 0;
		try {
		      makeDBConnection();
		      stat = conn.prepareStatement("SELECT id FROM `novartis`.`user` WHERE id = ?");
		      stat.setInt(1, consumer_id);
		      ResultSet rs = stat.executeQuery();
		      if(rs.next()) {
		    	  v = 1;
		      }
		    } catch (Exception e) {
		    	ERRORorWARNING = e.getMessage();
		    } finally {
/*		      try {
		    	   conn.close();
		      } catch (SQLException e) {
		    	  ERRORorWARNING = e.getMessage();
		    	   conn.close();
		      }
*/
                    }
		    return v;
	  }

}
