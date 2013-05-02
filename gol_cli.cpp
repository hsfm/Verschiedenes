#include <iostream>
#include <windows.h>
#include <string>

using namespace std;

int main()
{
	const int fy = 20;
	const int fx = 60;
	const string cell_live = "#";
	const string cell_dead = " ";
	string grid[fy][fx];
	int neighbours[fy][fx];
	
	// init random
	float seed = GetTickCount();
	srand(seed);
	
	// init grid and neighbours
	for(int i = 0; i < fy; i++)
	{
		for(int j = 0; j < fx; j++)
		{
			rand() / 100 % 2 == 1 ? grid[i][j] = cell_live : grid[i][j] = cell_dead;
			neighbours[i][j] = 0;
		}
	}
	
	// main loop
	for(int generation = 0; generation == generation; generation++)
	{
		system("cls");
		cout << generation << endl;
		
		// print grid
		for(i = 0; i < fy; i++)
		{
			for(int j = 0; j < fx; j++)
			{
				cout << grid[i][j];
			}
			cout << endl;
		}
		
		// count neighbours
		for(i = 0; i < fy; i++)
		{
			for(int j = 0; j < fx; j++)
			{
				int y = i;
				int x = j;
				int c = 0;
				x--;
				y--;
				int endX = x;
				endX += 3;
				int endY = y;
				endY += 3;
				for(int outer = y; outer < endY; outer++)
				{
					for(int inner = x; inner < endX; inner++)
					{
						if(outer < 0 || inner < 0 || outer >= fy || inner >= fx)
							continue;
						if(outer == i && inner == j)
							continue;						
						if(grid[outer][inner] == cell_live)
						{
							c++;
						}
					}
				}
				neighbours[i][j] = c;
			}
		}
		
		// apply
		for(i = 0; i < fy; i++)
		{
			for(int j = 0; j < fx; j++)
			{
				// Any live cell with fewer than two live neighbours dies, as if caused by under-population.
				if(grid[i][j] == cell_live && neighbours[i][j] < 2)
					grid[i][j] = cell_dead;
				// Any live cell with two or three live neighbours lives on to the next generation.
				if(grid[i][j] == cell_live && (neighbours[i][j] == 2 || neighbours[i][j] == 3))
					grid[i][j] = cell_live;
				// Any live cell with more than three live neighbours dies, as if by overcrowding.
				if(grid[i][j] == cell_live && neighbours[i][j] > 3)
					grid[i][j] = cell_dead;
				// Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
				if(grid[i][j] == cell_dead && neighbours[i][j] == 3)
					grid[i][j] = cell_live;
			}
		}
				
		Sleep(500);
	}	
	return 0;
}