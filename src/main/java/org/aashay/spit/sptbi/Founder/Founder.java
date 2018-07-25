package org.aashay.spit.sptbi.Founder;

public final class Founder {
	
	private int formid; //stores the formid to which the founder is associated 
	private int founderid; //stores the generated founderid
	private String founderName; //stores the name of founder
	private String founderEmail; //stores the email of founder 
	private long founderContact; //stores the contact of founder
	
	//getter and setter methods
	
	public int getFormid() {
		return formid;
	}
	public void setFormid(int formid) {
		this.formid = formid;
	}
	public int getFounderid() {
		return founderid;
	}
	public void setFounderid(int founderid) {
		this.founderid = founderid;
	}
	public String getName() {
		return founderName;
	}
	public void setName(String name) {
		this.founderName = name;
	}
	public String getEmail() {
		return founderEmail;
	}
	public void setEmail(String email) {
		this.founderEmail = email;
	}
	public long getContact() {
		return founderContact;
	}
	public void setContact(long contact) {
		this.founderContact = contact;
	}
	
	public Founder() {
		super();
		// TODO Auto-generated constructor stub
	}
	//constructor for POST request
	public Founder(String name, String email, long contact) {
		super();
		this.founderName = name;
		this.founderEmail = email;
		this.founderContact = contact;
	}
	//constructor for GET request
	public Founder(int formid, String name, String email, long contact) {
		super();
		this.formid = formid;
		this.founderName = name;
		this.founderEmail = email;
		this.founderContact = contact;
	}
}
