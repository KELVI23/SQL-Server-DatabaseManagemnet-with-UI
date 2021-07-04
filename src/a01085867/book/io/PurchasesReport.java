/**
 * s * Project: Books
 * File: PurchasesReport.java
 */

package a01085867.book.io;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.BookOptions;
import a01085867.book.data.AllData;
import a01085867.book.data.Book;
import a01085867.book.data.Customer;
import a01085867.book.data.Purchase;
import a01085867.book.data.util.Common;
import a01085867.book.db.BookDao;
import a01085867.book.db.CustomerDao;
import a01085867.book.sorters.Filters;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class PurchasesReport {

	public static final String REPORT_FILENAME = "purchases_report.txt";
	public static final String HORIZONTAL_LINE = "-------------------------------------------------------------------------------------------------------------------";
	public static final String HEADER_FORMAT = "%-24s %-80s %-8s";
	public static final String ROW_FORMAT = "%-24s %-80s $%.2f";

	private static final Logger LOG = LogManager.getLogger();

	private static List<Item> items;

	/**
	 * Print the report.
	 *
	 * @param out
	 */
	public static void print(PrintStream out) {
		LOG.debug("Printing the Purchases Report");
		String text = null;
		boolean hasTotal = BookOptions.isTotalOptionSet();
		LOG.debug("isTotalOptionSet = " + hasTotal);
		boolean descending = BookOptions.isDescendingOptionSet();
		LOG.debug("isDescendingOptionSet = " + descending);

		out.println("\nPurchases Report");

		out.println(HORIZONTAL_LINE);
		text = String.format(HEADER_FORMAT, "Name", "Title", "Price");
		out.println(text);
		out.println(HORIZONTAL_LINE);

		Collection<Purchase> purchases = AllData.getPurchases().values();
		Map<Long, Book> books = AllData.getBooks();
		Map<Long, Customer> customers = AllData.getCustomers();

		String customerIdString = BookOptions.getCustomerId();
		long optionsCustomerId = customerIdString == null ? -1L : Long.parseLong(customerIdString);
		items = new ArrayList<>();
		for (Purchase purchase : purchases) {
			long customerId = purchase.getCustomerId();
			if (customerIdString != null && customerId != optionsCustomerId) {
				continue;
			}

			long bookId = purchase.getBookId();
			Book book = books.get(bookId);
			Customer customer = customers.get(customerId);
			float price = purchase.getPrice();
			Item item = new Item(customer.getFirstName(), customer.getLastName(), book.getTitle(), price);
			items.add(item);
		}

		if (BookOptions.isByLastnameOptionSet()) {
			LOG.debug("isByLastnameOptionSet = true");
			if (descending) {
				Collections.sort(items, new CompareByLastNameDescending());
			} else {
				Collections.sort(items, new CompareByLastName());
			}
		}

		if (BookOptions.isByTitleOptionSet()) {
			LOG.debug("isByTitleOptionSet = true");
			if (descending) {
				Collections.sort(items, new CompareByTitleDescending());
			} else {
				Collections.sort(items, new CompareByTitle());
			}
		}

		float total = 0;
		for (Item item : items) {
			if (hasTotal) {
				total += item.price;
			}

			text = String.format(ROW_FORMAT, item.firstName + " " + item.lastName, item.title, item.price);
			out.println(text);
			LOG.trace(text);
		}

		if (hasTotal) {
			text = String.format("%nValue of purchases: $%,.2f%n", total);
			out.println(text);
			LOG.trace(text);
		}

	}

	public static List<Item> filters(List<Purchase> purchases) {
		try {
			items = new ArrayList<>();
			for (Purchase purchase : purchases) {
				long customerId = purchase.getCustomerId();
				long bookId = purchase.getBookId();
				Book book = BookDao.getTheInstance().getBook(bookId);
				Customer customer = CustomerDao.getTheInstance().getCustomer(customerId);
				float price = purchase.getPrice();
				Item item = new Item(customer.getFirstName(), customer.getLastName(), book.getTitle(), price);
				items.add(item);
			}
		} catch (Exception e1) {
			LOG.error("ERROR: ", e1);
		}

		if (Filters.isByLastName()) {
			if (Filters.isDescending()) {
				Collections.sort(items, new CompareByLastNameDescending());
			} else {
				Collections.sort(items, new CompareByLastName());
			}
		}

		if (Filters.isByTitle()) {
			if (Filters.isDescending()) {
				Collections.sort(items, new CompareByTitleDescending());
			} else {
				Collections.sort(items, new CompareByTitle());
			}
		}
		return items;
	}

	public static class CompareByLastName implements Comparator<Item> {
		@Override
		public int compare(Item item1, Item item2) {
			return item1.lastName.compareTo(item2.lastName);
		}
	}

	public static class CompareByLastNameDescending implements Comparator<Item> {
		@Override
		public int compare(Item item1, Item item2) {
			return item2.lastName.compareTo(item1.lastName);
		}
	}

	public static class CompareByTitle implements Comparator<Item> {
		@Override
		public int compare(Item item1, Item item2) {
			return item1.title.compareToIgnoreCase(item2.title);
		}
	}

	public static class CompareByTitleDescending implements Comparator<Item> {
		@Override
		public int compare(Item item1, Item item2) {
			return item2.title.compareToIgnoreCase(item1.title);
		}
	}

	public static class Item {
		private String firstName;
		private String lastName;
		private String title;
		private float price;

		/**
		 * @param firstName
		 * @param lastName
		 * @param title
		 * @param price
		 */
		public Item(String firstName, String lastName, String title, float price) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.title = Common.truncateIfRequired(title, 80);
			this.price = price;
		}

		/**
		 * @return the firstName
		 */
		public String getFirstName() {
			return firstName;
		}

		/**
		 * @return the lastName
		 */
		public String getLastName() {
			return lastName;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * @return the price
		 */
		public float getPrice() {
			return price;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Item [firstName=" + firstName + ", lastName=" + lastName + ", title=" + title + ", price=" + price + "]";
		}

	}

}
