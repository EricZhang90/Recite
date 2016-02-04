package ServerSide;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * 
 * @author eric
 * each severlet should be implement this interface
 */
interface RequestHandle{
	void init();
	void process(String detail, PrintWriter out);
}

/**
 * 
 * @author eric
 * Main server which created connection between server and client
 */
public class MainServer extends JFrame implements Runnable{
	// GUI
	DefaultListModel<String> dialog = new DefaultListModel<>();
	
	/**
	 * inlized GUI and start to listen to port
	 */
	public MainServer() 
	{
		init();
		serverListen();
	}
	
	/**
	 * GUI inilization
	 */
	public void init()
	{
		setTitle("Sever");
        setSize(400, 300);
        add(new JScrollPane(new JList<String>(dialog) ), BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// Server
	public final static int DEFAULT_PORT = 8040;
	protected ServerSocket server;
	Thread thread;
	java.util.List<MyConnection> conections = Collections.synchronizedList(new ArrayList<MyConnection>());
	
	/**
	 * listen to port and created thread if no thread running
	 */
	public void serverListen()
	{
		while(true){
			try {
				server = new ServerSocket(DEFAULT_PORT);
				processMsg("Listen on port:" + DEFAULT_PORT);
			} catch (IOException e) {
				System.err.println("Cannot to create server");
				e.printStackTrace();
			}
			if(thread == null){
				thread = new Thread(this);
				thread.run();
			}
		}
	}
	
	/**
	 * accepted connection from client
	 */
	@Override
	public void run() 
	{
		while(true){
			try {
				Socket sock = server.accept();
				processMsg("Accept client: " + sock.getInetAddress() + ", prot: " + sock.getPort());
				conections.add(new MyConnection(sock, this));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * display message
	 * @param msg the message to be displayed in server's GUI
	 */
	public void processMsg(String msg)
    {
        String currentTime = LocalDateTime.now().toString();
        String revisedTime = currentTime.substring(0, currentTime.lastIndexOf("."));
        SwingUtilities.invokeLater(()->dialog.addElement(revisedTime + ":  " + msg) );
    }
	
	/**
	 * lanuch server
	 * @param args
	 */
	public static void main(String[] args) {
		new MainServer();
	}
}

/**
 * 
 * @author eric
 * recieved request from client, and dynamically create request handle by relection
 */
class MyConnection extends Thread{
	Socket sock;
	MainServer server;
	BufferedReader in;
	PrintWriter out;
	
	/**
	 * initlized input and output buffer.
	 * @param sock client 
	 * @param server main server
	 */
	public MyConnection(Socket sock, MainServer server) 
	{
		this.sock = sock;
		this.server = server;
		
		try{
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
		}catch (IOException e){
			System.err.println("IO error");
			e.printStackTrace();
			try {
				sock.close();
			} catch (IOException e1) {
				System.err.println("Client cannot be closed");
				e1.printStackTrace();
			}
		}
		this.start();
	}
	
	/**
	 * permanently received request from client and add each connection to list (it may be more useful in future)
	 * handled request by created corresponding object
	 */
	@Override
	public void run() {
		while(true){
		try {
			String clientQuest = in.readLine();
			if(clientQuest == null) return;

			String requestType = clientQuest.substring(0, clientQuest.indexOf("$"));
			String detail = clientQuest.substring(clientQuest.indexOf("$") + 1);
			
			String currentPackge = this.getClass().getPackage().toString();
			String packageName = currentPackge.substring(currentPackge.indexOf(" ")+1) + ".";
			
			RequestHandle requestHandle = (RequestHandle) Class.forName( packageName + requestType).newInstance();
			server.processMsg("Require type: " + requestType);
			requestHandle.init();
			requestHandle.process(detail, out);
			server.processMsg("Require completed\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println("cannot create class");
			e.printStackTrace();
		}
		}
	}
	
}
