package com.thales.atm.seriousgame;

import javax.jws.WebService;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.Settings;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@WebService

public class Game {
	
	private Settings m_settings;
	private int m_turn;
	private Map<Integer,AOC> AOCplayers;
	private Map<Integer,FMP> FMPplayers;
	private Date currentDate;
	//private Airspace airspace;
	
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
		
	}
	
	public void initiateNewGame() throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Players creation
		String addNewPlayer="Y"; 
		int i=1;
		while (addNewPlayer=="Y"){
			System.out.println("Player "+i);
			System.out.println("Name of player "+i+" ");
			String name = br.readLine();
			String type="none";
			while (type!="AOC" && type!="FMP")
				System.out.println("Type of player: (AOC/FMP)");
				type=br.readLine();
				if (type=="AOC"){
					AOC P = new AOC(name,i);
					m_settings.addPlayer(P);
				}
				if(type=="FMP"){
					FMP P = new FMP(name,i);
					m_settings.addPlayer(P);
				}
			i+=1;
			System.out.println("Do you want to add a new player (Y/N) ? ");
			addNewPlayer = br.readLine();
		}
				
		//Files selection
		System.out.println("Airspace File ? ");
		String airspaceFile = br.readLine();
		m_settings.setAirspaceFile(airspaceFile);
		
		System.out.println("Sector File ? ");
		String sectorFile = br.readLine();
		m_settings.setSectorFile(sectorFile);
		
		System.out.println("Airblock File ? ");
		String airblockFile = br.readLine();
		m_settings.setAirblockFile(airblockFile);
		
		
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
		this.loadAirspace();
		
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
		// check if the game is finished or not
		return false;
	}

	public void startNewTurn(){
		
		this.allocateFlights();
		
		this.allocateTokens();
		
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
		// TODO Auto-generated method stub
		
		for ( int key : AOCplayers.keySet() ){
			AOCplayers.get(key).play();
		}
		
	}
	
	/**
	 Après le tour des AOC, le temps avance et à chaque besoin de régulation, les FMP interviennent sur leurs secteurs.
	 */
	private void startFMPTurn() {
			
		
	}
	
	/**
	 Fait avancer le temps d'un pas préalablement défini dans les Settings du jeu.
	 */
	private void forwardDate(){
		
	}
	
	


	
	
}
