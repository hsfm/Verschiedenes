#include <stdio.h>
#include <windows.h>
#define FIELD_X 9
#define FIELD_Y 9
#define MINE_AMOUNT 10
#define GFX_CLOSED '#' /* graphic symbols from here on out */
#define GFX_OPEN ' '
#define GFX_MINE '*'
#define GFX_FLAG '!'

struct tile
{
	char gfx;
	int isMine;
};

struct tile field[FIELD_X][FIELD_Y];

int main(int argc, char* argv[]);
void initGame();
void displayField();
void initRnd(); /* call only once or the seed gets screwed up */
int genRnd(int maxNumber);
int countMines(int x, int y);
void uncoverTiles(int x, int y);

int main(int argc, char* argv[])
{
	int x, y = -1; /* user input for x and y */
	int gamestate = -1; /* -1=running 0=over 1=victory */
	int action = -1; /* 1=flag 2=uncover */
			
	initGame();
	
	while(gamestate < 0) /* main game loop */
	{
		system("cls");
		
		displayField();		
		
		x = -1; /* these need to be reset every turn */
		y = -1;
		action = -1;
		
		while(x < 1 || x > FIELD_X)
		{
			printf("\nPlease enter the x-coordinate (1 - %d): ", FIELD_X);
			scanf("%d", &x);
		}				
		while(y < 1 || y > FIELD_Y)
		{
			printf("Please enter the y-coordinate (1 - %d): ", FIELD_Y);
			scanf("%d", &y);
		}
		while(action < 1 || action > 2)
		{
			printf("Please enter your desired action (1=Flag | 2=Uncover): ");
			scanf("%d", &action);
		}
		
		x--; /* decrease the user input to match array coordinates */
		y--;
		
		if(action == 1) /* flag */
		{
			if(field[x][y].gfx == GFX_FLAG)
			{
				field[x][y].gfx = GFX_CLOSED;
			}
			else
			{
				field[x][y].gfx = GFX_FLAG;
			}
		}
		else if(action == 2) /* uncover */
		{
			if(field[x][y].isMine == 1 && field[x][y].gfx != GFX_FLAG)
			{
				gamestate = 0;
				break;
			}
			else if(field[x][y].gfx != GFX_FLAG)
			{
				uncoverTiles(x, y);
			}
		}				
	}	
	
	switch(gamestate)
	{
		case 0:
			printf("\n\nGame Over!\n");
			break;
			
		case 1:
			printf("\n\nVictory!\n");
			break;
			
		default:
			printf("\n\nUnexpected case!\n");
			break;
	}
	
	return 0;	
}

void initGame()
{
	int i, j;
	int x, y;
	int minesPlanted = MINE_AMOUNT;
	
	initRnd();
	
	for(i = 0; i < FIELD_Y; i++) /* set the field to the default values */
	{
		for(j = 0; j < FIELD_X; j++)
		{
			field[j][i].gfx = GFX_CLOSED;
			field[j][i].isMine = 0;
		}
	}
	
	while(minesPlanted < MINE_AMOUNT) /* place mines randomly */
	{
		x = genRnd(FIELD_X);
		y = genRnd(FIELD_Y);
		if(field[x][y].isMine == 0)
		{
			field[x][y].isMine = 1;
			// field[x][y].gfx = '*';
			minesPlanted--;
		}
		else
		{
			continue;
		}
	}
}

void displayField()
{
	int i, j;
	
	printf("  "); /* display coordinates on the top */
	for(i = 0; i < FIELD_X; i++)
	{
		printf("%d", i + 1);
	}
	printf("\n  ");
	for(i = 0; i < FIELD_X; i++)
	{
		printf("-");
	}
	printf("\n");
	
	for(i = 0; i < FIELD_Y; i++)
	{
		printf("%d|", i + 1); /* display coordinates on the left side */
		for(j = 0; j < FIELD_X; j++)
		{			
			printf("%c", field[j][i].gfx);
		}
		printf("|%d", i + 1); /* display coordinates on the right side */
		printf("\n");
	}
	
	printf("  "); /* display coordinates at the bottom */
	for(i = 0; i < FIELD_X; i++)
	{
		printf("-");
	}
	printf("\n  ");
	for(i = 0; i < FIELD_X; i++) 
	{
		printf("%d", i + 1);
	}
	printf("\n");
}

void initRnd()
{	
	srand(GetTickCount());
}

int genRnd(int maxNumber) /* lowest possible is always 0 */
{		
	return rand() / 100 % maxNumber + 1;
}

int countMines(int x, int y)
{
	int origX = x;
	int origY = y;
	int mines = 0;
	int endX, endY;
	int i, j;
	
	x--; /* decrease them to start at the top left */
	y--;
	
	endX = x;
	endX += 3;
	endY = y;
	endY += 3;
	
	for(i = x; i < endX; i++)
	{
		for(j = y; j < endY; j++)
		{
			if(i == origX && j == origY) /* skip the middle tile */
			{
				continue;
			}					
			/* skip OOB */
			if( (i < 0 || j < 0) || (i >= FIELD_X || j >= FIELD_Y) )
			{
				continue;
			}					
			else
			{
				if(field[i][j].isMine == 1)
				{
					mines++;
				}						
			}
		}
	}
	
	return mines;
}

void uncoverTiles(int x, int y)
{
	int origX = x;
	int origY = y;
	int mines = 0;
	int endX, endY;
	int i, j;
	int amount = 0;
	
	x--; /* decrease them to start at the top left */
	y--;
	
	endX = x;
	endX += 3;
	endY = y;
	endY += 3;
	
	amount = countMines(origX, origY);
	if(amount == 0)
	{
		field[origX][origY].gfx = GFX_OPEN;
	}			
	else
	{
		field[origX][origY].gfx = '0' + amount;
	}			
	
	for(i = x; i < endX; i++) /* check surrounding tiles */
	{
		for(j = y; j < endY; j++)
		{
			if(i == origX && j == origY)
			{
				continue;
			}					
			if( (i < 0 || j < 0) || (i >= FIELD_X || j >= FIELD_Y) )
			{
				continue;
			}					
			else
			{
				/* not a mine & closed & and current tile empty */
				if(field[i][j].isMine == 0 && field[i][j].gfx == GFX_CLOSED && field[origX][origY].gfx == GFX_OPEN)
				{
					uncoverTiles(i, j);
				}												
			}
		}
	}		
}