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
	
	String m_name;
	ArrayList<Sector> m_sector;

	public AirSpace(String name, ArrayList<Sector> sect)
	{
		this.m_name=name;
		this.m_sector=sect;
	}
	public AirSpace()
	{
			this.m_name="test";
			this.m_sector=null;
	}
		
		//creation du dictionnaire des airblocks;
		

}
