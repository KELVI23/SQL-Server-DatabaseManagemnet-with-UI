package a01085867.book.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
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
import a01085867.book.io.PurchasesReport.CompareByLastNameDescending;
import a01085867.book.io.PurchasesReport.Item;
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	Collection<Purchase> collection = new ArrayList();
	private static List<Item> items;

	/**
	 * Launch the application.
	 */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// MainFrame frame = new MainFrame();
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public MainFrame(final CustomerDao customer, final BookDao book, final PurchasesDao purchase) throws Exception {

		LOG.debug("MainFrame created");
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
			LOG.error("Error: ", e.getMessage());
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
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[]", "[]"));
		buildMenu();
	}

	@SuppressWarnings("deprecation")
	private void buildMenu() {
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		JMenu booksMenu = new JMenu("Books");
		booksMenu.setMnemonic('B');
		JMenu customersMenu = new JMenu("Customers");
		customersMenu.setMnemonic('C');
		JMenu purchasesMenu = new JMenu("Purchases");
		purchasesMenu.setMnemonic('P');
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
						LOG.error("ERROR: ", e1.getMessage());
					} finally {
						exit();
					}
				}
			}
		});
		fileMenu.add(drop);

		JSeparator separator = new JSeparator();
		fileMenu.add(separator);

		JMenuItem exit = new JMenuItem("Quit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("Goodbye!");
				System.exit(0);

			}
		});
		fileMenu.add(exit);

		JCheckBoxMenuItem byAuthorCheckBox = new JCheckBoxMenuItem("By Author");
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

		booksMenu.add(byAuthorCheckBox);
		byAuthorCheckBox.setToolTipText("sort by author");
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

		JCheckBoxMenuItem descendingBooksCheckBox = new JCheckBoxMenuItem("Descending");
		booksMenu.add(descendingBooksCheckBox);
		descendingBooksCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (descendingBooksCheckBox.isSelected()) {
					descendingBooksCheckBox.setEnabled(true);
				} else {
					descendingBooksCheckBox.setEnabled(false);
					descendingBooksCheckBox.setSelected(false);
					Filters.setDescending(false);
				}

			}
		});

		JSeparator separate = new JSeparator();
		booksMenu.add(separate);

		JMenuItem countBooks = new JMenuItem("Count");
		booksMenu.add(countBooks);
		countBooks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(MainFrame.this, "Total of books: " + bookDao.countAllBooks(), "Count Books",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					LOG.error("ERROR: ", e1.getMessage());
				}
			}
		});

		JMenuItem listBooks = new JMenuItem("List");
		booksMenu.add(listBooks);
		listBooks.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (byAuthorCheckBox.isSelected()) {
					Filters.setByAuthor(true);
					if (descendingBooksCheckBox.isSelected()) {
						Filters.setDescending(true);
						books.sort(new BookSorter.CompareByAuthorDescending());
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

		// Customers

		JCheckBoxMenuItem byJoinDate = new JCheckBoxMenuItem("By Join Date");
		customersMenu.add(byJoinDate);
		byJoinDate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (byJoinDate.isSelected()) {
					byJoinDate.setEnabled(true);
				} else {
					byJoinDate.setEnabled(false);
					byJoinDate.setSelected(false);
					Filters.setByJoinDate(false);
				}

			}
		});

		JSeparator separator2 = new JSeparator();
		customersMenu.add(separator2);

		JMenuItem countCustomers = new JMenuItem("Count");
		customersMenu.add(countCustomers);
		countCustomers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(MainFrame.this, "Total of customers: " + customerDao.countAllCustomers(), "Count Customers",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					LOG.error("ERROR: ", e1.getMessage());
				}

			}
		});

		JMenuItem listCustomers = new JMenuItem("List");
		customersMenu.add(listCustomers);
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

		// purchases
		JCheckBoxMenuItem descendingPurhases = new JCheckBoxMenuItem("Descending");
		purchasesMenu.add(descendingPurhases);
		JCheckBoxMenuItem byTitle = new JCheckBoxMenuItem("By Title");
		JCheckBoxMenuItem byLastName = new JCheckBoxMenuItem("By Last Name");
		purchasesMenu.add(byLastName);
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

		purchasesMenu.add(byTitle);
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

		JSeparator separator3 = new JSeparator();
		purchasesMenu.add(separator3);

		JMenuItem listPurchases = new JMenuItem("List");
		purchasesMenu.add(listPurchases);
		listPurchases.addActionListener(new ActionListener() {
			//
			// if (byJoinDate.isSelected()) {
			// Filters.setByJoinDate(true);
			// customers.sort(new CustomerSorter.CompareByJoinedDate());
			// } else {
			// Filters.setByJoinDate(false);
			// customers.sort(new CustomerSorter.CompareByCustomerID());
			// }
			// CustomerListDialog.callDialog(customers);
			// }
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				if (byLastName.isSelected()) {
					Filters.setByLastName(true);
					// Collections.sort(items, new CompareByLastNameDescending());
					// } else {
					// Collections.sort(items, new CompareByLastName());

					Collections.sort(items, new CompareByLastNameDescending());
				} else {
					Filters.setByJoinDate(false);

					// Collections.sort(items,new CompareByLastName());

				}
				PurchaseDialog.callDialog(items);

			}
		});

		JMenuItem total = new JMenuItem("Total");
		purchasesMenu.add(total);
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
		purchasesMenu.add(filterByID);
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

		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, "Assignmnt 2\nBy Kelvin Musodza A01085867", "About Assignment 2",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		helpMenu.add(about);

		LOG.info("Menu created");

	}

	private void exit() {
		Instant endTime = Instant.now();
		LOG.info("End: " + endTime);
		LOG.info(String.format("Duration: %d ms", Duration.between(BookStore.getStartTime(), endTime).toMillis()));
		System.exit(0);

	}
}
