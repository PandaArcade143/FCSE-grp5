package entity;
import java.util.Objects;

/**
 * Represents an HDB Officer who is also an applicant,
 * but with the additional ability to register and manage a project.
 */
public class HDBOfficer extends Applicant{
	Project registeredProject;
	String registrationStatus;
	String role;

	/**
     * Constructs an HDB Officer using base applicant information.
     *
     * @param name the officer's name
     * @param nric the officer's NRIC
     * @param age the officer's age
     * @param maritalStatus the officer's marital status
     * @param password the login password
     */
	public HDBOfficer(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
		role = "HDBOfficer";
	}

	/**
     * Gets the role of the user.
     *
     * @return a string representing the role, in this case "HDBOfficer"
     */
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
		
	}

	/**
     * Gets the project this officer is currently registered to manage.
     *
     * @return the registered project
     */
	public Project getRegisteredProjects(){
		return this.registeredProject;
	}

	/**
     * Gets the registration status of this officer (e.g., Pending, Approved).
     *
     * @return the current registration status
     */
	public String getRegistrationStatus(){ 
		return this.registrationStatus;
	}

	/**
     * Registers this officer to a project.
     *
     * @param proj the project to register the officer to
     */
	public void addRegisteredProjects(Project proj){
		this.registeredProject = proj;
	}

	/**
     * Sets the current registration status of this officer.
     *
     * @param status the status to assign (e.g. "Approved")
     */
	public void setRegistrationStatus(String status){
		this.registrationStatus = status;
	}

	/**
     * Checks if the officer is eligible to register for a given project.
     * Officers are not eligible if:
     * - They have already applied to the same project as an applicant
     * - The new project overlaps in date with the currently registered project
     *
     * @param p the project to evaluate
     * @return true if eligible, false otherwise
     */
	public Boolean isEligibleForRegistration(Project p){
		// If officer has applied for the same project as an application, he is not allowed to join the project
		if (this.registeredProject == p && this.getAppliedProject() == p) return false;

		// Project must not be be in the same date range
		if (p.getOpenDate().after(registeredProject.getOpenDate()) && p.getOpenDate().before(registeredProject.getCloseDate())) {
			return false;
		}
		
		return true;
	}
	
	/**
     * Checks if this officer is equal to another based on NRIC.
     *
     * @param obj the object to compare with
     * @return true if the officers share the same NRIC
     */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (!(obj instanceof HDBOfficer)) return false;
	    HDBOfficer o = (HDBOfficer) obj;
	    return this.getNRIC().equals(o.getNRIC()); // or whatever uniquely identifies an officer
	}

	/**
     * Returns a hash code based on the officer's NRIC.
     *
     * @return hash code for this officer
     */
	@Override
	public int hashCode() {
	    return Objects.hash(this.getNRIC());
	}
}
