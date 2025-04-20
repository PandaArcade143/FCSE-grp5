package helpers;

import java.io.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.Inquiry;
import entity.Project;
import entity.User;

/**
 * Manages application-wide data including reading and writing of users,
 * projects, and inquiries from/to CSV files. Also handles runtime data setup
 * and cleanup on application exit.
 */
public class DataManager {
	private static List<Project> projects = new ArrayList<>();
	private static List<Applicant> applicants = new ArrayList<>();
	private static List<HDBOfficer> officers = new ArrayList<>();
	private static List<HDBManager> managers = new ArrayList<>();
	private static List<Inquiry> inquiries = new ArrayList<>();
	private static String type1 = "2-Room";
	private static String type2 = "3-Room";
	
    /**
     * Loads users, inquiries, and projects into memory on class initialization
     * and sets a shutdown hook to save data on exit.
     */	
	static {
		DataManager.loadUsers("data/ApplicantList.csv", Applicant.class);
		DataManager.loadUsers("data/ManagerList.csv", HDBManager.class);
		DataManager.loadUsers("data/OfficerList.csv", HDBOfficer.class);
		projects = DataManager.loadProjects("data/ProjectList.csv");
		inquiries = DataManager.loadInquiries("data/InquiryList.csv");
		
		// Creates a shutdown hook that will save data everytime the program closes
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			DataManager.saveAll();
		}));
	}
	
	
    /**
     * @return the current list of projects in memory
     */	
	public static List<Project> getProjects() {
		return projects;
	}

	 /**
     * @param projects list to replace the current in-memory project list
     */
	public static void setProjects(List<Project> projects) {
		DataManager.projects = projects;
	}

    /**
     * @return the list of all applicants
     */
	public static List<Applicant> getApplicants() {
		return applicants;
	}

	 /**
     * @param applicants the list of applicants to set
     */
	public static void setApplicants(List<Applicant> applicants) {
		DataManager.applicants = applicants;
	}
	
    /**
     * @return the list of HDB officers
     */
	public static List<HDBOfficer> getOfficers() {
		return officers;
	}

	 /**
     * @param officers list of HDB officers to set
     */
	public static void setOfficers(List<HDBOfficer> officers) {
		DataManager.officers = officers;
	}

	/**
     * @return the list of HDB managers
     */
	public static List<HDBManager> getManagers() {
		return managers;
	}

    /**
     * @param managers list of managers to set
     */
	public static void setManagers(List<HDBManager> managers) {
		DataManager.managers = managers;
	}

	/**
     * @return list of all inquiries
     */
	public static List<Inquiry> getInquiries() {
		return inquiries;
	}


    /**
     * @param inquiries list of inquiries to set
     */
	public static void setInquiries(List<Inquiry> inquiries) {
		DataManager.inquiries = inquiries;
	}
	
	   /**
     * Splits a CSV line while preserving quoted substrings as one field
     *
     * @param line the CSV line to split
     * @return a String array of parsed fields
     */
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
	

    /**
     * Joins a list of strings into a quoted CSV string
     *
     * @param list list of strings to join
     * @return the quoted, comma-separated string
     */
	private static String stringify(List<String> list) {
		String res = String.join(",",list);
		res = "\"" + res + "\"";
		return res;
	}
	
	  /**
     * Safely retrieves a CSV field by index, returns empty string if missing
     *
     * @param parts the array of fields
     * @param index the index to access
     * @return the field value or "" if index is out of bounds
     */
	private static String getCSVField(String[] parts, int index) {
	    return (index < parts.length) ? parts[index] : "";
	}

	 /**
     * Saves all entities to their respective CSV files
     */
	public static void saveAll() {

		System.out.println("Saving data before exiting....");
		saveApplicants("data/ApplicantList.csv");
		saveOfficers("data/OfficerList.csv");
		saveManagers("data/ManagerList.csv");
		saveProjects("data/ProjectList.csv");
		saveInquiries("data/InquiryList.csv");
	}

	 /**
     * Saves the list of applicants to a CSV file.
     *
     * @param filename the path to the file to write
     */
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
						applicant.getAppliedProject() == null ? "" : applicant.getAppliedProject().getName(),
						applicant.getApplicationStatus() == null ? "" : applicant.getApplicationStatus());
			}
		} catch (IOException e) {
			System.err.println("Error saving applicants to " + path + ": " + e.getMessage());
		}
	}

	/**
     * Saves the list of HDB officers to a CSV file.
     *
     * @param filename the path to the file to write
     */
	public static void saveOfficers(String path) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
			writer.println("Name,NRIC,Age,Marital Status,Password,Applied Project,Status");
			for (HDBOfficer officer : officers) {
				writer.printf("%s,%s,%s,%s,%s,%s,%s\n",
						officer.getName(),
						officer.getNRIC(),
						officer.getAge(),
						officer.getMaritalStatus(),
						officer.getPassword(),
						officer.getAppliedProject() == null ? "" : officer.getAppliedProject(),
						officer.getApplicationStatus() == null ? "" : officer.getApplicationStatus());
			}
		} catch (IOException e) {
			System.err.println("Error saving applicants to " + path + ": " + e.getMessage());
		}
	}

	/**
     * Saves the list of HDB managers to a CSV file.
     *
     * @param filename the path to the file to write
     */
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
			}
		} catch (IOException e) {
			System.err.println("Error saving applicants to " + path + ": " + e.getMessage());
		}
	}

	 /**
     * Saves the list of projects to a CSV file.
     *
     * @param filename the path to the file to write
     */
    public static void saveProjects(String path) {
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yy");

    	try(PrintWriter writer = new PrintWriter(new FileWriter(path))) {
    		writer.println("Project Name,Neighborhood,"
    				+ "Type 1,Number of units for Type 1,Selling price for Type 1,"
    				+ "Type 2,Number of units for Type 2,Selling price for Type 2,"
    				+ "Application opening date,Application closing date,"
    				+ "Manager,Officer Slot,Officer,Temp,Visiblity");
    		for (Project p : projects) {
    			List<HDBOfficer> projectOfficers = p.getOfficers();
    			List<String> projectOfficersName = projectOfficers.stream()
    					.map(HDBOfficer::getName)
    					.collect(Collectors.toList());
    			List<HDBOfficer> tempOfficers = p.getTemporaryOfficers();
    			List<String> tempOfficersName = tempOfficers.stream()
    					.map(HDBOfficer::getName)
    					.collect(Collectors.toList());
    			writer.printf("%s,%s,%s,%d,%d,%s,%d,%d,%s,%s,%s,%d,%s,%s,%s\n", 
    					p.getName(),
    					p.getLocation(),
    					type1,
    					p.getFlatTypeTotal().get(type1),
    					p.getFlatPrices().get(type1),
    					type2,
    					p.getFlatTypeTotal().get(type2),
    					p.getFlatPrices().get(type2),
    					formatter.format(p.getOpenDate()),
    					formatter.format(p.getCloseDate()),
    					p.getManagerName(),
    					p.getOfficerSlot(),
    					stringify(projectOfficersName),
    					stringify(tempOfficersName),
    					p.getVisibility()
    				);
    	}
    } catch (IOException e) {
    	System.err.println("Error saving projects: " + e.getMessage());
    	}
    }

    /**
     * Saves the list of inquiries to a CSV file.
     *
     * @param filename the path to the file to write
     */
    public static void saveInquiries(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("InquiryID,SenderNRIC,Subject,Message,Status,CreatedAt,ResolvedAt,RelatedProject,Reply");
            for (Inquiry i : inquiries) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s\n",
                		i.getInquiryId(),
                		i.getSenderNRIC(),
                		i.getSubject(),
                		i.getMessage(),
                		i.getStatus(),
                		i.getCreatedAt(),
                		i.getResolvedAt(),
                		i.getRelatedProject().getName(),
                        i.getReply() != null ? i.getReply() : "");
                        
            }
        } catch (IOException e) {
            System.err.println("Error saving inquiries: " + e.getMessage());
        }
    }

	
    /**
     * Loads a list of users from a CSV file.
     *
     * @param filename the path to the CSV file to read
     * @param Class type of user
     */	
	public static <T> void loadUsers(String path, Class<T> c) {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String currentLine;
			br.readLine();
			
			while ((currentLine = br.readLine()) != null) {
				String[] data = smartSplit(currentLine);
				String name = getCSVField(data,0);
				String nric = getCSVField(data, 1);
				int age = Integer.parseInt(getCSVField(data,2));
				String maritalStatus = getCSVField(data,3);
				String password = getCSVField(data,4);
				String projectName = getCSVField(data,5);
				String status = getCSVField(data,6);
				
				if (c == Applicant.class) {
					Applicant applicant = new Applicant(name, nric, age, maritalStatus, password);
					applicant.setAppliedProjectString(projectName);
					applicant.setApplicationStatus(status);
					applicants.add(applicant);
				} else if (c == HDBOfficer.class) {
					HDBOfficer hdbOfficer = new HDBOfficer(name, nric, age, maritalStatus, password);
					hdbOfficer.setAppliedProjectString(projectName);
					hdbOfficer.setApplicationStatus(status);
					officers.add(hdbOfficer);
				} else if (c == HDBManager.class) {
					HDBManager hdbManager = new HDBManager(name, nric, age, maritalStatus, password);
					managers.add(hdbManager);
				}
			}
			
		} catch (IOException e) {
			System.out.println("Error while reading file: " + e.getMessage());
		}
		
	}
	
	/**
     * Loads a list of Projects from a CSV file.
     *
     * @param filename the path to the CSV file to read
     * @return list of Project objects
     */
	public static List<Project> loadProjects(String path) {

		List<Project> projects = new ArrayList<Project>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String currentLine;
			br.readLine();
			while ((currentLine = br.readLine()) != null) {
				String[] data = smartSplit(currentLine);
				String projectName = getCSVField(data,0);
				String neighbourhood= getCSVField(data,1);
				type1 = getCSVField(data,2);
				int units1 = Integer.parseInt(getCSVField(data,3));
				int price1 = Integer.parseInt(getCSVField(data,4));
				type2 = getCSVField(data,5);
				int units2 = Integer.parseInt(getCSVField(data,6));
				int price2 = Integer.parseInt(getCSVField(data,7));
				SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yy");
				Date openingDate = formatter.parse(getCSVField(data,8));
				Date closingDate = formatter.parse(getCSVField(data,9));
				String manager = getCSVField(data,10);
				int officerSlot = Integer.parseInt(getCSVField(data,11));
				List<HDBOfficer> officersList = new ArrayList<HDBOfficer>();
				List<HDBOfficer> tempOfficersList = new ArrayList<HDBOfficer>();
				List<String> officersListName = Arrays.asList(smartSplit(getCSVField(data,12)));
				List<String> tempOfficersListName = Arrays.asList(smartSplit(getCSVField(data,13)));
				officersList = officers.stream()
						.filter(f -> officersListName.contains(f.getName()))
						.collect(Collectors.toList());
				tempOfficersList = officers.stream()
						.filter(f -> tempOfficersListName.contains(f.getName()))
						.collect(Collectors.toList());

				boolean visibility;
				// By default, if the visibility field is empty, it is visible
				if ("".equals(getCSVField(data,14))) {
					visibility = true;
				} else {
					visibility = Boolean.parseBoolean(getCSVField(data,14));
				}
				
				Map<String, Integer> flatTotal = new HashMap<>();
				flatTotal.put(type1, units1);
				flatTotal.put(type2, units2);
				
				Map<String, Integer> flatAvailable = new HashMap<>(flatTotal);
				
				Map<String, Integer> flatPrices = new HashMap<>();
				flatPrices.put(type1, price1);
				flatPrices.put(type2, price2);
				Project project = new Project(projectName, neighbourhood, 
						flatTotal, flatAvailable, flatPrices, 
						openingDate, closingDate,
						manager, officerSlot, officersList, visibility);
				project.setTemp(tempOfficersList);
				projects.add(project);
			}
				
			
		} catch (IOException e) {
			System.out.println("Error while reading file: " + e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Connects the projects to the users 
		for (HDBManager manager : managers) {
			List<Project> managerProject = new ArrayList<Project>();
			managerProject = projects.stream() 
					.filter(project -> project.getManagerName().contains(manager.getName()))
					.collect(Collectors.toList());
			for(Project p : managerProject) {
				p.setManager(manager);
			}
			manager.setCreatedProjects(managerProject);
		}
		
		for (HDBOfficer officer : officers) {
			Project p;
			p = projects.stream()
					.filter(project -> project.getOfficers().contains(officer))
					.findFirst()
					.orElse(null);
			if (p != null) {
			officer.addRegisteredProjects(p);
			}
		}
		
		for (Applicant applicant : applicants) {
			Project p;
			p = projects.stream()
					.filter(project -> project.getName().equals(applicant.getAppliedProjectString()))
					.findFirst()
					.orElse(null);
			if (p != null) {
			applicant.setAppliedProject(p);
			}
		}

		return projects;
		
	
	}
	
    /**
     * Loads a list of inquiries from a CSV file.
     *
     * @param filename the path to the CSV file to read
     * @return list of Inquiry objects
     */
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
				String inquiryID = getCSVField(data,0);
				String senderNRIC = getCSVField(data,1);
				String subject = getCSVField(data,2);
				String message = getCSVField(data,3);
				String status = getCSVField(data,4);
				String createdAtStr = getCSVField(data,5);
				LocalDateTime createdAt = LocalDateTime.parse(createdAtStr,DateTimeFormatter.ISO_LOCAL_DATE_TIME );
				String resolvedAtStr = getCSVField(data,6);
				LocalDateTime resolvedAt;
				if ("null".equals(resolvedAtStr)) {
					resolvedAt = null;
				} else {
					resolvedAt = LocalDateTime.parse(resolvedAtStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
				}
				String projectName = getCSVField(data,7);
				String reply = getCSVField(data,8);
				Project relatedProject = projects.stream()
						.filter(p -> p.getName().equals(projectName))
						.findFirst()
						.orElse(null);
				if (relatedProject == null) {
					System.err.println("Warning: Inquiry linked to unknown project: " + projectName);
					continue;
				}
				Inquiry inquiry = new Inquiry(inquiryID, senderNRIC, subject, message, status, createdAt, resolvedAt, relatedProject);
				inquiry.setReply(reply);
				inquiries.add(inquiry);

			}
				
				
			
		} catch (IOException e) {
			System.out.println("Error while reading file: " + e.getMessage());
		}
		
		return inquiries;
		
		
	}

}
