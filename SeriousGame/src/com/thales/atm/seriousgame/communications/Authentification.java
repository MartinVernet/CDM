package com.thales.atm.seriousgame.communications;

import java.net.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

import com.thales.atm.seriousgame.Game;
import com.thales.atm.seriousgame.Player;
import com.thales.atm.seriousgame.Settings;

public class Authentification implements Runnable {

	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private String id = null;
	private String type = null;
	public static HashSet<String> availableTypes 
					= new HashSet<String>(Arrays.asList("Board", "FMP", "AOC"));

	private Game game;
	public boolean authentifier = false;
	public Thread t2;
	public ConcurrentHashMap<String,Socket> clientsMap;//should be thread safe
	
	
	public Authentification(Socket s,ConcurrentHashMap<String,Socket> clientsMap, Game game){
		 socket = s;
		 this.clientsMap = clientsMap;
		 this.game = game;
		}
	
	public void run() {
	
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			out.println("AUT");
			out.flush();
			
		while(!authentifier){
			
			String message = in.readLine();
			System.out.println(message);
			String delims = "[$]";
			String[] messages = message.split(delims);
			
			if (messages[0].equals("ID?")){
				id = messages[1];
			}
			
			else if (messages[0].equals("TYP")){
				type = messages[1];
			}

			if(isValid(id,type)){
				
				if (type.equals("Board")){
					out.println("connecté");
					System.out.println(id +" ( "+type+" ) "+" vient de se connecter ");
					out.flush();
					authentifier = true;
					game.BoardMap.putIfAbsent(id, socket);
				}
				else if (type.equals("AOC") || type.equals("FMP")){
					if (game.getSettings().isReady()){
						out.println("connecté");
						System.out.println(id +" ( "+type+" ) "+" vient de se connecter ");
						out.flush();
						authentifier = true;
						game.PlayerMap.putIfAbsent(id, socket);
					}
					else{
						System.out.println("Please first connect the main IHM and define game settings");
					}
				}
				else{
					out.println("Unknown error");
					out.flush();
					authentifier = false;
				}
			}
			else {out.println("ID or Type not valid"); out.flush();}
		 }
		//System.out.println("Authentification done");
			//t2 = new Thread(new CommunicationClientServer(socket,id));
			//t2.start()

		
		if (type.equals("Board")){
			System.out.println("Asking for settings");
			t2 = new Thread(new SettingsGetter(socket,id,game));
			t2.start();
		}
		
		else if (type.equals("AOC")||type.equals("FMP")){
			if(clientsMap.keySet().size()==game.getSettings().getNbPlayers()){
				game.launchGame();
			}
			else{
				for (String player:game.getAOCplayersDict().keySet()){
					if (clientsMap.get(player) == null){
						System.out.println("Waiting for "+player+" to connect");
					}
				}
				for (String player:game.getFMPplayersDict().keySet()){
					if (clientsMap.get(player) == null){
						System.out.println("Waiting for "+player+" to connect");
					}
				}
			}
		}
		
			
		} catch (IOException e) {
			
			System.err.println(this.id+" ne répond pas !");
		}
	}
	
	private boolean isValid(String login,String type) {
		
		if (login == null || type == null){
			return false;
		}
		
		else{
			if (type.equals("Board")){
				if (game.BoardMap.keySet().size()==0){
					return true;
				}
				else{
					out.println("Board déjà connecté, il ne peut y en avoir qu'un seul");
					out.flush();
					return false;
				}
			}
			else if (type.equals("AOC")){
				if(game.PlayerMap.get(login)==null){
					if (game.getAOCplayersDict().get(login)==null){
						out.println("L'id du client ne correspond à aucun player");
						out.flush();
						return false;
					}
					else{
						return true;
					}
				}
				else{
					out.println("Un client s'est déjà connecté avec le même id");
					out.flush();
					return false;
				}
			}
			else if (type.equals("FMP")){
				if(game.PlayerMap.get(login)==null){
					if (game.getFMPplayersDict().get(login)==null){
						out.println("L'id du client ne correspond à aucun player");
						out.flush();
						return false;
					}
					else{
						return true;
					}
				}
				else{
					out.println("Un client s'est déjà connecté avec le même id");
					out.flush();
					return false;
				}
			}
			else{
				return false;
			}
		}
		
	}

}
