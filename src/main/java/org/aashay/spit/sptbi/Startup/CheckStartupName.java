package org.aashay.spit.sptbi.Startup;

// Model class to check if the startup name exists in the database

public final class CheckStartupName {

	private String check;

	public CheckStartupName() {
		
	}
	
	public CheckStartupName(String check)
	{
		this.check=check;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}
	
}