package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.AbstractCrmObject;

public class SearchTablePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1772875193943230752L;
	private JTable table;

	/**
	 * Create the panel.
	 */
	public SearchTablePanel()
	{
		setLayout(new BorderLayout(0, 0));


		final SearchTableModel model = new SearchTableModel(new ArrayList<AbstractCrmObject>());

		table = new JTable(model);

		// enable button clicks
		table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int column = table.getColumnModel().getColumnIndexAtX(e.getX());
				int row = e.getY() / table.getRowHeight();

				if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() &&
					column >= 0)
				{
					Object value = table.getValueAt(row, column);
					if (value instanceof JButton)
					{
						((JButton)value).doClick();
					}
				}
			}
		});

		table.setDefaultRenderer(JButton.class, new TableCellRenderer()
		{
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
			{
				if (value instanceof JButton)
				{
					return (JButton)value;
				}

				return new JLabel();
			}
		});

		table.getColumnModel().getColumn(0).setMaxWidth(100);
		table.getColumnModel().getColumn(2).setMaxWidth(30);
		table.getColumnModel().getColumn(3).setMaxWidth(30);
		table.getColumnModel().getColumn(4).setMaxWidth(30);
		table.setRowHeight(30);

		final JTextField searchField = new JTextField();

		searchField.addKeyListener(new KeyListener()
		{

			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					model.update(new ArrayList<AbstractCrmObject>());

					EventQueue.invokeLater(new Runnable()
					{

						@Override
						public void run()
						{
							List<AbstractCrmObject> results = model.getData();
							results.addAll(CrmManager.findContacts(searchField.getText()));
							model.update(results);
						}
					});
					EventQueue.invokeLater(new Runnable()
					{

						@Override
						public void run()
						{
							List<AbstractCrmObject> results = model.getData();
							results.addAll(CrmManager.findLeads(searchField.getText()));
							model.update(results);
						}
					});
					EventQueue.invokeLater(new Runnable()
					{

						@Override
						public void run()
						{
							List<AbstractCrmObject> results = model.getData();
							results.addAll(CrmManager.findOpportunity(searchField.getText()));
							model.update(results);
						}
					});
					EventQueue.invokeLater(new Runnable()
					{

						@Override
						public void run()
						{
							List<AbstractCrmObject> results = model.getData();
							results.addAll(CrmManager.findCase(searchField.getText()));
							model.update(results);
						}
					});
					EventQueue.invokeLater(new Runnable()
					{

						@Override
						public void run()
						{
							List<AbstractCrmObject> results = model.getData();
							results.addAll(CrmManager.findAccount(searchField.getText()));
							model.update(results);
						}
					});

				}
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
			}
		});

		add(searchField, BorderLayout.NORTH);

		add(new JScrollPane(table), BorderLayout.CENTER);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int index = e.getFirstIndex();
				if (index < 0)
					return;

				AbstractCrmObject data = model.getData().get(index);
				DataSelectionEventManager.dataSelected(data);
			}
		});
	}

}
