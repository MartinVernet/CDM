package com.thales.atm.seriousgame;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.jws.WebService;

@WebService

public class Sector {
	String m_name;
	//ArrayList<AirBlock> m_airBlocks;
	ArrayList<String> m_airBlocksId;
	int m_capacity;
	int m_occupation;
	Set<String> listOfFullAirb; //Attention not multiThread Safe
	private Set<String> neighbors; //Attention not multiThread Safe
	private String m_fatherId;
	
	private AirSpace m_father;
	

	/*public Sector(String name, ArrayList<AirBlock> airb)
	{
		this.m_name=name;
		this.m_airBlocks=airb;
	}*/
	public Sector(String name, ArrayList<String> airb)
	{
		this.m_name=name;
		this.m_airBlocksId=airb;
		this.listOfFullAirb =new HashSet<String>();
		this.neighbors=new HashSet<String>();
	}
	public Sector()
	{
		this.m_name="test";
		this.m_airBlocksId=null;
	}

	public String getFatherId() {
		return m_fatherId;
	}
	public void setFatherId(String m_fatherId) {
		this.m_fatherId = m_fatherId;
	}
	public AirSpace getFather() {
		return m_father;
	}
	public void setFather(AirSpace m_father) {
		this.m_father = m_father;
	}

	public void addFullAirb(String airbId)
	{
		//verifier s'il n'est pas deja dedans
		//listOfFullAirb.add(airbId);
		m_father.addFullAirb(airbId);
	}
	
	public void removeFull(String airbId)
	{
		//a optimiser
		m_father.removeFull(airbId);
		//listOfFullAirb.remove(airbId);
	}
	/**
	 * @return the neighbors
	 */
	public Set<String> getNeighbors() {
		return neighbors;
	}
	/**
	 * @param neighbors the neighbors to set
	 */
	public void setNeighbors(Set<String> neighbors) {
		this.neighbors = neighbors;
	}
		
}
