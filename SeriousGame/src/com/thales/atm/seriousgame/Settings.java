package com.thales.atm.seriousgame;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import com.thales.atm.seriousgame.communications.CommunicationMainIHM;

public class Settings {
	
	private ArrayList<Player> m_players;
	private String m_airspaceFile;
	private String m_sectorFile;
	private String m_airblockFile;
	private int m_level;
	private int nbTokensPerFlight;
	private int m_delta;
	private int m_turnLength;
	private int m_nbMaxTurn;
	private Date initDate;
	private boolean settingsOK=false;
	
	private int nbPlayers;
	
	public Settings(){
		this.m_players=new ArrayList<Player>();
		this.m_airspaceFile="";
		this.m_sectorFile="";
		this.m_airblockFile="";
		this.m_level=0;
		this.nbPlayers=0;
	}
	
	
	/**
	 * get settings from main IHM
	 * @throws Exception
	 */
	public void getSettings(CommunicationMainIHM mainClient) throws Exception{
		
		byte[] trame = new byte[3];
        int nbBytesLus = mainClient.read(trame, 3);
        if (nbBytesLus != 3) {
            throw new Exception("Erreur de lecture de l'entete de trame");
        } 
        String typeTrame = new String(trame, "ASCII");
        
        // ASF : AirspaceFile
        if (typeTrame.equalsIgnoreCase("ASF")) {
            trame = new byte[1];
            nbBytesLus = mainClient.read(trame, 1);
            if (nbBytesLus != 1) {
                throw new Exception("Erreur de lecture de la trame ASF");
            }
            
            int nbBytesToRead=(int) trame[0];
            trame = new byte[nbBytesToRead];
            nbBytesLus = mainClient.read(trame, nbBytesToRead);
            
            this.setAirspaceFile(new String(trame));
        }
        
        // SEF : SectorFile
        if (typeTrame.equalsIgnoreCase("SEF")) {
            trame = new byte[1];
            nbBytesLus = mainClient.read(trame, 1);
            if (nbBytesLus != 1) {
                throw new Exception("Erreur de lecture de la trame SEF");
            }
            
            int nbBytesToRead=(int) trame[0];
            trame = new byte[nbBytesToRead];
            nbBytesLus = mainClient.read(trame, nbBytesToRead);
            
            this.setSectorFile(new String(trame));
        }
        
        // ABF : AirblockFile
        if (typeTrame.equalsIgnoreCase("ABF")) {
            trame = new byte[1];
            nbBytesLus = mainClient.read(trame, 1);
            if (nbBytesLus != 1) {
                throw new Exception("Erreur de lecture de la trame ABF");
            }
            
            int nbBytesToRead=(int) trame[0];
            trame = new byte[nbBytesToRead];
            nbBytesLus = mainClient.read(trame, nbBytesToRead);
            
            this.setAirblockFile(new String(trame));;
        }
        
        // LVL : Level of the game
        if (typeTrame.equalsIgnoreCase("LVL")) {
            trame = new byte[1];
            nbBytesLus = mainClient.read(trame, 1);
            if (nbBytesLus != 1) {
                throw new Exception("Erreur de lecture de la trame LVL");
            }
            this.setLevel((int) trame[0]);
        }
        
        // NBT: nb tokens per flights
        if (typeTrame.equalsIgnoreCase("NBT")) {
            trame = new byte[1];
            nbBytesLus = mainClient.read(trame, 1);
            if (nbBytesLus != 1) {
                throw new Exception("Erreur de lecture de la trame NBT");
            }
            this.setNbTokensPerFlights((int) trame[0]);
        }       
	}
	
	
	
	
	
	public String getAirspaceFile(){
		return this.m_airspaceFile;
	}
	
	public String getSectorFile(){
		return this.m_sectorFile;
	}
	
	public String getAirBlockFile(){
		return this.m_airblockFile;
	}
	
	public void addPlayer(Player player){
		
		m_players.add(player);
		nbPlayers+=1;
	}
	
	public ArrayList<Player> getPlayersList(){
		return this.m_players;
	}
	
	public void setAirspaceFile(String airspaceFile){
		m_airspaceFile=airspaceFile;
	}
	
	public void setSectorFile(String sectorFile){
		m_sectorFile=sectorFile;
	}
	
	public void setAirblockFile(String airblockFile){
		m_airblockFile=airblockFile;
	}
	
	public void setLevel(int level){
		m_level=level;
	}
	
	public String returnSettingsInfos(){
		String mySettings="My game infos: \n";
		if (m_players.size()>1){
			mySettings+="There are "+m_players.size() +" players: ";
			for (int i=0;i<m_players.size();i++){
				mySettings+=m_players.get(i).getInfos()+"\n";
			}
		}
		else if (m_players.size()==1){
			mySettings+="There is 1 player :";
			mySettings+=m_players.get(0).getInfos()+"\n";
		}
		else{
			mySettings+="There is no player :";
		}
		mySettings+="The chosen files are "+m_airspaceFile+", "+m_sectorFile+" and "+m_airblockFile+"\n";
		mySettings+="The chosen level is "+m_level+"\n";	
		return mySettings;
	}

	public boolean checkIfReady() {
		// TODO Auto-generated method stub
		return true;
	}

	public void solveProblems() {
		// TODO Auto-generated method stub
		
	}

	public int getnbTokensPerFlight() {
		// TODO Auto-generated method stub
		return this.nbTokensPerFlight;
	}
	
	public void setNbTokensPerFlights(int nbTokens){
		nbTokensPerFlight=nbTokens;
	}

	public int getDelta() {
		return m_delta;
	}

	public void setDelta(int delta) {
		this.m_delta = delta;
	}

	public int getTurnLength() {
		return m_turnLength;
	}

	public void setTurnLength(int turnLength) {
		this.m_turnLength = turnLength;
	}

	public int getNbMaxTurn() {
		return m_nbMaxTurn;
	}

	public void setNbMaxTurn(int m_nbMaxTurn) {
		this.m_nbMaxTurn = m_nbMaxTurn;
	}


	public boolean AirspaceFileExists() {
		
		if (new File(m_airspaceFile).isFile()){
			return true;
		}
		else{
			return false;
		}
		
	}


	public boolean SectorFileExists() {

		if (new File(m_sectorFile).isFile()){
			return true;
		}
		else{
			return false;
		}
	}


	public boolean AirBlockFileExists() {
		
		if (new File(m_airblockFile).isFile()){
			return true;
		}
		else{
			return false;
		}
	}

	public int getNbPlayers(){
		return nbPlayers;
	}


	public int getLevel() {
		
		return m_level;
	}


	public boolean isReady() {
		
		return settingsOK;
	}
	
	public void setToOK(){
		settingsOK=true;
	}
	
}
