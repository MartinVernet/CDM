package com.thales.atm.seriousgame;
import javax.jws.WebService;

@WebService
public class Point {
	double m_x;
	double m_y;
	
	// Constructor
	public Point(double x, double y)
	{
	    this.m_x=x;
	    this.m_y=y;
	}
	
	// SET et GET
	public double GetX()
	{
		return m_x;
	}
	public double GetY()
	{
		return m_y;
	}
	public void SetX(double x)
	{
		this.m_x=x;
	}
	public void SetY(double y)
	{
		this.m_y=y;
	}
}
