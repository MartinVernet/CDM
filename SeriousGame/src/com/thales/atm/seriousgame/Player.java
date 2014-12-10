package com.thales.atm.seriousgame;

public class Player {
	
	String m_name;
	int m_id;
	
	public Player(String name,int id){
		m_name=name;
		m_id=id;
	}
	
	public String getInfos(){
		return "Name: "+m_name+" ; "+"ID: "+m_id;
	}
}
