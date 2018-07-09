package org.aashay.spit.sptbi.Startup;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.aashay.spit.sptbi.Database.MySql;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * 
 * Remember 1 ResultSet for 1 Statement class
 * 
 * */

public class StartupService {
	
	private MySql mysql=new MySql();
	
	public int postToDatabase(String json)
	{
		System.out.println("\"StartupService\": "+json);
		try
		{
			Statement stmt=mysql.connectToDatabase();
			String query2="select endRound1 from admin";
			System.out.println("\"StartupService\": "+query2);
			ResultSet rs=stmt.executeQuery(query2);
			long time=0;
			if(rs.next())
				time=rs.getLong(1);
			if(time==0)
			{
				int formId=getId("formid","form");
				JSONObject jsonObj=new JSONObject(json);
				
				// get the current timestamp
				
				long timestamp=(new Date()).getTime();
				
				// query to post the form
				// string manipulated to prevent error caused by escape characters
				
				String query="insert into form values("+formId+",'"+jsonObj.getString("startupName").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"
						+jsonObj.getString("legalEntity").toUpperCase()+"','"+jsonObj.getString("description").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"',"+jsonObj.getString("noFounders")
						+",'"+jsonObj.getString("painPoint").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+jsonObj.getString("primaryCustomer").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+jsonObj.getString("competitors").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"
						+jsonObj.getString("differentFromCompetitors").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+jsonObj.getString("moneyModel").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"
						+jsonObj.getString("workingIdea").toUpperCase()+"','"+jsonObj.getString("operationalRevenue").toUpperCase()+"','"
						+jsonObj.getString("startupIdea").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+jsonObj.getString("category").toUpperCase()+"','NEW','NEW',"+timestamp+",0,0,'','')";
				System.out.println("\"StartupService\": "+query);
				stmt.executeUpdate(query);
				int n=Integer.parseInt(jsonObj.getString("noFounders"));
				System.out.println("\"StartupService\": "+"No. of founders: "+n);
				
				JSONArray a=(JSONArray)jsonObj.get("founders");
				
				//parse the JSONArray and post the founder(s) to the database
				
				for(int i=0;i<n;i++)
				{
					System.out.println("\"StartupService\": "+"Name is: "+a.getJSONObject(i).getString("founderName"));
					System.out.println("\"StartupService\": "+"Contact no. is: "+a.getJSONObject(i).getString("founderContact"));
					System.out.println("\"StartupService\": "+"Email-ID is: "+a.getJSONObject(i).getString("founderEmail"));
					int founderId=getId("\"StartupService\": "+"founderid","founders");
					
					// query to post founder
					
					String query1="insert into founders values"
							+ "("+founderId+","+formId+",'"+a.getJSONObject(i).getString("founderName").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+a.getJSONObject(i).getString("founderEmail").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"',"
							+(Long.parseLong(a.getJSONObject(i).getString("founderContact")))+")";
					System.out.println("\"StartupService\": "+query1);
					stmt.executeUpdate("\"StartupService\": "+query1);
				}
				stmt.close();
				if(mysql.getConnection()!=null)
					mysql.getConnection().close();
			}
		} 
		catch (Exception e) 
		{
			System.out.println("\"StartupService\": "+e);
		}
		return 0;
	}
	
	// function to generate formid and founderid for a form

	private int getId(String idType,String table) 
	{
		ArrayList<Integer> list=new ArrayList<>(); //arraylist to store all the existing formid or founderid
		int id=0;
		try
		{
			Statement stmt=mysql.connectToDatabase();
			String query="select "+idType+" from "+table; // get all formids or founderids 
			String query1="select count("+idType+") from "+table; //get the count of no. of forms or founders
			System.out.println("\"StartupService\": "+query);
			System.out.println("\"StartupService\": "+query1);
			Statement s=mysql.connectToDatabase();
			ResultSet result=s.executeQuery(query1);
			if(result.next())
				id=result.getInt(1)+1;
			ResultSet rs=stmt.executeQuery(query);
			
			// get all the existing formids or founderid
			
			while(rs.next())
				list.add(rs.getInt(1));
			
			//increment the generated formid or founderid if it exists
			
			while(list.contains(id))
				id++;
			stmt.close();
			s.close();
			result.close();
			rs.close();
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
		}
		catch(Exception e)
		{
			System.out.println("\"StartupService\": "+e);
		}
		return id;
	}
}
