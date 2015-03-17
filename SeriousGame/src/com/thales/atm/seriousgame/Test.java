package com.thales.atm.seriousgame;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
		System.out.print(m_game.m_board.m_sectorDictionary.get("GMMMOCE").m_name);
		
		FMPIA Arthur= new FMPIA("arthur",2,m_game.m_board);
		Arthur.addAirspace("LFFFCTA");
		Arthur.addAirspace("LFEECTA");
		Arthur.setAirspaces(m_game.m_board);
		
		m_game.addFMP(Arthur);
		
		
		m_settings.setDelta(3);
		m_settings.setTurnLength(60);//multiple
		m_settings.setLevel(1);
		
		
		m_settings.setNbMaxTurn(20);
		AOCPlayer jeanPaul =new AOCPlayer("ryan air",3);
		m_game.addAOCPlayer(jeanPaul);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(2014, 9, 15, 13, 13, 00);
		Date date = cal.getTime();
		m_game.currentDate=date;
		System.out.println(m_game.currentDate);
		m_game.launchGame();
		
		
		
		
		
		
		

	}
	
	

}
