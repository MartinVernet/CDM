package com.thales.atm.seriousgame.flightparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.NavigableMap;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.thales.atm.seriousgame.Sector;

import com.thales.atm.seriousgame.flightmodel.FlightPlan;
/**

This class parse the Flight Plan xml file by using StAx API

 */
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
	String current_airspace=null;
	String current_airspacetype=null;
	
	public List<FlightPlan> parseFlightPlan(String FlightPlanFile, HashMap<String,Sector> board, HashMap<String,Sector> reducedBoard) {
		/*This function is use to parse the xml file
			*output : List of flights from xml file
			*input : xml file and board / reduced board to be sure to only get elementary sectors
		*/
	   Sector outSector = new Sector("Out",null); // An Out sector is use to maintaining the flight plan continuity when flight go out of the map.
	   outSector.setCapacity(10000);
	   Sector exitSector = new Sector("Exit",null); // An Exit sector is use to know when the flight exit the map
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
	        		
	        		//Set airspace profile to the TreeMap and add Out/Exit sector when needed
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
	            	//Only add flight plan in the range of the reduced map
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
}
