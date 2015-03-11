package com.thales.atm.seriousgame;

import java.io.BufferedReader;

import com.vividsolutions.jts.geom.*;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.Viewer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import javax.jws.WebService;

@WebService
public class map {
	//HashMap <String,AirBlock> m_airBlockDictionary;
	HashMap <String,Sector> m_sectorDictionary;
	HashMap <String,AirSpace> m_airSpaceDictionary;
	HashMap <String,AirBlock> m_airBlockWithAltDic;
	HashMap <String,Sector> m_completSectorDictionary;
	private int penality =5 ;
	Graph graph;


	
	
	public map(String AirbFile, String SectorFile, String AirSPaceFile)
	{
		HashMap <String,AirBlock> airBlockDictionaryTemp=InitAirBlock(AirbFile);
		m_sectorDictionary=InitSector(airBlockDictionaryTemp,SectorFile);
		m_airSpaceDictionary=InitAiSpace(m_sectorDictionary, AirSPaceFile);
		setFather();
		
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
	
	public void setFather()
	{
		for(String key : m_sectorDictionary.keySet())
		{
			Sector sector=m_sectorDictionary.get(key);
			sector.setFather(m_airSpaceDictionary.get(sector.getFatherId()));
		}
		
		for(String key : m_airBlockWithAltDic.keySet())
		{
			AirBlock airb=m_airBlockWithAltDic.get(key);
			airb.setFather(m_sectorDictionary.get(airb.getFatherId()));
		}
	}
	
	private void setAirblockNeighbors()
	{
		Map<String,Polygon> AirBpolys=new HashMap<String,Polygon>();
		for (String idAirB : m_airBlockWithAltDic.keySet())
		{
			Coordinate currentCoord[] = new Coordinate[m_airBlockWithAltDic.get(idAirB).GetCoord().size()];
			
			GeometryFactory GF = new GeometryFactory();
			for (int i=0;i<m_airBlockWithAltDic.get(idAirB).GetCoord().size();i++){
				double x=m_airBlockWithAltDic.get(idAirB).GetCoord().get(i).GetX();
				double y=m_airBlockWithAltDic.get(idAirB).GetCoord().get(i).GetY();
				
				currentCoord[i]=new Coordinate(x,y);
				

			}
			AirBpolys.put(idAirB,GF.createPolygon(currentCoord));
		}
		//To be optimised (on peut parcourir moins) 
		for (String idAirB1 : m_airBlockWithAltDic.keySet()){
			for (String idAirB2 : m_airBlockWithAltDic.keySet()){
				if (!(idAirB1.equals(idAirB2)) && AirBpolys.get(idAirB1).touches(AirBpolys.get(idAirB2))){
					double zmin1 = m_airBlockWithAltDic.get(idAirB1).GetAltMin();
					double zmin2 = m_airBlockWithAltDic.get(idAirB2).GetAltMin();
					double zmax1 = m_airBlockWithAltDic.get(idAirB1).GetAltMax();
					double zmax2 = m_airBlockWithAltDic.get(idAirB2).GetAltMax();
					if(!(zmin2>zmax1 || zmax2<zmin1))
					{
						m_airBlockWithAltDic.get(idAirB1).getNeighbors().add(idAirB2);
					}
				}
			}
		}
		
	}
	
	public void setSectorNeighbors()
	{
		Map<String,Polygon> AirBpolys=new HashMap<String,Polygon>();
		for (String idAirB : m_airBlockWithAltDic.keySet())
		{
			Coordinate currentCoord[] = new Coordinate[m_airBlockWithAltDic.get(idAirB).GetCoord().size()];
			
			GeometryFactory GF = new GeometryFactory();
			for (int i=0;i<m_airBlockWithAltDic.get(idAirB).GetCoord().size();i++){
				double x=m_airBlockWithAltDic.get(idAirB).GetCoord().get(i).GetX();
				double y=m_airBlockWithAltDic.get(idAirB).GetCoord().get(i).GetY();
				
				currentCoord[i]=new Coordinate(x,y);
				

			}
			AirBpolys.put(idAirB,GF.createPolygon(currentCoord));
		}
		//To be optimised (on peut parcourir moins) 
		for (String idAirB1 : m_airBlockWithAltDic.keySet()){
			for (String idAirB2 : m_airBlockWithAltDic.keySet()){
				if (!(idAirB1.equals(idAirB2)) && AirBpolys.get(idAirB1).touches(AirBpolys.get(idAirB2))){
					double zmin1 = m_airBlockWithAltDic.get(idAirB1).GetAltMin();
					double zmin2 = m_airBlockWithAltDic.get(idAirB2).GetAltMin();
					double zmax1 = m_airBlockWithAltDic.get(idAirB1).GetAltMax();
					double zmax2 = m_airBlockWithAltDic.get(idAirB2).GetAltMax();
					if(!(zmin2>zmax1 || zmax2<zmin1))
					{
						String idFather1 = m_airBlockWithAltDic.get(idAirB1).getFatherId();
						String idFather2 = m_airBlockWithAltDic.get(idAirB2).getFatherId();
						if(!idFather1.equals(idFather2))
						{
							m_sectorDictionary.get(idFather1).getNeighbors().add(m_sectorDictionary.get(idFather2));
							//m_airBlckWithAltDic.get(idAirB1).getNeighbors().add(idAirB2);

						}
					}
				}
			}
		}
		
	}
	public void reduceMap(ArrayList<String> chosenAirSpaces)
	{
		HashMap<String, AirBlock>reduceAirBlockDictionary=new HashMap<String, AirBlock>();
		HashMap<String, Sector>reduceSectorDictionary=new HashMap<String, Sector>();
		HashMap<String, AirSpace>reduceAirSpaceDictionary=new HashMap<String, AirSpace>();
		for ( String airSpaceId :chosenAirSpaces)
		{
			for (String sectorId :m_airSpaceDictionary.get(airSpaceId).m_sectorId)
			{
				for(String airbId :m_sectorDictionary.get(sectorId).m_airBlocksId)
				{
					reduceAirBlockDictionary.put(airbId, m_airBlockWithAltDic.get(airbId));
				}
				reduceSectorDictionary.put(sectorId, m_sectorDictionary.get(sectorId));
			}
			reduceAirSpaceDictionary.put(airSpaceId, m_airSpaceDictionary.get(airSpaceId));
		}
		
		m_completSectorDictionary = new HashMap <String ,Sector>(m_sectorDictionary);
		//fm_completSectorDictionary. m_sectorDictionary;
		m_sectorDictionary.clear();
		m_airSpaceDictionary.clear();
		m_airBlockWithAltDic.clear();
		
		m_airBlockWithAltDic=reduceAirBlockDictionary;
		m_sectorDictionary=reduceSectorDictionary;
		m_airSpaceDictionary=reduceAirSpaceDictionary;
		
		setSectorNeighbors();
		
	}
	
	
	/**
	 * Handle capacity change due to level
	 * Level 1 : All capacity stay at the initial normalCapacity
	 * Level 2 : Announce capacity diminution that will happen
	 * Level 3 : Announce probability of diminution in each sector
	 * Level 4 : No information
	 * @param level
	 */
	public void setCapacities(int level){
		
		for (String sectorID : m_sectorDictionary.keySet()){
			m_sectorDictionary.get(sectorID).resetCapacity();
		}
		
		if (level == 1){
			
		}
		
		else if (level == 2){
			
			Random random = new Random();
			ArrayList<String> keys = new ArrayList<String>(m_sectorDictionary.keySet());
			String randomKey = keys.get(random.nextInt(keys.size()));
			Sector impactedSector = m_sectorDictionary.get(randomKey);
			
			impactedSector.degradation(0.5);
			//prévenir AOC et FMP
		}
		
		else if (level == 3){
			
			Random random = new Random();
			ArrayList<String> keys = new ArrayList<String>(m_sectorDictionary.keySet());
			String randomKey = keys.get(random.nextInt(keys.size()));
			
			Sector impactedSector = m_sectorDictionary.get(randomKey);
			
			double decrease = random.nextDouble();
			
			impactedSector.degradation(decrease);
		}

	}
	
	public void displaymap()
	{		
		 graph = new SingleGraph("Tutorial 1");
		 
		 HashSet<String> alreadySet=new HashSet<String>();
		for (String sectorID: m_sectorDictionary.keySet())
		{
			 graph.addNode(sectorID);
		}
		for(Node node:graph)
		{
			node.addAttribute("ui.label", node.getId());
			node.addAttribute("ui.style", "fill-color: rgb(0,100,255);shape: box; size: 30px,30px;");
			
		}
		for (String sectorID: m_sectorDictionary.keySet())
		{
			for (Sector neighbor:m_sectorDictionary.get(sectorID).getNeighbors())
			{
				if(!alreadySet.contains(neighbor.m_name))
				{
					graph.addEdge(sectorID+neighbor.m_name, sectorID, neighbor.m_name);
					
				}
				
			}
			alreadySet.add(sectorID);
			//graph.addEdge(sectorID+neighbor, sectorID, neighbor);
		}
        

        graph.display();
	}
	
	ArrayList<Path> getSetsOfShortestPath(Sector A, Sector B, Sector I)// pour les FMP joueurs 
	{
		ArrayList<Path> rerouteChoices= new ArrayList<Path>();
		SingleGraph subgraph = new SingleGraph("sub_ graph");
		 HashSet<String> nodeAlreadySet=new HashSet<String>();
		 HashSet<String> edgeAlreadySet=new HashSet<String>();
		 HashSet<String> nodeSources=new HashSet<String>();
		//graph.addNode(B.m_name);
		for (Sector neighbourA: A.getNeighbors())
		{
			subgraph.addNode(neighbourA.m_name);
			nodeAlreadySet.add(neighbourA.m_name);
			if(I.getNeighbors().contains(neighbourA))
			{
				nodeSources.add(neighbourA.m_name);
			}
		}
		for (Sector neighbourB: B.getNeighbors())
		{
			if(!nodeAlreadySet.contains(neighbourB.m_name) && !neighbourB.m_name.equals(A.m_name))
			{
				subgraph.addNode(neighbourB.m_name);
				nodeAlreadySet.add(neighbourB.m_name);
			}
		}
		
		for (Node node:subgraph)
		{
			for (Sector neighbor:m_sectorDictionary.get(node.getId()).getNeighbors())
			{
				if(!edgeAlreadySet.contains(neighbor.m_name) && nodeAlreadySet.contains(neighbor.m_name))
				{
					graph.addEdge(node.getId()+neighbor.m_name, node.getId(), neighbor.m_name).addAttribute("length", 1);;
					
				}
			}
			edgeAlreadySet.add(node.getId());
		}
		
		
		
		 Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
		 
         // Compute the shortest paths in g from A to all nodes
         dijkstra.init(graph);
         dijkstra.setSource(graph.getNode(B.m_name));
         dijkstra.compute();
         for (String source: nodeSources)
         {
        	 rerouteChoices.add(dijkstra.getPath(graph.getNode(source)));
         }
         
         return rerouteChoices;
	}

	public int getPenality() {
		return penality;
	}

	public void setPenality(int penality) {
		this.penality = penality;
	}
	
	

}


