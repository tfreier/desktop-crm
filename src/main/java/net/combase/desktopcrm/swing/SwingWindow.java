package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class SwingWindow
{
	private JFrame frame;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
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
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

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

		JMenuItem mntmNotificationSetup = new JMenuItem("Notifications");
		mntmNotificationSetup.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				NotificationSettings s = new NotificationSettings();
				s.setVisible(true);
			}
		});
		mnSettings.add(mntmNotificationSetup);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				About s = new About();
				s.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);


		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Tasks", CrmIcons.DONE, new TaskTablePanel());
		tabbedPane.addTab("Calls", CrmIcons.CALL, new CallTablePanel());
		tabbedPane.addTab("Cases", CrmIcons.WARN, new CaseTablePanel());

		frame.getContentPane().add(tabbedPane);

		frame.setIconImage(CrmIcons.USER.getImage());

		frame.addWindowListener(new WindowAdapter()
		{

			@Override
			public void windowClosing(WindowEvent we)
			{
				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(null,
					"Are you sure you want to exit? You will no longer receive CRM notification if this window is closed.",
					"CRM Notifications",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons,
					ObjButtons[1]);
				if (PromptResult == 0)
				{
					System.exit(0);
				}
			}
		});

		NotificationManager.init();
	}

}
