/**
 * Project: A01085867_Assignment2_2021
 * File: CustomerDialog.java
 * Date: Jun. 27, 2021
 * Time: 4:07:34 p.m.
 */
package a01085867.book.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.ApplicationException;
import a01085867.book.data.Customer;
import a01085867.book.data.util.Validator;
import a01085867.book.db.CustomerDao;
import net.miginfocom.swing.MigLayout;

/**
 * @author Kelvin Musodza, A01085867
 *
 */
@SuppressWarnings("serial")
public class CustomerDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger();

	private final JPanel contentPanel = new JPanel();
	private JTextField idField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField streetField;
	private JTextField cityField;
	private JTextField postalCodeField;
	private JTextField phoneField;
	private JTextField emailField;
	private JTextField joinedDateField;

	/**
	 * Create the dialog.
	 */
	public CustomerDialog(/* JFrame frame */) {
		// super(frame, true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(130, 130, 450, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][]"));

		JLabel lblId = new JLabel("ID");
		contentPanel.add(lblId, "cell 0 0,alignx trailing");

		idField = new JTextField();
		idField.setEnabled(false);
		idField.setEditable(false);
		contentPanel.add(idField, "cell 1 0,growx");
		idField.setColumns(10);

		JLabel lblFirstName = new JLabel("First Name");
		contentPanel.add(lblFirstName, "cell 0 1,alignx trailing");

		firstNameField = new JTextField();
		contentPanel.add(firstNameField, "cell 1 1,growx");
		firstNameField.setColumns(10);

		JLabel lblLastName = new JLabel("Last Name");
		contentPanel.add(lblLastName, "cell 0 2,alignx trailing");

		lastNameField = new JTextField();
		contentPanel.add(lastNameField, "cell 1 2,growx");
		lastNameField.setColumns(10);

		JLabel lblStreet = new JLabel("Street");
		contentPanel.add(lblStreet, "cell 0 3,alignx trailing");

		streetField = new JTextField();
		contentPanel.add(streetField, "cell 1 3,growx");
		streetField.setColumns(10);

		JLabel lblCity = new JLabel("City");
		contentPanel.add(lblCity, "cell 0 4,alignx trailing");

		cityField = new JTextField();
		contentPanel.add(cityField, "cell 1 4,growx");
		cityField.setColumns(10);

		JLabel lblPostalCode = new JLabel("Postal Code");
		contentPanel.add(lblPostalCode, "cell 0 5,alignx trailing");

		postalCodeField = new JTextField();
		contentPanel.add(postalCodeField, "cell 1 5,growx");
		postalCodeField.setColumns(10);

		JLabel lblPhone = new JLabel("Phone");
		contentPanel.add(lblPhone, "cell 0 6,alignx trailing");

		phoneField = new JTextField();
		contentPanel.add(phoneField, "cell 1 6,growx");
		phoneField.setColumns(10);

		JLabel lblEmail = new JLabel("Email");
		contentPanel.add(lblEmail, "cell 0 7,alignx trailing");

		emailField = new JTextField();
		contentPanel.add(emailField, "cell 1 7,growx");
		emailField.setColumns(10);

		JLabel lblJoinedDate = new JLabel("Joined Date");
		contentPanel.add(lblJoinedDate, "cell 0 8,alignx trailing");

		joinedDateField = new JTextField();
		contentPanel.add(joinedDateField, "cell 1 8,growx");
		joinedDateField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					CustomerDialog.this.dispose();
				}
			});
			okButton.setActionCommand("OK");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Customer customer = null;
						long id = Integer.parseInt(idField.getText());
						String firstName = firstNameField.getText();
						String lastName = lastNameField.getText();
						String street = streetField.getText();
						String city = cityField.getText();
						String postalCode = postalCodeField.getText();
						String phone = phoneField.getText();
						String emailAddress = emailField.getText();
						if (!Validator.validateEmail(emailAddress)) {
							throw new ApplicationException(String.format("Invalid email: %s", emailAddress));
						}
						String yyyymmdd = joinedDateField.getText();
						if (!Validator.validateJoinedDate(yyyymmdd)) {
							customer = new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName).setStreet(street).setCity(city)
									.setPostalCode(postalCode).setEmailAddress(emailAddress)
									.setJoinedDate(CustomerDao.getTheInstance().getCustomer(id).getJoinedDate()).build();
						} else {
							int year = Integer.parseInt(yyyymmdd.substring(0, 4));
							int month = Integer.parseInt(yyyymmdd.substring(5, 7));
							int day = Integer.parseInt(yyyymmdd.substring(8, 10));

							try {
								customer = new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName).setStreet(street)
										.setCity(city).setPostalCode(postalCode).setEmailAddress(emailAddress).setJoinedDate(year, month, day)
										.build();
							} catch (DateTimeException e1) {
								throw new ApplicationException(e1.getMessage());
							}
						}
						CustomerDao.getTheInstance().update(customer);
						CustomerListDialog.setRefresh(true);

					} catch (Exception e1) {
						LOG.error("ERROR- ", e1.getCause());
					}
					// }
					CustomerDialog.this.dispose();
				}
				// }
			});
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);

			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					CustomerDialog.this.dispose();
				}

			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
	}

	/**
	 * @param customer
	 */
	public void setCustomer(Customer customer) {
		idField.setText(Long.toString(customer.getId()));
		firstNameField.setText(customer.getFirstName());
		lastNameField.setText(customer.getLastName());
		streetField.setText(customer.getStreet());
		cityField.setText(customer.getCity());
		postalCodeField.setText(customer.getPostalCode());
		phoneField.setText(customer.getPhone());
		emailField.setText(customer.getEmailAddress());
		joinedDateField.setText(customer.getJoinedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

}