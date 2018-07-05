package org.aashay.spit.sptbi.Panelist;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.aashay.spit.sptbi.Startup.Startup;

@Path("/panelist")
public class PanelistResource {
	
	private PanelistService panelist=new PanelistService();
	
	
	// resource to get all untouched forms
	
	@GET
	@Path("/{username}")
	public ArrayList<Startup> getAllNewStartup(@PathParam("username") String username)
	{
		return panelist.getAllNewStartup(username);
	}
	
	// resource to post the updates made by the panelists
	
	@POST
	@Path("/update/{username}")
	public int postToDatabase(String json,@PathParam("username") String username)
	{
		return panelist.postToDatabase(json,username);
	}
	
	@GET
	@Path("/limit/{username}")
	public Panelist getSelectionLimitByUsername(@PathParam("username") String username)
	{
		return panelist.getSelectionLimit(username);
	}
	
}
