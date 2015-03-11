package com.thales.atm.seriousgame.flightmodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
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
	  
	  public void refreshFlightPlan(Sector regulateSector, ArrayList<Sector> newSectors, int penality){
		 		    
		  if(this.airspaceProfileES.keySet().contains(regulateSector))
		  {
			 //find current sector to regulate
			 for (Entry <Date, Sector> entry : this.airspaceProfileES .entrySet())
			 {
				 if(regulateSector.equals(entry.getValue()))
				 {
					 Date firstDate = entry.getKey();
					 Sector firstSector = regulateSector;
					 NavigableMap<Date,Sector> planA = this.airspaceProfileES.subMap(this.entryMap, true, firstDate, false);

					 this.airspaceProfileES.replace(firstDate, firstSector);
					
					 NavigableMap<Date,Sector> planB = this.airspaceProfileES.subMap(firstDate, true, this.exitMap, true);
					 
					 NavigableMap<Date,Sector> planC = new TreeMap<Date,Sector>();
					 					 
					//new sectors
					 int nb = 0;
					 for (Sector rSector : newSectors)
					 {
						 nb++;
						 Calendar cal = Calendar.getInstance(); 
						 cal.setTime(firstDate);
						 cal.add(Calendar.MINUTE, nb*penality);
						 Date newDate=cal.getTime();
						 
						 planC.put(newDate, rSector);
 					 }
					 
					 
					//regulate planB 
					 for(Entry <Date, Sector> rEntry : planB.entrySet())
					 {
						 Date rDate = rEntry.getKey();
						 Sector rSector = rEntry.getValue();
						 planB.remove(rEntry.getKey(), rEntry.getValue());
						 
						 Calendar cal = Calendar.getInstance(); 
						 cal.setTime(rDate);
						 cal.add(Calendar.MINUTE, newSectors.size()*penality);
						 Date newDate=cal.getTime();
						 
						 planB.put(newDate, rSector);
					 }	
					 
					 //merge plans
					 NavigableMap<Date, Sector> regulateFlightPlan= new TreeMap<Date, Sector>();
					 regulateFlightPlan.putAll(planA);
					 regulateFlightPlan.putAll(planB);
					 regulateFlightPlan.putAll(planC);
					 
					 this.setAirspaceProfile(regulateFlightPlan);
					 break;
				 }
			 }
		 }
	  }
	  
	  public Sector getSectorFromDate(Date date) {
			
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
	  //new PrintingMap<EntryExitTime, String>(airspaceProfileES)


}

