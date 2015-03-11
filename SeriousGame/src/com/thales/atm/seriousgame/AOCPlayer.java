package com.thales.atm.seriousgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

public class AOCPlayer extends AOC {
	
	private String AOCType;
	
	public AOCPlayer(){
		super();
		setAOCType("human");
		
	}
	
	public AOCPlayer(String name, int i) {
		
		super(name,i);
		setAOCType("human");
		
	}

	public void play() 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println(this.m_name);
		System.out.println("budget max = ");
		System.out.println(m_budget);
		int budgetRestant=m_budget;
		for ( String flightID :newFlights.keySet())
		{
			
			int p =-1;
			while ((p<0 || p>6) || budgetRestant<0)
			{
				System.out.println("########");
				System.out.println("Set priority for airline "+this.m_name);
				System.out.println("budget restant = "+budgetRestant);
				System.out.println("priorité du vol : "+flightID +"=");
				String priority="";
				
				try {
					priority = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				p=Integer.parseInt(priority);
				if(p<0 || p>6 || budgetRestant<0)
				{
					System.out.println("erreur dans la saisie de la priorité, /n veuillez saisir un nombre entre 0 et 5");
				}
				else
				{
				 
				 budgetRestant=budgetRestant-p;
				 
				}
			}
			newFlights.get(flightID).setPriority(p);
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
			}
		}
		for (String flToRemove : currentflightToRemove){
			fligthsOnBoard.remove(flToRemove);
		}
		
	}

	public String getAOCType() {
		return AOCType;
	}

	public void setAOCType(String aOCType) {
		AOCType = aOCType;
	}
	


}
