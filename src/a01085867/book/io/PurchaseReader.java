/**
 * Project: Books
 * File: PurchaseReader.java
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
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.ApplicationException;
import a01085867.book.data.AllData;
import a01085867.book.data.Purchase;
import a01085867.book.db.PurchasesDao;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class PurchaseReader extends Reader {

	public static final String FILENAME = "purchases.csv";

	private static final Logger LOG = LogManager.getLogger();
	private static Set<Long> customerIds = AllData.getCustomers().keySet();
	private static Long[] customerIdArray = customerIds.toArray(new Long[0]);
	private static int customerIdCount = customerIdArray.length;
	private static Set<Long> bookIds = AllData.getBooks().keySet();
	private static Long[] bookIdArray = bookIds.toArray(new Long[0]);
	private static int bookIdCount = bookIdArray.length;

	/**
	 * private constructor to prevent instantiation
	 */
	private PurchaseReader() {
	}

	/**
	 * Read the inventory input data.
	 * 
	 * @return the inventory.
	 * @throws ApplicationException
	 */
	public static int read(File purchaseDataFile, PurchasesDao dao) throws ApplicationException {
		int purchaseCount = 0;
		File file = new File(FILENAME);
		FileReader in;
		Iterable<CSVRecord> records;
		try {
			in = new FileReader(file);
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}

		Map<Long, Purchase> purchases = new HashMap<>();
		LOG.debug("Reading" + file.getAbsolutePath());
		for (CSVRecord record : records) {
			long id = Long.parseLong(record.get("id"));
			long customerId = Long.parseLong(record.get("customer_id"));
			long bookId = Long.parseLong(record.get("book_id"));
			float price = Float.parseFloat(record.get("price"));

			Purchase purchase = new Purchase.Builder(id, customerId, bookId, price).build();
			purchases.put(purchase.getId(), purchase);
			LOG.debug("Added " + purchase.toString() + " as " + purchase.getId());

			if (!customerIds.contains(customerId)) {
				customerId = customerIdArray[(int) (Math.random() * customerIdCount)];
			}
			if (!bookIds.contains(bookId)) {
				bookId = bookIdArray[(int) (Math.random() * bookIdCount)];
			}
		}

		// return purchases;
		LOG.debug("All purchases added");
		List<Purchase> purchasesList = new ArrayList<>(purchases.values());
		try {
			for (Purchase purchase : purchasesList) {
				dao.add(purchase);
				LOG.debug("Added " + purchase.toString() + " to the database as " + purchase.getId());
				purchaseCount++;
			}
			LOG.debug("All purchases added");
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}

		return purchaseCount;
	}

}
