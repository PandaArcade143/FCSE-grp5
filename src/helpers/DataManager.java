package helpers;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.Project;
import entity.User;

public class DataManager {
	
	
	// custom parser to work with quoted strings
	private static String[] smartSplit(String line) {
		List<String> res = new ArrayList<>();
		boolean inQuotes = false;
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < line.length(); i++) {
			char ch = line.charAt(i);
			
			if (ch == '\"') {
				inQuotes = !inQuotes;
			} else if (ch == ',' && !inQuotes) {
				res.add(sb.toString().trim());
				sb.setLength(0);
			} else {
				sb.append(ch);
			}
		}
		
		// to add the last field
		res.add(sb.toString().trim());
		return res.toArray(new String[0]);
	}
	
	
	
	
	
	// Generic method for reading users
	public static <T extends User> List<T> loadUsers(String path, Class<T> c) {
		List<T> users = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String currentLine;
			br.readLine();
			
			while ((currentLine = br.readLine()) != null) {
				String[] data = smartSplit(currentLine);
				
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
	
	public static List<Project> loadProjects(String path) {
		List<Project> projects = new ArrayList<Project>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			
			String currentLine;
			br.readLine();
			while ((currentLine = br.readLine()) != null) {
				String[] data = smartSplit(currentLine);
				String projectName = data[0];
				String neighbourHood= data[1];
				Map<String, Integer> flatTypeTotal = new HashMap<>();
				
				for (int i = 0; i < Integer.parseInt(data[3]); i++) {
					flatTypeTotal.put(data[2], Integer.parseInt(data[4]));
				}
				for (int i = 0; i < Integer.parseInt(data[6]); i++) {
					flatTypeTotal.put(data[5], Integer.parseInt(data[7]));
				}

				SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yy");
				Date openingDate = formatter.parse(data[8]);
				Date closingDate = formatter.parse(data[9]);
				String manager = data[10];
				int officerSlot = Integer.parseInt(data[10]);
				String[] officers = smartSplit(data[11]);
			}
				
			
		} catch (IOException e) {
			System.out.println("Error while reading file: " + e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return projects;
		
		
	}
	
	// TODO: Read project file

}
