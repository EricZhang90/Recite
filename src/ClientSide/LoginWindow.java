package ClientSide;

import java.awt.Desktop;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * 
 * @author eric
 * created Login window 
 * 
 */

public class LoginWindow extends JFrame {
	JLabel lbUsername = new JLabel("User: ");
	JLabel lbPassword = new JLabel("Password: ");
	JTextField tfUsername = new JTextField();
	JPasswordField pfPassword = new JPasswordField();
	JButton btnLogin = new JButton("Login");
	JButton btnReg = new JButton("Register");
	JButton btnResetPw = new JButton("Forget password?");
	private Client client;
	
	/**
	 * started to initialize GUI and get connection from client
	 * @param client the connection between client and server 
	 */
	public LoginWindow(Client client)
	{
		init();
		this.client = client;
	}
	
	
	/**
	 * GUI initilization
	 */
	public void init()
	{
		setTitle("Login");
		setSize(300, 170);
		
		lbUsername.setBounds(10, 10, 80, 25);
		tfUsername.setBounds(100, 10, 160, 25);
		lbPassword.setBounds(10, 40, 80, 25);
		pfPassword.setBounds(100, 40, 160, 25);
		btnLogin.setBounds(10, 80, 80, 25);
		btnReg.setBounds(180, 80, 80, 25);
		btnResetPw.setBounds(10, 110, 150, 25);
		
		add(lbUsername);
		add(lbPassword);
		add(tfUsername);
		add(pfPassword);
		add(btnLogin);
		add(btnReg);
		add(btnResetPw);
		add(new JLabel());
		
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		btnLogin.addActionListener((e)->{
			String username = tfUsername.getText();
			String password = new String(pfPassword.getPassword());
			client.validate(username, password);
		});
		
		btnReg.addActionListener(e->openWebPage("https://zenit.senecac.on.ca/~int322_153b06/"));
		
		btnResetPw.addActionListener(e->openWebPage("https://zenit.senecac.on.ca/~int322_153b06/php/getPassword.php"));
	}

	/**
	 * 
	 * lanuch brower and open specific web page
	 * @param url the web address user is about to open 
	 */
	public void openWebPage(String url) {
		try {
			URL registerPage = new URL(url);
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
				desktop.browse(registerPage.toURI());
			}
		} catch (URISyntaxException e2) {
		    e2.printStackTrace();
		} catch (Exception e3){
			e3.printStackTrace();
		}
	}
	
	public String getUsername()
	{
		return tfUsername.getText();
	}
	
	/**
	 * close this window
	 */
	public void close()
	{
		dispose();
	}
}
