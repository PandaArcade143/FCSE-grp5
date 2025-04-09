package control;
import entity.*;
import java.util.List;
import java.util.ArrayList;
import helpers.DataManager;

public class ProjectController {
    private List <Project> projects = DataManager.getProjects();
    private List <Applicant> applicants = DataManager.getApplicants();
    private List <HDBOfficer> officers = DataManager.getOfficers();
    private List <HDBManager> manager = DataManager.getManagers();

    public ProjectController(){

    }

    public List <Project> getAvailableProjects(User user){
        List <Project> store = new ArrayList <> ();
        //Applicant type
        if (user.getRole() == "Applicant"){
            //Check for user details to determine
            //Single, 35 and above
            if (user.getMaritalStatus() == "Single" && user.getAge() >= 35){
                //Check for visibility and 2 room flats
                for (Project a : this.projects){
                    if (a.getVisibility() == true && a.getFlatTypeAvailable().get("2 room") != null){
                        store.add(a);
                    }
                }
                return store;

            //Married, 21 and above
            } else if (user.getMaritalStatus() == "Married" && user.getAge() >= 21){
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
        } else if (user.getRole() == "HDBOfficer"){
            //Able to view projects they are handling regardless of their visibility and also projects that are visiblie
            for (Project a : this.projects){
                if (a.getVisibility() == true || a.getOfficers().contains(user)){
                    store.add(a);
                }
            }
            return store;

        //Manager type
        } else if (user.getRole() == "HDBManager"){
            return this.projects;
        }
    //If nothing matches, then return empty list
    return store;
    }

    public boolean applyToProject (Applicant a, String projectName){
        //Check if already applied project
        if (a.getApplicationStatus() == "Pending" || a.getApplicationStatus() == "Successful" || a.getApplicationStatus() == "Booked"){
            return false;
        }
        
        //Check for HDBOfficer
        if (a.getRole() == "HDBOfficer"){
            for (Project n : this.projects){
                if (n.getName() == projectName){
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
            if (a.getAge() >= 21 && a.getMaritalStatus() == "Married"){
                for (Project n : this.projects){
                    if (n.getName() == projectName){
                        a.setAppliedProject(n);
                        a.setApplicationStatus("Pending");
                        return true;
                    }
                }

            //Single, 35 and above
            } else if (a.getAge() >= 35 && a.getMaritalStatus() == "Single"){
                for (Project n : this.projects){
                    if (n.getName() == projectName){
                        //Check if there is 2 room flats
                        if (n.getFlatTypeAvailable().get("2 room") != null){
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
}