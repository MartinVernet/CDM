
package com.thales.atm.seriousgame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import javax.jws.WebService;

@WebService
public class AirBlock {

	private String m_name;
	private ArrayList<Point> m_Coords;
	private double m_altitudeMin=0;
	private double m_altitudeMax=0;
	private HashMap<String,Flight> m_occupation;
	private int m_capacity;
	private String m_fatherId;
	private Sector m_father;
	private ArrayList<String> neighbors;
	
	public AirBlock(String name, ArrayList<Point> coords)
	{
		this.m_name=name;
		this.m_Coords=coords;
	}
	public AirBlock()
	{
		this.m_name="";
		this.m_Coords=null;
	}
	
	public String GetName()
	{
		return m_name;
	}
	public ArrayList<Point> GetCoord()
	{
		return m_Coords;
	}
	public void SetName(String x)
	{
		this.m_name=x;
	}
	public void AddCoord(Point y)
	{
		this.m_Coords.add(y);
	}
	
	public double GetAltMin()
	{
		return m_altitudeMin;
	}
	
	public void SetAltMin(double altMin)
	{
		this.m_altitudeMin=altMin;
	}
	
	public double GetAltMax()
	{
		return m_altitudeMax;
	}
	
	public void SetAltMax(double altMax)
	{
		this.m_altitudeMax=altMax;
	}

	
	public void addFlight(Flight flight){
		this.m_occupation.put(flight.getFlightID(), flight);
		if (this.m_occupation.size()==this.m_capacity)
		{
			m_father.addFullAirb(this.m_name);
		}
	}
	
	public void removeFlight(Flight flight){
		this.m_occupation.remove(flight.getFlightID());
	}
	
	public HashMap<String,Flight> getOccupationMap(){
		return this.m_occupation;
	}
	
	public int getNbFligths(){
		return this.m_occupation.size();
	}
	
	public int getCapacity(){
		return this.m_capacity;
	}
	
	public void setCapacity(int capacity){
		this.m_capacity=capacity;
	}
	


	public String getFatherId() {
		return m_fatherId;
	}
	public void setFatherId(String m_fatherId) {
		this.m_fatherId = m_fatherId;
	}
	public Sector getFather() {
		return m_father;
	}
	public void setFather(Sector m_father) {
		this.m_father = m_father;
	}
	
	/**
	 * @return the neighbors
	 */
	public ArrayList<String> getNeighbors() {
		return neighbors;
	}
	
	/**
	 * @param neighbors the neighbors to set
	 */
	public void setNeighbors(ArrayList<String> neighbors) {
		this.neighbors = neighbors;
	}
	

}

