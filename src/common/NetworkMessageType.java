package common;

public enum NetworkMessageType {
	NETWORK_MESSAGE_CONNECT				(0),
	NETWORK_MESSAGE_STOP				(1),
	NETWORK_MESSAGE_BOARD_UPDATE_DATA	(2),
	NETWORK_MESSAGE_OK					(3);
  
	private int 	myValue = 0;

	NetworkMessageType(int name) {
		this.myValue = name;
	}

	public int getValue(){
	    return myValue;
	}
}
