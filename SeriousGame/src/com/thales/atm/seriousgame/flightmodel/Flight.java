package com.thales.atm.seriousgame.flightmodel;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

@WebService
public class Flight {
	  private String flightId; 
	  private String aircraftType;
	  private String actualTakeOffTime;
	  private String actualTimeOfArrival;
	  private String icaoRoute;
	  private List<String> pointProfile;
	  private List<String> airspaceProfile;
	  
	  public Flight (){
		  pointProfile=new ArrayList<String>();
		  airspaceProfile=new ArrayList<String>();		  
	  }
	  
	  //Methods
	  		//Get and Set
	  public List<String> getPointProfile() {
		  return pointProfile;
	  }
	  
	  public List<String> getAirspaceProfile() {
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
	  public String getActualTimeOfArrival() {
	    return actualTimeOfArrival;
	  }
	  public void setActualTimeOfArrival(String actualTimeOfArrival) {
	    this.actualTimeOfArrival = actualTimeOfArrival;
	  }
	  public String getIcaoRoute() {
		    return aircraftType;
	  }
	  public void setIcaoRoute(String icaoRoute) {
		    this.icaoRoute = icaoRoute;
	  }
	  
	  		//Others
	 

	  @Override
	  public String toString() {
	    return "flight [flightId=" + flightId + ", aircraftType=" + aircraftType + ", takeOffTime="
	        + actualTakeOffTime + ", timeOfArrival=" + actualTimeOfArrival + ", icaoRoute=" + icaoRoute +", pointProfile=" + pointProfile +", airspaceProfile=" +airspaceProfile+"]";
	  }
}

