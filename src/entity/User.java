package entity;

public abstract class User {
	private String name;
	private String nric;
	private int age;
	private String maritalStatus;
	private String password;
	
	public User(String name, String nric, int age, String maritalStatus, String password) {
		this.name = name;
		this.nric = nric;
		this.age = age;
		this.maritalStatus = maritalStatus;
		this.password = password;
	}
	
	// TODO: Rest of the getters
	public String getName() {
		return name;
	}
	
	public String getNRIC() {
		return nric;
	}
	
	public String getPassword() {
		return password;
	}
	
	abstract public String getRole();

	public void setPassword(String newPassword) {
		password = newPassword;
		
	}
}
