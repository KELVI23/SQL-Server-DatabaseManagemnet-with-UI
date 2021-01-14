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
import a01085867.book.data.Book;
import a01085867.book.io.BookReader;

public class BookDao extends Dao implements DbConstants {

	private static BookDao theInstance = new BookDao();
	public static final String TABLE_NAME = BOOK_TABLE_NAME;
	private static final String BOOK_DATA_FILENAME = "books500.csv";
	private static Logger LOG = LogManager.getLogger(BookDao.class);
	private static Database database;

	// protected BookDao(Database database) throws ApplicationException {
	// super(database, TABLE_NAME);
	// load();
	// }
	private BookDao() {
		super(TABLE_NAME);

		database = Database.getTheInstance();
	}

	public static BookDao getTheInstance() {
		return theInstance;
	}

	public void load() throws ApplicationException {
		File bookDataFile = new File(BOOK_DATA_FILENAME);
		try {
			if (!Database.tableExists(BookDao.TABLE_NAME) || Database.dbTableDropRequested()) {
				if (Database.tableExists(BookDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}
				create();
				LOG.debug("Inserting the purchases");
				if (!bookDataFile.exists()) {
					throw new ApplicationException(String.format("Required file %s is missing", bookDataFile));

				}
				BookReader.read(bookDataFile, this);

			}
		} catch (SQLException e) {
			LOG.debug(e);
			throw new ApplicationException(e);
		}
	}

	@Override
	public void create() throws SQLException {
		LOG.debug("Creating database table " + TABLE_NAME);

		String sqlString = String.format("CREATE TABLE %s(" //
				+ "%s BIGINT, " // ID
				+ "%s VARCHAR(%d), " // ISBN
				+ "%s VARCHAR(%d), " // AUTHORS
				+ "%s INT, " // YEAR
				+ "%s VARCHAR(%d), " // TITLE
				+ "%s FLOAT, " // RATING
				+ "%s INT, " // RATING_COUNT
				+ "%s VARCHAR(%d), " // IMAGE_URL
				+ "PRIMARY KEY (%s))", // ID
				TABLE_NAME, //
				Column.ID.name, //
				Column.ISBN.name, Column.ISBN.length, //
				Column.AUTHORS.name, Column.AUTHORS.length, //
				Column.YEAR.name, //
				Column.TITLE.name, Column.TITLE.length, //
				Column.RATING.name, //
				Column.RATING_COUNT.name, //
				Column.IMAGE_URL.name, Column.IMAGE_URL.length, //
				Column.ID.name);

		super.create(sqlString);
	}

	public void add(Book book) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				book.getId(), //
				book.getIsbn(), //
				book.getAuthors(), //
				book.getYear(), //
				book.getTitle(), //
				book.getRating(), //
				book.getRatingsCount(), //
				book.getImageUrl());
		LOG.debug(String.format("Adding %s was %s", book, result ? "successful" : "unsuccessful"));
	}

	/**
	 * Update the book.
	 *
	 * @param book
	 * @throws SQLException
	 */
	public void update(Book book) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", TABLE_NAME, //
				Column.ISBN.name, //
				Column.AUTHORS.name, //
				Column.YEAR.name, //
				Column.TITLE.name, //
				Column.RATING.name, //
				Column.RATING_COUNT.name, //
				Column.IMAGE_URL.name, //
				Column.ID.name);
		LOG.debug("Update statment: " + sqlString);

		boolean result = execute(sqlString, //
				book.getIsbn(), //
				book.getAuthors(), //
				book.getYear(), //
				book.getTitle(), //
				book.getRating(), //
				book.getRatingsCount(), //
				book.getImageUrl(), //
				book.getId());
		LOG.debug(String.format("Updating %s was %s", book, result ? "successful" : "unsuccessful"));
	}

	/**
	 * Delete the book from the database.
	 *
	 * @param book
	 * @throws SQLException
	 */
	public void delete(Book book) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, book.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieve all the book IDs from the database
	 *
	 * @return the list of book IDs
	 * @throws SQLException
	 */
	public List<Long> getBookIds() throws SQLException {
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

		LOG.debug(String.format("Loaded %d books IDs from the database", ids.size()));

		return ids;
	}

	/**
	 * @param bookId
	 * @return
	 * @throws Exception
	 */
	public Book getBook(Long bookId) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, bookId);
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

				Book book = new Book.Builder(resultSet.getInt(Column.ID.name)). //
						setIsbn(resultSet.getString(Column.ISBN.name)). //
						setAuthors(resultSet.getString(Column.AUTHORS.name)). //
						setYear(resultSet.getInt(Column.YEAR.name)). //
						setTitle(resultSet.getString(Column.TITLE.name)). //
						setRating(resultSet.getFloat(Column.RATING.name)). // //
						setRatingsCount(resultSet.getInt(Column.RATING_COUNT.name)). //
						setImageUrl(resultSet.getString(Column.IMAGE_URL.name)).//
						build();

				return book;
			}
		} finally {
			close(statement);
		}

		return null;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public int countAllBooks() throws Exception {
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
		} finally {
			close(statement);
		}
		return count;
	}

	public enum Column {
		ID("id", 16), //
		ISBN("isbn", 15), //
		AUTHORS("authors", 100), //
		YEAR("publicationYear", 5), //
		TITLE("title", 100), //
		RATING("rating", 5), //
		RATING_COUNT("ratingsCount", 10), //
		IMAGE_URL("imageUrl", 100); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}

	}

}