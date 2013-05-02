public class Gol
{
	private int field_x;
	private int field_y;
	private Cell[][] grid;
	private MyButton[][] fieldDisplay;
			
	public Gol(int field_x, int field_y)
	{
		this.field_x = field_x;
		this.field_y = field_y;
		this.fieldDisplay = new MyButton[this.field_x][this.field_y];
		createGrid();
	}
	
	/* adds a button to the fieldDisplay array */
	public void addButton(int x, int y, MyButton button)
	{
		fieldDisplay[x][y] = button;
		if(!grid[x][y].getIs_alive())
		{
			fieldDisplay[x][y].setAlive(false);
		}
		else
		{
			fieldDisplay[x][y].setAlive(true);
		}
	}
	
	/* create grid */
	public void createGrid()
	{
		grid = new Cell[this.field_x][this.field_y];
		for(int i = 0; i < this.field_x; i++)
		{
			for(int j = 0; j < this.field_y; j++)
			{
				grid[i][j] = new Cell();
			}
		}
	}
	
	/* count every cells neighbors and apply changes accordingly */
	public void countNeighbors()
	{				
		// first two loops go through all the cells
		for(int i = 0; i < this.field_x; i++)
		{
			for(int j = 0; j < this.field_y; j++)
			{				
				// now loop 'around' each cell to count the neighbors
				int n = 0;
				int cell_start_x = i;
				int cell_start_y = j;
				cell_start_x--;
				cell_start_y--;
				int cell_end_x = cell_start_x;
				int cell_end_y = cell_start_y;
				cell_end_x += 3;
				cell_end_y += 3;
				for(int k = cell_start_x; k < cell_end_x; k++)
				{
					for(int l = cell_start_y; l < cell_end_y; l++)
					{
						// skip if out of bounds
						if(k < 0 || l < 0 || k >= this.field_x || l >= this.field_y)
						{
							continue;
						}
						// skip if we reach the tile we are looping around
						if(k == i && l == j)
						{
							continue;
						}
						if(grid[k][l].getIs_alive() == true)
						{
							n++;
						}
					}
				}
				grid[i][j].setNeighbours(n);
			}
		}
		applyChanges();
	}
	
	/* apply changes according to the rules of conway */
	private void applyChanges()
	{
		for(int i = 0; i < this.field_x; i++)
		{
			for(int j = 0; j < this.field_y; j++)
			{
				// Any live cell with fewer than two live neighbours dies, as if caused by under-population.
				if(grid[i][j].getIs_alive() && grid[i][j].getNeighbours() < 2)
				{
					grid[i][j].setIs_alive(false);
					fieldDisplay[i][j].setAlive(false);
				}				
				// Any live cell with two or three live neighbours lives on to the next generation.
				if(grid[i][j].getIs_alive() && grid[i][j].getNeighbours() == 2 || grid[i][j].getNeighbours() == 3)
				{
					grid[i][j].setIs_alive(true);
					fieldDisplay[i][j].setAlive(true);
				}
				// Any live cell with more than three live neighbours dies, as if by overcrowding.
				if(grid[i][j].getIs_alive() && grid[i][j].getNeighbours() >= 3)
				{
					grid[i][j].setIs_alive(false);
					fieldDisplay[i][j].setAlive(false);
				}
				// Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
				if(!grid[i][j].getIs_alive() && grid[i][j].getNeighbours() == 3)
				{
					grid[i][j].setIs_alive(true);
					fieldDisplay[i][j].setAlive(true);
				}
			}
		}
	}
}