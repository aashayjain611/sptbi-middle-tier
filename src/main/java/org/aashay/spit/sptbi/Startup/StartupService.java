package org.aashay.spit.sptbi.Startup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.aashay.spit.sptbi.Database.MySql;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * 
 * Remember 1 ResultSet for 1 PreparedStatement class
 * 
 * */

public class StartupService {
	
	private MySql mysql=new MySql();
	private Connection con=mysql.getConnection();
	private static final String TAG="\"StartupService\"";
	
	public int postToDatabase(String json)
	{
		System.out.println("Hello everyone");
		System.out.println(TAG+": "+json);
		try
		{
			
			String query2="select endRound1 from admin";
			System.out.println(TAG+": "+query2);
			PreparedStatement stmt=con.prepareStatement(query2);
			ResultSet rs=stmt.executeQuery();
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
				
				String query="insert into form(formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,round1,round2,timestamp,rating1,rating2,note1,note2) values("+formId+",'"+jsonObj.getString("startupName").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"
						+jsonObj.getString("legalEntity").toUpperCase()+"','"+jsonObj.getString("description").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"',"+jsonObj.getString("noFounders")
						+",'"+jsonObj.getString("painPoint").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+jsonObj.getString("primaryCustomer").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+jsonObj.getString("competitors").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"
						+jsonObj.getString("differentFromCompetitors").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+jsonObj.getString("moneyModel").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"
						+jsonObj.getString("workingIdea").toUpperCase()+"','"+jsonObj.getString("operationalRevenue").toUpperCase()+"','"
						+jsonObj.getString("startupIdea").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+jsonObj.getString("category").toUpperCase()+"','NEW','NEW',"+timestamp+",0,0,'','')";
				System.out.println(TAG+": "+query);
				stmt.executeUpdate(query);
				int n=Integer.parseInt(jsonObj.getString("noFounders"));
				System.out.println(TAG+": "+"No. of founders: "+n);
				
				JSONArray a=(JSONArray)jsonObj.get("founders");
				
				//parse the JSONArray and post the founder(s) to the database
				
				for(int i=0;i<n;i++)
				{
					System.out.println(TAG+": "+"Name is: "+a.getJSONObject(i).getString("founderName"));
					System.out.println(TAG+": "+"Contact no. is: "+a.getJSONObject(i).getString("founderContact"));
					System.out.println(TAG+": "+"Email-ID is: "+a.getJSONObject(i).getString("founderEmail"));
					int founderId=getId("founderid","founders");
					
					// query to post founder
					
					String query1="insert into founders values"
							+ "("+founderId+","+formId+",'"+a.getJSONObject(i).getString("founderName").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"','"+a.getJSONObject(i).getString("founderEmail").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"',"
							+(Long.parseLong(a.getJSONObject(i).getString("founderContact")))+")";
					System.out.println(TAG+": "+query1);
					PreparedStatement stmt1=con.prepareStatement(query1);
					stmt1.executeUpdate();
					stmt1.close();
				}
				stmt.close();
			}
		} 
		catch (Exception e) 
		{
			System.out.println(TAG+": "+e);
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
			String query="select "+idType+" from "+table; // get all formids or founderids 
			String query1="select count("+idType+") from "+table; //get the count of no. of forms or founders
			System.out.println(TAG+": "+query);
			System.out.println(TAG+": "+query1);
			PreparedStatement s=con.prepareStatement(query1);
			ResultSet result=s.executeQuery();
			if(result.next())
				id=result.getInt(1)+1;
			PreparedStatement stmt=con.prepareStatement(query);
			ResultSet rs=stmt.executeQuery();
			
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
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return id;
	}

	public CheckStartupName checkStartupName(String startupName) 
	{
		try
		{
			String query="select formid from form where startupName='"+startupName+"'";
			System.out.println(TAG+": "+query);
			PreparedStatement stmt=con.prepareStatement(query);
			ResultSet rs=stmt.executeQuery();
			if(rs.next())
			{
				stmt.close();
				rs.close();
				return new CheckStartupName("YES");
			}
			stmt.close();
			rs.close();
			return new CheckStartupName("NO");
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return null;
	}
}
