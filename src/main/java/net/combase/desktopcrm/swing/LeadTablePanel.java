package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.Lead;



public class LeadTablePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6149463410211475900L;

	private JTable table;


	/**
	 * Create the panel.
	 */
	public LeadTablePanel()
	{
		setLayout(new BorderLayout(0, 0));

		final LeadTableModel model = new LeadTableModel(new ArrayList<Lead>());

		table = new JTable(model);

		// enable button clicks
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int column = table.getColumnModel().getColumnIndexAtX(e.getX());
				int row = e.getY() / table.getRowHeight();

				if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0)
				{
					Object value = table.getValueAt(row, column);
					if (value instanceof JButton)
					{
						((JButton) value).doClick();
					}
				}
			}
		});

		table.setDefaultRenderer(JButton.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				if (value instanceof JButton)
				{
					return (JButton) value;
				}

				return new JLabel();
			}
		});

		table.getColumnModel().getColumn(1).setMaxWidth(30);
		table.getColumnModel().getColumn(2).setMaxWidth(30);
		table.getColumnModel().getColumn(3).setMaxWidth(30);
		table.setRowHeight(30);

		add(table.getTableHeader(), BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int index = e.getFirstIndex();
				if (index < 0)
					return;

				Lead data = model.getData().get(index);
				DataSelectionEventManager.dataSelected(data);
			}
		});

		UiUtil.runAndRepeat(new Runnable() {
			@Override
			public void run()
			{
				final List<Lead> updatedList = CrmManager.getLeadList();
				System.out.println(updatedList);
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run()
					{
						model.update(updatedList);
					}
				});
			}
		}, 250, 123000);
	}

}
