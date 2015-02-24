package com.thales.atm.seriousgame.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.thales.atm.seriousgame.AOC;
import com.thales.atm.seriousgame.FMP;

public class SettingsInterface implements Runnable{
	
	
	private PrintWriter out;
	private Scanner sc = null;
	private BufferedReader in;
	private boolean settingsOK = false;
	
	public SettingsInterface(PrintWriter out,BufferedReader in) {
		this.in = in;
		this.out = out;
	}
	
	public void run(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//while (!settingsOK){
		//Files selection
		System.out.println("Airspace File ? ");
		String airspaceFile;
		try {
			airspaceFile = br.readLine();
			out.println("ASF"+"$"+airspaceFile);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
				
		System.out.println("Sector File ? ");
		String sectorFile;
		try {
			sectorFile = br.readLine();
			out.println("SCF"+"$"+sectorFile);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
				
		System.out.println("Airblock File ? ");
		String airblockFile;
		try {
			airblockFile = br.readLine();
			out.println("ABF"+"$"+airblockFile);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//Level choice
		System.out.println("Enter level: ");
		try {
			int level = Integer.parseInt(br.readLine());
			out.println("LVL"+"$"+level);
			out.flush();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
		//Players creation
		String addNewPlayer="Y";
		int i=1;
		ArrayList<String> messages = new ArrayList<String>();
		while (addNewPlayer.equals("Y"))
		{
			String messagePlayer= "PLY";
			System.out.println("Player "+i);
			System.out.println("Name of player "+i+" ");
			String name;
			try {
				name = br.readLine();
			String type="none";
			while (!(type.equals("AOC") || type.equals("FMP")))
			{
				System.out.println("Type of player: (AOC/FMP)");
				type=br.readLine();
				System.out.println("Id of player");
				int id = Integer.parseInt(br.readLine());
				if (type.equals("AOC"))
				{
					messagePlayer+="$"+"AOC";
					messagePlayer+="$"+id;
					messagePlayer+="$"+name;
				}
				if(type.equals("FMP"))
				{
					messagePlayer+="$"+"FMP";
					messagePlayer+="$"+i;
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
					
					messagePlayer+="$"+nbPlayers+messageAirspaces;
				}
			}
			i+=1;
			System.out.println("Do you want to add a new player (Y/N) ? ");
			addNewPlayer = br.readLine();
			messages.add(messagePlayer);
			
			
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		out.println("NBP"+"$"+messages.size());
		out.flush();
		for (String player : messages){
			out.println(player);
			out.flush();
		}
		//}
		
		
	}
}
