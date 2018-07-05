package org.aashay.spit.sptbi.Founder;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.aashay.spit.sptbi.Database.MySql;

/*
 * 
 * Remember 1 ResultSet for 1 Statement class
 * 
 * */

public class FounderService {
	
	private MySql mysql=new MySql();
	
	//function to get all founders of the startup 

	public ArrayList<Founder> getFounderById(int parseInt)
	{
		ArrayList<Founder> list=new ArrayList<>(); //arraylist to store details of founder
		try
		{
			Statement stmt=mysql.connectToDatabase();
			
			//query to get details of founders
			
			String query="select founderName,founderEmail,founderContact from founders where formid='"+parseInt+"'";
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next())
				list.add(new Founder(rs.getString(1),rs.getString(2),rs.getLong(3)));
			rs.close();
			stmt.close();
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}
}
