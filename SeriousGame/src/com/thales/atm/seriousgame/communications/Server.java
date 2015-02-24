package com.thales.atm.seriousgame.communications;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.thales.atm.seriousgame.Game;
import com.thales.atm.seriousgame.Settings;

public class Server {
	public static ServerSocket ss = null;
	public static Thread t;
	public ConcurrentHashMap<String,Socket> clientsMap = new ConcurrentHashMap<String,Socket>(); 
	//public Settings settings;
	private Game game;
 
	//public static void main(String[] args) {
	public Server(){
		
		game = new Game();
		
		try {
			ss = new ServerSocket(2009);
			System.out.println("Le serveur est à l'écoute du port "+ss.getLocalPort());
			
			t = new Thread(new AcceptClients(ss,clientsMap,game));
			t.start();
			
		} catch (IOException e) {
			System.err.println("Le port "+ss.getLocalPort()+" est déjà utilisé !");
		}
	
	}

	
	}