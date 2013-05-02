// graphical representation of the field, user IO is done here

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MinesweeperGUI
{
	private final String title = "Minesweeper";	
	private final int windowsizeX = 250;
	private final int windowsizeY = 300;
	private final int fieldX = 10;
	private final int fieldY = 10;
	private final int mines = 15;
	private JFrame frame;	
	private JPanel field;
	private JPanel status;
	private Game game;
	
	public MinesweeperGUI() 
	{
		game = new Game(fieldX, fieldY, mines);				
		createWindow();	
		game.initField();
		game.createMines();	
		//game.debug();
	}
	
	/* create top level window */
	public void createWindow()
	{
		frame = new JFrame(title);
		//frame.pack();
		frame.setSize(windowsizeX, windowsizeY);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
		
		// call methods for component creation
		createMenu();
		createField();
		addButtons();
		createStatusbar();
		
		// repaint just to make sure
		frame.repaint();
		frame.setVisible(true);
	}
	
	/* create the menubar */
	public void createMenu()
	{
		JMenuBar menu = new JMenuBar();
		
		// add the menu to the top level window
		frame.setJMenuBar(menu);
		
		// top menu: game
		JMenu game = new JMenu("Game");
		game.setMnemonic(KeyEvent.VK_G);
		menu.add(game);
		
		// game menuitem: newgame
		JMenuItem newgame = new JMenuItem("New Game");
		newgame.setMnemonic(KeyEvent.VK_N);
		newgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		game.add(newgame);
	}
	
	/* create the JPanel used for the field */
	public void createField()
	{
		field = new JPanel();
		GridLayout grid = new GridLayout(0, 10);
		field.setLayout(grid);
		frame.add(field, BorderLayout.CENTER);
	}
	
	/* create the buttons for the field */
	public void addButtons()
	{		
		MyButton mb = null;
				
		for(int i = 0; i < fieldX; i++)
		{
			for(int j = 0; j < fieldY; j++)
			{
				mb = new MyButton();
				mb.createNewButton(i, j);
				game.addButton(i, j, mb);
				
				// add a listener for leftclicks
				mb.getButton().addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						game.checkTurn(e.getActionCommand(), true);
					}
				});
				
				// add a listener for rightclicks
				mb.getButton().addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						game.checkTurn(e.getComponent().getName(), false);
					}
				});
				field.add(mb.getButton());
			}
		}
	}
	
	/* create the JPanel used as the statusbar */
	public void createStatusbar()
	{
		status = new JPanel();
		
		// time
		JLabel time = new JLabel("Time:");
		status.add(time, BorderLayout.LINE_START);				
		
		final JTextField timeDisplay = new JTextField("00");
		status.add(timeDisplay, BorderLayout.CENTER);
		
		Timer t = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// autoboxing/unboxing
				Integer tmp = Integer.parseInt(timeDisplay.getText());
				tmp++;
				timeDisplay.setText(tmp.toString());
				//timeDisplay.repaint();
			}
		});
		t.start();
		
		// mines
		JLabel mines = new JLabel("Mines:");
		status.add(mines, BorderLayout.LINE_END);
		
		JTextField minesDisplay = new JTextField("99");
		status.add(minesDisplay, BorderLayout.LINE_END);
		
		frame.add(status, BorderLayout.PAGE_END);
	}	
	
	/* create a new instance of the GUI */
	public static void main(String args[])
	{
		new MinesweeperGUI();
	}
}
