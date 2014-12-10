package com.thales.atm.seriousgame;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.jws.WebService;

@WebService

public class Sector {
	String m_name;
	ArrayList<AirBlock> m_airBlocks;
	int m_capacity;
	int m_occupation;
	public Sector(String name, ArrayList<AirBlock> airb)
	{
		this.m_name=name;
		this.m_airBlocks=airb;
	}
	public Sector()
	{
		this.m_name="test";
		this.m_airBlocks=null;
	}
	
	//creation du dictionnaire des airblocks;
	/*HashMap <String ,AirBlock> InitAirBlock()
	{
		// First read the airblocks file and fill the airblock dictionnary
		HashMap <String ,AirBlock> airblocksDictionary = new HashMap <String ,AirBlock>();
		ArrayList<Point> points=new ArrayList<Point>();
		
		
		String csvFile = "C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airblock.gar";
		BufferedReader br = null;
		String a="";
		String name1=""; 
		String cvsSplitBy = ";";
		
		try {
	 
				br = new BufferedReader(new FileReader(csvFile));
				while ((a = br.readLine()) != null) 
				{
					
	 		        // use semi comma as separator
					String [] obj= a.split(cvsSplitBy);
					
				
				
					if(obj[0].equalsIgnoreCase("A"))
					{
						if(name1!="")
						{
						ArrayList<Point> l=new ArrayList<Point>();
						for (int i=0; i<points.size(); i++)
						{
							l.add(points.get(i));
						}
						AirBlock airb=new AirBlock(name1,l);
						airblocksDictionary.put(name1,airb);
						}
						name1=obj[1];
						points.clear();
					}
				
					if(obj[0].equals("P"))
					{
						Point p=new Point(Double.parseDouble(obj[1]),Double.parseDouble(obj[2]));
						points.add(p);
						
					}
				
				}
				AirBlock lastAirb= new  AirBlock(name1,points);
				airblocksDictionary.put(name1,lastAirb);
				for (int i=0; i<7; i++)
				System.out.println(airblocksDictionary.get("999UK").m_Coords.get(i).GetX());
			
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done");
		return airblocksDictionary;
	  }*/
	
		
}
