package com.thales.atm.seriousgame;

import java.util.HashMap;
import java.util.Date;

import javax.jws.WebService;

@WebService
public class Test
{
	public int add ( int a, int b )
	{
		return a + b;
	}
	
	/*public HashMap<String, AirSpace> createboard()
	{
		map m=new map();
		return m.GetAIrSPaceDictionary();
	}*/
	 public static void main(String[] args)
	  {
		// Sector s=new Sector( );
		 //s.InitAirBlock();
		 map m=new map(  "C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airblock.gar","C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Sector.gsl","C:/Users/arthur/Desktop/MS Centrale/PFE/map_ACC/Airspace.spc");
	  System.out.print(m.GetAIrSPaceDictionary().get("BIRDNO"));
	  
	  
	   
	  }
}
