package com.thales.atm.seriousgame.communications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.thales.atm.seriousgame.AOC;
import com.thales.atm.seriousgame.FMP;
import com.thales.atm.seriousgame.Game;
import com.thales.atm.seriousgame.Settings;

public class SettingsGetter implements Runnable{
	
	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private String id = "mainIHM";
	private Game game;
	public Thread t2;
	
	private boolean ASFok=false;//test airspace
	private boolean SCFok=false;//test sector
	private boolean ABFok=false;//test airblock
	private boolean PLYok=false;//test player
	private boolean LVLok=false;//test level

	public boolean settingsOK = false;
	

	
	public SettingsGetter(Socket s, String id, Game game){
		 socket = s;
		 this.id=id;
		 this.game=game;
		}
	
	public void run() {
	
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			out.println("SET");
			out.flush();
		
		Integer nbPlayers=null;
		
		while(!settingsOK){
			
			String message = in.readLine();
			String delims = "[$]";
			System.out.println("Main IHM "+id+" dit "+message);
			String[] messages = message.split(delims);
			
			
			if (messages[0].equals("ASF")){
				game.getSettings().setAirspaceFile(messages[1]);
				System.out.println("ASF set to "+game.getSettings().getAirspaceFile());
				ASFok=game.getSettings().AirspaceFileExists();
			}
			
			else if (messages[0].equals("SCF")){
				game.getSettings().setSectorFile(messages[1]);
				System.out.println("SCF set to "+game.getSettings().getSectorFile());
				SCFok=game.getSettings().SectorFileExists();

			}
			
			else if (messages[0].equals("ABF")){
				game.getSettings().setAirblockFile(messages[1]);
				System.out.println("ABF set to "+game.getSettings().getAirBlockFile());
				ABFok=game.getSettings().AirBlockFileExists();
			}
			
			else if (messages[0].equals("NBP")){
				nbPlayers=Integer.parseInt(messages[1]);
			}
			
			else if (messages[0].equals("LVL")){
				
				game.getSettings().setLevel(Integer.parseInt(messages[1]));
				System.out.println("Level set to "+game.getSettings().getLevel());
				LVLok=true;
			}
			
			
			else if (messages[0].equals("PLY")){
				
				if (ASFok && SCFok && ABFok){
					
					game.loadAirspace();
					
					if (messages[1].equals("AOC")){
						
						AOC player = new AOC(messages[3],Integer.parseInt(messages[2]));
						
						if (player.isOK()){
							game.addAOCPlayer(player);
						}
					}
					
					else if (messages[1].equals("FMP")){
						
						int id = Integer.parseInt(messages[2]);
						String name = messages[3];
						int nbAirspace = Integer.parseInt(messages[4]);
						ArrayList<String> playerAirspaces = new ArrayList<String>();
						//besoin de construire la map avant pour vérifier l'existence des airspaces
						int i=1;
						while (i<=nbAirspace){
							String newAirspaceName = messages[4+i];
							playerAirspaces.add(newAirspaceName);
							i+=1;
						}
						
						FMP player=new FMP(name,id,playerAirspaces);
						if (player.isOK()){
							game.addFMPPlayer(player);
							player.setAirspaces(game.getBoard());
						}
					}
					
					else{
						
					}
				}
				
				
				PLYok = (nbPlayers==game.getSettings().getNbPlayers());
			}

			settingsOK = ASFok && SCFok && ABFok && PLYok && LVLok;
			
		 }
			
			game.getSettings().setToOK();
			System.out.println(game.getSettings().returnSettingsInfos());
			 
			t2 = new Thread(new CommunicationClientServer(socket,id));
			t2.start();
			
			
			
		} catch (IOException e) {
			
			System.err.println(id+" ne répond pas !");
		}
	}
}
