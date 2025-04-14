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
		// Loads the lists on startup
		// this should go into the controller
		List<Applicant> applicantList = DataManager.loadUsers("data/ApplicantList.csv", Applicant.class);
		List<HDBManager> managerList = DataManager.loadUsers("data/ManagerList.csv", HDBManager.class);
		List<HDBOfficer> officerList = DataManager.loadUsers("data/OfficerList.csv", HDBOfficer.class);

		for (Applicant a : applicantList) {
			System.out.println(a.getName());
		}
		AuthController authcontroller = new AuthController();
	}

}
