package com.thales.atm.seriousgame.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;


public class ConnexionToServer implements Runnable {

	private Socket socket = null;
	public static Thread t2;
	private String clientID;

	
	public ConnexionToServer(Socket s,String id){
		
		clientID=id;
		socket = s;
	}
	
	public void run() {
			
			
			
			t2 = new Thread(new CommunicationClientServer(socket,clientID));
			t2.start();

	}

}
