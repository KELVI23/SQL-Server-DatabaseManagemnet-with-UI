/**
 * Project: A01085867_Assignment2_2021
 * File: CustomerListDialog.java
 * Date: Jun. 27, 2021
 * Time: 4:02:03 p.m.
 */
package a01085867.book.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.data.Customer;
import a01085867.book.data.util.Common;
import a01085867.book.db.CustomerDao;
import a01085867.book.sorters.CustomerSorter;
import a01085867.book.sorters.Filters;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CustomerListDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private static boolean refresh;
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Create the dialog.
	 */
	public CustomerListDialog(List<Customer> customers) {
		setBounds(100, 100, 800, 300);
		getContentPane().setLayout(new BorderLayout());
		setResizable(false);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		Object columnNames[] = { "ID", "First name", "Last name", "Street", "City", "Postal Code", "Phone", "Email", "Join Date", "Lenght" };

		// Constructs the JTable with the data retrieve from database
		Object rowData[][] = new Object[customers.size()][11];
		int i = 0;
		for (Customer customer : customers) {
			LocalDate date = customer.getJoinedDate();
			rowData[i][0] = customer.getId();
			rowData[i][1] = customer.getFirstName();
			rowData[i][2] = customer.getLastName();
			rowData[i][3] = Common.truncateIfRequired(customer.getStreet(), 40);
			rowData[i][4] = Common.truncateIfRequired(customer.getCity(), 25);
			rowData[i][5] = Common.truncateIfRequired(customer.getPostalCode(), 12);
			rowData[i][6] = customer.getPhone();
			rowData[i][7] = Common.truncateIfRequired(customer.getEmailAddress(), 40);
			rowData[i][8] = Common.DATE_FORMAT.format(date);
			rowData[i][9] = ChronoUnit.YEARS.between(date, LocalDate.now());
			++i;
		}

		final JTable table = new JTable(rowData, columnNames);
		int width = this.getWidth();
		int height = this.getHeight();
		contentPanel.setLayout(new MigLayout("", "[772px]", "[][297px]"));
		{
			JLabel lblListOfCustomers = new JLabel("List of Customers");
			lblListOfCustomers.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
			contentPanel.add(lblListOfCustomers, "cell 0 0");
		}

		table.setPreferredScrollableViewportSize(new Dimension(width - 30, height - 30));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultEditor(Object.class, null);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane scrollPane = new JScrollPane(table);

		contentPanel.add(scrollPane, "cell 0 1,alignx left,aligny top");

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {

				if (!event.getValueIsAdjusting()) {
					try {

						CustomerDialog dialog = new CustomerDialog();
						// CustomerDao.getTheInstance().getCustomer(Long.toString((long) table.getValueAt(table.getSelectedRow(), 0))));
						dialog.setCustomer(CustomerDao.getTheInstance().getCustomer((long) table.getValueAt(table.getSelectedRow(), 0)));
						dialog.setModalityType(ModalityType.APPLICATION_MODAL);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
						if (refresh) {
							CustomerListDialog.this.dispose();
							List<Long> ids;
							ids = CustomerDao.getTheInstance().getCustomerIds();
							List<Customer> customersList = new ArrayList<>();
							for (Long id : ids) {
								customersList.add(CustomerDao.getTheInstance().getCustomer(id));
							}
							customers.clear();
							customers.addAll(customersList);
							customers.sort(new CustomerSorter.CompareByCustomerID());
							if (Filters.isByJoinDate()) {
								customers.sort(new CustomerSorter.CompareByJoinedDate());
							}
							callDialog(customers);
						}

					} catch (Exception ex) {
						LOG.error("ERROR -", ex);
						ex.printStackTrace();
					}
				}

			}
		});
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						CustomerListDialog.this.dispose();

					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						CustomerListDialog.this.dispose();

					}
				});
			}
		}
	}

	/**
	 * @param customers
	 */
	public static void callDialog(List<Customer> customers) {
		try {
			CustomerListDialog dialog = new CustomerListDialog(customers);
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception ex) {
			LOG.error("ERROR -", ex);
		}
	}

	/**
	 * @return the refresh
	 */
	public static boolean isRefresh() {
		return refresh;
	}

	/**
	 * @param refresh
	 *            the refresh to set
	 */
	public static void setRefresh(boolean refresh) {
		CustomerListDialog.refresh = refresh;
	}

}
