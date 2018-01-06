package common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Board {
	private int 	myBoardWidth;
	private int 	myBoardHeight;
	private int		myMinimumNeighboursToLive; 
	private int		myMaximumNeighboursToLive; 
	private int		myNumberOfNeighboursToBeBorn;
	private int		myBoardCurrentIteration;
	private float 	myRefreshRateInMs;
	private boolean	myLivingStatusOnBorder = false;
	
	private	ArrayList<ArrayList<Boolean>> myBoard;
	
	public Board(int width, int height, int minNeighbours, int maxNeighbours, int numberOfNeighboursToBeBorn, float boardRefreshRate)
	{
		myBoardWidth = width;
		myBoardHeight = height;
		myMinimumNeighboursToLive = minNeighbours;
		myMaximumNeighboursToLive = maxNeighbours;
		myRefreshRateInMs = boardRefreshRate;
		myBoardCurrentIteration = 0; 
		myNumberOfNeighboursToBeBorn = numberOfNeighboursToBeBorn;
		
		myBoard = new ArrayList<ArrayList<Boolean>>();
		InitBoardFromRandomSeed();
	}
	
	public void InitBoardFromRandomSeed()
	{
		DeleteBoard();
		
		Random rand = new Random(System.currentTimeMillis());
		
		myBoard = new ArrayList<ArrayList<Boolean>>();
		for(int row = 0; row < myBoardHeight; ++row)
		{
			ArrayList<Boolean> currentRow = new ArrayList<Boolean>();
			for(int column = 0; column < myBoardWidth; ++column)
			{
				currentRow.add(rand.nextBoolean());
			}
			myBoard.add(currentRow);
		}
	}
	
	public void InitBoardFromSeed(int seed)
	{
		Random rand = new Random(seed);
	}
	
	public void InitBoardFromFile()
	{
		
	}
	
	public void DeleteBoard()
	{	
		final int rowSIze = myBoard.size();
		for(int row = 0; row < rowSIze; ++row)
		{
			ArrayList<Boolean> currentRow = myBoard.get(row);
			currentRow.clear();
		}
		
		myBoard.clear();
	}
	
	public void UpdateBoardFromNetworkMessage(ByteArrayOutputStream dataStream/*GameCommunicationData message*/)
	{
		ByteArrayInputStream dataInputStream = new ByteArrayInputStream(dataStream.toByteArray());
		DataInputStream inputStream = new DataInputStream(dataInputStream);
		
		try {
			while(inputStream.available() > (Integer.BYTES * 2))
			{
				int row = inputStream.readInt();
				int column = inputStream.readInt();
				myBoard.get(row).set(column, !myBoard.get(row).get(column));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void PlayATurn()
	{
		//
		/// Creat a communication message
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(stream);
		
		for(int row = 0; row < myBoardHeight; ++row)
		{
			for(int column = 0; column < myBoardWidth; ++column)
			{
				if(WillCellChangeAt(row, column))
				{
					try {
						dataStream.writeInt(row);
						dataStream.writeInt(column);	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		UpdateBoardFromNetworkMessage(stream);
	}

	private boolean WillCellChangeAt(int row, int column) {
		
		if(IsCelllivingAt(row, column))
		{
			final int numberOfLivingNeighbours = GetNumberOfLivingNeighbours(row, column);
			
			if (numberOfLivingNeighbours < myMinimumNeighboursToLive
				|| numberOfLivingNeighbours > myMaximumNeighboursToLive)
			{
				return true;
			}
		}
		else
		{
			if (GetNumberOfLivingNeighbours(row, column) == myNumberOfNeighboursToBeBorn)
			{
				return true;
			}
		}
		
		return false;
	}

	private int GetNumberOfLivingNeighbours(int row, int column) {
		int numberOfNeighbours = 0;
		//
		/// For each neighbours
		for(int rowOffset = -1 ; rowOffset <= 1 ; ++rowOffset)
		{
			for(int columnOffset = -1 ; columnOffset <= 1 ; ++columnOffset)
			{
				if(IsCelllivingAt(row + rowOffset, column+ columnOffset)) {
					++numberOfNeighbours;
				}
			}
		}
		return numberOfNeighbours;
	}

	public int GetCurrenIteration() {
		return myBoardCurrentIteration;
	}

	public int GetWidth() {
		return myBoardWidth;
	}

	public int GetHeight() {
		return myBoardHeight;
	}

	public int GetInitialSeed() {
		return 0;
	}

	public boolean IsCelllivingAt(int row, int column) {
		if(row < 0 ||
			column < 0 || 
			row >= myBoardWidth || 
			column >= myBoardHeight)
		{
			return myLivingStatusOnBorder;
		}
		
		return myBoard.get(row).get(column);
	}
}
