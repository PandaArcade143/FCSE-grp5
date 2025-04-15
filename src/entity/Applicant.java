package entity;
import java.util.List;
import java.util.ArrayList;

public class Applicant extends User{
	Project appliedProject;
	String flatType;
	String applicationStatus;
	String withdrawalStatus;
	List<Inquiry> inquiries = new ArrayList<Inquiry>();

	public Applicant(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
	}

	public String getRole() {
		return "Applicant";
	}

	public Project getAppliedProject() {
		return this.appliedProject;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}
	
	public String getWithdrawalStatus(){
		return withdrawalStatus;
	}
	
	public List<Inquiry> getInquiries(){ // UML diagram said to return a string and I don't see an Override ToString in UML
		return inquiries;
	}	

	// Setter
	public void setAppliedProject(Project appliedProj){
		this.appliedProject = appliedProj;
	}

	public void setApplicationStatus(String appStatus){
		this.applicationStatus = appStatus;
	}

	public void setWithdrawalStatus(String status){
		this.withdrawalStatus = status;
	}

	public void addInquiry(Inquiry inquiry){
		inquiries.add(inquiry);
	}

	public boolean isEligibleForProject(Project p){
		if (appliedProject == null) return false; // If already assigned to a project

		// Do I have to account for if there are any flats left?

		// If Single, can only apply to 2-room flats
		if (this.getMaritalStatus().equals("Single") && p.hasFlatType("2-Room")) {
			if (this.getAge() < 35) return false;
			return true;
		}
		// If Married, any flat type is allowed
		else if (this.getMaritalStatus().equals("Married")){
			if (this.getAge() < 21) return false;
			return true;
		}

		return false;
	}

	public String getFlatType(){
		return this.flatType;
	}

	public void setFlatType(String t){
		this.flatType = t;
	}
}
