package com.thales.atm.seriousgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

public class AOC extends Player {
	
	protected int m_budget;
	protected HashMap<String,Flight> m_flights;
	protected HashMap<String,Flight> newFlights;
	protected HashMap<String,Flight> fligthsOnBoard;
	protected HashMap<String,Flight> oldFlights;
	protected HashMap<String,Flight> regulatedFlights;
	
	public AOC(){
		super();
		m_budget=0;
		m_flights=new HashMap<String,Flight>();
		newFlights= new HashMap<String,Flight>();
		fligthsOnBoard= new HashMap<String,Flight>();
		oldFlights= new HashMap<String,Flight>();
		regulatedFlights = new HashMap<String,Flight>();
	}
	
	public AOC(String name, int i) {
		
		super(name,i);
		m_budget=0;
		m_flights=new HashMap<String,Flight>();
		newFlights= new HashMap<String,Flight>();
		fligthsOnBoard= new HashMap<String,Flight>();
		oldFlights= new HashMap<String,Flight>();
		regulatedFlights = new HashMap<String,Flight>();
	}

	public void play() 
	{
		
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
		this.newFlights.put(flight.getFlightID(),flight);
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

	public HashMap<String,Flight> getFligthsOnBoard() {
		return fligthsOnBoard;
	}

	public void setFligthsOnBoard(HashMap<String,Flight> fligthsOnBoard) {
		this.fligthsOnBoard = fligthsOnBoard;
	}

	public HashMap<String,Flight> getOldFlights() {
		return oldFlights;
	}

	public void setOldFlights(HashMap<String,Flight> oldFlights) {
		this.oldFlights = oldFlights;
	}

	public HashMap<String,Flight> getNewFlights() {
		return newFlights;
	}

	public void setNewFlights(HashMap<String,Flight> newFlights) {
		this.newFlights = newFlights;
	}

	public void moveFlights(Date currentDate) 
	{
		ArrayList<String > newflightToRemove = new ArrayList<String>();
		ArrayList<String > currentflightToRemove = new ArrayList<String>();

		for(String flightId: newFlights.keySet()) 
		{
			Flight f=newFlights.get(flightId);
			f.getSectorFromDate(currentDate);
			if(newFlights.get(flightId).getSectorFromDate(currentDate)!=null)
			{
				fligthsOnBoard.put(flightId, newFlights.get(flightId));
				newflightToRemove.add(flightId);
				//newFlights.remove(flightId);
			}
		}
		for (String flToRemove : newflightToRemove){
			newFlights.remove(flToRemove);
		}
		for (String flightId: fligthsOnBoard.keySet())
		{
			fligthsOnBoard.get(flightId).move(currentDate);
			if (fligthsOnBoard.get(flightId).getCurrentSector().m_name=="Exit")
			{
				oldFlights.put(flightId, newFlights.get(flightId));
				currentflightToRemove.add(flightId);
				//fligthsOnBoard.remove(flightId);
			}
		}
		for (String flToRemove : currentflightToRemove){
			fligthsOnBoard.remove(flToRemove);
		}
		
	}
	


}
