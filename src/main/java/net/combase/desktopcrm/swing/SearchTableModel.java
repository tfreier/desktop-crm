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
import net.combase.desktopcrm.domain.AbstractCrmObject;
import net.combase.desktopcrm.domain.Lead;

/**
 * @author "Till Freier"
 *
 */
public class SearchTableModel extends AbstractTableModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8394157828871760312L;

	private static final String[] COLUMN_NAMES = new String[] { "Type", "Name", "", "", "" };
	private static final Class<?>[] COLUMN_TYPES = new Class<?>[] { String.class, String.class,
 JButton.class, JButton.class, JButton.class };

	private final List<AbstractCrmObject> data;

	public SearchTableModel(List<AbstractCrmObject> data)
	{
		super();
		this.data = data;
	}

	public void update(List<AbstractCrmObject> tasks)
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
		AbstractCrmObject task = data.get(rowIndex);
		switch (columnIndex)
		{
			case 0 :
				return task.getClass().getSimpleName();
			case 1 :
				return task.getTitle();
			case 2 :
				return createViewButton(task);
			case 3:
				return createEmailButton(task);
			case 4:
				return createCallButton(task);

			default :
				return "Error";
		}
	}



	private JButton createViewButton(final AbstractCrmObject task)
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
		button.setToolTipText("View " + task.getClass().getSimpleName());

		return button;
	}


	private JButton createCallButton(final AbstractCrmObject task)
	{
		String no = null;

		if (task instanceof Lead)
		{
			Lead lead = (Lead) task;
			no = lead.getPhone();
		}

		final JButton button = new JButton();
		final String number = no;
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				AsteriskManager.dial(number);
			}
		});

		button.setEnabled(no != null && !no.isEmpty());

		button.setBackground(new Color(90, 90, 90, 100));
		button.setIcon(CrmIcons.CALL);
		button.setToolTipText("Call " + no);

		return button;
	}


	private JButton createEmailButton(final AbstractCrmObject task)
	{
		String mail = null;

		if (task instanceof Lead)
		{
			Lead lead = (Lead) task;
			mail = lead.getEmail();
		}

		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				SendEmailDialog.sendEmail(null, "", Arrays.asList((Lead) task));
			}
		});

		button.setEnabled(mail != null && !mail.isEmpty());

		button.setBackground(new Color(255, 255, 255, 100));
		button.setIcon(CrmIcons.MAIL);
		button.setToolTipText("email " + task.getClass().getSimpleName());

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

	public List<AbstractCrmObject> getData()
	{
		return new ArrayList<AbstractCrmObject>(data);
	}


}
