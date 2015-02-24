package com.thales.atm.seriousgame;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;
import com.thales.atm.seriousgame.flightparser.FlightPlanParser;

import javax.jws.WebService;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.Settings;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebService

public class Game {
	
	private Settings m_settings;
	private int m_turn;
	private Map<Integer,AOC> AOCplayers;
	private Map<Integer,FMP> FMPplayers;
	private Map<String,Integer> AirspaceToFMP;
	private Date currentDate;
	private map m_board;
	
	public Game(Settings settings){
		this.m_settings=settings;
		this.m_turn=0;
		this.AOCplayers=new HashMap<Integer, AOC>();
		this.FMPplayers=new HashMap<Integer, FMP>();
	}
	
	public void setSettings(Settings settings){
		m_settings=settings;
	}
	
	
	public void getFinalMapsOfPlayers(){
		
		ArrayList<Player> L=m_settings.getPlayersList();
		for (int i=0;i<L.size();i++){
			if (L.get(i).getType()=="AOC"){
				AOCplayers.put(L.get(i).getID(), (AOC) L.get(i));
			}
			else{
				FMPplayers.put(L.get(i).getID(), (FMP) L.get(i));
			}
		}
	}
	
	
	/**
    Crée les airspaces, sectors et airblocks à partir des fichiers donnés dans les settings.
	*/
	public void loadAirspace(){
		this.m_board = new map(this.m_settings.getAirBlockFile(),this.m_settings.getSectorFile(),this.m_settings.getAirspaceFile());
	}
	
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
		chosenAirSpace.add("GCCCCTA");
		chosenAirSpace.add("GMMMCTA");
		chosenAirSpace.add("GOOOCTA");
		
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
		//Flight plan
		FlightPlanParser read = new FlightPlanParser();
	    List<FlightPlan> parseFlightPlan = read.parseFlightPlan("PlansDeVol.xml", m_board.m_sectorDictionary);
	    	//Tests
	    for (FlightPlan flight : parseFlightPlan) {
	      System.out.println(flight);
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
					ArrayList<AirSpace> airspaces=new ArrayList<AirSpace>();
					System.out.println("Airspace name ?");
					airspaceID=br.readLine();
					airspaces.add(m_board.m_airSpaceDictionary.get(airspaceID));
					System.out.println("Add another airspace ? (Y/N)");
					String keep;
					keep=br.readLine();
					while(keep.equals("Y"))
					{
						System.out.println("Airspace name ?");
						airspaceID=br.readLine();
						airspaces.add(m_board.m_airSpaceDictionary.get(airspaceID));
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
	 * Lance le jeu, une fois que les settings ont étés définis
	 */
	public void launchGame(){
		
		//verify that the game is OK
		while (this.m_settings.checkIfReady()!=true){
			this.m_settings.solveProblems();
		}
		
		this.getFinalMapsOfPlayers();
		//this.loadAirspace();
		//this.m_board.reduceMap(null);//replace null by  selected airSpace from IHM
		
		//Main loop of the game
		while (this.isFinished()==false){
			
			this.m_turn++;
			this.startNewTurn();
		}
	}
	
	
	
	/**
	Vérifie si une condition d'arrêt du jeu est remplie.
	@return True si le jeu se termine, False sinon
	*/
	private boolean isFinished() {
		if(m_turn>= m_settings.getNbMaxTurn())
		{
			return true;
		}
		return false;
	}

	
	public void startNewTurn(){
		
		this.allocateFlights();
		
		this.allocateTokens();//
		
		this.startAOCTurn();//AOC allocates their tokens to their flights
		
		this.startFMPTurn();//FMP regulates their sectors
		
		this.getDashboards();//Display metrics of the last turn
	}
	
	
	
	
	
	/**
	 * Alloue un budget de jetons par AOC, en fonction d'un nombre de jeton par vol, défini dans les settings.
	*/
	private void allocateTokens() {
		
		for ( int key : AOCplayers.keySet() ){
			
			AOCplayers.get(key).setBudget(this.m_settings.getnbTokensPerFlight());
			
		}
		
	}

	
	
	
	
	/**
	A chaque nouveau tours, cherche les nouveaux vols qui arrivent sur le plateau et les alloue aux AOC correspondants pour qu'ils misent des jetons dessus.
	*/
	private void allocateFlights() {
		
	}

	
	
	
	
	private void getDashboards() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	/**
	 Le temps est mis en pause et chaque AOC peut miser ses jetons sur ses vols.
	 */
	private void startAOCTurn() {
		for ( int key : AOCplayers.keySet() ){
			AOCplayers.get(key).play();
		}
		
	}
	
	
	
	
	/**
	 Après le tour des AOC, le temps avance et à chaque besoin de régulation, les FMP interviennent sur leurs secteurs.
	 */
	private void startFMPTurn() {
		while (this.endOfTurn()==false){
			for ( int key : FMPplayers.keySet() ){
				FMPplayers.get(key).play();
			}
			this.forwardDate();
		}
		
	}
	
	
	
	/**
	 Fait avancer le temps d'un pas préalablement défini dans les Settings du jeu.
	 */
	private void forwardDate(){
		
	}
	
	private boolean endOfTurn(){
		return false;
	}
	
	
	
}
