package helpers;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.Inquiry;
import entity.Project;
import entity.User;

public class DataManager {
	private static List<Project> projects = new ArrayList<>();
	private static List<Applicant> applicants = new ArrayList<>();
	private static List<HDBOfficer> officers = new ArrayList<>();
	private static List<HDBManager> managers = new ArrayList<>();
	private static List<Inquiry> inquiries = new ArrayList<>();
	
	public static List<Project> getProjects() {
		return projects;
	}

	public static void setProjects(List<Project> projects) {
		DataManager.projects = projects;
	}

	public static List<Applicant> getApplicants() {
		return applicants;
	}

	public static void setApplicants(List<Applicant> applicants) {
		DataManager.applicants = applicants;
	}

	public static List<HDBOfficer> getOfficers() {
		return officers;
	}

	public static void setOfficers(List<HDBOfficer> officers) {
		DataManager.officers = officers;
	}

	public static List<HDBManager> getManagers() {
		return managers;
	}

	public static void setManagers(List<HDBManager> managers) {
		DataManager.managers = managers;
	}

	public static List<Inquiry> getInquiries() {
		return inquiries;
	}

	public static void setInquiries(List<Inquiry> inquiries) {
		DataManager.inquiries = inquiries;
	}

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

	private static String stringify(String[] input) {
		String res = String.join(",",input);
		res = "\"" + res + "\"";
		return res;
	}
	public static void saveAll() {
		saveApplicants("data/ApplicantList.csv");
		saveOfficers("data/OfficerList.csv");
		saveManagers("data/ManagerList.csv");
		saveProjects("data/ProjectList.csv");
		saveInquiries("data/InquiryList.csv");
	}
	public static void saveApplicants(String path) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
			writer.println("Name,NRIC,Age,Marital Status,Password,Applied Project,Status");
			for (Applicant applicant : applicants) {
				writer.printf("%s,%s,%s,%s,%s,%s,%s\n",
						applicant.getName(),
						applicant.getNRIC(),
						applicant.getAge(),
						applicant.getMaritalStatus(),
						applicant.getPassword(),
						applicant.getAppliedProject(),
						applicant.getApplicationStatus());
			}
		} catch (IOException e) {
			System.err.println("Error saving applicants to " + path + ": " + e.getMessage());
		}
	}
	public static void saveOfficers(String path) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
			writer.println("Name,NRIC,Age,Marital Status,Password,Applied Project,Status,Pending Projects,Approved Projects, Rejected Projects");
			for (HDBOfficer officer : officers) {
				writer.printf("%s,%s,%s,%s,%s,%s,%s\n",
						officer.getName(),
						officer.getNRIC(),
						officer.getAge(),
						officer.getMaritalStatus(),
						officer.getPassword(),
						officer.getAppliedProject(),
						officer.getApplicationStatus());
						//Stringify(officer.getPendingProjects()));
						//Stringify(officer.getRegisteredProjects()));
						//Stringify(officer.getRejectedProjects()));
			}
		} catch (IOException e) {
			System.err.println("Error saving applicants to " + path + ": " + e.getMessage());
		}
	}
	public static void saveManagers(String path) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
			writer.println("Name,NRIC,Age,Marital Status,Password,Created Projects");
			for (HDBManager manager : managers) {
				writer.printf("%s,%s,%s,%s,%s\n",
						manager.getName(),
						manager.getNRIC(),
						manager.getAge(),
						manager.getMaritalStatus(),
						manager.getPassword());
						//manager.getCreatedProjects());
			}
		} catch (IOException e) {
			System.err.println("Error saving applicants to " + path + ": " + e.getMessage());
		}
	}
    public static void saveProjects(String path) {
    	try(PrintWriter writer = new PrintWriter(new FileWriter(path))) {
    		writer.println("Project Name,Neighborhood,"
    				+ "Type 1,Number of units for Type 1,Selling price for Type 1,"
    				+ "Type 2,Number of units for Type 2,Selling price for Type 2,"
    				+ "Application opening date,Application closing date,"
    				+ "Manager,Officer Slot, Officer");
    		for (Project p : projects) {
    			writer.printf("%s,%s,%s,%d,%d,%s,%d,%d,%s,%s,%s,%d,%s\n", 
    					p.getName(),
    					p.getLocation(),
    					p.getType1(),
    					p.getFlatTypeTotal(p.getType1()),
    					p.getFlatPrice(p.getType1()),
    					p.getType2(),
    					p.getFlatTypeTotal(p.getType2()),
    					p.getFlatPrice(p.getType2()),
    					p.getOpenDate().toString(),
    					p.getCloseDate().toString(),
    					p.getManager(),
    					p.getOfficerSlot(),
    					stringify(p.getOfficers())
    				);
    	}
    } catch (IOException e) {
    	System.err.println("Error saving projects: " + e.getMessage());
    	}
    }
    public static void saveInquiries(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("SenderNRIC,ProjectName,Message,Reply,Resolved,Timestamp");
            for (Inquiry i : inquiries) {
                writer.printf("%s,%s,%s,%s,%b,%s\n",
                        i.getSenderNRIC(),
                        i.getRelatedProject().getName(),
                        i.getMessage(),
                        i.getReply() != null ? i.getReply() : "",
                        i.isResolved(),
                        i.getTimestamp().toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving inquiries: " + e.getMessage());
        }
    }

	
	
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
					applicants.add((Applicant) user);
				} else if (c == HDBOfficer.class) {
					user = c.cast(new HDBOfficer(name, nric, age, maritalStatus, password));
					officers.add((HDBOfficer) user);
				} else if (c == HDBManager.class) {
					user = c.cast(new HDBManager(name, nric, age, maritalStatus, password));
					managers.add((HDBManager) user);
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
				String neighbourhood= data[1];
				String type1 = data[2];
				int units1 = Integer.parseInt(data[3]);
				int price1 = Integer.parseInt(data[4]);
				String type2 = data[5];
				int units2 = Integer.parseInt(data[3]);
				int price2 = Integer.parseInt(data[4]);
				SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yy");
				Date openingDate = formatter.parse(data[8]);
				Date closingDate = formatter.parse(data[9]);
				String manager = data[10];
				int officerSlot = Integer.parseInt(data[11]);
				String[] officers = smartSplit(data[12]);
				boolean visibility;
				if (data[13] != null) {
					visibility = Boolean.parseBoolean(data[13]);
				} else {
					visibility = true;
				}
				
				Map<String, Integer> flatTotal = new HashMap<>();
				flatTotal.put(type1, units1);
				flatTotal.put(type1, units2);
				
				Map<String, Integer> flatAvailable = new HashMap<>(flatTotal);
				
				Map<String, Integer> flatPrices = new HashMap<>();
				flatPrices.put(type1, price1);
				flatPrices.put(type2, price2);
				
				Project project = new Project(projectName, neighbourhood, 
						flatTotal, flatAvailable, flatPrices, 
						openingDate, closingDate,
						manager, officerSlot, officers, visibility);
				projects.add(project);
			}
				
			
		} catch (IOException e) {
			System.out.println("Error while reading file: " + e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return projects;
		
		
	}
	
	public static List<Inquiry> loadInquiries(String path) {
		inquiries = new ArrayList<>();
		File file = new File(path); 
		if (!file.exists()) {
			return inquiries;
		}
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String currentLine;
			br.readLine();
			
			while ((currentLine = br.readLine()) != null) {
				String[] data = smartSplit(currentLine);
				String senderNric = data[0];
				String projectName = data[1];
				String message = data[2];
				String reply = data[3];
				boolean resolved = Boolean.parseBoolean(data[4]);
				LocalDateTime timestamp = LocalDateTime.parse(data[5]);
				
				Project relatedProject = projects.stream()
						.filter(p -> p.getName().equalsIgnoreCase(projectName))
						.findFirst()
						.orElse(null);
				
				if (relatedProject == null) {
					System.err.println("Warning: Inquiry linked to unknown project: " + projectName);
					continue;
				}
				
				Inquiry inquiry = new Inquiry(senderNric, message, relatedProject);
				inquiry.setReply(reply);
				inquiry.setResolved(resolved);
				inquiry.setTimestamp(timestamp);
				
				inquiries.add(inquiry);

			}
				
				
			
		} catch (IOException e) {
			System.out.println("Error while reading file: " + e.getMessage());
		}
		
		return inquiries;
		
		
	}

}
