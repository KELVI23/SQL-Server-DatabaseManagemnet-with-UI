/**
 * 
 */
package a01085867.book.db;

/**
 * @author scirka
 *
 */
public interface DbConstants {

	String DB_PROPERTIES_FILENAME = "db.properties";
	String DB_DRIVER_KEY = "db.driver";
	String DB_URL_KEY = "db.url";
	String DB_USER_KEY = "db.user";
	String DB_PASSWORD_KEY = "db.password";
	String TABLE_ROOT = "A01085867_";
	String CUSTOMER_TABLE_NAME = TABLE_ROOT + "Customers";
	String BOOK_TABLE_NAME = TABLE_ROOT + "Books";
	String PURCHASES_TABLE_NAME = TABLE_ROOT + "Purchases";

}
