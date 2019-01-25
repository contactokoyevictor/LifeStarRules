
package com.novartis.connectors.schedulers;
import javax.sql.DataSource;
import java.sql.*;
import javax.naming.*;
/**
 *
 * @author VICTOR_OKOYE
 */
public class RemindSender {

        private DataSource datasource;
        private int campaignid;
        private int consumerid;
	private String reminder_id ="";
	private String reminder_type = "";
	private String weekdays = "";
	private String start_date = "";
	private String time = "";
	private String frequency = "";
	private String email = "";
	private String sms = "";
        private String times = "";
        private java.util.List<String> weekday;
	private Connection conn = null;
	private String ERRORorWARNING = "";

        public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getReminder_id() {
		return reminder_id;
	}
	public void setReminder_id(String reminder_id) {
		this.reminder_id = reminder_id;
	}
	public String getReminder_type() {
		return reminder_type;
	}
	public void setReminder_type(String reminder_type) {
		this.reminder_type = reminder_type;
	}
	public String getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}
	public java.util.List<String> getWeekday() {
		return weekday;
	}
	public void setWeekday(java.util.List<String> weekday) {
		this.weekday = weekday;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public String getERRORorWARNING() {
		return ERRORorWARNING;
	}
	public void setERRORorWARNING(String eRRORorWARNING) {
		ERRORorWARNING = eRRORorWARNING;
	}
        public int getConsumerid() {
		return consumerid;
	}
	public void setConsumerid(int consumerid) {
		this.consumerid = consumerid;
	}
        public int getCampaignid() {
		return campaignid;
	}
	public void setCampaignid(int campaignid) {
		this.campaignid = campaignid;
	}
	public String getMessages(){
               return ERRORorWARNING;
	}
        public void SendReminderToAll(String segment_code, String channel, String frequency_type)throws Exception{
            if(channel.equalsIgnoreCase("sms")){
               sendSMS_AllType(segment_code,frequency_type);
            }
            if(channel.equalsIgnoreCase("email")){
               sendSMS_AllType(segment_code,frequency_type);
            }
        }
        public void sendSMS_AllType(String segment_code, String frequency_type)throws Exception
        {
         PreparedStatement stat = null;
         if(frequency_type.equalsIgnoreCase("Once")){
              try {
		      makeDBConnection();
                      stat = conn.prepareStatement("SELECT U.id, U.user_fname, U.user_lname, U.user_mphone, U.segment_code, S.Schedule_type, S.active_weekdays,S.frequency_type, S.message_channel_id, U.buddy_type FROM User AS U LEFT JOIN scheduler as S on U.id = S.id where (U.account_start_dt is not null and S.frequency_type = ? AND segment_code = ? AND last_run IS NULL AND S.active_weekdays LIKE (SELECT Date_Format(curdate(), '%a')) AND sms_opt_in_dt IS NOT NULL)");
                      stat.setString(1, frequency_type);
                      stat.setString(2, segment_code);
                      stat.executeQuery();
                      ResultSet rs = stat.executeQuery();
                      if(rs.next()){
                          System.out.println("BY ONCE SMS Sent For :" +rs.getString(2)+" -"+ rs.getString(4));
                      }
                      rs.close();
                      stat.close();

                  }
                  catch(Exception e){
                      e.getMessage();
                      stat.close();
                  }
                 }
            else if (frequency_type.equalsIgnoreCase("Daily")) {
            }
            else if(frequency_type.equalsIgnoreCase("Bi-Daily"))
            {
            }
            else if(frequency_type.equalsIgnoreCase("Weekly"))
            {
            }
            else if (frequency_type.equalsIgnoreCase("Bi-Weekly")) {
            }
            else if(frequency_type.equalsIgnoreCase("Monthly"))
            {
            }
            else if(frequency_type.equalsIgnoreCase("Bi-Monthly")){}
            else{
                if(frequency_type.equalsIgnoreCase("Anually")){
                }
            }


        }
        public void sendEmail_AllType(String segment_code, String frequency_type)throws Exception
        {
         PreparedStatement stat = null;
         if(frequency_type.equalsIgnoreCase("Once")){
              try {
		      makeDBConnection();
                      stat = conn.prepareStatement("SELECT U.id, U.user_fname, U.user_lname, U.user_mphone, U.segment_code, S.Schedule_type, S.active_weekdays,S.frequency_type, S.message_channel_id, U.buddy_type FROM User AS U LEFT JOIN scheduler as S on U.id = S.id where (U.account_start_dt is not null and S.frequency_type = ? AND segment_code = ? AND last_run IS NULL AND S.active_weekdays LIKE (SELECT Date_Format(curdate(), '%a')) AND sms_opt_in_dt IS NOT NULL)");
                      stat.setString(1, frequency_type);
                      stat.setString(2, segment_code);
                      stat.executeQuery();
                      ResultSet rs = stat.executeQuery();
                      System.out.println("BY ONCE SMS Sent For");
                      if(rs.next()){
                          System.out.println("BY ONCE SMS Sent For :" +rs.getString(2)+" -"+ rs.getString(4));
                      }
                       rs.close();
                      stat.close();
                  }
                  catch(Exception e){}
                 }
            else if (frequency_type.equalsIgnoreCase("Daily")) {
            }
            else if(frequency_type.equalsIgnoreCase("Bi-Daily"))
            {
            }
            else if(frequency_type.equalsIgnoreCase("Weekly"))
            {
            }
            else if (frequency_type.equalsIgnoreCase("Bi-Weekly")) {
            }
            else if(frequency_type.equalsIgnoreCase("Monthly"))
            {
            }
            else if(frequency_type.equalsIgnoreCase("Bi-Monthly")){}
            else{
                if(frequency_type.equalsIgnoreCase("Anually")){
                }
            }
        }

        public void processReminders(int campaignid, int consumerid, int reminder_type)throws Exception
        {}

        public void makeDBConnection()throws Exception{
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

}
