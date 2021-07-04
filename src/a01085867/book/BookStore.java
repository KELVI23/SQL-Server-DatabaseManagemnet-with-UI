package a01085867.book;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import a01085867.book.data.AllData;
import a01085867.book.db.BookDao;
import a01085867.book.db.CustomerDao;
import a01085867.book.db.Database;
import a01085867.book.db.PurchasesDao;
import a01085867.book.ui.MainFrame;

/**
 * Project: Books
 * File: BookStore.java
 */

/**
 * @author Sam Cirka, A00123456
 *
 */
public class BookStore {

	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	static {
		configureLogging();
	}
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Book Constructor. Processes the commandline arguments
	 * ex. -inventory -make=honda -by_count -desc -total -service
	 * 
	 * @throws ApplicationException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public BookStore() throws ApplicationException, FileNotFoundException, IOException, SQLException {
		// LOG.debug("Input args: " + Arrays.toString(args));
		// BookOptions.process(args);
		Database.getTheInstance().initiate();

		CustomerDao.getTheInstance().load();
		BookDao.getTheInstance().load();
		PurchasesDao.getTheInstance().load();
	}

	/**
	 * Entry point to GIS
	 * 
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		LOG.info("Starting Books");
		Instant startTime = Instant.now();
		LOG.info(startTime);

		// start the Bookstore System
		try {
			BookStore bookStore = new BookStore();
			bookStore.run();
			// if (BookOptions.isHelpOptionSet()) {
			// BookOptions.Value[] values = BookOptions.Value.values();
			// System.out.format("%-5s %-15s %-10s %s%n", "Option", "Long Option", "Has Value", "Description");
			// for (BookOptions.Value value : values) {
			// System.out.format("-%-5s %-15s %-10s %s%n", value.getOption(), ("-" + value.getLongOption()), value.isHasArg(),
			// value.getDescription());
			// }
			//
			// System.out.println("\nex. -inventory -make=honda -by_count -desc -total -service");

		} catch (ApplicationException | IOException | SQLException e) {
			// e.printStackTrace();
			LOG.debug(e.getMessage());
		}

		Instant endTime = Instant.now();
		LOG.info(endTime);
		LOG.info(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
		LOG.info("Books has stopped");
	}

	/**
	 * @return the startTime
	 */
	public static Instant getStartTime() {
		Instant startTime = Instant.now();
		return startTime;
	}

	/**
	 * Configures log4j2 from the external configuration file specified in LOG4J_CONFIG_FILENAME.
	 * If the configuration file isn't found then log4j2's DefaultConfiguration is used.
	 */
	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			System.out.println(String.format("WARNING! Can't find the log4j logging configuration file %s; using DefaultConfiguration for logging.",
					LOG4J_CONFIG_FILENAME));
			Configurator.initialize(new DefaultConfiguration());
		}
	}

	/**
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 * 
	 */
	private void run() throws ApplicationException, FileNotFoundException {
		LOG.debug("run()");
		AllData.loadData();
		CustomerDao customerDao = CustomerDao.getTheInstance();
		BookDao bookDao = BookDao.getTheInstance();
		PurchasesDao purchaseDao = PurchasesDao.getTheInstance();
		createUI(customerDao, bookDao, purchaseDao);
		// generateReports();
	}

	/**
	 * Launch the application.
	 */
	public static void createUI(final CustomerDao customerDao, final BookDao bookDao, final PurchasesDao purchasesDao) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame frame = new MainFrame(customerDao, bookDao, purchasesDao);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	// /**
	// * Generate the reports from the input data
	// *
	// * @throws FileNotFoundException
	// */
	// private void generateReports() throws FileNotFoundException {
	// LOG.debug("generating the reports");
	//
	// PrintStream out = null;
	//
	// if (BookOptions.isCustomersOptionSet()) {
	// LOG.debug("generating the customer report");
	// CustomersReport.print(System.out);
	// out = getOutputStream(CustomersReport.REPORT_FILENAME);
	// CustomersReport.print(out);
	// out.close();
	// }
	//
	// if (BookOptions.isBooksOptionSet()) {
	// LOG.debug("generating the book report");
	// BooksReport.print(System.out);
	// out = getOutputStream(BooksReport.REPORT_FILENAME);
	// BooksReport.print(out);
	// out.close();
	// }
	//
	// if (BookOptions.isPurchasesOptionSet()) {
	// LOG.debug("generating the purchase report");
	// PurchasesReport.print(System.out);
	// out = getOutputStream(PurchasesReport.REPORT_FILENAME);
	// PurchasesReport.print(out);
	// out.close();
	// }
	//
	// }
	//
	// /**
	// * @param reportFilename
	// * @return
	// * @throws ApplicationException
	// * @throws FileNotFoundException
	// */
	// private PrintStream getOutputStream(String reportFilename) throws FileNotFoundException {
	// PrintStream out = null;
	// try {
	// out = new PrintStream(new File(reportFilename));
	// } catch (FileNotFoundException e) {
	// LOG.error(e.getMessage());
	// throw e;
	// }
	//
	// return out;
	// }

}
