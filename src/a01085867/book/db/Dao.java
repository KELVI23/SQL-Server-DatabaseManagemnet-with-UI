/**
 * Project: A00123456Lab8
 * File: Dao.java
 */

package a01085867.book.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Sam Cirka, A00123456
 *
 */
public abstract class Dao {
	private static Logger LOG = LogManager.getLogger();
	protected final Database database;
	protected final String tableName;

	// protected Dao(Database database, String tableName) {
	// this.database = database;
	// this.tableName = tableName;
	// }
	protected Dao(String tableName) {
		database = Database.getTheInstance();
		this.tableName = tableName;
	}

	public abstract void create() throws SQLException;

	/**
	 * @return the database
	 */
	public Database getDatabase() {
		return database;
	}

	/**
	 * Delete the database table
	 * 
	 * @throws SQLException
	 */
	public void drop() throws SQLException {
		Statement statement = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			if (Database.tableExists(tableName)) {
				LOG.debug("drop table " + tableName);
				statement.executeUpdate("drop table " + tableName);
			}
		} finally {
			close(statement);
		}
	}

	/**
	 * Tell the database we're shutting down.
	 */
	public void shutdown() {
		database.shutdown();
		LOG.debug("database shutdown");
	}

	protected void create(String createStatement) throws SQLException {
		LOG.debug(createStatement);
		Statement statement = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(createStatement);
		} finally {
			close(statement);
		}
	}

	protected boolean execute(String preparedStatementString, Object... args) throws SQLException {
		LOG.debug(preparedStatementString);
		boolean result = false;
		PreparedStatement statement = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.prepareStatement(preparedStatementString);
			int i = 1;
			for (Object object : args) {
				if (object instanceof String) {
					statement.setString(i, object.toString());
				} else if (object instanceof Boolean) {
					statement.setBoolean(i, (Boolean) object);
				} else if (object instanceof Integer) {
					statement.setInt(i, (Integer) object);
				} else if (object instanceof Long) {
					statement.setLong(i, (Long) object);
				} else if (object instanceof Float) {
					statement.setFloat(i, (Float) object);
				} else if (object instanceof Double) {
					statement.setDouble(i, (Double) object);
				} else if (object instanceof Byte) {
					statement.setByte(i, (Byte) object);
				} else if (object instanceof Timestamp) {
					statement.setTimestamp(i, (Timestamp) object);
				} else if (object instanceof LocalDateTime) {
					statement.setTimestamp(i, Timestamp.valueOf((LocalDateTime) object));
				} else {
					statement.setString(i, object.toString());
				}

				i++;
			}

			result = statement.execute();
		} finally {
			close(statement);
		}

		return result;
	}

	protected void close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			LOG.error("Failed to close statment" + e);
		}
	}

	public static Timestamp toTimestamp(LocalDate date) {
		return Timestamp.valueOf(LocalDateTime.of(date, LocalTime.now()));
	}

	public static Timestamp toTimestamp(LocalDateTime dateTime) {
		return Timestamp.valueOf(dateTime);
	}
}
