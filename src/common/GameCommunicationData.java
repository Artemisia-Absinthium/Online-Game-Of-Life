package common;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

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
	
	public static void ReadAStartMessage(ServerManager serverManager)
	{
		try {
			ServerManager.board.myBoardWidth = serverManager.myInStream.readInt();
			ServerManager.board.myBoardHeight = serverManager.myInStream.readInt();
			ServerManager.board.myMinimumNeighboursToLive = serverManager.myInStream.readInt();
			ServerManager.board.myMaximumNeighboursToLive = serverManager.myInStream.readInt();
			ServerManager.board.myNumberOfNeighboursToBeBorn = serverManager.myInStream.readInt();

			ServerManager.board.myRefreshRateInMs = serverManager.myInStream.readFloat();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
