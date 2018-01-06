package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
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
	private boolean 			myClientRunning 	= false;
	MenuManager 		myMenu				= new MenuManager();
	private Board				myGameBoard			= new Board(15, 15, 2, 3, 3, 500.0f);
	Scanner 			myScan 				= new Scanner(System.in);

	//
	/// Network attributs
	String 				myServerAdress 	= null;
	int 				myServerPort;
	
	DataInputStream 	myInStream 				= null;
	DataOutputStream	myOutStream 			= null;
	Socket 				myClientSocket 			= null;
	
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
		
		
		ClientNetworkInterface.AskTheServerToStartAGame(this);
		
		//
		/// Launch the game loop
		while(myClientRunning)
		{
			//
			/// TODO Print the server info
			
			//
			/// TODO Check for a pause
			
				//
				/// Ask for pause command			
				myClientRunning = false;			
		}
		
		QuitApp();
	}

	
	
	public void QuitApp()
	{
		myScan.close();
		ClientNetworkInterface.closeConnections(myClientSocket);
		System.exit(0);
	}
}
