package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner; 

import common.Board;

/**
 * Client manager class
 * Handle the main loop, client data an connection.
 * @author Amélia Chavot
 *
 */
public class ClientManager {
	//
	/// Class attributs
	private boolean 	myClientRunning 	= false;
	MenuManager 		myMenu				= new MenuManager();
	Board				myGameBoard			= new Board(15, 15, 2, 3, 3, 500.0f);
	Scanner 			myScan 				= new Scanner(System.in);
	boolean				myBoardStarted		= false;
	
	//
	/// Network attributs
	String 				myServerAdress 	= null;
	int 				myServerPort;

	ServerSocketChannel 	myClientServerSocketChannel	= null;
	SocketChannel 			myClientSocketChannel		= null;

	//
	/// game loop
	public void MainLoop() {
		//
		/// Start with the first Menu State
		myMenu.PrintPage(MenuState.WELCOME_MENU);
		
		//////////////////
		//
		/// TEST ONLY
		//BoardVisualisator.printBoard(myGameBoard);
		
		//myGameBoard.PlayATurn();
		
		//BoardVisualisator.printBoard(myGameBoard);
		//////////////////
		
		//
		/// Connect the player to a server
		ClientNetworkInterface.ConnectThePlayer(this);
		
		//
		/// Send a game of life demand
		if(!myBoardStarted)
		{
			ClientNetworkInterface.AskTheServerToStartAGame(this);
		}
				
		myClientRunning = true;
		System.out.println("CLient Running");
		//
		/// Launch the game loop
		while(myClientRunning)
		{
			//
			/// TODO Check the server info
			if(ClientNetworkInterface.ReceiveServerInfo(this))
			{
				System.out.println("INFO Received");
			}
			else
			{
				//
				/// TODO Check for a pause
				if(myScan.hasNext())
				{
					System.out.println("PAUSE");
					myScan.nextLine();

					//
					/// TODO send serve pause
					
					//
					/// TODO Print pause menu and get input
					// if()
					boolean play = false;
					while(!play)	
					{
						myMenu.PrintPage(MenuState.PAUSE_MENU);

						String input = myScan.nextLine();
						if(input.equalsIgnoreCase("exit"))
						{
						}
						else if(input.equalsIgnoreCase("c"))
						{
							play = true;
							//
							/// TODO send serve play
						} 
					}
				}
				//
				/// Ask for pause command			
				// myClientRunning = false;		
			}
		}
		QuitApp();
	}

	
	
	public void QuitApp()
	{
		myScan.close();
		ClientNetworkInterface.closeConnections(this);
		System.exit(0);
	}
}
