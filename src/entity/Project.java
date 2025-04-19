package entity;

import java.util.Date;
import java.util.List;
import java.util.Map;


import java.util.ArrayList;

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
	List <HDBOfficer> temp;
	List <HDBOfficer> officers;
	boolean visibility;
	
	//Constructor
	public Project(String projectName, String neighbourhood, Map<String, Integer> flatTotal,
			Map<String, Integer> flatAvailable, Map<String, Integer> flatPrices, Date openingDate, Date closingDate,
			String manager, int officerSlot, List<HDBOfficer> officers, boolean visibility) {
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

	public List<HDBOfficer> getTemp() {
		return temp;
	}

	public void setTemp(List<HDBOfficer> temp) {
		this.temp = temp;
	}

	//Setter for visibility
	public void toggleVisibility(Boolean t){
		this.visibility = t;
	}

	//Getter for visibility
	public boolean getVisibility(){
		return this.visibility;
	}

	//Check whether there is still the flat type available
	public boolean hasFlatType (String type){
		//Check the number of flats for the selected flat type 
		//Will initilisation definitely have the two different flat types?

		if (this.flatTypeAvailable.get(type) <= 0){
			return false;
		}
		return true;
	}

	public boolean bookFlat (String book){
		//Checks if there is enough flats of this type
		if (this.hasFlatType(book)){
			//Reduce the numbers of flats of the selected type by 1
			int temp =  this.flatTypeAvailable.get(book) - 1;
			//Replace the value of the number of flats in the Map
			this.flatTypeAvailable.replace(book, temp);
			//Tells user that the booking was successful
			return true;
		}

		return false;
	}	

	//Getter for name
	public String getName(){
		return this.name;
	}

	//Setter for name
	public void setName (String n){
		this.name = n;
	}

	//Getter for location
	public String getLocation (){
		return this.location;
	}

	//Setter for location
	public void setLocation (String loc){
		this.location = loc;
	}

	public Map<String, Integer> getFlatTypeAvailable(){
		return this.flatTypeAvailable;
	}

	public void setFlatTypeAvailable (Map <String, Integer> t){
		this.flatTypeAvailable = t;
	}

	public void addFlatTypeAvailable (String t, int i){
		this.flatTypeAvailable.put(t, i);
	}

	public Date getOpenDate(){
		return this.openDate;
	}

	public void setOpenDate(Date d){
		this.openDate = d;
	}

	public Date getCloseDate(){
		return this.closeDate;
	}

	public void setCloseDate(Date d){
		this.closeDate = d;
	}

	public int getOfficerSlot (){
		return this.officerSlot;
	}

	public void setOfficerSlot (int i){
		this.officerSlot = i;
	}

	public Map <String, Integer> getFlatPrices (){
		return this.flatPrices;
	}

	public void setFlatPrices (Map <String, Integer> t){
		this.flatPrices = t;
	}

	public String getManager(){
		return this.manager;
	}

	public void setManager(String m){
		this.manager = m;
	}

	public List <HDBOfficer> getOfficers(){
		return this.officers;
	}

	public void addOfficer(HDBOfficer m){
		this.officers.add(m);
	}

	public Map<String, Integer> getFlatTypeTotal() {
		return flatTypeTotal;
	}

	public void setFlatTypeTotal(Map<String, Integer> flatTypeTotal) {
		this.flatTypeTotal = flatTypeTotal;
	}	

	public List <HDBOfficer> getTemporaryOfficers(){
		return this.temp;
	}

	public void addTemporaryOfficer(HDBOfficer n){
		this.temp.add(n);
	}

	public void removeTemporaryOfficer (HDBOfficer n){
		this.temp.remove(n);
	}
	
	
	
	
}
