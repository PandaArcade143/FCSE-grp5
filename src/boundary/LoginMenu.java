package boundary;

import java.util.List;
import java.util.Scanner;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.User;
import control.AuthController;

public class LoginMenu{
	AuthController authController = new AuthController();
    
    public Object showMenu(List<Applicant> applicantList, List<HDBManager> managerList, List<HDBOfficer> officerList) {
        Scanner scanner = new Scanner(System.in);
        Object user = null;
        String nric = "";
        
        // Display login prompt
        System.out.println("Welcome to the BTO Management System (Enter 'Quit' to quit).");
        while (true) {
            System.out.print("Enter your NRIC: ");
            nric = scanner.nextLine();
            if (nric.compareToIgnoreCase("Quit") == 0) {
            	scanner.close();
            	return null;
            } else if (!validateNRIC(nric)) {
            	//check if nric is valid
            	System.out.println("Invalid NRIC given, please try again.");
            } else {
            	 user = findUserByNRIC(nric, applicantList);
                 if (user == null) {
                     user = findUserByNRIC(nric, managerList);
                 }
                 if (user == null) {
                     user = findUserByNRIC(nric, officerList);
                 }

                 if (user == null) {
                     System.out.println("User for this NRIC does not exist, please try again.");
                 } else {
                 	break;
                 }
            }
            
        }
        
        while (true) {
        	System.out.print("Enter your password: ");
        	String password = scanner.nextLine();
        	if (password.compareToIgnoreCase("Quit") == 0) {
                scanner.close();
            	return null;
        	} else if (((User) user).getPassword().compareToIgnoreCase(password) != 0) {
        		System.out.println("Invalid password, please try again.");
        	} else {
        		System.out.println("Login successful! Welcome " + ((User) user).getName() + "!");
        		System.out.println("Age: " + ((User) user).getAge());
        		System.out.println("Marital Status: " + ((User) user).getMaritalStatus());
                if (((User) user).getRole() == "Applicant") {
                	ApplicantUI applicantUI = new ApplicantUI();
                	applicantUI.showMenu((Applicant) user);
                } else if (((User) user).getRole() == "HDBManager") {
                	HDBManagerUI hdbManagerUI = new HDBManagerUI();
                	hdbManagerUI.showMenu((HDBManager) user);
                } else if (((User) user).getRole() == "HDBOfficer") {
                	HDBOfficerUI hdbOfficerUI = new HDBOfficerUI();
                	hdbOfficerUI.showMenu((HDBOfficer) user);
                }
                scanner.close();
                return null;
        	}
        }
        
    }
    
    // Validates the NRIC format (starts with 'S' or 'T', followed by 7 digits, and ends with a letter)
    private boolean validateNRIC(String nric) {
        return nric.matches("[ST]\\d{7}[A-Z]");
    }
    
    private static <T> User findUserByNRIC(String nric, List<T> list) {
        for (T user : list) {
            if (((User) user).getNRIC().equalsIgnoreCase(nric)) {
                return (User) user;
            }
        }
        return null;
    }
}