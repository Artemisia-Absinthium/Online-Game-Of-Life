package common;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import server.ServerManager;

/**
 * This class handle the data of a game communication.
 * @author Amélia Chavot
 *
 */
public class GameCommunicationData {
	
	public static byte[] createAStartMessage(int width, int height, int minLiving, int maxLiving, int nbForBirth, float updateRate)
	{
		ByteBuffer message = ByteBuffer.allocate(Integer.BYTES * 6 + Float.BYTES);
		message.putInt(NetworkMessageType.NETWORK_MESSAGE_START.getValue());
		
		message.putInt(width);
		message.putInt(height);
		message.putInt(minLiving);
		message.putInt(maxLiving);
		message.putInt(nbForBirth);
		
		message.putFloat(updateRate);
		return message.array();
	}
	
	public static void ReadAStartMessage(SocketChannel socket)
	{
		try {
			ByteBuffer bufReader = ByteBuffer.allocate(Integer.BYTES);
			socket.read(bufReader);			
			ServerManager.board.myBoardWidth = bufReader.getInt();
			socket.read(bufReader);			

			ServerManager.board.myBoardHeight = bufReader.getInt();
			socket.read(bufReader);			

			ServerManager.board.myMinimumNeighboursToLive = bufReader.getInt();
			socket.read(bufReader);			

			ServerManager.board.myMaximumNeighboursToLive = bufReader.getInt();
			socket.read(bufReader);			

			ServerManager.board.myNumberOfNeighboursToBeBorn = bufReader.getInt();

			socket.read(bufReader);			

			ServerManager.board.myRefreshRateInMs = bufReader.getFloat();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] createAGameUpdateMessage(ServerManager serverManager)
	{
		return null;
	}

	public static byte[] createFullBoardMessage(Board board) {
		
		int height = board.myBoardHeight;
		int width = board.myBoardWidth;
		
		ByteBuffer message = ByteBuffer.allocate(Integer.BYTES + (height * width));
		message.putInt(NetworkMessageType.NETWORK_MESSAGE_FULL_BOARD.getValue());

		for(int row = 0; row < height; ++row)
		{
			for(int column = 0; column < width; ++column)
			{
				if(board.IsCelllivingAt(row, column))
				{
					message.put((byte) 1);
				}
				else
				{
					message.put((byte) 0);
				}
			}
		}
		
		return message.array();
	}
}
