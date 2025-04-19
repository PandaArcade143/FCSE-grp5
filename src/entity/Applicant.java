package entity;
import java.util.List;
import java.util.ArrayList;


/**
 * Represents an Applicant who can apply for a housing project.
 * Extends the {@link User} class with specific fields and behaviors related to project applications.
 */
public class Applicant extends User{
	Project appliedProject;
	String appliedProjectString;
	String flatType;
	String applicationStatus;
	String withdrawalStatus;
	List<Inquiry> inquiries = new ArrayList<Inquiry>();

	 /**
     * Constructs an Applicant with the given personal information.
     *
     * @param name          the name of the applicant
     * @param nric          the NRIC of the applicant
     * @param age           the age of the applicant
     * @param maritalStatus the marital status of the applicant
     * @param password      the password for login
     */
	public Applicant(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
	}
	
	/**
     * Returns the name or string representation of the applied project.
     *
     * @return the project string
     */
	public String getAppliedProjectString() {
		return appliedProjectString;
	}

	/**
     * Sets the project name or string representation.
     *
     * @param appliedProjectString the project string
     */
	public void setAppliedProjectString(String appliedProjectString) {
		this.appliedProjectString = appliedProjectString;
	}

	 /**
     * Returns the role of this user.
     *
     * @return the role string "Applicant"
     */
	@Override
	public String getRole() {
		return "Applicant";
	}

	/**
     * Returns the project the applicant applied for.
     *
     * @return the applied {@link Project}
     */
	public Project getAppliedProject() {
		return this.appliedProject;
	}
	

    /**
     * Sets the project the applicant has applied for.
     *
     * @param appliedProj the project to assign
     */
    public void setAppliedProject(Project appliedProj) {
        this.appliedProject = appliedProj;
    }	


	public String getApplicationStatus() {
		return applicationStatus;
	}
	
	/**
     * Returns the withdrawal status.
     *
     * @return the withdrawal status
     */
	public String getWithdrawalStatus(){
		return withdrawalStatus;
	}
	
	/**
     * Returns the list of inquiries made by the applicant.
     *
     * @return a list of {@link Inquiry} objects
     */
	public List<Inquiry> getInquiries(){ // UML diagram said to return a string and I don't see an Override ToString in UML
		return inquiries;
	}	

	/**
     * Sets the application status.
     *
     * @param appStatus the application status to set
     */
	public void setApplicationStatus(String appStatus){
		this.applicationStatus = appStatus;
	}

    /**
     * Sets the withdrawal status.
     *
     * @param status the status to set
     */
	public void setWithdrawalStatus(String status){
		this.withdrawalStatus = status;
	}
	
	/**
     * Adds an inquiry to the list of inquiries.
     *
     * @param inquiry the inquiry to add
     */
	public void addInquiry(Inquiry inquiry){
		inquiries.add(inquiry);
	}

	/**
     * Checks if the applicant is eligible to apply for the given project.
     * <ul>
     *     <li>Single applicants can only apply to 2-room flats and must be at least 35 years old.</li>
     *     <li>Married applicants can apply to any flat type and must be at least 21 years old.</li>
     * </ul>
     *
     * @param p the project to check eligibility for
     * @return true if eligible, false otherwise
     */
	public boolean isEligibleForProject(Project p){
		if (appliedProject == null) return false; // If already assigned to a project

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
	
	/**
     * Returns the flat type chosen by the applicant.
     *
     * @return the flat type
     */
	
	public String getFlatType(){
		return this.flatType;
	}
	
	/**
     * Returns the application status.
     *
     * @return the application status
     */
	public void setFlatType(String t){
		this.flatType = t;
	}
}
