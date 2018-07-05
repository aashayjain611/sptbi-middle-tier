package org.aashay.spit.sptbi.Founder;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/")
public class FounderResource {
	
	private FounderService founderService=new FounderService();
	
	// resource to get founders' details for a particular startup
	
	@GET
	public ArrayList<Founder> getFounderByFormId(@PathParam("formid") String formid)
	{
		return founderService.getFounderById(Integer.parseInt(formid));
	}
	
}
