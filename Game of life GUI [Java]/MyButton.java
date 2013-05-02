/* 
 * represents a single tile in the field
 */

// for JButton
import javax.swing.*;
// for Color
import java.awt.*;

public class MyButton 
{
	// holds the actual button
	private JButton button;
	// x/y representation of the field
	private Integer x;
	private Integer y;	
	
	public MyButton()
	{				
	}
	
	/* create a new button and add the respective coordinates */
	public void createNewButton(int x, int y)
	{
		this.x = x; // Autoboxing!
		this.y = y;
		button = new JButton();
		button.setPreferredSize(new Dimension(10, 10));
		// for leftclicks
		button.setActionCommand(this.x.toString() + " " + this.y.toString());
		// for rightclicks
		button.setName(this.x.toString() + " " + this.y.toString());
		//button.setBorderPainted(false);
	}			
	
	public JButton getButton()
	{
		return this.button;
	}
	
	public void setAlive(boolean alive)
	{
		if(!alive)
		{
			this.button.setBackground(Color.white);
		}
		else
		{
			this.button.setBackground(Color.black);
		}
	}
}
