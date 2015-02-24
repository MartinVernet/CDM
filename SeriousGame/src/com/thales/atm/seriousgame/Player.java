package com.thales.atm.seriousgame;

public abstract class Player {
	
	String m_name;
	int m_id;
	public static int nbInstances=0;
	
	public Player(){
		this.m_name="Player"+Integer.toString(nbInstances);
		this.m_id=nbInstances;
	}
	
	
	public Player(String name,int id){
		this.m_name=name;
		this.m_id=id;
		nbInstances++;
	}
	
	
	public String getInfos(){
		return "Name: "+m_name+" ; "+"ID: "+m_id;
	}
	
	public int getID(){
		return this.m_id;
	}
	
	public String getType(){
		return "none";
	}
	
	public void play(){
		
	}
	
	public void setName(String name){
		this.m_name=name;
	}
	
	public void setId(int id){
		this.m_id=id;
	}


	public String getName() {
		
		return m_name;
	}
}
