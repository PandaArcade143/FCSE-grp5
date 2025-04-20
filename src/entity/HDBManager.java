package entity;

import java.util.ArrayList;
import java.util.List;

public class HDBManager extends User{
	List<Project> createdProjects;

	public HDBManager(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
		this.createdProjects = new ArrayList<>();
	}
	
	public String getRole() {
		return "HDBManager";
		
	}

	public List<Project> getCreatedProjects(){
		return this.createdProjects;
	}

	// Performs actions to add/remove created projects
	public void addCreatedProjects(Project p){
		if (p != null){ // Check for null for safety
            this.createdProjects.add(p);
        }
	}

	public void removeCreatedProject(Project p){
		if (p != null){ // Check for null for safety
            this.createdProjects.remove(p);
        }
	}

	// Legacy function - remove once unneeded
	public void setCreatedProjects(List<Project> p){
		this.createdProjects = p;
	}
}
