/**
 *
 * @author Pkb
 */
package com.novartis.messaging.preps;
import java.sql.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class GetBPTrackerMainSql
{
	private Connection con;

       private InitialContext cxt = null;
       private DataSource datasource = null;

	public Connection Dbconnection() throws ClassNotFoundException, SQLException, NamingException
	{
                cxt = new InitialContext();
                datasource = (DataSource)cxt.lookup("java:/jdbc/novartis");
                con = datasource.getConnection();

            return con;
	}

	public void Closeconnection()
	{
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}