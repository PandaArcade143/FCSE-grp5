package boundary;

import java.util.Scanner;

public class HDBManagerUI {
    
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("HDB Manager Menu:");
        System.out.println("1. Create a new BTO project");
        System.out.println("2. Edit an existing project");
        System.out.println("3. Delete a project");
        System.out.println("4. View and approve HDB Officer registrations");
        System.out.println("5. Approve or reject applicant's BTO application");
        System.out.println("6. Generate reports");
        System.out.print("Select an option: ");
        
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                createBTOProject();
                break;
            case 2:
                editBTOProject();
                break;
            case 3:
                deleteBTOProject();
                break;
            case 4:
                viewHDBOfficerRegistrations();
                break;
            case 5:
                approveApplicantApplication();
                break;
            case 6:
                generateReports();
                break;
            default:
                System.out.println("Invalid option.");
        }
        
        scanner.close();
    }
    
    private void createBTOProject() {
        // Implement logic to create a new BTO project
        System.out.println("Creating a new BTO project...");
    }
    
    private void editBTOProject() {
        // Implement logic to edit an existing BTO project
        System.out.println("Editing an existing BTO project...");
    }
    
    private void deleteBTOProject() {
        // Implement logic to delete a BTO project
        System.out.println("Deleting a BTO project...");
    }
    
    private void viewHDBOfficerRegistrations() {
        // Implement logic to view and approve HDB Officer registrations
        System.out.println("Viewing HDB Officer registrations...");
    }
    
    private void approveApplicantApplication() {
        // Implement logic to approve or reject applicant's BTO application
        System.out.println("Approving/rejecting applicant's application...");
    }
    
    private void generateReports() {
        // Implement logic to generate reports
        System.out.println("Generating reports...");
    }
}
