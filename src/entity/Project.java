package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Represents a BTO project that contains flat types, pricing, officer assignment,
 * availability status, and application time window.
 */
public class Project {
	String name;
	String location;
	Map<String, Integer> flatTypeTotal;
	Map<String, Integer> flatTypeAvailable;
	Map<String, Integer> flatPrices;
	Date openDate;
	Date closeDate;
	String managerName;
	HDBManager manager;
	
	int officerSlot;
	List <HDBOfficer> temp;
	List <HDBOfficer> officers;
	boolean visibility;

	/**
     * Constructs a Project with specified data.
     *
     * @param projectName the name of the project
     * @param neighbourhood the location
     * @param flatTotal map of flat types and their total count
     * @param flatAvailable map of flat types and their available count
     * @param flatPrices map of flat types and their prices
     * @param openingDate application open date
     * @param closingDate application close date
     * @param manager name or ID of managing officer
     * @param officerSlot number of officer slots
     * @param officers assigned officers
     * @param visibility visibility status
     */	
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
		this.managerName = manager;
		this.officerSlot = officerSlot;
		this.officers = officers;
		this.visibility = visibility;
		this.temp = new ArrayList<>();
	}
	
	public void decrementFlat(String flatType) {
		Integer no = flatTypeAvailable.getOrDefault(flatType, 0);
		if (no > 0) {
			flatTypeAvailable.put(flatType, no - 1);
		} else {
			System.err.print("No flat of type " + flatType + " available to decrement.");
		}
	
	}

	public HDBManager getManager() {
		return manager;
	}


	public void setManager(HDBManager manager) {
		this.manager = manager;
	}

	 /**
     * Checks if a specific flat type is available in this project.
     * @param type the flat type
     * @return true if there are available units, false otherwise
     */
	public boolean hasFlatType (String type){
        return this.flatTypeAvailable.getOrDefault(type, 0) > 0;
	}
	
	/**
     * Books a flat of the specified type.
     * @param book the flat type to book
     * @return true if booking is successful, false otherwise
     */
	public boolean bookFlat (String book){
		if (this.hasFlatType(book)){
			int temp =  this.flatTypeAvailable.get(book) - 1;
			this.flatTypeAvailable.replace(book, temp);
			return true;
		}

		return false;
	}	

	/**
	 * Gets the name of the project.
	 *
	 * @return the project name
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Sets the name of the project.
	 *
	 * @param n the new name to set
	 */
	public void setName (String n){
		this.name = n;
	}

	/**
	 * Gets the location of the project.
	 *
	 * @return the location
	 */ 
	public String getLocation (){
		return this.location;
	}

	/**
	 * Sets the location of the project.
	 *
	 * @param loc the new location
	 */	
	public void setLocation (String loc){
		this.location = loc;
	}

	/**
	 * Gets the map of available flat types and their quantities.
	 *
	 * @return map of flat types to available counts
	 */
	public Map<String, Integer> getFlatTypeAvailable(){
		return this.flatTypeAvailable;
	}

	/**
	 * Sets the flat type availability map.
	 *
	 * @param t map of flat types to available counts
	 */
	public void setFlatTypeAvailable (Map <String, Integer> t){
		this.flatTypeAvailable = t;
	}

	/**
	 * Adds or updates availability for a flat type.
	 *
	 * @param t the flat type
	 * @param i the number of available units
	 */
	public void addFlatTypeAvailable (String t, int i){
		this.flatTypeAvailable.put(t, i);
	}


	/**
	 * Gets the open date for BTO application.
	 *
	 * @return the open date
	 */	
	public Date getOpenDate(){
		return this.openDate;
	}

	/**
	 * Sets the open date for BTO application.
	 *
	 * @param d the new open date
	 */
	public void setOpenDate(Date d){
		this.openDate = d;
	}


	/**
	 * Gets the close date for BTO application.
	 *
	 * @return the close date
	 */	
	public Date getCloseDate(){
		return this.closeDate;
	}

	/**
	 * Sets the close date for BTO application.
	 *
	 * @param d the new close date
	 */
	public void setCloseDate(Date d){
		this.closeDate = d;
	}

	/**
	 * Gets the number of officer slots assigned to the project.
	 *
	 * @return the officer slot count
	 */
	public int getOfficerSlot (){
		return this.officerSlot;
	}

	/**
	 * Sets the number of officer slots.
	 *
	 * @param i the number of slots to assign
	 */
	public void setOfficerSlot (int i){
		this.officerSlot = i;
	}

	/**
	 * Gets the flat price map by flat type.
	 *
	 * @return map of flat types to prices
	 */
	public Map <String, Integer> getFlatPrices (){
		return this.flatPrices;
	}

	/**
	 * Sets the flat price map.
	 *
	 * @param t map of flat types to prices
	 */
	public void setFlatPrices (Map <String, Integer> t){
		this.flatPrices = t;
	}

	/**
	 * Gets the manager identifier or name.
	 *
	 * @return the managerName
	 */
	public String getManagerName(){
		return this.managerName;
	}

	/**
	 * Sets the manager's identifier or name.
	 *
	 * @param m the managerName
	 */
	public void setManagerName(String m){
		this.managerName = m;
	}

	/**
	 * Gets the map of total flats by type.
	 *
	 * @return map of flat types to total counts
	 */
	public List <HDBOfficer> getOfficers(){
		return this.officers;
	}
	

	/**
	 * Sets the total number of flats for each type.
	 *
	 * @param flatTypeTotal map of flat types to total counts
 	*/
	public List<HDBOfficer> getTemp() {
		return temp;
	}
	
	/**
	 * sets the temporary HDBOfficers that have applied
	 *
	 * @param list of HDBOfficers
 	*/

	public void setTemp(List<HDBOfficer> temp) {
		this.temp = temp;
	}

	/**
	 * toggles the visibility of project
	 *
	 * @param the boolean value of the visibility to set
 	*/
	public void toggleVisibility(Boolean t){
		this.visibility = t;
	}

	/**
	 * Gets the visibility of project.
	 *
	 * @return returns the visibility of the project
 	*/
	public boolean getVisibility(){
		return this.visibility;
	}

	/**
     * Adds an officer to the list of assigned officers.
     * @param m the officer to add
     */
	public void addOfficer(HDBOfficer m){
		this.officers.add(m);
	}

	public Map<String, Integer> getFlatTypeTotal() {
		return flatTypeTotal;
	}

	public void setFlatTypeTotal(Map<String, Integer> flatTypeTotal) {
		this.flatTypeTotal = flatTypeTotal;
	}	

	/**
     * Returns a list of temporary officers.
     * @return list of temporary HDB officers
     */
	public List <HDBOfficer> getTemporaryOfficers(){
		return this.temp;
	}

	/**
     * Adds a temporary officer to the list.
     * @param n the officer to add temporarily
     */
	public void addTemporaryOfficer(HDBOfficer n){
		this.temp.add(n);
	}

	 /**
     * Removes a temporary officer from the list.
     * @param n the officer to remove
     */
	public void removeTemporaryOfficer (HDBOfficer n){
		this.temp.remove(n);
	}
	
	
	
	
}
