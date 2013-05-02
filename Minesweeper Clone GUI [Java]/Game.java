/*
 *  contains the game logic
 */

// for JOption
import javax.swing.*;
// for Random
import java.util.*;
//import java.io.*;

public class Game 
{	
	private int maxMines;
	private int fieldHeight;
	private int fieldWidth;
	// gamelogic operations are called on this
	private int[][] field;
	// represents a mine in the gamelogic field
	private final int indicatorMine = 9;	
	private final int indicatorFree = 0;
	// only displays the field game logic is done on field
	private MyButton[][] fieldDisplay;
	private int minesCleared;
	
	public Game(int h, int w, int m)
	{				
		this.fieldHeight = h;
		this.fieldWidth = w;
		this.maxMines = m;
		this.minesCleared = 0;
		
		this.field = new int[fieldHeight][fieldWidth];
		this.fieldDisplay = new MyButton[fieldHeight][fieldWidth];
	}
	
	/* initialize both fields */
	public void initField()
	{			
		for(int i = 0; i < fieldHeight; i++) 
		{			
			for(int j = 0; j < fieldWidth; j++)
			{					
				field[i][j] = indicatorFree;						
				fieldDisplay[i][j].setCoveredStatus(true);
			}
		}
	}
	
	/* adds a button to the fieldDisplay array */
	public void addButton(int x, int y, MyButton button)
	{
		fieldDisplay[x][y] = button;
	}
	
	/* randomly spawn mines on the field */
	public void createMines()
	{
		Random r = new Random();
		int maxRandomX, maxRandomY;
		int x, y;
		
		for(int i = 0; i < maxMines; i++)
		{			
			// decrease value to match array coordinates
			maxRandomX = fieldWidth;
			maxRandomX--;
			maxRandomY = fieldHeight;
			maxRandomY--;
			
			y = 1 + Math.abs(r.nextInt()) % maxRandomY;
			x = 1 + Math.abs(r.nextInt()) % maxRandomX;
			
			// only plant mines on tiles that dont include a mine already
			if(field[y][x] == indicatorFree)
			{
				field[y][x] = indicatorMine;
			}					
			else
			{
				--i;
				continue;
			}
		}	
	}
	
	/* count the mines around the specified tile */
	private int countMines(int x, int y)
	{
		int origX = x;
		int origY = y;
		int mines = 0;
		int endX, endY;
		
		// start at the tile top left of the middle one
		x--;
		y--;
		
		// difference needs to be 3 so it covers the area around a tile			
		endX = x;
		endX += 3;
		
		endY = y;
		endY += 3;
		
		// count the mines around the middle tile (origX and origY)
		for(int i = x; i < endX; i++)
		{
			for(int j = y; j < endY; j++)
			{
				// skip the middle tile
				if(i == origX & j == origY)
				{
					continue;
				}					
				// if out of bound skip too
				if( (i < 0 || j < 0) || (i >= fieldWidth || j >= fieldHeight) )
				{
					continue;
				}					
				else
				{
					if(field[i][j] == indicatorMine)
					{
						mines++;
					}						
				}
			}
		}						
		
		return mines;
	}
	
	/* uncover the specified tile */
	private void uncoverTiles(int x, int y)
	{										
		// notice the massive amount of Copy and Paste Programming in this section
		int origX = x;
		int origY = y;
		int endX;
		int endY;
		int amount;
		
		// start at the tile top left of the middle one
		x--;
		y--;
		
		// difference needs to be 3 so it covers the area around a tile
		endX = x;
		endX += 3;
		
		endY = y;
		endY += 3;
		
		// mark the field display with the amount of mines surrounding the current tile
		amount = countMines(origX, origY);
		if(amount == 0)
		{
			fieldDisplay[origX][origY].setUncovered();
			fieldDisplay[origX][origY].setNextToMineStatus(false);
		}			
		else
		{
			// IMPORTANT: set the amount of mines AND uncover it
			fieldDisplay[origX][origY].setUncovered();
			fieldDisplay[origX][origY].setAmount(amount);
			fieldDisplay[origX][origY].setNextToMineStatus(true);
		}			
		
		// check surrounding tiles
		for(int i = x; i < endX; i++)
		{
			for(int j = y; j < endY; j++)
			{
				// skip the middle tile
				if(i == origX & j == origY)
				{
					continue;
				}					
				// if out of bound skip too
				if( (i < 0 || j < 0) || (i >= fieldWidth || j >= fieldHeight) )
				{
					continue;
				}					
				else
				{
					// call the same function on surrounding tiles that match the expression: (not a mine & not uncovered & and current tile must be empty)					
					if( (field[i][j] == indicatorFree & fieldDisplay[i][j].getCoveredStatus()) & (!fieldDisplay[origX][origY].getCoveredStatus() & !fieldDisplay[origX][origY].getNextToMineStatus()) )
					{
						uncoverTiles(i, j);
					}												
				}
			}
		}												
	}
	
	/* check the turn the user made */
	public void checkTurn(String coordinates, boolean leftclick)
	{
		String[] tmp = coordinates.split(" ");
		int x = Integer.parseInt(tmp[0]);
		int y = Integer.parseInt(tmp[1]);
				
		// if it's not a leftclick then the user wants to flag/unflag a tile
		if(!leftclick)
		{
			// only on covered tiles
			if(fieldDisplay[x][y].getCoveredStatus())
			{
				fieldDisplay[x][y].setFlagged();
				
				// if the flagged tile contains a mine increase the internal counter
				if(field[x][y] == indicatorMine & fieldDisplay[x][y].getFlagStatus())
				{
					this.minesCleared++;
				}
				
				// if all mines are flagged you win
				if(this.minesCleared == this.maxMines)
				{
					JOptionPane.showMessageDialog(null, "You Won.", "Minesweeper", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
			}			
		}
		else // leftclick handling
		{
			if(fieldDisplay[x][y].getCoveredStatus() & !fieldDisplay[x][y].getFlagStatus())
			{
				// if the current tile is a mine and has not been flagged it's game over
				if(field[x][y] == indicatorMine & !fieldDisplay[x][y].getFlagStatus())
				{
					//System.out.println("Game Over!");
					JOptionPane.showMessageDialog(null, "Game Over.", "Minesweeper", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
				// only use uncover on tiles that are not flagged
				if(!fieldDisplay[x][y].getFlagStatus())
				{
					uncoverTiles(x, y);
				}
			}							
		}
	}
	
	/* for debug only */
	public void showMines()
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				if(field[i][j] == indicatorMine)
				{
					//fieldDisplay[i][j].setGfxMine();
					fieldDisplay[i][j].getButton().setText("*");
					continue;
				}
				else
				{
					//fieldDisplay[i][j].setAmount(countMines(i, j));
				}
			}
		}
	}
}
