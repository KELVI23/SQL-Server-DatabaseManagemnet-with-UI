package a01085867.book.db;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.ApplicationException;
import a01085867.book.data.Customer;
import a01085867.book.io.CustomerReader;

public class CustomerDao extends Dao implements DbConstants {

	public static final String TABLE_NAME = CUSTOMER_TABLE_NAME;
	private static final String CUSTOMERS_DATA_FILENAME = "customers.dat";
	private static Logger LOG = LogManager.getLogger(CustomerDao.class);
	private static CustomerDao theInstance = new CustomerDao();
	private static Database database;

	// public CustomerDao(Database database) throws ApplicationException {
	// super(database, TABLE_NAME);
	//
	// load();
	// }
	private CustomerDao() {
		super(TABLE_NAME);

		database = Database.getTheInstance();
	}

	public static CustomerDao getTheInstance() {
		return theInstance;
	}

	/*
	 * Load database upon instantiation
	 */
	public void load() throws ApplicationException {
		File customerDataFile = new File(CUSTOMERS_DATA_FILENAME);

		try {
			if (!Database.tableExists(CustomerDao.TABLE_NAME) || Database.dbTableDropRequested()) {
				if (Database.tableExists(CustomerDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}
				create();
				LOG.debug("Inserting the customers");
				if (!customerDataFile.exists()) {
					throw new ApplicationException(String.format("Required %s is missing", customerDataFile));

				}
				CustomerReader.read(customerDataFile, this);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
			throw new ApplicationException(e);
		}

	}

	@Override
	public void create() throws SQLException {
		LOG.debug("Creating database table" + TABLE_NAME);

		String sql = String.format("CREATE TABLE %s(" + "%s BIGINT,"//
				+ "%s VARCHAR(%d),"// fistName
				+ "%s VARCHAR(%d),"// lastName
				+ "%s VARCHAR(%d),"// street
				+ "%s VARCHAR(%d),"// city
				+ "%s VARCHAR(%d),"// postalCode
				+ "%s VARCHAR(%d),"// phone
				+ "%s VARCHAR(%d),"// email
				+ "%s DATETIME,"// joinedDate
				+ "PRIMARY KEY (%s))", // PrimaryKey ID
				TABLE_NAME, //
				Column.ID.name, //
				Column.FIRST_NAME.name, Column.FIRST_NAME.length, //
				Column.LAST_NAME.name, Column.LAST_NAME.length, //
				Column.STREET.name, Column.STREET.length, //
				Column.CITY.name, Column.CITY.length, //
				Column.POSTAL_CODE.name, Column.POSTAL_CODE.length, //
				Column.PHONE.name, Column.PHONE.length, //
				Column.EMAIL_ADDRESS.name, Column.EMAIL_ADDRESS.length, //
				Column.JOINED_DATE.name, //
				Column.ID.name);
		super.create(sql);

	}

	public void add(Customer customer) throws SQLException {
		String sql = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sql, //
				customer.getId(), //
				customer.getFirstName(), //
				customer.getLastName(), //
				customer.getStreet(), //
				customer.getPostalCode(), //
				customer.getPhone(), //
				customer.getEmailAddress(), //
				toTimestamp(customer.getJoinedDate()));
		LOG.debug(String.format("Adding %s was %s", customer, result));
	}

	/*
	 * Update the customer
	 */
	public void update(Customer customer) throws SQLException {
		String sql = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, WHERE %s=?", TABLE_NAME, //
				Column.FIRST_NAME.name, //
				Column.LAST_NAME.name, //
				Column.STREET.name, //
				Column.POSTAL_CODE.name, //
				Column.PHONE.name, //
				Column.EMAIL_ADDRESS.name, //
				Column.JOINED_DATE.name, //
				Column.ID.name);
		LOG.debug("Update statement: " + sql);
		boolean result = execute(sql, //
				customer.getId(), //
				customer.getFirstName(), //
				customer.getLastName(), //
				customer.getStreet(), //
				customer.getPostalCode(), //
				customer.getPhone(), //
				customer.getEmailAddress(), //
				toTimestamp(customer.getJoinedDate()));
		LOG.debug(String.format("Updating %s was %s", customer, result));
	}

	public void delete(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, customer.getId());
			int rowcount = statement.executeUpdate(sql);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/*
	 * Retrieve all customer IDS from the database
	 * @return the list of Customer IDS
	 */
	public List<Long> getCustomerIds() throws SQLException {
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
		} finally {
			close(statement);
		}

		return ids;

	}

	/*
	 * Retrieve specific customer using customer ID
	 */

	public Customer getCustomer(Long customerId) throws Exception {
		String sql = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, customerId);

		Statement statement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result but got %d", count));
				}
				Timestamp timestamp = resultSet.getTimestamp(Column.JOINED_DATE.name);
				LocalDate date = timestamp.toLocalDateTime().toLocalDate();

				Customer customer = new Customer.Builder(resultSet.getInt(Column.ID.name), resultSet.getString(Column.PHONE.name))//
						.setFirstName(resultSet.getString(Column.FIRST_NAME.name))//
						.setLastName(resultSet.getString(Column.LAST_NAME.name)) //
						.setStreet(resultSet.getString(Column.STREET.name)) //
						.setCity(resultSet.getString(Column.CITY.name)) //
						.setPostalCode(resultSet.getString(Column.POSTAL_CODE.name)) //
						.setEmailAddress(resultSet.getString(Column.EMAIL_ADDRESS.name)) //
						.setJoinedDate(date)//
						.build();

				return customer;
			}

		} finally {
			close(statement);
		}
		return null;

	}

	/*
	 * Count customers
	 */
	public int countAllCustomers() throws Exception {
		Statement statement = null;
		int count = 0;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT COUNT(*) AS total FROM %s", tableName);
			ResultSet resultSet = statement.executeQuery(sqlString);
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}
		} finally {
			close(statement);
		}
		return count;
	}

	/*
	 * table columns
	 */
	public enum Column {
		ID("id", 16), // 1
		FIRST_NAME("firstName", 20), // 2
		LAST_NAME("lastName", 20), // 3
		STREET("street", 40), // 4
		CITY("city", 20), // 5
		POSTAL_CODE("postalCode", 6), // 6
		PHONE("phone", 10), // 7
		EMAIL_ADDRESS("emailAddress", 40), // 8
		JOINED_DATE("joinedDate", 8);// 9

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}
	}

}
