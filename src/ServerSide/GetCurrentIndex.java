package ServerSide;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetCurrentIndex implements RequestHandle{
	DBManager conn;
	
	@Override
	public void init() 
	{
		conn = new DBManager();	
	}
	
	@Override
	public void process(String detail, PrintWriter out) 
	{
		out.println(conn.getCurrentIndex(detail));
		out.flush();
	}

}
