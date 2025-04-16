package application;

import java.util.List;


import boundary.ApplicantUI;
import boundary.LoginMenu;
import control.AuthController;
import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import helpers.DataManager;

public class Main {
	public static void main(String[] args) {
		LoginMenu menu = new LoginMenu();
		menu.showMenu(DataManager.getApplicants(), DataManager.getManagers(), DataManager.getOfficers());
	}

}
