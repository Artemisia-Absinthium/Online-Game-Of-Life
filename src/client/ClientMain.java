package client;
import common.GameCommunicationData;

/**
 * @author Amélia Chavot
 *
 */
public class ClientMain {
	private static ClientManager myManager = new ClientManager();
	
	public static void main(String[] args) {
		//
		/// Start the main loop
		myManager.MainLoop();
	}
}
