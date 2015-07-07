/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.Task;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

/**
 * @author "Till Freier"
 *
 */
public class TaskTableModel extends AbstractTableModel
{
	private enum RescheduleOption
	{
		LATER, TOMORRW, NEXT_WEEK
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3890791456083674319L;
	private static final String[] COLUMN_NAMES = new String[] { "Task", "Due", "", "", "" };
	private static final Class<?>[] COLUMN_TYPES = new Class<?>[] { String.class, String.class,
			JButton.class, JButton.class, JButton.class };

	private final List<Task> data;

	public TaskTableModel(List<Task> data)
	{
		super();
		this.data = data;
	}

	public void update(List<Task> tasks)
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
		Task task = data.get(rowIndex);
		switch (columnIndex)
		{
			case 0 :
				return task.getTitle();
			case 1 :
				if (task.getDue() == null)
					return "";

				return task.getDue().minusHours(4).toDateTime(DateTimeZone.getDefault()).toString("E MM/dd/yy HH:mm");
			case 2 :
				return createViewButton(task);
			case 3 :
				return createRescheduleButton(task);
			case 4 :
				return createDoneButton(task);

			default :
				return "Error";
		}
	}

	/**
	 * @return
	 */
	private JButton createDoneButton(final Task task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				CrmManager.completeTask(task);
				data.remove(task);
				fireTableDataChanged();
			}
		});


		button.setIcon(CrmIcons.DONE);
		if (task.getDue() != null && task.getDue().isBeforeNow())
			button.setBackground(new Color(255, 0, 0, 100));
		else if (task.getDue() != null && task.getDue().toLocalDate().isEqual(new LocalDate()))
			button.setBackground(new Color(255, 100, 100, 100));
		else
			button.setBackground(new Color(100, 255, 100, 100));
		button.setToolTipText("Mark as done...");

		return button;
	}

	private JButton createRescheduleButton(final Task task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				RescheduleOption[] values = RescheduleOption.values();
				int result = JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(button),
					"Please select a reschedule option.", "Reschedule", JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, CrmIcons.RECHEDULE, values,
					RescheduleOption.TOMORRW);

				if (result < 0)
					return;

				RescheduleOption value = values[result];

				DateTime due = task.getDue();
				if (due == null)
					due = new DateTime();
				switch (value)
				{
					case LATER :
						due = due.plusHours(1);
						break;
					case NEXT_WEEK :
						due = due.plusWeeks(1);
						break;
					case TOMORRW :
						due = due.plusDays(1);
						break;
					default :
						break;
				}

				task.setDue(due);

				EventQueue.invokeLater(new Runnable()
				{

					@Override
					public void run()
					{
						CrmManager.rescheduleTask(task, task.getDue());
						fireTableDataChanged();
					}
				});

			}
		});

		button.setBackground(new Color(255, 175, 80, 100));
		button.setIcon(CrmIcons.RECHEDULE);
		button.setToolTipText("Reschedule task...");

		return button;
	}

	private JButton createViewButton(final Task task)
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
		button.setToolTipText("View task...");

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
}
