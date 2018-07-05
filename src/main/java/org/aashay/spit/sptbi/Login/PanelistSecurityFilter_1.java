package org.aashay.spit.sptbi.Login;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

@Provider
public class PanelistSecurityFilter_1 implements ContainerRequestFilter {

	private static final String AUTHORIZATION_HEADER="Authorization"; 
	private static final String AUTHORIZATION_HEADER_PREFIX="Basic"; 
	private static final String SECURED_URI_PREFIX="panelistSecured_1"; 
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		if(requestContext.getUriInfo().getPath().contains(SECURED_URI_PREFIX)) {
			List<String> authHeader= requestContext.getHeaders().get(AUTHORIZATION_HEADER);
			
			if(authHeader!=null && authHeader.size()>0) {
				String authToken=authHeader.get(0);
				authToken=authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
				String decodeString=Base64.decodeAsString(authToken);
				StringTokenizer tokenizer=new StringTokenizer(decodeString,":");
				String username=tokenizer.nextToken();
				String password=tokenizer.nextToken();
				
				if("anukrit".equals(username) && "password".equals(password)) {
					return;
				}
				
			}
			
			Response unauthorizedStatus= Response.status(Response.Status.UNAUTHORIZED)
					.entity("User cannot access the resource")
					.build();
			
			requestContext.abortWith(unauthorizedStatus);
		}
		
	}

}
