package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import net.combase.desktopcrm.data.CrmHelper;
import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.AbstractCrmObject;
import net.combase.desktopcrm.domain.Call;
import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.event.NotificationEvent;
import ch.swingfx.twinkle.event.NotificationEventAdapter;

public class SwingWindow
{
	private JFrame frame;

	private static final Runnable actionRequiredCheckRunner = new Runnable()
	{

		@Override
		public void run()
		{
			List<AbstractCrmObject> leadList = CrmHelper.getActionObjects();
			for (final AbstractCrmObject lead : leadList)
			{
				System.out.println("request action item for " + lead.getTitle());
				String msg = "'" + lead.getTitle() +
					"' has no planned actions. Click here to schedule a task.";

				NotificationBuilder nb = DesktopUtil.createNotificationBuilder();
				nb.withTitle("Plan Follow Up Action");
				nb.withMessage(msg);
				nb.withIcon(CrmIcons.WARN);
				nb.withDisplayTime(60000);

				nb.withListener(new NotificationEventAdapter()
				{
					@Override
					public void clicked(NotificationEvent event)
					{
						DesktopUtil.openBrowser(lead.getViewUrl());
					}
				});

				nb.showNotification();

				// wait 2 minutes
				try
				{
					Thread.sleep(180000);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
			}
			// wait 5 minutes
			try
			{
				Thread.sleep(300000);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}

			actionRequiredCheckRunner.run();
		}
	};
	private static final Runnable callCheckRunner = new Runnable()
	{

		@Override
		public void run()
		{
			System.out.println("Check for calls...");
			final Call c = CrmManager.getUpcomingCall();
			if (c != null)
			{
				String msg = "You have an upcoming call: " + c.getTitle();

				NotificationBuilder nb = DesktopUtil.createNotificationBuilder();
				nb.withTitle("Upcoming Call");
				nb.withMessage(msg);
				nb.withIcon(CrmIcons.CALL);
				nb.withDisplayTime(30000);

				nb.withListener(new NotificationEventAdapter()
				{
					@Override
					public void clicked(NotificationEvent event)
					{
						DesktopUtil.openBrowser(c.getViewUrl());
					}
				});

				nb.showNotification();
			}
			// wait 2 minutes
			try
			{
				Thread.sleep(120000);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}

			callCheckRunner.run();
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
// NotificationBuilder n = DesktopUtil.createNotificationBuilder();
// n.withTitle("Test").withMessage("Hello").showNotification();

		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					NimbusLookAndFeel laf = new NimbusLookAndFeel();
					UIManager.setLookAndFeel(laf);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					SwingWindow window = new SwingWindow();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingWindow()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{


		frame = new JFrame("Sugar CRM");
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);

		JMenuItem mntmCrmSetup = new JMenuItem("CRM Setup");
		mntmCrmSetup.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CrmSettings s = new CrmSettings();
				s.setVisible(true);
			}
		});
		mnSettings.add(mntmCrmSetup);

		JMenuItem mntmNotifications = new JMenuItem("Notifications");
		mnSettings.add(mntmNotifications);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		frame.getContentPane().add(new TaskTablePanel());

		new Thread(actionRequiredCheckRunner).start();
		new Thread(callCheckRunner).start();
	}

}
