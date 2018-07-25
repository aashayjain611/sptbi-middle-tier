package org.aashay.spit.sptbi.Admin;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.aashay.spit.sptbi.Panelist.Panelist;
import org.aashay.spit.sptbi.Startup.Startup;

@Path("/admin")
public final class AdminResource {
	
private AdminService admin=new AdminService();
	
	@POST
	public int postToDatabase(Panelist panelist)
	{
		return admin.postToDatabase(panelist);
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public int deleteMessage(@QueryParam("username") String username) 
	{
		 return admin.removePanelist(username);
	}

	@GET
	@Path("/getPanelists")
	public ArrayList<Panelist> getAllPanelists()
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
	public void endRound(@QueryParam("endRound") int round)
	{
		admin.endRound(round,null);
	}
	
	@GET
	@Path("/getRoundStatus")
	public Admin getEndRoundStatus()
	{
		return admin.getEndRoundStatus();
	}
	
	@GET
	@Path("/getPanelistWithPendingForms")
	public ArrayList<Admin> getPanelistsWithPendingForms()
	{
		return admin.getPanelistsWithPendingForms();
	}

	@GET
	@Path("/startRegistration")
	public int startRegistration()
	{
		return admin.startRegistration();
	}
	
}
