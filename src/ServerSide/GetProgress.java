package ServerSide;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class GetProgress implements RequestHandle{
	
	DBManager conn;
	
	@Override
	public void init() 
	{
		conn = new DBManager();	
	}
	
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
