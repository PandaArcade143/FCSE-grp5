package boundary;

import java.util.List;
import java.util.Scanner;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.User;
import control.AuthController;

public class LoginMenu {
    AuthController authController = new AuthController();

    //Display the login menu and redirect user based on role
    public Object showMenu(List<Applicant> applicantList, List<HDBManager> managerList, List<HDBOfficer> officerList) {
        Scanner scanner = new Scanner(System.in);
        Object user = null;

        System.out.println("Welcome to the BTO Management System (Enter 'Quit' to quit).");

        // NRIC input and validation loop
        while (true) {
            System.out.print("Enter your NRIC: ");
            String nric = scanner.nextLine().trim();

            if (nric.equalsIgnoreCase("Quit")) {
                return null;
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
                return null;
            }

            if (!((User) user).getPassword().equals(password)) {
                System.out.println("Invalid password. Please try again.");
            } else {
                break;
            }
        }

        // Successful login
        User loggedInUser = (User) user;
        System.out.println("\nLogin successful! Welcome " + loggedInUser.getName() + "!");
        System.out.println("Age: " + loggedInUser.getAge());
        System.out.println("Marital Status: " + loggedInUser.getMaritalStatus());

        // Redirect based on role
        switch (loggedInUser.getRole()) {
            case "Applicant":
                new ApplicantUI().showMenu((Applicant) loggedInUser);
                break;
            case "HDBManager":
                new HDBManagerUI().showMenu((HDBManager) loggedInUser);
                break;
            case "HDBOfficer":
                new HDBOfficerUI().showMenu((HDBOfficer) loggedInUser);
                break;
            default:
                System.out.println("Unknown role detected.");
        }

        return null;
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
