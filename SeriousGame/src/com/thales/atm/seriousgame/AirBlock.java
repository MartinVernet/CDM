
package com.thales.atm.seriousgame;
import java.util.ArrayList;

import javax.jws.WebService;

@WebService
public class AirBlock {
	String m_name;
	ArrayList<Point> m_Coords;
	double m_altitudeMin=0;
	double m_altitudeMax=0;

	
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
	
}

