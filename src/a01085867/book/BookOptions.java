/**
 * Project: Books
 * File: BookReader.java
 */
package a01085867.book;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Extract the program options from the commandline arguments and store them for safe keeping.
 *
 * @author scirka
 *
 */
public class BookOptions {
	private static CommandLine commandLine;
	private static boolean customers;
	private static boolean books;
	private static boolean purchases;

	/**
	 * customers Print the customer report
	 * books Print the books report
	 * purchases Print the purchases report
	 * total When combined with the 'purchases' option, also print the total value of the purchases
	 * by_lastname Sorts the purchases report by part description in ascending order. This option is ignored if ‘purchases’ isn’t also specified
	 * by_title Sorts the purchases report by book title in ascending order. This option is ignored if ‘purchases’ isn’t also specified
	 * by_join_date Sorts the customer report by join date in ascending order. This option is ignored if ‘customers’ isn’t also specified
	 * desc Any sorted report is sorted in descending order. Must be combined with 'by_lastname', 'by_title', or 'by_join_date'
	 * customer_id=<id> Filters the purchases report, showing only customers that match the customer id
	 */

	private BookOptions() {
	}

	/**
	 * Process the commandline arguments and set the program options.
	 * 
	 * @param args
	 *            Commandline arguments.
	 * @throws ApplicationException
	 * @throws ParseException
	 */
	public static void process(String[] args) throws ApplicationException {
		try {
			commandLine = new DefaultParser().parse(createOptions(), args);
		} catch (ParseException e) {
			throw new ApplicationException(e);
		}

		if (args.length == 0) {
			customers = true;
			books = true;
			purchases = true;
		} else {
			customers = commandLine.hasOption(Value.CUSTOMERS.getOption());
			books = commandLine.hasOption(Value.BOOKS.getOption());
			purchases = commandLine.hasOption(Value.PURCHASES.getOption());
		}
	}

	/**
	 * @return the help option setting
	 */
	public static boolean isHelpOptionSet() {
		return commandLine.hasOption(Value.HELP.getOption());
	}

	/**
	 * @return the customers option setting
	 */
	public static boolean isCustomersOptionSet() {
		return customers;
	}

	/**
	 * @return the books option setting
	 */
	public static boolean isBooksOptionSet() {
		return books;
	}

	/**
	 * @return the purchases option setting
	 */
	public static boolean isPurchasesOptionSet() {
		return purchases;
	}

	/**
	 * @return the total option setting
	 */
	public static boolean isTotalOptionSet() {
		return commandLine.hasOption(Value.TOTAL.getOption());
	}

	/**
	 * @return the byLastname option setting
	 */
	public static boolean isByLastnameOptionSet() {
		return commandLine.hasOption(Value.BY_LASTNAME.getOption());
	}

	/**
	 * @return the byTitle option setting
	 */
	public static boolean isByTitleOptionSet() {
		return commandLine.hasOption(Value.BY_TITLE.getOption());
	}

	/**
	 * @return the byJoinDate option setting
	 */
	public static boolean isByJoinDateOptionSet() {
		return commandLine.hasOption(Value.BY_JOIN_DATE.getOption());
	}

	/**
	 * @return the byJoinDate option setting
	 */
	public static boolean isByAuthorOptionSet() {
		return commandLine.hasOption(Value.BY_AUTHOR.getOption());
	}

	/**
	 * @return the descending option setting
	 */
	public static boolean isDescendingOptionSet() {
		return commandLine.hasOption(Value.DESCENDING.getOption());
	}

	/**
	 * @return the customer_id option setting
	 */
	public static String getCustomerId() {
		return commandLine.getOptionValue(Value.CUSTOMER_ID.getOption());
	}

	private static Options createOptions() {
		Options options = new Options();

		for (Value value : Value.values()) {
			Option option = null;

			if (value.hasArg) {
				option = Option.builder(value.option).longOpt(value.longOption).hasArg().desc(value.description).build();
			} else {
				option = Option.builder(value.option).longOpt(value.longOption).desc(value.description).build();
			}

			options.addOption(option);
		}

		return options;
	}

	public enum Value {
		HELP("?", "help", false, "Display help"), //
		CUSTOMERS("c", "customers", false, "Print the customer report"), //
		BOOKS("b", "books", false, "Print the books report"), //
		PURCHASES("p", "purchases", false, "Print the purchases report"), //
		TOTAL("t", "total", false, "When combined with the 'purchases' option, also print the total value of the purchases"), //
		BY_AUTHOR("A", "by_author", false,
				"Sorts the books report by author in ascending order. This option is ignored if ‘books’ isn’t also specified"), //
		BY_LASTNAME("L", "by_lastname", false,
				"Sorts the purchases report by customer lastname in ascending order. This option is ignored if ‘purchases’ isn’t also specified"), //
		BY_TITLE("T", "by_title", false,
				"Sorts the purchases report by book title in ascending order. This option is ignored if ‘purchases’ isn’t also specified"), //
		BY_JOIN_DATE("J", "by_join_date", false,
				"Sorts the customers report by join date in ascending order. This option is ignored if ‘customers’ isn’t also specified"), //
		CUSTOMER_ID("C", "customer_id", true, "Filters the purchases report, showing only customers that match the customer id"), //
		DESCENDING("d", "descending", false,
				"Any sorted report is sorted in descending order. Must be combined with 'by_lastname', 'by_title', or 'by_join_date'");

		private final String option;
		private final String longOption;
		private final boolean hasArg;
		private final String description;

		Value(String option, String longOption, boolean hasArg, String description) {
			this.option = option;
			this.longOption = longOption;
			this.hasArg = hasArg;
			this.description = description;
		}

		/**
		 * @return the option
		 */
		public String getOption() {
			return option;
		}

		/**
		 * @return the longOption
		 */
		public String getLongOption() {
			return longOption;
		}

		/**
		 * @return the hasArg
		 */
		public boolean isHasArg() {
			return hasArg;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
	}

}
