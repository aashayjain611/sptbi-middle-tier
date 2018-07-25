package org.aashay.spit.sptbi.Panelist;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.aashay.spit.sptbi.Startup.Startup;

@Path("/panelist")
public final class PanelistResource {
	
	private PanelistService panelist=new PanelistService();
	
	
	// resource to get all untouched forms
	
	@GET
	public ArrayList<Startup> getAllNewStartup(@QueryParam("username") String username)
	{
		return panelist.getAllNewStartup(username);
	}
	
	// resource to post the updates made by the panelists
	
	@POST
	@Path("/update")
	public int postToDatabase(String json,@QueryParam("username") String username)
	{
		return panelist.postToDatabase(json,username);
	}
	
	// resource to get the selectionlimit of the panelist
	
	@GET
	@Path("/limit")
	public Panelist getSelectionLimitByUsername(@QueryParam("username") String username)
	{
		return panelist.getSelectionLimit(username);
	}
	
}
