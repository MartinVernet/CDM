package com.thales.atm.seriousgame;

import java.util.ArrayList;
import java.util.HashMap;

import com.thales.atm.seriousgame.Player;

public class FMP extends Player {
	
	private ArrayList<AirSpace> m_airspaces;
	
	public FMP(){
		super();
		ArrayList<AirSpace> m_airspaces;
	}
	
	public FMP(String name, int i, ArrayList<AirSpace> airspaces) {
		// TODO Auto-generated constructor stub
		super(name,i);
		this.setAirspaces(airspaces);
	}
	
	public void play(HashMap<Flight,FlightPlan> regulation){

	}
	
	public String getType(){
		return "FMP";
	}

	/**
	 * @return the m_airspaces
	 */
	public ArrayList<AirSpace> getAirspaces() {
		return m_airspaces;
	}

	/**
	 * @param m_airspaces the m_airspaces to set
	 */
	public void setAirspaces(ArrayList<AirSpace> m_airspaces) {
		this.m_airspaces = m_airspaces;
	}


}
