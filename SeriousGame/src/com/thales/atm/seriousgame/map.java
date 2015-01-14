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
	HashMap <String,AirBlock> m_airBlockDictionary;
	HashMap <String,Sector> m_sectorDictionary;
	HashMap <String,AirSpace> m_airSpaceDictionary;
	

	public map(String AirbFile, String SectorFile, String AirSPaceFile)
	{
		m_airBlockDictionary=InitAirBlock(AirbFile);
		m_sectorDictionary=InitSector(m_airBlockDictionary,SectorFile);
		m_airSpaceDictionary=InitAiSpace(m_sectorDictionary, AirSPaceFile);
	}
	
	public HashMap <String,AirBlock> GetAirBlocDictionary()
	{
		return m_airBlockDictionary;
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
		// First read the airblocks file and fill the airblock dictionnary
		HashMap <String ,Sector> SectorDictionary = new HashMap <String ,Sector>();
		ArrayList<AirBlock> airBlockList=new ArrayList<AirBlock>();
		
		
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
						ArrayList<AirBlock> l=new ArrayList<AirBlock>();
						for (int i=0; i<airBlockList.size(); i++)
						{
							l.add(airBlockList.get(i));
						}
						Sector sect=new Sector(name1,l);
						SectorDictionary.put(name1,sect);
						}
						name1=obj[1];
						airBlockList.clear();
					}
				
					if(obj[0].equals("A"))
					{

						AirBlock airb=new AirBlock(airBlockDic.get(obj[1]).GetName(),airBlockDic.get(obj[1]).GetCoord());
						airb.setFatherId(name1);
						airb.SetAltMin(Double.parseDouble(obj[3]));
						airb.SetAltMax(Double.parseDouble(obj[4]));

						airBlockList.add(airb);
						
					}
				
				}
				Sector lastSect= new  Sector(name1,airBlockList);
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
		ArrayList<Sector> SectorList=new ArrayList<Sector>();
		
		
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
						ArrayList<Sector> l=new ArrayList<Sector>();
						for (int i=0; i<SectorList.size(); i++)
						{
							l.add(SectorList.get(i));
						}
						AirSpace airSp=new AirSpace(name1,l);
						airSpaceDictionary.put(name1,airSp);
						}
						name1=obj[1];
						SectorList.clear();
					}
				
					if(obj[0].equals("S"))
					{
						Sector sect=sectorDic.get((obj[1]));

						sect.setFatherId(name1);

						SectorList.add(sect);
						
					}
				
				}
				AirSpace lastAirSp= new  AirSpace(name1,SectorList);
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


