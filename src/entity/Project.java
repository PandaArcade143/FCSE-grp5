package entity;

import java.util.Date;
import java.util.Map;

public class Project {
	String name;
	String location;
	Map<String, Integer> flatTypeTotal;
	Map<String, Integer> flatTypeAvailable;
	Map<String, Integer> flatPrices;
	Date openDate;
	Date closeDate;
	String manager;
	int officerSlot;
	String[] officers;
	boolean visibility;
	

	public Project(String projectName, String neighbourhood, Map<String, Integer> flatTotal,
			Map<String, Integer> flatAvailable, Map<String, Integer> flatPrices, Date openingDate, Date closingDate,
			String manager, int officerSlot, String[] officers, boolean visibility) {
		this.name = projectName;
		this.location = neighbourhood;
		this.flatTypeTotal = flatTotal;
		this.flatTypeAvailable = flatAvailable;
		this.flatPrices = flatPrices;
		this.openDate = openingDate;
		this.closeDate = closingDate;
		this.manager = manager;
		this.officerSlot = officerSlot;
		this.officers = officers;
		this.visibility = visibility;
	}


	public String getName() {
		return name;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public Integer getFlatTypeTotal(String flatType) {
		return flatTypeTotal.get(flatType);
	}


	public void setFlatTypeTotal(Map<String, Integer> flatTypeTotal) {
		this.flatTypeTotal = flatTypeTotal;
	}


	public Integer getFlatTypeAvailable(String flatType) {
		return flatTypeAvailable.get(flatType);
	}


	public void setFlatTypeAvailable(Map<String, Integer> flatTypeAvailable) {
		this.flatTypeAvailable = flatTypeAvailable;
	}


	public int getFlatPrice(String flatType) {
		return flatPrices.get(flatType);
	}


	public void setFlatPrice(Map<String, Integer> flatPrices) {
		this.flatPrices = flatPrices;
	}


	public Date getOpenDate() {
		return openDate;
	}


	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}


	public Date getCloseDate() {
		return closeDate;
	}


	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}


	public String getManager() {
		return manager;
	}


	public void setManager(String manager) {
		this.manager = manager;
	}


	public int getOfficerSlot() {
		return officerSlot;
	}


	public void setOfficerSlot(int officerSlot) {
		this.officerSlot = officerSlot;
	}


	public String[] getOfficers() {
		return officers;
	}
	

	public void setOfficers(String[] officers) {
		this.officers = officers;
	}


	public boolean isVisibility() {
		return visibility;
	}


	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType1() {
		return "2-Room";
	}


	public String getType2() {
		return "3-Room";
	}

}
