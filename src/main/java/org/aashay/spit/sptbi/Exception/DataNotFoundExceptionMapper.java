package org.aashay.spit.sptbi.Exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException ex) {
		// TODO Change url to url of sptbi homepage
		ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),404,"http://javabrains.koushik.org");
		return Response.status(Status.NOT_FOUND)
				.entity(errorMessage)
				.build();
		
	}

}
