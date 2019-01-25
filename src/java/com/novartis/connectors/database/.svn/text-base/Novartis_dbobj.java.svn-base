package com.novartis.connectors.database;

/**
 *
 * @author Infonaligy
 *
 */

import java.sql.*;

import javax.sql.DataSource;
import javax.naming.*;

public class Novartis_dbobj {
    protected String _errorOrWarningMessages = "";
    protected Connection _dbConnection;
    protected Statement _insertStatement;
    protected Statement _selectStatement;
    protected Statement _updateStatement;
    protected Statement _deleteStatement;

    protected void makeDBConnection() throws Exception {
        if (_dbConnection != null && !_dbConnection.isClosed()) return; // Already have a good connection

        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/jdbc/novartis");
            _dbConnection = ds.getConnection();
        } catch (Exception e) {
            System.out.println("SQL Exception is caught during connection creation:" + e);
            _errorOrWarningMessages = e.getMessage().toString();
            _dbConnection.close();
        }
    }
    
    public void executeInsert()
    {
    }


}

/*

                            makeDBConnection();
                            _sqlStatement = _dbConn.prepareStatement("SELECT id, drug_name FROM `novartis`.`drugs`");
//                            _sqlStatement.setInt(1, consumerID);
                            ResultSet rs = _sqlStatement.executeQuery();
                            while (rs.next()) {


 */