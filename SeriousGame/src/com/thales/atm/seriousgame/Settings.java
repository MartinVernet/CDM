package com.thales.atm.seriousgame;

import java.util.ArrayList;

public class Settings {
	
	private ArrayList<Player> m_players;
	private String m_airspaceFile;
	private String m_sectorFile;
	private String m_airblockFile;
	private int m_level;
	private int nbTokensPerFlight;
	private int m_anticipation;//déclenche une alerte m_anticipation tour avant regulation
	private int m_delta;
	private int m_turnLength;
	private int m_nbMaxTurn;
	
	public Settings(){
		this.m_players=new ArrayList<Player>();
		this.m_airspaceFile="";
		this.m_sectorFile="";
		this.m_airblockFile="";
		this.m_level=0;
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

	public int getAnticipation() {
		return m_anticipation;
	}

	public void setAnticipation(int m_anticipation) {
		this.m_anticipation = m_anticipation;
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
	
	
}
