/* 
 * represents a single tile in the field
 */

// for JButton
import javax.swing.*;
// for HashMap
import java.util.*;
// for Color
import java.awt.*;

public class MyButton 
{
	// holds the actual button
	private JButton button;
	// x/y representation of the field
	private Integer x;
	private Integer y;	
	private boolean isNextToMine;
	private boolean isFlagged;
	private boolean isCovered;
	private HashMap<String, ImageIcon> gfxHash;
	
	public MyButton()
	{
		this.isNextToMine = false;
		this.isFlagged = false;
		this.isCovered = true;
		
		initHash();
	}
	
	/* create a new button and add the respective coordinates */
	public void createNewButton(int x, int y)
	{
		this.x = x; // Autoboxing!
		this.y = y;
		button = new JButton();
		button.setPreferredSize(new Dimension(20, 20));
		// for leftclicks
		button.setActionCommand(this.x.toString() + " " + this.y.toString());
		// for rightclicks
		button.setName(this.x.toString() + " " + this.y.toString());
	}
	
	/* initializes the HashMap containing the numbers/flag icons */
	private void initHash()
	{
		gfxHash = new HashMap<String, ImageIcon>();
		gfxHash.put("gfx1", loadImageIcon("gfx/gfx1.gif"));
		gfxHash.put("gfx2", loadImageIcon("gfx/gfx2.gif"));
		gfxHash.put("gfx3", loadImageIcon("gfx/gfx3.gif"));
		gfxHash.put("gfx4", loadImageIcon("gfx/gfx4.gif"));
		gfxHash.put("gfx5", loadImageIcon("gfx/gfx5.gif"));
		gfxHash.put("gfx6", loadImageIcon("gfx/gfx6.gif"));
		gfxHash.put("gfx7", loadImageIcon("gfx/gfx7.gif"));
		gfxHash.put("gfx8", loadImageIcon("gfx/gfx8.gif"));
		gfxHash.put("gfxFlag", loadImageIcon("gfx/gfxFlag.gif"));
	}
	
	/* the actual process of loading an image */
	private ImageIcon loadImageIcon(String path)
	{
		java.net.URL imgURL = getClass().getResource(path);
		if(imgURL != null)
		{
			return new ImageIcon(imgURL);
		}
		else
		{
			return null;
		}
	}
	
	/* sets a button's status to uncovered including gfx and statusflags */
	public void setUncovered()
	{
		this.button.setBackground(Color.LIGHT_GRAY);
		//this.button.setEnabled(false);		
		setCoveredStatus(false);
	}
	
	/* set the specified icon for the button */
	private void setGfx(String gfxConst)
	{
		this.button.setIcon(gfxHash.get(gfxConst));
	}
	
	/* flag/unflag a button depending on the current state */
	public void setFlagged()
	{
		if(!this.isFlagged)
		{
			//setFlagStatus(true);
			this.isFlagged = true;
			setGfx("gfxFlag");
		}
		else
		{
			//setFlagStatus(false);
			this.isFlagged = false;
			this.button.setIcon(null);
		}
	}	
	
	/* sets an icon representing the amount of mines surrounding this tile */
	public void setAmount(Integer amount)
	{
		//this.button.setText(amount.toString());
		switch(amount) // auto unboxing!
		{
			case 1:
				setGfx("gfx1");
			break;
			
			case 2:
				setGfx("gfx2");
			break;
			
			case 3:
				setGfx("gfx3");
			break;
			
			case 4:
				setGfx("gfx4");
			break;
			
			case 5:
				setGfx("gfx5");
			break;
			
			case 6:
				setGfx("gfx6");
			break;
			
			case 7:
				setGfx("gfx7");
			break;
			
			case 8:
				setGfx("gfx8");
			break;
		}
	}

	public boolean getNextToMineStatus()
	{
		return this.isNextToMine;
	}
	
	public boolean getFlagStatus()
	{
		return this.isFlagged;
	}
	
	public boolean getCoveredStatus()
	{
		return this.isCovered;
	}
	
	public JButton getButton()
	{
		return this.button;
	}
	
	public void setNextToMineStatus(boolean value)
	{
		this.isNextToMine = value;
	}
	
	public void setFlagStatus(boolean value)
	{
		this.isFlagged = value;
	}
	
	public void setCoveredStatus(boolean value)
	{
		this.isCovered = value;
	}	
}
