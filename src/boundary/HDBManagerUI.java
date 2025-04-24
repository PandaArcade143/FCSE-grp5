package boundary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import helpers.DataManager;
import boundary.HDBStaffInt;

/**
 * The {@code HDBManagerUI} class represents the command-line interface
 * for {@link HDBManager} users in the BTO Management System.
 * <p>
 * It provides functionality for managers to:
 * <ul>
 *   <li>View and manage BTO projects</li>
 *   <li>Create, edit, delete, and toggle project visibility</li>
 *   <li>Handle officer registrations</li>
 *   <li>Manage applicant approvals and withdrawals</li>
 *   <li>Reply to inquiries and generate applicant reports</li>
 *   <li>Apply filters to project listings</li>
 * </ul>
 */
public class HDBManagerUI implements HDBStaffInt{

	 /**
     * Displays the interactive menu for an HDB Manager and handles actions based on user input.
     * <p>
     * This includes a wide range of operations:
     * <ul>
     *   <li>Project creation and management</li>
     *   <li>Officer registration processing</li>
     *   <li>Applicant approvals and withdrawals</li>
     *   <li>Inquiry handling and replying</li>
     *   <li>Filtering and reporting on applicants and projects</li>
     * </ul>
     *
     * @param hdbmanager the currently logged-in HDB manager
     */
	public void showMenu(HDBManager hdbmanager) {
		List<Applicant> applicantList = DataManager.getCombinedApplicants();
		List<HDBOfficer> hdbOfficerList = DataManager.getOfficers();
		Scanner scanner = new Scanner(System.in);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		ProjectController projectController = new ProjectController();
		HDBOfficer officer = null;
		Applicant applicant = null;
		Project chosenProject;
		boolean validDate;
		List<Project> projectList = new ArrayList<>();
		int projectIndex;
		List<Inquiry> inquiryList;
		int filterOption;
		String option;
		String projectName;


		while (true) {
			// Display menu options
			System.out.println("\n\n\nHDB Manager Menu:");
			System.out.println("1. View all created projects");
			System.out.println("2. View projects created by me");
			System.out.println("3. Create a new BTO project");
			System.out.println("4. Edit an existing BTO project");
			System.out.println("5. Delete a BTO project");
			System.out.println("6. Toggle visibility of project");
			System.out.println("7. View approved/pending officers for my projects");
			System.out.println("8. Approve/reject HDB Officer registrations");
			System.out.println("9. View pending/withdrawing applicants for my projects");
			System.out.println("10. Approve/reject applicant's BTO application");
			System.out.println("11. View and approve/reject applicant's withdrawal application");
			System.out.println("12. Generate applicant list report");
			System.out.println("13. View inquiries for ALL projects");
			System.out.println("14. Reply to inquiries for the projects you are handling");
			System.out.println("15. Filter projects");
			System.out.println("16. Quit");
			System.out.print("\nSelect an option: ");

			int choice;
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("\nInvalid input. Please enter a number.");
				continue;
			}

			switch (choice) {
			// Display all created projects
			case 1:
				projectList = projectController.getFilteredProjects(hdbmanager);
				System.out.println("\nAll projects created:");
				viewProjectInquiries(projectList);
				break;

				// Display only projects created by current HDB manager user
			case 2:
				projectList = hdbmanager.getCreatedProjects();
				System.out.println("\nProjects available:");
				for (Project project : projectList) {
					if (project != null && project.getManagerName().equalsIgnoreCase(hdbmanager.getName())) {
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
						System.out.println("Manager of project: " + project.getManagerName());
						System.out.println("Officer slots for project: " + project.getOfficerSlot());
						System.out.println("Officers of project: ");
						for (HDBOfficer hdbofficer: project.getOfficers()) {
							System.out.println(" - " + hdbofficer.getName());
						}
					} else {
						System.out.println("\nYou did not create any projects.");
						break;
					}
				}
				break;
				// Create new BTO project
			case 3:
				// Enter project details
				System.out.println("\nPlease enter the relevant information (or Quit)");

				// Enter project name
				System.out.print("\nProject Name: ");
				projectName = scanner.nextLine();
				if (projectName.equalsIgnoreCase("quit")) break;
				// Enter neighbourhood
				System.out.print("Neighbourhood: ");
				String neighbourhood = scanner.nextLine();

				// Enter type of flat and number of that type of flat
				Map<String, Integer> flatTotal = new HashMap<String, Integer>(); // Map to store flat and number pairs
				int i; 
				for (i = 0; i < 2; i++) {
					String flatType;
					while (true) {
						System.out.printf("\nType %d Flat: ", i+1); // 2-Room or 3-Room
						flatType = scanner.nextLine();

						if (flatType.equalsIgnoreCase("2-Room") || flatType.equalsIgnoreCase("2 Room")) {
							flatType = "2-Room";
							break;
						} else if (flatType.equalsIgnoreCase("3-Room") || flatType.equalsIgnoreCase("3 Room")) {
							flatType = "3-Room";
							break;
						}  else {
							System.out.print("Invalid type of flat. Please only input '2-Room' or '3-Room'.");
						}

					}

					System.out.print("Number of units for ");
					System.out.print(flatType);
					System.out.print(" flats: ");
					int numFlats = scanner.nextInt();
					scanner.nextLine();

					flatTotal.put(flatType, numFlats); // Store details in map
				}

				// Enter flat prices for each flat type
				Map<String, Integer> flatPrices = new HashMap<>();
				for (String flatType : flatTotal.keySet()) {
					System.out.print("\nEnter price for " + flatType + ": ");
					int price = scanner.nextInt();
					scanner.nextLine(); // consume newline
					flatPrices.put(flatType, price);
				}

				Date openingDate = new Date();
				Date closingDate = new Date();
				validDate = false;
				while (!validDate) {
					try {
						// Enter application opening date
						System.out.print("\nApplication opening date(dd/mm/yy): ");
						String applicationOpenDate = scanner.nextLine();
						openingDate = dateFormat.parse(applicationOpenDate);
						// Validate day, month, and year for the opening date
						String[] openDateParts = applicationOpenDate.split("/");
						int openDay = Integer.parseInt(openDateParts[0]);
						int openMonth = Integer.parseInt(openDateParts[1]);

						if (openDay < 1 || openDay > 31 || openMonth < 1 || openMonth > 12) {
							throw new IllegalArgumentException("Invalid day or month value.");
						}


						// Enter application closing date
						System.out.print("\nApplication closing date(dd/mm/yy): ");
						String applicationCloseDate = scanner.nextLine();
						closingDate = dateFormat.parse(applicationCloseDate);
						// Validate day, month, and year for the closing date
						String[] closeDateParts = applicationCloseDate.split("/");
						int closeDay = Integer.parseInt(closeDateParts[0]);
						int closeMonth = Integer.parseInt(closeDateParts[1]);
						if (closeDay < 1 || closeDay > 31 || closeMonth < 1 || closeMonth > 12) {
							throw new IllegalArgumentException("Invalid day or month value.");
						}
						if (!closingDate.after(openingDate)) {
							System.out.println("\nClosing date should be after opening date.");
						} else {
							validDate = true;
						}
					} catch (Exception e) {
						validDate = false;
						System.out.println("\nInvalid date format! Please use dd/mm/yy.");
					}
					if (validDate) {
						for (Project createdProject : hdbmanager.getCreatedProjects()) {
							if (openingDate.before(createdProject.getCloseDate()) && closingDate.after(createdProject.getOpenDate())) {
								System.out.println("\nYou are currently handling another project!");
								validDate = false;
								break;
							}
						}
					}
				}

				String newHDBManager = hdbmanager.getName();

				// Enter available HDB officer slots
				System.out.print("\nAvailable HDB officer slots: ");
				int availableSlots = scanner.nextInt();
				scanner.nextLine();

				if (availableSlots > 10 || availableSlots < 1) {
					System.out.println("\nInvalid number of slots (max 10)");
					break;
				}

				// Create new project
				Project newProject = new Project(projectName, neighbourhood, flatTotal, new HashMap<String, Integer>(flatTotal), 
						flatPrices, openingDate, closingDate, newHDBManager, availableSlots,               
						new ArrayList<HDBOfficer>(), false);                                                                     
				projectController.createProject(hdbmanager, newProject);
				System.out.println("\nProject created successfully.");
				break;

				// Edit BTO project
			case 4:
				// Display all projects for HDB manager to edit
				System.out.println("\nWhich project do you want to edit?\n");
				projectList = projectController.getAvailableProjects(hdbmanager);
				for (int proj=1; proj<=projectList.size(); proj++) {
					System.out.print(proj);
					System.out.print(". ");
					System.out.println(projectList.get(proj-1).getName());
				}

				// Select project to edit
				System.out.print("\nSelect project number to edit (Enter the index or 0 to quit): ");

				try {
					projectIndex = scanner.nextInt() - 1;
					scanner.nextLine();
					if (projectIndex == -1) break;
					if (projectIndex < 0 || projectIndex > projectList.size()) {
						System.out.println("\nProject does not exist.");
						break;
					}
					chosenProject = projectList.get(projectIndex);
				} catch (InputMismatchException e) {
					System.out.println("\nInvalid input. Please enter a number.");
					scanner.nextLine();
					break;
				}
				// Display fields to edit
				System.out.println("\n\nWhich field do you wish to edit?");
				System.out.println("1. Project Name");
				System.out.println("2. Neighbourhood");
				System.out.println("3. Type of flats, number and price of units for each type");
				System.out.println("4. Application Opening Date");
				System.out.println("5. Application Closing Date");
				System.out.println("6. Available HDB officer slots");
				System.out.print("\nSelect an option: ");
				int selected;
				try {
					selected = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("\nInvalid choice.");
					break;
				}
				switch (selected) {
				// Edit project name
				case 1:
					System.out.print("\nCurrent project name: ");
					System.out.println(chosenProject.getName());
					System.out.print("\nNew project name: ");
					String newProjectName = scanner.nextLine();
					//SOME ERROR DUE TO GENERIC METHOD IN PROJECT CONTROLLER
					projectController.editProject(hdbmanager, chosenProject, "name", newProjectName);
					break;

					// Edit Neighbourhood
				case 2:
					System.out.print("\nCurrent neighbourhood: ");
					System.out.println(chosenProject.getLocation());
					System.out.print("\nNew neighbourhood: ");
					String newLocation = scanner.nextLine();
					projectController.editProject(hdbmanager, chosenProject, "location", newLocation);
					break;

					// Edit type of flats, number and price of units for each type
				case 3:
					Map<String, Integer> newFlatTotal = new HashMap<String, Integer>(); // Map to store flat and number pairs
					option = "yes"; 
					do {
						System.out.print("\nType of Flat: "); // 2-Room or 3-Room
						String newFlatType = scanner.nextLine();

						System.out.print("Number of units for ");
						System.out.print(newFlatType);
						System.out.print(" flats: ");
						int newNumFlats = scanner.nextInt();
						scanner.nextLine();

						newFlatTotal.put(newFlatType, newNumFlats); // Store details in map

						System.out.print("\nAre there more types of flats (yes/no): ");
						option = scanner.nextLine();
					} while (option.equalsIgnoreCase("yes"));

					// Enter flat prices for each flat type
					Map<String, Integer> newFlatPrices = new HashMap<>();
					for (String flatType : newFlatTotal.keySet()) {
						System.out.print("\nEnter price for " + flatType + ": ");
						int newPrice = scanner.nextInt();
						scanner.nextLine(); // consume newline
						newFlatPrices.put(flatType, newPrice);
					}
					chosenProject.setFlatTypeTotal(newFlatTotal);
					chosenProject.setFlatPrices(newFlatPrices);
					chosenProject.setFlatTypeAvailable(newFlatTotal);
					break;

					// Edit application opening date
				case 4:
					validDate = false;
					Date openDate = new Date();
					String applicationOpenDate = "";
					while(!validDate){
						System.out.print("\nCurrent application opening date(dd/mm/yy): ");
						System.out.println(chosenProject.getOpenDate());
						System.out.print("\nNew application opening date(dd/mm/yy): ");

						try {
							applicationOpenDate = scanner.nextLine();
							openDate = dateFormat.parse(applicationOpenDate);
							String[] openDateParts = applicationOpenDate.split("/");
							int openDay = Integer.parseInt(openDateParts[0]);
							int openMonth = Integer.parseInt(openDateParts[1]);

							if (openDay < 1 || openDay > 31 || openMonth < 1 || openMonth > 12) {
								throw new IllegalArgumentException("Invalid day or month value.");
							}
							else{
								validDate = true;
							}

						} catch (Exception e) {
							System.out.println("\nInvalid date format! Please use dd/mm/yy.");
						}
					}


					for (Project createdProject : hdbmanager.getCreatedProjects()) {
						if (!createdProject.getCloseDate().before(openDate) && !createdProject.getOpenDate().after(openDate)) {
							validDate = false;
							break;
						}
					}

					if (validDate) {
						projectController.editProject(hdbmanager, chosenProject, "openDate", openDate);
					} else {
						System.out.println("\nYou are handling another project during that period!");
					}
					break;

					// Edit application closing date
				case 5:
					validDate = false;
					Date closeDate = new Date();
					String applicationCloseDate = "";
					while(!validDate){
						System.out.print("\nCurrent application closing date(dd/mm/yy): ");
						System.out.println(chosenProject.getCloseDate());
						System.out.print("\nNew application closing date(dd/mm/yy): ");
						applicationCloseDate = scanner.nextLine();


						try {

							closeDate = dateFormat.parse(applicationCloseDate);
							String[] closeDateParts = applicationCloseDate.split("/");
							int closeDay = Integer.parseInt(closeDateParts[0]);
							int closeMonth = Integer.parseInt(closeDateParts[1]);

							if (closeDay < 1 || closeDay > 31 || closeMonth < 1 || closeMonth > 12) {
								throw new IllegalArgumentException("Invalid day or month value.");
							}
							else{
								validDate = true;
							}

						} catch (Exception e) {
							System.out.println("\nInvalid date format! Please use dd/mm/yy.");
						}
					}


					for (Project createdProject : hdbmanager.getCreatedProjects()) {
						if (!createdProject.getCloseDate().before(closeDate) && !createdProject.getOpenDate().after(closeDate)) {
							validDate = false;
							break;
						}
					}

					if (validDate) {
						projectController.editProject(hdbmanager, chosenProject, "closeDate", closeDate);
					} else {
						System.out.println("\nYou are handling another project during that period!");
					}
					break;

					// Edit available HDB officer slots
				case 6:
					System.out.print("\nCurrent available HDB officer slots: ");
					System.out.println(chosenProject.getOfficerSlot());
					System.out.print("\nNew available HDB officer slots: ");
					int newOfficerSlots = scanner.nextInt();
					scanner.nextLine();
					projectController.editProject(hdbmanager, chosenProject, "officerSlot", newOfficerSlots);
					break;

				default:
					System.out.println("\nInvalid option.");
				}
				break;

				// Delete BTO project
			case 5:
				// Display all projects for HDB manager to delete
				System.out.println("\nWhich project do you want to delete?");
				projectList = projectController.getAvailableProjects(hdbmanager);
				for (int proj=1; proj<=projectList.size(); proj++) {
					System.out.print(proj);
					System.out.print(". ");
					System.out.println(projectList.get(proj-1).getName());
				}

				// Select project to delete
				System.out.print("\nSelect a project to delete (or 0 to quit): ");
				projectIndex = scanner.nextInt() - 1;
				scanner.nextLine();
				if (projectIndex == -1) break;
				if (projectIndex < 0 || projectIndex > projectList.size()) {
					System.out.println("\nProject does not exist.");
					break;
				}
				chosenProject = projectList.get(projectIndex);
				projectController.deleteProject(hdbmanager, chosenProject); // Delete project
				System.out.println("Project deleted!");
				break;

				// Toggle visibility of projects
			case 6:
				// Display all projects for HDB manager
				System.out.println("\nWhich project to toggle visibility?");
				projectList = projectController.getAvailableProjects(hdbmanager);
				for (int proj=1; proj<=projectList.size(); proj++) {
					System.out.print(proj);
					System.out.print(". ");
					System.out.println(projectList.get(proj-1).getName());
				}

				// Select project
				System.out.print("\nSelect a project to toggle visibility (select index or 0 to quit): ");
				projectIndex = scanner.nextInt();
				scanner.nextLine();
				if (projectIndex == 0) break;
				if (projectIndex < 0 || projectIndex > projectList.size()) {
					System.out.println("\nProject does not exist.");
					break;
				}
				chosenProject = projectList.get(projectIndex-1);

				// Select visibility
				System.out.print("\nCurrent project visibility: ");
				System.out.println(chosenProject.getVisibility());
				System.out.print("\nChange to visible (True/False): ");
				String visible = scanner.nextLine();
				if (visible.equalsIgnoreCase("true")) {
					projectController.toggleProject(chosenProject, true);
				} else {
					projectController.toggleProject(chosenProject, false);
				}
				break;



				// View all pending and approved officers for a project
			case 7:
				projectList = hdbmanager.getCreatedProjects();
				for (Project project : projectList) {
					if (project != null && project.getManagerName().equalsIgnoreCase(hdbmanager.getName())) {
						System.out.println("\nProject Name: " + project.getName());
						System.out.println("\nApproved officers:");
						for (HDBOfficer approvedOfficer : project.getOfficers()) {
							if (project.getOfficers() != null) {
								System.out.println(" - " + approvedOfficer.getName()  + " " + approvedOfficer.getNRIC());
							} else {
								System.out.println("No approved officers.");
							}
						}
						System.out.println("\nPending officers:");
						for (HDBOfficer pendingOfficer : project.getTemporaryOfficers()) {
							if (project.getTemporaryOfficers() != null) {
								System.out.println(" - " + pendingOfficer.getName() + " " + pendingOfficer.getNRIC());
							} else {
								System.out.println("No pending officers.");
							}
						}

					} else {
						System.out.println("\nYou did not create any projects.");
						break;
					}
				}
				break;

				// Approve / Reject HDB officer's registration
			case 8:
				while (true) {
					System.out.print("\nEnter officer's NRIC (or Quit): ");
					String nric = scanner.nextLine();
					if (nric.equalsIgnoreCase("quit")) break;
					if (!nric.matches("[ST]\\d{7}[A-Z]")) {
						//check if nric is valid
						System.out.println("\nInvalid NRIC given, please try again.");
					} else {
						while (true) {
							for (HDBOfficer o: hdbOfficerList) {
								if (o.getNRIC().equalsIgnoreCase(nric)) {
									officer = o;
								}
							}
							if (officer != null) {
								System.out.println("\n" + officer.getName() + " with NRIC of " + officer.getNRIC() + " has the following pending projects");
								List<Project> pending = officer.getPendingProjects();
								officer.getPendingProjects().forEach(p -> System.out.println(p.getName()));
								System.out.println("Which project would you like to approve/deny: ");
								String name = scanner.nextLine();
								Optional<Project> s = pending.stream()
										.filter(p->p.getName().equalsIgnoreCase(name))
										.findFirst();
								Project proj;
								if (s.isPresent()) {
									proj = s.get();
								} else {
									System.out.println("Project not found.");
									continue;
								}
								System.out.print("\nEnter new status (Approved/Denied): ");
								String status = scanner.nextLine();
								projectController.processRegistrations(hdbmanager, proj, officer,status);
								break;
							} else {
								System.out.println("\nNo officer with this NRIC was found, please try again.");
							}
							break;
						}
						break;
					}
				}
				break;

				// Display pending / withdrawing applicants
			case 9:
				projectList = hdbmanager.getCreatedProjects();
				int count = 0;
				for (Project project : projectList) {
					if (project != null && project.getManagerName().equalsIgnoreCase(hdbmanager.getName())) {
						System.out.println("\nProject Name: " + project.getName());
						System.out.println("\nPending applicants:");
						for (Applicant pendingApplicant : applicantList) {
							if (pendingApplicant.getApplicationStatus().equalsIgnoreCase("pending")) {
								System.out.println(" - " + pendingApplicant.getName()  + " " + pendingApplicant.getNRIC());
								count += 1;
							}
						}
						if (count == 0) {
							System.out.println("No applicants pending.");
						}
						count = 0;
						System.out.println("\nWithdrawing applicants:");
						for (Applicant withdrawingApplicant : applicantList) {
							if (withdrawingApplicant.getApplicationStatus().equalsIgnoreCase("withdrawing")) {
								System.out.println(" - " + withdrawingApplicant.getName()  + " " + withdrawingApplicant.getNRIC());
								count += 1;
							}
						}
						if (count == 0) {
							System.out.println("No applicants withdrawing.");
						}
					} else {
						System.out.println("\nYou did not create any projects.");
						break;
					}
					break;
				}
				break;

				// Approve / reject BTO applications
			case 10:
				while (true) {
					System.out.print("\nEnter applicant's NRIC (or Quit): ");
					String nric = scanner.nextLine();
					if (nric.equalsIgnoreCase("quit")) break;
					if (!nric.matches("[ST]\\d{7}[A-Z]")) {
						//check if nric is valid
						System.out.println("\nInvalid NRIC given, please try again.");
					} else {
						while (true) {
							List<Applicant> pendingApplicants = new ArrayList<Applicant>();
							pendingApplicants = applicantList.stream()
									.filter(a -> a.getApplicationStatus().equals("Pending"))
									.collect(Collectors.toList());

							for (Applicant a: pendingApplicants) {
								if (a.getNRIC().equalsIgnoreCase(nric)) {
									applicant = a;
								}
							}
							if (applicant != null) {
								System.out.println("\n" + applicant.getName() + " with NRIC of " + applicant.getNRIC() + " has a BTO status of: " + applicant.getApplicationStatus());
								while (true) {
									System.out.print("\nEnter new status (Successful/Unsuccessful): ");
									String status = scanner.nextLine();
									List<String> flatAvailability = projectController.getFlatAvailability(applicant);
									if ("successful".equalsIgnoreCase(status)) {
										if (flatAvailability.isEmpty()) {
											System.out.println("No flats available for " + applicant.getName());
										} else {
											System.out.println(applicant.getName() + "'s status is changed to Successful");
											applicant.setApplicationStatus(status);
											break;
										}

									} else if ("Unsuccessful".equalsIgnoreCase(status)){
										System.out.println(applicant.getName() + "'s status is changed to Unsuccessful");
										applicant.setApplicationStatus(status);
										break;

									} else if ("Pending".equalsIgnoreCase(status)) {
										System.out.println(applicant.getName() + "'s status is changed to Pending");
										applicant.setApplicationStatus(status);
										break;
									} else {
										System.out.println("\nInvalid status.");
									}
								}
							} else {
								System.out.println("\nNo applicant with this NRIC was found, please try again.");
								break;
							}
							break;
						}
						break;
					}
				}
				break;

				// Approve / reject withdrawal applications
			case 11:
				while (true) {
					System.out.print("\nEnter applicant's NRIC: ");
					String nric = scanner.nextLine();
					if (!nric.matches("[ST]\\d{7}[A-Z]")) {
						//check if nric is valid
						System.out.println("\nInvalid NRIC given, please try again.");
					} else {
						while (true) {
							for (Applicant a: applicantList) {
								if (a.getNRIC().equalsIgnoreCase(nric)) {
									applicant = a;
								}
							}
							if (applicant != null) {
								System.out.println("\n" + applicant.getName() + " with NRIC of " + applicant.getNRIC() + " has a withdrawal status of: " + applicant.getWithdrawalStatus());
								while (true) {
									System.out.print("\nEnter new status (approved/denied): ");
									String status = scanner.nextLine();
									if(projectController.processWithdrawal(hdbmanager, applicant, status))
										break;
									else {
										System.out.println("Invalid Status");
									}
								}
								break;
							} else {
								System.out.println("\nNo applicant with this NRIC was found, please try again.");
							}
						}
						break;
					}
				}
				break;

				// Generate report of applicants
			case 12:
				System.out.println("\n\nApplicant Report Generator");
				System.out.println("1. View All Applicants");
				System.out.println("2. Filter by Marital Status");
				System.out.println("3. Filter by Flat Type");
				System.out.println("4. Filter by Project Name");
				System.out.println("5. Filter by Age Range");
				System.out.print("\nChoose a filter option: ");

				filterOption = scanner.nextInt();
				scanner.nextLine(); // consume newline

				Stream<Applicant> stream = applicantList.stream();

				switch (filterOption) {
				case 1:
					// No filters
					break;
				case 2:
					System.out.print("\nEnter marital status to filter by (e.g., Married): ");
					String status = scanner.nextLine();
					stream = stream.filter(a -> a.getMaritalStatus().equalsIgnoreCase(status));
					break;
				case 3:
					System.out.print("\nEnter flat type to filter by (e.g., 2-Room): ");
					String flatType = scanner.nextLine();
					stream = stream.filter(a ->
					a.getAppliedProject() != null &&
					a.getAppliedProject().getFlatTypeAvailable() != null &&
					a.getAppliedProject().getFlatTypeAvailable().containsKey(flatType)
							);
					break;
				case 4:
					System.out.print("\nEnter project name to filter by: ");
					String project = scanner.nextLine();
					stream = stream.filter(a ->
					a.getAppliedProject() != null &&
					a.getAppliedProject().getName().equalsIgnoreCase(project)
							);
					break;
				case 5:
					while (true) {
						try {
							System.out.print("\nEnter min age: ");
							int minAge = scanner.nextInt();
							System.out.print("\nEnter max age: ");
							int maxAge = scanner.nextInt();
							scanner.nextLine();
							stream = stream.filter(a -> a.getAge() >= minAge && a.getAge() <= maxAge);
							break;
						} catch (InputMismatchException e) {
							System.out.println("\n Invalid input. Please enter a number.");
							scanner.nextLine();
						}
					}
					break;
				default:
					System.out.println("\nInvalid filter option.");
					break; // Exit early
				}

				List<Applicant> filteredApplicants = stream.collect(Collectors.toList());

				System.out.println("\n\n--- Filtered Applicant Report ---\n");
				if (filteredApplicants.isEmpty()) {
					System.out.println("\nNo applicants match the selected filter.");
				} else {
					filteredApplicants.forEach(a -> {
						String flatTypes = "N/A";
						String project = "N/A";

						if (a.getAppliedProject() != null) {
							project = a.getAppliedProjectString();
							if (a.getFlatType() != ""){
								flatTypes = a.getFlatType();
							}
							// Map<String, Integer> flatMap = a.getAppliedProject().getFlatTypeAvailable();
							// if (flatMap != null) {
							//     flatTypes = String.join(", ", flatMap.keySet());
							// }
						}

						System.out.printf("\nName: %s | Age: %d | Marital Status: %s | Flat Type(s): %s | Project: %s%n",
								a.getName(), a.getAge(), a.getMaritalStatus(), flatTypes, project);
					});
				}
				break;


				// View inquiries for ALL projects
			case 13:
				inquiryList = InquiryController.allInquiries();
				System.out.println("\nInquires:\n");
				for (int inq=1; inq<=inquiryList.size(); inq++) {
					if (inquiryList.get(inq-1).getStatus().equalsIgnoreCase("open")){
						System.out.print(inq);
						System.out.print(". ");
						System.out.print(inquiryList.get(inq-1).getRelatedProject().getName());
						System.out.print(": ");
						System.out.print(inquiryList.get(inq-1).getMessage());
						System.out.print("\n");
					}else if (inquiryList.get(inq-1).getStatus().equalsIgnoreCase("resolved")){
						System.out.print(inq);
						System.out.print(". ");
						System.out.print(inquiryList.get(inq-1).getRelatedProject().getName());
						System.out.print(": ");
						System.out.print(inquiryList.get(inq-1).getMessage());
						System.err.print(" (Resolved)");
						System.out.print("\n");
					}

				}
				break;

				// View and reply to inquiries regarding project that hdb manager is handling
			case 14:
				// Display only projects created by current HDB manager user
				List <Project> allProjList = hdbmanager.getCreatedProjects();
				projectList.clear();
				for (Project projs:allProjList){
					if (projs.getManagerName().equalsIgnoreCase(hdbmanager.getName())){
						projectList.add(projs);
					}
				}
				if (projectList.size() <= 0){
					System.out.println("\nNo projects found, exiting back to menu");
					break;
				}
				// Display all projects for HDB manager to reply to inquiries
				System.out.println("\nWhich project do you want to reply to inquiries?");

				System.out.println("\nProjects Created:\n");
				for (int proj=1; proj<=projectList.size(); proj++) {
					System.out.print(proj);
					System.out.print(". ");
					System.out.println(projectList.get(proj-1).getName());
				}

				// Select project
				System.out.println("\nSelect the project you wish to reply to (or 0 to quit): ");
				try {
					projectIndex = scanner.nextInt();
					scanner.nextLine();
					if (projectIndex == 0) break;
					if (projectIndex < 0 || projectIndex > projectList.size()) {
						System.out.println("\nProject does not exist.");
						break;
					}
					chosenProject = projectList.get(projectIndex-1);
				} catch (InputMismatchException e) {
					System.out.println("\nInvalid input. Please enter a number.");
					scanner.nextLine();
					break;
				}
				
				
				// View inquiries for chosen project
				System.out.println("\nInquires for project " + chosenProject.getName() + ":\n");
				List <Inquiry> inqList = InquiryController.viewInquiries(chosenProject);
				inquiryList = new ArrayList<>();
				int idx = 1;
				for (Inquiry inq:inqList) {
					if (inq.getStatus().equalsIgnoreCase("open")){
						inquiryList.add(inq);
						System.out.print(idx);
						System.out.print(". ");
						System.out.print(inq.getRelatedProject().getName());
						System.out.print(": ");
						System.out.print(inq.getMessage());
						System.out.print("\n");
					}
				}

				// Select inquiry to reply to
				System.out.println("Which inquiry do you wish to reply to:");
				int inquiryIndex = scanner.nextInt() - 1;
				scanner.nextLine();
				if (inquiryIndex < 0 || inquiryIndex >= inquiryList.size()) {
					System.out.println("\nInquiry does not exist.");
					break;
				}
				Inquiry chosenInquiry = inquiryList.get(inquiryIndex);

				// Enter reply message to inquiry
				System.out.print("\nEnter reply: ");
				String replyMessage = scanner.nextLine();
				replyToProjInquiry(chosenInquiry.getInquiryId(), replyMessage);
				// // Update inquiry reply
				// InquiryController.replyToInquiry(chosenInquiry.getInquiryId(), replyMessage);
				// System.out.println("\nIs the inquiry resolved? (Yes/No)");
				// String ans = scanner.nextLine();
				// if (ans.equalsIgnoreCase("yes")){
				// 	InquiryController.resolveInquiry(chosenInquiry.getInquiryId());
				// 	System.out.println("\nInquiry resolved");
				// } else{
				// 	System.out.println("Inquiry remains Unresolved");
				// }
				break;

			case 15:
				// Filter project list based on user-specified criteria
				System.out.println("\n\nFilter projects by:");
				System.out.println("1. Location");
				System.out.println("2. Max Price");
				System.out.println("3. Min Price");
				System.out.println("4. Clear all filters");
				System.out.print("\nSelect a filter option: ");

				try {
					filterOption = Integer.parseInt(scanner.nextLine());

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

			case 16:
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
	/**
	 * Replies to a project inquiry by updating the inquiry message and optionally resolving it.
	 *
	 * @param inquiryId     The ID of the inquiry to respond to.
	 * @param replyMessage  The message to be sent as a reply to the inquiry.
	 */
	public void viewProjectInquiries(List<Project> proj){
		for (Project project : proj) {
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
				System.out.println("Manager of project: " + project.getManagerName());
				System.out.println("Officer slots for project: " + project.getOfficerSlot());
				System.out.println("Officers of project: ");
				for (HDBOfficer hdbofficer: project.getOfficers()) {
					System.out.println(" - " + hdbofficer.getName());
				}
			} else {
				System.out.println("\nNo project has been created.");
			}
		}
	}


/**
 * Asks the user if the inquiry has been resolved and updates the inquiry status accordingly.
 *
 * @param inquiryId The ID of the inquiry to resolve.
 */
	public void replyToProjInquiry(String inquiryId, String replyMessage){
		// Update inquiry reply
		InquiryController.replyToInquiry(inquiryId, replyMessage);
		resolveInquiry(inquiryId);
		System.out.println("\nInquiry replied to successfully.");
	}
    public void resolveInquiry(String inquiryId){
		System.out.println("\nIs the inquiry resolved? (Yes/No)");
		Scanner scanner = new Scanner(System.in);
		String ans = scanner.nextLine();
		if (ans.equalsIgnoreCase("yes")){
			InquiryController.resolveInquiry(inquiryId);
			System.out.println("\nInquiry resolved");
		} else{
			System.out.println("Inquiry remains Unresolved");
		}
	}
}
