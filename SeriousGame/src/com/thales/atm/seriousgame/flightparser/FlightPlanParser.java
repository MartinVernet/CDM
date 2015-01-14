package com.thales.atm.seriousgame.flightparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.jws.WebService;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.thales.atm.seriousgame.flightmodel.EntryExitTime;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;

@WebService
public class FlightPlanParser {
	
	static final String FLIGHT = "flight"; 
	static final String FLIGHTID = "id"; 
	static final String AIRCRAFTTYPE = "aircraftType";
	static final String TAKEOFFTIME = "actualTakeOffTime";
	static final String POINT = "pointId";
	static final String TIMEPOINT = "timeOver";
	static final String AIRSPACE = "airspaceId";
	static final String ENTRYAS = "firstEntryTime";
	static final String EXITAS = "lastExitTime";

	Date current_datepoint=null;
	Date current_entrydate=null;
	Date current_exitdate=null;
	EntryExitTime current_entryexit=null;
	String current_airspace=null;
	
	  public List<FlightPlan> parseFlightPlan(String FlightPlanFile) {
	    List<FlightPlan> flights = new ArrayList<FlightPlan>();
	   
	    try {
	      // First, create a new XMLInputFactory
	      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	      // Setup a new eventReader
	      InputStream in = new FileInputStream(FlightPlanFile);
	      XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
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

	          if (event.isStartElement()) {
	            if (event.asStartElement().getName().getLocalPart()
	                .equals(FLIGHTID)) {
	              event = eventReader.nextEvent();
	              flight.setFlightId(event.asCharacters().getData());
	              continue;
	            }
	          }
	          if (event.asStartElement().getName().getLocalPart()
	              .equals(AIRCRAFTTYPE)) {
	            event = eventReader.nextEvent();
	            flight.setAircraftType(event.asCharacters().getData());
	            continue;
	          }

	          if (event.asStartElement().getName().getLocalPart()
	              .equals(TAKEOFFTIME) || event.asStartElement().getName().getLocalPart()
	              .equals("estimatedTakeOffTime")) {
	            event = eventReader.nextEvent();	 	          
	            flight.setActualTakeOffTime(event.asCharacters().getData());
	            continue;
	          }

 	  
		      if (event.asStartElement().getName().getLocalPart()
				       .equals(TIMEPOINT)) {
				     event = eventReader.nextEvent();
				     current_datepoint = stringToDate (event.asCharacters().getData());
				     continue;
				            
		       }
		       if (event.asStartElement().getName().getLocalPart()
				        .equals(POINT) || event.asStartElement().getName().getLocalPart()
				        .equals("aerodrome")) {
		           	  event = eventReader.nextEvent();
		           	  flight.getPointProfile().put(current_datepoint, event.asCharacters().getData());
				      continue;
		       }
		       
		       if (event.asStartElement().getName().getLocalPart()
				        .equals(AIRSPACE)) {
		           	  event = eventReader.nextEvent();
		           	  current_airspace = event.asCharacters().getData();
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
		             current_entryexit = new EntryExitTime(current_entrydate, current_exitdate);
		             flight.getAirspaceProfile().put(current_entryexit, current_airspace);
		             continue;
		       }
	          

	          
	        }
	        // If we reach the end of an flight element, we add it to the list
	        
	        if (event.isEndElement()) {
	        	EndElement endElement = event.asEndElement();
	          if (endElement.getName().getLocalPart() == (FLIGHT)) {
	        	  flights.add(flight);
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
