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
		
		CommunicationMainIHM mainClient=new CommunicationMainIHM(0);
		mainIHMSimulator mainIHM=new mainIHMSimulator(mainClient.getPort());
		mainIHM.speakToServer();
		mainClient.listenToClient();
		//mainIHMSimulator mainIHM2=new mainIHMSimulator(2102);
		System.out.println("yo");
		//mainIHM.closeAllSockets();
		//mainClient.closeAllSockets();
		//Game m_game=new Game(2009);
		//map m=new map(  "C:/Users/Martin/Desktop/Cours 3A/Projet/Datas/map_ACC/Airblock.gar","C:/Users/Martin/Desktop/Cours 3A/Projet/Datas/map_ACC/Sector.gsl","C:/Users/Martin/Desktop/Cours 3A/Projet/Datas/map_ACC/Airspace.spc");
		//System.out.print("valid");
		//m_game.initiateNewGame();
		//m_game.launchGame();
	}
	
	

}
