package boundary;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import control.ProjectController;
import control.InquiryController;
import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.Inquiry;
import entity.Project;
import entity.User;
import helpers.DataManager;

public class ApplicantUI {

    //Display menu for an Applicant and handle their actions
    public void showMenu(Applicant applicant) {
        ProjectController<Applicant> projectController = new ProjectController<>();
        Scanner scanner = new Scanner(System.in);
        List<Inquiry> inquiries = InquiryController.viewInquiries(applicant.getNRIC());
        List<Project> projectList = projectController.getAvailableProjects(applicant);

        String status;
        
        while (true) {
            // Display main menu options
        	System.out.println("Applicant Menu:");
            System.out.println("1. View available BTO projects");
            System.out.println("2. Apply for a project");
            System.out.println("3. View details of project applied for and application status");
            System.out.println("4. Withdraw my application");
            System.out.println("5. Submit an inquiry");
            System.out.println("6. View and manage inquiries");
            System.out.println("7. Filter projects");
            System.out.println("8. Switch menus");
            System.out.println("9. Quit");
            System.out.print("Select an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                // Handle non-integer input
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    // Display all currently available projects for this applicant
                    System.out.println("Projects available:");
                    for (Project project : projectList) {
                    	if (project != null) {
    	                	System.out.println("\nProject Name: " + project.getName());
    	                	System.out.println("Neighborhood: " + project.getLocation());
    	                    
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
    	                	
                    	} else {
                            System.out.println("You are not allowed to view any project.");
                        }
                    	break;
                    }
                    break;

                case 2:
                    // Prompt applicant to enter project name to apply to
                    System.out.print("Enter the name of the project you wish to apply for: ");
                    String projectName = scanner.nextLine().trim();
                    boolean applied = projectController.applyToProject(applicant, projectName);
                    if (applied) {
                        System.out.println("Project successfully applied!");
                    } else {
                        System.out.println("Unable to apply for project. Check eligibility or application status.");
                    }
                    break;

                case 3:
                    // View the details and status of the current application
                	Project project = applicant.getAppliedProject();
                	if (project != null) {
	                	System.out.println("Project Name: " + project.getName());
	                	System.out.println("Neighborhood: " + project.getLocation());
	                    
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
	                	System.out.println("Current application status: " + applicant.getApplicationStatus());
	                	
                	} else {
                        System.out.println("You did not apply for any project.");
                    }

                case 4:
                    // Attempt to withdraw current application
                	status = applicant.getApplicationStatus();
                	if (status != null) {
                		if (status.equals("withdrawing")) {
                			System.out.println("Application already pending withdrawal.");
                		} else {
                			applicant.setApplicationStatus("withdrawing");
                			System.out.println("Application is now pending for withdrawal.");
                		}
                    } else {
                        System.out.println("No application found.");
                    }
                    break;

                case 5:
                    // Allow user to submit an inquiry about a selected project
                    projectList = projectController.getAvailableProjects(applicant); // refresh project list
                    for (int i = 0; i < projectList.size(); i++) {
                        System.out.printf("%d. %s%n", i + 1, projectList.get(i).getName());
                    }
                    System.out.print("Select project number to inquire: ");
                    int projectIndex;
                    try {
                        projectIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number.");
                        break;
                    }
                    if (projectIndex < 0 || projectIndex >= projectList.size()) {
                        System.out.println("Invalid project selection.");
                        break;
                    }
                    Project selectedProject = projectList.get(projectIndex);
                    System.out.print("Subject: ");
                    String subject = scanner.nextLine().trim();
                    System.out.print("Message: ");
                    String message = scanner.nextLine().trim();
                    InquiryController.createInquiry(applicant.getNRIC(), subject, selectedProject, message);
                    break;

                case 6:
                    // Allow applicant to view, edit, or delete their own inquiries
                    if (inquiries.isEmpty()) {
                        System.out.println("No inquiries found.");
                        break;
                    }
                    System.out.println("Your inquiries:");
                    for (int i = 0; i < inquiries.size(); i++) {
                        Inquiry iq = inquiries.get(i);
                        System.out.printf("%d. [%s] %s - %s%n", i + 1, iq.getStatus(), iq.getSubject(), iq.getMessage());
                    }
                    // Inquiry management sub-menu
                    System.out.println("1. Edit an inquiry");
                    System.out.println("2. Delete an inquiry");
                    System.out.println("3. Back");
                    System.out.print("Select an option: ");
                    int actionChoice;
                    try {
                        actionChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid choice.");
                        break;
                    }
                    switch (actionChoice) {
                        case 1:
                            // Edit selected inquiry
                            System.out.print("Enter inquiry number to edit: ");
                            int inquiryIndex;
                            try {
                                inquiryIndex = Integer.parseInt(scanner.nextLine()) - 1;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number.");
                                break;
                            }
                            if (inquiryIndex < 0 || inquiryIndex >= inquiries.size()) {
                                System.out.println("Invalid selection.");
                                break;
                            }
                            Inquiry toEdit = inquiries.get(inquiryIndex);
                            System.out.print("New message: ");
                            String newMsg = scanner.nextLine().trim();
                            InquiryController.editInquiry(applicant.getNRIC(), toEdit, newMsg);
                            break;

                        case 2:
                            // Delete selected inquiry
                            System.out.print("Enter inquiry number to delete: ");
                            int deleteIndex;
                            try {
                                deleteIndex = Integer.parseInt(scanner.nextLine()) - 1;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number.");
                                break;
                            }
                            if (deleteIndex < 0 || deleteIndex >= inquiries.size()) {
                                System.out.println("Invalid selection.");
                                break;
                            }
                            InquiryController.deleteInquiry(applicant.getNRIC(), inquiries.get(deleteIndex));
                            break;

                        case 3:
                            // Return to main menu
                            break;

                        default:
                            System.out.println("Invalid option.");
                    }
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
                	// Switch menu if applicant has access
                	List <HDBOfficer> officerList = DataManager.getOfficers();
                	List <HDBManager> managerList = DataManager.getManagers();
                	    
                	Object user = findUserByNRIC(applicant.getNRIC(), managerList);
                    if (user == null) user = findUserByNRIC(applicant.getNRIC(), officerList);

                    switch (((User) user).getRole()) {
                        case "HDBManager":
                        	System.out.println("Switching to Manager Menu...");
                        	new HDBManagerUI().showMenu((HDBManager) user);
                            break;
                        case "HDBOfficer":
                        	System.out.println("Switching to Officer Menu...");
                        	new HDBOfficerUI().showMenu((HDBOfficer) user);
                        	break;
                        default:
                        	System.out.println("You do not have access outside of Applicant Menu.");
                    }
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
    
    //Generic method to find a User in a list by NRIC
    private static <T> User findUserByNRIC(String nric, List<T> list) {
        for (T u : list) {
            if (u instanceof User && ((User) u).getNRIC().equalsIgnoreCase(nric)) {
                return (User) u;
            }
        }
        return null;
    }
}
