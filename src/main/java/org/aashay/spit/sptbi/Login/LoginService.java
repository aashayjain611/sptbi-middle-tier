package org.aashay.spit.sptbi.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.aashay.spit.sptbi.Database.MySql;


public final class LoginService {
	
	private MySql mysql=new MySql();
	private Connection con=mysql.getConnection();
	private static final String TAG="\"LoginService\"";

	public CheckUser checkDatabase(Login login)
	{
		String username=login.getUsername();
		String password=login.getPassword();
		String category="";
		int round=0;
		try
		{
			String query1="select category,round from panelists where username='"+username+"' and password='"+password+"'";
			System.out.println(TAG+": "+query1);
			PreparedStatement stmt1=con.prepareStatement(query1);
			ResultSet rs1=stmt1.executeQuery();
			if(rs1.next())
			{
				category=rs1.getString(1);
				round=rs1.getInt(2);
			}
			stmt1.close();
			rs1.close();
		} 
		catch (Exception e) 
		{
			System.out.println(TAG+": "+e);
		}
		if(username.equals("Anukrit") && password.equals("Anukritjain"))
		{
			try
			{
				
				String query2="select username from admin";
				System.out.println(TAG+": "+query2);
				PreparedStatement stmt2=con.prepareStatement(query2);
				ResultSet rs2=stmt2.executeQuery();
				if(!rs2.next())
				{
					String query3="insert into admin(username,password) values('"+username+"','"+password+"')";
					PreparedStatement stmt3=con.prepareStatement(query3);
					stmt3.executeUpdate();
					System.out.println(TAG+": "+query3);
					stmt3.close();
				}
				stmt2.close();
				rs2.close();
			} 
			catch (Exception e)
			{
				System.out.println(TAG+": "+e);
			}
			return new CheckUser(3,"null","Anukrit");
		}
		else if(round!=0)
			return new CheckUser(round,category,username);
		return new CheckUser(4,"null","null");
	}

}
