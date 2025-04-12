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

	// check if NRIC and password matches, if so, login as said user.
	public User login(String nric, String password) {
		for (User u : userList) {
			if (u.getNRIC().equals(nric) && u.getPassword().equals(password)) {
				return u;

			}
		}
		return null;

	}


	// If password length is >= 6, change the password of user, else return false
	public boolean changePassword(User user, String toChange){
		if (toChange.length() >= 6){
			user.setPassword(toChange);
			return true;
		}

		return false;
	}
}
