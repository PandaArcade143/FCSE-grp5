package boundary;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import control.ProjectController;
import control.InquiryController;
import entity.Applicant;
import entity.HDBOfficer;
import entity.Inquiry;
import entity.Project;

public class ApplicantUI {

    //Display menu for an Applicant and handle their actions
    public void showMenu(Applicant applicant) {
        ProjectController projectController = new ProjectController();
        Scanner scanner = new Scanner(System.in);
        List<Inquiry> inquiries = InquiryController.viewInquiries(applicant.getNRIC());
        

        String status;
        
        while (true) {
        	List<Project> projectList = projectController.getFilteredProjects(applicant);
            // Display main menu options
        	System.out.println("\n\n\nApplicant Menu:");
            System.out.println("1. View available BTO projects");
            System.out.println("2. Apply for a project");
            System.out.println("3. View details of project applied for and application status");
            System.out.println("4. Withdraw my application");
            System.out.println("5. Submit an inquiry");
            System.out.println("6. View and manage inquiries");
            System.out.println("7. Filter projects");
            System.out.println("8. Switch menus");
            System.out.println("9. Quit");
            System.out.print("\nSelect an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                // Handle non-integer input
                System.out.println("\nInvalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    // Display all currently available projects for this applicant
                    System.out.println("\nProjects available:");
                    for (Project project : projectList) {
                    	if (project != null) {
                    		if (applicant.getRole() == "HDBOfficer") {
                    			if (((HDBOfficer) applicant).getRegisteredProjects() != null) {
	                    			if (project.getName().equals(((HDBOfficer) applicant).getRegisteredProjects().getName())) {
	                    				continue;
	                    			}
                    			}
                    		}
    	                	System.out.println("\nProject Name: " + project.getName());
    	                	System.out.println("Neighborhood: " + project.getLocation());
    	                    
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
    	                	
                    	} else {
                            System.out.println("\nYou are not allowed to view any project.");
                        }
                    }
                    break;

                case 2:
                    // Prompt applicant to enter project name to apply to
                    System.out.print("\nEnter the name of the project you wish to apply for: ");
                    String projectName = scanner.nextLine().trim();
                    boolean applied = projectController.applyToProject(applicant, projectName);
                    if (applied) {
                        System.out.println("\nProject successfully applied!");
                    } else {
                        System.out.println("\nUnable to apply for project. Check eligibility or application status.");
                    }
                    break;

                case 3:
                    // View the details and status of the current application
                	Project project = applicant.getAppliedProject();
                	if (project != null) {
	                	System.out.println("\nProject Name: " + project.getName());
	                	System.out.println("Neighborhood: " + project.getLocation());
	                    
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
	                	System.out.println("Current application status: " + applicant.getApplicationStatus());
	                	
                	} else {
                        System.out.println("\nYou did not apply for any project.");
                    }
                	break;

                case 4:
                    // Attempt to withdraw current application
                	status = applicant.getApplicationStatus();
                	if (status != null) {
                		if (status.equalsIgnoreCase("withdrawing")) {
                			System.out.println("\nApplication already pending withdrawal.");
                		} else {
                			applicant.setApplicationStatus("Withdrawing");
                			System.out.println("\nApplication is now pending for withdrawal.");
                		}
                    } else {
                        System.out.println("\nNo application found.");
                    }
                    break;

                case 5:
                    // Allow user to submit an inquiry about a selected project
                	System.out.print("\nProjects available:\n");
                    projectList = projectController.getAvailableProjects(applicant); // refresh project list
                    int j = 0;
                    for (int i = 0; i < projectList.size(); i++) {
                    	if (applicant.getRole() == "HDBOfficer" && projectList.get(i).getName().equals(((HDBOfficer) applicant).getRegisteredProjects().getName())) {
                			j += 1;
                    		continue;
                		}
                        System.out.printf("%d. %s%n", i + 1 - j, projectList.get(i).getName());
                    }
                    System.out.print("\nSelect project number to inquire: ");
                    int projectIndex;
                    try {
                        projectIndex = Integer.parseInt(scanner.nextLine()) - 1 + j;
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid number.");
                        break;
                    }
                    if (projectIndex < 0 || projectIndex  >= projectList.size()) {
                        System.out.println("\nInvalid project selection.");
                        break;
                    }
                    Project selectedProject = projectList.get(projectIndex);
                    System.out.print("\nSubject: ");
                    String subject = scanner.nextLine().trim();
                    System.out.print("\nMessage: ");
                    String message = scanner.nextLine().trim();
                    System.out.print("\n\n");
                    InquiryController.createInquiry(applicant.getNRIC(), subject, selectedProject, message);
                    break;

                case 6:
                    // Allow applicant to view, edit, or delete their own inquiries
                    inquiries = InquiryController.viewInquiries(applicant.getNRIC());
                	if (inquiries.size() == 0) {
                        System.out.println("\nNo inquiries found.");
                        break;
                    }
                    System.out.println("\nYour inquiries:\n");
                    for (int i = 0; i < inquiries.size(); i++) {
                        Inquiry iq = inquiries.get(i);
                        System.out.printf("%d. [%s] %s - %s%n\t%s\n", i + 1, iq.getStatus(), iq.getSubject(), iq.getMessage(), "Reply: " + iq.getReply());
                    }
                    // Inquiry management sub-menu
                    System.out.println("\n\nInquiry Menu:");
                    System.out.println("1. Edit an inquiry");
                    System.out.println("2. Delete an inquiry");
                    System.out.println("4. Back");
                    System.out.println("\nSelect an option: ");
                    int actionChoice;
                    try {
                        actionChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid choice.");
                        break;
                    }
                    switch (actionChoice) {
                        case 1:
                            // Edit selected inquiry
                            System.out.print("\nEnter inquiry number to edit: ");
                            int inquiryIndex;
                            try {
                                inquiryIndex = Integer.parseInt(scanner.nextLine()) - 1;
                            } catch (NumberFormatException e) {
                                System.out.println("\nInvalid number.");
                                break;
                            }
                            if (inquiryIndex < 0 || inquiryIndex >= inquiries.size()) {
                                System.out.println("\nInvalid selection.");
                                break;
                            }
                            Inquiry toEdit = inquiries.get(inquiryIndex);
                            System.out.print("\nNew message: ");
                            String newMsg = scanner.nextLine().trim();
                            InquiryController.editInquiry(applicant.getNRIC(), toEdit, newMsg);
                            break;

                        case 2:
                            // Delete selected inquiry
                            System.out.print("\nEnter inquiry number to delete: ");
                            int deleteIndex;
                            try {
                                deleteIndex = Integer.parseInt(scanner.nextLine()) - 1;
                            } catch (NumberFormatException e) {
                                System.out.println("\nInvalid number.");
                                break;
                            }
                            if (deleteIndex < 0 || deleteIndex >= inquiries.size()) {
                                System.out.println("\nInvalid selection.");
                                break;
                            }
                            InquiryController.deleteInquiry(applicant.getNRIC(), inquiries.get(deleteIndex));
                            break;

                        case 3:
                            // Return to main menu
                            break;

                        default:
                            System.out.println("\nInvalid option.");
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
                	// Switch menu if applicant has access
                    if (applicant.getRole().equals("HDBOfficer")) {
                        System.out.println("\n\nSwitching to Officer Menu...");
                        new HDBOfficerUI().showMenu((HDBOfficer) applicant);
                        return;
                    } else {
                        System.out.println("\nYou do not have access outside of Applicant Menu.");
                    }
                    break;
                    
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
