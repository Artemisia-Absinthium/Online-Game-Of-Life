package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import common.Board;
import common.BoardVisualisator;
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
	ServerSocketChannel 	myServerSocketChannel 	= null;
	SocketChannel 			myClientSocketChannel 	= null;


	public ServerManager(ServerSocketChannel serverSocketChannel) {
		this.myServerSocketChannel = serverSocketChannel;
	}

	public void run() {
		System.out.println("A new server managers was started");


		try {
			myClientSocketChannel = SocketChannel.open();
			myClientSocketChannel.connect(new InetSocketAddress(1830));
			myClientSocketChannel.configureBlocking(false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int input;
		boolean clientnNotAccepted = true;
		try {
			while(clientnNotAccepted)
			{
				//
				/// Accept the client
				SocketChannel socket = myServerSocketChannel.accept();
	
				if(null != socket)
				{
					ByteBuffer bufReader = ByteBuffer.allocate(Integer.BYTES);
					socket.read(bufReader);			
					input = bufReader.getInt();
					NetworkMessageType msg = NetworkMessageType.values()[input];
					if (msg == NetworkMessageType.NETWORK_MESSAGE_STOP)
					{
						CloseCurrentConnection();
						return;
					} else {
						if(boardInitialized)
						{
							ByteBuffer bufWritter = ByteBuffer.allocate(Integer.BYTES);
							bufWritter.putInt(NetworkMessageType.NETWORK_MESSAGE_OK_STARTED.getValue());

							bufWritter.flip();

							while(bufWritter.hasRemaining()) {
								myClientSocketChannel.write(bufWritter);
							}
							System.out.println("OK started Send");
						}
						else
						{
							
							ByteBuffer bufWritter = ByteBuffer.allocate(Integer.BYTES);
							bufWritter.putInt(NetworkMessageType.NETWORK_MESSAGE_OK_NOT_STARTED.getValue());

							bufWritter.flip();

							while(bufWritter.hasRemaining()) {
								myClientSocketChannel.write(bufWritter);
							}
							System.out.println("OK not started Send");
	
							//
							/// Wait for player board initialization
							InitTheBoardFromClient();
						}
						
						clientnNotAccepted = false;
					}	
	
					//
					/// Send the board to the client
					byte[] fullBoardMessage =  GameCommunicationData.createFullBoardMessage(ServerManager.board);
	
					ByteBuffer bufWritter = ByteBuffer.allocate(fullBoardMessage.length);
					bufWritter.put(fullBoardMessage);

					bufWritter.flip();

					while(bufWritter.hasRemaining()) {
						myClientSocketChannel.write(bufWritter);
					}

					System.out.println("Board sended to the client");
					BoardVisualisator.printBoard(ServerManager.board);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}


		boolean gameOfLifeIsRunning = false;
		while(gameOfLifeIsRunning)
		{
			//
			// TODO Check if the client send a pause message or disconnected
			System.out.println("Check pause");

			//
			/// TODO Send the board to the client
			board.PlayATurn();
			System.out.println("Send turn");
		}
	}

	private void InitTheBoardFromClient() {

		//
		/// Get client start message if board not started.
		int input;
		while(!boardInitialized)
		{
			try {
				SocketChannel socket = myServerSocketChannel.accept();
				//outStream = new DataOutputStream(clientManager.myClientSocketChannel.socket().getOutputStream());
	
				if(null != socket)
				{
					ByteBuffer bufReader = ByteBuffer.allocate(Integer.BYTES);
					socket.read(bufReader);			
					input = bufReader.getInt();
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
						GameCommunicationData.ReadAStartMessage(socket);
	
	
						board.InitBoardFromRandomSeed();
						boardInitialized = true;
	
						System.out.println("A board is created on the server");
					}
					else
					{
						//
						/// TODO Network error
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void CloseCurrentConnection() {
		System.out.println("A server managers stoped - A Client disconnected");
		try {
			myClientSocketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread.currentThread().interrupt();
	}
}
