package com.thales.atm.seriousgame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.jws.WebService;

@WebService
public class AirSpace {
	
	private String m_name;
	//ArrayList<Sector> m_sector;
	ArrayList<String> m_sectorId;
	private boolean m_needRegulation;
	Set<String> listOfFullAirb; //Attention not multiThread Safe

	public AirSpace(String name, ArrayList<String> sect)
	{
		this.setName(name);
		this.m_sectorId=sect;
		this.listOfFullAirb =new HashSet<String>();
	}
	public AirSpace()
	{
			this.setName("test");
			this.m_sectorId=null;
			this.listOfFullAirb =new HashSet<String>();
	}
	public String getName() {
		return m_name;
	}
	public void setName(String m_name) {
		this.m_name = m_name;
	}
	public boolean getNeedRegulation() {
		return m_needRegulation;
	}
	public void setNeedRegulation(boolean m_needRegulation) {
		this.m_needRegulation = m_needRegulation;
	}
	
	public void addFullAirb(String airbId)
	{
		//verifier s'il n'est pas deja dedans
		listOfFullAirb.add(airbId);
	}
	public void removeFull(String airbId)
	{
		//a optimiser
		listOfFullAirb.remove(airbId);
	}
		

}
