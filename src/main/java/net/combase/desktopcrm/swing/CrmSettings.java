package net.combase.desktopcrm.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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


	/**
	 * Create the frame.
	 */
	public CrmSettings() {
		setTitle("CRM Settings");
		setBounds(100, 100, 449, 198);
		setResizable(false);
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
		btnSave.setBounds(301, 128, 117, 25);
		contentPane.add(btnSave);
		

		Settings settings = DataStoreManager.getSettings();
		sugarUrl.setText(settings.getCrmUrl());
		login.setText(settings.getUser());
		password.setText(settings.getPassword());
	}


	protected void onSave() 
	{
		String url = sugarUrl.getText();
		String user = login.getText();
		String pwd = String.valueOf(password.getPassword());
		
		Settings settings = DataStoreManager.getSettings();
		settings.setCrmUrl(url);
		settings.setUser(user);
		settings.setPassword(pwd);
		
		DataStoreManager.writeSettings(settings);
		
		if (!CrmManager.setup())
			JOptionPane.showMessageDialog(this, "The CRM connection failed!");
		else
			setVisible(false);
	}
}
