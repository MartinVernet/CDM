
package com.thales.atm.seriousgame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import javax.jws.WebService;


/**class Airblock
 *  name : id of the airblock
 *  m_Coords:list of points that create a 2D plan 
 * altitudeMin and altitudeMax : the altitudes of the airblock. 
 * m_father: pointer to the father sector
 * neighbors: list of  names of neighbors airblocks
 */
@WebService
public class AirBlock {

	private String m_name;
	private ArrayList<Point> m_Coords;
	private double m_altitudeMin=0;
	private double m_altitudeMax=0;
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

