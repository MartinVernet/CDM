package com.thales.atm.seriousgame.client;

import java.io.*;
import java.net.*;

public class Client {

	public static Socket socket = null;
	public static Thread t1;
	public String id;
 
	
	//public static void main(String[] args) {
	public Client(String id){
	
		this.id=id;
		
	try {
		
		System.out.println("Demande de connexion");
		socket = new Socket("127.0.0.1",2009);
		System.out.println("Connexion établie avec le serveur"); // Si le message s'affiche c'est que je suis connecté
		
		t1 = new Thread(new ConnexionToServer(socket,id));
		t1.start();
		
		
		
	} catch (UnknownHostException e) {
	  System.err.println("Impossible de se connecter à l'adresse "+socket.getLocalAddress());
	} catch (IOException e) {
	  System.err.println("Aucun serveur à l'écoute du port "+socket.getLocalPort());
	}
	
	

	}

}
