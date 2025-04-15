package boundary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import control.InquiryController;
import control.ProjectController;
import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.Inquiry;
import entity.Project;

public class HDBManagerUI {
    
    public void showMenu(HDBManager hdbmanager, List<Applicant> applicantList, List<HDBOfficer> hdbOfficerList) {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ProjectController projectController = new ProjectController();
        InquiryController inquirycontroller = new InquiryController();
        HDBOfficer officer;
        Applicant applicant;
        Project chosenProject;
        List<Project> projectList;
        int projectIndex;
        List<Inquiry> inquiryList;
        
        System.out.println("HDB Manager Menu:");
        System.out.println("1. Create a new BTO project");
        System.out.println("2. Edit an existing BTO project");
        System.out.println("3. Delete a BTO project");
        System.out.println("4. Toggle visibility of project");
        System.out.println("5. View all created projects");
        System.out.println("6. View projects created by me");
        System.out.println("7. View and approve/reject HDB Officer registrations");
        System.out.println("8. View and approve/reject applicant's BTO application");
        System.out.println("9. View and approve/reject applicant's withdrawal application");
        System.out.println("10. Generate applicant list report");
        System.out.println("11. View inquiries for ALL projects");
        System.out.println("12. View and reply to inquiries for the projects you are handling");
        System.out.println("13. Filter projects.");
        System.out.println("14. Quit");
        System.out.print("Select an option: ");
        
        while (true) {
        	int choice = scanner.nextInt(); // Input choice of option
        	//Quit the program
        	if (choice == 14) {
            	System.out.println("Quit successful.");
            	break;	
        	}
	        switch (choice) {
	            // Create new BTO project
	            case 1:
	            //////////////////////////////////////////////////////////////////
	            /// Not sure how to implement checking for whether HDB manager ///
	            ///   is only handling one project within application period   ///
	            //////////////////////////////////////////////////////////////////
	                // Enter project details
	                System.out.println("Please enter the relevant information");
	                
	                // Enter project name
	                System.out.print("Project Name: ");
	                String projectName = scanner.nextLine();
	
	                // Enter neighbourhood
	                System.out.print("Neighbourhood: ");
	                String neighbourhood = scanner.nextLine();
	
	                // Enter type of flat and number of that type of flat
	                Map<String, Integer> flatTotal = new HashMap<String, Integer>(); // Map to store flat and number pairs
	                String option = "yes"; 
	                do {
	                    System.out.print("Type of Flat: "); // 2-Room or 3-Room
	                    String flatType = scanner.nextLine();
	
	                    System.out.print("Number of units for ");
	                    System.out.print(flatType);
	                    System.out.print(" flats: ");
	                    int numFlats = scanner.nextInt();
	                    scanner.nextLine();
	
	                    flatTotal.put(flatType, numFlats); // Store details in map
				
	                    System.out.print("Are there more types of flats (yes/no): ");
	                    option = scanner.nextLine();
	                } while (option.equalsIgnoreCase("yes"));
	                
	                Date openingDate = new Date();
	                Date closingDate = new Date();
	                try {
	                    // Enter application opening date
	                    System.out.print("Application opening date(dd/MM/yyyy): ");
	                    String applicationOpenDate = scanner.nextLine();
	                    openingDate = dateFormat.parse(applicationOpenDate);
	
	                    // Enter application closing date
	                    System.out.print("Application closing date(dd/MM/yyyy): ");
	                    String applicationCloseDate = scanner.nextLine();
	                    closingDate = dateFormat.parse(applicationCloseDate);
	                } catch (Exception e) {
	                    System.out.println("Invalid date format! Please use dd/MM/yyyy.");
	                }
	
	                // Enter HDB manager in charge
	                System.out.print("HDB manager in charge: ");
	                String newHDBManager = scanner.nextLine();
	
	                // Enter available HDB officer slots
	                System.out.print("Available HDB officer slots: ");
	                int availableSlots = scanner.nextInt();
	
	                if (availableSlots > 10 || availableSlots < 1) {
	                    System.out.println("Invalid number of slots (max 10)");
	                    break;
	                }
	
	                // Create new project
	                Project newProject = new Project(projectName, neighbourhood, flatTotal, new HashMap<String, Integer>(flatTotal), ////////////////////////////////
	                new HashMap<String, Integer>(flatTotal), openingDate, closingDate, newHDBManager, availableSlots,                /// Not sure about flat price///
	                new ArrayList<String>(), false);                                                                      ////////////////////////////////
	                projectController.createProject(hdbmanager, newProject);
	                break;
	
	            // Edit BTO project
	            case 2:
	                // Display all projects for HDB manager to edit
	                System.out.println("Which project do you want to edit?");
	                projectList = projectController.getAvailableProjects(hdbmanager);
	                for (int proj=1; proj<=projectList.size(); proj++) {
	                    System.out.print(proj);
	                    System.out.print(". ");
	                    System.out.println(projectList.get(proj-1).getName());
	                }
	
	                // Select project to edit
	                projectIndex = scanner.nextInt() - 1;
	                if (projectIndex < 0 || projectIndex > projectList.size()) {
	                    System.out.println("Project does not exist.");
	                    break;
	                }
	                chosenProject = projectList.get(projectIndex-1);
	
	                // Display fields to edit
	                System.out.println("Which field do you wish to edit?");
	                System.out.println("1. Project Name");
	                System.out.println("2. Neighbourhood");
	                System.out.println("3. Application Opening Date");
	                System.out.println("4. Application Closing Date");
	                System.out.println("5. Available HDB officer slots");
	
	                int selected = scanner.nextInt();
	                switch (selected) {
	                    // Edit project name
	                    case 1:
	                        System.out.print("Current project name: ");
	                        System.out.println(chosenProject.getName());
	                        System.out.print("New project name: ");
	                        String newProjectName = scanner.nextLine();
	                        projectController.editProject(hdbmanager, chosenProject, "name", newProjectName);
	                        break;
	
	                    // Edit Neighbourhood
	                    case 2:
	                        System.out.print("Current neighbourhood: ");
	                        System.out.println(chosenProject.getLocation());
	                        System.out.print("New neighbourhood: ");
	                        String newLocation = scanner.nextLine();
	                        projectController.editProject(hdbmanager, chosenProject, "location", newLocation);
	                        break;
	
	                    // Edit application opening date
	                    case 3:
	                        System.out.print("Current application opening date(dd/MM/yyyy): ");
	                        System.out.println(chosenProject.getOpenDate());
	                        System.out.print("New application opening date(dd/MM/yyyy): ");
	
	                        Date openDate = new Date();
	                        try {
	                            String applicationOpenDate = scanner.nextLine();
	                            openDate = dateFormat.parse(applicationOpenDate);
	                        } catch (Exception e) {
	                            System.out.println("Invalid date format! Please use dd/MM/yyyy.");
	                        }
	
	                        projectController.editProject(hdbmanager, chosenProject, "openDate", openDate);
	                        break;
	
	                    // Edit application closing date
	                    case 4:
	                        System.out.print("Current application closing date(dd/MM/yyyy): ");
	                        System.out.println(chosenProject.getCloseDate());
	                        System.out.print("New application closing date(dd/MM/yyyy): ");
	
	                        Date closeDate = new Date();
	                        try {
	                            String applicationCloseDate = scanner.nextLine();
	                            closeDate = dateFormat.parse(applicationCloseDate);
	                        } catch (Exception e) {
	                            System.out.println("Invalid date format! Please use dd/MM/yyyy.");
	                        }
	
	                        projectController.editProject(hdbmanager, chosenProject, "closeDate", closeDate);
	                        break;
	
	                    // Edit available HDB officer slots
	                    case 5:
	                        System.out.print("Current available HDB officer slots: ");
	                        System.out.println(chosenProject.getOfficerSlot());
	                        System.out.print("New available HDB officer slots: ");
	                        int newOfficerSlots = scanner.nextInt();
	                        projectController.editProject(hdbmanager, chosenProject, "officerSlot", newOfficerSlots);
	                        break;
	
	                    default:
	                        System.out.println("Invalid option.");
	                }
	                break;
	
	            // Delete BTO project
	            case 3:
	                // Display all projects for HDB manager to delete
	                System.out.println("Which project do you want to delete?");
	                projectList = projectController.getAvailableProjects(hdbmanager);
	                for (int proj=1; proj<=projectList.size(); proj++) {
	                    System.out.print(proj);
	                    System.out.print(". ");
	                    System.out.println(projectList.get(proj-1).getName());
	                }
	
	                // Select project to delete
	                projectIndex = scanner.nextInt() - 1;
	                if (projectIndex < 0 || projectIndex > projectList.size()) {
	                    System.out.println("Project does not exist.");
	                    break;
	                }
	                chosenProject = projectList.get(projectIndex-1);
	                projectController.deleteProject(hdbmanager, chosenProject); // Delete project
	                break;
	
	            // Toggle visibility of projects
	            case 4:
	                // Display all projects for HDB manager
	                System.out.println("Which project to toggle visibility?");
	                projectList = projectController.getAvailableProjects(hdbmanager);
	                for (int proj=1; proj<=projectList.size(); proj++) {
	                    System.out.print(proj);
	                    System.out.print(". ");
	                    System.out.println(projectList.get(proj-1).getName());
	                }
	
	                // Select project
	                projectIndex = scanner.nextInt() - 1;
	                if (projectIndex < 0 || projectIndex > projectList.size()) {
	                    System.out.println("Project does not exist.");
	                    break;
	                }
	                chosenProject = projectList.get(projectIndex-1);
	
	                // Select visibility
	                System.out.print("Current project visibility: ");
	                System.out.println(chosenProject.getVisibility());
	                System.out.print("Change to visible (True/False): ");
	                String visible = scanner.nextLine();
	                if (visible.equalsIgnoreCase("true")) {
	                	//METHODS UNDEFINED
	                    projectController.toggleProjectVisibility(chosenProject, true);
	                } else {
	                    projectController.toggleProjectVisibility(chosenProject, false);
	                }
	                break;
	
	            // Display all created projects
	            case 5:
	                projectList = projectController.getAvailableProjects(hdbmanager);
	                for (int proj=1; proj<=projectList.size(); proj++) {
	                    System.out.print(proj);
	                    System.out.print(". ");
	                    System.out.println(projectList.get(proj-1).getName());
	                }
	                break;
	
	            // Display only projects created by current HDB manager user
	            case 6:
	                projectList = hdbmanager.getCreatedProjects();
	                for (int proj=1; proj<=projectList.size(); proj++) {
	                    System.out.print(proj);
	                    System.out.print(". ");
	                    System.out.println(projectList.get(proj-1).getName());
	                }
	                break;
	            
	            // Approve / Reject HDB officer's registration
	            case 7:
	            	while (true) {
	                	System.out.print("Enter officer's NRIC: ");
	                	String nric = scanner.nextLine();
	                	if (!nric.matches("[ST]\\d{7}[A-Z]")) {
	                    	//check if nric is valid
	                    	System.out.println("Invalid NRIC given, please try again.");
	                    } else {
	                    	while (true) {
	                        	for (HDBOfficer o: hdbOfficerList) {
	                                if (o.getNRIC().equalsIgnoreCase(nric)) {
	                                    officer = o;
	                                }
	                            }
	                        	if (officer != null) {
	                        		//WHAT IS THIS KEY THING NEEDED FOR getRegistrationStatus()?
	                        		//System.out.println(officer.getName() + " with NRIC of " + officer.getNRIC() + " has a registration status of: " + officer.getRegistrationStatus());
	                        		while (true) {
	                        		System.out.print("Enter new status: ");
	                            	String status = scanner.nextLine();
	                            	projectController.processRegistrations(hdbmanager, officer,status);
	                        		}
	                        	} else {
	                        		System.out.println("No officer with this NRIC was found, please try again.");
	                        		break;
	                        	}
	                    	}
	                    	break;
	                    }
	            	}
	                break;
	
	            case 8:
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
	                    		//WHAT IS THIS KEY THING NEEDED FOR getRegistrationStatus()?
	                    		System.out.println(applicant.getName() + " with NRIC of " + applicant.getNRIC() + " has a BTO status of: " + applicant.getApplicationStatus());
	                    		while (true) {
	                    			System.out.print("Enter new status: ");
	                    			String status = scanner.nextLine();
	                    			if (status.compareTo("Successful") == 0) {
	                    				if ((applicant.getAge() > 35) && (applicant.getMaritalStatus().compareTo("Single") == 0) && (applicant.getAppliedProject().getFlatTypeAvailable().get("2-Room") != 0)) {
	                    					projectController.processApplication(hdbmanager, applicant, "2-room", status);
	                    					break;
	                            		
	                    				} else if ((applicant.getAge() > 21) && (applicant.getMaritalStatus().compareTo("Married") == 0)) {
	                    					if (applicant.getAppliedProject().getFlatTypeAvailable().get("3-Room") != 0) {
	                    						projectController.processApplication(hdbmanager, applicant, "3-room", status);
	                    						break;
	                        			
	                    					} else if (applicant.getAppliedProject().getFlatTypeAvailable().get("2-Room") != 0) {
	                    						projectController.processApplication(hdbmanager, applicant, "2-room", status);
	                    						break;
	                        			
	                    					} else {
	                    						System.out.println("No available flats for applicant, status update failed.");
	                    					}
	                    				} else {
	                    					System.out.println("No available flats for applicant, status update failed.");
	                    				}
	                            	
	                    			} else if (status.compareTo("Unsuccessful") == 0){
	                    				applicant.setApplicationStatus(status);
	                    				break;
	                        		
	                    			} else {
	                    				System.out.println("Invalid status.");
	                    			}
	                    		}
	                    	} else {
	                    		System.out.println("No officer with this NRIC was found, please try again.");
	                    		break;
	                    	}
	                	}
	                	break;
	                }
	        	}
	            break;
	            
	            ////////////////////////////
	            ///HOW IS WITHDRAWAL DONE///
	            ///////////////////////////
	            case 9:
	                break;
	
	            case 10:
	                System.out.println("Applicant Report Generator");
	                System.out.println("1. View All Applicants");
	                System.out.println("2. Filter by Marital Status");
	                System.out.println("3. Filter by Flat Type");
	                System.out.println("4. Filter by Project Name");
	                System.out.println("5. Filter by Age Range");
	                System.out.print("Choose a filter option: ");
	
	                int filterOption = scanner.nextInt();
	                scanner.nextLine(); // consume newline
	
	                Stream<Applicant> stream = applicantList.stream();
	
	                switch (filterOption) {
	                    case 1:
	                        // No filters
	                        break;
	                    case 2:
	                        System.out.print("Enter marital status to filter by (e.g., Married): ");
	                        String status = scanner.nextLine();
	                        stream = stream.filter(a -> a.getMaritalStatus().equalsIgnoreCase(status));
	                        break;
	                    case 3:
	                        System.out.print("Enter flat type to filter by (e.g., 2-Room): ");
	                        String flatType = scanner.nextLine();
	                        stream = stream.filter(a ->
	                            a.getAppliedProject() != null &&
	                            a.getAppliedProject().getFlatTypeAvailable() != null &&
	                            a.getAppliedProject().getFlatTypeAvailable().containsKey(flatType)
	                        );
	                        break;
	                    case 4:
	                        System.out.print("Enter project name to filter by: ");
	                        String project = scanner.nextLine();
	                        stream = stream.filter(a ->
	                            a.getAppliedProject() != null &&
	                            a.getAppliedProject().getName().equalsIgnoreCase(projectName)
	                        );
	                        break;
	                    case 5:
	                        System.out.print("Enter min age: ");
	                        int minAge = scanner.nextInt();
	                        System.out.print("Enter max age: ");
	                        int maxAge = scanner.nextInt();
	                        stream = stream.filter(a -> a.getAge() >= minAge && a.getAge() <= maxAge);
	                        break;
	                    default:
	                        System.out.println("Invalid filter option.");
	                        return; // Exit early
	                }
	
	                List<Applicant> filteredApplicants = stream.collect(Collectors.toList());
	
	                System.out.println("\n--- Filtered Applicant Report ---");
	                if (filteredApplicants.isEmpty()) {
	                    System.out.println("No applicants match the selected filter.");
	                } else {
	                    filteredApplicants.forEach(a -> {
	                        String flatTypes = "N/A";
	                        String project = "N/A";
	
	                        if (a.getAppliedProject() != null) {
	                            project = a.getAppliedProject().getName();
	                            Map<String, Integer> flatMap = a.getAppliedProject().getFlatTypeAvailable();
	                            if (flatMap != null) {
	                                flatTypes = String.join(", ", flatMap.keySet());
	                            }
	                        }
	
	                        System.out.printf("Name: %s | Age: %d | Marital Status: %s | Flat Type(s): %s | Project: %s%n",
	                            a.getName(), a.getAge(), a.getMaritalStatus(), flatTypes, project);
	                    });
	                }
	                break;
	
	
	            // View inquiries for ALL projects
	            case 11:
	                inquiryList = inquirycontroller.allInquiries();
	                for (int inq=1; inq<=inquiryList.size(); inq++) {
	                    System.out.print(inq);
	                    System.out.print(". ");
	                    System.out.print(inquiryList.get(inq-1).getRelatedProject().getName());
	                    System.out.print(": ");
	                    System.out.print(inquiryList.get(inq-1).getMessage());
	                }
	                break;
	
	            // View and reply to inquiries regarding project that hdb manager is handling
	            case 12:
	                // Display only projects created by current HDB manager user
	                projectList = hdbmanager.getCreatedProjects();
	                for (int proj=1; proj<=projectList.size(); proj++) {
	                    System.out.print(proj);
	                    System.out.print(". ");
	                    System.out.println(projectList.get(proj-1).getName());
	                }
	                
	                // Select project
	                projectIndex = scanner.nextInt() - 1;
	                if (projectIndex < 0 || projectIndex > projectList.size()) {
	                    System.out.println("Project does not exist.");
	                    break;
	                }
	                chosenProject = projectList.get(projectIndex-1);
	
	                // View inquiries for chosen project
	                inquiryList = inquirycontroller.viewInquiries(chosenProject);
	                for (int inq=1; inq<=inquiryList.size(); inq++) {
	                    System.out.print(inq);
	                    System.out.print(". ");
	                    System.out.print(inquiryList.get(inq-1).getMessage());
	                }
	
	                // Select inquiry to reply to
	                int inquiryIndex = scanner.nextInt() - 1;
	                if (inquiryIndex < 0 || inquiryIndex > inquiryList.size()) {
	                    System.out.println("Project does not exist.");
	                    break;
	                }
	                Inquiry chosenInquiry = inquiryList.get(inquiryIndex);
	
	                // Enter reply message to inquiry
	                System.out.print("Enter reply: ");
	                String replyMessage = scanner.nextLine();
	
	                // Update inquiry reply
	                inquirycontroller.replyToInquiry(chosenInquiry.getInquiryId(), replyMessage);
	                break;
	
	            default:
	                System.out.println("Invalid option.");
	        }
        }
        
        scanner.close();
    }
}
