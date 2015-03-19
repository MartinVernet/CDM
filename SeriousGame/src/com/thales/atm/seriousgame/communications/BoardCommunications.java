package com.thales.atm.seriousgame.communications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BoardCommunications {
	
	
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	
	
	public BoardCommunications(Socket socket){
		this.socket=socket;
		try {
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String key,String message){
		out.println(key+"$"+message);
		out.flush();
	}
	
	public void sendReduceMap(ArrayList<String> chosenAirspaces){
		String key = "RDM";
		String message = "";
		for (String asp : chosenAirspaces){
			message+="$"+asp;
		}
		out.println(key+message);
		out.flush();
	}
	
	public void sendNewTurn(int nbTurn){
		String key = "NWT";//New Turn
		String message = "$"+nbTurn;
		out.println(key+message);
		out.flush();
	}
	
	public void sendCurrentDate(Date date){
		String key = "NWD";//New Date
		String message = "$";
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int year = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH);
	    int day = cal.get(Calendar.DAY_OF_MONTH);
	    int hour = cal.get(Calendar.HOUR_OF_DAY);
	    int min = cal.get(Calendar.MINUTE);
	    message+=year+"/"+month+"/"+day+"/"+hour+"/"+min;
		out.println(key+message);
		out.flush();
	}
	
	public void sendGameOver(){
		out.println("GMO");
		out.flush();
	}
	
	public void sendMoves(){
		String key = "MVS";
		String message = "$";
	}
	
}
