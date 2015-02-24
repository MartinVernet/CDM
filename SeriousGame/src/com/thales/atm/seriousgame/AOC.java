package com.thales.atm.seriousgame;

import java.util.ArrayList;
import java.util.HashMap;

public class AOC extends Player {
	
	private int m_budget;
	private HashMap<String,Flight> m_flights;
	private ArrayList<String> newFlights;
	
	public AOC(){
		super();
		m_budget=0;
		m_flights=new HashMap<String,Flight>();
		newFlights= new ArrayList<String>();
	}
	
	public AOC(String name, int i) {
		
		super(name,i);
		m_budget=0;
		m_flights=new HashMap<String,Flight>();
		newFlights= new ArrayList<String>();
	}

	public void play(HashMap<Integer,Integer> flightPriorities){
		// Need to check that flightPriorities.keySet()==this.m_flights.keySet()
		for ( int flightID : flightPriorities.keySet() ){
			m_flights.get(flightID).setPriority(flightPriorities.get(flightID));
		}
	}
	
	public String getType(){
		return "AOC";
	}
	
	public int getBudget(){
		return this.m_budget;
	}
	
	public void setBudget(int budget){
		this.m_budget=budget;
	}
	
	public void clearNewFlights(){
		newFlights.clear();
	}
	
	public void addFlight(Flight flight){
		this.m_flights.put(flight.getFlightID(),flight);
		this.newFlights.add(flight.getFlightID());
	}
	
	public void setName(String name){
		this.m_name=name;
	}
	
	public void setId(int id){
		this.m_id=id;
	}
	
	public boolean isOK() {
		// A compléter
		return true;
	}
	


}
