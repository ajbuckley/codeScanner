/**
 * 
 */
package dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author Drew Buckley
 * 
 * Class for creating connections to the SQL Database.
 * Follows singleton pattern.
 *
 */
public class ConnectionDriver {

	private InitialContext initialContext;
	private static ConnectionDriver connectionInstance;
	
	/**
	 * Used to obtain the instance of the ConnectionDriver.
	 * 
	 * @return connectionInstance : The Single connection driver
	 */
	public static ConnectionDriver getConnectionInstance(){
		if(connectionInstance == null){
			connectionInstance = new ConnectionDriver();
			
		}
		return connectionInstance;
	}
	
	protected ConnectionDriver(){
		
	}

	/**
	 * Retrieves a connection to the SQL database.
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		
		//This needs to be figured out still. Just straight copied it from iTrust.
		try {
			if (initialContext == null)
				initialContext = new InitialContext();
			return ((DataSource) (((Context) initialContext.lookup("java:comp/env"))).lookup("jdbc/itrust"))
					.getConnection();
		} catch (NamingException e) {
			throw new SQLException(("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
}
