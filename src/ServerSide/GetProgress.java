package ServerSide;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author eric
 * a servlet that sent all progresses to client basing on username
 */
public class GetProgress implements RequestHandle{
	
	DBManager conn;
	
	@Override
	public void init() 
	{
		conn = new DBManager();	
	}
	
	
	/**
	 * 
	 * created a list of map to received data from DBManager
	 * enumerated list, by which sent progress to client one by one
	 * @param detail a string containing username
	 */
	@Override
	public void process(String detail, PrintWriter out) 
	{
		String username = detail;
		List<Map<String, String>> datas = conn.getData(username);
		for(Map<String, String> singleData : datas){
			String time = singleData.get("updateDate")
							+ " " + singleData.get("updateTime").substring(0, 2)
							+ " : " + singleData.get("updateTime").substring(2, 4);
		
			out.println(time);
			
			int correct = Integer.parseInt(singleData.get("correct"));
			int incorrect = Integer.parseInt(singleData.get("incorrect"));
			out.println("correct: 	  " + correct);
			out.println("incorrect: " + incorrect);
			double rate = (double)correct/( correct + incorrect);
			out.println("Rate: 	      " + (int)(rate * 100) + "%");
			out.flush();
		}
		
	}
}
