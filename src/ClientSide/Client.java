package ClientSide;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Client extends JFrame implements Closeable {
	JButton btnStart = new JButton("Start");
	JButton btnConnect = new JButton("Connect");
	DefaultListModel<String> defaultListModle = new DefaultListModel<>();
	JList<String> dialog = new JList<>(defaultListModle);
	JButton show = new JButton("Show");
	boolean isLogged = false;
	
	public Client()
	{
		init();
	}
	
	public void init()
	{
		try {
			sock = new Socket("127.0.0.1", DEFAULT_PORT);
			in = new BufferedReader( new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter( new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Client
	public static final int DEFAULT_PORT = 8040;
	Socket sock;
	Thread thread;
	BufferedReader in;
	PrintWriter out;
	
	
	public void validate(String username, String password)
	{
		String require = "Validation$" + username + "$" + password;
		out.println(require);
		out.flush();
	}
	
	public void getProgress()
	{
		String require = "GetProgress$eric";
		out.println(require);
		out.flush();
	}
	
	public void update(String query)
	{
		String request = "UpdateProgress$" + query;
		out.println(request);
		out.flush();
	}
	
	public void getCurrentIndex(String username)
	{
		String request = "GetCurrentIndex$" + username;
		//System.out.println("Client->getCurrentIndex, request::" + request);
		out.flush();
		out.println(request);
		out.flush();
	}
	
	public BufferedReader getBufferedReader()
	{
		return in;
	}
	
	@Override
	public void close() throws IOException 
	{
		sock.close();
	}
}
