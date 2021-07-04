/**
 * Project: A01085867_Assignment2_2021
 * File: BookDao.java
 * Date: Jun. 24, 2021
 * Time: 9:59:04 p.m.
 */
package a01085867.book.db;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.ApplicationException;
import a01085867.book.data.Purchase;
import a01085867.book.io.PurchaseReader;

public class PurchasesDao extends Dao implements DbConstants {

	public static final String TABLE_NAME = PURCHASES_TABLE_NAME;
	private static final String PURCHASES_DATA_FILENAME = "purchases.csv";
	private static final Logger LOG = LogManager.getLogger(PurchasesDao.class);
	private static PurchasesDao theInstance = new PurchasesDao();
	@SuppressWarnings("unused")
	private static Database database;

	private PurchasesDao() {
		super(TABLE_NAME);
		database = Database.getTheInstance();
	}

	public static PurchasesDao getTheInstance() {
		return theInstance;
	}

	public void load() throws ApplicationException {
		File purchasesDataFile = new File(PURCHASES_DATA_FILENAME);
		try {
			if (!Database.tableExists(TABLE_NAME) || (Database.dbTableDropRequested())) {
				if (Database.tableExists(PurchasesDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}
				create();
				LOG.debug("Inserting purchases into database");
				if (!purchasesDataFile.exists()) {
					throw new ApplicationException(String.format("Required file %s is missing", PURCHASES_DATA_FILENAME));

				}
				PurchaseReader.read(purchasesDataFile, this);

			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
	}

	@Override
	public void create() throws SQLException {
		LOG.debug("Creating database table " + TABLE_NAME);

		String sqlString = String.format("CREATE TABLE %s("//
				+ "%s BIGINT,"// ID
				+ "%s BIGINT," // CUSTOMER_ID
				+ "%s BIGINT," // BOOK_ID
				+ "%s FlOAT," // PRICE
				+ "PRIMARY KEY (%s),"// ID
				+ "CONSTRAINT CUST_FK " // 7
				+ "FOREIGN KEY (%s) " // CUSTOMER_ID 8
				+ "REFERENCES %s (%s), " // 9
				+ "CONSTRAINT BOOK_FK " // 10
				+ "FOREIGN KEY (%s) " // BOOK_ID 11
				+ "REFERENCES %s (%s)) ", // 12
				TABLE_NAME, // 1
				Column.ID.name, //
				Column.CUSTOMER_ID.name, //
				Column.BOOK_ID.name, //
				Column.PRICE.name, //
				Column.ID.name, //
				Column.CUSTOMER_ID.name, // 7
				CUSTOMER_TABLE_NAME, Column.CUSTOMER_ID.name, // 8,9
				Column.BOOK_ID.name, // 10
				BOOK_TABLE_NAME, // 11
				Column.BOOK_ID.name);// 12

		super.create(sqlString);

	}

	/**
	 * Insert into database
	 * 
	 * @param purchase
	 * @throws SQLException
	 */

	public void add(Purchase purchase) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?,?,?,?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				purchase.getId(), //
				purchase.getCustomerId(), //
				purchase.getBookId(), //
				purchase.getPrice());
		LOG.debug(String.format("Adding %s was %s", purchase, result ? "successful" : "unsuccessful"));
	}

	/**
	 * Update the purchase
	 * 
	 * @param purchase
	 * @throws SQLException
	 */
	public void update(Purchase purchase) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=?", TABLE_NAME, //
				Column.CUSTOMER_ID.name, //
				Column.BOOK_ID.name, //
				Column.PRICE.name, //
				Column.ID.name);
		LOG.debug("Update statment: " + sqlString);

		boolean result = execute(sqlString, //
				purchase.getCustomerId(), //
				purchase.getBookId(), //
				purchase.getPrice(), //
				purchase.getId());

		LOG.debug(String.format("Updating %s was %s", purchase, result ? "successful" : "unsuccessful"));

	}

	/**
	 * Delete the book from the database.
	 *
	 * @param purchase
	 * @throws SQLException
	 */
	public void delete(Purchase purchase) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, purchase.getId());
			LOG.debug(sqlString);
			int rowCount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowCount));
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieve all the purchase IDs from the database
	 *
	 * @return the list of purchase IDs
	 * @throws SQLException
	 */
	public List<Long> getPurchaseId() throws SQLException {
		List<Long> ids = new ArrayList<>();

		String selectString = String.format("SELECT %s FROM %s", Column.ID.name, TABLE_NAME);
		LOG.debug(selectString);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				ids.add(resultSet.getLong(Column.ID.name));
			}

		} catch (SQLException e) {
			LOG.error(e.getMessage());

		} finally {
			close(statement);
		}

		LOG.debug(String.format("Loaded %d purchase IDs from the database", ids.size()));

		return ids;
	}

	/**
	 * @param purchaseId
	 * @return
	 * @throws Exception
	 */
	public Purchase getPurchases(Long purchaseId) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, purchaseId);
		LOG.debug(sqlString);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);

			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}

				Purchase purchase = new Purchase.Builder(resultSet.getLong(Column.ID.name), //
						resultSet.getLong(Column.CUSTOMER_ID.name), //
						resultSet.getLong(Column.BOOK_ID.name), //
						resultSet.getFloat(Column.PRICE.name)).build();

				return purchase;
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());

		} finally {
			close(statement);
		}

		return null;
	}

	/**
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public int countAllPurchases() throws SQLException {
		Statement statement = null;
		int count = 0;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT COUNT(*) AS total FROM %s", TABLE_NAME);
			ResultSet resultSet = statement.executeQuery(sqlString);
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		} finally {
			close(statement);
		}
		return count;
	}

	public enum Column {
		ID("id", 16), //
		CUSTOMER_ID("customerId", 16), //
		BOOK_ID("bookId", 16), //
		PRICE("price", 10); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}
	}

}
