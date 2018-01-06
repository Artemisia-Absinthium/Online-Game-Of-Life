package client;

/**
 * Menu state Enum.
 * Handle the different menu state and display name.
 * @author Amélia Chavot
 *
 */
public enum MenuState {
	//
	/// Enum part
	MAIN_MENU		("Main Menu"),
	WELCOME_MENU	("Welcome menu"),
	CREDIT_MENU		("Credit menu"), 
	PAUSE_MENU		("Pause menu"), 
	CONNECTION_MENU ("Connection menu");
	
	//
	/// display specific part
	private String myName = "";

	MenuState(String name) {
		this.myName = name;
	}

	public String toString(){
	    return myName;
	}
}