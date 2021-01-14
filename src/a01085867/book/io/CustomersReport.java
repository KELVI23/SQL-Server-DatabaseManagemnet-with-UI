/**
 * Project: Books
 * File: CustomersReport.java
 */

package a01085867.book.io;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.BookOptions;
import a01085867.book.data.AllData;
import a01085867.book.data.Customer;
import a01085867.book.sorters.CustomerSorter;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class CustomersReport {

	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");
	public static final String REPORT_FILENAME = "customers_report.txt";

	public static final String HORIZONTAL_LINE = "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	public static final String HEADER_FORMAT = "%4s. %-6s %-12s %-12s %-40s %-25s %-12s %-15s %-40s %-12s %7s";
	public static final String ROW_FORMAT = "%4d. %06d %-12s %-12s %-40s %-25s %-12s %-15s %-40s %-12s %7d";

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Print the report.
	 *
	 * @param out
	 */
	public static void print(PrintStream out) {
		LOG.debug("Printing the Customers Report");
		String text = null;
		out.println("\nCustomers Report");
		out.println(HORIZONTAL_LINE);
		text = String.format(HEADER_FORMAT, "#", "ID", "First name", "Last name", "Street", "City", "Postal Code", "Phone", "Email", "Join Date",
				"Length");
		out.println(text);
		out.println(HORIZONTAL_LINE);

		Collection<Customer> customers = AllData.getCustomers().values();

		if (BookOptions.isByJoinDateOptionSet()) {
			LOG.debug("isByJoinDateOptionSet = true");
			List<Customer> list = new ArrayList<>(customers);
			if (BookOptions.isDescendingOptionSet()) {
				LOG.debug("isDescendingOptionSet = true");
				Collections.sort(list, new CustomerSorter.CompareByJoinedDateDescending());
			} else {
				LOG.debug("isDescendingOptionSet = false");
				Collections.sort(list, new CustomerSorter.CompareByJoinedDate());
			}

			customers = list;
		}

		int i = 0;
		for (Customer customer : customers) {
			LocalDate date = customer.getJoinedDate();
			text = String.format(ROW_FORMAT, ++i, customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getStreet(),
					customer.getCity(), customer.getPostalCode(), customer.getPhone(), customer.getEmailAddress(), DATE_FORMAT.format(date),
					calculateJoinDuration(date));
			out.println(text);
		}
	}

	/**
	 * Calculate how long this person has been a customer
	 *
	 * @param date
	 *            the join date
	 * @return the age
	 */
	private static long calculateJoinDuration(LocalDate date) {
		return ChronoUnit.YEARS.between(date, LocalDate.now());
	}

}
