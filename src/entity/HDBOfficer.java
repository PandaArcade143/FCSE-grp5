package entity;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class HDBOfficer extends Applicant{
	Project registeredProject;
	String registrationStatus;

	public HDBOfficer(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
	}

	public String getRole() {
		return "HDBOfficer";
	}

	public Project getRegisteredProjects(){
		return this.registeredProject;
	}

	public String getRegistrationStatus(String key){ // Added a key value to be received
		return this.registrationStatus;
	}

	public void addRegisteredProjects(Project proj){
		this.registeredProject = proj;
	}

	public void setRegistrationStatus(String status){
		this.registrationStatus = status;
	}

	public Boolean isEligibleForRegistration(Project p){
		// If officer has applied for the same project as an application, he is not allowed to join the project
		if (this.registeredProject == p && this.getAppliedProject() == p) return false;

		// Unable to compute date range yet, confirm during meeting
		// for (Project proj : registeredProjects){

		// }
		
		
		return true;

	}

	// What info should be in ToString()?
}
