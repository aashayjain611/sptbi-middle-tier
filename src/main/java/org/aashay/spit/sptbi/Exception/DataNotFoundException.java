package org.aashay.spit.sptbi.Exception;

public class DataNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4261597837459699594L;
	
	private static final String TAG="\"DataNotFoundException\"";
	
	public  DataNotFoundException(String message) {
		super(TAG+": "+message);
	}
}
