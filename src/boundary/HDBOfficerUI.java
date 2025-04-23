package boundary;

import java.util.*;
import java.util.stream.Collectors;

import control.InquiryController;
import control.ProjectController;
import entity.Applicant;
import entity.HDBOfficer;
import entity.Inquiry;
import entity.Project;
import helpers.DataManager;

public class HDBOfficerUI {

    public void showMenu(HDBOfficer hdbofficer) {

    	ProjectController projectController = new ProjectController();
        List<Applicant> applicantList = DataManager.getCombinedApplicants();
        List<HDBOfficer> hdbOfficerList = DataManager.getOfficers();
        Scanner scanner = new Scanner(System.in);
        Project registeredProject = hdbofficer.getRegisteredProjects();
        
        while (true) {
        	List<Project> projectList = projectController.getFilteredProjects(hdbofficer); // Fetches list of available projects
	        // Display menu
	        System.out.println("\n\n\nHDB Officer Menu:");
	        System.out.println("1. View available BTO projects");
	        System.out.println("2. Register for a project");
	        System.out.println("3. View registration status for project");
	        System.out.println("4. View details of project registered for / handling.");
	        System.out.println("5. Respond to enquiries");
	        System.out.println("6. Update BTO status and generate applicant receipt");
	        System.out.println("7. Filter projects");
	        System.out.println("8. Switch menus");
	        System.out.println("9. Quit");
	        System.out.println("\nSelect an option: ");
        	
        	int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.");
                continue;
            }
            
        
            switch (choice) {
                case 1:
                	// Display list of available projects
                	 System.out.println("\nProjects available:");
                     for (Project project : projectList) {
                     	if (project != null) {
     	                	System.out.println("\nProject Name: " + project.getName());
     	                	System.out.println("Neighborhood: " + project.getLocation());
     	                	System.out.println("Flat types and total number of units for corresponding types:");
     	                	for (Map.Entry<String, Integer> pair : project.getFlatTypeTotal().entrySet()) {
     	                       System.out.println(" - Flat type: " + pair.getKey() + ", total number of units: " + pair.getValue());
     	                    }
     	                	System.out.println("Flat types and available number of units left for corresponding types:");
     	                	for (Map.Entry<String, Integer> pair : project.getFlatTypeAvailable().entrySet()) {
     	                       System.out.println(" - Flat type: " + pair.getKey() + ", available number of units left: " + pair.getValue());
     	                    }
     	                	System.out.println("Flat types and prices for corresponding types:");
     	                	for (Map.Entry<String, Integer> pair : project.getFlatPrices().entrySet()) {
     	                       System.out.println(" - Flat type: " + pair.getKey() + ", selling price: " + pair.getValue());
     	                    }
     	                	System.out.println("Application opening date: " + project.getOpenDate());
     	                	System.out.println("Application closing date: " + project.getCloseDate());
     	                	System.out.println("Manager of project: " + project.getManager());
     	                	System.out.println("Officer slots for project: " + project.getOfficerSlot());
     	                	System.out.println("Officers of project: ");
     	                	for (HDBOfficer officer: project.getOfficers()) {
     	                		System.out.println(" - " + officer.getName());
     	                    }
                     	} else {
                             System.out.println("\nYou are not allowed to view any project.");
                         }
                     }
                     break;
                    
                case 2:
                	// HDB officer registers for a project
                	System.out.println("\nEnter the name of the project you wish to register for: ");
                    String projectName = scanner.nextLine().trim();
                    Date currDate = new Date();
                    for (Project project : projectList) {
                        if (project.getName().equalsIgnoreCase(projectName)) {
                        	registeredProject = project;
                        }
                    }
                    // Displays message depending on whether project is successfully applied
                    if (hdbofficer.getAppliedProject() == registeredProject) {
                        System.out.println("\nProject is unable to be registered for as you are already applying as an applicant.");
                    } else if (registeredProject == hdbofficer.getRegisteredProjects()){
                        System.out.println("\nYou have already registered for this project.");
                    } else if (registeredProject.getOfficerSlot() == registeredProject.getOfficers().size()){
                    	System.out.println("\nNo free officer slots left for this project.");
                    } else if (hdbofficer.getRegisteredProjects() != null) {
                    	if (!hdbofficer.getRegisteredProjects().getOpenDate().after(currDate) && !hdbofficer.getRegisteredProjects().getCloseDate().before(currDate)) {
                    		System.out.println("\nYou have already registered for another project.");
                    	} else {
                    		System.out.println("\nProject successfully registered!");
                        	registeredProject.addTemporaryOfficer(hdbofficer);
                        	hdbofficer.addRegisteredProjects(registeredProject);
                    	}
                    } else{
                    	System.out.println("\nProject successfully registered!");
                    	registeredProject.addTemporaryOfficer(hdbofficer);
                    	hdbofficer.addRegisteredProjects(registeredProject);
                    }
                    break;
                    
                case 3:
                	// View registration status
                	if (hdbofficer.getRegisteredProjects() != null && !hdbofficer.getRegistrationStatus().equals("Pending")) {
                		System.out.println("\nYou are already registered for the " + hdbofficer.getRegisteredProjects().getName() + " project.");
                	} else if (hdbofficer.getRegistrationStatus() == null) {
                        System.out.println("\nYou are not registered for any project or your registration has been rejected.");
                    } else {
                    	System.out.println("\nRegistered Project: " + hdbofficer.getRegisteredProjects().getName());
                    	System.out.println("Registration status: " + hdbofficer.getRegistrationStatus());
                    }
                	
                    break;
                    
                case 4:
                	// View project details
                	if (hdbofficer.getRegistrationStatus() != null) {
	                	System.out.println("\nProject Name: " + registeredProject.getName());
	                	System.out.println("Neighborhood: " + registeredProject.getLocation());
	                	System.out.println("Flat types and total number of units for corresponding types:");
	                	for (Map.Entry<String, Integer> pair : registeredProject.getFlatTypeTotal().entrySet()) {
	                       System.out.println(" - Flat type: " + pair.getKey() + ", total number of units: " + pair.getValue());
	                    }
	                	System.out.println("Flat types and available number of units left for corresponding types:");
	                	for (Map.Entry<String, Integer> pair : registeredProject.getFlatTypeAvailable().entrySet()) {
	                       System.out.println(" - Flat type: " + pair.getKey() + ", available number of units left: " + pair.getValue());
	                    }
	                	System.out.println("Flat types and prices for corresponding types:");
	                	for (Map.Entry<String, Integer> pair : registeredProject.getFlatPrices().entrySet()) {
	                       System.out.println(" - Flat type: " + pair.getKey() + ", selling price: " + pair.getValue());
	                    }
	                	System.out.println("Application opening date: " + registeredProject.getOpenDate());
	                	System.out.println("Application closing date: " + registeredProject.getCloseDate());
	                	System.out.println("Manager of project: " + registeredProject.getManager());
	                	System.out.println("Officer slots for project: " + registeredProject.getOfficerSlot());
	                	System.out.println("Officers of project: ");
	                	for (HDBOfficer officer: registeredProject.getOfficers()) {
	                		System.out.println(" - " + officer.getName());
	                    }
                	} else {
                        System.out.println("\nYou are not registered for any project or your registration has been rejected.");
                    }
                	break;
                	
                case 5:
                	// Respond to inquiries
                	if (registeredProject == null && !((hdbofficer.getRegistrationStatus()).equals("Pending"))) {
                		System.out.println("\nYou are not handling any project.");
                	} else {
                		List<Inquiry> inquiries = InquiryController.viewInquiries(registeredProject);
                        if (inquiries.isEmpty()) {
                            System.out.println("\nNo inquiries found.");
                        } else {
                            System.out.println("\nInquiries for project " + registeredProject.getName() + ":\n");
                            for (Inquiry inquiry : inquiries) {
                                System.out.println(inquiry.getInquiryId() + ": " + inquiry.getMessage());
                            }
                            System.out.print("Enter Inquiry ID to respond to or type 'Back' to return: ");
                            String inquiryId = scanner.nextLine();
                            if (inquiryId.equalsIgnoreCase("Back")) {
                                break;
                            }
                            System.out.print("\nEnter your reply: ");
                            String replyMessage = scanner.nextLine();
                            System.out.print("\n\n\n");
                            InquiryController.replyToInquiry(inquiryId, replyMessage);
                            InquiryController.resolveInquiry(inquiryId);

                        }
                        break;
                	}
                	break;
                	
                    
                case 6:
                	if (registeredProject == null || hdbofficer.getRegistrationStatus().equals("Pending")) {
                		System.out.print("\nYou are not handling any project.");
                	} else {
                		// Update BTO status and generate receipt
                    	System.out.println("Project: " + registeredProject.getName());
                    	System.out.println("Approved Applicants: ");
                    	List<Applicant> approvedApplicants = new ArrayList<Applicant>();
                    	approvedApplicants = applicantList.stream()
                    			.filter(applicant -> "successful".equalsIgnoreCase(applicant.getApplicationStatus()))
                    			.collect(Collectors.toList());
                    	for (Applicant applicant : approvedApplicants) {
                    		System.out.println("Name: " + applicant.getName());
                    		System.out.println("NRIC: " + applicant.getNRIC());
                    		System.out.println("Available Flat Types: " + projectController.getFlatAvailability(applicant).toString());
                    		System.out.println();
                    	}
                    	while (true) {
                        	System.out.print("\nEnter applicant's NRIC: ");
                        	String nric = scanner.nextLine();
                        	if (!nric.matches("[ST]\\d{7}[A-Z]")) {
                            	//check if nric is valid
                            	System.out.println("Invalid NRIC given, please try again.");
                            } else {
                            		Optional<Applicant> result = approvedApplicants.stream() 
                            				.filter(applicant -> applicant.getNRIC().equalsIgnoreCase(nric))
                            				.findFirst();
                                	if (result.isPresent()) {
                                		Applicant applicant = result.get();
                                		System.out.println("\n" + applicant.getName() + " with NRIC of " + applicant.getNRIC() + " has a BTO status of " + applicant.getApplicationStatus());
                                		while (true) {
                                			System.out.print("\nEnter flat type of applicant: ");
                                			String flatType = scanner.nextLine();
                                			if (projectController.getFlatAvailability(applicant).contains(flatType)) { 
                                				applicant.setFlatType(flatType);
                                				applicant.getAppliedProject().decrementFlat(flatType);
                                				applicant.setApplicationStatus("Booked");
                                				break;
                                			} else {
                                				System.out.print("Invalid flat type or not available. Please Try again.");
                                			}
                                    	}
                                		System.out.print("Status updated to Booked");
                                		System.out.println("\n\nApplicant Receipt");
                                		System.out.println("\nApplicant's name: " + applicant.getName());
                                		System.out.println("Applicant's NRIC: " + applicant.getNRIC());
                                		System.out.println("Applicant's age: " + applicant.getAge());
                                		System.out.println("Applicant's marital status: " + applicant.getMaritalStatus());
                                		System.out.println("Applicant's flat type booked: " + applicant.getFlatType());
                                		System.out.println("Project Name: " + registeredProject.getName());
                                		System.out.println("Neighborhood: " + registeredProject.getLocation());
                                		break;
                                	} else {
                                		System.out.println("\nNo applicant with this NRIC was found, please try again.");
                                	}
                            }
                    	}
                	}
                	break;
                	
                
                case 7:
                    // Filter project list based on user-specified criteria
                    System.out.println("\n\nFilter projects by:");
                    System.out.println("1. Location");
                    System.out.println("2. Max Price");
                    System.out.println("3. Min Price");
                    System.out.println("4. Clear all filters");
                    System.out.print("\nSelect a filter option: ");
                    
                    try {
                        int filterOption = Integer.parseInt(scanner.nextLine());
                        
                        switch (filterOption) {
                            case 1:
                                System.out.print("\nEnter location: ");
                                projectController.filterByLocation(scanner.nextLine().trim());
                                System.out.println("Location filter applied.");
                                break;
                                
                            case 2:
                                System.out.print("\nEnter max price: ");
                                try {
                                    int maxPrice = Integer.parseInt(scanner.nextLine());
                                    projectController.filterByMaxPrice(maxPrice);
                                    System.out.println("Max price filter applied.");
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid price. Please enter a number.");
                                }
                                break;
                                
                            case 3:
                                System.out.print("\nEnter min price: ");
                                try {
                                    int minPrice = Integer.parseInt(scanner.nextLine());
                                    projectController.filterByMinPrice(minPrice);
                                    System.out.println("Min price filter applied.");
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid price. Please enter a number.");
                                }
                                break;
                                
                            case 4:
                                // Clear all filters
                                projectController.getFilters().clear();
                                projectController.getFilters().put("location", "");
                                projectController.getFilters().put("flatType", "");
                                projectController.getFilters().put("maxPrice", "");
                                projectController.getFilters().put("minPrice", "");
                                System.out.println("All filters cleared.");
                                break;
                                
                                
                            default:
                                System.out.println("\nInvalid filter option.");
                        }
                        
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid choice. Please enter a number.");
                    }
                    break;
                    
                case 8:
                	// Switch menu to applicant
                	System.out.println("\n\nSwitching to Applicant Menu...");
                    new ApplicantUI().showMenu((Applicant) hdbofficer);
                    return;

                case 9:
                    // Exit the menu and application loop
                    System.out.println("\nGoodbye!");
                    scanner.close();
                    return;
                	
                default:
                	// Notify user if selection is invalid
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
        
       
    }
}