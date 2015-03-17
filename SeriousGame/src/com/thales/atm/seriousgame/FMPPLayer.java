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

public class FMPPLayer extends FMP {
	
	public FMPPLayer(){
		super();
		/**
		m_airSpaces = new ArrayList<AirSpace>();
		airspacesId = new ArrayList<String>();
		*/
	}
	
	public FMPPLayer(String name,int i, map board){
		super(name,i,board);
		/**
		m_airSpaces = new ArrayList<AirSpace>();
		airspacesId = new ArrayList<String>();
		
		this.board = board;
		*/
	}
	
	public FMPPLayer(String name, int i, ArrayList<String> airspaces) {
		// TODO Auto-generated constructor stub
		super(name,i,airspaces);
		/**
		this.airspacesId=airspaces;
		m_airSpaces = new ArrayList<AirSpace>();
		*/
	}
	/**
	public void setAirspaces(map board){
		for (String id : airspacesId){
			m_airSpaces.add(board.m_airSpaceDictionary.get(id));
		}

	}
	*/
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
			flight.addRegulations();
		}
	}
	
	public String chooseFlightRegulated(Sector sector,HashSet<String> choiceOfFlight){
		
		String flightChosen = "";
		System.out.println("choose flight ");
		for (String flightID: choiceOfFlight)
		{
			Flight flight = sector.getOccupation().get(flightID);
			System.out.println(flight.getFlightID()+ " priority:"+ sector.getOccupation().get(flight.getFlightID()).getPriority());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			flightChosen = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		ArrayList<Path> rerouteChoices = board.getSetsOfShortestPath(sector, nextSector, previousSector);
		int i=0;
		ArrayList<ArrayList<Sector>> arrayRerouteChoices = new ArrayList<ArrayList<Sector>>();
		for (Path path : rerouteChoices)
		{
			String p="";
			//Path local = new Path();
			//local = path;
			ArrayList<Sector> arrayPath = new ArrayList<Sector>();
			//System.out.println(local);
			while (path.size()>1)
			{
				//p+=local.popNode()+" ; ";
				arrayPath.add(board.m_sectorDictionary.get(path.popNode().getId()));
			}
			if (path.size()==1)
			{
				//p+=local.peekNode();
				arrayPath.add(board.m_sectorDictionary.get(path.peekNode().getId()));
			}
			//System.out.println(i+" : " +path.toString());
			arrayRerouteChoices.add(arrayPath);
			System.out.println(i+" : " +arrayPath);
			i+=1;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			routeChosen = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Path pathChosen =  rerouteChoices.get(Integer.parseInt(routeChosen));
		ArrayList<Sector> pathChosen = arrayRerouteChoices.get(Integer.parseInt(routeChosen));
		System.out.println(pathChosen);
		/**while(pathChosen.size()>1)
		{
			reroute.add(board.m_sectorDictionary.get(pathChosen.popNode().getId()));
			System.out.println(pathChosen.popNode().getId());
		}
		if(pathChosen.size()==1)
		{
			reroute.add(board.m_sectorDictionary.get(pathChosen.peekNode().getId()));
			System.out.println(pathChosen.peekNode().getId());
		}*/
		
		return pathChosen;
	}
	/**
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
	}*/
}
