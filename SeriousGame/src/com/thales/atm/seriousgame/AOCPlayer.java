package com.thales.atm.seriousgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

public class AOCPlayer extends AOC {
	
	private String AOCType;
	
	public AOCPlayer(){
		super();
		setAOCType("human");
		
	}
	
	public AOCPlayer(String name, int i) {
		
		super(name,i);
		setAOCType("human");
		
	}

	public void play() 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println(this.m_name);
		System.out.println("budget max = ");
		System.out.println(m_budget);
		int budgetRestant=m_budget;
		for ( String flightID :newFlights.keySet())
		{
			
			int p =-1;
			while ((p<0 || p>6) || budgetRestant<0)
			{
				System.out.println("########");
				System.out.println("Set priority for airline "+this.m_name);
				System.out.println("budget restant = "+budgetRestant);
				System.out.println("priorité du vol : "+flightID +" ("+newFlights.get(flightID).getFlightPlan().getAircraftType()+") "+"=");
				String priority="";
				
				try {
					priority = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				p=Integer.parseInt(priority);
				if(p<0 || p>6 || budgetRestant<0)
				{
					System.out.println("erreur dans la saisie de la priorité, /n veuillez saisir un nombre entre 0 et 5");
				}
				else
				{
				 
				 budgetRestant=budgetRestant-p;
				 
				}
			}
			newFlights.get(flightID).setPriority(p);
		}
	}

	public String getAOCType() {
		return AOCType;
	}

	public void setAOCType(String aOCType) {
		AOCType = aOCType;
	}
	


}
