package com.thales.atm.seriousgame;

import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

public class Flight {
	
	private String m_flightID;
	private TreeMap<Date,String> m_flightplan;
	private String m_currentPoint;
	private AirBlock m_currentAirblock;
	private int m_priority;
	
	public Flight(String ID, TreeMap<Date,String> flightplan){
		this.m_flightID=ID;
		this.m_flightplan=flightplan;
	}
	
	public void refreshCoordinates(Date date){
		
	}
	
	public void getCoordinates(){
		
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
	    this.m_currentPoint=m_flightplan.get(m_flightplan.floorKey(newDate));
	    refreshAirblocksOccupation();
	}

	public void refreshAirblocksOccupation() {
		if (this.m_currentAirblock!=getAirblockFromPoint(this.m_currentPoint)){
			this.m_currentAirblock.removeFlight(this);
			this.m_currentAirblock=getAirblockFromPoint(this.m_currentPoint);
			this.m_currentAirblock.addFlight(this);
			
		}
	}

	public AirBlock getAirblockFromPoint(String pointID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
