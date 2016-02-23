/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import net.combase.desktopcrm.data.AsteriskManager;
import net.combase.desktopcrm.domain.Lead;

/**
 * @author "Till Freier"
 *
 */
public class LeadTableModel extends AbstractTableModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3890791456083674319L;

	private static final String[] COLUMN_NAMES = new String[] { "Lead", "", "", "" };

	private static final Class<?>[] COLUMN_TYPES = new Class<?>[] { String.class, JButton.class, JButton.class,
			JButton.class };

	private final List<Lead> data;

	public LeadTableModel(List<Lead> data)
	{
		super();
		this.data = data;
	}

	public void update(List<Lead> tasks)
	{
		data.clear();
		data.addAll(tasks);

		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return COLUMN_NAMES[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return COLUMN_TYPES[columnIndex];
	}


	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex)
	{
		/* Adding components */
		Lead task = data.get(rowIndex);
		switch (columnIndex)
		{
			case 0 :
				return task.getTitle();
			case 1 :
				return createViewButton(task);
			case 2 :
				return createEmailButton(task);
			case 3:
				return createCallButton(task);

			default :
				return "Error";
		}
	}


	private JButton createEmailButton(final Lead task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				SendEmailDialog.sendEmail(null, "", Arrays.asList(task));


			}
		});

		button.setBackground(new Color(255, 255, 255, 100));
		button.setIcon(CrmIcons.MAIL);
		button.setToolTipText("Send email...");

		return button;
	}


	private JButton createCallButton(final Lead task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				AsteriskManager.dial(task.getPhone());
			}
		});

		button.setBackground(new Color(90, 90, 90, 100));
		button.setIcon(CrmIcons.CALL);
		button.setToolTipText("Call " + task.getPhone());

		return button;
	}

	private JButton createViewButton(final Lead task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				DesktopUtil.openBrowser(task.getViewUrl());
			}
		});


		button.setBackground(new Color(90, 115, 255, 100));
		button.setIcon(CrmIcons.VIEW);
		button.setToolTipText("View lead...");

		return button;
	}

	@Override
	public int getRowCount()
	{
		return data.size();
	}

	@Override
	public int getColumnCount()
	{
		return COLUMN_NAMES.length;
	}

	public List<Lead> getData()
	{
		return new ArrayList<Lead>(data);
	}


}
