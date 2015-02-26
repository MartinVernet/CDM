package com.thales.atm.seriousgame;

import javax.jws.WebService;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.Settings;
import com.thales.atm.seriousgame.client.mainIHMSimulator;
import com.thales.atm.seriousgame.communications.CommunicationMainIHM;
import com.thales.atm.seriousgame.flightparser.FlightPlanParser;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;
import com.thales.atm.seriousgame.flightmodel.PrintingMap;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;


@WebService

public class Game {
	
	
	
	private Settings m_settings;
	private int m_turn;
	private HashMap<String,AOC> AOCplayers;
	private HashMap<String,FMP> FMPplayers;
	
	private HashMap<String,Integer> AirspaceToFMP;
	private Date currentDate;
	private map m_board;
	private TreeMap<Date,ArrayList<FlightPlan>> entryDate2FlightPlan;
	private CommunicationMainIHM mainClient;
	private mainIHMSimulator mainIHM;
	
	public ConcurrentHashMap<String,Socket> BoardMap = new ConcurrentHashMap<String,Socket>();
	public ConcurrentHashMap<String,Socket> PlayerMap = new ConcurrentHashMap<String,Socket>();
	
	
	
	/**
	 * Constructeur de la classe Game
	 * @param port
	 */
	public Game()
	{
		this.m_settings=new Settings();
		this.m_turn=0;
		this.AOCplayers=new HashMap<String, AOC>();
		this.FMPplayers=new HashMap<String, FMP>();
	}
	
	public Game(Settings settings)
	{
		this.m_settings=settings;
		this.m_turn=0;
		this.AOCplayers=new HashMap<String, AOC>();
		this.FMPplayers=new HashMap<String, FMP>();
	}
	
	
	/**
	 * Lance le jeu, une fois que les settings ont étés définis
	 */
	public void launchGame()
	{
		
		if(!this.m_settings.checkIfReady()){
			System.out.println("settings non conformes");
		}
		
		else
		{
		ArrayList<String> chosenAirspaces = new ArrayList<String>();
		for (String key : FMPplayers.keySet()){
			chosenAirspaces.addAll(FMPplayers.get(key).getAirspacesID());
			}
		this.m_board.reduceMap(chosenAirspaces);
		
		//Main loop of the game
		while (this.isFinished()==false){
			this.m_turn++;
			this.startNewTurn();
			}
		}
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
		long t=currentDate.getTime();
		Date endOfTurn=new Date(m_settings.getTurnLength() + (10 * 60000));
		
		this.allocateFlights(currentDate,endOfTurn);//alloue à chaque AOC ses vols arrivant sur le plateau au prochain tour
		
		this.allocateBudgets();//alloue un budget à chaque AOC
		
		this.startAOCTurn();//AOC allocates their tokens to their flights
		
		this.startFMPTurn();//FMP regulates their sectors
		
		this.getDashboards();//Display metrics of the last turn
	}
	
	
	
	/**
	 Le temps est mis en pause et chaque AOC peut miser ses jetons sur ses vols.
	 */
	private void startAOCTurn() 
	{
		for ( String key : AOCplayers.keySet() ){
			AOCplayers.get(key).play();
		}
		
	}
	
	
	
	
	/**
	 Après le tour des AOC, le temps avance et à chaque besoin de régulation, les FMP interviennent sur leurs secteurs.
	 */
	private void startFMPTurn() 
	{
		while (this.endOfTurn()==false){
			for ( String key : FMPplayers.keySet() ){
				FMPplayers.get(key).play();
			}
			this.forwardDate();
		}
		
	}
	
	
	

	
	/**
	 * A partir d'une liste de joueurs, remplis les deux dicts AOCplayers et FMP players
	 */
	public void getFinalMapsOfPlayers()
	{
		ArrayList<Player> L=m_settings.getPlayersList();
		for (int i=0;i<L.size();i++){
			if (L.get(i).getType()=="AOC"){
				AOCplayers.put(L.get(i).getName(), (AOC) L.get(i));
			}
			else{
				FMPplayers.put(L.get(i).getName(), (FMP) L.get(i));
			}
		}
	}
	
	
	
	
	/**
    Crée les airspaces, sectors et airblocks à partir des fichiers donnés dans les settings.
	*/
	public void loadAirspace()
	{
		this.m_board = new map(this.m_settings.getAirBlockFile(),this.m_settings.getSectorFile(),this.m_settings.getAirspaceFile());
	}
	
	
	public void loadFlightPlans()
	{
		FlightPlanParser read = new FlightPlanParser();
	    List<FlightPlan> parseFlightPlan = read.parseFlightPlan("PlansDeVol.xml", m_board.m_sectorDictionary);
	    //Tests
	    for (FlightPlan flight : parseFlightPlan) {
	      System.out.println(flight);
	    }
	    //Create Tree Map
	    entryDate2FlightPlan= new TreeMap<Date, ArrayList<FlightPlan>>();
	    for (FlightPlan fp : parseFlightPlan){
	    	if (entryDate2FlightPlan.get(fp.getEntryMap())==null){
	    		ArrayList<FlightPlan> FPlist = new ArrayList<FlightPlan>();
	    		FPlist.add(fp);
	    		entryDate2FlightPlan.put(fp.getEntryMap(), FPlist);
	    	}
	    	else{
	    		entryDate2FlightPlan.get(fp.getEntryMap()).add(fp);
	    	}
	    }
	}
	
	
	
	/**
	 * Initiation des settings d'un nouveau jeu à l'aide de la console
	 * @throws IOException
	 */
	public void initiateNewGame() throws IOException
	{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Files selection
		//System.out.println("Airspace File ? ");
		//String airspaceFile = br.readLine();

		m_settings.setAirspaceFile("Airspace.spc");
				
		//System.out.println("Sector File ? ");
		//String sectorFile = br.readLine();
		m_settings.setSectorFile("Sector.gsl");
				
		//System.out.println("Airblock File ? ");
		//String airblockFile = br.readLine();
		m_settings.setAirblockFile("Airblock.gar");

		this.loadAirspace();
		/*for (String sectorID: m_board.m_sectorDictionary.get("BIRDNO").getNeighbors())
		{
			System.out.println("#####################");
			System.out.println(sectorID);
			System.out.println("*******");
			for (String sectorID2: m_board.m_sectorDictionary.get(sectorID).getNeighbors())
			{
				System.out.println(sectorID2);
			}
		}
		*/		
		ArrayList<String> chosenAirSpace =new ArrayList<String>();
		

		chosenAirSpace.add("LIMMCTA"); //Milano
		chosenAirSpace.add("LIPPCTA"); //Padova
		chosenAirSpace.add("LIRRCTA"); //Roma

		this.m_board.reduceMap(chosenAirSpace);
		for(String airsSpaceID:m_board.m_airSpaceDictionary.keySet())
		{
			
			System.out.println(airsSpaceID);
		}
		System.out.println("#####################");
		for(String sectorID:m_board.m_sectorDictionary.keySet())
		{
			System.out.println(m_board.m_sectorDictionary.get(sectorID).getFatherId());
		}
		
		this.loadFlightPlans();
		PrintingMap<Date, ArrayList<FlightPlan>> test = new PrintingMap<Date, ArrayList<FlightPlan>>(entryDate2FlightPlan);
		//System.out.println(test);

		//Players creation
		String addNewPlayer="Y";
		int i=1;
		while (addNewPlayer.equals("Y"))
		{
			System.out.println("Player "+i);
			System.out.println("Name of player "+i+" ");
			String name = br.readLine();
			String type="none";
			while (!(type.equals("AOC") || type.equals("FMP")))
			{
				System.out.println("Type of player: (AOC/FMP)");
				type=br.readLine();
				if (type.equals("AOC"))
				{
					AOC P = new AOC(name,i);
					this.addAOCPlayer(P);
				}
				if(type.equals("FMP"))
				{
					String airspaceID="";
					ArrayList<String> airspaces=new ArrayList<String>();
					System.out.println("Airspace name ?");
					airspaceID=br.readLine();
					airspaces.add(airspaceID);

					System.out.println("Add another airspace ? (Y/N)");
					String keep;
					keep=br.readLine();
					while(keep.equals("Y"))
					{
						System.out.println("Airspace name ?");
						airspaceID=br.readLine();
						airspaces.add(airspaceID);

						System.out.println("Add another airspace ? (Y/N)");
						keep=br.readLine();
					}
					FMP P = new FMP(name,i,airspaces);
					this.addFMPPlayer(P);
					P.setAirspaces(m_board);
				}
			}
			i+=1;
			System.out.println("Do you want to add a new player (Y/N) ? ");
			addNewPlayer = br.readLine();
		}
		
		
		//Level choice
		System.out.println("Enter level: ");
		int level = Integer.parseInt(br.readLine());
		m_settings.setLevel(level);
		        
		//Summary
		System.out.println(m_settings.returnSettingsInfos());
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
		
		for ( String key : AOCplayers.keySet() ){
			
			AOCplayers.get(key).setBudget(this.m_settings.getnbTokensPerFlight());
			
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
		SortedMap<Date,ArrayList<FlightPlan>> nextTurnFlightPlans 
						= entryDate2FlightPlan.subMap(beginDate, endDate);
		for (Date d : nextTurnFlightPlans.keySet()){
			for (FlightPlan fp : nextTurnFlightPlans.get(d)){
				Flight flight = new Flight(fp);
				AOCplayers.get(flight.getAirline()).addFlight(flight);
			}
		}
		
	}

	
	
	
	
	private void getDashboards() 
	{
		// TODO Auto-generated method stub
		
	}
	

	
	
	
	/**
	 Fait avancer le temps d'un pas préalablement défini dans les Settings du jeu.
	 */
	private void forwardDate()
	{
		
	}
	
	
	
	
	
	private boolean endOfTurn()
	{
		return false;
	}
	
	public Settings getSettings(){
		return m_settings;
	}
	
	public void setSettings(Settings settings){
		m_settings=settings;
	}
	
	public void addAOCPlayer(AOC player){
		AOCplayers.put(player.m_name,player);
		m_settings.addPlayer(player);
	}
	
	public void addFMPPlayer(FMP player){
		FMPplayers.put(player.m_name,player);
		m_settings.addPlayer(player);
	}
	
	public HashMap<String,AOC> getAOCplayersDict(){
		return AOCplayers;
	}
	
	public HashMap<String,FMP> getFMPplayersDict(){
		return FMPplayers;
	}

	public map getBoard() {
		return m_board;
	}
	
}
