/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.Case;
import net.combase.desktopcrm.domain.Contact;

/**
 * @author "Till Freier"
 *
 */
public class CaseTableModel extends AbstractTableModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3890791456083674319L;
	private static final String[] COLUMN_NAMES = new String[] { "Case", "", "" };
	private static final Class<?>[] COLUMN_TYPES = new Class<?>[] { String.class, JButton.class,
			JButton.class };

	private final List<Case> data;

	public CaseTableModel(List<Case> data)
	{
		super();
		this.data = data;
	}

	public void update(List<Case> tasks)
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
		Case task = data.get(rowIndex);
		switch (columnIndex)
		{
			case 0 :
				return task.getTitle();
			case 1 :
				return createViewButton(task);
			case 2 :
				return createEmailButton(task);

			default :
				return "Error";
		}
	}


	private JButton createEmailButton(final Case task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				final Collection<Contact> contacts = CrmManager.getContactListByCase(task.getId());


				StringBuilder subjectSB = new StringBuilder();
				subjectSB.append("[CASE:").append(task.getNumber()).append("] ");
				subjectSB.append(task.getTitle());

				SendEmailDialog.sendEmail(null, subjectSB.toString(), contacts);


			}
		});

		button.setBackground(new Color(255, 255, 255, 100));
		button.setIcon(CrmIcons.MAIL);
		button.setToolTipText("Send email...");

		return button;
	}

	private JButton createViewButton(final Case task)
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
		button.setToolTipText("View case...");

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

	public List<Case> getData()
	{
		return new ArrayList<>(data);
	}
}
