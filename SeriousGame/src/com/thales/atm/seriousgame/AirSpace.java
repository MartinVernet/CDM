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
/**
 *m_name: id of the  airspace
 *m_sectorId: list of names of the sectors that are in this airspace 
 *m_needRegulation: true if this airspace cotains overloaded sectors 
 *listOfFullSector: list of sectors that are overloaded 
 */
public class AirSpace {
	
	private String m_name;
	ArrayList<String> m_sectorId;
	private boolean m_needRegulation;
	Set<Sector> listOfFullSector;

	public AirSpace(String name, ArrayList<String> sect)
	{
		this.setName(name);
		this.m_sectorId=sect;
		this.listOfFullSector =new HashSet<Sector>();
	}
	public AirSpace()
	{
			this.setName("test");
			this.m_sectorId=null;
			this.listOfFullSector =new HashSet<Sector>();
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
	public void addFullSector(Sector sectorId)
	{
		listOfFullSector.add(sectorId);
	}
	public void removeFullSector(Sector sectorId)
	{
		listOfFullSector.remove(sectorId);
	}
}
