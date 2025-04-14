package boundary;

import entity.Applicant;
import entity.Project;
import entity.Inquiry;
import control.ProjectController;
import control.InquiryController;
import java.util.List;
import java.util.Scanner;

public class ApplicantUI {
    ProjectController projectController = new ProjectController();
    InquiryController inquiryController = new InquiryController();

    // Display menu for Applicant user
    public void showMenu(Applicant applicant) {
        
        List<Project> projectList = projectController.getAvailableProjects(applicant); // Fetches list of available projects
        List<Inquiry> inquiryList = InquiryController.viewInquiries(applicant.getNRIC());
        int inquiryIndex = 0;
        Inquiry inquiry;
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Applicant Menu:");
        System.out.println("1. View available BTO projects");
        System.out.println("2. Apply for a project");
        System.out.println("3. View my application status");
        System.out.println("4. Withdraw my application");
        System.out.println("5. Submit an inquiry");
        System.out.println("6. View and manage inquiries");
        System.out.println("7. Quit");
        System.out.print("Select an option: ");
        
        while (true) {
        	int choice = scanner.nextInt(); // Input choice of option
        	//Quit the program
        	if (choice == 7) {
            	System.out.println("Quit successful.");
            	break;	
        	}
        	switch (choice) {
                // Displays list of available projects
                case 1:// Loops through list of projects and prints out their names
                	System.out.println("Projects available:");
                	for (Project project : projectList) {
                        System.out.println(project.getName());
                    }
                    break;

                // Applicant applies for a project
                case 2:
                    System.out.println("Enter the name of the project you wish to apply: ");
                    String projectName = scanner.nextLine(); // Applicant inputs the name of the project they wish to apply
                    boolean projectApplied = projectController.applyToProject(applicant, projectName); // Applies to the project and gets whether applicant is eligible for application
                    
                    // Displays message depending on whether project is successfully applied
                    if (projectApplied) {
                        System.out.println("Project successfully applied!");
                    } else {
                        System.out.println("Project is unable to be applied to.");
                    }
                    break;

                // Displays applicant's application status
                case 3:
                    String applicationStatus = applicant.getApplicationStatus();
                    System.out.print("Current application status: ");
                    System.out.println(applicationStatus);
                    
                    /////////////////////////////////////////////////////////////////////////
                    // Is applicant supposed to be able to book through HDB Officer here???//
                    /////////////////////////////////////////////////////////////////////////

                    break;

                // Withdraw application of project
                //////////////////////
                /// Not sure how do///
                /// //////////////////
                case 4:
                    //withdrawApplication();
                    break;

                // Submit applicant's inquiry
                case 5:
                    // Display available projects
                    for (int i=1; i<=projectList.size(); i++) {
                        System.out.print(i);
                        System.out.print(". ");
                        System.out.println(projectList.get(i-1).getName());
                    }

                    // Applicant chooses which project to inquire
                    System.out.println("Which project do you wish to inquire about: ");
                    int projectIndex = scanner.nextInt() - 1;
                    if (projectIndex < 0 || projectIndex > projectList.size()) {
                        System.out.println("Project does not exist.");
                        break;
                    }
                    Project project = projectList.get(projectIndex);

                    // Applicant enters a subject for inquiry
                    System.out.println("What is the subject for your inquiry: ");
                    String subject = scanner.nextLine();

                    // Applicant enters message for inquiry
                    System.out.println("What is your inquiry: ");
                    String message = scanner.nextLine();

                    // Create and add inquiry
                    InquiryController.createInquiry(applicant.getNRIC(), subject, project, message);
                    break;

                // View/Edit/Delete applicant's inquiries
                case 6:
                    System.out.println("Inquiry Menu");
                    System.out.println("1. View");
                    System.out.println("2. Edit");
                    System.out.println("3. Delete");
                    System.out.println("Select an option: ");

                    int option = scanner.nextInt();
                    switch (option) {
                        // View applicant's inquiries
                        case 1:
                            for (int i=1; i<=inquiryList.size(); i++) {
                                System.out.print(i);
                                System.out.print(". ");
                                System.out.print(inquiryList.get(i-1).getSubject());
                                System.out.print(": ");
                                System.out.println(inquiryList.get(i-1).getMessage());
                            }
                            break;

                        // Edit applicant's inquiries
                        case 2:
                            // Display applicant's inquiries
                            for (int i=1; i<=inquiryList.size(); i++) {
                                System.out.print(i);
                                System.out.print(". ");
                                System.out.print(inquiryList.get(i-1).getSubject());
                                System.out.print(": ");
                                System.out.println(inquiryList.get(i-1).getMessage());
                            }

                            // Applicant chooses inquiry to edit
                            System.out.println("Which inquiry do you wish to edit: ");
                            inquiryIndex = scanner.nextInt() - 1;
                            if (inquiryIndex < 0 || inquiryIndex > inquiryList.size()) {
                                System.out.println("Inquiry does not exist.");
                                break;
                            }
                            inquiry = inquiryList.get(inquiryIndex);

                            // Applicant enters new message
                            System.out.println("What would you like to change your inquiry to: ");
                            String newMessage = scanner.nextLine();

                            // Edit inquiry
                            InquiryController.editInquiry(applicant.getNRIC(), inquiry, newMessage);
                            break;

                        // Delete applicant's inquiry
                        case 3:
                            // Display applicant's inquiries
                            for (int i=1; i<=inquiryList.size(); i++) {
                                System.out.print(i);
                                System.out.print(". ");
                                System.out.print(inquiryList.get(i-1).getSubject());
                                System.out.print(": ");
                                System.out.println(inquiryList.get(i-1).getMessage());
                            }

                            // Applicant chooses inquiry to edit
                            System.out.println("Which inquiry do you wish to edit: ");
                            inquiryIndex = scanner.nextInt() - 1;
                            if (inquiryIndex < 0 || inquiryIndex > inquiryList.size()) {
                                System.out.println("Inquiry does not exist.");
                                break;
                            }
                            inquiry = inquiryList.get(inquiryIndex);

                            // Delete inquiry
                            InquiryController.deleteInquiry(applicant.getNRIC(), inquiry);
                            break;

                        default:
                            System.out.println("Invalid option.");
                    }
                    break;
                	
                default:
                    System.out.println("Invalid option.");
            }
        }
        
        scanner.close();
    }
}