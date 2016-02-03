package ClientSide;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * 
 * @author eric
 * used to sent client's requests to server
 */

public class Client implements Closeable {	
	public Client()
	{
		init();
	}
	
	/**
	 * made connection
	 */
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
	
	/**
	 * this method is used to sent valiadation request to server
	 * 
	 * it concatenated username and password by delimiter $ sign
	 * added request type at the beginning of above string.
	 * then sent the string qs request
	 * @param username 
	 * @param password
	 */
	public void validate(String username, String password)
	{
		String require = "Validation$" + username + "$" + Encrypt.md5(password);
		out.println(require);
		out.flush();
	}
	
	/**
	 * 
	 * asked server send back password
	 */
	public void getProgress()
	{
		String require = "GetProgress$eric";
		out.println(require);
		out.flush();
	}
	
	/**
	 * sent update request to server
	 * @param query update SQL
	 */
	public void update(String query)
	{
		String request = "UpdateProgress$" + query;
		out.println(request);
		out.flush();
	}
	
	/**
	 * specified username and asked server to
	 * return current index for the user
	 * 
	 * @param username
	 */
	public void getCurrentIndex(String username)
	{
		String request = "GetCurrentIndex$" + username;
		out.println(request);
		out.flush();
	}
	
	/**
	 * 
	 * @return return a buffer by which main program(MyRecite) 
	 * is able to get feedback from server
	 */
	public BufferedReader getBufferedReader()
	{
		return in;
	}
	
	/**
	 * disconection
	 */
	@Override
	public void close() throws IOException 
	{
		sock.close();
	}
}
