/**
 * Project: A01085867_Assignment2_2021
 * File: BookDialog.java
 * Date: Jun. 27, 2021
 * Time: 4:02:32 p.m.
 */
package a01085867.book.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01085867.book.data.Book;
import net.miginfocom.swing.MigLayout;

/**
 * @author Kelvin Musodza, A01085867
 *
 */
@SuppressWarnings("serial")
public class BookDialog extends JDialog {

	private static Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public BookDialog(List<Book> books) {
		setBounds(100, 100, 800, 300);
		getContentPane().setLayout(new BorderLayout());
		setResizable(false);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		Object columnNames[] = { "ID", "ISBN", "Authors", "Title", "Year", "Rating", "Ratings Count", "Image URL" };

		Object rowData[][] = new Object[books.size()][11];
		int i = 0;
		for (Book book : books) {
			rowData[i][0] = book.getId();
			rowData[i][1] = book.getIsbn();
			rowData[i][2] = book.getAuthors();
			rowData[i][3] = book.getYear();
			rowData[i][4] = book.getTitle();
			rowData[i][5] = book.getRating();
			rowData[i][6] = book.getRatingsCount();
			rowData[i][7] = book.getImageUrl();
			++i;
		}

		final JTable table = new JTable(rowData, columnNames);
		int width = this.getWidth();
		int height = this.getHeight();
		contentPanel.setLayout(new MigLayout("", "[772px]", "[][297px]"));
		{
			JLabel lblListOfBooks = new JLabel("List of Books");
			lblListOfBooks.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
			contentPanel.add(lblListOfBooks, "cell 0 0");
		}

		table.setPreferredScrollableViewportSize(new Dimension(width - 30, height - 30));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultEditor(Object.class, null);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane scrollPane = new JScrollPane(table);

		contentPanel.add(scrollPane, "cell 0 1,alignx left,aligny top");

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
						BookDialog.this.dispose();

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
						BookDialog.this.dispose();
					}
				});
			}
		}
	}

	protected static void callDialog(List<Book> books) {
		try {
			BookDialog dialog = new BookDialog(books);
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception ex) {
			LOG.error("ERROR -", ex.getMessage());
		}
	}
}