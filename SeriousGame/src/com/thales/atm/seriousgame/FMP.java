package com.thales.atm.seriousgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;

public class FMP extends Player {
	
	protected ArrayList<AirSpace> m_airSpaces;
	protected ArrayList<String> airspacesId;
	protected map board;
	
	public FMP(){
		super();
		m_airSpaces = new ArrayList<AirSpace>();
		airspacesId = new ArrayList<String>();
	}
	
	public FMP(String name,int i, map board){
		super(name,i);
		m_airSpaces = new ArrayList<AirSpace>();
		airspacesId = new ArrayList<String>();
		this.board = board;
	}
	
	public FMP(String name, int i, ArrayList<String> airspaces) {
		// TODO Auto-generated constructor stub
		super(name,i);
		this.airspacesId=airspaces;
		m_airSpaces = new ArrayList<AirSpace>();
	}
	
	public void setAirspaces(map board){
		for (String id : airspacesId){
			m_airSpaces.add(board.m_airSpaceDictionary.get(id));
		}

	}
	/**
	public void play(HashMap<Flight,FlightPlan> regulation){
		
	}
	*/
	public void play(){
		
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

	public ArrayList<AirSpace> getAirspaces() {
		return m_airSpaces;
	}
	
	public ArrayList<String> getAirspacesID() {
		return airspacesId;
	}

	public void addAirspace(String airspaceId)
	{
		airspacesId.add(airspaceId);
	}
}

