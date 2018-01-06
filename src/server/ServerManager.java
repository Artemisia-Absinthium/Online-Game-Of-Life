package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import common.Board;
import common.GameCommunicationData;
import common.NetworkMessageType;

public class ServerManager extends Thread {
	//
	/// Static attributs
	public static Board 	board;
	public static boolean	boardInitialized;
	
	//
	/// Client specific atrtibuts
	private boolean 		isBoardInitializer;
	protected Socket 		clientSocket;
	public DataInputStream 	myInStream 				= null;
	public DataOutputStream	myOutStream 			= null;

	public ServerManager(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {
		System.out.println("A new server managers was started");


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
				CloseCurrentConnection();
				return;
			} else {
				if(boardInitialized)
				{
					myOutStream.writeInt(NetworkMessageType.NETWORK_MESSAGE_OK_STARTED.getValue());
					myOutStream.flush();
					System.out.println("OK started Send");
				}
				else
				{
					myOutStream.writeInt(NetworkMessageType.NETWORK_MESSAGE_OK_NOT_STARTED.getValue());
					myOutStream.flush();
					System.out.println("OK not started Send");
				}
			}	
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		//
		/// Get client start message if board not started.
		while(!boardInitialized)
		{
			try {
				input = myInStream.readInt();
				NetworkMessageType msg = NetworkMessageType.values()[input];

				if (msg == NetworkMessageType.NETWORK_MESSAGE_STOP)
				{
					CloseCurrentConnection();
				}
				else if (msg == NetworkMessageType.NETWORK_MESSAGE_START)
				{
					board = new Board();
					
					//
					/// Get info from the client
					GameCommunicationData.ReadAStartMessage(this);

					
					board.InitBoardFromRandomSeed();
					boardInitialized = true;
					
					System.out.println("A board is created on the server");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		boolean gameOfLifeIsRunning = true;
		while(gameOfLifeIsRunning)
		{
			//
			// TODO Check if the client send a pause message or disconnected
			System.out.println("Check pause");
			
			//
			/// TODO Send the board to the client
			System.out.println("Send turn");
		}
	}

	private void CloseCurrentConnection() {
		System.out.println("A server managers stoped - A Client disconnected");
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread.currentThread().interrupt();
	}
}
