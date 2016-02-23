package net.combase.desktopcrm.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.combase.desktopcrm.data.AsteriskManager;
import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.data.DataStoreManager;
import net.combase.desktopcrm.domain.Settings;

public class CrmSettings extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6410315947750123167L;
	private JPanel contentPane;
	private JTextField sugarUrl;
	private JTextField login;
	private JPasswordField password;
	private JSpinner spinner;
	private JTextField asteriskManagerField;
	private JTextField asteriskPasswordField;
	private JTextField asteriskHostField;
	private JTextField extensionField;

	private JTextField dialUrlField;


	/**
	 * Create the frame.
	 */
	public CrmSettings() {
		setTitle("CRM Settings");
		setBounds(100, 100, 449, 420);
		setResizable(false);
		setIconImage(CrmIcons.SETTINGS.getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblC = new JLabel("Sugar URL");
		lblC.setBounds(15, 6, 130, 31);
		contentPane.add(lblC);
		
		sugarUrl = new JTextField();
		sugarUrl.setBounds(139, 12, 279, 25);
		contentPane.add(sugarUrl);
		sugarUrl.setColumns(10);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(15, 49, 70, 15);
		contentPane.add(lblLogin);
		
		login = new JTextField();
		login.setBounds(139, 49, 279, 25);
		contentPane.add(login);
		login.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(15, 86, 70, 15);
		contentPane.add(lblPassword);
		
		password = new JPasswordField();
		password.setText("sdfsdf");
		password.setBounds(139, 84, 279, 25);
		contentPane.add(password);
		
		JButton btnSave = new JButton("save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onSave();
			}
		});
		btnSave.setBounds(308, 347, 117, 25);
		contentPane.add(btnSave);
		

		Settings settings = DataStoreManager.getSettings();
		sugarUrl.setText(settings.getCrmUrl());
		login.setText(settings.getUser());
		password.setText(settings.getPassword());

		JLabel lblGmtOffset = new JLabel("GMT Offset");
		lblGmtOffset.setBounds(15, 118, 104, 15);
		contentPane.add(lblGmtOffset);


		spinner = new JSpinner();
		spinner.setBounds(139, 116, 279, 25);
		spinner.setValue(settings.getGmtOffset());

		contentPane.add(spinner);

		asteriskManagerField = new JTextField();
		asteriskManagerField.setBounds(139, 153, 279, 25);
		contentPane.add(asteriskManagerField);
		asteriskManagerField.setColumns(10);
		asteriskManagerField.setText(settings.getAsteriskUser());

		asteriskPasswordField = new JTextField();
		asteriskPasswordField.setBounds(139, 190, 279, 25);
		contentPane.add(asteriskPasswordField);
		asteriskPasswordField.setColumns(10);
		asteriskPasswordField.setText(settings.getAsteriskPassword());

		asteriskHostField = new JTextField();
		asteriskHostField.setBounds(139, 227, 279, 25);
		contentPane.add(asteriskHostField);
		asteriskHostField.setColumns(10);
		asteriskHostField.setText(settings.getAsteriskHost());

		JLabel lblAsteriskManager = new JLabel("Asterisk Manager");
		lblAsteriskManager.setBounds(15, 153, 130, 15);
		contentPane.add(lblAsteriskManager);

		JLabel lblPassword_1 = new JLabel("Password");
		lblPassword_1.setBounds(15, 190, 70, 15);
		contentPane.add(lblPassword_1);

		JLabel lblHost = new JLabel("Host");
		lblHost.setBounds(15, 227, 70, 15);
		contentPane.add(lblHost);

		extensionField = new JTextField();
		extensionField.setBounds(139, 264, 279, 25);
		contentPane.add(extensionField);
		extensionField.setColumns(10);
		extensionField.setText(settings.getAsteriskExtension());

		dialUrlField = new JTextField();
		dialUrlField.setBounds(139, 301, 279, 25);
		contentPane.add(dialUrlField);
		dialUrlField.setText(settings.getDialUrl());

		JLabel lblExtension = new JLabel("Extension");
		lblExtension.setBounds(15, 264, 70, 15);
		contentPane.add(lblExtension);

		JLabel dialUrl = new JLabel("Dial URL");
		dialUrl.setBounds(15, 301, 70, 15);
		contentPane.add(dialUrl);
	}


	protected void onSave() 
	{
		String url = sugarUrl.getText();
		String user = login.getText();
		String pwd = String.valueOf(password.getPassword());
		int offset = (int)spinner.getValue();
		String asteriskHost = asteriskHostField.getText();
		String asteriskUser = asteriskManagerField.getText();
		String asteriskPassword = asteriskPasswordField.getText();
		String asteriskExtension = extensionField.getText();
		String dialUrl = dialUrlField.getText();
		
		Settings settings = DataStoreManager.getSettings();
		settings.setCrmUrl(url);
		settings.setUser(user);
		settings.setPassword(pwd);
		settings.setGmtOffset(offset);
		settings.setAsteriskHost(asteriskHost);
		settings.setAsteriskExtension(asteriskExtension);
		settings.setAsteriskHost(asteriskHost);
		settings.setAsteriskPassword(asteriskPassword);
		settings.setAsteriskUser(asteriskUser);
		settings.setDialUrl(dialUrl);
		
		DataStoreManager.writeSettings(settings);
		
		if (asteriskHost != null && !asteriskHost.trim().isEmpty())
		{
			if (!AsteriskManager.setup())
			{
				JOptionPane.showMessageDialog(this, "The Asterisk connection failed!");
				return;
			}
		}

		if (!CrmManager.setup())
			JOptionPane.showMessageDialog(this, "The CRM connection failed!");
		else
			setVisible(false);
	}
}
