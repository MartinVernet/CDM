package com.thales.atm.seriousgame;


import java.io.IOException;
import java.util.HashMap;
import java.util.Date;
import javax.jws.WebService;

@WebService
public class Test
{
	
	Settings m_settings=new Settings();
	Game m_game=new Game(m_settings);
	
	public int add ( int a, int b )
	{
		return a + b;
	}

	public static void main(String[] args) throws IOException{
		
		Settings m_settings=new Settings();
		Game m_game=new Game(m_settings);
		//map m=new map(  "C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airblock.gar","C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Sector.gsl","C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airspace.spc");
		m_game.initiateNewGame();
		
	}

}
