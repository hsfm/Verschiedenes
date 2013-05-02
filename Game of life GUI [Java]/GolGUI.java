/*
 * Implements Conway's Game of life
 */


import java.awt.*;

import javax.swing.*;

public class GolGUI 
{
	private final String title = "Game of life";
	private int field_x;
	private int field_y;
	private JFrame frame;
	private JPanel field;
	private Gol gol;
	private int sleep;
	
	/* constructor with parameters */
	public GolGUI(int x, int y, int sleep) throws InterruptedException
	{		
		this.sleep = sleep;
		this.field_x = x;
		this.field_y = y;
		gol = new Gol(this.field_x, this.field_y);
		createWindow();
		game();
	}
	
	/* create top level window */
	public void createWindow()
	{
		frame = new JFrame(title);		
		frame.setResizable(false);									
		
		createField();
		addButtons();
		
		frame.pack();
		frame.repaint();
		frame.setVisible(true);
		
		// center
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/* create the JPanel used for the field */
	public void createField()
	{
		field = new JPanel();
		GridLayout grid = new GridLayout(0, field_x);
		field.setLayout(grid);
		frame.add(field, BorderLayout.CENTER);
	}
	
	/* create the buttons for the field */
	public void addButtons()
	{		
		MyButton mb = null;
				
		for(int i = 0; i < this.field_x; i++)
		{
			for(int j = 0; j < this.field_y; j++)
			{
				mb = new MyButton();
				mb.createNewButton(i, j);
				gol.addButton(i, j, mb);
				field.add(mb.getButton());
			}
		}
	}
	
	/* main game loop */
	private void game() throws InterruptedException
	{
		for(int generation = 0; generation == generation; generation++)		
		{
			gol.countNeighbors();
			Thread.sleep(this.sleep);
		}
	}
	
	public static void main(String[] args) throws InterruptedException 
	{
		new GolGUI(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
	}
}
