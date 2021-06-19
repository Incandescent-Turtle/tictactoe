package game.tictactoe.main.util;

import java.awt.Dimension;

//utility class
public class Util 
{
	private Util(){}
	
	//helper to create a sqaure Dimension
	public static Dimension newDim(int size)
	{
		return new Dimension(size, size);
	}
}
