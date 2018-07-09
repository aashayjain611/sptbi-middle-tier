package org.aashay.spit.sptbi.Panelist;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.aashay.spit.sptbi.Database.MySql;
import org.aashay.spit.sptbi.Founder.Founder;
import org.aashay.spit.sptbi.Founder.FounderService;
import org.aashay.spit.sptbi.Startup.Startup;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * 
 * Remember 1 ResultSet for 1 Statement class
 * 
 * */

public class PanelistService {
	
	private MySql mysql=new MySql();
	
	/*
	 * function to divide the forms of each category among the panelists of that category
	 * */
	
	// get all the untouched forms (ie forms that have not reviewed even once)
	
	public ArrayList<Startup> getAllNewStartup(String username) 
	{
		ArrayList<Startup> list=new ArrayList<>(); // arraylist to store details of that form
		try 
		{
			String category="";
			int round=0;
			int start=0;// start, end have not been used because function to divide forms is not yet called
			int end=0;
			String query1="select category,round,start,end from panelists where username='"+username+"'"; // get details of the panelists
			System.out.println("\"PanelistService\": "+query1);
			Statement stmt1=mysql.connectToDatabase();
			ResultSet rs1=stmt1.executeQuery(query1);
			if(rs1.next())
			{
				category=rs1.getString(1);
				round=rs1.getInt(2);
				start=rs1.getInt(3);
				end=rs1.getInt(4);
			}
			ArrayList<Integer> temp=new ArrayList<>(); //arraylist of formids of that category
			String query2="select formid from form where category='"+category+"' order by formid asc"; // get forms based on their category
			Statement stmt2=mysql.connectToDatabase();
			System.out.println("\"PanelistService\": "+query2);
			ResultSet rs2=stmt2.executeQuery(query2);
			while(rs2.next())
				temp.add(rs2.getInt(1));
			for(int i=start;i<=end;i++) //  use this for loop after calling the function to divide no of forms
			{
				Statement stmt3=mysql.connectToDatabase();
				int formid=temp.get(i);
				String query3=""; // query to get new forms
				if(round>1)
					query3="select formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,"
							+ "differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,"
							+ "round"+round+",rating"+round+",note"+round+" from form where "
							+ "category='"+category+"' and round"+round+"='NEW' and round"+(round-1)+"='YES' and formid="
							+ formid;
				else
					query3="select formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,"
							+ "differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,"
							+ "round"+round+",rating"+round+",note"+round+" from form where formid="+formid
							+ " and category='"+category+"' and round"+round+"='NEW'";
				System.out.println("\"PanelistService\": "+query3);
				ResultSet rs3=stmt3.executeQuery(query3);
				if(rs3.next())
				{
					int form_id=rs3.getInt(1);
					ArrayList<Founder> founders=(new FounderService()).getFounderById(form_id);
					System.out.println("\"PanelistService\": "+"Founders for formid "+form_id+"=");
					for(Founder f:founders)
						System.out.println("\"PanelistService\": "+"Founder name: "+f.getName());
					list.add(new Startup(rs3.getInt(1),rs3.getString(2),rs3.getString(3),rs3.getString(4)
							,rs3.getInt(5),rs3.getString(6),rs3.getString(7),rs3.getString(8),rs3.getString(9),rs3.getString(10)
							,rs3.getString(11),rs3.getString(12),rs3.getString(13),rs3.getString(14),rs3.getString(15)
							,rs3.getInt(16),rs3.getString(17),founders));
				}
				stmt1.close();
				rs1.close();
				stmt2.close();
				rs2.close();
				stmt3.close();
				rs3.close();
				if(mysql.getConnection()!=null)
					mysql.getConnection().close();
			}
		} 
		catch (Exception e) 
		{
			System.out.println("\"PanelistService\": "+e);
		} 
		return list;
	}

	// function to update the status,ratings,notes for each form  
	
	public int postToDatabase(String json,String username)
	{
		System.out.println("\"PanelistService\": "+"postToDatabase");
		System.out.println("\"PanelistService\": "+json);
		try
		{
			Statement stmt=mysql.connectToDatabase();
			String query1="select round,category from panelists where username='"+username+"'"; // get round and category of that panelists
			ResultSet rs=stmt.executeQuery(query1);
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
				
				System.out.println("\"PanelistService\": "+"Form-ID is: "+jsonObject.getInt("formid"));
				System.out.println("\"PanelistService\": "+"Note for round"+round+" is: "+jsonObject.getString("note"));
				System.out.println("\"PanelistService\": "+"Status of round"+round+" is: "+jsonObject.getString("status"));
				System.out.println("\"PanelistService\": "+"Rating for round"+round+" is: "+jsonObject.getInt("rating"));
				
				// update query to update the status,ratings,notes for each form
				
				String update="update form set round"+round+"='"+jsonObject.getString("status").toUpperCase()+"'"
							+ " ,note"+round+"='"+jsonObject.getString("note").replace("\\", "\\"+"\\").replace("\'", "\\'").replace("\"", "\\"+"\"")+"' "
							+ ",rating"+round+"="+jsonObject.getInt("rating")+""
							+ " where formid="+jsonObject.getInt("formid")+" and category='"+category+"'";
				System.out.println("\"PanelistService\": "+update);
				stmt.executeUpdate(update);
				int selectionLimit=0;
				String query2="select selectionLimit from panelists where username='"+username+"'";
				System.out.println("\"PanelistService\": "+query2);
				Statement stmt1=mysql.connectToDatabase();
				ResultSet resultSet=stmt1.executeQuery(query2);
				if(resultSet.next())
					selectionLimit=resultSet.getInt(1);
				System.out.println("\"PanelistService\": "+jsonObject.getString("status").toUpperCase()+"\t"+jsonObject.getString("status").toUpperCase().equals("YES"));
				if(jsonObject.getString("status").toUpperCase().equals("YES"))
					selectionLimit--;
				System.out.println("\"PanelistService\": "+"Selection limit is: "+selectionLimit);
				Statement stmt3=mysql.connectToDatabase();
				String update2="update panelists set selectionLimit="+selectionLimit+" where username='"+username+"'";
				System.out.println("\"PanelistService\": "+"New limit="+selectionLimit);
				stmt3.executeUpdate(update2);
				resultSet.close();
				stmt3.close();
				stmt1.close();
				if(mysql.getConnection()!=null)
					mysql.getConnection().close();
			}
			rs.close();
			stmt.close();
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
			return 1;
		}
		catch(Exception e)
		{
			System.out.println("\"PanelistService\": "+e);
		}
		return 0;
	}

	public Panelist getSelectionLimit(String username) 
	{
		Panelist panelist=null;
		try
		{
			Statement stmt=mysql.connectToDatabase();
			String query="select selectionLimit from panelists where username='"+username+"'";
			System.out.println("\"PanelistService\": "+query);
			ResultSet rs=stmt.executeQuery(query);
			if(rs.next())
				panelist=new Panelist(rs.getInt(1));
			stmt.close();
			rs.close();
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
		}
		catch(Exception e)
		{
			System.out.println("\"PanelistService\": "+e);
		}
		return panelist;
	}
}
