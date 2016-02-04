package ServerSide;

import java.io.PrintWriter;

/**
 * 
 * @author eric
 * a servlet which sent password valiation result to client
 */
public class Validation implements RequestHandle{
	DBManager conn;
	
	@Override
	public void init() 
	{
		conn = new DBManager();	
	}
	
	/**
	 * 
	 * connect to database by DBManager, and compared correct password and the password client side sent to server side
	 * sent a signal char "T" if password is valid, otherwise sent a signal char "F"
	 * @param detail a string containing username and password by order and has a delimiter & sign
	 */
	@Override
	public void process(String detail, PrintWriter out) 
	{
		String username = detail.substring(0, detail.indexOf("$"));
		String password = detail.substring(detail.indexOf("$") + 1);

		String correctPw = conn.validate(username);
		
		if(correctPw.equals(password) == true)
			out.println("T");
		else{
			out.println("F");
		}
		out.flush();
	}
}
