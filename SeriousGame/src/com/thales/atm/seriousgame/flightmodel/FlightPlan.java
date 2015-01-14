package com.thales.atm.seriousgame.flightmodel;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.thales.atm.seriousgame.flightmodel.PrintingMap;
import com.thales.atm.seriousgame.flightmodel.EntryExitTime;

import javax.jws.WebService;

@WebService
public class FlightPlan {
	  private String flightId; 
	  private String aircraftType;
	  private String actualTakeOffTime;
	  private Map<Date,String> pointProfile;
	  private Map<EntryExitTime,String> airspaceProfile;
	  //position
	  //priority
	  
	  public FlightPlan (){
		  pointProfile=new TreeMap<Date,String>();
		  airspaceProfile=new TreeMap<EntryExitTime,String>(secondDateComparator);	  
	  }
	  
	  //Methods
	  		//Get and Set
	  public Map<Date,String> getPointProfile() {
		  return pointProfile;
	  }
	  
	  public Map<EntryExitTime,String> getAirspaceProfile() {
		  return airspaceProfile;
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
	  
	  		//Others
	  
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
	  
	  //Print for testing
	  @Override
	  public String toString() {
	    return "flight [flightId=" + flightId + ", aircraftType=" + aircraftType + ", takeOffTime="
	        + actualTakeOffTime + ", spaceProfile=" +  new PrintingMap<EntryExitTime, String>(airspaceProfile) +"]";
	  }
}

