package com.thales.atm.seriousgame;

import javax.jws.WebService;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.Settings;
import com.thales.atm.seriousgame.client.mainIHMSimulator;
import com.thales.atm.seriousgame.communications.CommunicationMainIHM;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		
		//create socket toward main IHM
		//this.mainClient=new CommunicationMainIHM(port);
		//this.mainIHM=new mainIHMSimulator(port);
	}
	
	public Game(Settings settings)
	{
		this.m_settings=settings;
		this.m_turn=0;
		this.AOCplayers=new HashMap<String, AOC>();
		this.FMPplayers=new HashMap<String, FMP>();
		
		
		
		//create socket toward main IHM
		//this.mainClient=new CommunicationMainIHM(port);
		//this.mainIHM=new mainIHMSimulator(port);
	}
	
	public void initiateGame(){
		this.loadAirspace();
		this.getFinalMapsOfPlayers();
		for (String name : FMPplayers.keySet()){
			FMPplayers.get(name).setAirspaces(m_board);
		}
	}
	
	
	
	
	/**
	 * Lance le jeu, une fois que les settings ont �t�s d�finis
	 */
	public void launchGame()
	{
		
		//verify that the game is OK
		while (this.m_settings.checkIfReady()!=true){
			this.m_settings.solveProblems();
		}
		
		
		//this.loadAirspace();
		//this.m_board.reduceMap(null);//replace null by  selected airSpace from IHM
		
		//Main loop of the game
		while (this.isFinished()==false){
			
			this.m_turn++;
			this.startNewTurn();
		}
	}
	
	
	
	/**
	 * G�re le d�roulement d'un nouveau tour
	 * 1. Allocation des vols du nouveau tour aux airlines
	 * 2. Allocation du budget de jetons aux airlines
	 * 3. D�but du tour des AOC
	 * 4. D�but du tour des FMP
	 * 5. Retour de Dashboards r�sumant le tour pour chaque joueur
	 */
	public void startNewTurn()
	{
		
		this.allocateFlights();
		
		this.allocateTokens();//
		
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
	 Apr�s le tour des AOC, le temps avance et � chaque besoin de r�gulation, les FMP interviennent sur leurs secteurs.
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
    Cr�e les airspaces, sectors et airblocks � partir des fichiers donn�s dans les settings.
	*/
	public void loadAirspace()
	{
		this.m_board = new map(this.m_settings.getAirBlockFile(),this.m_settings.getSectorFile(),this.m_settings.getAirspaceFile());
	}
	
	
	
	/**
	 * Initiation des settings d'un nouveau jeu � l'aide de la console
	 * @throws IOException
	 */
	public void initiateNewGame() throws IOException
	{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Files selection
		//System.out.println("Airspace File ? ");
		//String airspaceFile = br.readLine();
		m_settings.setAirspaceFile("C:/Users/Martin/Desktop/Cours 3A/Projet/Datas/map_ACC/Airspace.spc");
				
		//System.out.println("Sector File ? ");
		//String sectorFile = br.readLine();
		m_settings.setSectorFile("C:/Users/Martin/Desktop/Cours 3A/Projet/Datas/map_ACC/Sector.gsl");
				
		//System.out.println("Airblock File ? ");
		//String airblockFile = br.readLine();
		m_settings.setAirblockFile("C:/Users/Martin/Desktop/Cours 3A/Projet/Datas/map_ACC/Airblock.gar");
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
		chosenAirSpace.add("GCCCCTA");
		chosenAirSpace.add("GMMMCTA");
		chosenAirSpace.add("GOOOCTA");
		
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
					m_settings.addPlayer(P);
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
					m_settings.addPlayer(P);
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
	V�rifie si une condition d'arr�t du jeu est remplie.
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
	 * Alloue un budget de jetons par AOC, en fonction d'un nombre de jeton par vol, d�fini dans les settings.
	*/
	private void allocateTokens() 
	{
		
		for ( String key : AOCplayers.keySet() ){
			
			AOCplayers.get(key).setBudget(this.m_settings.getnbTokensPerFlight());
			
		}
		
	}

	
	
	
	
	/**
	A chaque nouveau tours, cherche les nouveaux vols qui arrivent sur le plateau et les alloue aux AOC correspondants pour qu'ils misent des jetons dessus.
	*/
	private void allocateFlights() 
	{
		
	}

	
	
	
	
	private void getDashboards() 
	{
		// TODO Auto-generated method stub
		
	}
	

	
	
	
	/**
	 Fait avancer le temps d'un pas pr�alablement d�fini dans les Settings du jeu.
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
	
}
