package ServerSide;

import java.io.PrintWriter;

public class Validation implements RequestHandle{
	DBManager conn;
	
	@Override
	public void init() 
	{
		conn = new DBManager();	
	}
	
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
