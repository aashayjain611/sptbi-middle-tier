package org.aashay.spit.sptbi.Admin;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.aashay.spit.sptbi.Startup.Startup;

@Path("/admin")
public class AdminResource {
	
private AdminService admin=new AdminService();
	

	@POST
	public int postToDatabase(Admin admins)
	{
		return admin.postToDatabase(admins);
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{username}")
	public int deleteMessage(@PathParam("username") String username) 
	{
		 return admin.removePanelist(username);
	}

	@GET
	@Path("/getPanelists")
	public ArrayList<Admin> getAllPanelists()
	{
		return admin.getAllPanelists();
	}
	
	@GET
	@Path("/getFormAndPanelist")
	public ArrayList<Startup> getFormAndPanelist()
	{
		return admin.getFormAndPanelist();
	}
	
	@GET
	public int endRound(@QueryParam("endRound") int round)
	{
		return admin.endRound(round);
	}
	
	@GET
	@Path("/getRoundStatus")
	public EndRoundStatus getEndRoundStatus()
	{
		return admin.getEndRoundStatus();
	}

}
