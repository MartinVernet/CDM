package com.thales.atm.seriousgame.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Emission implements Runnable {

	private PrintWriter out;
	private String message = null;
	private Scanner sc = null;
	
	public Emission(PrintWriter out,String message) {
		this.out = out;
		this.message=message;
		
	}
	
	public Emission(PrintWriter out){
		this.out=out;
	}
	
	public void run() {
		
		  sc = new Scanner(System.in);
		  
		  if (message!=null){
			//while(true){
				out.println(message);
			    out.flush();
			  //}
		  }
		  else{
			  while(true){
				    System.out.println("Votre message :");
					message = sc.nextLine();
					out.println(message);
				    out.flush();
				  }
		  }
		  
	}
}