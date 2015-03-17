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

public class FMPIA extends FMP {
	
	public FMPIA(){
		super();
		m_airSpaces = new ArrayList<AirSpace>();
		airspacesId = new ArrayList<String>();
	}
	
	public FMPIA(String name,int i, map board){
		super(name,i,board);
		/**
		m_airSpaces = new ArrayList<AirSpace>();
		airspacesId = new ArrayList<String>();
		this.board = board;
		*/
	}
	
	public FMPIA(String name, int i, ArrayList<String> airspaces) {
		// TODO Auto-generated constructor stub
		super(name,i,airspaces);
		/**
		this.airspacesId=airspaces;
		m_airSpaces = new ArrayList<AirSpace>();
		*/
	}
	
	public void setAirspaces(map board){
		for (String id : airspacesId){
			m_airSpaces.add(board.m_airSpaceDictionary.get(id));
		}

	}
	
	public void play(HashMap<Flight,FlightPlan> regulation){
		
	}
	
	public void play(){
		System.out.println("#####"+ this.m_id);
		String flightChosen="";
		ArrayList<Sector> routeChosen;
		HashMap<Flight,ArrayList<Sector>> allRegulations = new HashMap<Flight,ArrayList<Sector>>();
		for ( AirSpace airSpace : m_airSpaces )
		{
			for(Sector sector : airSpace.listOfFullSector)
			{
				int nbFlightToRegulate = sector.getOccupation().size()-sector.getCapacity();
				HashMap<Flight,ArrayList<Sector>> regulations = new HashMap<Flight,ArrayList<Sector>>();
				HashSet<String> choiceOfFlight = new HashSet<String>(sector.getOccupation().keySet());
				int i = 0;
				while (i<nbFlightToRegulate && choiceOfFlight.size()>0)
				{
					System.out.println(sector.m_name+" need regulation");//board.m_airSpaceDictionary.get(sector);//action mettre dans boite au lettre , regulation a faire sur ce airblock
					System.out.println("choose flight ");
					flightChosen = chooseFlightRegulated(sector,choiceOfFlight);
					System.out.println("choose possible rerout ");
					routeChosen = chooseReroute(sector,flightChosen);
					if (routeChosen==null){
						System.out.println("Choose another flight. ERROR: First entrance on map");
						choiceOfFlight.remove(flightChosen);
					}
					else{
						System.out.println("regulation du secteur "+sector.m_name+ " : \n"+ flightChosen+"  "+ routeChosen);
						regulations.put(sector.getOccupation().get(flightChosen),routeChosen);
						choiceOfFlight.remove(flightChosen);
						i+=1;
					}
										
					
				}
				allRegulations.putAll(regulations);
			}
		}
		for(Flight flight : allRegulations.keySet() ){
			flight.getFlightPlan().refreshFlightPlanV2(flight.getCurrentSector(),allRegulations.get(flight),board.getPenality());
			flight.refreshPositionAfterRegulation(allRegulations.get(flight).get(0));
		}
	}
	
	public String chooseFlightRegulated(Sector sector,HashSet<String> choiceOfFlight){
		int minPriority = 1000;
		String flightChosen = "";
		for (String flightID: choiceOfFlight)
		{
			Flight flight = sector.getOccupation().get(flightID);
			if (flight.getPriority()<minPriority){
				minPriority = flight.getPriority();
				flightChosen = flightID;
			}
			else if (flight.getPriority()==minPriority){
				if (flight.getNbRegulations()<sector.getOccupation().get(flightChosen).getNbRegulations()){
					flightChosen = flightID;
				}
			}
		}
		return flightChosen;
	}
	
	public ArrayList<Sector> chooseReroute(Sector sector, String flightChosen){
		
		String routeChosen = "";
		FlightPlan fp = sector.getOccupation().get(flightChosen).getFlightPlan();
		System.out.println(fp);
		Sector nextSector = new Sector();
		Sector previousSector = new Sector();
		ArrayList<Sector> reroute = new ArrayList<Sector>();
		for (Date d:fp.getAirspaceProfile().keySet())
		{
			if (fp.getAirspaceProfile().get(d).m_name.equals(sector.m_name))
			{
				nextSector=fp.getAirspaceProfile().get( fp.getAirspaceProfile().higherKey(d));
				if (nextSector.m_name.equals("Exit")){
					nextSector=null;
					return null;
				}
				else if (nextSector.m_name.equals("Out")){
					Sector out =new Sector ();
					out.m_name="Out";
					reroute.add(out);
					return reroute;
				}
				else{
					
				}
				if (sector.m_name.equals(fp.getAirspaceProfile().firstEntry().getValue().m_name))
				{
					previousSector=null;
					return null;
				}
				else
				{
					previousSector=fp.getAirspaceProfile().get(fp.getAirspaceProfile().lowerKey(d));
				}
				
			}
			
		}
		
		//ArrayList<ArrayList<Sector>> rerouteChoices = sector.getPossibleRerouting(nextSector);
		Path path = board.getShortestPath(sector, nextSector, previousSector);
		ArrayList<Sector> arrayPath = new ArrayList<Sector>();
		path.popNode();
		while (path.size()>1)
		{
			arrayPath.add(board.m_sectorDictionary.get(path.popNode().getId()));
		}
		if (path.size()==1)
		{
			arrayPath.add(board.m_sectorDictionary.get(path.peekNode().getId()));
		}
		
		return arrayPath;
	}
}
