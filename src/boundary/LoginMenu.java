package boundary;

import java.util.List;
import java.util.Scanner;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.User;
import control.AuthController;

public class LoginMenu {

    //Display the login menu and redirect user based on role
    public void showMenu(List<Applicant> applicantList, List<HDBManager> managerList, List<HDBOfficer> officerList) {
    	AuthController authController = new AuthController();
    	Scanner scanner = new Scanner(System.in);
        Object user = null;
        String nric;

        System.out.println("Welcome to the BTO Management System (Enter 'Quit' to quit).");

        // NRIC input and validation loop
        while (true) {
            System.out.print("\nEnter your NRIC: ");
            nric = scanner.nextLine().trim();

            if (nric.equalsIgnoreCase("Quit")) {
            	scanner.close();
                return;
            }

            // NRIC validation: starts with S/T, 7 digits, ends with uppercase letter
            if (!nric.matches("[ST]\\d{7}[A-Z]")) {
                System.out.println("Invalid NRIC format. Please enter S/T + 7 digits + letter (e.g., S1234567A).");
                continue;
            }
            

            // Search user in all role lists
            user = findUserByNRIC(nric, applicantList);
            if (user == null) user = findUserByNRIC(nric, managerList);
            if (user == null) user = findUserByNRIC(nric, officerList);

            if (user == null) {
                System.out.println("No user found for this NRIC. Please try again.");
            } else {
                break;
            }
        }

        // Password input loop
        while (true) {
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (password.equalsIgnoreCase("Quit")) {
            	scanner.close();
                return;
            }
            
            
            if (authController.login(nric, password) == null) {
            	System.out.println("Invalid password. Please try again.");
            } else {
            	break;
            }

//            if (!((User) user).getPassword().equals(password)) {
//                System.out.println("Invalid password. Please try again.");
//            } else {
//                break;
//            }
        }

        // Successful login
        User loggedInUser = (User) user;
        System.out.println("\nLogin successful! Welcome " + loggedInUser.getName() + "!");
        System.out.println("Age: " + loggedInUser.getAge());
        System.out.println("Marital Status: " + loggedInUser.getMaritalStatus());
        
        // Menu
        while (true) {
        	System.out.println("\n\n\nLogin Menu:");
        	System.out.println("1. Proceed to other menus");
        	System.out.println("2. Change password");
        	System.out.println("3. Quit");
        	
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
             		// Redirect based on role
                    String menuChoice;
                    switch (loggedInUser.getRole()) {
                        case "Applicant":
                            new ApplicantUI().showMenu((Applicant) loggedInUser);
                            break;
                        case "HDBManager":
                        	while (true) {
                        		System.out.println("\nAccess Applicant Menu or Manager Menu?: ");
                                menuChoice = scanner.nextLine();
                                if (menuChoice.equalsIgnoreCase("Applicant")) {
                                	new ApplicantUI().showMenu((Applicant) loggedInUser);
                                    break;
                                } else if (menuChoice.equalsIgnoreCase("Manager")) {
                                	new HDBManagerUI().showMenu((HDBManager) loggedInUser);
                                    break;
                                } else {
                                	System.out.println("Invalid option, please try again.");
                                }
                        	}
                            break;
                        case "HDBOfficer":
                        	while (true) {
                        		System.out.println("\nAccess Applicant Menu or Officer Menu?: ");
                                menuChoice = scanner.nextLine();
                                if (menuChoice.equalsIgnoreCase("Applicant")) {
                                	new ApplicantUI().showMenu((Applicant) loggedInUser);
                                    break;
                                } else if (menuChoice.equalsIgnoreCase("Officer")) {
                                	new HDBOfficerUI().showMenu((HDBOfficer) loggedInUser);
                                    break;
                                } else {
                                	System.out.println("Invalid option, please try again.");
                                }
                        	}
                            break;
                    }
                    scanner.close();
                    return;
                    
             	case 2:
             		// Change user's password
             		while (true) {
             			System.out.println("\nEnter new password: ");
             			String newPassword = scanner.nextLine();
             			if (newPassword.equals(((User) user).getPassword())) {
             				System.out.println("Please enter a different password from your old one.");
             			} else if (newPassword.length() < 6) {
             				System.out.println("Please enter a password that is 6 characters or longer.");
             			} else {
             				System.out.println("\nRe-enter the password: ");
                 			String newPasswordConfirmation = scanner.nextLine();
                 			if (newPasswordConfirmation.equals(newPassword)) {
                 				authController.changePassword((User) user, newPassword);
                 				System.out.println("\nPassword successfully changed!");
                 				break;
                 			} else {
                 				System.out.println("Re-entered password does not match the new password, please try again.");
                 				break;
                 			}
             			}
             		}
             		break;
             		
             	case 3:
                    // Exit the menu and application loop
                    System.out.println("\nGoodbye!");
                    scanner.close();
                    break;
                	
                default:
                	// Notify user if selection is invalid
                    System.out.println("\nInvalid option. Please try again.");
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
