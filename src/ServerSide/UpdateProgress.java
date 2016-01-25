package ServerSide;

import java.io.PrintWriter;

public class UpdateProgress implements RequestHandle{
	
	DBManager conn;
	
	@Override
	public void init() 
	{
		conn = new DBManager();	
	}
	
	@Override
	public void process(String detail, PrintWriter out) 
	{
		conn.update(detail);
	}
}
