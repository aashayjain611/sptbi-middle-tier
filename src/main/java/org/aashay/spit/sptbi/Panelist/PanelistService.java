package org.aashay.spit.sptbi.Panelist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.aashay.spit.sptbi.Database.MySql;
import org.aashay.spit.sptbi.Founder.Founder;
import org.aashay.spit.sptbi.Founder.FounderService;
import org.aashay.spit.sptbi.Startup.Startup;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * 
 * Remember 1 ResultSet for 1 PreparedStatement class
 * 
 * */

public final class PanelistService {
	
	private MySql mysql=new MySql();
	private Connection con=mysql.getConnection();
	private static final String TAG="\"PanelistService\"";
	
	// function to update the status,ratings,notes for each form  
	
	public int postToDatabase(String json,String username)
	{
		System.out.println(TAG+": "+"postToDatabase");
		System.out.println(TAG+": "+json);
		try
		{
			
			long endRound1=0;
			long endRound2=0;
			String query="select endRound1,endRound2 from admin";
			System.out.println(TAG+": "+query);
			PreparedStatement stmt2=con.prepareStatement(query);
			ResultSet rs2=stmt2.executeQuery();
			if(rs2.next())
			{
				endRound1=rs2.getLong(1);
				endRound2=rs2.getLong(2);
			}
			
			String query1="select round,category from panelists where username='"+username+"'"; // get round and category of that panelists
			PreparedStatement stmt=con.prepareStatement(query1);
			ResultSet rs=stmt.executeQuery();
			int round=0;
			String category="";
			if(rs.next())
			{
				round=rs.getInt(1);
				category=rs.getString(2);
			}
			
			// parsing the JSON received
			
			/*JSONObject jsonObj=new JSONObject(json);
			JSONArray ja=(JSONArray)jsonObj.get("forms");*/
			JSONArray ja=new JSONArray(json);
			int n=ja.length();
			for(int i=0;i<n;i++)
			{
				
				JSONObject jsonObject=ja.getJSONObject(i);
				
				// String is manipulated to prevent error caused by escape characters
				
				System.out.println(TAG+": "+"Form-ID is: "+jsonObject.getInt("formid"));
				System.out.println(TAG+": "+"Note for round"+round+" is: "+jsonObject.getString("note"));
				System.out.println(TAG+": "+"Status of round"+round+" is: "+jsonObject.getString("status"));
				System.out.println(TAG+": "+"Rating for round"+round+" is: "+jsonObject.getInt("rating"));
				
				// update query to update the status,ratings,notes for each form
				String update="";
				if((round==1 && endRound1!=0 && endRound2==0) || (round==2 && endRound1!=0 && endRound2!=0))
				{
					update="update form set round"+round+"='"+jsonObject.getString("status").toUpperCase()+"'"
							+ " ,note"+round+"='"+jsonObject.getString("note").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"' "
							+ ",rating"+round+"="+jsonObject.getInt("rating")+""
							+ " where formid="+jsonObject.getInt("formid")+" and category='"+category+"'";
					System.out.println(TAG+": "+update);
					stmt.executeUpdate(update);
					int selectionLimit=0;
					String query2="select selectionlimit from panelists where username='"+username+"'";
					System.out.println(TAG+": "+query2);
					PreparedStatement stmt1=con.prepareStatement(query2);
					ResultSet resultSet=stmt1.executeQuery();
					if(resultSet.next())
						selectionLimit=resultSet.getInt(1);
					System.out.println(TAG+": "+jsonObject.getString("status").toUpperCase()+"\t"+jsonObject.getString("status").toUpperCase().equals("YES"));
					if(jsonObject.getString("status").toUpperCase().equals("YES"))
						selectionLimit--;
					System.out.println(TAG+": "+"Selection limit is: "+selectionLimit);
					String update2="update panelists set selectionlimit="+selectionLimit+" where username='"+username+"'";
					System.out.println(TAG+": "+"New limit="+selectionLimit);
					PreparedStatement stmt3=con.prepareStatement(update2);
					stmt3.executeUpdate();
					resultSet.close();
					stmt3.close();
					stmt1.close();
				}
				
			}
			rs.close();
			stmt.close();
			stmt2.close();
			rs2.close();
			return 1;
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return 0;
	}

	public Panelist getSelectionLimit(String username) 
	{
		Panelist panelist=null;
		try
		{
			String query="select selectionlimit from panelists where username='"+username+"'";
			System.out.println(TAG+": "+query);
			PreparedStatement stmt=con.prepareStatement(query);
			ResultSet rs=stmt.executeQuery();
			if(rs.next())
				panelist=new Panelist(rs.getInt(1));
			stmt.close();
			rs.close();
			
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return panelist;
	}

	public ArrayList<Startup> getAllNewStartup(String username) 
	{
		ArrayList<Startup> newStartupForms=new ArrayList<>();
		try
		{
			long endRound1=0;
			long endRound2=0;
			String query1="select endRound1,endRound2 from admin";
			System.out.println(TAG+": "+query1);
			PreparedStatement stmt1=con.prepareStatement(query1);
			ResultSet rs1=stmt1.executeQuery();
			if(rs1.next())
			{
				endRound1=rs1.getLong(1);
				endRound2=rs1.getLong(2);
			}
			int round=0;
			String category="";
			String query2="select category,round from panelists where username='"+username+"'";
			System.out.println(TAG+": "+query2);
			PreparedStatement stmt2=con.prepareStatement(query2);
			ResultSet rs2=stmt2.executeQuery();
			if(rs2.next())
			{
				round=rs2.getInt("round");
				category=rs2.getString("category");
			}
			String query3="";
			if(round==1 && endRound1!=0 && endRound2==0)
				query3="select formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,"
						+ "differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,"
						+ "round"+round+",rating"+round+",note"+round+" from form where "
						+ "category='"+category+"' and round"+round+"='NEW' and round"+(round+1)+"='NEW' and panelist"+round+"='"+username+"'";
			else if(round==2 && endRound1!=0 && endRound2!=0)
				query3="select formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,"
						+ "differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,"
						+ "round"+round+",rating"+round+",note"+round+" from form where "
						+ "category='"+category+"' and round"+round+"='NEW' and round"+(round-1)+"='YES' and panelist"+round+"='"+username+"'";
			System.out.println(TAG+": "+query3);
			if(!query3.isEmpty())
			{
				PreparedStatement stmt3=con.prepareStatement(query3);
				ResultSet rs3=stmt3.executeQuery();
				FounderService founderService=new FounderService();
				while(rs3.next())
				{
					ArrayList<Founder> founders=founderService.getFounderById(rs3.getInt("formid"));
					newStartupForms.add(new Startup(rs3.getInt("formid"),rs3.getString("startupName"),rs3.getString("legalEntity"),
							rs3.getString("description"),rs3.getInt("noFounders"),rs3.getString("painPoint"),rs3.getString("primaryCustomer"),
							rs3.getString("competitors"),rs3.getString("differentFromCompetitors"),rs3.getString("moneyModel"),
							rs3.getString("workingIdea"),rs3.getString("operationalRevenue"),rs3.getString("startupIdea"),
							rs3.getString("category"),rs3.getString("round"+round),rs3.getInt("rating"+round),rs3.getString("note"+round),founders));
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return newStartupForms;
	}
}
