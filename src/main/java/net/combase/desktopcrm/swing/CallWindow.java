/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.joda.time.DateTime;

import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.AbstractCrmObject;
import net.combase.desktopcrm.domain.Call;
import net.combase.desktopcrm.domain.CallType;
import net.combase.desktopcrm.domain.Contact;
import net.combase.desktopcrm.domain.Opportunity;
import net.combase.desktopcrm.swing.DataSelectionEventManager.DataSelectionListener;



/**
 * TODO: create task from call use URL crm/index.php?module=Tasks&action=EditView&record=4fc558fb-1303-f533-cdda-57913ac416e9
 * 
 * @author "Till Freier"
 */
public class CallWindow extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -601570990578198225L;

	private AbstractCrmObject contact;

	private DateTime start = new DateTime();

	private JTextArea text;

	private final CallType type;


	public CallWindow(String number, CallType type)
	{
		super();
		this.type = type;
		setIconImage(CrmIcons.CALL.getImage());

		setContact(number);

		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());

		text = new JTextArea();
		text.setText("\n\nCalled via " + number);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		main.add(new JScrollPane(text), BorderLayout.CENTER);

		JPanel top = new JPanel();
		top.setLayout(new FlowLayout());

		final JLabel label = new JLabel(contact != null ? contact.getTitle() : number);
		top.add(label);
		final JButton select = new JButton("...");
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				select.setEnabled(false);
				DataSelectionEventManager.initiateDataSelection(new DataSelectionListener() {
					@Override
					public void dataSelected(AbstractCrmObject data)
					{
						contact = data;
						label.setText(data.getClass().getSimpleName() + ": " + data.getTitle());
						select.setEnabled(true);

						setState(Frame.NORMAL);
						setAlwaysOnTop(true);
						toFront();
						requestFocus();
						setAlwaysOnTop(false);
					}
				});
			}
		});
		top.add(select);

		main.add(top, BorderLayout.NORTH);

		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());

		JButton support = new JButton("Support");
		support.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveAction("Support");
			}
		});
		bottom.add(support);

		JButton sales = new JButton("Sales");
		sales.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveAction("Sales");
			}
		});
		bottom.add(sales);

		JButton other = new JButton("Other");
		other.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveAction("Other");
			}
		});
		bottom.add(other);

		main.add(bottom, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent we)
			{
				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(CallWindow.this, "Are you sure this call is not worth getting logged?", "CRM Call Logging", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, CrmIcons.WARN, ObjButtons, ObjButtons[1]);
				if (PromptResult == 0)
				{
					setVisible(false);
				}
				else
				{
					EventQueue.invokeLater(new Runnable() {

						@Override
						public void run()
						{
							setVisible(true);
							CallWindow.this.requestFocus();
						}
					});
				}
			}
		});

		getContentPane().add(main);
		setSize(300, 250);
		setVisible(true);

		setState(Frame.NORMAL);
		setAlwaysOnTop(true);
		toFront();
		requestFocus();
		setAlwaysOnTop(false);
		text.requestFocus();
		text.requestFocusInWindow();
		text.setCaretPosition(0);
	}


	protected void saveAction(String type)
	{
		Call c = new Call();
		c.setStart(start);
		if (contact != null)
		{
			c.setRelatedObjectId(contact.getId());
			c.setRelatedObjectType(contact.getCrmEntityType());
		}
		String desc = text.getText();
		String title = ": ";
		if (desc != null && desc.length() > 3)
			title += desc.substring(0, Math.min(desc.length(), 30));
		c.setTitle(type + title);
		c.setDescription(desc);
		c.setType(this.type);

		setVisible(false);
		CrmManager.createCall(c);
	}


	/**
	 * @param number
	 */
	private void setContact(String number)
	{
		contact = CrmManager.getContactByNumber(number);
		if (contact != null)
		{
			setIconImage(CrmIcons.USER.getImage());
			final String accountId = ((Contact) contact).getAccountId();
			if (accountId != null && !accountId.isEmpty())
			{
				Collection<Opportunity> list = CrmManager.getOpportunityListByAccount(accountId);
				for (Opportunity opportunity : list)
				{
					contact = opportunity;
					setIconImage(CrmIcons.BELL.getImage());
				}
			}

		}
		else
		{
			contact = CrmManager.getLeadByNumber(number);
			setIconImage(CrmIcons.USER.getImage());
		}

		if (contact != null)
			setTitle("Call with " + contact.getTitle());
		else
			setTitle("Call with " + number);
	}

}
