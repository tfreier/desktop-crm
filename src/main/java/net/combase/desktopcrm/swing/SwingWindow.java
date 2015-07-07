package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;

import org.apache.commons.io.IOUtils;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.event.NotificationEvent;
import ch.swingfx.twinkle.event.NotificationEventAdapter;
import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.Call;
import net.combase.desktopcrm.domain.Lead;

public class SwingWindow {
	private JFrame frame;

	private static final ImageIcon userIcon;
	private static final ImageIcon callIcon;

	static {
		Image ui = null, ci = null;
		try {
			ui = new ImageIcon(IOUtils.toByteArray(TaskTableModel.class.getResourceAsStream("/user.png")))
					.getImage();
			ci = new ImageIcon(IOUtils.toByteArray(TaskTableModel.class.getResourceAsStream("/call.png")))
					.getImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final int size = 20;
		userIcon = new ImageIcon(ui.getScaledInstance(size, size, Image.SCALE_SMOOTH));
		callIcon = new ImageIcon(ci.getScaledInstance(size, size, Image.SCALE_SMOOTH));

	}

	private static final Runnable leadCheckRunner = new Runnable() {

		@Override
		public void run() {
			List<Lead> leadList = CrmManager.getLeadList();
			for (final Lead lead : leadList) {
				boolean noAction = CrmManager.getTaskListByParent(lead.getId()).isEmpty();
				if (noAction)
					noAction = CrmManager.getCallListByParent(lead.getId()).isEmpty();

				if (noAction) {
					String msg = lead.getTitle() + " has no planned actions. Click here to schedule an action.";

					NotificationBuilder nb = DesktopUtil.createNotificationBuilder();
					nb.withTitle("Plan Lead Action");
					nb.withMessage(msg);
					nb.withIcon(userIcon);

					nb.withListener(new NotificationEventAdapter() {
						@Override
						public void clicked(NotificationEvent event) {
							DesktopUtil.openBrowser(lead.getViewUrl());
						}
					});

					nb.showNotification();

					// wait 5 minutes
					try {
						Thread.sleep(300000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				// wait 5 minutes
				try {
					Thread.sleep(300000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				leadCheckRunner.run();
			}
		}
	};
	private static final Runnable callCheckRunner = new Runnable() {

		@Override
		public void run() {
			System.out.println("Check for calls...");
			final Call c = CrmManager.getUpcomingCall();
			if (c != null) {
				String msg = "You have an upcoming call: "+c.getTitle();

				NotificationBuilder nb = DesktopUtil.createNotificationBuilder();
				nb.withTitle("Upcoming Call");
				nb.withMessage(msg);
				nb.withIcon(callIcon);
				nb.withDisplayTime(30000);

				nb.withListener(new NotificationEventAdapter() {
					@Override
					public void clicked(NotificationEvent event) {
						DesktopUtil.openBrowser(c.getViewUrl());
					}
				});

				nb.showNotification();
			}
			// wait 2 minutes
			try {
				Thread.sleep(120000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			callCheckRunner.run();
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// SeaGlassLookAndFeel laf = new SeaGlassLookAndFeel();
					// UIManager.setLookAndFeel(laf);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					SwingWindow window = new SwingWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// NotificationBuilder n = DesktopUtil.createNotificationBuilder();
		// n.withTitle("Test").withMessage("Hello").showNotification();

		frame = new JFrame();
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);

		JMenuItem mntmCrmSetup = new JMenuItem("CRM Setup");
		mntmCrmSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CrmSettings s = new CrmSettings();
				s.setVisible(true);
			}
		});
		mnSettings.add(mntmCrmSetup);

		JMenuItem mntmNotifications = new JMenuItem("Notifications");
		mnSettings.add(mntmNotifications);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		frame.getContentPane().add(new TaskTablePanel());

		new Thread(leadCheckRunner).start();
		new Thread(callCheckRunner).start();
	}

}
