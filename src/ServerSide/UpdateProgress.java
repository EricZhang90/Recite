package ServerSide;

import java.io.PrintWriter;

/**
 * 
 * @author eric
 * update user current  progress to databse
 */
public class UpdateProgress implements RequestHandle{
	
	DBManager conn;
	
	@Override
	public void init() 
	{
		conn = new DBManager();	
	}
	
	/**
	 * @param detail a query containing full update statement
	 */
	@Override
	public void process(String detail, PrintWriter out) 
	{
		conn.update(detail);
	}
}
