package com.thales.atm.seriousgame.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Reception implements Runnable {

	private BufferedReader in;
	private PrintWriter out;
	private String message = null;
	private String id;
	private Thread runningCommunicationThread= new Thread();
	
	public Reception(BufferedReader in,PrintWriter out, String id){
		this.out=out;
		this.in = in;
		this.id=id;
		
	}
	
	public void run() {
		
		while(true){
	        try {
	        	
			message = in.readLine();
			
			if (message!=null){
				
				System.out.println("Le serveur vous dit : " +message);
				if (message.equals("AUT")){
					Message id = new Message(out,"Please enter your ID : ","ID?");
					Message type = new Message(out,"Please enter your type : ","TYP");
				}
				else if (message.equals("SET")){
					runningCommunicationThread.interrupt();
					System.out.println("Le serveur attend les settings");
					Thread tset;
					SettingsInterface set = new SettingsInterface(out,in);
					set.run();
					//tset = new Thread(new SettingsInterface(out,in));
					//tset.start();
					//runningCommunicationThread=tset;
				}
				

			}
			
		    } catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

}
