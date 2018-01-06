package client;

/**
 * Client entry point.
 * Handle the manager and system informations.
 * @author Amélia Chavot
 *
 */
public class ClientMain {
	//
	/// Class attributs
	private static ClientManager myManager = new ClientManager();
	
	//
	/// Entry point method
	public static void main(String[] args) {
		//
		/// Start the client main loop
		myManager.MainLoop();
		// The app is quit at the end of the mainLoop
	}
}
