package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class SwingWindow {

	private JFrame frame;

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
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
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
	}

}
