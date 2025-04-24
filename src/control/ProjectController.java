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

/**
 * Handles project-related logic such as filtering, creation, application,
 * booking, and modification of BTO projects for applicants, officers, and managers.
 */
public class ProjectController {
	
    /** List of all projects in the system */	
    private List <Project> projects = DataManager.getProjects();
    /** List of all applicants in the system */
    private List <Applicant> applicants = DataManager.getApplicants();
    /** Map containing current active filters for searching projects */
    private Map<String, String> filters;

    /**
     * Constructs a ProjectController and initializes default filters.
     */
    public ProjectController(){
    	this.filters = new HashMap<>();
    	filters.put("location", "");
    	filters.put("flatType", "");
    	filters.put("maxPrice", "");
    	filters.put("minPrice", "");
    }
    
    /**
     * Returns a list of flat types available for the given applicant based on 
     * their age and marital status.
     * <p>
     * The eligibility rules are as follows:
     * <ul>
     *   <li>If the applicant is 35 or older and single, they are eligible for a 
     *       2-Room flat if it is available.</li>
     *   <li>If the applicant is 21 or older and married, they are eligible for a 
     *       2-Room flat if available, and additionally for a 3-Room flat if the 
     *       2-Room is also available.</li>
     * </ul>
     * 
     * @param a the applicant whose eligibility and flat availability is being checked
     * @return a list of flat types the applicant is eligible to apply for
     */ 
    public List<String> getFlatAvailability(Applicant a) {
    	List<String> flats = new ArrayList<String>();
    	Project p = a.getAppliedProject();

    	if (a.getAge() >= 35 && "single".equalsIgnoreCase(a.getMaritalStatus())){
    		if (p.getFlatTypeAvailable().get("2-Room") > 0) {
    			flats.add("2-Room");
    		}
    	} else if (a.getAge() >= 21 && "married".equalsIgnoreCase(a.getMaritalStatus())) {
    		if (p.getFlatTypeAvailable().get("2-Room") > 0) {
    			flats.add("2-Room");
    		}
    		if (p.getFlatTypeAvailable().get("2-Room") > 0) {
    			flats.add("3-Room");
    		}
    	}
    	return flats;
    }

    /**
     * Applies a location filter.
     * @param location the location to filter by
     */ 
    public void filterByLocation(String location) {
    	filters.put("location", location);
    }
    /**
     * Applies a flat type filter.
     * @param flatType the flat type to filter by (e.g. "3-Room")
     */ 
    public void filterByFlatType(String flatType) {
    	filters.put("flatType", flatType);
    }
    /**
     * Applies a maximum price filter.
     * @param maxPrice the maximum flat price allowed
     */ 
    public void filterByMaxPrice(int maxPrice) {
    	filters.put("maxPrice", Integer.toString(maxPrice));
    }
    /**
     * Applies a minimum price filter.
     * @param minPrice the minimum flat price allowed
     */ 
    public void filterByMinPrice(int minPrice) {
    	filters.put("minPrice", Integer.toString(minPrice));
    }
    /**
     * Retrieves the current active filter settings.
     * @return map of filter keys and their values
     */ 
    public Map<String, String> getFilters() {
    	return filters;
    }
    
    /**
     * Returns a list of filtered projects based on user role and active filters.
     * Applicants will only see projects they're eligible for.
     * Officers see their assigned projects and all visible ones.
     * Managers see all projects.
     *
     * @param user the user requesting the project list
     * @return a list of projects filtered by eligibility and criteria
     */
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
    /**
     * Returns a list of filtered projects based on an existing list of projects and active filters.
     * This method is useful for refining a pre-filtered set of projects.
     *
     * @param p the list of projects to further filter
     * @return a list of projects after applying all filters
     */
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

    /**
     * Returns a list of available projects for the given user based on their role and eligibility.
     * Applicants see projects that match age, marital status, and flat type.
     * Officers see visible projects and those they are assigned to.
     * Managers see all projects.
     *
     * @param user the user requesting the list of available projects
     * @return list of projects visible or applicable to the user
     */ 
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

    /**
     * Allows an applicant to apply to a project based on role eligibility and project name.
     * HDB Officers are restricted from applying to projects they manage.
     * Eligibility is based on age, marital status, and project requirements.
     *
     * @param a the applicant attempting to apply
     * @param projectName the name of the project to apply for
     * @return true if application is accepted, false otherwise
     */
    public boolean applyToProject (Applicant a, String projectName){
        //Check if already applied project
        if ("Pending".equals(a.getApplicationStatus()) || "Successful".equals(a.getApplicationStatus()) || "Booked".equals(a.getApplicationStatus()) || "Withdrawing".equals(a.getApplicationStatus())){
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

    /**
     * Books a flat of a specific type for an applicant based on NRIC.
     * Updates application status and flat count.
     *
     * @param officer the HDB officer performing the booking
     * @param n the NRIC of the applicant
     * @param flatType the flat type to book (e.g., "3-Room")
     */
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

    /**
     * Toggles the visibility of a project.
     *
     * @param p the project to update
     * @param a the visibility status to set
     */
    public void toggleProject(Project p, boolean a){
        p.toggleVisibility(a);
    }

    /**
     * Creates a new project and adds it to both the manager's created list and the global list.
     *
     * @param m the manager creating the project
     * @param p the project to be created
     */
    public void createProject (HDBManager m, Project p){
        m.addCreatedProjects(p);
        if (this.projects.contains(p)){
            return;
        } else {
            this.projects.add(p);
        }
    }
    
    /**
     * Deletes a project from both the manager's project list and the system-wide list.
     *
     * @param m the manager requesting deletion
     * @param p the project to delete
     */
    public void deleteProject (HDBManager m, Project p){
        // Do we need to remove from the list of HDBManager, do we need to keep the manager attribute as list or just project
        m.removeCreatedProject(p);
        this.projects.remove(p);

    }
    /**
     * Edits a specific field of a project based on field name and value.
     * Supports editing name, location, dates, and officer slot count.
     *
     * @param manager the manager editing the project
     * @param p the project to edit
     * @param field the field name to edit (e.g., "name", "location")
     * @param info the new value to assign to the field
     * @param <T> the type of value to be passed (e.g., String, Date, Integer)
     */
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

    /**
     * Updates the map of available flat types and their counts for a project.
     *
     * @param p the project to update
     * @param k the map of flat types and availability to set
     */
    public void editFlatTypeAvailable(Project p, Map <String, Integer> k){
        p.setFlatTypeAvailable(k);
    }

    /**
     * Updates the flat price map for a project.
     *
     * @param p the project to update
     * @param k the map of flat types and prices to set
     */
    public void editFlatPrices (Project p, Map <String, Integer> k){
        p.setFlatPrices(k);
    }

    /**
     * Processes an application by assigning an application status to the applicant
     * if the requested flat type is still available in the project.
     *
     * @param manager the HDB manager handling the application
     * @param a the applicant whose application is being processed
     * @param flatType the flat type requested by the applicant
     * @param status the new application status (e.g., "Approved", "Rejected")
     */
    public void processApplication (HDBManager manager, Applicant a, String flatType, String status){
        if (a.getAppliedProject().getFlatTypeAvailable().get(flatType) >= 0){
            a.setApplicationStatus(status);
        }

    }

    /**
     * Returns a list of HDB officers registered to a given project.
     *
     * @param p the project to query
     * @return a list of assigned HDB officers
     */
    public List <HDBOfficer> getRegistrations(Project p){
        return p.getOfficers();
    }
    
    /**
     * Returns the list of all applicants stored in memory.
     *
     * @return a list of all applicants
     */
    public List <Applicant> getApplications (){
        return this.applicants;
    }

    /**
     * Processes the registration decision of an officer for a project.
     * Updates the officer's registration status and moves them into the official list.
     *
     * @param manager the manager approving or denying the request
     * @param p the project the officer applied to
     * @param officer the officer whose registration is being processed
     * @param status the registration status to set ("Approved" or "Denied")
     */
    public void processRegistrations(HDBManager manager, Project p, HDBOfficer officer, String status){
        if (status.equalsIgnoreCase("approved")){
            p.removeTemporaryOfficer(officer);
            p.addOfficer(officer);
            officer.setRegistrationStatus("Approved");
        	System.out.print("Status updated.");
        } else if (status.equalsIgnoreCase("denied")){
            p.removeTemporaryOfficer(officer);
            officer.setRegistrationStatus("Denied");
        	System.out.print("Status updated.");
        } else{
            System.out.println("Invalid Status");
        }
    }
    
    /**
     * Processes a withdrawal request from an applicant.
     *
     * @param manager the manager processing the request
     * @param a the applicant requesting withdrawal
     * @param status the status to apply ("approved" or "denied")
     */
    public boolean processWithdrawal(HDBManager manager, Applicant a, String status){

        if (status.equals("approved")){
            a.setWithdrawalStatus(status);
            a.setApplicationStatus("Withdrawn");
            return true;
        } else if (status.equals("denied")){
            a.setWithdrawalStatus(status);
            a.setApplicationStatus("RejectWithdrawal");
            return true;
        } else{
            return false;
        }
    }
}