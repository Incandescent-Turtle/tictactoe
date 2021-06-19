package game.tictactoe.main.gfx;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import game.tictactoe.main.board.GameBoard;

//class to statically reference the icons
public class Icons 
{

	public static Icon RED_X, GREEN_X, RED_O, GREEN_O;
	public static Icons loader;
	
	//when class first accsessed 
	static 
	{
		loader = new Icons();
	}
	
	private Icons() 
	{
		loadIcons();
	}
	
	//used to load the icons from the pngs in the jar. Sizes them according to the board size
	private Icon loadIcon(String name)
	{
		int size = GameBoard.boardSize/GameBoard.SIZE - 30;
		try {
			return new ImageIcon(ImageIO.read(getClass().getResource("/" + name + ".png")).getScaledInstance(size, size, 0));
		} catch (Exception e) {
			System.out.println("Error occured while loading " + "res/" + name + ".png" + " Ensure that the png file is named correctly and is placed within the correct folder.");
		}
		return null;
	}
	
	//reloads the icons. used to change size
	public void loadIcons()
	{
		RED_X = loadIcon("redX");
		GREEN_X = loadIcon("greenX");
		RED_O = loadIcon("redO");
		GREEN_O = loadIcon("greenO");
	}
}
