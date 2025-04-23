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
        Project registeredProject = null;
        hdbofficer.setRole("HDBOfficer");
        
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
                    if (registeredProject == null) {
                    	System.out.println("\nNo project with that name found");
                    	break;
                    }
                    // Displays message depending on whether project is successfully applied
                    if (hdbofficer.getAppliedProject() == registeredProject) {
                        System.out.println("\nProject is unable to be registered for as you are already applying as an applicant.");
                    } else if (hdbofficer.getApprovedProjects().contains(registeredProject) || hdbofficer.getPendingProjects().contains(registeredProject)){
                        System.out.println("\nYou have already registered for this project.");
                    } else if (registeredProject.getOfficerSlot() == registeredProject.getOfficers().size()){
                    	System.out.println("\nNo free officer slots left for this project.");
                    } else if (!hdbofficer.isEligibleForRegistration(registeredProject)) {
                    	System.out.println("\nYou have already registered for another project which overlaps.");
                    } else {
                    	System.out.println("\nProject successfully registered!");
                        registeredProject.addTemporaryOfficer(hdbofficer);
                        hdbofficer.addRegisteredProjects(registeredProject);
                        hdbofficer.addPendingProject(registeredProject);
                    }
                    break;
                    
                case 3:
                	// View registration status
                	System.out.println("Approved Projects:");
                	hdbofficer.getApprovedProjects().forEach(p -> System.out.printf("Name: %s\nPeriod: %s to %s\n\n", p.getName(), p.getOpenDate().toString(), p.getCloseDate().toString()));
                	System.out.println("Pending Projects:");
                    hdbofficer.getPendingProjects().forEach(p -> System.out.printf("Name: %s\nPeriod: %s to %s\n\n" ,p.getName(), p.getOpenDate().toString(), p.getCloseDate().toString()));

                    break;
                    
                case 4:
                	// View project details
                	for (Project p : hdbofficer.getApprovedProjects()) {
	                	System.out.println("\nProject Name: " + p.getName());
	                	System.out.println("Neighborhood: " + p.getLocation());
	                	System.out.println("Flat types and total number of units for corresponding types:");
	                	for (Map.Entry<String, Integer> pair : p.getFlatTypeTotal().entrySet()) {
	                       System.out.println(" - Flat type: " + pair.getKey() + ", total number of units: " + pair.getValue());
	                    }
	                	System.out.println("Flat types and available number of units left for corresponding types:");
	                	for (Map.Entry<String, Integer> pair : p.getFlatTypeAvailable().entrySet()) {
	                       System.out.println(" - Flat type: " + pair.getKey() + ", available number of units left: " + pair.getValue());
	                    }
	                	System.out.println("Flat types and prices for corresponding types:");
	                	for (Map.Entry<String, Integer> pair : p.getFlatPrices().entrySet()) {
	                       System.out.println(" - Flat type: " + pair.getKey() + ", selling price: " + pair.getValue());
	                    }
	                	System.out.println("Application opening date: " + p.getOpenDate());
	                	System.out.println("Application closing date: " + p.getCloseDate());
	                	System.out.println("Manager of project: " + p.getManager());
	                	System.out.println("Officer slots for project: " + p.getOfficerSlot());
	                	System.out.println("Officers of project: ");
	                	for (HDBOfficer officer: p.getOfficers()) {
	                		System.out.println(" - " + officer.getName());
	                    }
                    }
                	break;
                	
                case 5:
                	// Respond to inquiries
                	System.out.println("Current Projects: ");
                	hdbofficer.getApprovedProjects().forEach(p->System.out.println(p.getName()));
                	System.out.println("Select which project to reply inquiries for: ");
                	String proj = scanner.nextLine();
                	Optional<Project> s = hdbofficer.getApprovedProjects().stream()
                			.filter(p -> p.getName().equals(proj))
                			.findFirst();
                	if (!s.isPresent()) {
                		System.out.println("No matching project with that name.");
                		break;
                	} else {
                		registeredProject = s.get();
                	}

                	List<Inquiry> inquiries = InquiryController.viewInquiries(registeredProject);
                	inquiries = inquiries.stream()
                			.filter(i -> i.getStatus().equals("Open"))
                			.collect(Collectors.toList());

                	if (inquiries.isEmpty()) {
                		System.out.println("\nNo inquiries found.");
                	} else {
                		System.out.println("\nInquiries for project " + registeredProject.getName() + ":\n");
                		for (int i = 0; i < inquiries.size(); i++) {
                		    Inquiry inquiry = inquiries.get(i);
                		    System.out.println((i + 1) + ". " + inquiry.getSubject() + ": " + inquiry.getMessage());
                		}
                		System.out.print("Enter which inquiry to respond to or type 'Back' to return: ");                	

                		String input = scanner.nextLine();
                		if (input.equalsIgnoreCase("Back")) {
                		    break;
                		}
                		try {
                		    int index = Integer.parseInt(input) - 1;

                		    if (index >= 0 && index < inquiries.size()) {
                		        Inquiry selectedInquiry = inquiries.get(index);
                		        // Now proceed to respond to selectedInquiry
                		        System.out.print("\nEnter your reply: ");
                		        String replyMessage = scanner.nextLine();
                		        System.out.print("\n\n\n");
                		        InquiryController.replyToInquiry(selectedInquiry.getInquiryId(), replyMessage);
                		        InquiryController.resolveInquiry(selectedInquiry.getInquiryId());
                		    } else {
                		        System.out.println("Invalid inquiry number.");
                		    }
                		} catch (NumberFormatException e) {
                		    System.out.println("Please enter a valid number.");
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