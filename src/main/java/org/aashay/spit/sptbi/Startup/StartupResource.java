package org.aashay.spit.sptbi.Startup;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.aashay.spit.sptbi.Founder.FounderResource;

@Path("/startup")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StartupResource {
	
	private StartupService startupService=new StartupService();
	
	// resource to post the form to database
	
	@POST
	public int postToDatabase(String json)
	{
		return startupService.postToDatabase(json);
	}
	
	/*@GET
	@Path("/{formid}")
	public Startup getStartupByFormId(@PathParam("formid") String formid)
	{
		return startupService.getStartupById(Integer.parseInt(formid));
	}*/
	
	// resource to get founders' details for a particular startup
	
	@Path("{formid}/founder")
	public FounderResource getFounder()
	{
		return new FounderResource();
	}
}
