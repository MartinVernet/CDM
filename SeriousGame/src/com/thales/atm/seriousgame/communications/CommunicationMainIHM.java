package com.thales.atm.seriousgame.communications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.thales.atm.seriousgame.Settings;

public class CommunicationMainIHM {
	
	
	private InputStream  in;
    private OutputStream out;
    private ServerSocket serverSocket;
    private Socket socketOfServer;
	
	
	public CommunicationMainIHM(int settingsPort){
		/**
		try
		{
			
			//create server socket
			this.serverSocket = new ServerSocket(settingsPort);
			System.out.println("Le serveur est à l'écoute du port "+serverSocket.getLocalPort());
			this.socketOfServer=serverSocket.accept();
				System.out.println("Connexion");
			
			//get input and output stream
			this.out=socketOfServer.getOutputStream();
			this.in=socketOfServer.getInputStream();
			
			PrintWriter outWriter=new PrintWriter(out);
				outWriter.println("IHM connectée");
				outWriter.flush();
				
				
		}
		catch(Exception e)
		{
			System.out.println("Erreur creation serveur : " + e.getMessage());
		}
		*/
		ServerSocket socket;
		try {
		socket = new ServerSocket(settingsPort);
		//Thread t = new Thread(new AcceptClients(socket));
		//t.start();
		System.out.println("Mes employeurs sont prêts !");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void listenToClient() throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		while (true){
			try{
				String message=br.readLine();
				System.out.println(message);
			}catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void closeAllSockets() throws IOException
	{
		try{
			socketOfServer.close();
			serverSocket.close();
		}
		catch(Exception e)
		{
			System.out.println("Erreur fermeture sockets serveur: " + e.getMessage());
		}
		
	}
	
	public int read(byte[] b, int len) throws Exception
    {
        return in.read(b, 0, len);
    }
	
	public int getPort(){
		return serverSocket.getLocalPort();
	}
	

}

