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

/**
 * 
 * @author eric
 * database manager, connect to MySQL, handled query
 */
public class DBManager implements Closeable{
	private Connection conn;
	private Statement statement;
	private ResultSet rs;
	
	public DBManager()
	{
		init();
	}
	
 /* 
 * user profile:
 * create table userProfile( 
 * username char (50) primary key, 
 * password char (40) not null, 
 * hint char (30));
 * 
 * user's daily accomplishment: 
 * create table accomplishment( 
 * username char (50), 
 * updateDate date, 
 * updateTime mediumint (6) unsigned not null,
 * correct mediumint (4) unsigned not null, 
 * incorrect mediumint (4) unsigned not null, 
 * currentIndex mediumint (4) unsigned not null,
 * primary key (username, updateDate, updateTime),  
 * FOREIGN KEY (username) references userprofile (username));
 */	
 
 	/**
	 * initlized drive and connect to database
	 */
	public void init()
	{
		try {
			/* I use MySQL, and user Information is private, so I use HOSTNAME, USERNAME 
			 * instead of real login information
			 * database table is showed upon.
			 */
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://HOSTNAME", "USERNAME", "PASSWORD");
			statement = conn.createStatement();
			statement.executeUpdate("use DATABASE");
		} catch (SQLException e) {
			System.err.println("Cannot connect to database");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		}	
	}
	
	/**
	 * update table
	 * @param query 
	 */
	public void update(String query)
	{
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.err.println("Cannot complete query: " + query);
			e.printStackTrace();
		}
	}
	
	/**
	 * basing on username, got password by select statment 
	 * @param username
	 * @return password
	 */
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
	
	/**
	 * get user progress and analyzed data
	 * @param username
	 * @return a list which contains a map where keys and values are paired
	 */
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
	
	/**
	 * according to username, return current index by a select satatment
	 * @param username
	 * @return current index
	 */
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
	
		
	/**
	 * close conncetion
	 */
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
