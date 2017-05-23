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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.window.Positions;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.AbstractCrmObject;
import net.combase.desktopcrm.domain.Call;
import net.combase.desktopcrm.domain.CallType;
import net.combase.desktopcrm.domain.Contact;
import net.combase.desktopcrm.domain.Opportunity;
import net.combase.desktopcrm.domain.Task;
import net.combase.desktopcrm.swing.DataSelectionEventManager.DataSelectionListener;



/**
 * TODO: create task from call use URL
 * crm/index.php?module=Tasks&action=EditView&record=4fc558fb-1303-f533-cdda-57913ac416e9
 * 
 * @author "Till Freier"
 */
public class CallWindow extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -601570990578198225L;

	private AbstractCrmObject relatedObject;
	private Contact contact;

	private DateTime start = new DateTime();

	private JTextArea text;

	private final CallType type;

	private TaskTablePanel taskPanel;

	private JPanel activitySummary;

	@SuppressWarnings("restriction")
	private WebView browser;

	private JLabel label;


	public CallWindow(String number, CallType type)
	{
		super();
		this.type = type;
		init(number);
	}


	@SuppressWarnings("restriction")
	private void init(String number)
	{
		setIconImage(CrmIcons.CALL.getImage());

		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());

		text = new JTextArea();
		text.setText("\n\nCalled via " + number);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		main.add(new JScrollPane(text), BorderLayout.CENTER);

		JPanel top = new JPanel();
		top.setLayout(new FlowLayout());

		label = new JLabel(number);
		top.add(label);
		final JButton select = new JButton("");
		select.setIcon(CrmIcons.SETTINGS);
		select.setToolTipText("Change Contact");
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				select.setEnabled(false);
				DataSelectionEventManager.initiateDataSelection(new DataSelectionListener() {
					@Override
					public void dataSelected(AbstractCrmObject data)
					{
						relatedObject = data;
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
		final JButton view = new JButton("");
		view.setIcon(CrmIcons.VIEW);
		view.setToolTipText("Open Contact");
		view.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (relatedObject == null)
				{
					NotificationBuilder nb = DesktopUtil.createNotificationBuilder();
					nb.withTitle("No contact found");
					nb.withMessage("Please assign a contact first.");
					nb.withIcon(CrmIcons.WARN);
					nb.withDisplayTime(3000);
					nb.withPosition(Positions.CENTER);

					nb.showNotification();
				}
				else
					DesktopUtil.openBrowser(relatedObject.getViewUrl());

			}
		});
		top.add(view);

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

		taskPanel = new TaskTablePanel(new ArrayList<Task>(0));

		final JPanel mainTask = new JPanel();
		mainTask.setLayout(new BorderLayout());
		mainTask.add(taskPanel, BorderLayout.CENTER);

		activitySummary = new JPanel();
		activitySummary.setLayout(new BorderLayout());

		new JFXPanel();
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				initBrowser();
			}
		});

		final JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Call Info", CrmIcons.CALL, main);
		tabbedPane.addTab("Related Tasks", CrmIcons.DONE, mainTask);
		tabbedPane.addTab("Activities", CrmIcons.TALK, activitySummary);

		getContentPane().add(tabbedPane);
		setSize(640, 480);
		setVisible(true);

		setState(Frame.NORMAL);
		setAlwaysOnTop(true);
		toFront();
		requestFocus();
		setAlwaysOnTop(false);
		text.requestFocus();
		text.requestFocusInWindow();
		text.setCaretPosition(0);

		setContact(number);
	}


	@SuppressWarnings("restriction")
	void initBrowser()
	{
		Stage stage = new Stage();

		stage.setTitle("Hello Java FX");
		stage.setResizable(true);

		Group root = new Group();
		Scene scene = new Scene(root, 1, 1);
		stage.setScene(scene);

		// Set up the embedded browser:
		browser = new WebView();
		browser.setPrefSize(630, 435);
		ObservableList<Node> children = root.getChildren();
		children.add(browser);

		JFXPanel jfxPanel = new JFXPanel();
		jfxPanel.setScene(scene);

		activitySummary.add(jfxPanel, BorderLayout.CENTER);
		activitySummary.revalidate();

	}


	protected void saveAction(String type)
	{
		Call c = new Call();
		c.setStart(start);
		if (relatedObject != null)
		{
			c.setRelatedObjectId(relatedObject.getId());
			c.setRelatedObjectType(relatedObject.getCrmEntityType());
		}
		
		if (contact != null)
			c.setContactId(contact.getId());
		
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
		relatedObject = CrmManager.getContactByNumber(number);
		if (relatedObject != null)
		{
			setIconImage(CrmIcons.USER.getImage());
			Contact c = (Contact) relatedObject;
			final String accountId = c.getAccountId();
			if (accountId != null && !accountId.isEmpty())
			{
				Collection<Opportunity> list = CrmManager.getOpportunityListByAccount(accountId);
				if (!list.isEmpty())
				{
				for (Opportunity opportunity : list)
				{
					relatedObject = opportunity;
					setIconImage(CrmIcons.BELL.getImage());
				}
				}
				else
				{
					relatedObject = CrmManager.getAccount(accountId);
					contact = c;
				}
			}
			else
				relatedObject = c;
		}
		else
		{
			relatedObject = CrmManager.getLeadByNumber(number);
			setIconImage(CrmIcons.USER.getImage());
		}

		if (relatedObject != null)
			setTitle("Call with " + relatedObject.getTitle());
		else
			setTitle("Call with " + number);

		SwingUtilities.invokeLater(new Runnable() {

			@SuppressWarnings("restriction")
			@Override
			public void run()
			{
				if (relatedObject == null)
					return;
				final List<Task> tasks = CrmManager.getTaskListByParent(relatedObject.getId());
				taskPanel.updateTaskList(Lists.reverse(tasks));
				label.setText(relatedObject.getTitle());

				Platform.runLater(new Runnable() {
					@Override
					public void run()
					{
						WebEngine engine = browser.getEngine();
						engine.load(relatedObject.getActivitiesUrl());

					}
				});

				text.requestFocus();
			}
		});
	}

}
