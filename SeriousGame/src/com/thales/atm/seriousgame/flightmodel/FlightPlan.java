package com.thales.atm.seriousgame.flightmodel;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


import com.thales.atm.seriousgame.AirSpace;
import com.thales.atm.seriousgame.Sector;
import com.thales.atm.seriousgame.map;
import com.thales.atm.seriousgame.flightmodel.PrintingMap;
import com.thales.atm.seriousgame.flightmodel.EntryExitTime;

import javax.jws.WebService;

@WebService
//This class is use to map the xml file
public class FlightPlan {
	  private String flightId; 
	  private String aircraftType;
	  private String actualTakeOffTime;
	  private Map<Date,String> pointProfile;
	 // private Map<EntryExitTime,String> airspaceProfileES;
	  private TreeMap<Date,Sector> airspaceProfileES;
	  
	  //position
	  //priority
	  
	  public FlightPlan (){
		  pointProfile=new TreeMap<Date,String>();
		  airspaceProfileES=new TreeMap<Date,Sector>();
	  }
	  
	  //Methods
	  		//Get and Set
	  public Map<Date,String> getPointProfile() {
		  return pointProfile;
	  }
	  
	  public Map<Date,Sector> getAirspaceProfile() {
		  return airspaceProfileES;
	  }
	  
	  public String getFlightId() {
	    return flightId;
	  }
	  public void setFlightId(String flightId) {
	    this.flightId = flightId;
	  }
	  public String getAircraftType() {
	    return aircraftType;
	  }
	  public void setAircraftType(String aircraftType) {
	    this.aircraftType = aircraftType;
	  }
	  public String getActualTakeOffTime() {
	    return actualTakeOffTime;
	  }
	  public void setActualTakeOffTime(String actualTakeOffTime) {
	    this.actualTakeOffTime = actualTakeOffTime;
	  }
	  public String getIcaoRoute() {
		    return aircraftType;
	  }
	  		
	  		//Flight Plan modification
	  public void addSector(Date time,Sector sectorID){
		  this.airspaceProfileES.put(time, sectorID);
	  }
	  
	  public void replaceSector(Date time,Sector oldSectorID,Sector newSectorID){
		  this.airspaceProfileES.replace(time, oldSectorID, newSectorID);
	  }
	  
	  public void deletSector(Date time,Sector sectorID){ 
		  this.airspaceProfileES.remove(time, sectorID);
	  }
	  
	  public void rerouteFlight(){
		  //Tend à revenir sur le plan de vol de base
		  //delet
		  //add
		  //Synchroniser les temps ?
		  // ... ??
	  }
	  
	  public void regulateFlight(int speed){
		  
	  }
	  		//Others
	  //Override compare function to compare EntryExitTime type
	  Comparator<EntryExitTime> secondDateComparator = new Comparator<EntryExitTime>() {
	      @Override public int compare(EntryExitTime t1, EntryExitTime t2) {
	    	  // Same entry time
	    	  if((t1.getEntryTime().compareTo(t2.getEntryTime())) == 0 && (t1.getExitTime().compareTo(t2.getExitTime())) < 0) {
	    		  return -1; 
	    	  } else {
	    		  if((t1.getEntryTime().compareTo(t2.getEntryTime())) == 0 && (t1.getExitTime().compareTo(t2.getExitTime())) > 0) {
		    		  return 1;
		    	  } 
	    	  }
	          // Different entry time
	    	  if((t1.getEntryTime().compareTo(t2.getEntryTime())) > 0) {
	    		  return 1;
	    	  } else {
	    		  return -1;
	    	  }          	    	  
	      }           
	  };
	  
	  //Print for testing purpose
	  @Override
	  public String toString() {
	    return "[flightId=" + flightId + ", aircraftType=" + aircraftType + ", takeOffTime="
	        + actualTakeOffTime + ", spaceProfile=" +  new PrintingMap<Date, Sector>(airspaceProfileES) +"]";
	  }
	  //new PrintingMap<EntryExitTime, String>(airspaceProfileES)

	public Sector getSectorFromDate(Date date) {
		
		Date entryDate = airspaceProfileES.floorKey(date);
		Sector sector =airspaceProfileES.get(entryDate);
		return sector;
		
	}
}

