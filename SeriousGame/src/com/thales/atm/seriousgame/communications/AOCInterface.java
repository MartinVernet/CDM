package com.thales.atm.seriousgame.communications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.thales.atm.seriousgame.AOC;
import com.thales.atm.seriousgame.Flight;

public class AOCInterface implements Runnable{
	
	private Socket socket;
	private AOC player;
	private BufferedReader in;
	private PrintWriter out;
	
	public AOCInterface(Socket socket,AOC player){
		this.socket=socket;
		this.player=player;
		try {
			this.in=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.out=new PrintWriter(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run(){
		
		boolean ready = false;
		while(!ready){
			
			String message;
			
			try {
				
				message = in.readLine();
				String delims = "[$]";
				String[] messages = message.split(delims);
				
				if (messages[0].equals("RDY")){
					ready=true;
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	public void sendDashboard(){
		
	}
	
	public void sendBudget(){
		out.print("BDG"+"$"+player.getBudget());
		out.flush();
	}
	
	public void addFlight(Flight flight){
		out.println("FLT"+"$"+flight.getFlightID()+"$"+flight.getEconomicValue()+"$"+flight.getPriority()+"$"+flight.getCurrentSector().m_name);
		out.flush();
	}
	
	public void refreshFlightPosition(Flight flight, String newSector){
		out.println("POS"+"$"+flight.getFlightID()+"$"+newSector);
		out.flush();
	}
	
	public void regulateFlight(Flight flight){
		out.println("RGL"+"$"+flight.getFlightID());//indiquer le retard
		out.flush();
	}
	
	public void removeFlight(Flight flight){
		out.println("RMV"+"$"+flight.getFlightID());
		out.flush();
	}

}
