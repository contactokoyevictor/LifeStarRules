package com.novartis.cadence.database;

import java.sql.Timestamp;
import java.util.Calendar;

public class User {

//=============================================================================
	private int weekOfMonth;
	private int weekOfYear;
	private int dayOfWeek;
	private int dayOfMonth;
	private int userID = 0;
	private String segmentCode;
	private int numberOfWeek;
	private boolean lastWeekOfMonth = false;
	private int weeksOfMonth;
	private Timestamp smsOptoutDate;
	private Timestamp smsOptinDate;
	private String bpBuddy = "";
	private String smsKitCode = "";
	private String emailKitCode = "";
	private String kitCode = "";

//=============================================================================
	public User()
	{
		try {
			Calendar now = Calendar.getInstance();
			dayOfWeek 	 = now.get(Calendar.DAY_OF_WEEK);
			dayOfMonth 	 = now.get(Calendar.DAY_OF_MONTH);
			weekOfMonth  = now.get(Calendar.WEEK_OF_MONTH);
			weekOfYear  = now.get(Calendar.WEEK_OF_YEAR);
			weeksOfMonth = now.getActualMaximum(Calendar.WEEK_OF_MONTH);

			if(weeksOfMonth == now.get(Calendar.WEEK_OF_MONTH)){
		        lastWeekOfMonth = true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
//=============================================================================
	public void setUserID(int userid)
	{
		userID = userid;
	}
	public int getUserID()
	{
		return userID;
	}
//=============================================================================
	public void setSegmentCode(String segCode)
	{
		segmentCode = segCode;
	}
	public String getSegmentCode()
	{
		return segmentCode;
	}
//=============================================================================
	public void setNumberOfWeek(int numberofweek)
	{
		numberOfWeek = numberofweek;
	}
	public int getNumberOfWeek()
	{
		return numberOfWeek;
	}
//=============================================================================
	public void setLastWeekOfMonth(boolean status)
	{
		lastWeekOfMonth = status;
	}
	public boolean getLastWeekOfMonth()
	{
		return lastWeekOfMonth;
	}
//=============================================================================
	public void setWeekOfMonth(int weekofmonth)
	{
		this.weekOfMonth = weekofmonth;
	}
	public int getWeekOfMonth()
	{
		return this.weekOfMonth;
	}
//==============================================================================
	public void setWeekOfYear(int weekofyear)
	{
		this.weekOfYear = weekofyear;
	}
	public int getWeekOfYear()
	{
		return this.weekOfYear;
	}
//==============================================================================
	public void setDayOfWeek(int dayofweek)
	{
		this.dayOfWeek = dayofweek;
	}
	public int getDayOfWeek()
	{
		return this.dayOfWeek;
	}
//===============================================================================
	public void setDayOfMonth(int dayofmonth)
	{
		this.dayOfMonth = dayofmonth;
	}
	public int getDayOfMonth()
	{
		return this.dayOfMonth;
	}
//===============================================================================
	public void setSmsOptoutDate(Timestamp optoutdate)
	{
		this.smsOptoutDate = optoutdate;
	}
	public Timestamp getSmsOptoutDate()
	{
		return this.smsOptoutDate;
	}
//===============================================================================
	public void setSmsOptinDate(Timestamp optindate)
	{
		this.smsOptinDate = optindate;
	}
	public Timestamp getSmsOptinDate()
	{
		return this.smsOptinDate;
	}
//===============================================================================
	public void setBpBuddy(String optStatus)
	{
		this.bpBuddy = optStatus;
	}
	public String getBpBuddy()
	{

			return this.bpBuddy.toLowerCase();
	}
//===============================================================================
	public void setSmsKitCode(String kitCode)
	{
		this.smsKitCode = kitCode;
	}
	public String getSmsKitCode()
	{

			return this.smsKitCode.toUpperCase()+ numberOfWeek;
	}
//===============================================================================
	public void setEmailKitCode(String kitCode)
	{
		this.emailKitCode = kitCode;
	}
	public String getEmailKitCode()
	{

			return this.emailKitCode.toUpperCase() + numberOfWeek;
	}
//===============================================================================
	public void setKitCode(String kitCode)
	{
		this.kitCode = kitCode;
	}
	public String getKitCode()
	{

			return this.kitCode.toUpperCase();
	}
}
//===============================================================================

