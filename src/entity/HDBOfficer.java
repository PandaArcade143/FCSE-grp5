package entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import control.InquiryController;
import IHDBStaff;

/**
 * Represents an HDB Officer who is also an applicant,
 * but with the additional ability to register and manage a project.
 */
public class HDBOfficer extends Applicant implements IHDBStaff{
	Project registeredProject;
	List<Project> approvedProjects = new ArrayList<Project>();
	List<Project> pendingProjects = new ArrayList<Project>();
	String registrationStatus;
	String role;

	/**
	 * Returns the list of projects that have been approved for the user.
	 *
	 * @return a list of approved projects
	 */	
	public List<Project> getApprovedProjects() {
		return approvedProjects;
	}

	/**
	 * Sets the list of approved projects for the user.
	 *
	 * @param approvedProjects the list of approved projects to set
	 */
	public void setApprovedProjects(List<Project> approvedProjects) {
		this.approvedProjects = approvedProjects;
	}
	
	/**
	 * Adds a project to the list of approved projects.
	 *
	 * @param p the project to add to the approved list
	 */
	public void addApprovedProject(Project p) {
		this.approvedProjects.add(p);
	}

	/**
	 * Returns the list of projects that are currently pending approval for the user.
	 *
	 * @return a list of pending projects
	 */
	public List<Project> getPendingProjects() {
		return pendingProjects;
	}

	/**
	 * Sets the list of pending projects for the user.
	 *
	 * @param pendingProjects the list of pending projects to set
	 */
	public void setPendingProjects(List<Project> pendingProjects) {
		this.pendingProjects = pendingProjects;
	}

	/**
	 * Adds a project to the list of pending projects.
	 *
	 * @param p the project to add to the pending list
	 */
	public void addPendingProject(Project p) {
		this.pendingProjects.add(p);
	}

	/**
	 * Removes a project from the list of pending projects.
	 *
	 * @param p the project to remove from the pending list
	 */
	public void removePendingProject(Project p) {
		this.pendingProjects.remove(p);
	}

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
		if (approvedProjects.contains(p) || this.getAppliedProject() == p) {
		
				System.out.print("This is trunning");
			return false;
		}
	
		// Project must not be be in the same date range
		for (Project project : approvedProjects) {

			boolean overlaps = !(p.getCloseDate().before(project.getOpenDate()) ||
	                         p.getOpenDate().after(project.getCloseDate()));	                        		
			if (overlaps) {
				return false;
			}
		}
		
		return true;
	}

	/**
	* Reply to an inquiry
	* 
	* @param inquiryId is the ID of the inquiry to reply to
	* @param replyMessage is the message for the inquiry
	*/
	@Override
	void replyToInquiry(String inquiryId, String replyMessage){
		InquiryController.replyToInquiry(inquiryId, replyMessage);
	}
  
	/**
	* Closes and resolves an inquiry
	*
	* @param inquiryId is the ID of the inquiry to resolve
	*/
	@Override
	void resolveInquiry(String inquiryId){
		InquiryController.resolveInquiry(inquiryId);
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
