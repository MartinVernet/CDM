package com.thales.atm.seriousgame.client;
import java.io.*;
import java.net.*;
import java.util.Scanner;


public class CommunicationClientServer implements Runnable {

	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private Scanner sc;
	private Thread t3, t4;
	private String clientID;
	
	public CommunicationClientServer(Socket s,String id){
		socket = s;
		clientID=id;
	}
	
	public void run() {
		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			sc = new Scanner(System.in);
			

			Thread t3 = new Thread(new Reception(in,out,clientID));
			t3.start();
			//Thread t4 = new Thread(new Emission(out));
			//t4.start();
		
		   
		    
		} catch (IOException e) {
			System.err.println("Le serveur distant s'est déconnecté !");
		}
	}

}
