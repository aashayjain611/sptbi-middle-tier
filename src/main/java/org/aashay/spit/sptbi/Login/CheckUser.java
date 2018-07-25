package org.aashay.spit.sptbi.Login;
 //This class is used to return the category the panelist will handle as well as the round number. This class was made because of Urvi
public final class CheckUser {
	private int check; //checks if it is an admin,panelist from round 1, panelist from round 2 or incorrect user
	private String category;
	private String username;
	
	public CheckUser() {
		
	}
	
	public CheckUser(int check, String category, String username) {
		this.check=check;
		this.category=category;
		this.username=username;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
