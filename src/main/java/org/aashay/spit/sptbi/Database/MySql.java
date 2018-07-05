package org.aashay.spit.sptbi.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//class that connects MySQL to middle tier

public class MySql {
	
	private Connection con=null;
	
	public Statement connectToDatabase()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sptbi","root","root");    
			Statement stmt=con.createStatement(); 
			return stmt;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
	
	public Connection getConnection()
	{
		return con;
	}

}
