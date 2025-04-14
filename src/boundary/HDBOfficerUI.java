package boundary;

import java.util.*;

import control.InquiryController;
import control.ProjectController;
import entity.Applicant;
import entity.HDBOfficer;
import entity.Inquiry;
import entity.Project;
import entity.User;

public class HDBOfficerUI {
	ProjectController projectController = new ProjectController();
    InquiryController inquiryController = new InquiryController();
    
    
    public void showMenu(HDBOfficer officer, List<Applicant> applicantList) {
        Scanner scanner = new Scanner(System.in);
        List<Project> projectList = projectController.getAvailableProjects(officer); // Fetches list of available projects
        Project registeredProject = officer.getRegisteredProjects();
        
        // Display menu
        System.out.println("HDB Officer Menu:");
        System.out.println("1. View available BTO projects");
        System.out.println("2. Register for a project");
        System.out.println("3. View registration status for projects");
        System.out.println("4. View project details");
        System.out.println("5. Respond to enquiries");
        System.out.println("6. Update BTO status and generate applicant receipt");
        System.out.println("7. Quit");
        System.out.print("Select an option: ");
        
        while (true) {
        	int choice = scanner.nextInt();
        	//Quit the program
        	if (choice == 7) {
            	System.out.println("Quit successful.");
            	break;	
        	}
            switch (choice) {
            	// Display list of available projects
                case 1:// Loops through list of projects and prints out their names
                	System.out.println("Projects available:");
                	for (Project project : projectList) {
                        System.out.println(project.getName());
                    }
                    break;
                    
                // HDB officer registers for a project
                case 2:
                	System.out.println("Enter the name of the project you wish to register for: ");
                    String projectName = scanner.nextLine(); // Applicant inputs the name of the project they wish to apply
                    Date currDate = new Date();
                    for (Project project : projectList) {
                        if (project.getName().compareToIgnoreCase(projectName) == 0) {
                        	registeredProject = project;
                        }
                    }
                    // Displays message depending on whether project is successfully applied
                    if (officer.isEligibleForRegistration(registeredProject) == false) {
                        System.out.println("Project is unable to be registered for as you are already applying as an applicant.");
                    } else if (projectName == officer.getRegisteredProjects().getName()){
                        System.out.println("You have already registered for this project.");
                    } else if (officer.getRegisteredProjects() != null && !officer.getRegisteredProjects().getOpenDate().after(currDate) && !officer.getRegisteredProjects().getCloseDate().before(currDate)) {
                    	System.out.println("You have already registered for another project.");
                    } else if (registeredProject.getOfficerSlot() == registeredProject.getOfficers().size()){
                    	System.out.println("No free officer slots left for this project.");
                    } else{
                    	System.out.println("Project successfully registered!");
                    	//NOT SURE HOW TO REGISTER HDBOFFICER
                    	//registeredProject.addTemporaryOfficer();
                    }
                    break;
                case 3:
                	System.out.println("Registration status:");
                	//WHAT IS THE KEY VALUE IN THE ARGUMENT LIST??
                	//System.out.println(officer.getRegistrationStatus())
                    break;
                case 4:
                	System.out.println("Project Name: " + registeredProject.getName());
                	System.out.println("Neighborhood: " + registeredProject.getLocation());
                	System.out.println("Flat types and total number of units for corresponding types:");
                	for (Map.Entry<String, Integer> pair : registeredProject.getFlatTypeTotal().entrySet()) {
                       System.out.print("	Flat type: " + pair.getKey() + ", total number of units: " + pair.getValue());
                    }
                	System.out.println("Flat types and available number of units left for corresponding types:");
                	for (Map.Entry<String, Integer> pair : registeredProject.getFlatTypeAvailable().entrySet()) {
                       System.out.print("	Flat type: " + pair.getKey() + ", available number of units left: " + pair.getValue());
                    }
                	System.out.println("Flat types and prices for corresponding types:");
                	for (Map.Entry<String, Integer> pair : registeredProject.getFlatPrices().entrySet()) {
                       System.out.print("	Flat type: " + pair.getKey() + ", selling price: " + pair.getValue());
                    }
                	System.out.println("Application opening date: " + registeredProject.getOpenDate());
                	System.out.println("Application closing date: " + registeredProject.getCloseDate());
                	System.out.println("Manager of project: " + registeredProject.getManager());
                	System.out.println("Officer slots for project: " + registeredProject.getOfficerSlot());
                	System.out.println("Officers of project: ");
                	for (String hdbofficer: registeredProject.getOfficers()) {
                		System.out.println("	" + hdbofficer);
                    }
                case 5:
                	System.out.print("Inquiry ID and messages:");
                	for (Inquiry inquiry: InquiryController.viewInquiries(registeredProject)) {
                		System.out.println(inquiry.getInquiryId() + ": " + inquiry.getMessage());
                    }
                	System.out.print("Enter Inquiry ID to respond to:");
                	String inquiryID = scanner.nextLine();
                	System.out.print("Enter reply message: ");
                	String replyMessage = scanner.nextLine();
                	InquiryController.replyToInquiry(inquiryID, replyMessage);
                case 6:
                	Map<String, Integer> flatTypeAvailable = registeredProject.getFlatTypeAvailable();

                	while (true) {
                		System.out.print("Enter flat type to update: ");
                    	String flatType = scanner.nextLine();
                    	System.out.print("Enter new available number of flats: ");
                    	int availableFlats = scanner.nextInt();
                    	if (flatTypeAvailable.containsKey(flatType)) {
                        	flatTypeAvailable.put(flatType, availableFlats);
                        	registeredProject.setFlatTypeAvailable(flatTypeAvailable);
                        	break;
                    	} else {
                    		System.out.print("No such flat type found in the project.");
                    	}
                	}

                	Applicant applicant = null;
                	
                	while (true) {
                    	System.out.print("Enter applicant's NRIC: ");
                    	String nric = scanner.nextLine();
                    	if (nric.matches("[ST]\\d{7}[A-Z]")) {
                        	//check if nric is valid
                        	System.out.println("Invalid NRIC given, please try again.");
                        } else {
                        	while (true) {
                            	for (Applicant a: applicantList) {
                                    if (a.getNRIC().equalsIgnoreCase(nric)) {
                                        applicant = a;
                                    }
                                }
                            	if (applicant != null) {
                            		System.out.println(applicant.getName() + " with NRIC of " + applicant.getNRIC() + " has a BTO status of " + applicant.getApplicationStatus());
                            		System.out.print("Enter new status: ");
                                	String status = scanner.nextLine();
                                	applicant.setApplicationStatus(status);
                                	System.out.print("Status updated.");
                                	System.out.print("Enter flat type of applicant: ");
                                	String flatType = scanner.nextLine();
                                	//METHOD NOT DEFINED BUT IN UML
                                	//applicant.setFlatType(flatType);
                                	System.out.print("Flat type updated.");
                            		break;
                            	} else {
                            		System.out.println("No applicant with this NRIC was found, please try again.");
                            		System.out.print("Enter applicant's NRIC: ");
                                	nric = scanner.nextLine();
                            	}
                        	}
                        	break;
                        }
                	}
                	
                	System.out.println("Applicant's name: " + applicant.getName());
                	System.out.println("Applicant's NRIC: " + applicant.getNRIC());
                	System.out.println("Applicant's age: " + applicant.getAge());
                	System.out.println("Applicant's marital status: " + applicant.getMaritalStatus());
                	//METHOD NOT DEFINED BUT IN UML
                	//System.out.println("Applicant's flat type booked: " + applicant.getFlatType());
                	System.out.println("Project Name: " + registeredProject.getName());
                	System.out.println("Neighborhood: " + registeredProject.getLocation());
                	
                	break;
                	
                default:
                    System.out.println("Invalid option.");
            }
        }
        
        
        scanner.close();
    }
}