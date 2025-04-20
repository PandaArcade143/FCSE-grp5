package entity;
import java.util.Objects;

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

	public String getRegistrationStatus(){ 
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

		// Project must not be be in the same date range
		if (p.getOpenDate().after(registeredProject.getOpenDate()) && p.getOpenDate().before(registeredProject.getCloseDate())) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (!(obj instanceof HDBOfficer)) return false;
	    HDBOfficer o = (HDBOfficer) obj;
	    return this.getNRIC().equals(o.getNRIC()); // or whatever uniquely identifies an officer
	}

	@Override
	public int hashCode() {
	    return Objects.hash(this.getNRIC());
	}
}
