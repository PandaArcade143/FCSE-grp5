package entity;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class HDBOfficer extends Applicant{
	List<Project> registeredProjects = new ArrayList<Project>();
	Map<String, String> registrationStatus = new HashMap<String, String>();

	public HDBOfficer(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
	}

	public String getRole() {
		return "HDBOfficer";
	}

	public List<Project> getRegisteredProjects(){
		return registeredProjects;
	}

	public String getRegistrationStatus(String key){ // Added a key value to be received
		return registrationStatus.get(key);
	}

	public void addRegisteredProjects(Project proj){
		registeredProjects.add(proj);
	}

	public void addRegistrationStatus(String name, String status){ // Boolean?
		registrationStatus.put(name, status); // Is a string, string list
	}

	public Boolean isEligibleForRegistration(Project p){
		// If officer has applied for the same project as an application, he is not allowed to join the project
		if (this.project == p && this.getAppliedProject() == p) return false;

		// Unable to compute date range yet, confirm during meeting
		// for (Project proj : registeredProjects){

		// }
		
		
		return true;

	}

	// What info should be in ToString()?
}
