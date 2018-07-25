package org.aashay.spit.sptbi.Startup;

import java.util.ArrayList;

import org.aashay.spit.sptbi.Founder.Founder;

public final class Startup {
	
	private int formid; //stores formid generated by middle tier
	private String startupName; //stores the startup name
	private String legalEntity; //stores whether the startup is a legal entity or not ("YES" or "NO")
	private String description; //stores the description given for the startup
	private int noFounders; //stores no of founders in that startup
	private String painPoint; //stores pain point the startup is trying to solve 
	private String primaryCustomer; //stores primary customers of the startup
	private String competitors; //stores competitors of that startup
	private String differentFromCompetitors; //stores what different startup does from their competitors
	private String moneyModel; //stores money model of the startup
	private String workingIdea; //stores whether the startup has a working model or not ("YES" or "NO")
	private String operationalRevenue; //stores whether the startup has a operational revenue or not ("YES" or "NO")
	private String startupIdea; //stores the startup idea 
	private String category; //stores the category to which the startup belongs
	private String status; //stores the status for a particular round ("NO","YES","NEW")
	private String timestamp; //stores the timestamp when the form is registered
	private int rating; //stores rating for a particular round
	private String note; //stores note for a particular round
	private ArrayList<Founder> founders; //stores list of founders associated with a startup
	private String panelist; //stores username of the panelist 
	private int round; //stores the round no. of the startup
	
	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getPanelist() {
		return panelist;
	}

	public void setPanelist(String panelist) {
		this.panelist = panelist;
	}

	public ArrayList<Founder> getFounders() {
		return founders;
	}

	public void setFounders(ArrayList<Founder> founders) {
		this.founders = founders;
	}

	public Startup() {
		super();
	}
	
	//constructor for POST request (with ratings and notes)
	
	public Startup(int formid, String startupName, String legalEntity, String description, int noFounders,
			String painPoint, String primaryCustomer, String competitors, String differentFromCompetitors,
			String moneyModel, String workingIdea, String operationalRevenue, String startupIdea, String category,
			String status, int rating, String note,ArrayList<Founder> founders) {
		super();
		this.formid = formid;
		this.startupName = startupName;
		this.legalEntity = legalEntity;
		this.description = description;
		this.noFounders = noFounders;
		this.painPoint = painPoint;
		this.primaryCustomer = primaryCustomer;
		this.competitors = competitors;
		this.differentFromCompetitors = differentFromCompetitors;
		this.moneyModel = moneyModel;
		this.workingIdea = workingIdea;
		this.operationalRevenue = operationalRevenue;
		this.startupIdea = startupIdea;
		this.category = category;
		this.status = status;
		this.rating = rating;
		this.note = note;
		this.founders=founders;
	}
	
	public Startup(int formid, String startupName, String legalEntity, String description, int noFounders,
			String painPoint, String primaryCustomer, String competitors, String differentFromCompetitors,
			String moneyModel, String workingIdea, String operationalRevenue, String startupIdea, String category,
			String status, int rating, String note,ArrayList<Founder> founders,String panelist,int round, String timestamp) {
		super();
		this.formid = formid;
		this.startupName = startupName;
		this.legalEntity = legalEntity;
		this.description = description;
		this.noFounders = noFounders;
		this.painPoint = painPoint;
		this.primaryCustomer = primaryCustomer;
		this.competitors = competitors;
		this.differentFromCompetitors = differentFromCompetitors;
		this.moneyModel = moneyModel;
		this.workingIdea = workingIdea;
		this.operationalRevenue = operationalRevenue;
		this.startupIdea = startupIdea;
		this.category = category;
		this.status = status;
		this.rating = rating;
		this.note = note;
		this.founders=founders;
		this.panelist=panelist;
		this.round=round;
		this.timestamp=timestamp;
	}
	
	// getters and setter methods

	public int getFormid() {
		return formid;
	}
	public void setFormid(int formid) {
		this.formid = formid;
	}
	public String getStartupName() {
		return startupName;
	}
	public void setStartupName(String startupName) {
		this.startupName = startupName;
	}
	public String getLegalEntity() {
		return legalEntity;
	}
	public void setLegalEntity(String legalEntity) {
		this.legalEntity = legalEntity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getNoFounders() {
		return noFounders;
	}
	public void setNoFounders(int noFounders) {
		this.noFounders = noFounders;
	}
	public String getPainPoint() {
		return painPoint;
	}
	public void setPainPoint(String painPoint) {
		this.painPoint = painPoint;
	}
	public String getPrimaryCustomer() {
		return primaryCustomer;
	}
	public void setPrimaryCustomer(String primaryCustomer) {
		this.primaryCustomer = primaryCustomer;
	}
	public String getCompetitors() {
		return competitors;
	}
	public void setCompetitors(String competitors) {
		this.competitors = competitors;
	}
	public String getDifferentFromCompetitors() {
		return differentFromCompetitors;
	}
	public void setDifferentFromCompetitors(String differentFromCompetitors) {
		this.differentFromCompetitors = differentFromCompetitors;
	}
	public String getMoneyModel() {
		return moneyModel;
	}
	public void setMoneyModel(String moneyModel) {
		this.moneyModel = moneyModel;
	}
	public String getWorkingIdea() {
		return workingIdea;
	}
	public void setWorkingIdea(String workingIdea) {
		this.workingIdea = workingIdea;
	}
	public String getOperationalRevenue() {
		return operationalRevenue;
	}
	public void setOperationalRevenue(String operationalRevenue) {
		this.operationalRevenue = operationalRevenue;
	}
	public String getStartupIdea() {
		return startupIdea;
	}
	public void setStartupIdea(String startupIdea) {
		this.startupIdea = startupIdea;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
