package org.aashay.spit.sptbi.Login;

import java.sql.ResultSet;
import java.sql.Statement;

import org.aashay.spit.sptbi.Database.MySql;


public class LoginService {

	private MySql mysql=new MySql();
	
	public CheckUser checkDatabase(Login login)
	{
		String username=login.getUsername();
		String password=login.getPassword();
		String category="";
		int round=0;
		Statement stmt1=mysql.connectToDatabase();
		try
		{
			String query1="select category,round from panelists where username='"+username+"' and password='"+password+"'";
			System.out.println(query1);
			ResultSet rs1=stmt1.executeQuery(query1);
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
			System.out.println(e);
		}
		if(username.equals("Anukrit") && password.equals("Anukritjain"))
		{
			try
			{
				Statement stmt2=mysql.connectToDatabase();
				String query2="select username from admin";
				System.out.println(query2);
				ResultSet rs2=stmt2.executeQuery(query2);
				if(!rs2.next())
				{
					Statement stmt3=mysql.connectToDatabase();
					String query3="insert into admin(username,password) values('"+username+"','"+password+"')";
					stmt3.executeUpdate(query3);
					System.out.println(query3);
					stmt3.close();
				}
				stmt2.close();
				rs2.close();
				if(mysql.getConnection()!=null)
					mysql.getConnection().close();
			} 
			catch (Exception e)
			{
				System.out.println(e);
			}
			return new CheckUser(3,"null","Anukrit");
		}
		else if(round!=0)
			return new CheckUser(round,category,username);
		return new CheckUser(4,"null","null");
	}

}
