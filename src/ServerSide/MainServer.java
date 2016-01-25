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

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

interface RequestHandle{
	void init();
	void process(String detail, PrintWriter out);
}

public class MainServer extends JFrame implements Runnable{
	// GUI
	DefaultListModel<String> dialog = new DefaultListModel<>();
	
	public MainServer() 
	{
		init();
		serverListen();
	}
	
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
	
	@Override
	public void run() 
	{
		while(true){
			try {
				Socket sock = server.accept();
				processMsg("Accept client: " + sock.getInetAddress() + ", prot: " + sock.getPort());
				new MyConnection(sock, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void processMsg(String msg)
    {
        String currentTime = LocalDateTime.now().toString();
        String revisedTime = currentTime.substring(0, currentTime.lastIndexOf("."));
        SwingUtilities.invokeLater(()->dialog.addElement(revisedTime + ":  " + msg) );
    }
	
	public static void main(String[] args) {
		new MainServer();
	}
}

class MyConnection extends Thread{
	Socket sock;
	MainServer server;
	BufferedReader in;
	PrintWriter out;
	
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