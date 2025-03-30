package entity;

public class Applicant extends User{
	Project project;
	String applicationStatus;

	public Applicant(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
	}

	public String getRole() {
		return "Applicant";
	}

	public Project getAppliedProject() {
		return project;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}
	
	
	
	
	// Setter
	


}
