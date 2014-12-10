package com.thales.atm.seriousgame;

import java.util.ArrayList;

public class Settings {
	
	private ArrayList<Player> m_players;
	private String m_airspaceFile;
	private String m_sectorFile;
	private String m_airblockFile;
	private int m_level;
	
	public Settings(){
		m_players=new ArrayList<Player>();
		m_airspaceFile="";
		m_sectorFile="";
		m_airblockFile="";
		m_level=0;
	}
	
	public void addPlayer(Player player){
		m_players.add(player);
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
}
