package ServerSide;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DBManager implements Closeable{
	private Connection conn;
	private Statement statement;
	private ResultSet rs;
	
	public DBManager()
	{
		init();
	}
	
	public void init()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://zenit.senecac.on.ca", "int322_153b06", "fpTW6548");
			statement = conn.createStatement();
			statement.executeUpdate("use int322_153b06");
		} catch (SQLException e) {
			System.err.println("Cannot connect to database");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		}	
	}
	
	public void update(String query)
	{
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.err.println("Cannot complete query: " + query);
			e.printStackTrace();
		}
	}
	
	public String validate(String username)
	{
		String password = "";
		String query = "select password from userProfile where username='" + username + "';";
		try {
			rs = statement.executeQuery(query);
			if(rs.next())
				password = rs.getString("password");
		} catch (SQLException e) {
			e.printStackTrace();
			password = "incorrect password or username";
		}
		return password;
	}
	
	public List<Map<String, String>> getData(String username)
	{
		List<Map<String, String>> datas = new LinkedList<>();
		
		String query = "select * from accomplishment where username='" + username + "'";
		
		try {
			rs = statement.executeQuery(query);
			while(rs.next()){
				Map<String, String> singleData = new TreeMap<>();
				singleData.put("updateDate", rs.getString("updateDate"));
				singleData.put("updateTime", rs.getString("updateTime"));
				singleData.put("correct", rs.getString("correct"));
				singleData.put("incorrect", rs.getString("incorrect"));
				singleData.put("currentIndex", rs.getString("currentindex"));
				datas.add(singleData);
			}
		} catch (SQLException e) {
			System.err.println("Cannot get data from query: " + query);
			e.printStackTrace();
		}

		return datas;
	}
	
	public int getCurrentIndex(String username){
		int currentIndex = 0;
		String query = "select currentIndex "
					 + "from accomplishment "
				     + "where username = '" +username + "' "
				     + "order by updateDate desc, updateTime desc "
				     + "limit 1;";
		try {
			rs = statement.executeQuery(query);
			if(rs.next())
				currentIndex = Integer.parseInt(rs.getString("currentIndex"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currentIndex;
	}
	
	@Override
	public void close() throws IOException 
	{
		try {
			conn.close();
		} catch (SQLException e) {
			System.err.println("Cannot disconnect");
			e.printStackTrace();
		}
	}
}