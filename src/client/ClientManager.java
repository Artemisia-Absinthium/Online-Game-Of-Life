package client;

import common.Board;
import common.BoardVisualisator;

public class ClientManager {

	private boolean 		clientConnected = false;
	private MenuManager 	myMenu			= new MenuManager();
	private Board			myGameBoard		= new Board(15, 15, 2, 3, 3, 500.0f);
			
	public void MainLoop() {
		//
		/// Start with the first Menu State
		myMenu.PrintPage(MenuState.WELCOME_MENU);
		
		BoardVisualisator.printBoard(myGameBoard);
		
		myGameBoard.PlayATurn();
		
		BoardVisualisator.printBoard(myGameBoard);

		//
		/// Launch the game loop
		while(clientConnected)
		{
			//
			/// Print current screen
			myMenu.PrintCurrentPage();
			//
			/// Get the client input
			myMenu.GetClientAction();
			
			//
			/// Update the informations
			
			clientConnected = false;			
		}
	}

}
