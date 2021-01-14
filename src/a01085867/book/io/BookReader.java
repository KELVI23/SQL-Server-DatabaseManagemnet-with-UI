/**
 * Project: Books
 * File: BookReader.java
 */

package a01085867.book.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.ApplicationException;
import a01085867.book.data.Book;
import a01085867.book.db.BookDao;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class BookReader extends Reader {

	public static final String FILENAME = "books500.csv";

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * private constructor to prevent instantiation
	 */
	private BookReader() {
	}

	/**
	 * Read the book input data.
	 * 
	 * @return A collection of books.
	 * @throws ApplicationException
	 * @throws IOException
	 */
	public static int read(File bookDataFile, BookDao dao) throws ApplicationException {
		int bookCount = 0;
		File file = new File(FILENAME);
		FileReader in;
		Iterable<CSVRecord> records;
		try {
			in = new FileReader(file);
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}

		Map<Long, Book> books = new HashMap<>();

		LOG.debug("Reading" + file.getAbsolutePath());
		for (CSVRecord record : records) {
			String id = record.get("book_id");
			String isbn = record.get("isbn");
			String authors = record.get("authors");
			String original_publication_year = record.get("original_publication_year");
			String original_title = record.get("original_title");
			String average_rating = record.get("average_rating");
			String ratings_count = record.get("ratings_count");
			String image_url = record.get("image_url");

			Book book = new Book.Builder(Long.parseLong(id)). //
					setIsbn(isbn). //
					setAuthors(authors). //
					setYear(Integer.parseInt(original_publication_year)). //
					setTitle(original_title). //
					setRating(Float.parseFloat(average_rating)). // //
					setRatingsCount(Integer.parseInt(ratings_count)). //
					setImageUrl(image_url).//
					build();

			books.put(book.getId(), book);
			LOG.trace("Added " + book.toString() + " as " + id);
		}

		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				throw new ApplicationException(e);
			}
		}
		List<Book> booksList = new ArrayList<>(books.values());
		try {
			for (Book book : booksList) {
				dao.add(book);
				bookCount++;
			}
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
		return bookCount;
	}
}

/*
 * public static Map<Long, Book> read() throws ApplicationException {
 * File file = new File(FILENAME);
 * FileReader in;
 * Iterable<CSVRecord> records;
 * FileWriter out;
 * CSVPrinter printer;
 * try {
 * in = new FileReader(file);
 * records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
 * out = new FileWriter(new File("books500.csv"));
 * printer = new CSVPrinter(out, CSVFormat.DEFAULT);// CSVFormat.DEFAULT.print(new File("cleanbooks.csv"), charset);
 * } catch (IOException e) {
 * throw new ApplicationException(e);
 * }
 * for (CSVRecord record : records) {
 * String book_id = record.get("book_id");
 * String goodreads_book_id = record.get("goodreads_book_id");
 * String best_book_id = record.get("best_book_id");
 * String work_id = record.get("work_id");
 * String books_count = record.get("books_count");
 * String isbn = record.get("isbn");
 * String isbn13 = record.get("isbn13");
 * String authors = record.get("authors");
 * String original_publication_year = record.get("original_publication_year");
 * String original_title = record.get("original_title");
 * String title = record.get("title");
 * String language_code = record.get("language_code");
 * String average_rating = record.get("average_rating");
 * String ratings_count = record.get("ratings_count");
 * String work_ratings_count = record.get("work_ratings_count");
 * String work_text_reviews_count = record.get("work_text_reviews_count");
 * String ratings_1 = record.get("ratings_1");
 * String ratings_2 = record.get("ratings_2");
 * String ratings_3 = record.get("ratings_3");
 * String ratings_4 = record.get("ratings_4");
 * String ratings_5 = record.get("ratings_5");
 * String image_url = record.get("image_url");
 * String small_image_url = record.get("small_image_url");
 * if (original_publication_year.length() < 4) {
 * continue;
 * }
 * Object[] o = new Object[] { book_id, isbn, authors, original_publication_year.substring(0, 4), original_title, average_rating,
 * ratings_count, image_url };
 * try {
 * printer.printRecord(o);
 * } catch (IOException e) {
 * // TODO Auto-generated catch block
 * e.printStackTrace();
 * }
 * }
 */
