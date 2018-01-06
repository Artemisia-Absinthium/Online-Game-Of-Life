package client;

/**
 * Menu selection Enum.
 * Handle the different menu choices and texts.
 * @author Amélia Chavot
 *
 */
public enum MenuSelection {
	//
	/// Enum part
	MENU_SELECTION_YES		("Yes"),
	MENU_SELECTION_NO		("No"),
	MENU_SELECTION_QUIT		("Quit");
  
	//
	/// display specific part
	private String myName = "";

	MenuSelection(String name) {
		this.myName = name;
	}

	public String toString(){
	    return myName;
	}
}
