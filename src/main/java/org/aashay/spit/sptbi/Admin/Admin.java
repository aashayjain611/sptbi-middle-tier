package org.aashay.spit.sptbi.Admin;

public final class Admin {
	
	private String statusEndRound1;
	private String statusEndRound2;
	private String panelistNameWithPendingWork;

	public Admin() {
		// TODO Auto-generated constructor stub
	}
	
	public Admin(String statusEndRound1, String statusEndRound2) {
		super();
		this.statusEndRound1 = statusEndRound1;
		this.statusEndRound2 = statusEndRound2;
	}
	
	public Admin(String panelistNameWithPendingWork) {
		super();
		this.panelistNameWithPendingWork = panelistNameWithPendingWork;
	}

	public String getPanelistNameWithPendingWork() {
		return panelistNameWithPendingWork;
	}

	public void setPanelistNameWithPendingWork(String panelistNameWithPendingWork) {
		this.panelistNameWithPendingWork = panelistNameWithPendingWork;
	}

	public String getStatusEndRound1() {
		return statusEndRound1;
	}

	public void setStatusEndRound1(String statusEndRound1) {
		this.statusEndRound1 = statusEndRound1;
	}

	public String getStatusEndRound2() {
		return statusEndRound2;
	}

	public void setStatusEndRound2(String statusEndRound2) {
		this.statusEndRound2 = statusEndRound2;
	}
	
}