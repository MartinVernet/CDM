package com.thales.atm.seriousgame.client;

import java.io.PrintWriter;
import java.util.Scanner;

public class Message {
	private PrintWriter out;
	private String question;
	private String KEY;
	private Scanner sc;
	
	public Message(PrintWriter out,String question, String KEY) {
		this.out = out;
		this.question=question;
		this.KEY=KEY;
		
		sc = new Scanner(System.in);
		System.out.println(question);
		String message = sc.nextLine();
		out.println(KEY+"$"+message);
		out.flush();
		
	}
	
	
}
