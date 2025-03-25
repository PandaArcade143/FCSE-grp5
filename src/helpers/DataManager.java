package helpers;

import java.io.*;
import java.util.*;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.User;

public class DataManager {
	// Generic method for reading users
	public static <T extends User> List<T> loadUsers(String path, Class<T> c) {
		List<T> users = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String currentLine;
			br.readLine();
			
			while ((currentLine = br.readLine()) != null) {
				String[] data = currentLine.split(",");
				
				if (data.length < 5) {
					throw new IOException("Row has missing data in " + path);
				}
				
				String name = data[0];
				String nric = data[1];
				int age = Integer.parseInt(data[2]);
				String maritalStatus = data[3];
				String password = data[4];
				
				T user = null;
				
				// Create object and cast to the corresponding type
				if (c == Applicant.class) {
					user = c.cast(new Applicant(name, nric, age, maritalStatus, password));
				} else if (c == HDBOfficer.class) {
					user = c.cast(new HDBOfficer(name, nric, age, maritalStatus, password));
				} else if (c == HDBManager.class) {
					user = c.cast(new HDBManager(name, nric, age, maritalStatus, password));
				}
				
				if (user != null) {
					users.add(user);
				}
			}
			
		} catch (IOException e) {
			System.out.println("Error while reading file: " + e.getMessage());
		}
		
		return users;
	}
	
	// TODO: Read project file

}
