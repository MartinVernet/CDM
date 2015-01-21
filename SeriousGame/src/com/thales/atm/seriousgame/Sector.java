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
	ArrayList<String> listOfFullAirb;

	private String m_fatherId;
	

	/*public Sector(String name, ArrayList<AirBlock> airb)
	{
		this.m_name=name;
		this.m_airBlocks=airb;
	}*/
	public Sector(String name, ArrayList<String> airb)
	{
		this.m_name=name;
		this.m_airBlocksId=airb;
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

		
}
