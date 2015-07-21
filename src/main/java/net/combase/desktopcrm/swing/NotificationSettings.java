package net.combase.desktopcrm.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.combase.desktopcrm.data.DataStoreManager;
import net.combase.desktopcrm.domain.Settings;

public class NotificationSettings extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7302979661665267145L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public NotificationSettings()
	{
		setBounds(100, 100, 450, 300);
		setIconImage(CrmIcons.BELL.getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		final Settings settings = DataStoreManager.getSettings();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.anchor = GridBagConstraints.WEST;

		final JCheckBox callReminder = new JCheckBox("Call reminder", settings.isCallReminder());
		gbc.gridy++;
		contentPane.add(callReminder, gbc);

		final JCheckBox taskReminder = new JCheckBox("Task reminder", settings.isTaskReminder());
		gbc.gridy++;
		contentPane.add(taskReminder, gbc);

		final JCheckBox leadReminder = new JCheckBox("Lead reminder", settings.isLeadReminder());
		gbc.gridy++;
		contentPane.add(leadReminder, gbc);

		final JCheckBox opportunityReminder = new JCheckBox("Opportunity reminder",
			settings.isOpportunityReminder());
		gbc.gridy++;
		contentPane.add(opportunityReminder, gbc);

		final JCheckBox caseReminder = new JCheckBox("Case reminder", settings.isCaseReminder());
		gbc.gridy++;
		contentPane.add(caseReminder, gbc);

		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				settings.setCallReminder(callReminder.isSelected());
				settings.setTaskReminder(taskReminder.isSelected());
				settings.setLeadReminder(leadReminder.isSelected());
				settings.setOpportunityReminder(opportunityReminder.isSelected());
				settings.setCaseReminder(caseReminder.isSelected());

				DataStoreManager.writeSettings(settings);

				NotificationSettings.this.setVisible(false);
			}
		});
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.EAST;
		contentPane.add(save, gbc);

		pack();
		setResizable(false);
	}

}
