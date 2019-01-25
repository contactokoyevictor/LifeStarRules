package com.novartis.cadence.database;

import javax.sql.DataSource;
import javax.naming.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.*;
import com.novartis.cadence.rulesengine.RulesEngine;


public class Cadence{

	private RulesEngine rulesEngine = new RulesEngine();
        //Segment Code
	private static final  String SKEPTICS = "GOTSKPSEG";
	private static final  String STUDENT = "GOTSTUSEG";
	private static final  String INDEPENDANT = "GOTINDSEG";
	private static final  String STRUGGLERS = "GOTSTGSEG";

        public Connection _dbConn;
        public PreparedStatement _sqlStatement;
        public String _errorOrWarningMessage = "";
        private User user;

        public void makeDBConnection() throws Exception {
        try {
             Context ctx = new InitialContext();
             DataSource ds = (DataSource) ctx.lookup("java:/jdbc/novartis");
             _dbConn = ds.getConnection();
        } catch (Exception e) {
                System.out.println("SQL Exception is caught during connection creation." + e);
                Logger.getLogger(Cadence.class.getName()).log(Level.SEVERE, null, e.getMessage());
                _errorOrWarningMessage = e.getMessage().toString();
                _dbConn.close();

        }
    }
//==========================================================================================================================
	public void invokeCadence()
	{     	user = new User();
		try{
                    makeDBConnection();
                    _sqlStatement = _dbConn.prepareStatement("SELECT U.id, U.segment_code, FLOOR(DATEDIFF(CURDATE(), U.account_start_dt)/7) + 1, " +
                		" U.sms_opt_out_dt, U.sms_opt_in_dt, B.opt_in_out_flag " +
				" FROM `novartis`.`user` as U left join buddy B " +
				" on U.buddy_id = B.id " +
				" where novartis_optout_flag is null");

                    ResultSet rs  = _sqlStatement.executeQuery();
                    while(rs.next())
                    {
                        user.setUserID(rs.getInt(1));
                        user.setSegmentCode(rs.getString(2));
                        user.setNumberOfWeek(rs.getInt(3));
                        user.setSmsOptoutDate(rs.getTimestamp(4));
                        user.setSmsOptinDate(rs.getTimestamp(5));
                        user.setBpBuddy(rs.getString(6));

                        System.out.println("Rules invoked for User : " + user.getUserID());
                        rulesEngine.invokeRules(user);
                    }
                    rs.close();
                    _sqlStatement.close();
                    _dbConn.close();
                  }
                    catch(Exception e)
                    {
                        Logger.getLogger(Cadence.class.getName()).log(Level.SEVERE, null, e.getMessage());
                    }
	   }
//======================================================================================================================================
	public void insertSchedule(int userID,String schedtype, int active, String weekdays,  String frequencyType, int msg_channel, String schedule_subtype,String kitCode)
	{
	      try{
                  makeDBConnection();
                 _sqlStatement = _dbConn.prepareStatement("insert into `novartis`.`scheduler`"
                                                        + "(user_id, schedule_type , schedule_active , active_weekdays, frequency_type, start_date,"
                                                        + " message_channel_id, schedule_subtype, nvs_kit_code) values (? , ? , ? , ? , ?  , now(), ? , ? , ?)");

                 _sqlStatement.setInt(1, userID);               // user ID
                 _sqlStatement.setString(2, schedtype); 	// schedule type
                 _sqlStatement.setInt(3, active); 		// schedule Active
                 _sqlStatement.setString(4, weekdays); 		// weekdays
                 _sqlStatement.setString(5, frequencyType);	// frequency Type
                 _sqlStatement.setInt(6, msg_channel);		// message channel ID
                 _sqlStatement.setString(7, schedule_subtype);
                 _sqlStatement.setString(8,kitCode);

                 int exc = _sqlStatement.executeUpdate();
                 if(exc > 0){
                    System.out.println("Stored Successfully");
                 }
                 _sqlStatement.close();
                 _dbConn.close();
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
                        Logger.getLogger(Cadence.class.getName()).log(Level.SEVERE, null, ex.getMessage());
		}
	}
	
//==============================================================================================================================
	public void createSchedule(int userID ,String shedtype, int active, String weekdays,  String frequencyType, int msg_channel, String schedule_subtype, String kitCode)
	{

	}
}