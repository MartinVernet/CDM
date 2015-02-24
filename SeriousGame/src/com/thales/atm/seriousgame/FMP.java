package com.thales.atm.seriousgame;

import java.util.ArrayList;
import java.util.HashMap;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;

public class FMP extends Player {
	
	private ArrayList< AirSpace> m_airSpaces;
	private ArrayList<String> airspacesId;
	
	public FMP(){
		super();
		m_airSpaces = new ArrayList<AirSpace>();
		airspacesId = new ArrayList<String>();
	}
	
	public FMP(String name,int i){
		super(name,i);
	}
	
	public FMP(String name, int i, ArrayList<String> airspaces) {
		// TODO Auto-generated constructor stub
		super(name,i);
		this.airspacesId=airspaces;
	}
	
	public void setAirspaces(map board){
		for (String id : airspacesId){
			m_airSpaces.add(board.m_airSpaceDictionary.get(id));
		}

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

}
