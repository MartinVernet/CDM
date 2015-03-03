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
	private boolean NBPok=false;

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
				ASFok=game.getSettings().AirspaceFileExists();
				if (ASFok){
					System.out.println("ASF set to "+game.getSettings().getAirspaceFile());
					out.println("ASF"+"$"+"ok");
					out.flush();
				}
				else{
					out.println("ASF"+"$"+"notok");
					out.flush();
				}
			}
			
			else if (messages[0].equals("SCF")){
				game.getSettings().setSectorFile(messages[1]);
				SCFok=game.getSettings().SectorFileExists();
				if (SCFok){
					System.out.println("SCF set to "+game.getSettings().getSectorFile());
					out.println("SCF"+"$"+"ok");
					out.flush();
				}
				else{
					out.println("SCF"+"$"+"notok");
					out.flush();
				}
			}
			
			else if (messages[0].equals("ABF")){
				game.getSettings().setAirblockFile(messages[1]);
				ABFok=game.getSettings().AirBlockFileExists();
				if (ABFok){
					System.out.println("ABF set to "+game.getSettings().getAirBlockFile());
					out.println("ABF"+"$"+"ok");
					out.flush();
				}
				else{
					out.println("ABF"+"$"+"notok");
					out.flush();
				}
			}
			
			else if (messages[0].equals("NBP")){
				nbPlayers=Integer.parseInt(messages[1]);
				NBPok=true;
				if (NBPok){
					out.println("NBP"+"$"+"ok");
					out.flush();
				}
				else{
					out.println("NBP"+"$"+"notok");
					out.flush();
				}
			}
			
			else if (messages[0].equals("LVL")){
				game.getSettings().setLevel(Integer.parseInt(messages[1]));
				LVLok=true;
				if (LVLok){
					System.out.println("Level set to "+game.getSettings().getLevel());
					out.println("LVL"+"$"+"ok");
					out.flush();
				}
				else{
					out.println("LVL"+"$"+"notok");
					out.flush();
				}
			}
			
			
			else if (messages[0].equals("PLY")){
				
				if (ASFok && SCFok && ABFok && NBPok){
					
					game.loadAirspace();
					
					if (messages[1].equals("AOC")){
						
						AOC player = new AOC(messages[2],0);
						
						if (player.isOK()){
							game.addAOCPlayer(player);
							out.println("ply"+"$"+"ok");
							out.flush();
						}
						else{
							out.println("ply"+"$"+"notok");
							out.flush();
						}
					}
					
					else if (messages[1].equals("FMP")){
						
						//int id = Integer.parseInt(messages[2]);
						String name = messages[2];
						int nbAirspace = Integer.parseInt(messages[3]);
						ArrayList<String> playerAirspaces = new ArrayList<String>();
						//besoin de construire la map avant pour vérifier l'existence des airspaces
						int i=1;
						while (i<=nbAirspace){
							String newAirspaceName = messages[3+i];
							playerAirspaces.add(newAirspaceName);
							i+=1;
						}
						
						FMP player=new FMP(name,0,playerAirspaces);
						if (player.isOK()){
							game.addFMPPlayer(player);
							player.setAirspaces(game.getBoard());
							out.println("ply"+"$"+"ok");
							out.flush();
						}
						else{
							out.println("ply"+"$"+"notok");
							out.flush();
						}
					}
					
					else{
						
					}
				}
				
				
				PLYok = (nbPlayers==game.getSettings().getNbPlayers());
				
				if(PLYok){
					out.println("PLY"+"$"+"ok");
					out.flush();
				}
				else{
					out.println("PLY"+"$"+"notok");
					out.flush();
				}
					
			}
			
			

			settingsOK = ASFok && SCFok && ABFok && PLYok && LVLok;
			
		 }
			
			game.getSettings().setToOK();
			System.out.println(game.getSettings().returnSettingsInfos());
			
			out.println("SET"+"$"+"ok");
			out.flush();
			 
			t2 = new Thread(new CommunicationClientServer(socket,id));
			t2.start();
			
			
			
		} catch (IOException e) {
			
			System.err.println(id+" ne répond pas !");
		}
	}
}
