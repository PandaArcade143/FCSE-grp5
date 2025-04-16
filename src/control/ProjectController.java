package control;
import entity.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import helpers.DataManager;

public class ProjectController <T>{
    private List <Project> projects = DataManager.getProjects();
    private List <Applicant> applicants = DataManager.getApplicants();
    private List <HDBOfficer> officers = DataManager.getOfficers();
    private List <HDBManager> manager = DataManager.getManagers();

    public ProjectController(){

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
        if (a.getApplicationStatus().equals("Pending") || a.getApplicationStatus().equals("Successful") || a.getApplicationStatus().equals("Booked")){
            return false;
        }
        
        //Check for HDBOfficer
        if (a.getRole().equals("HDBOfficer")){
            for (Project n : this.projects){
                if (n.getName().equals(projectName)){
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
                    if (n.getName().equals(projectName)){
                        a.setAppliedProject(n);
                        a.setApplicationStatus("Pending");
                        return true;
                    }
                }

            //Single, 35 and above
            } else if (a.getAge() >= 35 && a.getMaritalStatus().equals("Single")){


                for (Project n : this.projects){
                	System.out.println("Current: " + n.getName());
                	System.out.println("comparing to: " + projectName);
                    if (n.getName().equals(projectName)){
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
        //CHange the HDBMAnager class to just one project, change the method for setting 
        m.setCreatedProjects(p);
        if (this.projects.contains(p)){
            return;
        } else {
            this.projects.add(p);
        }
    }

    public void deleteProject (HDBManager m, Project p){
        // Do we need to remove from the list of HDBManager, do we need to keep the manager attribute as list or just project
        m.setCreatedProjects(null);
        this.projects.remove(p);

    }

    public void editProject (HDBManager manager, Project p,  String field, T info){
        if (field == "name"){
            p.setName((String)info);
        } else if (field == "location"){
            p.setLocation((String)info);
        } else if (field == "openDate"){
            p.setOpenDate((Date) info);
        } else if (field == "closeDate"){
            p.setCloseDate((Date) info);
        } else if (field == "officerSlot"){
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

    public void processRegistrations(HDBManager manager, HDBOfficer officer, String status){
        if (status == "Approved"){
            manager.getCreatedProjects().removeTemporaryOfficer(officer);
            manager.getCreatedProjects().addOfficer(officer);
            officer.setRegistrationStatus(status);
        	System.out.print("Status updated.");
        } else if (status == "Denied"){
            manager.getCreatedProjects().removeTemporaryOfficer(officer);
            officer.setRegistrationStatus(status);
        	System.out.print("Status updated.");
        } else{
            System.out.println("Invalid Status");
        }
    }
}