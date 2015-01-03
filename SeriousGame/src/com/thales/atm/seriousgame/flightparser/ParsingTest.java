package com.thales.atm.seriousgame.flightparser;
import com.thales.atm.seriousgame.flightmodel.Flight;

import java.util.List;

public class ParsingTest {

	public static void main(String args[]) {
		FlightPlanParser read = new FlightPlanParser();
	    List<Flight> parseFlightPlan = read.parseFlightPlan("PlansDeVol.xml");
	    for (Flight flight : parseFlightPlan) {
	      System.out.println(flight);
	    }
	  }
}
