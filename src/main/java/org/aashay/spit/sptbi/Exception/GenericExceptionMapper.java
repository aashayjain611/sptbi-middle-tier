package org.aashay.spit.sptbi.Exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
//This is for every exception
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable ex) {
		// TODO Change url to url of sptbi homepage
		ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),404,"http://javabrains.koushik.org");
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(errorMessage)
				.build();
		
	}

}
