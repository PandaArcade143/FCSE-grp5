package application;


import boundary.LoginMenu;
import helpers.DataManager;


/**
 * Entry point for the BTO Application and Management System.
 * Initializes the login menu and starts the user interface.
 */
public class Main {
	/**
     * Main method to launch the application.
     * It initializes the login menu and provides user data retrieved from the DataManager.
     *
     * @param args command-line arguments (not used)
     */
	public static void main(String[] args) {
		LoginMenu menu = new LoginMenu();
		menu.showMenu(DataManager.getApplicants(), DataManager.getManagers(), DataManager.getOfficers());
	}

}
