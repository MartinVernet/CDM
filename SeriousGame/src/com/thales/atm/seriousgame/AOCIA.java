package com.thales.atm.seriousgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

public class AOCIA extends AOC {
	
	private String AOCType;
	
	public AOCIA(){
		super();
		setAOCType("IA");
		
	}
	
	public AOCIA(String name, int i) {
		
		super(name,i);
		setAOCType("IA");
		
	}

	public void play() 
	{
		int budgetRestant= m_budget;
		for ( String flightID :newFlights.keySet())
		{
			int max =Math.min(budgetRestant,5);
			Random rand =new Random();
			int random = (int)(Math.random() * (max)) ;
			int priority = rand.nextInt(max+1);
			newFlights.get(flightID).setPriority(priority);
			budgetRestant=budgetRestant-priority;
		}
	}
	
	public String getAOCType() {
		return AOCType;
	}

	public void setAOCType(String aOCType) {
		AOCType = aOCType;
	}
	


}
