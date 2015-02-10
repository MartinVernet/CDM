package com.thales.atm.seriousgame;

import java.util.ArrayList;
import java.util.HashMap;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;

public class FMP extends Player {
	
	private ArrayList< AirSpace> m_airSpaces;
	
	public FMP(){
		super();
		m_airSpaces =new ArrayList<AirSpace>();
	}
	
	public FMP(String name, int i, ArrayList< AirSpace> airspaces) {
		// TODO Auto-generated constructor stub
		super(name,i);
		this.m_airSpaces=airspaces;
	}
	
	public void play(HashMap<Flight,FlightPlan> regulation){
		
	}
	
	public void play(map board ){
		for ( AirSpace airSpace : m_airSpaces )
		{
			for(String sector : airSpace.listOfFullSector)
			{
				board.m_airSpaceDictionary.get(sector);//action mettre dans boite au lettre , regulation a faire sur ce airblock
			}
		}
	}
	
	public String getType(){
		return "FMP";
	}
	

}
