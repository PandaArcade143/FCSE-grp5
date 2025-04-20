package boundary;

import java.util.*;

import control.InquiryController;
import control.ProjectController;
import entity.Applicant;
import entity.HDBOfficer;
import entity.Inquiry;
import entity.Project;
import helpers.DataManager;

public class HDBOfficerUI {

    public void showMenu(HDBOfficer hdbofficer) {

    	ProjectController<HDBOfficer> projectController = new ProjectController<>();
        List<Applicant> applicantList = DataManager.getApplicants();
        Scanner scanner = new Scanner(System.in);
        List<Project> projectList = projectController.getAvailableProjects(hdbofficer); // Fetches list of available projects
        Project registeredProject = hdbofficer.getRegisteredProjects();
        
        while (true) {
	        // Display menu
	        System.out.println("HDB Officer Menu:");
	        System.out.println("1. View available BTO projects");
	        System.out.println("2. Register for a project");
	        System.out.println("3. View registration status for project");
	        System.out.println("4. View details of project registered for / handling.");
	        System.out.println("5. Respond to enquiries");
	        System.out.println("6. Update BTO status and generate applicant receipt");
	        System.out.println("7. Filter projects");
	        System.out.println("8. Switch menus");
	        System.out.println("9. Quit");
	        System.out.print("Select an option: ");
        	
        	int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            
        
            switch (choice) {
                case 1:
                	// Display list of available projects
                	 System.out.println("Projects available:");
                     for (Project project : projectList) {
                     	if (project != null) {
     	                	System.out.println("Project Name: " + project.getName());
     	                	System.out.println("Neighborhood: " + project.getLocation());
     	                	System.out.println("Flat types and total number of units for corresponding types:");
     	                	for (Map.Entry<String, Integer> pair : project.getFlatTypeTotal().entrySet()) {
     	                       System.out.print("	Flat type: " + pair.getKey() + ", total number of units: " + pair.getValue());
     	                    }
     	                	System.out.println("Flat types and available number of units left for corresponding types:");
     	                	for (Map.Entry<String, Integer> pair : project.getFlatTypeAvailable().entrySet()) {
     	                       System.out.print("	Flat type: " + pair.getKey() + ", available number of units left: " + pair.getValue());
     	                    }
     	                	System.out.println("Flat types and prices for corresponding types:");
     	                	for (Map.Entry<String, Integer> pair : project.getFlatPrices().entrySet()) {
     	                       System.out.print("	Flat type: " + pair.getKey() + ", selling price: " + pair.getValue());
     	                    }
     	                	System.out.println("Application opening date: " + project.getOpenDate());
     	                	System.out.println("Application closing date: " + project.getCloseDate());
     	                	System.out.println("Manager of project: " + project.getManager());
     	                	System.out.println("Officer slots for project: " + project.getOfficerSlot());
     	                	System.out.println("Officers of project: ");
     	                	for (HDBOfficer officer: project.getOfficers()) {
     	                		System.out.println("	" + officer.getName());
     	                    }
                     	} else {
                             System.out.println("You are not allowed to view any project.");
                         }
                     	break;
                     }
                     break;
                    
                case 2:
                	// HDB officer registers for a project
                	System.out.println("Enter the name of the project you wish to register for: ");
                    String projectName = scanner.nextLine().trim();
                    Date currDate = new Date();
                    for (Project project : projectList) {
                        if (project.getName().equalsIgnoreCase(projectName)) {
                        	registeredProject = project;
                        }
                    }
                    // Displays message depending on whether project is successfully applied
                    if (hdbofficer.isEligibleForRegistration(registeredProject) == false) {
                        System.out.println("Project is unable to be registered for as you are already applying as an applicant.");
                    } else if (projectName == hdbofficer.getRegisteredProjects().getName()){
                        System.out.println("You have already registered for this project.");
                    } else if (hdbofficer.getRegisteredProjects() != null && !hdbofficer.getRegisteredProjects().getOpenDate().after(currDate) && !hdbofficer.getRegisteredProjects().getCloseDate().before(currDate)) {
                    	System.out.println("You have already registered for another project.");
                    } else if (registeredProject.getOfficerSlot() == registeredProject.getOfficers().size()){
                    	System.out.println("No free officer slots left for this project.");
                    } else{
                    	System.out.println("Project successfully registered!");
                    	registeredProject.addTemporaryOfficer(hdbofficer);
                    	hdbofficer.addRegisteredProjects(registeredProject);
                    }
                    break;
                    
                case 3:
                	// View registration status
                	if (registeredProject == null) {
                        System.out.println("You are not registered for any project.");
                    } else {
                    	System.out.println("Registration status:");
                    	System.out.println(hdbofficer.getRegistrationStatus());
                    }
                	
                    break;
                    
                case 4:
                	// View project details
                	if (registeredProject != null) {
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
	                	for (HDBOfficer officer: registeredProject.getOfficers()) {
	                		System.out.println("	" + officer.getName());
	                    }
                	} else {
                        System.out.println("You are not registered for any project.");
                    }
                	break;
                	
                case 5:
                	// Respond to inquiries
                	List<Inquiry> inquiries = InquiryController.viewInquiries(registeredProject);
                    if (inquiries.isEmpty()) {
                        System.out.println("No inquiries found.");
                    } else {
                        System.out.println("Inquiries for project " + registeredProject.getName() + ":");
                        for (Inquiry inquiry : inquiries) {
                            System.out.println(inquiry.getInquiryId() + ": " + inquiry.getMessage());
                        }
                        System.out.print("Enter Inquiry ID to respond to or type 'back' to return: ");
                        String inquiryId = scanner.nextLine();
                        if (inquiryId.equalsIgnoreCase("back")) {
                            break;
                        }
                        System.out.print("Enter your reply: ");
                        String replyMessage = scanner.nextLine();
                        InquiryController.replyToInquiry(inquiryId, replyMessage);
                    }
                    break;
                    
                case 6:
                	// Update BTO status and generate receipt
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
                    	if (!nric.matches("[ST]\\d{7}[A-Z]")) {
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
                                	applicant.setFlatType(flatType);
                                	System.out.print("Flat type updated.");
                            		break;
                            	} else {
                            		System.out.println("No applicant with this NRIC was found, please try again.");
                            		break;
                            	}
                        	}
                        	break;
                        
                        }
                	}
                	
                	System.out.println("Applicant's name: " + applicant.getName());
                	System.out.println("Applicant's NRIC: " + applicant.getNRIC());
                	System.out.println("Applicant's age: " + applicant.getAge());
                	System.out.println("Applicant's marital status: " + applicant.getMaritalStatus());
                	System.out.println("Applicant's flat type booked: " + applicant.getFlatType());
                	System.out.println("Project Name: " + registeredProject.getName());
                	System.out.println("Neighborhood: " + registeredProject.getLocation());
                	
                	break;
                
                case 7:
                    // Filter project list based on user-specified criteria
                    System.out.println("Filter projects by:");
                    System.out.println("1. Location");
                    System.out.println("2. Flat Type");
                    System.out.println("3. Max Price");
                    System.out.println("4. Min Price");
                    System.out.println("5. Clear all filters");
                    System.out.print("Select a filter option: ");
                    int filterOption;
                    try {
                        filterOption = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid choice.");
                        break;
                    }
                    switch (filterOption) {
                        case 1:
                            System.out.print("Enter location: ");
                            projectController.filterByLocation(scanner.nextLine().trim());
                            break;
                        case 2:
                            System.out.print("Enter flat type: ");
                            projectController.filterByFlatType(scanner.nextLine().trim());
                            break;
                        case 3:
                            System.out.print("Enter max price: ");
                            projectController.filterByMaxPrice(Integer.parseInt(scanner.nextLine()));
                            break;
                        case 4:
                            System.out.print("Enter min price: ");
                            projectController.filterByMinPrice(Integer.parseInt(scanner.nextLine()));
                            break;
                        case 5:
                            // Reset filters by creating a new controller instance
                            projectController = new ProjectController<>();
                            break;
                            
                        default:
                            System.out.println("Invalid filter option.");
                            break;
                    }
                    break;
                    
                case 8:
                	// Switch menu to applicant
                	System.out.println("Switching to Applicant Menu...");
                    new ApplicantUI().showMenu((Applicant) hdbofficer);
                    break;

                case 9:
                    // Exit the menu and application loop
                    System.out.println("Goodbye!");
                    scanner.close();
                    break;
                	
                default:
                	// Notify user if selection is invalid
                    System.out.println("Invalid option. Please try again.");
            }
        }
        
       
    }
}