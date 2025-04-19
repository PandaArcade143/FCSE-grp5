package entity;

/**
 * Abstract representation of a user in the BTO system.
 * This class serves as the base class for Applicants, HDB Officers, and HDB Managers.
 */

public abstract class User {
	private String name;
	private String nric;
	private int age;
	private String maritalStatus;
	private String password;
	
	/**
	 * Constructs a User with the specified attributes.
	 *
	 * @param name the full name of the user
	 * @param nric the NRIC of the user
	 * @param age the age of the user
	 * @param maritalStatus the marital status of the user
	 * @param password the password used for login authentication
	 */

	public User(String name, String nric, int age, String maritalStatus, String password) {
		this.name = name;
		this.nric = nric;
		this.setAge(age);
		this.setMaritalStatus(maritalStatus);
		this.password = password;
	}

	/**
	 * Gets the user's name.
	 *
	 * @return the user's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the user's NRIC.
	 *
	 * @return the user's NRIC
	 */
	public String getNRIC() {
		return nric;
	}
	
	/**
	 * Gets the user's login password.
	 *
	 * @return the user's password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Gets the role of the user.
	 * Implemented by subclasses to return "Applicant", "HDBOfficer", or "HDBManager".
	 *
	 * @return the user role
	 */
	abstract public String getRole();

	/**
	 * Sets the user's password.
	 *
	 * @param newPassword the new password to set
	 */
	public void setPassword(String newPassword) {
		password = newPassword;
		
	}
	
	/**
	 * Gets the user's age.
	 *
	 * @return the user's age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Sets the user's age.
	 *
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Gets the user's marital status.
	 *
	 * @return the user's marital status
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * Sets the user's marital status.
	 *
	 * @param maritalStatus the marital status to set
	 */
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	
}
