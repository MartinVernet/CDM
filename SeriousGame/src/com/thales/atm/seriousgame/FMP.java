package com.thales.atm.seriousgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.graphstream.graph.Path;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;

public class FMP extends Player {
	
	private ArrayList<AirSpace> m_airSpaces;
	private ArrayList<String> airspacesId;
	private map board;
	
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
	
	public void play(HashMap<Flight,FlightPlan> regulation){
		
	}
	
	public void play(){
		System.out.println("#####"+ this.m_id);
		String flightChosen="";
		Path routeChosen=new Path();
		HashMap<Flight,Path> allRegulations = new HashMap<Flight,Path>();
		for ( AirSpace airSpace : m_airSpaces )
		{
			for(Sector sector : airSpace.listOfFullSector)
			{
				int nbFlightToRegulate = sector.getOccupation().size()-sector.getCapacity();
				HashMap<Flight,Path> regulations = new HashMap<Flight,Path>();
				for (int i = 0;i<nbFlightToRegulate;i++)
				{
					System.out.println(sector.m_name+" need regulation");//board.m_airSpaceDictionary.get(sector);//action mettre dans boite au lettre , regulation a faire sur ce airblock
					System.out.println("choose flight ");
					flightChosen = chooseFlightRegulated(sector,regulations);
					System.out.println("choose possible rerout ");
					routeChosen = chooseReroute(sector,flightChosen);
					System.out.println("regulation du secteur "+sector.m_name+ " : \n"+ flightChosen+"  "+ routeChosen);
					regulations.put(sector.getOccupation().get(flightChosen),routeChosen);
					
					
				}
				//fligthPlanRefresh()
				//flightChosen.move()
				allRegulations.putAll(regulations);
			}
		}
		for(Flight flight : allRegulations.keySet() ){
			//flight.getFlightPlan().refreshFlightPlan()
		}
	}
	
	public String chooseFlightRegulated(Sector sector,HashMap<Flight,Path> alreadyRegulated){
		String flightChosen = "";
		System.out.println("choose flight ");
		for (String flightID: sector.getOccupation().keySet())
		{
			Flight flight = sector.getOccupation().get(flightID);
			if (!alreadyRegulated.containsKey(flight.getFlightID())){
				System.out.println(flight.getFlightID()+ " priority:"+ sector.getOccupation().get(flight.getFlightID()).getPriority());
			}
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
	
	public Path chooseReroute(Sector sector, String flightChosen){
		String routeChosen = "";
		FlightPlan fp = sector.getOccupation().get(flightChosen).getFlightPlan();
		Sector nextSector = new Sector();
		Sector previousSector = new Sector();
		for (Date d:fp.getAirspaceProfile().keySet())
		{
			if (fp.getAirspaceProfile().get(d).m_name.equals(sector))
			{
				nextSector=fp.getAirspaceProfile().get( fp.getAirspaceProfile().higherKey(d));
				previousSector=fp.getAirspaceProfile().get(fp.getAirspaceProfile().lowerKey(d));
			}
			
		}
		
		//ArrayList<ArrayList<Sector>> rerouteChoices = sector.getPossibleRerouting(nextSector);
		ArrayList<Path> rerouteChoices = board.getSetsOfShortestPath(sector, nextSector, previousSector);
		int i=0;
		for (Path path : rerouteChoices)
		{
			i+=1;
			System.out.println(i+" : " +path.toString());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			routeChosen = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rerouteChoices.get(Integer.parseInt(routeChosen));
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
