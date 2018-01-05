package common;

public class BoardVisualisator {

	public static void printBoard(Board board)
	{
		final int width 	= board.GetWidth();
		final int height 	= board.GetHeight();
		
		//
		/// Board title
		System.out.println("Conway's Game of Life");
		System.out.println("board iteration: " + Integer.toString(board.GetCurrenIteration()));
		System.out.println("board size: " + Integer.toString(width) + " X " + Integer.toString(height));
		System.out.println("board initial seed: " + Integer.toString(board.GetInitialSeed()));
		System.out.println();
		
		//
		/// Main board
		for(int row = 0; row < height; ++row)
		{
			for(int column = 0; column < width; ++column)
			{
				if(board.IsCelllivingAt(row, column))
				{
					System.out.print("· ");
				}
				else
				{
					System.out.print("X ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
