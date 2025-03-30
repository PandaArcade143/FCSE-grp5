package entity;

public class HDBOfficer extends Applicant{

	public HDBOfficer(String name, String nric, int age, String maritalStatus, String password) {
		super(name, nric, age, maritalStatus, password);
	}

	public String getRole() {
		return "HDBOfficer";
	}

}
