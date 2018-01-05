package common;

public enum NetworkMessageType {
	NETWORK_MESSAGE_CONNECT				("Connection"),
	NETWORK_MESSAGE_STOP				("Stop"),
	NETWORK_MESSAGE_BOARD_UPDATE_DATA	("Board update data");
  
	private String myName = "";

	NetworkMessageType(String name) {
		this.myName = name;
	}

	public String toString(){
	    return myName;
	}
}
