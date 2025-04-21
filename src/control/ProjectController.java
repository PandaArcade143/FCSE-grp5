package control;
import entity.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import helpers.DataManager;

public class ProjectController {
    private List <Project> projects = DataManager.getProjects();
    private List <Applicant> applicants = DataManager.getApplicants();
    private Map<String, String> filters;


    public ProjectController(){
    	this.filters = new HashMap<>();
    	filters.put("location", "");
    	filters.put("flatType", "");
    	filters.put("maxPrice", "");
    	filters.put("minPrice", "");
    }
    
    public void filterByLocation(String location) {
    	filters.put("location", location);
    }
    
    public void filterByFlatType(String flatType) {
    	filters.put("flatType", flatType);
    }
    
    public void filterByMaxPrice(int maxPrice) {
    	filters.put("maxPrice", Integer.toString(maxPrice));
    }
    
    public void filterByMinPrice(int minPrice) {
    	filters.put("minPrice", Integer.toString(minPrice));
    }
    
    public Map<String, String> getFilters() {
    	return filters;
    }
    
    public List <Project> getFilteredProjects(User user) {
    	List <Project> res = getAvailableProjects(user);
    	if (!filters.get("location").equals("")) {
    		res = res.stream()
					.filter(project -> project.getLocation().equals(filters.get("location")))
					.collect(Collectors.toList());
    	}
    	
    	if (!filters.get("flatType").equals("")) {
    		res = res.stream()
    				.filter(project -> project.getFlatTypeTotal().containsKey(filters.get("flatType")))
					.collect(Collectors.toList());
    	}
    	
    	if (!filters.get("maxPrice").equals("")) {
    		res = res.stream()
    				.filter(project -> Collections.max(project.getFlatPrices().values()) < Integer.parseInt(filters.get("maxPrice")))
    				.collect(Collectors.toList());
    	}

    	if (!filters.get("minPrice").equals("")) {
    		res = res.stream()
    				.filter(project -> Collections.min(project.getFlatPrices().values()) > Integer.parseInt(filters.get("minPrice")))
    				.collect(Collectors.toList());
    	}
    	
    	return res;
    }

    public List <Project> getFilteredProjects(List<Project> p) {
    	List <Project> res = new ArrayList<>(p);
    	if (!filters.get("location").equals("")) {
    		res = res.stream()
					.filter(project -> project.getLocation().equals(filters.get("location")))
					.collect(Collectors.toList());
    	}
    	
    	if (!filters.get("flatType").equals("")) {
    		res = res.stream()
    				.filter(project -> project.getFlatTypeTotal().containsKey(filters.get("flatType")))
					.collect(Collectors.toList());
    	}
    	
    	if (!filters.get("maxPrice").equals("")) {
    		res = res.stream()
    				.filter(project -> Collections.max(project.getFlatPrices().values()) < Integer.parseInt(filters.get("maxPrice")))
    				.collect(Collectors.toList());
    	}

    	if (!filters.get("minPrice").equals("")) {
    		res = res.stream()
    				.filter(project -> Collections.min(project.getFlatPrices().values()) > Integer.parseInt(filters.get("minPrice")))
    				.collect(Collectors.toList());
    	}
    	
    	return res;
    }

    

    public List <Project> getAvailableProjects(User user){
        List <Project> store = new ArrayList <> ();

        //Applicant type
        if (user.getRole().equals("Applicant")){

            //Check for user details to determine
            //Single, 35 and above
            if (user.getMaritalStatus().equals("Single") && user.getAge() >= 35){

                //Check for visibility and 2 room flats
                for (Project a : this.projects){
                    if (a.getVisibility() == true && a.getFlatTypeAvailable().get("2-Room") != null){
                        store.add(a);
                    }
                }
                return store;

            //Married, 21 and above
            } else if (user.getMaritalStatus().equals("Married") && user.getAge() >= 21){
                //Check for visibility
                for (Project a : this.projects){
                    if (a.getVisibility() == true){
                        store.add(a);
                    }
                }
                return store;

            } else{
                return store;
            }
            
        //Officer type
        } else if (user.getRole().equals("HDBOfficer")){
            //Able to view projects they are handling regardless of their visibility and also projects that are visiblie
            for (Project a : this.projects){
                if (a.getVisibility() == true || a.getOfficers().contains(user)){
                    store.add(a);
                }
            }
            return store;

        //Manager type
        } else if (user.getRole().equals("HDBManager")){
            return this.projects;
        }
    //If nothing matches, then return empty list
    return store;
    }

    public boolean applyToProject (Applicant a, String projectName){
        //Check if already applied project
        if ("Pending".equals(a.getApplicationStatus()) || "Successful".equals(a.getApplicationStatus()) || "Booked".equals(a.getApplicationStatus())){
            return false;
        }
        
        //Check for HDBOfficer
        if (a.getRole().equals("HDBOfficer")){
            for (Project n : this.projects){
                if (n.getName().equalsIgnoreCase(projectName)){
                    //Check if n is an officer for the slot
                    if (n.getOfficers().contains(a)){
                        return false;
                    } else{
                        a.setAppliedProject(n);
                        a.setApplicationStatus("Pending");
                        return true;
                    }
                }
            }
        }

        //Check the attributes of the applicant
        if (a.getRole() == "Applicant"){

            //Married, 21 and above
            if (a.getAge() >= 21 && a.getMaritalStatus().equals("Married")){
                for (Project n : this.projects){
                    if (n.getName().equalsIgnoreCase(projectName)){
                        a.setAppliedProject(n);
                        a.setApplicationStatus("Pending");
                        return true;
                    }
                }

            //Single, 35 and above
            } else if (a.getAge() >= 35 && a.getMaritalStatus().equals("Single")){


                for (Project n : this.projects){
                    if (n.getName().equalsIgnoreCase(projectName)){
                        //Check if there is 2 room flats
                        if (n.getFlatTypeAvailable().get("2-Room") != null){
                            a.setAppliedProject(n);
                            a.setApplicationStatus("Pending");
                            return true;
                        } else{
                            return false;
                        }
                    }
                }
            }
        }
    

    //If none of the criteria is met
    return false;
    }

    public void bookFlat (HDBOfficer officer, String n, String flatType){
        Applicant k = null;
        for (Applicant i : this.applicants){
            if (i.getNRIC() == n){
                k = i;
            }
        } 
        if (k == null){
            return;
        }
            
        k.getAppliedProject().bookFlat(flatType);
        k.setApplicationStatus("booked");
        k.setFlatType(flatType);
    }

    public void toggleProject(Project p, boolean a){
        p.toggleVisibility(a);
    }

    public void createProject (HDBManager m, Project p){
        m.addCreatedProjects(p);
        if (this.projects.contains(p)){
            return;
        } else {
            this.projects.add(p);
        }
    }

    public void deleteProject (HDBManager m, Project p){
        // Do we need to remove from the list of HDBManager, do we need to keep the manager attribute as list or just project
        m.removeCreatedProjects(p);
        this.projects.remove(p);

    }

    public <T> void editProject (HDBManager manager, Project p,  String field, T info){
        if (field.equals("name")){
            p.setName((String)info);
        } else if (field.equals("location")){
            p.setLocation((String)info);
        } else if (field.equals("openDate")){
            p.setOpenDate((Date) info);
        } else if (field.equals("closeDate")){
            p.setCloseDate((Date) info);
        } else if (field.equals("officerSlot")){
            p.setOfficerSlot((int) info);
        } else {
            return;
        }
    }

    public void editFlatTypeAvailable(Project p, Map <String, Integer> k){
        p.setFlatTypeAvailable(k);
    }

    public void editFlatPrices (Project p, Map <String, Integer> k){
        p.setFlatPrices(k);
    }

    public void processApplication (HDBManager manager, Applicant a, String flatType, String status){
        if (a.getAppliedProject().getFlatTypeAvailable().get(flatType) >= 0){
            a.setApplicationStatus(status);
        }

    }

    public List <HDBOfficer> getRegistrations(Project p){
        return p.getOfficers();
    }

    public List <Applicant> getApplications (){
        return this.applicants;
    }

    public void processRegistrations(HDBManager manager, Project p, HDBOfficer officer, String status){
        if (status == "Approved"){
            p.removeTemporaryOfficer(officer);
            p.addOfficer(officer);
            officer.setRegistrationStatus(status);
        	System.out.print("Status updated.");
        } else if (status == "Denied"){
            p.removeTemporaryOfficer(officer);
            officer.setRegistrationStatus(status);
        	System.out.print("Status updated.");
        } else{
            System.out.println("Invalid Status");
        }
    }

    public void processWithdrawal(HDBManager manager, Applicant a, String status){
        if (status.equals("approved")){
            a.setWithdrawalStatus(status);
        } else if (status.equals("denied")){
            a.setWithdrawalStatus(status);
        } else{
            return;
        }
    }
}