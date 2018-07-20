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
import org.aashay.spit.sptbi.Startup.Startup;


/*
 * 
 * Remember 1 ResultSet for 1 PreparedStatement class
 * 
 */

public class AdminService {
	
	private MySql mysql=new MySql();
	private Connection con=mysql.getConnection();
	private static final String TAG="\"AdminService\"";
	
	public int postToDatabase(Admin admin)
	{
		try
		{
			String s1="";
			int id=getId(admin.getCategory());
			System.out.println(TAG+": "+"ID: "+id);
			s1="insert into panelists (username,password,category,round,panelistno,selectionlimit) values"
					+ "('"+admin.getUserName()+"','"+admin.getPassword()+"','"+admin.getCategory().toUpperCase()+"'"
					+ ","+admin.getRound()+","+id+","+admin.getSelectionLimit()+")";
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
			
			if(admin.getRound()==1 && endRound1!=0 && endRound2==0)
				endRound(1,admin.getCategory());
			else if(admin.getRound()==2 && endRound1!=0 && endRound2!=0)
				endRound(2,admin.getCategory());
			
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

	public int getId(String category)
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
	
	public ArrayList<Admin> getAllPanelists()
	{
		ArrayList<Admin> list=new ArrayList<>();
		try 
		{
			String query="select username,category,selectionlimit,round,password from panelists";
			PreparedStatement stmt=con.prepareStatement(query);
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next())
				list.add(new Admin(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5)));
			stmt.close();
			rs.close();
		} 
		catch (Exception e) 
		{
			System.out.println(TAG+": "+e);
		}
		
		return list;
	}
	
//	public ArrayList<Startup> getFormAndPanelist()
//	{
//		ArrayList<Startup> list=new ArrayList<>();
//		ArrayList<String> allFormIds=new ArrayList<>();
//		ArrayList<String> allocatedFormids=new ArrayList<>();
//		try
//		{
//			String query="select formid from form";
//			PreparedStatement stmt=con.prepareStatement(query);
//			System.out.println(TAG+": "+query);
//			ResultSet rs=stmt.executeQuery(query);
//			while(rs.next())
//				allFormIds.add(String.valueOf(rs.getInt(1)));
//			
//			String query1="select username,category,round,start,end from panelists order by username asc";
//			PreparedStatement stmt1=con.prepareStatement(query1);
//			System.out.println(TAG+": "+query1);
//			ResultSet rs1=stmt1.executeQuery();
//			while(rs1.next())
//			{
//				String username=rs1.getString(1);
//				String category=rs1.getString(2);
//				int round=rs1.getInt(3);
//				int start=rs1.getInt(4);
//				int end=rs1.getInt(5);
//				if(start!=-1 && end!=-1)
//				{
//					String query2="";
//					if(round==1)
//						query2="select formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,"
//								+ "differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,"
//								+ "round"+round+",rating"+round+",note"+round+",timestamp from form where" 
//								+ " category='"+category+"' and round"+(round+1)+"='NEW' order by formid asc";
//					else if(round==2)
//						query2="select formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,"
//								+ "differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,"
//								+ "round"+round+",rating"+round+",note"+round+",timestamp from form where" 
//								+ " category='"+category+"' and round"+(round-1)+"='YES' order by formid asc";
//					System.out.println(TAG+": "+query2);
//					if(!query2.isEmpty())
//					{
//						PreparedStatement stmt2=con.prepareStatement(query2);
//						ResultSet rs2=stmt2.executeQuery();
//						System.out.println(TAG+": "+query2);
//						ArrayList<Startup> forms=new ArrayList<>();
//						while(rs2.next())
//						{
//							allocatedFormids.add(String.valueOf(rs2.getInt(1)));
//							forms.add(new Startup(rs2.getInt(1),rs2.getString(2),rs2.getString(3),rs2.getString(4)
//									,rs2.getInt(5),rs2.getString(6),rs2.getString(7),rs2.getString(8),rs2.getString(9),rs2.getString(10)
//									,rs2.getString(11),rs2.getString(12),rs2.getString(13),rs2.getString(14),rs2.getString(15)
//									,rs2.getInt(16),rs2.getString(17),dateFormatter(rs2.getLong(18))));
//						}
//						for(int i=start;i<=end;i++)
//						{
//							ArrayList<Founder> founders=new ArrayList<>();
//							String query3="select founderName,founderEmail,founderContact from founders where formid="+forms.get(i).getFormid();
//							System.out.println(TAG+": "+query3);
//							PreparedStatement stmt3=con.prepareStatement(query3);
//							ResultSet rs3=stmt3.executeQuery();
//							while(rs3.next())
//								founders.add(new Founder(rs3.getString(1),rs3.getString(2),rs3.getLong(3)));
//							
//							System.out.println(TAG+": "+"Round is: "+round+"\nUsername is: "+username);
//							list.add(new Startup(forms.get(i).getFormid(),forms.get(i).getStartupName(),forms.get(i).getLegalEntity(),forms.get(i).getDescription()
//									,forms.get(i).getNoFounders(),forms.get(i).getPainPoint(),forms.get(i).getPrimaryCustomer(),forms.get(i).getCompetitors(),forms.get(i).getDifferentFromCompetitors(),forms.get(i).getMoneyModel()
//									,forms.get(i).getWorkingIdea(),forms.get(i).getOperationalRevenue(),forms.get(i).getStartupIdea(),forms.get(i).getCategory(),forms.get(i).getStatus()
//									,forms.get(i).getRating(),forms.get(i).getNote(),founders,username,round,forms.get(i).getTimestamp()));
//							stmt3.close();
//							rs3.close();
//						}
//						stmt2.close();
//						rs2.close();
//					}
//					else
//						break;
//				}
//			}
//			
//			allFormIds.removeAll(allocatedFormids);
//			FounderService founderService=new FounderService();
//			for(String id:allFormIds)
//			{
//				ArrayList<Founder> founders=founderService.getFounderById(Integer.parseInt(id));
//				String query4="select formid,startupName,legalEntity,description,noFounders,painPoint,primaryCustomer,competitors,"
//						+ "differentFromCompetitors,moneyModel,workingIdea,operationalRevenue,startupIdea,category,round1,round2, "
//						+ "timestamp from form where formid="+id;
//				PreparedStatement stmt4=con.prepareStatement(query4);
//				ResultSet rs3=stmt4.executeQuery();
//				while(rs3.next())
//				{
//					int round=0;
//					if(rs3.getString(15).equals("NEW") && rs3.getString(16).equals("NEW"))
//						round=1;
//					else if(rs3.getString(15).equals("YES") && rs3.getString(16).equals("NEW"))
//						round=2;
//					
//					list.add(new Startup(rs3.getInt(1),rs3.getString(2),rs3.getString(3),rs3.getString(4),rs3.getInt(5),
//							rs3.getString(6),rs3.getString(7),rs3.getString(8),rs3.getString(9),rs3.getString(10),
//							rs3.getString(11),rs3.getString(12),rs3.getString(13),rs3.getString(14),"NEW",0,"NOT ASSIGNED",
//							founders,"NOT ASSIGNED",round,dateFormatter(rs3.getLong(15))));
//				}
//				stmt4.close();
//				rs3.close();
//				
//				/* 
//				 * public Startup(int formid, String startupName, String legalEntity, String description, int noFounders,
//			String painPoint, String primaryCustomer, String competitors, String differentFromCompetitors,
//			String moneyModel, String workingIdea, String operationalRevenue, String startupIdea, String category,
//			String status, int rating, String note,ArrayList<Founder> founders,String panelist,int round, long timstamp) 
//				 * 
//				 */
//				
//			}
//			
//			stmt1.close();
//			rs1.close();
//			System.out.println(TAG+": "+"No. of forms: "+list.size());
//			
//		}
//		catch(Exception e)
//		{
//			System.out.println(TAG+": "+e);
//		}
//		return list;
//	}
	
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

	public int endRound(int round,String categoryOfPanelist) 
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
				
				return 1;
			}
		} 
		catch (SQLException e)
		{
			System.out.println(TAG+": "+e);
		}
		
		return 0;
	}
	
	public void setLimits(String username, String category, int round, int selectionLimit) 
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

	public EndRoundStatus getEndRoundStatus() 
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
			
			return new EndRoundStatus(endRound1,endRound2);
		}
		catch (SQLException e)
		{
			System.out.println(TAG+": "+e);
		}
		
		return null;
	}

	public ArrayList<EndRoundStatus> getPanelistsWithPendingForms() 
	{
		ArrayList<EndRoundStatus> panelistNames=new ArrayList<>();
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
						panelistNames.add(new EndRoundStatus(username));
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
	
	public String dateFormatter(long timestamp)
	{
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(timestamp));
	}
	
	public int startRegistration()
	{
		try
		{
			String query1="delete from form";
			String query2="delete from panelists";
			String query3="delete from founders";
			String query4="delete from admin";
			return con.prepareStatement(query1).executeUpdate()+con.prepareStatement(query2).executeUpdate()+
					con.prepareStatement(query3).executeUpdate()+con.prepareStatement(query4).executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(TAG+": "+e);
		}
		return 0;
	}
	
}