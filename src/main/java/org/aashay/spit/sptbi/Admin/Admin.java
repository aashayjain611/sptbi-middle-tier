package org.aashay.spit.sptbi.Admin;

public class Admin {
	
	private String userName;
	private String category;
	private int selectionLimit;
	private int round;
	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Admin() {
		
	}
	
	public Admin(String userName, String category,int selectionLimit,int round,String password) 
	{
		this.userName=userName;
		this.category=category;
		this.selectionLimit=selectionLimit;
		this.round=round;
		this.password=password;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getSelectionLimit() {
		return selectionLimit;
	}
	public void setSelectionLimit(int selectionLimit) {
		this.selectionLimit = selectionLimit;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	
	
}
