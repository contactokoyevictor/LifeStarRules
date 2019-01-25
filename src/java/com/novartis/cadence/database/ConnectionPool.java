package com.novartis.cadence.database;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.*;
import java.util.logging.*;
/**
 * @author OKOYE_VICTOR
 *
 */
public class ConnectionPool {

    public static String _errorOrWarningMessage = "";
        public static Connection _dbConn;

        public Connection makeDBConnection() throws Exception {
        try{
             Context ctx = new InitialContext();
             DataSource ds = (DataSource) ctx.lookup("java:/jdbc/novartis");
             _dbConn = ds.getConnection();
            }catch (Exception e) {
             System.out.println("SQL Exception is caught during connection creation." + e);
             Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, e.getMessage());
             _errorOrWarningMessage = e.getMessage().toString();
             _dbConn.close();
          }
            return _dbConn;
         }

        
	}



