package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import common.GameCommunicationData;
import common.GameUtility;
import common.NetworkMessageType;

public class ClientNetworkInterface {

	static void ConnectThePlayer(ClientManager clientManager) {
		boolean notConnected = true;

		//
		/// While we are not connected to a server
		while(notConnected)
		{
			clientManager.myMenu.PrintPage(MenuState.CONNECTION_MENU);

			//
			/// Ask for server address
			boolean IPOk = false;
			while(!IPOk)
			{
				System.out.println("Enter the server address (localhost or IP):");
				String input = clientManager.myScan.nextLine();

				if(input.equalsIgnoreCase("localhost") || GameUtility.IsValidIP(input))
				{
					clientManager.myServerAdress = input;

					IPOk = true;
				} 
				if(input.equalsIgnoreCase("exit"))
				{
					clientManager.QuitApp();
				} 

				GameUtility.ClearScreen();
				if(!IPOk)
				{
					System.out.println("Your IP is not valid. Please try again");
				}
			}

			//
			/// Ask for server port
			boolean PortOk = false;
			while(!PortOk)
			{
				System.out.println("Enter the server port:");
				String input = clientManager.myScan.nextLine();

				if(input.equalsIgnoreCase("exit"))
				{
					clientManager.QuitApp();
				} 

				try{
					int port = Integer.parseInt(input);

					clientManager.myServerPort = port;
					PortOk = true;
				}catch(NumberFormatException nfe){
					GameUtility.ClearScreen();
					System.out.println("Your port is not valid. Please try again");
				}
			}

			//
			/// try to connect to the server
			if(ConnectToAServer(clientManager))
			{
				notConnected = false;
			}
			else
			{
				GameUtility.ClearScreen();
				System.out.println("An error occured when connecting to the server");	
			}
		}
	}

	static boolean ConnectToAServer(ClientManager clientManager)
	{
		try {
			//
			/// create the client socket, connect it to the server.
			clientManager.myClientSocket = new Socket(clientManager.myServerAdress, clientManager.myServerPort);

			//
			/// create output and input stream attached to socket
			clientManager.myOutStream = new DataOutputStream(clientManager.myClientSocket.getOutputStream());
			clientManager.myInStream = new DataInputStream(clientManager.myClientSocket.getInputStream());

			//
			/// Ask the server for the connection acception
			clientManager.myOutStream.writeInt(NetworkMessageType.NETWORK_MESSAGE_CONNECT.getValue());
			clientManager.myOutStream.flush();
			System.out.println("CONNECTE Send");

			//
			/// Wait for server response
			int input;
			boolean notAccepted = true;
			while (notAccepted) {
				try {
					input = clientManager.myInStream.readInt();
					NetworkMessageType msg = NetworkMessageType.values()[input];
					if (msg == NetworkMessageType.NETWORK_MESSAGE_OK_NOT_STARTED)
					{
						notAccepted = false;
						System.out.println("OK not started Received");
					} 
					else if (msg == NetworkMessageType.NETWORK_MESSAGE_OK_STARTED){
						notAccepted = false;
						System.out.println("OK started Received");
						clientManager.myBoardStarted = true;
					} 
					else if (msg == NetworkMessageType.NETWORK_MESSAGE_KO)
					{
						return false;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			System.out.println("Client accepted by the server");
			return true;
		} 
		catch (IOException exception) {
			exception.printStackTrace();

			return false;
		}
	}

	static void AskTheServerToStartAGame(ClientManager clientManager)
	{
		//
		/// Get infos for a board
		int 	width, height, minLiving, maxLiving, nbForBirth;
		float 	updateRate;
		
		width 		= askForInt("width", 		clientManager);
		height 		= askForInt("height", 		clientManager);
		minLiving 	= askForInt("minLiving", 	clientManager);
		maxLiving 	= askForInt("maxLiving", 	clientManager);
		nbForBirth 	= askForInt("nbForBirth", 	clientManager);

		updateRate 	= askForFloat("updateRate", clientManager);

		//
		/// Send start to the server
		byte[] startMessage =  GameCommunicationData.createAStartMessage(width, height, minLiving, maxLiving, nbForBirth, updateRate);

		try {
			clientManager.myOutStream.write(startMessage);
			clientManager.myOutStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static int askForInt(String intName, ClientManager clientManager) {
		int 	number 		= 0;
		boolean numberOK 	= false;
		
		while(!numberOK)
		{
			System.out.println("Enter the board desired width:");
			String input = clientManager.myScan.nextLine();

			if(input.equalsIgnoreCase("exit"))
			{
				clientManager.QuitApp();
			} 

			try{
				number = Integer.parseInt(input);
				numberOK = true;
			}catch(NumberFormatException nfe){
				GameUtility.ClearScreen();
				System.out.println("Your " + intName + " Is not valid. Try again please.");
			}
			GameUtility.ClearScreen();
			if(!numberOK)
			{
				GameUtility.ClearScreen();
				System.out.println("Your " + intName + " Is not valid. Try again please.");
			}
		}
		
		return number;
	}

	private static float askForFloat(String floatName, ClientManager clientManager) {
		float 	number 		= 0;
		boolean numberOK 	= false;
		
		while(!numberOK)
		{
			System.out.println("Enter the board desired width:");
			String input = clientManager.myScan.nextLine();

			if(input.equalsIgnoreCase("exit"))
			{
				clientManager.QuitApp();
			} 

			try{
				number = Float.parseFloat(input);
				numberOK = true;
			}catch(NumberFormatException nfe){
				GameUtility.ClearScreen();
				System.out.println("Your " + floatName + " Is not valid. Try again please.");
			}
			GameUtility.ClearScreen();
			if(!numberOK)
			{
				GameUtility.ClearScreen();
				System.out.println("Your " + floatName + "cIs not valid. Try again please.");
			}
		}
		
		return number;
	}
	static void AskTheServerToStopAGame(ClientManager clientManager)
	{

	}

	static boolean ReceiveServerInfo(ClientManager clientManager)
	{

		return false;
	}

	static boolean IsConnected(ClientManager clientManager) {
		return ((clientManager.myInStream != null) && (clientManager.myOutStream != null) && (clientManager.myClientSocket != null));
	}

	static void closeConnections(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			}
			catch (IOException exception) {
				System.out.println("Couldn’t close socket:" + exception);
			}
		}
	}
}
