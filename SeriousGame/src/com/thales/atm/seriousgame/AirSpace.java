package com.thales.atm.seriousgame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jws.WebService;

@WebService
public class AirSpace {
	
	private String m_name;
	ArrayList<Sector> m_sector;

	public AirSpace(String name, ArrayList<Sector> sect)
	{
		this.setName(name);
		this.m_sector=sect;
	}
	public AirSpace()
	{
			this.setName("test");
			this.m_sector=null;
	}
	public String getName() {
		return m_name;
	}
	public void setName(String m_name) {
		this.m_name = m_name;
	}
		
		//creation du dictionnaire des airblocks;
		

}
