package ClientSide;

import java.io.IOException;

import javax.jws.soap.SOAPBinding.Use;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ServerSide.DBManager;

public class LoginWindow extends JFrame {
	JLabel lbUsername = new JLabel("User: ");
	JLabel lbPassword = new JLabel("Password: ");
	JTextField tfUsername = new JTextField();
	JPasswordField pfPassword = new JPasswordField();
	JButton btnLogin = new JButton("Login");
	JButton btnReg = new JButton("Register");
	private DBManager db;
	private Client client;
	
	public LoginWindow(Client client)
	{
		init();
		this.client = client;
	}
	
	public void init()
	{
		setTitle("Login");
		setSize(300, 150);
		
		lbUsername.setBounds(10, 10, 80, 25);
		tfUsername.setBounds(100, 10, 160, 25);
		lbPassword.setBounds(10, 40, 80, 25);
		pfPassword.setBounds(100, 40, 160, 25);
		btnLogin.setBounds(10, 80, 80, 25);
		btnReg.setBounds(180, 80, 80, 25);
		
		add(lbUsername);
		add(lbPassword);
		add(tfUsername);
		add(pfPassword);
		add(btnLogin);
		add(btnReg);
		add(new JLabel());
		
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		btnLogin.addActionListener((e)->{
			String username = tfUsername.getText();
			String password = new String(pfPassword.getPassword());
			client.validate(username, password);
		});
	}
	
	public String getUsername()
	{
		return tfUsername.getText();
	}
	
	public void close()
	{
		dispose();
	}
	
	public static void main(String[] args) 
	{
		new LoginWindow(new Client());
	}
}
