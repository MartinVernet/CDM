package com.thales.atm.seriousgame;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.jws.WebService;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

@WebService

public class Sector {
	public String m_name;
	//ArrayList<AirBlock> m_airBlocks;
	ArrayList<String> m_airBlocksId;
	private int capacity;
	private Set<String> neighbors; //Attention not multiThread Safe
	private String m_fatherId;
	private HashMap<String,Flight> occupation;
	private AirSpace m_father;
	private boolean needRegulation;
	

	/*public Sector(String name, ArrayList<AirBlock> airb)
	{
		this.m_name=name;
		this.m_airBlocks=airb;
	}*/
	public Sector(String name, ArrayList<String> airb)
	{
		this.m_name=name;
		this.m_airBlocksId=airb;
		this.neighbors=new HashSet<String>();
		this.capacity=15;
		
	}
	public Sector()
	{
		this.m_name="test";
		this.m_airBlocksId=null;
	}

	public String getFatherId() {
		return m_fatherId;
	}
	public void setFatherId(String m_fatherId) {
		this.m_fatherId = m_fatherId;
	}
	public AirSpace getFather() {
		return m_father;
	}
	public void setFather(AirSpace m_father) {
		this.m_father = m_father;
	}

	/**
	 * @return the neighbors
	 */
	public Set<String> getNeighbors() {
		return neighbors;
	}
	/**
	 * @param neighbors the neighbors to set
	 */
	public void setNeighbors(Set<String> neighbors) {
		this.neighbors = neighbors;
	}
	public HashMap<String,Flight> getOccupation() {
		return occupation;
	}
	public void setOccupation(HashMap<String,Flight> occupation) {
		this.occupation = occupation;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void removeFlight(Flight flight)
	{
		occupation.remove(flight.getFlightID());
		if(capacity==occupation.size())
		{
			needRegulation= false;
			m_father.removeFullSector(this.m_name);
		}
	}
	public void addFlight(Flight flight)
	{
		occupation.put(flight.getFlightID(), flight);
		if(capacity==occupation.size()+1)
		{
			needRegulation= true;
			m_father.addFullSector(this.m_name);
		}

	}

	@Override
	  public String toString() {
	    return "Sector [Name=" + m_name + "]";
	  }		

}
