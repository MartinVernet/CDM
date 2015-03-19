package com.thales.atm.seriousgame;

import javax.jws.WebService;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

import sun.util.locale.provider.AvailableLanguageTags;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.Settings;
import com.thales.atm.seriousgame.client.mainIHMSimulator;
import com.thales.atm.seriousgame.communications.CommunicationMainIHM;
import com.thales.atm.seriousgame.flightparser.FlightPlanParser;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;
import com.thales.atm.seriousgame.flightmodel.PrintingMap;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;


@WebService
/**
 * 
 * @author Martin
 *	Main class of the game.
 *	This is the "Game Maker" which ask each player to play and make sure that the board is refresehd and the time forwarded
 *	Attributes :
 *	settings 				: settings of the game
 *	turn 					: current turn of the game
 *	AOCplayers 				: only AOC human players
 *	FMPplayers 				: all FMP players (humans + ia)
 *	currentDate 			: current date of the game
 *	board					: map of the game
 *	entryDate2FlightPlan	: treemap of all flightPlans ranked in chronological order in order to build flights 
 * 	availableAirlines		: all aoc (ia+players)
 * 	airlineIA				: only ia aoc
 * 	BoardMap				: not used (was a draft for IHM communications)
 * 	PlayerMap				: not used (was a draft for IHM communications)
 */
public class Game {
	
	private Settings m_settings;
	private int m_turn;
	private HashMap<String,AOCPlayer> AOCplayers;
	private HashMap<String,FMP> FMPplayers;
	public Date currentDate;
	public map m_board;
	private TreeMap<Date,ArrayList<FlightPlan>> entryDate2FlightPlan;
	private HashMap<String,AOC> availableAirlines = new HashMap<String,AOC>();
	private HashMap<String,AOCIA> airlineIA = new HashMap<String,AOCIA>();//instancié pour créer touts les airlines des vols arrivant sur la map
	
	public ConcurrentHashMap<String,Socket> BoardMap = new ConcurrentHashMap<String,Socket>();//not used (Interface draft)
	public ConcurrentHashMap<String,Socket> PlayerMap = new ConcurrentHashMap<String,Socket>();//not used ()
	
	
	
	/**
	 * Constructeur de la classe Game
	 */
	public Game()
	{
		this.m_settings=new Settings();
		this.m_turn=0;
		this.AOCplayers=new HashMap<String, AOCPlayer>();
		this.FMPplayers=new HashMap<String, FMP>();
	}
	
	/**
	 * Constructeur de la classe Game
	 */
	public Game(Settings settings)
	{
		this.m_settings=settings;
		this.m_turn=0;
		this.AOCplayers=new HashMap<String, AOCPlayer>();
		this.FMPplayers=new HashMap<String, FMP>();
	}
	
	
	/**
	 * Launch game once settings are defined
	 */
	public void launchGame()
	{
		ArrayList<String> chosenAirspaces = new ArrayList<String>();
		
		for (String key : FMPplayers.keySet()){
			chosenAirspaces.addAll(FMPplayers.get(key).getAirspacesID());
		}
		
		this.m_board.reduceMap(chosenAirspaces);
		this.m_board.displaymap();
		loadFlightPlans();
		
		//Main loop of the game
		while (this.isFinished()==false){
			this.m_turn++;
			this.startNewTurn();
			}
		printGameResults();
		System.out.println("Game Is Over");
	}
	
	
	
	/**
	 * Gère le déroulement d'un nouveau tour
	 * 1. Allocation des vols du nouveau tour aux airlines
	 * 2. Allocation du budget de jetons aux airlines
	 * 3. Début du tour des AOC
	 * 4. Début du tour des FMP
	 * 5. Retour de Dashboards résumant le tour pour chaque joueur
	 */
	public void startNewTurn()
	{	
		//long t=currentDate.getTime();
		
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(this.currentDate);
	    cal.add(Calendar.MINUTE, m_settings.getTurnLength());
	    Date endOfTurn=cal.getTime();
		
	    //alloue à chaque AOC ses vols arrivant sur le plateau au prochain tour
		this.allocateFlights(currentDate,endOfTurn);
		
		//alloue un budget à chaque AOC
		this.allocateBudgets();
		
		//AOC allocates their tokens to their flights
		this.startAOCTurn();		
		
		this.m_board.setCapacities(m_settings.getLevel());
		
		//FMP regulates their sectors
		this.startFMPTurn(endOfTurn);
	}
	
	
	/**
	 * Time is paused and each AOC player can bet his tokens on his flights entering the map.
	 */
	private void startAOCTurn() 
	{
		// *******IHM******* Send to each AOC IHM the flights needing prority allocation
		// *******IHM******* Receive from AOC IHM the priority for each flight
		for ( String key : AOCplayers.keySet() ){
			AOCplayers.get(key).play();
		}
		for ( String key : airlineIA.keySet() ){
			airlineIA.get(key).play();
		}
		
	}
	
	
	
	
	/**
	 * After AOC turn, time is going forward and at each step, it will check whether there is a regulation need so that the FMP can play and implement their regulation
	 */
	private void startFMPTurn(Date endOfturn) 
	{
		
		while (endOfturn.after(currentDate)){
			this.forwardDate();
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.moveFlights();
			
			//log file to keep history of the game
			ecrire("test.txt", "\r\n");
			ecrire("test.txt", currentDate.toString()+"\r\n");
			
			boolean regulation = true;
			while (regulation){
				
				// *******IHM******* If the airspace of an FMP player need regulation, send him the possible regulations
				// *******IHM******* Then receive the regulated flight and the chosen reroute
				for ( String key : FMPplayers.keySet() ){
					FMPplayers.get(key).play();
				}
				regulation = false;
				for (String as: m_board.m_airSpaceDictionary.keySet()){
					regulation = regulation || m_board.m_airSpaceDictionary.get(as).getNeedRegulation();
				}
			}
			
			
			for(String IdSect: m_board.m_sectorDictionary.keySet())
			{
				
				ecrire("test.txt", "	"+IdSect+"\r\n");
				for (String flightid : m_board.m_sectorDictionary.get(IdSect).getOccupation().keySet())
				{
					ecrire("test.txt", "		"+flightid+" [priority= "+m_board.m_sectorDictionary.get(IdSect).getOccupation().get(flightid).getPriority()+" |airline= "+m_board.m_sectorDictionary.get(IdSect).getOccupation().get(flightid).getAirline()+"]" +"\r\n");
				}
			}
		}	
	}
	
	
	/**
	 * move all flights currently on board and then refresh the graph
	 */
	private void moveFlights()
	{
		System.out.println(currentDate);
		
		for(String AOCId: availableAirlines.keySet() )
		{
			availableAirlines.get(AOCId).moveFlights(currentDate);
		}
		
		// refresh the graph
		// *******IHM******* Should refresh the position of the flights on the IHM
		for(Node node :m_board.graph )
		{
			int nbofFlight=m_board.m_sectorDictionary.get(node.getId()).getOccupation().size();
			node.addAttribute("ui.label", node.getId()+" "+nbofFlight);
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==0)
			{
				node.setAttribute("ui.style", "fill-color: rgb(0,0,255);");//bleu
				node.addAttribute("ui.label", node.getId()+" "+nbofFlight);
			}
			else 
			{
				String flights="";
				for(String flightId: m_board.m_sectorDictionary.get(node.getId()).getOccupation().keySet())
				{
					flights+=", "+flightId ;
					
				}
				node.addAttribute("ui.label", node.getId()+" "+nbofFlight+"/ "+flights);
			}
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==1 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(0,255,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==2 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(50,200,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==3 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(100,150,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==4 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(150,100,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==5 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(200,50,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==6 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(125,255,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==7 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(150,255,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==9 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(175,255,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==10 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(200,255,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==11 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(225,255,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()>11 && m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()<m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(255,255,0);");
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()>m_board.m_sectorDictionary.get(node.getId()).getCapacity())
				node.setAttribute("ui.style", "fill-color: rgb(255,0,0);");//rouge
		}
		
		
		
	}
	

	
	/**
	 * From a list of players coming from the settings, this method will fill the maps AOCplayers and FMPplayers
	 * Beware : in get players list there should only be actual human players for AOCs BUT ALL FMP should be in settings.PlayersList (IA and Players)
	 */
	public void getFinalMapsOfPlayers()
	{
		ArrayList<Player> L=m_settings.getPlayersList();
		for (int i=0;i<L.size();i++){
			if (L.get(i).getType()=="AOC"){
				AOCplayers.put(L.get(i).getName(), (AOCPlayer) L.get(i));
			}
			else{
				FMPplayers.put(L.get(i).getName(), (FMPPLayer) L.get(i));
			}
		}
	}
	
	
	
	
	/**
	 * Create airspaces, sectors and airblock given filename defined in settings
	*/
	public void loadAirspace()
	{
		this.m_board = new map(this.m_settings.getAirBlockFile(),this.m_settings.getSectorFile(),this.m_settings.getAirspaceFile());
		this.m_board.setPenality(m_settings.getDelayPerSector());
	}
	
	/**
	 * using the flightPlanParser, read the flightplans and then create ia airlines and fill entryDate2FlightPlan map
	 */
	public void loadFlightPlans()
	{
		FlightPlanParser read = new FlightPlanParser();
	    List<FlightPlan> parseFlightPlan = read.parseFlightPlan("PlansDeVol.xml", m_board.m_completSectorDictionary, m_board.m_sectorDictionary);
	    //Tests
	    for (FlightPlan flight : parseFlightPlan) {
	      System.out.println(flight);
	    }
	    //Create Tree Map
	    entryDate2FlightPlan= new TreeMap<Date, ArrayList<FlightPlan>>();
	    for (FlightPlan fp : parseFlightPlan){
	    	if (availableAirlines.get(fp.getAirline())==null){
	    		if (AOCplayers.get(fp.getAirline())==null)
	    		{
		    		AOCIA aoc = new AOCIA(fp.getAirline(),0);
		    		availableAirlines.put(fp.getAirline(),aoc);
		    		airlineIA.put(fp.getAirline(),aoc);
		    		//AOCplayers.put(aoc.getName(), aoc);
	    		}
	    		else 
	    		{
	    			availableAirlines.put(fp.getAirline(),AOCplayers.get(fp.getAirline()));
	    		}
	    	}
	    	if (entryDate2FlightPlan.get(fp.getEntryMap())==null){
	    		ArrayList<FlightPlan> FPlist = new ArrayList<FlightPlan>();
	    		FPlist.add(fp);
	    		entryDate2FlightPlan.put(fp.getEntryMap(), FPlist);
	    	}
	    	else{
	    		entryDate2FlightPlan.get(fp.getEntryMap()).add(fp);
	    	}
	    }
	    for (String id:availableAirlines.keySet())
	    {
	    	System.out.println(id);
	    }
	    for (Date d :entryDate2FlightPlan.keySet() )
	    {
	    	System.out.println("######");
	    	System.out.println(d);
	    	for (FlightPlan fp: entryDate2FlightPlan.get(d))
	    	{
	    		System.out.println(fp);
	    	}
	    }
	}
	
	
	
	
	/**
	Vérifie si une condition d'arrêt du jeu est remplie.
	@return True si le jeu se termine, False sinon
	*/
	private boolean isFinished() 
	{
		if(m_turn>= m_settings.getNbMaxTurn())
		{
			return true;
		}
		return false;
	}
	
	
	
	
	/**
	 * Alloue un budget de jetons par AOC, en fonction d'un nombre de jeton par vol, défini dans les settings.
	*/
	private void allocateBudgets() 
	{
		for ( String key : availableAirlines.keySet() ){
			int nbflight =availableAirlines.get(key).getNewFlights().size();
			int budgetmax=nbflight*m_settings.getnbTokensPerFlight();
			availableAirlines.get(key).setBudget(budgetmax);
		}
		
	}
	
	/**
	A chaque nouveau tours, cherche les nouveaux vols qui arrivent sur le plateau et les alloue aux AOC correspondants pour qu'ils misent des jetons dessus.
	*/
	private void allocateFlights(Date beginDate, Date endDate)
	{
		for (String key : AOCplayers.keySet()){
			AOCplayers.get(key).clearNewFlights();
		}
		
		SortedMap<Date,ArrayList<FlightPlan>> nextTurnFlightPlans =new TreeMap <Date, ArrayList<FlightPlan>>();			
		nextTurnFlightPlans= entryDate2FlightPlan.subMap(beginDate, endDate);
		
		for (Date d : nextTurnFlightPlans.keySet()){
			for (FlightPlan fp : nextTurnFlightPlans.get(d)){
				Flight flight = new Flight(fp);
				if(availableAirlines.get(flight.getAirline())!=null)
				{
					availableAirlines.get(flight.getAirline()).addFlight(flight);
					flight.setAOC(availableAirlines.get(flight.getAirline()));
				}
			}
		}
		
	}
	
	public void printGameResults(){
		PrintWriter writer;
		try {
			writer = new PrintWriter("Results.txt", "UTF-8");
			writer.println("RESULTS OF THE GAME");
			writer.println("\n");
			writer.println("\n");
			writer.println("\n");
			for (AOC aoc : availableAirlines.values()){
				writer.println("* Regulated flights of "+aoc.m_name+": ");
				for (Flight flight : aoc.regulatedFlights.values()){
					writer.println("	> "+flight.getFlightID());
					writer.println("	  "+"Aircraft: "+flight.getFlightPlan().getAircraftType());
					writer.println("	  "+"Priority: "+flight.getPriority());
				}
				if (aoc.regulatedFlights.keySet().size()==0){
					writer.println("	> No regulations");
				}
				writer.println("\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 Fait avancer le temps d'un pas préalablement défini dans les Settings du jeu.
	 */
	private void forwardDate()
	{
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(this.currentDate);
	    cal.add(Calendar.MINUTE, m_settings.getDelta()); 
	    Date newDate=cal.getTime();
	    this.currentDate=newDate;
	}
	
	public Settings getSettings(){
		return m_settings;
	}
	
	public void setSettings(Settings settings){
		m_settings=settings;
	}
	
	public void addAOCPlayer(AOCPlayer player){
		AOCplayers.put(player.m_name,player);
		m_settings.addPlayer(player);
	}
	public void addAOCIA(AOCIA IA){
		airlineIA.put(IA.m_name,IA);
		m_settings.addPlayer(IA);
	}
	
	
	public void addFMP(FMP player){
		FMPplayers.put(player.m_name,player);
		m_settings.addPlayer(player);
	}
	
	public HashMap<String,AOCPlayer> getAOCplayersDict(){
		return AOCplayers;
	}
	
	public HashMap<String,FMP> getFMPplayersDict(){
		return FMPplayers;
	}

	public map getBoard() {
		return m_board;
	}
	

	public void setCurrentDate(Date date){
		this.currentDate=date;
	}
	
	public Date getCurrentDate(){
		return currentDate;
	}
	
	
	/**
	 * Test function
	 * @param nomFic
	 * @param texte
	 */
	public void ecrire(String nomFic, String texte)
	{
		String adressedufichier = System.getProperty("user.dir") + "/"+ nomFic;
	
		try
		{
			FileWriter fw = new FileWriter(adressedufichier, true);
			
			BufferedWriter output = new BufferedWriter(fw);
			
			output.write(texte+"\n");
			
			output.flush();
			
			output.close();
		}
		catch(IOException ioe){
			System.out.print("Erreur : ");
			ioe.printStackTrace();
			}
	}
	
	
	

}
