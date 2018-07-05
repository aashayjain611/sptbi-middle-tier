package org.aashay.spit.sptbi.Panelist;

public class Panelist 
{
	private int panelistno;
	private String username;
	private String password;
	private String category;
	private int round;
	private int selectionLimit;
	private int start;
	private int end;
	
	public Panelist() {
		super();
	}

	public int getPanelistno() {
		return panelistno;
	}

	public void setPanelistno(int panelistno) {
		this.panelistno = panelistno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getSelectionLimit() {
		return selectionLimit;
	}

	public void setSelectionLimit(int selectionLimit) {
		this.selectionLimit = selectionLimit;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public Panelist(int selectionLimit) {
		super();
		this.selectionLimit = selectionLimit;
	}
	
	
	
}
