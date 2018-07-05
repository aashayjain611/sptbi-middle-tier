package org.aashay.spit.sptbi.Login;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/login")
public class LoginResource {

private LoginService login=new LoginService();
	
	

	@POST
	public CheckUser checkDatabase(Login logins)
	{
		return login.checkDatabase(logins);
	}

	@GET
	public CheckUser getUser(@QueryParam("username") String username,@QueryParam("password") String password)
	{
		return login.checkDatabase(new Login(username,password));
	}
}
