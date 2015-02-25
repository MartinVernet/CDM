package com.thales.atm.seriousgame.flightmodel;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.NavigableMap;



import com.thales.atm.seriousgame.AirSpace;
import com.thales.atm.seriousgame.Sector;
import com.thales.atm.seriousgame.map;
import com.thales.atm.seriousgame.flightmodel.PrintingMap;
import com.thales.atm.seriousgame.flightmodel.EntryExitTime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;

@WebService
//This class is use to map the xml file
public class FlightPlan {
	  private String flightId; 
	  private String aircraftType;
	  private String airline;
	  private Date exitMap;
	 // private Map<EntryExitTime,String> airspaceProfileES;
	  private TreeMap<Date,Sector> airspaceProfileES;

	  
	  public FlightPlan (){

		  airspaceProfileES=new TreeMap<Date,Sector>();
	  }
	  
	  //Methods

	  		//Get and Set 
	  public TreeMap<Date,Sector> getAirspaceProfile() {

		  return airspaceProfileES;
	  }
	  
	  public void setAirspaceProfile(TreeMap<Date,Sector> AirspaceProfile) {
		  this.airspaceProfileES=AirspaceProfile;
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
	  
	  public Date getExitMap() {
		return exitMap;
	  }

	  public void setExitMap(Date exitmap) {
		this.exitMap = exitmap;
	  }
	  		
	  public Date getEntryMap(){		  
		  Date EntryMap = airspaceProfileES.firstKey();
		  return EntryMap;
	  }

	  public String getAirline(){
		  return airline;
	  }
	  
	  public void setAirline(String airline){
		  this.airline=airline;
	  }
	  
	  public void setAirlineFromId(){	
		  
		  String airlineID = StringUtils.substring(this.flightId, 0, 3);
		  
		  String csvFile = "airlines.txt";
			BufferedReader br = null;
			String a="";
			String cvsSplitBy = ";";
			boolean itemfind = false;
			
			try {
		 
					br = new BufferedReader(new FileReader(csvFile));
					while ((a = br.readLine()) != null && itemfind == false) 
					{
						
		 		        // use semi comma as separator
						String [] obj= a.split(cvsSplitBy);
					
						if(obj[0].equalsIgnoreCase(airlineID))
						{
							airline = obj[1];
							setAirline(airline);
							itemfind = true;
						}
						else
						{
							airline = "Private Airline";
							setAirline(airline);
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		  
	  }	  
	  
	  
	  public Sector getSectorFromDate(Date date) {
			
		Date entryDate = airspaceProfileES.floorKey(date);
		Sector sector =airspaceProfileES.get(entryDate);
		return sector;
			
	  }
	  

	  		//Others
	  //Override compare function to compare EntryExitTime type
	 /* Comparator<EntryExitTime> secondDateComparator = new Comparator<EntryExitTime>() {
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
	  };*/
	  
	  //Print for testing purpose
	  @Override
	  public String toString() {
	    return "[flightId=" + flightId + ", Airline=" + airline + ", aircraftType=" + aircraftType + ", EntryMap=" + getEntryMap() + ", ExitMap=" + exitMap + ", spaceProfile=" +  new PrintingMap<Date, Sector>(airspaceProfileES) +"]";
	  }
	  //new PrintingMap<EntryExitTime, String>(airspaceProfileES)


}

