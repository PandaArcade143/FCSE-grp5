package entity;

public class HDBManager extends Applicant {

	public HDBManager(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
	}
	
	public String getRole() {
		return "HDBManager";
		
	}

}
