package com.thales.atm.seriousgame.flightparser;
import com.thales.atm.seriousgame.flightmodel.FlightPlan;

import java.util.List;

public class ParsingTest {

	public static void main(String args[]) {
		FlightPlanParser read = new FlightPlanParser();
	    List<FlightPlan> parseFlightPlan = read.parseFlightPlan("PlansDeVol.xml");
	    for (FlightPlan flight : parseFlightPlan) {
	      System.out.println(flight);
	    }
	  }
}
