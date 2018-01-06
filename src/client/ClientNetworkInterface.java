package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
				
				//scan.close();
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
				
				//scan.close();
			}
			
			//
			/// try to connect to the server
			if(ConnectToAServer(clientManager))
			{
				notConnected = false;
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
			/// Ask the server for the connection acceptation
			clientManager.myOutStream.writeInt(NetworkMessageType.NETWORK_MESSAGE_CONNECT.getValue());
			clientManager.myOutStream.flush();
        	System.out.println("CONNECTE Send");

        	
			//
			/// Wait for server response
			int input;
			boolean notAccepted = true;
			
			while (notAccepted) {
	            try {
	            	//if(myInStream.available() > Integer.BYTES)
					{
	                	input = clientManager.myInStream.readInt();
	                	NetworkMessageType msg = NetworkMessageType.values()[input];
	                	if (msg == NetworkMessageType.NETWORK_MESSAGE_OK)
	                	{
	                		notAccepted = false;
	                    	System.out.println("OK Received");

	                    } else {
	                        System.out.println(msg.toString());
	                    	System.out.println("ELSE Received");
	                    }	
					}
	            } catch (IOException e) {
	                e.printStackTrace();
	                return false;
	            }
	        }
			System.out.println("ACCEPTED =D");
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
		/// TODO get info for a board
		
		//
		/// Send start to the server
		clientManager.myScan.nextLine();
		
		try {
			clientManager.myOutStream.writeInt(NetworkMessageType.NETWORK_MESSAGE_BOARD_UPDATE_DATA.getValue());
			clientManager.myOutStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void AskTheServerToStopAGame(ClientManager clientManager)
	{
		
	}
	
	static void ReceiveServerInfo(ClientManager clientManager)
	{
		
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
