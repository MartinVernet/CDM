package com.thales.atm.seriousgame;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

import javax.jws.WebService;

import com.thales.atm.seriousgame.client.mainIHMSimulator;
import com.thales.atm.seriousgame.communications.CommunicationMainIHM;

@WebService
public class Test
{
	
	public int add ( int a, int b )
	{
		return a + b;
	}

	public static void main(String[] args) throws IOException{
		

		Settings m_settings=new Settings();
		Game m_game=new Game(m_settings);
		//map m=new map(  "C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airblock.gar","C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Sector.gsl","C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airspace.spc");
		//System.out.print("valid");
		//m_game.initiateNewGame();
		m_settings.setAirspaceFile("C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airspace.spc");
		m_settings.setSectorFile("C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Sector.gsl");
		m_settings.setAirblockFile("C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/AirBlock.gar");
		
		m_game.loadAirspace();
		
		FMP Arthur= new FMP("arthur",2);
		Arthur.addAirspace("GMMMCTA");
		Arthur.setAirspaces(m_game.m_board);
		
		m_game.addFMPPlayer(Arthur);
		
		//ArrayList<String> chosenAirSpace =new ArrayList<String>();
		//chosenAirSpace.add("GMMMCTA");
		//m_game.m_board.reduceMap(chosenAirSpace);
		
		m_settings.setDelta(3);
		m_settings.setTurnLength(60);//multiple
		
		m_game.loadFlightPlans();
		
		AOC jeanPaul =new AOC ("JAF",3);
		m_game.addAOCPlayer(jeanPaul);
		
		m_game.launchGame();
		
		
		
		
		
		
		

	}
	
	

}
