/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import net.combase.desktopcrm.data.AsteriskManager;
import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.Call;
import net.combase.desktopcrm.domain.Contact;
import net.combase.desktopcrm.domain.Lead;



/**
 * @author "Till Freier"
 */
public class CallTableModel extends AbstractTableModel
{
	private enum RescheduleOption
	{
		LATER("Later"),
		TOMORROW_AM("Tomorrow AM"),
		TOMORROW_PM("Tomorrow PM"),
		DAYS_2("In 2 days"),
		NEXT_WEEK("Next Week");

		private final String label;


		/**
		 * @param label
		 */
		private RescheduleOption(String label)
		{
			this.label = label;
		}


		/*
		 * (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return label;
		}

	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -3890791456083674319L;

	private static final String[] COLUMN_NAMES = new String[] { "Call", "Time", "", "", "", ""};

	private static final Class<?>[] COLUMN_TYPES = new Class<?>[] { String.class, String.class, JButton.class, JButton.class, JButton.class, JButton.class };

	private final List<Call> data;


	public CallTableModel(List<Call> data)
	{
		super();
		this.data = data;
	}


	public void update(List<Call> tasks)
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
		Call task = data.get(rowIndex);
		switch (columnIndex)
		{
			case 0:
				return task.getTitle() + " [" + task.getExtendedTitle() + "]";
			case 1:
				if (task.getStart() == null)
					return "";

				return task.getStart().toDateTime(DateTimeZone.getDefault()).toString("E MM/dd/yy HH:mm");
			case 2:
				return createViewButton(task);
			case 3:
				return createCallButton(task);
			case 4:
				return createRescheduleButton(task);
			case 5:
				return createDoneButton(task);

			default:
				return "Error";
		}
	}


	/**
	 * @return
	 */
	private JButton createDoneButton(final Call task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				CrmManager.heldCall(task);
				data.remove(task);
				fireTableDataChanged();
			}
		});

		button.setIcon(CrmIcons.DONE);
		if (task.getStart() != null && task.getStart().isBeforeNow() && task.isPlanned())
			button.setBackground(new Color(255, 0, 0, 100));
		else if (task.getStart() != null && task.getStart().toLocalDate().isEqual(new LocalDate()) && task.isPlanned())
			button.setBackground(new Color(255, 150, 0, 100));
		else
			button.setBackground(new Color(100, 255, 100, 100));
		if (task.isPlanned())
			button.setToolTipText("Mark as done...");
		else
			button.setToolTipText("Call is completed.");
		button.setEnabled(task.isPlanned());

		return button;
	}


	private JButton createRescheduleButton(final Call task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				RescheduleOption[] values = RescheduleOption.values();
				int result = JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(button), "Please select a reschedule option.", "Reschedule", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, CrmIcons.RECHEDULE, values, RescheduleOption.TOMORROW_AM);

				if (result < 0)
					return;

				RescheduleOption value = values[result];

				DateTime due = task.getStart();

				if (due == null || due.isBeforeNow())
					due = new DateTime();

				switch (value)
				{
					case LATER:
						due = due.plusHours(3);
						break;
					case NEXT_WEEK:
						due = due.plusWeeks(1);
						break;
					case TOMORROW_AM:
						due = new DateTime().plusDays(1).toLocalDate().toDateTime(new LocalTime(9, 0, 0));
						break;
					case TOMORROW_PM:
						due = new DateTime().plusDays(1).toLocalDate().toDateTime(new LocalTime(14, 0, 0));
						break;
					case DAYS_2:
						due = due.plusDays(2);
						break;
					default:
						break;
				}

				task.setStart(due);

				CrmManager.rescheduleCall(task, task.getStart());
				fireTableDataChanged();
			}
		});

		button.setBackground(new Color(250, 245, 0, 100));
		button.setIcon(CrmIcons.RECHEDULE);
		button.setToolTipText("Reschedule call...");
		button.setEnabled(task.isPlanned());

		return button;
	}


	private JButton createViewButton(final Call task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				DesktopUtil.openBrowser(task.getViewUrl());
			}
		});

		button.setBackground(new Color(90, 115, 255, 100));
		button.setIcon(CrmIcons.VIEW);
		button.setToolTipText("View call...");

		return button;
	}


	private JButton createCallButton(final Call task)
	{
		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String name = "";
				String no = null;
				switch (task.getParentType())
				{
					case "Leads":
						Lead lead = CrmManager.getLead(task.getParentId());
						if (lead == null)
							break;
						name = lead.getTitle();
						no = lead.getPhone();
					case "Contacts":
						Contact c = CrmManager.getContact(task.getParentId());
						if (c == null)
							break;
						name = c.getTitle();
						no = c.getPhone();
					case "Opportunities":
						Collection<Contact> contacts = CrmManager.getContactListByOpportunity(task.getParentId());
						if (contacts != null && !contacts.isEmpty())
						{
							Contact next = contacts.iterator().next();
							name = next.getTitle();
							no = next.getPhone();
						}
					default:
						break;
				}
				if (no == null || no.trim().isEmpty())
					JOptionPane.showMessageDialog(null, "No number found for " + name);

				AsteriskManager.dial(no);
			}
		});

		button.setBackground(new Color(90, 90, 90, 100));

		button.setIcon(CrmIcons.CALL);

		if (task.getParentId() == null || task.getParentId().trim().isEmpty())
		{
			button.setVisible(false);
			button.setEnabled(false);
		}

		button.setToolTipText("Call " + task.getExtendedTitle());

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
