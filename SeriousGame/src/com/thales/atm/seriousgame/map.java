package com.thales.atm.seriousgame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jws.WebService;

@WebService
public class map {
	//HashMap <String,AirBlock> m_airBlockDictionary;
	HashMap <String,Sector> m_sectorDictionary;
	HashMap <String,AirSpace> m_airSpaceDictionary;
	HashMap <String,AirBlock> m_airBlockWithAltDic;

	public map(String AirbFile, String SectorFile, String AirSPaceFile)
	{
		HashMap <String,AirBlock> airBlockDictionaryTemp=InitAirBlock(AirbFile);
		m_sectorDictionary=InitSector(airBlockDictionaryTemp,SectorFile);
		m_airSpaceDictionary=InitAiSpace(m_sectorDictionary, AirSPaceFile);
	}
	
	public HashMap <String,AirBlock> GetAirBlocDictionary()
	{
		return m_airBlockWithAltDic;
	}
	
	public HashMap <String,Sector> getSectorDictionary()
	{
		return m_sectorDictionary;
	}
	
	public HashMap <String,AirSpace> GetAIrSPaceDictionary()
	{
		return m_airSpaceDictionary;
	}
	
	//init airBlock
	HashMap <String ,AirBlock> InitAirBlock(String airbFile)
	{
		// First read the airblocks file and fill the airblock dictionnary
		HashMap <String ,AirBlock> airblocksDictionary = new HashMap <String ,AirBlock>();
		ArrayList<Point> points=new ArrayList<Point>();
			
		
		String csvFile =airbFile;// "C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airblock.gar";
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

				System.out.println(airblocksDictionary.get("999UK").GetCoord().get(i).GetX());

			
	 
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
	  }
	
	
	//init sector
	HashMap <String ,Sector> InitSector(HashMap<String ,AirBlock> airBlockDic, String SectorFile)
	{
		
		HashMap <String ,Sector> SectorDictionary = new HashMap <String ,Sector>();
		m_airBlockWithAltDic=new HashMap<String, AirBlock>();
		//ArrayList<AirBlock> airBlockList=new ArrayList<AirBlock>();
		ArrayList<String> airBlockListId=new ArrayList<String>();
		
		
		
		String csvFile = SectorFile;// "C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Sector.gsl";
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
					
				
				
					if(obj[0].equalsIgnoreCase("S"))
					{
						if(name1!="")
						{
						ArrayList<String> l=new ArrayList<String>();
						for (int i=0; i<airBlockListId.size(); i++)
						{
							l.add(airBlockListId.get(i));
						}
						Sector sect=new Sector(name1,l);
						
						SectorDictionary.put(name1,sect);
						}
						name1=obj[1];
						airBlockListId.clear();
					}
				
					if(obj[0].equals("A"))
					{

						String nameWithAlt=airBlockDic.get(obj[1]).GetName()+"_"+obj[3]+"_"+obj[4] ; 
						AirBlock airb=new AirBlock(nameWithAlt,airBlockDic.get(obj[1]).GetCoord());
						airb.setFatherId(name1);
						airb.SetAltMin(Double.parseDouble(obj[3]));
						airb.SetAltMax(Double.parseDouble(obj[4]));
						m_airBlockWithAltDic.put(nameWithAlt, airb);
						//airBlockList.add(airb);
						airBlockListId.add(nameWithAlt);
						
					}
				
				}
				Sector lastSect= new  Sector(name1,airBlockListId);
				SectorDictionary.put(name1,lastSect);
				for (int i=0; i<7; i++)
				System.out.println(SectorDictionary.get("BIRDNO"));
			
	 
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
		return SectorDictionary;
	  }
	//init AirBlock
	HashMap <String ,AirSpace> InitAiSpace(HashMap<String ,Sector> sectorDic, String AirSPaceFile)
	{
		// First read the airblocks file and fill the airblock dictionnary
		HashMap <String ,AirSpace> airSpaceDictionary = new HashMap <String ,AirSpace>();
		//ArrayList<Sector> SectorList=new ArrayList<Sector>();
		ArrayList<String> SectorListId=new ArrayList<String>();
		
		
		String csvFile =AirSPaceFile;// "C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airspace.spc";
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
						ArrayList<String> l=new ArrayList<String>();
						for (int i=0; i<SectorListId.size(); i++)
						{
							l.add(SectorListId.get(i));
						}
						AirSpace airSp=new AirSpace(name1,l);
						//AirSpace airSp=new AirSpace(name1,SectorListId);
						airSpaceDictionary.put(name1,airSp);
						}
						name1=obj[1];
						SectorListId.clear();
					}
				
					if(obj[0].equals("S"))
					{
						Sector sect=sectorDic.get((obj[1]));

						sect.setFatherId(name1);

						SectorListId.add(sect.m_name);
						
					}
				
				}
				AirSpace lastAirSp= new  AirSpace(name1,SectorListId);
				airSpaceDictionary.put(name1,lastAirSp);
				for (int i=0; i<7; i++)
				System.out.println(airSpaceDictionary.get("BIRDNO"));
			
	 
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
		return airSpaceDictionary;
	  }

	public ArrayList<AirSpace> checkOccupation() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean checkIfRegulationNeeded() {
		// TODO Auto-generated method stub
		
		return true;
	}

	

}


