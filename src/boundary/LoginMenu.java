package boundary;

import java.util.List;
import java.util.Scanner;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.User;
import control.AuthController;

/**
 * The {@code LoginMenu} class handles user authentication and redirection
 * to appropriate role-based menus within the BTO Management System.
 * <p>
 * It validates user credentials, authenticates the login,
 * and displays a role-based menu for further interaction.
 */
public class LoginMenu {

    /**
     * Displays the login menu for the system and processes user authentication.
     * <p>
     * It performs NRIC validation, password checking, and then redirects users
     * to different menus based on their roles (Applicant, HDBManager, or HDBOfficer).
     *
     * @param applicantList the list of registered applicants
     * @param managerList the list of registered HDB managers
     * @param officerList the list of registered HDB officers
     */
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
			System.out.print("\nEnter your password: ");
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
					new HDBManagerUI().showMenu((HDBManager) loggedInUser);
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
				return;

			default:
				// Notify user if selection is invalid
				System.out.println("\nInvalid option. Please try again.");
			}
		}
	}
	
    /**
     * Finds a {@link User} in the provided list based on the specified NRIC.
     * <p>
     * This generic helper method checks each object in the list to determine
     * if it is a {@code User} and compares its NRIC (case-insensitive) with the input.
     *
     * @param nric the NRIC to search for
     * @param list a list of any type, expected to contain {@code User} objects
     * @return the matching {@code User} if found; {@code null} otherwise
     * @param <T> the type parameter of the list elements
     */
	private static <T> User findUserByNRIC(String nric, List<T> list) {
		for (T u : list) {
			if (u instanceof User && ((User) u).getNRIC().equalsIgnoreCase(nric)) {
				return (User) u;
			}
		}
		return null;
	}
}
