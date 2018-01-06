package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerMain {

	static final int SERVER_PORT = 1871;
	static int	clientCount;
	
	public static void main(String[] args) {
		System.out.println("The server is starting");

		ServerSocketChannel 	serverSocketChannel 	= null;

		boolean 		waitingForConnections 	= true;
		ServerSocket 	serverSocket			= null;
		Selector 		selector				= null;

		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocket = serverSocketChannel.socket();
			serverSocketChannel.socket().bind(new InetSocketAddress(SERVER_PORT));
			serverSocketChannel.configureBlocking(false);

			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true)
		{
			int num;
			try {
				num = selector.select();
				if (num == 0)
				{
					continue;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Set keys = selector.selectedKeys();
			Iterator it = keys.iterator();
			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
					Socket Client;
					try {
						Client = serverSocket.accept();
						clientCount++;
						System.out.println("Client Connected...." + "you have "
								+ clientCount + " clients connected");
						SocketChannel ClientChannel = Client.getChannel();
						ClientChannel.configureBlocking(false);
						ClientChannel.register(selector, SelectionKey.OP_READ);// read
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				// incoming
				// stream
				} else {
					if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
						SocketChannel Client = null;
						Client = (SocketChannel) key.channel();
						// client=Client;
						// ReadClientStream();
					}
				}
				it.remove(); // Add this to Remove Already Selected SelectionKeys 

			}
		}
		//
		/// Start a manager for each connecting client
		/*while(waitingForConnections) {
			try {
				SocketChannel socketChannel =
			            serverSocketChannel.accept();

			    if(socketChannel != null){
					// new thread for a client
					new ServerManager(serverSocketChannel).start();
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			serverSocketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		// System.out.println("The server is closed");
	}
}
