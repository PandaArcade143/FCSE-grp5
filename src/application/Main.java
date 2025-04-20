package application;


import boundary.LoginMenu;
import helpers.DataManager;

public class Main {
	public static void main(String[] args) {
		LoginMenu menu = new LoginMenu();
		menu.showMenu(DataManager.getApplicants(), DataManager.getManagers(), DataManager.getOfficers());
	}

}
