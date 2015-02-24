package com.thales.atm.seriousgame;


import java.io.IOException;
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
		m_game.initiateNewGame();
		m_game.launchGame();

	}
	
	

}
