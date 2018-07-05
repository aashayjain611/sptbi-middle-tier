package org.aashay.spit.sptbi.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.aashay.spit.sptbi.Database.MySql;
import org.aashay.spit.sptbi.Founder.Founder;
import org.aashay.spit.sptbi.Startup.Startup;

/*
 * 
 * Remember 1 ResultSet for 1 Statement class
 * 
 */

public class AdminService {
	
	private MySql mysql=new MySql();
	
	public int postToDatabase(Admin admin)
	{
		try
		{
			Statement stmt=mysql.connectToDatabase();
			String s1="";
			int id=getId(admin.getCategory());
			System.out.println(id);
			s1="insert into panelists (username,password,category,round,panelistno,selectionlimit) values"
					+ "('"+admin.getUserName()+"','"+admin.getPassword()+"','"+admin.getCategory().toUpperCase()+"'"
					+ ","+admin.getRound()+","+id+","+admin.getSelectionLimit()+")";
			stmt.executeUpdate(s1);
			System.out.println(s1);
			stmt.close();
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
			return 1;
		}
		catch(Exception e)
		{	
			System.out.println(e);
		}
		return -1;
	}

	public int getId(String category)
	{
		int id=0;
		int countPanelists=0;
		try
		{
			Statement stmt1=mysql.connectToDatabase();
			String query1="select count(username) from panelists where category='"+category+"'";
			System.out.println(query1);
			ResultSet rs1=stmt1.executeQuery(query1);
			if(rs1.next())
				countPanelists=rs1.getInt(1);
			id=countPanelists+1;
			Statement stmt2=mysql.connectToDatabase();
			ArrayList<Integer> panelistno=new ArrayList<>();
			String query2="select panelistno from panelists where category='"+category+"'";
			System.out.println(query2);
			ResultSet rs2=stmt2.executeQuery(query2);
			while(rs2.next())
				panelistno.add(rs2.getInt(1));
			while(panelistno.contains(id))
				id++;
			stmt1.close();
			stmt2.close();
			rs1.close();
			rs2.close();
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		return id;
	}
	
	public int removePanelist(String username) 
	{
		try
		{
			Statement stmt=mysql.connectToDatabase();
			String query="delete from panelists where username='"+username+"'";
			System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
			return 1;
		}
		catch(Exception e)
		{	
			System.out.println(e);
		}
		return -1;
	}
	
	public ArrayList<Admin> getAllPanelists()
	{
		ArrayList<Admin> list=new ArrayList<>();
		try 
		{
			Statement stmt=mysql.connectToDatabase();
			String query="select username,category,selectionLimit,round,password from panelists";
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next())
				list.add(new Admin(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5)));
			stmt.close();
			rs.close();
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<Startup> getFormAndPanelist()
	{
		ArrayList<Startup> list=new ArrayList<>();
		try
		{
			Statement stmt1=mysql.connectToDatabase();
			String query1="select username,category,round,start,end from panelists order by username asc";
			System.out.println(query1);
			ResultSet rs1=stmt1.executeQuery(query1);
			while(rs1.next())
			{
				String username=rs1.getString(1);
				String category=rs1.getString(2);
				int round=rs1.getInt(3);
				int start=rs1.getInt(4);
				int end=rs1.getInt(5);
				if(start!=-1 && end!=-1)
				{
					String query2="";
					if(round==1)
						query2="select formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,"
								+ "differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,"
								+ "round"+round+",rating"+round+",note"+round+" from form where" 
								+ " category='"+category+"' and round"+(round+1)+"='NEW' order by formid asc";
					else if(round==2)
						query2="select formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,"
								+ "differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,"
								+ "round"+round+",rating"+round+",note"+round+" from form where" 
								+ " category='"+category+"' and round"+(round-1)+"='YES' order by formid asc";
					System.out.println(query2);
					Statement stmt2=mysql.connectToDatabase();
					ResultSet rs2=stmt2.executeQuery(query2);
					ArrayList<Startup> forms=new ArrayList<>();
					while(rs2.next())
						forms.add(new Startup(rs2.getInt(1),rs2.getString(2),rs2.getString(3),rs2.getString(4)
								,rs2.getInt(5),rs2.getString(6),rs2.getString(7),rs2.getString(8),rs2.getString(9),rs2.getString(10)
								,rs2.getString(11),rs2.getString(12),rs2.getString(13),rs2.getString(14),rs2.getString(15)
								,rs2.getInt(16),rs2.getString(17)));
					for(int i=start;i<=end;i++)
					{
						ArrayList<Founder> founders=new ArrayList<>();
						String query3="select founderName,founderEmail,founderContact from founders where formid="+forms.get(i).getFormid();
						System.out.println(query3);
						Statement stmt3=mysql.connectToDatabase();
						ResultSet rs3=stmt3.executeQuery(query3);
						while(rs3.next())
							founders.add(new Founder(rs3.getString(1),rs3.getString(2),rs3.getLong(3)));
						
						System.out.println("Round is: "+round+"\nUsername is: "+username);
						list.add(new Startup(forms.get(i).getFormid(),forms.get(i).getStartupName(),forms.get(i).getLegalEntity(),forms.get(i).getDescription()
								,forms.get(i).getNoFounders(),forms.get(i).getPainPoint(),forms.get(i).getPrimaryCustomer(),forms.get(i).getCompetitors(),forms.get(i).getDifferentFromCompetitors(),forms.get(i).getMoneyModel()
								,forms.get(i).getWorkingIdea(),forms.get(i).getOperationalRevenue(),forms.get(i).getStartupIdea(),forms.get(i).getCategory(),forms.get(i).getStatus()
								,forms.get(i).getRating(),forms.get(i).getNote(),founders,username,round));
						stmt3.close();
						rs3.close();
					}
					stmt2.close();
					rs2.close();
				}
			}
			stmt1.close();
			rs1.close();
			System.out.println("No. of forms: "+list.size());
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}

	public int endRound(int round) 
	{
		try 
		{
			
			long timestamp=(new Date()).getTime();
			
			Statement stmt1=mysql.connectToDatabase();
			String query1="update admin set endRound"+round+"="+timestamp;
			stmt1.executeUpdate(query1);
			
			String query2="select username,category,selectionLimit,start,end from panelists where round="+round;
			System.out.println(query2);
			ResultSet rs2=stmt1.executeQuery(query2);
			while(rs2.next())
			{
				String username=rs2.getString(1);
				String category=rs2.getString(2);
				int selectionLimit=rs2.getInt(3);
				int start=rs2.getInt(4);
				int end=rs2.getInt(5);
				setLimits(username,category,round,selectionLimit,start,end);
			}
			
			stmt1.close();
			rs2.close();
			
			if(mysql.getConnection()!=null)
				mysql.getConnection().close();
			
			return 1;
		} 
		catch (SQLException e)
		{
			System.out.println(e);
		}
		
		return 0;
	}
	
	public void setLimits(String username, String category, int round, int selectionLimit, int start, int end) 
	{	
		ArrayList<String> usernames=new ArrayList<>();
		try 
		{
			int countForms=0;
			int countPanelists=0;
			String query1="select username from panelists where category='"+category+"' and round="+round+" order by username asc";
			System.out.println(query1);
			Statement stmt1=mysql.connectToDatabase();
			ResultSet rs1=stmt1.executeQuery(query1);
			while(rs1.next())
				usernames.add(rs1.getString(1));
			countPanelists=usernames.size();
			String query2="";
			if(round==1)
				query2="select count(formid) from form where category='"+category+"' and round1='NEW' and round"+(round+1)+"='NEW'";
			else if(round==2)
				query2="select count(formid) from form where category='"+category+"' and round1='YES' and round2='NEW'";
			System.out.println(query2);
			Statement stmt2=mysql.connectToDatabase();
			ResultSet rs2=stmt2.executeQuery(query2);
			if(rs2.next())
				countForms=rs2.getInt(1);
			if(countForms!=0 && countPanelists!=0)
			{
				System.out.println("No. of forms: "+countForms+"\nNo. of panelists: "+countPanelists);
				int index=usernames.indexOf(username);
				if (index!=-1) 
				{
					int n=countForms/countPanelists;
					start=index*n;
					if(index==countPanelists-1)
						end=countForms-1;
					else
						end=(index+1)*n-1;
					System.out.println("Start: "+start+"\nEnd: "+end);
					String query3 = "update panelists set start=" + start + ",end=" + end + " where " + "username='"
							+ username + "'";
					System.out.println(query3);
					Statement stmt3 = mysql.connectToDatabase();
					stmt3.executeUpdate(query3);
					stmt3.close();
				}
			}
			rs2.close();
			stmt2.close();
			rs1.close();
			stmt1.close();
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
	}
	
}