package com.thales.atm.seriousgame.communications;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.thales.atm.seriousgame.Game;
import com.thales.atm.seriousgame.Settings;

class AcceptClients implements Runnable {

	   private ServerSocket socketserver;
	   private Socket socket;
	   private int nbrclient = 1;
	   public Thread t1;
	   public ConcurrentHashMap<String,Socket> clientsMap;//should be thread safe
	   private Game game;

	   
	   public AcceptClients(ServerSocket s, ConcurrentHashMap<String,Socket> clientsMap,Game game){
			socketserver = s;
			this.clientsMap = clientsMap;
			this.game=game;
		}
		
		public void run() {

	        try {
	        	while(true){
	        		socket = socketserver.accept(); // Un client se connecte on l'accepte
	                System.out.println("Le client numéro "+nbrclient+ " est connecté !");
	                nbrclient++;
	                t1=new Thread(new Authentification(socket,clientsMap,game));
	                t1.start();
	        	}
	        
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
