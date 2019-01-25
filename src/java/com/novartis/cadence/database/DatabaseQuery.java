package com.novartis.cadence.database;
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.*;
import java.util.logging.*;

import com.novartis.cadence.rulesengine.RulesEngine;

public class DatabaseQuery{
	private RulesEngine rulesEngine = new RulesEngine();
        private User user;
        public String _errorOrWarningMessage = "";
        public static Connection _dbConn;
        public PreparedStatement _sqlStatement;


//Segment Code
	private static final  String SKEPTICS = "GOTSKPSEG";
	private static final  String STUDENT = "GOTSTUSEG";
	private static final  String INDEPENDANT = "GOTINDSEG";
	private static final  String STRUGGLERS = "GOTSTGSEG";

        public Connection makeDBConnection() throws Exception {
        try{
             Context ctx = new InitialContext();
             DataSource ds = (DataSource) ctx.lookup("java:/jdbc/novartis");
             _dbConn = ds.getConnection();
            }catch (Exception e) {
             System.out.println("SQL Exception is caught during connection creation." + e);
             Logger.getLogger(DatabaseQuery.class.getName()).log(Level.SEVERE, null, e.getMessage());
             _errorOrWarningMessage = e.getMessage().toString();
             _dbConn.close();
          }
            return _dbConn;
         }

	public void invokeCadence()
	{
		PreparedStatement stmt = null;
		user = new User();
		try{
                    _dbConn = makeDBConnection();
                    stmt = _dbConn.prepareStatement("SELECT U.id, U.segment_code, FLOOR(DATEDIFF(CURDATE(), U.account_start_dt)/7) + 1, " +
						" U.sms_opt_out_dt, U.sms_opt_in_dt, B.opt_in_out_flag " +
						" FROM `novartis`.`user` as U left join buddy B " +
						" on U.buddy_id = B.id " +
						" where novartis_optout_flag is null");
			ResultSet result = stmt.executeQuery();
			while(result.next())
			{
				user.setUserID(result.getInt(1));
				user.setSegmentCode(result.getString(2));
				user.setNumberOfWeek(result.getInt(3));
				user.setSmsOptoutDate(result.getTimestamp(4));
				user.setSmsOptinDate(result.getTimestamp(5));
				user.setBpBuddy(result.getString(6));
				System.out.println("Rules invoked for User : " + user.getUserID());
				rulesEngine.invokeRules(user);
			}
                         result.close();
                         stmt.close();
                        _dbConn.close();

		}
		catch(Exception e)
		{

		}
	}

//======================================================================================================================================
	public void insertSchedule(int userID,String schedtype, int active, String weekdays,  String frequencyType, int msg_channel, String schedule_subtype,String kitCode)
	{
            PreparedStatement stmt = null;
            try{
                 _dbConn = makeDBConnection();

                stmt = _dbConn.prepareStatement("insert into `novartis`.`scheduler`"
                    + " (user_id, schedule_type , schedule_active , active_weekdays, frequency_type, "
                    + " start_date ,message_channel_id, schedule_subtype, nvs_kit_code)"
                    + " values (? , ? , ? , ? , ?  , now(), ? , ? , ?)");

                stmt.setInt(1, userID);			// user ID
                stmt.setString(2, schedtype); 		//schedule type
                stmt.setInt(3, active); 		// schedule Active
                stmt.setString(4, weekdays); 		// weekdays
                stmt.setString(5, frequencyType);	// frequency Type
                stmt.setInt(6, msg_channel);		// message channel ID
                stmt.setString(7, schedule_subtype);
                stmt.setString(8,kitCode);
                int exec = stmt.executeUpdate();
                if(exec > 0){
                System.out.println("Stored Successfully");
                stmt.close();
                 _dbConn.close();
                }
                else{
                    System.out.println("Storage was Unsuccessfully");
                    stmt.close();
                    _dbConn.close();
                }

		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}

//======================================================================================================================
	public void insertSchedule(int userID[], String schedtype, int active, String weekdays,  String frequencyType, int msg_channel, String schedule_subtype,String kitCode)
	{
		PreparedStatement stmt = null;
		String insertString = "insert into `novartis`.`scheduler`"
            + "(user_id, schedule_type , schedule_active , active_weekdays, frequency_type, start_date ,message_channel_id, schedule_subtype, nvs_kit_code)";
               insertString += " values (? , ? , ? , ? , ?  , now(), ? , ? , ?)";
    try{
            _dbConn = makeDBConnection();
            stmt = _dbConn.prepareStatement(insertString);

            for(int uid : userID)
            {
                stmt.setInt(1, uid);		// user ID
                stmt.setString(2, schedtype); 	//schedule type
                stmt.setInt(3, active); 	// schedule Active
                stmt.setString(4, weekdays); 	// weekdays
                stmt.setString(5, frequencyType);// frequency Type
                stmt.setInt(6, msg_channel);	// message channel ID
                stmt.setString(7, schedule_subtype);
                stmt.setString(8,kitCode);
                stmt.executeUpdate();
            }
		    stmt.close();
                    _dbConn.close();
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}
//==============================================================================================================================
	public void createSchedule(int userID ,String shedtype, int active, String weekdays,  String frequencyType, int msg_channel, String schedule_subtype, String kitCode)
	{

	}

}