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
		List<Applicant> applicantList = DataManager.loadUsers("data/ApplicantList.csv", Applicant.class);
		List<HDBManager> managerList = DataManager.loadUsers("data/ManagerList.csv", HDBManager.class);
		List<HDBOfficer> officerList = DataManager.loadUsers("data/OfficerList.csv", HDBOfficer.class);
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


}
