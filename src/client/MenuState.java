package client;

public enum MenuState {
	MAIN_MENU		("Main Menu"),
	WELCOME_MENU	("Welcome menu"),
	CREDIT_MENU		("Credit menu");
  
	private String myName = "";

	MenuState(String name) {
		this.myName = name;
	}

	public String toString(){
	    return myName;
	}
}