package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import common.Board;
import common.BoardVisualisator;
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
			clientManager.myClientSocketChannel = SocketChannel.open();
			clientManager.myClientSocketChannel.connect(new InetSocketAddress(clientManager.myServerAdress, clientManager.myServerPort));
			clientManager.myClientSocketChannel.configureBlocking(false);

			//
			/// Ask for the last used port
			clientManager.myClientServerSocketChannel = ServerSocketChannel.open();
			clientManager.myClientServerSocketChannel.socket().bind(new InetSocketAddress(1830));
			clientManager.myClientServerSocketChannel.configureBlocking(false);


			//
			/// Ask the server for the connection acception
			
			
			ByteBuffer bufConnect = ByteBuffer.allocate(Integer.BYTES);
			bufConnect.putInt(NetworkMessageType.NETWORK_MESSAGE_CONNECT.getValue());

			bufConnect.flip();

			while(bufConnect.hasRemaining()) {
				clientManager.myClientSocketChannel.write(bufConnect);
			}
			System.out.println("CONNECTE Send");

			//
			/// Wait for server response
			int input;
			boolean notAccepted = true;
			while (notAccepted) {
				SocketChannel socket = clientManager.myClientServerSocketChannel.accept();
				//outStream = new DataOutputStream(clientManager.myClientSocketChannel.socket().getOutputStream());
	
				if(null != socket)
				{
						ByteBuffer bufReader = ByteBuffer.allocate(Integer.BYTES);
						socket.read(bufReader);			
						input = bufReader.getInt();

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
			ByteBuffer bufWritter = ByteBuffer.allocate(startMessage.length);
			bufWritter.put(startMessage);

			bufWritter.flip();

			while(bufWritter.hasRemaining()) {
				clientManager.myClientSocketChannel.write(bufWritter);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//
		/// Start the client board
		clientManager.myGameBoard = new Board(width, height, minLiving, maxLiving, nbForBirth, updateRate);
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
		int input = 0;
		try {
			System.out.println("TEST INFO");
			SocketChannel socket = clientManager.myClientServerSocketChannel.accept();
			//outStream = new DataOutputStream(clientManager.myClientSocketChannel.socket().getOutputStream());

			if(null != socket)
			{
				ByteBuffer bufReader = ByteBuffer.allocate(Integer.BYTES);
				socket.read(bufReader);			
				input = bufReader.getInt();
					
				NetworkMessageType msg = NetworkMessageType.values()[input];
				
				if (msg == NetworkMessageType.NETWORK_MESSAGE_FULL_BOARD)
				{
					System.out.println("Server sended full board");
					clientManager.myGameBoard.UpdateFullBoardFromNetworkMessage(socket);
					
					BoardVisualisator.printBoard(clientManager.myGameBoard);
					System.out.flush();
					
					clientManager.QuitApp();
				} 
				else if (msg == NetworkMessageType.NETWORK_MESSAGE_BOARD_UPDATE_DATA)
				{
					System.out.println("Server sended infos");
					return true;
				} 
				else if (msg == NetworkMessageType.NETWORK_MESSAGE_STOP){
					System.out.println("Server stoped");
					clientManager.QuitApp();
					return true;
				} 
				else
				{
					System.out.println("Server sended nothing good");
					return true;
				}
			}
			System.out.println("TEST INFO 2");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	static void closeConnections(ClientManager clientManager) {
		if (clientManager.myClientServerSocketChannel != null) {
			try {
				clientManager.myClientServerSocketChannel.close();
				clientManager.myClientServerSocketChannel = null;
			}
			catch (IOException exception) {
				System.out.println("Couldn’t close socket:" + exception);
			}
		}
		
		if (clientManager.myClientSocketChannel != null) {
			try {
				clientManager.myClientSocketChannel.close();
				clientManager.myClientSocketChannel = null;
			}
			catch (IOException exception) {
				System.out.println("Couldn’t close socket:" + exception);
			}
		}
	}
}
