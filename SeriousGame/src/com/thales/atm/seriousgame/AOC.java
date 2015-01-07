package com.thales.atm.seriousgame;

import java.util.HashMap;

public class AOC extends Player {
	
	private int m_budget;
	private HashMap<Integer,Flight> m_flights;
	
	public AOC(){
		super();
		this.m_budget=0;
		this.m_flights=new HashMap<Integer,Flight>();
	}
	
	public AOC(String name, int i) {
		
		super(name,i);
		this.m_budget=0;
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

}
