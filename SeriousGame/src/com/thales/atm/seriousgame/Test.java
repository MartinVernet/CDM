package com.thales.atm.seriousgame;

import java.io.IOException;


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
		
		m_game.initiateNewGame();
		
	}
}
