package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import common.NetworkMessageType;

public class ServerManager extends Thread {
	protected Socket clientSocket;

	public ServerManager(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {
		System.out.println("New server managers started");

		DataInputStream 	myInStream 				= null;
		DataOutputStream	myOutStream 			= null;

		try {
			myInStream 	= new DataInputStream(clientSocket.getInputStream());
			myOutStream = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			return;
		}

		//
		/// Accept the client
		int input;
		try {
			input = myInStream.readInt();
			NetworkMessageType msg = NetworkMessageType.values()[input];
			if (msg == NetworkMessageType.NETWORK_MESSAGE_STOP)
			{
				System.out.println("STOP Received");
				clientSocket.close();
				return;
			} else {
				System.out.println("ELSE Received");
				myOutStream.writeInt(NetworkMessageType.NETWORK_MESSAGE_OK.getValue());
				myOutStream.flush();
				System.out.println("OK Send");
			}	
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			input = myInStream.readInt();
			System.out.println("Received - " + Integer.toString(input));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Server managers OFF");
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
