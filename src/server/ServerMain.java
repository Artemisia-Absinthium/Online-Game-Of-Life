package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

	static final int SERVER_PORT = 1871;

	public static void main(String[] args) {
		System.out.println("The server is starting");

		ServerSocket 	serverSocket 	= null;
		Socket 			clientSocket 	= null;

		boolean 		waitingForConnections = true;
		
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();

		}
		
		//
		/// Start a manager for each connecting client
		while(waitingForConnections) {
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// new thread for a client
			new ServerManager(clientSocket).start();
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("The server is closed");
	}
}
