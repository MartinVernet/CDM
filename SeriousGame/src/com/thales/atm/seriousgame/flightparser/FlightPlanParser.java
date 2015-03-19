package com.thales.atm.seriousgame.flightparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.jws.WebService;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.events.Attribute;
//import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

import com.thales.atm.seriousgame.Sector;

import com.thales.atm.seriousgame.map;
//import com.thales.atm.seriousgame.map;
//import com.thales.atm.seriousgame.flightmodel.EntryExitTime;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;

@WebService
//This class parse the Flight Plan xml file
public class FlightPlanParser {
	
	static final String FLIGHT = "flight"; 
	static final String AIRCRAFTID = "aircraftId"; 
	static final String AIRCRAFTTYPE = "aircraftType";
	static final String AIRSPACE = "airspaceId";
	static final String ENTRYAS = "firstEntryTime";
	static final String EXITAS = "lastExitTime";

	Date current_entrydate=null;
	Date current_exitdate=null;
	Date final_exitdate=null;
	boolean findEntry=false;
	//EntryExitTime current_entryexit=null;
	String current_airspace=null;
	String current_airspacetype=null;
	

	public List<FlightPlan> parseFlightPlan(String FlightPlanFile, HashMap<String,Sector> board, HashMap<String,Sector> reducedBoard) {

	   Sector outSector = new Sector("Out",null);
	   outSector.setCapacity(10000);
	   Sector exitSector = new Sector("Exit",null);
	   exitSector.setCapacity(10000);
	   List<FlightPlan> flights = new ArrayList<FlightPlan>();
	   
	   try {
	      // First, create a new XMLInputFactory
	      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	      // Setup a new eventReader
	      InputStream in = new FileInputStream(FlightPlanFile);
	      XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
	      //XMLEventReader eventReader2 = inputFactory.createXMLEventReader(in);
	      
	      // read the XML document
	      FlightPlan flight = null;

	      while (eventReader.hasNext()) {
	        XMLEvent event = eventReader.nextEvent();

	        if (event.isStartElement()) {
	          StartElement startElement = event.asStartElement();
	          // If we have an item element, we create a new item
	          if (startElement.getName().getLocalPart() == (FLIGHT)) {
	        	  flight = new FlightPlan();
	          }

	          if (event.asStartElement().getName().getLocalPart()
	                .equals(AIRCRAFTID)) {
	            event = eventReader.nextEvent();
	            flight.setFlightId(event.asCharacters().getData());
	            continue;
	          }
	        
	          if (event.asStartElement().getName().getLocalPart()
	              .equals(AIRCRAFTTYPE)) {
	            event = eventReader.nextEvent();
	            flight.setAircraftType(event.asCharacters().getData());
	            continue;
	          }
		       		       
		       if (event.asStartElement().getName().getLocalPart()
				        .equals(AIRSPACE)) {
		           	  event = eventReader.nextEvent();
		           	  current_airspace = event.asCharacters().getData();
				      continue;
		       }
		       
		       if (event.asStartElement().getName().getLocalPart()
				        .equals("airspaceType")) {
		           	  event = eventReader.nextEvent();
		           	  current_airspacetype=event.asCharacters().getData();	
		           	  continue;
		       }
           
	           if (event.asStartElement().getName().getLocalPart()
		               .equals(ENTRYAS)) {
		             event = eventReader.nextEvent();
		             current_entrydate = stringToDate (event.asCharacters().getData());
		             continue;
		       }

	           if (event.asStartElement().getName().getLocalPart()
		               .equals(EXITAS)) {
		             event = eventReader.nextEvent();
		             current_exitdate = stringToDate (event.asCharacters().getData());
		           	 continue;
		       }
	          
	        }
	        // If we reach the end of an End element, we add it to the list
	        
	        if (event.isEndElement()) {

	        	if (event.asEndElement().getName().getLocalPart() == ("ctfmAirspaceProfile")) {
	        		if(board.keySet().contains(current_airspace))
		           	{
	        				if (reducedBoard.keySet().contains(current_airspace))
	        				{
	        					
	        					final_exitdate=current_exitdate;
	        					flight.getAirspaceProfile().put(current_entrydate, reducedBoard.get(current_airspace));						        					
	        					flight.setExitMap(final_exitdate);
	        					
								if(findEntry == false){
	        						flight.setEntryMap(current_entrydate);
									findEntry = true;
								}
	        				}
	        				else
	        				{   
	        					if(flight.getAirspaceProfile().containsKey(current_entrydate) == false)
	        					{
	        						flight.getAirspaceProfile().put(current_entrydate, outSector);
	        					}
	        				}
		           	}
	        		
	        		
	        		
		        }
	            if (event.asEndElement().getName().getLocalPart() == (FLIGHT) && flight.getExitMap() != null) { 
	            	
	            		
		            	flight.getAirspaceProfile().put(flight.getExitMap(), exitSector);
		            	NavigableMap<Date,Sector> reducedmap = flight.getAirspaceProfile().subMap(flight.getEntryMap(), true, flight.getExitMap(), true);
		            	
		            	flight.setAirspaceProfile(reducedmap);
		            	flight.setAirlineFromId();	            	
		            	flights.add(flight);
		            	findEntry = false;
	            	}

	            }
	        }

	      
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (XMLStreamException e) {
	      e.printStackTrace();
	    }
	    return flights;
	  }	  
	  
	//String to Date and time
	  public Date stringToDate (String stringdate) {
		  
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
		  sdf.setLenient(false);
		  try {		  
		  Date d = sdf.parse(stringdate);		  
		  return d;
		  } catch (ParseException e) {
				e.printStackTrace();
				return null;
		  }		  
	  }
	  
	  public int stringToDuration (String stringduration) {
		  
		  String stringhours = StringUtils.substring(stringduration, 0, 2);
		  int hours = Integer.parseInt(stringhours);
		  
		  String stringminutes = StringUtils.substring(stringduration, 2, 4);
		  int minutes = Integer.parseInt(stringminutes);
		  
		  String stringseconds = StringUtils.substring(stringduration, 4, 6);
		  int seconds = Integer.parseInt(stringseconds);
		  
		  int duration = 3600 * hours + 60 * minutes + seconds;
		  return duration;
	  }
	        	  
}
