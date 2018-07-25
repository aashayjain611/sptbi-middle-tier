package org.aashay.spit.sptbi.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.aashay.spit.sptbi.Database.MySql;
import org.aashay.spit.sptbi.Founder.Founder;
import org.aashay.spit.sptbi.Founder.FounderService;
import org.aashay.spit.sptbi.Panelist.Panelist;
import org.aashay.spit.sptbi.Startup.Startup;


/*
 * 
 * Remember 1 ResultSet for 1 PreparedStatement class
 * 
 */

public final class AdminService {
	
	private MySql mysql=new MySql();
	private Connection con=mysql.getConnection();
	private static final String TAG="\"AdminService\"";
	
	public int postToDatabase(Panelist panelist)
	{
		try
		{
			String s1="";
			int id=getId(panelist.getCategory());
			System.out.println(TAG+": "+"ID: "+id);
			s1="insert into panelists (username,password,category,round,panelistno,selectionlimit) values"
					+ "('"+panelist.getUsername()+"','"+panelist.getPassword()+"','"+panelist.getCategory().toUpperCase()+"'"
					+ ","+panelist.getRound()+","+id+","+panelist.getSelectionLimit()+")";
			PreparedStatement stmt=con.prepareStatement(s1);
			stmt.executeUpdate();
			System.out.println(TAG+": "+s1);
			stmt.close();
			
			long endRound1=0;
			long endRound2=0;
			String query1="select endRound1,endRound2 from admin";
			PreparedStatement stmt1=con.prepareStatement(query1);
			System.out.println(TAG+": "+query1);
			ResultSet rs1=stmt1.executeQuery();
			if(rs1.next())
			{
				endRound1=rs1.getLong(1);
				endRound2=rs1.getLong(2);
			}
			
			if(panelist.getRound()==1 && endRound1!=0 && endRound2==0)
				endRound(1,panelist.getCategory());
			else if(panelist.getRound()==2 && endRound1!=0 && endRound2!=0)
				endRound(2,panelist.getCategory());
			
			stmt1.close();
			rs1.close();
			
			return 1;
		}
		catch(Exception e)
		{	
			System.out.println(TAG+": "+e);
		}
		
		return -1;
	}

	private int getId(String category)
	{
		int id=0;
		int countPanelists=0;
		try
		{
			String query1="select count(username) from panelists where category='"+category+"'";
			PreparedStatement stmt1=con.prepareStatement(query1);
			System.out.println(TAG+": "+query1);
			ResultSet rs1=stmt1.executeQuery();
			if(rs1.next())
				countPanelists=rs1.getInt(1);
			id=countPanelists+1;
			ArrayList<Integer> panelistno=new ArrayList<>();
			String query2="select panelistno from panelists where category='"+category+"'";
			PreparedStatement stmt2=con.prepareStatement(query2);
			System.out.println(TAG+": "+query2);
			ResultSet rs2=stmt2.executeQuery();
			while(rs2.next())
				panelistno.add(rs2.getInt(1));
			while(panelistno.contains(id))
				id++;
			stmt1.close();
			stmt2.close();
			rs1.close();
			rs2.close();
		} 
		catch (Exception e) 
		{
			System.out.println(TAG+": "+e);
		}
		
		return id;
	}
	
	public int removePanelist(String username) 
	{
		try
		{
			String category="";
			String query2="select category from panelists where username='"+username+"'";
			PreparedStatement stmt2=con.prepareStatement(query2);
			ResultSet rs2=stmt2.executeQuery();
			if(rs2.next())
				category=rs2.getString(1);
			
			String query="delete from panelists where username='"+username+"'";
			PreparedStatement stmt=con.prepareStatement(query);
			System.out.println(TAG+": "+query);
			stmt.executeUpdate();
			stmt.close();
			
			long endRound1=0;
			long endRound2=0;
			String query1="select endRound1,endRound2 from admin";
			PreparedStatement stmt1=con.prepareStatement(query1);
			System.out.println(TAG+": "+query1);
			ResultSet rs1=stmt1.executeQuery(query1);
			if(rs1.next())
			{
				endRound1=rs1.getLong(1);
				endRound2=rs1.getLong(2);
			}
			
			if(endRound1!=0 && endRound2==0)
				endRound(1,category);
			else if(endRound1!=0 && endRound2!=0)
				endRound(2,category);
			
			stmt1.close();
			rs1.close();
			stmt2.close();
			rs2.close();
			
			return 1;
		}
		catch(Exception e)
		{	
			System.out.println(TAG+": "+e);
		}
		
		return -1;
	}
	
	public ArrayList<Panelist> getAllPanelists()
	{
		ArrayList<Panelist> list=new ArrayList<>();
		try 
		{
			String query="select username,category,selectionlimit,round,password from panelists";
			PreparedStatement stmt=con.prepareStatement(query);
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next())
				list.add(new Panelist(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5)));
			stmt.close();
			rs.close();
		} 
		catch (Exception e) 
		{
			System.out.println(TAG+": "+e);
		}
		
		return list;
	}
	
	public ArrayList<Startup> getFormAndPanelist()
	{
		ArrayList<Startup> formAndPanelist=new ArrayList<>();
		try
		{
			String query="select * from form";
			System.out.println(TAG+": "+query);
			PreparedStatement stmt=con.prepareStatement(query);
			ResultSet rs=stmt.executeQuery();
			FounderService founderService=new FounderService();
			while(rs.next())
			{
				ArrayList<Founder> founders=founderService.getFounderById(rs.getInt("formid"));
				String round1Status=rs.getString("round1");
				String round2Status=rs.getString("round2");
				int round=0;
				if(round2Status.equals("NEW"))
				{
					round=1;
					formAndPanelist.add(new Startup(rs.getInt("formid"),rs.getString("startupName"),rs.getString("legalEntity"),
							rs.getString("description"),rs.getInt("noFounders"),rs.getString("painPoint"),rs.getString("primaryCustomer"),
							rs.getString("competitors"),rs.getString("differentFromCompetitors"),rs.getString("moneyModel"),
							rs.getString("workingIdea"),rs.getString("operationalRevenue"),rs.getString("startupIdea"),
							rs.getString("category"),rs.getString("round1"),rs.getInt("rating1"),rs.getString("note1"),founders,
							rs.getString("panelist1"),round,dateFormatter(rs.getLong("timestamp"))));
				}
				if(round1Status.equals("YES"))
				{
					round=2;
					formAndPanelist.add(new Startup(rs.getInt("formid"),rs.getString("startupName"),rs.getString("legalEntity"),
							rs.getString("description"),rs.getInt("noFounders"),rs.getString("painPoint"),rs.getString("primaryCustomer"),
							rs.getString("competitors"),rs.getString("differentFromCompetitors"),rs.getString("moneyModel"),
							rs.getString("workingIdea"),rs.getString("operationalRevenue"),rs.getString("startupIdea"),
							rs.getString("category"),rs.getString("round2"),rs.getInt("rating2"),rs.getString("note2"),founders,
							rs.getString("panelist2"),round,dateFormatter(rs.getLong("timestamp"))));
				}
				/* 
				 * public Startup(int formid, String startupName, String legalEntity, String description, int noFounders,
			String painPoint, String primaryCustomer, String competitors, String differentFromCompetitors,
			String moneyModel, String workingIdea, String operationalRevenue, String startupIdea, String category,
			String status, int rating, String note,ArrayList<Founder> founders,String panelist,int round, long timstamp) 
				 * 
				 */
			}
			stmt.close();
			rs.close();
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return formAndPanelist;
	}

	public void endRound(int round,String categoryOfPanelist) 
	{	
		try 
		{
			
			long endRound1=0;
			long endRound2=0;
			String query="select endRound1,endRound2 from admin";
			System.out.println(TAG+": "+query);
			PreparedStatement stmt=con.prepareStatement(query);
			ResultSet rs=stmt.executeQuery();
			if(rs.next())
			{
				endRound1=rs.getLong("endRound1");
				endRound2=rs.getLong("endRound2");
			}
			stmt.close();
			rs.close();
			
			if((round==2 && endRound1!=0 && endRound2==0) || (endRound1==0 && endRound2==0 && round==1))
			{
				long timestamp=(new Date()).getTime();
				
				String query1="update admin set endRound"+round+"="+timestamp;
				PreparedStatement stmt1=con.prepareStatement(query1);
				stmt1.executeUpdate(query1);
				
				String query2="";
				if(categoryOfPanelist==null)
					query2="select username,category,selectionlimit from panelists where round="+round;
				else
					query2="select username,category,selectionlimit from panelists where round="+round+" and category='"+categoryOfPanelist+"'";
				System.out.println(TAG+": "+query2);
				PreparedStatement stmt2=con.prepareStatement(query2);
				ResultSet rs2=stmt2.executeQuery();
				while(rs2.next())
				{
					String username=rs2.getString(1);
					String category=rs2.getString(2);
					int selectionLimit=rs2.getInt(3);
					setLimits(username,category,round,selectionLimit);
				}
				
				stmt1.close();
				stmt2.close();
				rs2.close();
				
			}
		} 
		catch (SQLException e)
		{
			System.out.println(TAG+": "+e);
		}
		
	}
	
	private void setLimits(String username, String category, int round, int selectionLimit) 
	{	
		
		ArrayList<String> usernames=new ArrayList<>();
		try 
		{
			long endRound1=0;
			long endRound2=0;
			String query4="select endRound1,endRound2 from admin";
			System.out.println(TAG+": "+query4);
			PreparedStatement stmt4=con.prepareStatement(query4);
			ResultSet rs4=stmt4.executeQuery();
			if(rs4.next())
			{
				endRound1=rs4.getLong("endRound1");
				endRound2=rs4.getLong("endRound2");
			}
			stmt4.close();
			rs4.close();
			int countForms=0;
			int countPanelists=0;
			String query1="select username from panelists where category='"+category+"' and round="+round+" order by username asc";
			System.out.println(TAG+": "+query1);
			PreparedStatement stmt1=con.prepareStatement(query1);
			ResultSet rs1=stmt1.executeQuery();
			while(rs1.next())
				usernames.add(rs1.getString(1));
			countPanelists=usernames.size();
			String query2="";
			if(round==1 && endRound1!=0 && endRound2==0)
				query2="select formid from form where category='"+category+"' and round1='NEW' and round"+(round+1)+"='NEW' order by formid asc";
			else if(round==2 && endRound1!=0 && endRound2!=0)
				query2="select formid from form where category='"+category+"' and round1='YES' and round2='NEW' order by formid asc";
			System.out.println(TAG+": "+query2);
			ArrayList<Integer> formids=new ArrayList<>();
			PreparedStatement stmt2=con.prepareStatement(query2);
			ResultSet rs2=stmt2.executeQuery();
			while(rs2.next())
				formids.add(rs2.getInt(1));
			countForms=formids.size();
			if(countForms!=0 && countPanelists!=0)
			{
				System.out.println(TAG+": "+"No. of forms: "+countForms+"\nNo. of panelists: "+countPanelists);
				int index=usernames.indexOf(username);
				if (index!=-1) 
				{
					int n=countForms/countPanelists;
					int start=index*n;
					int end=0;
					if(index==countPanelists-1)
						end=countForms-1;
					else
						end=(index+1)*n-1;
					System.out.println(TAG+": "+"Start: "+start+"\nEnd: "+end);
					for (int i = start; i <=end; i++) 
					{
						String query3 = "update form set panelist"+round+"='"+username+ "' where " + "formid="+formids.get(i);
						System.out.println(TAG + ": " + query3);
						PreparedStatement stmt3 = con.prepareStatement(query3);
						stmt3.executeUpdate();
						stmt3.close();
					}
				}
			}
			rs2.close();
			stmt2.close();
			rs1.close();
			stmt1.close();
		} 
		catch (Exception e) 
		{
			System.out.println(TAG+": "+e);
		}
		
	}

	public Admin getEndRoundStatus() 
	{
		try 
		{
			String endRound1="";
			String endRound2="";
			String query="select endRound1,endRound2 from admin";
			PreparedStatement stmt=con.prepareStatement(query);
			System.out.println(TAG+": "+query);
			ResultSet rs=stmt.executeQuery();
			if(rs.next())
			{
				if(rs.getLong(1)==0)
					endRound1="ONGOING";
				else
					endRound1="END";
				if(rs.getLong(2)==0)
					endRound2="ONGOING";
				else
					endRound2="END";
			}
			
			stmt.close();
			rs.close();
			
			return new Admin(endRound1,endRound2);
		}
		catch (SQLException e)
		{
			System.out.println(TAG+": "+e);
		}
		
		return null;
	}

	public ArrayList<Admin> getPanelistsWithPendingForms() 
	{
		ArrayList<Admin> panelistNames=new ArrayList<>();
		System.out.println("hello guys");
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
				endRound1=rs1.getLong("endRound1");
				endRound2=rs1.getLong("endRound2");
			}
			stmt1.close();
			rs1.close();
			if(endRound1!=0 && endRound2==0)
			{
				String query2="select username,category from panelists where round=1";
				System.out.println(TAG+": "+query2);
				PreparedStatement stmt2=con.prepareStatement(query2);
				ResultSet rs2=stmt2.executeQuery();
				while(rs2.next())
				{
					String username=rs2.getString("username");
					String category=rs2.getString("category");
					String query3="select formid from form where panelist1='"+username+"' and category='"+category+"' and "
							+ "round1='NEW' and round2='NEW'";
					System.out.println(TAG+": "+query3);
					PreparedStatement stmt3=con.prepareStatement(query3);
					ResultSet rs3=stmt3.executeQuery();
					if(rs3.next())
						panelistNames.add(new Admin(username));
					stmt3.close();
					rs3.close();
				}
				stmt2.close();
				rs2.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return panelistNames;
	}
	
	private String dateFormatter(long timestamp)
	{
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(timestamp));
	}
	
	public int startRegistration()
	{
		try
		{
			String query[]= {"delete from form","delete from panelists","delete from founders","delete from admin"};
			PreparedStatement stmt[]=new PreparedStatement[query.length];
			for(int i=0;i<query.length;i++)
			{
				System.out.println(TAG+": "+query[i]);
				stmt[i]=con.prepareStatement(query[i]);
				stmt[i].executeUpdate();
				stmt[i].close();
			}
			return 1;
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return 0;
	}
	
}