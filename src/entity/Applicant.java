package entity;

public class Applicant extends User{
	Project appliedProject;
	String applicationStatus;

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
	
	
	
	
	// Setter
	public void setAppliedProject (Project p){
		this.appliedProject = p;
	}

	public void setApplicationStatus(String status){
		this.applicationStatus = status;
	}
	


}
