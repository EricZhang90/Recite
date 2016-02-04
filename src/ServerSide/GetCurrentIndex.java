package ServerSide;

import java.io.PrintWriter;

/**
 * 
 * @author eric
 * a servlet which sent the last index of user read
 */
public class GetCurrentIndex implements RequestHandle{
	DBManager conn;
	
	@Override
	public void init() 
	{
		conn = new DBManager();	
	}
	
	/**
	 * @param detail username
	 */
	@Override
	public void process(String detail, PrintWriter out) 
	{
		out.println(conn.getCurrentIndex(detail));
		out.flush();
	}

}
