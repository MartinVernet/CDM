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
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
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

public class Game {
	
	private Settings m_settings;
	private int m_turn;
	private HashMap<String,AOCPlayer> AOCplayers;
	private HashMap<String,FMP> FMPplayers;
	
	private HashMap<String,Integer> AirspaceToFMP;
	public Date currentDate;
	public map m_board;
	private TreeMap<Date,ArrayList<FlightPlan>> entryDate2FlightPlan;
	private CommunicationMainIHM mainClient;
	private mainIHMSimulator mainIHM;
	private HashMap<String,AOC> availableAirlines = new HashMap<String,AOC>();
	private HashMap<String,AOCIA> airlineIA = new HashMap<String,AOCIA>();
	
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
		this.AOCplayers=new HashMap<String, AOCPlayer>();
		this.FMPplayers=new HashMap<String, FMP>();
	}
	
	public Game(Settings settings)
	{
		this.m_settings=settings;
		this.m_turn=0;
		this.AOCplayers=new HashMap<String, AOCPlayer>();
		this.FMPplayers=new HashMap<String, FMP>();
	}
	
	
	/**
	 * Lance le jeu, une fois que les settings ont étés définis
	 */
	public void launchGame()
	{
		
		if(!this.m_settings.checkIfReady()){
			//system.out.println("settings non conformes");
		}
		
		else
		{
		ArrayList<String> chosenAirspaces = new ArrayList<String>();
		for (String key : FMPplayers.keySet()){
			chosenAirspaces.addAll(FMPplayers.get(key).getAirspacesID());
		}
		//chosenAirspaces.addAll(m_board.m_airSpaceDictionary.keySet());
		this.m_board.reduceMap(chosenAirspaces);
		this.m_board.displaymap();
		loadFlightPlans();
		//Main loop of the game
		while (this.isFinished()==false){
			this.m_turn++;
			this.startNewTurn();
			}
		System.out.println("Game Is Over");
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
		
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(this.currentDate);
	    cal.add(Calendar.MINUTE, 60);
	    Date endOfTurn=cal.getTime();
		
		//Date endOfTurn=new Date(m_settings.getTurnLength() + (10 * 60000));
		
		this.allocateFlights(currentDate,endOfTurn);//alloue à chaque AOC ses vols arrivant sur le plateau au prochain tour
		
		this.allocateBudgets();//alloue un budget à chaque AOC
		
		this.startAOCTurn();//AOC allocates their tokens to their flights
		
		//need function that start the slot, every x time the Board is reloaded with sectors occupation actualized and the current Date
		
		
		
		this.startFMPTurn(endOfTurn);//FMP regulates their sectors
		
		
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
		for ( String key : airlineIA.keySet() ){
			airlineIA.get(key).play();
		}
		
	}
	
	
	
	
	/**
	 Après le tour des AOC, le temps avance et à chaque besoin de régulation, les FMP interviennent sur leurs secteurs.
	 */
	private void startFMPTurn(Date endOfturn) 
	{
		
		while (endOfturn.after(currentDate)){
			this.forwardDate();
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.moveFlights();
			ecrire("test.txt", "\r\n");
			ecrire("test.txt", currentDate.toString()+"\r\n");
			for ( String key : FMPplayers.keySet() ){
				FMPplayers.get(key).play();
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
	
	private void moveFlights()
	{
		System.out.println(currentDate);
		//ArrayList<Sprite>
		SpriteManager sman = new SpriteManager(m_board.graph);
		for(String AOCId: availableAirlines.keySet() )
		{
			availableAirlines.get(AOCId).moveFlights(currentDate);
		}
		
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
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==1)
				node.setAttribute("ui.style", "fill-color: rgb(0,255,0);");//vert
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==2)
				node.setAttribute("ui.style", "fill-color: rgb(255,255,0);");//jaune
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()==3)
				node.setAttribute("ui.style", "fill-color: rgb(255,0,200);");//violet
			if (m_board.m_sectorDictionary.get(node.getId()).getOccupation().size()>3)
				node.setAttribute("ui.style", "fill-color: rgb(255,0,0);");//rouge
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
				AOCplayers.put(L.get(i).getName(), (AOCPlayer) L.get(i));
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
	 * Initiation des settings d'un nouveau jeu à l'aide de la console
	 * @throws IOException
	 */
	public void initiateNewGame() throws IOException
	{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Files selection
		////system.out.println("Airspace File ? ");
		//String airspaceFile = br.readLine();

		m_settings.setAirspaceFile("Airspace.spc");
				
		////system.out.println("Sector File ? ");
		//String sectorFile = br.readLine();
		m_settings.setSectorFile("Sector.gsl");
				
		////system.out.println("Airblock File ? ");
		//String airblockFile = br.readLine();
		m_settings.setAirblockFile("Airblock.gar");

		this.loadAirspace();
		/*for (String sectorID: m_board.m_sectorDictionary.get("BIRDNO").getNeighbors())
		{
			//system.out.println("#####################");
			//system.out.println(sectorID);
			//system.out.println("*******");
			for (String sectorID2: m_board.m_sectorDictionary.get(sectorID).getNeighbors())
			{
				//system.out.println(sectorID2);
			}
		}
		*/		
		ArrayList<String> chosenAirSpace =new ArrayList<String>();
		
		//tests
		chosenAirSpace.add("LFBBCTA"); // Bordeaux
		chosenAirSpace.add("LFFFCTA"); // Paris
		chosenAirSpace.add("LFMNFCTA"); // Nice
		chosenAirSpace.add("LIMMCTA"); // Milano
		chosenAirSpace.add("LIPPCTA"); // Padova
		chosenAirSpace.add("LIRRCTA"); // Roma

		this.m_board.reduceMap(chosenAirSpace);
		for(String airsSpaceID:m_board.m_airSpaceDictionary.keySet())
		{
			
			//system.out.println(airsSpaceID);
		}
		//system.out.println("#####################");
		for(String sectorID:m_board.m_sectorDictionary.keySet())
		{
			//system.out.println(m_board.m_sectorDictionary.get(sectorID).getFatherId());
		}
		
		this.loadFlightPlans();

		//Players creation
		String addNewPlayer="Y";
		int i=1;
		while (addNewPlayer.equals("Y"))
		{
			//system.out.println("Player "+i);
			//system.out.println("Name of player "+i+" ");
			String name = br.readLine();
			String type="none";
			while (!(type.equals("AOC") || type.equals("FMP")))
			{
				//system.out.println("Type of player: (AOC/FMP)");
				type=br.readLine();
				if (type.equals("AOC"))
				{
					AOCPlayer P = new AOCPlayer(name,i);
					this.addAOCPlayer(P);
				}
				if(type.equals("FMP"))
				{
					String airspaceID="";
					ArrayList<String> airspaces=new ArrayList<String>();
					//system.out.println("Airspace name ?");
					airspaceID=br.readLine();
					airspaces.add(airspaceID);

					//system.out.println("Add another airspace ? (Y/N)");
					String keep;
					keep=br.readLine();
					while(keep.equals("Y"))
					{
						//system.out.println("Airspace name ?");
						airspaceID=br.readLine();
						airspaces.add(airspaceID);

						//system.out.println("Add another airspace ? (Y/N)");
						keep=br.readLine();
					}
					FMP P = new FMP(name,i,airspaces);
					this.addFMPPlayer(P);
					P.setAirspaces(m_board);
				}
			}
			i+=1;
			//system.out.println("Do you want to add a new player (Y/N) ? ");
			addNewPlayer = br.readLine();
		}
		
		
		//Level choice
		//system.out.println("Enter level: ");
		int level = Integer.parseInt(br.readLine());
		m_settings.setLevel(level);
		        
		//Summary
		//system.out.println(m_settings.returnSettingsInfos());
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
		
		/*for ( String key : AOCplayers.keySet() ){
			
			//AOCplayers.get(key).setBudget(this.m_settings.getnbTokensPerFlight());
			int nbflight =AOCplayers.get(key).getNewFlights().size();
			int budgetmax=nbflight*m_settings.getnbTokensPerFlight();
			AOCplayers.get(key).setBudget(budgetmax);
			
		}
		for ( String key : airlineIA.keySet() ){
			
			//AOCplayers.get(key).setBudget(this.m_settings.getnbTokensPerFlight());
			int nbflight =airlineIA.get(key).getNewFlights().size();
			int budgetmax=nbflight*m_settings.getnbTokensPerFlight();
			airlineIA.get(key).setBudget(budgetmax);
			
		}*/
		for ( String key : availableAirlines.keySet() ){
			
			//AOCplayers.get(key).setBudget(this.m_settings.getnbTokensPerFlight());
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
		////system.out.println(beginDate);
		////system.out.println(endDate);
		for (String key : AOCplayers.keySet()){
			AOCplayers.get(key).clearNewFlights();
		}
		
		SortedMap<Date,ArrayList<FlightPlan>> nextTurnFlightPlans =new TreeMap <Date, ArrayList<FlightPlan>>();			
		nextTurnFlightPlans= entryDate2FlightPlan.subMap(beginDate, endDate);
		
		for (Date d :nextTurnFlightPlans.keySet() )
	    {
	    	//system.out.println("######");
	    	//system.out.println(d);
	    	for (FlightPlan fp: nextTurnFlightPlans.get(d))
	    	{
	    		//system.out.println(fp);
	    	}
	    }
		for (Date d : nextTurnFlightPlans.keySet()){
			for (FlightPlan fp : nextTurnFlightPlans.get(d)){
				Flight flight = new Flight(fp);
				if(availableAirlines.get(flight.getAirline())!=null)
				{
					availableAirlines.get(flight.getAirline()).addFlight(flight);
				}
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
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(this.currentDate);
	    cal.add(Calendar.MINUTE, m_settings.getDelta()); 
	    Date newDate=cal.getTime();
	    //ici: recuperer tous les avions qui sont sur sur le plateau et faire refresh sectorOccupation sur chacun 
	    //refreshSectorsOccupation(newDate);
	    this.currentDate=newDate;
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
	
	public void addAOCPlayer(AOCPlayer player){
		AOCplayers.put(player.m_name,player);
		m_settings.addPlayer(player);
	}
	public void addAOCIA(AOCIA IA){
		airlineIA.put(IA.m_name,IA);
		m_settings.addPlayer(IA);
	}
	
	
	public void addFMPPlayer(FMP player){
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

	public void ecrire(String nomFic, String texte)
	{
		//on va chercher le chemin et le nom du fichier et on me tout ca dans un String
		String adressedufichier = System.getProperty("user.dir") + "/"+ nomFic;
	
		//on met try si jamais il y a une exception
		try
		{
			/**
			 * BufferedWriter a besoin d un FileWriter, 
			 * les 2 vont ensemble, on donne comme argument le nom du fichier
			 * true signifie qu on ajoute dans le fichier (append), on ne marque pas par dessus 
			 
			 */
			FileWriter fw = new FileWriter(adressedufichier, true);
			
			// le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
			BufferedWriter output = new BufferedWriter(fw);
			
			//on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
			output.write(texte+"\n");
			//on peut utiliser plusieurs fois methode write
			
			output.flush();
			//ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
			
			output.close();
			//et on le ferme
			//System.out.println("fichier créé");
		}
		catch(IOException ioe){
			System.out.print("Erreur : ");
			ioe.printStackTrace();
			}

	}
	

}
