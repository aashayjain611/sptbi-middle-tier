package org.aashay.spit.sptbi.Database;

import java.sql.Connection;
import java.sql.DriverManager;

//class that connects MySQL to middle tier
//uses Connection pooling

public final class MySql {
	
	private static Connection dataSource=null;
	private static final String TAG="MySql";
	
	private static Connection connectToDatabase()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sptbi","root","root");    
			return con;
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return null;
	}
	
	public Connection getConnection()
	{
		if(dataSource==null)
			dataSource=connectToDatabase();
			 
		return dataSource;
	}

}