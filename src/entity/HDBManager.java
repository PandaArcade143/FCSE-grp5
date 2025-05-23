package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an HDB Manager, a user responsible for creating and managing BTO projects.
 * This class maintains a list of projects created by the manager.
 */
public class HDBManager extends User{
	List<Project> createdProjects;
	
	/**
     * Constructs an HDBManager using the given user details.
     *
     * @param name the manager's name
     * @param nric the manager's NRIC
     * @param age the manager's age
     * @param maritalStatus the manager's marital status
     * @param password the login password
     */
	public HDBManager(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
		this.createdProjects = new ArrayList<>();
	}
	
	/**
     * Gets the role of the user.
     *
     * @return the string "HDBManager"
     */
	public String getRole() {
		return "HDBManager";
		
	}

	/**
     * Returns the list of projects created by this manager.
     *
     * @return a list of created Project objects
     */
	public List<Project> getCreatedProjects(){
		return this.createdProjects;
	}

	/**
     * Adds a project to the list of created projects.
     *
     * @param p the project to add
     */
	public void addCreatedProjects(Project p){
		if (p != null){ // Check for null for safety
            this.createdProjects.add(p);
        }
	}

	/**
     * Removes a project from the list of created projects.
     *
     * @param p the project to remove
     */
	public void removeCreatedProject(Project p){
		if (p != null){ // Check for null for safety
            this.createdProjects.remove(p);
        }
	}

	/**
     * Sets the entire list of created projects.
     *
     * @param p the list of projects to assign to this manager
     */
	public void setCreatedProjects(List<Project> p){
		this.createdProjects = p;
	}
	
	/**
     * Returns the manager's name as a string representation.
     *
     * @return the manager's name
     */
	public String toString() {
		return this.getName();
	}
}
