package boundary;

import java.util.Scanner;

public class HDBOfficerUI {
    
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        // Display menu
        System.out.println("HDB Officer Menu:");
        System.out.println("1. Register for a project");
        System.out.println("2. View registration status for projects");
        System.out.println("3. View project details");
        System.out.println("4. Respond to enquiries");
        System.out.println("5. Update BTO status");
        System.out.println("6. Generate applicant receipt");
        System.out.print("Select an option: ");
        
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                registerForProject();
                break;
            case 2:
                viewRegistrationStatus();
                break;
            case 3:
                viewProjectDetails();
                break;
            case 4:
                respondToEnquiries();
            case 5:
                updateBTOStatus();
            case 6:
                generateReceipt();
            default:
                System.out.println("Invalid option.");
        }
        
        scanner.close();
    }
    
    private void registerForProject() {
        // Display projects to register
        System.out.println("Viewing available projects...");
        
    }
    
    private void viewRegistrationStatus() {
        // Implement logic to respond to enquiries
        System.out.println("Responding to enquiries...");
    }

    private void viewProjectDetails() {
        // Implement logic to respond to enquiries
        System.out.println("Responding to enquiries...");
    }

    private void respondToEnquiries() {
        // Implement logic to respond to enquiries
        System.out.println("Responding to enquiries...");
    }

    private void updateBTOStatus() {
        // Implement logic to respond to enquiries
        System.out.println("Responding to enquiries...");
    }
    
    private void generateReceipt() {
        // Implement logic to generate applicant receipt
        System.out.println("Generating receipt...");
    }
}
