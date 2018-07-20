package org.aashay.spit.sptbi.Founder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.aashay.spit.sptbi.Database.MySql;

/*
 * 
 * Remember 1 ResultSet for 1 PreparedStatement class
 * 
 * */

public class FounderService {
	
	private MySql mysql=new MySql();
	private Connection con=mysql.getConnection();
	private static final String TAG="\"FounderService\"";
	
	//function to get all founders of the startup 

	public ArrayList<Founder> getFounderById(int parseInt)
	{
		ArrayList<Founder> list=new ArrayList<>(); //arraylist to store details of founder
		try
		{
			//query to get details of founders
			
			String query="select founderName,founderEmail,founderContact from founders where formid="+parseInt;
			System.out.println(TAG+": "+query);
			PreparedStatement stmt=con.prepareStatement(query);
			ResultSet rs=stmt.executeQuery();
			while(rs.next())
				list.add(new Founder(rs.getString(1),rs.getString(2),rs.getLong(3)));
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return list;
	}
}
