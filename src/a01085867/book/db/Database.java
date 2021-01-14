/**
 * Project: A00123456Lab8
 * File: Database.java
 */

package a01085867.book.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class Database implements DbConstants {

	// private static final String DB_PROPERTIES_FILENAME = "db.properties";
	// public static final String DB_DRIVER_KEY = "db.driver";
	// public static final String DB_URL_KEY = "db.url";
	// public static final String DB_USER_KEY = "db.user";
	// public static final String DB_PASSWORD_KEY = "db.password";

	private static final Logger LOG = LogManager.getLogger();
	private static Database theInstance = new Database();
	private static Connection connection;
	private static Properties properties;
	private static boolean dbTableDropRequested;

	private Database() {
	}

	/**
	 * @return the theInstance
	 */
	public static Database getTheInstance() {
		return theInstance;
	}

	public void initiate() throws FileNotFoundException, IOException {
		if (properties == null) {
			LOG.debug("Loading database properties from db.properties");
			properties = new Properties();
			properties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
		}
	}

	public static Connection getConnection() throws SQLException {
		if (connection != null) {
			return connection;
		}

		try {
			connect();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}

		return connection;
	}

	private static void connect() throws ClassNotFoundException, SQLException {
		String dbDriver = properties.getProperty(DB_DRIVER_KEY);
		LOG.debug(dbDriver);
		Class.forName(dbDriver);
		System.out.println("Driver loaded");
		connection = DriverManager.getConnection(properties.getProperty(DB_URL_KEY), properties.getProperty(DB_USER_KEY),
				properties.getProperty(DB_PASSWORD_KEY));
		LOG.debug("Database connected");
	}

	/**
	 * Close the connections to the database
	 */
	public void shutdown() {
		if (connection != null) {
			try {
				LOG.debug("Closing the DB connection");
				connection.close();
				connection = null;
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}
	}

	/**
	 * Determine if the database table exists.
	 * 
	 * @param tableName
	 * @return true is the table exists, false otherwise
	 * @throws SQLException
	 */
	public static boolean tableExists(String targetTableName) throws SQLException {
		DatabaseMetaData databaseMetaData = Database.getTheInstance().getConnection().getMetaData();
		ResultSet resultSet = null;
		String tableName = null;

		try {
			resultSet = databaseMetaData.getTables(connection.getCatalog(), "%", "%", null);
			while (resultSet.next()) {
				tableName = resultSet.getString("TABLE_NAME");
				if (tableName.equalsIgnoreCase(targetTableName)) {
					LOG.debug("Found the target table named: " + targetTableName);
					return true;
				}
			}
		} finally {
			resultSet.close();
		}

		return false;
	}

	public static void requestDbTableDrop() {
		dbTableDropRequested = true;
	}

	public static boolean dbTableDropRequested() {
		return dbTableDropRequested;
	}

	public static boolean isMsSqlServer() {
		return Database.properties.get(DB_DRIVER_KEY).toString().contains("sqlserver");
	}
}
