package com.thales.atm.seriousgame.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.thales.atm.seriousgame.AOC;
import com.thales.atm.seriousgame.FMPPlayer;

public class SettingsInterface{
	
	
	private PrintWriter out;
	private Scanner sc = null;
	private BufferedReader in;
	private boolean settingsOK = false;
	BufferedReader br;
	
	public SettingsInterface(PrintWriter out,BufferedReader in) {
		this.in = in;
		this.out = out;
		this.br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void run(){
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		

		try{
			String message = "";
			
			//Airspace file
			while (!(message.equals("ASF$ok"))){
				System.out.println("ASF");
				sendASF();
				message = in.readLine();
			}
			
			//Sector file
			while (!message.equals("SCF$ok")){
				System.out.println("SCF");
				sendSCF();
				message = in.readLine();
			}
			
			//Airblock file
			while (!message.equals("ABF$ok")){
				System.out.println("ABF");
				sendABF();
				message = in.readLine();
			}
				
			//Level
			while (!message.equals("LVL$ok")){
				sendLVL();
				message = in.readLine();
			}
			
			//Delta
			while (!message.equals("DLT$ok")){
				sendDLT();
				message = in.readLine();
			}
			
			//Turn length
			while (!message.equals("TRN$ok")){
				sendTRN();
				message = in.readLine();
			}
			
			//Nb max trun
			while (!message.equals("NBT$ok")){
				sendNBT();
				message = in.readLine();
			}
			
			//Initial date
			while (!message.equals("IDT$ok")){
				sendIDT();
				message = in.readLine();
			}
			
			while (!message.equals("PLY$ok")){
				while(!message.equals("ply$ok"))
					sendNewPlayer();
					message = in.readLine();
			}
				
			
			}
			catch(IOException e){
				
			}
		
	}
	
	private void sendIDT() {
		
		System.out.println("Initial Date ? ");
		String Date;
		try {
			System.out.println("Year/Month/Day/Hour/Minutes ? ");
			Date = br.readLine();
			out.println("IDT"+"$"+Date);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private void sendNBT() {
		
		System.out.println("Nb turn ? ");
		String NbTurn;
		try {
			NbTurn = br.readLine();
			out.println("NBT"+"$"+NbTurn);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void sendTRN() {
		
		System.out.println("Turn Length (nb minutes) ? ");
		String length;
		try {
			length = br.readLine();
			out.println("TRN"+"$"+length);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void sendDLT() {
		
		System.out.println("Delta (nb minutes) ? ");
		String delta;
		try {
			delta = br.readLine();
			out.println("DLT"+"$"+delta);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void sendASF(){
		System.out.println("Airspace File ? ");
		String airspaceFile;
		try {
			airspaceFile = br.readLine();
			out.println("ASF"+"$"+airspaceFile);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendSCF(){
		System.out.println("Sector File ? ");
		String sectorFile;
		try {
			sectorFile = br.readLine();
			out.println("SCF"+"$"+sectorFile);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendABF(){
		System.out.println("Airblock File ? ");
		String airblockFile;
		try {
			airblockFile = br.readLine();
			out.println("ABF"+"$"+airblockFile);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendLVL(){
		System.out.println("Level ? ");
		String level;
		try {
			level = br.readLine();
			out.println("LVL"+"$"+level);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendNBP(){
		System.out.println("Number of players ? ");
		String nbp;
		try {
			nbp = br.readLine();
			out.println("NBP"+"$"+nbp);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendNewPlayer(){
		String messagePlayer= "PLY";
		System.out.println("Name of player : ");
		String name;
		try {
			name = br.readLine();
		String type="none";
		while (!(type.equals("AOC") || type.equals("FMP")))
		{
			System.out.println("Type of player: (AOC/FMP)");
			type=br.readLine();
			//System.out.println("Id of player");
			//int id = Integer.parseInt(br.readLine());
			if (type.equals("AOC"))
			{
				messagePlayer+="$"+"AOC";
				//messagePlayer+="$"+id;
				messagePlayer+="$"+name;
			}
			if(type.equals("FMP"))
			{
				messagePlayer+="$"+"FMP";
				messagePlayer+="$"+name;
				
				String messageAirspaces="";
				String airspaceID="";
				
				System.out.println("Airspace name ?");
				airspaceID=br.readLine();
				
				messageAirspaces+="$"+airspaceID;
				int nbPlayers = 1;
				System.out.println("Add another airspace ? (Y/N)");
				String keep;
				keep=br.readLine();
				while(keep.equals("Y"))
				{
					System.out.println("Airspace name ?");
					airspaceID=br.readLine();
					messageAirspaces+="$"+airspaceID;
					nbPlayers+=1;
					
					System.out.println("Add another airspace ? (Y/N)");
					keep=br.readLine();
				}
			}
		}
		out.println(messagePlayer);
		out.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}

