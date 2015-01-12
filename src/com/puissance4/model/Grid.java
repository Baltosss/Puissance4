package com.puissance4.model;

import com.puissance4.model.exceptions.*;

import java.util.Arrays;
import java.util.Random;


public class Grid {
	private static int LINE_SIZE_TO_WIN = 4;
	public static int MAX_ALLOWED_RESHUFFLE = 10;
	public static int MAX_ALLOWED_SUFFLE_REPLAY = 100;
	private int width, height;
	private int lastSlotRow, lastSlotColumn;
	private int[][] grid;
	private int countPlayers;
	private int countReshuffle;
	
	public Grid(int height, int width, int countPlayers) {
		if(height <= 3) {
			this.height = 10;
		}
		else {
			this.height = height;
		}
		if(width <= 3) {
			this.width = 10;
		}
		else {
			this.width = width;
		}
		this.countPlayers = countPlayers;
		if(countPlayers < 0)
		{
			this.countPlayers = 2;
		}
		initializeGrid();
		lastSlotRow = lastSlotColumn = -1;
	}

	/*renvoie : 
	 * 0 -> coups joué
	 * 1 -> colonne pleine
	 * 2 -> erreur dans les données
	 */
	public int playAtRow(int row, int newSlotState) throws FullRowException, ImpossibleRowPlayException, NoneMoveException
	{
		if(newSlotState != -1)
		{
			if(row>=0 && row<height)
			{
				int column = 0;
				boolean foundUsedSlot = false;
				while(!foundUsedSlot && column<width)
				{
					if(grid[column][row]==-1)
					{
						column++;
					}
					else
					{
						foundUsedSlot = true;
					}
				}
				if(column > 0)
				{
					grid[column - 1][row] = newSlotState;
					lastSlotColumn = column - 1;
					lastSlotRow = row;
					return 0;
				}
				else
				{
					throw new FullRowException(row);
				}
			}
			else {
				throw new ImpossibleRowPlayException(row);
			}
		}
		else
		{
			throw new NoneMoveException();
		}
	}
	
	/*renvoie : 
	 * 0 -> coups joué
	 * 1 -> colonne pleine
	 * 2 -> erreur dans les données
	 */
	public int playAtColumn(int column, int newSlotState) throws FullColumnException, ImpossibleColumnPlayException, NoneMoveException
	{
		if(newSlotState != -1)
		{
			if(column>=0 && column<width)
			{
				int row = 0;
				boolean foundUsedSlot = false;
				while(!foundUsedSlot && row<height)
				{
					if(grid[column][row]==-1)
					{
						row++;
					}
					else
					{
						foundUsedSlot = true;
					}
				}
				if(row > 0)
				{
					grid[column][row - 1]=newSlotState;
					lastSlotColumn = column;
					lastSlotRow = row - 1;
					return 0;
				}
				else 
				{
					throw new FullColumnException(column);
				}
			}
			else {
				throw new ImpossibleColumnPlayException(column);
			}
		}
		else
		{
			throw new NoneMoveException();
		}
	}
	
	public boolean hasWon() {
		int diagHLDR=0;	//Diagonal high left to down right corner
		int diagHRDL=0;	//Diagonal high right to down left corner
		int horizontalLine=0;
		int verticalLine=0;
		if((lastSlotRow > 0) && (lastSlotColumn > 0))
		{
			diagHLDR = diagonalHLSize(lastSlotRow-1, lastSlotColumn-1);
		}
		if((lastSlotRow < height-1) && (lastSlotColumn < width-1))
		{
			diagHLDR += diagonalDRSize(lastSlotRow+1, lastSlotColumn+1);
		}
		if(diagHLDR>=LINE_SIZE_TO_WIN-1)
		{
			return true;
		}
		if((lastSlotRow > 0) && (lastSlotColumn < width-1))
		{
			diagHRDL = diagonalHRSize(lastSlotRow-1, lastSlotColumn+1);
		}
		if((lastSlotRow < height-1) && (lastSlotColumn > 0))
		{
			diagHRDL += diagonalDLSize(lastSlotRow+1, lastSlotColumn-1);
		}
		if(diagHRDL >= LINE_SIZE_TO_WIN-1)
		{
			return true;
		}
		if(lastSlotColumn > 0)
		{
			horizontalLine = lineToLeftSize(lastSlotColumn-1);
		}
		if(lastSlotColumn < width-1)
		{
			horizontalLine += lineToRightSize(lastSlotColumn+1);
		}
		if(horizontalLine >= LINE_SIZE_TO_WIN-1)
		{
			return true;
		}
		if(lastSlotRow > 0)
		{
			verticalLine = lineToHighSize(lastSlotRow-1);
		}
		if(lastSlotRow < height-1)
		{
			verticalLine += lineToDownSize(lastSlotRow+1);
		}
		if(verticalLine >= LINE_SIZE_TO_WIN-1)
		{
			System.out.println(verticalLine);
			return true;
		}
		return false;
	}
	
	private int lineToDownSize(int row) {
		if(grid[lastSlotColumn][row]==grid[lastSlotColumn][lastSlotRow])
		{
			if(row < height-1)
			{
				return 1+lineToDownSize(row+1);
			}
			return 1;
		}
		return 0;
	}

	private int lineToHighSize(int row) {
		if(grid[lastSlotColumn][row]==grid[lastSlotColumn][lastSlotRow])
		{
			if(row > 0)
			{
				return 1+lineToHighSize(row-1);
			}
			return 1;
		}
		return 0;
	}

	private int lineToRightSize(int column) {
		if(grid[column][lastSlotRow]==grid[lastSlotColumn][lastSlotRow])
		{
			if(column < width-1)
			{
				return 1+lineToRightSize(column+1);
			}
			return 1;
		}
		return 0;
	}

	private int lineToLeftSize(int column) {
		if(grid[column][lastSlotRow]==grid[lastSlotColumn][lastSlotRow])
		{
			if(column>0)
			{
				return 1+lineToLeftSize(column-1);
			}
			return 1;
		}
		return 0;
	}

	private int diagonalDLSize(int row, int column) {
		if(grid[column][row]==grid[lastSlotColumn][lastSlotRow])
		{
			if((row<height-1) && (column>0))
			{
				return 1+diagonalDLSize(row+1, column-1);
			}
			return 1;
		}
		return 0;
	}

	private int diagonalHRSize(int row, int column) {
		if(grid[column][row]==grid[lastSlotColumn][lastSlotRow])
		{
			if((row>0) && (column<width-1))
			{
				return 1+diagonalHRSize(row-1, column+1);
			}
			return 1;
		}
		return 0;
	}

	private int diagonalDRSize(int row, int column) {
		if(grid[column][row]==grid[lastSlotColumn][lastSlotRow])
		{
			if((row < height-1) && (column< width-1))
			{
				return 1+diagonalDRSize(row+1, column+1);
			}
			return 1;
		}
		return 0;
	}

	private int diagonalHLSize(int row, int column) {
		if(grid[column][row]==grid[lastSlotColumn][lastSlotRow])
		{
			if((row > 0) && (column > 0))
			{
				return 1+diagonalDLSize(row-1, column-1);
			}
			return 1;
		}
		return 0;
	}

	public int[][] getGrid() {
		return grid;
	}

	public void setGrid(int[][] grid) throws WrongHeightException, WrongWidthException {
		if(grid.length>=3 && grid.length<=20) {
			if(grid[0].length>=3 && grid[0].length<=20) {
				this.grid = grid;
				width = grid.length;
				height = grid[0].length;
			}
			else {
				throw new WrongHeightException();
			}
		}
		else {
			throw new WrongWidthException();
		}
		
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getLastSlotRow() {
		return lastSlotRow;
	}

	public void setLastSlotRow(int lastSlotRow) {
		this.lastSlotRow = lastSlotRow;
	}

	public int getLastSlotColumn() {
		return lastSlotColumn;
	}

	public void setLastSlotColumn(int lastSlotColumn) {
		this.lastSlotColumn = lastSlotColumn;
	}

	public String toString() {
		String result = "Dimensions : "+height+"x"+width+"\nLastSlotColumn = "+lastSlotColumn+" LastSlotRow = "+lastSlotRow+"\n";
		for(int row = 0; row < height; row++)
		{
			result+="|";
			for(int column = 0; column < width; column++)
			{
				if(grid[column][row]==-2)
				{
					result+="X";
				}
				else if(grid[column][row]==-1)
				{
					result+=" ";
				}
				else
				{
					result+=Integer.toString(grid[column][row]);
				}
				result+="|";
			}
			result+="\n";
		}
		return result;
	}


	private void initializeGrid() {
		grid = new int[this.width][this.height];
		System.out.println("width = "+width+" height = "+height);
		for(int column = 0; column < this.width; column++)
		{
			grid[column]=new int[this.height];
			for(int row = 0; row < this.height; row ++)
			{
				grid[column][row] = -1;
			}
		}
	}
	
	public void reshuffle(int[] countSlots, int newSlotState, int countMoves)
	{
		int nextShuffledSlotState = 0;
		int countRetries = 0;
		initializeGrid();
		Random random = new Random();
		while(countMoves>0)
		{
			if(countSlots[nextShuffledSlotState]>0)
			{
				countSlots[nextShuffledSlotState]--;
				countMoves--;
				countRetries = 0;
				boolean foundSlot = true;
				do{
					if(!foundSlot)
					{
						grid[lastSlotColumn][lastSlotRow] = -1;
					}
					foundSlot = false;
					boolean notAvailableMove = true;
					if(random.nextInt(2) == 0)
					{
						while(notAvailableMove)
						{
							notAvailableMove = false;
							try {
								playAtColumn(random.nextInt(width), nextShuffledSlotState);
							} catch (FullColumnException e) {
								System.out.println(e.toString());
								notAvailableMove = true;
							} catch (ImpossibleColumnPlayException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoneMoveException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else
					{
						notAvailableMove = false;
						while(notAvailableMove)
						{
							try {
								playAtRow(random.nextInt(height), nextShuffledSlotState);
							} catch (FullRowException e) {
								notAvailableMove = true;
								e.printStackTrace();
							} catch (ImpossibleRowPlayException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoneMoveException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					countRetries++;
				}while((hasWon() || isNullGame()) && (countRetries<MAX_ALLOWED_SUFFLE_REPLAY));
			}
			if(nextShuffledSlotState<countPlayers-1)
			{
				nextShuffledSlotState++;
			}
			else
			{
				nextShuffledSlotState=0;
			}
		}
		if(isNullGame())
		{
			countReshuffle ++;
			if(countReshuffle < MAX_ALLOWED_RESHUFFLE)
			{
				reshuffle(countSlots, newSlotState, countMoves);
			}
		}
		if(countRetries==MAX_ALLOWED_SUFFLE_REPLAY)
		{
			countReshuffle ++;
			if(countReshuffle < MAX_ALLOWED_RESHUFFLE)
			{
				reshuffle(countSlots, newSlotState, countMoves);
			}
		}
	}
	
	public void shuffle(int newSlotState) {
		System.out.println(countPlayers);
		int[] countSlots = new int[countPlayers];
		int countMoves=1;
		Arrays.fill(countSlots,0);
		countSlots[newSlotState]++;
		for(int[] column : grid)
		{
			for(int row=0; row<height; row++)
			{
				if(column[row]>=0){
					countSlots[column[row]]++;
					countMoves++;
				}
			}
		}
		countReshuffle = 0;
		reshuffle(countSlots, newSlotState, countMoves);
	}
	
	public boolean isNullGame() {
		for(int i=0; i<height; i++)
		{
			if(grid[0][i]==-1)
			{
				return false;
			}
		}
		for(int i=1; i<width; i++)
		{
			if(grid[i][0]==-1)
			{
				return false;
			}
		}
		return true;
	}
}
