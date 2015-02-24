package com.thales.atm.seriousgame;

import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

import com.thales.atm.seriousgame.flightmodel.FlightPlan;

public class Flight {
	
	private String m_flightID;
	private FlightPlan m_flightplan;
	private Sector currentSector;
	private int m_priority;
	private int economicValue;
	
	public Flight(String ID, FlightPlan flightplan){
		this.m_flightID=ID;
		this.m_flightplan=flightplan;
	}
	
	
	public String getFlightID(){
		return this.m_flightID;
	}
	
	public int getPriority(){
		return this.m_priority;
	}
	
	public void setPriority(int priority){
		this.m_priority=priority;
	}
	
	public void move(Date currentDate, int nbMinutes){
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(currentDate);
	    cal.add(Calendar.MINUTE, nbMinutes); 
	    Date newDate=cal.getTime();
	    refreshSectorsOccupation(newDate);
	}

	public void refreshSectorsOccupation(Date newDate) {
		Sector newSector =	getSectorFromDate(newDate);
		if(currentSector != newSector)
		{
			currentSector.removeFlight(this);
			currentSector=newSector;
			currentSector.addFlight(this);
		}
	}

	public Sector getSectorFromDate(Date date) {
		// recupere le secteur dans lequel est l'avion dans le plan de vol
		return m_flightplan.getSectorFromDate(date);
	}
	
	public int getEconomicValue(){
		return economicValue;
	}


	public Sector getCurrentSector() {
		
		return currentSector;
	}
	
}
