import java.util.*;

public class Cell 
{
	private boolean is_alive;
	private int neighbours;
	
	public Cell()
	{			
		if(Math.abs(new Random().nextInt()) % 2 == 1)
		{
			is_alive = true;
		}
		else
		{
			is_alive = false;
		}
	}

	public boolean getIs_alive() 
	{
		return is_alive;
	}

	public void setIs_alive(boolean is_alive) 
	{
		this.is_alive = is_alive;
	}

	public int getNeighbours() 
	{
		return neighbours;
	}

	public void setNeighbours(int neighbours) 
	{
		this.neighbours = neighbours;
	}	
}
