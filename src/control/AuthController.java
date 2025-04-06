package control;

import java.util.ArrayList;
import java.util.List;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.User;
import helpers.DataManager;
public class AuthController {
	private List<User> userList;
	
	public AuthController() {
		List<Applicant> applicantList = DataManager.getApplicants();
		List<HDBManager> managerList = DataManager.getManagers();
		List<HDBOfficer> officerList = DataManager.getOfficers();
		userList = new ArrayList<>();
		userList.addAll(applicantList);
		userList.addAll(managerList);
		userList.addAll(officerList);
	}

	public User login(String nric, String password) {
		for (User u : userList) {
			if (u.getNRIC().equals(nric) && u.getPassword().equals(password)) {
				return u;

			}
		}
		return null;

	}

	/*
	public String changePassword(String toChange){
		// I assume that they need to login first before changing password?
		// How would we proceed with this? Ask for password to change then ask for user login again to reconfirm?
	}
	*/
}
