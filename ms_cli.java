/* *
20:10 20.10.2010

todo:
lock flagged tiles (also exclude them from uncoverTile())

add different modes (easy intermediate pro) 

add scores maybe with encryption to prevent script kiddies from fiddling with it

add a decent function to check for victory

obviously add a timer and a mine counter

employ oop techniques correctly

fix random bugs

port this whole mess to a decent GUI
**/

import java.util.*;

public class test
{
	public static class Minesweeper
	{
		// member variables			
		
		private int fieldWidth;
		
		private int fieldHeight;
		
		private int maxMines;
		
		// for debug purposes and to show mines after failure
		private boolean showMines;
		
		// coordinates that the user enters
		private int userY, userX;
		
		// 1 to flag a tile or 2 to reveal the tile
		private int userAction;
		
		private final int userActionFlag = 1;
		
		private final int userActionUncover = 2;
		
		// amount of mines already cleared
		private int minesCleared;
		
		// graphic representation of the respective items 
		private final String gfxField = "#";
		
		private final String gfxFree = ".";
		
		private final String gfxFlag = "!";
		
		private final String gfxMine = "+";
		
		// constants for the actual field
		private final int indicatorMine = 9;
		
		private final int indicatorFree = 0;
		
		// the graphic display of the field
		public String[][] fieldDisplay; 
		
		// used for the actual game calculations
		public int[][] field; 
				
		
		// methods
		
		/* constructor takes care of initialization */		
		public Minesweeper(int fWidth, int fHeight, int mines)
		{
			// initialize every member variable
			this.fieldWidth = fWidth;
			this.fieldHeight = fHeight;
			this.maxMines = mines;			
			this.fieldDisplay = new String[this.fieldHeight][this.fieldWidth];
			this.field = new int[this.fieldHeight][this.fieldWidth];
			this.userX = 0;
			this.userY = 0;
			this.userAction = 0;
			this.minesCleared = 0;
		}				
		
		/* make the mines visible (only for debug purposes) */
		public void displayMines(boolean value)
		{
			showMines = value;
		}
		
		/* initialize both fields */
		public void initField()
		{			
			for(int i = 0; i < fieldHeight; i++) 
			{			
				for(int j = 0; j < fieldWidth; j++)
				{					
					field[i][j] = indicatorFree;						
					fieldDisplay[i][j] = gfxField;
				}
			}
		}

		/* plant the mines randomly on the field */
		public void setMines()
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
				
		/* print the field and the sidebars */
		public void displayField()
		{
			int tmp = 0;
			char c = 'A';
			
			System.out.println("\nLegend: " + gfxField + "=Field, " + gfxFree + "=Free, " + gfxFlag + "=Flag, " + gfxMine + "=Mine");
			
			// print the top row (not part of the field) 
			System.out.print("\n\t");
			for(int top = 0; top < fieldWidth; top++)
			{
				System.out.print(c++);
			}				
			System.out.println("\n");
			
			// print the actual field and the side bars
			for(int i = 0; i < fieldHeight; i++)
			{			
				tmp = i;
				// print the left sidebar
				System.out.print(++tmp + "\t");
				// print the field
				for(int j = 0; j < fieldWidth; j++)
				{
					// note that if this is enabled it will overwrite anything else on the mine tiles even flags
					if(showMines == true)
					{
						if(field[i][j] == indicatorMine)
							System.out.print(gfxMine);
						else
							System.out.print(fieldDisplay[i][j]);
					}
					else					
						System.out.print(fieldDisplay[i][j]);
						
				}
				// print the right sidebar
				System.out.print("\t" + tmp);
				System.out.println();
			}						
			
			// print the bottom row (not part of the field)
			c = 'A';
			System.out.print("\n\t");
			for(int bottom = 0; bottom < fieldWidth; bottom++)
			{
				System.out.print(c++);
			}				
			System.out.println("\n");
		}
		
		/* get the user input */
		public void getInput()
		{
			Scanner scanner = new Scanner(System.in);			
			
			// loop the "get input" functions until we get a valid argument
			for(;;)
			{
				System.out.print("please enter x [1-" + fieldWidth +"]: ");
				userX = scanner.nextInt();
				if(userX > 0 & userX <= fieldWidth)
					break;
			}			
			
			for(;;)
			{
				System.out.print("please enter y [1-" + fieldHeight +"]: ");
				userY = scanner.nextInt();
				if(userY > 0 & userY <= fieldHeight)
					break;
			}									

			for(;;)
			{
				System.out.print("please choose your action [" + userActionFlag + "=Flag, " + userActionUncover + "=Reveal]: ");
				userAction = scanner.nextInt();
				if(userAction == userActionFlag || userAction == userActionUncover)
					break;
			}
		}
		
		/* react to user input */
		public boolean checkTurn()
		{
			// decrease user input by one to match array coordinates!
			--userX;
			--userY;
			
			// check if tile contains a mine and if the user action was "uncover"
			if( (field[userY][userX] == indicatorMine) & (userAction == userActionUncover) )
			{
				showMines = true;
				displayField();
				System.out.println("\nGAME OVER!");
				return false;
			}									
			
			// if not a mine continue with the game (duh)
			if(userAction == userActionFlag)
				flagTile(userX, userY);
			else if(userAction == userActionUncover & fieldDisplay[userY][userX].equals(gfxField))
				uncoverTiles(userX, userY);
			
			// normally we also need to make sure all tiles are uncovered in order to win but oh well
			if(minesCleared == maxMines)
			{
				System.out.println("\nA WINNER IS YOU!");
				return false;
			}						
				
			return true;	
		}
		
		/* flag a given position */
		public void flagTile(int x, int y)
		{
			// only flag valid fields
			if(fieldDisplay[y][x].equals(gfxField))
				fieldDisplay[y][x] = gfxFlag;
			
			// keep track of how many mines we have cleared
			if( (field[y][x] == indicatorMine) & (fieldDisplay[y][x].equals(gfxFlag)) )
				minesCleared++;
		}
		
		/* count the mines around the specified tile */
		public int countMines(int x, int y)
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
						continue;
					// if out of bound skip too
					if( (i < 0 || j < 0) || (i >= fieldWidth || j >= fieldHeight) )
						continue;
					else
					{
						if(field[j][i] == indicatorMine)
							mines++;
					}
				}
			}						
			
			return mines;
		}
		
		/* uncover the specified tile */
		public void uncoverTiles(int x, int y)
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
				fieldDisplay[origY][origX] = gfxFree;
			else
				fieldDisplay[origY][origX] = Integer.toString(amount);
			
			// check surrounding tiles
			for(int i = x; i < endX; i++)
			{
				for(int j = y; j < endY; j++)
				{
					// skip the middle tile
					if(i == origX & j == origY)
						continue;
					// if out of bound skip too
					if( (i < 0 || j < 0) || (i >= fieldWidth || j >= fieldHeight) )
						continue;
					else
					{
						// call the same function on surrounding tiles that match the expression (not a mine & not uncovered & and current tile must be empty)
						if( (field[j][i] == indicatorFree & fieldDisplay[j][i].equals(gfxField)) & (fieldDisplay[origY][origX].equals(gfxFree)) )
							uncoverTiles(i, j);						
					}
				}
			}												
		}	
	}
	
	public static void main(String[] args)
	{				
		int x = 16;
		int y = 16;
		int minen = 40;
		
		// if additional arguments are supplied use those instead of the default values
		if(args.length >= 4)
		{
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			minen = Integer.parseInt(args[3]);
		}				
		
		Minesweeper mines = new Minesweeper(x, y, minen);

		// check if debug mode is enabled
		if(args.length != 0 && args[0].equals("god"))		
			mines.displayMines(true);						
		
		mines.initField();
		mines.setMines();
		
		boolean isOkay = true;
		
		// loop until we either win or lose
		while(isOkay == true)
		{
			mines.displayField();
			mines.getInput();
			isOkay = mines.checkTurn();
		}		
	}
}