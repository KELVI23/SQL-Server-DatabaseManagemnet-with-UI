
package a01085867.ui;

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

import a01085867.book.io.PurchasesReport.Item;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PurchaseDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Create the dialog.
	 */
	public PurchaseDialog(List<Item> purchases) {
		setBounds(100, 100, 800, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		Object columnNames[] = { "Customer Name", "Book Title", "Price CAD$" };

		Object rowData[][] = new Object[purchases.size()][11];
		int i = 0;
		for (Item purchase : purchases) {
			try {
				rowData[i][0] = purchase.getFirstName().concat(" ").concat(purchase.getLastName());
				rowData[i][1] = purchase.getTitle();
				rowData[i][2] = String.format("%,.2f", purchase.getPrice());
				++i;
			} catch (Exception e) {
				LOG.error("ERROR -", e);
			}
		}

		final JTable table = new JTable(rowData, columnNames);
		int width = this.getWidth();
		int height = this.getHeight();
		contentPanel.setLayout(new MigLayout("", "[772px]", "[][297px]"));
		{
			JLabel lblListOfPurchases = new JLabel("List of Purchases");
			lblListOfPurchases.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
			contentPanel.add(lblListOfPurchases, "cell 0 0");
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
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						PurchaseDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						PurchaseDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	protected static void callDialog(List<Item> purchases) {
		try {
			PurchaseDialog dialog = new PurchaseDialog(purchases);
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception ex) {
			LOG.error("ERROR -", ex);
		}
	}
}
