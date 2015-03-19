package com.thales.atm.seriousgame.flightmodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.NavigableMap;

import com.thales.atm.seriousgame.Sector;
import com.thales.atm.seriousgame.flightmodel.PrintingMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

/**
   *This class is use to map the xml file in order to define all the flight plan
   *
**/
public class FlightPlan {
	  private String flightId; 
	  private String aircraftType;
	  private String airline;
	  private Date exitMap;
	  private Date entryMap;
	  private NavigableMap<Date,Sector> airspaceProfileES;

	  public FlightPlan (){
		  airspaceProfileES=new TreeMap<Date,Sector>();
	  }
	  
	  //Methods
	  		//Get and Set 
	  public NavigableMap<Date,Sector> getAirspaceProfile() {
		  return airspaceProfileES;
	  }
	  
	  public void setAirspaceProfile(NavigableMap<Date,Sector> AirspaceProfile) {
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
		  return entryMap;
	  }
	  
	  public void setEntryMap(Date entryMap){
		  this.entryMap = entryMap;
	  }
	  
	  public String getAirline(){
		  return airline;
	  }
	  
	  public void setAirline(String airline){
		  this.airline=airline;
	  }
	  
	  public void setAirlineFromId(){	
		  //This function is use to set the airline name		  
		  String airlineID = StringUtils.substring(this.flightId, 0, 3);
		  String csvFile = "airlines.txt";
		  BufferedReader br = null;
		  String a="";
		  String cvsSplitBy = ";"; 
		  boolean itemfind= false;			
		  try { 
			  br = new BufferedReader(new FileReader(csvFile));
			  while ((a = br.readLine()) != null && itemfind ==false) 
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
	  
	  public void refreshFlightPlanV2(Sector regulateSector, ArrayList<Sector> newSectors, int penalty)
	  {
		  //This function is use to change the flight plan after a regulation	  
		  System.out.println("flight plan avant "+ airspaceProfileES);
		  Date dA=new Date();
		  Date dB= new Date();
		  for (Entry <Date, Sector> entry : this.airspaceProfileES .entrySet())
			 {
				 if(regulateSector.equals(entry.getValue()))
				 {
					 dA = entry.getKey();	 
				 }
				 else if (newSectors.get(newSectors.size()-1).equals(entry.getValue())){
					 dB = entry.getKey();
				 }
		  }	  
		  //Flight plan behind the sector to regulate
		  NavigableMap<Date,Sector> planA = this.airspaceProfileES.subMap(this.entryMap, true, dA, false);
		  //Flight plan ahead of the sector to regulate
		  NavigableMap<Date,Sector> planB = new TreeMap<Date,Sector>(this.airspaceProfileES.subMap(dB, false, this.exitMap, true));
		  //New sectors to add to the flight plan
		  NavigableMap<Date,Sector> planC = new TreeMap<Date,Sector>();
		  int n=newSectors.size()-1;
		  int totalDelay=n*penalty;
		  Calendar cal = Calendar.getInstance(); 
		  cal.setTime(dB);
		  cal.add(Calendar.MINUTE,totalDelay);
		  Date dBnew=cal.getTime();
		  int deltaT=(int)((dBnew.getTime()/60000) - (dA.getTime()/60000));
		  int step=0;
		  if (newSectors.size()>1){
			  step =(int)deltaT/n;
		  }
		  else{
			  totalDelay = penalty;
			  step=0;
		  }
		  int i=0;
		  //Set planC with new sectors
		  for (Sector sector:newSectors)
		  {
			  if(i<n)
			  {
				  Calendar local = Calendar.getInstance(); 
				  local.setTime(dA);
				  local.add(Calendar.MINUTE,i*step);
				  i+=1;
				  Date d=local.getTime();
				  planC.put(d, sector);
			  }
			  else
			  {
				  planC.put(dBnew, sector);
			  }
		  }
		  //Set planB with time penalty
		  NavigableMap<Date,Sector> copyPlanB = new TreeMap<Date,Sector>(planB);
			 for(Entry <Date, Sector> rEntry : copyPlanB.entrySet())
			 {
				 Date rDate = rEntry.getKey();
				 Sector rSector = rEntry.getValue();
				 planB.remove(rEntry.getKey(), rEntry.getValue());
				 
				 Calendar local = Calendar.getInstance(); 
				 local.setTime(rDate);
				 local.add(Calendar.MINUTE, totalDelay);
				 Date newDate=local.getTime();
				 
				 planB.put(newDate, rSector);
			 }	
			 Calendar local = Calendar.getInstance(); 
			 local.setTime(exitMap);
			 local.add(Calendar.MINUTE, totalDelay);
			 exitMap=local.getTime();
			 //merge plans
			 NavigableMap<Date, Sector> regulateFlightPlan= new TreeMap<Date, Sector>();
			 regulateFlightPlan.putAll(planA);
			 regulateFlightPlan.putAll(planC);
			 regulateFlightPlan.putAll(planB);
			 
			 this.setAirspaceProfile(regulateFlightPlan);
			 System.out.println("flight plan apres "+ airspaceProfileES);			 
	  }
	  
	  public Sector getSectorFromDate(Date date) {
		//This function is to find in which sector is a flight at a specific date			
		Date entryDate = airspaceProfileES.floorKey(date);
		if (entryDate != null)
		{
		Sector sector =airspaceProfileES.get(entryDate);
		return sector;
		}
		else return null;		
	  }
	  
	  //Print for testing purpose
	  @Override
	  public String toString() {
	    return "[flightId=" + flightId + ", Airline=" + airline + ", aircraftType=" + aircraftType + ", EntryMap=" + entryMap + ", ExitMap=" + exitMap + ", spaceProfile=" +  new PrintingMap<Date, Sector>(airspaceProfileES) +"]";
	  }
}

