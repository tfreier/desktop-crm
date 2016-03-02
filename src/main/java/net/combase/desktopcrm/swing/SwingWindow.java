package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.apache.commons.io.IOUtils;

import net.combase.desktopcrm.data.FileImporter;
import net.combase.desktopcrm.domain.Lead;
import net.combase.desktopcrm.swing.DataSelectionEventManager.DataSelectionActivationListener;

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
		frame.setBounds(100, 100, 800, 500);
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

		final JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Tasks", CrmIcons.DONE, new TaskTablePanel());
		tabbedPane.addTab("Calls", CrmIcons.CALL, new CallTablePanel());
		tabbedPane.addTab("Action Required", CrmIcons.RECHEDULE, new ActionRequiredTablePanel());
		tabbedPane.addTab("Cases", CrmIcons.WARN, new CaseTablePanel());
		tabbedPane.addTab("Leads", CrmIcons.USER, new LeadTablePanel());
		tabbedPane.addTab("Search", CrmIcons.VIEW, new SearchTablePanel());
		tabbedPane.addTab("E-Mail Templates", CrmIcons.MAIL, new EmailTemplateTablePanel());

		frame.getContentPane().add(tabbedPane);

		frame.setIconImage(CrmIcons.USER.getImage());

		frame.addWindowListener(new WindowAdapter()
		{

			@Override
			public void windowClosing(WindowEvent we)
			{
				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(
					frame,
					"Are you sure you want to exit? You will no longer receive CRM notification if this window is closed.",
					"CRM Notifications", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					CrmIcons.WARN, ObjButtons, ObjButtons[1]);
				if (PromptResult == 0)
				{
					System.exit(0);
				}
			}
		});

		NotificationManager.init();

		DataSelectionEventManager.setDataSelActivationListener(new DataSelectionActivationListener()
		{

			@Override
			public void initiateDataSelection()
			{
				tabbedPane.setSelectedIndex(4);
				frame.setVisible(true);
				frame.setState(Frame.NORMAL);
				frame.setAlwaysOnTop(true);
				frame.toFront();
				frame.requestFocus();
				frame.setAlwaysOnTop(false);
			}
		});

		createDropListener(frame);
	}

	private void createDropListener(JFrame frame2)
	{
		DropTargetListener dropListener = new java.awt.dnd.DropTargetListener()
		{
			@Override
			public void dragEnter(java.awt.dnd.DropTargetDragEvent evt)
			{
				evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
			}

			@Override
			public void dragOver(java.awt.dnd.DropTargetDragEvent evt)
			{

			}

			@Override
			public void drop(java.awt.dnd.DropTargetDropEvent evt)
			{
				try
				{
					java.awt.datatransfer.Transferable tr = evt.getTransferable();

					if (tr.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.javaFileListFlavor))
					{
						evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);

						@SuppressWarnings("unchecked")
						List<File> fileList = (List<File>)tr.getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor);
						for (File file : fileList)
						{
							Collection<Lead> leads = FileImporter.importFile(file);
							for (Lead l : leads)
								DesktopUtil.openBrowser(l.getViewUrl());
						}
					}
					else
					{
						DataFlavor[] flavors = tr.getTransferDataFlavors();
						boolean handled = false;
						for (DataFlavor df : flavors)
						{
							if (df.isRepresentationClassReader())
							{
								evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);

								Reader reader = df.getReaderForText(tr);

								BufferedReader br = new BufferedReader(reader);

								String line = null;
								while ((line = br.readLine()) != null)
									System.out.println(line);

								// Mark that drop is completed.
								evt.getDropTargetContext().dropComplete(true);
								handled = true;
								break;
							}
						}
						if (!handled)
						{
							for (DataFlavor df : flavors)
							{
								try
								{
									if (df.isRepresentationClassInputStream())
									{
										evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
										InputStream transferData = (InputStream)tr.getTransferData(df);
										char[] bArray = IOUtils.toCharArray(transferData,
											"UTF-16LE");
										String str = new String(bArray);
										System.out.println("IS Line: " + str);
									}
									else if (df.isRepresentationClassByteBuffer())
									{
										evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
										ByteBuffer bf = (ByteBuffer)tr.getTransferData(df);
										String str = new String(bf.array(), "UTF-16LE");
										System.out.println("byte buffer: " + str);
									}
									else
									{
										evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
										System.out.println(tr.getTransferData(df));

									}
								}
								catch (java.awt.dnd.InvalidDnDOperationException e)
								{
									System.err.println(e.getMessage());
								}
							}
						}

					}
				}
				catch (java.io.IOException | UnsupportedFlavorException io)
				{
					io.printStackTrace();
				}

			}

			@Override
			public void dragExit(java.awt.dnd.DropTargetEvent evt)
			{

			}

			@Override
			public void dropActionChanged(java.awt.dnd.DropTargetDragEvent evt)
			{
				evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);

			}
		};

		frame.setDropTarget(new DropTarget(frame, dropListener));
	}

}
