package entity;

import java.util.ArrayList;
import java.util.List;

public class HDBManager extends User{
	List<Project> createdProject;

	public HDBManager(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
	}
	
	public String getRole() {
		return "HDBManager";
		
	}

	public List <Project> getCreatedProjects(){ // UML diagram said to return a string and I don't see an Override ToString in UML
		return this.createdProject;
	}

	public void setCreatedProjects(List<Project> p){
		this.createdProject = p;
	}

	
	
}
