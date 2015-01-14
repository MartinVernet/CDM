package com.thales.atm.seriousgame;

import java.util.HashMap;

import com.thales.atm.seriousgame.Player;

public class FMP extends Player {
	
	private AirSpace m_airspace;
	
	public FMP(){
		super();
		AirSpace m_airspace;
	}
	
	public FMP(String name, int i, AirSpace airspace) {
		// TODO Auto-generated constructor stub
		super(name,i);
		this.m_airspace=airspace;
	}
	
	public void play(HashMap<Flight,FlightPlan> regulation){

	}
	
	public String getType(){
		return "FMP";
	}
	

}
