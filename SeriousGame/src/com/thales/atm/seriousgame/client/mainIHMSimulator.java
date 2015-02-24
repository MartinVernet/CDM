package com.thales.atm.seriousgame.client;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class mainIHMSimulator {
	
	private Socket socket;
	private BufferedReader inFromServer;
	private BufferedReader inFromUser;
	private PrintWriter outToServer;
	//private InputStream inFromServer;
	//private OutputStream outToServer;
	
	
	public mainIHMSimulator(int port) {
		/**
		try {
		
		     socket = new Socket(InetAddress.getLocalHost(),port);
		     	System.out.println("Demande de connexion");
		     	inFromServer=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		     	String message_distant = inFromServer.readLine();
		     	System.out.println(message_distant);
		     	//socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
		*/
		
		Socket socket;
		try {
			socket = new Socket("localhost",port);
			//socket.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
		
	
	public void speakToServer() throws IOException{
		inFromUser=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Airspace File ? ");
		String airspaceFile="";
		airspaceFile = inFromUser.readLine();
		OutputStream socketOut=socket.getOutputStream();
		PrintWriter outToServer=new PrintWriter(socketOut);
		String message="ASF"+airspaceFile.getBytes().length+airspaceFile;
		outToServer.println(message);
	}
	
	
	public void closeAllSockets() throws IOException{
		try{
			socket.close();
		}
		catch(Exception e)
		{
			System.out.println("Erreur fermeture sockets client: " + e.getMessage());
		}
		
	}

}