package client;

public enum MenuSelection {
	MENU_SELECTION_YES		("Yes"),
	MENU_SELECTION_NO		("No"),
	MENU_SELECTION_QUIT		("Quit");
  
	private String myName = "";

	MenuSelection(String name) {
		this.myName = name;
	}

	public String toString(){
	    return myName;
	}
}
