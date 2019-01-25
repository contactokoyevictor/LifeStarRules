/**
 *
 * @author Pkb
 */
package com.novartis.messaging.preps;
import com.novartis.messaging.helpers.OtherResponse;
import com.novartis.messaging.helpers.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import javax.naming.NamingException;

import javax.sql.DataSource;

public class GotSMSMessagingPrep {

    private DataSource ds;
    private String norvatisMessage;
    private String tipOne = "tipOne";
    private String tipTwo = "tipTwo";
    private int count = 0;
    GotBPMessageTips Ms = new GotBPMessageTips();
    GetBPTrackerMainSql conn = new GetBPTrackerMainSql();

    private int getUserId(String mobile) throws ClassNotFoundException, NamingException, SQLException, PrepException {
        String sql = "SELECT id FROM USER WHERE user_mphone = ?";
        PreparedStatement statement = conn.Dbconnection().prepareStatement(sql);
        statement.setString(1, mobile);
        ResultSet res = statement.executeQuery();
        res.last();
        int row_count = res.getRow();

        if(row_count == 0){
            throw new PrepException("Error in GotSMSMessagingPrep.getUserId() : mobile number not in database");
        }
        res.beforeFirst();
        res.next();

        return res.getInt("id");
    }

    private void recordBP(String mobilenum, int systolic, int diastolic) throws SQLException, NamingException, ClassNotFoundException, PrepException{
        String sql = "INSERT INTO blood_pressures (user_id, systolic, diastolic) VALUES (?, ?, ?)";
        PreparedStatement statement = conn.Dbconnection().prepareStatement(sql);
        statement.setInt(1, getUserId(mobilenum));
        statement.setInt(2, systolic);
        statement.setInt(3, diastolic);
        //statement.setDate(3, (java.sql.Date) reading_dt);

        statement.executeUpdate();
    }

    public GotSMSMessagingPrep(){
        //this.ds = ds;
    }

    public String processSMS(String message,String smsnumber, String shortcode, int carrierid) throws NumberFormatException, SQLException, ClassNotFoundException, NamingException, PrepException{
        try{
        String systolic;
        String diastolic;

        message = message.trim().toLowerCase();

        if(message.matches("bp")){
            norvatisMessage = "GetOnTrack BP Tracker: Please reply back with your top and bottom BP numbers (eg, 150 over 90). Msg&DataRatesMayApply, Opt-out:Txt STOP";
        }
        else if(message.matches("stop")){
            //optout user?


            String url = "http://appdev.ipsh.net/connectormessaging/optout";
            String datasource = "java:/jdbc/novartis";
            OptoutPrep optoutPrep = new OptoutPrep(url, datasource);
            OtherResponse res = optoutPrep.doOptout(smsnumber, getUserId(smsnumber), 5, carrierid);
            if(res.getStatus().matches("FAIL")){
                throw new PrepException("Error Doing optout in GotSMSMessagingPrep.processSMS()");
            }


        	norvatisMessage = "Novartis Pharmaceuticals Corp: You're unsubscribed and will not receive Txt messages. Didn't mean to? Call 877-577-7726. Txt HELP for info or ussms.novartis.com";
        }else if(message.matches("yes")){
        	//Check If sms number is in db
        	norvatisMessage = BpLevelChecker(smsnumber);

        }
        else if(message.matches("help")){
            norvatisMessage = "GetOnTrack BP Tracker: Please reply with your top and bottom BP numbers (eg, 150 over 90). Message and Data Rates May Apply, Opt-out:Txt STOP;  For more info call: 877-577-7726";
        }
        else{
            String[] keywords = message.split("[\\s]+");

            if((keywords.length == 3) && (keywords[1].contains("over")) && keywords[0].matches("[1-9][0-9]*") && keywords[2].matches("[1-9][0-9]*")){
               //Delete SmsIf Exists
            	SmsExists(smsnumber);
            	systolic = keywords[0];
                diastolic = keywords[2];
                recordBP(smsnumber, Integer.parseInt(systolic), Integer.parseInt(diastolic));
                norvatisMessage = checkBp(Integer.parseInt(systolic), Integer.parseInt(diastolic),smsnumber);
            }else{
            	norvatisMessage = "GetOnTrack BP Tracker: We couldn't understand your response. Please reply with your BP numbers again (eg, 150 over 90).";



            }
        }
        }
        catch(PrepException pe){
            this.norvatisMessage = "You have register for GetOnTrack BP Tracker to use this service. Call 877-577-7726 for more info";
        }

        return this.norvatisMessage;
    }

    public String checkBp(int systolic, int diastolic, String smsnumber) throws SQLException, ClassNotFoundException, NamingException{
        String message = "";
        java.sql.Timestamp  sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());
       if(diastolic > systolic){
    	   message = "GetOnTrack BP Tracker: ";
           message += ("["+systolic+", "+diastolic+"] isn't a valid BP reading");
           message += ". Your higher number should be first, then your lower number. Please reply with your numbers again (eg, 150 over 90).";

        //Very Low Bp
        }else if((systolic < 90) || (diastolic < 60)){
            message = "GetOnTrack BP Tracker: The numbers you entered, ";
            message += ("["+systolic+", "+diastolic+"]");
            message += ", seem low. Please text YES if they're correct, or reply with new numbers (eg, 150 over 90).";
           //Insert Into Status Into Db Low
            PreparedStatement statement = conn.Dbconnection().prepareStatement("INSERT INTO bp_smsinteraction (sms_number,systolic,diastolic,bp_status,response) VALUES ('"+smsnumber+"','"+systolic+"','"+diastolic+"','L','"+sqlDate+"') ");
            statement.executeUpdate();
        }
        else if((systolic >=90 && systolic <=119) || (diastolic >=60 && diastolic <= 79)){
        	message = "GetOnTrack BP Tracker: Your BP, ";
            message += ("["+systolic+", "+diastolic+"]"+" is considered reasonable ");
            message += ", according to national guidelines. Check back with doctor to see if it's right for you.";
            message += "\n";
            message += "==TIPS==";
            message += "\n";
            message +=  Ms.GetMessageTip(Ms.getCoefficientValues(tipOne),GetRandnumber(4));


        }else if((systolic >=120 && systolic <=139) || (diastolic >=80 && diastolic <= 89)){
        	message = "GetOnTrack BP Tracker: Your BP, ";
            message += ("["+systolic+", "+diastolic+"]"+" is considered reasonable ");
            message += ", is considered reasonable for most people, according to national guidelines. Ask doctor if it's right for you.";
            message += "\n";
            message += "==TIPS==";
            message += "\n";
            message += Ms.GetMessageTip(Ms.getCoefficientValues(tipTwo),GetRandnumber(4));
        }else if((systolic >=140 && systolic <=179) || (diastolic >=90 && diastolic <= 109)){
        	message = "GetOnTrack BP Tracker: Your BP, ";
            message += ("["+systolic+", "+diastolic+"]"+" is too high ");
            message += ", according to national guidelines. Check back with your doctor to know what number is good for you.";
            message += "\n";
            message += "==TIPS==";
            message += "\n";
            message += Ms.GetMessageTip(Ms.getCoefficientValues(tipTwo),GetRandnumber(4));
        }
        //Very High Bp
        else if((systolic > 180 || diastolic <=110)){
        	message = "GetOnTrack BP Tracker: The numbers you entered, ";
            message += ("["+systolic+", "+diastolic+"]"+" seem extremely high ");
            message += ". Please text YES if they're correct, or reply with new numbers (eg, 150 over 90).";
        //Insert Into Status Into Db
            PreparedStatement statement = conn.Dbconnection().prepareStatement("INSERT INTO bp_smsinteraction (sms_number,systolic,diastolic,bp_status,response) VALUES ('"+smsnumber+"','"+systolic+"','"+diastolic+"','H','"+sqlDate+"') ");
            statement.executeUpdate();

        }

        return message;
    }

    public int GetRandnumber(int range)
    {
    	Random randomGenerator = new Random();
    	int randomInt = randomGenerator.nextInt(range);
		return randomInt;
    }

    public String BpLevelChecker(String smsnumber) throws SQLException, NamingException, ClassNotFoundException
    {
    	int currentRow;
		int rowCount;
		String message = "";
    	PreparedStatement statement = conn.Dbconnection().prepareStatement("SELECT sms_number,systolic,diastolic,bp_status FROM bp_smsinteraction WHERE sms_number = '"+smsnumber+"'");
        ResultSet results = statement.executeQuery();
        currentRow = results.getRow();
		   rowCount = results.last() ? results.getRow() : 0;
		   if (currentRow == 0)
		   results.beforeFirst();
		   else
	       results.absolute(currentRow);
		   if(rowCount > 0){
			   while(results.next())
			   {
				   //High
				   if(count == 0 && results.getString("bp_status").matches("H")){
					//return message
					    message = "GetOnTrack BP Tracker: Warning. Your BP, ";
			            message += ("["+results.getString("systolic")+", "+results.getString("diastolic")+"]"+" is at a severely high level ");
			            message += ". Please talk to your doctor right away. ";
			            message += "\n";
			            message += "GetOnTrack BP Tracker: Your reading has been reported to Novartis Drug Safety ";
			            message += "because it's considered extremely high. Call 1-877-577-7726 ";
			            message += "if you have questions.";

			          //Delete from db
						   PreparedStatement statementsec = conn.Dbconnection().prepareStatement("DELETE FROM bp_smsinteraction WHERE sms_number = '"+smsnumber+"'");
					       statementsec.executeUpdate();

				   }
				   //Low
				   else if(count == 0 && results.getString("bp_status").matches("L")){

				      //return message
					    message = "GetOnTrack BP Tracker: Warning. Your BP, ";
			            message += ("["+results.getString("systolic")+", "+results.getString("diastolic")+"]"+"  is at a severely low level ");
			            message += ". Please talk to your doctor right away.";
			            message += "\n";
			            message += "GetOnTrack BP Tracker: Your reading has been reported to Novartis Drug Safety ";
			            message += "because it is considered extremely low. Call 1-877-577-7726 ";
			            message += "if you have questions.";

			            //Delete from db
						   PreparedStatement statementsec = conn.Dbconnection().prepareStatement("DELETE FROM bp_smsinteraction WHERE sms_number = '"+smsnumber+"'");
					       statementsec.executeUpdate();

				   }
				   count++;
			   }

		  }else{
			  		    message = "GetOnTrack BP Tracker: We couldn't understand your response. Please reply with your BP numbers again (eg, 150 over 90).";

		  }
		   return message;

    }

    private void SmsExists(String smsnumber) throws SQLException, ClassNotFoundException, NamingException
    {
    	int currentRow;
		int rowCount;
		PreparedStatement statement = conn.Dbconnection().prepareStatement("SELECT sms_number,systolic,diastolic,bp_status FROM bp_smsinteraction WHERE sms_number = '"+smsnumber+"'");
        ResultSet results = statement.executeQuery();
        currentRow = results.getRow();
		   rowCount = results.last() ? results.getRow() : 0;
		   if (currentRow == 0)
		      results.beforeFirst();
		   else
	      results.absolute(currentRow);
		   if(rowCount > 0){
			   //Delete from db
			   PreparedStatement statementsec = conn.Dbconnection().prepareStatement("DELETE FROM bp_smsinteraction WHERE sms_number = '"+smsnumber+"'");
		       statementsec.executeUpdate();
		   }
    }

}