package com.thales.atm.seriousgame;

import java.util.Date;

public class Flight {
	
	private int m_flightID;
	private FlightPlan m_flightplan;
	private Coordinates m_coordinates;
	private int m_priority;
	
	public Flight(int ID, FlightPlan flightplan){
		this.m_flightID=ID;
		this.m_flightplan=flightplan;
	}
	
	public void refreshCoordinates(Date date){
		
	}
	
	public void getCoordinates(){
		
	}
	
	public int getFlightID(){
		return this.m_flightID;
	}
	
	public int getPriority(){
		return this.m_priority;
	}
	
	public void setPriority(int priority){
		this.m_priority=priority;
	}
	
}
