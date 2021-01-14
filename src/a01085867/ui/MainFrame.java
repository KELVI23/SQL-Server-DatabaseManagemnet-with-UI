package a01085867.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.BookStore;
import a01085867.book.data.Book;
import a01085867.book.data.Customer;
import a01085867.book.data.Purchase;
import a01085867.book.db.BookDao;
import a01085867.book.db.CustomerDao;
import a01085867.book.db.PurchasesDao;
import a01085867.book.io.PurchasesReport;
import a01085867.book.sorters.BookSorter;
import a01085867.book.sorters.CustomerSorter;
import a01085867.book.sorters.Filters;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private static Logger LOG = LogManager.getLogger(MainFrame.class);
	private CustomerDao customerDao;
	private BookDao bookDao;
	private PurchasesDao purchasesDao;
	private static List<Customer> customers;
	private static List<Book> books;
	private static List<Purchase> purchases;

	/**
	 * Create the frame.
	 */
	public MainFrame(final CustomerDao customer, final BookDao book, final PurchasesDao purchase) {

		LOG.debug("MainFram created");
		customerDao = customer;
		bookDao = book;
		purchasesDao = purchase;

		try {
			List<Long> customerIds;
			customerIds = customerDao.getCustomerIds();
			customers = new ArrayList<>();
			for (Long id : customerIds) {
				customers.add(customerDao.getCustomer(id));
			}

			List<Long> bookIds;
			bookIds = bookDao.getBookIds();
			books = new ArrayList<>();
			for (Long id : bookIds) {
				books.add(bookDao.getBook(id));
			}

			List<Long> purchaseIds;
			purchaseIds = purchasesDao.getPurchaseId();
			purchases = new ArrayList<>();
			for (Long id : purchaseIds) {
				purchases.add(purchasesDao.getPurchases(id));
			}
		} catch (Exception e) {
			LOG.error("Error: ", e);
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("Assignment 2");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[]", "[]"));

		buildMenu();

	}

	public void buildMenu() {
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		JMenu fileMenu = new JMenu("File");
		JMenu booksMenu = new JMenu("Books");
		JMenu customersMenu = new JMenu("Customers");
		JMenu purchasesMenu = new JMenu("Purchases");
		JMenu helpMenu = new JMenu("Help");

		menu.add(fileMenu);
		menu.add(booksMenu);
		menu.add(customersMenu);
		menu.add(purchasesMenu);
		menu.add(helpMenu);

		JMenuItem drop = new JMenuItem("Drop");
		drop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int buttonPressed = JOptionPane.showConfirmDialog(MainFrame.this, "Do you want to delete all BookStore data?", "Delete Data",
						JOptionPane.YES_NO_OPTION);
				if (buttonPressed == JOptionPane.YES_OPTION) {
					try {
						LOG.debug("Dropping Purchase table");
						purchasesDao.drop();
						LOG.debug("Dropping Customer table");
						customerDao.drop();
						LOG.debug("Dropping Book table");
						bookDao.drop();
						LOG.debug("All tables have been deleted successfully");
						JOptionPane.showMessageDialog(MainFrame.this, "All tables have been deleted successfully");
					} catch (SQLException e1) {
						LOG.error("ERROR: ", e1);
					} finally {
						exit();
					}
				}
			}
		});
		fileMenu.add(drop);

		JSeparator separator = new JSeparator();
		fileMenu.add(separator);

		JMenuItem countBooks = new JMenuItem("Count");
		countBooks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(MainFrame.this, "Total of books: " + bookDao.countAllBooks(), "Count Books",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					LOG.error("ERROR: ", e1);
				}
			}
		});

		JCheckBox byAuthorCheckBox = new JCheckBox("By Author");
		byAuthorCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (byAuthorCheckBox.isSelected()) {
					byAuthorCheckBox.setEnabled(true);
				} else {
					byAuthorCheckBox.setEnabled(false);
					byAuthorCheckBox.setSelected(false);
					Filters.setDescending(false);
				}
			}
		});

		JMenuItem descendingBooks = new JMenuItem("Descending");
		descendingBooks.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		JMenuItem listBooks = new JMenuItem("List");
		listBooks.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (byAuthorCheckBox.isSelected()) {
					Filters.setByAuthor(true);
					if (descendingBooks.isSelected()) {
						Filters.setDescending(true);
					} else {
						Filters.setDescending(false);
					}
					// Collections.sort(customers, new CustomerSorters.CompareByJoinedDate());
					books.sort(new BookSorter.CompareByAuthor());
				} else {
					Filters.setByAuthor(false);
					Filters.setDescending(false);
					books.sort(new BookSorter.CompareBookbyID());
				}

				BookDialog.callDialog(books);

			}
		});
		booksMenu.add(countBooks);

		booksMenu.add(byAuthorCheckBox);
		byAuthorCheckBox.setToolTipText("sort by author");
		booksMenu.add(descendingBooks);
		booksMenu.add(listBooks);

		JMenuItem countCustomers = new JMenuItem("Count");
		countCustomers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(MainFrame.this, "Total of customers: " + customerDao.countAllCustomers(), "Count Customers",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					LOG.error("ERROR: ", e1);
				}

			}
		});
		JCheckBox byJoinDate = new JCheckBox("By Join Date");
		byJoinDate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		JMenuItem listCustomers = new JMenuItem("List");
		listCustomers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (byJoinDate.isSelected()) {
					Filters.setByJoinDate(true);
					customers.sort(new CustomerSorter.CompareByJoinedDate());
				} else {
					Filters.setByJoinDate(false);
					customers.sort(new CustomerSorter.CompareByCustomerID());
				}
				CustomerListDialog.callDialog(customers);
			}
		});
		customersMenu.add(countCustomers);
		customersMenu.add(byJoinDate);
		customersMenu.add(listCustomers);

		JMenuItem total = new JMenuItem("Total");
		total.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				float total = 0.00f;
				if (purchases != null && !purchases.isEmpty()) {
					for (Purchase purchase : purchases) {
						total += purchase.getPrice();
					}
				}
				JOptionPane.showMessageDialog(MainFrame.this, String.format("Total Purchases: $%,.2f", total), "Total ",
						JOptionPane.INFORMATION_MESSAGE);

			}
		});

		JMenuItem filterByID = new JMenuItem("Filter by Customer ID");
		JCheckBox descendingPurhases = new JCheckBox("Descending");
		JCheckBox byTitle = new JCheckBox("By Title");
		JCheckBox byLastName = new JCheckBox("By Last Name");

		byTitle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (byTitle.isSelected()) {
					byLastName.setSelected(false);
					descendingPurhases.setEnabled(true);
				} else {
					descendingPurhases.setEnabled(false);
					descendingPurhases.setSelected(false);
					Filters.setDescending(false);
				}

			}
		});

		descendingBooks.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		filterByID.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (byLastName.isSelected()) {
					Filters.setByLastName(true);
					if (descendingPurhases.isSelected()) {
						Filters.setDescending(true);
					} else {
						Filters.setDescending(false);
					}
				} else {
					Filters.setByLastName(false);
				}

				if (byTitle.isSelected()) {
					Filters.setByTitle(true);
					if (descendingPurhases.isSelected()) {
						Filters.setDescending(true);
					} else {
						Filters.setDescending(false);
					}
				} else {
					Filters.setByTitle(false);
				}
				List<Long> purchaseIds;
				try {
					purchaseIds = purchasesDao.getPurchaseId();
					purchases = new ArrayList<>();
					for (Long id : purchaseIds) {
						purchases.add(purchasesDao.getPurchases(id));
					}
				} catch (Exception e2) {
					LOG.error("ERROR: ", e2);
				}

				String answer = JOptionPane.showInputDialog(MainFrame.this, "Customer ID:", "Filter by Customer ID", JOptionPane.INFORMATION_MESSAGE);
				long filterId = 0;
				if (answer != null && answer.length() > 0) {
					try {
						filterId = Long.parseLong(answer);
					} catch (Exception e1) {
						LOG.error("ERROR: ", e1);
					}
					int count = 0;
					List<Purchase> purchaseFilter = new ArrayList<>();
					for (Purchase purchase : purchases) {
						if (purchase.getCustomerId() == filterId) {
							try {
								purchaseFilter.add(purchase);
							} catch (Exception e1) {
								LOG.error("ERROR: ", e1);
							}
							count++;
						}
					}
					System.out.println("The count is: " + count);
					if (count > 0) {
						MainFrame.purchases = purchaseFilter;
					}

					PurchaseDialog.callDialog(PurchasesReport.filters(purchases));
				}
			}
		});

		byLastName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (byLastName.isSelected()) {
					byTitle.setSelected(false);
					descendingPurhases.setEnabled(true);
				} else {
					descendingPurhases.setEnabled(false);
					descendingPurhases.setSelected(false);
					Filters.setDescending(false);
				}

			}
		});

		JMenuItem listPurchases = new JMenuItem("List");
		listPurchases.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		purchasesMenu.add(total);
		purchasesMenu.add(byLastName);
		purchasesMenu.add(byTitle);
		purchasesMenu.add(descendingPurhases);
		purchasesMenu.add(filterByID);
		purchasesMenu.add(listPurchases);

		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, "Assignmnt 2\nBy Kelvin Musodza A01085867", "About Assignment 2",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(about);
		about.setAccelerator(KeyStroke.getKeyStroke("F1"));
		LOG.info("Menu created");

	}

	private void exit() {
		Instant endTime = Instant.now();
		LOG.info("End: " + endTime);
		LOG.info(String.format("Duration: %d ms", Duration.between(BookStore.getStartTime(), endTime).toMillis()));
		System.exit(0);
	}

}
