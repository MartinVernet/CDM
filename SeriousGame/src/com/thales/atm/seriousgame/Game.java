package com.thales.atm.seriousgame;

import javax.jws.WebService;

import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.Settings;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;


@WebService
public class Game {
	
	private static Settings m_settings;
	
	public Game(Settings settings){
		m_settings=settings;
	}
	
	public void changeSettings(Settings settings){
		m_settings=settings;
	}
	
	public void initiateNewGame() throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Players creation
		String addNewPlayer="Y"; 
		int i=1;
		while (addNewPlayer=="Y"){
			System.out.println("Player "+i);
			System.out.println("Name of player "+i+" ");
			String name = br.readLine();
			Player P = new Player(name,i);
			m_settings.addPlayer(P);
			i+=1;
			System.out.println("Do you want to add a new player (Y/N) ? ");
			addNewPlayer = br.readLine();
		}
				
		//Files selection
		System.out.println("Airspace File ? ");
		String airspaceFile = br.readLine();
		m_settings.setAirspaceFile(airspaceFile);
		
		System.out.println("Sector File ? ");
		String sectorFile = br.readLine();
		m_settings.setSectorFile(sectorFile);
		
		System.out.println("Airblock File ? ");
		String airblockFile = br.readLine();
		m_settings.setAirblockFile(airblockFile);
		
		
		//Level choice
		System.out.println("Enter level: ");
		int level = Integer.parseInt(br.readLine());
		m_settings.setLevel(level);
		        
		//Summary
		System.out.println(m_settings.returnSettingsInfos());
	}
}
