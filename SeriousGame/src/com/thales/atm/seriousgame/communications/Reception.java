package com.thales.atm.seriousgame.communications;

import java.io.BufferedReader;
import java.io.IOException;


public class Reception implements Runnable {

	private BufferedReader in;
	private String message = null;
	private String id = null;
	
	public Reception(BufferedReader in, String id){
		
		this.in = in;
		this.id = id;
	}
	
	public void run() {
		
		while(true){
	        try {
	        	
			message = in.readLine();
			System.out.println(message);
			
			
			
		    } catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

}