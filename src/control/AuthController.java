package control;

import java.util.ArrayList;

import java.util.List;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.User;
import helpers.DataManager;


/**
 * Controls authentication-related logic such as login and password changes.
 * Combines all users from applicants, managers, and officers into a unified user list.
 */
public class AuthController {
	private List<User> userList;
	
	/**
     * Constructs the AuthController and loads all users from DataManager.
     * Combines applicants, HDB managers, and HDB officers into a single list for authentication.
     */
	public AuthController() {
		List<Applicant> applicantList = DataManager.getApplicants();
		List<HDBManager> managerList = DataManager.getManagers();
		List<HDBOfficer> officerList = DataManager.getOfficers();
		userList = new ArrayList<>();
		userList.addAll(applicantList);
		userList.addAll(managerList);
		userList.addAll(officerList);
	}

	/**
     * Authenticates a user by matching NRIC and password.
     *
     * @param nric the NRIC provided at login
     * @param password the password provided at login
     * @return the authenticated User if credentials match, null otherwise
     */
	public User login(String nric, String password) {
		for (User u : userList) {
			if (u.getNRIC().equals(nric) && u.getPassword().equals(password)) {
				return u;

			}
		}
		return null;

	}

	/**
     * Changes the user's password if the new password meets the minimum length requirement.
     *
     * @param user the User whose password is being changed
     * @param toChange the new password to assign
     * @return true if the password was successfully changed, false otherwise
     */
	public boolean changePassword(User user, String toChange){
		if (toChange.length() >= 6){
			user.setPassword(toChange);
			return true;
		}

		return false;
	}
}
